package acp.forms;

import javax.swing.*;

import acp.db.service.ISourceManagerEdit;
import acp.forms.dto.SourceDto;
import acp.forms.frame.FrameEdit;
import acp.utils.*;

public class SourceEdit extends FrameEdit {
  private static final long serialVersionUID = 1L;

  private ISourceManagerEdit tableManager;

  private JLabel lblName = new JLabel(Messages.getString("Column.Name"),
      JLabel.TRAILING);
  private JTextField txtName = new JTextField(20);

  public SourceEdit(ISourceManagerEdit tblManager) {
    tableManager = tblManager;

    initPnlData();
    initFormNone();
    pack();
    setToCenter();
  }

  private void initPnlData() {
    pnlData.setLayout(new SpringLayout());
    pnlData.add(lblName);
    pnlData.add(txtName);
    lblName.setLabelFor(txtName);
    SpringUtilities.makeCompactGrid(pnlData, 1, 2, 10, 10, 10, 10);
  }
  
  protected void setEditableData(int act) {
    if (act == ACT_NEW) {
      txtName.setEditable(true);
    } else if (act == ACT_EDIT) {
      txtName.setEditable(true);
    } else {
      txtName.setEditable(false);
    }
  }

  protected void clearData() {
    txtName.setText("");
  }

  protected boolean fillData() {
    if (act == ACT_EDIT) {
      SourceDto recObj = tableManager.select(recId);
      if (recObj == null) {
        return false;
      }  
      // ------------------------------
      txtName.setText(recObj.getName());
      // ------------------------------
    }  
    return true;
  }

  protected boolean validateData() {
    String vName = txtName.getText();
    // --------------------
    if (vName.equals("")) {
      DialogUtils.errorMsg(Messages.getString("Message.IsEmpty") + ": "
          + Messages.getString("Column.Name"));
      return false;
    }
    // --------------------
    return true;
  }

  protected SourceDto getObj() {
    String vName = txtName.getText();
    // --------------------
    SourceDto formObj = new SourceDto();
    formObj.setId(recId);
    formObj.setName(vName);
    // --------------------
    return formObj;
  }

  protected boolean saveObj() {
    boolean res = false;
    SourceDto formObj = getObj();
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
