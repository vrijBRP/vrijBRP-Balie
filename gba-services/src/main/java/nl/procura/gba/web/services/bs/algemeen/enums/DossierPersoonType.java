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

import nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType;

public enum DossierPersoonType {

  AANGEVER(10, "Aangever", "", false),
  AFGEVER(11, "Afgever", "", true),
  OVERLEDENE(15, "Overledene", "", true),
  KIND(20, "Kind", "", true),
  VADER_DUO_MOEDER(30, "Vader / duo-moeder", "", true),
  MOEDER(40, "Moeder", "V", true),
  PARTNER(50, "Partner", "", true, HUWELIJK, PARTNERSCHAP),
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
  GERELATEERDE_NIET_BRP(82, "Gerelateerde niet in BRP", "", true);

  private long                   code;
  private String                 descr;
  private String                 geslacht;
  private boolean                magOverledenZijn;
  private DossierPersoonType[]   subTypes;
  private BurgerlijkeStaatType[] bsTypes;

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
    for (final DossierPersoonType a : values()) {
      if (a.getCode() == code) {
        return a;
      }
    }

    return ONBEKEND;
  }

  public BurgerlijkeStaatType[] getBsTypes() {
    return bsTypes;
  }

  public void setBsTypes(BurgerlijkeStaatType[] bsTypes) {
    this.bsTypes = bsTypes;
  }

  public long getCode() {
    return code;
  }

  public void setCode(long code) {
    this.code = code;
  }

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  public String getDescrExtra() {
    return (KIND.equals(this) ? "het " : "de ") + getDescr().toLowerCase();
  }

  public String getGeslacht() {
    return geslacht;
  }

  public void setGeslacht(String geslacht) {
    this.geslacht = geslacht;
  }

  public DossierPersoonType[] getSubTypes() {
    return subTypes;
  }

  public boolean heeftMogelijkeBurgerlijkeStaat(BurgerlijkeStaatType burgerlijkeStaat) {

    if (getBsTypes() != null) {
      for (final BurgerlijkeStaatType bsType : bsTypes) {
        if (bsType == burgerlijkeStaat) {
          return true;
        }
      }
      return false;
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

  public boolean isMagOverledenZijn() {
    return magOverledenZijn;
  }

  public void setMagOverledenZijn(boolean magOverledenZijn) {
    this.magOverledenZijn = magOverledenZijn;
  }

  @Override
  public String toString() {
    return getDescr();
  }
}
