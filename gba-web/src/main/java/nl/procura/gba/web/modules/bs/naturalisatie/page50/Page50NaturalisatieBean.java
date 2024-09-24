/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.bs.naturalisatie.page50;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.bs.naturalisatie.enums.MinderjarigeKinderenAkkoordType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.ProTextArea;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page50NaturalisatieBean implements Serializable {

  public static final String F_BERICHT_OMTRENT_TOELATING   = "berichtOmtrentToelating";
  public static final String F_MINDERJARIGE_KINDEREN_12    = "minderjarigeKinderen12";
  public static final String F_MINDERJARIGE_KINDEREN_16    = "minderjarigeKinderen16";
  public static final String F_ANDERE_OUDER_AKKOORD        = "andereOuderAkkoord";
  public static final String F_NAAM_ANDERE_OUDER_WETTELIJK = "naamAndereOuderWettelijk";
  public static final String F_TOELICHTING                 = "toelichting";
  public static final String F_INFORMATIE_JUSTIS           = "informatieJustis";
  public static final String F_EINDE_TERMIJN               = "eindeTermijn";
  public static final String F_BESLISSING                  = "beslissing";
  public static final String F_ADVIES                      = "advies";
  public static final String F_TOELICHTING2                = "toelichting2";
  public static final String F_DATUM_BEVESTIGING           = "datumBevestiging";
  public static final String F_DATUM_AANVRAAG              = "datumAanvraag";
  public static final String F_DATUM_KONING_BESLUIT        = "datumKoningBesluit";
  public static final String F_NR_KONING_BESLUIT           = "nrKoningBesluit";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Bericht Omtrent Toelating opgevraagd")
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  private Boolean berichtOmtrentToelating;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Minderjarige kinderen (12 t/m 15 jr.) akkoord")
  @Select(containerDataSource = MinderjarigeKinderenAkkoordContainer.class)
  private MinderjarigeKinderenAkkoordType minderjarigeKinderen12;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Minderjarige kinderen (16+) akkoord")
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  private Boolean minderjarigeKinderen16;

  @Field(type = FieldType.LABEL,
      caption = "Andere ouder/wettelijk vertegenwoordiger akkoord",
      readOnly = true)
  private String andereOuderAkkoord;

  @Field(type = FieldType.LABEL,
      caption = "Andere ouder/wettelijk vertegenwoordiger",
      readOnly = true)
  private String naamAndereOuderWettelijk;

  @Field(customTypeClass = ProTextArea.class,
      width = "500px",
      caption = "Toelichting")
  @TextArea(rows = 5, nullRepresentation = "")
  private String toelichting;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Informatie opgevraagd bij justitie en politie")
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  private Boolean informatieJustis;

  @Field(customTypeClass = ProDateField.class,
      caption = "Einde termijn")
  private Date eindeTermijn;

  @Field(type = FieldType.LABEL,
      caption = "Beslissing",
      readOnly = true)
  private String beslissing;

  @Field(type = FieldType.LABEL,
      caption = "Advies burgemeester",
      readOnly = true)
  private String advies;

  @Field(customTypeClass = ProTextArea.class,
      width = "500px",
      caption = "Toelichting")
  @TextArea(rows = 5, nullRepresentation = "")
  private String toelichting2;

  @Field(type = FieldType.LABEL,
      caption = "Datum bevestiging",
      readOnly = true)
  private String datumBevestiging;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum aanvraag")
  private Date datumAanvraag;

  @Field(type = FieldType.LABEL,
      caption = "Datum Koninklijk Besluit",
      readOnly = true)
  private String datumKoningBesluit;

  @Field(type = FieldType.LABEL,
      caption = "Nummer Koninklijk Besluit",
      readOnly = true)
  private String nrKoningBesluit;
}
