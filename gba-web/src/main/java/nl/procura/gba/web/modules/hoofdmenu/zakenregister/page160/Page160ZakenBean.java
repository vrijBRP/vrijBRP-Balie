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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page160;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page160ZakenBean implements Serializable {

  public static final String PERIODE            = "periode";
  public static final String INCOMPLEET         = "incompleet";
  public static final String WACHTKAMER         = "wachtkamer";
  public static final String OPGENOMEN          = "opgenomen";
  public static final String INBEHANDELING      = "inbehandeling";
  public static final String GEPREVALIDEERD     = "geprevalideerd";
  public static final String DOCUMENT_ONTVANGEN = "documentOntvangen";
  public static final String GEWEIGERD          = "geweigerd";
  public static final String VERWERKT           = "verwerkt";
  public static final String GEANNULEERD        = "geannuleerd";

  @Field(type = FieldType.CHECK_BOX,
      caption = "Incompleet")
  private boolean incompleet = false;

  @Field(type = FieldType.CHECK_BOX,
      caption = "Wachtkamer")
  private boolean wachtkamer = false;

  @Field(type = FieldType.CHECK_BOX,
      caption = "Opgenomen")
  private boolean opgenomen = false;

  @Field(type = FieldType.CHECK_BOX,
      caption = "Geprevalideerd")
  private boolean geprevalideerd = false;

  @Field(type = FieldType.CHECK_BOX,
      caption = "Inbehandeling")
  private boolean inbehandeling = false;

  @Field(type = FieldType.CHECK_BOX,
      caption = "Document onvangen")
  private boolean documentOntvangen = false;

  @Field(type = FieldType.CHECK_BOX,
      caption = "Geweigerd")
  private boolean geweigerd = false;

  @Field(type = FieldType.CHECK_BOX,
      caption = "Verwerkt")
  private boolean verwerkt = false;

  @Field(type = FieldType.CHECK_BOX,
      caption = "Geannuleerd")
  private boolean geannuleerd = false;
}
