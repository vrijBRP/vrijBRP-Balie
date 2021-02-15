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

import static java.util.Arrays.asList;
import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory.FAULT;
import static nl.procura.gba.web.services.gba.templates.ZoekProfielType.PROFIEL_STANDAARD;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.text.MessageFormat;
import java.util.*;

import org.apache.commons.io.IOUtils;

import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.diensten.gba.ple.procura.utils.diacrits.DiacList;
import nl.procura.diensten.gba.ple.procura.utils.diacrits.DiacList.Diacref;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.services.gba.ple.PersonenWsClient;
import nl.procura.gba.web.services.gba.tabellen.gemeentelijk.*;
import nl.procura.gba.web.services.gba.templates.GbaTemplateService;
import nl.procura.gbaws.web.rest.v1_0.tabellen.GbaWsRestTabel;
import nl.procura.gbaws.web.rest.v1_0.tabellen.GbaWsRestTabelRecord;
import nl.procura.gbaws.web.rest.v1_0.tabellen.GbaWsRestTabellenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.tabellen.GbaWsRestTabellenVraag;
import nl.procura.standard.diacrieten.ProcuraDiacrieten;
import nl.procura.standard.exceptions.ProException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TabellenService extends GbaTemplateService {

  private static final List<TabelResultaat> tabellen = Collections.synchronizedList(new ArrayList<>());

  public TabellenService() {
    super("Tabellen");
  }

  public static synchronized TabelResultaat getTabel(GBATable tabel) {

    int index = tabellen.indexOf(new TabelResultaat(tabel.getTableCode()));

    if (index < 0) {
      return new TabelResultaat(tabel.getTableCode(), "");
    }

    return tabellen.get(index);
  }

  public synchronized void clearTabellen() {
    // Reset containers
    getTabellen().clear();
    init();
    GbaTables.reload();
  }

  public List<TabelResultaat> getTabellen() {
    return tabellen;
  }

  @Override
  public void init() {
    if (!getServices().getPersonenWsService().isInitiated()) {
      addMessage(false, FAULT, ERROR, "De basistabellen konden niet worden geladen.",
          "De GBA-WS service is niet te benaderen.", null);
    }

    // Landelijk
    add("Nationaliteiten", GBATable.NATIO);
    add("Plaatsen", GBATable.PLAATS);
    add("Woonplaatsen", GBATable.WOONPLAATS);
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
    add("Straten", new Straat());
    add("Openbare ruimtes", new OpenbareRuimte());
    add("Locaties", new Locatie());
    add("Gemeentedelen", new GemDeel());
    add("Wijken", new Wijk());
    add("Buurten", new Buurt());
    add("Subbuurten", new Subbuurt());

    try {
      GbaTables.loadIfNeeded();
    } catch (Exception e) {
      log.error("Fout bij controleren tabellen", e);
    }

    super.init();
  }

  private synchronized void add(String title, GBATable tabel) {
    if (!exists(tabel)) {
      TabelResultaat tab = zoekLandelijk(title, tabel.getTableCode());
      if (tab.getRecords().size() > 0) {
        tabellen.add(tab);
      }
    }
  }

  private synchronized void add(String title, GemeentelijkeTabel tabel) {
    if (!exists(tabel.getTabel())) {
      TabelResultaat tab = zoekGemeentelijk(title, tabel);
      if (tab.getRecords().size() > 0) {
        tabellen.add(tab);
      }
    }
  }

  private boolean exists(GBATable tabel) {
    return tabellen.contains(new TabelResultaat(tabel.getTableCode())) && (getTabel(tabel).getRecords().size() > 0);
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
    TabelResultaat tab = new TabelResultaat(tabel.getTabel().getTableCode(), omschrijving);

    try {
      for (String[] row : getServices().getProbevSqlService().find(tabel.getSql())) {
        row = tabel.format(row);
        // Diacrrieten erop plakken
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
        TabelRecordAttributen attributen = new TabelRecordAttributen();

        tab.getRecords().add(new TabelRecord(rCode, rOms, rDatumIn, rDatumEnd, attributen));
      }
    } catch (Exception e) {
      if (!(e instanceof ConnectException)) {
        e.printStackTrace();
      }

      addMessage(false, FAULT, ERROR, MessageFormat.format("Tabel: {0} kan niet worden geladen.", omschrijving),
          getReden(e), e);
    }

    return tab;
  }

  private TabelResultaat zoekLandelijk(String omschrijving, int tabelCode) {

    TabelResultaat tabel = new TabelResultaat(tabelCode, omschrijving);

    try {
      GbaWsRestTabellenVraag vraag = new GbaWsRestTabellenVraag(true, asList(tabelCode));
      PersonenWsClient client = getServices().getPersonenWsService().getPersonenWsClient(PROFIEL_STANDAARD);
      GbaWsRestTabellenAntwoord antwoord = client.getTabel().getTabellen(vraag);

      for (GbaWsRestTabel rTabel : antwoord.getTabellen()) {
        for (GbaWsRestTabelRecord rRecord : rTabel.getRecords()) {
          String rCode = rRecord.getCode();
          String rOms = rRecord.getOmschrijving();
          int rDatumIn = rRecord.getDatumIngang();
          int rDatumEnd = rRecord.getDatumEinde();
          String attributen = rRecord.getAttributen();

          tabel.getRecords()
              .add(new TabelRecord(rCode, rOms, rDatumIn, rDatumEnd, getAttributeMap(attributen)));
        }
      }
    } catch (Exception e) {
      addMessage(false, FAULT, ERROR, MessageFormat.format("Tabel: {0} kan niet worden geladen.", omschrijving),
          getReden(e), e);
    }

    tabel.setTijdstip(new Date().getTime());
    tabel.setOmschrijving(omschrijving);

    return tabel;
  }
}
