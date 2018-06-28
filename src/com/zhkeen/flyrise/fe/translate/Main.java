package com.zhkeen.flyrise.fe.translate;

import com.zhkeen.flyrise.fe.translate.model.ConfigModel;
import com.zhkeen.flyrise.fe.translate.util.DbUtil;
import com.zhkeen.flyrise.fe.translate.util.FileUtil;

import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {

        FileUtil fileUtil = new FileUtil();
        ConfigModel configModel = fileUtil.readConfigurationModel();
        DbUtil dbUtil = new DbUtil(configModel);
        dbUtil.translate("Select distinct SF02 from FE_BASE5..SYS_FUNCTION");
        dbUtil.translate("select distinct NAME from FE_BASE5..DESKTOP_MENU_MANAGEMENT");
        dbUtil.translate("select distinct SE09 from FE_BASE5..SYS_EVENT");
        dbUtil.translate("Select distinct SI02 from FE_BASE5..SORT_INFOR");


    }
}
