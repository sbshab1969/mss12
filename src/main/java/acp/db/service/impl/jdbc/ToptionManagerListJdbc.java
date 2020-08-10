package acp.db.service.impl.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.sql.Date;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.service.IToptionManagerList;
import acp.forms.dto.ToptionDto;
import acp.utils.*;

public class ToptionManagerListJdbc extends ManagerListJdbc<ToptionDto> implements IToptionManagerList {
  private static Logger logger = LoggerFactory.getLogger(ToptionManagerListJdbc.class);

//  private String tableName;
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

  private List<ToptionDto> cacheObj = new ArrayList<>();

  private String path;
  private ArrayList<String> attrs;
  private int attrSize;
  private int attrMax = 5;
  private String attrPrefix;

  public ToptionManagerListJdbc(String path, ArrayList<String> attrs) {
    this.path = path;
    this.attrs = attrs;
    this.attrSize = attrs.size();
    String[] pathArray = path.split("/");
    this.attrPrefix = pathArray[pathArray.length - 1];

    createFields();
    
    strFields = QueryUtils.buildSelectFields(fields, null);

    createTable(-1L);  
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

  private void createFields() {
    fields = new String[attrSize + 3];
    headers = new String[attrSize + 3];
    types = new Class<?>[attrSize + 3];
    // ---
    int j = 0;
    fields[j] = "config_id";
    headers[j] = "ID";
    types[j] = Long.class;
    pkColumn = fields[j];
    // ---
    for (int i = 0; i < attrSize; i++) {
      j++;
      fields[j] = "P" + j;
      headers[j] = FieldConfig.getString(attrPrefix + "." + attrs.get(i));
      types[j] = String.class;
    }
    // ---
    j++;
    fields[j] = "date_begin";
    headers[j] = Messages.getString("Column.DateBegin");
    types[j] = Date.class;
    // ---
    j++;
    fields[j] = "date_end";
    headers[j] = Messages.getString("Column.DateEnd");
    types[j] = Date.class;
    // ---
  }

  public void createTable(Long src) {
    String res = "table(mss.spr_options(" + src + ",'" + path + "'";
    for (int i = 0; i < attrSize; i++) {
      res += ",'" + attrs.get(i) + "'";
    }
    for (int i = attrSize; i < attrMax; i++) {
      res += ",null";
    }
    res += "))";
    strFrom = res;
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
    strWhere = strAwhere;
  }
  
  @Override
  public List<ToptionDto> queryAll() {
    openQueryAll();  // forward
    cacheObj = fetchAll();
    closeQuery();
    return cacheObj;    
  }

  @Override
  public List<ToptionDto> fetchPage(int startPos, int cntRows) {
    cacheObj = fetchPart(startPos,cntRows);
    return cacheObj;
  }  

  private List<ToptionDto> fetchAll() {
    ArrayList<ToptionDto> cache = new ArrayList<>();
    try {
      while (rs.next()) {
        ToptionDto record = getObject(rs);
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

  private List<ToptionDto> fetchPart(int startPos, int cntRows) {
    ArrayList<ToptionDto> cache = new ArrayList<>();
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
        ToptionDto record = getObject(rs);
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
  
  private ToptionDto getObject(ResultSet rs) throws SQLException {
    //---------------------------------------
    Long rsId = rs.getLong("config_id");
    // ----------------------
    int j = 0;
    ArrayList<String> pArr = new ArrayList<>();
    for (int i = 0; i < attrSize; i++) {
      j = i + 1;
      String pj = rs.getString("p" + j);
      pArr.add(pj);
    }
    // ----------------------
    Date rsDateBegin = rs.getTimestamp("date_begin");
    Date rsDateEnd = rs.getTimestamp("date_end");
    //---------------------------------------
    ToptionDto obj = new ToptionDto();
    obj.setId(rsId);
    obj.setArrayP(pArr);
    obj.setDateBegin(rsDateBegin);
    obj.setDateEnd(rsDateEnd);
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
