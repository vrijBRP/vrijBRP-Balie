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

import java.util.Arrays;

public enum StempasAanduidingType {

  AAND_KIEZERSPAS(1, "Kiezerspas", "Vervangen door kiezerspas"),
  AAND_BRIEFSTEMBEWIJS(2, "Briefstembewijs", "Vervangen door briefstembewijs"),
  AAND_VERVANGEN(3, "Vervangen", "Vervangen door nieuwe stempas"),
  AAND_VOLMACHTBEWIJS(4, "Volmachtbewijs", "Vervangen door volmachtbewijs"),
  AAND_ONGELDIG_OVERL(5, "Overlijden", "Ongeldig verklaart wegens overlijden"),
  AAND_ONGELDIG_KIESG(6, "Niet kiesgerechtigd", "Ongeldig verklaart wegens ontbreken kiesgerechtigheid"),
  AAND_INTREKKEN_VERLIES(7, "Verlies/ontvreemding", "Ongeldig verklaart wegens vastgestelde ontvreemding"),
  AAND_INTREKKEN_OVERIG(8, "Overige reden", "Stempas ongeldig verklaart wegens overige reden"),
  AAND_ONBEKEND(0, "Onbekend", "Onbekende aanduiding"),
  AAND_ALLE(100, "Alle aanduidingen", "Alle aanduidingen"),
  AAND_GEEN(-1, "Geen aanduiding", "Geen");

  private final long   code;
  private final String type;
  private final String oms;

  StempasAanduidingType(long code, String type, String oms) {
    this.code = code;
    this.type = type;
    this.oms = oms;
  }

  public static StempasAanduidingType get(Long code) {
    return Arrays.stream(values())
        .filter(a -> a.getCode() == code)
        .findFirst().orElse(AAND_ONBEKEND);

  }

  public long getCode() {
    return code;
  }

  public String getType() {
    return type;
  }

  public String getOms() {
    return oms;
  }

  @Override
  public String toString() {
    return getType();
  }
}
