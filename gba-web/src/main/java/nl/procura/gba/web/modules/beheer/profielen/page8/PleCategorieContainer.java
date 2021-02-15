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

package nl.procura.gba.web.modules.beheer.profielen.page8;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.gba.web.services.beheer.profiel.gba_categorie.PleCategorie;

public class PleCategorieContainer {

  private GBACat       gbaCategorie = null;
  private PleCategorie pleCategorie = null;

  public PleCategorieContainer(GBACat gbaCategorie, PleCategorie pleCategorie) {

    setGbaCategorie(gbaCategorie);
    setPleCategorie(pleCategorie);
  }

  public GBACat getGbaCategorie() {
    return gbaCategorie;
  }

  public void setGbaCategorie(GBACat gbaCategorie) {
    this.gbaCategorie = gbaCategorie;
  }

  public PleCategorie getPleCategorie() {
    return pleCategorie;
  }

  public void setPleCategorie(PleCategorie pleCategorie) {
    this.pleCategorie = pleCategorie;
  }
}
