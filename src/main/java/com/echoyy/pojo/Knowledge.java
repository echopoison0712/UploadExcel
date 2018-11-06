package com.echoyy.pojo;

/**
 * @ClassName Knowledge
 * @Description
 * @Author echoyy
 * @Date 2018/8/29 下午3:05
 * @Version 1.0
 */
public class Knowledge {
    // 编号
    int id;
    // 分类
    String classify;
    // 领域
    String field;
    // 模块
    String module;
    // 问题
    String problem;
    // 答案
    String answer;
    // 提问题的人
    String askpsn;
    // 回答问题的人
    String answerpsn;
    // 答案最后编辑人
    String modifypsn;
    // 解决时间
    String tmaketime;
    // 最后修改时间
    String tmodifytime;
    // 删除标志
    int dr;
    // 分数
    int score;
    // 版本
    int version;
    // 附件地址
    String attachment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAskpsn() {
        return askpsn;
    }

    public void setAskpsn(String askpsn) {
        this.askpsn = askpsn;
    }

    public String getAnswerpsn() {
        return answerpsn;
    }

    public void setAnswerpsn(String answerpsn) {
        this.answerpsn = answerpsn;
    }

    public String getModifypsn() {
        return modifypsn;
    }

    public void setModifypsn(String modifypsn) {
        this.modifypsn = modifypsn;
    }

    public String getTmaketime() {
        return tmaketime;
    }

    public void setTmaketime(String tmaketime) {
        this.tmaketime = tmaketime;
    }

    public String getTmodifytime() {
        return tmodifytime;
    }

    public void setTmodifytime(String tmodifytime) {
        this.tmodifytime = tmodifytime;
    }

    public int getDr() {
        return dr;
    }

    public void setDr(int dr) {
        this.dr = dr;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
