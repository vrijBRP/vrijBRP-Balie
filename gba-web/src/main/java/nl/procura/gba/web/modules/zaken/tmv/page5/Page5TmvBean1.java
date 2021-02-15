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

package nl.procura.gba.web.modules.zaken.tmv.page5;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.modules.zaken.tmv.objects.TmvZaakStatusContainer;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page5TmvBean1 implements Serializable {

  public static final String MELDING           = "melding";
  public static final String VERANTWOORDELIJKE = "verantwoordelijke";
  public static final String RAPPELDATUM       = "rappeldatum";
  public static final String RESULTAAT         = "resultaat";
  public static final String STATUS            = "status";

  @Field(type = FieldType.TEXT_AREA,
      caption = "Melding",
      readOnly = true,
      width = "500px")
  @TextArea(rows = 2)
  private String         melding           = "";
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Verantwoordelijke")
  @Select(nullSelectionAllowed = false)
  private UsrFieldValue  verantwoordelijke = null;
  @Field(customTypeClass = DatumVeld.class,
      caption = "Rappeldatum")
  private DateFieldValue rappeldatum       = new DateFieldValue();
  @Field(type = FieldType.TEXT_AREA,
      caption = "Resultaat",
      width = "500px")
  @TextArea(rows = 2)
  private String         resultaat         = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Status",
      required = true)
  @Select(containerDataSource = TmvZaakStatusContainer.class)
  private ZaakStatusType status = null;

  public String getMelding() {
    return melding;
  }

  public void setMelding(String melding) {
    this.melding = melding;
  }

  public DateFieldValue getRappeldatum() {
    return rappeldatum;
  }

  public void setRappeldatum(DateFieldValue rappeldatum) {
    this.rappeldatum = rappeldatum;
  }

  public String getResultaat() {
    return resultaat;
  }

  public void setResultaat(String resultaat) {
    this.resultaat = resultaat;
  }

  public ZaakStatusType getStatus() {
    return status;
  }

  public void setStatus(ZaakStatusType status) {
    this.status = status;
  }

  public UsrFieldValue getVerantwoordelijke() {
    return verantwoordelijke;
  }

  public void setVerantwoordelijke(UsrFieldValue verantwoordelijke) {
    this.verantwoordelijke = verantwoordelijke;
  }
}
