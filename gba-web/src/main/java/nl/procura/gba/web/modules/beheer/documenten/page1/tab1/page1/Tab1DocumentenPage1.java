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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page1;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;

import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.dialogs.DeleteRecordsFromTable;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.documenten.DocumentenTabPage;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.Tab1DocumentenPage2;
import nl.procura.gba.web.modules.beheer.overig.DirectoryLayout;
import nl.procura.gba.web.modules.beheer.overig.NavigationSorter;
import nl.procura.gba.web.modules.beheer.overig.NavigationSorter.Dir;
import nl.procura.gba.web.modules.beheer.overig.NavigationSorter.NavFile;
import nl.procura.gba.web.modules.beheer.overig.TabelToonType;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.gba.web.services.zaken.documenten.BestandType;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.vaadin.component.layout.page.PageNavigation;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.functies.VaadinUtils;

public class Tab1DocumentenPage1 extends DocumentenTabPage {

  private String          path  = "";
  private GbaTable        table = null;
  private DirectoryLayout directoryLayout;

  public Tab1DocumentenPage1() {

    super("Overzicht van de documenten");
    setMargin(true);
    addbuttons();
  }

  public Tab1DocumentenPage1(String path) {

    this();
    this.path = path;
  }

  @Override
  public void event(PageEvent event) {

    Opties opties = VaadinUtils.addOrReplaceComponent(getButtonLayout(),
        new Opties()); // de opties knop moet ververst worden
    getButtonLayout().setComponentAlignment(opties, Alignment.MIDDLE_LEFT); // bekende bug van PopupButtons

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
        Tab1DocumentenPage1.this.deleteDir(dir);

      }

      @Override
      public void deleteRecord(Record r) {
        Tab1DocumentenPage1.this.deleteNonDir(r);
      }

      @Override
      public int getTotalNumberOfSelectedRecords() {
        return getAllSelectedDocs().size();
      }
    };
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Tab1DocumentenPage2(new DocumentRecord()));
  }

  protected int totalNumberOfSelectedRecords() {
    return getAllSelectedDocs().size();
  }

  private void addbuttons() {
    addButton(buttonNew, buttonDel);
  }

  private void addDocsInNavFilesToList(List<NavFile> navFiles, List<DocumentRecord> docsInDirs) {
    for (NavFile navFile : navFiles) {
      if (navFile.getObj() instanceof DocumentRecord) {
        DocumentRecord doc = (DocumentRecord) (navFile.getObj());
        docsInDirs.add(doc);
      }
    }
  }

  private List<DocumentRecord> addListsTogether(List<DocumentRecord> docList1, List<DocumentRecord> docList2) {

    List<DocumentRecord> allDocs = new ArrayList<>(docList1);
    allDocs.addAll(docList2);

    return allDocs;
  }

  private void deleteDir(Dir dir) {

    NavigationSorter sorter = new NavigationSorter();
    List<DocumentRecord> docList = getServices().getDocumentService().getDocumenten(false);
    String dirPath = dir.getPath();

    for (DocumentRecord doc : docList) {
      sorter.add(doc.getPad(), doc);
    }

    for (Dir subDir : sorter.getDirs(dirPath, null)) {
      deleteDir(subDir);
    }

    for (NavFile<DocumentRecord> file : sorter.getFiles(dirPath)) {
      getServices().getDocumentService().delete(file.getObj());
    }
  }

  private void deleteNonDir(Record r) {

    if (r.getObject() instanceof DocumentRecord) {
      getServices().getDocumentService().delete((DocumentRecord) r.getObject());
    }
  }

  private List<DocumentRecord> getAllSelectedDocs() {

    List<DocumentRecord> docList = getSelectedDocs();
    List<DocumentRecord> docsInDirs = getDocsInDirs();

    return addListsTogether(docList, docsInDirs);
  }

  private List<DocumentRecord> getDocsInDirs() {

    List<Dir> dirList = getSelectedDirs();

    NavigationSorter nav = new NavigationSorter();
    List<DocumentRecord> allDocs = getServices().getDocumentService().getDocumenten(false);
    List<DocumentRecord> docsInDirs;
    docsInDirs = new ArrayList<>();

    for (DocumentRecord doc : allDocs) {
      nav.add(doc.getPad(), doc);
    }

    for (Dir dir : dirList) {
      List<NavFile> navFiles = nav.getAllFiles(dir.getPath());
      addDocsInNavFilesToList(navFiles, docsInDirs);
    }

    return docsInDirs;
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

  private List<DocumentRecord> getSelectedDocs() {
    List<DocumentRecord> selectedDocs = new ArrayList<>();
    for (Record r : table.getSelectedRecords()) {
      if (r.getObject() instanceof DocumentRecord) {
        DocumentRecord doc = (DocumentRecord) r.getObject();
        selectedDocs.add(doc);
      }
    }
    return selectedDocs;
  }

  private boolean isParentMapOnlySelectedRec(List<DocumentRecord> allSelectedDocs) {

    boolean onlyParentMap = false;

    if (allSelectedDocs.isEmpty()) {
      onlyParentMap = true;
    }

    return onlyParentMap;
  }

  private void setDirectoryLayout() {

    directoryLayout = new DirectoryLayout(table) {

      @Override
      protected void changeValue(GeldigheidStatus status) {
        table.init();
      }

      @Override
      protected void changeValue(TabelToonType toonType) {
        updatePath("");
        table.init();
      }
    };

    updatePath(path);
  }

  private void setTable() {

    table = new GbaTable() {

      @Override
      public void onDoubleClick(Record record) {

        if (record.getObject() instanceof Dir) {
          Dir tableDir = (Dir) record.getObject();
          updatePath(tableDir.getPath());
          table.init();
        }

        if (record.getObject() instanceof DocumentRecord) {
          DocumentRecord doc = (DocumentRecord) record.getObject();
          getNavigation().goToPage(new Tab1DocumentenPage2(doc));
        }
      }

      @Override
      public void setColumns() {

        setSelectable(true);
        setMultiSelect(true);

        addColumn("", 20).setClassType(Embedded.class);
        addColumn("Vnr", 40);
        addColumn("Naam").setUseHTML(true);
        addColumn("Code", 40);
        addColumn("Sjabloon", 300).setUseHTML(true);
        addColumn("Type", 170);
        addColumn("Vertrouwelijkheid", 130);
      }

      @Override
      public void setRecords() {
        List<DocumentRecord> docList = getServices().getDocumentService().getDocumenten(
            directoryLayout.getRecordStatus(), true);

        Collections.sort(docList);

        TabelToonType toonLijst = (TabelToonType) directoryLayout.getMapListField().getValue();

        if (toonLijst.equals(TabelToonType.MAPPEN)) { // laat de mapjes zien
          toonMappen(docList);
        } else {
          toonDocumenten(docList);
        }
      }
    };
  }

  private boolean sjabloonBestaat(String sjabloon) {
    return new File(getServices().getDocumentService().getSjablonenMap(), sjabloon).exists();
  }

  private void toonDocumenten(List<DocumentRecord> docList) {

    for (DocumentRecord doc : docList) {

      String sjabloon = doc.getBestand();
      Record r = table.addRecord(doc);
      String gv = GeldigheidStatus.BEEINDIGD.equals(doc.getGeldigheidStatus()) ? ", beëindigd" : "";

      String error = trim(gv);
      String errors = fil(error) ? setClass("red", " (" + error.trim() + ")") : "";

      r.addValue("");
      r.addValue((along(doc.getVDocument()) > 0) ? doc.getVDocument() : "-");
      r.addValue(doc.getDocument() + errors);
      r.addValue(doc.getCDocument());
      r.addValue(sjabloonBestaat(sjabloon) ? sjabloon
          : setClass("red",
              sjabloon + " (Dit sjabloon kan niet gevonden worden)"));
      r.addValue(doc.getDocumentType());
      r.addValue(doc.getVertrouwelijkheid().getOmschrijving());
    }
  }

  private void toonMappen(List<DocumentRecord> docList) {

    NavigationSorter sorter = new NavigationSorter();
    List<DocumentRecord> docInMapList = new ArrayList<>();
    for (DocumentRecord doc : docList) {
      sorter.add(doc.getPad(), doc);
    }

    Dir parentDir = sorter.getParentDir(path);

    if (fil(parentDir.getName())) {
      Record r = table.addRecord(parentDir);

      r.addValue(TableImage.getByBestandType(BestandType.VORIGE_MAP));
      r.addValue("");
      r.addValue("<b>" + parentDir.getName() + " (" + sorter.getFileCount(parentDir.getPath()) + ")" + "</b>");
      r.addValue("");
      r.addValue("");
      r.addValue("");
      r.addValue("");
    }

    for (Dir dir : sorter.getDirs(path, null)) {
      Record r = table.addRecord(dir);

      r.addValue(TableImage.getByBestandType(BestandType.MAP));
      r.addValue("");
      r.addValue("<b>" + dir.getName() + " (" + sorter.getFileCount(dir.getPath()) + ")" + "</b>");
      r.addValue("");
      r.addValue("");
      r.addValue("");
      r.addValue("");
    }

    for (NavFile<?> file : sorter.getFiles(path)) {
      DocumentRecord doc = (DocumentRecord) file.getObj();
      docInMapList.add(doc);
    }
    toonDocumenten(docInMapList);
  }

  private void updatePath(String path) {

    this.path = path;
    directoryLayout.setPath(path);
  }

  public class Opties extends Tab1DocumentenPopup {

    public Opties() {
      super(table, TabelToonType.MAPPEN);
    }

    @Override
    protected List<DocumentRecord> getAllSelectedDocs() {
      return Tab1DocumentenPage1.this.getAllSelectedDocs();
    }

    @Override
    protected PageNavigation getNavigation() {
      return Tab1DocumentenPage1.this.getNavigation();
    }

    @Override
    protected boolean isParentMapOnlySelectedRec(List<DocumentRecord> allSelectedDocs) {
      return Tab1DocumentenPage1.this.isParentMapOnlySelectedRec(allSelectedDocs);
    }
  }
}
