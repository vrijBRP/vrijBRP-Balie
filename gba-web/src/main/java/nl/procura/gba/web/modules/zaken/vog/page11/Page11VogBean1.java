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

package nl.procura.gba.web.modules.zaken.vog.page11;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.HuisnummerVeld;
import nl.procura.gba.web.services.zaken.vog.VogBelanghebbende;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.field.PostalcodeField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page11VogBean1 implements Serializable {

  public static final String SNELKEUZE         = "snelkeuze";
  public static final String NAAM              = "naam";
  public static final String VERTEGENWOORDIGER = "vertegenwoordiger";
  public static final String STRAAT            = "straat";
  public static final String HNR               = "hnr";
  public static final String HNRL              = "hnrL";
  public static final String HNRT              = "hnrT";
  public static final String POSTCODE          = "postcode";
  public static final String PLAATS            = "plaats";
  public static final String LAND              = "land";
  public static final String TELEFOON          = "telefoon";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Snelkeuze")
  @Immediate
  private VogBelanghebbende snelkeuze         = null;
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Naam",
      width = "400px",
      required = true)
  @TextField(maxLength = 75)
  private String            naam              = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Vertegenwoordiger",
      width = "400px",
      required = true)
  @TextField(maxLength = 75)
  private String            vertegenwoordiger = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Adres",
      required = true,
      width = "200px")
  @TextField(maxLength = 35)
  @InputPrompt(text = "Straat")
  private String            straat            = "";
  @Field(customTypeClass = HuisnummerVeld.class,
      caption = "Huisnummer",
      width = "30px",
      description = "Huisnummer")
  @InputPrompt(text = "Nr.")
  private String            hnr               = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Huisletter",
      width = "30px",
      description = "Huisletter")
  @InputPrompt(text = "L")
  @TextField(maxLength = 1)
  private String            hnrL              = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Toevoeging",
      width = "50px",
      description = "Toevoeging")
  @InputPrompt(text = "T")
  private String            hnrT              = "";

  @Field(customTypeClass = PostalcodeField.class,
      caption = "Postcode / Plaats",
      width = "60px")
  private FieldValue postcode = null;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Plaats",
      required = true,
      width = "134px")
  @TextField(maxLength = 40)
  private String plaats = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Land",
      required = true,
      width = "200px")
  @Select(containerDataSource = LandContainer.class)
  private FieldValue land = null;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Telefoon")
  private String telefoon = "";

  public String getHnr() {
    return hnr;
  }

  public void setHnr(String hnr) {
    this.hnr = hnr;
  }

  public String getHnrL() {
    return hnrL;
  }

  public void setHnrL(String hnrL) {
    this.hnrL = hnrL;
  }

  public String getHnrT() {
    return hnrT;
  }

  public void setHnrT(String hnrT) {
    this.hnrT = hnrT;
  }

  public FieldValue getLand() {
    return land;
  }

  public void setLand(FieldValue land) {
    this.land = land;
  }

  public String getNaam() {
    return naam;
  }

  public void setNaam(String naam) {
    this.naam = naam;
  }

  public String getPlaats() {
    return plaats;
  }

  public void setPlaats(String plaats) {
    this.plaats = plaats;
  }

  public FieldValue getPostcode() {
    return postcode;
  }

  public void setPostcode(FieldValue postcode) {
    this.postcode = postcode;
  }

  public VogBelanghebbende getSnelkeuze() {
    return snelkeuze;
  }

  public void setSnelkeuze(VogBelanghebbende snelkeuze) {
    this.snelkeuze = snelkeuze;
  }

  public String getStraat() {
    return straat;
  }

  public void setStraat(String straat) {
    this.straat = straat;
  }

  public String getTelefoon() {
    return telefoon;
  }

  public void setTelefoon(String telefoon) {
    this.telefoon = telefoon;
  }

  public String getVertegenwoordiger() {
    return vertegenwoordiger;
  }

  public void setVertegenwoordiger(String vertegenwoordiger) {
    this.vertegenwoordiger = vertegenwoordiger;
  }
}
