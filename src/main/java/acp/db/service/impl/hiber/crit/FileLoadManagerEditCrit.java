package acp.db.service.impl.hiber.crit;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.domain.FileLoadClass;
import acp.db.service.IFileLoadManagerEdit;
import acp.db.service.impl.hiber.all.ManagerBaseHiber;
import acp.forms.dto.FileLoadDto;
import acp.utils.DialogUtils;

public class FileLoadManagerEditCrit extends ManagerBaseHiber implements IFileLoadManagerEdit {
  private static Logger logger = LoggerFactory.getLogger(FileLoadManagerEditCrit.class);

  private FileLoadDto createDto(FileLoadClass objClass) {
    FileLoadDto objDto = new FileLoadDto();
    // ----------------------------------
    objDto.setId(objClass.getId());    
    objDto.setName(objClass.getName());    
    objDto.setMd5(objClass.getMd5());    
    objDto.setDateCreate(objClass.getDateCreate());
    objDto.setDateWork(objClass.getDateWork());
    objDto.setOwner(objClass.getOwner());
    objDto.setConfigId(objClass.getConfigId());
    if (objClass.getConfig() != null) {
      objDto.setConfigName(objClass.getConfig().getName());
    }
    objDto.setRecAll(objClass.getRecAll());
    objDto.setRecErr(objClass.getRecErr());
    // ----------------------------------
    ArrayList<String> statList = new ArrayList<>();
    statList.add(String.valueOf(objClass.getRecAll()));
    statList.add(String.valueOf(objClass.getRecErr()));
    objDto.setStatList(statList);
    // ----------------------------------
    return objDto;
  }

  public FileLoadDto select(Long objId) {
    FileLoadDto objDto = null; 
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      // ----------------------------------------
      Criteria query = session.createCriteria(FileLoadClass.class,"fl");
      query.createCriteria("config","cfg"); 
      query.add(Restrictions.idEq(objId));
      // ----------------------------------------
      FileLoadClass objClass = (FileLoadClass) query.uniqueResult();
      objDto = createDto(objClass);
      // ----------------------------------------
      tx.commit();
    } catch (HibernateException e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      objDto = null;
    } catch (Exception e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      objDto = null;
    } finally {
      session.close();
    }  
    return objDto;
  }

}
