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

package nl.procura.gba.web.modules.beheer.gebruikers;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.services.beheer.KoppelActie.KOPPEL;
import static nl.procura.gba.web.services.beheer.KoppelActie.ONTKOPPEL;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.Arrays;
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
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.KoppelbaarAanGebruiker;
import nl.procura.gba.web.services.beheer.profiel.KoppelbaarAanProfiel;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.gba.web.services.zaken.documenten.BestandType;

/**

 * <p>
 * 13 jul. 2011
 * <p>
 * Een generieke gebruikerstabel die gebruikt wordt bij beheer/locaties, beheer/profielen en beheer/documenten.
 * Het type van K kan dus Locatie, Profiel of Document zijn.
 */

public class KoppelGebruikerTabel<K extends KoppelbaarAanGebruiker> extends KoppelTabel<KoppelbaarAanProfiel> {

  private static final int INDEX_STATUS = 1;
  private final List<K>    koppelList;

  private DirectoryLayout  directoryLayout = null;
  private String           pad             = "";
  private NavigationSorter sorter          = null;

  private List<Gebruiker> alleGebruikers = new ArrayList<>();

  @SuppressWarnings("unchecked")
  public KoppelGebruikerTabel(K koppelObject) {
    this(Arrays.asList(koppelObject));
  }

  public KoppelGebruikerTabel(List<K> koppelList) {
    this.koppelList = koppelList;
    setDirectoryLayout();
  }

  public DirectoryLayout getDirectoryLayout() {
    return directoryLayout;
  }

  public List<Gebruiker> getUsersInDir(Dir dir) {
    List<Gebruiker> usersInDir = new ArrayList<>();
    List<NavFile> navFiles = sorter.getAllFiles(dir.getPath());
    addUsersInNavFilesToList(navFiles, usersInDir);

    return usersInDir;
  }

  @Override
  public void onDoubleClick(Record record) {

    if (isSelectable()) {

      if (record.getObject() instanceof Gebruiker) {

        Gebruiker gebr = (Gebruiker) record.getObject();
        boolean isGekoppeld = gebr.isGekoppeld(koppelList);

        if (isGekoppeld) {
          getApplication().getServices().getGebruikerService().koppelActie(koppelList, asList(gebr),
              ONTKOPPEL);
          setRecordValue(record, INDEX_STATUS, KoppelActie.get(false).getStatus());
        } else {
          getApplication().getServices().getGebruikerService().koppelActie(koppelList, asList(gebr), KOPPEL);
          setRecordValue(record, INDEX_STATUS, KoppelActie.get(true).getStatus());
        }
      } else if (record.getObject() instanceof Dir) {
        Dir tableDir = (Dir) record.getObject();
        updatePad(tableDir.getPath());
        init();
      }
    }
  }

  @Override
  public void setColumns() {

    addColumn("", 20).setClassType(Embedded.class);
    addColumn("Status", 200).setUseHTML(true);
    addColumn("Code", 40);
    addColumn("Naam", 350).setUseHTML(true);
    addColumn("Gebruiker");
    addColumn("Info");
  }

  /**
   * wordt aangeroepen bij het wisselen van 'map' naar 'lijst',
   * daarom bijhouden of functie voor het eerst aangeroepen wordt:
   * we hoeven dan de lijst van gebruikers maar 1 keer te laden.
   */

  @Override
  public void setRecords() {

    if (alleGebruikers.isEmpty()) {
      alleGebruikers = getApplication().getServices().getGebruikerService().getGebruikers(
          directoryLayout.getRecordStatus(), false);
    }

    TabelToonType toonType = (TabelToonType) directoryLayout.getMapListField().getValue();

    if (toonType.equals(TabelToonType.MAPPEN)) {
      toonMappen(alleGebruikers);
    } else {
      toonGebruikers(alleGebruikers);
    }
  }

  public void setTableStatus(KoppelActie koppelActie, List<Record> selectedRecords) {

    for (Record r : selectedRecords) {

      if (r.getObject() instanceof Dir) {

        Dir dir = (Dir) r.getObject();

        if (!dir.isParentDir()) {
          setRecordValue(r, INDEX_STATUS, koppelActie.getStatus());
        }
      } else {
        setRecordValue(r, INDEX_STATUS, koppelActie.getStatus());
      }
    }
  }

  private void addUsersInNavFilesToList(List<NavFile> navFiles, List<Gebruiker> usersInDir) {

    for (NavFile<Gebruiker> navFile : navFiles) {
      Gebruiker user = navFile.getObj();
      usersInDir.add(user);
    }
  }

  private boolean isKoppelobjectenGekoppeldAanMap(Dir dir) {

    List<Gebruiker> usersInDir = getUsersInDir(dir);

    for (Gebruiker user : usersInDir) {
      if (!user.isGekoppeld(koppelList)) {
        return false;
      }
    }

    return true;
  }

  private void setDirectoryLayout() {

    directoryLayout = new DirectoryLayout(this) {

      @Override
      protected void changeValue(GeldigheidStatus status) {
        alleGebruikers = getApplication().getServices().getGebruikerService().getGebruikers(status, false);
        init();
      }

      @Override
      protected void changeValue(TabelToonType toonType) {

        updatePad("");
        init();
      }
    };

    updatePad(pad);
  }

  private void toonGebruikers(List<Gebruiker> list) {

    for (Gebruiker gebr : list) {

      boolean isKoppelObjectenGekoppeld = gebr.isGekoppeld(koppelList);
      Record r = addRecord(gebr);

      r.addValue("");
      r.addValue(KoppelActie.get(isKoppelObjectenGekoppeld).getStatus());
      r.addValue(gebr.getCUsr());
      r.addValue(gebr.getNaam());
      r.addValue(gebr.getGebruikersnaam());
      r.addValue(gebr.getOmschrijving());
    }
  }

  private void toonMappen(List<Gebruiker> list) {

    List<Gebruiker> gebrInMapList = new ArrayList<>();

    sorter = new NavigationSorter();

    for (Gebruiker gebr : list) {
      sorter.add(gebr.getPad(), gebr);
    }

    Dir parentDir = sorter.getParentDir(pad);

    if (fil(parentDir.getName())) {

      Record record = addRecord(parentDir);
      record.addValue(TableImage.getByBestandType(BestandType.VORIGE_MAP));
      record.addValue("");
      record.addValue("");
      record.addValue(
          "<b>" + parentDir.getName() + " (" + sorter.getFileCount(parentDir.getPath()) + ")" + "</b>");
      record.addValue("");
      record.addValue("");

    }

    for (Dir dir : sorter.getDirs(pad, null)) {

      boolean isKoppelobjectenGekoppeldAanMap = isKoppelobjectenGekoppeldAanMap(dir);
      Record record = addRecord(dir);
      record.addValue(TableImage.getByBestandType(BestandType.MAP));
      record.addValue(KoppelActie.get(isKoppelobjectenGekoppeldAanMap).getStatus());
      record.addValue("");
      record.addValue("<b>" + dir.getName() + " (" + sorter.getFileCount(dir.getPath()) + ")" + "</b>");
      record.addValue("");
      record.addValue("");
    }

    for (NavFile<?> file : sorter.getFiles(pad)) {
      Gebruiker gebr = (Gebruiker) file.getObj();
      gebrInMapList.add(gebr);
    }

    toonGebruikers(gebrInMapList);
  }

  private void updatePad(String path) {

    this.pad = path;
    directoryLayout.setPath(path);
  }
}
