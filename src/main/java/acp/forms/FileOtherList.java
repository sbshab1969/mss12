package acp.forms;

import acp.db.service.IManagerList;
import acp.db.service.factory.FileOtherManagerFactory;
import acp.forms.dm.DmFileOtherList;
import acp.forms.dto.FileOtherDto;
import acp.forms.frame.FrameList;
import acp.utils.*;

public class FileOtherList extends FrameList {
  private static final long serialVersionUID = 1L;
  private Long fileId;

  private IManagerList<FileOtherDto> listManager;
  private DmFileOtherList dmForm;

  public FileOtherList(Long file_id) {
    fileId = file_id;
    if (fileId > 0L) {
      setTitle(Messages.getString("Title.AdvFileInfo"));
      setSize(1000, 500);
    } else {
      setTitle(Messages.getString("Title.OtherLogs"));
      setSize(1200, 650);
    }
    setToCenter();
    setMaximizable(true);
    setResizable(true);

    // Filter ---
    setFilterVisible(false);

    // Table ---
    listManager = FileOtherManagerFactory.getManagerList(fileId);
    dmForm = new DmFileOtherList(listManager);

    pnlTable.setModel(dmForm);
    if (fileId > 0L) {
      pnlTable.setModePage(false);
    } else {
      pnlTable.setModePage(true);
      pnlTable.setRecPerPage(30);
    }
  }

  public void initForm() {
//    listManager.prepareQuery();
    pnlTable.queryTable(pnlTable.NAV_FIRST);
    if (fileId != 0L) {
      pnlTable.selectRow(-1);
    }
  }
  
}
