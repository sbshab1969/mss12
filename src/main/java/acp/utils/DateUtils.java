package acp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

  private static final String DATE_FORMAT = "dd.MM.yyyy";
  private static final String TS_FORMAT = "dd.MM.yyyy HH:mm:ss";

  private static SimpleDateFormat dtFormat = new SimpleDateFormat(DATE_FORMAT);
  private static SimpleDateFormat tsFormat = new SimpleDateFormat(TS_FORMAT);

  public static String date2Str(Date value) {
    String val = null;
    if (value != null) {
      val = dtFormat.format(value);
    }
    return val;
  }

  public static Date str2Date(String value) throws ParseException {
    Date val = null;
    if (value != null) {
      val = dtFormat.parse(value);
    }
    return val;
  }

  public static String dateTime2Str(Date value) {
    String val = null;
    if (value != null) {
      val = tsFormat.format(value);
    }
    return val;
  }

  public static Date str2DateTime(String value) throws ParseException {
    Date val = null;
    if (value != null) {
      val = tsFormat.parse(value);
    }
    return val;
  }

  public static java.util.Date sql2util(java.sql.Date dtSql) {
    java.util.Date dtUtil = null;
    if (dtSql != null) {
      dtUtil = new java.util.Date(dtSql.getTime());
    }
    return dtUtil;
  }

  public static java.sql.Date util2sql(java.util.Date dtUtil) {
    java.sql.Date dtSql = null;
    if (dtUtil != null) {
      dtSql = new java.sql.Date(dtUtil.getTime());
    }
    return dtSql;
  }

  public static java.util.Date ts2util(java.sql.Timestamp ts) {
    java.util.Date dtUtil = null;
    if (ts != null) {
      dtUtil = new java.util.Date(ts.getTime());
    }
    return dtUtil;
  }

  public static java.sql.Timestamp util2ts(java.util.Date dtUtil) {
    java.sql.Timestamp ts = null;
    if (dtUtil != null) {
      ts = new java.sql.Timestamp(dtUtil.getTime());
    }
    return ts;
  }

}
