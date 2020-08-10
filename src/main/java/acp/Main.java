package acp;

import javax.swing.*;

import java.awt.event.*;

import acp.db.connect.IDbConnect;
import acp.db.connect.factory.DbConnectFactory;
import acp.db.connect.forms.Logon;
import acp.utils.*;

public class Main extends JFrame {
  private static final long serialVersionUID = 1L;
  
  private static Main mainFrame = null;
  public static JDesktopPane desktop = new JDesktopPane();
  
  // 0 - прямое подключение; 
  // 1 - форма logon;
  public static int modeLogon = 1; 
  
  // 0 - jdbc
  // 1 - Hibernate HQL
  // 2 - Hibernate SQL
  // 3 - Hibernate Criteria (sqlRestriction)
  // 4 - Hibernate Criteria2 (Restrictions)
  public static int modeWorkDb = 4;
  public static IDbConnect dbConnect = DbConnectFactory.getDbConnect();

  public Main() {
    super(Messages.getString("Title.Main"));
    setContentPane(desktop);
    setSize(1200, 700);
    setLocationRelativeTo(null); // размещение по центру экрана
    setExtendedState(MAXIMIZED_BOTH);
    // desktop.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
    // desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

    MainMenu mainMenu = new MainMenu();
    setJMenuBar(mainMenu.createMenuBar());

//    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent evt) {
        formWindowClosing(evt);
      }
    });
  }

  private void formWindowClosing(WindowEvent evt) {
//    System.out.println("formWindowClosing");
    dbConnect.disconnect();
  }

  public static void setTitle() {
    mainFrame.setTitle(Messages.getString("Title.Main"));
  }

  private static void createAndShowGUI() {
    // --- Установка L&F перед созданием формы ---
    // JFrame.setDefaultLookAndFeelDecorated(true);
    // JDialog.setDefaultLookAndFeelDecorated(true);
    // Установка Look and Feel
    //try {
    //  UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); // default
    //  // UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    //  // UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
    //  // UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
    //  // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    //} catch (Exception e) {
    //  e.printStackTrace();
    //}
    // System.out.println(UIManager.getSystemLookAndFeelClassName());

    // java.util.Locale.setDefault(java.util.Locale.US);
    mainFrame = new Main();
    
    // --- Установка L&F после создания формы ---
    // mainFrame.setUndecorated(true);
    // mainFrame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);

    // Если есть установки после создания формы, то setVisible после
    mainFrame.setVisible(true);
  }

  private static void logonDefault() {
    // ------------------------------------
    boolean res = dbConnect.connectDefault();
    // ------------------------------------
    if (res == true) {
      DialogUtils.infoDialog(Messages.getString("Message.ConnectOk")); ;
    } else {
      DialogUtils.errorMsg(Messages.getString("Message.ConnectError"));
    }
  }

  private static void logonForm() {
    Logon logon = new Logon();
    boolean resInit = logon.initForm();
    if (resInit) {
      logon.showForm();
    }  
    logon = null;
  }

  private static void logon() {
    if (modeLogon == 0) {    
      logonDefault();
    } else {  
      logonForm();
    }  
  }

  public static void main(String[] args) {
    createAndShowGUI();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        logon();
      }
    });
  }
  
}
