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

package nl.procura.gba.web.modules.zaken.indicatie.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningIndicatie;
import nl.procura.gba.web.services.zaken.indicaties.IndicatieActie;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.component.field.ProTextArea;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2IndicatieBean implements Serializable {

  public static final String ACTIE       = "actie";
  public static final String INDICATIE   = "indicatie";
  public static final String TOELICHTING = "toelichting";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Actie",
      required = true)
  @Select(containerDataSource = IndicatieActieContainer.class)
  private IndicatieActie actie = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Keuze indicatie",
      required = true)
  private PlAantekeningIndicatie indicatie = null;

  @Field(customTypeClass = ProTextArea.class,
      caption = "Toelichting",
      width = "500px")
  @TextArea(rows = 5,
      maxLength = 70)
  private String toelichting = "";
}
