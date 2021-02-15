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

package nl.procura.gba.web.modules.beheer.logbestanden.page1;

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.standard.exceptions.ProExceptionType.UNKNOWN;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.vaadin.event.ShortcutAction;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.web.common.misc.FileZipper;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.dialogs.DeleteRecordsFromTable;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.logbestanden.page2.Page2Logbestanden;
import nl.procura.gba.web.modules.beheer.overig.DirectoryLayout;
import nl.procura.gba.web.modules.beheer.overig.NavigationSorter;
import nl.procura.gba.web.modules.beheer.overig.NavigationSorter.Dir;
import nl.procura.gba.web.modules.beheer.overig.NavigationSorter.NavFile;
import nl.procura.gba.web.modules.beheer.overig.TabelToonType;
import nl.procura.gba.web.services.zaken.documenten.BestandType;
import nl.procura.standard.ProcuraDate;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.functies.downloading.StreamDownloader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Page1Logbestanden extends NormalPageTemplate {

  private static final int INT_1024   = 1024;
  private final Button     buttonExp  = new Button("Exporteren (F2)");
  private final Button     buttonMail = new Button("E-mail");

  private String          tablePath = "";
  private GbaTable        table;
  private DirectoryLayout directoryLayout;

  public Page1Logbestanden() {

    super("Overzicht van de logbestanden");
    addButton(buttonExp);
    addButton(buttonMail);
    addButton(buttonDel);

    loadTable();
    setDirectoryLayout();

    addComponent(directoryLayout);
    addExpandComponent(table);
  }

  private static void deleteRecord(Dir dir) {

    File physicalDir = dir.getFile();

    try {
      if (physicalDir.isDirectory()) {
        FileUtils.deleteDirectory(physicalDir);
      }
    } catch (IOException e) {
      throw new ProException(UNKNOWN, ERROR, "Fout bij verwijderen directory.", e);
    }
  }

  private static void deleteRecord(Record r) {

    if (r.getObject() instanceof File) {

      File file = (File) r.getObject();

      if (!file.delete()) {

        throw new ProException(UNKNOWN, ERROR, "Fout bij verwijderen logbestand: " + file.getName());
      }
    }
  }

  private static String geefDatTijdWijziging(File file) {
    // file is echt een file en geen directory

    ProcuraDate date = new ProcuraDate();
    date.setDateFormat(new Date(file.lastModified()));

    return date.getFormatDate() + " / " + date.getFormatTime();
  }

  private static File getLogDir() {
    return new File(GbaConfig.getPath().getServerDir(), "logs");
  }

  private static String getSize(File dir) {

    long size;
    if (dir.isDirectory()) {
      size = FileUtils.sizeOfDirectory(dir);
    } else {
      size = dir.length();
    }

    if (size == NumberUtils.LONG_ZERO) {
      return "0 kb";
    } else if ((0 < (size / INT_1024)) && (size / INT_1024) < 1) {
      return "1 kb"; // voor het gemak
    } else {
      return (size / INT_1024) + " kb";
    }
  }

  private static NavigationSorter navigationSorterWithLogfiles() {

    NavigationSorter sorter = new NavigationSorter();
    Collection<File> allFiles = FileUtils.listFiles(getLogDir(), null, true);

    for (File file : allFiles) {
      String filePath = sorter.getPath(file, getLogDir());
      sorter.add(filePath, file);
    }

    return sorter;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if ((button == buttonExp) || (keyCode == ShortcutAction.KeyCode.F2)) {

      List<File> files = getSelectedFiles();

      try {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        new FileZipper(bos, files.toArray(new File[0]));

        StreamDownloader.download(new ByteArrayInputStream(bos.toByteArray()), getWindow(), "logs.zip", true);
      } catch (RuntimeException e) {
        throw new ProException(UNKNOWN, ERROR, "Fout bij exporteren bestanden.", e);
      }

      table.init();
    } else if (button == buttonMail) {

      try {
        String http = URLEncoder.encode("http://localhost:8081/personen/rest/v1.0/download/bestand/abc",
            "UTF-8");

        String url = "mailto:proweb@procura.nl?subject=Logbestanden Proweb personen&body=Zie bijlage&attachment=\""
            + http + "\"";

        getWindow().open(new ExternalResource(url));
      } catch (UnsupportedEncodingException e) {
        log.trace("Error", e);
      }
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onDelete() {

    new DeleteRecordsFromTable(table) {

      @Override
      public void deleteRecord(Dir dir) {
        Page1Logbestanden.deleteRecord(dir);
      }

      @Override
      public void deleteRecord(Record r) {
        Page1Logbestanden.deleteRecord(r);
      }

      @Override
      public int getTotalNumberOfSelectedRecords() {
        return totalOfSelectedFiles();
      }
    };
  }

  private List<File> getSelectedFiles() {

    List<File> list = new ArrayList<>();

    List<Object> objecten = table.getSelectedValues(Object.class);

    if (objecten.isEmpty()) {
      throw new ProException(INFO, "Geen bestanden geselecteerd");
    }

    for (Object object : objecten) {

      if (object instanceof Dir) {

        list.add(((Dir) object).getFile());
      } else {

        list.add(((File) object));
      }
    }

    return list;
  }

  private void loadTable() {

    table = new GbaTable() {

      @Override
      public void onDoubleClick(Record record) {

        if (record.getObject() instanceof Dir) {
          Dir tableDir = (Dir) record.getObject();
          updatePath(tableDir.getPath());
          table.init();
        }

        if (record.getObject() instanceof File) { // we hebben een file
          File logBestand = (File) record.getObject();
          getNavigation().goToPage(new Page2Logbestanden(logBestand));
        }
      }

      @Override
      public void setColumns() {

        setSelectable(true);
        setMultiSelect(true);

        addColumn("", 30).setClassType(Embedded.class);
        addColumn("Naam").setUseHTML(true);
        addColumn("Datum", 150);
        addColumn("Grootte", 150);
      }

      @Override
      public void setRecords() {
        toonRecords((TabelToonType) directoryLayout.getMapListField().getValue());
      }
    };
  }

  private void setDirectoryLayout() {

    directoryLayout = new DirectoryLayout(table) {

      @Override
      protected void changeValue(TabelToonType toonType) {

        updatePath("");
        table.init();
      }
    };

    updatePath(tablePath);
  }

  private void toonLogs(Collection<File> fileList) {

    for (File file : fileList) {
      Record r = table.addRecord(file);

      r.addValue(TableImage.getByBestandType(BestandType.getType(file)));
      r.addValue(file.getName());
      r.addValue(geefDatTijdWijziging(file));
      r.addValue(getSize(file));
    }
  }

  private void toonMappen(NavigationSorter sorter) {

    Dir parentDir = sorter.getParentDir(tablePath);
    List<File> logsInMap = new ArrayList<>();

    if (fil(parentDir.getName())) {

      Record r = table.addRecord(parentDir);

      r.addValue(TableImage.getByBestandType(BestandType.VORIGE_MAP));
      r.addValue("<b>" + parentDir.getName() + " (" + sorter.getFileCount(parentDir.getPath()) + ")" + "</b>");
      r.addValue("");
      r.addValue("");
    }

    for (Dir dir : sorter.getDirs(tablePath, getLogDir().getPath())) {

      Record r = table.addRecord(dir);

      r.addValue(TableImage.getByBestandType(BestandType.MAP));
      r.addValue("<b>" + dir.getName() + " (" + sorter.getFileCount(dir.getPath()) + ")" + "</b>");
      r.addValue("");
      r.addValue("");
    }

    for (NavFile<File> file : sorter.getFiles(
        tablePath)) { // file bevat nu als object een file in de huidige directorie
      logsInMap.add(file.getObj());
    }
    toonLogs(logsInMap);
  }

  private void toonRecords(TabelToonType toonType) {

    NavigationSorter sorter = navigationSorterWithLogfiles(); // de logfiles worden hier in de sorter opgeslagen.
    Collection<File> allFiles = FileUtils.listFiles(getLogDir(), null,
        true); // geeft alle files in de directorie terug
    // geeft de directories niet terug

    if (TabelToonType.MAPPEN.equals(toonType)) {
      toonMappen(sorter);
    } else if (TabelToonType.LIJST.equals(toonType)) {
      toonLogs(allFiles);
    }
  }

  private int totalOfSelectedFiles() {

    int totalSelectedFiles = 0;

    for (Record r : table.getSelectedRecords()) {

      if (r.getObject() instanceof File) {
        totalSelectedFiles++;
      } else if (r.getObject() instanceof Dir) {
        Dir dir = (Dir) r.getObject();
        File physicalDir = dir.getFile();
        int numberOfFilesInPhysicalDir = FileUtils.listFiles(physicalDir, null, true).size();

        totalSelectedFiles += numberOfFilesInPhysicalDir;
      }
    }

    return totalSelectedFiles;

  }

  private void updatePath(String path) {

    this.tablePath = path;
    directoryLayout.setPath(path);
  }
}
