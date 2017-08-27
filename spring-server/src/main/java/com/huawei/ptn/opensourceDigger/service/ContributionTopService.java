package com.huawei.ptn.opensourceDigger.service;

/**
 * Created by d00190167 on 2017/7/7.
 */

import com.huawei.ipm.base.utils.date.DateUtil;
import com.huawei.ptn.opensourceDigger.config.api.HttpRestCallApi;
import com.huawei.ptn.opensourceDigger.model.ContributionTopBean;
import com.huawei.ptn.opensourceDigger.model.ProjectGroups;
import net.sf.json.JSONArray;
import org.apache.commons.collections.MapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 个人贡献榜service层
 * @Title:ContributionTopService.java
 * @Package com.huawei.ipm.duIsourceNew.service
 * @Description:TODO
 * @author:jack.deng.00190167
 * @time:2016年7月14日 下午3:49:19
 */
public interface ContributionTopService {

    public List<ProjectGroups> queryProjectRank(List<ContributionTopBean> szTops);

    public List<ContributionTopBean> queryGroupRank(List<ContributionTopBean> szTops, String groupName);


    public  String createPersonalRankPng(String pngName,List<ContributionTopBean> rankData);

    public  String createProjectRankPng(String pngName,List<ProjectGroups> rankProjectData);


    public Map<String,List> getTeamSquad();


    /**
     * 查询流控深圳地区个人贡献榜数据列表（一条SQL查出结果）
     *
     * @param @param  默认查询QOS
     * @param @return
     * @throws
     * @Description:
     * @author: jack.deng
     * @time:2017年7月10日
     * @return:List<ContributionTopBean>
     */
    public List<ContributionTopBean> queryContributionSZTops();

    /**
     * 查询流控 *指定地区* 地区个人贡献榜数据列表
     *
     * @param @param  参数化查询
     * @param @return
     * @throws
     * @Description:
     * @author: jack.deng
     * @time:2017年7月10日
     * @return:List<ContributionTopBean>
     */
    public List<ContributionTopBean> queryContributionDomainTops(Map<String,List> param, Map<String,String> condition);

    /**
     * 查询个人贡献榜数据列表（一条SQL查出结果）
     *
     * @param @param  param
     * @param @return
     * @throws
     * @Description:TODO
     * @author:jack.deng.00190167
     * @time:2016年7月14日 下午3:53:09
     * @return:List<Map<String,Object>>
     */
    public List<ContributionTopBean> queryContributionTops(Map<String, String> param);

    /**
     * 查询个人贡献榜数据列表（多条SQL查询然后拼装数据）
     *
     * @param @param  param
     * @param @return
     * @throws
     * @Description:TODO
     * @author:jack.deng.00190167
     * @time:2016年7月18日 上午8:58:10
     * @return:List<ContributionTopBean>
     */
    public List<ContributionTopBean> queryContributionTopList(Map<String, String> param);

    /**
     * 查询三级部门
     *
     * @param @param  time
     * @param @return
     * @throws
     * @Description:TODO
     * @author:jack.deng.00190167
     * @time:2016年7月20日 下午5:24:41
     * @return:List<Object>
     */
    public List<Object> getDeptThree(String uid);

    /**
     * 查询四级部门
     *
     * @param @param  time
     * @param @return
     * @throws
     * @Description:TODO
     * @author:jack.deng.00190167
     * @time:2016年7月20日 下午5:24:41
     * @return:List<Object>
     */
    public List<Object> getDeptFour(String deptThree);

    /**
     * 查询五级部门
     *
     * @param @param  time
     * @param @return
     * @throws
     * @Description:TODO
     * @author:jack.deng.00190167
     * @time:2016年7月20日 下午5:24:41
     * @return:List<Object>
     */
    public List<Object> getDeptFive(String deptThree, String deptFour);

    /**
     * 查询角色列表
     *
     * @param @param  param
     * @param @return
     * @throws
     * @Description:TODO
     * @author:jack.deng.00190167
     * @time:2016年8月2日 下午2:25:23
     * @return:List<ContributionTopBean>
     */
    public List<Object> queryRoleInfoList();


    public List<String> querySendMailUsers(List<ContributionTopBean> topsz);

    public String queryMailAddressByUserId(String userName);

}