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

package nl.procura.gba.common;

import java.util.ArrayList;
import java.util.List;

public enum ZaakType {

  UITTREKSEL(10, "Uittreksel", true),
  VERSTREKKINGSBEPERKING(20, "Verstrek. beperk.", true),
  NAAMGEBRUIK(30, "Naamgebruik", true),
  VERHUIZING(40, "Verhuizing", true),
  COVOG(50, "VOG", true),
  GPK(60, "GPK", true),
  REISDOCUMENT(70, "Reisdocument", true),
  INHOUD_VERMIS(75, "Inhouding / vermissing document", false),
  RIJBEWIJS(80, "Rijbewijs", true),
  TERUGMELDING(90, "Terugmelding", false),
  GEBOORTE(100, "Geboorte", true),
  ERKENNING(200, "Erkenning", true),
  HUWELIJK_GPS_GEMEENTE(300, "Huwelijk / GPS in gemeente", true),
  NAAMSKEUZE(400, "Naamskeuze", true),
  OVERLIJDEN_IN_GEMEENTE(500, "Overlijden in gemeente", true),
  LIJKVINDING(600, "Lijkvinding", true),
  OVERLIJDEN_IN_BUITENLAND(700, "Overlijden buitenland", true),
  LEVENLOOS(800, "Levenloos geboren kind", true),
  INDICATIE(900, "Indicatie", false),
  LEGE_PERSOONLIJST(1000, "Lege zaak", false),
  CORRESPONDENTIE(1100, "Correspondentie", false),
  GEGEVENSVERSTREKKING(1200, "Gegevensverstrekking", true),
  OMZETTING_GPS(1300, "Omzetting GPS in Huwelijk", true),
  ONTBINDING_GEMEENTE(1400, "Ontbinding/einde huwelijk/GPS in gemeente", true),
  INBOX(1500, "Inboxbericht", true),
  ONDERZOEK(1600, "Onderzoek", true),
  REGISTRATION(1700, "Eerste inschrijving", true),
  RISK_ANALYSIS(1800, "Risicoanalyse", true),
  PL_MUTATION(1900, "Persoonslijst mutaties", true),
  ONBEKEND(0, "Onbekend", false);

  private long    code;
  private String  oms;
  private boolean heeftBijlagen;

  ZaakType(long code, String oms, boolean heeftBijlagen) {

    this.code = code;
    this.oms = oms;
    this.heeftBijlagen = heeftBijlagen;
  }

  public static ZaakType get(long code) {
    for (ZaakType a : values()) {
      if (a.getCode() == code) {
        return a;
      }
    }

    return ONBEKEND;
  }

  public static List<ZaakType> getAll() {
    List<ZaakType> types = new ArrayList<>();
    for (ZaakType type : values()) {
      if (type != ONBEKEND) {
        types.add(type);
      }
    }

    return types;
  }

  public boolean is(ZaakType... zaakTypen) {
    for (ZaakType type : zaakTypen) {
      if (getCode() == type.getCode()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return getOms();
  }

  public String getOms() {
    return oms;
  }

  public long getCode() {
    return code;
  }

  public boolean isHeeftBijlagen() {
    return heeftBijlagen;
  }
}
