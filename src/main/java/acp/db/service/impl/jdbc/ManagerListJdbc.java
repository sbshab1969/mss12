package acp.db.service.impl.jdbc;

import acp.db.service.IManagerList;
import acp.utils.DialogUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ManagerListJdbc<T> extends ManagerBaseJdbc implements IManagerList<T> {
  private static Logger logger = LoggerFactory.getLogger(ManagerListJdbc.class);

  protected String strQuery;
  // protected String strQueryCnt;

  protected Statement stmt;
  protected ResultSet rs;

  @Override
  public void openQueryAll() {
    openCursor();
  }  

  @Override
  public void openQueryPage() {
    openCursor(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
  }  

  @Override
  public void closeQuery() {
    closeCursor();
  }

  private void openCursor() {
    // System.out.println("OpenCursor: " + dbConn);
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(strQuery);
    } catch (SQLException e) {
      DialogUtils.errorPrint(e,logger);
    } catch (Exception e) {
      DialogUtils.errorPrint(e,logger);
    }
  }

  private void openCursor(int typeCursor, int typeConcur) {
    /*
    ResultSet.TypeCursor:
      TYPE_FORWARD_ONLY
      TYPE_SCROLL_INSENSITIVE
      TYPE_SCROLL_SENSITIVE
      TYPE_FORWARD_ONLY
   ResultSet.TypeConcur:
      CONCUR_READ_ONLY;
      CONCUR_UPDATABLE;
      CONCUR_READ_ONLY;
 */
    // System.out.println("OpenCursor2: " + dbConn);
    try {
      stmt = dbConn.createStatement(typeCursor, typeConcur);
      rs = stmt.executeQuery(strQuery);
    } catch (SQLException e) {
      DialogUtils.errorPrint(e,logger);
    } catch (Exception e) {
      DialogUtils.errorPrint(e,logger);
    }
  }

  private void closeCursor() {
    // System.out.println("CloseCursor: " + dbConn);
    try {
      if (stmt != null) {
        stmt.close();
      }
    } catch (SQLException e) {
      DialogUtils.errorPrint(e,logger);
    } catch (Exception e) {
      DialogUtils.errorPrint(e,logger);
    }
    stmt = null;
    rs = null;
  }

}
