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

package nl.procura.gba.web.modules.account.meldingen.pages.page.stacktrace;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.applicatie.meldingen.ServiceMelding;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

public class StracktraceForm extends ReadOnlyForm {

  private static final String MELDING = "melding";
  private static final String OORZAAK = "oorzaak";

  public StracktraceForm(ServiceMelding melding) {

    setColumnWidths("60px", "");
    setOrder(MELDING, OORZAAK);

    Bean bean = new Bean();
    bean.setMelding(melding.getMelding());
    bean.setOorzaak(melding.getOorzaak());

    setBean(bean);
  }

  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Bean implements Serializable {

    @Field(type = FieldType.TEXT_FIELD,
        caption = "Melding")
    private String melding = "";

    @Field(type = FieldType.TEXT_FIELD,
        caption = "Oorzaak")
    private String oorzaak = "";

    public String getMelding() {
      return melding;
    }

    public void setMelding(String melding) {
      this.melding = melding;
    }

    public String getOorzaak() {
      return oorzaak;
    }

    public void setOorzaak(String oorzaak) {
      this.oorzaak = oorzaak;
    }
  }
}
