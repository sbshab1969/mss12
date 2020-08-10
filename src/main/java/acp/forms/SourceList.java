package acp.forms;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.*;

import acp.db.service.IManagerList;
import acp.db.service.ISourceManagerEdit;
import acp.db.service.factory.SourceManagerFactory;
import acp.forms.dm.DmSourceList;
import acp.forms.dto.SourceDto;
import acp.forms.frame.FrameList;
import acp.utils.*;

public class SourceList extends FrameList {
  private static final long serialVersionUID = 1L;

  private IManagerList<SourceDto> listManager;
  private ISourceManagerEdit editManager;
  private DmSourceList dmForm;

  private JPanel pnlFilter_1 = new JPanel();
  private JPanel pnlFilter_2 = new JPanel();
  private JPanel pnlBtnFilter = new JPanel();
  private JLabel lblName = new JLabel(Messages.getString("Column.Name"));
  private JTextField txtName = new JTextField(20);
  private JLabel lblOwner = new JLabel(Messages.getString("Column.Owner"));
  private JTextField txtOwner = new JTextField(20);

  public SourceList() {
    setTitle(Messages.getString("Title.SourceList"));
    setSize(640, 480);
    setToCenter(); // метод из MyInternalFrame
    setMaximizable(true);
    setResizable(true);

    // Filter ---
    setFilterVisible(true);
    initFilter();

    // --- Table ---
    listManager = SourceManagerFactory.getManagerList();
    editManager = SourceManagerFactory.getManagerEdit();
    dmForm = new DmSourceList(listManager);
    
    pnlTable.setModel(dmForm);
    // pnlTable.setModePage(true);
    // pnlTable.setRecPerPage(3);
    
    // Buttons ---
    pnlBtnRecord.add(btnAdd);
    pnlBtnRecord.add(btnEdit);
    pnlBtnRecord.add(btnDelete);
    pnlBtnAct.add(btnRefresh);
  }

  private void initFilter() {
    pnlFilter.setBorder(new LineBorder(Color.BLACK));
    pnlFilter.setLayout(new BorderLayout());

    lblName.setLabelFor(txtName);
    lblOwner.setLabelFor(txtOwner);

    pnlFilter_1.setLayout(new SpringLayout());
    pnlFilter_1.add(lblName);
    pnlFilter_1.add(txtName);
    pnlFilter_1.add(lblOwner);
    pnlFilter_1.add(txtOwner);
    SpringUtilities.makeCompactGrid(pnlFilter_1, 2, 2, 8, 8, 8, 8);

    pnlFilter_2.setLayout(new FlowLayout());
    pnlFilter_2.add(pnlBtnFilter);

    pnlBtnFilter.setLayout(new GridLayout(2, 1, 5, 5));
    pnlBtnFilter.add(btnFilter);
    pnlBtnFilter.add(btnFltClear);

    pnlFilter.setLayout(new BorderLayout());
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
    txtName.setText("");
    txtOwner.setText("");
  }

  protected boolean validateFilter() {
    return true;
  }
  
  protected Map<String,String> fillMapFilter() {
    // ------------------------------
    String vName = txtName.getText(); 
    String vOwner = txtOwner.getText(); 
    // ------------------------------
    Map<String,String> mapFilter = new HashMap<>();
    mapFilter.put("name", vName);
    mapFilter.put("owner", vOwner);
    // ------------------------------
    return mapFilter;
  }

  protected boolean canDeleteRecord(int recId) {
    return true;
  }

  protected void editRecord(int act, Long recId) {
    SourceEdit sourceEdit = new SourceEdit(editManager);
    // boolean resInit = true;
    boolean resInit = sourceEdit.initForm(act, recId);
    if (resInit) {
      sourceEdit.showForm();
      int resForm = sourceEdit.getResultForm();
      if (resForm == RES_OK) {
        pnlTable.refreshTable(pnlTable.NAV_CURRENT);
      }
    }
    sourceEdit = null;
  }

  protected void deleteRecord(Long recId) {
    editManager.delete(recId);
    pnlTable.refreshTable(pnlTable.NAV_CURRENT);
  }
}
