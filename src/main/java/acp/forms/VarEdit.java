package acp.forms;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.ArrayList;

import java.util.Date;

import javax.swing.*;

import acp.db.service.IVarManagerEdit;
import acp.forms.dto.VarDto;
import acp.forms.frame.FrameEdit;
import acp.forms.ui.*;
import acp.utils.*;

public class VarEdit extends FrameEdit {
  private static final long serialVersionUID = 1L;

  private IVarManagerEdit tableManager;

  private JLabel lblName = new JLabel(Messages.getString("Column.Name"));
  private JLabel lblType = new JLabel(Messages.getString("Column.Type"));
  private JLabel lblValueN = new JLabel(Messages.getString("Column.Number"));
  private JLabel lblValueV = new JLabel(Messages.getString("Column.Varchar"));
  private JLabel lblValueD = new JLabel(Messages.getString("Column.Date"));
  private JLabel lblFormatD = new JLabel("/ "
      + Messages.getString("Column.DateFormat") + " /");

  private SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
  // private JLabel lblFormatD = new JLabel("/ " + formatDate.toPattern() +
  // " /");

  private JTextField txtName = new JTextField(30);
  private JComboBox<CbClass> cmbType;
  private CbModel cmbTypeModel;

  private JTextField txtValueN = new JTextField(30);
  private JTextField txtValueV = new JTextField(30);
  private JFormattedTextField txtValueD = new JFormattedTextField(formatDate);
  private JPanel pnlDate = new JPanel();

  public VarEdit(IVarManagerEdit tblManager) {
    tableManager = tblManager;

    initPnlData();
    initFormNone();
    pack();
    setToCenter();
  }

  private void initPnlData() {
    cmbTypeModel = new CbModel();
    cmbType = new JComboBox<>(cmbTypeModel);
    // --------------
    txtValueN.setColumns(14);
    txtValueD.setColumns(14);
    txtValueD.setFocusLostBehavior(JFormattedTextField.COMMIT);
    // txtValueD.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT); ;
    // txtValueD.setFocusLostBehavior(JFormattedTextField.REVERT); ;
    // txtValueD.setFocusLostBehavior(JFormattedTextField.PERSIST); ;
    cmbType.setPreferredSize(txtValueN.getPreferredSize());
    // --------------
    lblName.setLabelFor(txtName);
    lblType.setLabelFor(cmbType);
    lblValueN.setLabelFor(txtValueN);
    lblValueV.setLabelFor(txtValueV);
    lblValueD.setLabelFor(txtValueD);

    pnlData.setLayout(new GridBagLayout());

    // pnlDate.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
    pnlDate.setLayout(new GridLayout(1, 2, 5, 0));
    // pnlDate.setBorder(new LineBorder(Color.BLACK));
    pnlDate.add(txtValueD);
    pnlDate.add(lblFormatD);

    GridBagConstraints cons = new GridBagConstraints();

    cons.insets = new Insets(10, 10, 2, 10);
    cons.gridx = 0;
    cons.gridy = 0;
    cons.anchor = GridBagConstraints.EAST;
    pnlData.add(lblName, cons);
    cons.gridx = 1;
    cons.gridy = 0;
    cons.anchor = GridBagConstraints.WEST;
    pnlData.add(txtName, cons);

    cons.insets = new Insets(2, 10, 2, 10);
    cons.gridx = 0;
    cons.gridy = 1;
    cons.anchor = GridBagConstraints.EAST;
    pnlData.add(lblType, cons);
    cons.gridx = 1;
    cons.gridy = 1;
    cons.anchor = GridBagConstraints.WEST;
    pnlData.add(cmbType, cons);

    cons.insets = new Insets(2, 10, 2, 10);
    cons.gridx = 0;
    cons.gridy = 2;
    cons.anchor = GridBagConstraints.EAST;
    pnlData.add(lblValueN, cons);
    cons.gridx = 1;
    cons.gridy = 2;
    cons.anchor = GridBagConstraints.WEST;
    pnlData.add(txtValueN, cons);

    cons.insets = new Insets(2, 10, 2, 10);
    cons.gridx = 0;
    cons.gridy = 3;
    cons.anchor = GridBagConstraints.EAST;
    pnlData.add(lblValueV, cons);
    cons.gridx = 1;
    cons.gridy = 3;
    cons.anchor = GridBagConstraints.WEST;
    pnlData.add(txtValueV, cons);

    cons.insets = new Insets(2, 10, 10, 10);
    cons.gridx = 0;
    cons.gridy = 4;
    cons.anchor = GridBagConstraints.EAST;
    pnlData.add(lblValueD, cons);
    cons.gridx = 1;
    cons.gridy = 4;
    cons.anchor = GridBagConstraints.WEST;
    pnlData.add(pnlDate, cons);

    MyActionListener myActionListener = new MyActionListener();
    cmbType.addActionListener(myActionListener);
  }

  protected void initFormBefore() {
    if (act != ACT_NONE) {
      ArrayList<CbClass> items = new ArrayList<>();
      items.add(new CbClass("N", Messages.getString("Column.Number")));
      items.add(new CbClass("V", Messages.getString("Column.Varchar")));
      items.add(new CbClass("D", Messages.getString("Column.Date")));
      items.add(new CbClass("U", Messages.getString("Column.Universal")));
      // ----------
      cmbTypeModel.setArrayList(items);
      // ----------
    }
  }

  protected void setEditableData() {
    if (act == ACT_NEW) {
      txtName.setEditable(true);
      cmbType.setEnabled(true);
      setEditableVars();
    } else if (act == ACT_EDIT) {
      txtName.setEditable(false);
      cmbType.setEnabled(false);
      setEditableVars();
    } else {
      txtName.setEditable(false);
      cmbType.setEnabled(false);
      txtValueV.setEditable(false);
      txtValueN.setEditable(false);
      txtValueD.setEditable(false);
    }
  }

  protected void setEditableVars() {
    int index = cmbType.getSelectedIndex();
    String key = cmbTypeModel.getKeyStringAt(index);
    if (key == "V") {
      txtValueV.setEditable(true);
      txtValueN.setEditable(false);
      txtValueD.setEditable(false);
    } else if (key == "N") {
      txtValueV.setEditable(false);
      txtValueN.setEditable(true);
      txtValueD.setEditable(false);
    } else if (key == "D") {
      txtValueV.setEditable(false);
      txtValueN.setEditable(false);
      txtValueD.setEditable(true);
    } else if (key == "U") {
      txtValueV.setEditable(true);
      txtValueN.setEditable(true);
      txtValueD.setEditable(true);
    } else {
      txtValueV.setEditable(false);
      txtValueN.setEditable(false);
      txtValueD.setEditable(false);
    }
  }

  protected void clearData() {
    txtName.setText("");
    cmbType.setSelectedIndex(-1);
    txtValueN.setText("");
    txtValueV.setText("");
    txtValueD.setValue(null);
  }

  protected boolean fillData() {
    if (act == ACT_EDIT) {
      VarDto recObj = tableManager.select(recId);
      if (recObj == null) {
        return false;
      }
      // -------------------------------
      txtName.setText(recObj.getName());
      cmbTypeModel.setKeyString(recObj.getType());
      Double valn = recObj.getValuen();
      String strValn = "";
      if (valn != null) {
        strValn = valn.toString();
      }
      txtValueN.setText(strValn);
      txtValueV.setText(recObj.getValuev());
      txtValueD.setValue(recObj.getValued());
      // -------------------------------
    }
    return true;
  }

  protected boolean validateData() {
    String vName = txtName.getText();
    int index = cmbType.getSelectedIndex();
    String vType = cmbTypeModel.getKeyStringAt(index);
    String strValn = txtValueN.getText();
    String strValv = txtValueV.getText();
    String strVald = txtValueD.getText();
    // --------------------
    if (vName.equals("")) {
      DialogUtils.errorMsg(Messages.getString("Message.IsEmpty") + ": "
          + Messages.getString("Column.Name"));
      return false;
    }
    if (index == -1) {
      DialogUtils.errorMsg(Messages.getString("Message.IsEmpty") + ": "
          + Messages.getString("Column.Type"));
      return false;
    }
    if (vType == "N") {
      if (strValn.equals("")) {
        DialogUtils.errorMsg(Messages.getString("Message.IsEmpty") + ": "
            + Messages.getString("Column.Number"));
        return false;
      }
    } else if (vType == "V") {
      if (strValv.equals("")) {
        DialogUtils.errorMsg(Messages.getString("Message.IsEmpty") + ": "
            + Messages.getString("Column.Varchar"));
        return false;
      }
    } else if (vType == "D") {
      if (strVald.equals("")) {
        DialogUtils.errorMsg(Messages.getString("Message.IsEmpty") + ": "
            + Messages.getString("Column.Date"));
        return false;
      }
    }
    if (!strValn.equals("")) {
      try {
        Double.valueOf(strValn);
      } catch (NumberFormatException e) {
        DialogUtils.errorMsg(Messages.getString("Message.BadNumber"));
        return false;
      }
    }
    if (!strVald.equals("")) {
      try {
        txtValueD.commitEdit();
      } catch (ParseException e) {
        DialogUtils.errorMsg(Messages.getString("Message.BadDate"));
        return false;
      }
    } else {
      txtValueD.setValue(null);
    }
    // --------------------
    return true;
  }

  protected VarDto getObj() {
    String vName = txtName.getText();
    int index = cmbType.getSelectedIndex();
    String vType = cmbTypeModel.getKeyStringAt(index);
    String strValn = txtValueN.getText();
    Double valn = null;
    if (!strValn.equals("")) {
      valn = Double.valueOf(strValn);
    }
    String valv = txtValueV.getText();
    Date vald = (Date) txtValueD.getValue();
    // --------------------
    if (vType == "N") {
      valv = null;
      vald = null;
    } else if (vType == "V") {
      valn = null;
      vald = null;
    } else if (vType == "D") {
      valn = null;
      valv = null;
    }
    // --------------------
    VarDto formObj = new VarDto();
    formObj.setId(recId);
    formObj.setName(vName);
    formObj.setType(vType);
    formObj.setValuen(valn);
    formObj.setValuev(valv);
    formObj.setValued(vald);
    // --------------------
    return formObj;
  }

  protected boolean saveObj() {
    boolean res = false;
    VarDto formObj = getObj();
    if (act == ACT_NEW) {
      Long objId = tableManager.insert(formObj);
      if (objId != null) {
        res = true;
      }
    } else if (act == ACT_EDIT) {
      res = tableManager.update(formObj);
    }
    return res;
  }

  private class MyActionListener implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      Object objSource = ae.getSource();
      if (objSource.equals(cmbType)) {
        if (act == ACT_NEW || act == ACT_EDIT) {
          setEditableVars();
        }
      }
    }
  }

}
