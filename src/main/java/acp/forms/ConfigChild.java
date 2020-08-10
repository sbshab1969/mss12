package acp.forms;

import java.util.ArrayList;

import javax.swing.*;

import org.w3c.dom.*;

import acp.forms.frame.FrameEdit;
import acp.utils.*;

public class ConfigChild extends FrameEdit {
  private static final long serialVersionUID = 1L;

  private Node parentNode;
  private Node newNode;

  private JLabel lblName = new JLabel(Messages.getString("Column.Name"),JLabel.TRAILING);

  private DefaultListModel<String> listModel = new DefaultListModel<String>();
  private JList<String> lstName;
  private JScrollPane listScrollPane;

  public ConfigChild(Node vParent) {
    parentNode = vParent;

    initPnlData();
    initFormNone();
    pack();
    setToCenter();
  }

  private void initPnlData() {
    pnlData.setLayout(new SpringLayout());

    if (parentNode != null) {
      ArrayList<String> newNodes = XmlUtils.getNewNodes(parentNode);
      for (String key : newNodes) {
        String itemName = FieldConfig.getString(key);
        listModel.addElement(itemName);
      }
    }
    lstName = new JList<String>(listModel);
    lstName.setLayoutOrientation(JList.VERTICAL);
    lstName.setVisibleRowCount(5);
    listScrollPane = new JScrollPane(lstName);
    if (listModel.getSize() > 0) {
      lstName.setSelectedIndex(0);
    }

    pnlData.add(lblName);
    pnlData.add(listScrollPane);
    SpringUtilities.makeCompactGrid(pnlData, 1, 2, 10, 10, 10, 10);
  }

  protected void setEditableData() {
    if (act == ACT_NEW) {
      lstName.setEnabled(true);
    } else {
      lstName.setEnabled(false);
    }
  }

  protected boolean validateData() {
    int indexName = lstName.getSelectedIndex();
    // --------------------
    if (indexName == -1) {
      DialogUtils.errorMsg(Messages.getString("Message.IsEmpty") + ": "
          + Messages.getString("Column.Name"));
      return false;
    }
    return true;
  }

  protected boolean saveObj() {
    if (act == ACT_NEW) {
      ArrayList<String> newNodes = XmlUtils.getNewNodes(parentNode);
      String newNodeName = newNodes.get(lstName.getSelectedIndex());
      // ---------------------------
      Document docum = parentNode.getOwnerDocument();
      Element item = docum.createElement(newNodeName);
      newNode = parentNode.appendChild(item);
      // ---------------------------
    }
    return true;
  }

  public Node getNewNode() {
    return newNode;
  }

}
