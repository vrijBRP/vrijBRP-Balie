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

package nl.procura.gba.web.modules.zaken.verhuizing.page10;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisAdres;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page10VerhuizingBean1 implements Serializable {

  public static final String TYPEVERHUIZING = "typeVerhuizing";
  public static final String HUIDIGADRES    = "huidigAdres";
  public static final String AANGEVER       = "aangever";
  public static final String TOELICHTING    = "toelichting";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Soort verhuizing")
  @Select(nullSelectionAllowed = false)
  @Immediate()
  private VerhuisType  typeVerhuizing = VerhuisType.BINNENGEMEENTELIJK;
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Huidig adres")
  @Select(nullSelectionAllowed = false)
  @Immediate()
  private VerhuisAdres huidigAdres    = null;
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Aangever")
  @Select(nullSelectionAllowed = false)
  @Immediate()
  private FieldValue   aangever       = new FieldValue();

  @Field(type = FieldType.TEXT_AREA,
      caption = "Toelichting",
      visible = false)
  @TextArea(columns = 50,
      rows = 3,
      maxLength = 170)
  private String toelichting = "";

  public FieldValue getAangever() {
    return aangever;
  }

  public void setAangever(FieldValue aangever) {
    this.aangever = aangever;
  }

  public VerhuisAdres getHuidigAdres() {
    return huidigAdres;
  }

  public void setHuidigAdres(VerhuisAdres huidigAdres) {
    this.huidigAdres = huidigAdres;
  }

  public String getToelichting() {
    return toelichting;
  }

  public void setToelichting(String toelichting) {
    this.toelichting = toelichting;
  }

  public VerhuisType getTypeVerhuizing() {
    return typeVerhuizing;
  }

  public void setTypeVerhuizing(VerhuisType soortVerhuizing) {
    this.typeVerhuizing = soortVerhuizing;
  }
}
