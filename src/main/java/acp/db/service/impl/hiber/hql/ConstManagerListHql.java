package acp.db.service.impl.hiber.hql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Transaction;

import acp.db.service.impl.hiber.all.ManagerListHiber;
import acp.forms.dto.ConstDto;
import acp.utils.*;

public class ConstManagerListHql extends ManagerListHiber<ConstDto> {
  private static Logger logger = LoggerFactory.getLogger(ConstManagerListHql.class);

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

  private List<ConstDto> cacheObj = new ArrayList<>();

  public ConstManagerListHql() {
    tableName = "ConstClass";

    fields = new String[] { "id", "name", "value" };

    headers = new String[] {
        "ID"
      , Messages.getString("Column.Name")
      , Messages.getString("Column.Value") 
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
    strQueryCnt = QueryUtils.buildQuery("select count(*) from " + strFrom, strWhere, null);
  }

  private void setWhere(Map<String,String> mapFilter) {
    // ----------------------------------
    String vName = mapFilter.get("name"); 
    // ----------------------------------
    String phWhere = null;
    String str = null;
    // ---
    if (!QueryUtils.emptyString(vName)) {
      str = "upper(name) like upper('" + vName + "%')";
      phWhere = QueryUtils.strAddAnd(phWhere, str);
    }
    strWhere = QueryUtils.strAddAnd(strAwhere, phWhere);
  }

  @Override
  public List<ConstDto> queryAll() {
    cacheObj = fetchPage(-1,-1);
    return cacheObj;    
  }

  @Override
  public List<ConstDto> fetchPage(int startPos, int cntRows) {
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
  
  private ConstDto getObject(Object[] obj) {
    //---------------------------------------
    Long rsId = (Long) obj[0];
    String rsName = (String) obj[1];
    String rsValue = (String) obj[2];
    //---------------------------------------
    ConstDto objDto = new ConstDto();
    objDto.setId(rsId);
    objDto.setName(rsName);
    objDto.setValue(rsValue);
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
