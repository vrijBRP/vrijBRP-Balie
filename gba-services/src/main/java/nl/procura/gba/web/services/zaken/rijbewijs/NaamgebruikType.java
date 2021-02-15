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

package nl.procura.gba.web.services.zaken.rijbewijs;

import java.util.Arrays;

public enum NaamgebruikType {

  EIGEN_NAAM("E", "Eigen geslachtsnaam", 1),
  NAAM_PARTNER("P", "Geslachtsnaam partner", 2),
  NAAM_PARTNER_EIGEN_NAAM("V", "Geslachtsnaam partner voor eigen geslachtsnaam", 3),
  EIGEN_NAAM_NAAM_PARTNER("N", "Eigen geslachtsnaam voor geslachtsnaam partner", 4);

  final private long   rdwCode;
  final private String afk;
  final private String oms;

  NaamgebruikType(String afk, String oms, long rdwCode) {
    this.rdwCode = rdwCode;
    this.afk = afk;
    this.oms = oms;
  }

  public static NaamgebruikType getByRdwCode(long code) {
    return Arrays.stream(values()).filter(a -> a.getRdwCode() == code).findFirst().orElse(EIGEN_NAAM);
  }

  public static NaamgebruikType getByAfk(String afk) {
    return Arrays.stream(values()).filter(a -> a.getAfk().equals(afk)).findFirst().orElse(EIGEN_NAAM);
  }

  public String getAfk() {
    return afk;
  }

  public long getRdwCode() {
    return rdwCode;
  }

  public String getOms() {
    return oms;
  }

  @Override
  public String toString() {
    return getAfk() + ": " + getOms();
  }
}
