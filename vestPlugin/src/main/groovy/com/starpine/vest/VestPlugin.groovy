package com.starpine.vest

import com.starpine.vest.bean.VestInfo
import com.starpine.vest.task.ReplaceStrField
import org.gradle.api.Plugin
import org.gradle.api.Project;
import com.starpine.vest.task.ReplaceClassNameTask

/**
 * 描述：
 *
 * @Name： VestPlugin
 * @Description：
 * @Author： liaosf
 * @Date： 2022/10/12 16:15
 */
class VestPlugin implements Plugin<Project>{
    public final String vestReplace = "vestReplace"
    @Override
    void apply(Project project) {
        VestInfo vestInfo = project.getExtensions().create(vestReplace , VestInfo)
        project.afterEvaluate {
            //创建修改类名任务
            ReplaceClassNameTask replaceClassNameTask = project.tasks.create("replaceClassName",ReplaceClassNameTask)
            replaceClassNameTask.init(vestInfo, project)

            //创建修改多语言字段任务
            ReplaceStrField replaceStrField = project.tasks.create("replaceStr", ReplaceStrField)
            replaceStrField.init(vestInfo, project)
        }
    }
}
