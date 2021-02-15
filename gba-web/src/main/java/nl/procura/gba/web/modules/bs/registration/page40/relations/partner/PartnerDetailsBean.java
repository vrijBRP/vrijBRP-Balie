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

package nl.procura.gba.web.modules.bs.registration.page40.relations.partner;

import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.containers.PlaatsContainer;
import nl.procura.gba.web.components.containers.RedenHuwelijkOntbindingContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.modules.bs.registration.page40.relations.CommitmentTypeContainer;
import nl.procura.gba.web.services.bs.registration.CommitmentType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class PartnerDetailsBean {

  public static final String F_START_DATE                 = "startDate";
  public static final String F_START_COUNTRY              = "startCountry";
  public static final String F_START_MUNICIPALITY         = "startMunicipality";
  public static final String F_START_FOREIGN_MUNICIPALITY = "startForeignMunicipality";
  public static final String F_COMMITMENT_TYPE            = "commitmentType";
  public static final String F_END                        = "end";
  public static final String F_END_DATE                   = "endDate";
  public static final String F_END_COUNTRY                = "endCountry";
  public static final String F_END_MUNICIPALITY           = "endMunicipality";
  public static final String F_END_FOREIGN_MUNICIPALITY   = "endForeignMunicipality";
  public static final String F_END_REASON                 = "endReason";

  @Field(customTypeClass = GbaDateField.class,
      caption = "Datum verbintenis",
      width = "80px",
      required = true)
  private GbaDateFieldValue startDate;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Land verbintenis",
      required = true,
      width = "230px")
  @Select(containerDataSource = LandContainer.class)
  private FieldValue startCountry;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Plaats verbintenis",
      required = true,
      width = "230px")
  @Select(containerDataSource = PlaatsContainer.class)
  private FieldValue startMunicipality;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Plaats verbintenis",
      required = true,
      width = "230px")
  @TextField(nullRepresentation = "")
  private String startForeignMunicipality;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Soort verbintenis",
      width = "230px",
      required = true)
  @Select(containerDataSource = CommitmentTypeContainer.class)
  private CommitmentType commitmentType;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Ontbonden",
      required = true)
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE,
      nullSelectionAllowed = false)
  @Immediate
  private Boolean end = false;

  @Field(customTypeClass = GbaDateField.class,
      caption = "Op datum",
      required = true,
      width = "80px")
  private GbaDateFieldValue endDate;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Land ontbinding",
      required = true,
      width = "238px")
  @Select(containerDataSource = LandContainer.class)
  private FieldValue endCountry;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Plaats ontbinding",
      required = true,
      width = "238px")
  @Select(containerDataSource = PlaatsContainer.class)
  private FieldValue endMunicipality;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Plaats ontbinding",
      required = true,
      width = "238px")
  @TextField(nullRepresentation = "")
  private String endForeignMunicipality;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Reden ontbinding",
      required = true,
      width = "238px")
  @Select(containerDataSource = RedenHuwelijkOntbindingContainer.class)
  private FieldValue endReason;

}
