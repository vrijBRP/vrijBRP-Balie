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

package nl.procura.gba.web.modules.zaken.rijbewijs.page4.page4b;

import nl.procura.gba.web.modules.zaken.rijbewijs.RijbewijsPage;
import nl.procura.rdw.processen.p0252.f07.UITGMAATRGEG;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Uitsplitsing van de maatregel
 */
public class Page4bRijbewijs extends RijbewijsPage {

  private final UITGMAATRGEG c;

  public Page4bRijbewijs(UITGMAATRGEG c) {

    super("Maatregelen inzien");

    this.c = c;

    setMargin(true);

    addButton(buttonPrev);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addComponent(new Page4bRijbewijsForm1(c));
      addComponent(new Page4bRijbewijsForm2(c));
      addComponent(new Page4bRijbewijsForm3(c));
      addComponent(new Page4bRijbewijsForm4(c));

      addComponent(new Fieldset("Ongeldigverklaring categorieën", new Page4bRijbewijsTable1(c)));
      addComponent(new Fieldset("Inhoudingsperiode gegevens", new Page4bRijbewijsTable2(c)));
      addComponent(new Fieldset("Ontzeggingsperiode gegevens", new Page4bRijbewijsTable3(c)));
      addComponent(new Fieldset("Innemingsperiode gegevens", new Page4bRijbewijsTable4(c)));
    }

    super.event(event);
  }
}
