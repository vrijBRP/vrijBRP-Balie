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

package nl.procura.gba.web.modules.zaken.vog.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2VogBean5 implements Serializable {

  public static final String BIJZONDERHEDEN = "bijzonderheden";
  public static final String PERSISTEREN    = "persisteren";
  public static final String COVOGADRES     = "covogadres";
  public static final String TOELICHTING    = "toelichting";
  public static final String ADVIES         = "advies";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Bijzonderheden")
  private String bijzonderheden = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Persisteren")
  private String persisteren    = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Covogadres")
  private String covogadres     = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Toelichting")
  private String toelichting    = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Advies")
  private String advies         = "";

  public String getAdvies() {
    return advies;
  }

  public void setAdvies(String advies) {
    this.advies = advies;
  }

  public String getBijzonderheden() {
    return bijzonderheden;
  }

  public void setBijzonderheden(String bijzonderheden) {
    this.bijzonderheden = bijzonderheden;
  }

  public String getCovogadres() {
    return covogadres;
  }

  public void setCovogadres(String covogadres) {
    this.covogadres = covogadres;
  }

  public String getPersisteren() {
    return persisteren;
  }

  public void setPersisteren(String persisteren) {
    this.persisteren = persisteren;
  }

  public String getToelichting() {
    return toelichting;
  }

  public void setToelichting(String toelichting) {
    this.toelichting = toelichting;
  }
}
