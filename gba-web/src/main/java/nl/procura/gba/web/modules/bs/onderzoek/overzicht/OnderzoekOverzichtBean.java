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

package nl.procura.gba.web.modules.bs.onderzoek.overzicht;

import static nl.procura.vaadin.annotation.field.Field.FieldType.LABEL;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class OnderzoekOverzichtBean implements Serializable {

  public static final String BRON                       = "bron";
  public static final String NAAM                       = "naam";
  public static final String DOSSIER_NR_TMV             = "dossiernrTmv";
  public static final String AFDELING                   = "afdeling";
  public static final String INSTANTIE                  = "instantie";
  public static final String ADRES                      = "adres";
  public static final String TAV                        = "tav";
  public static final String PC                         = "pc";
  public static final String PLAATS                     = "plaats";
  public static final String KENMERK                    = "kenmerk";
  public static final String DATUM_ONTVANGST            = "datumOntvangst";
  public static final String AARD                       = "aard";
  public static final String VERMOED_ADRES              = "vermoedAdres";
  public static final String BINNEN_5_DAGEN             = "binnen5dagen";
  public static final String REDEN                      = "reden";
  public static final String DATUM_AANVANG_ONDERZOEK    = "datumAanvangOnderzoek";
  public static final String AAND_GEG_ONDERZOEK         = "aandGegOnderzoek";
  public static final String ONDERZOEK_DOOR_ANDERGEDAAN = "onderzoekDoorAnderGedaan";
  public static final String VOLDOENDE_REDEN            = "voldoendeReden";
  public static final String TOELICHTING1               = "toelichting1";

  //processverloop
  public static final String START_FASE1_OP    = "startFase1Op";
  public static final String START_FASE1_TM    = "startFase1Tm";
  public static final String REACTIE_ONTVANGEN = "reactieOntvangen";
  public static final String TOELICHTING2      = "toelichting2";
  public static final String VERVOLGACTIES     = "vervolgacties";

  public static final String START_FASE2_OP        = "startFase2Op";
  public static final String START_FASE2_TM        = "startFase2Tm";
  public static final String EXTERNE_BRON1         = "externebron1";
  public static final String EXTERNE_BRON2         = "externebron2";
  public static final String ONDERZOEK_TER_PLAATSE = "onderzoekTerPlaatse";
  public static final String UITGEVOERD_OP         = "uitgevoerdOp";
  public static final String TOELICHTING3          = "toelichting3";

  //Resultaat
  public static final String BETROKKENEN           = "betrokkenen";
  public static final String DATUM_EINDE_ONDERZOEK = "datumEindeOnderzoek";

  public static final String RESULTAAT_NOGMAALS = "resultaatNogmaals";
  public static final String RESULTAAT_ADRES    = "resultaatAdres";
  public static final String RESULTAAT_PC       = "resultaatPc";
  public static final String RESULTAAT_PCGEM    = "resultaatPcGemeente";
  public static final String RESULTAAT_BUITENL1 = "resultaatBuitenl1";
  public static final String RESULTAAT_BUITENL2 = "resultaatBuitenl2";
  public static final String RESULTAAT_BUITENL3 = "resultaatBuitenl3";
  public static final String RESULTAAT_LAND     = "resultaatLand";
  public static final String RESULTAAT_TOEL     = "resultaatToel";

  @Field(type = LABEL,
      caption = "Bron")
  private Object bron = null;

  @Field(type = LABEL,
      caption = "Naam (persoon)")
  private Object naam = null;

  @Field(type = LABEL,
      caption = "Dossiernummer TMV")
  private Object dossiernrTmv = null;

  @Field(type = LABEL,
      caption = "Naam instantie")
  private Object instantie = null;

  @Field(type = LABEL,
      caption = "Naam afdeling")
  private Object afdeling = null;

  @Field(type = LABEL,
      caption = "Ter attentie ")
  private Object tav = null;

  @Field(type = LABEL,
      caption = "Adres")
  private Object adres = null;

  @Field(type = LABEL,
      caption = "Postcode")
  private Object pc = null;

  @Field(type = LABEL,
      caption = "Plaats")
  private Object plaats = null;

  @Field(type = LABEL,
      caption = "Kenmerk")
  private Object kenmerk = null;

  @Field(type = LABEL,
      caption = "Datum ontvangst")
  private Object datumOntvangst = null;

  @Field(type = LABEL,
      caption = "Reden")
  private Object aard = null;

  @Field(type = LABEL,
      caption = "Vermoedelijk adres")
  private Object vermoedAdres = null;

  @Field(type = LABEL,
      caption = "Binnen 5 dagen")
  private Object binnen5dagen = null;

  @Field(type = LABEL,
      caption = "Reden")
  private Object reden = null;

  @Field(type = LABEL,
      caption = "datum aanvang onderzoek")
  private Object datumAanvangOnderzoek = null;

  @Field(type = LABEL,
      caption = "Aanduiding gegevens in onderzoek")
  private Object aandGegOnderzoek = null;

  @Field(type = LABEL,
      caption = "Gedegen onderzoek ander overheidsorgaan beschikbaar")
  private Object onderzoekDoorAnderGedaan = null;

  @Field(type = LABEL,
      caption = "Reden om stap(pen) over te slaan")
  private Object voldoendeReden = null;

  @Field(type = LABEL,
      caption = "Toelichting")
  private Object toelichting1 = null;

  // procesverloop

  @Field(type = LABEL,
      caption = "Start 1e fase op")
  private Object startFase1Op = null;

  @Field(type = LABEL,
      caption = "Datum einde termijn")
  private Object startFase1Tm = null;

  @Field(type = LABEL,
      caption = "Reactie ontvangen?")
  private Object reactieOntvangen;

  @Field(type = LABEL,
      caption = "Toelichting")
  private Object toelichting2 = "";

  @Field(type = LABEL,
      caption = "Vervolgactie(s) noodzakelijk?")
  private Object vervolgacties;

  @Field(type = LABEL,
      caption = "Start 2e fase op")
  private Object startFase2Op = null;

  @Field(type = LABEL,
      caption = "Datum einde termijn")
  private Object startFase2Tm = null;

  @Field(type = LABEL,
      caption = "Onderzoek ter plaatse gewenst?")
  private Object onderzoekTerPlaatse;

  @Field(type = LABEL,
      caption = "Uitgevoerd op")
  private Object uitgevoerdOp = null;

  @Field(type = LABEL,
      caption = "Toelichting")
  private Object toelichting3 = "";

  @Field(type = LABEL,
      caption = "Betrokkene(n) is/zijn")
  private Object betrokkenen = null;

  @Field(type = LABEL,
      caption = "Datum einde onderzoek")
  private Object datumEindeOnderzoek = null;

  @Field(type = LABEL,
      caption = "Nogmaals aanschrijven")
  private Object resultaatNogmaals = null;

  @Field(type = LABEL,
      caption = "Adres")
  private Object resultaatAdres = null;

  @Field(type = LABEL,
      caption = "Postcode")
  private Object resultaatPc = null;

  @Field(type = LABEL,
      caption = "Postcode / gemeente")
  private Object resultaatPcGemeente = null;

  @Field(type = LABEL,
      caption = "Adres")
  private Object resultaatBuitenl1 = null;

  @Field(type = LABEL,
      caption = "Woonplaats")
  private Object resultaatBuitenl2 = null;

  @Field(type = LABEL,
      caption = "Gemeente, district, provincie, eiland")
  private Object resultaatBuitenl3 = null;

  @Field(type = LABEL,
      caption = "Land")
  private Object resultaatLand = null;

  @Field(type = LABEL,
      caption = "Toelichting")
  private Object resultaatToel = null;

}
