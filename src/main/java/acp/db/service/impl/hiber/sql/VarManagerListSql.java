package acp.db.service.impl.hiber.sql;

import java.util.Date;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.service.impl.hiber.all.ManagerListHiber;
import acp.forms.dto.VarDto;
import acp.utils.*;

public class VarManagerListSql extends ManagerListHiber<VarDto> {
  private static Logger logger = LoggerFactory.getLogger(VarManagerListSql.class);

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

  private List<VarDto> cacheObj = new ArrayList<>();

  public VarManagerListSql() {
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
    
    hiberTypes = new AbstractSingleColumnStandardBasicType<?>[] { 
      LongType.INSTANCE
    , StringType.INSTANCE
    , StringType.INSTANCE
    , DoubleType.INSTANCE
    , StringType.INSTANCE
    , DateType.INSTANCE
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
    strQueryCnt = QueryUtils.buildQuery("select count(*) cnt from " + strFrom, strWhere, null);
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
    cacheObj = fetchPage(-1,-1);
    return cacheObj;    
  }

  @Override
  public List<VarDto> fetchPage(int startPos, int cntRows) {
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try { 
      // HQL -------------------------------------
      SQLQuery query = session.createSQLQuery(strQuery);
      for (int i=0; i<hiberTypes.length; i++) {
        query.addScalar(fields[i], hiberTypes[i]);
      }
      // -----------------------------------------
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
  
  private VarDto getObject(Object[] obj) {
    //---------------------------------------
    Long rsId = (Long) obj[0];
    String rsName = (String) obj[1];
    String rsType = (String) obj[2];
    Double rsValuen = (Double) obj[3];
    String rsValuev = (String) obj[4];
    Date rsValued = (Date) obj[5];
    //---------------------------------------
    VarDto objDto = new VarDto();
    objDto.setId(rsId);
    objDto.setName(rsName);
    objDto.setType(rsType);
    objDto.setValuen(rsValuen);
    objDto.setValuev(rsValuev);
    objDto.setValued(rsValued);
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
