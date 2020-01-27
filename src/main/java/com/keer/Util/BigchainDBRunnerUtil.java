package com.keer.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigchaindb.builders.BigchainDbConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @BelongsProject: BDQLGUI
 * @BelongsPackage: Util
 * @Author: keer
 * @CreateTime: 2020-01-14 22:08
 * @Description: 连接BigchainDB的工具类
 */
public class BigchainDBRunnerUtil {
    //日志输出
    private Logger logger = LoggerFactory.getLogger(BigchainDBRunnerUtil.class);
    //获取配置文件的BigchainDB的url
    private String url = PropertyUtil.getProperties("blockchaindb.base-url");

    /**
     * 连接BigchainDB
     */
    public void StartConn() {

        BigchainDbConfigBuilder
                .baseUrl(url)
                .setup();
        logger.info("与节点：" + url + ",连接成功");
    }

    public boolean StartConn(String url) {

        BigchainDbConfigBuilder
                .baseUrl(url)
                .setup();
        String body = HttpUtil.httpGet(url);
        logger.info(body);
        JSONObject jsonObject = JSON.parseObject(body, JSONObject.class);
        logger.info(jsonObject.getString("version"));
        if (jsonObject.getString("version").equals("2.0.0b9")) {
            logger.info("与节点：" + url + ",连接成功");
            return true;
        } else {
            logger.info("与节点：" + url + ",连接失败");
            return false;
        }
    }
}
