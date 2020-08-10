package acp.forms.dm;

import java.util.ArrayList;
import java.util.List;

import acp.db.service.IManagerList;
import acp.forms.dto.FileLoadDto;

public class DmFileLoadList extends DmPanel {
  private static final long serialVersionUID = 1L;

  private IManagerList<FileLoadDto> tableManager;
  private List<FileLoadDto> cacheObj = new ArrayList<>();

  public DmFileLoadList(IManagerList<FileLoadDto> tblMng) {
    tableManager = tblMng;
    setHeaders();
  }

  public void setManager(IManagerList<FileLoadDto> tblMng) {
    tableManager = tblMng;
    setHeaders();
  }
  
  private void setHeaders() {
    if (tableManager != null) {
      headers = tableManager.getHeaders();
      types = tableManager.getTypes();
      colCount = headers.length;
    } else {
      headers = new String[] {};
      types = new Class<?>[] {};
      colCount = 0;
    }
  }

  // --- TableModel ---

  @Override
  public int getRowCount() {
    return cacheObj.size();
  }

  @Override
  public Object getValueAt(int row, int col) {
    FileLoadDto obj = cacheObj.get(row); 
    switch (col) {
    case 0:  
      return obj.getId();
    case 1:  
      return obj.getName();
    case 2:  
      return obj.getMd5();
    case 3:  
      return obj.getOwner();
    case 4:  
      return obj.getDateWork();
    case 5:  
      return obj.getRecAll();
    }  
    return null;
  }
  // --------------------------------------
  
  @Override
  public long countRecords() {
    long recCnt = tableManager.countRecords();
    return recCnt;
  }

  @Override
  public void queryAll() {
    cacheObj = tableManager.queryAll();
    fireTableChanged(null);
  }

  @Override
  public void queryPage() {
    calcPageCount();
    if (currPage > pageCount) {
      currPage = pageCount;
    }  
    tableManager.openQueryPage();
    fetchPage(currPage);
  }

  @Override
  public void fetchPage(long page) {
    long startRec = calcStartRec(page);
    if (testStartRec(startRec) == false) { 
      return;
    }
    setCurrPage(page);
    int startPos = (int) startRec;
    cacheObj = tableManager.fetchPage(startPos,recPerPage);
    fireTableChanged(null);
  }

  @Override
  public void closeQuery() {
    tableManager.closeQuery();
  }

}
