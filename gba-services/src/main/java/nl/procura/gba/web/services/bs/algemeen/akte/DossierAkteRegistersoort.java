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

package nl.procura.gba.web.services.bs.algemeen.akte;

import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.common.ZaakType.*;
import static nl.procura.standard.Globalfunctions.along;

import java.math.BigDecimal;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;

public enum DossierAkteRegistersoort {

  AKTE_GEBOORTE(1, "Geboorte", GEBOORTE),
  AKTE_OVERLIJDEN(2, "Overlijden / Lijkvinding / Levenloos geboren kind",
      OVERLIJDEN_IN_GEMEENTE, LIJKVINDING, LEVENLOOS),
  AKTE_HUWELIJK(3, "Huwelijk / Omzetting GPS in huwelijk", HUWELIJK_GPS_GEMEENTE, OMZETTING_GPS),
  AKTE_GPS(5, "GPS", HUWELIJK_GPS_GEMEENTE),
  AKTE_ERKENNING_NAAMSKEUZE(6, "Erkenning / Naamskeuze", ERKENNING, NAAMSKEUZE),
  AKTE_ONBEKEND(0, "Onbekend", ONBEKEND);

  private long       code      = 0;
  private ZaakType[] zaakTypes = { ZaakType.ONBEKEND };
  private String     oms       = "";

  DossierAkteRegistersoort(long code, String oms, ZaakType... zaakTypes) {

    setCode(code);
    setZaakTypes(zaakTypes);
    setOms(oms);
  }

  public static DossierAkteRegistersoort get(BigDecimal registersoort) {
    for (DossierAkteRegistersoort a : values()) {
      if (a.getCode() == along(registersoort)) {
        return a;
      }
    }
    return AKTE_ONBEKEND;
  }

  public static DossierAkteRegistersoort get(Dossier dossier) {

    if (dossier.getZaakDossier() != null && (dossier.getZaakDossier() instanceof DossierHuwelijk)) {
      SoortVerbintenis soort = to(dossier.getZaakDossier(), DossierHuwelijk.class).getSoortVerbintenis();
      return (soort == SoortVerbintenis.GPS) ? DossierAkteRegistersoort.AKTE_GPS
          : DossierAkteRegistersoort.AKTE_HUWELIJK;
    }

    for (DossierAkteRegistersoort a : values()) {
      for (ZaakType zt : a.getZaakTypes()) {
        if (zt == dossier.getType()) {
          return a;
        }
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

  public ZaakType[] getZaakTypes() {
    return zaakTypes;
  }

  public void setZaakTypes(ZaakType... zaakTypes) {
    this.zaakTypes = zaakTypes;
  }

  public boolean is(DossierAkteRegistersoort... soorten) {
    for (DossierAkteRegistersoort soort : soorten) {
      if (this == soort) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
