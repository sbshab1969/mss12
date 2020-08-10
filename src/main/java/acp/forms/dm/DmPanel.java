package acp.forms.dm;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import acp.utils.DialogUtils;
import acp.utils.Messages;

public class DmPanel extends AbstractTableModel {
  private static final long serialVersionUID = 1L;

  protected static final long MAX_INT = (long) Integer.MAX_VALUE;
  
  private List<String[]> cache = new ArrayList<>();

  protected String[] headers = new String[] {};
  protected Class<?>[] types = new Class<?>[] {};

  protected int colCount = 0;
  protected int recPerPage = 20;
  protected int firstRowOnPage = 1;

  protected long recCount = 0L;
  protected long pageCount = 0L;

  protected long currPage = 0L;

  public DmPanel() {
  }

  // --- AbstractTableModel ---
  public String getColumnName(int col) {
    return headers[col];
  }

  public Class<?> getColumnClass(int col) {
    return types[col];
  }

  public boolean isCellEditable(int row, int col) {
    return false;
  }

  // --- TableModel ---
  public int getColumnCount() {
    return colCount;
  }

  public int getRowCount() {
    return cache.size();
  }

  public Object getValueAt(int row, int col) {
    return cache.get(row)[col];
  }
  // --------------------------------------
  
  public void setRecPerPage(int recOnPage) {
    this.recPerPage = recOnPage;
  }

  public long getPageCount() {
    return pageCount;
  }

  public long getCurrPage() {
    return currPage;
  }

  public void setCurrPage(long currPg) {
    currPage = currPg;
  }
  // --------------------------------------

  protected long countRecords() {
    return 0L;
  }

  protected void calcPageCount() {
    recCount = countRecords();
    if (recCount > 0L) {
      long fullPageCount = recCount / recPerPage;
      long tail = recCount - fullPageCount*recPerPage;
      if (tail == 0) {
        pageCount = fullPageCount;
      } else {
        pageCount = fullPageCount + 1;
      }
    } else {
      pageCount = 0L;
    }
  }

  protected long calcStartRec(long page) {
    long startRec = 0L;
    if (page > 0L) {
      startRec = (page-1)*recPerPage + firstRowOnPage;
    }
    return startRec;
  }

  protected boolean testStartRec(long startRec) {
    if (startRec > MAX_INT) {
      DialogUtils.errorPrint(Messages.getString("Message.MaxInteger") + ": " + startRec);
      return false;
    }
    return true;
  }

  public void firstPage() {
    calcPageCount();
    long newPage = currPage;
    if (newPage > 1) {
      newPage = 1;
    }
    fetchPage(newPage);
  }

  public void previousPage() {
    calcPageCount();
    long newPage = currPage;
    if (newPage > 1) {
      newPage--;
    }
    fetchPage(newPage);
  }

  public void nextPage() {
    calcPageCount();
    long newPage = currPage;
    if (newPage < pageCount) {
      newPage++;
    } else {
      newPage = pageCount;
    }
    fetchPage(newPage);
  }

  public void lastPage() {
    calcPageCount();
    long newPage = pageCount;
    fetchPage(newPage);
  }

  public void queryAll() {
  }

  public void queryPage() {
  }

  public void fetchPage(long page) {
    setCurrPage(page);
  }

  public void closeQuery() {
  }

}
