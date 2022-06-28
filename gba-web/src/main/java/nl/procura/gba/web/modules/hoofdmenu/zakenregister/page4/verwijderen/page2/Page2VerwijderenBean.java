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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.verwijderen.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2VerwijderenBean implements Serializable {

  public static final String OMS    = "oms";
  public static final String TOTAAL = "totaal";
  public static final String AANTAL = "aantal";

  @Field(type = FieldType.LABEL, caption = "Omschrijving")
  private String oms = null;

  @Field(type = FieldType.LABEL, caption = "Totaal")
  private long totaal = 0;

  @Field(customTypeClass = GbaNativeSelect.class, caption = "Aantal tonen", width = "200px")
  @Select(containerDataSource = AantalResultatenContainer.class, nullSelectionAllowed = false)
  private AantalResultaten aantal = new AantalResultaten(50);
}
