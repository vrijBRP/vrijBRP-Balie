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

package nl.procura.gba.web.modules.zaken.rijbewijs.page12;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.fil;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page12RijbewijsBean1 implements Serializable {

  public static final String STATUS      = "status";
  public static final String SOORT       = "soort";
  public static final String REDEN       = "reden";
  public static final String AANVRAAGNR  = "aanvraagNr";
  public static final String HUIDIGRBWNR = "huidigRbwNr";
  public static final String NIEUWRBWNR  = "nieuwRbwNr";
  public static final String BELEMMERING = "belemmering";

  @Field(caption = "Status")
  private String status      = "";
  @Field(caption = "Soort aanvraag")
  private String soort       = "";
  @Field(caption = "Reden aanvraag")
  private String reden       = "";
  @Field(caption = "Aanvraagnummer")
  private String aanvraagNr  = "";
  @Field(caption = "Huidig rijbewijsnr.")
  private String huidigRbwNr = "";
  @Field(caption = "Nieuw rijbewijsnr.",
      width = "300px")
  private String nieuwRbwNr  = "";

  @Field(caption = "Sprake van belemmering")
  private String belemmering = "";

  public String getAanvraagNr() {
    return aanvraagNr;
  }

  public void setAanvraagNr(String aanvraagNr) {
    this.aanvraagNr = aanvraagNr;
  }

  public String getBelemmering() {
    return belemmering;
  }

  public void setBelemmering(String nieuweBelemmering) {

    if (fil(nieuweBelemmering)) {
      nieuweBelemmering = setClass(false, "Ja, " + nieuweBelemmering);
    } else {
      nieuweBelemmering = setClass(true, "Nee");
    }

    this.belemmering = nieuweBelemmering;
  }

  public String getHuidigRbwNr() {
    return huidigRbwNr;
  }

  public void setHuidigRbwNr(String HuidigRbwNr) {
    this.huidigRbwNr = HuidigRbwNr;
  }

  public String getNieuwRbwNr() {
    return nieuwRbwNr;
  }

  public void setNieuwRbwNr(String NieuwRbwNr) {
    this.nieuwRbwNr = NieuwRbwNr;
  }

  public String getReden() {
    return reden;
  }

  public void setReden(String reden) {
    this.reden = reden;
  }

  public String getSoort() {
    return soort;
  }

  public void setSoort(String soort) {
    this.soort = soort;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
