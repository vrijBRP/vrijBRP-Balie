/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.hoofdmenu.requestinbox;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
@Data
public class RequestInboxLayoutBean implements Serializable {

  public static final String ID          = "id";
  public static final String DESCRIPTION = "description";
  public static final String STATUS      = "status";
  public static final String HANDLER     = "handler";

  @Field(type = FieldType.LABEL,
      caption = "Verzoek-id")
  private String id = null;

  @Field(type = FieldType.LABEL,
      caption = "Omschrijving")
  private String description = null;

  @Field(type = FieldType.LABEL,
      caption = "Status")
  private String status = null;

  @Field(type = FieldType.LABEL,
      caption = "Behandelaar")
  private String handler = null;
}
