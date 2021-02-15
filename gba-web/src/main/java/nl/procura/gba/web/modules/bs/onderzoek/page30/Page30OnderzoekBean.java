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

package nl.procura.gba.web.modules.bs.onderzoek.page30;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.ProTextArea;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page30OnderzoekBean implements Serializable {

  public static final String START_FASE1_OP        = "startFase1Op";
  public static final String START_FASE1_TM        = "startFase1Tm";
  public static final String REACTIE_ONTVANGEN     = "reactieOntvangen";
  public static final String TOELICHTING1          = "toelichting1";
  public static final String VERVOLGACTIES         = "vervolgacties";
  public static final String START_FASE2_OP        = "startFase2Op";
  public static final String START_FASE2_TM        = "startFase2Tm";
  public static final String ONDERZOEK_TER_PLAATSE = "onderzoekTerPlaatse";
  public static final String UITGEVOERD_OP         = "uitgevoerdOp";
  public static final String TOELICHTING2          = "toelichting2";

  @Field(customTypeClass = ProDateField.class,
      caption = "Start 1e fase op",
      width = "97px",
      required = true)
  private Date startFase1Op;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum einde termijn",
      width = "97px",
      required = true)
  private Date startFase1Tm;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Reactie ontvangen?")
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private Boolean reactieOntvangen;

  @Field(customTypeClass = ProTextArea.class,
      caption = "Toelichting",
      width = "450px")
  @TextArea(maxLength = 250,
      rows = 3,
      nullRepresentation = "")
  private String toelichting1 = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Vervolgactie(s) noodzakelijk?")
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private Boolean vervolgacties;

  @Field(customTypeClass = ProDateField.class,
      caption = "Start 2e fase op",
      width = "97px",
      required = true)
  private Date startFase2Op;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum einde termijn",
      width = "97px",
      required = true)
  private Date startFase2Tm;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Onderzoek ter plaatse gewenst?")
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private Boolean onderzoekTerPlaatse;

  @Field(customTypeClass = ProDateField.class,
      caption = "Uitgevoerd op",
      width = "97px")
  private Date uitgevoerdOp;

  @Field(customTypeClass = ProTextArea.class,
      caption = "Toelichting",
      width = "450px")
  @TextArea(rows = 3,
      maxLength = 250,
      nullRepresentation = "")
  private String toelichting2 = "";
}
