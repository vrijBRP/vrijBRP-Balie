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

import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.HUWELIJK;
import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.PARTNERSCHAP;

import java.util.Arrays;

import nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType;

import lombok.Getter;

@Getter
public enum DossierPersoonType {

  AANGEVER(10, "Aangever", "", false),
  AFGEVER(11, "Afgever", "", true),
  OVERLEDENE(15, "Overledene", "", true),
  KIND(20, "Kind", "", true),
  VADER_DUO_MOEDER(30, "Vader/duo-moeder", "", true),
  MOEDER(40, "Moeder", "V", true),
  PARTNER(50, "Partner", "", true, HUWELIJK, PARTNERSCHAP),
  PARTNER_ANDERE_OUDER(51, "Partner/andere ouder", "", true),
  EXPARTNER(55, "Ex-partner", "", true),
  ERKENNER(60, "Erkenner", "", true),
  PARTNER1(70, "Partner 1", "", true),
  PARTNER2(71, "Partner 2", "", true),
  GETUIGE(72, "Getuige", "", true),
  AMBTENAAR(73, "Ambtenaar", "", true),
  OUDER(74, "Ouder", "", true, VADER_DUO_MOEDER, MOEDER),
  BETROKKENE(75, "Betrokkene", "", false),
  ONBEKEND(0, "Onbekend", "", true),
  INSCHRIJVER(80, "Inschrijver", "", false),
  GERELATEERDE_BRP(81, "Gerelateerde in BRP", "", true),
  GERELATEERDE_NIET_BRP(82, "Gerelateerde niet in BRP", "", true),
  VERTEGENWOORDIGER(83, "Vertegenwoordiger", "", false),
  TOESTEMMINGGEVER(84, "Toestemminggever", "", false),
  MEDE_VERZOEKER_PARTNER(85, "Mede-verzoeker (partner)", "", false),
  MEDE_VERZOEKER_KIND(86, "Mede-verzoeker (kind)", "", false),
  ADOPTIEFOUDER(87, "Adoptiefouder", "", false);

  private final long                   code;
  private final String                 descr;
  private final String                 geslacht;
  private final boolean                magOverledenZijn;
  private final DossierPersoonType[]   subTypes;
  private final BurgerlijkeStaatType[] bsTypes;

  DossierPersoonType(long code, String descr, String geslacht, boolean magOverledenZijn) {
    this(code, descr, geslacht, magOverledenZijn, null, null);
  }

  DossierPersoonType(long code, String descr, String geslacht, boolean magOverledenZijn,
      BurgerlijkeStaatType... bsTypes) {
    this(code, descr, geslacht, magOverledenZijn, null, bsTypes);
  }

  DossierPersoonType(long code, String descr, String geslacht, boolean magOverledenZijn,
      DossierPersoonType... bsTypes) {
    this(code, descr, geslacht, magOverledenZijn, bsTypes, null);
  }

  DossierPersoonType(long code, String descr, String geslacht, boolean magOverledenZijn,
      DossierPersoonType[] subTypes, BurgerlijkeStaatType[] bsTypes) {

    this.code = code;
    this.descr = descr;
    this.geslacht = geslacht;
    this.magOverledenZijn = magOverledenZijn;
    this.subTypes = subTypes;
    this.bsTypes = bsTypes;
  }

  public static DossierPersoonType get(long code) {
    return Arrays.stream(values())
        .filter(type -> type.getCode() == code)
        .findFirst().orElse(ONBEKEND);
  }

  public String getDescrExtra() {
    return (KIND.equals(this) ? "het " : "de ") + getDescr().toLowerCase();
  }

  public boolean heeftMogelijkeBurgerlijkeStaat(BurgerlijkeStaatType burgerlijkeStaat) {
    if (getBsTypes() != null) {
      return Arrays.stream(bsTypes)
          .anyMatch(bsType -> bsType == burgerlijkeStaat);

    }

    return true;
  }

  public boolean is(DossierPersoonType... types) {
    if (types != null) {
      for (final DossierPersoonType type : types) {
        if (this == type) {
          return true;
        }
        // SubTypes
        if (type.getSubTypes() != null) {
          for (final DossierPersoonType subType : type.getSubTypes()) {
            if (this == subType) {
              return true;
            }
          }
        }
      }
    }

    return false;
  }

  @Override
  public String toString() {
    return getDescr();
  }
}
