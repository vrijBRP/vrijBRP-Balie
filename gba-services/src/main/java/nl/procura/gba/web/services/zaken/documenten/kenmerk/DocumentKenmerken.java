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

package nl.procura.gba.web.services.zaken.documenten.kenmerk;

import java.util.ArrayList;
import java.util.List;

public class DocumentKenmerken {

  private List<DocumentKenmerk> alle = new ArrayList<>();

  public DocumentKenmerken() {
  }

  public DocumentKenmerken(List<DocumentKenmerk> alle) {
    this.alle = alle;
  }

  public DocumentKenmerk get(DocumentKenmerkType type) {
    for (DocumentKenmerk km : getAlle()) {
      if (km.getKenmerkType() == type) {
        return km;
      }
    }

    return null;
  }

  public List<DocumentKenmerk> getAlle() {
    return alle;
  }

  public boolean is(DocumentKenmerkType... types) {
    for (DocumentKenmerkType type : types) {
      if (get(type) != null) {
        return true;
      }
    }
    return false;
  }

  public void setAll(List<DocumentKenmerk> alle) {
    this.alle = alle;
  }

  public String toString() {
    return "KoppelElementenImpl [all=" + alle + "]";
  }
}
