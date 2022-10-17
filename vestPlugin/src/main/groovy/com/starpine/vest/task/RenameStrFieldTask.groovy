package com.starpine.vest.task

import com.starpine.vest.bean.StringsInfo
import com.starpine.vest.bean.VestInfo
import com.starpine.vest.utils.GuardWordUtils
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.TaskAction

/**
 * 描述：多语言字段批量修改
 *
 * @Name ：VestPlugin* @Description ：
 * @Author： liaosf
 * @Date ：2022/10/13 10:57
 */
class RenameStrFieldTask extends DefaultTask {

    Project project
    VestInfo vestInfo
    def renameMapping
    def guardFilePath
    def mappingString = ""

    RenameStrFieldTask() {
        group = "vest guard"
    }

    void init(VestInfo vestInfo, Project project) {
        this.vestInfo = vestInfo
        this.project = project
    }

    @TaskAction
    def start() {
        GuardWordUtils guardWordUtils = new GuardWordUtils()
        if (vestInfo.obfuscationdictionary == null) {
            def finallyGuard = guardWordUtils.generateGuardWord(vestInfo, project)
            guardFilePath = finallyGuard
        } else {
            guardFilePath = project.projectDir.path + File.separator + vestInfo.obfuscationdictionary
        }
        def renameMappingDir = project.getProjectDir().path + File.separator + "vest_mapping/"
        renameMapping = renameMappingDir + "strings-name-mapping.txt"
        File mappingDir = new File(renameMappingDir)
        if (!mappingDir.exists()) {
            mappingDir.mkdir()
        }

        FileTree sourceFileTree = project.fileTree("../") {
            include '**/values/strings.xml'
            exclude '**/build'
            exclude '**/androidTest'
            exclude '**/test'
        }

        sourceFileTree.each {
            println(it.path)
            replaceStrField(it.path, guardWordUtils)
        }

        project.file(renameMapping).withWriter('UTF-8') {
            writer ->
                writer.append(mappingString)
        }
    }

    def replaceStrField(sourceFilePath, guardWordUtils) {
        //基准文件
        File sourceFile = new File(sourceFilePath)
        List<String> sourceWordList = sourceFile.readLines()
        def srcModuleName = sourceFilePath.replaceAll(".*$project.rootDir.name\\\\", "")
                .replaceAll("\\\\src.*", "")


        //混淆文件
        File guardFile = new File(guardFilePath)
        if (!guardFile.exists()) {
            guardWordUtils.generateGuardWord(vestInfo, project, vestInfo.obfuscationdictionary)
        }
        List<String> renameStrWord = guardFile.readLines()

        List<StringsInfo> stringsFileList = new ArrayList<>()

        FileTree stringsTree = project.fileTree("../") {
            include '**/strings.xml'
        }
        stringsTree.each {
            String path = it.getPath()
            File renameStringsFile = new File(path)
            byte[] contents = renameStringsFile.bytes
            String content = new String(contents)

            def values = path.substring(path.indexOf("values"), path.length()).split("\\\\")[0]
            def moduleName = path.replaceAll(".*$project.rootDir.name\\\\", "").replaceAll("\\\\src.*", "")
            stringsFileList.add(new StringsInfo(values, path, content, moduleName))

        }

        int index = 0
        sourceWordList.each { String lines ->
            if (lines.find("<string name=")) {

                String strSrcName = lines.replaceAll(".*name=\"", "")
                        .replaceAll("\">.*", "")
                String targetName = renameStrWord.get(index)
                index++
                println "$index--------$strSrcName"
                mappingString <<= "$strSrcName -> $targetName"
                mappingString << "\n"
                //替换语言包字段
                for (i in 0..<stringsFileList.size()) {
                    def stringsInfo = stringsFileList.get(i)
                    stringsInfo.content = stringsInfo.content.replaceAll("(?<=\")$strSrcName(?=\".*)", targetName)
                    stringsFileList.set(i, stringsInfo)
                }

                //替换引用了语言包的文件
                FileTree targeTree = project.fileTree("../") {
                    include '**/*.java'
                    include '**/layout/*.xml'
                    include '**/styles.xml'
                    include '**/AndroidManifest.xml'
                    exclude '**/androidTest/**'
                    exclude '**/gradle-build/**'
                }
                targeTree.each { File f ->
                    strSrcReplacer(f.path, strSrcName, targetName)
                }
            }
        }

        //重新替换语言包文件内容
        for (i in 0..<stringsFileList.size()) {
            def stringsInfo = stringsFileList.get(i)
            project.file(stringsInfo.path).withWriter('UTF-8') {
                writer ->
                    writer.append(stringsInfo.content)
            }
        }
    }

    def strSrcReplacer(String filePath, sourceName, targetName) {
        def isXml = filePath.endsWith(".xml")
        def hasReplace = false
        String targetContent = ""
        File renameStringsFile = new File(filePath)
        byte[] contents = renameStringsFile.bytes
        String content = new String(contents)
        String str = content
        if (content.find("$sourceName")) {
            if (isXml) {
                content = content.replaceAll("(?<=@string/)$sourceName(?![0-9a-zA-Z_]+)", targetName)
            } else {
                content = content.replaceAll("(?<!android\\.)R\\.string\\.$sourceName(?![0-9a-zA-Z_]+)", "R\\.string\\.$targetName")
            }
            targetContent <<= content
            targetContent <<= '\n'
            if (!content.equals(str)) {
                hasReplace = true
            }
        }


        if (hasReplace) {
            println filePath
            if (targetContent.toString().endsWith("\n")) {
                targetContent = targetContent.subSequence(0, targetContent.length() - 1)
            }
            project.file(filePath).withWriter('UTF-8') {
                writer ->
                    writer.append(targetContent)
            }
        }
    }

}
