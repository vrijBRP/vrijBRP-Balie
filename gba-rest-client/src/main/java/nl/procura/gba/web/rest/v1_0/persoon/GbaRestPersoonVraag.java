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

package nl.procura.gba.web.rest.v1_0.persoon;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "vraag")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "nummers", "categorieen", "toonHistorie", "toonArchief", "toonOpgeschort" })
public class GbaRestPersoonVraag {

  @XmlElementWrapper(name = "nummers")
  @XmlElement(name = "nummer")
  private List<Long> nummers = new ArrayList<>();

  @XmlElementWrapper(name = "categorieen")
  @XmlElement(name = "categorie")
  private List<Integer> categorieen = new ArrayList<>();

  private boolean toonArchief    = true;
  private boolean toonHistorie   = true;
  private boolean toonOpgeschort = true;

  public GbaRestPersoonVraag() {
  }

  public GbaRestPersoonVraag(long nummer) {
    nummers.add(nummer);
  }

  public List<Long> getNummers() {
    return nummers;
  }

  public void setNummers(List<Long> nummers) {
    this.nummers = nummers;
  }

  public List<Integer> getCategorieen() {
    return categorieen;
  }

  public void setCategorieen(List<Integer> categorieen) {
    this.categorieen = categorieen;
  }

  public boolean isToonHistorie() {
    return toonHistorie;
  }

  public void setToonHistorie(boolean toonHistorie) {
    this.toonHistorie = toonHistorie;
  }

  public boolean isToonArchief() {
    return toonArchief;
  }

  public void setToonArchief(boolean toonArchief) {
    this.toonArchief = toonArchief;
  }

  public boolean isToonOpgeschort() {
    return toonOpgeschort;
  }

  public void setToonOpgeschort(boolean toonOpgeschort) {
    this.toonOpgeschort = toonOpgeschort;
  }
}
