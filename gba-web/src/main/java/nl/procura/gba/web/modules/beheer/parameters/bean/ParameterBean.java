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

package nl.procura.gba.web.modules.beheer.parameters.bean;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.*;
import static nl.procura.standard.Globalfunctions.astr;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.jpa.personen.db.Parm;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.beheer.parameters.container.*;
import nl.procura.gba.web.modules.bs.onderzoek.page20.AanduidingOnderzoekContainer;
import nl.procura.gba.web.services.beheer.parameter.ParameterType;
import nl.procura.gba.web.services.beheer.parameter.Parameters;
import nl.procura.gba.web.services.beheer.parameter.annotations.ParameterAnnotation;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.layout.FormBean;
import nl.procura.vaadin.annotation.layout.Position;
import nl.procura.vaadin.annotation.layout.Position.Direction;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.validator.GetalValidator;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD,
    defaultWidth = "220px")
@FormBean(defaultCaptionWidth = "200px")
public class ParameterBean implements Serializable {

  public static final long    GEEN_GEBRUIKER             = 0;
  public static final long    GEEN_PROFIEL               = 0;
  private static final String CODE_STATUS_OPGENOMEN      = "0";
  private static final String CODE_STATUS_IN_BEHANDELING = "1";
  private static final String CODE_STATUS_VERWERKT       = "2";
  private static final String CODE_STATUS_WACHTKAMER     = "5";

  // Testomgeving
  @ParameterAnnotation(TEST_OMGEVING)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Testomgeving",
      description = "Is dit de testomgeving.")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String testOmgeving = "";

  // Testomgeving
  @ParameterAnnotation(ZOEK_PROFIEL)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Aanvullend zoekprofiel",
      description = "Welk zoekprofiel is van toepassing?.")
  private String zoekProfiel = "";

  // PLE Url
  @ParameterAnnotation(ZOEK_PLE_JAVA_SERVER_URL)
  @Position(order = "0")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Endpoint opvragen persoonsgegevens",
      width = "400px")
  private String urlPersonenWs = "";

  @ParameterAnnotation(ZAKEN_TAB_VOLGORDE)
  @Position(order = "0.1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Volgorde tabbladen van een zaak")
  @Select(containerDataSource = TabVolgordeContainer.class,
      itemCaptionPropertyId = TabVolgordeContainer.OMSCHRIJVING)
  private String zakenTabVolgorde = "";

  @ParameterAnnotation(ZAKEN_EINDSTATUS)
  @Position(order = "0.2")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Eindstatus wijzigen",
      description = "Mag een zaak met een 'eindstatus' nog worden gewijzigd")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String zakenEindstatus = "";

  @ParameterAnnotation(ZAKEN_STATUS_EIGEN_ZAAK)
  @Position(order = "0.3")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Status van zelf-ingevoerde zaak wijzigen",
      description = "Mag de status van een zelf-ingevoerde-zaak gewijzigd")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String zakenEigenStatus = "";

  @ParameterAnnotation(ZAKEN_MAX_STATUS_ZAAK_WIJZIGEN)
  @Position(order = "0.4")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Maximale status voor wijzigen zaak")
  private String zakenMaxStatusZaakWijzigen = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_AFSTAM_GEB)
  @Position(order = "Afstamming (geboorte)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Afstamming (geboorte)")
  private String initieleStatusAfstamGeb = "";

  @ParameterAnnotation(ZAKEN_DMS_AFSTAM_GEB)
  @Position(order = "Afstamming (geboorte)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Afstamming (geboorte)")
  private String dmsAfstamGeb = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_AFSTAM_ERK)
  @Position(order = "Afstamming (geboorte")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Afstamming (geboorte")
  private String initieleStatusAfstamErk = "";

  @ParameterAnnotation(ZAKEN_DMS_AFSTAM_ERK)
  @Position(order = "Afstamming (erkenning)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Afstamming (erkenning)")
  private String dmsAfstamErk = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_AFSTAM_NK)
  @Position(order = "Afstamming (naamskeuze)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Afstamming (naamskeuze)")
  private String initieleStatusAfstamNk = "";

  @ParameterAnnotation(ZAKEN_DMS_AFSTAM_NK)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Afstamming (naamskeuze)")
  private String dmsAfstamNk = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_CORRES)
  @Position(order = "Correspondentie")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Correspondentie")
  private String initieleStatusCorres = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_COVOG)
  @Position(order = "COVOG")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "COVOG")
  private String initieleStatusCovog = "";

  @ParameterAnnotation(ZAKEN_DMS_COVOG)
  @Position(order = "COVOG")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "COVOG")
  private String dmsCovog = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_VERSTREK)
  @Position(order = "Verstrekkingsbeperking")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Verstrekkingsbeperking")
  private String initieleStatusVerstrek = "";

  @ParameterAnnotation(ZAKEN_DMS_VERSTREK)
  @Position(order = "Verstrekkingsbeperking")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Verstrekkingsbeperking")
  private String dmsVerstrek = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_GPK)
  @Position(order = "GPK")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "GPK")
  private String initieleStatusGpk = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_INBOX)
  @Position(order = "Inbox")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Inbox")
  private String initieleStatusInbox = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_INDICATIE)
  @Position(order = "Inbox")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Indicatie")
  private String initieleStatusIndicatie = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_HUW_GPS)
  @Position(order = "Huwelijk / GPS")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Huwelijk / GPS")
  private String initieleStatusHuwGps = "";

  @ParameterAnnotation(ZAKEN_DMS_HUW_GPS)
  @Position(order = "Huwelijk / GPS")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Huwelijk / GPS")
  private String dmsHuwGps = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_OMZET_GPS)
  @Position(order = "Omzetting GPS in huwelijk")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Omzetting GPS in huwelijk")
  private String initieleStatusOmzettingGps = "";

  @ParameterAnnotation(ZAKEN_DMS_OMZET_GPS)
  @Position(order = "Omzetting GPS in huwelijk")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Omzetting GPS in huwelijk")
  private String dmsOmzetGps = "";

  @ParameterAnnotation(ZAKEN_DMS_ONTB_HUW_GPS)
  @Position(order = "Ontbinding/einde Huwelijk/GPS")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Ontbinding/einde Huwelijk/GPS")
  private String dmsOntbHuwGps = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_ONTBINDING)
  @Position(order = "Ontbinding/einde huwelijk/GPS in gemeente")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Ontbinding/einde huwelijk/GPS in gemeente")
  private String initieleStatusOntbinding = "";

  @ParameterAnnotation(ZAKEN_DMS_ONDERZOEK)
  @Position(order = "Onderzoek")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Onderzoek")
  private String dmsOnderzoek = "";

  @ParameterAnnotation(ZAKEN_DMS_REGISTRATION)
  @Position(order = "Eerste inschrijving")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Eerste inschrijving")
  private String dmsRegistration = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_ONDERZOEK)
  @Position(order = "Onderzoek")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Onderzoek")
  private String initieleStatusOnderzoek = "";

  @ParameterAnnotation(ZAKEN_DMS_RISK_ANALYSIS)
  @Position(order = "Risicoanalyse")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Risicoanalyse")
  private String dmsRiskAnalysis = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_RISK_ANALYSIS)
  @Position(order = "Risicoanalyse")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Risicoanalyse")
  private String initialStatusRiskAnalysis = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_INH_VERMIS)
  @Position(order = "Inhouding / vermissing reisdocument")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Inhouding / vermissing reisdocument")
  private String initieleStatusInhVermis = "";

  @ParameterAnnotation(ZAKEN_DMS_INH_VERMIS)
  @Position(order = "Inhouding/vermissing")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Inhouding/vermissing")
  private String dmsInhVerm = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_INH_VERMIS_RYB)
  @Position(order = "Inhouding / vermissing rijbewijs")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Inhouding / vermissing rijbewijs")
  private String initieleStatusInhVermisRijb = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_OVERL_GEM)
  @Position(order = "Overlijden (in gemeente)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Overlijden (in gemeente)")
  private String initieleStatusOverlGem = "";

  @ParameterAnnotation(ZAKEN_DMS_OVERL_GEM)
  @Position(order = "Overlijden (in gemeente)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Overlijden (in gemeente)")
  private String dmsOverlGem = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_OVERL_LIJKV)
  @Position(order = "Overlijden (lijkvinding)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Overlijden (lijkvinding)")
  private String initieleStatusOverlLijkv = "";

  @ParameterAnnotation(ZAKEN_DMS_OVERL_LIJKV)
  @Position(order = "Overlijden (lijkvinding)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Overlijden (lijkvinding)")
  private String dmsOverlLijkv = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_OVERL_LEVENLOOS)
  @Position(order = "Overlijden (levenloos geboren kind)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Overlijden (levenloos geboren kind)")
  private String initieleStatusOverlLevenloos = "";

  @ParameterAnnotation(ZAKEN_DMS_OVERL_LEVENLOOS)
  @Position(order = "Overlijden (levenloos geboren kind)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Overlijden (levenloos geboren kind)")
  private String dmsOverlLevenloos = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_GV)
  @Position(order = "Gegevensverstrekking")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Gegevensverstrekking")
  private String initieleStatusGv = "";

  @ParameterAnnotation(ZAKEN_DMS_GV)
  @Position(order = "Gegevensverstrekking")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Gegevensverstrekking")
  private String dmsGv = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_REISDOC)
  @Position(order = "Reisdocument")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Reisdocument")
  private String initieleStatusReisdoc = "";

  @ParameterAnnotation(ZAKEN_DMS_REISDOC)
  @Position(order = "Reisdocument")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Reisdocument")
  private String dmsReisdoc = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_RIJBEWIJS)
  @Position(order = "Rijbewijs")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Rijbewijs")
  private String initieleStatusRijbewijs = "";

  @ParameterAnnotation(ZAKEN_DMS_RIJBEWIJS)
  @Position(order = "Rijbewijs")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Rijbewijs")
  private String dmsRijbewijs = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_TMV)
  @Position(order = "Terugmelding")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Terugmelding")
  private String initieleStatusTmv = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_UITTREKSEL)
  @Position(order = "Uittreksel")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Uittreksel")
  private String initieleStatusUitt = "";

  @ParameterAnnotation(ZAKEN_DMS_UITTREKSEL)
  @Position(order = "Uittreksel")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Uittreksel")
  private String dmsUitt = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_VERH_BINNEN_WA)
  @Position(order = "Binnenverhuizing (woonadres)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Binnenverhuizing (woonadres)")
  private String initieleStatusBinnenVerhWoonAdres = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_VERH_BINNEN_BA)
  @Position(order = "Binnenverhuizing (briefadres)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Binnenverhuizing (briefadres)")
  private String initieleStatusBinnenVerhBriefAdres = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_VERH_BUITEN_WA)
  @Position(order = "Intergem. verhuizing (woonadres)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Intergem. verhuizing (woonadres)")
  private String initieleStatusBuitenVerhWoonAdres = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_VERH_BUITEN_BA)
  @Position(order = "Intergem. verhuizing (briefadres)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Intergem. verhuizing (briefadres)")
  private String initieleStatusBuitenVerhBriefAdres = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_VERH_HERV_WA)
  @Position(order = "Hervestiging (woonadres)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Hervestiging (woonadres)")
  private String initieleStatusHervestigingWoonAdres = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_VERH_HERV_BA)
  @Position(order = "Hervestiging (briefadres)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Hervestiging (briefadres)")
  private String initieleStatusHervestigingBriefAdres = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_VERH_EMIGR_WA)
  @Position(order = "Emigratie (woonadres)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Emigratie (woonadres)")
  private String initieleStatusEmigrWoonAdres = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_VERH_EMIGR_BA)
  @Position(order = "Emigratie (briefadres)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Emigratie (briefadres)")
  private String initieleStatusEmigrBriefAdres = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_REGISTRATION_WA)
  @Position(order = "Eerste inschrijving (Woonadres)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Eerste inschrijving (Woonadres)")
  private String initieleStatusRegWoonAdres = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_REGISTRATION_BA)
  @Position(order = "Eerste inschrijving (Briefadres)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Eerste inschrijving (Briefadres)")
  private String initieleStatusRegBriefAdres = "";

  @ParameterAnnotation(ZAKEN_DMS_VERHUIZING)
  @Position(order = "Verhuizing")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Verhuizing")
  private String dmsVerhuizing = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_NAAMGEBRUIK)
  @Position(order = "Naamgebruik")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Naamgebruik")
  private String initieleStatusNaamgebruik = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_PL_MUTATIE)
  @Position(order = "PL mutatie")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "PL mutatie")
  private String initieleStatusPLMutatie = "";

  @ParameterAnnotation(ZAKEN_DMS_NAAMGEBRUIK)
  @Position(order = "Naamgebruik")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Naamgebruik")
  private String dmsNaamgebruik = "";

  // COVOG ID Code
  @ParameterAnnotation(COVOG_ID_CODE)
  @Position(order = "3")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "COVOG identificatiecode")
  @TextField(secret = true)
  private String covogIdentificatiecode = "";

  // COVOG Relatie ID
  @ParameterAnnotation(COVOG_RELATIE_ID)
  @Position(order = "2")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "COVOG relatiecode")
  private String covogRelatiecode = "";

  // COVOG URL
  @ParameterAnnotation(COVOG_URL)
  @Position(order = "1")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "COVOG URL",
      width = "400px")
  private String covogURL = "";

  // COVOG URL
  @ParameterAnnotation(SSL_PROXY_URL)
  @Position(order = "9")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "SSL Proxy (URL)",
      width = "400px")
  private String sslProxyUrl = "";

  @ParameterAnnotation(BSM_INTERNAL_URL)
  @Position(order = "9a")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Taakplanner (Interne URL)",
      width = "400px")
  private String bsmInternalUrl = "";

  @ParameterAnnotation(BSM_EXTERNAL_URL)
  @Position(order = "9a")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Taakplanner (Externe URL)",
      width = "400px")
  private String bsmExternalUrl = "";

  @ParameterAnnotation(BSM_ZAKEN_DMS)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Zaken DMS van toepassing",
      width = "400px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String bsmZakenDmsQueryAan = "";

  @ParameterAnnotation(BSM_ZAKEN_DMS_ZAAKTYPE)
  @Position(order = "2")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Zaken DMS Zaak-id type",
      width = "400px")
  @Select(containerDataSource = ZaakIdentificatieTypeParameterContainer.class,
      itemCaptionPropertyId = ZaakIdentificatieTypeParameterContainer.OMSCHRIJVING)
  private String bsmZakenDmsQueryZaaktype = "";

  @ParameterAnnotation(BSM_ZAKEN_DMS_VARIANT)
  @Position(order = "3")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Zaakgegevens opvragen variant",
      width = "400px")
  @Select(containerDataSource = ZaakDmsVariantContainer.class,
      itemCaptionPropertyId = ZaakDmsVariantContainer.OMSCHRIJVING)
  private String bsmZakenDmsZaakdetailsApart = "";

  // Test Debug
  @ParameterAnnotation(LOCATIE_OPSLAG)
  @Position(order = "11")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Laatste locatie automatisch selecteren",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String locatieOpslaan;
  // Handleidingen
  @ParameterAnnotation(HANDLEIDING_RAADPLEGER)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Handleiding voor raadplegers",
      width = "60px")
  @Position(order = "1")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String handleidingRaadpleger = "";

  @ParameterAnnotation(HANDLEIDING_GEBRUIKER)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Handleiding voor gebruikers",
      width = "60px")
  @Position(order = "2")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String handleidingGebruiker = "";

  @ParameterAnnotation(HANDLEIDING_BEHEERDER)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Handleiding voor beheerders",
      width = "60px")
  @Position(order = "3")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String handleidingBeheerder = "";

  @ParameterAnnotation(HANDLEIDING_INSCHRIJVING)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Handleiding voor 1e inschrijving",
      width = "60px")
  @Position(order = "4")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String handleidingInschrijving = "";

  @ParameterAnnotation(HANDLEIDING_HUP)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Handleiding uitvoeringsprocedures (HUP)",
      width = "60px")
  @Position(order = "5")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String handleidingUitvoeringsprocedures = "";

  // Documenten Verwijderen
  @ParameterAnnotation(DOC_VERWIJDEREN)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Opgeslagen documenten verwijderen",
      description = "Mogen de documenten die zijn opgeslagen door de gebruiker worden verwijderd?",
      width = "60px")
  @Position(order = "1")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String documentenVerwijderen = "";

  // Documenten Verwijderen
  @ParameterAnnotation(DOC_TOON_BESTAND)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Bestand tonen bij afdrukken",
      description = "Toon bij het genereren ook het sjabloon?",
      width = "60px")
  @Position(order = "10")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String documentSjabloonTonen = "";

  // Documenten Template Path
  @ParameterAnnotation(DOC_TEMPLATE_PAD)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Documenten sjabloonmap",
      description = "Map waarin de sjabolen voor de documenten worden opgeslagen. Leeg laten voor de standaard map.",
      width = "300px")
  @Position(order = "20")
  private String documentenTemplatePath = "";

  // Documenten Verwijderen
  @ParameterAnnotation(DOC_DMS_TYPE)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Type documentenopslag",
      description = "De manier waarop de documenten worden opgeslagen",
      width = "300px")
  @Position(order = "30")
  @Select(containerDataSource = DmsTypeContainer.class,
      itemCaptionPropertyId = DmsTypeContainer.OMSCHRIJVING)
  @Immediate
  private String documentDmsType = "";

  // Documenten Output Path
  @ParameterAnnotation(DOC_OUTPUT_PAD)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Proweb Personen documentenmap",
      description = "Map waarin de documentuitvoer wordt opgeslagen. Leeg laten voor de standaard map.",
      width = "300px")
  @Position(order = "40")
  private String documentenOutputPath = "";

  // Documenten mate van vertrouwelijkheid
  @ParameterAnnotation(DOC_CONFIDENTIALITY)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Mate van vertrouwelijkheid documenten",
      width = "300px")
  @Position(order = "41")
  @Select(containerDataSource = DocumentVertrouwelijkheidParameterContainer.class,
      itemCaptionPropertyId = DocumentVertrouwelijkheidParameterContainer.OMSCHRIJVING)
  @Immediate
  private String documentConf = "";

  // Gemeentecodes
  @ParameterAnnotation(GEMEENTE_CODES)
  @Position(order = "5")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gemeentecode(s)",
      description = "De code(s) van de gemeente om te bepalen of een PL of adres binnengemeentelijk is. " +
          "Als er meerdere codes zijn, dan moeten deze door een komma (,) worden gescheiden.")
  private String gemeenteCodes = "";

  // Google Maps Key
  @ParameterAnnotation(GMAPKEY)
  @Position(order = "2")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Google Maps sleutel",
      description = "De kaart bij de extra persoonsgegevens werkt alleen als hier een geldige sleutel is ingegeven. " +
          "Deze sleutel kan worden verkregen bij http://www.google.com/apis/maps",
      width = "400px")
  private String googleMapKey = "";

  //  GPK
  @ParameterAnnotation(GPK_AFGEVER)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "De afgever van GP-kaarten")
  private String gpk = "";

  // Identificering Overslaan
  @ParameterAnnotation(ID_VERPLICHT)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Identificering verplicht",
      width = "250px")
  @Select(containerDataSource = IdVerplichtContainer.class,
      itemCaptionPropertyId = IdVerplichtContainer.OMSCHRIJVING)
  private String identificeringVerplichtheid = "";

  // Functiescheiding bij reisdocumenten
  @ParameterAnnotation(FS_REISDOC)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Functiescheiding reisdocumenten",
      description = "Moet er functiescheiding zijn met betrekking tot reisdocumentaanvragen?",
      width = "60px")
  @Position(order = "1")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String functieScheidingReisdocumenten = "";

  // Functiescheiding bij reisdocumenten
  @ParameterAnnotation(FS_RIJBEWIJS)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Functiescheiding rijbewijzen",
      description = "Moet er functiescheiding zijn met betrekking tot rijbewijsaanvragen?",
      width = "60px")
  @Position(order = "1")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String functieScheidingRijbewijzen = "";

  @ParameterAnnotation(TOON_AANTEKENING)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Tonen aantekeningen",
      description = "Moeten de aantekeningen worden getoond zodra een persoonslijst wordt gezocht?",
      width = "60px")
  @Position(order = "1")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String tonenAantekeningen = "";

  @ParameterAnnotation(ACCESS_RISK_PROFILE_SIGNALS)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Toegang tot markeringen",
      description = "Heeft deze persoon of profiel toegang tot het markeren van personen en adressen voor risicoanalyses?",
      width = "60px")
  @Position(order = "1")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String accessRiskProfileSignal = "";

  // Contactgegevens overslaan
  @ParameterAnnotation(CONTACT_VERPLICHT)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Contactgegevens verplicht",
      width = "250px")
  @Select(containerDataSource = ContactVerplichtContainer.class,
      itemCaptionPropertyId = ContactVerplichtContainer.OMSCHRIJVING)
  private String contactgegevensVerplichtheid = "";

  // Inlog opmerking
  @ParameterAnnotation(REMEMBER_ME)
  @Position(order = "4")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Onthoud-mij-optie",
      description = "Toon de optie op het inlogscherm waarmee de gebruiker automatisch kan inloggen.")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String rememberMeOption = "";

  // Inlog opmerking
  @ParameterAnnotation(SCHERMOPBOUWTYPE)
  @Position(order = "4a")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Schermopbouwtype",
      description = "Dynamisch: tabellen worden verticaal vergroot naar 100% van de pagina. Dit werkt niet goed op kleine schermen en/of in Internet Explorer 11. <br/>Vast: tabellen worden niet verticaal vergroot naar 100%.")
  @Select(containerDataSource = SchermopbouwtypeContainer.class,
      itemCaptionPropertyId = SchermopbouwtypeContainer.OMSCHRIJVING)
  private String schermopbouwtype = "";

  // Inlog opmerking
  @ParameterAnnotation(INLOGOPMERKING)
  @Position(order = "4")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Inlogopmerking",
      description = "Een opmerking die wordt getoond na het inloggen.",
      width = "400px",
      height = "100px")
  @TextField(rows = 3)
  private String inlogOpmerking = "";

  // Kassa Clear List
  @ParameterAnnotation(KASSA_CLEAR_LIST)
  @Position(order = "7")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Kassalijst opschonen na versturen naar de kassa")
  @Select(containerDataSource = KassalijstOpschonenContainer.class,
      itemCaptionPropertyId = KassalijstOpschonenContainer.OMSCHRIJVING)
  private String kassaClearList = "";

  // Kassa FTP Password
  @ParameterAnnotation(KASSA_FTP_PW)
  @Position(order = "5")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Kassa FTP wachtwoord")
  @TextField(secret = true)
  private String kassaFTPPassword = "";

  // Kassa FTP Url
  @ParameterAnnotation(KASSA_FTP_URL)
  @Position(order = "3",
      direction = Direction.VERTICAL_WITH_RULER)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Kassa FTP site",
      width = "400px")
  private String kassaFTPURL = "";

  // Kassa FTP Username
  @ParameterAnnotation(KASSA_FTP_USERNAME)
  @Position(order = "4")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Kassa FTP gebruikersnaam")
  @TextField(secret = true)
  private String kassaFTPUsername = "";

  // Kassa Filename
  @ParameterAnnotation(KASSA_FILENAME)
  @Position(order = "2",
      direction = Direction.VERTICAL_WITH_RULER)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Kassa uitvoerbestand",
      width = "400px")
  private String kassaFilename = "";

  // Kassa ID
  @ParameterAnnotation(KASSA_ID)
  @Position(order = "6",
      direction = Direction.VERTICAL_WITH_RULER)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Kassa ID")
  private String kassaID = "";

  // Log
  @ParameterAnnotation(LOG)
  @Position(order = "3")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Toegang tot de log",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String log;

  // Midoffice Binnenverhuizing
  @ParameterAnnotation(MIDOFFICE_BVH_OPNEMEN)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Binnenverhuizing naar midoffice sturen?",
      description = "De applicatie moet de binnenverhuizingen van deze gebruiker naar het midoffice doorsturen.",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String midofficeBinnenverhuizingOpnemen = "";

  @ParameterAnnotation(MIDOFFICE_DASHBOARD_BRONNEN)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Midoffice bronnen (dashboard)",
      description = "Wat voor bronnen gelden al midoffice bron.",
      width = "400px")
  private String moDashboardBron = "";

  @ParameterAnnotation(MIDOFFICE_DASHBOARD_LEVERANCIERS)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Midoffice leveranciers (dashboard)",
      description = "Wat voor leveranciers gelden al midoffice leverancier.",
      width = "400px")
  private String moDashboardLeverancier = "";

  // Open Office Hostname
  @ParameterAnnotation(OPENOFFICE_HOSTNAME)
  @Position(order = "1")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "OpenOffice host",
      description = "Om de volledige functionaliteit van de documenten te kunnen benutten moet er " +
          "een OpenOffice listener draaien op een bepaalde host en port die de sjablonen " +
          "kan converteren naar het gewenste formaat")
  private String openOfficeHostname = "";

  // Open Office Port
  @ParameterAnnotation(OPENOFFICE_PORT)
  @Position(order = "2")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "OpenOffice poort",
      description = "De poort waarop de OpenOffice listener draait",
      width = "60px",
      validators = GetalValidator.class)
  @TextField(maxLength = 5)
  private String openOfficePort;

  // Open Office installatie
  @ParameterAnnotation(OPENOFFICE_CONVERT)
  @Position(order = "3")
  @Field(type = FieldType.NATIVE_SELECT,
      caption = "OpenOffice installatie")
  @Select(containerDataSource = OpenOfficeContainer.class,
      itemCaptionPropertyId = OpenOfficeContainer.OMSCHRIJVING)
  private String openOfficeInstallatie = "remote";

  // Testomgeving
  @ParameterAnnotation(BZ_CONNECT_ENABLED)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Van toepassing",
      description = "Is dit de testomgeving.")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String bzConnectEnabled = "";

  @ParameterAnnotation(BZ_CONNECT_HOST)
  @Position(order = "2")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Server",
      description = "De server waarop de netwerkprinter service draait")
  @TextField
  private String bzConnectHost;

  @ParameterAnnotation(BZ_CONNECT_PORT)
  @Position(order = "3")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Poort",
      description = "De poort waarop de netwerkprinter service draait",
      validators = { GetalValidator.class },
      width = "60px")
  @TextField(maxLength = 5)
  private String bzConnectPort = "";

  // Netwerkprinter
  @ParameterAnnotation(BZ_CONNECT_PKK)
  @Position(order = "4")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Certificaat",
      description = "Het certificaat waarmee deze applicatie zich identificeert",
      width = "500px")
  @TextField
  private String bzConnectPkk;

  @ParameterAnnotation(BZ_CONNECT_PASSPHRASE)
  @Position(order = "5")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Certificaat wachtwoord",
      description = "Het wachtwoord van het certificaat")
  @TextField(secret = true)
  private String bzConnectPassphrase;

  @ParameterAnnotation(BZ_CONNECT_USERNAME)
  @Position(order = "6")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gebruikersnaam",
      description = "De gebruikersnaam waarmee deze applicatie zich identificeert")
  @TextField
  private String bzConnectUsername;

  // PPD code
  @ParameterAnnotation(GEBR_PPD)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "PPD code",
      validators = { GetalValidator.class },
      width = "30px")
  @TextField(maxLength = 2)
  private String ppdCode = "";

  // Presentievraag
  @ParameterAnnotation(PRESENTIE_VRAAG_ENDPOINT)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "URL van de presentievraag",
      width = "400px")
  private String presentievraagURL = "";

  // Kennisbank
  @ParameterAnnotation(KENNISBANK_URL)
  @Position(order = "0")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "URL van de vindburgerzaken homepage ",
      width = "400px")
  private String kennisbankHomepageURL = "";

  // Protocollering PL
  @ParameterAnnotation(PROT_STORE_PL)
  @Field(type = FieldType.NATIVE_SELECT,
      caption = "Mate van protocollering",
      description = "De mate van protocollering bij het zoeken van een specifieke persoonslijst",
      width = "300px")
  @Position(order = "2")
  @Select(containerDataSource = ProtocolleringContainer.class,
      itemCaptionPropertyId = ProtocolleringContainer.OMSCHRIJVING)
  private String protocolleringPL = "";

  // Protocollering zoekopdrachten niet vastleggen
  @ParameterAnnotation(PROT_IGNORE_SEARCH)
  @Field(type = FieldType.NATIVE_SELECT,
      caption = "Geen zoekopdrachten protocolleren",
      description = "Van dit account worden geen zoekopdrachten vastgelegd",
      width = "100px")
  @Position(order = "3")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String protIgnoreSearch = "";

  // Protocollering logingpogingen niet vastleggen
  @ParameterAnnotation(PROT_IGNORE_LOGIN)
  @Field(type = FieldType.NATIVE_SELECT,
      caption = "Geen inlogpogingen protocolleren",
      description = "Van dit account worden geen inlogpogingen vastgelegd",
      width = "100px")
  @Position(order = "4")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String protIgnoreLogin = "";

  // Vergelijkscherm Tonen
  @ParameterAnnotation(RYB_VERGELIJKSCHERM)
  @Position(order = "11")
  @Field(type = FieldType.NATIVE_SELECT,
      caption = "Vergelijkingsscherm tonen",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String rijbewijsVergelijkscherm = "";

  @ParameterAnnotation(RYB_TEST)
  @Position(order = "12")
  @Field(type = FieldType.NATIVE_SELECT,
      caption = "Gebruik testberichten",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String rijbewijsTesten = "";

  @ParameterAnnotation(RYB_AANPASSINGEN)
  @Position(order = "13")
  @Field(type = FieldType.NATIVE_SELECT,
      caption = "RDW aanpassingen",
      width = "200px")
  @Select(containerDataSource = RdwAanpassingenContainer.class,
      itemCaptionPropertyId = RdwAanpassingenContainer.OMSCHRIJVING)
  private String rijbewijsWijzigingen = "";

  @ParameterAnnotation(RYB_ENABLED)
  @Position(order = "01")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Rijbewijzen service actief",
      width = "70px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String rijbewijzenEnabled = "";

  @ParameterAnnotation(RYB_URL)
  @Position(order = "02",
      direction = Direction.VERTICAL_WITH_RULER)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "URL van de rijbewijzen",
      width = "400px")
  private String rijbewijsUrl = "";

  @ParameterAnnotation(RYB_GEBRUIKERSNAAM)
  @Position(order = "05",
      direction = Direction.VERTICAL_WITH_RULER)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "RDW account-id",
      width = "200px")
  private String rijbewijsGebrNaam = "";

  @ParameterAnnotation(RYB_PV_NR)
  @Position(order = "13")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Standaard proces-verbaalnummer",
      width = "400px")
  private String rijbewijsPvNr = "";

  @ParameterAnnotation(RYB_VERVALTERMIJN_DAGEN)
  @Position(order = "07")
  @Field(customTypeClass = NumberField.class,
      caption = "Wachtwoord vervaltermijn (in dagen)",
      width = "40px")
  private String rijbewijsVervaltermijnInDagen = "";

  @ParameterAnnotation(REISD_PV_NR)
  @Position(order = "01")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Standaard proces-verbaalnummer",
      width = "400px")
  private String reisdocumentPvNr = "";

  @ParameterAnnotation(REISD_TERMIJN_WIJZIGING)
  @Position(order = "02")
  @Field(customTypeClass = DatumVeld.class,
      caption = "Ingangsdatum nieuwe reglementen",
      width = "80px")
  private String reisdocumentReglementenWijziging = "";

  @ParameterAnnotation(REISD_SIGNAL_INFO)
  @Position(order = "03")
  @Field(type = FieldType.TEXT_AREA,
      caption = "Toelichting bij signalering",
      width = "400px")
  @TextArea(rows = 5)
  private String reisdocumentToelSignalering = "";

  @ParameterAnnotation(VRS_SERVICE_URL)
  @Position(order = "04")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "URL van VRS services application API",
      width = "400px")
  private String reisdocumentVrsUrl = "";

  @ParameterAnnotation(VRS_START_DATE)
  @Position(order = "05")
  @Field(customTypeClass = DatumVeld.class,
      caption = "Ingangsdatum VRS",
      width = "80px")
  private String reisdocumentVrsStartDate = "";

  // Terugmelding beheer
  @ParameterAnnotation(TERUGMBEHEER)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Beheerder van de terugmeldingen",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String terugmeldingBeheer;
  // Curatele
  @ParameterAnnotation(CURATELE_URL)
  @Position(order = "01")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "URL van de curatele webservice",
      width = "400px")
  private String curateleURL = "";

  @ParameterAnnotation(CURATELE_GEBRUIKERSNAAM)
  @Position(order = "02",
      direction = Direction.VERTICAL_WITH_RULER)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gebruikersnaam",
      width = "250px")
  private String curateleGebrNaam = "";

  @ParameterAnnotation(CURATELE_WACHTWOORD)
  @TextField(secret = true)
  @Position(order = "03")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Wachtwoord",
      width = "250px")
  private String curateleWachtwoord = "";

  // Test Debug
  @ParameterAnnotation(TEST)
  @Position(order = "7")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Code debuggen (Veel logging)",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String test;

  // Test Debug
  @ParameterAnnotation(MIJN_OVERHEID_BULK_UITTREKSELS)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Mijn-overheid bij bulk uittreksels",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String mijnOverheid;
  // TMV URL
  @ParameterAnnotation(TMV_URL)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Terugmeldingvoorziening URL",
      width = "400px")
  private String tmvUrl = "";

  @ParameterAnnotation(GV_KB_URL)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Schema URL",
      description = "URL van het Schema van gegevensverstrekking",
      width = "400px")
  private String gvSchemaUrl = "";

  @ParameterAnnotation(GV_DATUM_KENBAAR_MAKEN)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Termijn in dagen (kenbaar maken)",
      description = "Datum van einde termijn",
      width = "50px")
  private String gvDatumTermijnKenbaar;

  @ParameterAnnotation(GV_DATUM_VERSTREKKEN_GEEN_BEZW)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Termijn in dagen (verstrekken zonder bezwaar)",
      description = "Datum van einde termijn",
      width = "50px")
  private String gvDatumVerstrekkenZonderBezwaar = "";

  @ParameterAnnotation(GV_DATUM_VERSTREKKEN)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Termijn in dagen (verstrekken met bezwaar of geen reactie)",
      description = "Datum van einde termijn",
      width = "50px")
  private String gvDatumVerstrekkenMetBezwaar = "";

  @ParameterAnnotation(ONTBINDING_KB_URL)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Beslisboom kennisbank",
      description = "URL van de beslisboom om te bepalen of een echtscheiding kan worden ingeschreven in Nederlandse registers",
      width = "400px")
  private String ontbindingKbUrl = "";

  // Onderzoek
  @ParameterAnnotation(ONDERZ_FASE1_TERMIJN)
  @Position(order = "1")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Termijn in dagen 1e fase",
      width = "400px")
  private String onderzoekTermijnFase1 = "";

  @ParameterAnnotation(ONDERZ_FASE2_TERMIJN)
  @Position(order = "2")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Termijn in dagen 2e fase",
      width = "400px")
  private String onderzoekTermijnFase2 = "";

  @ParameterAnnotation(ONDERZ_EXTRA_TERMIJN)
  @Position(order = "3")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Termijn in dagen extra aanschrijving",
      width = "400px")
  private String onderzoekTermijnExtra = "";

  @ParameterAnnotation(ONDERZ_VOORNEMEN_TERMIJN)
  @Position(order = "4")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Termijn in dagen voornemen",
      width = "400px")
  private String onderzoekTermijnVoornemen = "";

  @ParameterAnnotation(ONDERZ_DEFAULT_AAND)
  @Position(order = "5")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Aanduiding gegevens in onderzoek",
      width = "400px")
  @Select(containerDataSource = AanduidingOnderzoekContainer.class,
      itemCaptionPropertyId = AanduidingOnderzoekContainer.OMSCHRIJVING)
  private String onderzoekAandGegevens = "";

  @ParameterAnnotation(ONDERZ_5_DAGEN_TERM)
  @Position(order = "6")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Binnen 5 dagen af te handelen",
      description = "Standaardwaarde voor \"Is het onderzoek binnen 5 dagen af te handelen?\"")
  @Select(containerDataSource = ParmEmptyBooleanContainer.class,
      itemCaptionPropertyId = ParmEmptyBooleanContainer.OMSCHRIJVING)
  private String afhanTerm5Dagen = "";

  @ParameterAnnotation(ONDERZ_ANDER_ORGAAN)
  @Position(order = "7")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Gedegen onderzoek ander orgaan",
      description = "Standaardwaarde voor \"Is er gedegen onderzoek door ander overheidsorgaan en beschikbaar?\"")
  @Select(containerDataSource = ParmEmptyBooleanContainer.class,
      itemCaptionPropertyId = ParmEmptyBooleanContainer.OMSCHRIJVING)
  private String onderzAnderOrgaan = "";

  @ParameterAnnotation(ONDERZ_REDEN_OVERSLAAN)
  @Position(order = "8")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Voldoende reden overslaan stappen",
      description = "Standaardwaarde voor vraag \"Is er voldoende reden om stap(pen) over te slaan?\"")
  @Select(containerDataSource = ParmEmptyBooleanContainer.class,
      itemCaptionPropertyId = ParmEmptyBooleanContainer.OMSCHRIJVING)
  private String onderzRedenOversl = "";

  // Verhuistermijn
  @ParameterAnnotation(VERHUIS_DATUM_LIMIET_TOEKOMST)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Verhuistermijn in dagen (toekomst)",
      description = "Maximaal aantal dagen tussen vandaag en de verhuisdatum. (leeg = geen controle).",
      width = "50px")
  private String verhuizingDatumLimietToekomst = "";

  // Verhuistermijn
  @ParameterAnnotation(VERHUIS_DATUM_LIMIET_VERLEDEN)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Verhuistermijn in dagen (verleden)",
      description = "Maximaal aantal dagen tussen verhuisdatum in het verleden en vandaag. (leeg = geen controle).",
      width = "50px")
  private String verhuizingDatumLimietVerleden = "";

  // Verhuistermijn
  @ParameterAnnotation(RISKANALYSIS_RELOCATION_IND)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Verhuizingindicatie",
      description = "De indicatie die gebruikt wordt om een persoon te markeren.",
      width = "100px")
  private String riskanalysisRelocationInd = "";

  // Verificatievraag
  @ParameterAnnotation(VER_VRAAG_ENDPOINT)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "URL van de verificatievraag",
      width = "400px")
  private String verificatievraagURL = "";

  // Wachtwoord verloop
  @ParameterAnnotation(WACHTWOORD_VERLOOP)
  @Position(order = "1")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Wachtw. verloop in dagen",
      description = "Het aantal dagen voor het verloop van het wachtwoord",
      validators = { GetalValidator.class },
      width = "40px")
  @TextField(maxLength = 3)
  private String wachtwoordVerloop = "";

  // Sessie timeout
  @ParameterAnnotation(SESSIE_TIMEOUT)

  @Position(order = "6")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Sessie timeout (in minuten)",
      validators = GetalValidator.class,
      width = "40px")
  @TextField(maxLength = 2)
  private String sessieTimeout = "15";

  // X-UA-Compatible
  @ParameterAnnotation(X_UA_COMPATIBLE)

  @Position(order = "7")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "IE 10/11 legacy mode (x-ua-compatible)")
  private String xUaCompatible = "";

  // Maximaal aantal zoekresultaten
  @ParameterAnnotation(ZOEK_MAX_FOUND_COUNT)
  @Position(order = "5")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Maximaal aantal zoekresultaten",
      description = "Het aantal persoonslijsten dat maximaal per zoekactie mag worden gezocht. " +
          "Dit geldt alleen voor zoeken in de gemeentelijke database.",
      validators = { GetalValidator.class },
      width = "60px")
  @TextField(maxLength = 4)
  private String zoekMaxAantalResultaten = "";

  @ParameterAnnotation(ZOEK_ALL_RECORDS)
  @Position(order = "6")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Mag alle zoekresultaten tonen",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String zoekAlleZoekresultaten;

  @ParameterAnnotation(ZOEK_PLE_JAVA_SERVER_USERNAME)
  @Position(order = "1")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gebruikersnaam profiel")
  private String gebruikerPersonenWsProfiel1 = "";

  // PLE wachtwoord
  @ParameterAnnotation(ZOEK_PLE_JAVA_SERVER_PW)
  @Position(order = "10")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Wachtwoord profiel")
  @TextField(secret = true)
  private String wachtwoordPersonenWsProfiel1 = "";

  // PLE Username
  @ParameterAnnotation(ZOEK_PERSONEN_PROFIEL2_USERNAME)
  @Position(order = "1")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gebruikersnaam profiel")
  private String gebruikerPersonenWsProfiel2 = "";

  // PLE wachtwoord
  @ParameterAnnotation(ZOEK_PERSONEN_PROFIEL2_PW)
  @Position(order = "10")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Wachtwoord profiel")
  @TextField(secret = true)
  private String wachtwoordPersonenWsProfiel2 = "";

  @ParameterAnnotation(ZOEK_PLE_BRON_GEMEENTE)
  @Position(order = "8",
      direction = Direction.VERTICAL_WITH_RULER)
  @Field(type = FieldType.NATIVE_SELECT,
      caption = "Zoeken in gemeente database",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String zoekPleBronGemeente = "";

  @ParameterAnnotation(ZOEK_PLE_BRON_GBAV)
  @Position(order = "9",
      direction = Direction.VERTICAL_WITH_RULER)
  @Field(type = FieldType.NATIVE_SELECT,
      caption = "Zoeken in landelijke database",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String zoekPleBronGbav = "";

  // Kassa Clear List
  @ParameterAnnotation(KASSA_TYPE)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Kassatype")
  @Select(containerDataSource = KassaLeverancierContainer.class,
      itemCaptionPropertyId = KassaLeverancierContainer.OMSCHRIJVING)
  private String kassaType = "";

  // Kassa Send Type
  @ParameterAnnotation(KASSA_SEND_TYPE)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Versturen via")
  @Select(containerDataSource = KassaVerstuurTypeContainer.class,
      itemCaptionPropertyId = KassaVerstuurTypeContainer.OMSCHRIJVING)
  private String kassaSendType = "";

  // PLE Naamgebruik
  @ParameterAnnotation(ZOEK_PLE_NAAMGEBRUIK)
  @Position(order = "7")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "PL gegevens naar naamgebruik",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String zoekPLENaamgebruik;

  // PLE Tonen administratieve historie
  @ParameterAnnotation(ZOEK_PLE_ADMIN_HIST)
  @Position(order = "7.1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Toon administratieve historie",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String zoekAdminHist;

  @ParameterAnnotation(EMAIL_HOST)
  @Position(order = "1")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "E-mail server",
      width = "400px")
  private String emailServer = "";

  @ParameterAnnotation(EMAIL_PORT)
  @Position(order = "2")
  @Field(customTypeClass = NumberField.class,
      caption = "Poort",
      width = "400px")
  private String emailPort = "";

  @ParameterAnnotation(EMAIL_USERNAME)
  @Position(order = "3")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gebruikersnaam",
      width = "400px")
  private String emailGebruikersnaam = "";

  @ParameterAnnotation(EMAIL_PW)
  @Position(order = "4")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Wachtwoord",
      width = "400px")
  @TextField(secret = true)
  private String emailWachtwoord = "";

  @ParameterAnnotation(EMAIL_EIGENSCHAPPEN)
  @Position(order = "9")
  @Field(type = FieldType.TEXT_AREA,
      caption = "Overige e-mail eigenschappen",
      width = "400px")
  @TextArea(rows = 5)
  private String emailEigenschappen = "";

  // PROBEV
  @ParameterAnnotation(PROBEV_GEBR_CODE)
  @Position(order = "1")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "PROBEV gebruiker code",
      validators = { GetalValidator.class },
      width = "90px")
  @TextField(maxLength = 10)
  private String probevGebrCode = "";

  // SMS
  @ParameterAnnotation(SMS_ENABLED)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "SMS service actief",
      width = "70px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String smsEnabled = "";

  @ParameterAnnotation(SMS_ENDPOINT)
  @Position(order = "10")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "SMS service endpoint",
      width = "400px")
  private String smsEndpoint = "";

  @ParameterAnnotation(SMS_USERNAME)
  @Position(order = "20")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "SMS gebruikersnaam",
      width = "200px")
  private String smsUsername = "";

  @ParameterAnnotation(SMS_PW)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "SMS wachtwoord",
      width = "200px")
  @Position(order = "30")
  @TextField(secret = true)
  private String smsPassword = "";

  // GEO
  @ParameterAnnotation(GEO_ENABLED)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Geo / BAG service actief",
      width = "70px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String geoEnabled = "";

  @ParameterAnnotation(GEO_ENDPOINT)
  @Position(order = "10")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geo / BAG service endpoint",
      width = "400px")
  private String geoEndpoint = "";

  @ParameterAnnotation(GEO_SEARCH_DEFAULT)
  @Position(order = "11")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Veld 'Bron gegevens' standaard op BAG",
      width = "70px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String geoSourceDefault = "";

  // RAAS
  @ParameterAnnotation(RAAS_ENDPOINT)
  @Position(order = "10")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "RAAS service endpoint",
      width = "400px")
  private String raasEndpoint = "";

  @ParameterAnnotation(RAAS_ENABLED)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "RAAS service actief",
      width = "70px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String raasEnabled = "";

  @ParameterAnnotation(RAAS_IDENT_BEWIJS)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Omschrijving bewijsstukken identiteit",
      width = "400px")
  @Position(order = "40")
  @TextField(maxLength = 80)
  private String raasBewijsIdentificatie = "";

  @ParameterAnnotation(SYSTEM_MIN_HD_SIZE)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Minimale grootte vrije ruimte in MB",
      width = "100px",
      required = true)
  @Position(order = "1")
  private String systemMinHdSize = "";

  public ParameterBean() {
  }

  public static List<ParameterBeanField> getFields(FieldFilter filter) {
    List<ParameterBeanField> list = new ArrayList<>();
    for (java.lang.reflect.Field field : ParameterBean.class.getDeclaredFields()) {
      ParameterAnnotation parmAnn = field.getAnnotation(ParameterAnnotation.class);
      Field fieldAnn = field.getAnnotation(Field.class);
      if (parmAnn != null && fieldAnn != null) {
        ParameterBeanField b = new ParameterBeanField(field, fieldAnn, parmAnn.value());
        if (filter.is(b)) {
          list.add(b);
        }
      }
    }
    return list;
  }

  /**
   * Vult de velden met de waarden uit de parameters
   */
  public void setFieldValues(Parameters parameters, long cUsr, long cProfile) throws IllegalAccessException {
    for (ParameterBeanField field : getFields((b) -> true)) {
      field.getField().set(this, astr(getValueFromParameter(parameters, field.getType(), cUsr, cProfile)));
    }
  }

  /**
   * geeft de waarde van de parameter terug
   */
  private String getValueFromParameter(Parameters parameters,
      ParameterType type, long cUsr, long cProfile) {

    return parameters.getAlle().stream()
        .filter(parm -> type.isKey(parm.getParm()))
        .filter(parm -> parm.isUserCode(cUsr) && parm.isProfileCode(cProfile))
        .findFirst()
        .map(Parm::getValue)
        .orElse(null);
  }

  public interface FieldFilter {

    boolean is(ParameterBeanField b);
  }

  @Data
  @AllArgsConstructor
  public static class ParameterBeanField {

    private java.lang.reflect.Field field;
    private Field                   fieldAnn;
    private ParameterType           type;

    public boolean isType(ParameterType type) {
      return this.type == type;
    }

    public boolean isFieldName(Object id) {
      return field.getName().equals(id);
    }
  }
}
