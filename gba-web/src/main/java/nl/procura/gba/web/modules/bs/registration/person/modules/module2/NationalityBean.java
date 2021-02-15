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

package nl.procura.gba.web.modules.bs.registration.person.modules.module2;

import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.NatioContainer;
import nl.procura.gba.web.components.containers.RedenNationaliteitContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierNationaliteitDatumVanafType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.ProComboBox;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.field.fieldvalues.TabelFieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class NationalityBean {

  public static final String F_NATIONALITY   = "nationality";
  public static final String F_SINCE         = "since";
  public static final String F_START_DATE    = "startDate";
  public static final String F_REASON        = "reason";
  public static final String F_REASON_MANUAL = "reasonManual";
  public static final String F_SOURCE        = "source";
  public static final String F_EUR_PERSON_NR = "euPersonNumber";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Nationaliteit",
      width = "360px",
      required = true)
  @Select(containerDataSource = NatioContainer.class)
  private TabelFieldValue nationality;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Sinds",
      width = "360px",
      required = true)
  @Select(containerDataSource = NationalityTypeContainer.class)
  private DossierNationaliteitDatumVanafType since;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Reden opnemen",
      width = "500px",
      readOnly = true)
  private FieldValue reason;

  @Field(customTypeClass = ProComboBox.class,
      caption = "Reden opnemen",
      width = "360px",
      required = true)
  @Select(containerDataSource = RedenNationaliteitContainer.class)
  private FieldValue reasonManual;

  @Field(customTypeClass = GbaDateField.class,
      width = "100px",
      required = true,
      requiredError = "Veld \"Date \" is verplicht.")
  private GbaDateFieldValue startDate;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Brondocument",
      width = "360px")
  @TextField(maxLength = 40)
  private String source = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "EU-persoonsnummer",
      width = "360px")
  @TextField(maxLength = 14)
  private String euPersonNumber = "";
}
