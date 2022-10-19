package com.starpine.vest.tasks

import com.android.build.gradle.FeaturePlugin
import com.android.build.gradle.ProguardFiles
import com.android.build.gradle.ReportingPlugin
import com.android.build.gradle.TestPlugin
import com.google.common.io.Resources
import com.starpine.vest.Jiang
import com.starpine.vest.JunkCodePlugin
import com.starpine.vest.bean.VestGuardInfo
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

import static java.nio.charset.StandardCharsets.UTF_8;

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
    VestGuardInfo vestInfo

    TestTask() {
        group = "vest guard"
    }

    void init(VestGuardInfo vestInfo, Project project) {
        this.vestInfo = vestInfo
        this.project = project
    }

    @TaskAction
    def test() {
        def kong = "TestTask.groovy"
//        project.file(kong).withWriter('UTF-8'){
//            it.append()
//        }
        def stream = ProguardFiles.class.getResourceAsStream("proguard-common.txt")
        stream.eachLine {
            println it
        }
//        project.file(kong).withReader {
//            it.eachLine {lines->
//                println it
//
//            }
//        }
//        println Resources.toString(resource, UTF_8)
    }
}
