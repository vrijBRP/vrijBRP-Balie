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

package nl.procura.gba.web.services.beheer.profiel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.services.beheer.profiel.actie.Actie;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActieType;
import nl.procura.gba.web.services.beheer.profiel.gba_categorie.PleCategorie;
import nl.procura.gba.web.services.beheer.profiel.gba_element.PleElement;
import nl.procura.gba.web.services.beheer.profiel.veld.ProfielVeld;
import nl.procura.gba.web.services.beheer.profiel.veld.ProfielVeldType;
import nl.procura.gba.web.services.beheer.profiel.veld.Veld;

public class Profielen implements Serializable {

  private static final long serialVersionUID = 7931046575439646623L;

  private List<Profiel> alle = new ArrayList<>();

  public List<Profiel> getAlle() {
    return alle;
  }

  public void setAlle(ArrayList<Profiel> alle) {
    this.alle = alle;
  }

  public boolean isActie(Actie actie) {
    return getAlle().stream().anyMatch(p -> p.getActies().getActie(actie) != null);
  }

  public boolean isGbaCategorie(PleCategorie pleCategorie) {
    return getAlle().stream().anyMatch(p -> p.getCategorieen().getPleCategorie(pleCategorie) != null);
  }

  public boolean isGbaElement(PleElement pleElement) {
    return getAlle().stream().anyMatch(p -> p.getElementen().getPleElement(pleElement) != null);
  }

  public boolean isProfielActie(ProfielActie profielActie) {
    return (profielActie == ProfielActie.ONBEKEND) || isActie(profielActie.getActie());
  }

  public boolean isProfielActie(ProfielActieType type, ProfielActie profielActie) {
    return isProfielActie(ProfielActie.getActie(type, profielActie));
  }

  public boolean isProfielVeld(ProfielVeld profielVeld) {
    return (profielVeld == ProfielVeld.ONBEKEND) || isVeld(profielVeld.getVeld());
  }

  public boolean isProfielVeld(ProfielVeldType type, ProfielVeld profielVeld) {
    return isProfielVeld(ProfielVeld.getVeld(type, profielVeld));
  }

  public boolean isVeld(Veld veld) {
    return getAlle().stream().anyMatch(p -> p.getVelden().getVeld(veld) != null);
  }

  @Override
  public String toString() {
    return "ProfielenImpl [all=" + alle + "]";
  }
}
