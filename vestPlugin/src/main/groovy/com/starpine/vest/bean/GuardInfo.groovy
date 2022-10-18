package com.starpine.vest.bean;

/**
 * 描述：
 * 注意：生成闭包嵌套配置格式一定要在相关的bean类添加跟属性名一样的方法才能生效
 * @Name： VestPlugin
 * @Description：
 * @Author： liaosf
 * @Date： 2022/10/17 12:26
 */
class GuardInfo {
    //需要生成的混淆字典的数量
    int guardWordAmount
    //需要生成的混淆字典的规则字段
    String guardWord
    //需要生成的混淆字典文件名
    String finallyGuardWordFile

    void guardWordAmount(int guardWordAmount) {
        this.guardWordAmount = guardWordAmount
    }

    void guardWord(String guardWord) {
        this.guardWord = guardWord
    }

    void finallyGuardWordFile(String finallyGuardWordFile) {
        this.finallyGuardWordFile = finallyGuardWordFile
    }
}
