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

package nl.procura.gba.web.services.gba.ple.relatieLijst;

import static nl.procura.gba.web.services.gba.ple.relatieLijst.AangifteSoort.*;

public enum RelatieType {

  AANGEVER(1, INGESCHREVENE, "Aangever"),
  PARTNER(2, ECHTGENOOT_GEREGISTREERD_PARTNER, "Partner"),
  KIND(3, GEZAGHOUDER, "Kind"),
  OUDER(4, MEERDERJARIG_INWONEND_KIND_VOOR_OUDER, "Ouder"),
  MEERDERJARIG_KIND(5, INWONENDE_OUDER_VOOR_MEERDERJARIG_KIND, "Kind > 18"),
  NIET_GERELATEERD(9, MEERDERJARIGE_GEMACHTIGDE, "Niet-gerelateerd"),
  GEZAGHEBBENDE(10, MEERDERJARIGE_GEMACHTIGDE, "Gezaghebbende"),
  NIET_GERELATEERD_MINDERJARG(11, AMBTSHALVE, "Niet-gerelateerd"),
  ONBEKEND(99, MEERDERJARIGE_GEMACHTIGDE, "Onbekend");

  private long          code     = 0;
  private String        oms      = "";
  private AangifteSoort aangifte = AangifteSoort.ONBEKEND;

  RelatieType(long code, AangifteSoort as, String oms) {
    setCode(code);
    setAangifte(as);
    setOms(oms);
  }

  public static RelatieType getType(long code) {
    for (RelatieType e : values()) {
      if (e.getCode() == code) {
        return e;
      }
    }

    return ONBEKEND;
  }

  public AangifteSoort getAangifte() {
    return aangifte;
  }

  public void setAangifte(AangifteSoort aangifte) {
    this.aangifte = aangifte;
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

  @Override
  public String toString() {
    return getOms();
  }
}
