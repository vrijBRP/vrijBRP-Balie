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

package nl.procura.gba.web.modules.zaken.reisdocument.overzicht;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class ReisdocumentOverzichtBean3 implements Serializable {

  public static final String DOCUMENT    = "document";
  public static final String SIGNALERING = "signalering";
  public static final String AFLEVERING  = "aflevering";
  public static final String AFSLUITING  = "afsluiting";
  public static final String CODE_RAAS   = "codeRaas";

  public static final String AFLEVERING2 = "aflevering2";
  public static final String AFSLUITING2 = "afsluiting2";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Document")
  private String document = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Signalering")
  private String signalering = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Aflevering")
  private String aflevering = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Afsluiting")
  private String afsluiting = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Code raas")
  private String codeRaas = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Aflevering")
  private String aflevering2 = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Afsluiting")
  private String afsluiting2 = "";

  public String getAflevering() {
    return aflevering;
  }

  public void setAflevering(String aflevering) {
    this.aflevering = aflevering;
  }

  public String getAfsluiting() {
    return afsluiting;
  }

  public void setAfsluiting(String afsluiting) {
    this.afsluiting = afsluiting;
  }

  public String getDocument() {
    return document;
  }

  public void setDocument(String document) {
    this.document = document;
  }

  public String getSignalering() {
    return signalering;
  }

  public void setSignalering(String signalering) {
    this.signalering = signalering;
  }

  public String getAflevering2() {
    return aflevering2;
  }

  public void setAflevering2(String aflevering2) {
    this.aflevering2 = aflevering2;
  }

  public String getAfsluiting2() {
    return afsluiting2;
  }

  public void setAfsluiting2(String afsluiting2) {
    this.afsluiting2 = afsluiting2;
  }

  public String getCodeRaas() {
    return codeRaas;
  }

  public void setCodeRaas(String codeRaas) {
    this.codeRaas = codeRaas;
  }
}
