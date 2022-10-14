package com.starpine.vest.bean

import org.gradle.api.Action
import org.gradle.util.ConfigureUtil;

/**
 * 描述：
 *
 * @Name： VestPlugin
 * @Description：
 * @Author： liaosf
 * @Date： 2022/10/12 16:35
 */
class VestInfo {
    //目标类文件
    String targetClassPath
    //换名混淆字典文件
    String renameFile
    //换名映射文件
    String renameMapping
    //目标多语言文件
    String strPath
    //目标文件夹
    String targetDirPath
    String newDirText

}
