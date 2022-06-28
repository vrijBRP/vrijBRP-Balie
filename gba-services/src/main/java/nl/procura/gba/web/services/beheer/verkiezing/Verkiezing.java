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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.jpa.personen.db.KiesrVerk;
import nl.procura.standard.ProcuraDate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Verkiezing {

  private List<Stempas> stempassen = new ArrayList<>();
  private KiesrVerk     verk;

  public Verkiezing() {
  }

  public Verkiezing(KiesrVerk verk) {
    this.verk = verk;
  }

  @Override
  public String toString() {
    return MessageFormat.format("{0} - {1}",
        new ProcuraDate(verk.getdVerk()).getFormatDate(),
        verk.getVerkiezing());
  }

  public KiesrVerk getVerk() {
    return verk;
  }

  public List<Stempas> getStempassen() {
    return stempassen;
  }

  public void setStempassen(List<Stempas> stempassen) {
    this.stempassen = stempassen;
    this.stempassen.forEach(stempas -> stempas.setVerkiezing(this));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Verkiezing)) return false;
    Verkiezing that = (Verkiezing) o;
    return new EqualsBuilder().append(verk.getcKiesrVerk(), that.verk.getcKiesrVerk()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(verk.getcKiesrVerk()).toHashCode();
  }
}
