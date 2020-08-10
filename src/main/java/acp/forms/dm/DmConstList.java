package acp.forms.dm;

import java.util.ArrayList;
import java.util.List;

import acp.db.service.IManagerList;
import acp.forms.dto.ConstDto;

public class DmConstList extends DmPanel {
  private static final long serialVersionUID = 1L;

  private IManagerList<ConstDto> tableManager;
  private List<ConstDto> cacheObj = new ArrayList<>();

  public DmConstList(IManagerList<ConstDto> tblMng) {
    tableManager = tblMng;
    setHeaders();
  }

  public void setManager(IManagerList<ConstDto> tblMng) {
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
    ConstDto obj = cacheObj.get(row); 
    switch (col) {
    case 0:  
      return obj.getId();
    case 1:  
      return obj.getName();
    case 2:  
      return obj.getValue();
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
    long newPage = currPage;
    if (newPage > pageCount) {
      newPage = pageCount;
    }
    // ----------------------
    tableManager.openQueryPage();
    fetchPage(newPage);
    // ----------------------
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
