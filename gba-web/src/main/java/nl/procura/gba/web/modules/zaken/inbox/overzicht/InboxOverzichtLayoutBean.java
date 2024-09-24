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

package nl.procura.gba.web.modules.zaken.inbox.overzicht;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
@Data
public class InboxOverzichtLayoutBean implements Serializable {

  public static final String ZAAK_ID_INTERN = "zaakIdIntern";
  public static final String ZAAK_ID_EXTERN = "zaakIdExtern";
  public static final String BESTANDSNAAM   = "bestandsnaam";
  public static final String OMSCHRIJVING   = "omschrijving";
  public static final String SOORT          = "soort";

  @Field(type = FieldType.LABEL,
      caption = "Intern verzoek-id")
  private String zaakIdIntern = null;

  @Field(type = FieldType.LABEL,
      caption = "Extern verzoek-id")
  private String zaakIdExtern = null;

  @Field(type = FieldType.LABEL,
      caption = "Bestandsnaam")
  private String bestandsnaam = null;

  @Field(type = FieldType.LABEL,
      caption = "Omschrijving")
  private String omschrijving = null;

  @Field(type = FieldType.LABEL,
      caption = "Soort verzoek")
  private Object soort = null;
}
