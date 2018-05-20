package com.zhkeen.flyrise.fe.translate.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class Constants {

  public static Map<String, String> ALL_LANGUAGE_MAP = getAllLanguageMap();

  private static Map<String, String> getAllLanguageMap(){
    Map<String, String> allMap = new LinkedHashMap<>();
    allMap.put("ZH","简体中文");
    allMap.put("CHt","繁体中文");
    allMap.put("EN","英文");
    allMap.put("JP","日语");
    allMap.put("KOR","韩语");
    allMap.put("FRA","法语");
    allMap.put("SPA","西班牙语");
    allMap.put("RU","俄语");
    allMap.put("DE","德语");
    allMap.put("PT","葡萄牙语");
    return allMap;
  }

}
