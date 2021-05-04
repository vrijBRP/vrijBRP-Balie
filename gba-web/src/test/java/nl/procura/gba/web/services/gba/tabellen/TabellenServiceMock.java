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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.gba.web.common.tables.GbaTables;

public final class TabellenServiceMock {

  private static final Logger LOGGER = LoggerFactory.getLogger(TabellenServiceMock.class);

  public static final String UNKNOWN_MUNICIPALITY_CODE = "0000";
  public static final String TEST_MUNICIPALITY_CODE    = "0001";
  public static final String TEST_MUNICIPALITY_DESC    = "Municipality One";
  public static final String UNKNOWN_COUNTRY_CODE      = "0000";
  public static final String TEST_COUNTRY_CODE         = "0001";
  public static final String TEST_COUNTRY_DESC         = "Country Test";
  public static final String NETHERLANDS_COUNTRY_CODE  = "6030";
  public static final String NETHERLANDS_COUNTRY_DESC  = "Nederland";

  public static final String UNKNOWN_DUTCH_TRAVEL_DOC_CODE = "..";
  public static final String DUTCH_TRAVEL_AUTH_BU0518_CODE = "BU0518";

  private static final String UNKNOWN_DUTCH_TRAVEL_DOC_DESC     = "Onbekend";
  private static final String UNKNOWN_MUNICIPALITY_DESC         = "Onbekend";
  private static final String UNKNOWN_COUNTRY_DESC              = "Onbekend";
  private static final String DUTCH_TRAVEL_DOC_AUTH_BU0518_DESC = "Minister van Buitenlandse Zaken ('s-Gravenhage)";

  private TabellenServiceMock() {
  }

  public static void init() {
    LOGGER.info("Initialize tables");
    List<TabelResultaat> tables = new TabellenService().getTabellen();
    TabelResultaat municipalities = new TabelResultaat(GBATable.PLAATS.getTableCode(),
        GBATable.PLAATS.getDescr());
    municipalities.getRecords()
        .add(
            new TabelRecord(UNKNOWN_MUNICIPALITY_CODE, UNKNOWN_MUNICIPALITY_DESC, -1, -1, new TabelRecordAttributen()));
    municipalities.getRecords()
        .add(new TabelRecord(TEST_MUNICIPALITY_CODE, TEST_MUNICIPALITY_DESC, -1, -1, new TabelRecordAttributen()));
    tables.add(municipalities);

    TabelResultaat countries = new TabelResultaat(GBATable.LAND.getTableCode(), GBATable.LAND.getDescr());
    countries.getRecords()
        .add(new TabelRecord(UNKNOWN_COUNTRY_CODE, UNKNOWN_COUNTRY_DESC, -1, -1, new TabelRecordAttributen()));
    countries.getRecords()
        .add(new TabelRecord(TEST_COUNTRY_CODE, TEST_COUNTRY_DESC, -1, -1, new TabelRecordAttributen()));
    countries.getRecords()
        .add(new TabelRecord(NETHERLANDS_COUNTRY_CODE, NETHERLANDS_COUNTRY_DESC, -1, -1, new TabelRecordAttributen()));
    tables.add(countries);

    TabelResultaat dutchTravelDocs = new TabelResultaat(GBATable.NED_REISDOC.getTableCode(),
        GBATable.NED_REISDOC.getDescr());
    dutchTravelDocs.getRecords()
        .add(new TabelRecord(UNKNOWN_DUTCH_TRAVEL_DOC_CODE, UNKNOWN_DUTCH_TRAVEL_DOC_DESC, -1, -1,
            new TabelRecordAttributen()));
    dutchTravelDocs.getRecords()
        .add(new TabelRecord("PB", "Reisdocument voor vreemdelingen", -1, -1, new TabelRecordAttributen()));
    tables.add(dutchTravelDocs);

    TabelResultaat dutchTravelDocAuthorities = new TabelResultaat(GBATable.AUT_VERSTREK_NED_REISD.getTableCode(),
        GBATable.AUT_VERSTREK_NED_REISD.getDescr());
    dutchTravelDocAuthorities.getRecords()
        .add(new TabelRecord(DUTCH_TRAVEL_AUTH_BU0518_CODE, DUTCH_TRAVEL_DOC_AUTH_BU0518_DESC, -1, -1,
            new TabelRecordAttributen()));
    dutchTravelDocAuthorities.getRecords()
        .add(new TabelRecord("B", "Burgemeester van", -1, -1, new TabelRecordAttributen()));
    dutchTravelDocAuthorities.getRecords()
        .add(new TabelRecord("C", "Consulaire post in", -1, -1, new TabelRecordAttributen()));
    dutchTravelDocAuthorities.getRecords()
        .add(new TabelRecord("G", "Gouverneur van Aruba / Gouverneur van of Gezaghebber in de Nederlandse Antillen", -1,
            -1, new TabelRecordAttributen()));
    tables.add(dutchTravelDocAuthorities);

    // tables with empty records so they won't be retrieved from an external service
    TabelResultaat streets = new TabelResultaat(GBATable.STRAAT.getTableCode(), GBATable.STRAAT.getDescr());
    streets.getRecords().add(new TabelRecord());
    tables.add(streets);

    TabelResultaat obr = new TabelResultaat(GBATable.OPENBARE_RUIMTE.getTableCode(), GBATable.OPENBARE_RUIMTE.getDescr());
    obr.getRecords().add(new TabelRecord());
    tables.add(obr);

    TabelResultaat locations = new TabelResultaat(GBATable.LOCATIE.getTableCode(), GBATable.LOCATIE.getDescr());
    locations.getRecords().add(new TabelRecord());
    tables.add(locations);

    TabelResultaat parts = new TabelResultaat(GBATable.GEMEENTE_DEEL.getTableCode(), GBATable.GEMEENTE_DEEL.getDescr());
    parts.getRecords().add(new TabelRecord());
    tables.add(parts);

    TabelResultaat districts = new TabelResultaat(GBATable.WIJK.getTableCode(), GBATable.WIJK.getDescr());
    districts.getRecords().add(new TabelRecord());
    tables.add(districts);

    TabelResultaat neighborhoods = new TabelResultaat(GBATable.BUURT.getTableCode(), GBATable.BUURT.getDescr());
    neighborhoods.getRecords().add(new TabelRecord());
    tables.add(neighborhoods);

    TabelResultaat subNeighborhoods = new TabelResultaat(GBATable.SUBBUURT.getTableCode(),
        GBATable.SUBBUURT.getDescr());
    subNeighborhoods.getRecords().add(new TabelRecord());
    tables.add(subNeighborhoods);

    GbaTables.reload();
  }
}
