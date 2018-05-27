package com.zhkeen.flyrise.fe.translate.util;

import com.zhkeen.flyrise.fe.translate.model.ConfigurationModel;
import com.zhkeen.flyrise.fe.translate.model.TranslateResultModel;
import com.zhkeen.flyrise.fe.translate.util.baidu.TransApi;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class DbUtil {

  private static final String TABLE_TRANSLATE = "TRANSLATE";
  private static final String TABLE_TRANSLATE_RELATE = "TRANSLATE_RELATE";
  private TransApi transApi;
  private Connection connection;
  private String defaultLanguage;
  private Map<String, String> supportLanguage;

  public DbUtil(ConfigurationModel configurationModel)
      throws SQLException, ClassNotFoundException {
    this.transApi = new TransApi(configurationModel.getAppId(), configurationModel.getSecretKey());
    this.connection = getConnection(configurationModel);
    this.defaultLanguage = configurationModel.getDefaultLanguage();
    this.supportLanguage = configurationModel.getSupportLanguageMap();
  }

  private Connection getConnection(ConfigurationModel model)
      throws SQLException, ClassNotFoundException {
    Class.forName(model.getDriverName());
    Connection connection = DriverManager
        .getConnection(model.getJdbcUrl(), model.getJdbcUserName(), model.getJdbcPassword());
    return connection;
  }

  public TranslateResultModel findByMessage(String defaultLanguage, String message,
      Map<String, String> supportLanguageMap) throws SQLException, ClassNotFoundException {
    String langFields = getSearchField(supportLanguageMap);
    String selectSql = String
        .format("SELECT ID, %s FROM %s WHERE %s = ?", langFields, TABLE_TRANSLATE,
            defaultLanguage);
    PreparedStatement pstmt = connection.prepareStatement(selectSql);
    pstmt.setString(1, message);
    ResultSet rs = pstmt.executeQuery();
    TranslateResultModel model = buildResultModel(rs, supportLanguageMap);
    return model;
  }

  public void update(TranslateResultModel model) throws SQLException, ClassNotFoundException {
    String selectSql = String
        .format("SELECT ID FROM %s WHERE ID = ?", TABLE_TRANSLATE);
    PreparedStatement pstmt = connection.prepareStatement(selectSql);
    pstmt.setString(1, model.getId());
    ResultSet rs = pstmt.executeQuery();

    if (rs.next()) {
      String updateSql = "UPDATE " + TABLE_TRANSLATE + " SET ";
      for (String lang : model.getTranslateMap().keySet()) {
        updateSql = updateSql + " " + lang + " = ?,";
      }
      updateSql = updateSql + " LASTUPDATE = ? WHERE ID = ?";
      PreparedStatement psUpdate = connection.prepareStatement(updateSql);
      int i = 1;
      for (String lang : model.getTranslateMap().keySet()) {
        psUpdate.setString(i, model.getTranslateMap().get(lang));
        i++;
      }
      psUpdate.setDate(i, model.getLastUpdate());
      psUpdate.setString(i + 1, model.getId());
      psUpdate.executeUpdate();
    } else {
      String insertSql =
          "INSERT INTO " + TABLE_TRANSLATE + "(ID, " + getSearchField(model.getTranslateMap())
              + ", LASTUPDATE) VALUES(?,";
      int j = model.getTranslateMap().size();
      for (int i = 0; i < j; i++) {
        insertSql = insertSql + "?,";
      }
      insertSql = insertSql + "?)";
      PreparedStatement psInsert = connection.prepareStatement(insertSql);
      psInsert.setString(1, model.getId());
      int m = 2;
      for (String lang : model.getTranslateMap().keySet()) {
        psInsert.setString(m, model.getTranslateMap().get(lang));
        m++;
      }
      psInsert.setDate(m, model.getLastUpdate());
      psInsert.execute();
    }
    if (StringUtils.isNotEmpty(model.getFileType()) && StringUtils
        .isNotEmpty(model.getFileName())) {
      String updateRelate = String.format(
          " IF NOT EXISTS (SELECT ID FROM %s WHERE ID = ? AND RELATE_TYPE = ? AND RELATION = ?)  BEGIN INSERT INTO %s(ID, RELATE_TYPE, RELATION) VALUES(?, ?, ?) END",
          TABLE_TRANSLATE_RELATE, TABLE_TRANSLATE_RELATE);

      PreparedStatement psUpdateRelate = connection.prepareStatement(updateRelate);
      psUpdateRelate.setString(1, model.getId());
      psUpdateRelate.setString(2, model.getFileType());
      psUpdateRelate.setString(3, model.getFileName());
      psUpdateRelate.setString(4, model.getId());
      psUpdateRelate.setString(5, model.getFileType());
      psUpdateRelate.setString(6, model.getFileName());
      psUpdateRelate.execute();
    }
  }

  public String insert(String message, String isJS)
      throws SQLException, UnsupportedEncodingException {
    String selectSql = String
        .format("SELECT ID,ISJS FROM %s WHERE %s = ?", TABLE_TRANSLATE, defaultLanguage);
    PreparedStatement pstmt = connection.prepareStatement(selectSql);
    pstmt.setString(1, message);
    ResultSet rs = pstmt.executeQuery();
    if (!rs.next()) {
      String id = LanguageIdUtil.generateId();
      String insertSql =
          "INSERT INTO " + TABLE_TRANSLATE + "(ID, ISJS," + getSearchField(supportLanguage)
              + ", LASTUPDATE) VALUES(?,?,";
      int j = supportLanguage.size();
      for (int i = 0; i < j; i++) {
        insertSql = insertSql + "?,";
      }
      insertSql = insertSql + "?)";
      PreparedStatement psInsert = connection.prepareStatement(insertSql);
      psInsert.setString(1, LanguageIdUtil.generateId());
      psInsert.setString(2, isJS);
      int m = 3;
      for (String lang : supportLanguage.keySet()) {
        psInsert.setString(m, transApi.getTransResult(message, "auto", lang.toLowerCase()));
        m++;
      }
      psInsert.setDate(m, new java.sql.Date(new Date().getTime()));
      psInsert.execute();
      return id;
    } else {
      if ("0".equals(rs.getString("ISJS")) && "1".equals(rs.getString("ISJS"))) {
        String updateSql =
            "UPDATE " + TABLE_TRANSLATE + " SET ISJS = ?, LASTUPDATE = ? WHERE ID = ?";
        PreparedStatement psUpdate = connection.prepareStatement(updateSql);
        psUpdate.setString(1, isJS);
        psUpdate.setDate(2, new java.sql.Date(new Date().getTime()));
        psUpdate.setString(3, rs.getString("ID"));
        psUpdate.executeUpdate();
      }
      return rs.getString("ID");
    }
  }

  public void initalControllProperties() throws SQLException {
    String excutePropertiesSql =
        " IF NOT EXISTS(SELECT SP00 FROM SYS_CTRLPROPERTY WHERE SP00='TRANSLATE') "
            + " BEGIN INSERT FE_BASE5..SYS_CTRLPROPERTY(SP00,SP01,SP02,SP03,SP04,SP05,SP06,SP07,SP08,SP09,SP10,SP11,SP12,SP13,SP14,SP15,SP16,SP17)"
            + " VALUES('TRANSLATE','翻译编号','','全部','','','','0','Data','数据',99,'0','0','',0,NULL,NULL,1110) END";
    PreparedStatement psmtProperties = connection
        .prepareStatement(excutePropertiesSql);
    psmtProperties.execute();

    String selectSql = String
        .format("SELECT SC00 FROM SYS_CONTROL");
    PreparedStatement pstmt = connection.prepareStatement(selectSql);
    ResultSet rs = pstmt.executeQuery();
    while (rs.next()) {
      String excuteControllPropertiesSql =
          "IF NOT EXISTS(SELECT SC00 FROM SYS_CTRLPROPS WHERE SC00=? AND SC01='TRANSLATE') "
              + " BEGIN INSERT SYS_CTRLPROPS(SC00, SC01, SC02, SC03,SC04,SC05) VALUES(?,'TRANSLATE','','','0',NULL) END";
      PreparedStatement psmtControllProperties = connection
          .prepareStatement(excuteControllPropertiesSql);
      psmtControllProperties.setString(1, rs.getString("SC00"));
      psmtControllProperties.setString(2, rs.getString("SC00"));
      psmtControllProperties.execute();
    }
  }

  public void handleFormLanguage() throws SQLException, UnsupportedEncodingException {
    String selectSql = String
        .format(
            "SELECT SP00,SP01,SP02 FROM SYS_FACECTRLPROP where SP01='CAPTION' AND SP02 <> '' AND SP02 IS NOT NULL"
                + " AND SP00 NOT IN (SELECT SP00 FROM SYS_FACECTRLPROP WHERE SP01='TRANSLATE')");
    PreparedStatement pstmt = connection.prepareStatement(selectSql);
    ResultSet rs = pstmt.executeQuery();
    while (rs.next()) {
      String id = insert(rs.getString("SP02"), "0");
      String insertSql = "INSERT SYS_FACECTRLPROP(SP00,SP01,SP02) VALUES(?,'TRANSLATE',?)";
      PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
      preparedStatement.setLong(1, rs.getLong("SP00"));
      preparedStatement.setString(2, String.valueOf(id));
      preparedStatement.execute();
    }
  }

  private TranslateResultModel buildResultModel(ResultSet rs,
      Map<String, String> supportLanguageMap)
      throws SQLException {
    if (rs.next()) {
      TranslateResultModel model = new TranslateResultModel();
      Map<String, String> map = new LinkedHashMap<>();
      model.setId(rs.getString("ID"));
      for (String lang : supportLanguageMap.keySet()) {
        map.put(lang, rs.getString(lang));
      }
      model.setLastUpdate(rs.getDate("LASTUPDATE"));
      model.setTranslateMap(map);
      return model;
    } else {
      return null;
    }
  }

  private String getSearchField(Map<String, String> supportLanguageMap) {
    String field = "";
    for (String lang : supportLanguageMap.keySet()) {
      field = field + lang + ",";
    }
    field = field.substring(0, field.length() - 1);
    return field;
  }
}
