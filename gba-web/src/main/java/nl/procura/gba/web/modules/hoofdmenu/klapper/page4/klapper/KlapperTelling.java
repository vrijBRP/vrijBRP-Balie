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

package nl.procura.gba.web.modules.hoofdmenu.klapper.page4.klapper;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;

public class KlapperTelling {

  private String            titel    = "";
  private String            gemeente = "";
  private String            periode  = "";
  private String            volgorde = "";
  private List<DossierAkte> aktes    = new ArrayList<>();

  public KlapperTelling() {
  }

  public KlapperTelling(List<DossierAkte> aktes) {
    setAktes(aktes);
  }

  public List<DossierAkte> getAktes() {
    return aktes;
  }

  public void setAktes(List<DossierAkte> aktes) {
    this.aktes = aktes;
  }

  public String getGemeente() {
    return gemeente;
  }

  public void setGemeente(String gemeente) {
    this.gemeente = gemeente;
  }

  public String getPeriode() {
    return periode;
  }

  public void setPeriode(String periode) {
    this.periode = periode;
  }

  public String getTitel() {
    return titel;
  }

  public void setTitel(String titel) {
    this.titel = titel;
  }

  public String getVolgorde() {
    return volgorde;
  }

  public void setVolgorde(String volgorde) {
    this.volgorde = volgorde;
  }
}
