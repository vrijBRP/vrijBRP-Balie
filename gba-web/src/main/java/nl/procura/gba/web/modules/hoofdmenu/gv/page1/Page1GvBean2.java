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

package nl.procura.gba.web.modules.hoofdmenu.gv.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.modules.bs.ontbinding.page30.AanhefContainer;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.field.EmailField;
import nl.procura.vaadin.component.field.PostalcodeField;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1GvBean2 implements Serializable {

  public static final String INFORMATIEVRAGER = "informatievrager";
  public static final String TAV_AANHEF       = "tavAanhef";
  public static final String TAV_VOORL        = "tavVoorl";
  public static final String TAV_NAAM         = "tavNaam";
  public static final String EMAIL            = "email";
  public static final String ADRES            = "adres";
  public static final String PC               = "pc";
  public static final String PLAATS           = "plaats";
  public static final String KENMERK          = "kenmerk";

  @Field(type = FieldType.LABEL,
      caption = "Informatievrager",
      width = "290px",
      required = true)
  private String informatievrager = "";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Ter attentie van",
      width = "80px")
  @Select(containerDataSource = AanhefContainer.class)
  @InputPrompt(text = "Aanhef")
  private FieldValue tavAanhef = null;

  @Field(type = FieldType.TEXT_FIELD,
      width = "70px")
  @InputPrompt(text = "Voorletters")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String tavVoorl = "";

  @Field(type = FieldType.TEXT_FIELD,
      width = "140px")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  @InputPrompt(text = "Naam")
  private String tavNaam = "";

  @Field(customTypeClass = EmailField.class,
      caption = "E-mail",
      width = "300px")
  @TextField(maxLength = 250)
  private String email;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Adres",
      width = "290px",
      required = true)
  @TextField(maxLength = 250)
  private String adres = "";

  @Field(customTypeClass = PostalcodeField.class,
      caption = "Postcode",
      width = "100px",
      description = "Postcode")
  private FieldValue pc = null;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Plaats",
      width = "290px",
      required = true)
  @TextField(maxLength = 250)
  private String plaats = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Kenmerk",
      width = "290px")
  private String kenmerk = "";

  public String getAdres() {
    return adres;
  }

  public void setAdres(String adres) {
    this.adres = adres;
  }

  public String getInformatievrager() {
    return informatievrager;
  }

  public void setInformatievrager(String informatievrager) {
    this.informatievrager = informatievrager;
  }

  public String getKenmerk() {
    return kenmerk;
  }

  public void setKenmerk(String kenmerk) {
    this.kenmerk = kenmerk;
  }

  public FieldValue getPc() {
    return pc;
  }

  public void setPc(FieldValue pc) {
    this.pc = pc;
  }

  public String getPlaats() {
    return plaats;
  }

  public void setPlaats(String plaats) {
    this.plaats = plaats;
  }

  public FieldValue getTavAanhef() {
    return tavAanhef;
  }

  public void setTavAanhef(FieldValue tavAanhef) {
    this.tavAanhef = tavAanhef;
  }

  public String getTavNaam() {
    return tavNaam;
  }

  public void setTavNaam(String tavNaam) {
    this.tavNaam = tavNaam;
  }

  public String getTavVoorl() {
    return tavVoorl;
  }

  public void setTavVoorl(String tavVoorl) {
    this.tavVoorl = tavVoorl;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
