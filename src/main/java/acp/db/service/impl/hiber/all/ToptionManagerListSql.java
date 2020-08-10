package acp.db.service.impl.hiber.all;

import java.util.Date;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.LongType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.service.IToptionManagerList;
import acp.forms.dto.ToptionDto;
import acp.utils.*;

public class ToptionManagerListSql extends ManagerListHiber<ToptionDto> 
                                   implements IToptionManagerList {
  private static Logger logger = LoggerFactory.getLogger(ToptionManagerListSql.class);

  // private String tableName;
  private String[] fields;
  private String[] headers;
  private Class<?>[] types;
  //  private AbstractSingleColumnStandardBasicType<?>[] hiberTypes;

  private String pkColumn;
  private Long seqId;

  private String strFields;
  private String strFrom;
  private String strAwhere;
  private String strWhere;
  private String strOrder;
  
  private String strQuery;
  private String strQueryCnt;

  private List<ToptionDto> cacheObj = new ArrayList<>();

  private String path;
  private ArrayList<String> attrs;
  private int attrSize;
  private int attrMax = 5;
  private String attrPrefix;

  public ToptionManagerListSql(String path, ArrayList<String> attrs) {
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

  @Override
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
    strQueryCnt = QueryUtils.buildQuery("select count(*)", strFrom, strWhere, null);
  }

  private void setWhere(Map<String,String> mapFilter) {
    strWhere = strAwhere;
  }
  
  @Override
  public List<ToptionDto> queryAll() {
    cacheObj = fetchPage(-1,-1);
    return cacheObj;    
  }

  @Override
  public List<ToptionDto> fetchPage(int startPos, int cntRows) {
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try { 
      // HQL -------------------------------------
      SQLQuery query = session.createSQLQuery(strQuery);
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
  
  private ToptionDto getObject(Object[] obj) {
    //---------------------------------------
    BigDecimal idBigDec = (BigDecimal) obj[0];
    Long rsId = idBigDec.longValue();
    // ----------------------
    int j = 0;
    ArrayList<String> pArr = new ArrayList<>();
    for (int i = 0; i < attrSize; i++) {
      j = i + 1;
      String pj = (String) obj[j];
      pArr.add(pj);
    }
    // ----------------------
    Date rsDateBegin = (Date) obj[++j];
    Date rsDateEnd = (Date) obj[++j];
    //---------------------------------------
    ToptionDto objDto = new ToptionDto();
    objDto.setId(rsId);
    objDto.setArrayP(pArr);
    objDto.setDateBegin(rsDateBegin);
    objDto.setDateEnd(rsDateEnd);
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
