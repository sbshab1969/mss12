package acp.forms.dm;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import org.w3c.dom.*;

import acp.utils.*;

public class DmConfigFields extends AbstractTableModel {
  private static final long serialVersionUID = 1L;

  private Node node;

  private int cols = 0;
  private int rows = 0;
  ArrayList<String> validAttrs;
  String[] headers;
  ArrayList<String[]> listData = new ArrayList<>();

  public DmConfigFields(Node vNode) {
    node = vNode;
    fillHead();
    fillTable();
  }

  public void fillHead() {
    validAttrs = XmlUtils.getValidAttrs("field");
    cols = validAttrs.size();
    headers = new String[cols];
    for (int i = 0; i < cols; i++) {
      // headers[i] = validAttrs.get(i);
      String key = "field." + validAttrs.get(i);
      String attrName = FieldConfig.getString(key);
      headers[i] = attrName;
    }
  }

  public String[] fillRow(Node fldNode) {
    NamedNodeMap attrs = fldNode.getAttributes();
    String[] newRow = new String[cols];
    for (int i = 0; i < cols; i++) {
      String nameAttr = validAttrs.get(i);
      Node attr = attrs.getNamedItem(nameAttr);
      if (attr != null) {
        newRow[i] = attr.getNodeValue();
      }
    }
    return newRow;
  }

  public void fillTable() {
    rows = 0;
    listData.clear();
    NodeList childs = node.getChildNodes();
    for (int i = 0; i < childs.getLength(); i++) {
      Node fieldNode = childs.item(i);
      if (XmlUtils.isValidNode(fieldNode)) {
        if (fieldNode.getNodeName().equals("field")) {
          addRow(fieldNode);
        }
      }
    }
  }

  public void addRow(Node fldNode) {
    String[] newRow = fillRow(fldNode);
    listData.add(newRow);
    rows++;
  }

  public void deleteRow(int row) {
    listData.remove(row);
    rows--;
    fireTableDataChanged();
  }

  public Node getNode() {
    return node;
  }

  public String[] getHeader() {
    return headers;
  }

  public String[] getRowData(int row) {
    if (row < 0 || row >= listData.size()) {
      return null;
    }
    return listData.get(row);
  }

  public ArrayList<String> getValidAttrs() {
    return validAttrs;
  }

  public ArrayList<String[]> getListData() {
    return listData;
  }

  @Override
  public String getColumnName(int col) {
    return headers[col];
  }

  @Override
  public int getColumnCount() {
    return cols;
  }

  @Override
  public int getRowCount() {
    return rows;
  }

  @Override
  public Object getValueAt(int row, int col) {
    return (listData.get(row))[col];
  }

  @Override
  public void setValueAt(Object val, int row, int col) {
    String strVal = val.toString();
    (listData.get(row))[col] = strVal;
    fireTableDataChanged();
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    // return (col != 0);
    return false;
  }

  @Override
  public Class<?> getColumnClass(int col) {
    return String.class;
  }

}
