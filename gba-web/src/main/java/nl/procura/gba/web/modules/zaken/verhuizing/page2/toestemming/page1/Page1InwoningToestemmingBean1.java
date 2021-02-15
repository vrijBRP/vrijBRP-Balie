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

package nl.procura.gba.web.modules.zaken.verhuizing.page2.toestemming.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.zaken.verhuizing.page2.toestemming.page1.InwoningAangifteContainer.InwoningAangifte;
import nl.procura.gba.web.modules.zaken.verhuizing.page2.toestemming.page1.InwoningToestemmingContainer.InwoningToestemming;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1InwoningToestemmingBean1 implements Serializable {

  public static final String TOESTEMMING   = "toestemming";
  public static final String AANGIFTE      = "aangifte";
  public static final String AANGIFTETEKST = "aangifteTekst";

  @Field(customTypeClass = GbaNativeSelect.class,
      width = "300px",
      caption = "Toestemming")
  @Select(containerDataSource = InwoningToestemmingContainer.class,
      nullSelectionAllowed = false)
  private InwoningToestemming toestemming = InwoningToestemming.ONBEKEND;

  @Field(type = FieldType.NATIVE_SELECT,
      width = "300px",
      caption = "Aangifte accepteren")
  @Select(containerDataSource = InwoningAangifteContainer.class,
      nullSelectionAllowed = false)
  private InwoningAangifte aangifte = InwoningAangifte.ONBEKEND;

  @Field(type = FieldType.LABEL,
      caption = "Aangifte accepteren",
      width = "300px",
      visible = false)
  private String aangifteTekst = "Ja";

  public InwoningAangifte getAangifte() {
    return aangifte;
  }

  public void setAangifte(InwoningAangifte aangifte) {
    this.aangifte = aangifte;
  }

  public String getAangifteTekst() {
    return aangifteTekst;
  }

  public void setAangifteTekst(String aangifteTekst) {
    this.aangifteTekst = aangifteTekst;
  }

  public InwoningToestemming getToestemming() {
    return toestemming;
  }

  public void setToestemming(InwoningToestemming toestemming) {
    this.toestemming = toestemming;
  }
}
