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

package nl.procura.gba.web.modules.zaken.identificatie.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1IdentificatieBean3 implements Serializable {

  public static final String NUMMER               = "nummer";
  public static final String GELDIG               = "geldig";
  public static final String DATUMVERLIESDIEFSTAL = "datumVerliesDiefstal";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Nummer")
  private String nummer               = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geldig t/m")
  private String geldig               = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum verlies / diefstal")
  private String datumVerliesDiefstal = "";

  public String getDatumVerliesDiefstal() {
    return datumVerliesDiefstal;
  }

  public void setDatumVerliesDiefstal(String datumVerliesDiefstal) {
    this.datumVerliesDiefstal = datumVerliesDiefstal;
  }

  public String getGeldig() {
    return geldig;
  }

  public void setGeldig(String geldig) {
    this.geldig = geldig;
  }

  public String getNummer() {
    return nummer;
  }

  public void setNummer(String nummer) {
    this.nummer = nummer;
  }
}
