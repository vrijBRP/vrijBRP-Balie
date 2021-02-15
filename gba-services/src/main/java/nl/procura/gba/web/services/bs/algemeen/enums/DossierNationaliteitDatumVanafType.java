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

package nl.procura.gba.web.services.bs.algemeen.enums;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

import java.util.Arrays;

public enum DossierNationaliteitDatumVanafType {

  GEBOORTE_DATUM("G", "Geboortedatum"),
  GEBOORTE_DATUM_DERDE("D", "Geboortedatum (derde generatie Nederlander)"),
  ERKENNINGS_DATUM("E", "Erkenningsdatum"),
  ERKENNING_AANGIFTE_DATUM("A", "Datum erkenning/aangifte"),
  ANDERS("N", "Zelf invullen"),
  ONBEKEND("", "Onbekend");

  private String code  = "";
  private String descr = "";

  DossierNationaliteitDatumVanafType(String code, String descr) {
    this.code = code;
    this.descr = descr;
  }

  public static DossierNationaliteitDatumVanafType get(String code) {
    return Arrays.stream(values()).filter(a -> equalsIgnoreCase(a.getCode(), code)).findFirst().orElse(ONBEKEND);
  }

  public boolean is(DossierNationaliteitDatumVanafType... types) {
    return Arrays.stream(types).anyMatch(type -> type == this);
  }

  public String getCode() {
    return code;
  }

  public String getDescr() {
    return descr;
  }

  @Override
  public String toString() {
    return getDescr();
  }
}
