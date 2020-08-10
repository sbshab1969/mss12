package acp.forms.frame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import acp.utils.*;

public abstract class FrameView extends MyInternalFrame {
  private static final long serialVersionUID = 1L;

  protected int act = ACT_NONE;
  protected Long recId = null;
  
  protected JPanel pnlData = new JPanel();
  protected JPanel pnlButtons = new JPanel();
  protected JPanel pnlBtnRecord = new JPanel();
  protected JButton btnClose = new JButton(Messages.getString("Button.Close"));

  public FrameView() {
    pnlData.setBorder(new LineBorder(Color.BLACK));

    pnlBtnRecord.add(btnClose);
    btnClose.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });

    pnlButtons.setLayout(new BorderLayout());
    pnlButtons.add(pnlBtnRecord, BorderLayout.EAST);

    add(pnlData, BorderLayout.CENTER);
    add(pnlButtons, BorderLayout.SOUTH);
  }

  public boolean initFormNone() {
    boolean res = initForm(ACT_NONE, null);
    return res;
  }

  public boolean initForm() {
    boolean res = initForm(ACT_VIEW, null);
    return res;
  }

  public boolean initForm(int act) {
    boolean res = initForm(act, null);
    return res;
  }

  public boolean initForm(int act, Long recId) {
    // ------------------------
    this.act = act;
    this.recId = recId;
    // ------------------------
    // Заголовок
    // ------------------------
    if (act == ACT_VIEW) {
      setTitle(Messages.getString("Title.RecordView"));
    } else {
      setTitle(Messages.getString("Title.RecordNone"));
    }
    // ------------------------
    initFormBefore();
    // ------------------------
    // Значения полей
    // ------------------------
    boolean res = true;
    if (act == ACT_VIEW) {
      res = fillData();
    }
    // ------------------------
    initFormAfter();
    // ------------------------
    return res;
  }
  
  protected void initFormBefore() {
  }
  
  protected void initFormAfter() {
  }
 
  protected boolean fillData() {
    return true;
  }
  
}
