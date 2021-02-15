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

package nl.procura.gbaws.web.vaadin.module.sources.gbav.page3;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.ProTextField;

@FormFieldFactoryBean(accessType = ElementType.FIELD,
    defaultRequiredError = "Veld \"{}\" is verplicht.")
public class Page3DbGbavBean implements Serializable {

  public static final String PW           = "password";
  public static final String CODE         = "code";
  public static final String LETTER       = "letter";
  public static final String OMSCHRIJVING = "omschrijving";
  public static final String REFERENTIE   = "referentie";

  @Field(customTypeClass = ProTextField.class,
      width = "200px",
      caption = "GBA-V wachtwoord",
      required = true)
  @TextField(nullRepresentation = "",
      maxLength = 250)
  private String password = "";

  @Field(type = FieldType.LABEL,
      caption = "Code")
  private String code = "";

  @Field(type = FieldType.LABEL,
      caption = "Letter")
  private String letter = "";

  @Field(type = FieldType.LABEL,
      caption = "Omschrijving")
  private String omschrijving = "";

  @Field(type = FieldType.LABEL,
      caption = "Referentie")
  private String referentie = "";

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getLetter() {
    return letter;
  }

  public void setLetter(String letter) {
    this.letter = letter;
  }

  public String getOmschrijving() {
    return omschrijving;
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving = omschrijving;
  }

  public String getReferentie() {
    return referentie;
  }

  public void setReferentie(String referentie) {
    this.referentie = referentie;
  }
}
