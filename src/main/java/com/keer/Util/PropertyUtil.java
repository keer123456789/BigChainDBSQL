package com.keer.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @BelongsProject: BDQLGUI
 * @BelongsPackage: Util
 * @Author: keer
 * @CreateTime: 2020-01-14 22:06
 * @Description: 获取配置文件数据
 */
public class PropertyUtil {
    static Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

    /**
     * 输入key，返回value
     *
     * @param name key
     * @return value
     * @throws IOException
     */
    public static String getProperties(String name) {
        Properties prop = new Properties();
        try {
            prop.load(PropertyUtil.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {

            logger.error("配置文件不存在");
            e.printStackTrace();
        }
        return prop.getProperty(name);
    }

}
