package acp.forms.frame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import acp.utils.*;

public abstract class FrameEdit extends MyInternalFrame {
  private static final long serialVersionUID = 1L;

  protected int act = ACT_NONE;
  protected Long recId = null;

  protected JPanel pnlData = new JPanel();
  protected JPanel pnlButtons = new JPanel();
  protected JPanel pnlBtnRecord = new JPanel();
  protected JButton btnSave = new JButton(Messages.getString("Button.Save"));
  protected JButton btnCancel = new JButton(Messages.getString("Button.Cancel"));

  public FrameEdit() {
    pnlData.setBorder(new LineBorder(Color.BLACK));

//  pnlBtnRecord.setBorder(new LineBorder(Color.BLACK));
    pnlBtnRecord.setLayout(new GridLayout(1, 2, 30, 0));
    pnlBtnRecord.add(btnSave);
    pnlBtnRecord.add(btnCancel);
    pnlButtons.add(pnlBtnRecord);

    add(pnlData, BorderLayout.CENTER);
    add(pnlButtons, BorderLayout.SOUTH);

    MyActionListener myActionListener = new MyActionListener();
    btnSave.addActionListener(myActionListener);
    btnCancel.addActionListener(myActionListener);
  }

  public boolean initFormNone() {
    boolean res = initForm(ACT_NONE, null);
    return res;
  }

  public boolean initForm() {
    boolean res = initForm(ACT_NONE, null);
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
    if (act == ACT_NEW) {
      setTitle(Messages.getString("Title.RecordAdd"));
    } else if (act == ACT_EDIT) {
      setTitle(Messages.getString("Title.RecordEdit"));
    } else if (act == ACT_DELETE) {
      setTitle(Messages.getString("Title.RecordDelete"));
    } else {
      setTitle(Messages.getString("Title.RecordNone"));
    }
    // ------------------------
    initFormBefore();
    // ------------------------
    // Заполнение полей формы
    // ------------------------
    boolean res = true;
    if (act != ACT_NONE) {
      res = fillData();
    }
    setEditableData();
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

  protected void setEditableData() {
  }
  
  protected void setEnabledButtons() {
    if (act != ACT_NONE) {
      btnSave.setEnabled(true);
    } else {
      btnSave.setEnabled(false);
    }
    btnCancel.setEnabled(true);
  }

  protected void clearData() {
  }
  
  protected boolean fillData() {
    return true;
  }
  
  protected boolean validateData() {
    return true;
  }
  
  protected Object getObj() {
    return null;
  }

  protected abstract boolean saveObj();

  private class MyActionListener implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      Object objSource = ae.getSource();

      if (objSource.equals(btnSave)) {
        if (act != ACT_NONE) {
          boolean resValidate = validateData();
          if (resValidate) {
            // -----------------
            boolean res = saveObj();
            // -----------------
            if (res) {
              dispose();
              resultForm = RES_OK;
            }
          }
        }

      } else if (objSource.equals(btnCancel)) {
        dispose();
        resultForm = RES_CANCEL;
      }
    }
  }

}
