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

package nl.procura.gba.web.rest.v1_0.klapper;

import static nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakType.*;
import static nl.procura.standard.Globalfunctions.along;

import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakType;

public enum GbaRestKlapperRegisterSoort {

  AKTE_GEBOORTE(1, "Geboorte", GEBOORTE),
  AKTE_OVERLIJDEN(2, "Overlijden / Lijkvinding / Levenloos geboren kind", OVERLIJDEN_IN_GEMEENTE, LIJKVINDING,
      LEVENLOOS_GEBOREN_KIND),
  AKTE_HUWELIJK(3, "Huwelijk", HUWELIJK_GPS_GEMEENTE),
  AKTE_GPS(5, "GPS", HUWELIJK_GPS_GEMEENTE),
  AKTE_ERKENNING(6, "Erkenning / Naamskeuze", ERKENNING, NAAMSKEUZE),
  AKTE_ONBEKEND(0, "Onbekend", ONBEKEND);

  private long              code      = 0;
  private GbaRestZaakType[] zaakTypes = { GbaRestZaakType.ONBEKEND };
  private String            oms       = "";

  GbaRestKlapperRegisterSoort(long code, String oms, GbaRestZaakType... zaakTypes) {

    setCode(code);
    setZaakTypes(zaakTypes);
    setOms(oms);
  }

  public static GbaRestKlapperRegisterSoort get(long registersoort) {
    for (GbaRestKlapperRegisterSoort a : values()) {
      if (a.getCode() == along(registersoort)) {
        return a;
      }
    }
    return AKTE_ONBEKEND;
  }

  public long getCode() {
    return code;
  }

  public void setCode(long code) {
    this.code = code;
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public GbaRestZaakType[] getZaakTypes() {
    return zaakTypes;
  }

  public void setZaakTypes(GbaRestZaakType... zaakTypes) {
    this.zaakTypes = zaakTypes;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
