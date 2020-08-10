package acp.db.service.impl.hiber.crit2;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.domain.FileLoadClass;
import acp.db.service.impl.hiber.all.ManagerListHiber;
import acp.forms.dto.FileLoadDto;
import acp.utils.*;

public class FileLoadManagerListCrit2 extends ManagerListHiber<FileLoadDto> {
  private static Logger logger = LoggerFactory.getLogger(FileLoadManagerListCrit2.class);

  private Class<?> tableClass; 
  private String[] fields;
  private String[] headers;
  private Class<?>[] types;
  
  private String pkColumn;
  private Long seqId;

  private Map<String,String> mapFilter;

  private List<FileLoadDto> cacheObj = new ArrayList<>();

  public FileLoadManagerListCrit2() {
    tableClass = FileLoadClass.class;

    fields = new String[] { "id", "name", "md5", "owner", "dateWork", "recAll" };

    headers = new String[] { 
        "ID"
      , Messages.getString("Column.FileName")
      , "MD5"
      , Messages.getString("Column.Owner")
      , Messages.getString("Column.DateWork")
      , Messages.getString("Column.RecordCount") 
    };
    
    types = new Class<?>[] { 
        Long.class
      , String.class
      , String.class
      , String.class
      , Timestamp.class
      , int.class
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

  private void addRestrictions(Criteria crit) throws ParseException {
    // ----------------------------------
    String vName = mapFilter.get("name");
    String vOwner = mapFilter.get("owner");
    String vDateWorkBeg = mapFilter.get("dateWorkBeg");
    String vDateWorkEnd = mapFilter.get("dateWorkEnd");
    String vRecAllBeg = mapFilter.get("recAllBeg");
    String vRecAllEnd = mapFilter.get("recAllEnd");
    // ----------------------------------
    String vField;
    // ---
    if (!QueryUtils.emptyString(vName)) {
      crit.add(Restrictions.like("name", vName, MatchMode.START).ignoreCase());
    }
    // ---
    if (!QueryUtils.emptyString(vOwner)) {
      crit.add(Restrictions.like("owner", vOwner, MatchMode.START).ignoreCase());
    }
    //---
    vField = "dateWork";
    Date dtBeg = null;
    Date dtEnd = null;
    if (!QueryUtils.emptyString(vDateWorkBeg)) {
      dtBeg = DateUtils.str2Date(vDateWorkBeg);
    }
    if (!QueryUtils.emptyString(vDateWorkEnd)) {
      dtEnd = DateUtils.str2DateTime(vDateWorkEnd + " 23:59:59");
    }
    if (dtBeg != null && dtEnd != null) {
      crit.add(Restrictions.between(vField, dtBeg, dtEnd));
    } else if (dtBeg != null && dtEnd == null) {
      crit.add(Restrictions.ge(vField, dtBeg));
    } else if (dtBeg == null && dtEnd != null) {
      crit.add(Restrictions.le(vField, dtEnd));
    }
    //---
    vField = "recAll";
    Integer intBeg = null;
    Integer intEnd = null;
    if (!QueryUtils.emptyString(vRecAllBeg)) {
      intBeg = Integer.valueOf(vRecAllBeg);
    }
    if (!QueryUtils.emptyString(vRecAllEnd)) {
      intEnd = Integer.valueOf(vRecAllEnd);
    }
    if (intBeg != null && intEnd != null) {
      crit.add(Restrictions.between(vField, intBeg, intEnd));
    } else if (intBeg != null && intEnd == null) {
      crit.add(Restrictions.ge(vField, intBeg));
    } else if (intBeg == null && intEnd != null) {
      crit.add(Restrictions.le(vField, intEnd));
    }
    //---
  }

  @Override
  public List<FileLoadDto> queryAll() {
    cacheObj = fetchPage(-1,-1);
    return cacheObj;    
  }

  @Override
  public List<FileLoadDto> fetchPage(int startPos, int cntRows) {
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
  
  private FileLoadDto getObject(Object[] obj) {
    //---------------------------------------
    Long rsId = (Long) obj[0];
    String rsName = (String) obj[1];
    String rsMd5 = (String) obj[2];
    String rsOwner = (String) obj[3];
    Timestamp rsDateWork = (Timestamp) obj[4];
    Integer rsRecAll = (Integer) obj[5];
    //---------------------------------------
    FileLoadDto objDto = new FileLoadDto();
    objDto.setId(rsId);
    objDto.setName(rsName);
    objDto.setMd5(rsMd5);
    objDto.setOwner(rsOwner);
    objDto.setDateWork(rsDateWork);
    objDto.setRecAll(rsRecAll);
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