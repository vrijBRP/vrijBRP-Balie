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

package nl.procura.gba.web.modules.beheer.overig;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import com.vaadin.data.Container;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

public class KoppelForm extends ReadOnlyForm {

  private static final String AANTAL = "aantal";

  public KoppelForm() {

    setColumnWidths("110px", "");

    setOrder(AANTAL);

    setAantal("");
  }

  public void check(GbaTable table) {

    int wel = 0;

    Container container = table.getContainerDataSource();

    for (Object item : container.getItemIds()) {

      for (Object pId : container.getItem(item).getItemPropertyIds()) {

        Object pItem = container.getItem(item).getItemProperty(pId);

        if (pItem.toString().contains("Gekoppeld") || pItem.toString().contains("Wel historie")) {

          wel++;
        }
      }
    }

    setAantal(wel, container.getItemIds().size());
  }

  public void setAantal(String aantal) {

    Bean b = new Bean();

    b.setAantal(aantal);

    setBean(b);
  }

  private void setAantal(int wel, int totaal) {

    if (wel == 0) {
      setAantal("Geen");
    } else if (wel == totaal) {

      if (wel == 1) {
        setAantal("1 gekoppeld");
      } else {
        setAantal("Alle " + totaal + " gekoppeld");
      }
    } else {
      setAantal(wel + " wel / " + (totaal - wel) + " niet gekoppeld");
    }
  }

  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Bean implements Serializable {

    @Field(type = FieldType.LABEL,
        caption = "Aantal gekoppeld")
    private String aantal = "";

    public Bean() {
    }

    public String getAantal() {
      return aantal;
    }

    public void setAantal(String aantal) {
      this.aantal = aantal;
    }
  }
}
