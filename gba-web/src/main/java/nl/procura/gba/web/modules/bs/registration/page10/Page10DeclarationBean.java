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

package nl.procura.gba.web.modules.bs.registration.page10;

import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.containers.StayDurationContainer;
import nl.procura.gba.web.components.fields.CountryBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.bs.registration.OriginSituationType;
import nl.procura.gba.web.services.bs.registration.StaydurationType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page10DeclarationBean {

  public static final String F_DATUM_AANGIFTE   = "datumAangifte";
  public static final String F_COUNTRY          = "country";
  public static final String F_STAY_DURATION    = "stayDuration";
  public static final String F_ORIGIN_SITUATION = "originSituation";

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum aangifte",
      width = "97px",
      required = true)
  private Date datumAangifte;

  @Field(customTypeClass = CountryBox.class,
      caption = "Herkomstsituatie",
      width = "430px",
      required = true)
  @Select(containerDataSource = CountryOfOriginContainer.class,
      nullSelectionAllowed = false)
  private OriginSituationType originSituation;

  @Field(customTypeClass = CountryBox.class,
      caption = "Land van herkomst",
      width = "430px",
      required = true,
      visible = false)
  private FieldValue country;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Vermoedelijk verblijfsduur voor de komende 6 maanden",
      required = true,
      width = "430px")
  @Select(containerDataSource = StayDurationContainer.class,
      nullSelectionAllowed = false)
  @Immediate
  private StaydurationType stayDuration;
}
