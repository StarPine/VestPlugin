package com.starpine.vest.bean


/**
 * 描述：
 *
 * @Name： VestPlugin
 * @Description：
 * @Author： liaosf
 * @Date： 2022/10/12 16:35
 */
class VestInfo {
    //需要生成的混淆字典的数量
    int guardWordAmount
    //需要生成的混淆字典的规则字段
    String guardWord
    //需要生成的混淆字典文件名
    String finallyGuardWordFile
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

}
