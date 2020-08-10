package acp.db.service.impl.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.forms.dto.SourceDto;
import acp.utils.*;

public class SourceManagerListJdbc extends ManagerListJdbc<SourceDto> {
  private static Logger logger = LoggerFactory.getLogger(SourceManagerListJdbc.class);

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

  private List<SourceDto> cacheObj = new ArrayList<>();

  public SourceManagerListJdbc() {
    tableName = "mss_source";

    fields = new String[] { "msss_id", "msss_name", "msss_owner" };
    
    headers = new String[] { 
        "ID"
      , Messages.getString("Column.Name")
      , Messages.getString("Column.Owner") 
    };
    
    types = new Class<?>[] { 
        Long.class
      , String.class
      , String.class
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
    String vOwner = mapFilter.get("owner");;
    // ----------------------------------
    String phWhere = null;
    String str = null;
    // ---
    if (!QueryUtils.emptyString(vName)) {
      str = "upper(msss_name) like upper('" + vName + "%')";
      phWhere = QueryUtils.strAddAnd(phWhere, str);
    }
    // ---
    if (!QueryUtils.emptyString(vOwner)) {
      str = "upper(msss_owner) like upper('" + vOwner + "%')";
      phWhere = QueryUtils.strAddAnd(phWhere, str);
    }
    // ---
    strWhere = QueryUtils.strAddAnd(strAwhere, phWhere);
  }

  @Override
  public List<SourceDto> queryAll() {
    openQueryAll();  // forward
    cacheObj = fetchAll();
    closeQuery();
    return cacheObj;    
  }

  @Override
  public List<SourceDto> fetchPage(int startPos, int cntRows) {
    cacheObj = fetchPart(startPos,cntRows);
    return cacheObj;
  }  

  private List<SourceDto> fetchAll() {
    ArrayList<SourceDto> cache = new ArrayList<>();
    try {
      while (rs.next()) {
        SourceDto record = getObject(rs);
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

  private List<SourceDto> fetchPart(int startPos, int cntRows) {
    ArrayList<SourceDto> cache = new ArrayList<>();
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
        SourceDto record = getObject(rs);
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

  private SourceDto getObject(ResultSet rs) throws SQLException {
    //---------------------------------------
    Long rsId = rs.getLong("msss_id");
    String rsName = rs.getString("msss_name");
    String rsOwner = rs.getString("msss_owner");
    //---------------------------------------
    SourceDto obj = new SourceDto();
    obj.setId(rsId);
    obj.setName(rsName);
    obj.setOwner(rsOwner);
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
