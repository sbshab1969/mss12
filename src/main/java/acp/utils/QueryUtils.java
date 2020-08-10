package acp.utils;

import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

  public static boolean emptyString(String str) {
    if (str == null || str.equals("")) {
      return true;
    }
    return false;
  }

  public static String strAddAnd(String str1, String str2) {
    String str = "";
    if (emptyString(str1) && emptyString(str2)) {
      str = "";
    } else if (!emptyString(str1) && emptyString(str2)) {
      str = str1;
    } else if (emptyString(str1) && !emptyString(str2)) {
      str = str2;
    } else {
      str = str1 + " and " + str2;
    }
    return str;
  }

  public static String buildSelectFields(String[] fields, String[] fieldnames) {
    StringBuilder query = new StringBuilder("select ");
    if (fields != null) {
      for (int i = 0; i < fields.length; i++) {
        query.append(fields[i]);
        if (fieldnames != null)
          query.append(" " + "\"" + fieldnames[i] + "\"");
        if (i != fields.length - 1)
          query.append(", ");
      }
    } else {
      query.append("*");
    }
    return query.toString();
  }

  public static String buildSelectFrom(String[] fields, String[] fieldnames, String tblFrom) {
    String query = buildSelectFields(fields, fieldnames);
    query = query + " from " + tblFrom;
    return query;
  }

  public static String buildQuery(String selFrom, String where, String order) {
    StringBuilder query = new StringBuilder(selFrom);
    if (!emptyString(where)) {
      query.append(" where " + where);
    }
    if (!emptyString(order)) {
      query.append(" order by " + order);
    }
    // System.out.println(query);
    return query.toString();
  }

  public static String buildQuery(String selFields, String tblFrom, String where, String order) {
    // -----------------------------
    StringBuilder query = new StringBuilder("");
    if (!emptyString(selFields)) {
      query.append(selFields + " ");
    }
    query.append("from " + tblFrom);
    // -----------------------------
    if (!emptyString(where)) {
      query.append(" where " + where);
    }
    if (!emptyString(order)) {
      query.append(" order by " + order);
    }
    // System.out.println(query);
    return query.toString();
  }

  public static List<String[]> getListString(List<?> objList) {
    List<String[]> cache = new ArrayList<>();
    // -------------------------------------------
    for (int i=0; i < objList.size(); i++) {
      Object[] objArr =  (Object[]) objList.get(i);
      int cntCols = objArr.length; 
      String[] record = new String[cntCols];
      for (int j = 0; j < cntCols; j++) {
        record[j] = objArr[j].toString();
      }
      cache.add(record);
    }
    // -------------------------------------------
    return cache;
  }

}
