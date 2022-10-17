package com.starpine.vest.task

import com.starpine.vest.bean.VestInfo
import com.starpine.vest.utils.GuardWordUtils
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction;

/**
 * 描述：生成混淆字典文件
 *
 * @Name ：VestPlugin
 * @Description ：
 * @Author： liaosf
 * @Date ：2022/10/17 15:51
 */
class GenerateGuardTask extends DefaultTask {
    Project project
    VestInfo vestInfo
    def data = "oO0"
    def guardFilePath = ""
    def arrayList
    def amount = 5000

    GenerateGuardTask() {
        group = "vest guard"
    }

    void init(VestInfo vestInfo, Project project) {
        this.vestInfo = vestInfo
        this.project = project
    }

    @TaskAction
    def generate() {
        GuardWordUtils guardWordUtils = new GuardWordUtils()
        guardWordUtils.generateGuardWord(vestInfo,project,null)

    }
}
