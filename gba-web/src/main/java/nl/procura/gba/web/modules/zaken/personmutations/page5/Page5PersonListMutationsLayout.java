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

package nl.procura.gba.web.modules.zaken.personmutations.page5;

import static nl.procura.gba.web.modules.zaken.personmutations.PersonListMutationsTravelDocUtils.getInfoLayout;
import static nl.procura.gba.web.modules.zaken.personmutations.overview.PersonMutationOverviewBean.CAT;
import static nl.procura.gba.web.modules.zaken.personmutations.overview.PersonMutationOverviewBean.EXPLANATION;
import static nl.procura.gba.web.modules.zaken.personmutations.overview.PersonMutationOverviewBean.OPERATION;
import static nl.procura.gba.web.modules.zaken.personmutations.overview.PersonMutationOverviewBean.PROCESS_RELATIONS;
import static nl.procura.gba.web.modules.zaken.personmutations.overview.PersonMutationOverviewBean.RECORD;
import static nl.procura.gba.web.modules.zaken.personmutations.overview.PersonMutationOverviewBean.SET;

import com.vaadin.ui.CheckBox;

import nl.procura.gba.jpa.personen.db.PlMut;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.personmutations.PersonListMutationsCheckBox;
import nl.procura.gba.web.modules.zaken.personmutations.PersonListMutationsCheckBoxType;
import nl.procura.gba.web.modules.zaken.personmutations.overview.PersonMutationOverviewForm;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page5PersonListMutationsLayout extends GbaVerticalLayout {

  private final Page5PersonListMutationsTable table;

  public Page5PersonListMutationsLayout(PlMut mutation) {
    table = new Page5PersonListMutationsTable(mutation);
    PersonMutationOverviewForm form = new PersonMutationOverviewForm(mutation,
        EXPLANATION, PROCESS_RELATIONS, CAT, RECORD, SET, OPERATION);

    addComponent(new Fieldset("Gegevens"));
    getInfoLayout(mutation.getPlMutRecs()).ifPresent(this::addComponent);
    addComponent(form);
    addComponent(new Fieldset("Overzicht elementen"));
    addComponent(getCheckbox());
    addExpandComponent(table);
  }

  private CheckBox getCheckbox() {
    return new PersonListMutationsCheckBox(
        PersonListMutationsCheckBoxType.CHANGED,
        value -> {
          table.setFilter(value);
          VaadinUtils.resetHeight(getWindow());
        });
  }

  public GbaTable getTable() {
    return table;
  }
}
