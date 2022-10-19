package com.starpine.vest

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.BaseVariant
import com.starpine.vest.tasks.JunkCodeTask
import org.gradle.api.Plugin
import org.gradle.api.Project;

/**
 * 描述：
 *
 * @Name： VestPlugin
 * @Description：
 * @Author： liaosf
 * @Date： 2022/10/19 11:24
 */
class JunkCodePlugin implements Plugin<Project> {
    public final String vestRename = "vestJunkCode"
    @Override
    void apply(Project project) {

            AppExtension android = project.extensions.findByName("android")
            if (!android || !android.hasProperty("applicationVariants")) {
                throw IllegalArgumentException("must apply this plugin after 'com.android.application'")
            }
            //创建生成混淆字典文件任务
            JunkCodeTask junkCodeTask = project.tasks.create("generateJunkCode", JunkCodeTask)
            junkCodeTask.init(null, project)
            android.applicationVariants.all { BaseVariant variant ->
                variant.mergeResources.dependsOn(junkCodeTask)
            }

    }
}
