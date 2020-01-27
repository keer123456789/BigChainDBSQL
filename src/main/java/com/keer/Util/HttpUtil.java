package com.keer.Util;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @BelongsProject: BDQLGUI
 * @BelongsPackage: Util
 * @Author: keer
 * @CreateTime: 2020-01-14 22:09
 * @Description: http工具类
 */
public class HttpUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);


    private static void onSuccess(Response response) throws IOException {
        //TODO : Add your logic here with response from server
        logger.info("Success : 操作成功");
    }

    private static void onFailure() {
        //TODO : Add your logic here
        logger.info("Transaction failed");
    }


    /**
     * 发起get请求
     *
     * @param url
     * @return
     */
    public static String httpGet(String url) {
        String result = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送httppost请求
     *
     * @param url
     * @param data  提交的参数为key=value&key1=value1的形式
     * @return
     */
    public static String httpPost(String url, String data) {
        String result = null;
        OkHttpClient httpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), data);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        try {
            Response response = httpClient.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
