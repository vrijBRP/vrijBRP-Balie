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

package nl.procura.gba.web.modules.bs.onderzoek.aanschrijving;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoekBron;
import nl.procura.gba.web.services.bs.onderzoek.enums.AanschrijvingFaseType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.ProDateField;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class AanschrijvingBean implements Serializable {

  public static final String F_AANSCHRIJFPERSOON = "aanschrijfpersoon";
  public static final String F_EXTERNE_BRON      = "externeBron";
  public static final String F_SOORT             = "soort";

  public static final String F_DATUM_FASE1     = "datumFase1";
  public static final String F_DATUM_FASE1_END = "datumFase1End";

  public static final String F_DATUM_FASE2     = "datumFase2";
  public static final String F_DATUM_FASE2_END = "datumFase2End";

  public static final String F_DATUM_EXTRA     = "datumExtra";
  public static final String F_DATUM_EXTRA_END = "datumExtraEnd";

  public static final String F_DATUM_VOORNEMEN     = "datumVoornemen";
  public static final String F_DATUM_VOORNEMEN_END = "datumVoornemenEnd";

  public static final String F_DATUM_BESLUIT = "datumBesluit";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Aanschrijfpersoon",
      required = true,
      width = "300px")
  @Select(itemCaptionPropertyId = AanschrijfpersoonContainer.OMSCHRIJVING,
      nullSelectionAllowed = false)
  private DossierPersoon aanschrijfpersoon;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Externe bron",
      required = true,
      width = "300px")
  @Select(itemCaptionPropertyId = ExterneBronContainer.OMSCHRIJVING,
      nullSelectionAllowed = false)
  private DossierOnderzoekBron externeBron;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Soort document",
      required = true,
      width = "300px")
  @Select(nullSelectionAllowed = false)
  private AanschrijvingFaseType soort;

  @Field(customTypeClass = ProDateField.class,
      caption = "Aangemaakt op",
      width = "97px",
      required = true,
      visible = false)
  private Date datumFase1;

  @Field(customTypeClass = ProDateField.class,
      caption = "Uiterste reactiedatum",
      width = "97px",
      required = true,
      visible = false)
  @Immediate
  private Date datumFase1End;

  @Field(customTypeClass = ProDateField.class,
      caption = "Aangemaakt op",
      width = "97px",
      required = true,
      visible = false)
  @Immediate
  private Date datumFase2;

  @Field(customTypeClass = ProDateField.class,
      caption = "Uiterste reactiedatum",
      width = "97px",
      required = true,
      visible = false)
  private Date datumFase2End;

  @Field(customTypeClass = ProDateField.class,
      caption = "Aangemaakt op",
      width = "97px",
      required = true,
      visible = false)
  @Immediate
  private Date datumExtra;

  @Field(customTypeClass = ProDateField.class,
      caption = "Uiterste reactiedatum",
      width = "97px",
      required = true,
      visible = false)
  private Date datumExtraEnd;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum voornemen",
      width = "97px",
      required = true,
      visible = false)
  @Immediate
  private Date datumVoornemen;

  @Field(customTypeClass = ProDateField.class,
      caption = "Uiterste reactiedatum",
      width = "97px",
      required = true,
      visible = false)
  private Date datumVoornemenEnd;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum besluit",
      width = "97px",
      required = true,
      visible = false)
  private Date datumBesluit;
}
