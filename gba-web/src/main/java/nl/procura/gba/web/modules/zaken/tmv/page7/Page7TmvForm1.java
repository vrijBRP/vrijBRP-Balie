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

package nl.procura.gba.web.modules.zaken.tmv.page7;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.TextArea;

import lombok.Data;

public class Page7TmvForm1 extends GbaForm<Page7TmvForm1.Bean> {

  private static final String MELDING = "melding";

  public Page7TmvForm1(TerugmeldingAanvraag tmv) {

    setCaption("Overzicht reacties");
    setColumnWidths(WIDTH_130, "");
    setOrder(MELDING);
    setReadonlyAsText(false);

    Bean b = new Bean();
    b.setMelding(tmv.getTerugmelding());
    setBean(b);
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Bean implements Serializable {

    @Field(type = FieldType.TEXT_AREA,
        caption = "Melding",
        width = "500px",
        readOnly = true)
    @TextArea(rows = 2)
    private String melding = "";
  }
}
