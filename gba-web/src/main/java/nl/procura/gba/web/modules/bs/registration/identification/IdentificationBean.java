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

package nl.procura.gba.web.modules.bs.registration.identification;

import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.modules.zaken.identificatie.page1.DocumentTypeContainer;
import nl.procura.gba.web.services.zaken.identiteit.IdentificatieType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class IdentificationBean {

  public static final String F_SOORT           = "soort";
  public static final String F_ISSUING_COUNTRY = "issuingCountry";
  public static final String F_NUMMER          = "nummer";

  @Field(customTypeClass = GbaNativeSelect.class, caption = "Soort document", required = true, width = "260px")
  @Select(containerDataSource = DocumentTypeContainer.class)
  @Immediate()
  private IdentificatieType soort;

  @Field(customTypeClass = GbaComboBox.class, caption = "Land van afgifte", width = "260px", required = true)
  @Select(containerDataSource = LandContainer.class)
  private FieldValue issuingCountry;

  @Field(customTypeClass = GbaTextField.class, caption = "Nummer", required = true, width = "260px")
  @TextField(maxLength = 40)
  private String nummer = "";

}
