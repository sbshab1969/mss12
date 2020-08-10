package acp.db.service.impl.hiber.crit2;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
//import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.domain.ConfigClass;
import acp.db.service.impl.hiber.all.ManagerListHiber;
import acp.forms.dto.ConfigDto;
import acp.utils.*;

public class ConfigManagerListCrit2 extends ManagerListHiber<ConfigDto> {
  private static Logger logger = LoggerFactory.getLogger(ConfigManagerListCrit2.class);

  private Class<?> tableClass; 
  private String[] fields;
  private String[] headers;
  private Class<?>[] types;
  
  private String pkColumn;
  private Long seqId;

  private Map<String,String> mapFilter;
  
  private List<ConfigDto> cacheObj = new ArrayList<>();

  public ConfigManagerListCrit2() {
    tableClass = ConfigClass.class;

    fields = new String[] { 
        "cfg.id"
      , "cfg.name"
      , "cfg.dateBegin"
      , "cfg.dateEnd"
      , "cfg.comment"
      , "cfg.owner"
      , "src.name"
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
    this.mapFilter = mapFilter;
  }
 
  private void addRestrictions(Criteria crit) {
    // ----------------------------------
    String vName = mapFilter.get("name"); 
    String vOwner = mapFilter.get("owner"); 
    String vSourceId = mapFilter.get("sourceId"); 
    // ----------------------------------
    if (!QueryUtils.emptyString(vName)) {
      crit.add(Restrictions.like("name", vName, MatchMode.START).ignoreCase());
    }
    // ---
    if (!QueryUtils.emptyString(vOwner)) {
      crit.add(Restrictions.like("owner", vOwner, MatchMode.START).ignoreCase());
    }
    // ---
    if (!QueryUtils.emptyString(vSourceId)) {
      Long longSourceId = Long.parseLong(vSourceId);
      crit.add(Restrictions.eq("sourceId", longSourceId));
    }
    // ----------------------------------
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
      // -------------------------------------
      ProjectionList pl = Projections.projectionList();
      for (int i=0; i<fields.length; i++) {
        pl.add(Property.forName(fields[i]));
      }
      // -----------
      Criteria query = session.createCriteria(tableClass,"cfg");
      query.createCriteria("source","src"); 
//      query.createCriteria("source","src",JoinType.LEFT_OUTER_JOIN); 
      query.setProjection(pl);
//      query.add(Restrictions.sqlRestriction(strWhere));
      addRestrictions(query);
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
  
  private ConfigDto getObject(Object[] obj) {
    //---------------------------------------
    Long rsId = (Long) obj[0];
    String rsName = (String) obj[1];
    Timestamp rsDateBegin = (Timestamp) obj[2];
    Timestamp rsDateEnd = (Timestamp) obj[3];
    String rsComment = (String) obj[4];
    String rsOwner = (String) obj[5];
    String rsSourceName = (String) obj[6];
    //---------------------------------------
    ConfigDto objDto = new ConfigDto();
    objDto.setId(rsId);
    objDto.setName(rsName);
    objDto.setDateBegin(rsDateBegin);
    objDto.setDateEnd(rsDateEnd);
    objDto.setComment(rsComment);
    objDto.setOwner(rsOwner);
    objDto.setSourceName(rsSourceName);
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
//      query.add(Restrictions.sqlRestriction(strWhere));
      addRestrictions(query);
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
