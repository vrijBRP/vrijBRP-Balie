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

package nl.procura.gba.web.services.gba.basistabellen.gemeente;

import java.math.BigDecimal;

import nl.procura.gba.jpa.personen.db.Gem;
import nl.procura.standard.NaturalComparator;
import nl.procura.validation.Postcode;

public class Gemeente extends Gem implements Comparable<Gemeente> {

  public Gemeente() {
    setCbscode(new BigDecimal(-1L));
    setAdres("");
    setGemeente("");
    setPc("");
    setPlaats("");
  }

  @Override
  public int compareTo(Gemeente o) {
    return NaturalComparator.compareTo(getGemeente(), o.getGemeente());
  }

  public String getPostcode() {
    return Postcode.getFormat(getPc());
  }
}
