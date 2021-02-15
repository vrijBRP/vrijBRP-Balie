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

package nl.procura.gba.web.modules.zaken.gpk.page4;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page4GpkBean implements Serializable {

  public static final String NUMMER      = "nummer";
  public static final String KAARTTYPE   = "kaartType";
  public static final String VERVALDATUM = "vervalDatum";
  public static final String PRINTDATUM  = "printDatum";
  public static final String AFGEVER     = "afgever";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Nummer")
  private String nummer = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Kaarttype")
  private String kaartType = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Vervaldatum")
  private String vervalDatum = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Printdatum")
  private String printDatum = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Afgever")
  private String afgever = "";
}
