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

package nl.procura.gba.web.modules.zaken.verhuizing.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2VerhuizingBean1 implements Serializable {

  public static final String ADRES         = "adres";
  public static final String FUNCTIE_ADRES = "functieadres";
  public static final String PCPLAATS      = "pcPlaats";
  public static final String AANTAL        = "aantal";
  public static final String INWONING      = "inwoning";
  public static final String HOOFDBEWONER  = "hoofdbewoner";
  public static final String TOESTEMMING   = "toestemming";
  public static final String BESTEMMING    = "bestemming";
  public static final String AANGIFTE      = "aangifte";

  @Field(type = FieldType.LABEL,
      caption = "Adres")
  private String adres = "";

  @Field(type = FieldType.LABEL,
      caption = "Functie adres")
  private String functieadres = "";

  @Field(type = FieldType.LABEL,
      caption = "Postcode / plaats")
  private String pcPlaats = "";

  @Field(type = FieldType.LABEL,
      caption = "Sprake van inwoning")
  private String inwoning = "";

  @Field(type = FieldType.LABEL,
      caption = "Aantal personen op nieuw adres")
  private String aantal = "";

  @Field(type = FieldType.LABEL,
      caption = "BSN hoofdbewoner")
  private String hoofdbewoner = "";

  @Field(type = FieldType.LABEL,
      caption = "Toestemming")
  private String toestemming = "";

  @Field(type = FieldType.LABEL,
      caption = "Bestemming huidige bewoners")
  private String bestemming = "";

  @Field(type = FieldType.LABEL,
      caption = "Aangifte geaccepteerd")
  private String aangifte = "";

  public String getAangifte() {
    return aangifte;
  }

  public void setAangifte(String aangifte) {
    this.aangifte = aangifte;
  }

  public String getAantal() {
    return aantal;
  }

  public void setAantal(String aantal) {
    this.aantal = aantal;
  }

  public String getAdres() {
    return adres;
  }

  public void setAdres(String adres) {
    this.adres = adres;
  }

  public String getBestemming() {
    return bestemming;
  }

  public void setBestemming(String bestemming) {
    this.bestemming = bestemming;
  }

  public String getFunctieadres() {
    return functieadres;
  }

  public void setFunctieadres(String functieadres) {
    this.functieadres = functieadres;
  }

  public String getHoofdbewoner() {
    return hoofdbewoner;
  }

  public void setHoofdbewoner(String hoofdbewoner) {
    this.hoofdbewoner = hoofdbewoner;
  }

  public String getInwoning() {
    return inwoning;
  }

  public void setInwoning(String inwoning) {
    this.inwoning = inwoning;
  }

  public String getPcPlaats() {
    return pcPlaats;
  }

  public void setPcPlaats(String pcPlaats) {
    this.pcPlaats = pcPlaats;
  }

  public String getToestemming() {
    return toestemming;
  }

  public void setToestemming(String toestemming) {
    this.toestemming = toestemming;
  }
}
