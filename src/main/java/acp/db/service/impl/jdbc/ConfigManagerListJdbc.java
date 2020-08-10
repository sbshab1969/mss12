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

import acp.forms.dto.ConfigDto;
import acp.utils.*;

public class ConfigManagerListJdbc extends ManagerListJdbc<ConfigDto> {
  private static Logger logger = LoggerFactory.getLogger(ConfigManagerListJdbc.class);

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

  private List<ConfigDto> cacheObj = new ArrayList<>();

  public ConfigManagerListJdbc() {
    tableName = "mss_options";
    
    fields = new String[] { 
        "msso_id"
      , "msso_name"
      , "msso_dt_begin"
      , "msso_dt_end"
      , "msso_comment"
      , "msso_owner"
      , "msso_msss_id"
      , "msss_name" 
    };

    headers = new String[] { 
        "ID"
      , Messages.getString("Column.Name")
      , Messages.getString("Column.SourceName")
      , Messages.getString("Column.DateBegin")
      , Messages.getString("Column.DateEnd")
      , Messages.getString("Column.Comment")
      , Messages.getString("Column.Owner") 
    };
    
    types = new Class<?>[] { 
        Long.class
      , String.class
      , String.class
      , Date.class
      , Date.class
      , String.class
      , String.class 
    };

    pkColumn = fields[0];
    seqId = 1000L;

    strFields = QueryUtils.buildSelectFields(fields, null);
//    strFrom = "mss_options, mss_source";
    strFrom = tableName + ", mss_source";
    strAwhere = "msso_msss_id=msss_id";
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
    String vOwner = mapFilter.get("owner"); 
    String vSourceId = mapFilter.get("sourceId"); 
    // ----------------------------------
    String phWhere = null;
    String str = null;
    // ---
    if (!QueryUtils.emptyString(vName)) {
      str = "upper(msso_name) like upper('" + vName + "%')";
      phWhere = QueryUtils.strAddAnd(phWhere, str);
    }
    // ---
    if (!QueryUtils.emptyString(vOwner)) {
      str = "upper(msso_owner) like upper('" + vOwner + "%')";
      phWhere = QueryUtils.strAddAnd(phWhere, str);
    }
    // ---
    if (!QueryUtils.emptyString(vSourceId)) {
      str = "msso_msss_id=" + vSourceId;
      phWhere = QueryUtils.strAddAnd(phWhere, str);
    }
    // ---
    strWhere = QueryUtils.strAddAnd(strAwhere, phWhere);
  }
  
  @Override
  public List<ConfigDto> queryAll() {
    openQueryAll();  // forward
    cacheObj = fetchAll();
    closeQuery();
    return cacheObj;    
  }

  @Override
  public List<ConfigDto> fetchPage(int startPos, int cntRows) {
    cacheObj = fetchPart(startPos,cntRows);
    return cacheObj;
  }  

  private List<ConfigDto> fetchAll() {
    ArrayList<ConfigDto> cache = new ArrayList<>();
    try {
      while (rs.next()) {
        ConfigDto record = getObject(rs);
        cache.add(record);
      }
    } catch (SQLException e) {
      DialogUtils.errorPrint(e,logger);
      cache = new ArrayList<>();
    }
    return cache;
  }

  private List<ConfigDto> fetchPart(int startPos, int cntRows) {
    ArrayList<ConfigDto> cache = new ArrayList<>();
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
        ConfigDto record = getObject(rs);
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
  
  private ConfigDto getObject(ResultSet rs) throws SQLException {
    //---------------------------------------
    Long rsId = rs.getLong("msso_id");
    String rsName = rs.getString("msso_name");
    Date rsDateBegin = rs.getTimestamp("msso_dt_begin");
    Date rsDateEnd = rs.getTimestamp("msso_dt_end");
    String rsComment = rs.getString("msso_comment");
    String rsOwner = rs.getString("msso_owner");
    Long rsSourceId = rs.getLong("msso_msss_id");
    String rsSourceName = rs.getString("msss_name");
    //---------------------------------------
    ConfigDto obj = new ConfigDto();
    obj.setId(rsId);
    obj.setName(rsName);
    obj.setDateBegin(rsDateBegin);
    obj.setDateEnd(rsDateEnd);
    obj.setComment(rsComment);
    obj.setOwner(rsOwner);
    obj.setSourceId(rsSourceId);
    obj.setSourceName(rsSourceName);
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
