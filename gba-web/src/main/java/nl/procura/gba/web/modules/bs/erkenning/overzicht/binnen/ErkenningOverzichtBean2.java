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

package nl.procura.gba.web.modules.bs.erkenning.overzicht.binnen;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class ErkenningOverzichtBean2 implements Serializable {

  public static final String NAAM_MOEDER      = "naamMoeder";
  public static final String GEBOREN_MOEDER   = "geborenMoeder";
  public static final String NAAM_ERKENNER    = "naamErkenner";
  public static final String GEBOREN_ERKENNER = "geborenErkenner";

  public static final String TOESTEMMING_GEVER        = "toestemminggever";
  public static final String TOESTEMMING_RECHT_MOEDER = "toestemmingrechtMoeder";
  public static final String TOESTEMMING_RECHT_KIND   = "toestemmingrechtKind";
  public static final String VERKLARING_GEZAG         = "verklaringGezag";

  public static final String AFSTAMMINGS_RECHT   = "afstammingsRecht";
  public static final String KEUZE_GESLACHTSNAAM = "keuzegeslachtsnaam";
  public static final String KEUZE_VOORV         = "keuzevoorv";
  public static final String KEUZE_TITEL         = "keuzetitel";
  public static final String NAMENRECHT          = "namenRecht";
  public static final String NAAMSKEUZE          = "naamskeuze";
  public static final String NAAMSKEUZE_PERSOON  = "naamskeuzePersoon";

  @Field(type = FieldType.LABEL,
      caption = "Naam")
  private String naamMoeder = "";

  @Field(type = FieldType.LABEL,
      caption = "Geboren")
  private String geborenMoeder = "";

  @Field(type = FieldType.LABEL,
      caption = "Naam")
  private String naamErkenner = "";

  @Field(type = FieldType.LABEL,
      caption = "Geboren")
  private String geborenErkenner = "";

  @Field(type = FieldType.LABEL,
      caption = "Toegepast recht op de moeder")
  private FieldValue toestemmingrechtMoeder = new FieldValue();

  @Field(type = FieldType.LABEL,
      caption = "Toegepast recht op het kind")
  private FieldValue toestemmingrechtKind = new FieldValue();

  @Field(type = FieldType.LABEL,
      caption = "Verklaring moeder+erkenner m.b.t gezag bij moeder?")
  private String verklaringGezag = "";

  @Field(type = FieldType.LABEL,
      caption = "Toestemming gegeven door")
  private String toestemminggever = "";

  @Field(type = FieldType.LABEL,
      caption = "Toegepast recht van")
  private FieldValue afstammingsRecht = new FieldValue();

  @Field(type = FieldType.LABEL,
      caption = "Keuze geslachtsnaam")
  private String keuzegeslachtsnaam = "";

  @Field(type = FieldType.LABEL,
      caption = "Naam gekregen van")
  private String naamskeuzePersoon = "";

  @Field(type = FieldType.LABEL,
      caption = "Keuze voorvoegsel")
  private String keuzevoorv = "";

  @Field(type = FieldType.LABEL,
      caption = "Keuze titel")
  private FieldValue keuzetitel = new FieldValue();

  @Field(type = FieldType.LABEL,
      caption = "Toegepast recht van")
  private FieldValue namenRecht = new FieldValue();

  @Field(type = FieldType.LABEL,
      caption = "Naamskeuze")
  private String naamskeuze = "";

  public FieldValue getAfstammingsRecht() {
    return afstammingsRecht;
  }

  public void setAfstammingsRecht(FieldValue afstammingsRecht) {
    this.afstammingsRecht = afstammingsRecht;
  }

  public String getGeborenErkenner() {
    return geborenErkenner;
  }

  public void setGeborenErkenner(String geborenErkenner) {
    this.geborenErkenner = geborenErkenner;
  }

  public String getGeborenMoeder() {
    return geborenMoeder;
  }

  public void setGeborenMoeder(String geborenMoeder) {
    this.geborenMoeder = geborenMoeder;
  }

  public String getKeuzegeslachtsnaam() {
    return keuzegeslachtsnaam;
  }

  public void setKeuzegeslachtsnaam(String keuzegeslachtsnaam) {
    this.keuzegeslachtsnaam = keuzegeslachtsnaam;
  }

  public FieldValue getKeuzetitel() {
    return keuzetitel;
  }

  public void setKeuzetitel(FieldValue keuzetitel) {
    this.keuzetitel = keuzetitel;
  }

  public String getKeuzevoorv() {
    return keuzevoorv;
  }

  public void setKeuzevoorv(String keuzevoorv) {
    this.keuzevoorv = keuzevoorv;
  }

  public String getNaamErkenner() {
    return naamErkenner;
  }

  public void setNaamErkenner(String naamErkenner) {
    this.naamErkenner = naamErkenner;
  }

  public String getNaamMoeder() {
    return naamMoeder;
  }

  public void setNaamMoeder(String naamMoeder) {
    this.naamMoeder = naamMoeder;
  }

  public String getNaamskeuze() {
    return naamskeuze;
  }

  public void setNaamskeuze(String naamskeuze) {
    this.naamskeuze = naamskeuze;
  }

  public String getNaamskeuzePersoon() {
    return naamskeuzePersoon;
  }

  public void setNaamskeuzePersoon(String naamskeuzePersoon) {
    this.naamskeuzePersoon = naamskeuzePersoon;
  }

  public FieldValue getNamenRecht() {
    return namenRecht;
  }

  public void setNamenRecht(FieldValue namenRecht) {
    this.namenRecht = namenRecht;
  }

  public String getToestemminggever() {
    return toestemminggever;
  }

  public void setToestemminggever(String toestemminggever) {
    this.toestemminggever = toestemminggever;
  }

  public FieldValue getToestemmingrechtKind() {
    return toestemmingrechtKind;
  }

  public void setToestemmingrechtKind(FieldValue toestemmingrechtKind) {
    this.toestemmingrechtKind = toestemmingrechtKind;
  }

  public FieldValue getToestemmingrechtMoeder() {
    return toestemmingrechtMoeder;
  }

  public void setToestemmingrechtMoeder(FieldValue toestemmingrechtMoeder) {
    this.toestemmingrechtMoeder = toestemmingrechtMoeder;
  }

  public String getVerklaringGezag() {
    return verklaringGezag;
  }

  public void setVerklaringGezag(String verklaringGezag) {
    this.verklaringGezag = verklaringGezag;
  }
}
