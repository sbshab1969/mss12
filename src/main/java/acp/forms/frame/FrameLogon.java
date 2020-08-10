package acp.forms.frame;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import acp.utils.*;

public abstract class FrameLogon extends MyInternalFrame {
  private static final long serialVersionUID = 1L;
  
  protected int act = ACT_NONE;

  protected JPanel pnlData = new JPanel();
  
  protected JPanel pnlButtons = new JPanel();
  protected JPanel pnlBtnRecord = new JPanel();
  protected JButton btnOk = new JButton(Messages.getString("Button.Ok"));
  protected JButton btnCancel = new JButton(Messages.getString("Button.Cancel"));

  public FrameLogon() {
    pnlData.setBorder(new LineBorder(Color.BLACK));
    
    pnlBtnRecord.setLayout(new GridLayout(1, 2, 30, 0));
    pnlBtnRecord.add(btnOk);
    pnlBtnRecord.add(btnCancel);
    pnlButtons.add(pnlBtnRecord);

    add(pnlData, BorderLayout.CENTER);
    add(pnlButtons, BorderLayout.SOUTH);
  }

  public boolean initFormNone() {
    boolean res = initForm(ACT_NONE);
    return res;
  }

  public boolean initForm() {
    boolean res = initForm(ACT_LOGON);
    return res;
  }

  public boolean initForm(int act) {
    this.act = act;
    // ------------------------
    // Заголовок
    // ------------------------
    if (act == ACT_LOGON) {
      setTitle(Messages.getString("Title.Logon"));
    } else {
      setTitle(Messages.getString("Title.RecordNone"));
    }
    // ------------------------
    // Значения полей
    // ------------------------
    boolean res = true;
    if (act == ACT_LOGON) {
      res = fillData();
    }
    setEnabledButtons();
    // ------------------------
    initFormAfter();
    // ------------------------
    return res;
  }

  protected void initFormBefore() {
  }
  
  protected void initFormAfter() {
  }

  protected void clearData() {
  }

  protected boolean fillData() {
    return true;
  }

  protected void setEnabledButtons() {
    if (act == ACT_LOGON) {
      btnOk.setEnabled(true);
    } else {
      btnOk.setEnabled(false);
    }
    btnCancel.setEnabled(true);
  }

}
