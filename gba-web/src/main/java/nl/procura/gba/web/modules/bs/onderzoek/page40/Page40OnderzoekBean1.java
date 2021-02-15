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

package nl.procura.gba.web.modules.bs.onderzoek.page40;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.bs.onderzoek.enums.BetrokkeneType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.ProTextArea;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page40OnderzoekBean1 implements Serializable {

  public static final String HETZELFDE             = "hetzelfde";
  public static final String BETROKKENEN           = "betrokkene";
  public static final String DATUM_EINDE_ONDERZOEK = "datumEindeOnderzoek";
  public static final String NOGMAALS_AANSCHRIJVEN = "nogmaalsAanschrijven";
  public static final String TOELICHTING           = "toelichting";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Gelijk aan het vermoedelijke adres?",
      required = true)
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private Boolean hetzelfde;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Betrokkene(n) is/zijn",
      width = "350px",
      required = true)
  @Select(containerDataSource = BetrokkeneTypeContainer.class)
  private BetrokkeneType betrokkene = null;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum einde onderzoek",
      width = "97px",
      required = true,
      visible = false)
  private Date datumEindeOnderzoek = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Nogmaals aanschrijven?",
      required = true,
      visible = false)
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private Boolean nogmaalsAanschrijven;

  @Field(customTypeClass = ProTextArea.class,
      caption = "Toelichting",
      width = "350px",
      visible = false)
  @TextArea(maxLength = 250,
      rows = 3,
      nullRepresentation = "")
  private String toelichting = "";
}
