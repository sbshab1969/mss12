package acp.forms;

import java.util.ArrayList;
import javax.swing.*;

import acp.db.service.IToptionManagerEdit;
import acp.forms.dto.ToptionDto;
import acp.forms.frame.FrameEdit;
import acp.utils.*;

public class XmlTableEdit extends FrameEdit {
  private static final long serialVersionUID = 1L;

  private IToptionManagerEdit tableManager;
  private ToptionDto recOldObj;

  private ArrayList<JLabel> lblList = new ArrayList<JLabel>();
  private ArrayList<JTextField> textList = new ArrayList<JTextField>();

  public XmlTableEdit(IToptionManagerEdit tblManager) {
    tableManager = tblManager;

    initPnlData();
    initFormNone();
    pack();
    setToCenter();
  }

  private void initPnlData() {
    ArrayList<String> attrs = tableManager.getAttrs();
    int attrSize = attrs.size();
    String attrPrefix = tableManager.getAttrPrefix();

    pnlData.setLayout(new SpringLayout());
    for (int i = 0; i < attrSize; i++) {
      // ------------------
      String lblName = FieldConfig.getString(attrPrefix + "." + attrs.get(i));
      JLabel lbl = new JLabel(lblName, JLabel.TRAILING);
      lblList.add(lbl);
      pnlData.add(lbl);
      // ------------------
      JTextField edt = new JTextField(30);
      textList.add(edt);
      pnlData.add(edt);
      // ------------------
      lbl.setLabelFor(edt);
    }
    SpringUtilities.makeCompactGrid(pnlData, attrSize, 2, 10, 10, 10, 10);
  }

  protected void setEditableData() {
    if (act != ACT_NONE) {
      setEditableFields(true);
    } else {
      setEditableFields(false);
    }
  }

  private void setEditableFields(boolean flag) {
    for (int i = 0; i < textList.size(); i++) {
      if (textList.get(i).getText().equals("")) {
        textList.get(i).setEditable(false);
      } else {
        textList.get(i).setEditable(flag);
      }
    }
  }

  protected void clearData() {
    for (int i = 0; i < textList.size(); i++) {
      textList.get(i).setText("");
    }
  }

  protected boolean fillData() {
    if (act == ACT_EDIT) {
      // ---------------------------------
      recOldObj = tableManager.select(recId);
      if (recOldObj == null) {
        return false;
      }
      // ---------------------------------
      ArrayList<String> pArr = recOldObj.getPArray();
      for (int i = 0; i < pArr.size(); i++) {
        String val = pArr.get(i);
        textList.get(i).setText(val);
      }
      // ---------------------------------
    }  
    return true;
  }

  protected boolean validateData() {
    ArrayList<String> oldVal = recOldObj.getPArray();
    for (int i = 0; i < textList.size(); i++) {
      // --------------------------------------
      String vLabel = lblList.get(i).getText();
      String vText = textList.get(i).getText();
      String vOldText = oldVal.get(i);
      // --------------------------------------
      if (vOldText != null && vText.equals("")) {
        DialogUtils.errorMsg(Messages.getString("Message.IsEmpty") + ": "
            + vLabel);
        return false;
      }
      // --------------------------------------
    }
    return true;
  }

  protected ToptionDto getObj() {
    ArrayList<String> pArr = new ArrayList<>();
    for (int i = 0; i < textList.size(); i++) {
      String vText = textList.get(i).getText();
      pArr.add(vText);
    }
    // ------------------
    ToptionDto formObj = new ToptionDto();
    formObj.setId(recId);
    formObj.setArrayP(pArr);
    // ------------------
    return formObj;
  }

  protected boolean saveObj() {
    boolean res = false;
    ToptionDto recNewObj = getObj();
    if (act == ACT_EDIT) {
      res = tableManager.update(recOldObj, recNewObj);
    }
    return res;
  }

}
