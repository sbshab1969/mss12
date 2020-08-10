package acp.forms;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;

import acp.db.service.ISourceManagerEdit;
import acp.db.service.IToptionManagerEdit;
import acp.db.service.IToptionManagerList;
import acp.db.service.factory.SourceManagerFactory;
import acp.db.service.factory.ToptionManagerFactory;
import acp.forms.dm.DmToptionList;
import acp.forms.frame.FrameList;
import acp.forms.ui.*;
import acp.utils.*;

public class XmlTableList extends FrameList {
  private static final long serialVersionUID = 1L;

  private IToptionManagerList listManager;
  private IToptionManagerEdit editManager;
  private ISourceManagerEdit sourceManager;
  private DmToptionList dmForm;

  private JPanel pnlFilter_1 = new JPanel();
  private JPanel pnlFilter_2 = new JPanel();
  private JPanel pnlBtnFilter = new JPanel();

  private JLabel lblSource = new JLabel(Messages.getString("Column.SourceName"),
      JLabel.TRAILING);
  private JComboBox<CbClass> cbdbSource;
  private CbModel cbdbSourceModel;

  public XmlTableList(String keyTitle, String path, ArrayList<String> attrs) {
    setTitle(FieldConfig.getString(keyTitle));
    setSize(640, 480);
    setToCenter(); // метод из MyInternalFrame
    setMaximizable(true);
    setResizable(true);

    // Filter ---
    setFilterVisible(true);
    initFilter();

    // --- Table ---
    listManager = ToptionManagerFactory.getManagerList(path, attrs);
    editManager = ToptionManagerFactory.getManagerEdit(path, attrs);
    sourceManager = SourceManagerFactory.getManagerEdit();
    dmForm = new DmToptionList(listManager);

    pnlTable.setModel(dmForm);
    // pnlTable.setModePage(true);
    // pnlTable.setRecPerPage(3);

    // Buttons ---
    pnlBtnRecord.add(btnEdit);
    pnlBtnAct.add(btnRefresh);
  }

  private void initFilter() {
    pnlFilter.setBorder(new LineBorder(Color.BLACK));
    pnlFilter.setLayout(new BorderLayout());

    cbdbSourceModel = new CbModel();
    cbdbSourceModel.setNeedNullItem(true);
    cbdbSource = new JComboBox<CbClass>(cbdbSourceModel);
    lblSource.setLabelFor(cbdbSource);

    pnlFilter_1.setLayout(new SpringLayout());
    // pnlFilter_1.setBorder(new LineBorder(Color.BLACK));
    pnlFilter_1.add(lblSource);
    pnlFilter_1.add(cbdbSource);
    SpringUtilities.makeCompactGrid(pnlFilter_1, 1, 2, 8, 8, 8, 8);

    pnlBtnFilter.setLayout(new GridLayout(1, 2, 5, 5));
    pnlBtnFilter.add(btnFilter);
    pnlBtnFilter.add(btnFltClear);

    pnlFilter_2.setLayout(new FlowLayout());
    pnlFilter_2.add(pnlBtnFilter);

    pnlFilter.add(pnlFilter_1, BorderLayout.CENTER);
    pnlFilter.add(pnlFilter_2, BorderLayout.EAST);
  }
  
  public void initForm() {
    cbdbSourceModel.setArrayString(sourceManager.getSources());
    cbdbSource.setSelectedIndex(-1);
    // -----------
    applyFilter();
    // -----------
  }

  protected void applyFilter() {
    int index = cbdbSource.getSelectedIndex();
    Long keyLong = cbdbSourceModel.getKeyLongAt(index);
    listManager.createTable(keyLong);
    listManager.prepareQuery(null);
    pnlTable.queryTable(pnlTable.NAV_FIRST);
  }
  
  protected void cancelFilter() {
    clearFilter();
    applyFilter();
  }

  protected void clearFilter() {
    cbdbSource.setSelectedIndex(-1);
  }

  protected void editRecord(int act, Long recId) {
    XmlTableEdit xmlEdit = new XmlTableEdit(editManager);
    // boolean resInit = false;
    boolean resInit = xmlEdit.initForm(act, recId);
    if (resInit) {
      xmlEdit.showForm();
      int resForm = xmlEdit.getResultForm();
      if (resForm == RES_OK) {
        pnlTable.refreshTable(pnlTable.NAV_CURRENT);
      }
    }
    xmlEdit = null;
  }
}
