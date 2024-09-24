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

package nl.procura.gba.web.modules.bs.naturalisatie.aanschrijving;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.naturalisatie.document.Ceremonie;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class AanschrijvingBean implements Serializable {

  public static final String F_AANSCHRIJFPERSOON = "aanschrijfpersoon";
  public static final String F_SOORT             = "soort";
  public static final String F_CEREMONIE         = "ceremonie";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Aanschrijfpersoon",
      width = "400px",
      required = true)
  @Select(itemCaptionPropertyId = AanschrijfpersoonContainer.OMSCHRIJVING,
      nullSelectionAllowed = false)
  private DossierPersoon aanschrijfpersoon;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Soort document",
      width = "400px",
      required = true)
  @Select(nullSelectionAllowed = false)
  private String soort;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Naturalisatieceremonie",
      width = "400px",
      required = true)
  @Select(nullSelectionAllowed = false)
  private Ceremonie ceremonie;
}
