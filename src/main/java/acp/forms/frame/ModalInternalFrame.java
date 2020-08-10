package acp.forms.frame;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class ModalInternalFrame extends JInternalFrame {
  private static final long serialVersionUID = 1L;

  private boolean modal = false;

  public boolean isModal() {
    return this.modal;
  }

  public void showModal(boolean value) {
    super.setVisible(value);
    this.modal = value;
    if (value) {
      startModal();
    } else {
      stopModal();
    }
  }

  private synchronized void startModal() {
    try {
      if (SwingUtilities.isEventDispatchThread()) {
        EventQueue theQueue = getToolkit().getSystemEventQueue();
        while (isVisible()) {
          AWTEvent event = theQueue.getNextEvent();
          Object source = event.getSource();
          boolean dispatch = true;
          // -------------------------------
          if (event instanceof MouseEvent) {
            MouseEvent e = (MouseEvent) event;
            MouseEvent m = SwingUtilities.convertMouseEvent(
                (Component) e.getSource(), e, this);
            if (!this.contains(m.getPoint())
                && e.getID() != MouseEvent.MOUSE_DRAGGED
               // && e.getID() != MouseEvent.MOUSE_WHEEL
            ) {
              dispatch = false;
            }
          }
          // -------------------------------
          if (dispatch) {
            if (event instanceof ActiveEvent) {
              ((ActiveEvent) event).dispatch();
            } else if (source instanceof Component) {
              ((Component) source).dispatchEvent(event);
            } else if (source instanceof MenuComponent) {
              ((MenuComponent) source).dispatchEvent(event);
            } else {
              System.err.println("Unable to dispatch: " + event);
            }
          }
          // -------------------------------
        }
      } else {
        while (isVisible()) {
          wait();
        }
      }
    } catch (InterruptedException ignored) {
    }
  }

  private synchronized void stopModal() {
    notifyAll();
  }

}
