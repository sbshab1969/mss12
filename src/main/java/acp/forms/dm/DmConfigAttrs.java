package acp.forms.dm;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import acp.utils.*;

public class DmConfigAttrs extends AbstractTableModel {
  private static final long serialVersionUID = 1L;

  private Node node;

  private final String[] HEADERS = { 
      Messages.getString("Column.Name")
     ,Messages.getString("Column.Value") 
   };

  private final int COLS = 2;
  private int rows = 0;
  ArrayList<String[]> list = new ArrayList<>();

  // ArrayList<String[]> list = new ArrayList<String[]>();

  public DmConfigAttrs(Node vNode) {
    node = vNode;
    refreshTable();
  }

  public void refreshTable() {
    rows = 0;
    list.clear();
    // ----------
    NamedNodeMap attrs = node.getAttributes();
    for (int i = 0; i < attrs.getLength(); i++) {
      Node attr = attrs.item(i);
      String key = node.getNodeName().trim() + "." + attr.getNodeName().trim();
      String attrName = FieldConfig.getString(key);
      String attrValue = attr.getNodeValue();
      addRow(attrName, attrValue);
    }
  }

  public Node getNode() {
    return node;
  }

  public Node getAttr(int n) {
    NamedNodeMap attrs = node.getAttributes();
    Node attr = attrs.item(n);
    return attr;
  }

  @Override
  public String getColumnName(int col) {
    return HEADERS[col];
  }

  @Override
  public int getRowCount() {
    return rows;
  }

  @Override
  public int getColumnCount() {
    return COLS;
  }

  @Override
  public Object getValueAt(int row, int col) {
    return list.get(row)[col];
  }

  @Override
  public void setValueAt(Object val, int row, int col) {
    String strVal = val.toString();
    list.get(row)[col] = strVal;
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

  public void addRow(String name, String value) {
    String[] newRow = new String[COLS];
    newRow[0] = name;
    newRow[1] = value;
    // ----------
    list.add(newRow);
    rows++;
  }

  public void deleteRow(int row) {
    list.remove(row);
    rows--;
    fireTableDataChanged();
  }

}
