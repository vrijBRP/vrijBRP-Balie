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

package nl.procura.gba.web.modules.zaken.reisdocument.page11;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.ExtendedMultiField;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.UpperCaseField;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.StaatloosType;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.TekenenType;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.VermeldPartnerType;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.VermeldTitelType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page11ReisdocumentBean1 implements Serializable {

  public static final String VERMELDING_PARTNER   = "vermeldingPartner";
  public static final String VERMELDING_TITEL     = "vermeldingTitel";
  public static final String VERMELDING_LAND      = "vermeldingLand";
  public static final String PSEUDONIEM           = "pseudoniem";
  public static final String ONDERTEKENING        = "ondertekening";
  public static final String UITGEZONDERDELANDEN  = "uitgezonderdeLanden";
  public static final String GELDIGVOORREIZENNAAR = "geldigVoorReizenNaar";
  public static final String STAATLOOS            = "staatloos";
  public static final String TERVERVANGINGVAN     = "terVervangingVan";
  public static final String TERMIJNGELD          = "termijnGeld";
  public static final String DGELDEND             = "dGeldEnd";
  public static final String DGELDENDOMS          = "dGeldEndOms";
  public static final String NATIOGBA             = "natioGBA";
  public static final String DVBDOC               = "dVbDoc";
  public static final String NRVBDOC              = "nrVbDoc";
  public static final String BUITENLDOC           = "buitenlDoc";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Vermelding partner",
      width = "200px")
  @Immediate
  @Select(nullSelectionAllowed = false)
  private VermeldPartnerType vermeldingPartner = VermeldPartnerType.NIET_VAN_TOEPASSING;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Vermelding titel / predikaat",
      width = "200px",
      required = true)
  @Immediate
  @Select(nullSelectionAllowed = false)
  private VermeldTitelType vermeldingTitel = VermeldTitelType.NVT;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Pseudoniem",
      width = "400px")
  @TextField(maxLength = 255)
  private String pseudoniem = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Ondertekening",
      width = "200px")
  @Select(containerDataSource = OndertekenContainer.class,
      nullSelectionAllowed = false)
  private TekenenType ondertekening = TekenenType.KAN_TEKENEN;

  @Field(customTypeClass = ExtendedMultiField.class,
      caption = "Uitgezonderde landen",
      width = "400px")
  @TextField(maxLength = 255)
  private String uitgezonderdeLanden = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geldig voor reizen naar",
      width = "400px")
  @TextField(maxLength = 255)
  private String geldigVoorReizenNaar = "";

  @Field(type = FieldType.LABEL,
      caption = "Staatloos",
      readOnly = true)
  private StaatloosType staatloos = StaatloosType.NIET_VAN_TOEPASSING;

  @Field(customTypeClass = UpperCaseField.class,
      caption = "Ter vervanging van",
      width = "200px",
      validators = { VervangendReisdocumentValidator.class })
  @TextField(maxLength = 9)
  private String terVervangingVan = "";

  @Field(customTypeClass = ProNativeSelect.class,
      width = "200px",
      caption = "Geldigheidstermijn")
  @Select(nullSelectionAllowed = false)
  @Immediate
  private FieldValue termijnGeld = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum einde geldigheid",
      width = "200px",
      required = true,
      visible = false)
  private DateFieldValue dGeldEnd = null;

  @Field(type = FieldType.LABEL,
      visible = false)
  private String dGeldEndOms = "";

  @Field(type = FieldType.LABEL,
      caption = "Nationaliteit in BRP")
  private String natioGBA = "";

  @Field(customTypeClass = DatumVeld.class,
      caption = "Geldig tot",
      width = "80px",
      required = true)
  private DateFieldValue dVbDoc = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Nummer verblijfsdocument",
      width = "100px",
      required = true)
  private String nrVbDoc = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "In bezit buitenlands reisdocument",
      width = "100px",
      required = true)
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private Boolean buitenlDoc;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Vermelding land achter geboorteplaats",
      width = "400px",
      required = true)
  @Immediate
  @Select(itemCaptionPropertyId = LandvermeldContainer.OMSCHRIJVING)
  private Boolean vermeldingLand;

  public DateFieldValue getdGeldEnd() {
    return dGeldEnd;
  }

  public void setdGeldEnd(DateFieldValue dGeldEnd) {
    this.dGeldEnd = dGeldEnd;
  }

  public String getdGeldEndOms() {
    return dGeldEndOms;
  }

  public void setdGeldEndOms(String dGeldEndOms) {
    this.dGeldEndOms = dGeldEndOms;
  }

  public DateFieldValue getdVbDoc() {
    return dVbDoc;
  }

  public void setdVbDoc(DateFieldValue dVbDoc) {
    this.dVbDoc = dVbDoc;
  }

  public String getGeldigVoorReizenNaar() {
    return geldigVoorReizenNaar;
  }

  public void setGeldigVoorReizenNaar(String geldigVoorReizenNaar) {
    this.geldigVoorReizenNaar = geldigVoorReizenNaar;
  }

  public String getNatioGBA() {
    return natioGBA;
  }

  public void setNatioGBA(String natioGBA) {
    this.natioGBA = natioGBA;
  }

  public String getNrVbDoc() {
    return nrVbDoc;
  }

  public void setNrVbDoc(String nrVbDoc) {
    this.nrVbDoc = nrVbDoc;
  }

  public TekenenType getOndertekening() {
    return ondertekening;
  }

  public void setOndertekening(TekenenType ondertekening) {
    this.ondertekening = ondertekening;
  }

  public String getPseudoniem() {
    return pseudoniem;
  }

  public void setPseudoniem(String pseudoniem) {
    this.pseudoniem = pseudoniem;
  }

  public StaatloosType getStaatloos() {
    return staatloos;
  }

  public void setStaatloos(StaatloosType staatloos) {
    this.staatloos = staatloos;
  }

  public FieldValue getTermijnGeld() {
    return termijnGeld;
  }

  public void setTermijnGeld(FieldValue termijnGeld) {
    this.termijnGeld = termijnGeld;
  }

  public String getTerVervangingVan() {
    return terVervangingVan;
  }

  public void setTerVervangingVan(String terVervangingVan) {
    this.terVervangingVan = terVervangingVan;
  }

  public String getUitgezonderdeLanden() {
    return uitgezonderdeLanden;
  }

  public void setUitgezonderdeLanden(String uitgezonderdeLanden) {
    this.uitgezonderdeLanden = uitgezonderdeLanden;
  }

  public VermeldPartnerType getVermeldingPartner() {
    return vermeldingPartner;
  }

  public void setVermeldingPartner(VermeldPartnerType vermeldingPartner) {
    this.vermeldingPartner = vermeldingPartner;
  }

  public VermeldTitelType getVermeldingTitel() {
    return vermeldingTitel;
  }

  public void setVermeldingTitel(VermeldTitelType vermeldingTitel) {
    this.vermeldingTitel = vermeldingTitel;
  }

  public Boolean getBuitenlDoc() {
    return buitenlDoc;
  }

  public void setBuitenlDoc(Boolean buitenlDoc) {
    this.buitenlDoc = buitenlDoc;
  }

  public Boolean getVermeldingLand() {
    return vermeldingLand;
  }

  public void setVermeldingLand(Boolean vermeldingLand) {
    this.vermeldingLand = vermeldingLand;
  }
}
