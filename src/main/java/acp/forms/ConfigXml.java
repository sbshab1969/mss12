package acp.forms;

import java.awt.*;

import javax.swing.*;

import acp.db.service.IConfigManagerEdit;
import acp.forms.frame.FrameEdit;
import acp.utils.*;

public class ConfigXml extends FrameEdit {
  private static final long serialVersionUID = 1L;

  private IConfigManagerEdit tableManager;

  private JTextArea txtConf = new JTextArea();
  private JScrollPane txtView = new JScrollPane(txtConf);

  public ConfigXml(IConfigManagerEdit tblManager) {
    setSize(700, 500);
    setResizable(true);

    tableManager = tblManager;

    initPnlData();
    initFormNone();
//    pack();
    setToCenter();
  }

  private void initPnlData() {
    pnlData.setLayout(new BorderLayout());
    pnlData.add(txtView, BorderLayout.CENTER);
  }
  
  protected void initFormBefore() {
    setTitle(Messages.getString("Title.DirectEdit"));
  }

  protected void clearData() {
    txtConf.setText(null);
  }

  protected boolean fillData() {
    if (act == ACT_EDIT) {
      String txtCfg = tableManager.getCfgStr(recId);
      txtConf.setText(txtCfg);
    }  
    return true;
  }

  protected boolean saveObj() {
    boolean res = false;
    if (act == ACT_EDIT) {
      String txtCfg = txtConf.getText();
      res = tableManager.updateCfgStr(recId, txtCfg);
    }
    return res;
  }

}
