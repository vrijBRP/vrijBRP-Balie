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

package nl.procura.gba.web.modules.bs.naturalisatie.page20;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.bs.naturalisatie.enums.BasisVerzoekType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.ProTextField;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page20NaturalisatieBean implements Serializable {

  public static final String F_BASIS_VERZOEK     = "basisVerzoek";
  public static final String F_DOSSIERNR         = "dossiernr";
  public static final String F_VERTEGENWOORDIGER = "vertegenwoordiger";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Basis voor verzoek",
      width = "500px",
      required = true)
  private BasisVerzoekType basisVerzoek;

  @Field(customTypeClass = ProTextField.class,
      caption = "Dossiernummer",
      width = "110px")
  @TextField(nullRepresentation = "", maxLength = 12)
  private String dossiernr;

  @Field(customTypeClass = ProTextField.class,
      caption = "Wettelijke vertegenwoordiger",
      width = "500px",
      readOnly = true)
  private String vertegenwoordiger;
}
