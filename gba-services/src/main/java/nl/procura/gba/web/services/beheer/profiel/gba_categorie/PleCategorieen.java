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

package nl.procura.gba.web.services.beheer.profiel.gba_categorie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PleCategorieen implements Serializable {

  private static final long serialVersionUID = -8859107592476594796L;

  private List<PleCategorie> all = new ArrayList<>();

  public PleCategorieen() {
  }

  public PleCategorieen(List<PleCategorie> all) {
    setAll(all);
  }

  public List<PleCategorie> getAlle() {
    return all;
  }

  public PleCategorie getPleCategorie(PleCategorie pleCategorie) {
    for (PleCategorie pE : getAlle()) {
      if (pE.equals(pleCategorie)) {
        return pE;
      }
    }

    return null;
  }

  public void setAll(List<PleCategorie> all) {
    this.all = all;
  }

  @Override
  public String toString() {
    return "GbaCategorieenImpl [all=" + all + "]";
  }
}
