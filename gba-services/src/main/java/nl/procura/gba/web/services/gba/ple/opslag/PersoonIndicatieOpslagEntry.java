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

package nl.procura.gba.web.services.gba.ple.opslag;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;

public class PersoonIndicatieOpslagEntry {

  private final BasePLExt pl;
  private final String    indicatie;

  public PersoonIndicatieOpslagEntry(BasePLExt pl, String indicatie) {
    this.pl = pl;
    this.indicatie = indicatie;
  }

  @Override
  public boolean equals(Object obj) {

    if (obj instanceof PersoonIndicatieOpslagEntry) {
      PersoonIndicatieOpslagEntry entry = (PersoonIndicatieOpslagEntry) obj;
      boolean isPl = (entry.getPl().getPersoon().getNummer() == getPl().getPersoon().getNummer());
      boolean isInd = (entry.getIndicatie().equals(getIndicatie()));
      return (isPl && isInd);
    }

    return false;
  }

  public String getIndicatie() {
    return indicatie;
  }

  public BasePLExt getPl() {
    return pl;
  }

}
