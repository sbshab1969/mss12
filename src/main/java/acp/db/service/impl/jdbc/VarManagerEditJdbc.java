package acp.db.service.impl.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.service.IVarManagerEdit;
import acp.forms.dto.VarDto;
import acp.utils.*;

public class VarManagerEditJdbc extends ManagerBaseJdbc implements IVarManagerEdit {
  private static Logger logger = LoggerFactory.getLogger(VarManagerEditJdbc.class);
  
  private ManagerUtilJdbc mngUtil = new ManagerUtilJdbc();

  @Override
  public VarDto select(Long objId) {
    // ------------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("select mssv_id, mssv_name, mssv_type, mssv_valuen, mssv_valuev, mssv_valued");
    sbQuery.append("  from mss_vars");
    sbQuery.append(" where mssv_id=?");
    String strQuery = sbQuery.toString();
    // ------------------------------------------------------
    VarDto varObj = null;
    try {
      PreparedStatement ps = dbConn.prepareStatement(strQuery);
      ps.setLong(1, objId);
      ResultSet rsq = ps.executeQuery();
      if (rsq.next()) {
        String rsqName = rsq.getString("mssv_name");
        String rsqType = rsq.getString("mssv_type");
        String strValn = rsq.getString("mssv_valuen");
        Double rsqValn = null;
        if (strValn != null) {
          rsqValn = Double.valueOf(strValn);
        }
        String rsqValv = rsq.getString("mssv_valuev");
        Date rsqVald = rsq.getTimestamp("mssv_valued");
        // ---------------------
        varObj = new VarDto();
        varObj.setId(objId);
        varObj.setName(rsqName);
        varObj.setType(rsqType);
        varObj.setValuen(rsqValn);
        varObj.setValuev(rsqValv);
        varObj.setValued(rsqVald);
        // ---------------------
      }
      rsq.close();
      ps.close();
    } catch (SQLException e) {
      DialogUtils.errorPrint(e,logger);
      varObj = null;
    } catch (Exception e) {
      DialogUtils.errorPrint(e,logger);
      varObj = null;
    }
    // ------------------------------------------------------
    return varObj;
  }

  @Override
  public Long insert(VarDto newObj) {
    // ------------------------------------------------------
    Long objId = mngUtil.getValueL("select mssv_seq.nextval from dual");
    // ------------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("insert into mss_vars");
    sbQuery.append(" (mssv_id, mssv_name, mssv_type, mssv_len,");
    sbQuery.append(" mssv_valuen, mssv_valuev, mssv_valued, mssv_last_modify, mssv_owner)");
    sbQuery.append(" values (?, upper(?), ?, 120, ?, ?, ?, sysdate, user)");
    String strQuery = sbQuery.toString();
    // ------------------------------------------------------
    try {
      PreparedStatement ps = dbConn.prepareStatement(strQuery);
      ps.setLong(1, objId);
      ps.setString(2, newObj.getName());
      ps.setString(3, newObj.getType());

      Double valn = newObj.getValuen();
      if (valn != null) {
        ps.setDouble(4, valn);
      } else {
        ps.setNull(4, Types.DOUBLE);
      }
      
      ps.setString(5, newObj.getValuev());
      
      Timestamp tsValued = DateUtils.util2ts(newObj.getValued());
      ps.setTimestamp(6,tsValued);
      // --------------------------
      ps.executeUpdate();
      // --------------------------
      ps.close();
    } catch (SQLException e) {
      DialogUtils.errorPrint(e,logger);
      objId = null;
    } catch (Exception e) {
      DialogUtils.errorPrint(e,logger);
      objId = null;
    }
    // ------------------------------------------------------
    return objId;
  }

  @Override
  public boolean update(VarDto newObj) {
    boolean res = false;
    // -----------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("update mss_vars");
    sbQuery.append("   set mssv_name=upper(?)");
    sbQuery.append("      ,mssv_type=?");
    sbQuery.append("      ,mssv_valuen=?");
    sbQuery.append("      ,mssv_valuev=?");
    sbQuery.append("      ,mssv_valued=?");
    sbQuery.append("      ,mssv_last_modify=sysdate");
    sbQuery.append("      ,mssv_owner=user");
    sbQuery.append(" where mssv_id=?");
    String strQuery = sbQuery.toString();
    // -----------------------------------------
    try {
      PreparedStatement ps = dbConn.prepareStatement(strQuery);
      ps.setString(1, newObj.getName());
      ps.setString(2, newObj.getType());
      Double valn = newObj.getValuen();
      if (valn != null) {
        ps.setDouble(3, valn);
      } else {
        ps.setNull(3, Types.DOUBLE);
      }
      ps.setString(4, newObj.getValuev());
      Timestamp tsValued = DateUtils.util2ts(newObj.getValued());
      ps.setTimestamp(5,tsValued);
      ps.setLong(6, newObj.getId());
      // --------------------------
      ps.executeUpdate();
      // --------------------------
      ps.close();
      res = true;
    } catch (SQLException e) {
      DialogUtils.errorPrint(e,logger);
      res = false;
    } catch (Exception e) {
      DialogUtils.errorPrint(e,logger);
      res = false;
    }
    // -----------------------------------------
    return res;
  }

  @Override
  public boolean delete(Long objId) {
    boolean res = false;
    // -----------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("delete from mss_vars where mssv_id=?");
    String strQuery = sbQuery.toString();
    // -----------------------------------------------------
    try {
      PreparedStatement ps = dbConn.prepareStatement(strQuery);
      ps.setLong(1, objId);
      // --------------------------
      ps.executeUpdate();
      // --------------------------
      ps.close();
      res = true;
    } catch (SQLException e) {
      DialogUtils.errorPrint(e,logger);
      res = false;
    } catch (Exception e) {
      DialogUtils.errorPrint(e,logger);
      res = false;
    }
    // -----------------------------------------------------
    return res;
  }

  @Override
  public void fillVars(Map<String, String> varMap) {
    // ------------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("select t.* from mss_vars t");
    sbQuery.append(" where upper(mssv_name) like 'CERT%'");
    sbQuery.append(" or upper(mssv_name) = 'VERSION_MSS' order by mssv_name");
    String strQuery = sbQuery.toString();
    // ------------------------------------------------------
    try {
      Statement st = dbConn.createStatement();
      ResultSet rsq = st.executeQuery(strQuery);
      while (rsq.next()) {
        String rsqName = rsq.getString("mssv_name");
        String valuev = null;
        Date valued = null;
        if (rsqName.startsWith("CERT")) {
          valuev = rsq.getString("mssv_valuev");
          varMap.put(rsqName, valuev);
        } else if (rsqName.equals("VERSION_MSS")) {
          valuev = rsq.getString("mssv_valuev");
          valued = rsq.getDate("mssv_valued");
          varMap.put("VERSION", valuev);
          varMap.put("VERSION_DATE", DateUtils.date2Str(valued));
        }
      }
      rsq.close();
      st.close();
    } catch (SQLException e) {
      DialogUtils.errorPrint(e, logger);
    } catch (Exception e) {
      DialogUtils.errorPrint(e, logger);
    }
    // ---------------------------------------------
  }

}
