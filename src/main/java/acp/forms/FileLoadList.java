package acp.forms;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.*;

import acp.db.service.IFileLoadManagerEdit;
import acp.db.service.IManagerList;
import acp.db.service.factory.FileLoadManagerFactory;
import acp.forms.dm.DmFileLoadList;
import acp.forms.dto.FileLoadDto;
import acp.forms.frame.FrameList;
import acp.utils.*;

public class FileLoadList extends FrameList {
  private static final long serialVersionUID = 1L;

  private IManagerList<FileLoadDto> listManager;
  private IFileLoadManagerEdit editManager;
  private DmFileLoadList dmForm;

  private JPanel pnlFilter_1 = new JPanel();
  private JPanel pnlFilter_2 = new JPanel();
  private JPanel pnlBtnFilter = new JPanel();

  private SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
  private JLabel lblFileName = new JLabel(Messages.getString("Column.FileName"));
  private JTextField txtFileName = new JTextField(20);
  private JLabel lblOwner = new JLabel(Messages.getString("Column.Owner"));
  private JTextField txtOwner = new JTextField(20);
  private JLabel lblDtBegin = new JLabel(Messages.getString("Column.DateWork")
      + Messages.getString("Column.Begin")); // , JLabel.TRAILING
  private JLabel lblDtEnd = new JLabel(Messages.getString("Column.End"), JLabel.CENTER);
  private JFormattedTextField dtBegin = new JFormattedTextField(formatDate);
  private JFormattedTextField dtEnd = new JFormattedTextField(formatDate);
  private JLabel lblRecBegin = new JLabel(Messages.getString("Column.RecordCount")
      + Messages.getString("Column.Begin"));
  private JLabel lblRecEnd = new JLabel(Messages.getString("Column.End"), JLabel.CENTER);
  private JTextField recBegin = new JTextField(20);
  private JTextField recEnd = new JTextField(20);

  private JButton btnInfo = new JButton(Messages.getString("Button.Info"));
  private JButton btnLogs = new JButton(Messages.getString("Button.Logs"));

  public FileLoadList() {
    setTitle(Messages.getString("Title.FileList"));
    setSize(1200, 650);
    setToCenter(); // метод из MyInternalFrame
    setMaximizable(true);
    setResizable(true);

    // Filter ---
    setFilterVisible(true);
    initFilter();

    // Table ---
    listManager = FileLoadManagerFactory.getManagerList();
    editManager = FileLoadManagerFactory.getManagerEdit();
    dmForm = new DmFileLoadList(listManager);

    pnlTable.setModel(dmForm);
    pnlTable.setModePage(true);
    pnlTable.setRecPerPage(25);
    
    pnlBtnRecord.add(btnInfo);
    pnlBtnRecord.add(btnLogs);

    // Listeners ---
    MyActionListener myActionListener = new MyActionListener();
    btnInfo.addActionListener(myActionListener);
    btnLogs.addActionListener(myActionListener);
  }

  private void initFilter() {
    pnlFilter.setBorder(new LineBorder(Color.BLACK));
    pnlFilter.setLayout(new BorderLayout());

    lblFileName.setLabelFor(txtFileName);
    lblOwner.setLabelFor(txtOwner);

    Calendar gcBefore = new GregorianCalendar();
    gcBefore.add(Calendar.DAY_OF_YEAR, -7);
    // gcBefore.add(Calendar.MONTH, -1);
    // gcBefore.add(Calendar.YEAR, -2);
    Date dtBefore = gcBefore.getTime();
    Date dtNow = new Date();
    dtBegin.setValue(dtBefore);
    dtEnd.setValue(dtNow);

    // dtBegin.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
    // dtBegin.setFocusLostBehavior(JFormattedTextField.COMMIT);
    // dtBegin.setFocusLostBehavior(JFormattedTextField.REVERT);
    // dtBegin.setFocusLostBehavior(JFormattedTextField.PERSIST);

    dtBegin.setFocusLostBehavior(JFormattedTextField.COMMIT);
    dtEnd.setFocusLostBehavior(JFormattedTextField.COMMIT);

    pnlFilter_1.add(lblFileName);
    pnlFilter_1.add(txtFileName);
    pnlFilter_1.add(lblOwner);
    pnlFilter_1.add(txtOwner);

    pnlFilter_1.setLayout(new SpringLayout());
    // pnlFilter_1.setBorder(new LineBorder(Color.BLACK));
    pnlFilter_1.add(lblDtBegin);
    pnlFilter_1.add(dtBegin);
    pnlFilter_1.add(lblDtEnd);
    pnlFilter_1.add(dtEnd);

    pnlFilter_1.add(lblRecBegin);
    pnlFilter_1.add(recBegin);
    pnlFilter_1.add(lblRecEnd);
    pnlFilter_1.add(recEnd);
    SpringUtilities.makeCompactGrid(pnlFilter_1, 3, 4, 8, 8, 8, 8);

    pnlBtnFilter.setLayout(new GridLayout(2, 1, 5, 5));
    pnlBtnFilter.add(btnFilter);
    pnlBtnFilter.add(btnFltClear);

    pnlFilter_2.setLayout(new FlowLayout());
    // pnlFilter_2.setLayout(new FlowLayout(FlowLayout.CENTER,6,6));
    pnlFilter_2.add(pnlBtnFilter);

    pnlFilter.add(pnlFilter_1, BorderLayout.CENTER);
    pnlFilter.add(pnlFilter_2, BorderLayout.EAST);
  }

  public void initForm() {
    applyFilter();
  }

  protected void applyFilter() {
    boolean retValidate = validateFilter();
    if (retValidate == true) {
      Map<String,String> mapFilter = fillMapFilter();
      listManager.prepareQuery(mapFilter);
      pnlTable.queryTable(pnlTable.NAV_FIRST);
    }  
  }
  
  protected void cancelFilter() {
    clearFilter();
    applyFilter();
  }
    
  protected void clearFilter() {
    txtFileName.setText("");
    txtOwner.setText("");
    dtBegin.setValue(null);
    dtEnd.setValue(null);
    recBegin.setText("");
    recEnd.setText("");
  }

  protected boolean validateFilter() {
//    String vName = txtFileName.getText();
//    String vOwner = txtOwner.getText();
    String vDateWorkBeg = dtBegin.getText();
    String vDateWorkEnd = dtEnd.getText();
    String vRecAllBeg = recBegin.getText();
    String vRecAllEnd = recEnd.getText();
    // --------------------
    if (!vDateWorkBeg.equals("")) {
      try {
        dtBegin.commitEdit();
      } catch (ParseException e) {
        DialogUtils.errorMsg(Messages.getString("Message.BadDate"));
        return false;
      }
    } else {
      dtBegin.setValue(null);
    }
    // --------------------
    if (!vDateWorkEnd.equals("")) {
      try {
        dtEnd.commitEdit();
      } catch (ParseException e) {
        DialogUtils.errorMsg(Messages.getString("Message.BadDate"));
        return false;
      }
    } else {
      dtEnd.setValue(null);
    }
    // --------------------
    Date dateBeg = (Date) dtBegin.getValue();
    Date dateEnd = (Date) dtEnd.getValue();
    if (dateBeg != null && dateEnd !=null) {
      if (dateBeg.after(dateEnd)) {
        DialogUtils.errorMsg(Messages.getString("Message.BadDatePeriod"));
        return false;
      }
    }  
    // --------------------
    Integer intBeg = null;
    Integer intEnd = null;
    if (!vRecAllBeg.equals("")) {
      try {
        intBeg = Integer.valueOf(vRecAllBeg);
      } catch (NumberFormatException e) {
        DialogUtils.errorMsg(Messages.getString("Message.BadNumber"));
        return false;
      }
    }
    // --------------------
    if (!vRecAllEnd.equals("")) {
      try {
        intEnd = Integer.valueOf(vRecAllEnd);
      } catch (NumberFormatException e) {
        DialogUtils.errorMsg(Messages.getString("Message.BadNumber"));
        return false;
      }
    }
    // --------------------
    if (intBeg != null && intEnd !=null) {
      if (intBeg > intEnd) {
        DialogUtils.errorMsg(Messages.getString("Message.BadNumberPeriod"));
        return false;
      }
    }  
    // --------------------
    return true;
  }

  protected Map<String,String> fillMapFilter() {
    // ------------------------------
    String vName = txtFileName.getText();
    String vOwner = txtOwner.getText();
    String vDateWorkBeg = dtBegin.getText();
    String vDateWorkEnd = dtEnd.getText();
    String vRecAllBeg = recBegin.getText();
    String vRecAllEnd = recEnd.getText();
    // ------------------------------
    Map<String,String> mapFilter = new HashMap<>();
    mapFilter.put("name", vName);
    mapFilter.put("owner", vOwner);
    mapFilter.put("dateWorkBeg", vDateWorkBeg);
    mapFilter.put("dateWorkEnd", vDateWorkEnd);
    mapFilter.put("recAllBeg", vRecAllBeg);
    mapFilter.put("recAllEnd", vRecAllEnd);
    // ------------------------------
    return mapFilter;
  }

  private void showInfo(Long recId) {
    FileLoadInfo fileInfo = new FileLoadInfo(editManager);
    boolean resInit = fileInfo.initForm(ACT_VIEW, recId);
    if (resInit) {
      fileInfo.showForm();
    }
    fileInfo = null;
  }

  private void showLogs(Long recId) {
    FileOtherList fileLog = new FileOtherList(recId);
    fileLog.initForm();
    fileLog.showForm();
    fileLog = null;
  }

  private class MyActionListener implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      Object objSource = ae.getSource();

      if (objSource.equals(btnInfo)) {
        Long recId = pnlTable.getRecordId();
        if (recId != null) {
          showInfo(recId);
        }

      } else if (objSource.equals(btnLogs)) {
        Long recId = pnlTable.getRecordId();
        if (recId != null) {
          showLogs(recId);
        }
      }
    }
  }
  
}
