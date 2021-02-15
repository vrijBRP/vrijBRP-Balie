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

package nl.procura.gba.web.modules.beheer.onderhoud.page1.tab3;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page3OnderhoudBean implements Serializable {

  public static final String CURRENT        = "current";
  public static final String TOTAL          = "total";
  public static final String MAX            = "max";
  public static final String FILESIZE_TOTAL = "fileSizeTotal";
  public static final String FILESIZE_USED  = "fileSizeUsed";
  public static final String FILESIZE_FREE  = "fileSizeFree";

  @Field(type = FieldType.LABEL,
      caption = "Huidig")
  private String current = "Onbekend";

  @Field(type = FieldType.LABEL,
      caption = "Gereserveerd")
  private String total = "Onbekend";

  @Field(type = FieldType.LABEL,
      caption = "Max. beschikbaar")
  private String max = "Onbekend";

  @Field(type = FieldType.LABEL,
      caption = "Totaal")
  private String fileSizeTotal = "Onbekend";

  @Field(type = FieldType.LABEL,
      caption = "In gebruik")
  private String fileSizeUsed = "Onbekend";

  @Field(type = FieldType.LABEL,
      caption = "Beschikbaar")
  private String fileSizeFree = "Onbekend";
}
