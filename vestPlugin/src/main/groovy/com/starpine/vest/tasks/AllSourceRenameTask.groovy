package com.starpine.vest.tasks

import com.starpine.vest.bean.VestGuardInfo
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction;

/**
 * 描述：全部资源名字批量更改（类名，文件夹名，多语言字段）
 *
 * @Name： VestPlugin
 * @Description：
 * @Author： liaosf
 * @Date： 2022/10/13 10:57
 */
class AllSourceRenameTask extends DefaultTask{

    Project project
    VestGuardInfo vestInfo

    AllSourceRenameTask() {
        group = "vest guard"
    }

    void init(VestGuardInfo vestInfo, Project project) {
        this.vestInfo = vestInfo
        this.project = project
    }

    @TaskAction
    def all(){
        println("全部替换完成")
    }

}
