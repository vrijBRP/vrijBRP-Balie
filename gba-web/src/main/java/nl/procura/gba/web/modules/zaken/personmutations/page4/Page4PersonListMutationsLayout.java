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

package nl.procura.gba.web.modules.zaken.personmutations.page4;

import static nl.procura.gba.web.modules.zaken.personmutations.PersonListMutationsCheckBoxType.CHANGED;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import com.vaadin.ui.CheckBox;

import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.personmutations.AbstractPersonMutationsTable;
import nl.procura.gba.web.modules.zaken.personmutations.PersonListMutationsCheckBox;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElems;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.component.field.ProTextArea;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;

import lombok.Data;

public class Page4PersonListMutationsLayout extends GbaVerticalLayout {

  private final Form                         form;
  private final AbstractPersonMutationsTable table;

  public Page4PersonListMutationsLayout(PersonListMutElems list) {

    form = new Form();
    table = new Page4PersonMutationsTable(list);

    addComponent(new InfoLayout("Wat zijn de redenen voor deze mutatie?"));
    addComponent(form);
    addComponent(new Fieldset("Overzicht elementen"));
    addComponent(getFilter());
    addExpandComponent(table);
  }

  public GbaTable getTable() {
    return table;
  }

  public Form getForm() {
    return form;
  }

  private CheckBox getFilter() {
    return new PersonListMutationsCheckBox(CHANGED, table::setFilter);
  }

  public static class Form extends GbaForm<Bean> {

    public Form() {
      setColumnWidths("60px", "");
      setBean(new Bean());
    }
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Bean implements Serializable {

    @Field(customTypeClass = ProTextArea.class,
        caption = "Redenen",
        width = "700px")
    @TextArea(rows = 3)
    private String explanation = "";
  }

}
