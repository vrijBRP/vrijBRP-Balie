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

package nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.subpages;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaBsnField;
import nl.procura.gba.web.modules.zaken.reisdocument.page18.ReisdocumentValidator;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.AnrField;
import nl.procura.vaadin.component.field.DatumVeld;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Tab1RaasPage2Bean implements Serializable {

  public static final String CODE             = "code";
  public static final String STATUS_VERW      = "statusVerw";
  public static final String AANVRAAGNUMMER   = "aanvraagNummer";
  public static final String NR_NL_DOC        = "nrNLDoc";
  public static final String C_RAAS           = "cRaas";
  public static final String DATUM_TIJD_AANVR = "datumTijdAanvr";
  public static final String STATUS_AANVR     = "statusAanvr";
  public static final String IND_SPOED        = "indSpoed";
  public static final String DATUM_TIJD_LEV   = "datumTijdLev";
  public static final String STATUS_LEV       = "statusLev";
  public static final String DATUM_TIJD_AFSL  = "datumTijdAfsl";
  public static final String STATUS_AFSL      = "statusAfsl";
  public static final String NR_NL_DOC_IN     = "nrNLDocIn";
  public static final String RDM_DOC          = "rdmDoc";
  public static final String D_VERSTREK       = "dVerstrek";
  public static final String D_GELD_END       = "datumGeldigEnd";
  public static final String ANR              = "anr";
  public static final String BSN              = "bsn";
  public static final String VOORV            = "voorv";
  public static final String VOORN            = "voorn";
  public static final String TP               = "tp";
  public static final String NAAM             = "naam";
  public static final String D_GEB            = "dGeb";
  public static final String P_GEB            = "pGeb";
  public static final String L_GEB            = "lGeb";
  public static final String GESLACHT         = "geslacht";
  public static final String NAAMGEBRUIK      = "naamgebruik";
  public static final String NATIO            = "natio";
  public static final String AAND_BIJZ_NL     = "aandBijzNl";
  public static final String NAAM_P           = "naamP";
  public static final String VOORV_P          = "voorvP";
  public static final String TP_P             = "tpP";
  public static final String HUW_SRT          = "huwSrt";
  public static final String RDN_ONTB         = "rdnOntb";
  public static final String STRAAT           = "straat";
  public static final String HNR              = "hnr";
  public static final String HNR_L            = "hnrL";
  public static final String HNR_T            = "hnrT";
  public static final String HNR_A            = "hnrA";
  public static final String PC               = "postcode";
  public static final String GEM_DEEL         = "gemeentedeel";
  public static final String LOCATIE          = "locatie";
  public static final String LENGTE           = "lengte";
  public static final String AUTORIT          = "autorit";
  public static final String IND_IDENT        = "indIdent";
  public static final String BEWIJS_IDENT     = "bewijsIdent";
  public static final String TOESTEM          = "toestem";
  public static final String NR_VB_DOC        = "nrVbDoc";
  public static final String D_VB_DOC         = "datumVbDoc";
  public static final String IND_VERM         = "indVerm";
  public static final String D_VERM           = "datumVerm";
  public static final String PV_VERM          = "pvVerm";
  public static final String NR_VERM          = "nrVerm";
  public static final String AUTORIT_VERM     = "autoritVerm";
  public static final String VERZOEK_VERM     = "verzoekVerm";
  public static final String CL_I             = "clI";
  public static final String CL_IV            = "clIV";
  public static final String CL_V             = "clV";
  public static final String CL_XA            = "clXA";
  public static final String CL_XB            = "clXB";
  public static final String CL_XIB           = "clXIB";
  public static final String CL_XII           = "clXII";
  public static final String NIET_AANW        = "nietAanw";
  public static final String UPDATE_PROWEB    = "updateProweb";
  public static final String UPDATE_PROBEV    = "updateProbev";

  @Field(type = Field.FieldType.LABEL,
      caption = "Code")
  private Object code = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Status verwerking bericht")
  private Object statusVerw = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Aanvraagnummer")
  private Object aanvraagNummer = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Nummer reisdocument")
  private Object nrNLDoc = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "LocatieCode")
  private Object cRaas = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Datum / tijd aanvraag")
  private Object datumTijdAanvr = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Status aanvraag")
  private Object statusAanvr = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Indicatie Spoed")
  private Object indSpoed = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Datum / tijd levering")
  private Object datumTijdLev = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Status levering")
  private Object statusLev = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Datum / tijd afsluiting")
  private Object datumTijdAfsl = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Status afsluiting")
  private Object statusAfsl = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Huidig reisdocument ingeleverd")
  @TextField(nullRepresentation = "")
  private Object nrNLDocIn = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Soort reisdocument")
  @TextField(nullRepresentation = "")
  private Object rdmDoc = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Datum Verstrekking")
  private Object dVerstrek = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Datum einde geldigheid")
  private Object datumGeldigEnd = "";

  @Field(customTypeClass = AnrField.class,
      caption = "A-nummer",
      required = true)
  private Object anr = null;

  @Field(customTypeClass = GbaBsnField.class,
      caption = "Burgerservicenummer",
      required = true)
  private Object bsn = null;

  @Field(type = Field.FieldType.LABEL,
      caption = "Voorna(a)m(en)")
  @TextField(nullRepresentation = "",
      maxLength = 200)
  private Object voorn = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Voorvoegsel")
  @Select(nullSelectionAllowed = false)
  private Object voorv;

  @Field(type = Field.FieldType.LABEL,
      caption = "Geslachtsnaam",
      required = true)
  @TextField(nullRepresentation = "",
      maxLength = 200)
  private Object naam = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Titel/predikaat")
  private Object tp = null;

  @Field(type = Field.FieldType.LABEL,
      caption = "Geboortedatum",
      required = true)
  private Object dGeb = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Geboorteplaats")
  @TextField(nullRepresentation = "")
  private Object pGeb = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Geboorteland")
  @TextField(nullRepresentation = "")
  private Object lGeb = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Geslacht")
  private Object geslacht;

  @Field(type = Field.FieldType.LABEL,
      caption = "Naamgebruik")
  private Object naamgebruik;

  @Field(type = Field.FieldType.LABEL,
      caption = "Nationaliteit")
  @TextField(nullRepresentation = "")
  private Object natio = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Aand. bijzonder Nederlanderschap")
  private Object aandBijzNl;

  @Field(type = Field.FieldType.LABEL,
      caption = "Naam")
  @TextField(nullRepresentation = "")
  private Object naamP = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Voorvoegsel")
  private Object voorvP;

  @Field(type = Field.FieldType.LABEL,
      caption = "Titel/predikaat")
  private Object tpP;

  @Field(type = Field.FieldType.LABEL,
      caption = "Soort partnerschap")
  private Object huwSrt;

  @Field(type = Field.FieldType.LABEL,
      caption = "Reden ontbinding")
  private Object rdnOntb;

  @Field(type = Field.FieldType.LABEL,
      caption = "Straat")
  @TextField(nullRepresentation = "")
  private Object straat = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Huisnummer")
  private Object hnr = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Huisletter")
  @TextField(nullRepresentation = "")
  private Object hnrL = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Huisnummertoevoeging")
  @TextField(nullRepresentation = "")
  private Object hnrT = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Huisnummeraanduiding")
  private Object hnrA;

  @Field(type = Field.FieldType.LABEL,
      caption = "Postcode")
  @TextField(nullRepresentation = "")
  private Object postcode = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Locatie")
  @TextField(nullRepresentation = "")
  private Object locatie = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Gemeentedeel")
  @TextField(nullRepresentation = "")
  private Object gemeentedeel = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Lengte",
      required = true)
  private Object lengte = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Autoriteit",
      required = true)
  @TextField(nullRepresentation = "")
  private Object autorit = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Identiteit vastgesteld")
  private Object indIdent = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Bewijsstukken identiteit")
  @TextField(nullRepresentation = "")
  private Object bewijsIdent = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Aantal toestemmingen")
  private Object toestem = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Nummer")
  @TextField(nullRepresentation = "")
  private Object nrVbDoc = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Datum einde geldigheid")
  private Object datumVbDoc = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Aanduiding",
      width = "90px")
  @Immediate
  private Object indVerm;

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum vermissing",
      width = "90px")
  private Object datumVerm;

  @Field(type = Field.FieldType.LABEL,
      caption = "Proces-verbaal",
      width = "250px")
  @TextField(nullRepresentation = "",
      maxLength = 25)
  private Object pvVerm = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Nummer reisdocument",
      width = "90px",
      validators = ReisdocumentValidator.class)
  @TextField(nullRepresentation = "",
      maxLength = 9)
  private Object nrVerm = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Autoriteit reisdocument",
      width = "250px")
  @TextField(nullRepresentation = "",
      maxLength = 60)
  private Object autoritVerm = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Verzoek",
      width = "250px")
  @TextField(nullRepresentation = "",
      maxLength = 40)
  private Object verzoekVerm = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "I (vermelding partner)")
  private Object clI;

  @Field(type = Field.FieldType.LABEL,
      caption = "IV  (pseudoniem)")
  @TextField(nullRepresentation = "")
  private Object clIV = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "V  (niet in staat tot ondertekening)")
  private Object clV;

  @Field(type = Field.FieldType.LABEL,
      caption = "XA  (uitgezonderde landen)")
  @TextField(nullRepresentation = "")
  private Object clXA = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "XB  (geldig voor reizen naar)")
  @TextField(nullRepresentation = "")
  private Object clXB = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "XIb XXA  (staatloze)")
  private Object clXIB;

  @Field(type = Field.FieldType.LABEL,
      caption = "XII  (dit paspoort is afgegeven ter vervanging van)")
  @TextField(nullRepresentation = "")
  private Object clXII = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Reden niet aanwezig")
  @TextField(nullRepresentation = "")
  private Object nietAanw = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Update zaak")
  private Object updateProweb = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Update persoonslijst")
  private Object updateProbev = "";
}
