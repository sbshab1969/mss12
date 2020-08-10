package acp.forms;

import java.awt.*;
import java.text.SimpleDateFormat;

import javax.swing.*;

import acp.db.service.IFileLoadManagerEdit;
import acp.forms.dto.FileLoadDto;
import acp.forms.frame.FrameView;
import acp.utils.*;

public class FileLoadInfo extends FrameView {
  private static final long serialVersionUID = 1L;

  private IFileLoadManagerEdit tableManager;

  private SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
  
  private JLabel lblId = new JLabel(Messages.getString("Column.fi_id"),
      JLabel.TRAILING);
  private JLabel lblName = new JLabel(Messages.getString("Column.fi_name"),
      JLabel.TRAILING);
  private JLabel lblMd5 = new JLabel(Messages.getString("Column.fi_md5"),
      JLabel.TRAILING);
  private JLabel lblDtCreate = new JLabel(Messages.getString("Column.fi_dt_create"),
      JLabel.TRAILING);
  private JLabel lblDtWork = new JLabel(Messages.getString("Column.fi_dt_work"),
      JLabel.TRAILING);
  private JLabel lblOwner = new JLabel(Messages.getString("Column.fi_owner"),
      JLabel.TRAILING);
  private JLabel lblRecAll = new JLabel(Messages.getString("Column.fi_records_all"),
      JLabel.TRAILING);
  private JLabel lblRecErr = new JLabel(
      Messages.getString("Column.fi_records_error"), JLabel.TRAILING);
  private JLabel lblConfig = new JLabel(Messages.getString("Column.fi_config"),
      JLabel.TRAILING);

  private JLabel txtId = new JLabel();
  private JLabel txtName = new JLabel();
  private JLabel txtMd5 = new JLabel();
  private JLabel txtDtCreate = new JLabel();
  private JLabel txtDtWork = new JLabel();
  private JLabel txtOwner = new JLabel();
  private JLabel txtRecAll = new JLabel();
  private JLabel txtRecErr = new JLabel();
  private JLabel txtConfig = new JLabel();

  public FileLoadInfo(IFileLoadManagerEdit tblManager) {
    tableManager = tblManager;

    initPnlData();
    initFormNone();
//    pack();
//    setToCenter();
  }

  private void initPnlData() {
    pnlData.setLayout(new SpringLayout());

//    txtId.setPreferredSize(new Dimension(200,20));
    txtId.setText(Messages.getString("Message.NoData"));
    txtName.setText(Messages.getString("Message.NoData"));
    txtMd5.setText(Messages.getString("Message.NoData"));
    txtDtCreate.setText(Messages.getString("Message.NoData"));
    txtDtWork.setText(Messages.getString("Message.NoData"));
    txtOwner.setText(Messages.getString("Message.NoData"));
    txtRecAll.setText(Messages.getString("Message.NoData"));
    txtRecErr.setText(Messages.getString("Message.NoData"));
    txtConfig.setText(Messages.getString("Message.NoData"));

    Color cBlue = new Color(0, 0, 128);
    txtId.setForeground(cBlue);
    txtName.setForeground(cBlue);
    txtMd5.setForeground(cBlue);
    txtDtCreate.setForeground(cBlue);
    txtDtWork.setForeground(cBlue);
    txtOwner.setForeground(cBlue);
    txtRecAll.setForeground(cBlue);
    txtRecErr.setForeground(cBlue);
    txtConfig.setForeground(cBlue);

    pnlData.add(lblId);
    pnlData.add(txtId);
    pnlData.add(lblName);
    pnlData.add(txtName);
    pnlData.add(lblMd5);
    pnlData.add(txtMd5);
    pnlData.add(lblDtCreate);
    pnlData.add(txtDtCreate);
    pnlData.add(lblDtWork);
    pnlData.add(txtDtWork);
    pnlData.add(lblOwner);
    pnlData.add(txtOwner);
    pnlData.add(lblRecAll);
    pnlData.add(txtRecAll);
    pnlData.add(lblRecErr);
    pnlData.add(txtRecErr);
    pnlData.add(lblConfig);
    pnlData.add(txtConfig);
  }
  
  protected void initFormAfter() {
    SpringUtilities.makeCompactGrid(pnlData, 9, 2, 10, 10, 10, 10);
    if (act == ACT_VIEW) {
      pack();
    }  
    setToCenter();
  }

  protected boolean fillData() {
    if (act == ACT_VIEW) {
      FileLoadDto recObj = tableManager.select(recId);
      if (recObj == null) {
        return false;
      }
      // ------------------------------
      txtId.setText(String.valueOf(recId));
      txtName.setText(recObj.getName());
      txtMd5.setText(recObj.getMd5());
      txtDtCreate.setText(formatDate.format(recObj.getDateCreate()));
      txtDtWork.setText(formatDate.format(recObj.getDateWork()));
      txtOwner.setText(recObj.getOwner());
      txtConfig.setText(recObj.getConfigName());
      txtRecAll.setText(recObj.getStatList().get(0));
      txtRecErr.setText(recObj.getStatList().get(1));
      // ------------------------------
    }
    return true;
  }

}
