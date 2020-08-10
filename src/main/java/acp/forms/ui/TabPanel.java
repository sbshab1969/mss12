package acp.forms.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Timestamp;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import acp.forms.dm.DmPanel;
import acp.utils.DialogUtils;
import acp.utils.Messages;

public class TabPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public final int NAV_NONE = -1;
  public final int NAV_FIRST = 0;
  public final int NAV_LAST = 1;
  public final int NAV_CURRENT = 2;

  public final int MANY_PAGES = 100;

  private DmPanel dmf;
  private JTable jTable;

  private boolean modePage = false;
  
  private JToolBar tbNav;
  
  // private JButton btnFirst = new JButton(new ImageIcon("images/first.png"));
  // private JButton btnPrevious = new JButton(new ImageIcon("images/previous.png"));
  // private JButton btnNext = new JButton(new ImageIcon("images/next.png"));
  // private JButton btnLast = new JButton(new ImageIcon("images/last.png"));
  
  // "/images/first.png" - если относительно корня
  // "images/first.png" - если относительно acp.forms.ui
  
  // private URL urlFirst = TabPanel.class.getResource("/images/first.png");
  // private URL urlPrevious = TabPanel.class.getResource("/images/previous.png");
  // private URL urlNext = TabPanel.class.getResource("/images/next.png");
  // private URL urlLast = TabPanel.class.getResource("/images/last.png");
  
  private URL urlFirst = this.getClass().getResource("/images/first.png");
  private URL urlPrevious = this.getClass().getResource("/images/previous.png");
  private URL urlNext = this.getClass().getResource("/images/next.png");
  private URL urlLast = this.getClass().getResource("/images/last.png");

  private JButton btnFirst = new JButton(new ImageIcon(urlFirst));
  private JButton btnPrevious = new JButton(new ImageIcon(urlPrevious));
  private JButton btnNext = new JButton(new ImageIcon(urlNext));
  private JButton btnLast = new JButton(new ImageIcon(urlLast));

  private JLabel lblCurrent = new JLabel("");
  
  public TabPanel() {
    dmf = new DmPanel();
    initFrame();
  }

  public TabPanel(DmPanel dmFrame) {
    dmf = dmFrame;
    initFrame();
  }

  public DmPanel getModel() {
    return dmf;
  }

  public void setModel(DmPanel dmFrame) {
    dmf = dmFrame;
    jTable.setModel(dmFrame);
  }

  public JTable getTable() {
    return jTable;
  }

  private void initFrame() {
    jTable = new JTable(dmf);
    JScrollPane scrollpane = new JScrollPane(jTable);
    // ----------------------------------------------------------------
    jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    // jTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    // jTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); /* default */
    // ------------------------------------
    // jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    // jTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
    // jTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS); /* (default) */
    // jTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    // jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    // ----------------------------------------------------------------
    // jTable.setAutoCreateRowSorter(true);
    // ----------------------------------------------------------------
    DateTimeRenderer dtRenderer = new DateTimeRenderer();
//    jTable.setDefaultRenderer(Date.class, dtRenderer);
    jTable.setDefaultRenderer(Timestamp.class, dtRenderer);
    // ----------------------------------------------------------------
    initToolBar();
    setLayout(new BorderLayout());
    add(tbNav, BorderLayout.NORTH);
    add(scrollpane, BorderLayout.CENTER);
  }

  private void initToolBar() {
    tbNav = new JToolBar();
    tbNav.setRollover(true);
    tbNav.add(btnFirst);
    tbNav.add(btnPrevious);
    tbNav.add(btnNext);
    tbNav.add(btnLast);
    tbNav.add(lblCurrent);
    setModePage(modePage);

    MyActionListener myActionListener = new MyActionListener();
    btnFirst.addActionListener(myActionListener);
    btnPrevious.addActionListener(myActionListener);
    btnNext.addActionListener(myActionListener);
    btnLast.addActionListener(myActionListener);
  }
  
  public void setModePage(boolean modePg) {
    modePage = modePg;
    tbNav.setVisible(modePage);
    if (modePage) {
      lblCurrent.setText(getPagePosition());
    }
  }

  public void setRecPerPage(int recPerPage) {
    dmf.setRecPerPage(recPerPage);
  }

  public String getPagePosition() {
    String str = "   " + Messages.getString("Message.Page") + " "
        + dmf.getCurrPage() + " / " + dmf.getPageCount();
    return str;
  }

  public void queryTable(int navMode) {
    dmf.setCurrPage(1);
    execQuery(navMode);
  }

  public void refreshTable(int navMode) {
    // currPage не меняется.
    execQuery(navMode);
  }

  private void execQuery(int navMode) {
    int currRecord = jTable.getSelectedRow();
    // --------------------------
    if (modePage) {
      dmf.queryPage();
      lblCurrent.setText(getPagePosition());
    } else {
      dmf.queryAll();
    }
    // --------------------------
    int selRow = -1;
    int rows = jTable.getRowCount();
    if (rows > 0) {
      switch (navMode) {
      case NAV_FIRST:
        selRow = 0;
        break;
      case NAV_LAST:
        selRow = rows - 1;
        break;
      case NAV_CURRENT:
        selRow = currRecord;
        break;
      default:
        selRow = 0;
      }
      if (selRow < 0) {
        selRow = 0;
      }
      if (selRow >= rows) {
        selRow = rows - 1;
      }
    }
    if (selRow >= 0) {
      selectRow(selRow);
    }
    resizeColumns();
  }

  public void closeQuery() {
    dmf.closeQuery();
  }

  public void selectRow(int rowNum) {
    int rowCount = jTable.getRowCount();
    if (rowNum >= 0 && rowNum < rowCount) {
      jTable.setRowSelectionInterval(rowNum, rowNum);
//      jTable.setColumnSelectionInterval(0, 0);
    } else {
      jTable.clearSelection();
    }
  }

  public void selectFirst() {
    selectRow(0);
  }

  public void resizeColumns() {
    TableColumnModel columnModel = jTable.getColumnModel();
    TableColumn column = columnModel.getColumn(0);
    // -------------------
    column.setMinWidth(0);
    column.setMaxWidth(100);
    column.setPreferredWidth(60);
//    column.setWidth(25);
    // -------------------
  }
  
  public Long getRecordId() {
    Long result = null;
    int selectRow = jTable.getSelectedRow();
    if (selectRow >= 0) {
      Object obj0 = jTable.getValueAt(selectRow, 0); // ID
      if (obj0 instanceof String) {
        String resStr = (String) obj0;
        result = Long.valueOf(resStr);
      } else if (obj0 instanceof Long) {
        result = (Long) obj0;
      }
    } else {
      DialogUtils.errorPrint(Messages.getString("Message.NoSelectRecord"));
    }
    return result;
  }

  private class MyActionListener implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      Object objSource = ae.getSource();
      if (objSource.equals(btnFirst)) {
        dmf.firstPage();
      } else if (objSource.equals(btnPrevious)) {
        dmf.previousPage();
      } else if (objSource.equals(btnNext)) {
        dmf.nextPage();
      } else if (objSource.equals(btnLast)) {
        Long cntPages = dmf.getPageCount();
        if (cntPages > MANY_PAGES) {
          String message = Messages.getString("Message.ManyPageCount") + " /"
              + cntPages + "/. " + Messages.getString("Message.Continue");
          if (DialogUtils.confirmDialog(message,
              Messages.getString("Title.Warning"), 1) == 0) {
            dmf.lastPage();
          }
        } else {
          dmf.lastPage();
        }
      }
      selectFirst();
      resizeColumns();
      if (modePage) {
        lblCurrent.setText(getPagePosition());
      }
    }
  }

}
