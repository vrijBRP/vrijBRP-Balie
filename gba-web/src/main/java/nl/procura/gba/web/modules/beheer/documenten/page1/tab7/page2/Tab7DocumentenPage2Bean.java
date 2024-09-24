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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab7.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.DocumentKenmerkTypeContainer;
import nl.procura.gba.web.services.zaken.documenten.kenmerk.DocumentKenmerkType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.ProTextArea;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Tab7DocumentenPage2Bean implements Serializable {

  public static final String KENMERK = "kenmerk";
  public static final String TYPE    = "type";
  public static final String LABEL   = "label";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Kenmerk",
      required = true,
      width = "300px")
  private String kenmerk = "";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Type",
      required = true,
      width = "300px")
  @Select(containerDataSource = DocumentKenmerkTypeContainer.class)
  @Immediate
  private DocumentKenmerkType type = null;

  @Field(customTypeClass = ProTextArea.class,
      readOnly = true,
      width = "500px",
      caption = "Omschrijving")
  @TextArea(rows = 5)
  private String label = null;
}
