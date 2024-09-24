/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.jpa.personen.types;

import static nl.procura.gba.jpa.personen.types.RiskProfileRuleMap.Type.COUNTRY;
import static nl.procura.gba.jpa.personen.types.RiskProfileRuleMap.Type.DISTRICT1;
import static nl.procura.gba.jpa.personen.types.RiskProfileRuleMap.Type.DISTRICT2;
import static nl.procura.gba.jpa.personen.types.RiskProfileRuleMap.Type.DISTRICT3;
import static nl.procura.gba.jpa.personen.types.RiskProfileRuleMap.Type.NUMBER;
import static nl.procura.gba.jpa.personen.types.RiskProfileRuleMap.Type.POSNUMBER;
import static nl.procura.gba.jpa.personen.types.RiskProfileRuleVar.X;
import static nl.procura.gba.jpa.personen.types.RiskProfileRuleVar.Y;
import static nl.procura.gba.jpa.personen.types.RiskProfileRuleVar.Z;

import nl.procura.gba.common.EnumUtils;
import nl.procura.gba.common.EnumWithCode;

public enum RiskProfileRuleType implements EnumWithCode<Integer> {

  RULE_1(1, "Verhuizing van persoon waarvan het BSN is gemarkeerd (algemeen)"),

  RULE_2(2, "Verhuizing van persoon die minimaal x aantal keer in de afgelopen y dagen is verhuisd",
      new RiskProfileRuleMap().add(X, "Aantal keer (x)", POSNUMBER).add(Y, "Aantal dagen (y)", POSNUMBER)),

  RULE_3(3, "Verhuizing van persoon die minimaal x keer in de afgelopen y dagen naar land z is vertrokken",
      new RiskProfileRuleMap().add(X, "Aantal keer (x)", POSNUMBER).add(Y, "Aantal dagen (y)", POSNUMBER).add(Z,
          "Land (z)",
          COUNTRY)),

  RULE_4(4, "Verhuizing van persoon die verhuist van een briefadres naar een woonadres of andersom"),

  RULE_5(5, "Verhuizing van persoon waarbij categorie 08 (verblijfplaats) in onderzoek staat"),

  RULE_6(6, "Verhuizing waarvan de aangifte wordt gedaan op een bepaald tijdstip (tussen x en y uur)",
      new RiskProfileRuleMap().add(X, "Begin uur (x)", NUMBER).add(Y, "Eind uur (y)", POSNUMBER)),

  RULE_7(7,
      "Verhuizing naar een adres in wijk x",
      new RiskProfileRuleMap().add(X, "Wijk (x)", DISTRICT1)),

  RULE_8(8,
      "Verhuizing naar een adres in buurt x",
      new RiskProfileRuleMap().add(X, "Buurt (x)", DISTRICT2)),

  RULE_9(9,
      "Verhuizing naar een adres in subbuurt x",
      new RiskProfileRuleMap().add(X, "Subbuurt (x)", DISTRICT3)),

  RULE_10(10,
      "Verhuizing naar een gemarkeerd adres",
      new RiskProfileRuleMap()),

  RULE_11(11, "Verhuizing naar een adres wat niet voor bewoning geschikt is"),

  RULE_12(12,
      "Verhuizing van persoon waarvan het reisdocument of rijbewijs verloopt binnen periode x aantal dagen",
      new RiskProfileRuleMap().add(X, "Aantal dagen (x)", POSNUMBER)),

  RULE_13(13,
      "Verhuizing naar een adres wat niet als gebruiksdoel woonfunctie heeft (BAG)"),

  RULE_14(14,
      "Verhuizing naar een adres waarbij de oppervlakte per persoon < x (BAG)",
      new RiskProfileRuleMap().add(X, "Oppervlakte pp (x)", POSNUMBER)),

  RULE_15(15, "Verhuizing van persoon die onder curatele staat"),
  RULE_16(16, "Verhuizing naar het huidige adres"),
  RULE_17(17, "Hoofdbewoner is geen bewoner van het nieuwe adres en het is geen baliezaak"),
  RULE_18(18, "Adres is een nevenadres"),
  RULE_19(19, "Verhuizing binnen aantal dagen na vorige verhuizing",
      new RiskProfileRuleMap().add(X, "Aantal dagen", POSNUMBER)),

  UNKNOWN(0, "Onbekend");

  private final int                code;
  private final String             descr;
  private final RiskProfileRuleMap variables;

  RiskProfileRuleType(int code, String descr) {
    this(code, descr, new RiskProfileRuleMap());
  }

  RiskProfileRuleType(int code, String descr, RiskProfileRuleMap variables) {
    this.code = code;
    this.descr = descr;
    this.variables = variables;
  }

  public static RiskProfileRuleType get(Number value) {
    return EnumUtils.get(RiskProfileRuleType.values(), value, UNKNOWN);
  }

  public boolean is(RiskProfileRuleType... types) {
    return EnumUtils.is(types, code);
  }

  @Override
  public Integer getCode() {
    return code;
  }

  public String getDescr() {
    return descr;
  }

  public RiskProfileRuleMap getVariables() {
    return variables;
  }

  @Override
  public String toString() {
    return getDescr();
  }
}
