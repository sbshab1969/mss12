package acp.forms.frame;

import java.awt.*;
import java.awt.event.*;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.*;

import acp.forms.ui.TabPanel;
import acp.utils.*;

public abstract class FrameList extends MyInternalFrame {
  private static final long serialVersionUID = 1L;

  protected JPanel pnlFilter = new JPanel();
  protected TabPanel pnlTable = new TabPanel();

  protected JPanel pnlButtons = new JPanel();
  protected JPanel pnlBtnRecord = new JPanel();
  protected JPanel pnlBtnAct = new JPanel();
  protected JPanel pnlBtnExit = new JPanel();

  protected JButton btnFilter = new JButton(Messages.getString("Button.Filter"));
  protected JButton btnFltClear = new JButton(Messages.getString("Button.Clear"));

  protected JButton btnAdd = new JButton(Messages.getString("Button.Add"));
  protected JButton btnEdit = new JButton(Messages.getString("Button.Edit"));
  protected JButton btnDelete = new JButton(Messages.getString("Button.Delete"));
  protected JButton btnCopy = new JButton(Messages.getString("Button.Copy"));
  protected JButton btnRefresh = new JButton(Messages.getString("Button.Refresh"));
  protected JButton btnClose = new JButton(Messages.getString("Button.Close"));

  public FrameList() {
    setTitle(Messages.getString("Title.Frame"));
    setSize(640, 480);
    setToCenter(); // метод из MyInternalFrame
    setMaximizable(true);
    setResizable(true);
    // setIconifiable(true);
    // setClosable(true);

    // Filter ---
    setFilterVisible(false);
    
    // Table ---
    pnlTable.setModePage(false);

    // Buttons ---
    pnlButtons.setLayout(new BorderLayout());
    pnlButtons.add(pnlBtnRecord, BorderLayout.WEST);
    pnlButtons.add(pnlBtnAct, BorderLayout.CENTER);
    pnlButtons.add(pnlBtnExit, BorderLayout.EAST);

    pnlBtnExit.add(btnClose);

    // --- Layout ---
    setLayout(new BorderLayout()); // default layout for JFrame
    add(pnlFilter, BorderLayout.NORTH);
    add(pnlTable, BorderLayout.CENTER);
    add(pnlButtons, BorderLayout.SOUTH);

    // Listeners ---
    MyFilterListener myFilterListener = new MyFilterListener();
    btnFilter.addActionListener(myFilterListener);
    btnFltClear.addActionListener(myFilterListener);

    MyActionListener myActionListener = new MyActionListener();
    btnAdd.addActionListener(myActionListener);
    btnEdit.addActionListener(myActionListener);
    btnDelete.addActionListener(myActionListener);
    btnCopy.addActionListener(myActionListener);
    btnRefresh.addActionListener(myActionListener);
    btnClose.addActionListener(myActionListener);

    MyMouseListener myMouseListener = new MyMouseListener(); 
    pnlTable.getTable().addMouseListener(myMouseListener);

    addInternalFrameListener(new InternalFrameAdapter() {
      public void internalFrameClosing(InternalFrameEvent e) {
        frameClosing();
      }
    });

  }

  private void frameClosing() {
    pnlTable.closeQuery();
  }

  public void setFilterVisible(boolean modeFlt) {
    pnlFilter.setVisible(modeFlt);
  }

  public void initForm() {}
  protected void initTable() {}
  protected void applyFilter() {}
  protected void cancelFilter() {}
  protected void clearFilter() {}
  protected boolean validateFilter() {return true;}
  protected Map<String, String> fillMapFilter() {return null;}

  protected boolean canDeleteRecord(Long recId) {return true;}
  protected void editRecord(int act, Long recId) {}
  protected void deleteRecord(Long recId) {}
  protected void copyRecord(Long recId) {}
  
  private class MyActionListener implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      Object objSource = ae.getSource();
      
      if (objSource.equals(btnAdd)) {
        editRecord(ACT_NEW, null);

      } else if (objSource.equals(btnEdit)) {
        Long recId = pnlTable.getRecordId();
        if (recId != null) {
          editRecord(ACT_EDIT, recId);
        }
      
      } else if (objSource.equals(btnDelete)) {
        Long recId = pnlTable.getRecordId();
        if (recId != null) {
          boolean resValidate = canDeleteRecord(recId);
          if (resValidate) {
            if (DialogUtils.confirmDialog(
                Messages.getString("Message.DeleteRecord") + " /id=" + recId
                    + "/", Messages.getString("Title.RecordDelete"), 1) == 0) {
              // ------------------------
              deleteRecord(recId);
              // ------------------------
            }
          }
        }
      
      } else if (objSource.equals(btnCopy)) {
        Long recId = pnlTable.getRecordId();
        if (recId != null) {
          if (DialogUtils.confirmDialog(
              Messages.getString("Message.CopyRecord") + " /id=" + recId + "/",
              Messages.getString("Title.RecordCopy"), 1) == 0) {
            // ------------------------
            copyRecord(recId);
            // ------------------------
          }
        }
    
      } else if (objSource.equals(btnRefresh)) {
        pnlTable.refreshTable(pnlTable.NAV_CURRENT);
      
      } else if (objSource.equals(btnClose)) {
        frameClosing();
        dispose();
      }
    }
  }
  
  private class MyFilterListener implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      Object objSource = ae.getSource();
      if (objSource.equals(btnFilter)) {
        applyFilter();
      } else if (objSource.equals(btnFltClear)) {
        cancelFilter();
      }
    }  
  }

  private class MyMouseListener extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      if (e.getClickCount() == 2) {
        Long recId = pnlTable.getRecordId();
        if (recId != null) {
          editRecord(ACT_EDIT, recId);
        }
      }
    }
  }

}
