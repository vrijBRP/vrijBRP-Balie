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

package nl.procura.gba.web.modules.beheer.parameters.layout.instances.page2;

import static nl.procura.standard.Globalfunctions.pos;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.services.applicatie.onderhoud.Application;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.ProNativeSelect;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD,
    defaultWidth = "200px")
public class Page2InstanceBean implements Serializable {

  public static final String NAME       = "name";
  public static final String URL        = "url";
  public static final String USERNAME   = "username";
  public static final String SYNC_USERS = "syncUsers";
  public static final String ACTIVE     = "active";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Omschrijving",
      required = true)
  @TextField(nullRepresentation = "")
  private String name = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "URL",
      required = true)
  @TextField(nullRepresentation = "")
  private String url = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Gebruiker",
      required = true)
  @TextField(nullRepresentation = "")
  private String username = "";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Gebruikers",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private Boolean syncUsers = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Actief",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private Boolean active = null;

  public Page2InstanceBean() {
  }

  public Page2InstanceBean(Application app) {
    if (app != null) {
      setActive(pos(app.getEntity().getActive()));
      setName(app.getEntity().getName());
      setUrl(app.getEntity().getUrl());
      setUsername(app.getEntity().getUsername());
      setSyncUsers(app.getAttributes().isSyncUsers());
    }
  }
}
