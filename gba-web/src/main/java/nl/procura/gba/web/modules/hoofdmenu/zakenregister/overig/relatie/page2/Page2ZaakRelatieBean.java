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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.relatie.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2ZaakRelatieBean implements Serializable {

  public static final String ZAAK_OMS    = "zaakOms";
  public static final String ZAAK_ID     = "zaakId";
  public static final String ZAAK_ID_REL = "zaakIdRel";

  @Field(type = FieldType.LABEL,
      caption = "Zaak")
  private String zaakOms = null;

  @Field(type = FieldType.LABEL,
      caption = "Zaak-id")
  private String zaakId = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Gerelateerd zaak-id",
      width = "200px")
  private String zaakIdRel = null;
}
