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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.address.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.StraatContainer;
import nl.procura.gba.web.components.containers.actueel.WoonplaatsActueelContainer;
import nl.procura.gba.web.components.fields.BagPopupField;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.HuisnummerVeld;
import nl.procura.gba.web.modules.bs.common.layouts.relocation.AddressSourceTypeContainer;
import nl.procura.gba.web.modules.bs.common.layouts.relocation.DesignationContainer;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.gba.web.services.interfaces.address.AddressSourceType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.PostalcodeField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1QuickSearchBean implements Serializable {

  public static final String F_SOURCE         = "source";
  public static final String F_BAG_ADDRESS    = "bagAddress";
  public static final String F_STREET         = "street";
  public static final String F_HOUSE_NUMBER   = "houseNumber";
  public static final String F_HOUSE_NUMBER_L = "houseNumberL";
  public static final String F_HOUSE_NUMBER_T = "houseNumberT";
  public static final String F_HOUSE_NUMBER_A = "houseNumberA";
  public static final String F_POSTAL_CODE    = "postalCode";
  public static final String F_RESIDENCE      = "residence";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Bron gegevens",
      width = "200px",
      required = true,
      description = "Bron van de verblijfsobjectgegevens")
  @Select(containerDataSource = AddressSourceTypeContainer.class,
      nullSelectionAllowed = false)
  private AddressSourceType source;

  @Field(customTypeClass = BagPopupField.class,
      caption = "Adres",
      description = "Adres",
      width = "436px",
      required = true,
      writeThrough = true)
  @Immediate()
  private Address bagAddress = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Adres",
      width = "200px",
      description = "Straat")
  @Select(containerDataSource = StraatContainer.class)
  private FieldValue street = new FieldValue();

  @Field(customTypeClass = HuisnummerVeld.class,
      caption = "Hnr",
      width = "50px",
      description = "Huisnummer")
  private String houseNumber = "";

  @Field(customTypeClass = GbaTextField.class,
      description = "Huisletter",
      width = "30px")
  @TextField(nullRepresentation = "", maxLength = 1)
  private String houseNumberL = "";

  @Field(customTypeClass = GbaTextField.class,
      description = "Toevoeging",
      width = "50px")
  @TextField(nullRepresentation = "")
  private String houseNumberT = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      description = "Aanduiding")
  @Select(containerDataSource = DesignationContainer.class)
  private FieldValue houseNumberA = new FieldValue();

  @Field(customTypeClass = PostalcodeField.class,
      caption = "Postcode / woonplaats",
      width = "120px")
  private FieldValue postalCode = new FieldValue();

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Woonplaats",
      width = "312px")
  @Select(containerDataSource = WoonplaatsActueelContainer.class,
      nullSelectionAllowed = false)
  @Immediate
  private FieldValue residence;

  public Page1QuickSearchBean() {
  }

  public AddressSourceType getSource() {
    return source == null ? AddressSourceType.UNKNOWN : source;
  }
}
