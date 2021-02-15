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

package nl.procura.gba.web.modules.beheer.onderhoud.rdw.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2RdwBean implements Serializable {

  public static final String ACCOUNT_ID = "accountId";
  public static final String WACHTWOORD = "wachtwoord";
  public static final String DATUM      = "datum";

  @Field(type = FieldType.LABEL,
      caption = "Account-id",
      width = "100px",
      description = "Account-id",
      required = true)
  private String accountId = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Huidig wachtwoord",
      width = "100px",
      description = "Wachtwoord",
      required = true)
  @TextField(maxLength = 8)
  private String wachtwoord = "";

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum wijziging",
      width = "100px",
      description = "Datum laatste wachtwoord wijziging",
      required = true)
  private FieldValue datum = null;

  public Page2RdwBean(String accountId, String datum) {
    setAccountId(accountId);
    setDatum(new FieldValue(datum));
  }
}
