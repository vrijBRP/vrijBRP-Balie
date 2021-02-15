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

package nl.procura.gba.web.modules.bs.huwelijk.page75;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.ProDateField;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page65HuwelijkBean1 implements Serializable {

  public static final String DATUM_VOORNEMEN        = "datumVoornemen";
  public static final String DATUM_TIJD_VERBINTENIS = "datumTijdVerbintenis";

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum voornemen",
      width = "150px",
      description = "Datum voornemen")
  private Date datumVoornemen = null;

  @Field(type = FieldType.LABEL,
      caption = "Datum / tijd verbintenis",
      width = "200px")
  private String datumTijdVerbintenis = null;

  public String getDatumTijdVerbintenis() {
    return datumTijdVerbintenis;
  }

  public void setDatumTijdVerbintenis(String datumTijdVerbintenis) {
    this.datumTijdVerbintenis = datumTijdVerbintenis;
  }

  public Date getDatumVoornemen() {
    return datumVoornemen;
  }

  public void setDatumVoornemen(Date datumVoornemen) {
    this.datumVoornemen = datumVoornemen;
  }
}
