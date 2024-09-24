/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.bs.naturalisatie.page60;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page60NaturalisatieBean implements Serializable {

  public static final String F_CEREMONIE_1       = "ceremonie1";
  public static final String F_CEREMONIE_2       = "ceremonie2";
  public static final String F_CEREMONIE_3       = "ceremonie3";

  public static final String F_DATUM_UITREIKING = "datumUitreiking";
  public static final String F_DATUM_VERVAL     = "datumVerval";

  @Field(type = FieldType.LABEL,
      caption = "Ceremonie 1",
      readOnly = true)
  private String ceremonie1;

  @Field(type = FieldType.LABEL,
      caption = "Ceremonie 2",
      readOnly = true)
  private String ceremonie2;

  @Field(type = FieldType.LABEL,
      caption = "Ceremonie 3",
      readOnly = true)
  private String ceremonie3;

  @Field(type = FieldType.LABEL,
      caption = "Bevestiging / KB uitgereikt op",
      readOnly = true)
  private String datumUitreiking;

  @Field(type = FieldType.LABEL,
      caption = "Vervaldatum",
      readOnly = true)
  private String datumVerval;
}
