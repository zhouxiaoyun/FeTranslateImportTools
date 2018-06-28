package com.zhkeen.flyrise.fe.translate.util;

import com.zhkeen.flyrise.fe.translate.model.ConfigModel;
import com.zhkeen.flyrise.fe.translate.model.TranslateResultModel;
import com.zhkeen.flyrise.fe.translate.util.baidu.MD5;
import com.zhkeen.flyrise.fe.translate.util.baidu.TransApi;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DbUtil {

    private static final String TABLE_TRANSLATE = "TRANSLATE";
    private static final SimpleDateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private ConfigModel model;
    private Connection connection;
    private TransApi transApi;

    public DbUtil(ConfigModel model) throws ClassNotFoundException, SQLException {
        this.model = model;
        Class.forName(model.getDriverName());
        this.connection = DriverManager
                .getConnection(model.getJdbcUrl(), model.getJdbcUser(), model.getJdbcPassword());
        this.transApi = new TransApi(model.getAppid(), model.getSecretkey());
    }

    public void translate(String sql) throws SQLException, UnsupportedEncodingException, ClassNotFoundException {
        PreparedStatement pstmt = connection.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            String message = rs.getString(1);
            if (message != null && message.length() > 0) {
                insertMessage(message);
            }
        }
    }

    public void insertMessage(String message)
            throws SQLException, ClassNotFoundException, UnsupportedEncodingException {
        System.out.println(message);
        TranslateResultModel translateResultModel = findByMessage(message);
        if (translateResultModel == null) {
            translateResultModel = new TranslateResultModel();
            translateResultModel.setCode(MD5.md5(message));
            translateResultModel.setCn(message);
            translateResultModel.setTw(transApi.getTransResult(message, "auto", "cht"));
            translateResultModel.setUs(transApi.getTransResult(message, "auto", "en"));
            translateResultModel.setUpdateTime(DATA_FORMAT.format(new Date()));
            translateResultModel.setUnitCode("1");
            translateResultModel.setUserId("1");

            insertTranslate(translateResultModel);
        }
    }

    public TranslateResultModel findByMessage(String message)
            throws SQLException {

        String selectSql = String
                .format("SELECT ID,CODE,CN,TW,US,UPDATETIME,UNITCODE,USERID FROM %s WHERE CN = ?",
                        TABLE_TRANSLATE);
        PreparedStatement pstmt = connection.prepareStatement(selectSql);
        pstmt.setString(1, message);
        ResultSet rs = pstmt.executeQuery();
        TranslateResultModel model = buildResultModel(rs);
        return model;
    }

    public void insertTranslate(TranslateResultModel model)
            throws SQLException {
        String selectSql = String
                .format("SELECT ID FROM %s WHERE CODE = ? OR CN = ?", TABLE_TRANSLATE);
        PreparedStatement pstmt = connection.prepareStatement(selectSql);
        pstmt.setString(1, model.getCode());
        pstmt.setString(2, model.getCn());

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            throw new SQLException("已经存在相同的编码[CODE]或者条目[CN]!");
        } else {
            int maxId = 1;
            String maxIdSql = String.format("SELECT MAX(ID) AS MAXID FROM %s", TABLE_TRANSLATE);
            PreparedStatement pstmtMaxId = connection.prepareStatement(maxIdSql);
            ResultSet rsMaxId = pstmtMaxId.executeQuery();
            if (rsMaxId.next()) {
                maxId = rsMaxId.getInt("MAXID") + 1;
            }
            String insertSql = String
                    .format(
                            "INSERT INTO %s(ID,CODE,CN,TW,US,UPDATETIME,UNITCODE,USERID) VALUES(?,?,?,?,?,?,?,?)",
                            TABLE_TRANSLATE);
            PreparedStatement pstmtInsert = connection.prepareStatement(insertSql);
            pstmtInsert.setInt(1, maxId);
            pstmtInsert.setString(2, model.getCode());
            pstmtInsert.setString(3, model.getCn());
            pstmtInsert.setString(4, model.getTw());
            pstmtInsert.setString(5, model.getUs());
            pstmtInsert.setString(6, model.getUpdateTime());
            pstmtInsert.setString(7, model.getUnitCode());
            pstmtInsert.setString(8, model.getUserId());
            pstmtInsert.executeUpdate();
        }
    }

    private TranslateResultModel buildResultModel(ResultSet rs)
            throws SQLException {
        if (rs.next()) {
            TranslateResultModel model = new TranslateResultModel();
            model.setId(rs.getInt("ID"));
            model.setCode(rs.getString("CODE"));
            model.setCn(rs.getString("CN"));
            model.setTw(rs.getString("TW"));
            model.setUs(rs.getString("US"));
            model.setUpdateTime(rs.getString("UPDATETIME"));
            model.setUnitCode(rs.getString("UNITCODE"));
            model.setUserId(rs.getString("USERID"));
            return model;
        } else {
            return null;
        }
    }

}
