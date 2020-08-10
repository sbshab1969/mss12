package acp.db.service.impl.hiber.sql;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.service.impl.hiber.all.ManagerListHiber;
import acp.forms.dto.ConfigDto;
import acp.utils.*;

public class ConfigManagerListSql extends ManagerListHiber<ConfigDto> {
  private static Logger logger = LoggerFactory.getLogger(ConfigManagerListSql.class);

  private String tableName;
  private String[] fields;
  private String[] headers;
  private Class<?>[] types;
  private AbstractSingleColumnStandardBasicType<?>[] hiberTypes;

  private String pkColumn;
  private Long seqId;

  private String strFields;
  private String strFrom;
  private String strAwhere;
  private String strWhere;
  private String strOrder;
  
  private String strQuery;
  private String strQueryCnt;

  private List<ConfigDto> cacheObj = new ArrayList<>();

  public ConfigManagerListSql() {
    tableName = "mss_options";

    fields = new String[] { 
        "msso_id"
      , "msso_name"
      , "msss_name" 
      , "msso_dt_begin"
      , "msso_dt_end"
      , "msso_comment"
      , "msso_owner"
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

    hiberTypes = new AbstractSingleColumnStandardBasicType<?>[] { 
      LongType.INSTANCE
    , StringType.INSTANCE
    , StringType.INSTANCE
    , TimestampType.INSTANCE
    , TimestampType.INSTANCE
    , StringType.INSTANCE
    , StringType.INSTANCE
    };

    pkColumn = fields[0];
    seqId = 1000L;

    strFields = QueryUtils.buildSelectFields(fields, null);
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
    strQueryCnt = QueryUtils.buildQuery("select count(*) cnt from " + strFrom, strWhere, null);
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
    cacheObj = fetchPage(-1,-1);
    return cacheObj;    
  }

  @Override
  public List<ConfigDto> fetchPage(int startPos, int cntRows) {
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try { 
      // HQL -------------------------------------
      SQLQuery query = session.createSQLQuery(strQuery);
      for (int i=0; i<hiberTypes.length; i++) {
        query.addScalar(fields[i], hiberTypes[i]);
      }
      // ------------------------------------------
      if (startPos>0) {
        query.setFirstResult(startPos-1);  // Hibernate начинает с 0
      }
      if (cntRows>0) {
        query.setMaxResults(cntRows);
      }  
      // ==============
      fillCache(query);
      // ==============
      tx.commit();
    } catch (HibernateException e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
    } catch (Exception e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
    } finally {
      session.close();
    }  
    return cacheObj;    
  }  

  private void fillCache(SQLQuery query) {
    logger.info("\nQuery string: " + query.getQueryString());
    // ============================
    List<?> objList = query.list();
    // ============================
    cacheObj = new ArrayList<>();
    for (int i=0; i < objList.size(); i++) {
      Object[] obj = (Object[]) objList.get(i);
      cacheObj.add(getObject(obj));
    }
  }
  
  private ConfigDto getObject(Object[] obj) {
    //---------------------------------------
    Long rsId = (Long) obj[0];
    String rsName = (String) obj[1];
    String rsSourceName = (String) obj[2];
    Timestamp rsDateBegin = (Timestamp) obj[3];
    Timestamp rsDateEnd = (Timestamp) obj[4];
    String rsComment = (String) obj[5];
    String rsOwner = (String) obj[6];
    //---------------------------------------
    ConfigDto objDto = new ConfigDto();
    objDto.setId(rsId);
    objDto.setName(rsName);
    objDto.setSourceName(rsSourceName);
    objDto.setDateBegin(rsDateBegin);
    objDto.setDateEnd(rsDateEnd);
    objDto.setComment(rsComment);
    objDto.setOwner(rsOwner);
    //---------------------------------------
    return objDto;
  }

  @Override
  public long countRecords() {
    long cntRecords = 0; 
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      SQLQuery query = session.createSQLQuery(strQueryCnt)
              .addScalar("cnt", LongType.INSTANCE);
      cntRecords = (Long) query.uniqueResult();
      tx.commit();
    } catch (HibernateException e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
    } catch (Exception e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
    } finally {
      session.close();
    }
    return cntRecords;    
  }

}
