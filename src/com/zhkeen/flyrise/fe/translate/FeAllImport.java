package com.zhkeen.flyrise.fe.translate;

import com.zhkeen.flyrise.fe.translate.model.ConfigurationModel;
import com.zhkeen.flyrise.fe.translate.util.ConfigurationException;
import com.zhkeen.flyrise.fe.translate.util.DbUtil;
import com.zhkeen.flyrise.fe.translate.util.FileUtil;
import java.io.IOException;
import java.sql.SQLException;

public class FeAllImport {

  public void languageImport(String projectPath)
      throws IOException, ConfigurationException, SQLException, ClassNotFoundException {
    FileUtil fileUtil = new FileUtil();
    ConfigurationModel configurationModel= fileUtil.readConfigurationModel();
    DbUtil dbUtil = new DbUtil(configurationModel);

    ProjectImport projectImport = new ProjectImport(dbUtil);
    projectImport.languageImport(projectPath);

    FormImport formImport = new FormImport(dbUtil);
    formImport.languageImport();
  }
}
