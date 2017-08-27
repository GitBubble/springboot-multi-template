package com.huawei.ptn.opensourceDigger.utils;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Pack200;

/**
 * Created by d00190167 on 2017/8/14.
 */
@Service
public class DiggerHelper {
    @Value(value= "${config.json.squad.path}")
    private String squad_list_path;

    public Map<String,List> loadOurTeamSquad(String geoDefineDep) {
        String[] details = geoDefineDep.split("-");
        String dept_lv4 = details[0];
        String dept_lv5 = details[1];
        String dept_lv6 = details[2];
        String dept_geo = details[3];
        JsonParser parser = new JsonParser();
        Map<String,List> forceMap = new HashMap<String, List>();

        try {
            Object obj = parser.parse(new FileReader(squad_list_path));
            JsonArray jsonArray = (JsonArray) obj;

            for(JsonElement element : jsonArray){
                JsonObject o = element.getAsJsonObject();
                String curDept4 = o.get("四级部门").getAsString();
                String curDept5 = o.get("五级部门").getAsString();
                String curDept6 = o.get("六级部门").getAsString();
                String curGeo   = o.get("地域").getAsString();
                if(dept_lv4.equals(curDept4) && dept_lv5.equals(curDept5) && dept_lv6.equals(curDept6) && dept_geo.equals(curGeo)){
                    //parse groups
                    //JsonArray groupData = element.getValue().getAsJsonArray();
                    String groupName = o.get("PL团队/项目组").getAsString();
                    List<String> memberList = forceMap.get(groupName);
                    if( null == memberList){
                        List<String> list = new ArrayList<String>();
                        forceMap.put(groupName,list);
                    }else{
                        List<String> list = forceMap.get(groupName);
                        list.add(o.get("员工姓名").toString());
                        forceMap.put(groupName,list);
                    }
                     //for(Map.Entry<String,JsonElement> wolf : groupObj.entrySet()){

                }


            }
        }
        catch (Exception e){
            System.out.println("the info is " + e.getLocalizedMessage());
        }

        return forceMap;
    }


}
