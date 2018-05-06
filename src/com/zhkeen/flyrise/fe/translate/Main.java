package com.zhkeen.flyrise.fe.translate;

import com.zhkeen.flyrise.fe.translate.util.ConfigurationException;
import java.io.IOException;
import java.sql.SQLException;

public class Main {

  public static void main(String[] args) {
    FeAllImport feAllImport = new FeAllImport();
    try {
      feAllImport.languageImport(args[0]);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ConfigurationException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
