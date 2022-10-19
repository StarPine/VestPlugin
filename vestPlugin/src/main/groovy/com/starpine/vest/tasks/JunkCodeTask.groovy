package com.starpine.vest.tasks

import com.starpine.vest.bean.JunkCodeConfigInfo
import com.starpine.vest.bean.VestGuardInfo
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

/**
 * 描述：仅测试打印日志使用
 *
 * @Name ：VestPlugin
 * @Description ：
 * @Author： liaosf
 * @Date ：2022/10/17 15:51
 */
class JunkCodeTask extends DefaultTask {
    Project project
    JunkCodeConfigInfo configInfo
    final def kkk = '''package com.starpine.vestplugin;

public class Eeeeehe {

    private String msgId;
    private long sendTime;

    public Eeeeehe(String msgId, long sendTime) {
        this.msgId = msgId;
        this.sendTime = sendTime;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}'''

    JunkCodeTask() {
        group = "vest guard"
    }

    void init(JunkCodeConfigInfo configInfo, Project project) {
        this.configInfo = configInfo
        this.project = project
    }

    @TaskAction
    def test() {
        File files = new File(project.getProjectDir().path + File.separator + "src/main/java/com/starpine/vestplugin/Eeeeehe.java")
        println files.path
        project.file(files.path).withWriter('UTF-8') {
            writer ->
                writer.append(kkk)
        }
    }
}
