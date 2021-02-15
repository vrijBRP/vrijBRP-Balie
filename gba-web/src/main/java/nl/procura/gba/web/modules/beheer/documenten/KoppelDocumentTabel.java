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

package nl.procura.gba.web.modules.beheer.documenten;

import static java.util.Arrays.asList;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vaadin.ui.Embedded;

import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.modules.beheer.overig.DirectoryLayout;
import nl.procura.gba.web.modules.beheer.overig.KoppelTabel;
import nl.procura.gba.web.modules.beheer.overig.NavigationSorter;
import nl.procura.gba.web.modules.beheer.overig.NavigationSorter.Dir;
import nl.procura.gba.web.modules.beheer.overig.NavigationSorter.NavFile;
import nl.procura.gba.web.modules.beheer.overig.TabelToonType;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.profiel.KoppelbaarAanProfiel;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.gba.web.services.zaken.documenten.BestandType;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.KoppelbaarAanDocument;

/**

 * <p>
 * 2012
 * <p>
 * Een generieke documententabel die gebruikt wordt bij beheer/documenten onder printopties en beheer/gebruikers
 * Het type van K is dus PrintOptie of Gebruiker.
 */

public class KoppelDocumentTabel<K extends KoppelbaarAanDocument> extends KoppelTabel<KoppelbaarAanProfiel> {

  private static final int INDEX_STATUS = 1;
  private List<K>          koppelList   = new ArrayList<>();
  private DirectoryLayout  directoryLayout;

  private String               path           = "";
  private NavigationSorter     sorter         = null;
  private List<DocumentRecord> alleDocumenten = new ArrayList<>();

  public KoppelDocumentTabel(List<K> koppelList) {

    setKoppelList(koppelList);
    setDirectoryLayout();
    setAllDocs(alleDocumenten);
  }

  public List<DocumentRecord> getAllDocs() {
    return alleDocumenten;
  }

  public void setAllDocs(List<DocumentRecord> allDocs) {
    this.alleDocumenten = allDocs;
  }

  public DirectoryLayout getDirectoryLayout() {
    return directoryLayout;
  }

  public List<DocumentRecord> getDocumentenInMap(Dir dir) {

    List<DocumentRecord> docsInDir = new ArrayList<>();
    List<NavFile> navFiles = sorter.getAllFiles(dir.getPath());
    voegDocumentenToe(navFiles, docsInDir);

    return docsInDir;
  }

  public List<K> getKoppelList() {
    return koppelList;
  }

  public void setKoppelList(List<K> koppelList) {
    this.koppelList = koppelList;
  }

  @Override
  public void onDoubleClick(Record record) {

    if (isSelectable()) {

      if (record.getObject() instanceof DocumentRecord) {

        DocumentRecord doc = (DocumentRecord) record.getObject();

        boolean isGekoppeld = doc.isGekoppeld(getKoppelList());

        getApplication().getServices().getDocumentService().koppelActie(koppelList, asList(doc),
            KoppelActie.get(!isGekoppeld));

        setRecordValue(record, INDEX_STATUS, KoppelActie.get(!isGekoppeld).getStatus());
      } else if (record.getObject() instanceof Dir) {

        Dir tableDir = record.getObject(Dir.class);
        updatePath(tableDir.getPath());

        init();
      }
    }
  }

  @Override
  public void setColumns() {

    addColumn("", 20).setClassType(Embedded.class);
    addColumn("Status", 100).setUseHTML(true);
    addColumn("Vnr.", 50);
    addColumn("Naam").setUseHTML(true);
  }

  /**
   * wordt aangeroepen bij het wisselen van 'map' naar 'lijst',
   * daarom bijhouden of functie voor het eerst aangeroepen wordt:
   * we hoeven dan de lijst van gebruikers maar 1 keer te laden.
   */
  @Override
  public void setRecords() {

    if (alleDocumenten.isEmpty()) {
      alleDocumenten = getApplication().getServices().getDocumentService().getDocumenten(
          directoryLayout.getRecordStatus(), true);
    }

    Collections.sort(alleDocumenten);

    TabelToonType toonLijst = (TabelToonType) directoryLayout.getMapListField().getValue();

    if (toonLijst.equals(TabelToonType.MAPPEN)) { // laat de mapjes zien
      toonMappen();
    } else {
      toonLijst(alleDocumenten);
    }
  }

  public void setTableStatus(KoppelActie koppelActie, List<Record> selectedRecords) {

    for (Record r : selectedRecords) {

      if (r.getObject() instanceof Dir) {

        Dir dir = r.getObject(Dir.class);

        if (!dir.isParentDir()) {
          setRecordValue(r, INDEX_STATUS, koppelActie.getStatus());
        }
      } else {
        setRecordValue(r, INDEX_STATUS, koppelActie.getStatus());
      }
    }
  }

  private boolean isKoppelobjectenGekoppeldAanMap(Dir dir) {

    List<DocumentRecord> docsInDir = getDocumentenInMap(dir);

    for (DocumentRecord doc : docsInDir) {
      if (!doc.isGekoppeld(koppelList)) {
        return false;
      }
    }
    return true;
  }

  private void setDirectoryLayout() {

    directoryLayout = new DirectoryLayout(this) {

      @Override
      protected void changeValue(GeldigheidStatus status) {
        alleDocumenten = getApplication().getServices().getDocumentService().getDocumenten(status, true);
        init();
      }

      @Override
      protected void changeValue(TabelToonType toonType) {
        updatePath("");
        init();
      }
    };

    updatePath(path);
  }

  private void toonLijst(List<DocumentRecord> docList) {

    for (DocumentRecord doc : docList) {

      Record r = addRecord(doc);
      boolean isKoppelobjectenGekoppeld = doc.isGekoppeld(koppelList);

      r.addValue("");
      r.addValue(KoppelActie.get(isKoppelobjectenGekoppeld).getStatus());
      r.addValue((along(doc.getVDocument()) > 0) ? doc.getVDocument() : "-");
      r.addValue(doc.getDocument());
    }
  }

  private void toonMappen() {

    List<DocumentRecord> docInMapList = new ArrayList<>();

    sorter = new NavigationSorter();

    for (DocumentRecord doc : alleDocumenten) {
      sorter.add(doc.getPad(), doc);
    }

    Dir parentDir = sorter.getParentDir(path);

    if (fil(parentDir.getName())) {
      Record r = addRecord(parentDir);

      r.addValue(TableImage.getByBestandType(BestandType.VORIGE_MAP));
      r.addValue("");
      r.addValue("");
      r.addValue("<b>" + parentDir.getName() + " (" + sorter.getFileCount(parentDir.getPath()) + ")" + "</b>");
      r.addValue("");
    }

    for (Dir dir : sorter.getDirs(path, null)) {
      Record r = addRecord(dir);
      boolean isKoppelobjectenGekoppeldAanMap = isKoppelobjectenGekoppeldAanMap(dir);

      r.addValue(TableImage.getByBestandType(BestandType.MAP));
      r.addValue(KoppelActie.get(isKoppelobjectenGekoppeldAanMap).getStatus());
      r.addValue("");
      r.addValue("<b>" + dir.getName() + " (" + sorter.getFileCount(dir.getPath()) + ")" + "</b>");
    }

    for (NavFile<?> file : sorter.getFiles(path)) {
      DocumentRecord doc = (DocumentRecord) file.getObj();
      docInMapList.add(doc);
    }

    toonLijst(docInMapList);
  }

  private void updatePath(String path) {

    this.path = path;

    directoryLayout.setPath(path);
  }

  private void voegDocumentenToe(List<NavFile> navFiles, List<DocumentRecord> documentenInMap) {

    for (NavFile navFile : navFiles) {
      if (navFile.getObj() instanceof DocumentRecord) {
        DocumentRecord doc = (DocumentRecord) (navFile.getObj());
        documentenInMap.add(doc);
      }
    }
  }
}
