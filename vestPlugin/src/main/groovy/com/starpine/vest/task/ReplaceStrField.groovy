package com.starpine.vest.task

import com.starpine.vest.bean.VestInfo
import org.gradle.api.Project
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.TaskAction;

/**
 * 描述：多语言字段批量修改
 *
 * @Name： VestPlugin
 * @Description：
 * @Author： liaosf
 * @Date： 2022/10/13 10:57
 */
class ReplaceStrField {

    Project project
    VestInfo vestInfo

    ReplaceStrField() {
        group = "vest replace"
    }

    void init(VestInfo vestInfo, Project project) {
        this.vestInfo = vestInfo
        this.project = pro
    }

    @TaskAction
    def replaceStrField(){
        String strPath = "E:\\Develop_WS\\DL_Work\\Friendly_CC\\lib-src\\src\\main\\res\\values\\strings.xml"
        File renameStringsFile = new File(strPath)
        List<String> renameWord = renameStringsFile.readLines()

        File renameFile = new File(strRenameFilePath)
        List<String> strWord = renameFile.readLines()

        //初始化语言包内容
        String defStrings = ""
        String zhStrings = ""
        String ruStrings = ""
        String trStrings = ""
        FileTree stringsTree = fileTree("../lib-src") {
            include '**/strings.xml'
        }
        stringsTree.each {
            List<String> strContent = it.readLines()
            String content = strContent.toString()
            String path = it.getPath()
            if (path.find("values\\\\")) {
                defStrings = content
            } else if (path.find("values-ru\\\\")) {
                ruStrings = content
            } else if (path.find("values-tr\\\\")) {
                trStrings = content
            } else if (path.find("values-zh\\\\")) {
                zhStrings = content
            }
        }

        int index = 0
        renameWord.each { String lines ->
            if (lines.find("<string name=")) {

                String souStrName = lines.replaceAll(".*name=\"", "")
                        .replaceAll("\">.*", "")
                String targetName = strWord.get(index)
                index++
                println "$index--------$souStrName"

                //替换语言包字段
                defStrings = defStrings.replaceAll("(?<=\")$souStrName(?=\".*)", targetName)
                ruStrings = ruStrings.replaceAll("(?<=\")$souStrName(?=\".*)", targetName)
                trStrings = trStrings.replaceAll("(?<=\")$souStrName(?=\".*)", targetName)
                zhStrings = zhStrings.replaceAll("(?<=\")$souStrName(?=\".*)", targetName)

                //替换引用了语言包的文件
                FileTree processTree = fileTree("../") {
                    include '**/*.java'
                    include '**/layout/*.xml'
                    include '**/styles.xml'
                    include '**/AndroidManifest.xml'
                    exclude '**/androidTest/**'
                    exclude '**/gradle-build/**'
                    exclude '**/lib-toast/**'
                }
                processTree.each { File f ->
                    strSoureReplacer(f.path, souStrName, targetName)
                }
            }
        }

        //重新替换语言包文件
        stringsTree.each {
            String content = ""
            String path = it.getPath()
            if (path.find("values\\\\|values-en")) {
                content = defStrings
            } else if (path.find("values-ru")) {
                content = ruStrings
            } else if (path.find("values-tr")) {
                content = trStrings
            } else if (path.find("values-zh")) {
                content = zhStrings
            }
            content = content.replaceAll("(?<![\\w])\\[(?![\\w])", "")
                    .replaceAll("(?<!> {0,10})\\](?!> *<)", "")
                    .replaceAll("(?<=</resources>)\\]", "")
                    .replaceAll("(?<=(-->)|(<resources>)|(</string>))(, )+", "\n")
            project.file(path).withWriter('UTF-8') {
                writer ->
                    writer.append(content)
            }
        }
    }

    def strSoureReplacer(String filePath, sourceName, targetName) {
        def isXml = filePath.endsWith(".xml")
        def hasReplace = false
        String targetStr = ""
        File renameStringsFile = new File(filePath)
        byte[] contents = renameStringsFile.bytes
        String content = new String(contents)
        String str = content
        if (content.find("$sourceName")) {
            if (isXml) {
                content = content.replaceAll("(?<=@string/)$sourceName(?![0-9a-zA-Z_]+)", targetName)
            } else {
                content = content.replaceAll("(?<=R\\.string\\.)$sourceName(?![0-9a-zA-Z_]+)", targetName)
            }
            targetStr <<= content
            targetStr <<= '\n'
            if (!content.equals(str)){
                hasReplace = true
            }
        }


        if (hasReplace) {
            println filePath
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
