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

package nl.procura.gba.web.common.misc;

import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class Landelijk {

  public static final long ONBEKEND = 0;

  public static final long LAND_NEDERLAND    = 6030;
  public static final long LAND_ARUBA        = 5095;
  public static final long LAND_BONAIRE      = 5106;
  public static final long LAND_CURACAO      = 5107;
  public static final long LAND_SABA         = 5108;
  public static final long LAND_ST_EUSTATIUS = 5109;
  public static final long LAND_ST_MAARTEN   = 5110;

  public static final long NATIONALITEIT_NEDERLANDS                = 1;
  public static final long NATIONALITEIT_STAATLOOS                 = 499;
  public static final long NATIONALITEIT_BEHANDELD_ALS_NEDERLANDER = 9999;
  public static final int  RNI                                     = 1999;

  public static final long REDEN_ONTERECHT_OPGENOMEN_CAT = 405;
  public static final long EUKIESRECHT_ONTVANG_OPROEP    = 2L;

  /**
   * Geeft Nederland terug in FieldValue formaat
   */
  public static FieldValue getNederland() {
    return GbaTables.LAND.get(Landelijk.LAND_NEDERLAND);
  }

  /**
   * Geeft de Nederlandse nationaliteit terug in FieldValue formaat
   */
  public static FieldValue getNederlandse() {
    return GbaTables.NATIO.get(Landelijk.NATIONALITEIT_NEDERLANDS);
  }

  /**
   * / * Is deze DossierNationalitet Nederland?
   */
  public static boolean isNationaliteit(DossierNationaliteit nat, long codeNationaliteit) {
    return nat != null && isValue(nat.getNationaliteit(), codeNationaliteit);
  }

  public static boolean isNationaliteit(FieldValue fv, long codeNationaliteit) {
    return isValue(fv, codeNationaliteit);
  }

  /**
   * / * Is deze DossierNationalitet Nederland?
   */
  public static boolean isNederland(DossierNationaliteit nat) {
    return isNationaliteit(nat, NATIONALITEIT_NEDERLANDS);
  }

  /**
   * Is de FieldValue Nederland of de Nederlandse nationaliteit?
   */
  public static boolean isNederland(FieldValue fv) {

    if (fv != null) {
      boolean isNat = fv.equals(GbaTables.NATIO.get(NATIONALITEIT_NEDERLANDS));
      boolean isLand = fv.equals(GbaTables.LAND.get(LAND_NEDERLAND));
      return isNat || isLand;
    }

    return false;
  }

  /**
   * Komt de waarde van FieldValue overeen met Nederland of Onbekend
   */
  public static boolean isNederlandOfOnbekend(FieldValue fv) {
    return isNederland(fv) || isOnbekend(fv);
  }

  /**
   * Is de FieldValue Onbekend
   */
  public static boolean isOnbekend(FieldValue fv) {
    return isValue(fv, ONBEKEND);
  }

  /**
   * Komt de waarde van FieldValue overeen met de value parameter
   */
  public static boolean isValue(FieldValue fv, long value) {
    return fv != null && fv.getLongValue() == value;
  }
}
