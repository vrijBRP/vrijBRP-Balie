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

package nl.procura.gba.web.rest.v1_0.zaak;

import java.util.ArrayList;
import java.util.List;

public enum GbaRestZaakType {

  UITTREKSEL(10, "Uittreksel"),
  VERSTREKKINGSBEPERKING(20, "Verstrek. beperk."),
  NAAMGEBRUIK(30, "Naamgebruik"),
  VERHUIZING(40, "Verhuizing"),
  COVOG(50, "COVOG"),
  GPK(60, "GPK"),
  REISDOCUMENT(70, "Reisdocument"),
  INHOUD_VERMIS(75, "Inhouding / vermissing document"),
  RIJBEWIJS(80, "Rijbewijs"),
  TERUGMELDING(90, "Terugmelding"),
  GEBOORTE(100, "Geboorte"),
  ERKENNING(200, "Erkenning"),
  HUWELIJK_GPS_GEMEENTE(300, "Huwelijk / GPS in gemeente"),
  NAAMSKEUZE(400, "Naamskeuze"),
  OVERLIJDEN_IN_GEMEENTE(500, "Overlijden in gemeente"),
  LIJKVINDING(600, "Lijkvinding"),
  OVERLIJDEN_IN_BUITENLAND(700, "Overlijden buitenland"),
  LEVENLOOS_GEBOREN_KIND(800, "Levenloos geboren kind"),
  INDICATIE(900, "Indicatie"),
  CORRESPONDENTIE(1100, "Correspondentie"),
  GEGEVENSVERSTREKKING(1200, "Gegevensverstrekking"),
  OMZETTING_GPS(1300, "Omzetting GPS in Huwelijk"),
  ONTBINDING_GEMEENTE(1400, "Ontbinding/einde in gemeente"),
  INBOX(1500, "Inboxbericht"),
  ONDERZOEK(1600, "Onderzoek"),
  REGISTRATION(1700, "Eerste inschrijving"),
  RISK_ANALYSIS(1800, "Risicoanalyse"),
  PL_MUTATION(1900, "Persoonslijst mutaties"),
  ONBEKEND(0, "Onbekend");

  private long   code = 0;
  private String oms  = "";

  GbaRestZaakType(long code, String descr) {

    setCode(code);
    setOms(descr);
  }

  public static GbaRestZaakType get(long code) {

    for (GbaRestZaakType a : values()) {
      if (a.getCode() == code) {
        return a;
      }
    }

    return ONBEKEND;
  }

  public static List<GbaRestZaakType> getAll() {
    List<GbaRestZaakType> types = new ArrayList<>();
    for (GbaRestZaakType type : values()) {
      if (type != ONBEKEND) {
        types.add(type);
      }
    }

    return types;
  }

  @Override
  public String toString() {
    return getOms();
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public long getCode() {
    return code;
  }

  public void setCode(long code) {
    this.code = code;
  }
}
