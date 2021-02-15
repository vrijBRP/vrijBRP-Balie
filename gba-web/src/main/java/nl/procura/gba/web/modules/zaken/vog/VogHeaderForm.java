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

package nl.procura.gba.web.modules.zaken.vog;

import static nl.procura.standard.Globalfunctions.pos;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

public class VogHeaderForm extends ReadOnlyForm {

  private static final String NRS = "nrs";

  public VogHeaderForm(Zaak zaak) {

    setOrder(NRS);
    setColumnWidths(WIDTH_130, "");

    ZaakHeaderBean b = new ZaakHeaderBean();

    if (zaak instanceof VogAanvraag) {

      VogAanvraag vogAanvraag = (VogAanvraag) zaak;

      Object aanvraagNummer = (pos(
          vogAanvraag.getAanvraagId()) ? vogAanvraag.getAanvraagId() : "Wordt bepaald na verzending");
      Object covogNummer = vogAanvraag.getVogNummer().getCOVOGNummerFormatted();

      b.setNrs(covogNummer + " / " + aanvraagNummer);
    }

    setBean(b);
  }

  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class ZaakHeaderBean implements Serializable {

    @Field(caption = "COVOG- / Aanvraagnr.")
    private String nrs = "";

    public String getNrs() {
      return nrs;
    }

    public void setNrs(String nrs) {
      this.nrs = nrs;
    }
  }
}
