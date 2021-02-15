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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab2;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionType.AUTHENTICATION;
import static nl.procura.standard.exceptions.ProExceptionType.UNKNOWN;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.dialogs.DeleteRecordsFromTable;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.documenten.DocumentenTabPage;
import nl.procura.gba.web.modules.beheer.documenten.components.DocumentImportExportHandler;
import nl.procura.gba.web.modules.beheer.overig.DirectoryLayout;
import nl.procura.gba.web.modules.beheer.overig.NavigationSorter;
import nl.procura.gba.web.modules.beheer.overig.NavigationSorter.Dir;
import nl.procura.gba.web.modules.beheer.overig.NavigationSorter.NavFile;
import nl.procura.gba.web.modules.beheer.overig.TabelToonType;
import nl.procura.gba.web.services.zaken.documenten.BestandType;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentSoort;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.standard.ProcuraDate;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Tab2DocumentenPage extends DocumentenTabPage {

  private final Button    buttonExport = new Button("Export");
  private String          tablePath    = "";
  private GbaTable        table        = null;
  private DirectoryLayout directoryLayout;

  public Tab2DocumentenPage() {

    super("Overzicht van de sjablonen");
    setMargin(true);
    addButton(buttonDel, buttonExport);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setTable();
      setDirectoryLayout();
      addComponent(directoryLayout);
      addExpandComponent(table);
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (buttonExport.equals(button)) {

      File templDir = getServices().getDocumentService().getSjablonenMap();
      List<File> templList = getTemplList(); // houd rekening met mapjes!
      GbaWindow window = (GbaWindow) getWindow();

      new DocumentImportExportHandler().exportTemplates(templDir, templList, window);
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onDelete() {

    new DeleteRecordsFromTable(table) {

      @Override
      public void deleteRecord(Dir dir) {
        Tab2DocumentenPage.this.deleteRecord(dir);
      }

      @Override
      public void deleteRecord(Record r) {
        Tab2DocumentenPage.this.deleteRecord(r);

      }

      @Override
      public int getTotalNumberOfSelectedRecords() {
        return totalOfSelectedTemplates();
      }
    };
  }

  private void deleteRecord(Dir dir) {

    File file = dir.getFile();
    try {

      if (file.isDirectory()) {
        FileUtils.deleteDirectory(file);
      }
    } catch (IOException e) {
      throw new ProException(AUTHENTICATION, ERROR, "Fout bij het verwijderen van direcory", e);
    }
  }

  private void deleteRecord(Record r) {

    if (r.getObject() instanceof File) {
      File file = (File) r.getObject();

      if (!file.delete()) {
        throw new ProException(UNKNOWN, ERROR, "Fout bij verwijderen van sjabloon.");
      }
    }
  }

  private DatumGewijzigd getDatumTijdWijziging(File file) {
    ProcuraDate date = new ProcuraDate(new Date(file.lastModified()));
    return new DatumGewijzigd(new DateTime(along(date.getSystemDate()), along(date.getSystemTime())));
  }

  private String getKoppelStatus(File file, List<DocumentSoort> list) {
    String status = setClass("red", "nee");

    // file is nu echt een file en geen directory
    for (DocumentSoort docS : list) {
      for (DocumentRecord doc : docS.getDocumenten()) {
        if (doc.getBestand().equals(file.getName())) {
          status = setClass("green", "ja");
        }
      }
    }
    return status;
  }

  private File getTemplateDir() {
    return new File(GbaConfig.getPath().getApplicationDir(), "documenten/odt_templates");
  }

  private Collection<File> getTemplatesInDir(Dir dir) {

    Collection<File> templatesInDir = new ArrayList<>();
    File physicalDir = dir.getFile();

    if (physicalDir.isDirectory()) {
      templatesInDir = FileUtils.listFiles(physicalDir, null, true);
    }

    return templatesInDir;
  }

  private List<File> getTemplList() {

    List<File> templList = new ArrayList<>();

    for (Record r : table.getSelectedRecords()) {

      if (r.getObject() instanceof File) {

        File file = (File) r.getObject();
        templList.add(file);
      } else if (r.getObject() instanceof Dir) {

        Dir dir = (Dir) r.getObject();
        Collection<File> templatesInDir = getTemplatesInDir(dir);
        templList.addAll(templatesInDir);

      }
    }
    return templList;
  }

  private void setDirectoryLayout() {

    directoryLayout = new DirectoryLayout(table) {

      @Override
      protected void changeValue(TabelToonType toonType) {

        updateTablePath("");
        table.init();
      }
    };

    updateTablePath(tablePath);
  }

  private void setTable() {

    table = new GbaTable() {

      @Override
      public void onDoubleClick(Record record) {
        if (record.getObject() instanceof Dir) {
          Dir tableDir = (Dir) record.getObject();
          updateTablePath(tableDir.getPath());
          table.init();
        }
      }

      @Override
      public void setColumns() {

        setSelectable(true);
        setMultiSelect(true);

        addColumn("", 30).setClassType(Embedded.class);
        addColumn("Naam").setUseHTML(true);
        addColumn("Gekoppeld", 150).setUseHTML(true);
        addColumn("Gewijzigd", 200);
      }

      @Override
      public void setRecords() {
        showRecords((TabelToonType) directoryLayout.getMapListField().getValue());
      }
    };
  }

  private void showRecords(TabelToonType toonType) {

    NavigationSorter sorter = new NavigationSorter();
    File templDir = getTemplateDir();
    List<File> templates = (List<File>) FileUtils.listFiles(templDir, TrueFileFilter.INSTANCE,
        TrueFileFilter.INSTANCE);
    // alle sjablonen in de sjablonendir

    for (File file : templates) {
      String filePath = sorter.getPath(file, getTemplateDir());
      sorter.add(filePath, file);
    }

    if (toonType.equals(TabelToonType.MAPPEN)) {
      toonMappen(sorter);
    } else if (toonType.equals(TabelToonType.LIJST)) {
      toonSjablonen(templates);
    }
  }

  private void toonMappen(NavigationSorter sorter) {

    Dir parentDir = sorter.getParentDir(tablePath);
    List<File> docsInMapList = new ArrayList<>();
    String pathOfMap = getTemplateDir().getAbsolutePath();

    if (fil(parentDir.getName())) {
      Record r = table.addRecord(parentDir);

      r.addValue(TableImage.getByBestandType(BestandType.VORIGE_MAP));
      r.addValue("<b>" + parentDir.getName() + " (" + sorter.getFileCount(parentDir.getPath()) + ")" + "</b>");
      r.addValue("");
      r.addValue(new DatumGewijzigd());
    }

    for (Dir dir : sorter.getDirs(tablePath, pathOfMap)) {
      Record r = table.addRecord(dir);

      r.addValue(TableImage.getByBestandType(BestandType.MAP));
      r.addValue("<b>" + dir.getName() + " (" + sorter.getFileCount(dir.getPath()) + ")" + "</b>");
      r.addValue("");
      r.addValue(new DatumGewijzigd());
    }

    for (NavFile<File> file : sorter.getFiles(tablePath)) {
      docsInMapList.add(file.getObj());
    }

    toonSjablonen(docsInMapList);
  }

  private void toonSjablonen(List<File> fileList) { // fileList bestaat nu echt uit files en niet uit directories

    List<DocumentSoort> list = getServices().getDocumentService().getAlleDocumentSoorten(false);

    for (File file : fileList) {
      Record r = table.addRecord(file);

      r.addValue(TableImage.getByBestandType(BestandType.getType(file)));
      r.addValue(file.getName());
      r.addValue(getKoppelStatus(file, list));
      r.addValue(getDatumTijdWijziging(file));
    }
  }

  private int totalOfSelectedTemplates() {

    int totalSelectedTemplates = 0;

    for (Record r : table.getSelectedRecords()) {

      if (r.getObject() instanceof File) {
        totalSelectedTemplates++;
      } else if (r.getObject() instanceof Dir) {
        Dir dir = (Dir) r.getObject();
        File physicalDir = dir.getFile();
        int numberOfFilesInPhysicalDir = FileUtils.listFiles(physicalDir, null, true).size();

        totalSelectedTemplates += numberOfFilesInPhysicalDir;
      }
    }

    return totalSelectedTemplates;
  }

  private void updateTablePath(String path) {

    this.tablePath = path;

    directoryLayout.setPath(path);
  }

  public class DatumGewijzigd implements Comparable<DatumGewijzigd> {

    private Object datum = "";

    public DatumGewijzigd() {
    }

    public DatumGewijzigd(Object datum) {
      this.datum = datum;
    }

    @Override
    public int compareTo(DatumGewijzigd object) {

      if ((this.datum instanceof DateTime) && (object.getDatum() instanceof DateTime)) {
        DateTime d1 = (DateTime) this.datum;
        DateTime d2 = (DateTime) object.getDatum();
        return d1.compareTo(d2);
      }

      return 0;
    }

    public Object getDatum() {
      return datum;
    }

    public void setDatum(Object datum) {
      this.datum = datum;
    }

    @Override
    public String toString() {
      return astr(datum);
    }
  }
}
