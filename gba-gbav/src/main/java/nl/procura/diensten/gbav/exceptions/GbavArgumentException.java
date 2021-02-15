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

package nl.procura.diensten.gbav.exceptions;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;

public class GbavArgumentException extends RuntimeException {

  private static final long serialVersionUID = -1127457222532496267L;

  GBACat  categorie = GBACat.UNKNOWN;
  GBAElem element   = GBAElem.ONBEKEND;
  String  name      = "";

  public GbavArgumentException(GBACat categorie, GBAElem element, String name) {

    setCategorie(categorie);
    setElement(element);
    setName(name);
  }

  @Override
  public String getMessage() {
    return "AUTORISATIE|" + getCategorie() + "|" + getElement() + "|" + getName();
  }

  public GBACat getCategorie() {
    return categorie;
  }

  public void setCategorie(GBACat categorie) {
    this.categorie = categorie;
  }

  public GBAElem getElement() {
    return element;
  }

  public void setElement(GBAElem element) {
    this.element = element;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
