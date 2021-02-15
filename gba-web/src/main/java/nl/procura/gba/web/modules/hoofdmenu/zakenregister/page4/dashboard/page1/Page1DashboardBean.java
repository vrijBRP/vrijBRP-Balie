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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.dashboard.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.commons.core.utils.ProPlanningUtils.PlanningPeriode;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.dashboard.containers.PlanningJaarContainer;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.dashboard.containers.PlanningKwartaalContainer;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.dashboard.containers.PlanningMaandContainer;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.dashboard.containers.PlanningPeriodeContainer;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.dashboard.periodes.DashboardPeriode;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1DashboardBean implements Serializable {

  public static final String PERIODE      = "periode";
  public static final String OMSCHRIJVING = "omschrijving";
  public static final String JAAR         = "jaar";
  public static final String KWARTAAL     = "kwartaal";
  public static final String MAAND        = "maand";
  public static final String DAG          = "dag";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Periode van invoer",
      width = "150px",
      required = true)
  @Select(containerDataSource = PlanningPeriodeContainer.class)
  @Immediate()
  private PlanningPeriode periode = null;

  @Field(type = FieldType.LABEL,
      caption = "Selectieperiode",
      width = "150px")
  private String omschrijving = "-";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Jaar",
      width = "150px",
      required = true,
      visible = false)
  @Select(containerDataSource = PlanningJaarContainer.class,
      nullSelectionAllowed = false)
  private DashboardPeriode jaar = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Maand",
      width = "150px",
      required = true,
      visible = false)
  @Select(containerDataSource = PlanningMaandContainer.class,
      nullSelectionAllowed = false)
  private DashboardPeriode maand = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Kwartaal",
      width = "150px",
      required = true,
      visible = false)
  @Select(containerDataSource = PlanningKwartaalContainer.class,
      nullSelectionAllowed = false)
  private DashboardPeriode kwartaal = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "Dag",
      width = "150px",
      required = true,
      visible = false)
  private DateFieldValue dag = null;
}
