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

package nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.bs.algemeen.DossierService;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.component.layout.HLayout;

public class PageBsGerelateerdeNavLayout extends HLayout {

  private DossierService       service;
  private GbaTable             table;
  private DossierPersoon       person;
  private List<DossierPersoon> persons;
  private int                  index;

  public PageBsGerelateerdeNavLayout(DossierService service,
      GbaTable table,
      DossierPersoon person,
      List<DossierPersoon> allPersons,
      int index) {

    this.service = service;
    this.table = table;
    this.person = person;
    this.index = index;
    this.persons = allPersons
        .stream()
        .filter(p -> p.getDossierPersoonType() == person.getDossierPersoonType())
        .collect(Collectors.toList());

    person.setVolgorde(new Long(index));
    UpButton upButton = new UpButton();
    DownButton downButton = new DownButton();
    upButton.setEnabled(index > 0);
    downButton.setEnabled(index < (persons.size() - 1));

    add(upButton);
    add(downButton);
  }

  private void switchOrder(DossierPersoon person1, DossierPersoon person2) {
    long p1 = person1.getVolgorde();
    long p2 = person2.getVolgorde();
    person1.setvDossPers(p2);
    person2.setvDossPers(p1);
    service.savePersoon(person1);
    service.savePersoon(person2);
  }

  public class UpButton extends Button {

    public UpButton() {
      setIcon(new ThemeResource("../gba-web/buttons/img/up.png"));
      setWidth("30px");
      addListener((ClickListener) event -> {
        switchOrder(person, persons.get(index - 1));
        table.init();
      });
    }
  }

  public class DownButton extends Button {

    public DownButton() {
      setIcon(new ThemeResource("../gba-web/buttons/img/down.png"));
      setWidth("30px");
      addListener((ClickListener) event -> {
        switchOrder(person, persons.get(index + 1));
        table.init();
      });
    }
  }
}
