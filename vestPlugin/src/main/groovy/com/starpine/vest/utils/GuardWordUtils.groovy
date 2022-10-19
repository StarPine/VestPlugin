package com.starpine.vest.utils


import com.starpine.vest.bean.VestGuardInfo
import org.gradle.api.Project

/**
 * 描述：生成混淆字典工具类
 *
 * @Name ：VestPlugin* @Description ：
 * @Author： liaosf
 * @Date ：2022/10/17 17:09
 */
class GuardWordUtils {
    def data = "oO0"
    def guardFilePath = ""
    def arrayList
    def amount = 5000
    def generateGuardWord(VestGuardInfo vestInfo, Project project) {
        generateGuardWord(vestInfo, project, null)
    }
    def generateGuardWord(VestGuardInfo vestInfo, Project project, String guard) {
        if (vestInfo.guardInfo.finallyGuardWordFile == null) {
            guardFilePath = project.projectDir.path + File.separator + "vest-guard.txt"
        } else {
            guardFilePath = project.projectDir.path + File.separator + vestInfo.guardInfo.finallyGuardWordFile
        }
        if (guard != null) {
            guardFilePath = project.projectDir.path + File.separator + guard
        }
        if (vestInfo.guardInfo.guardWord != null) {
            data = vestInfo.guardInfo.guardWord
        }
        if (vestInfo.guardInfo.guardWordAmount != 0) {
            amount = vestInfo.guardInfo.guardWordAmount
        }
        data = data.replaceAll("[^a-zA-Z0-9,]", "")
        if (data.find(",")) {
            arrayList = data.split(",").toList()
        } else {
            arrayList = data.toList()
        }
        def content = ""
        LinkedHashSet<String> hashSet = new LinkedHashSet<>();
        Random rd = new Random();
        for (i in 0..<amount) {
            int h = rd.nextInt(6) + 6;
            String data = "";
            for (j in 0..<h) {
                int k = rd.nextInt(arrayList.size());
                data = data + arrayList.get(k);
            }
            hashSet.add(toUpperCaseFirstOne(data));
        }
        hashSet.each {
            content <<= it
            content <<= '\n'
        }
        project.file(guardFilePath).withWriter('UTF-8') {
            writer ->
                writer.append(content)
        }

        return guardFilePath
    }

    static String toUpperCaseFirstOne(String s) {
        if (Character.isDigit(s.charAt(0))) {
            return s.replaceAll("^[0-9]*", "")
        }
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

}
