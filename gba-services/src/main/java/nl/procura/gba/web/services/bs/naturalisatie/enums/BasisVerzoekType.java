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

package nl.procura.gba.web.services.bs.naturalisatie.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import nl.procura.gba.common.EnumUtils;
import nl.procura.gba.common.EnumWithCode;

public enum BasisVerzoekType implements EnumWithCode<Integer> {

  ONBEKEND(-1, "Onbekend", false, false),
  // ==================
  // Minderjarig, optie
  // ==================
  OPTIE_100(100, "b: geboren in Koninkrijk, minstens 3 jr. toelating en verblijf "
      + "en staatloos sinds geboorte", false, true),
  OPTIE_101(101, "c: erkend door een NL-er en min. 3 jr onafgebroken verzorgd door deze persoon", false, true),
  OPTIE_102(102, "d: vanaf geboorte onder gezamenlijk gezag niet-NL en NL ouder en "
      + "min. 3 jr onafgebroken verzorg door de NL-er",
      false, true),
  OPTIE_103(103, "k: geboren als kind van een onder i of j bedoelde persoon", false, true),
  OPTIE_104(104, "l: voor het 7e jr erkend door een onder i of j bedoelde persoon", false, true),
  OPTIE_105(105, "m: als minderjarige erkend door een onder i of j bedoelde persoon die de "
      + "aangetoonde biologische vader is", false, true),
  OPTIE_106(106, "n: door gerechtelijke vaststelling ouderschap kind van een in i of j "
      + "bedoelde persoon geworden", false, true),
  OPTIE_107(107, "o: in het Koninkrijk door adoptie door een in i of j bedoelde persoon", false, true),
  OPTIE_109(109, "p: van rechtswege NL-schap verloren incl. Unieburgerschap waarbij de "
      + "onevenredige gevolgen voorzienbaar waren", false, true),
  OPTIE_108(108, "art II lid 1 RW 27-06-2008, erkenning of wettiging door NL als minderjarige "
      + "tussen 1-4-2003 en 1-3-2009", false, true),
  // ===================
  // Meerderjarig, optie
  // ===================
  OPTIE_200(200, "a: geboren toegelaten en sinds geboorte verblijf in Koninkrijk", true, true),
  OPTIE_201(201, "b: geboren in Koninkrijk, minstens 3 jr. toelating en verblijf en "
      + "staatloos sinds geboorte", true, true),
  OPTIE_202(202, "e: vanaf 4e jr toelating en wonend in Koninkrijk", true, true),
  OPTIE_203(203, "f: oud-NL-er en min. 1 jr. toegelaten en wonend in Koninkrijk", true, true),
  OPTIE_204(204, "g: min. 3 jr. huw/GPS met NL-er en min. 15 jr toegelaten en wonend in Koninkrijk", true, true),
  OPTIE_205(205, "h: 65+ en min. 15 jr toegelaten en wonend in Koninkrijk", true, true),
  OPTIE_206(206, "i: uit een NL moeder en een niet-NL vader", true, true),
  OPTIE_207(207, "j: in het Koninkrijk als minderjarige geadopteerd door een NL vrouw", true, true),
  OPTIE_208(208, "k: geboren als kind van een onder i of j bedoelde persoon", true, true),
  OPTIE_209(209, "l: voor het 7e jr erkend door een onder i of j bedoelde persoon", true, true),
  OPTIE_210(210, "m: als minderjarige erkend door een onder i of j bedoelde persoon die "
      + "de aangetoonde biologische vader is", true, true),
  OPTIE_211(211, "n: door gerechtelijke vaststelling ouderschap kind van een in i of j "
      + "bedoelde persoon geworden", true, true),
  OPTIE_212(212, "o: in het Koninkrijk door adoptie door een in i of j bedoelde persoon", true, true),
  OPTIE_213(213, "p: van rechtswege NL-schap verloren incl. Unieburgerschap waarbij de "
      + "onevenredige gevolgen voorzienbaar waren", true, true),
  OPTIE_214(214, "art 28: herkrijgen na verlies NL-schap door huwelijk met niet-NL", true, true),
  // ==========================
  // Minderjarig, naturalisatie
  // ==========================
  OPTIE_300(300, "Artikel 11, lid 4 RWN (na-naturalisatie)", false, false),
  // ===========================
  // Meerderjarig, naturalisatie
  // ===========================
  OPTIE_400(400, "Artikel 8, lid 1c: 5 jr. onafgebroken toelating en hoofdverblijf in NL", true, false),
  OPTIE_401(401, "Artikel 8, lid 2: 3jr. onafgebroken gehuwd en samenwonend op hetzelfde adres "
      + "met dezelfde NL-er of als meerderjarige geadopteerd of oud-NL-er", true, false),
  OPTIE_402(402, "Artikel 8, lid 3: 10 jr. toelating en hoofdverblijf waarvan laatste 2 jr. "
      + "onafgebroken", true, false),
  OPTIE_403(403, "Artikel 8, lid 4: 3jr. onafgebroken toelating en hoofdverblijf en 3 jr. "
      + "onafgebroken ongehuwd samenwonend op hetzelfde adres met dezelfde NL-er of staatloos", true, false),
  OPTIE_404(404, "Artikel 8, lid 5: erkenning en wettiging als minderjarige door NL-er en 3 jr. "
      + "onafgebroken toelating en hoofdverblijf", true, false),
  OPTIE_405(405, "Artikel 11, lid 5: niet gedeeld in de verkrijging of verlening van het NL-schap "
      + "van de ouder alleen vanwege meerderjarigheid of betrokkene heeft 3 jr. onafgebroken toelating en "
      + "hoofdverblijf aanvangende voor meerderjarigheid", true, false);

  private final int     code;
  private final String  oms;
  private final boolean meerderjarig;
  private final boolean optie;

  BasisVerzoekType(int code, String oms, boolean meerderjarig, boolean optie) {
    this.code = code;
    this.oms = oms;
    this.meerderjarig = meerderjarig;
    this.optie = optie;
  }

  public static BasisVerzoekType get(Number code) {
    return EnumUtils.get(values(), code, ONBEKEND);
  }

  public static List<BasisVerzoekType> get(boolean meerderjarig, boolean optie) {
    return Arrays.stream(values())
        .filter(val -> val.getCode() > 0 && (meerderjarig == val.isMeerderjarig() && optie == val.isOptie()))
        .collect(Collectors.toList());
  }

  @Override
  public Integer getCode() {
    return code;
  }

  public String getOms() {
    return oms;
  }

  public boolean isMeerderjarig() {
    return meerderjarig;
  }

  public boolean isOptie() {
    return optie;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
