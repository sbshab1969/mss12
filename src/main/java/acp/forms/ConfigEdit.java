package acp.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.*;

import acp.db.service.IConfigManagerEdit;
import acp.db.service.ISourceManagerEdit;
import acp.db.service.factory.SourceManagerFactory;
import acp.forms.dto.ConfigDto;
import acp.forms.frame.FrameEdit;
import acp.forms.ui.*;
import acp.utils.*;

public class ConfigEdit extends FrameEdit {
  private static final long serialVersionUID = 1L;

  private IConfigManagerEdit tableManager;
  private ISourceManagerEdit sourceManager;

  private SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
  private JTextField txtName = new JTextField(20);
  private JComboBox<CbClass> cbdbSource;
  private CbModel cbdbSourceModel;
  private JPanel pnlDt = new JPanel();
  private JFormattedTextField dtBegin = new JFormattedTextField(formatDate);
  private JFormattedTextField dtEnd = new JFormattedTextField(formatDate);
  private JTextArea taComment = new JTextArea(5, 20);
  private JScrollPane spComment = new JScrollPane(taComment);

  public ConfigEdit(IConfigManagerEdit tblManager) {
    tableManager = tblManager;
    sourceManager = SourceManagerFactory.getManagerEdit();

    initPnlData();
    initFormNone();
    pack();
    setToCenter();
  }

  private void initPnlData() {
    pnlData.setLayout(new SpringLayout());

    JLabel lblName = new JLabel(Messages.getString("Column.Name"),
        JLabel.TRAILING);
    JLabel lblSource = new JLabel(Messages.getString("Column.SourceName"),
        JLabel.TRAILING);
    JLabel lblDtBegin = new JLabel(Messages.getString("Column.Date")
        + Messages.getString("Column.Begin"), JLabel.TRAILING);
    JLabel lblDtEnd = new JLabel("  " + Messages.getString("Column.End") + "  ");
    JLabel lblComment = new JLabel(Messages.getString("Column.Comment"),
        JLabel.TRAILING);

    pnlData.add(lblName);
    pnlData.add(txtName);
    lblName.setLabelFor(txtName);
    // lblName.setBorder(new LineBorder(Color.BLACK));

    cbdbSourceModel = new CbModel();
    cbdbSource = new JComboBox<CbClass>(cbdbSourceModel);

    pnlData.add(lblSource);
    pnlData.add(cbdbSource);
    lblSource.setLabelFor(cbdbSource);

    pnlDt.setLayout(new SpringLayout());
    dtBegin.setColumns(10);
    dtEnd.setColumns(10);
    dtBegin.setFocusLostBehavior(JFormattedTextField.COMMIT);
    dtEnd.setFocusLostBehavior(JFormattedTextField.COMMIT);
    pnlData.add(lblDtBegin);
    pnlData.add(pnlDt);
    pnlDt.add(dtBegin);
    pnlDt.add(lblDtEnd);
    pnlDt.add(dtEnd);
    SpringUtilities.makeCompactGrid(pnlDt, 1, 3, 0, 0, 0, 0);

    lblComment.setVerticalAlignment(SwingConstants.TOP);
    // lblComment.setBorder(new LineBorder(Color.BLACK));
    pnlData.add(lblComment);
    pnlData.add(spComment);

    SpringUtilities.makeCompactGrid(pnlData, 4, 2, 10, 10, 10, 10);
  }
  
  protected void initFormBefore() {
    if (act != ACT_NONE) {
      cbdbSourceModel.setArrayString(sourceManager.getSources());
      cbdbSource.setSelectedIndex(-1);
    }
  }

  protected void setEditableData() {
    if (act == ACT_NEW || act == ACT_EDIT) {
      txtName.setEditable(true);
      cbdbSource.setEnabled(true);
      dtBegin.setEditable(true);
      dtEnd.setEditable(true);
      taComment.setEnabled(true);
    } else if (act == ACT_DELETE) {
      txtName.setEditable(false);
      cbdbSource.setEnabled(false);
      dtBegin.setEditable(false);
      dtEnd.setEditable(false);
      taComment.setEnabled(false);
    } else {
      txtName.setEditable(false);
      cbdbSource.setEnabled(false);
      dtBegin.setEditable(false);
      dtEnd.setEditable(false);
      taComment.setEnabled(false);
    }
  }

  protected void clearData() {
    txtName.setText("");
    cbdbSource.setSelectedIndex(-1);
    dtBegin.setValue(null);
    dtEnd.setValue(null);
    taComment.setText(null);
  }

  protected boolean fillData() {
    if (act == ACT_EDIT) {
      // -------------------------------
      ConfigDto recObj = tableManager.select(recId);
      if (recObj == null) {
        return false;
      }
      // -------------------------------
      txtName.setText(recObj.getName());
      cbdbSourceModel.setKeyLong(recObj.getSourceId());
      dtBegin.setValue(recObj.getDateBegin());
      dtEnd.setValue(recObj.getDateEnd());
      taComment.setText(recObj.getComment());
      // -------------------------------
    }
    return true;
  }

  protected boolean validateData() {
    String vName = txtName.getText();
    int sourceIndex = cbdbSource.getSelectedIndex();
    String vDateBegin = dtBegin.getText();
    String vDateEnd = dtEnd.getText();
    String vComment = taComment.getText();
    // --------------------
    if (vName.equals("")) {
      DialogUtils.errorMsg(Messages.getString("Message.IsEmpty") + ": "
          + Messages.getString("Column.Name"));
      return false;
    }
    if (sourceIndex == -1) {
      DialogUtils.errorMsg(Messages.getString("Message.IsEmpty") + ": "
          + Messages.getString("Column.SourceName"));
      return false;
    }
    if (vDateBegin.equals("")) {
      DialogUtils.errorMsg(Messages.getString("Message.IsEmpty") + ": "
          + Messages.getString("Column.DateBegin"));
      return false;
    }
    if (vDateEnd.equals("")) {
      DialogUtils.errorMsg(Messages.getString("Message.IsEmpty") + ": "
          + Messages.getString("Column.DateEnd"));
      return false;
    }
    if (vComment.equals("")) {
      DialogUtils.errorMsg(Messages.getString("Message.IsEmpty") + ": "
          + Messages.getString("Column.Comment"));
      return false;
    }
    if (!vDateBegin.equals("")) {
      try {
        dtBegin.commitEdit();
      } catch (ParseException e) {
        DialogUtils.errorMsg(Messages.getString("Message.BadDateBegin"));
        return false;
      }
    } else {
      dtBegin.setValue(null);
    }
    if (!vDateEnd.equals("")) {
      try {
        dtEnd.commitEdit();
      } catch (ParseException e) {
        DialogUtils.errorMsg(Messages.getString("Message.BadDateEnd"));
        return false;
      }
    } else {
      dtEnd.setValue(null);
    }
    // --------------------
    return true;
  }

  protected ConfigDto getObj() {
    String vName = txtName.getText();
    int index = cbdbSource.getSelectedIndex();
    Long vSourceId = cbdbSourceModel.getKeyLongAt(index);
    Date dBegin = (Date) dtBegin.getValue();
    Date dEnd = (Date) dtEnd.getValue();
    String vComment = taComment.getText();
    // --------------------
    ConfigDto formObj = new ConfigDto();
    formObj.setId(recId);
    formObj.setName(vName);
    formObj.setDateBegin(dBegin);
    formObj.setDateEnd(dEnd);
    formObj.setComment(vComment);
    formObj.setSourceId(vSourceId);
    // --------------------
    return formObj;
  }

  protected boolean saveObj() {
    boolean res = false;
    ConfigDto formObj = getObj();
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

}
