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

package nl.procura.gba.web.modules.tabellen.huwelijkslocatie.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatieSoort;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2HuwelijksLocatieBean implements Serializable {

  public static final String LOCATIE     = "locatie";
  public static final String SOORT       = "soort";
  public static final String TOELICHTING = "toelichting";
  public static final String ALIAS       = "alias";

  public static final String INGANG_GELD = "ingangGeld";
  public static final String EINDE_GELD  = "eindeGeld";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Locatie",
      required = true,
      width = "400px")
  @TextField(maxLength = 255)
  private String locatie = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Soort",
      required = true,
      width = "400px")
  @Select(containerDataSource = Container.class,
      nullSelectionAllowed = false)
  private HuwelijksLocatieSoort soort = null;

  @Field(type = FieldType.TEXT_AREA,
      caption = "Alias(sen)",
      width = "400px")
  @TextArea(rows = 2,
      maxLength = 1000)
  private String alias = "";

  @Field(type = FieldType.TEXT_AREA,
      caption = "Toelichting",
      width = "400px",
      height = "114px")
  @TextArea(rows = 4,
      maxLength = 1000)
  private String toelichting = "";

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum ingang",
      width = "80px")
  private DateFieldValue ingangGeld = new DateFieldValue();

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum einde",
      width = "80px")
  private DateFieldValue eindeGeld = new DateFieldValue();

  public static class Container extends ArrayListContainer {

    public Container() {
      super(HuwelijksLocatieSoort.values(), false);
    }
  }
}
