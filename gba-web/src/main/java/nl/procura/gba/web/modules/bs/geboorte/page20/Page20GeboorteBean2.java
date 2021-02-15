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

package nl.procura.gba.web.modules.bs.geboorte.page20;

import static nl.procura.standard.Globalfunctions.fil;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.bs.geboorte.RedenVerplicht;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.ProTextArea;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page20GeboorteBean2 implements Serializable {

  public static final String REDEN_VERPLICHT   = "redenVerplicht";
  public static final String TARDIEVE_AANGIFTE = "tardieveAangifte";
  public static final String TARDIEVE_REDEN    = "tardieveReden";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Reden verplicht / bevoegd",
      required = true)
  @Select(containerDataSource = RedenVerplichtContainer.class)
  private RedenVerplicht redenVerplicht = RedenVerplicht.ONBEKEND;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Tardieve aangifte")
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private boolean tardieveAangifte = false;

  @Field(customTypeClass = ProTextArea.class,
      caption = "Reden",
      required = true,
      width = "500px")
  @TextArea(rows = 4)
  private String tardieveReden = "";

  public Page20GeboorteBean2() {
  }

  public Page20GeboorteBean2(RedenVerplicht reden) {

    if (fil(reden.getCode())) {
      setRedenVerplicht(reden);
    }
  }

  public RedenVerplicht getRedenVerplicht() {
    return redenVerplicht;
  }

  public void setRedenVerplicht(RedenVerplicht redenVerplicht) {
    this.redenVerplicht = redenVerplicht;
  }

  public String getTardieveReden() {
    return tardieveReden;
  }

  public void setTardieveReden(String tardieveReden) {
    this.tardieveReden = tardieveReden;
  }

  public boolean isTardieveAangifte() {
    return tardieveAangifte;
  }

  public void setTardieveAangifte(boolean tardieveAangifte) {
    this.tardieveAangifte = tardieveAangifte;
  }
}
