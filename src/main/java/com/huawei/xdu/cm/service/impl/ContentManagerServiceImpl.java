package com.huawei.xdu.cm.service.impl;

//import com.huawei.ipm.base.utils.string.StringUtil;
import com.huawei.xdu.cm.model.ProjectGroups;
import com.huawei.xdu.cm.orm.HibernateDao;
import com.huawei.xdu.cm.service.ContentManagerService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContentManagerServiceImpl implements ContentManagerService {
    @Autowired
    private HibernateDao<ProjectGroups, Integer> groupsDao;
//    @Autowired
//    private RestTemplate template;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentManagerServiceImpl.class);

    public HibernateDao<ProjectGroups, Integer> getGroupsDao() {
        return this.groupsDao;
    }

    public void setGroupsDao(HibernateDao<ProjectGroups, Integer> groupsDao) {
        this.groupsDao = groupsDao;
        this.groupsDao.setEntityClass(ProjectGroups.class);
    }
    @Transactional
    @Override
//    @HystrixCommand(fallbackMethod = "groupsFallback")
    public List queryProjectTops() {
    //public List<Map<String, String>> queryProjectGroups(String select, String where) {
    /*    List<Map<String, String>> groups = new ArrayList<Map<String, String>>();
        if (StringUtil.nullOrEmpty(select)) {
            select = "pid,pname,powner,vid,vname,vowner";
        }*/

        String sql = "SELECT tdu.id,tsu.name,tsu.YU_NAME employNum,tsu.DEPARTMENT_FOUR departmentFour,tsu.DEPARTMENT_FIVE departmentFive,tsu.CODE role,tsu.ROLE position, ISNULL(tdc.coin,0) score,ISNULL(tdc.experice_point,0) totalContribution  FROM T_SFM_USER tsu LEFT JOIN T_DuIsource_users tdu ON tsu.EMPLOY_ID = SUBSTRING (tdu.extern_uid,2,len(tdu.extern_uid)) LEFT JOIN T_DuIsource_Coins tdc ON tdu.ID = tdc.user_id    WHERE tsu.time='2017-06'      AND tsu.DEPARTMENT_THREE='路由器与电信以太开发管理部' AND tsu.ISHUAWEI='华为' AND tsu.DEPARTMENT_FOUR = '流控软件开发部' AND tsu.DEPARTMENT_FIVE = '模型算法开发部'";

        List t = groupsDao.createSqlQuery(sql).list();

        return t;
    }

//    public String test() {
//        template.
//    }

    public List<Map<String, String>> groupsFallback(String select, String where) {
        ContentManagerServiceImpl.LOGGER.info(
                "Abnormal, fallback into the method to receive the parameters: select = {}, where = {}",
                select, where);
        List<Map<String, String>> groups = new ArrayList<Map<String, String>>();
        Map<String, String> row = new HashMap<String, String>();
        String[] keys = select.split(",");
        for (int i = 0; i < keys.length; i++) {
            row.put(keys[i], "Hystrix");
        }
        groups.add(row);
        return groups;
    }
}
