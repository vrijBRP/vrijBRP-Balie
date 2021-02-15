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

package nl.procura.gba.web.modules.zaken.rijbewijs.errorpage;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class RijbewijsErrorBean1 implements Serializable {

  public static final String PROCES       = "proces";
  public static final String SYSTEEM      = "systeem";
  public static final String MELDING      = "melding";
  public static final String SOORTMELDING = "soortMelding";
  public static final String MELDINGVAR   = "meldingVar";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Proces")
  private String proces       = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Afkomstig van")
  private String systeem      = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Melding")
  private String melding      = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Soort melding")
  private String soortMelding = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Betreft variabele")
  private String meldingVar   = "";

  public String getMelding() {
    return melding;
  }

  public void setMelding(String melding) {
    this.melding = melding;
  }

  public String getMeldingVar() {
    return meldingVar;
  }

  public void setMeldingVar(String meldingVar) {
    this.meldingVar = meldingVar;
  }

  public String getProces() {
    return proces;
  }

  public void setProces(String proces) {
    this.proces = proces;
  }

  public String getSoortMelding() {
    return soortMelding;
  }

  public void setSoortMelding(String soortMelding) {
    this.soortMelding = soortMelding;
  }

  public String getSysteem() {
    return systeem;
  }

  public void setSysteem(String systeem) {
    this.systeem = systeem;
  }

}
