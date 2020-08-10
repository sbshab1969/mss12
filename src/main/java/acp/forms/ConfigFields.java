package acp.forms;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import org.w3c.dom.*;

import acp.forms.dm.DmConfigFields;
import acp.forms.frame.FrameEdit;
import acp.utils.*;

public class ConfigFields extends FrameEdit {
  private static final long serialVersionUID = 1L;

  private static final int FIELDS_COUNT = 17;
  private static final String FIELD_NAME = "field";

  private DmConfigFields dmField;
  private int cols = 0;

  private ArrayList<String> validAttrs;
  private String[] headers;
  private String[] rowData;
  
  private Node parentNode;
  private Node currentNode;
  private int currentRow = -1;

  private JPanel pnlFields;
  private JScrollPane spFields;
  
  private ArrayList<JLabel> lblList = new ArrayList<JLabel>();
  private ArrayList<JTextField> textList = new ArrayList<JTextField>();

  public ConfigFields(DmConfigFields dmFld, int row) {
    // setResizable(true);
    // setSize(350, 300);
    setSize(650, 600);

    dmField = dmFld;
    parentNode = dmField.getNode();
    currentRow = row;

    initPnlData();
    initFormNone();
    if (cols <= FIELDS_COUNT) {
      pack();
    }
    setToCenter();
  }

  private void initPnlData() {
    pnlData.setLayout(new BorderLayout());

    pnlFields = new JPanel();
    pnlFields.setLayout(new SpringLayout());

    validAttrs = dmField.getValidAttrs();
    headers = dmField.getHeader();
    cols = headers.length;
    for (int i = 0; i < cols; i++) {
      // ------------------
      String lblName = headers[i];
      JLabel lbl = new JLabel(lblName, JLabel.TRAILING);
      lblList.add(lbl);
      pnlFields.add(lbl);
      // ------------------
      JTextField edt = new JTextField(30);
      textList.add(edt);
      pnlFields.add(edt);
      // ------------------
      lbl.setLabelFor(edt);
    }
    SpringUtilities.makeCompactGrid(pnlFields, cols, 2, 10, 10, 10, 10);

    spFields = new JScrollPane(pnlFields);
    pnlData.add(spFields);
  }
  
  protected void clearData() {
    for (int i = 0; i < cols; i++) {
      textList.get(i).setText("");
    }
  }

  protected boolean fillData() {
    if (currentRow >= 0) {
      rowData = dmField.getRowData(currentRow);
      for (int i = 0; i < cols; i++) {
        textList.get(i).setText(rowData[i]);
      }
    }
    return true;
  }

  protected void setEditableData() {
    if (act == ACT_NEW || act == ACT_EDIT) {
      setEditableFields(true);
    } else if (act == ACT_DELETE) {
      setEditableFields(false);
    } else {
      setEditableFields(false);
    }
  }

  private void setEditableFields(boolean flag) {
    for (int i = 0; i < cols; i++) {
      textList.get(i).setEditable(flag);
    }
  }

  private void fillAttrs() {
    for (int i = 0; i < cols; i++) {
      Element currentNodeElem = (Element) currentNode;
      String attrName = validAttrs.get(i);
      String attrValue = textList.get(i).getText();
      if (!attrValue.equals("")) {
        currentNodeElem.setAttribute(attrName, attrValue);
      } else {
        currentNodeElem.removeAttribute(attrName);
      }
    }
  }

  protected boolean validateData() {
    int cnt = 0;
    for (int i = 0; i < cols; i++) {
      String attrValue = textList.get(i).getText();
      if (!attrValue.equals("")) {
        cnt++;
      }
    }
    if (cnt == 0) {
      DialogUtils.errorMsg(Messages.getString("Message.IsEmptyFields"));
      return false;
    }
    return true;
  }

  protected boolean saveObj() {
    if (act == ACT_NEW) {
      Document docum = parentNode.getOwnerDocument();
      Element item = docum.createElement(FIELD_NAME);
      currentNode = parentNode.appendChild(item);
    } else if (act == ACT_EDIT) {
      currentNode = XmlUtils.getChild(parentNode, currentRow);
    }
    fillAttrs();
    return true;
  }

  public Node getCurrentNode() {
    return currentNode;
  }

}
