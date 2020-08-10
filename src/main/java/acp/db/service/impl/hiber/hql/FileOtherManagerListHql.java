package acp.db.service.impl.hiber.hql;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.service.impl.hiber.all.ManagerListHiber;
import acp.forms.dto.FileOtherDto;
import acp.utils.*;

public class FileOtherManagerListHql extends ManagerListHiber<FileOtherDto> {
  private static Logger logger = LoggerFactory.getLogger(FileOtherManagerListHql.class);

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
  
  private String strQuery;
  private String strQueryCnt;

  private List<FileOtherDto> cacheObj = new ArrayList<>();

  public FileOtherManagerListHql(Long file_id) {
    tableName = "FileOtherClass";

    fields = new String[] { "id", "dateEvent", "descr" };

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
    strAwhere = "refId=" + file_id;
    strFrom = tableName;
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
    strQueryCnt = QueryUtils.buildQuery("select count(*) from " + strFrom, strWhere, null);
  }

  private void setWhere(Map<String,String> mapFilter) {
    strWhere = strAwhere;
  }

  @Override
  public List<FileOtherDto> queryAll() {
    cacheObj = fetchPage(-1,-1);
    return cacheObj;    
  }

  @Override
  public List<FileOtherDto> fetchPage(int startPos, int cntRows) {
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try { 
      // HQL -------------------------------------
      Query query = session.createQuery(strQuery);
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

  private void fillCache(Query query) {
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
  
  private FileOtherDto getObject(Object[] obj) {
    //---------------------------------------
    Long rsId = (Long) obj[0];
    Timestamp rsDateEvent = (Timestamp) obj[1];
    String rsDescr = (String) obj[2];
    //---------------------------------------
    FileOtherDto objDto = new FileOtherDto();
    objDto.setId(rsId);
    objDto.setDateEvent(rsDateEvent);
    objDto.setDescr(rsDescr);
    //---------------------------------------
    return objDto;
  }

  @Override
  public long countRecords() {
    long cntRecords = 0; 
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      Query query = session.createQuery(strQueryCnt);
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
