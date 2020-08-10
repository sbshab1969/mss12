package acp.forms;

import java.util.ArrayList;

import javax.swing.*;

import org.w3c.dom.*;

import acp.forms.frame.FrameEdit;
import acp.utils.*;

public class ConfigAttr extends FrameEdit {
  private static final long serialVersionUID = 1L;

  private Node node;
  private Node attr;

  private String nodeName;
  private String attrName;

  private JLabel lblName = new JLabel(Messages.getString("Column.Name"),
      JLabel.TRAILING);
  private JLabel lblValue = new JLabel(Messages.getString("Column.Value"),
      JLabel.TRAILING);

  private JComboBox<String> cmbName = new JComboBox<String>();
  private JTextField txtValue = new JTextField(30);

  public ConfigAttr(Node vNode, Node vAttr) {
    node = vNode;
    attr = vAttr;
    if (node != null) {
      nodeName = node.getNodeName();
    }
    if (attr != null) {
      attrName = attr.getNodeName();
    }

    initPnlData();
    initFormNone();
    pack();
    setToCenter();
  }

  private void initPnlData() {
    pnlData.setLayout(new SpringLayout());

    pnlData.add(lblName);
    pnlData.add(cmbName);
    lblName.setLabelFor(cmbName);

    pnlData.add(lblValue);
    pnlData.add(txtValue);
    lblValue.setLabelFor(txtValue);
    
    cmbName.setMaximumRowCount(4);

    SpringUtilities.makeCompactGrid(pnlData, 2, 2, 10, 10, 10, 10);
  }
  
  protected void clearData() {
    cmbName.setSelectedIndex(-1);
    txtValue.setText("");
  }

  protected boolean fillData() {
    if (act == ACT_NEW) {
      if (node != null) {
        ArrayList<String> newAttrs = XmlUtils.getNewAttrs(node);
        for (String newAttr : newAttrs) {
          String key = nodeName + "." + newAttr;
          String itemName = FieldConfig.getString(key);
          cmbName.addItem(itemName);
        }
      }
    } else if (act == ACT_EDIT) {
      if (attr != null) {
        ArrayList<String> oldAttrs = XmlUtils.getOldAttrs(node);
        for (String oldAttr : oldAttrs) {
          String key = nodeName + "." + oldAttr;
          String itemName = FieldConfig.getString(key);
          cmbName.addItem(itemName);
        }
        String key = nodeName + "." + attrName;
        String itemName = FieldConfig.getString(key);
        cmbName.setSelectedItem(itemName);
        txtValue.setText(attr.getNodeValue());
      }
    }
    return true;
  }

  protected void setEditableData() {
    if (act == ACT_NEW) {
      cmbName.setEnabled(true);
      txtValue.setEditable(true);
    } else if (act == ACT_EDIT) {
      cmbName.setEnabled(false);
      txtValue.setEditable(true);
    } else {
      cmbName.setEnabled(false);
      txtValue.setEditable(false);
    }
  }

  protected boolean validateData() {
    int indexName = cmbName.getSelectedIndex();
    String val = txtValue.getText();
    // --------------------
    if (indexName == -1) {
      DialogUtils.errorMsg(Messages.getString("Message.IsEmpty") + ": "
          + Messages.getString("Column.Name"));
      return false;
    }
    if (val.equals("")) {
      DialogUtils.errorMsg(Messages.getString("Message.IsEmpty") + ": "
          + Messages.getString("Column.Value"));
      return false;
    }
    return true;
  }

  protected boolean saveObj() {
    if (act == ACT_NEW) {
      ArrayList<String> newAttrs = XmlUtils.getNewAttrs(node);
      String newAttrName = newAttrs.get(cmbName.getSelectedIndex());
      String newAttrValue = txtValue.getText();
      // ---------------------------
      // Правильны оба варианта
      // Сейчас работает первый (Element)
      // ---------------------------
      if (node instanceof Element) {
        Element nodeElem = (Element) node;
        nodeElem.setAttribute(newAttrName, newAttrValue);
      } else {
        Document docum = node.getOwnerDocument();
        Attr newAttr = docum.createAttribute(newAttrName);
        // newAttr.setNodeValue(newAttrValue);
        newAttr.setValue(newAttrValue);
        NamedNodeMap nodeMap = node.getAttributes();
        nodeMap.setNamedItem(newAttr);
      }

    } else if (act == ACT_EDIT) {
      String textVal = txtValue.getText();
      // ------------------------
      attr.setNodeValue(textVal); // !!!!!!!
      // ------------------------
    }
    return true;
  }

  public Node getAttr() {
    return attr;
  }

}
