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

package nl.procura.gba.web.modules.zaken.rijbewijs.page5;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.VermeldTitelType;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagReden;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagSoort;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.container.NLBooleanContainer;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page5RijbewijsBean1 implements Serializable {

  public static final String SOORT            = "soort";
  public static final String REDEN            = "reden";
  public static final String VERVANGTRBW      = "vervangtRbw";
  public static final String SPOED            = "spoed";
  public static final String PROCESVERBAAL    = "procesVerbaal";
  public static final String NAAMPARTNER      = "naamPartner";
  public static final String GBABESTENDIG     = "gbaBestendig";
  public static final String DAGEN185         = "dagen185";
  public static final String INFO             = "info";
  public static final String AFHAAL_LOCATIE   = "afhaalLocatie";
  public static final String VERMELDING_TITEL = "vermeldingTitel";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Soort",
      required = true,
      width = "300px")
  @Immediate()
  private RijbewijsAanvraagSoort soort = null;

  @Field(customTypeClass = Page5RedenField.class,
      caption = "Reden",
      required = true,
      width = "300px")
  @Immediate()
  private RijbewijsAanvraagReden reden = null;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Vervangt RBW")
  private String vervangtRbw = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Spoed",
      required = true)
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE,
      nullSelectionAllowed = false)
  @Immediate()
  private Boolean spoed = false;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Proces-verbaal",
      width = "160px",
      readOnly = true)
  private String procesVerbaal = Page5RijbewijsForm1.NIET_VAN_TOEPASSING;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Naam partner",
      required = true)
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE,
      nullSelectionAllowed = false)
  @Immediate()
  private Boolean naamPartner;

  @Field(type = FieldType.LABEL,
      caption = "BRP bestendig")
  private String gbaBestendig = "";

  @Field(type = FieldType.LABEL,
      caption = "185 dagen in NL")
  private String dagen185 = "";

  @Field(type = FieldType.LABEL,
      caption = "Ter informatie",
      width = "500px")
  private String info = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Afhaallocatie",
      required = true)
  private Locatie afhaalLocatie = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Vermelding titel / predikaat",
      width = "160px",
      required = true)
  @Immediate
  @Select(nullSelectionAllowed = false)
  private VermeldTitelType vermeldingTitel = VermeldTitelType.NVT;
}
