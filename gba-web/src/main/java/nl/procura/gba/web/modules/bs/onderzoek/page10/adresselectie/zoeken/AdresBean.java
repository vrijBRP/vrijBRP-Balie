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

package nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.zoeken;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.StraatContainer;
import nl.procura.gba.web.components.fields.GbaBsnField;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.HuisnummerVeld;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.PostalcodeField;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class AdresBean implements Serializable {

  public static final String BSN    = "bsn";
  public static final String STRAAT = "straat";
  public static final String HNR    = "hnr";
  public static final String HNRL   = "hnrL";
  public static final String HNRT   = "hnrT";
  public static final String PC     = "pc";

  @Field(customTypeClass = GbaBsnField.class,
      caption = "BSN",
      width = "200px")
  private BsnFieldValue bsn = new BsnFieldValue();

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Straat",
      width = "200px")
  @Select(containerDataSource = StraatContainer.class)
  private FieldValue straat = new FieldValue();

  @Field(customTypeClass = HuisnummerVeld.class,
      caption = "Huisnummer",
      width = "30px")
  @Immediate()
  private String hnr = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Huisletter",
      width = "30px",
      writeThrough = true)
  @Immediate()
  private String hnrL = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Toevoeging",
      width = "50px")
  private String hnrT = "";

  @Field(customTypeClass = PostalcodeField.class,
      caption = "Postcode")
  private FieldValue pc = new FieldValue();
}
