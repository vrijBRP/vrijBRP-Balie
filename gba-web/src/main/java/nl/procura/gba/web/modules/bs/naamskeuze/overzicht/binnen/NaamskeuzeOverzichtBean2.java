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

package nl.procura.gba.web.modules.bs.naamskeuze.overzicht.binnen;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class NaamskeuzeOverzichtBean2 implements Serializable {

  public static final String NAAM_MOEDER     = "naamMoeder";
  public static final String GEBOREN_MOEDER  = "geborenMoeder";
  public static final String NAAM_PARTNER    = "naamPartner";
  public static final String GEBOREN_PARTNER = "geborenPartner";

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
  private String naamPartner = "";

  @Field(type = FieldType.LABEL,
      caption = "Geboren")
  private String geborenPartner = "";

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

  public String getGeborenPartner() {
    return geborenPartner;
  }

  public void setGeborenPartner(String geborenPartner) {
    this.geborenPartner = geborenPartner;
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

  public String getNaamPartner() {
    return naamPartner;
  }

  public void setNaamPartner(String naamPartner) {
    this.naamPartner = naamPartner;
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
}
