/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.services.beheer.verkiezing;

import static nl.procura.gba.web.services.beheer.verkiezing.StempasAanduidingType.AAND_BRIEFSTEMBEWIJS;
import static nl.procura.gba.web.services.beheer.verkiezing.StempasAanduidingType.AAND_GEEN;
import static nl.procura.gba.web.services.beheer.verkiezing.StempasAanduidingType.AAND_INTREKKEN_OVERIG;
import static nl.procura.gba.web.services.beheer.verkiezing.StempasAanduidingType.AAND_INTREKKEN_VERLIES;
import static nl.procura.gba.web.services.beheer.verkiezing.StempasAanduidingType.AAND_KIEZERSPAS;
import static nl.procura.gba.web.services.beheer.verkiezing.StempasAanduidingType.AAND_ONGELDIG_KIESG;
import static nl.procura.gba.web.services.beheer.verkiezing.StempasAanduidingType.AAND_ONGELDIG_OVERL;
import static nl.procura.gba.web.services.beheer.verkiezing.StempasAanduidingType.AAND_VERVANGEN;
import static nl.procura.gba.web.services.beheer.verkiezing.StempasAanduidingType.AAND_VOLMACHTBEWIJS;

import java.util.Arrays;

public enum KiezersregisterActieType {

  ACT_KIEZERSPAS(1, "Vervangen door kiezerspas", AAND_KIEZERSPAS),
  ACT_BRIEFSTEMMEN(2, "Vervangen door briefstembewijs", AAND_BRIEFSTEMBEWIJS),
  ACT_VERVANGEN(3, "Vervangen door nieuwe stempas", AAND_VERVANGEN),
  ACT_MACHTIGEN(4, "Machtigen", AAND_VOLMACHTBEWIJS),
  ACT_ONGELDIG_OVERL(5, "Ongeldig verklaren wegens overlijden", AAND_ONGELDIG_OVERL),
  ACT_ONGELDIG_KIESG(6, "Ongeldig verklaren wegens ontbreken kiesgerechtigheid", AAND_ONGELDIG_KIESG),
  ACT_INTREKKEN_VERLIES(7, "Ongeldig verklaren (intrekken) wegens vastgestelde ontvreemding", AAND_INTREKKEN_VERLIES),
  ACT_INTREKKEN_OVERIG(8, "Stempas ongeldig verklaren (intrekken) wegens overige reden", AAND_INTREKKEN_OVERIG),
  ACT_TOEVOEGEN(9, "Toevoegen aan kiezersregister", AAND_GEEN),
  ACT_VERWIJDEREN(99, "Aanduiding ongedaan maken", AAND_GEEN),
  ACT_ONBEKEND(0, "Onbekend", AAND_KIEZERSPAS);

  private final int                   code;
  private final String                oms;
  private final StempasAanduidingType aanduidingType;

  KiezersregisterActieType(int code, String oms, StempasAanduidingType aanduidingType) {
    this.code = code;
    this.oms = oms;
    this.aanduidingType = aanduidingType;
  }

  public static KiezersregisterActieType get(Long code) {
    return Arrays.stream(values())
        .filter(a -> a.getCode() == code)
        .findFirst().orElse(ACT_ONBEKEND);

  }

  public int getCode() {
    return code;
  }

  public String getOms() {
    return oms;
  }

  public StempasAanduidingType getAanduidingType() {
    return aanduidingType;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
