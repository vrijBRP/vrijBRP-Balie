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

package nl.procura.gba.web.modules.zaken.correspondentie.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.services.zaken.correspondentie.CorrespondentieAfsluitingType;
import nl.procura.gba.web.services.zaken.correspondentie.CorrespondentieRoute;
import nl.procura.gba.web.services.zaken.correspondentie.CorrespondentieType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.field.ProTextArea;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2CorrespondentieBean implements Serializable {

  public static final String ROUTE        = "route";
  public static final String TYPE         = "type";
  public static final String AFSLUIT_TYPE = "afsluitType";
  public static final String ANDERS       = "anders";
  public static final String TOELICHTING  = "toelichting";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Binnengekomen / uitgaande",
      required = true,
      width = "300px")
  @Select(nullSelectionAllowed = false,
      containerDataSource = CorrespondentieRouteContainer.class)
  private CorrespondentieRoute route = CorrespondentieRoute.ONBEKEND;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Betreft",
      required = true,
      width = "300px")
  @Select(nullSelectionAllowed = false,
      containerDataSource = CorrespondentieTypeContainer.class)
  @Immediate
  private CorrespondentieType type = CorrespondentieType.ONBEKEND;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Afsluiten",
      required = true,
      width = "300px")
  @Select(nullSelectionAllowed = false,
      containerDataSource = CorrespondentieAfsluitTypeContainer.class)
  @Immediate
  private CorrespondentieAfsluitingType afsluitType = CorrespondentieAfsluitingType.AUTOMATISCH;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Anders, namelijk",
      required = true,
      visible = false,
      width = "300px")
  private String anders = "";

  @Field(customTypeClass = ProTextArea.class,
      caption = "Toelichting",
      width = "300px")
  @TextArea(nullRepresentation = "",
      rows = 3)
  private String toelichting = "";
}
