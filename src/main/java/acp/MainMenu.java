package acp;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import acp.db.connect.IDbConnect;
import acp.db.connect.forms.Logon;
import acp.forms.*;
import acp.utils.*;

public class MainMenu implements ActionListener {

  private IDbConnect dbConnect = Main.dbConnect;

  private static final Locale localeUS = new Locale("en", "US");
  private static final Locale localeRU = new Locale("ru", "RU");

  JMenuBar menuBar = new JMenuBar();
  ArrayList<String> arrTitles = new ArrayList<>();
  ArrayList<JMenuItem> arrItems = new ArrayList<>();

  protected JMenuBar createMenuBar() {
    String menuTitle = null;
    JMenu menu = null;
    JMenuItem menuItem = null;
    // -----------------------------------------
    // 1. Пункт меню Файл
    // -----------------------------------------

    menuTitle = new String("Menu.File");
    menu = new JMenu(Messages.getString(menuTitle));
    menuBar.add(menu);
    arrTitles.add(menuTitle);
    arrItems.add(menu);

    menuTitle = new String("Menu.File.Connect");
    menuItem = new JMenuItem(Messages.getString(menuTitle));
    menuItem.setActionCommand("connect");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    arrTitles.add(menuTitle);
    arrItems.add(menuItem);

    menuTitle = new String("Menu.File.Disconnect");
    menuItem = new JMenuItem(Messages.getString(menuTitle));
    menuItem.setActionCommand("disconnect");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    arrTitles.add(menuTitle);
    arrItems.add(menuItem);

    menu.add(new JSeparator());

    menuTitle = new String("Menu.File.Exit");
    menuItem = new JMenuItem(Messages.getString(menuTitle));
    menuItem.setActionCommand("quit");
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
        InputEvent.CTRL_MASK));
    menuItem.addActionListener(this);
    menu.add(menuItem);
    arrTitles.add(menuTitle);
    arrItems.add(menuItem);

    // -----------------------------------------
    // 2. Пункт меню Протоколы
    // -----------------------------------------
    menuTitle = new String("Menu.Load");
    menu = new JMenu(Messages.getString(menuTitle));
    menuBar.add(menu);
    arrTitles.add(menuTitle);
    arrItems.add(menu);

    // Пункт меню Протоколы -> Загруженные файлы
    menuTitle = new String("Menu.Load.LogUpload");
    menuItem = new JMenuItem(Messages.getString(menuTitle));
    menuItem.setActionCommand("upload");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    arrTitles.add(menuTitle);
    arrItems.add(menuItem);

    // Пункт меню Протоколы -> Другие протоколы
    menuTitle = new String("Menu.Load.LogOther");
    menuItem = new JMenuItem(Messages.getString(menuTitle));
    menuItem.setActionCommand("otherlogs");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    arrTitles.add(menuTitle);
    arrItems.add(menuItem);

    // -----------------------------------------
    // 3. Пункт меню Справочники
    // -----------------------------------------
    menuTitle = new String("Menu.Refs");
    menu = new JMenu(Messages.getString(menuTitle));
    menuBar.add(menu);
    arrTitles.add(menuTitle);
    arrItems.add(menu);

    // Пункт меню Справочники -> Константы
    menuTitle = new String("Menu.Refs.Consts");
    menuItem = new JMenuItem(Messages.getString(menuTitle));
    menuItem.setActionCommand("const");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    arrTitles.add(menuTitle);
    arrItems.add(menuItem);

    // Пункт меню Справочники -> Переменные
    menuTitle = new String("Menu.Refs.Variables");
    menuItem = new JMenuItem(Messages.getString(menuTitle));
    menuItem.setActionCommand("vars");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    arrTitles.add(menuTitle);
    arrItems.add(menuItem);

    menu.add(new JSeparator());

    // Пункт меню Справочники -> Источники
    menuTitle = new String("Menu.Refs.Sources");
    menuItem = new JMenuItem(Messages.getString(menuTitle));
    menuItem.setActionCommand("src");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    arrTitles.add(menuTitle);
    arrItems.add(menuItem);

    // Пункт меню Справочники -> Конфигурации источников
    menuTitle = new String("Menu.Refs.Configs");
    menuItem = new JMenuItem(Messages.getString(menuTitle));
    menuItem.setActionCommand("cfg");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    arrTitles.add(menuTitle);
    arrItems.add(menuItem);

    menu.add(new JSeparator());

    // Пункт меню Справочники -> Редактирование таблиц
    menuTitle = new String("Menu.Refs.Tables");
    JMenu submenu = new JMenu(Messages.getString(menuTitle));
    menu.add(submenu);
    arrTitles.add(menuTitle);
    arrItems.add(submenu);

    // Пункт меню Справочники -> Редактирование таблиц -> Местные номера
    menuTitle = new String("Menu.Refs.Tables.LocalNmbs");
    menuItem = new JMenuItem(Messages.getString(menuTitle));
    menuItem.setActionCommand("table_lclnmbs");
    menuItem.addActionListener(this);
    submenu.add(menuItem);
    arrTitles.add(menuTitle);
    arrItems.add(menuItem);

    // Пункт меню Справочники -> Редактирование таблиц -> SID-pref
    menuTitle = new String("Menu.Refs.Tables.SIDPref");
    menuItem = new JMenuItem(Messages.getString(menuTitle));
    menuItem.setActionCommand("table_sidpref");
    menuItem.addActionListener(this);
    submenu.add(menuItem);
    arrTitles.add(menuTitle);
    arrItems.add(menuItem);

    // Пункт меню Справочники -> Редактирование таблиц -> trace-SOP
    menuTitle = new String("Menu.Refs.Tables.TraceSOP");
    menuItem = new JMenuItem(Messages.getString(menuTitle));
    menuItem.setActionCommand("table_tracesop");
    menuItem.addActionListener(this);
    submenu.add(menuItem);
    arrTitles.add(menuTitle);
    arrItems.add(menuItem);

    // Пункт меню Справочники -> Редактирование таблиц -> Input
    menuTitle = new String("Menu.Refs.Tables.Input");
    menuItem = new JMenuItem(Messages.getString(menuTitle));
    menuItem.setActionCommand("table_input");
    menuItem.addActionListener(this);
    submenu.add(menuItem);
    menu.add(submenu);
    arrTitles.add(menuTitle);
    arrItems.add(menuItem);

    // Пункт меню Справочники -> Редактирование таблиц -> Output
    menuTitle = new String("Menu.Refs.Tables.Output");
    menuItem = new JMenuItem(Messages.getString(menuTitle));
    menuItem.setActionCommand("table_output");
    menuItem.addActionListener(this);
    submenu.add(menuItem);
    menu.add(submenu);
    arrTitles.add(menuTitle);
    arrItems.add(menuItem);

    // -----------------------------------------
    // 4. Пункт меню Настройки
    // -----------------------------------------
    menuTitle = new String("Menu.Options");
    menu = new JMenu(Messages.getString(menuTitle));
    menuBar.add(menu);
    arrTitles.add(menuTitle);
    arrItems.add(menu);

    // Пункт меню Настройки -> Язык
    menuTitle = new String("Menu.Options.Language");
    JMenu submenuLang = new JMenu(Messages.getString(menuTitle));
    menu.add(submenuLang);
    arrTitles.add(menuTitle);
    arrItems.add(submenuLang);

    ButtonGroup group = new ButtonGroup();

    menuTitle = new String("Menu.Options.Language.English");
    JRadioButtonMenuItem rbEnglish = new JRadioButtonMenuItem(
        Messages.getString(menuTitle));
    rbEnglish.setActionCommand("locale_en");
    rbEnglish.addActionListener(this);
    group.add(rbEnglish);
    submenuLang.add(rbEnglish);
    arrTitles.add(menuTitle);
    arrItems.add(rbEnglish);

    menuTitle = new String("Menu.Options.Language.Russian");
    JRadioButtonMenuItem rbRussian = new JRadioButtonMenuItem(
        Messages.getString(menuTitle));
    rbRussian.setActionCommand("locale_ru");
    rbRussian.addActionListener(this);
    group.add(rbRussian);
    submenuLang.add(rbRussian);
    arrTitles.add(menuTitle);
    arrItems.add(rbRussian);

    Locale locDefault = Locale.getDefault();
    if (locDefault.equals(localeUS)) {
      rbEnglish.setSelected(true);
    } else if (locDefault.equals(localeRU)) {
      rbRussian.setSelected(true);
    }

    // -----------------------------------------
    // 5. Пункт меню Помощь
    // -----------------------------------------
    // menuBar.add(Box.createHorizontalGlue()); // Замена setHelpMenu(menu)
    menuTitle = new String("Menu.Help");
    menu = new JMenu(Messages.getString(menuTitle));
    // menuBar.setHelpMenu(menu); // Does not work!!!!
    menuBar.add(menu); // Обычный способ добавления item
    arrTitles.add(menuTitle);
    arrItems.add(menu);

    // Пункт меню Помощь -> О программе
    menuTitle = new String("Menu.Help.About");
    menuItem = new JMenuItem(Messages.getString(menuTitle));
    menuItem.setActionCommand("about");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    arrTitles.add(menuTitle);
    arrItems.add(menuItem);

    return menuBar;
  }

  private void refreshMenu() {
    for (int i = 0; i < arrItems.size(); i++) {
      JMenuItem menuItm = arrItems.get(i);
      menuItm.setText(Messages.getString(arrTitles.get(i)));
    }
  }

  private void setLocale(Locale loc) {
    Locale.setDefault(loc);
    // ---------------------
    Messages.reloadBundle();
    FieldConfig.reloadBundle();
    FieldValid.reloadBundle();
    // ---------------------
    Main.setTitle();
    refreshMenu();
  }

  private boolean testConnection() {
    if (!dbConnect.testConnect()) {
      DialogUtils.errorMsg(Messages.getString("Message.ConnectNo"));
      return false;
    }
    return true;
  }
  
  public void actionPerformed(ActionEvent e) {
    String actionComm = e.getActionCommand();

    if (actionComm.equals("connect")) {
      Logon logon = new Logon();
      boolean resInit = logon.initForm();
      if (resInit) {
        logon.showForm();
      }  
      logon = null;

    } else if (actionComm.equals("disconnect")) {
      if (testConnection()) {
        if (DialogUtils.confirmDialog(Messages.getString("Message.Disconnect"),
            Messages.getString("Title.Disconnect"), 1) == 0) {
          // ------------------------
          dbConnect.disconnect();
          // ------------------------
        }
      }

    } else if (actionComm.equals("quit")) {
      dbConnect.disconnect();
      System.exit(0);

    } else if (actionComm.equals("upload")) {
      if (testConnection()) {
        FileLoadList fileList = new FileLoadList();
        fileList.initForm();
        fileList.showForm();
        fileList = null;
      }
    } else if (actionComm.equals("otherlogs")) {
      if (testConnection()) {
        FileOtherList otherLogs = new FileOtherList(0L);
        otherLogs.initForm();
        otherLogs.showForm();
        otherLogs = null;
      }
    } else if (actionComm.equals("const")) {
      if (testConnection()) {
        ConstList constList = new ConstList();
        constList.initForm();
        constList.showForm();
        constList = null;
      }
    } else if (actionComm.equals("vars")) {
      if (testConnection()) {
        VarList varList = new VarList();
        varList.initForm();
        varList.showForm();
        varList = null;
      }
    } else if (actionComm.equals("src")) {
      if (testConnection()) {
        SourceList srcList = new SourceList();
        srcList.initForm();
        srcList.showForm();
        srcList = null;
      }
    } else if (e.getActionCommand().equals("cfg")) {
      if (testConnection()) {
        ConfigList cfgList = new ConfigList();
        cfgList.initForm();
        cfgList.showForm();
        cfgList = null;
      }
    } else if (e.getActionCommand().equals("table_lclnmbs")) {
      if (testConnection()) {
        String path = "/config/ats/fmt/local_nmbs/field";
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.add("min");
        attrs.add("max");
        
        XmlTableList tblList = new XmlTableList("local_nmbs", path, attrs);
        tblList.initForm();
        tblList.showForm();
        tblList = null;
      }
    } else if (e.getActionCommand().equals("table_sidpref")) {
      if (testConnection()) {
        String path = "/config/ats/fmt/sid_pref/field";
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.add("key");
        attrs.add("value");
        
        XmlTableList tblList = new XmlTableList("sid_pref", path, attrs);
        tblList.initForm();
        tblList.showForm();
        tblList = null;
      }
    } else if (e.getActionCommand().equals("table_tracesop")) {
      if (testConnection()) {
        String path = "/config/ats/fmt/trace_sop/field";
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.add("key");
        attrs.add("value");
        
        XmlTableList tblList = new XmlTableList("trace_sop", path, attrs);
        tblList.initForm();
        tblList.showForm();
        tblList = null;
      }
    } else if (e.getActionCommand().equals("table_input")) {
      if (testConnection()) {
        String path = "/config/ats/input";
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.add("dir");
        
        XmlTableList tblList = new XmlTableList("input", path, attrs);
        tblList.initForm();
        tblList.showForm();
        tblList = null;
      }
    } else if (e.getActionCommand().equals("table_output")) {
      if (testConnection()) {
        String path = "/config/ats/output";
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.add("mode");
        attrs.add("file");
        attrs.add("dir");
        
        XmlTableList tblList = new XmlTableList("output", path, attrs);
        tblList.initForm();
        tblList.showForm();
        tblList = null;
      }

    } else if (e.getActionCommand().equals("locale_en")) {
      setLocale(localeUS);

    } else if (e.getActionCommand().equals("locale_ru")) {
      setLocale(localeRU);

    } else if (e.getActionCommand().equals("about")) {
      if (testConnection()) {
        About myAbout = new About();
        boolean resInit = myAbout.initForm();
        if (resInit) {
          myAbout.showForm();
        }  
        myAbout = null;
      }
    }

  }

}
