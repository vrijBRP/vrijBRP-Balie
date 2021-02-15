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

package nl.procura.gba.web.modules.beheer.aktes.page3;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteRegistersoort;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.NumberField;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2AktesBean2 implements Serializable {

  public static final String SOORT        = "soort";
  public static final String CODE         = "code";
  public static final String OMSCHRIJVING = "omschrijving";
  public static final String MIN          = "min";
  public static final String MAX          = "max";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Registersoort",
      required = true)
  @Select(containerDataSource = AkteRegistersoortContainer.class)
  private DossierAkteRegistersoort soort = DossierAkteRegistersoort.AKTE_ONBEKEND;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Code",
      required = true,
      width = "30px")
  @TextField(maxLength = 1)
  private String code = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Omschrijving",
      required = true)
  private String omschrijving = "";

  @Field(customTypeClass = NumberField.class,
      caption = "Min.",
      required = true,
      width = "50px")
  @TextField(maxLength = 4)
  private String min = "";

  @Field(customTypeClass = NumberField.class,
      caption = "Max.",
      required = true,
      width = "50px")
  @TextField(maxLength = 4)
  private String max = "";

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMax() {
    return max;
  }

  public void setMax(String max) {
    this.max = max;
  }

  public String getMin() {
    return min;
  }

  public void setMin(String min) {
    this.min = min;
  }

  public String getOmschrijving() {
    return omschrijving;
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving = omschrijving;
  }

  public DossierAkteRegistersoort getSoort() {
    return soort;
  }

  public void setSoort(DossierAkteRegistersoort soort) {
    this.soort = soort;
  }
}
