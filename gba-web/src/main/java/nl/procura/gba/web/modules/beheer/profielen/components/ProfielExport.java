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

package nl.procura.gba.web.modules.beheer.profielen.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.services.beheer.parameter.Parameter;
import nl.procura.gba.web.services.beheer.profiel.actie.Actie;
import nl.procura.gba.web.services.beheer.profiel.gba_categorie.PleCategorie;
import nl.procura.gba.web.services.beheer.profiel.gba_element.PleElement;
import nl.procura.gba.web.services.beheer.profiel.veld.Veld;

public class ProfielExport implements Serializable {

  private static final long serialVersionUID = -6428217692560019824L;

  private List<P> pList = new ArrayList<>();

  public P addP(String profile, String descr) {

    P p = new P(profile, descr);
    getpList().add(p);
    return p;
  }

  public List<P> getpList() {
    return pList;
  }

  public void setpList(List<P> pList) {
    this.pList = pList;
  }

  public static class P implements Serializable {

    private static final long serialVersionUID = -2484192882172776604L;

    private String                       profiel;
    private String                       oms;
    private List<String>                 usernames   = new ArrayList<>();
    private List<? extends Parameter>    parameters  = new ArrayList<>();
    private List<? extends Actie>        acties      = new ArrayList<>();
    private List<? extends Veld>         velden      = new ArrayList<>();
    private List<? extends PleElement>   elementen   = new ArrayList<>();
    private List<? extends PleCategorie> categorieen = new ArrayList<>();

    private P(String profiel, String oms) {

      this.profiel = profiel;
      this.oms = oms;
    }

    public List<? extends Actie> getActies() {
      return acties;
    }

    public void setActies(List<? extends Actie> acties) {
      this.acties = acties;
    }

    public List<? extends PleCategorie> getCategorieen() {
      return categorieen;
    }

    public void setCategorieen(List<? extends PleCategorie> categorieen) {
      this.categorieen = categorieen;
    }

    public List<? extends PleElement> getElementen() {
      return elementen;
    }

    public void setElementen(List<? extends PleElement> elementen) {
      this.elementen = elementen;
    }

    public String getOms() {
      return oms;
    }

    public void setOms(String oms) {
      this.oms = oms;
    }

    public List<? extends Parameter> getParameters() {
      return parameters;
    }

    public void setParameters(List<? extends Parameter> parameters) {
      this.parameters = parameters;
    }

    public String getProfiel() {
      return profiel;
    }

    public void setProfiel(String profiel) {
      this.profiel = profiel;
    }

    public List<String> getUsernames() {
      return usernames;
    }

    public void setUsernames(List<String> usernames) {
      this.usernames = usernames;
    }

    public List<? extends Veld> getVelden() {
      return velden;
    }

    public void setVelden(List<Veld> velden) {
      this.velden = velden;
    }
  }
}
