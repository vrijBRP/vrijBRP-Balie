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

package nl.procura.gba.web.modules.zaken.verhuizing.page19;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.zaken.verhuizing.EmigratieDuur;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page19VerhuizingBean1 implements Serializable {

  public static final String AANVANG = "aanvang";
  public static final String ADRES1  = "adres1";
  public static final String ADRES2  = "adres2";
  public static final String ADRES3  = "adres3";
  public static final String LAND    = "land";
  public static final String DUUR    = "duur";
  @Field(customTypeClass = ProDateField.class,
      caption = "Aanvang",
      required = true,
      width = "100px")
  private Date               aanvang = null;
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Adres",
      width = "300px")
  @TextField(maxLength = 35)
  private String             adres1  = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Woonplaats",
      width = "300px")
  @TextField(maxLength = 35)
  private String             adres2  = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gemeente, district, provincie, eiland",
      width = "300px")
  @TextField(maxLength = 35)
  private String             adres3  = "";
  @Field(customTypeClass = GbaComboBox.class,
      caption = "Land",
      required = true)
  @Select(containerDataSource = LandContainer.class)
  private FieldValue         land    = null;
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Vermoedelijke verblijfsduur voor de komende <b>12</b> maanden",
      required = true)
  @Select(containerDataSource = EmigratieDuurContainer.class,
      nullSelectionAllowed = false)
  @Immediate()
  private EmigratieDuur      duur    = EmigratieDuur.LANGER;

  public Date getAanvang() {
    return aanvang;
  }

  public void setAanvang(Date aanvang) {
    this.aanvang = aanvang;
  }

  public String getAdres1() {
    return adres1;
  }

  public void setAdres1(String adres1) {
    this.adres1 = adres1;
  }

  public String getAdres2() {
    return adres2;
  }

  public void setAdres2(String adres2) {
    this.adres2 = adres2;
  }

  public String getAdres3() {
    return adres3;
  }

  public void setAdres3(String adres3) {
    this.adres3 = adres3;
  }

  public EmigratieDuur getDuur() {
    return duur;
  }

  public void setDuur(EmigratieDuur duur) {
    this.duur = duur;
  }

  public FieldValue getLand() {
    return land;
  }

  public void setLand(FieldValue land) {
    this.land = land;
  }
}
