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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.component.field.AnrField;
import nl.procura.vaadin.component.field.BsnField;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1QuickSearchBean implements Serializable {

  public static final String BSN           = "bsn";
  public static final String ANR           = "anr";
  public static final String GEBOORTEDATUM = "geboortedatum";
  public static final String GESLACHTSNAAM = "geslachtsnaam";

  @Field(customTypeClass = BsnField.class,
      caption = "Burgerservicenummer")
  @Immediate
  private BsnFieldValue bsn = null;

  @Field(customTypeClass = AnrField.class,
      caption = "A-nummer")
  private AnrFieldValue anr = null;

  @Field(customTypeClass = GbaDateField.class,
      caption = "Geboortedatum")
  private GbaDateFieldValue geboortedatum = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geslachtsnaam")
  private String geslachtsnaam = "";

  public Page1QuickSearchBean() {
  }
}
