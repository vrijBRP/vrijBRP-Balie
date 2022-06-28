/*
 * Copyright 2021 - 2022 Procura B.V.
 *
 * In licentie gegeven krachtens de EUPL, versie 1.2
 * U mag dit werk niet gebruiken, behalve onder de voorwaarden van de licentie.
 * U kunt een kopie van de licentie vinden op:
 *
 *   https://github.com/vrijBRP/vrijBRP/blob/master/LICENSE.md
 *
 * Deze bevat zowel de Nederlandse als de Engelse tekst
 *
 * Tenzij dit op grond van toepasselijk recht vereist is of schriftelijk
 * is overeengekomen, wordt software krachtens deze licentie verspreid
 * "zoals deze is", ZONDER ENIGE GARANTIES OF VOORWAARDEN, noch expliciet
 * noch impliciet.
 * Zie de licentie voor de specifieke bepalingen voor toestemmingen en
 * beperkingen op grond van de licentie.
 */

package nl.procura.gba.web.modules.beheer.gebruikers.page1;

import static java.util.Arrays.asList;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.trim;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Embedded;

import nl.procura.gba.web.common.misc.spreadsheets.GebruikerSpreadsheetStandaard;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.dialogs.DeleteRecordsFromTable;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.gebruikers.page2.Page2Gebruikers;
import nl.procura.gba.web.modules.beheer.overig.DirectoryLayout;
import nl.procura.gba.web.modules.beheer.overig.NavigationSorter;
import nl.procura.gba.web.modules.beheer.overig.NavigationSorter.Dir;
import nl.procura.gba.web.modules.beheer.overig.NavigationSorter.NavFile;
import nl.procura.gba.web.modules.beheer.overig.TabelToonType;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.gba.web.services.zaken.documenten.BestandType;
import nl.procura.gba.web.services.zaken.documenten.UitvoerformaatType;
import nl.procura.vaadin.component.layout.page.PageNavigation;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page1Gebruikers extends NormalPageTemplate {

  private String          tablePath       = "";
  private GbaTable        table           = null;
  private DirectoryLayout directoryLayout = null;

  public Page1Gebruikers() {
    super("Overzicht van gebruikers");
    addButtons();
  }

  public Page1Gebruikers(String path) {
    this();
    this.tablePath = path;
  }

  @Override
  public void event(PageEvent event) {

    VaadinUtils.addOrReplaceComponent(getButtonLayout(), new Opties());

    if (event.isEvent(InitPage.class)) {

      setTable();
      setDirectoryLayout();

      addComponent(directoryLayout);
      addExpandComponent(table);
    } else if (event.isEvent(AfterReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void onDelete() {

    new DeleteRecordsFromTable(table) {

      @Override
      public void deleteRecord(Dir dir) {
        Page1Gebruikers.this.deleteRecord(dir);
      }

      @Override
      public void deleteRecord(Record r) {
        Page1Gebruikers.this.deleteRecord(r);
      }

      @Override
      public int getTotalNumberOfSelectedRecords() {
        return getAllSelectedUsers().size();
      }
    };
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Page2Gebruikers(new Gebruiker()));
  }

  private void addButtons() {
    addButton(buttonNew, buttonDel);
  }

  private List<Gebruiker> addListsTogether(List<Gebruiker> userList1, List<Gebruiker> userList2) {

    List<Gebruiker> allUsers = new ArrayList<>(userList1);
    allUsers.addAll(userList2);

    return allUsers;
  }

  private void addUsersInNavFilesToList(List<NavFile> navFiles, List<Gebruiker> usersInDirs) {

    for (NavFile navFile : navFiles) {
      if (navFile.getObj() instanceof Gebruiker) {
        Gebruiker user = (Gebruiker) (navFile.getObj());
        usersInDirs.add(user);
      }
    }
  }

  private void deleteRecord(Dir dir) {

    NavigationSorter sorter = new NavigationSorter();
    List<Gebruiker> usrList = getServices().getGebruikerService().getGebruikers(false);
    String dirPath = dir.getPath();

    for (Gebruiker gebruiker : usrList) {
      sorter.add(gebruiker.getPad(), gebruiker);
    }

    for (Dir subDir : sorter.getDirs(dirPath, null)) {
      deleteRecord(subDir);
    }

    for (NavFile<Gebruiker> file : sorter.getFiles(dirPath)) {
      getServices().getGebruikerService().delete(file.getObj());
    }
  }

  private void deleteRecord(Record r) {
    if (r.getObject() instanceof Gebruiker) {
      getServices().getGebruikerService().delete((Gebruiker) r.getObject());
    }
  }

  private List<Gebruiker> getAllSelectedUsers() {

    List<Gebruiker> selectedUsers = getSelectedUsers();
    List<Gebruiker> usersInDirs = getUsersInDirs();

    return addListsTogether(selectedUsers, usersInDirs);
  }

  private List<Dir> getSelectedDirs() {
    List<Dir> dirList = new ArrayList<>();

    for (Record r : table.getSelectedRecords()) {

      if (r.getObject() instanceof Dir) {
        Dir dir = (Dir) r.getObject();

        if (!dir.isParentDir()) {
          dirList.add(dir);
        }
      }
    }
    return dirList;
  }

  private List<Gebruiker> getSelectedUsers() {

    List<Gebruiker> gebrList = new ArrayList<>();

    for (Record r : table.getSelectedRecords()) {

      if (r.getObject() instanceof Gebruiker) {
        Gebruiker gebruiker = (Gebruiker) r.getObject();
        gebrList.add(gebruiker);
      }
    }
    return gebrList;
  }

  private List<Gebruiker> getUsersInDirs() {

    List<Dir> dirList = getSelectedDirs();

    NavigationSorter nav = new NavigationSorter();
    List<Gebruiker> allUsers = getServices().getGebruikerService().getGebruikers(directoryLayout.getRecordStatus(),
        false);
    List<Gebruiker> usersInDirs = new ArrayList<>();

    for (Gebruiker gebruiker : allUsers) {
      nav.add(gebruiker.getPad(), gebruiker);
    }

    for (Dir dir : dirList) {
      List<NavFile> navFiles = nav.getAllFiles(dir.getPath());
      addUsersInNavFilesToList(navFiles, usersInDirs);
    }

    return usersInDirs;

  }

  private boolean isParentMapOnlySelectedRecord(List<Gebruiker> allSelectedUsers) {

    boolean onlyParentMap = allSelectedUsers.isEmpty();

    return onlyParentMap;
  }

  private void setDirectoryLayout() {

    GebruikerSpreadSheet spreadsheetXls = new GebruikerSpreadSheet(UitvoerformaatType.CSV_SEMICOLON);

    directoryLayout = new DirectoryLayout(table, asList(spreadsheetXls)) {

      @Override
      protected void changeValue(GeldigheidStatus status) {
        table.init();
      }

      @Override
      protected void changeValue(TabelToonType toonLijstValue) {
        updatePath("");
        table.init();
      }
    };

    updatePath(tablePath);
  }

  private void setTable() {

    table = new GbaTable() {

      @Override
      public void onDoubleClick(Record record) {

        Object tableObj = record.getObject();

        if (tableObj instanceof Gebruiker) {

          Gebruiker g = (Gebruiker) tableObj;

          Gebruiker volGebruiker = getServices().getGebruikerService().getGebruikerByCode(g.getCUsr(), true);

          getNavigation().goToPage(new Page2Gebruikers(volGebruiker));
        } else if (tableObj instanceof Dir) {

          Dir tableDir = (Dir) tableObj;

          updatePath(tableDir.getPath());

          table.init();
        }
      }

      @Override
      public void setColumns() {

        setSizeFull();
        setSelectable(true);
        setMultiSelect(true);

        addColumn("", 20).setClassType(Embedded.class);
        addColumn("ID", 40);
        addColumn("Naam").setUseHTML(true);
        addColumn("Gebruikersnaam", 200);
        addColumn("E-mail", 200);
        addColumn("App. beheerder", 100);
      }

      @Override
      public void setRecords() {

        List<Gebruiker> gebruikers = getServices().getGebruikerService().getGebruikers(
            directoryLayout.getRecordStatus(), false);

        for (Gebruiker gebruiker : gebruikers) {
          getServices().getGebruikerService().setWachtwoordVerloop(gebruiker); // Controleren of wachtwoord kan verlopen
        }

        TabelToonType toonType = (TabelToonType) directoryLayout.getMapListField().getValue();

        if (toonType.equals(TabelToonType.MAPPEN)) { // laat de mapjes zien
          toonMappen(gebruikers);
        } else { // laat het lijstje zien
          toonGebruikers(gebruikers);
        }
      }
    };
  }

  private void toonGebruikers(List<Gebruiker> list) {
    getServices().getGebruikerService().setInformatie(list);
    for (Gebruiker gebruiker : list) {

      Record r = table.addRecord(gebruiker);
      String blok = gebruiker.isGeblokkeerd() ? "geblokkeerd" : "";
      String ww = gebruiker.isWachtwoordVerlopen() ? ", wacht. verlopen" : "";

      String gv;
      switch (gebruiker.getGeldigheidStatus()) {
        case BEEINDIGD:
          gv = ", beëindigd";
          break;

        case NOG_NIET_ACTUEEL:
          gv = ", nog niet actueel";
          break;

        default:
          gv = "";
          break;
      }

      String error = trim(blok + ww + gv);
      String errors = fil(error) ? setClass("red", " (" + error.trim() + ")") : "";

      r.addValue("");
      r.addValue(gebruiker.getCUsr());
      r.addValue(gebruiker.getNaam() + errors);
      r.addValue(gebruiker.getGebruikersnaam());
      r.addValue(gebruiker.getEmail());
      r.addValue(gebruiker.isAdministrator() ? "Ja" : "Nee");
    }
  }

  private void toonMappen(List<Gebruiker> list) {

    NavigationSorter sorter = new NavigationSorter();

    List<Gebruiker> gebrInMapList = new ArrayList<>();

    for (Gebruiker gebruiker : list) {
      sorter.add(gebruiker.getPad(), gebruiker);
    }

    Dir parentDir = sorter.getParentDir(tablePath);

    if (fil(parentDir.getName())) {
      Record r = table.addRecord(parentDir);

      r.addValue(TableImage.getByBestandType(BestandType.VORIGE_MAP));
      r.addValue("");
      r.addValue("<b>" + parentDir.getName() + " (" + sorter.getFileCount(parentDir.getPath()) + ")" + "</b>");
      r.addValue("");
      r.addValue("");
      r.addValue("");
      r.addValue("");
      r.addValue("");
    }

    for (Dir dir : sorter.getDirs(tablePath, null)) {
      Record r = table.addRecord(dir);
      r.addValue(TableImage.getByBestandType(BestandType.MAP));
      r.addValue("");
      r.addValue("<b>" + dir.getName() + " (" + sorter.getFileCount(dir.getPath()) + ")" + "</b>");
      r.addValue("");
      r.addValue("");
      r.addValue("");
      r.addValue("");
      r.addValue("");
    }

    for (NavFile<Gebruiker> file : sorter.getFiles(tablePath)) {
      gebrInMapList.add(file.getObj());
    }

    toonGebruikers(gebrInMapList);
  }

  private void updatePath(String path) {

    this.tablePath = path;
    directoryLayout.setPath(path);
  }

  public class Opties extends Page1GebruikersPopup {

    public Opties() {
      super(table, TabelToonType.MAPPEN);
    }

    @Override
    protected List<Gebruiker> getAllSelectedUsers() {
      return Page1Gebruikers.this.getAllSelectedUsers();
    }

    @Override
    protected PageNavigation getNavigation() {
      return Page1Gebruikers.this.getNavigation();
    }

    @Override
    protected boolean isParentMapOnlySelectedRecord(List<Gebruiker> allSelectedUsers) {
      return Page1Gebruikers.this.isParentMapOnlySelectedRecord(allSelectedUsers);
    }
  }

  class GebruikerSpreadSheet extends GebruikerSpreadsheetStandaard {

    public GebruikerSpreadSheet(UitvoerformaatType type) {
      super(type);
    }

    @Override
    public void compose() {

      GebruikerService db = getServices().getGebruikerService();
      setServices(getServices());
      setGebruikers(db.getGebruikers(Page1Gebruikers.this.getAllSelectedUsers(), GeldigheidStatus.ALLES, true));

      super.compose();
    }
  }
}
