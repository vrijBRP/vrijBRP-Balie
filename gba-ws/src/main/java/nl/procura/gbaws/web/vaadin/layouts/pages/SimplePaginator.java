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

package nl.procura.gbaws.web.vaadin.layouts.pages;

import java.io.Serializable;

/**
 * Bepaalt wat de status is van elementen in een rij.
 */
public class SimplePaginator implements Serializable {

  private int current = 0;

  public SimplePaginator() {
    doCheck();
  }

  public void onFirst() {
  }

  public void onMiddle() {
  }

  public void onLast() {
  }

  public void onNoPages() {
  }

  public void next() {

    if (current < (getMax() - 1)) {
      current++;
    }

    doCheck();
  }

  public void previous() {

    if (current > 0) {
      current--;
    }

    doCheck();
  }

  public void doCheck() {

    if (getMax() <= 1) {
      onNoPages();
    } else if (current == 0) {
      onFirst();
    } else if (current == (getMax() - 1)) {
      onLast();
    } else {
      onMiddle();
    }
  }

  public int getCurrent() {
    return current;
  }

  public void setCurrent(int current) {
    this.current = current;
  }

  public int getMax() {
    return 0;
  }
}
