package com.zhkeen.flyrise.fe.translate.util;

import com.zhkeen.flyrise.fe.translate.model.ConfigModel;

import java.io.IOException;
import java.util.Properties;

public class FileUtil {

    private static final String CONFIG_PROPERTIES = "com/zhkeen/flyrise/fe/translate/util/config.properties";

    public ConfigModel readConfigurationModel()
            throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream(CONFIG_PROPERTIES));
        String driverName = properties.getProperty("mssql.jdbc.driver");
        String jdbcUrl = properties.getProperty("mssql.jdbc.url");
        String jdbcUserName = properties.getProperty("mssql.jdbc.user");
        String jdbcPassword = properties.getProperty("mssql.jdbc.password");

        String appId = properties.getProperty("baidu.appid");
        String secretKey = properties.getProperty("baidu.secretkey");

        if (StringUtils.isNotEmpty(driverName) && StringUtils.isNotEmpty(jdbcUrl) && StringUtils
                .isNotEmpty(jdbcUserName) && StringUtils.isNotEmpty(jdbcPassword) && StringUtils
                .isNotEmpty(appId)
                && StringUtils.isNotEmpty(secretKey)) {

            ConfigModel configModel = new ConfigModel();

            configModel.setDriverName(driverName);
            configModel.setJdbcUrl(jdbcUrl);
            configModel.setJdbcUser(jdbcUserName);
            configModel.setJdbcPassword(jdbcPassword);
            configModel.setAppid(appId);
            configModel.setSecretkey(secretKey);

            return configModel;
        }
        return null;
    }
}
