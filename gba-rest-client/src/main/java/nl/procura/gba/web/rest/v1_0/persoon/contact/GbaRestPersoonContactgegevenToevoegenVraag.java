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

package nl.procura.gba.web.rest.v1_0.persoon.contact;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "vraag")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "burgerServiceNummer", "contactgegevens" })
public class GbaRestPersoonContactgegevenToevoegenVraag {

  @XmlElement(name = "burgerServiceNummer")
  private long burgerServiceNummer = -1;

  @XmlElementWrapper(name = "contactgegevens")
  @XmlElement(name = "contactgegeven")
  private List<GbaRestPersoonContactgegeven> contactgegevens = new ArrayList<>();

  public List<GbaRestPersoonContactgegeven> getContactgegevens() {
    return contactgegevens;
  }

  public void setContactgegevens(List<GbaRestPersoonContactgegeven> contactgegevens) {
    this.contactgegevens = contactgegevens;
  }

  public long getBurgerServiceNummer() {
    return burgerServiceNummer;
  }

  public void setBurgerServiceNummer(long burgerServiceNummer) {
    this.burgerServiceNummer = burgerServiceNummer;
  }
}
