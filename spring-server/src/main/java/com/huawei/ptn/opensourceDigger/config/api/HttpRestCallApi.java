package com.huawei.ptn.opensourceDigger.config.api;

import com.huawei.ipm.base.utils.date.DateUtil;
import com.huawei.ipm.base.utils.string.StringUtil;
import net.sf.json.JSONArray;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by d00190167 on 2017/7/24.
 */
public class HttpRestCallApi {

    private static Logger log = Logger.getLogger(HttpRestCallApi.class);

    public static final String POSTPARAMORDERNEGTIVE = "-1";
    public static final String POSTPARAMORDERPOSITIVE = "1";

    public static String readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while( (len = inStream.read(buffer)) !=-1 ){
            outStream.write(buffer, 0, len);
        }
        String data = outStream.toString("UTF-8");
        outStream.close();
        inStream.close();
        return data;
    }

    private static String dealWithState(int state, HttpMethodBase method, String url) throws IOException, Exception{
        String result = "";

        if (state == 200) {
            String resEncode = "";
            Header resHeaderEncode = method.getResponseHeader("Content-Encoding");
            if(resHeaderEncode != null){
                resEncode = resHeaderEncode.getValue().trim().toLowerCase();
            }

            String resHeaderTransferEncodeValue = "";
            Header resHeaderTransferEncode = method.getResponseHeader("Transfer-Encoding");
            if(resHeaderTransferEncode != null){
                resHeaderTransferEncodeValue = resHeaderTransferEncode.getValue().trim().toLowerCase();
            }

            Header[] headers = method.getResponseHeaders();

            if ("gzip".equals(resEncode)) {
                result = readInputStream(new GZIPInputStream(method.getResponseBodyAsStream()));
            } else {

        		/*
        		if("chunked".equalsIgnoreCase(resHeaderTransferEncodeValue)){
        			byte[] b=method.getResponseBody();
            		result = new String(b,"utf-8");
        		}else{
        			result = method.getResponseBodyAsString();
        		}*/

                byte[] b=method.getResponseBody();
                result = new String(b,"utf-8");

                try{
                    result = URLDecoder.decode(result, "UTF-8");
                }catch(Exception e){
                    log.info("接口获取的字符串未经过URL编码，无需解码。");
                }
            }

        } else {
            log.error("callHttpPostMethod fialed!! state=>" + state
                    + " url=>" + url);
        }

        return result;
    }

    public static String callHttpUtf8PostMethod(
            String url, String username, String password, String queryString, String queryStringTarget,
            Map<String, String> headers, Map<String, String> params) {
        int state = 0;
        String result = "";
        try {
            HttpClient client = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
            if (StringUtil.notNullandEmpty(username)) {
                client.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
            }
            PostMethod method = new PostMethod(url);
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"utf-8");
            RequestEntity requestEntity = null;

            setRequestEntity(POSTPARAMORDERPOSITIVE, queryString, requestEntity, method);

            for (Map.Entry<String, String> header : headers.entrySet()) {
                method.setRequestHeader(header.getKey(), header.getValue());
            }

            setQueryStringAndRequestBody(POSTPARAMORDERPOSITIVE,queryString, queryStringTarget, params, method);

            state = client.executeMethod(method);
            result = dealWithState(state, method, url);
            method.releaseConnection();
            ((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
        } catch (Exception e) {
            log.error(e);
        }
        return result;
    }


    private static void setRequestEntity(String order, String queryString, RequestEntity requestEntity, PostMethod method) throws IOException {
        if(!POSTPARAMORDERPOSITIVE.equals(order)){
            if (StringUtil.notNullandEmpty(queryString)) {
                byte[] queryBytes = queryString.getBytes("utf-8");
                InputStream queryStream = new ByteArrayInputStream(queryBytes, 0, queryBytes.length);
                requestEntity = new InputStreamRequestEntity(queryStream, queryBytes.length, "application/soap+xml; charset=utf-8");
                queryStream.close();
            }
            method.setRequestEntity(requestEntity);
        }
    }

    private static void setQueryStringAndRequestBody(String order,String queryString, String queryStringTarget, Map<String, String> params, PostMethod method){
        if(!POSTPARAMORDERPOSITIVE.equals(order)){
            if (params.size() != 0) {
                NameValuePair[] pairs = new NameValuePair[params.size()];
                int i = 0;
                for (Map.Entry<String, String> param : params.entrySet()) {
                    pairs[i] = new NameValuePair(param.getKey(), param.getValue());
                    i++;
                }
                method.setQueryString(pairs);
            }
        }else if(POSTPARAMORDERPOSITIVE.equals(order)){
            if(queryStringTarget != null){
                NameValuePair[] pairs = new NameValuePair[2];
                pairs[0] = new NameValuePair("", queryString);
                pairs[1] = new NameValuePair("", queryStringTarget);
                method.setQueryString(pairs);
            }else{
                NameValuePair[] pairs = new NameValuePair[1];
                pairs[0] = new NameValuePair("", queryString);
                method.setQueryString(pairs);
            }

            NameValuePair[] data = getNameValuePairFromMap(params);
            if(data != null){
                // 把参数值放入postMethod中
                method.setRequestBody(data);
            }
        }
    }

    /**
     * 将Map转换为NameValuePair
     * @param params
     * @return
     */
    private static NameValuePair[] getNameValuePairFromMap(Map<String, String> params){
        NameValuePair[] rtn = null;

        if(params == null){
            return rtn;
        }

        rtn = new NameValuePair[params.size()];

        int i = 0;
        for(String key : params.keySet()){
            NameValuePair nvp = new NameValuePair(key, MapUtils.getString(params, key, ""));
            rtn[i++] = nvp;
        }

        return rtn;
    }
}