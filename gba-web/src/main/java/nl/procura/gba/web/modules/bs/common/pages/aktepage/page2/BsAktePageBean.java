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

package nl.procura.gba.web.modules.bs.common.pages.aktepage.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteDeel;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteRegistersoort;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.field.NumberField;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class BsAktePageBean implements Serializable {

  public static final String DATUM  = "datum";
  public static final String DEEL   = "deel";
  public static final String TYPE   = "type";
  public static final String NUMMER = "nummer";

  @Field(type = FieldType.LABEL,
      caption = "Datum akte")
  private String datum = "";

  @Field(type = FieldType.LABEL,
      caption = "Type akte")
  private DossierAkteRegistersoort type = DossierAkteRegistersoort.AKTE_ONBEKEND;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Registerdeel",
      required = true,
      width = "300px")
  @Select(nullSelectionAllowed = false)
  @Immediate
  private DossierAkteDeel deel = null;

  @Field(customTypeClass = NumberField.class,
      caption = "Volgnummer",
      required = true,
      width = "60px")
  @TextField(maxLength = 4)
  @Immediate
  private String nummer = "";
}
