package acp.db.service.impl.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.forms.dto.VarDto;
import acp.utils.*;

public class VarManagerListJdbc extends ManagerListJdbc<VarDto> {
  private static Logger logger = LoggerFactory.getLogger(VarManagerListJdbc.class);

  private String tableName;
  private String[] fields;
  private String[] headers;
  private Class<?>[] types;

  private String pkColumn;
  private Long seqId;

  private String strFields;
  private String strFrom;
  private String strAwhere;
  private String strWhere;
  private String strOrder;

  // private String strQuery;
  private String strQueryCnt;

  private List<VarDto> cacheObj = new ArrayList<>();

  public VarManagerListJdbc() {
    tableName = "mss_vars";
    
    fields = new String[] { "mssv_id", "mssv_name", "mssv_type"
        ,"mssv_valuen", "mssv_valuev", "mssv_valued" };

    headers = new String[] { 
        "ID"
      , Messages.getString("Column.Name")
      , Messages.getString("Column.Type")
      , Messages.getString("Column.Number")
      , Messages.getString("Column.Varchar")
      , Messages.getString("Column.Date") };
    
    types = new Class<?>[] { 
        Long.class
      , String.class
      , String.class
      , Double.class
      , String.class
      , Date.class
    };

    pkColumn = fields[0];
    seqId = 1000L;

    strFields = QueryUtils.buildSelectFields(fields, null);
    strFrom = tableName;
    strAwhere = null;
    strWhere = strAwhere;
    strOrder = pkColumn;

    prepareQuery(null);
  }

  @Override
  public String[] getHeaders() {
    return headers;    
  }

  @Override
  public Class<?>[] getTypes() {
    return types;    
  }

  @Override
  public Long getSeqId() {
    return seqId;
  }

  @Override
  public void prepareQuery(Map<String,String> mapFilter) {
    if (mapFilter != null) {
      setWhere(mapFilter);
    } else {
      strWhere = strAwhere;
    }
    strQuery = QueryUtils.buildQuery(strFields, strFrom, strWhere, strOrder);
    strQueryCnt = QueryUtils.buildQuery("select count(*) cnt", strFrom, strWhere, null);
  }

  private void setWhere(Map<String,String> mapFilter) {
    // ----------------------------------
    String vName = mapFilter.get("name"); 
    // ----------------------------------
    String phWhere = null;
    String str = null;
    // ---
    if (!QueryUtils.emptyString(vName)) {
      str = "upper(mssv_name) like upper('" + vName + "%')";
      phWhere = QueryUtils.strAddAnd(phWhere, str);
    }
    // ---
    strWhere = QueryUtils.strAddAnd(strAwhere, phWhere);
  }

  @Override
  public List<VarDto> queryAll() {
    openQueryAll();  // forward
    cacheObj = fetchAll();
    closeQuery();
    return cacheObj;    
  }

  @Override
  public List<VarDto> fetchPage(int startPos, int cntRows) {
    cacheObj = fetchPart(startPos,cntRows);
    return cacheObj;
  }  

  private List<VarDto> fetchAll() {
    ArrayList<VarDto> cache = new ArrayList<>();
    try {
      while (rs.next()) {
        VarDto record = getObject(rs);
        cache.add(record);
      }
    } catch (SQLException e) {
      DialogUtils.errorPrint(e,logger);
      cache = new ArrayList<>();
    } catch (Exception e) {
      DialogUtils.errorPrint(e,logger);
      cache = new ArrayList<>();
    }
    return cache;
  }

  private List<VarDto> fetchPart(int startPos, int cntRows) {
    ArrayList<VarDto> cache = new ArrayList<>();
    if (startPos <= 0 || cntRows<=0) { 
      return cache;
    }
    try {
      boolean res = rs.absolute(startPos);
      if (res == false) {
        return cache;
      }
      int curRow = 0;
      //------------------------------------------
      do {
        curRow++;
        VarDto record = getObject(rs);
        cache.add(record);
        if (curRow>=cntRows) break;
        //----------------------------------------
      } while (rs.next());
      //------------------------------------------
    } catch (SQLException e) {
      DialogUtils.errorPrint(e,logger);
      cache = new ArrayList<>();
    } catch (Exception e) {
      DialogUtils.errorPrint(e,logger);
      cache = new ArrayList<>();
    }
    return cache;
  }

  private VarDto getObject(ResultSet rs) throws SQLException {
    //---------------------------------------
    Long rsId = rs.getLong("mssv_id");
    String rsName = rs.getString("mssv_name");
    String rsType = rs.getString("mssv_type");
//    double rsValuen = rs.getDouble("mssv_valuen");
    String strValuen = rs.getString("mssv_valuen");
    Double rsValuen = null;
    if (strValuen != null) {
      rsValuen = Double.valueOf(strValuen);
    }
    String rsValuev = rs.getString("mssv_valuev");
    // Date rsValued = rs.getDate("mssv_valued");
    Date rsValued = rs.getTimestamp("mssv_valued");
    //---------------------------------------
    VarDto obj = new VarDto();
    obj.setId(rsId);
    obj.setName(rsName);
    obj.setType(rsType);
    obj.setValuen(rsValuen);
    obj.setValuev(rsValuev);
    obj.setValued(rsValued);
    //---------------------------------------
    return obj;    
  }

  @Override
  public long countRecords() {
    long cntRecords = 0; 
    try {
      Statement stmt = dbConn.createStatement();
      ResultSet rs = stmt.executeQuery(strQueryCnt);
      rs.next();
      cntRecords = rs.getLong(1);
      rs.close();
      stmt.close();
    } catch (SQLException e) {
      DialogUtils.errorPrint(e,logger);
      cntRecords = 0; 
    } catch (Exception e) {
      DialogUtils.errorPrint(e,logger);
      cntRecords = 0; 
    }
    return cntRecords;    
  }

}
