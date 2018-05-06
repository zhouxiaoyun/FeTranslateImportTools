package com.zhkeen.flyrise.fe.translate.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class Constants {

  public static Map<String, String> ALL_LANGUAGE_MAP = getAllLanguageMap();

  private static Map<String, String> getAllLanguageMap(){
    Map<String, String> allMap = new LinkedHashMap<>();
    allMap.put("zh","简体中文");
    allMap.put("cht","繁体中文");
    allMap.put("en","英文");
    allMap.put("jp","日语");
    allMap.put("kor","韩语");
    allMap.put("fra","法语");
    allMap.put("spa","西班牙语");
    allMap.put("ru","俄语");
    allMap.put("de","德语");
    allMap.put("pt","葡萄牙语");
    return allMap;
  }

}
