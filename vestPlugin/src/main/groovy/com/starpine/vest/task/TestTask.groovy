package com.starpine.vest.task

import com.starpine.vest.bean.VestInfo
import com.starpine.vest.utils.GuardWordUtils
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction;

/**
 * 描述：仅测试打印日志使用
 *
 * @Name ：VestPlugin
 * @Description ：
 * @Author： liaosf
 * @Date ：2022/10/17 15:51
 */
class TestTask extends DefaultTask {
    Project project
    VestInfo vestInfo

    TestTask() {
        group = "vest guard"
    }

    void init(VestInfo vestInfo, Project project) {
        this.vestInfo = vestInfo
        this.project = project
    }

    @TaskAction
    def test() {
        println vestInfo.toString()
        println vestInfo.innerExt.msg
    }
}
