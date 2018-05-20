package com.zhkeen.flyrise.fe.translate;

import com.zhkeen.flyrise.fe.translate.util.DbUtil;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

public class FormImport {

  private DbUtil dbUtil;

  public FormImport(DbUtil dbUtil) {
    this.dbUtil = dbUtil;
  }

  public void languageImport() throws SQLException, UnsupportedEncodingException {
    this.dbUtil.initalControllProperties();
    this.dbUtil.handleFormLanguage();
  }
}
