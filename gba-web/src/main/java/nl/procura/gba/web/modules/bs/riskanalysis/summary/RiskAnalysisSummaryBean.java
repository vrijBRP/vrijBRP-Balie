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

package nl.procura.gba.web.modules.bs.riskanalysis.summary;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class RiskAnalysisSummaryBean implements Serializable {

  public static final String F_PROFILE     = "profile";
  public static final String F_THRESHOLD   = "threshold";
  public static final String F_RELATEDCASE = "relatedCase";

  @Field(type = Field.FieldType.LABEL,
      caption = "Risicoprofiel")
  private String profile;

  @Field(type = Field.FieldType.LABEL,
      caption = "Drempel")
  private String threshold;

  @Field(type = Field.FieldType.LABEL,
      caption = "Toepassen op")
  private String relatedCase;
}
