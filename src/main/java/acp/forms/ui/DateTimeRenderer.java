package acp.forms.ui;

import java.text.DateFormat;
import javax.swing.table.*;

public class DateTimeRenderer extends DefaultTableCellRenderer {
  private static final long serialVersionUID = 1L;

  public void setValue(Object value) {
    DateFormat formatter = DateFormat.getDateTimeInstance();
    setText((value == null) ? "" : formatter.format(value));
  }

}
