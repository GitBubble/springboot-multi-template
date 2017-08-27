package com.huawei.ptn.opensourceDigger.model;

/**
 * Created by d00190167 on 2017/7/7.
 */
/**
 * 个人贡献榜Bean
 * @Title:ContributionTopBean.java
 * @Package com.huawei.ptn.opensourceDigger.model
 * @Description:TODO
 * @author:jack.deng.00190167
 * @time:2016年7月15日 上午10:45:40
 */
public class ContributionTopBean {
   private int ID;

    /*
     * 工号
     */
    private String employId;

    /*
     * 工号带姓
     */
    private String employNum;

    /*
     * 姓名
     */
    private String name;

    /*
     * 组名 (新增)
    */

    private String group;

    /*
     * 四级部门
     */
    private String departmentFour;

    /*
     * 五级部门
     */
    private String departmentFive;

    /*
     * 是否编码
     */
    private String role;

    /*
     * 角色
     */
    private String position;

    /*
     * issue总数
     */
    private int issueTotalCount;

    /*
     * 有效issue数
     */
    private int effectiveIssueCount;
    /**
     * 自提issue数
     */
    private int effectiveIssueCountBySelf;
    /**
     * 自己创建的责任issue
     */
    private int resposbilityIssueCountBySelf;
    /*
     * 闭环issue数
     */
    private int closeIssueCount;

    /*
     * MR评论数(自己)
     */
    private int noteTotalCount;

    /*
     * MR评论数(别人)
     */
    private int noteTotalOtherCount;
    /*
     * 处理MR数
     */
    private int handleMRCount;

    /*
     * 责任issue数
     */
    private int resposbilityIssueCount;

    /*
     * 被review闭环数
     */
    private int beiCloseIssueCount;

    /*
     * 被review未闭环数
     */
    private int unBeiCloseIssueCount;

    /*
     * 发起MR数
     */
    private int mrTotalCount;

    /*
     * 代码量超出范围MR数
     */
    private int overLineCount;

    /*
     * 发起MR被评论数
     */
    private int mrCommonCount;

    /*
     * 被驳回MR
     */
    private int mrRejected;

    /*
     * 积分
     */
    private int score;

    /*
     * 总贡献
     */
    private int totalContribution;

    /*
     * 功能问题数
     */
    private int funcErrSum;

    /*
     * 热度
     */
    private String hotDegree;

    /*
     * 参与度
     */
    private String participateDegree;

    private int totalPersionNum;

    private int beyondIssue;

    private int beiBeyondIssue;

    private int mergedMrCount;

    private int unHandleMRCount;

    private int mrCommonReplyCount;

    private int personalScore;

    public int getID() {
        return ID;
    }

    public void setID(int iD) { ID = iD; }

    public String getEmployNum() {
        return employNum;
    }

    public void setEmployNum(String employNum) {
        this.employNum = employNum;
    }

    public String getEmployId() {
        return employId;
    }

    public void setEmployId(String employId) {
        this.employId = employId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartmentFour() {
        return departmentFour;
    }

    public void setDepartmentFour(String departmentFour) {
        this.departmentFour = departmentFour;
    }

    public String getDepartmentFive() {
        return departmentFive;
    }

    public void setDepartmentFive(String departmentFive) {
        this.departmentFive = departmentFive;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getIssueTotalCount() {
        return issueTotalCount;
    }

    public void setIssueTotalCount(int issueTotalCount) {
        this.issueTotalCount = issueTotalCount;
    }

    public int getEffectiveIssueCount() {
        return effectiveIssueCount;
    }

    public void setEffectiveIssueCount(int effectiveIssueCount) {
        this.effectiveIssueCount = effectiveIssueCount;
    }

    public int getCloseIssueCount() {
        return closeIssueCount;
    }

    public void setCloseIssueCount(int closeIssueCount) {
        this.closeIssueCount = closeIssueCount;
    }

    public int getNoteTotalCount() {
        return noteTotalCount;
    }

    public void setNoteTotalCount(int noteTotalCount) {
        this.noteTotalCount = noteTotalCount;
    }

    public int getResposbilityIssueCount() {
        return resposbilityIssueCount;
    }

    public void setResposbilityIssueCount(int resposbilityIssueCount) {
        this.resposbilityIssueCount = resposbilityIssueCount;
    }

    public int getBeiCloseIssueCount() {
        return beiCloseIssueCount;
    }

    public void setBeiCloseIssueCount(int beiCloseIssueCount) {
        this.beiCloseIssueCount = beiCloseIssueCount;
    }

    public int getMrTotalCount() {
        return mrTotalCount;
    }

    public void setMrTotalCount(int mrTotalCount) {
        this.mrTotalCount = mrTotalCount;
    }

    public int getOverLineCount() {
        return overLineCount;
    }

    public void setOverLineCount(int overLineCount) {
        this.overLineCount = overLineCount;
    }

    public int getMrCommonCount() {
        return mrCommonCount;
    }

    public void setMrCommonCount(int mrCommonCount) {
        this.mrCommonCount = mrCommonCount;
    }

    public int getMrRejected() {
        return mrRejected;
    }

    public void setMrRejected(int mrRejected) {
        this.mrRejected = mrRejected;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalContribution() {
        return totalContribution;
    }

    public void setTotalContribution(int totalContribution) {
        this.totalContribution = totalContribution;
    }

    public int getFuncErrSum() {
        return funcErrSum;
    }

    public void setFuncErrSum(int funcErrSum) {
        this.funcErrSum = funcErrSum;
    }

    public String getHotDegree() {
        return hotDegree;
    }

    public void setHotDegree(String hotDegree) {
        this.hotDegree = hotDegree;
    }

    public String getParticipateDegree() {
        return participateDegree;
    }

    public void setParticipateDegree(String participateDegree) {
        this.participateDegree = participateDegree;
    }

    public int getTotalPersionNum() {
        return totalPersionNum;
    }

    public void setTotalPersionNum(int totalPersionNum) {
        this.totalPersionNum = totalPersionNum;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getUnBeiCloseIssueCount() {
        return unBeiCloseIssueCount;
    }

    public int getBeyondIssue() {
        return beyondIssue;
    }

    public void setBeyondIssue(int beyondIssue) {
        this.beyondIssue = beyondIssue;
    }

    public void setUnBeiCloseIssueCount(int unBeiCloseIssueCount) {
        this.unBeiCloseIssueCount = unBeiCloseIssueCount;
    }

    public int getBeiBeyondIssue() {
        return beiBeyondIssue;
    }

    public void setBeiBeyondIssue(int beiBeyondIssue) {
        this.beiBeyondIssue = beiBeyondIssue;
    }

    public int getMergedMrCount() {
        return mergedMrCount;
    }

    public void setMergedMrCount(int mergedMrCount) {
        this.mergedMrCount = mergedMrCount;
    }

    public int getUnHandleMRCount() {
        return unHandleMRCount;
    }

    public void setUnHandleMRCount(int unHandleMRCount) {
        this.unHandleMRCount = unHandleMRCount;
    }

    public int getMrCommonReplyCount() {
        return mrCommonReplyCount;
    }

    public void setMrCommonReplyCount(int mrCommonReplyCount) {
        this.mrCommonReplyCount = mrCommonReplyCount;
    }

    public int getPersonalScore() {
        return personalScore;
    }

    public void setPersonalScore(int personalScore) {
        this.personalScore = personalScore;
    }

    public int getNoteTotalOtherCount() {
        return noteTotalOtherCount;
    }

    public void setNoteTotalOtherCount(int noteTotalOtherCount) {
        this.noteTotalOtherCount = noteTotalOtherCount;
    }

    public int getEffectiveIssueCountBySelf() {
        return effectiveIssueCountBySelf;
    }

    public void setEffectiveIssueCountBySelf(int effectiveIssueCountBySelf) {
        this.effectiveIssueCountBySelf = effectiveIssueCountBySelf;
    }

    public int getResposbilityIssueCountBySelf() {
        return resposbilityIssueCountBySelf;
    }

    public void setResposbilityIssueCountBySelf(int resposbilityIssueCountBySelf) {
        this.resposbilityIssueCountBySelf = resposbilityIssueCountBySelf;
    }

    public int getHandleMRCount() {
        return handleMRCount;
    }

    public void setHandleMRCount(int handleMRCount) {
        this.handleMRCount = handleMRCount;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

}