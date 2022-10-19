package com.starpine.vest.bean

import org.gradle.api.Action
import org.gradle.util.ConfigureUtil


/**
 * 描述：
 *
 * @Name： VestPlugin
 * @Description：
 * @Author： liaosf
 * @Date： 2022/10/12 16:35
 */
class VestGuardInfo {

    //混淆字典文件
    String obfuscationdictionary
    //类混淆字典文件
    String classobfuscationdictionary
    //包混淆字典文件
    String packageobfuscationdictionary
    //目标类文件
    String sourceClassPath
    //目标文件夹
    String sourceDirPath
    //新文件夹基准单词
    String newDirWord

    GuardInfo guardInfo = new GuardInfo()

    //创建内部Extension，名称为方法名 guard
    void guardConfig(Action<GuardInfo> action) {
        action.execute(guardConfig)
    }

    //创建内部Extension，名称为方法名 guard
    void guardConfig(Closure closure) {
        ConfigureUtil.configure(closure, guardInfo)
    }

}
