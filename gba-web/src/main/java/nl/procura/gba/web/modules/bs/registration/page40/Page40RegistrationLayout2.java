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

package nl.procura.gba.web.modules.bs.registration.page40;

import java.util.List;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;

import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.relations.Relation;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.VLayout;

public class Page40RegistrationLayout2 extends VLayout {

  CheckBox                         cb1 = new CheckBox("In te schrijven personen");
  CheckBox                         cb2 = new CheckBox("Gerelateerden in de BRP");
  CheckBox                         cb3 = new CheckBox("Gerelateerden niet in de BRP");
  private Page40RegistrationTable2 table;

  public Page40RegistrationLayout2() {

    cb1.setValue(true);
    cb2.setValue(false);
    cb3.setValue(false);

    cb1.setImmediate(true);
    cb2.setImmediate(true);
    cb3.setImmediate(true);

    cb1.addListener((Button.ValueChangeListener) event -> update());
    cb2.addListener((Button.ValueChangeListener) event -> update());
    cb3.addListener((Button.ValueChangeListener) event -> update());

    table = new Page40RegistrationTable2();
    addComponent(new HLayout()
        .height("30px")
        .align(Alignment.MIDDLE_LEFT)
        .add(cb1, cb2, cb3));
    addExpandComponent(table);
  }

  public void update() {
    table.update((boolean) cb1.getValue(),
        (boolean) cb2.getValue(),
        (boolean) cb3.getValue());
  }

  public void update(List<DossierPersoon> people, List<Relation> relations) {
    table.update(people, relations,
        (boolean) cb1.getValue(),
        (boolean) cb2.getValue(),
        (boolean) cb3.getValue());
  }
}
