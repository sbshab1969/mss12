package acp.db.service.impl.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.forms.dto.FileOtherDto;
import acp.utils.*;

public class FileOtherManagerListJdbc extends ManagerListJdbc<FileOtherDto> {
  private static Logger logger = LoggerFactory.getLogger(FileOtherManagerListJdbc.class);

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

  private List<FileOtherDto> cacheObj = new ArrayList<>();

  public FileOtherManagerListJdbc(Long file_id) {
    tableName = "mss_logs";

    fields = new String[] { "mssl_id", "mssl_dt_event", "mssl_desc" };
    
    headers = new String[] { "ID"
      , Messages.getString("Column.Time")
      , Messages.getString("Column.Desc") 
    };
    
    types = new Class<?>[] { 
        Long.class
      , Timestamp.class
      , String.class
    };
    
    pkColumn = fields[0];
    seqId = 1000L;

    strFields = QueryUtils.buildSelectFields(fields, null);
    strFrom = tableName;
    strAwhere = "mssl_ref_id=" + file_id;
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
    strWhere = strAwhere;
  }

  @Override
  public List<FileOtherDto> queryAll() {
    openQueryAll();  // forward
    cacheObj = fetchAll();
    closeQuery();
    return cacheObj;    
  }

  @Override
  public List<FileOtherDto> fetchPage(int startPos, int cntRows) {
    cacheObj = fetchPart(startPos,cntRows);
    return cacheObj;
  }  

  private List<FileOtherDto> fetchAll() {
    ArrayList<FileOtherDto> cache = new ArrayList<>();
    try {
      while (rs.next()) {
        FileOtherDto record = getObject(rs);
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

  private List<FileOtherDto> fetchPart(int startPos, int cntRows) {
    ArrayList<FileOtherDto> cache = new ArrayList<>();
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
        FileOtherDto record = getObject(rs);
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
  
  private FileOtherDto getObject(ResultSet rs) throws SQLException {
    //---------------------------------------
    Long rsId = rs.getLong("mssl_id");
    Timestamp rsDateEvent = rs.getTimestamp("mssl_dt_event");
    String rsDescr = rs.getString("mssl_desc");
    //---------------------------------------
    FileOtherDto obj = new FileOtherDto();
    obj.setId(rsId);
    obj.setDateEvent(rsDateEvent);
    obj.setDescr(rsDescr);
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
