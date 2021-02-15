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

package nl.procura.diensten.gba.ple.procura.utils;

import java.util.*;

import nl.procura.diensten.gba.ple.procura.templates.PLETemplateProcura;

public class PLStore {

  private Map<Long, PL> ps = new HashMap<>();

  public PL newPL(SortableObject so) {

    PL pl = new PL();
    getPs().put(so.getAnr(), pl);

    return pl;
  }

  public PL getP(SortableObject so) {
    return getPs().get(so.getAnr());
  }

  public void sort() {
    Iterator<Long> it = getPs().keySet().iterator();

    while (it.hasNext()) {
      long anr = it.next();
      PL p = getPs().get(anr);
      for (Cat c : p.getCats()) {
        Collections.sort(c.getObjects());
      }
    }
  }

  public Map<Long, PL> getPs() {
    return ps;
  }

  public void setPs(Map<Long, PL> ps) {
    this.ps = ps;
  }

  public static class Cat {

    private int                  nr      = 0;
    private List<SortableObject> objects = new ArrayList<>();
    private PLETemplateProcura   template;

    public Cat(int nr, PLETemplateProcura template) {

      setNr(nr);
      setTemplate(template);
    }

    public int getNr() {
      return nr;
    }

    public void setNr(int nr) {
      this.nr = nr;
    }

    public List<SortableObject> getObjects() {
      return objects;
    }

    public void setObjects(List<SortableObject> objects) {
      this.objects = objects;
    }

    public PLETemplateProcura getTemplate() {
      return template;
    }

    public void setTemplate(PLETemplateProcura template) {
      this.template = template;
    }
  }

  public class PL {

    private List<Cat> cats = new ArrayList<>();

    public void addCat(int i, SortableObject so, PLETemplateProcura template) {
      Cat c = getCat(i);
      if (c == null) {
        c = new Cat(i, template);
        getCats().add(c);
      }

      c.getObjects().add(so);
    }

    public Cat getCat(int i) {
      return getCats().stream().filter(c -> c.getNr() == i)
          .findFirst().orElse(null);
    }

    public List<Cat> getCats() {
      return cats;
    }

    public void setCats(List<Cat> cats) {
      this.cats = cats;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + getOuterType().hashCode();
      result = prime * result + ((cats == null) ? 0 : cats.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {

      if (this == obj) {
        return true;
      }

      if (obj == null) {
        return false;
      }

      if (getClass() != obj.getClass()) {
        return false;
      }

      PL other = (PL) obj;
      if (!getOuterType().equals(other.getOuterType())) {
        return false;
      }

      if (cats == null) {
        return other.cats == null;
      } else
        return cats.equals(other.cats);

    }

    private PLStore getOuterType() {
      return PLStore.this;
    }
  }
}
