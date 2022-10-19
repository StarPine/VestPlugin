package com.starpine.vest


import com.starpine.vest.bean.VestGuardInfo
import com.starpine.vest.tasks.AllSourceRenameTask
import com.starpine.vest.tasks.GenerateGuardTask
import com.starpine.vest.tasks.RenameFolderTask
import com.starpine.vest.tasks.RenameStrFieldTask
import com.starpine.vest.tasks.TestTask
import org.gradle.api.Plugin
import org.gradle.api.Project;
import com.starpine.vest.tasks.RenameClassNameTask

/**
 * 描述：
 *
 * @Name： VestPlugin
 * @Description：
 * @Author： liaosf
 * @Date： 2022/10/12 16:15
 */
class VestGuardPlugin implements Plugin<Project>{
    public final String vestRename = "vestGuard"
    @Override
    void apply(Project project) {
        VestGuardInfo vestInfo = project.getExtensions().create(vestRename , VestGuardInfo)
        project.afterEvaluate {
            //创建生成混淆字典文件任务
            GenerateGuardTask generateGuardTask = project.tasks.create("generateGuardWord",GenerateGuardTask)
            generateGuardTask.init(vestInfo, project)

            //创建测试任务
            TestTask testTask = project.tasks.create("testSetting", TestTask)
            testTask.init(vestInfo, project)

            //创建修改类名任务
            RenameClassNameTask renameClassNameTask = project.tasks.create("renameClassName",RenameClassNameTask)
            renameClassNameTask.init(vestInfo, project)

            //创建修改多语言字段任务
            RenameStrFieldTask renameStrField = project.tasks.create("renameStr", RenameStrFieldTask)
            renameStrField.init(vestInfo, project)

            //创建修改多语言字段任务
            RenameFolderTask renameDirTask = project.tasks.create("renameFolder", RenameFolderTask)
            renameDirTask.init(vestInfo, project)

            //创建一键替换所有属性任务（包含：类别，多语言字段等）
            AllSourceRenameTask allRenameTask = project.tasks.create("allRename", AllSourceRenameTask)
            allRenameTask.init(vestInfo, project)

            //添加依赖关系
            allRenameTask.dependsOn(renameClassNameTask, renameStrField, renameDirTask)
        }
    }
}
