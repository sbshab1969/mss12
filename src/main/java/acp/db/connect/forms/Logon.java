package acp.db.connect.forms;

import java.awt.event.*;
import java.util.Properties;

import javax.swing.*;

import acp.Main;
import acp.db.connect.IDbConnect;
import acp.forms.frame.FrameLogon;
import acp.utils.*;

public class Logon extends FrameLogon {
  private static final long serialVersionUID = 1L;
  
  private IDbConnect dbConnect = Main.dbConnect;

  private Properties currProp;

  private String[] listConfig;

  private JLabel lblUser = new JLabel(Messages.getString("Column.User"),JLabel.TRAILING);
  private JLabel lblPassword = new JLabel(Messages.getString("Column.Password"),JLabel.TRAILING);
  private JLabel lblDatabase = new JLabel(Messages.getString("Column.FileConfig"),JLabel.TRAILING);

  private JTextField txtUser = new JTextField(20);
  private JPasswordField txtPassword = new JPasswordField(20);
  private JComboBox<String> cmbDatabase = new JComboBox<>();

  public Logon() {
    initPnlData();
    initFormNone();
    pack();
    setToCenter();
  }

  private void initPnlData() {
    pnlData.setLayout(new SpringLayout());
    
    pnlData.add(lblUser);
    pnlData.add(txtUser);
    lblUser.setLabelFor(txtUser);

    pnlData.add(lblPassword);
    pnlData.add(txtPassword);
    lblPassword.setLabelFor(txtPassword);

    pnlData.add(lblDatabase);
    pnlData.add(cmbDatabase);
    lblDatabase.setLabelFor(cmbDatabase);

    SpringUtilities.makeCompactGrid(pnlData, 3, 2, 10, 10, 10, 10);

    BtnActionListener btnActionListener = new BtnActionListener();
    btnOk.addActionListener(btnActionListener);
    btnCancel.addActionListener(btnActionListener);
  }  
  
  protected void clearData() {
    txtUser.setText("");
    txtPassword.setText("");
  }
 
  protected boolean fillData() {
    initListDb();
    // -------------------------------
    Properties props = dbConnect.getDbProp();
    if (props != null) {
      String vIndex = props.getProperty(dbConnect.getDbKeyIndex(),"-1");
      int index = Integer.valueOf(vIndex);
      if (index>=0 && index < cmbDatabase.getItemCount()) {
        cmbDatabase.setSelectedIndex(index);
      }  
    } else {
      if (cmbDatabase.getItemCount()>0 ) {
        cmbDatabase.setSelectedIndex(0);
      }
    }
    // -------------------------------
    return true;
  }

  private void initListDb() {
    // -------------------------------
    if (listConfig == null) {
      listConfig = dbConnect.getFileList();
    } 
    // -------------------------------
    cmbDatabase.removeAllItems();
    for (String conf : listConfig) {
      cmbDatabase.addItem(conf);
    }
//    cmbDatabase.addItem(null);
    
    cmbDatabase.setMaximumRowCount(3);
    cmbDatabase.setSelectedIndex(-1);
    // -------------------------------
    CbActionListener cbActionListener = new CbActionListener();
    cmbDatabase.addActionListener(cbActionListener);
    // -------------------------------
  }
  
  protected Properties getProperty() {
    String user = txtUser.getText();
    String passwd = new String(txtPassword.getPassword());
    // -------------------------------------------
    if (currProp != null) {
      currProp.setProperty(dbConnect.getDbKeyUser(), user);
      currProp.setProperty(dbConnect.getDbKeyPassword(), passwd);
    }  
    // -------------------------------------------
    return currProp; 
  }

  protected void setProperty(Properties props) {
    currProp = props;
    if (props != null) {
      txtUser.setText(props.getProperty(dbConnect.getDbKeyUser()));
      txtPassword.setText(props.getProperty(dbConnect.getDbKeyPassword()));
    } else {
      clearData();
    }
  }

  private class CbActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      JComboBox<?> cmb = (JComboBox<?>) e.getSource();
      int index = cmb.getSelectedIndex();
      if (index>=0 && index < cmbDatabase.getItemCount()) {
        String item = (String) cmb.getSelectedItem();
        // ---------------------------------------------
        Properties props = new Properties();
        props.setProperty(dbConnect.getDbKeyIndex(), String.valueOf(index));
        props.setProperty(dbConnect.getDbKeyName(), item);
        // ----------------------
        dbConnect.readCfg(props);  // user, password
        // ----------------------
        setProperty(props);
      } else {
        setProperty(null);
      }
    }
  }
  
  private class BtnActionListener implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      Object objSource = ae.getSource();
      if (objSource.equals(btnOk)) {
        // -----------------------------------------------
        Properties props = getProperty();  
        if (props == null) {
          DialogUtils.errorMsg(Messages.getString("Message.ConnectNoParams"));
          return;
        }
        dbConnect.setDbProp(props);
        //------------------
        // boolean res = DbConnect.connect();
        boolean res = dbConnect.reconnect();
        if (res == true) {
          dispose();
          resultForm = RES_OK;
          // DialogUtils.infoDialog(Messages.getString("Message.ConnectOk"));
        } else {  
          DialogUtils.errorMsg(Messages.getString("Message.ConnectError"));
          return;
        }
        // -----------------------------------------------

      } else if (objSource.equals(btnCancel)) {
        dispose();
        resultForm = RES_CANCEL;
      }
    }
  }

}
