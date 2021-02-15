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

package nl.procura.gba.web.modules.beheer.logbestanden.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.beheer.logbestanden.page2.RegelSelectieContainer.RegelSelectie;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class RegelSelectieBean implements Serializable {

  public static final String REGELSELECTIE = "regelSelectie";
  public static final String ZOEKINFILE    = "zoekInFile";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Aantal regels tonen")
  @Select(nullSelectionAllowed = false,
      containerDataSource = RegelSelectieContainer.class)
  @Immediate
  private RegelSelectie regelSelectie = RegelSelectie.LAATSTE100;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Zoek in bestand")
  @Immediate
  private String zoekInFile = "";
}
