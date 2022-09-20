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

package nl.procura.gba.web.modules.zaken.reisdocument.page10;

import java.lang.annotation.ElementType;

import nl.procura.gba.web.services.zaken.reisdocumenten.SignaleringResult;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Getter;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
@Getter
public class ResultaatBean {

  static final String RESULTAAT_CODE = "resultaatCode";
  static final String RESULTAAT_OMS  = "resultaatOms";

  @Field(type = Field.FieldType.TEXT_FIELD, caption = "Code")
  private final String resultaatCode;
  @Field(type = Field.FieldType.TEXT_FIELD, caption = "Omschrijving")
  private final String resultaatOms;

  public ResultaatBean(SignaleringResult result) {
    this.resultaatCode = result.getResultaatCode().toString();
    this.resultaatOms = result.getResultaatOmschrijving();
  }
}
