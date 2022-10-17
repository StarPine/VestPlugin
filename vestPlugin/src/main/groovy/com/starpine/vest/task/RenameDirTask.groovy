package com.starpine.vest.task

import com.starpine.vest.bean.VestInfo
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.TaskAction

/**
 * 描述：当前项目包名下的文件夹名字批量更改
 *
 * @Name： VestPlugin
 * @Description：
 * @Author： liaosf
 * @Date： 2022/10/13 10:57
 */
class RenameDirTask extends DefaultTask{

    Project project
    VestInfo vestInfo
    def index = 0
    String finallyName = "aaa"
    def renameMapping
    def mappingString = ""

    RenameDirTask() {
        group = "vest guard"
    }

    void init(VestInfo vestInfo, Project project) {
        this.vestInfo = vestInfo
        this.project = project
    }

    @TaskAction
    def startRenameDir(){
        def renameMappingDir = project.getProjectDir().path + File.separator + "vest_mapping/"
        renameMapping = renameMappingDir + "dir-name-mapping.txt"
        File mappingDir = new File(renameMappingDir)
        if(!mappingDir.exists()){
            mappingDir.mkdir()
        }

        if (vestInfo.newDirWord != null){
            finallyName = vestInfo.newDirWord
        }
        File file = new File(vestInfo.sourceDirPath)
        renameDirName(file, false)

        project.file(renameMapping).withWriter('UTF-8') {
            writer ->
                writer.append(mappingString)
        }
    }

    def renameDirName(file, isRename) {
        if (file.isDirectory()) {
            println(file.path+"----$index")

            if (isRename) {
                def dirPath = file.path
                def dirName = file.name
                def sourcePackage = dirPath.substring(dirPath.indexOf("com"), dirPath.length()).replaceAll("\\\\", ".")
                def targetName = "$finallyName$index"
                String targetPackage = sourcePackage.replace(dirName,targetName)
                def newFileName = file.parent + File.separator + targetName
                mappingString <<= "$sourcePackage -> $targetPackage"
                mappingString << "\n"
                println file.renameTo(newFileName)
                file = new File(newFileName)

                FileTree targetTree = project.fileTree("../app") {
                    include '**/*.java'
                    include '**/layout/*.xml'
                    include '**/styles.xml'
                    include '**/AndroidManifest.xml'
                    include '**/proguard-rules.pro'
                    exclude '**/androidTest/**'
                    exclude '**/gradle-build/**'
                    exclude '**/lib-toast/**'
                }

                targetTree.each {
                    renameRelyFile(it.path, sourcePackage, targetPackage)
                }

            }

            file.listFiles().each {
                if (it.isDirectory()){
                    index++
                    renameDirName(it, true)
                }
            }
        }
    }

    def renameRelyFile(filePath, sourcePackage, targetPackage) {
        def hasReplace = false
        String targetStr = ""
        File renameStringsFile = new File(filePath)
        byte[] contents = renameStringsFile.bytes
        String content = new String(contents)
        String str = content
        if (content.find(sourcePackage)) {
            content = content.replaceAll("$sourcePackage(?![0-9a-zA-Z_]+)(?=[\\.\\*;]*)", targetPackage)
            targetStr <<= content
            targetStr <<= '\n'
            if (!content.equals(str)){
                hasReplace = true
            }
        }

        if (hasReplace) {
            if (targetStr.toString().endsWith("\n")) {
                targetStr = targetStr.subSequence(0, targetStr.length() - 1)
            }
            project.file(filePath).withWriter('UTF-8') {
                writer ->
                    writer.append(targetStr)
            }
        }
    }

}
