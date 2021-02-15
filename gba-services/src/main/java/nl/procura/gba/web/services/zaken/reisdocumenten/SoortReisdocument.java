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

package nl.procura.gba.web.services.zaken.reisdocumenten;

import static nl.procura.standard.Globalfunctions.aval;

import nl.procura.gba.jpa.personen.db.BaseEntity;
import nl.procura.gba.jpa.personen.db.Reisdoc;

public class SoortReisdocument extends Reisdoc {

  private static final long serialVersionUID = -5988434237258231833L;

  public SoortReisdocument() {
  }

  public static SoortReisdocument getDefault() {
    SoortReisdocument g = new SoortReisdocument();
    g.setCReisdoc(BaseEntity.DEFAULT);
    return g;
  }

  @Override
  public boolean equals(Object other) {
    // self check
    if (this == other) {
      return true;
    }
    // null check
    if (other == null) {
      return false;
    }
    // type check and cast
    if (!(other instanceof SoortReisdocument)) {
      return false;
    }
    return (other != null) && ((SoortReisdocument) other).getCReisdoc().equals(getCReisdoc());
  }

  @Override
  public int hashCode() {
    return aval(getCReisdoc());
  }

  @Override
  public String toString() {
    return getZkarg() + " (" + getReisdoc() + ")";
  }
}
