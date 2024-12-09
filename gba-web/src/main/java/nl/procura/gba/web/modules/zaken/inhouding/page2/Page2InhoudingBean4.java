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

package nl.procura.gba.web.modules.zaken.inhouding.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import lombok.Data;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.ProNativeSelect;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2InhoudingBean4 implements Serializable {

  public static final String DATUM_TIJD          = "datumTijd";
  public static final String MELDING_TYPE        = "meldingType";
  public static final String REDEN_TYPE          = "redenType";
  public static final String DATUM_REDEN_MELDING = "datumRedenMelding";
  public static final String INGELEVERD = "ingeleverd";

  @Field(type = FieldType.LABEL,
      caption = "Datum/tijd")
  private String datumTijd = "";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Type melding",
      width = "400px",
      required = true)
  private String meldingType = "";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Reden",
      width = "400px",
      required = true)
  private String redenType = "";

  @Field(type = FieldType.LABEL,
      caption = "Datum reden",
      width = "400px",
      required = true)
  private String datumRedenMelding = "";

  @Field(type = FieldType.LABEL,
      caption = "Is document ook ingeleverd?",
      width = "400px",
      required = true)
  private String ingeleverd = "";
}
