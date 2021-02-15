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

package nl.procura.gba.web.modules.bs.common.pages.persoonpage;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

public class BsStatusForm extends GbaForm<BsStatusForm.Bean> {

  private static final String ZAAKTYPE = "zaakType";
  private static final String STATUS   = "status";

  public BsStatusForm(Dossier dossier) {

    setColumnWidths("60px", "", "50px", "150px");
    setOrder(ZAAKTYPE, STATUS);

    Bean b = new Bean();
    b.setZaakType(dossier.getType().getOms());
    b.setStatus(dossier.getStatus().getOms());

    setBean(b);
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Bean implements Serializable {

    @Field(type = FieldType.LABEL,
        caption = "Zaaktype")
    private String zaakType = "";

    @Field(type = FieldType.LABEL,
        caption = "Status")
    private String status = "";
  }
}
