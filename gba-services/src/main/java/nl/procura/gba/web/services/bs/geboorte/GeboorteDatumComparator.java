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

package nl.procura.gba.web.services.bs.geboorte;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;

public class GeboorteDatumComparator implements Comparator<DossierPersoon> {

  private final boolean oplopend;

  public GeboorteDatumComparator(boolean oplopend) {
    this.oplopend = oplopend;
  }

  @Override
  public int compare(DossierPersoon p1, DossierPersoon p2) {

    long datum1 = p1.getDatumGeboorte().getLongDate();
    long datum2 = p2.getDatumGeboorte().getLongDate();

    long tijd1 = p1.getTijdGeboorte().getLongTime();
    long tijd2 = p2.getTijdGeboorte().getLongTime();

    if (oplopend) {
      return new CompareToBuilder().append(datum1, datum2).append(tijd1, tijd2).build();
    }

    return new CompareToBuilder().append(datum2, datum1).append(tijd2, tijd1).build();
  }
}
