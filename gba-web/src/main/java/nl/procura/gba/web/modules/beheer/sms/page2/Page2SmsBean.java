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

package nl.procura.gba.web.modules.beheer.sms.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.beheer.sms.page2.Page2SmsForm.SenderContainer;
import nl.procura.gba.web.services.beheer.sms.SmsType;
import nl.procura.sms.rest.domain.Sender;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.ProTextArea;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2SmsBean implements Serializable {

  public static final String TYPE      = "type";
  public static final String ACTIVE    = "active";
  public static final String SENDER    = "sender";
  public static final String CONTENT   = "content";
  public static final String AUTO_SEND = "autoSend";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Geactiveerd",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private boolean active = false;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Type",
      required = true,
      width = "400px")
  @Select(containerDataSource = SmsTypeContainer.class)
  @Immediate
  private SmsType type = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "SMS Afzender",
      required = true)
  @Select(itemCaptionPropertyId = SenderContainer.DESCRIPTION)
  @Immediate
  private Sender sender = null;

  @Field(customTypeClass = ProTextArea.class,
      caption = "Sms bericht",
      required = true,
      width = "400px")
  @TextArea(rows = 10)
  private String content = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Automatisch verzenden",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private boolean autoSend = true;
}
