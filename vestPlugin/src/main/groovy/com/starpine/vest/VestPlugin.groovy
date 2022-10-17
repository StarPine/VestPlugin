package com.starpine.vest

import com.starpine.vest.bean.VestInfo
import com.starpine.vest.task.AllSourceRenameTask
import com.starpine.vest.task.GenerateGuardTask
import com.starpine.vest.task.RenameDirTask
import com.starpine.vest.task.RenameStrFieldTask
import org.gradle.api.Plugin
import org.gradle.api.Project;
import com.starpine.vest.task.RenameClassNameTask

/**
 * 描述：
 *
 * @Name： VestPlugin
 * @Description：
 * @Author： liaosf
 * @Date： 2022/10/12 16:15
 */
class VestPlugin implements Plugin<Project>{
    public final String vestRename = "vestGuard"
    @Override
    void apply(Project project) {
        VestInfo vestInfo = project.getExtensions().create(vestRename , VestInfo)
        project.afterEvaluate {
            //创建生成混淆字典文件任务
            GenerateGuardTask generateGuardTask = project.tasks.create("generateGuardWord",GenerateGuardTask)
            generateGuardTask.init(vestInfo, project)

            //创建修改类名任务
            RenameClassNameTask renameClassNameTask = project.tasks.create("renameClassName",RenameClassNameTask)
            renameClassNameTask.init(vestInfo, project)

            //创建修改多语言字段任务
            RenameStrFieldTask renameStrField = project.tasks.create("renameStr", RenameStrFieldTask)
            renameStrField.init(vestInfo, project)

            //创建修改多语言字段任务
            RenameDirTask renameDirTask = project.tasks.create("renameDirName", RenameDirTask)
            renameDirTask.init(vestInfo, project)

            //创建一键替换所有属性任务（包含：类别，多语言字段等）
            AllSourceRenameTask allRenameTask = project.tasks.create("allRename", AllSourceRenameTask)
            allRenameTask.init(vestInfo, project)

            //添加依赖关系
            allRenameTask.dependsOn(renameClassNameTask, renameStrField, renameDirTask)
        }
    }
}
