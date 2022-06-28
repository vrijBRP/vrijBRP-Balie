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

package nl.procura.gba.web.modules.zaken.protocol.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.common.misc.ZaakPeriode;
import nl.procura.gba.web.components.containers.ProtocolleringGroepContainer;
import nl.procura.gba.web.components.fields.BsnAnrVeld;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.telling.ZaakPeriodes;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.zoeken.ZaakHistorischePeriodesContainer;
import nl.procura.gba.web.services.zaken.protocol.ProtocolleringGroep;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1ProtocolleringBean implements Serializable {

  public static final String GEBRUIKER = "gebruiker";
  public static final String ANR       = "anr";
  public static final String PERIODE   = "periode";
  public static final String VAN       = "van";
  public static final String TM        = "tm";
  public static final String GROEP     = "groep";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Gebruiker",
      width = "220px")
  @Select(nullSelectionAllowed = false)
  private UsrFieldValue gebruiker = null;

  @Field(customTypeClass = BsnAnrVeld.class,
      caption = "A-nummer / BSN",
      width = "220px")
  private FieldValue anr = new FieldValue();

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Periode",
      width = "220px")
  @Select(containerDataSource = ZaakHistorischePeriodesContainer.class,
      nullSelectionAllowed = false)
  @Immediate()
  private ZaakPeriode periode = new ZaakPeriodes().getHistorischePeriodes().get(1);

  @Field(customTypeClass = DatumVeld.class,
      caption = "Van",
      width = "80px",
      required = true,
      visible = false)
  private DateFieldValue van = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "t/m",
      width = "80px",
      required = true,
      visible = false)
  private DateFieldValue tm = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Groeperen op",
      width = "220px")
  @Select(nullSelectionAllowed = false,
      containerDataSource = ProtocolleringGroepContainer.class)
  private ProtocolleringGroep groep = ProtocolleringGroep.GEBRUIKER;
}
