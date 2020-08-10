package acp.db.service.impl.hiber.crit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import acp.db.domain.ConstClass;
import acp.db.service.impl.hiber.all.ManagerListHiber;
import acp.forms.dto.ConstDto;
import acp.utils.*;

public class ConstManagerListCrit extends ManagerListHiber<ConstDto> {
  private static Logger logger = LoggerFactory.getLogger(ConstManagerListCrit.class);

  private Class<?> tableClass; 
  private String[] fields;
  private String[] headers;
  private Class<?>[] types;
  
  private String pkColumn;
  private Long seqId;

  private String strAwhere;
  private String strWhere;

  private List<ConstDto> cacheObj = new ArrayList<>();

  public ConstManagerListCrit() {
    tableClass = ConstClass.class; 

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

    strAwhere = null;
    strWhere = strAwhere;

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
  }

  private void setWhere(Map<String,String> mapFilter) {
    // ----------------------------------
    String vName = mapFilter.get("name"); 
    // ----------------------------------
    String phWhere = null;
    String str = null;
    // ---
    if (!QueryUtils.emptyString(vName)) {
      str = "upper(mssc_name) like upper('" + vName + "%')";
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
      // -------------------------------------
      ProjectionList pl = Projections.projectionList();
      for (int i=0; i<fields.length; i++) {
        pl.add(Property.forName(fields[i]));
      }
      // -----------
      Criteria query = session.createCriteria(tableClass);
      query.setProjection(pl);
      query.add(Restrictions.sqlRestriction(strWhere));
      query.addOrder(Order.asc(pkColumn));
      // -----------
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

  private void fillCache(Criteria query) {
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
      //-----------------------------------------------------------
      Criteria query = session.createCriteria(tableClass);
      // ---
      ProjectionList pl = Projections.projectionList();
      pl.add(Projections.rowCount());
      // ---
      query.setProjection(pl);
      query.add(Restrictions.sqlRestriction(strWhere));
      //-----------------------------------------------------------
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
