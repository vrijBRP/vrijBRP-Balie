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

package nl.procura.gba.web.modules.hoofdmenu.requestinbox.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.burgerzaken.requestinbox.api.model.InboxItemStatus;
import nl.procura.burgerzaken.requestinbox.api.model.InboxItemTypeName;
import nl.procura.gba.web.components.fields.GbaBsnField;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1RequestInboxBean implements Serializable {

  public static final String STATUS = "status";
  public static final String TYPE   = "type";
  public static final String BSN    = "bsn";
  public static final String USER   = "user";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Status",
      width = "200px")
  private InboxItemStatus status;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Type",
      width = "200px")
  private InboxItemTypeName type;

  @Field(customTypeClass = GbaBsnField.class,
      caption = "BSN",
      width = "200px")
  private BsnFieldValue bsn;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Behandelaar",
      width = "200px")
  private Gebruiker user;
}
