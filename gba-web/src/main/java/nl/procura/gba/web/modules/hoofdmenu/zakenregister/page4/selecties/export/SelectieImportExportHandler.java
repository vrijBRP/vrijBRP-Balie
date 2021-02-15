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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.selecties.export;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import nl.procura.gba.common.Serializer;
import nl.procura.gba.jpa.personen.dao.GenericDao;
import nl.procura.gba.jpa.personen.dao.SelDao;
import nl.procura.gba.jpa.personen.db.Sel;
import nl.procura.gba.jpa.personen.utils.GbaJpa;
import nl.procura.gba.web.common.misc.ImportExportHandler;
import nl.procura.gba.web.services.zaken.selectie.Selectie;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.vaadin.functies.downloading.DownloadHandlerImpl;

public class SelectieImportExportHandler extends ImportExportHandler {

  /**
   * Importeert selecties vanuit een zip-bestand
   */
  public static List<String> importSelecties(final ImportSelectieArgumenten args) {

    final List<String> meldingen = new ArrayList<>();

    readZip(args.getBestand(), (zis, entry, dir, name) -> {

      SelectieExport im = Serializer.deSerialize(zis, SelectieExport.class);

      EntityManager em = GbaJpa.getManager();

      for (SelectieExport.Sel sel : im.getList()) {

        em.getTransaction().begin();

        String selectieName = getNewName(sel, 0);

        Selectie selectie = new Selectie();
        selectie.setSelectie(selectieName);
        selectie.setOmschrijving(sel.getOmschrijving());
        selectie.setStatement(sel.getStatement());
        selectie.setId(sel.getId());

        GenericDao.saveEntity(selectie);

        meldingen.add(String.format("Geïmporteerd selectie: %s", selectie.getSelectie()));

        em.getTransaction().commit();
      }
    });

    return meldingen;
  }

  private static String getNewName(SelectieExport.Sel sel, int nr) {
    String selectieName = sel.getSelectie();
    if (nr > 0) {
      selectieName += " (" + nr + ")";
    }
    List<Sel> existing = SelDao.findByName(selectieName);
    if (existing.isEmpty()) {
      return selectieName;
    }
    return getNewName(sel, ++nr);
  }

  /**
   * Exporteert de selecties naar een zip-bestand
   */
  public void exportSelecties(GbaWindow window, List<Selectie> selecties) {

    SelectieExport export = new SelectieExport();
    selecties.forEach(export::addSelectie);

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    Serializer.serialize(bos, export);
    Map<String, byte[]> map = new HashMap<>();
    map.put("inhoud.ser", bos.toByteArray());
    exportObject(map, "selecties.zip", new DownloadHandlerImpl(window));
  }
}
