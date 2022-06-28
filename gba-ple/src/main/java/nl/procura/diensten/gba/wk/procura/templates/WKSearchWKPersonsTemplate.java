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

package nl.procura.diensten.gba.wk.procura.templates;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import nl.procura.gba.jpa.probev.db.Vbo;
import nl.procura.gba.jpa.probev.db.VboKrt;

public class WKSearchWKPersonsTemplate extends WKTemplateProcura {

  private Vbo          object  = new Vbo();
  private List<VboKrt> persons = new ArrayList<>();

  @Override
  public void parse() {
    setPersons(getBewon());
  }

  @SuppressWarnings("unchecked")
  private List<VboKrt> getBewon() {
    setSql("select b from VboKrt b where b.cVbo = :c_vbo");
    addProp("c_vbo", getObject().getId().getCVbo());

    if (getProps().isEmpty()) {
      return new ArrayList<>();
    }

    Query q = getEntityManager().createQuery(getSql());
    Iterator<?> it = getProps().keySet().iterator();

    while (it.hasNext()) {
      String key = (String) it.next();
      q.setParameter(key, getProps().get(key));
    }

    return (List<VboKrt>) q.getResultList();
  }

  public Vbo getObject() {
    return object;
  }

  public void setObject(Vbo object) {
    this.object = object;
  }

  public List<VboKrt> getPersons() {
    return persons;
  }

  public void setPersons(List<VboKrt> persons) {
    this.persons = persons;
  }
}
