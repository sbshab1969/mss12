package acp.forms;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import acp.db.service.IConfigManagerEdit;
import acp.forms.dm.DmConfigAttrs;
import acp.forms.dm.DmConfigFields;
import acp.forms.frame.FrameEdit;
import acp.utils.*;

public class ConfigTree extends FrameEdit {
  private static final long serialVersionUID = 1L;
  private static Logger logger = LoggerFactory.getLogger(ConfigTree.class);

  private IConfigManagerEdit tableManager;

  private static final int FIELDS_COUNT = 10;
  private static final String FIELD_NAME = "field";

  private String cfgName = "";
  private Document doc = null;

  private JTree tree = new JTree();
  private DefaultTreeModel treeModel;

  private JTable attrTable = new JTable();
  private DmConfigAttrs dmAttr;

  private JTable fieldTable = new JTable();
  private DmConfigFields dmField;

  private JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
  private JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP,
      JTabbedPane.SCROLL_TAB_LAYOUT);

  private JPanel pnlTree = new JPanel();
  private JPanel pnlAttr = new JPanel();
  private JPanel pnlField = new JPanel();

  private JScrollPane treeView = new JScrollPane();
  private JScrollPane attrView = new JScrollPane();
  private JScrollPane fieldView = new JScrollPane();

  private JPanel pnlTreeBut = new JPanel();
  private JPanel pnlTreeButRec = new JPanel();
  private JButton btnTreeAdd = new JButton(Messages.getString("Button.Add"));
  private JButton btnTreeDel = new JButton(Messages.getString("Button.Delete"));

  private JPanel pnlAttrBut = new JPanel();
  private JPanel pnlAttrButRec = new JPanel();
  private JButton btnAttrAdd = new JButton(Messages.getString("Button.Add"));
  private JButton btnAttrUpd = new JButton(Messages.getString("Button.Edit"));
  private JButton btnAttrDel = new JButton(Messages.getString("Button.Delete"));

  private JPanel pnlFieldBut = new JPanel();
  private JPanel pnlFieldButRec = new JPanel();
  private JPanel pnlFieldButImp = new JPanel();
  private JButton btnFieldAdd = new JButton(Messages.getString("Button.Add"));
  private JButton btnFieldUpd = new JButton(Messages.getString("Button.Edit"));
  private JButton btnFieldDel = new JButton(Messages.getString("Button.Delete"));
  private JButton btnFieldImp = new JButton(Messages.getString("Button.Import"));
  private JButton btnFieldExp = new JButton(Messages.getString("Button.Export"));

  public ConfigTree(IConfigManagerEdit tblManager) {
    tableManager = tblManager;

    setMaximizable(true);
    setResizable(true);
    setClosable(true);
    setSize(700, 600);

    initPnlData();
    initFormNone();
//    pack();
    setToCenter();
  }

  private void initPnlData() {
    pnlData.setLayout(new BorderLayout());
    pnlData.add(splitter, BorderLayout.CENTER);
    
    splitter.setTopComponent(pnlTree);
    splitter.setBottomComponent(tabPane);
    // splitter.setBottomComponent(pnlAttr);
    tabPane.add(Messages.getString("Title.Attrs"), pnlAttr);
    tabPane.add(Messages.getString("Title.Fields"), pnlField);
    // splitter.setDividerLocation(getHeight() / 3);
    splitter.setOneTouchExpandable(true);
    splitter.setResizeWeight(0.7);
    splitter.setContinuousLayout(true);

    treeView.setViewportView(tree);
    attrView.setViewportView(attrTable);
    attrTable.setFillsViewportHeight(true);
    attrTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    fieldView.setViewportView(fieldTable);
    fieldTable.setFillsViewportHeight(true);
    fieldTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    fieldTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    pnlTree.setLayout(new BorderLayout());
    pnlTreeBut.setLayout(new BorderLayout());
    pnlTreeBut.setBorder(new LineBorder(Color.BLACK));
    pnlTreeBut.add(pnlTreeButRec, BorderLayout.WEST);
    pnlTreeButRec.add(btnTreeAdd);
    pnlTreeButRec.add(btnTreeDel);

    pnlAttr.setLayout(new BorderLayout());
    pnlAttrBut.setLayout(new BorderLayout());
    pnlAttrBut.setBorder(new LineBorder(Color.BLACK));
    pnlAttrBut.add(pnlAttrButRec, BorderLayout.WEST);
    pnlAttrButRec.add(btnAttrAdd);
    pnlAttrButRec.add(btnAttrUpd);
    pnlAttrButRec.add(btnAttrDel);

    pnlField.setLayout(new BorderLayout());

    pnlFieldBut.setLayout(new BorderLayout());
    pnlFieldBut.setBorder(new LineBorder(Color.BLACK));
    pnlFieldBut.add(pnlFieldButRec, BorderLayout.WEST);
    pnlFieldBut.add(pnlFieldButImp, BorderLayout.EAST);

    pnlFieldButRec.add(btnFieldAdd);
    pnlFieldButRec.add(btnFieldUpd);
    pnlFieldButRec.add(btnFieldDel);

    pnlFieldButImp.add(btnFieldExp);
    pnlFieldButImp.add(btnFieldImp);

    pnlTree.add(treeView, BorderLayout.CENTER);
    pnlTree.add(pnlTreeBut, BorderLayout.SOUTH);

    pnlAttr.add(attrView, BorderLayout.CENTER);
    pnlAttr.add(pnlAttrBut, BorderLayout.SOUTH);

    pnlField.add(fieldView, BorderLayout.CENTER);
    pnlField.add(pnlFieldBut, BorderLayout.SOUTH);

    tree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) tree
            .getLastSelectedPathComponent();
        if (treeNode != null) {
          createTableAttr(treeNode);
          createTableField(treeNode);
        }
      }
    });

    TreeActionListener treeActionListener = new TreeActionListener();
    btnTreeAdd.addActionListener(treeActionListener);
    btnTreeDel.addActionListener(treeActionListener);

    AttrActionListener attrActionListener = new AttrActionListener();
    btnAttrAdd.addActionListener(attrActionListener);
    btnAttrUpd.addActionListener(attrActionListener);
    btnAttrDel.addActionListener(attrActionListener);

    FieldActionListener fieldActionListener = new FieldActionListener();
    btnFieldAdd.addActionListener(fieldActionListener);
    btnFieldUpd.addActionListener(fieldActionListener);
    btnFieldDel.addActionListener(fieldActionListener);
    btnFieldImp.addActionListener(fieldActionListener);
    btnFieldExp.addActionListener(fieldActionListener);

    attrTable.addMouseListener(new MyAttrMouseListener());
    fieldTable.addMouseListener(new MyFieldMouseListener());
  }
  
  protected void initFormBefore() {
  }
  
  protected void initFormAfter() {
    setTitle(Messages.getString("Title.ConfigEdit") + " " + cfgName);
  }

  protected boolean fillData() {
    if (act == ACT_EDIT) {
      String cfgName = tableManager.getCfgName(recId);
      String cfgString = tableManager.getCfgStr(recId);
      Reader cfgReader = new StringReader(cfgString);
      InputSource is = new InputSource(cfgReader);
      Document doc = createDoc(is);
      createTree(doc);
      // --------------------
      this.cfgName = cfgName;
      this.doc = doc;
      // --------------------
    }
    return true;
  }

  protected boolean saveObj() {
    boolean res = false;
    String txtCfg = XmlUtils.xml2string(doc);
    if (act == ACT_EDIT) {
      res = tableManager.updateCfgStr(recId, txtCfg);
    }
    return res;
  }
  
  private Document createDoc(InputSource is) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    Document doc = null;
    try {
      builder = factory.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      DialogUtils.errorPrint(e);
      return null;
    }
    try {
      doc = builder.parse(is);
    } catch (SAXException | IOException e) {
      DialogUtils.errorPrint(e,logger);
      return null;
    }
    doc.setXmlStandalone(true);
    doc.normalizeDocument();
    // doc.getDocumentElement().normalize();
    return doc;
  }

  private void createTree(Document doc) {
    Element root = doc.getDocumentElement();
    NodeList childs = root.getChildNodes();
    for (int i = 0; i < childs.getLength(); i++) {
      Node cfg = childs.item(i);
      if (XmlUtils.isValidNode(cfg)) {
        String nodeName = cfg.getNodeName().trim();
        if (nodeName.equalsIgnoreCase("ats")
            || nodeName.equalsIgnoreCase("eml.mailer")
            || nodeName.equalsIgnoreCase("sverka.ats")) {

          DefaultMutableTreeNode top = new DefaultMutableTreeNode(new NodeInfo(
              cfg));
          treeModel = new DefaultTreeModel(top);
          tree.setModel(treeModel);

          tree.removeAll();
          NodeList cfgChilds = cfg.getChildNodes();
          for (int j = 0; j < cfgChilds.getLength(); j++) {
            Node childNode = cfgChilds.item(j);
            putNode(top, childNode);
          }
          tree.setSelectionRow(0);
          tree.expandRow(0);
          // tree.setSelectionPath(new TreePath(top.getPath()));
          // tree.expandPath(new TreePath(top.getPath()));
        }
      }
    }
  }

  private void putNode(DefaultMutableTreeNode treeNode, Node newNode) {
    if (XmlUtils.isValidNode(newNode)) {
      DefaultMutableTreeNode item = new DefaultMutableTreeNode(new NodeInfo(
          newNode));
      treeNode.add(item);
      NodeList subNodes = newNode.getChildNodes();
      for (int i = 0; i < subNodes.getLength(); i++) {
        Node subnode = subNodes.item(i);
        // if (!subnode.getNodeName().equals("field")) {
        putNode(item, subnode);
        // }
      }
    }
  }

  private Node getNode(DefaultMutableTreeNode treeNode) {
    NodeInfo nodeInfo = (NodeInfo) treeNode.getUserObject();
    Node node = nodeInfo.getNode();
    return node;
  }

  private Node getCurrentNode() {
    DefaultMutableTreeNode currentTreeNode = (DefaultMutableTreeNode) tree
        .getLastSelectedPathComponent();
    Node node = getNode(currentTreeNode);
    return node;
  }

  private void createTableAttr(DefaultMutableTreeNode treeNode) {
    Node node = getNode(treeNode);
    dmAttr = new DmConfigAttrs(node);
    attrTable.setModel(dmAttr);
    if (dmAttr.getRowCount() > 0) {
      attrTable.setRowSelectionInterval(0, 0);
    }
  }

  private void createTableField(DefaultMutableTreeNode treeNode) {
    Node node = getNode(treeNode);
    dmField = new DmConfigFields(node);
    fieldTable.setModel(dmField);
    if (dmField.getColumnCount() < FIELDS_COUNT) {
      fieldTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    } else {
      fieldTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }
    if (dmField.getRowCount() > 0) {
      fieldTable.setRowSelectionInterval(0, 0);
    }
  }

  private void addTreeNode(int act) {
    DefaultMutableTreeNode currentTreeNode = (DefaultMutableTreeNode) tree
        .getLastSelectedPathComponent();
    Node node = getNode(currentTreeNode);
    // ----------------------------------------------
    ConfigChild cfgChild = new ConfigChild(node);
    // ----------------------------------------------
    boolean resInit = true;
    resInit = cfgChild.initForm(act);
    if (resInit) {
      cfgChild.showForm();
      int resForm = cfgChild.getResultForm();
      if (resForm == RES_OK) {
        Node newNode = cfgChild.getNewNode();
        DefaultMutableTreeNode newTreeNode = new DefaultMutableTreeNode(
            new NodeInfo(newNode));
        currentTreeNode.add(newTreeNode);
        treeModel.reload(currentTreeNode);
        // tree.setSelectionPath(new TreePath(newTreeNode.getPath()));
      }
    }
    cfgChild = null;
    // ---------------------------
    int currentRow = tree.getSelectionModel().getMinSelectionRow();
    if (currentRow < 0) {
      tree.setSelectionRow(0);
    }
    dmField.fillTable();
    fieldTable.updateUI();
  }

  private void deleteTreeNode() {
    DefaultMutableTreeNode currentTreeNode = (DefaultMutableTreeNode) tree
        .getLastSelectedPathComponent();
    Node node = getNode(currentTreeNode);

    Node parentNode = node.getParentNode();
    if (parentNode == null) {
      DialogUtils.errorMsg(Messages.getString("Message.RootNode"));
      return;
    }

    DefaultMutableTreeNode parentTreeNode = (DefaultMutableTreeNode) (currentTreeNode
        .getParent());
    if (parentTreeNode == null) {
      DialogUtils.errorMsg(Messages.getString("Message.RootNode"));
      return;
    }

    if (DialogUtils.confirmDialog(Messages.getString("Message.DeleteNode"),
        Messages.getString("Title.RecordDelete"), 1) == 0) {
      // ---------------------------
      parentNode.removeChild(node); // XML
      treeModel.removeNodeFromParent(currentTreeNode); // Tree
      // ---------------------------
      treeModel.reload(parentTreeNode);
      // tree.setSelectionRow(0);
      tree.setSelectionPath(new TreePath(parentTreeNode.getPath()));
    }
  }

  private void refreshTreeNode() {
    DefaultMutableTreeNode currentTreeNode = (DefaultMutableTreeNode) tree
        .getLastSelectedPathComponent();
    NodeInfo nodeInfo = (NodeInfo) currentTreeNode.getUserObject();
    nodeInfo.fillTitle();
    treeModel.reload(currentTreeNode);
    int currentRow = tree.getSelectionModel().getMinSelectionRow();
    if (currentRow < 0) {
      currentRow = 0;
    }
    tree.setSelectionRow(currentRow);
  }

  private boolean testAttr() {
    int rows = dmAttr.getRowCount();
    if (rows == 0) {
      DialogUtils.errorMsg(Messages.getString("Message.NoAttr"));
      return false;
    }
    int row = attrTable.getSelectedRow();
    if (row < 0) {
      DialogUtils.errorMsg(Messages.getString("Message.NoSelectAttr"));
      return false;
    }
    return true;
  }

  private void editAttr(int act, Node vNode) {
    int row = -1;
    Node vAttr = null;
    if (act == ACT_EDIT) {
      if (testAttr() == false) {
        return;
      }
      row = attrTable.getSelectedRow();
      vAttr = dmAttr.getAttr(row);
    }
    // ----------------------------------------------
    ConfigAttr cfgAttr = new ConfigAttr(vNode, vAttr);
    // ----------------------------------------------
    boolean resInit = true;
    resInit = cfgAttr.initForm(act);
    if (resInit) {
      cfgAttr.showForm();
      int resForm = cfgAttr.getResultForm();
      if (resForm == RES_OK) {
        dmAttr.refreshTable(); // Table
        refreshTreeNode(); // Tree
        if (row >= 0) {
          attrTable.setRowSelectionInterval(row, row);
        } else {
          attrTable.setRowSelectionInterval(0, 0);
        }
      }
    }
    cfgAttr = null;
  }

  private void deleteAttr() {
    if (testAttr() == false) {
      return;
    }
    int row = attrTable.getSelectedRow();
    if (DialogUtils.confirmDialog(Messages.getString("Message.DeleteRecord"),
        Messages.getString("Title.RecordDelete"), 1) == 0) {
      // --------------------
      dmAttr.deleteRow(row); // Attr
      XmlUtils.deleteAttr(dmAttr.getNode(), row); // XML
      refreshTreeNode(); // Tree
      // --------------------
      if (dmAttr.getRowCount() > 0) {
        attrTable.setRowSelectionInterval(0, 0);
      }
    }
  }

  private boolean testField() {
    int rows = dmField.getRowCount();
    if (rows == 0) {
      DialogUtils.errorMsg(Messages.getString("Message.NoField"));
      return false;
    }
    int row = fieldTable.getSelectedRow();
    if (row < 0) {
      DialogUtils.errorMsg(Messages.getString("Message.NoSelectField"));
      return false;
    }
    return true;
  }

  private void editFields(int act) {
    int row = -1;
    if (act == ACT_NEW) {
      String nodeName = getCurrentNode().getNodeName();
      if (XmlUtils.existValidNode(nodeName, FIELD_NAME) == false) {
        DialogUtils.errorMsg(Messages.getString("Message.NoNewField"));
        return;
      }
    } else if (act == ACT_EDIT) {
      if (testField() == false) {
        return;
      }
      row = fieldTable.getSelectedRow();
    }
    // ----------------------------------------------
    ConfigFields cfgFields = new ConfigFields(dmField, row);
    // ----------------------------------------------
    boolean resInit = true;
    resInit = cfgFields.initForm(act);
    if (resInit) {
      cfgFields.showForm();
      int resForm = cfgFields.getResultForm();
      if (resForm == RES_OK) {
        dmField.fillTable(); // Table
        DefaultMutableTreeNode currentTreeNode = 
            (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (act == ACT_NEW) {
          Node newNode = cfgFields.getCurrentNode();
          DefaultMutableTreeNode newTreeNode = new DefaultMutableTreeNode(
              new NodeInfo(newNode));
          currentTreeNode.add(newTreeNode);
          treeModel.reload(currentTreeNode);
          tree.setSelectionPath(new TreePath(currentTreeNode.getPath()));
          row = dmField.getRowCount() - 1;
          fieldTable.setRowSelectionInterval(row, row);
          // fieldTable.updateUI();

        } else if (act == ACT_EDIT) {
          DefaultMutableTreeNode childTreeNode = 
              (DefaultMutableTreeNode) currentTreeNode.getChildAt(row);
          NodeInfo nodeInfo = (NodeInfo) childTreeNode.getUserObject();
          nodeInfo.fillTitle();
          refreshTreeNode(); // Tree
          if (row >= 0) {
            fieldTable.setRowSelectionInterval(row, row);
          } else {
            fieldTable.setRowSelectionInterval(0, 0);
          }
        }
      }
    }
    cfgFields = null;
  }

  private void deleteField() {
    if (testField() == false) {
      return;
    }
    int row = fieldTable.getSelectedRow();
    if (DialogUtils.confirmDialog(Messages.getString("Message.DeleteRecord"),
        Messages.getString("Title.RecordDelete"), 1) == 0) {
      // --------------------
      dmField.deleteRow(row); // Fields
      XmlUtils.deleteChild(dmField.getNode(), row); // XML
      // --------------------
      DefaultMutableTreeNode currentTreeNode = (DefaultMutableTreeNode) tree
          .getLastSelectedPathComponent();
      currentTreeNode.remove(row); // Tree
      refreshTreeNode(); // Tree
      // --------------------
      if (dmField.getRowCount() > 0) {
        fieldTable.setRowSelectionInterval(0, 0);
      }
    }
  }

  private void exportFields() {
    if (testField() == false) {
      return;
    }
    JFileChooser fch = new JFileChooser();
    // if (fch.showSaveDialog(btnFieldExp) == JFileChooser.APPROVE_OPTION) {
    if (fch.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
      File vDir = fch.getSelectedFile();
      try {
        FileWriter fw = new FileWriter(vDir.getAbsolutePath());
        int rows = dmField.getRowCount();
        int cols = dmField.getColumnCount();
        for (int i = 0; i < rows; i++) {
          String[] newRow = dmField.getRowData(i);
          StringBuilder sbLine = new StringBuilder();
          for (int j = 0; j < cols; j++) {
            if (newRow[j] != null) {
              sbLine.append(newRow[j]);
            }
            if (j < cols - 1) {
              sbLine.append(";");
            }
          }
          sbLine.append("\n");
          fw.write(sbLine.toString());
        }
        fw.close();
        DialogUtils.infoDialog(Messages.getString("Message.ExportOK"));
      } catch (IOException e) {
        DialogUtils.errorPrint(e);
      }
    }
  }

  private String[] mySplit(String line) {
    line += "&";
    String[] arrLine = line.split(";");
    int cnt = arrLine.length;
    String strLast = arrLine[cnt - 1];
    if (strLast.length() > 1) {
      arrLine[cnt - 1] = strLast.substring(0, strLast.length() - 1);
    } else {
      arrLine[cnt - 1] = "";
    }
    // System.out.println(Arrays.deepToString(arrLine));
    return arrLine;
  }

  private boolean testImportFile(File impFile, int cntFields) {
    try {
      FileReader fr = new FileReader(impFile.getAbsolutePath());
      BufferedReader br = new BufferedReader(fr);
      String line;
      String[] arrFields;
      boolean isErr = false;
      while ((line = br.readLine()) != null) {
        arrFields = mySplit(line);
        if (arrFields.length != cntFields) {
          DialogUtils.errorMsg(Messages.getString("Message.CountFieldsError"));
          isErr = true;
          break;
        }
      }
      fr.close();
      if (isErr) {
        return false;
      }
    } catch (IOException e) {
      DialogUtils.errorPrint(e);
      return false;
    }
    return true;
  }

  private void fillAttrs(Node node, String[] attrValues) {
    Element nodeElem = (Element) node;
    int cols = dmField.getColumnCount();
    ArrayList<String> validAttrs = dmField.getValidAttrs();
    for (int i = 0; i < cols; i++) {
      String attrName = validAttrs.get(i);
      String attrValue = attrValues[i];
      if (!attrValue.equals("")) {
        nodeElem.setAttribute(attrName, attrValue);
      } else {
        nodeElem.removeAttribute(attrName);
      }
    }
  }

  private void loadFile(File impFile, int cntFields) {
    DefaultMutableTreeNode currentTreeNode = (DefaultMutableTreeNode) tree
        .getLastSelectedPathComponent();
    Node currentNode = getNode(currentTreeNode);
    Document docum = currentNode.getOwnerDocument();
    try {
      FileReader fr = new FileReader(impFile.getAbsolutePath());
      BufferedReader br = new BufferedReader(fr);
      String line;
      String[] arrFields;
      while ((line = br.readLine()) != null) {
        // XML --------------------
        Element item = docum.createElement(FIELD_NAME);
        Node newNode = currentNode.appendChild(item);
        arrFields = mySplit(line);
        fillAttrs(newNode, arrFields);
        // Tree -----------------------
        DefaultMutableTreeNode newTreeNode = new DefaultMutableTreeNode(
            new NodeInfo(newNode));
        currentTreeNode.add(newTreeNode);
      }
      fr.close();
    } catch (IOException e) {
      DialogUtils.errorPrint(e);
      return;
    }
    treeModel.reload(currentTreeNode);
    TreePath treePath = new TreePath(currentTreeNode.getPath());
    tree.setSelectionPath(treePath);
    tree.expandPath(treePath);

    dmField.fillTable();
    fieldTable.updateUI();
  }

  private void importFields() {
    String nodeName = getCurrentNode().getNodeName();
    if (XmlUtils.existValidNode(nodeName, FIELD_NAME) == false) {
      DialogUtils.errorMsg(Messages.getString("Message.NoNewField"));
      return;
    }
    int cntFields = dmField.getColumnCount();
    JFileChooser fch = new JFileChooser();
    fch.setFileFilter(new FileNameExtensionFilter("Text files", "csv", "txt"));
    fch.setFileSelectionMode(JFileChooser.FILES_ONLY);
    if (fch.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
      File fileImport = fch.getSelectedFile();
      if (testImportFile(fileImport, cntFields)) {
        loadFile(fileImport, cntFields);
      }
    }
  }

  private class TreeActionListener implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      Object objSource = ae.getSource();
      if (objSource.equals(btnTreeAdd)) {
        Node node = getCurrentNode();
        if (XmlUtils.getCountNewNodes(node) > 0) {
          addTreeNode(ACT_NEW);
        } else {
          DialogUtils.errorMsg(Messages.getString("Message.NoNewChild"));
        }

      } else if (objSource.equals(btnTreeDel)) {
        deleteTreeNode();
      }
    }
  }

  private class AttrActionListener implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      Object objSource = ae.getSource();
      if (objSource.equals(btnAttrAdd)) {
        Node node = dmAttr.getNode();
        if (XmlUtils.getCountNewAttrs(node) > 0) {
          editAttr(ACT_NEW, node);
        } else {
          DialogUtils.errorMsg(Messages.getString("Message.NoNewAttr"));
        }

      } else if (objSource.equals(btnAttrUpd)) {
        editAttr(ACT_EDIT, dmAttr.getNode());

      } else if (objSource.equals(btnAttrDel)) {
        deleteAttr();
      }
    }
  }

  private class FieldActionListener implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      Object objSource = ae.getSource();
      if (objSource.equals(btnFieldAdd)) {
        editFields(ACT_NEW);

      } else if (objSource.equals(btnFieldUpd)) {
        editFields(ACT_EDIT);

      } else if (objSource.equals(btnFieldDel)) {
        deleteField();

      } else if (objSource.equals(btnFieldExp)) {
        exportFields();

      } else if (objSource.equals(btnFieldImp)) {
        importFields();
      }
    }
  }

  private class MyAttrMouseListener extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      if (e.getClickCount() == 2) {
        editAttr(ACT_EDIT, dmAttr.getNode());
      }
    }
  }

  private class MyFieldMouseListener extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      if (e.getClickCount() == 2) {
        editFields(ACT_EDIT);
      }
    }
  }

}

class NodeInfo {
  private Node node;
  private String title;

  NodeInfo(Node vNode) {
    setNode(vNode);
  }

  Node getNode() {
    return node;
  }

  void setNode(Node vNode) {
    node = vNode;
    fillTitle();
  }

  void fillTitle() {
    String nodeName = node.getNodeName().trim();
    title = FieldConfig.getString(nodeName);
    NamedNodeMap attr = node.getAttributes();
    if (attr.getLength() > 0) {
      title += " [ ";
      for (int i = 0; i < attr.getLength(); i++) {
        Node at = attr.item(i);
        if (i > 0) {
          title += ", ";
        }
        title += at.getNodeValue();
      }
      title += " ]";
    }
  }

  public String toString() {
    return title;
  }

}
