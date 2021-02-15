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

package nl.procura.gba.web.modules.bs.common.pages.nationaliteitpage;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.NatioContainer;
import nl.procura.gba.web.components.containers.RedenNationaliteitContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierNationaliteitDatumVanafType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class BsNatioBean implements Serializable {

  public static final String NATIO_MIN      = "nationaliteitMin";
  public static final String NATIO_MAX      = "nationaliteitMax";
  public static final String SINDS          = "sinds";
  public static final String SINDS_ERK_AFST = "sindsErkenningAfstamming";
  public static final String DATUM_VANAF    = "datumVanaf";
  public static final String REDEN          = "reden";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Nationaliteit",
      required = true,
      width = "300px")
  private FieldValue nationaliteitMin = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Nationaliteit",
      required = true,
      width = "300px")
  @Select(containerDataSource = NatioContainer.class)
  private FieldValue nationaliteitMax = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Sinds",
      required = true,
      width = "300px")
  @Select(containerDataSource = BsNatioContainer.class)
  @Immediate
  private DossierNationaliteitDatumVanafType sinds = null;

  @Field(type = FieldType.LABEL,
      caption = "Sinds",
      required = true,
      width = "300px")
  private DossierNationaliteitDatumVanafType sindsErkenningAfstamming = DossierNationaliteitDatumVanafType.GEBOORTE_DATUM;

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum vanaf",
      width = "80px",
      required = true,
      visible = false)
  private DateFieldValue datumVanaf = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Reden",
      required = true,
      visible = false)
  @Select(containerDataSource = RedenNationaliteitContainer.class)
  private FieldValue reden = null;
}
