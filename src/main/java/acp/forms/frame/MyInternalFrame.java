package acp.forms.frame;

import java.awt.*;
import java.beans.PropertyVetoException;
import javax.swing.*;

import acp.Main;

public abstract class MyInternalFrame extends ModalInternalFrame {
  private static final long serialVersionUID = 1L;

  public final static int ACT_NONE = -1;
  public final static int ACT_LOGON = 0;
  public final static int ACT_VIEW = 1;
  public final static int ACT_NEW = 2;
  public final static int ACT_EDIT = 3;
  public final static int ACT_DELETE = 4;
  public final static int ACT_COPY = 5;

  public final static int RES_NONE = -1;
  public final static int RES_OK = 1;
  public final static int RES_CANCEL = 0;

  protected int resultForm = RES_NONE;

  protected final static JDesktopPane desktop = Main.desktop;

  public MyInternalFrame() {
    setSize(400, 300);
    setClosable(true);
    // setResizable(true);
    // setIconifiable(true);
  }

  public void setToCenter() {
    int desktopWidth = desktop.getWidth();
    int desktopHeight = desktop.getHeight();
    int frameWidth = getWidth();
    int frameHeight = getHeight();

    Point newLocation = new Point((desktopWidth - frameWidth) / 2,
        (desktopHeight - frameHeight) / 2);
    setLocation(newLocation);
    // doLayout();
    validate();
  }
  
  public void showForm() {
    desktop.add(this);
    try {
      setSelected(true);
    } catch (PropertyVetoException e1) {
    }
    // -----------------------
    showModal(true);
  }
  
  public int getResultForm() {
    return resultForm;
  }

}
