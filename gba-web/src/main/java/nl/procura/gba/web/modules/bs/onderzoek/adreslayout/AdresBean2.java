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

package nl.procura.gba.web.modules.bs.onderzoek.adreslayout;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.actueel.PlaatsActueelContainer;
import nl.procura.gba.web.components.containers.actueel.WoonplaatsActueelContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.HuisnummerVeld;
import nl.procura.gba.web.modules.bs.common.layouts.relocation.DesignationContainer;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.field.PostalcodeField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class AdresBean2 implements Serializable {

  public static final String STRAAT   = "straat";
  public static final String HNR      = "hnr";
  public static final String HNR_L    = "hnrL";
  public static final String HNR_T    = "hnrT";
  public static final String HNR_A    = "hnrA";
  public static final String PC       = "pc";
  public static final String WPL      = "woonplaats";
  public static final String GEMEENTE = "gemeente";
  public static final String POSTBUS  = "postbus";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Adres",
      width = "200px",
      description = "Straat",
      required = true)
  @TextField(nullRepresentation = "")
  private FieldValue straat = new FieldValue();

  @Field(customTypeClass = HuisnummerVeld.class,
      caption = "Hnr",
      width = "30px",
      description = "Huisnummer",
      required = true)
  private String hnr = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "HnrL",
      description = "Huisletter",
      width = "30px")
  @TextField(nullRepresentation = "", maxLength = 1)
  private String hnrL = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "HnrT",
      description = "Toevoeging",
      width = "50px")
  @TextField(nullRepresentation = "")
  private String hnrT = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "HnrA",
      description = "Aanduiding")
  @Select(containerDataSource = DesignationContainer.class)
  private FieldValue hnrA;

  @Field(customTypeClass = PostalcodeField.class,
      caption = "Postcode",
      required = true)
  private FieldValue pc;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Woonplaats",
      width = "300px",
      required = true)
  @Select(containerDataSource = WoonplaatsActueelContainer.class)
  @Immediate
  private FieldValue woonplaats = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Gemeente",
      width = "300px",
      required = true)
  @Select(containerDataSource = PlaatsActueelContainer.class)
  @Immediate
  private FieldValue gemeente = null;

  @Field(type = Field.FieldType.LABEL,
      caption = "Adres / postbus van gemeente")
  private String postbus = "";
}
