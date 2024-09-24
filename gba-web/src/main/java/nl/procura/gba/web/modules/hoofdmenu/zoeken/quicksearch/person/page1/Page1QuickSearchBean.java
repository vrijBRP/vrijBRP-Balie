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

import nl.procura.gba.web.components.fields.BagPopupField;
import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.HuisnummerVeld;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.component.field.AnrField;
import nl.procura.vaadin.component.field.BsnField;
import nl.procura.vaadin.component.field.PostalcodeField;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1QuickSearchBean implements Serializable {

  public static final String F_BSN           = "bsn";
  public static final String F_ANR           = "anr";
  public static final String F_GEBOORTEDATUM = "geboortedatum";
  public static final String F_GESLACHTSNAAM = "geslachtsnaam";
  public static final String F_POSTCODE      = "postcode";
  public static final String F_HNR           = "hnr";
  public static final String F_ADRES         = "adres";

  @Field(customTypeClass = BsnField.class,
      caption = "Burgerservicenummer",
      width = "150px")
  @Immediate
  private BsnFieldValue bsn = null;

  @Field(customTypeClass = AnrField.class,
      caption = "A-nummer",
      width = "150px")
  private AnrFieldValue anr = null;

  @Field(customTypeClass = GbaDateField.class,
      caption = "Geboortedatum",
      width = "150px")
  private GbaDateFieldValue geboortedatum = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geslachtsnaam",
      width = "150px")
  private String geslachtsnaam = "";

  @Field(customTypeClass = PostalcodeField.class,
      caption = "Postcode",
      width = "150px")
  private FieldValue postcode = new FieldValue();

  @Field(customTypeClass = HuisnummerVeld.class,
      caption = "Huisnummer",
      width = "50px",
      description = "Huisnummer")
  private String hnr = "";

  @Field(customTypeClass = BagPopupField.class,
      caption = "BAG-adres",
      description = "Adres",
      width = "482px")
  @Immediate
  private Address adres = null;

  public Page1QuickSearchBean() {
  }
}
