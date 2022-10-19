package com.starpine.vest.bean;

/**
 * 描述：
 *
 * @Name： VestPlugin
 * @Description：
 * @Author： liaosf
 * @Date： 2022/10/17 12:26
 */
class StringsInfo {
    String values
    String path
    String content
    String moduleName

    StringsInfo(String values, String path, String content, String moduleName) {
        this.values = values
        this.path = path
        this.content = content
        this.moduleName = moduleName
    }
}
