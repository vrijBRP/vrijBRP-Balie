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

package nl.procura.gba.web.modules.zaken.verhuizing.page12;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.components.containers.StraatContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.HuisnummerVeld;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.field.PostalcodeField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page12VerhuizingBean1 implements Serializable {

  public static final String GEGEVENSBRON = "gegevensbron";
  public static final String ADRESIND     = "adresind";
  public static final String STRAAT_GEM   = "straatGem";
  public static final String STRAAT_LAND  = "straatLand";
  public static final String HNR          = "hnr";
  public static final String PC           = "pc";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Gegevensbron")
  @Select(nullSelectionAllowed = false)
  @Immediate()
  private PLEDatasource gegevensbron = null;
  @Field(type = FieldType.CHECK_BOX,
      caption = "Adresidentificatie",
      description = "Zoek alle personen die op hetzelfde adres wonen")
  private boolean       adresind     = false;
  @Field(customTypeClass = GbaComboBox.class,
      caption = "Straat",
      width = "200px")
  @Select(containerDataSource = StraatContainer.class)
  private FieldValue    straatGem    = new FieldValue();
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Straat",
      width = "200px",
      visible = false)
  private String        straatLand   = "";
  @Field(customTypeClass = HuisnummerVeld.class,
      caption = "Hnr",
      width = "30px")

  @InputPrompt(text = "hnr")
  private String        hnr          = "";
  @Field(customTypeClass = PostalcodeField.class,
      caption = "Postcode")
  private FieldValue    pc           = new FieldValue();

  public PLEDatasource getGegevensbron() {
    return gegevensbron;
  }

  public void setGegevensbron(PLEDatasource gegevensbron) {
    this.gegevensbron = gegevensbron;
  }

  public String getHnr() {
    return hnr;
  }

  public void setHnr(String hnr) {
    this.hnr = hnr;
  }

  public FieldValue getPc() {
    return pc;
  }

  public void setPc(FieldValue pc) {
    this.pc = pc;
  }

  public FieldValue getStraatGem() {
    return straatGem;
  }

  public void setStraatGem(FieldValue straatGem) {
    this.straatGem = straatGem;
  }

  public String getStraatLand() {
    return straatLand;
  }

  public void setStraatLand(String straatLand) {
    this.straatLand = straatLand;
  }

  public boolean isAdresind() {
    return adresind;
  }

  public void setAdresind(boolean adresind) {
    this.adresind = adresind;
  }
}
