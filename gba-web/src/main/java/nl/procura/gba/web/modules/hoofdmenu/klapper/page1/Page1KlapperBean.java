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

package nl.procura.gba.web.modules.hoofdmenu.klapper.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.math.BigDecimal;

import nl.procura.gba.web.components.fields.GbaBsnField;
import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteDeel;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteInvoerType;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteRegistersoort;
import nl.procura.gba.web.services.bs.algemeen.akte.KlapperVolgordeType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.InputPrompt;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.field.ProTextField;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1KlapperBean implements Serializable {

  public static final String INVOERTYPE    = "invoerType";
  public static final String DATUM_FEIT    = "datumFeit";
  public static final String DATUM_AKTE    = "datumAkte";
  public static final String JAAR          = "jaar";
  public static final String SOORT         = "soort";
  public static final String DEEL          = "deel";
  public static final String NUMMER        = "nummer";
  public static final String BSN           = "bsn";
  public static final String GESLACHTSNAAM = "geslachtsnaam";
  public static final String VOORNAMEN     = "voornamen";
  public static final String GEBOORTEDATUM = "geboortedatum";
  public static final String VOLGORDE      = "volgorde";
  public static final String OPMERKING     = "opmerking";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Type",
      width = "170px")
  @Immediate
  private DossierAkteInvoerType invoerType = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Periode",
      width = "170px")
  @Immediate
  private BigDecimal jaar;

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datums",
      width = "80px")
  @Immediate
  @InputPrompt(text = "Feit")
  private DateFieldValue datumFeit = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datums",
      width = "85px")
  @Immediate
  @InputPrompt(text = "Akte")
  private DateFieldValue datumAkte = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Registersoort",
      width = "280px")
  @Immediate
  private DossierAkteRegistersoort soort = DossierAkteRegistersoort.AKTE_ONBEKEND;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Registerdeel",
      width = "280px")
  @Immediate
  private DossierAkteDeel deel = null;

  @Field(customTypeClass = NumberField.class,
      caption = "Volgnummer",
      width = "80px")
  @TextField(maxLength = 4)
  @Immediate
  private String nummer = "";

  @Field(customTypeClass = GbaBsnField.class,
      caption = "Burgerservicenummer",
      width = "170px")
  @Immediate
  private BsnFieldValue bsn = null;

  @Field(customTypeClass = ProTextField.class,
      caption = "Geslachtsnaam",
      width = "170px")
  @Immediate
  @TextField(nullRepresentation = "")
  private String geslachtsnaam = null;

  @Field(customTypeClass = ProTextField.class,
      caption = "Voornamen",
      width = "170px")
  @Immediate
  @TextField(nullRepresentation = "")
  private String voornamen = null;

  @Field(customTypeClass = GbaDateField.class,
      caption = "Geboortedatum",
      width = "170px")
  @Immediate
  private GbaDateFieldValue geboortedatum = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Volgorde",
      width = "170px")
  @Immediate
  private KlapperVolgordeType volgorde = KlapperVolgordeType.DATUM_OPLOPEND;

  @Field(customTypeClass = ProTextField.class,
      caption = "Opmerking",
      width = "170px")
  @Immediate
  @TextField(nullRepresentation = "")
  private String opmerking = null;
}
