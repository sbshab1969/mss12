package acp.forms;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.*;

import acp.db.service.IConfigManagerEdit;
import acp.db.service.IManagerList;
import acp.db.service.ISourceManagerEdit;
import acp.db.service.factory.ConfigManagerFactory;
import acp.db.service.factory.SourceManagerFactory;
import acp.forms.dm.DmConfigList;
import acp.forms.dto.ConfigDto;
import acp.forms.frame.FrameList;
import acp.forms.ui.*;
import acp.utils.*;

public class ConfigList extends FrameList {
  private static final long serialVersionUID = 1L;

  private IManagerList<ConfigDto> listManager;
  private IConfigManagerEdit editManager;
  private ISourceManagerEdit sourceManager;
  private DmConfigList dmForm;

  private JPanel pnlFilter_1 = new JPanel();
  private JPanel pnlFilter_2 = new JPanel();
  private JPanel pnlBtnFilter = new JPanel();

  private JLabel lblName = new JLabel(Messages.getString("Column.Name"),
      JLabel.TRAILING);
  private JTextField txtName = new JTextField(20);
  private JLabel lblOwner = new JLabel(Messages.getString("Column.Owner"),
      JLabel.TRAILING);
  private JTextField txtOwner = new JTextField(20);
  private JLabel lblSource = new JLabel(Messages.getString("Column.SourceName"),
      JLabel.TRAILING);
  private JComboBox<CbClass> cbdbSource;
  private CbModel cbdbSourceModel;
  private JPanel pnlEmpty41 = new JPanel();
  private JPanel pnlEmpty42 = new JPanel();

  private JButton btnConfig = new JButton(Messages.getString("Button.Config"));
  private JButton btnConfigXml = new JButton(Messages.getString("Button.ConfigXml"));

  public ConfigList() {
    setTitle(Messages.getString("Title.ConfigList"));
    setSize(900, 600);
    setToCenter(); // метод из MyInternalFrame
    setMaximizable(true);
    setResizable(true);

    // Filter ---
    initFilter();
    setFilterVisible(true);

    // Table ---
    listManager = ConfigManagerFactory.getManagerList();
    editManager = ConfigManagerFactory.getManagerEdit();
    sourceManager = SourceManagerFactory.getManagerEdit();
    dmForm = new DmConfigList(listManager);

    pnlTable.setModel(dmForm);
    // pnlTable.setModePage(true);
    // pnlTable.setRecPerPage(5);

    // Buttons ---
    pnlBtnRecord.add(btnAdd);
    pnlBtnRecord.add(btnEdit);
    pnlBtnRecord.add(btnDelete);
    pnlBtnRecord.add(btnCopy);
    pnlBtnRecord.add(btnConfig);
    pnlBtnRecord.add(btnConfigXml);
    pnlBtnAct.add(btnRefresh);

    // --- Layout ---
    setLayout(new BorderLayout()); // default layout for JFrame
    add(pnlFilter, BorderLayout.NORTH);
    add(pnlTable, BorderLayout.CENTER);
    add(pnlButtons, BorderLayout.SOUTH);

    // Listeners ---
    MyActionListener myActionListener = new MyActionListener();
    btnConfig.addActionListener(myActionListener);
    btnConfigXml.addActionListener(myActionListener);
  }

  private void initFilter() {
    pnlFilter.setBorder(new LineBorder(Color.BLACK));
    pnlFilter.setLayout(new BorderLayout());

    cbdbSourceModel = new CbModel();
    cbdbSourceModel.setNeedNullItem(true);
    cbdbSource = new JComboBox<CbClass>(cbdbSourceModel);

    lblName.setLabelFor(txtName);
    lblSource.setLabelFor(cbdbSource);
    lblOwner.setLabelFor(txtOwner);

    pnlFilter_1.setLayout(new SpringLayout());
    pnlFilter_1.add(lblName);
    pnlFilter_1.add(txtName);
    pnlFilter_1.add(lblSource);
    pnlFilter_1.add(cbdbSource);
    pnlFilter_1.add(lblOwner);
    pnlFilter_1.add(txtOwner);
    pnlFilter_1.add(pnlEmpty41);
    pnlFilter_1.add(pnlEmpty42);
    SpringUtilities.makeCompactGrid(pnlFilter_1, 2, 4, 8, 8, 8, 8);

    pnlBtnFilter.setLayout(new GridLayout(2, 1, 5, 5));
    pnlBtnFilter.add(btnFilter);
    pnlBtnFilter.add(btnFltClear);

    pnlFilter_2.setLayout(new FlowLayout());
    pnlFilter_2.add(pnlBtnFilter);

    pnlFilter.add(pnlFilter_1, BorderLayout.CENTER);
    pnlFilter.add(pnlFilter_2, BorderLayout.EAST);
  }

  public void initForm() {
    cbdbSourceModel.setArrayString(sourceManager.getSources());
    cbdbSource.setSelectedIndex(-1);
    // -----------
    applyFilter();
    // -----------
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
    txtName.setText("");
    txtOwner.setText("");
    cbdbSource.setSelectedIndex(-1);
  }

  protected  boolean validateFilter() {
    return true;
  }
  
  protected  Map<String,String> fillMapFilter() {
    // ------------------------------
    String vName = txtName.getText();
    String vOwner = txtOwner.getText();
    int index = cbdbSource.getSelectedIndex();
    String vSourceId = cbdbSourceModel.getKeyStringAt(index);
    // ------------------------------
    Map<String,String> mapFilter = new HashMap<>();
    mapFilter.put("name", vName);
    mapFilter.put("owner", vOwner);
    mapFilter.put("sourceId", vSourceId);
    // ------------------------------
    return mapFilter;
  }

  protected boolean canDeleteRecord(int recId) {
    return true;
  }

  protected  void editRecord(int act, Long recId) {
    ConfigEdit cfgEdit = new ConfigEdit(editManager);
    // boolean resInit = true;
    boolean resInit = cfgEdit.initForm(act, recId);
    if (resInit) {
      cfgEdit.showForm();
      int resForm = cfgEdit.getResultForm();
      if (resForm == RES_OK) {
        pnlTable.refreshTable(pnlTable.NAV_CURRENT);
      }
    }
    cfgEdit = null;
  }

  private void editConfigTree(Long recId) {
    ConfigTree cfgTree = new ConfigTree(editManager);
    // boolean resInit = true;
    boolean resInit = cfgTree.initForm(ACT_EDIT, recId);
    if (resInit) {
      cfgTree.showForm();
    }
    cfgTree = null;
  }

  private void editConfigXml(Long recId) {
    ConfigXml cfgXml = new ConfigXml(editManager);
    // boolean resInit = true;
    boolean resInit = cfgXml.initForm(ACT_EDIT, recId);
    if (resInit) {
      cfgXml.showForm();
    }
    cfgXml = null;
  }

  protected void deleteRecord(Long recId) {
    editManager.delete(recId);
    pnlTable.refreshTable(pnlTable.NAV_CURRENT);
  }

  protected void copyRecord(Long recId) {
    editManager.copy(recId);
    pnlTable.refreshTable(pnlTable.NAV_CURRENT);
  }

  private class MyActionListener implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      Object objSource = ae.getSource();
      
      if (objSource.equals(btnConfig)) {
        Long recId = pnlTable.getRecordId();
        if (recId != null) {
          editConfigTree(recId);
        }
      
      } else if (objSource.equals(btnConfigXml)) {
        Long recId = pnlTable.getRecordId();
        if (recId != null) {
          if (DialogUtils.confirmDialog(
              Messages.getString("Message.DirectEditMsg"),
              Messages.getString("Title.Warning"), 1) == 0) {
            editConfigXml(recId);
          }
        }
      }
    }
  }

}
