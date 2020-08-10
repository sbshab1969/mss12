package acp.db.service.impl.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.utils.*;

public class ManagerUtilJdbc extends ManagerBaseJdbc {
  private static Logger logger = LoggerFactory.getLogger(ManagerUtilJdbc.class);

  public Long getValueL(String strQuery) {
    Long val = null;
    try {
      Statement stmt = dbConn.createStatement();
      ResultSet rs = stmt.executeQuery(strQuery);
      rs.next();
      val = rs.getLong(1);
      rs.close();
      stmt.close();
    } catch (SQLException e) {
      DialogUtils.errorPrint(e,logger);
      val = null;
    } catch (Exception e) {
      DialogUtils.errorPrint(e,logger);
      val = null;
    }
    return val;
  }

  public String getValueV(String strQuery) {
    String val = null;
    try {
      Statement stmt = dbConn.createStatement();
      ResultSet rs = stmt.executeQuery(strQuery);
      rs.next();
      val = rs.getString(1);
      rs.close();
      stmt.close();
    } catch (SQLException e) {
      DialogUtils.errorPrint(e,logger);
      val = null;
    } catch (Exception e) {
      DialogUtils.errorPrint(e,logger);
      val = null;
    }
    return val;
  }

  public Timestamp getValueT(String strQuery) {
    Timestamp val = null;
    try {
      Statement stmt = dbConn.createStatement();
      ResultSet rs = stmt.executeQuery(strQuery);
      rs.next();
      val = rs.getTimestamp(1);
      rs.close();
      stmt.close();
    } catch (SQLException e) {
      DialogUtils.errorPrint(e,logger);
      val = null;
    } catch (Exception e) {
      DialogUtils.errorPrint(e,logger);
      val = null;
    }
    return val;
  }

  public String getUser() {
    String usr = getValueV("select user from dual");
    return usr;
  }

  public Timestamp getSysdate() {
    Timestamp tst = getValueT("select sysdate from dual");
    return tst;
  }

}
