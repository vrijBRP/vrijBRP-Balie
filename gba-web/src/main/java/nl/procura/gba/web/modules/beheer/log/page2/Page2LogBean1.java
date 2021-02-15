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

package nl.procura.gba.web.modules.beheer.log.page2;

import static nl.procura.gba.common.MiscUtils.setClass;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import org.apache.commons.lang3.builder.ToStringBuilder;

import nl.procura.gba.web.services.beheer.log.InLogpoging;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2LogBean1 implements Serializable {

  public static final String DATUMTIJD = "datumTijd";
  public static final String GEBRUIKER = "gebruiker";
  public static final String INLOGNAAM = "inlognaam";
  public static final String IPADRES   = "IPadres";
  public static final String BROWSER   = "browser";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Applicatie")
  private String             browser   = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "IP-adres")
  private String             IPadres   = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum/tijd")
  private String             datumTijd = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gebruiker")
  private String             gebruiker = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Inlognaam")
  private String             inlognaam = "";

  public Page2LogBean1() {
  }

  public Page2LogBean1(InLogpoging log) {

    setDatumTijd(log.getDatumTijd().toString());
    setInlognaam(log.getUsr());
    setGebruiker(log.isOnbekend() ? setClass("red", "Onbekend") : log.getGebruiker().getDescription());
    setIPadres(log.getIp());
    setBrowser(log.getBrowser());
  }

  public String getBrowser() {
    return browser;
  }

  public void setBrowser(String browser) {
    this.browser = browser;
  }

  public String getDatumTijd() {
    return datumTijd;
  }

  public void setDatumTijd(String datumTijd) {
    this.datumTijd = datumTijd;
  }

  public String getGebruiker() {
    return gebruiker;
  }

  public void setGebruiker(String gebruiker) {
    this.gebruiker = gebruiker;
  }

  public String getInlognaam() {
    return inlognaam;
  }

  public void setInlognaam(String inlognaam) {
    this.inlognaam = inlognaam;
  }

  public String getIPadres() {
    return IPadres;
  }

  public void setIPadres(String IPadres) {
    this.IPadres = IPadres;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
