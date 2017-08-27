package com.huawei.ptn.opensourceDigger.service.impl;


import com.huawei.ipm.base.utils.date.DateUtil;
import com.huawei.ipm.base.utils.string.StringUtil;
import com.huawei.ptn.opensourceDigger.config.api.HttpRestCallApi;
import com.huawei.ptn.opensourceDigger.model.ContributionTopBean;
import com.huawei.ptn.opensourceDigger.model.ProjectGroups;
import com.huawei.ptn.opensourceDigger.orm.HibernateDao;
import com.huawei.ptn.opensourceDigger.utils.DiggerHelper;
import net.sf.json.JSONArray;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.huawei.ptn.opensourceDigger.service.ContributionTopService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.*;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 个人贡献榜service实现类
 * @Title:ContributionTopServiceImpl.java
 * @Package com.huawei.ptn.opensourceDigger.service.impl
 * @Description:TODO
 * @author:dengwenbin 00190167
 * @time:2017/7/26
 */

@Service
public class ContributionTopServiceImpl implements ContributionTopService{

    @SuppressWarnings("rawtypes")
    @Autowired
    private HibernateDao<ContributionTopBean, Integer> topDao;

    @SuppressWarnings("rawtypes")
    @Autowired
    private JdbcTemplate neJdbcTemplate;

    @Autowired
    private DiggerHelper helper;

    @Value(value="${config.default.organization}")
    private String  default_organization;

    Map<String,List> teamSquad = null;

    private String pngGeneratorUrl = "http://127.0.0.1:3000/test";

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentManagerServiceImpl.class);

    public HibernateDao<ContributionTopBean, Integer> getTopDao() {
        return this.topDao;
    }


    public void setTopDao(HibernateDao<ContributionTopBean, Integer> topDao) {
        this.topDao = topDao;
        this.topDao.setEntityClass(ContributionTopBean.class);
    }


    public Map<String,List> getTeamSquad(){
        return teamSquad;
    }

    public void setTeamSquad(Map<String,List> team){
        teamSquad = team;
    }
    /*public enum GROUP_IDENTIFIER {
        QOS安全组, LS, HS, 交叉组, 子卡OM组
    }*/


    private String[] getTimeSpan(int interval){

        if(interval <= 0 || interval > 6){
            interval = 1;
        }

        Map<Integer,Integer> deptNameMapping = new HashedMap();
        deptNameMapping.put(1,-7);
        deptNameMapping.put(2,-14);
        deptNameMapping.put(3,-30);
        deptNameMapping.put(4,-60);
        deptNameMapping.put(5,-90);
        deptNameMapping.put(6,-365);


        String endTime = DateUtil.CurrentTime();
        String startTime = DateUtil.obtainOverTime(endTime, deptNameMapping.get(interval));

        String[] timeSpan = {startTime,endTime};

        return timeSpan;
    }

    public String getSortCondition(int sortId){

        if(sortId <= 0 || sortId > 2){
            sortId = 1;
        }

        Map<Integer,String> sortCondition = new HashedMap();
        sortCondition.put(1,"personalScore");
        sortCondition.put(2,"handleMRCount");
        sortCondition.put(3,"effectiveIssueCount");

        return sortCondition.get(sortId);
    }

    // query sort scope -> du ? pdu ? lm ?
    private void filterScope(Map<String,String> condition,String sortScope,String[] detail){
        if(sortScope.equals("2")){
            condition.put("deptFour",detail[1]);// 4级部门, 如流控软件开发部
        }else if(sortScope.equals("1")){
            condition.put("deptFive",detail[2]);// 5级部门，如模型算法开发部
        }

        return;
    }

    private void configDefaultQueryCondition(Map<String,List> param, Map<String,String> opt){
        if(param.size() == 0 ){
            //load again
            //DiggerHelper helper = new DiggerHelper();
            Map<String,List> teamSquad = helper.loadOurTeamSquad(default_organization);
        }else{
            setTeamSquad(param); //save to members
        }

        //opt's default value will determined by following procedures..

        return;
    }

    /**
     * 前台 RUN 按钮自定义条件查询接口
     * @Title:ContributionTopServiceImpl.java
     * @Description:TODO
     * @author:dengwenbin 00190167
     * @time:2017/8/10
     */
    @Transactional
    public List<ContributionTopBean> queryContributionDomainTops(Map<String,List> param,Map<String,String> opt)
    {
           configDefaultQueryCondition(param,opt);

           String sortCondition = opt.get("sortCondition");
           String sortScope = opt.get("sortScope");
           String sortPeriod = opt.get("period");

           String[] timeSpan = getTimeSpan(Integer.parseInt(sortPeriod));
           String[] depatDetail = opt.get("org").split("-");

           Map<String,String> condition = new HashMap<String, String>();

           filterScope(condition,sortScope,depatDetail);

           condition.put("deptThree",depatDetail[0]);
           condition.put("geoLocation",depatDetail[3]);
           condition.put("startTime",timeSpan[0]);
           condition.put("endTime",timeSpan[1]);
           condition.put("sort",sortCondition);
           condition.put("lineNum","500");
           condition.put("staffType","华为");
           condition.put("role","编码");
           condition.put("position","员工"); // do not use this field, unless you dont want 'COMMITER' appears
           condition.put("start","0");
           condition.put("dir","ASC");

           return queryListByCondition(condition);
    }



    @Transactional
    public List<ContributionTopBean> queryListByCondition(Map<String,String> condition){

        //mainly tackle the situation of sortScope ,return the top 10 contribution users

        return queryContributionTops(condition);
    }


    /**
     * 只查询深圳地区的排行榜
     * @Title:ContributionTopServiceImpl.java
     * @Description:TODO
     * @author:dengwenbin 00190167
     * @time:2017/8/10
     */
    @Transactional
    @Override
    public List<ContributionTopBean> queryContributionSZTops(){

       // DiggerHelper helper = new DiggerHelper();
        Map<String,List> teamSquad = helper.loadOurTeamSquad(default_organization);
        setTeamSquad(teamSquad);
        String endTime = DateUtil.CurrentTime();
        String startTime = DateUtil.obtainOverTime(endTime,-7);
        Map<String,String> param = new HashMap<String, String>();
        param.put("deptThree","路由器与电信以太开发管理部");
        param.put("deptFour","流控软件开发部");
        param.put("deptFive","模型算法开发部");
        param.put("startTime",startTime);
        param.put("endTime",endTime);
        param.put("lineNum","500");
        param.put("staffType","华为");
        param.put("role","编码");
        param.put("position","员工"); // do not use this field, unless you dont want 'COMMITER' appears
        param.put("sort","personalScore");// sort by the formula iSource define, i will change it latter,jack.deng
        param.put("start","0");
        param.put("dir","ASC");
        return queryContributionTops(param);
    }

    /**
     * 通用查询，被调用
     * @Title:ContributionTopServiceImpl.java
     * @Description: 根据用户，查询xdu中所有isource的数据
     * @author:dengwenbin 00190167
     * @time:2017/8/10
     */
    @SuppressWarnings("unchecked")
    public List<ContributionTopBean> queryContributionTops(
            Map<String, String> param) {
        List<Map<String, Object>> listUser = queryUserInfoList(param);
        List<Map<String, Object>> listOverLineCount = queryOverLineCount(param);
        List<Map<String, Object>> listMR = queryMR(param);
        List<Map<String, Object>> listIssue = queryIssue(param);
        List<Map<String, Object>> listSelfNoteMr = querySelfNoteMr(param);
        List<Map<String, Object>> listOtherNoteMr = queryOtherNoteMr(param);
        List<Map<String, Object>> listMrCount = queryMrCount(param);
        List<Map<String, Object>> listHandleMR = queryHandleMR(param);
        List<Map<String, Object>> listMrReplyCount = queryMrReplyCount(param);
        List<Map<String, Object>> listReviewIssue = queryReviewIssue(param);

        for(Map<String, Object> map:listUser){
            String authorId = StringUtil.getStringFromObject(map.get("authorId"));
            map.put("overLineCount", 0);
            map.put("mrTotalCount", 0);
            map.put("mrRejected", 0);
            map.put("mergedMrCount", 0);
            map.put("nHandleMRCount", 0);
            map.put("issueTotalCount", 0);
            map.put("effectiveIssueCount", 0);
            map.put("effectiveIssueCountBySelf", 0);
            map.put("closeIssueCount", 0);
            map.put("beyondIssue", 0);
            map.put("noteTotalCount", 0);
            map.put("noteTotalOtherCount", 0);
            map.put("mrCommonCount", 0);
            map.put("mrCommonReplyCount", 0);
            map.put("resposbilityIssueCountBySelf", 0);
            map.put("resposbilityIssueCount", 0);
            map.put("beiCloseIssueCount", 0);
            map.put("unBeiCloseIssueCount", 0);
            map.put("beiBeyondIssue", 0);
            for (Map<String, Object> overLineCount : listOverLineCount) {
                if(authorId.trim().equals(overLineCount.get("authorId"))){
                    map.put("overLineCount", MapUtils.getInteger(overLineCount, "overLineCount", 0));
                    break;
                }
            }

            for (Map<String, Object> mr : listMR) {
                if(authorId.trim().equals(mr.get("authorId"))){
                    map.put("mrTotalCount", MapUtils.getInteger(mr, "mrTotalCount", 0));
                    map.put("mrRejected", MapUtils.getInteger(mr, "mrRejected", 0));
                    map.put("mergedMrCount", MapUtils.getInteger(mr, "mergedMrCount", 0));
                    map.put("nHandleMRCount", MapUtils.getInteger(mr, "nHandleMRCount", 0));
                    break;
                }
            }

            for (Map<String, Object> issue : listIssue) {
                if(authorId.trim().equals(issue.get("authorId"))){
                    map.put("issueTotalCount", MapUtils.getInteger(issue, "issueTotalCount", 0));
                    map.put("effectiveIssueCount", MapUtils.getInteger(issue, "effectiveIssueCount", 0));
                    map.put("effectiveIssueCountBySelf", MapUtils.getInteger(issue, "effectiveIssueCountBySelf", 0));
                    map.put("closeIssueCount", MapUtils.getInteger(issue, "closeIssueCount", 0));
                    map.put("beyondIssue", MapUtils.getInteger(issue, "beyondIssue", 0));
                    break;
                }
            }

            for (Map<String, Object> selfNoteMr : listSelfNoteMr) {
                if(authorId.trim().equals(selfNoteMr.get("authorId"))){
                    map.put("noteTotalCount", MapUtils.getInteger(selfNoteMr, "noteTotalCount", 0));
                    break;
                }
            }

            for (Map<String, Object> otherNoteMr : listOtherNoteMr) {
                if(authorId.trim().equals(otherNoteMr.get("authorId"))){
                    map.put("noteTotalOtherCount", MapUtils.getInteger(otherNoteMr, "noteTotalOtherCount", 0));
                    break;
                }
            }

            for (Map<String, Object> mrCount : listMrCount) {
                if(authorId.trim().equals(mrCount.get("authorId"))){
                    map.put("mrCommonCount", MapUtils.getInteger(mrCount, "mrCommonCount", 0));
                    break;
                }
            }

            for(Map<String, Object> handMrCount : listHandleMR){
                if(authorId.trim().equals(handMrCount.get("authorId"))){
                    map.put("handleMRCount", MapUtils.getInteger(handMrCount,"handleMRCount",0));
                }
            }

            for (Map<String, Object> mrReplyCount : listMrReplyCount) {
                if(authorId.trim().equals(mrReplyCount.get("authorId"))){
                    map.put("mrCommonReplyCount", MapUtils.getInteger(mrReplyCount, "mrCommonReplyCount", 0));
                    break;
                }
            }

            for (Map<String, Object> reviewIssue : listReviewIssue) {
                if(authorId.trim().equals(reviewIssue.get("authorId"))){
                    map.put("resposbilityIssueCountBySelf", MapUtils.getInteger(reviewIssue, "resposbilityIssueCountBySelf", 0));
                    map.put("resposbilityIssueCount", MapUtils.getInteger(reviewIssue, "resposbilityIssueCount", 0));
                    map.put("beiCloseIssueCount", MapUtils.getInteger(reviewIssue, "beiCloseIssueCount", 0));
                    map.put("unBeiCloseIssueCount", MapUtils.getInteger(reviewIssue, "unBeiCloseIssueCount", 0));
                    map.put("beiBeyondIssue", MapUtils.getInteger(reviewIssue, "beiBeyondIssue", 0));
                    break;
                }
            }
        }

        List<ContributionTopBean> beanList = new ArrayList<ContributionTopBean>();
        if(null != listUser && listUser.size() > 0){
            for (Map<String, Object> map : listUser) {
                ContributionTopBean contributionTopBean = new ContributionTopBean();
                int effectiveIssueCount = StringUtil.getIntFromObject(map.get("effectiveIssueCount"));
                int noteTotalOtherCount = StringUtil.getIntFromObject(map.get("noteTotalOtherCount"));
                int mergedMrCount = StringUtil.getIntFromObject(map.get("mergedMrCount"));
                int mergedMrCountScore = 0;
                if(mergedMrCount>10){
                    mergedMrCountScore = mergedMrCount/10;
                }
                int personalScore = effectiveIssueCount*3+noteTotalOtherCount*2+mergedMrCountScore;


                contributionTopBean.setName(StringUtil.getStringFromObject(map.get("name")));
                contributionTopBean.setEmployNum(StringUtil.getStringFromObject(map.get("employNum")));
                contributionTopBean.setDepartmentFour(StringUtil.getStringFromObject(map.get("departmentFour")));
                contributionTopBean.setDepartmentFive(StringUtil.getStringFromObject(map.get("departmentFive")));
                contributionTopBean.setRole(StringUtil.getStringFromObject(map.get("role")));
                contributionTopBean.setPosition(StringUtil.getStringFromObject(map.get("position")));
                contributionTopBean.setIssueTotalCount(StringUtil.getIntFromObject(map.get("issueTotalCount")));
                contributionTopBean.setEffectiveIssueCount(effectiveIssueCount);
                contributionTopBean.setEffectiveIssueCountBySelf(StringUtil.getIntFromObject(map.get("effectiveIssueCountBySelf")));
                contributionTopBean.setCloseIssueCount(StringUtil.getIntFromObject(map.get("closeIssueCount")));
                contributionTopBean.setBeyondIssue(StringUtil.getIntFromObject(map.get("beyondIssue")));
                contributionTopBean.setNoteTotalCount(StringUtil.getIntFromObject(map.get("noteTotalCount")));
                contributionTopBean.setNoteTotalOtherCount(noteTotalOtherCount);
                contributionTopBean.setResposbilityIssueCount(StringUtil.getIntFromObject(map.get("resposbilityIssueCount")));
                contributionTopBean.setResposbilityIssueCountBySelf(StringUtil.getIntFromObject(map.get("resposbilityIssueCountBySelf")));
                contributionTopBean.setBeiCloseIssueCount(StringUtil.getIntFromObject(map.get("beiCloseIssueCount")));
                contributionTopBean.setUnBeiCloseIssueCount(StringUtil.getIntFromObject(map.get("unBeiCloseIssueCount")));
                contributionTopBean.setBeiBeyondIssue(StringUtil.getIntFromObject(map.get("beiBeyondIssue")));
                contributionTopBean.setMrCommonCount(StringUtil.getIntFromObject(map.get("mrCommonCount")));
                contributionTopBean.setMrCommonReplyCount(StringUtil.getIntFromObject(map.get("mrCommonReplyCount")));

                //MR detail
                contributionTopBean.setMergedMrCount(mergedMrCount);
                int mrTotalCount = StringUtil.getIntFromObject(map.get("mrTotalCount"));
                int unHandleMr = StringUtil.getIntFromObject(map.get("unHandleMRCount"));
                contributionTopBean.setMrTotalCount(mrTotalCount);
                contributionTopBean.setUnHandleMRCount(unHandleMr);
                contributionTopBean.setHandleMRCount( StringUtil.getIntFromObject(map.get("handleMRCount")) );
                contributionTopBean.setMrRejected(StringUtil.getIntFromObject(map.get("mrRejected")));
                contributionTopBean.setOverLineCount(StringUtil.getIntFromObject(map.get("overLineCount")));
                contributionTopBean.setTotalContribution(StringUtil.getIntFromObject(map.get("totalContribution")));
                contributionTopBean.setScore(StringUtil.getIntFromObject(map.get("score")));
                contributionTopBean.setPersonalScore(personalScore);
                contributionTopBean.setGroup(StringUtil.getStringFromObject(map.get("group")));
                beanList.add(contributionTopBean);
            }
            String querySql = "select DomainId from T_ISOURCE_Committer tt";
            List<Object> employNumList = topDao.createSqlQuery(querySql).list();

            // nasty process ....i dont care
            for(ContributionTopBean contributionTopBean : beanList){
                boolean flag = false;
                for(Object employNum : employNumList){
                    if(contributionTopBean.getEmployNum().equalsIgnoreCase(employNum.toString())){
                        flag = true;
                    }
                }
                if(flag){
                    contributionTopBean.setPosition("COMMITTER");
                }
            }
        }

        this.sortByCondition(beanList,param);

        return beanList;
    }

    public List<Map<String, Object>> queryHandleMR(Map<String, String> param){

        String mrSql = " (select mr.*  FROM T_DuIsource_Merge_Requests mr LEFT JOIN T_DuIsource_Merge_Requests_other other On mr.id = other.id where other.merge_request_id = '0') ";

        String startTime = param.get("startTime");
        String endTime = param.get("endTime");
        StringBuffer sql = new StringBuffer()
                .append("	SELECT mr.handler_id,COUNT (1) AS handleMRCount ")
                .append(" 	FROM "+mrSql+" mr ")
                .append(" 	WHERE mr.author_id != mr.handler_id ")
                .append(" 	AND mr.created_at > CAST ('"+startTime+"' AS datetime) ")
                .append(" 	AND mr.created_at < CAST ('"+endTime+"' AS datetime) ")
                .append(" 	GROUP BY mr.handler_id ");

        List<Object> list = topDao.createSqlQuery(sql.toString()).list();
        List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
        for (Object object : list) {
            Object[] obj = (Object[])object;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("authorId", StringUtil.getStringFromObject(obj[0]));
            map.put("handleMRCount", StringUtil.getStringFromObject(obj[1]));
            listMap.add(map);
        }
        return listMap;
    }

    private void calcProjectScore(List<ProjectGroups> projectRank,List<ContributionTopBean> groupRanks)
    {
        ProjectGroups groupX = new ProjectGroups();

        for(ContributionTopBean obj:groupRanks){
            //add
            groupX.setNAME(obj.getGroup());
            groupX.setISSUESCORE(groupX.getISSUESCORE() + obj.getEffectiveIssueCount());
            groupX.setMRSCORE(groupX.getMRSCORE() + obj.getMergedMrCount());
        }

        groupX.setPROJECTSCORES(groupX.getISSUESCORE() + groupX.getMRSCORE());
        //groupX.setPROJECTSCORES(groupX.getISSUESCORE() );

        projectRank.add(groupX);
    }


    public List<ProjectGroups> queryProjectRank(List<ContributionTopBean> szTops){

        List<ProjectGroups> projectRank = new ArrayList<ProjectGroups>();

        Map<String,List> team = this.getTeamSquad();

        for(Map.Entry<String,List> o : team.entrySet()){
            List<ContributionTopBean> rankData = queryGroupRank(szTops,o.getKey().toString());
            calcProjectScore(projectRank,rankData);
        }

        this.sortProject(projectRank);

        return projectRank;
    }

    public List<ContributionTopBean> queryGroupRank(List<ContributionTopBean> szTops,String groupName){


        List<ContributionTopBean> groupTops =  new ArrayList<ContributionTopBean>();

        for(ContributionTopBean member:szTops){
            if(member.getGroup().equals(groupName) ){
                groupTops.add(member);
            }
        }

        return groupTops;

    }
/*
    public List<ContributionTopBean> queryGroupRank(List<ContributionTopBean> szTops,int groupId){


        List<ContributionTopBean> groupTops =  new ArrayList<ContributionTopBean>();

        for(ContributionTopBean member:szTops){
            if(member.getGroup().equals(GROUP_IDENTIFIER.values()[groupId].name()) ){
                groupTops.add(member);
            }
        }

        return groupTops;

    }
*/
    /*
     * 组装五级部门查询条件
     */
    private String getDeptFiveCon(String deptStr){
        String[] deptArr = deptStr.split(",");
        StringBuffer deptCon = new StringBuffer();
        for(String dept : deptArr){
            deptCon.append("'" + dept.trim() + "',");
        }
        if(StringUtil.notNullandEmpty(deptCon.toString())){
            return deptCon.toString().substring(0, deptCon.toString().length() - 1);
        }
        return "''";
    }

    /*
     * 查询个人贡献榜数据列表（多条SQL查询然后拼装数据）
     */
    public List<ContributionTopBean> queryContributionTopList(Map<String, String> param) {

        List<Map<String, Object>> listUser = queryUserInfoList(param);
        List<Map<String, Object>> listOverLineCount = queryOverLineCount(param);
        List<Map<String, Object>> listMR = queryMR(param);
        List<Map<String, Object>> listIssue = queryIssue(param);
        List<Map<String, Object>> listSelfNoteMr = querySelfNoteMr(param);
        List<Map<String, Object>> listOtherNoteMr = queryOtherNoteMr(param);
        List<Map<String, Object>> listMrCount = queryMrCount(param);
        List<Map<String, Object>> listMrReplyCount = queryMrReplyCount(param);
        List<Map<String, Object>> listReviewIssue = queryReviewIssue(param);

        for(Map<String, Object> map:listUser){
            String authorId = StringUtil.getStringFromObject(map.get("authorId"));
            map.put("overLineCount", 0);
            map.put("mrTotalCount", 0);
            map.put("mrRejected", 0);
            map.put("mergedMrCount", 0);
            map.put("nHandleMRCount", 0);
            map.put("issueTotalCount", 0);
            map.put("effectiveIssueCount", 0);
            map.put("effectiveIssueCountBySelf", 0);
            map.put("closeIssueCount", 0);
            map.put("beyondIssue", 0);
            map.put("noteTotalCount", 0);
            map.put("noteTotalOtherCount", 0);
            map.put("mrCommonCount", 0);
            map.put("mrCommonReplyCount", 0);
            map.put("resposbilityIssueCountBySelf", 0);
            map.put("resposbilityIssueCount", 0);
            map.put("beiCloseIssueCount", 0);
            map.put("unBeiCloseIssueCount", 0);
            map.put("beiBeyondIssue", 0);
            //map.put("group",MapUtils.getString())
            for (Map<String, Object> overLineCount : listOverLineCount) {
                if(authorId.trim().equals(overLineCount.get("authorId"))){
                    map.put("overLineCount", MapUtils.getInteger(overLineCount, "overLineCount", 0));
                    break;
                }
            }

            for (Map<String, Object> mr : listMR) {
                if(authorId.trim().equals(mr.get("authorId"))){
                    map.put("mrTotalCount", MapUtils.getInteger(mr, "mrTotalCount", 0));
                    map.put("mrRejected", MapUtils.getInteger(mr, "mrRejected", 0));
                    map.put("mergedMrCount", MapUtils.getInteger(mr, "mergedMrCount", 0));
                    map.put("nHandleMRCount", MapUtils.getInteger(mr, "nHandleMRCount", 0));
                    break;
                }
            }

            for (Map<String, Object> issue : listIssue) {
                if(authorId.trim().equals(issue.get("authorId"))){
                    map.put("issueTotalCount", MapUtils.getInteger(issue, "issueTotalCount", 0));
                    map.put("effectiveIssueCount", MapUtils.getInteger(issue, "effectiveIssueCount", 0));
                    map.put("effectiveIssueCountBySelf", MapUtils.getInteger(issue, "effectiveIssueCountBySelf", 0));
                    map.put("closeIssueCount", MapUtils.getInteger(issue, "closeIssueCount", 0));
                    map.put("beyondIssue", MapUtils.getInteger(issue, "beyondIssue", 0));
                    break;
                }
            }

            for (Map<String, Object> selfNoteMr : listSelfNoteMr) {
                if(authorId.trim().equals(selfNoteMr.get("authorId"))){
                    map.put("noteTotalCount", MapUtils.getInteger(selfNoteMr, "noteTotalCount", 0));
                    break;
                }
            }

            for (Map<String, Object> otherNoteMr : listOtherNoteMr) {
                if(authorId.trim().equals(otherNoteMr.get("authorId"))){
                    map.put("noteTotalOtherCount", MapUtils.getInteger(otherNoteMr, "noteTotalOtherCount", 0));
                    break;
                }
            }

            for (Map<String, Object> mrCount : listMrCount) {
                if(authorId.trim().equals(mrCount.get("authorId"))){
                    map.put("mrCommonCount", MapUtils.getInteger(mrCount, "mrCommonCount", 0));
                    break;
                }
            }

            for (Map<String, Object> mrReplyCount : listMrReplyCount) {
                if(authorId.trim().equals(mrReplyCount.get("authorId"))){
                    map.put("mrCommonReplyCount", MapUtils.getInteger(mrReplyCount, "mrCommonReplyCount", 0));
                    break;
                }
            }

            for (Map<String, Object> reviewIssue : listReviewIssue) {
                if(authorId.trim().equals(reviewIssue.get("authorId"))){
                    map.put("resposbilityIssueCountBySelf", MapUtils.getInteger(reviewIssue, "resposbilityIssueCountBySelf", 0));
                    map.put("resposbilityIssueCount", MapUtils.getInteger(reviewIssue, "resposbilityIssueCount", 0));
                    map.put("beiCloseIssueCount", MapUtils.getInteger(reviewIssue, "beiCloseIssueCount", 0));
                    map.put("unBeiCloseIssueCount", MapUtils.getInteger(reviewIssue, "unBeiCloseIssueCount", 0));
                    map.put("beiBeyondIssue", MapUtils.getInteger(reviewIssue, "beiBeyondIssue", 0));
                    break;
                }
            }
        }

        List<ContributionTopBean> beanList = new ArrayList<ContributionTopBean>();
        if(null != listUser && listUser.size() > 0){
            for (Map<String, Object> map : listUser) {
                ContributionTopBean contributionTopBean = new ContributionTopBean();
                int effectiveIssueCount = StringUtil.getIntFromObject(map.get("effectiveIssueCount"));
                int noteTotalOtherCount = StringUtil.getIntFromObject(map.get("noteTotalOtherCount"));
                int mergedMrCount = StringUtil.getIntFromObject(map.get("mergedMrCount"));
                int mergedMrCountScore = 0;

                if(mergedMrCount>10){
                    mergedMrCountScore = mergedMrCount/10;
                }
                int personalScore = effectiveIssueCount*3+noteTotalOtherCount*2+mergedMrCountScore;


                contributionTopBean.setName(StringUtil.getStringFromObject(map.get("name")));
                contributionTopBean.setEmployNum(StringUtil.getStringFromObject(map.get("employNum")));
                contributionTopBean.setDepartmentFour(StringUtil.getStringFromObject(map.get("departmentFour")));
                contributionTopBean.setDepartmentFive(StringUtil.getStringFromObject(map.get("departmentFive")));
                contributionTopBean.setRole(StringUtil.getStringFromObject(map.get("role")));
                contributionTopBean.setPosition(StringUtil.getStringFromObject(map.get("position")));
                contributionTopBean.setIssueTotalCount(StringUtil.getIntFromObject(map.get("issueTotalCount")));
                contributionTopBean.setEffectiveIssueCount(effectiveIssueCount);
                contributionTopBean.setEffectiveIssueCountBySelf(StringUtil.getIntFromObject(map.get("effectiveIssueCountBySelf")));
                contributionTopBean.setCloseIssueCount(StringUtil.getIntFromObject(map.get("closeIssueCount")));
                contributionTopBean.setBeyondIssue(StringUtil.getIntFromObject(map.get("beyondIssue")));
                contributionTopBean.setNoteTotalCount(StringUtil.getIntFromObject(map.get("noteTotalCount")));
                contributionTopBean.setNoteTotalOtherCount(noteTotalOtherCount);
                contributionTopBean.setHandleMRCount(StringUtil.getIntFromObject(map.get("handleMRCount")));
                contributionTopBean.setResposbilityIssueCount(StringUtil.getIntFromObject(map.get("resposbilityIssueCount")));
                contributionTopBean.setResposbilityIssueCountBySelf(StringUtil.getIntFromObject(map.get("resposbilityIssueCountBySelf")));
                contributionTopBean.setBeiCloseIssueCount(StringUtil.getIntFromObject(map.get("beiCloseIssueCount")));
                contributionTopBean.setUnBeiCloseIssueCount(StringUtil.getIntFromObject(map.get("unBeiCloseIssueCount")));
                contributionTopBean.setBeiBeyondIssue(StringUtil.getIntFromObject(map.get("beiBeyondIssue")));
                contributionTopBean.setMrCommonCount(StringUtil.getIntFromObject(map.get("mrCommonCount")));
                contributionTopBean.setMrCommonReplyCount(StringUtil.getIntFromObject(map.get("mrCommonReplyCount")));
                contributionTopBean.setMrTotalCount(StringUtil.getIntFromObject(map.get("mrTotalCount")));
                contributionTopBean.setMergedMrCount(mergedMrCount);
                contributionTopBean.setUnHandleMRCount(StringUtil.getIntFromObject(map.get("unHandleMRCount")));
                contributionTopBean.setMrRejected(StringUtil.getIntFromObject(map.get("mrRejected")));
                contributionTopBean.setOverLineCount(StringUtil.getIntFromObject(map.get("overLineCount")));
                contributionTopBean.setTotalContribution(StringUtil.getIntFromObject(map.get("totalContribution")));
                contributionTopBean.setScore(StringUtil.getIntFromObject(map.get("score")));
                contributionTopBean.setPersonalScore(personalScore);
                contributionTopBean.setGroup(StringUtil.getStringFromObject(map.get("group")));
                beanList.add(contributionTopBean);
            }
            String querySql = "select DomainId from T_ISOURCE_Committer tt";
            List<Object> employNumList = topDao.createSqlQuery(querySql).list();
            for(ContributionTopBean contributionTopBean : beanList){
                boolean flag = false;
                for(Object employNum : employNumList){
                    if(contributionTopBean.getEmployNum().equalsIgnoreCase(employNum.toString())){
                        flag = true;
                    }
                }
                if(flag){
                    contributionTopBean.setPosition("COMMITTER");
                }
            }
        }
        this.sortByCondition(beanList,param);
        return beanList;
    }


    /**
     * @Description:根据积分排序
     * @author:jack.deng00190167
     * @time:2017年6月12日 上午11:03:14
     * @param @param beanList
     * @return:void
     * @throws
     */
    private void sortByCondition(List<ContributionTopBean> beanList,Map<String,String> param) {

          String condition = param.get("sort");

          if(condition.equals("personalScore")){
              sort(beanList);
          }else if(condition.equals("mergedMrCount")){
              sortByHandledMr(beanList);
          }else if(condition.equals("effectiveIssueCount")){
              sortByEffectiveIssue(beanList);
          }else{
              //default
              sort(beanList);
          }

          return;

    }

    /**
     * @Description:根据积分排序
     * @author:jack.deng00190167
     * @time:2017年6月12日 上午11:03:14
     * @param @param beanList
     * @return:void
     * @throws
     */
    private void sort(List<ContributionTopBean> beanList) {
        Collections.sort(beanList,new Comparator<ContributionTopBean>() {
            @Override
            public int compare(ContributionTopBean o1, ContributionTopBean o2) {
                if(o1.getPersonalScore()<o2.getPersonalScore()){
                    return 1;
                }else if(o1.getPersonalScore()>o2.getPersonalScore()){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
    }


    /**
     * @Description:根据成员的有效ISSUE排序
     * @author:jack.deng00190167
     * @time:2017年6月12日 上午11:03:14
     * @param @param beanList
     * @return:void
     * @throws
     */
    private void sortByEffectiveIssue(List<ContributionTopBean> beanList) {
        Collections.sort(beanList,new Comparator<ContributionTopBean>() {
            @Override
            public int compare(ContributionTopBean o1, ContributionTopBean o2) {
                if(o1.getEffectiveIssueCount()<o2.getEffectiveIssueCount()){
                    return 1;
                }else if(o1.getEffectiveIssueCount()>o2.getEffectiveIssueCount()){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
    }

    /**
     * @Description:根据处理过的MR排序排序
     * @author:jack.deng00190167
     * @time:2017年6月12日 上午11:03:14
     * @param @param beanList
     * @return:void
     * @throws
     */
    private void sortByHandledMr(List<ContributionTopBean> beanList) {
        Collections.sort(beanList,new Comparator<ContributionTopBean>() {
            @Override
            public int compare(ContributionTopBean o1, ContributionTopBean o2) {
                if(o1.getMergedMrCount()<o2.getMergedMrCount()){
                    return 1;
                }else if(o1.getMergedMrCount()>o2.getMergedMrCount()){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
    }

    /**
     * @Description:对项目组进行积分排序
     * @author:dengwenbin
     * @time:2017年7月17日
     * @param @param beanList
     * @return:void
     * @throws
     */
    private void sortProject(List<ProjectGroups> beanList) {
        Collections.sort(beanList,new Comparator<ProjectGroups>() {
            public int compare(ProjectGroups o1, ProjectGroups o2) {
                if(o1.getPROJECTSCORES()<o2.getPROJECTSCORES()){
                    return 1;
                }else if(o1.getPROJECTSCORES()>o2.getPROJECTSCORES()){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
    }

    /**
     * @Description:获取组别编号
     * @author:dengwenbin
     * @time:2017年7月17日
     * @param @param beanList
     * @return:void
     * @throws
     */

    private String getGroupName(String name){

        //String[] QosGroup = qosList.split(",");
        //String[] CxbGroup = cxbList.split(",");
        //String[] FrmGroup = frmList.split(",");

        Map<String,List> squad = this.getTeamSquad();

        for (Map.Entry entry : squad.entrySet()){

            //System.out.println(entry.getValue().toString());

            if(entry.getValue().toString().contains(name)){
                return entry.getKey().toString();
            }else{
                continue;
            }
        }

        return null;
    }

    /**
     * @Description:人员信息
     * @author:jack.deng00190167
     * @time:2017年6月9日 下午2:28:25
     * @param @param param
     * @param @return
     * @return:List<Map<String,Object>>
     * @throws
     */
    private List<Map<String, Object>> queryUserInfoList(Map<String, String> param) {
        String yearMonth = DateUtil.N() + "-" + DateUtil.M();
        String deptThree = param.get("deptThree");
        String deptFour = param.get("deptFour");
        String deptFive = param.get("deptFive");
        String staffType = param.get("staffType");
        String role = param.get("role");
        String position = param.get("position");
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("   SELECT ")
                .append("		   tdu.id,")
                .append("		   tsu.name,")
                .append("		   tsu.YU_NAME employNum,")
                .append("		   tsu.DEPARTMENT_FOUR departmentFour,")
                .append("		   tsu.DEPARTMENT_FIVE departmentFive,")
                .append("		   tsu.CODE role,")
                .append("		   tsu.ROLE position,")
                .append(" 		   ISNULL(tdc.coin,0) score,")
                .append("		   ISNULL(tdc.experice_point,0) totalContribution")
                .append("     FROM T_SFM_USER tsu ")
                .append("LEFT JOIN T_DuIsource_users tdu ON tsu.EMPLOY_ID = SUBSTRING (tdu.extern_uid,2,len(tdu.extern_uid)) ")
                .append("LEFT JOIN T_DuIsource_Coins tdc ON tdu.ID = tdc.user_id")
                .append("    WHERE tsu.time='"+yearMonth+"'")
                .append("      AND tsu.DEPARTMENT_THREE='"+deptThree+"'")
                .append(" AND tsu.ISHUAWEI='" + staffType + "'")
                .append(" AND tsu.CODE='" + role + "'");
                //.append(" AND tsu.ROLE='" + position + "'")

        //some hackings here
        if( deptFour != null  ){
            sqlBuffer.append(" AND tsu.DEPARTMENT_FOUR = '"+deptFour+"'");
        }

        if(  deptFive != null  ){
            sqlBuffer.append(" AND tsu.DEPARTMENT_FIVE = '"+deptFive+"'");
        }


        List<Object> list = topDao.createSqlQuery(sqlBuffer.toString()).list();
        List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
        for (Object object : list) {
            Object[] obj = (Object[])object;
            String name = StringUtil.getStringFromObject(obj[1]);

            //group id hackings
            String groupName = this.getGroupName(name);
            if( groupName == null && deptFour != null && deptFive != null)
            {
                continue;
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("authorId", StringUtil.getStringFromObject(obj[0]));
            map.put("name", name);
            map.put("employNum", StringUtil.getStringFromObject(obj[2]));
            map.put("departmentFour", StringUtil.getStringFromObject(obj[3]));
            map.put("departmentFive", StringUtil.getStringFromObject(obj[4]));
            map.put("role", StringUtil.getStringFromObject(obj[5]));
            map.put("position", StringUtil.getStringFromObject(obj[6]));
            map.put("score", StringUtil.getStringFromObject(obj[7]));
            map.put("totalContribution", StringUtil.getStringFromObject(obj[8]));
            map.put("group",groupName);

            listMap.add(map);
        }
        return listMap;
    }

    /**
     * @Description:代码超出范围的MR数
     * @author:jack.deng00190167
     * @time:2017年6月8日 下午3:04:22
     * @param @param param
     * @param @return
     * @return:List<Map<String,String>>
     * @throws
     */
    public List<Map<String, Object>> queryOverLineCount(Map<String, String> param){
        String startTime = param.get("startTime");
        String endTime = param.get("endTime");
        String lineNum = param.get("lineNum");

        StringBuffer sql = new StringBuffer()
                .append(" SELECT mr.author_id, ")
                .append(" SUM (CASE WHEN mrtl.add_lines + mrtl.del_lines > "+lineNum+" THEN 1 ELSE 0 END) overLineCount ")
                .append(" FROM T_DuIsource_Merge_Requests mr ")
                .append(" LEFT JOIN T_DuIsource_MergeRequestTotalLines mrtl ")
                .append(" ON mr.id = mrtl.merge_request_id ")
                .append(" AND mr.created_at > CAST ('"+startTime+"' AS datetime) ")
                .append(" AND mr.created_at < CAST ('"+endTime+"' AS datetime) ")
                .append(" GROUP BY mr.author_id ");

        List<Object> list = topDao.createSqlQuery(sql.toString()).list();
        List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
        for (Object object : list) {
            Object[] obj = (Object[])object;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("authorId", StringUtil.getStringFromObject(obj[0]));
            map.put("overLineCount", StringUtil.getStringFromObject(obj[1]));
            listMap.add(map);
        }
        return listMap;
    }

    /**
     * @Description:发起MR数、合入MR数、待处理MR数、被驳回MR数
     * @author:jack.deng00190167
     * @time:2017年6月8日 下午3:48:37
     * @param @param param
     * @param @return
     * @return:List<Map<String,String>>
     * @throws
     */
    public List<Map<String,Object>> queryMR(Map<String, String> param){
        String startTime = param.get("startTime");
        String endTime = param.get("endTime");

        StringBuffer sql = new StringBuffer()
                .append(" SELECT  mr.author_id,")
                .append(" count(1) mrTotalCount,")
                .append(" SUM (CASE WHEN mr.closed = 1 AND mr.merged = 0 THEN 1 ELSE 0 END) mrRejected, ")
                .append(" SUM (CASE WHEN mr.merged = 1 THEN 1 ELSE 0 END) mergedMrCount, ")
                .append(" SUM (CASE WHEN mr.merged = 0 AND mr.closed = 0 THEN 1 ELSE 0 END) unHandleMRCount ")
                //.append(" SUM (CASE WHEN mr.closed = 1 OR mr.merged = 1 THEN 1 ELSE 0 END) nHandleMRCount")
                .append(" FROM T_DuIsource_Merge_Requests mr")
                .append(" WHERE mr.created_at > CAST ('"+startTime+"' AS datetime)")
                .append(" AND mr.created_at < CAST ('"+endTime+"' AS datetime)")
                .append(" GROUP BY mr.author_id");

        List<Object> list = topDao.createSqlQuery(sql.toString()).list();
        List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
        for (Object object : list) {
            Object[] obj = (Object[])object;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("authorId", StringUtil.getStringFromObject(obj[0]));
            map.put("mrTotalCount", StringUtil.getStringFromObject(obj[1]));
            map.put("mrRejected", StringUtil.getStringFromObject(obj[2]));
            map.put("mergedMrCount", StringUtil.getStringFromObject(obj[3]));
            map.put("unHandleMRCount", StringUtil.getStringFromObject(obj[4]));
            //map.put("nHandleMRCount", StringUtil.getStringFromObject(obj[5]));
            listMap.add(map);
        }
        return listMap;
    }

    /**
     * @Description:issue总数、有效issue数、闭环issue数、超期issue数
     * @author:jack.deng00190167
     * @time:2017年6月8日 下午4:38:43
     * @param @param param
     * @param @return
     * @return:List<Map<String,String>>
     * @throws
     */
    public List<Map<String, Object>> queryIssue(Map<String, String> param){
        String startTime = param.get("startTime");
        String endTime = param.get("endTime");

        StringBuffer sql = new StringBuffer()
                .append(" SELECT tdi.author_id, ")
                .append(" COUNT (1) issueTotalCount,")
                .append(" SUM(CASE WHEN tdi.state NOT IN ('new', 'rejected') and tdi.assignee_id !=tdi.author_id THEN 1 ELSE 0 END) effectiveIssueCount,")
                .append(" SUM(CASE WHEN tdi.state NOT IN ('new', 'rejected') and tdi.assignee_id =tdi.author_id THEN 1 ELSE 0 END) effectiveIssueCountBySelf,")
                .append(" SUM(CASE WHEN tdi.state NOT IN ('new', 'rejected') AND tdi.closed = 1 THEN 1 ELSE 0 END) closeIssueCount,  ")
                .append(" SUM(CASE WHEN tdi.closed=0 and tdi.created_at < '"+ DateUtil.obtainOverTime(DateUtil.CurrentTime(), -7)+"' then 1 else 0 end) as beyondIssue ")
                .append(" FROM T_DuIsource_issue tdi")
                .append(" WHERE tdi.created_at > CAST ('"+startTime+"' AS datetime)")
                .append(" AND tdi.created_at < CAST ('"+endTime+"' AS datetime)")
                .append(" GROUP BY tdi.author_id ");

        List<Object> list = topDao.createSqlQuery(sql.toString()).list();
        List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
        for (Object object : list) {
            Object[] obj = (Object[])object;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("authorId", StringUtil.getStringFromObject(obj[0]));
            map.put("issueTotalCount", StringUtil.getStringFromObject(obj[1]));
            map.put("effectiveIssueCount", StringUtil.getStringFromObject(obj[2]));
            map.put("effectiveIssueCountBySelf", StringUtil.getStringFromObject(obj[3]));
            map.put("closeIssueCount", StringUtil.getStringFromObject(obj[4]));
            map.put("beyondIssue", StringUtil.getStringFromObject(obj[5]));
            listMap.add(map);
        }
        return listMap;
    }

    /**
     * @Description:MR自评数
     * @author:jack.deng00190167
     * @time:2017年6月8日 下午4:45:43
     * @param @param param
     * @param @return
     * @return:List<Map<String,String>>
     * @throws
     */
    public List<Map<String,Object>> querySelfNoteMr(Map<String, String> param){
        String startTime = param.get("startTime");
        String endTime = param.get("endTime");
        StringBuffer sql = new StringBuffer()
                .append(" SELECT tdr.author_id,")
                .append(" COUNT(1) noteTotalCount")
                .append(" FROM T_DuIsource_Note_Merge_Requests tdr ")
                .append(" LEFT JOIN T_DuIsource_Merge_Requests tdmr ")
                .append(" ON tdr.merge_request_id=tdmr.id ")
                .append(" WHERE tdr.created_at > CAST ('"+startTime+"' AS datetime)")
                .append(" AND tdr.created_at < CAST ('"+endTime+"' AS datetime) ")
                .append(" AND tdr.author_id=tdmr.author_id ")
                .append(" GROUP BY tdr.author_id ");

        List<Object> list = topDao.createSqlQuery(sql.toString()).list();
        List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
        for (Object object : list) {
            Object[] obj = (Object[])object;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("authorId", StringUtil.getStringFromObject(obj[0]));
            map.put("noteTotalCount", StringUtil.getStringFromObject(obj[1]));
            listMap.add(map);
        }
        return listMap;
    }

    /**
     * @Description:MR评论数
     * @author:jack.deng00190167
     * @time:2017年6月8日 下午5:29:52
     * @param @param param
     * @param @return
     * @return:List<Map<String,String>>
     * @throws
     */
    public List<Map<String, Object>> queryOtherNoteMr(Map<String, String> param){
        String startTime = param.get("startTime");
        String endTime = param.get("endTime");
        StringBuffer sql = new StringBuffer()
                .append("SELECT tdr.author_id, ")
                .append(" COUNT(1) noteTotalOtherCount ")
                .append(" FROM T_DuIsource_Note_Merge_Requests tdr ")
                .append(" LEFT JOIN T_DuIsource_Merge_Requests tdmr ")
                .append(" ON tdr.merge_request_id=tdmr.id ")
                .append(" WHERE tdr.created_at > CAST ('"+startTime+"' AS datetime)")
                .append(" AND tdr.created_at < CAST ('"+endTime+"' AS datetime)")
                .append(" AND tdr.author_id!=tdmr.author_id ")
                .append(" GROUP BY tdr.author_id ");

        List<Object> list = topDao.createSqlQuery(sql.toString()).list();
        List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
        for (Object object : list) {
            Object[] obj = (Object[])object;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("authorId", StringUtil.getStringFromObject(obj[0]));
            map.put("noteTotalOtherCount", StringUtil.getStringFromObject(obj[1]));
            listMap.add(map);
        }
        return listMap;
    }

    /**
     * @Description:发起MR被评论数
     * @author:jack.deng00190167
     * @time:2017年6月9日 上午9:26:15
     * @param @param param
     * @param @return
     * @return:List<Map<String,String>>
     * @throws
     */
    public List<Map<String, Object>> queryMrCount(Map<String, String> param){
        String startTime = param.get("startTime");
        String endTime = param.get("endTime");
        StringBuffer sql = new StringBuffer()
                .append("SELECT tdr.author_id,")
                .append("SUM (CASE WHEN tdnr.id IS NOT NULL THEN 1 ELSE 0 END) mrCommonCount ")
                .append("FROM T_DuIsource_Merge_Requests tdr ")
                .append("LEFT JOIN T_DuIsource_Note_Merge_Requests tdnr ON tdr.id = tdnr.merge_request_id AND tdr.author_id != tdnr.author_id ")
                .append("AND tdr.created_at > CAST ('"+startTime+"' AS datetime)")
                .append("AND tdr.created_at < CAST ('"+endTime+"' AS datetime)")
                .append("GROUP BY tdr.author_id ");

        List<Object> list = topDao.createSqlQuery(sql.toString()).list();
        List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
        for (Object object : list) {
            Object[] obj = (Object[])object;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("authorId", StringUtil.getStringFromObject(obj[0]));
            map.put("mrCommonCount", StringUtil.getStringFromObject(obj[1]));
            listMap.add(map);
        }
        return listMap;
    }

    /**
     * @Description:MR被评论回复数
     * @author:jack.deng00190167
     * @time:2017年6月9日 上午9:47:04
     * @param @param param
     * @param @return
     * @return:List<Map<String,String>>
     * @throws
     */
    public List<Map<String, Object>> queryMrReplyCount(Map<String, String> param){
        String startTime = param.get("startTime");
        String endTime = param.get("endTime");

        StringBuffer sql = new StringBuffer()
                .append(" SELECT tdr.author_id, ")
                .append(" SUM (CASE WHEN tdnr.id IS NOT NULL THEN 1 ELSE 0 END) mrCommonReplyCount ")
                .append(" FROM T_DuIsource_Merge_Requests tdr ")
                .append(" LEFT JOIN T_DuIsource_Note_Merge_Requests tdnr ON tdr.id = tdnr.merge_request_id AND tdr.author_id = tdnr.author_id ")
                .append(" AND tdr.created_at > CAST ('"+startTime+"' AS datetime) ")
                .append(" AND tdr.created_at < CAST ('"+endTime+"' AS datetime) ")
                .append(" GROUP BY tdr.author_id ");

        List<Object> list = topDao.createSqlQuery(sql.toString()).list();
        List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
        for (Object object : list) {
            Object[] obj = (Object[])object;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("authorId", StringUtil.getStringFromObject(obj[0]));
            map.put("mrCommonReplyCount", StringUtil.getStringFromObject(obj[1]));
            listMap.add(map);
        }
        return listMap;
    }

    /**
     * @Description:责任Issue总数、闭环Issue数、未闭环Issue数、超期Issue数
     * @author:jack.deng00190167
     * @time:2017年6月9日 上午9:55:37
     * @param @param param
     * @param @return
     * @return:List<Map<String,String>>
     * @throws
     */
    public List<Map<String, Object>> queryReviewIssue(Map<String, String> param){
        String startTime = param.get("startTime");
        String endTime = param.get("endTime");

        StringBuffer sql = new StringBuffer()
                .append(" SELECT tdu.ID,")
                .append(" SUM (CASE WHEN  tdu.id = tdi.author_id THEN 1 ELSE	0 END	) resposbilityIssueCountBySelf, ")
                .append(" SUM (CASE WHEN  tdu.id != tdi.author_id THEN 1 ELSE	0 END) resposbilityIssueCount, ")
                .append(" SUM (CASE WHEN tdi.closed = 1 THEN 1 ELSE 0 END ) beiCloseIssueCount,")
                .append(" SUM (CASE WHEN tdi.closed = 0 THEN 1 ELSE 0 END ) unBeiCloseIssueCount, ")
                .append(" SUM(CASE WHEN tdi.closed=0 and tdi.created_at < '"+ DateUtil.obtainOverTime(DateUtil.CurrentTime(), -7)+"' then 1 else 0 end) as beiBeyondIssue ")
                .append(" FROM T_DuIsource_users tdu ")
                .append(" LEFT JOIN T_DuIsource_issue tdi ON tdu.id=tdi.assignee_id ")
                .append(" where tdi.created_at > CAST ('"+startTime+"' AS datetime)")
                .append(" AND tdi.created_at < CAST ('"+endTime+"' AS datetime)")
                .append(" GROUP BY tdu.ID ");

        List<Object> list = topDao.createSqlQuery(sql.toString()).list();
        List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
        for (Object object : list) {
            Object[] obj = (Object[])object;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("authorId", StringUtil.getStringFromObject(obj[0]));
            map.put("resposbilityIssueCountBySelf", StringUtil.getStringFromObject(obj[1]));
            map.put("resposbilityIssueCount", StringUtil.getStringFromObject(obj[2]));
            map.put("beiCloseIssueCount", StringUtil.getStringFromObject(obj[3]));
            map.put("unBeiCloseIssueCount", StringUtil.getStringFromObject(obj[4]));
            map.put("beiBeyondIssue", StringUtil.getStringFromObject(obj[5]));
            listMap.add(map);
        }
        return listMap;
    }

    /**
     * 查询四级部门
     * @Description:TODO
     * @author:jack.deng.00190167
     * @time:2016年7月20日 下午5:24:41
     * @param @param time
     * @param @return
     * @return:List<Object>
     * @throws
     */
    public List<Object> getDeptFour(String deptThree){
        String time = DateUtil.N() + "-" + DateUtil.M();
        StringBuffer sql = new StringBuffer("");
        sql.append("select distinct t.DEPARTMENT_FOUR from T_SFM_USER t");
        sql.append(" where t.time='"+time+"' and t.DEPARTMENT_THREE='"+deptThree+"' and t.DEPARTMENT_FOUR<>''");
        String finalsql = sql.toString();
        Query query = topDao.createSqlQuery(finalsql);
        List<Object> departsList = query.list();
        return departsList;
    }

    public List<Object> getDeptThree(String uid) {
        String time = DateUtil.N() + "-" + DateUtil.M();
        List<Object> params = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer("");
        sql.append(" select distinct t.DEPARTMENT_THREE from T_SFM_USER t");
        sql.append(" order by t.DEPARTMENT_THREE");
        String finalsql = sql.toString();
        Query query = topDao.createSqlQuery(finalsql,params.toArray());
        List<Object> departsList = query.list();
        return departsList;
    }

    /**
     * 获取特殊权限用户
     * @author:lwx444553
     * @time:2017年5月23日
     * @return:List<Object>
     */
    @SuppressWarnings("unchecked")
    public List<Object> getSpecUsr(){
        StringBuffer sql = new StringBuffer("");
        sql.append(" select distinct YU_NAME from T_SFM_SP_USER  where VALID_STA = 'T' ");//VALID_STA 表示状态：T：有效 F:无效
        Query query = topDao.createSqlQuery(sql.toString());
        List<Object> specUsrs = query.list();
        return specUsrs;
    }

    public List<Object> getDeptFive(String deptThree, String deptFour) {
        String time = DateUtil.N() + "-" + DateUtil.M();
        StringBuffer sql = new StringBuffer("");
        sql.append("select distinct t.DEPARTMENT_FIVE from T_SFM_USER t");
        sql.append(" where t.time='"+time+"' and t.DEPARTMENT_THREE='"+deptThree+"' and t.DEPARTMENT_FOUR='"+deptFour+"' and t.DEPARTMENT_FIVE<>''");
        String finalsql = sql.toString();
        Query query = topDao.createSqlQuery(finalsql);
        List<Object> departsList = query.list();
        return departsList;
    }

    public List<Object> queryRoleInfoList() {
        String querySql = "select distinct role from T_SFM_USER";
        Query query = topDao.createSqlQuery(querySql);
        List<Object> departsList = query.list();
        return departsList;
    }


    /**
     * @Description:添加项目并根据项目id和版本id去重
     * @author:sWX329996
     * @time:2016年10月10日 上午9:46:06
     * @param @param proId
     * @param @param proName
     * @param @param orgId
     * @param @param parentId
     * @return:void
     * @throws
     */
    private void addPro(String proId, String proName, String orgId,String parentId) {
        StringBuffer querySql = new StringBuffer();
        querySql.append("select name from T_DUISOURCE_PROJECT tt where tt.id='"+proId+"' and tt.versionId = '"+parentId+"' "  );
        List<Object> projects = topDao.createSqlQuery(querySql.toString()).list();
        //根据id判断是否存在，如果不存在则新增
        if(null != projects && (projects.size() > 0)){
            return;
        }else{
            /*
            DuIsourceProject proBean = new DuIsourceProject();
            proBean.setId(Integer.parseInt(proId));
            proBean.setName(proName);
            proBean.setOrgId(Integer.parseInt(orgId));
            proBean.setVersionId(Integer.parseInt(parentId));
            topDao.saveAndCommit(proBean);
            */
        }
    }
    /**
     * @Description:根据组织名称去Isource中查询组织ID
     * @author:sWX329996
     * @time:2016年10月10日 上午10:19:59
     * @param @param orgName
     * @param @return
     * @return:List<Map<String,Object>>
     * @throws
     */
    private List<Map<String, Object>> getOrgByName(String orgName){
        StringBuffer querySql = new StringBuffer();
        querySql.append("select o.id,o.name from organizations o where o.name = '"+orgName+"'");
        List<Map<String, Object>> orgs = neJdbcTemplate.queryForList(querySql.toString());
        return orgs;
    }
    /**
     * @Description:根据组织Id和项目名称去Isource中查询项目ID
     * @author:sWX329996
     * @time:2016年10月10日 上午10:21:30
     * @param @param proName
     * @param @param orgId
     * @param @return
     * @return:List<Map<String,Object>>
     * @throws
     */
    private List<Map<String, Object>> getProByNameAndOrgId(String proName, String orgId){
        StringBuffer querySql = new StringBuffer();
        querySql.append("select p.id,p.name from projects p where p.name = '"+proName+"' and p.organization_id = '"+orgId+"'");
        List<Map<String, Object>> pros = neJdbcTemplate.queryForList(querySql.toString());
        return pros;
    }


    public List<String> querySendMailUsers(List<ContributionTopBean> topsz) {

        List<String> toUserEmails = new ArrayList<String>(topsz.size());

        for(ContributionTopBean top : topsz){
            toUserEmails.add(top.getEmployNum()+"@notesmail.huawei.com");
        }
        /*
        //查询当前月份的人员id
        String deptFive = "模型算法开发部";

        StringBuffer sql_userIds=new StringBuffer();
        sql_userIds.append(" select tt.id,tt.YU_NAME,tt.DEPARTMENT_THREE,tt.DEPARTMENT_FOUR,tt.DEPARTMENT_FIVE,tt.FULL_NAME ");
        sql_userIds.append(" from T_Duisource_SendMailManagement s LEFT JOIN( ");
        sql_userIds.append(" select t.YU_NAME,t.DEPARTMENT_THREE,t.DEPARTMENT_FOUR,t.DEPARTMENT_FIVE,u.id,t.FULL_NAME from ");
        sql_userIds.append(" (select u.YU_NAME,u.DEPARTMENT_THREE,u.DEPARTMENT_FOUR,u.DEPARTMENT_FIVE,u.FULL_NAME from T_sfm_user u ");
        sql_userIds.append(" where u.[TIME] = '"+new SimpleDateFormat("yyyy-MM").format(new Date()) + "' ");
//		sql_userIds.append(" and u.YU_NAME in ('w00406818')");
        sql_userIds.append(" ) as t LEFT JOIN T_DuIsource_users u on  t.YU_NAME = u.username) as tt  ");
        sql_userIds.append(" ON tt.[DEPARTMENT_FIVE] = '"+deptFive+"' and tt.id is not null ");
        List<Object> userIdList = topDao.createSqlQuery(sql_userIds.toString()).list();*/

        //filters....

        return toUserEmails;
    }


    public String queryMailAddressByUserId(String userName) {

        //cannot read from #users# table of duIsource database, users is a table of xdu not isource
        //so, we concate notemail string here.

        return userName+"@notesmail.huawei.com.cn";

        /*
        String mailAddress = "";
        try {
            StringBuffer sql_queryMail = new StringBuffer().append(" SELECT u.email from users u where u.id = ").append(uid);
            List<Object> list = topDao.createSqlQuery(sql_queryMail.toString()).list();
            if (null != list && list.size() > 0) {
                //mailAddress = list.get(0).
            }
        } catch (Exception e) {
            LOGGER.error(e+"queryEmail error : userId="+uid);
        }
        return mailAddress;*/
    }

    //currently useless....if you want add more column data ..could use this
    private Map<String,Integer> getValidIssueMap(Map<String,Integer> data){
        if(data != null ){
            return data;
        }else
        {
            data.put("a",0);
            data.put("b",0);
            data.put("effectiveIssueNum",0);

        }

        return data;
    }


    public  String createPersonalRankPng(String pngName, List<ContributionTopBean> rankData){

        Map<String,ArrayList<String>>  data = new HashMap<String,ArrayList<String>>();
        ArrayList<String> nameList = new ArrayList<String>();
        ArrayList<String> scoreList = new ArrayList<String>();

        for (ContributionTopBean o : rankData) {
                 nameList.add(o.getName());
                 scoreList.add(Integer.toString(o.getPersonalScore()));
        }

        data.put("name",nameList);
        data.put("scores",scoreList);

        String chartName = pngName +".png";
        String weekAgoDate = DateUtil.obtainOverTime(DateUtil.getDate(), -7);
        String nextDate = DateUtil.obtainOverTime(DateUtil.getDate(), 0);
        String title = getChartName(pngName);

        Map<String,String> issueColumnData = new HashMap<String,String>();
        issueColumnData.put("chartType","issueColumnChart");
        issueColumnData.put("chartName",chartName);
        issueColumnData.put("chartTitle",title);
        issueColumnData.put("subTitle", weekAgoDate+"~"+nextDate);
        issueColumnData.put("chartData", JSONArray.fromObject(data).toString());

        Map<String,String> hearder = new HashMap<String,String>();
        String result = MapUtils.getString(issueColumnData, "chartData","[]");
        if(!"[]".equals(result)) {
            HttpRestCallApi.callHttpUtf8PostMethod(pngGeneratorUrl, "",null,null, null,hearder, issueColumnData);
        }

        return chartName;
    }

    public  String createProjectRankPng(String pngName, List<ProjectGroups> rankData){

        Map<String,ArrayList<String>>  data = new HashMap<String,ArrayList<String>>();
        ArrayList<String> nameList = new ArrayList<String>();
        ArrayList<String> scoreList = new ArrayList<String>();

        for (ProjectGroups o : rankData) {
            nameList.add(o.getNAME());
            scoreList.add(Integer.toString(o.getPROJECTSCORES()));
        }

        data.put("name",nameList);
        data.put("scores",scoreList);

        String chartName = pngName +".png";
        String weekAgoDate = DateUtil.obtainOverTime(DateUtil.getDate(), -7);
        String nextDate = DateUtil.obtainOverTime(DateUtil.getDate(), 0);
        String title = getChartName(pngName);

        Map<String,String> issueColumnData = new HashMap<String,String>();
        issueColumnData.put("chartType","issueColumnChart");
        issueColumnData.put("chartName",chartName);
        issueColumnData.put("chartTitle",title);
        issueColumnData.put("subTitle", weekAgoDate+"~"+nextDate);
        issueColumnData.put("chartData", JSONArray.fromObject(data).toString());

        Map<String,String> hearder = new HashMap<String,String>();
        String result = MapUtils.getString(issueColumnData, "chartData","[]");
        if(!"[]".equals(result)) {
            HttpRestCallApi.callHttpUtf8PostMethod(pngGeneratorUrl, "",null,null, null,hearder, issueColumnData);
        }

        return chartName;
    }

    private String getChartName(String pngName){

        String chartName = "";

        Map<String,String> nameMapping = new HashMap<String, String>();
        nameMapping.put("qos","QOS&安全组排名");
        nameMapping.put("cxb","子卡OM与整机组排名");
        nameMapping.put("frm","交叉项目组排名");
        nameMapping.put("project","流控深圳团队排名");

        chartName = nameMapping.get(pngName);
        return StringUtil.equalsNull(chartName)?("服务器故障了！以下肯定是来自火星的数据"):(chartName);
    }

    public HibernateDao gettopDao() {
        return topDao;
    }

    public void settopDao(HibernateDao topDao) {
        this.topDao = topDao;
    }

    public JdbcTemplate getNeJdbcTemplate() {
        return neJdbcTemplate;
    }

    public void setNeJdbcTemplate(JdbcTemplate neJdbcTemplate) {
        this.neJdbcTemplate = neJdbcTemplate;
    }

}
