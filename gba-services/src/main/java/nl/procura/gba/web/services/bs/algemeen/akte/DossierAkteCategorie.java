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

package nl.procura.gba.web.services.bs.algemeen.akte;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.jpa.personen.db.DossAkteRdCat;

public class DossierAkteCategorie extends DossAkteRdCat {

  private static final long serialVersionUID = 1606681095407830651L;

  private List<DossierAkteDeel> akteDelen = new ArrayList<>();

  public DossierAkteCategorie() {
    setCategorie("");
  }

  public List<DossierAkteDeel> getAkteDelen() {
    return akteDelen;
  }

  public void setAkteDelen(List<DossierAkteDeel> akteDelen) {
    this.akteDelen = akteDelen;
  }

  public String getCategorie() {
    return getDossAkteRdCat();
  }

  public void setCategorie(String categorie) {
    setDossAkteRdCat(categorie);
  }

  public Long getCode() {
    return getCDossAkteRdCat();
  }

  @Override
  public String toString() {
    return getCategorie();
  }
}
