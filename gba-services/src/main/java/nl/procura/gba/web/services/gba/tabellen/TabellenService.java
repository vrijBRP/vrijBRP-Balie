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

package nl.procura.gba.web.services.gba.tabellen;

import static java.util.Collections.singletonList;
import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory.FAULT;
import static nl.procura.gba.web.services.gba.templates.ZoekProfielType.PROFIEL_STANDAARD;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.diensten.gba.ple.procura.utils.diacrits.DiacList;
import nl.procura.diensten.gba.ple.procura.utils.diacrits.DiacList.Diacref;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.services.gba.ple.PersonenWsClient;
import nl.procura.gba.web.services.gba.tabellen.gemeentelijk.Buurt;
import nl.procura.gba.web.services.gba.tabellen.gemeentelijk.GemDeel;
import nl.procura.gba.web.services.gba.tabellen.gemeentelijk.GemeentelijkeTabel;
import nl.procura.gba.web.services.gba.tabellen.gemeentelijk.Locatie;
import nl.procura.gba.web.services.gba.tabellen.gemeentelijk.OpenbareRuimte;
import nl.procura.gba.web.services.gba.tabellen.gemeentelijk.Straat;
import nl.procura.gba.web.services.gba.tabellen.gemeentelijk.Subbuurt;
import nl.procura.gba.web.services.gba.tabellen.gemeentelijk.Wijk;
import nl.procura.gba.web.services.gba.tabellen.gemeentelijk.Woonplaats;
import nl.procura.gba.web.services.gba.templates.GbaTemplateService;
import nl.procura.gbaws.web.rest.v1_0.tabellen.GbaWsRestTabel;
import nl.procura.gbaws.web.rest.v1_0.tabellen.GbaWsRestTabelRecord;
import nl.procura.gbaws.web.rest.v1_0.tabellen.GbaWsRestTabellenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.tabellen.GbaWsRestTabellenVraag;
import nl.procura.standard.diacrieten.ProcuraDiacrieten;
import nl.procura.commons.core.exceptions.ProException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TabellenService extends GbaTemplateService {

  private static final List<TabelResultaat> tabellen = Collections.synchronizedList(new ArrayList<>());
  private static final Object               loadLock = new Object();

  public TabellenService() {
    super("Tabellen");
  }

  @Override
  public void init() {
    try {
      laadTabellen();
    } catch (Exception e) {
      log.error("Fout bij controleren tabellen", e);
    }
    super.init();
  }

  public void laadTabellen() {
    synchronized (loadLock) {
      if (!getServices().getPersonenWsService().isInitiated()) {
        addMessage(false, FAULT, ERROR, "De basistabellen konden niet worden geladen.",
            "De GBA-WS service is niet te benaderen.", null);
      }

      // Landelijk
      add("Nationaliteiten", GBATable.NATIO);
      add("Plaatsen", GBATable.PLAATS);
      add("Landen", GBATable.LAND);
      add("Voorvoegselsel", GBATable.VOORVOEGSEL);
      add("Redenen opnemen / beëindigen nationaliteit", GBATable.REDEN_NATIO);
      add("Titels / predikaten", GBATable.TITEL_PREDIKAAT);
      add("Akte aanduidingen", GBATable.AKTE_AANDUIDING);
      add("Redenen huwelijks ontbinding", GBATable.REDEN_HUW_ONTB);
      add("Nederlands reisdocumenten", GBATable.NED_REISDOC);
      add("Aut. verstrekking nederlandse reisd.", GBATable.AUT_VERSTREK_NED_REISD);
      add("verblijfstitels", GBATable.VERBLIJFSTITEL);
      add("Indicaties gezag minderjarige", GBATable.IND_GEZAG_MINDERJ);

      // Gemeentelijk
      add("Woonplaatsen", new Woonplaats());
      add("Straten", new Straat());
      add("Openbare ruimtes", new OpenbareRuimte());
      add("Locaties", new Locatie());
      add("Gemeentedelen", new GemDeel());
      add("Wijken", new Wijk());
      add("Buurten", new Buurt());
      add("Subbuurten", new Subbuurt());

      GbaTables.loadIfNeeded();
    }
  }

  public static TabelResultaat getTabel(GBATable tabel) {
    int index = tabellen.indexOf(new TabelResultaat(tabel.getTableCode()));
    if (index < 0) {
      return new TabelResultaat(tabel.getTableCode(), "");
    }
    return tabellen.get(index);
  }

  public synchronized void clearTabellen() {
    tabellen.clear();
    init();
    GbaTables.reload();
  }

  public List<TabelResultaat> getTabellen() {
    return tabellen;
  }

  private synchronized void add(String title, GBATable tabel) {
    if (!getTabel(tabel).isGeladen()) {
      long st = System.currentTimeMillis();
      TabelResultaat tab = zoekLandelijk(title, tabel.getTableCode());
      if (tab.isGeladen()) {
        long et = System.currentTimeMillis();
        log.info(String.format("Tabel %s geladen in %d ms. ...", title, et - st));
        tabellen.add(tab);
      }
    }
  }

  private synchronized void add(String title, GemeentelijkeTabel tabel) {
    if (!getTabel(tabel.getTabel()).isGeladen()) {
      long st = System.currentTimeMillis();
      TabelResultaat tab = zoekGemeentelijk(title, tabel);
      if (tab.isGeladen()) {
        long et = System.currentTimeMillis();
        log.info(String.format("Tabel %s geladen in %d ms. ...", title, et - st));
        tabellen.add(tab);
      }
    }
  }

  /**
   * Converts the properties to a Map
   */
  private TabelRecordAttributen getAttributeMap(String attributen) {
    TabelRecordAttributen attr = new TabelRecordAttributen();

    if (fil(attributen)) {
      Properties attrProps = new Properties();
      ByteArrayInputStream bis = null;

      try {
        bis = new ByteArrayInputStream(attributen.getBytes());
        attrProps.load(bis);
        attr = new TabelRecordAttributen(new HashMap(attrProps));
      } catch (IOException e) {
        throw new ProException("Fout bij inlezen attributen", e);
      } finally {
        IOUtils.closeQuietly(bis);
      }
    }

    return attr;
  }

  private TabelResultaat zoekGemeentelijk(String omschrijving, GemeentelijkeTabel tabel) {
    TabelResultaat result = new TabelResultaat(tabel.getTabel().getTableCode(), omschrijving);

    try {
      for (String[] row : getServices().getProbevSqlService().find(tabel.getSql())) {
        row = tabel.format(row);
        // Diacrieten erop plakken
        String rCode = tabel.getCode();

        if (fil(tabel.getDiac()) && tabel.isDiacriet()) {
          Diacref ref = DiacList.getRef(tabel.getDiac());
          if (fil(ref.getCodefield())) {
            String diacSql = String.format("select x from Diac x where x.id.cVeld = %s", rCode);
            for (String[] diacRow : getServices().getProbevSqlService().find(diacSql)) {
              if (diacRow.length == 3) {
                if (ref.getDescr().equals(diacRow[0])) {
                  tabel.setWaarde(ProcuraDiacrieten.merge(row[1], ProcuraDiacrieten.parseDiacBytes(
                      diacRow[2].getBytes())));
                }
              }
            }
          }
        }

        String rOms = tabel.getWaarde();
        long rDatumIn = tabel.getDatumIngang();
        long rDatumEnd = tabel.getDatumEinde();
        TabelRecordAttributen recordAttributen = new TabelRecordAttributen();
        result.getRecords().add(new TabelRecord(rCode, rOms, rDatumIn, rDatumEnd, recordAttributen));
      }

      result.setTijdstip(new Date().getTime());
      result.setOmschrijving(omschrijving);

    } catch (Exception e) {
      addMessage(false, FAULT, ERROR,
          MessageFormat.format("Tabel: {0} kan niet worden geladen.", omschrijving),
          getReden(e), e);
      throw new RuntimeException(e);
    }

    return result;
  }

  private TabelResultaat zoekLandelijk(String omschrijving, int tabelCode) {
    TabelResultaat result = new TabelResultaat(tabelCode, omschrijving);

    try {
      GbaWsRestTabellenVraag vraag = new GbaWsRestTabellenVraag(true, singletonList(tabelCode));
      PersonenWsClient client = getServices().getPersonenWsService().getPersonenWsClient(PROFIEL_STANDAARD);
      GbaWsRestTabellenAntwoord antwoord = client.getTabel().getTabellen(vraag);

      for (GbaWsRestTabel rTabel : antwoord.getTabellen()) {
        for (GbaWsRestTabelRecord rRecord : rTabel.getRecords()) {
          String rCode = rRecord.getCode();
          String rOms = rRecord.getOmschrijving();
          int rDatumIn = rRecord.getDatumIngang();
          int rDatumEnd = rRecord.getDatumEinde();
          String attributen = rRecord.getAttributen();
          TabelRecordAttributen recordAttributen = getAttributeMap(attributen);
          result.getRecords().add(new TabelRecord(rCode, rOms, rDatumIn, rDatumEnd, recordAttributen));
        }
      }

      result.setTijdstip(new Date().getTime());
      result.setOmschrijving(omschrijving);

    } catch (Exception e) {
      addMessage(false, FAULT, ERROR,
          MessageFormat.format("Tabel: {0} kan niet worden geladen.", omschrijving),
          getReden(e), e);
      throw new RuntimeException(e);
    }

    return result;
  }
}
