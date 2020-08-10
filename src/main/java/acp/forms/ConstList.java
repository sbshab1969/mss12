package acp.forms;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.*;

import acp.db.service.IConstManagerEdit;
import acp.db.service.IManagerList;
import acp.db.service.factory.ConstManagerFactory;
import acp.forms.dm.DmConstList;
import acp.forms.dto.ConstDto;
import acp.forms.frame.FrameList;
import acp.utils.*;

public class ConstList extends FrameList {
  private static final long serialVersionUID = 1L;

  private IManagerList<ConstDto> listManager;
  private IConstManagerEdit editManager;
  
  private DmConstList dmForm;

  private JPanel pnlBtnFilter = new JPanel();

  private JLabel lblName = new JLabel(Messages.getString("Column.Name"));
  private JTextField txtName = new JTextField(20);
  
  public ConstList() {
    setTitle(Messages.getString("Title.ConstList"));
    setSize(640, 480);
    setToCenter(); // метод из MyInternalFrame
    setMaximizable(true);
    setResizable(true);
    // setIconifiable(true);
    // setClosable(true);

    // Filter ---
    setFilterVisible(true);
    initFilter();

    // Table ---
    listManager = ConstManagerFactory.getManagerList();
    editManager = ConstManagerFactory.getManagerEdit();
    dmForm = new DmConstList(listManager);

    pnlTable.setModel(dmForm);
//    pnlTable.setModePage(true);
//    pnlTable.setRecPerPage(3);

    // Buttons ---
    pnlBtnRecord.add(btnAdd);
    pnlBtnRecord.add(btnEdit);
    pnlBtnRecord.add(btnDelete);
    pnlBtnAct.add(btnRefresh);
  }

  private void initFilter() {
    pnlFilter.setBorder(new TitledBorder(new LineBorder(Color.BLACK), 
        Messages.getString("Title.Filter")));
    pnlFilter.setLayout(new GridBagLayout());

    GridBagConstraints cons = new GridBagConstraints();
    cons.insets = new Insets(0, 2, 2, 2);
    // cons.gridwidth = GridBagConstraints.HORIZONTAL;
    cons.weightx = 1.0;

    lblName.setLabelFor(txtName);
    lblName.setHorizontalAlignment(SwingConstants.RIGHT);

    cons.anchor = GridBagConstraints.WEST;
    pnlFilter.add(lblName, cons);

    cons.anchor = GridBagConstraints.WEST;
    // cons.gridwidth = GridBagConstraints.HORIZONTAL;
    pnlFilter.add(txtName, cons);

    cons.gridwidth = GridBagConstraints.REMAINDER;
    cons.anchor = GridBagConstraints.EAST;
    // cons.anchor = GridBagConstraints.LINE_START;
    // cons.anchor = GridBagConstraints.LINE_END;
    // cons.fill = GridBagConstraints.HORIZONTAL;
    cons.weightx = 1.0;
    pnlFilter.add(pnlBtnFilter, cons);

    // pnlBtnFilter.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
    pnlBtnFilter.setLayout(new GridLayout(1, 2, 2, 2));
    pnlBtnFilter.add(btnFilter);
    pnlBtnFilter.add(btnFltClear);
    // pnlBtnFilter.setBorder(new LineBorder(Color.BLACK));
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
  }

  protected boolean validateFilter() {
    return true;
  }

  protected Map<String,String> fillMapFilter() {
    // ------------------------------
    String vName = txtName.getText(); 
    // ------------------------------
    Map<String,String> mapFilter = new HashMap<>();
    mapFilter.put("name", vName);
    // ------------------------------
    return mapFilter;
  }

  protected boolean canDeleteRecord(Long recId) {
    Long seqId = listManager.getSeqId();
    if (recId < seqId) {
      DialogUtils.errorMsg(Messages.getString("Message.DeleteSystemRecord"));
      return false;
    }
    return true;
  }

  protected void editRecord(int act, Long recId) {
    ConstEdit constEdit = new ConstEdit(editManager);
    // boolean resInit = true;
    boolean resInit = constEdit.initForm(act, recId);
    if (resInit) {
      constEdit.showForm();
      int resForm = constEdit.getResultForm();
      if (resForm == RES_OK) {
        pnlTable.refreshTable(pnlTable.NAV_CURRENT);
      }
    }
    constEdit = null;
  }

  protected void deleteRecord(Long recId) {
    editManager.delete(recId);
    pnlTable.refreshTable(pnlTable.NAV_CURRENT);
  }
}
