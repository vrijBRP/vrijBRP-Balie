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

package nl.procura.gba.web.modules.beheer.onderhoud.gbav.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2GbavBean implements Serializable {

  public static final String DATUM      = "datum";
  public static final String WACHTWOORD = "wachtwoord";

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum wijziging",
      width = "80px",
      description = "Datum laatste wachtwoord wijziging",
      required = true)
  private FieldValue datum = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Huidig wachtwoord",
      width = "200px",
      description = "Wachtwoord",
      required = true)
  private String wachtwoord = "";

  public Page2GbavBean(String datum) {
    setDatum(new FieldValue(datum));
  }
}
