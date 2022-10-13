package com.starpine.vest.task

import com.starpine.vest.bean.VestInfo
import org.gradle.api.DefaultTask
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
class AllReplaceTask extends DefaultTask{

    Project project
    VestInfo vestInfo

    AllReplaceTask() {
        group = "vest replace"
    }

    void init(VestInfo vestInfo, Project project) {
        this.vestInfo = vestInfo
        this.project = project
    }

    @TaskAction
    def all(){
        println("全部替换完成")
    }

}
