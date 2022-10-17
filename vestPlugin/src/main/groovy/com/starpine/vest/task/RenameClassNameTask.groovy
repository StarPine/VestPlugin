package com.starpine.vest.task

import com.starpine.vest.bean.VestInfo
import com.starpine.vest.utils.GuardWordUtils
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.TaskAction

/**
 * 描述：类名批量修改
 *
 * @Name ：VestPlugin
 * @Description ：
 * @Author ：liaosf* @Date ：2022/10/12 16:47
 */
class RenameClassNameTask extends DefaultTask {
    def needRenamePath
    def guardFilePath
    def renameMapping
    Project project
    VestInfo vestInfo

    RenameClassNameTask() {
        group = "vest guard"
    }

    void init(VestInfo vestInfo, Project project) {
        needRenamePath = vestInfo.sourceClassPath
        this.project = project
        this.vestInfo = vestInfo
    }

    @TaskAction
    void replaceClassName() {

        GuardWordUtils guardWordUtils = new GuardWordUtils()
        if (vestInfo.classobfuscationdictionary == null) {
            def finallyGuard = guardWordUtils.generateGuardWord(vestInfo, project)
            guardFilePath = finallyGuard
        } else {
            guardFilePath = project.projectDir.path + File.separator + vestInfo.classobfuscationdictionary
        }
        def renameMappingDir = project.getProjectDir().path + File.separator + "vest_mapping/"
        renameMapping = renameMappingDir + "class-name-mapping.txt"
        File mappingDir = new File(renameMappingDir)
        if (!mappingDir.exists()) {
            mappingDir.mkdir()
        }

        def mappingString = ""
        File guardFile = new File(guardFilePath)
        if (!guardFile.exists()) {
            guardWordUtils.generateGuardWord(vestInfo, project, vestInfo.classobfuscationdictionary)
        }
        List<String> renameWord = guardFile.readLines()
        int index = 0
        FileTree sourceTree = project.fileTree(needRenamePath) {
            include '**/*.java'
            include '**/*.kt'
            exclude '**/RoundedImageView.java'
            exclude '**/androidTest/**'
            exclude '**/test/**'
        }
        sourceTree.each { File sourceFile ->
            String sourceName = sourceFile.name
            println sourceName
            String fileSuffix = sourceName.substring(sourceName.lastIndexOf("."))
            sourceName = sourceName.replace(fileSuffix, "")
            String targetName = ""
            println renameWord.get(index)
            targetName = renameWord.get(index)
            index++

            String packageName = ""
            project.file(sourceFile).withReader('UTF-8') { reader ->
                reader.eachLine { String content ->
                    if (content.find("package ") && content.indexOf("package ") == 0) {
                        packageName = content
                    }
                }
            }
            mappingString <<= packageName.replaceAll("package ", "").replaceAll(";", ".") + sourceName + " -> $targetName"
            mappingString << "\n"
            sourceFile.renameTo(sourceFile.parentFile.path + File.separator + targetName + fileSuffix)

            //生成一个文件树,替换import后面的路径
            FileTree processTree = project.fileTree(needRenamePath) {
                include '**/*.java'
                include '**/*.kt'
                include '**/layout/*.xml'
                include '**/values/*.xml'
                include '**/AndroidManifest.xml'
                exclude '**/androidTest/**'
                exclude '**/test/**'
            }
            processTree.each { File f ->
                importSoureReplacer(f.path, sourceName, targetName, packageName)
            }
        }

        project.file(renameMapping).withWriter('UTF-8') {
            writer ->
                writer.append(mappingString)
        }
    }

    //替换有引用目标类的文件内容
    def importSoureReplacer(String filePath, sourceName, targetName, packageName) {
        def packageStr = packageName.replace("package ", "").replace(";", "")
        def readerString = ""
        def pgRegex = "(?<=($packageStr.)| |(R.styleable.)|(R.style.))$sourceName(?!( *=)|[a-zA-Z0-9]+)"
        def otherRegex = "(?<![a-zA-Z0-9]{1,10})(?<=[\\.]{0,10})$sourceName(?=[\\.]*)(?![a-zA-Z0-9]+)"
        //是否有字段替换
        def hasReplace = false
        //是否为依赖关系
        def isRely = false
        //是否在同一个包名下
        def isSamePackage = false
        def isXml = filePath.endsWith(".xml")
        def isManifest = filePath.endsWith("AndroidManifest.xml")
        project.file(filePath).withReader('UTF-8') { reader ->
            reader.eachLine { String lines ->

                if (lines.find("package ")) {
                    if (lines.equals(packageName)) {
                        isSamePackage = true
                    }
                }

                if (lines.find("import $packageStr\\.\\*;")) {
                    isRely = true
                }

                if (lines.find(sourceName)) {
                    if (lines.find("\\.$sourceName")) {

                        if (isManifest) {
                            String manifestPackage = ""
                            if (lines.find("package=\"")) {
                                manifestPackage = lines.replaceAll("package=\"").replaceAll("\">")
                            }
                            if (lines.find("android:name=\"")) {
                                if (lines.matches(".*android:name=\".*")) {
                                    lines = lines.replaceAll(sourceName, targetName)
                                }
                            }
                            if (lines.find("android:authorities=\"")) {
                                lines = lines.replaceAll(sourceName, targetName)
                            }
                        } else {
                            String str = lines
                            lines = lines.replaceAll(pgRegex, targetName)
                            if (lines.find("import ")) {
                                if (!lines.equals(str)) {
                                    isRely = true
                                } else {
                                    if (lines.endsWith(".$sourceName;")) {
                                        isSamePackage = false
                                    }
                                }
                            }
                        }

                    } else {
                        if (isRely || isSamePackage || isXml) {
                            lines = lines.replaceAll(otherRegex, targetName)
                        }
                    }
                    hasReplace = true
                }

                readerString <<= lines
                readerString << '\n'

            }
        }
        if (hasReplace) {
            if (readerString.toString().endsWith("\n")) {
                readerString = readerString.subSequence(0, readerString.length() - 1)
            }
            //替换文件
            project.file(filePath).withWriter('UTF-8') {
                writer ->
                    writer.append(readerString)
            }
        }
    }

}
