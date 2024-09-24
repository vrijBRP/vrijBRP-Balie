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


package nl.procura.gba.web.modules.bs.common.pages.persoonpage;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

public class BsStatusForm extends GbaForm<BsStatusForm.Bean> {

  private static final String ZAAK = "zaak";

  public BsStatusForm(Dossier dossier) {
    this(dossier, null);
  }

  public BsStatusForm(Dossier dossier, DossierPersoonType type) {
    setColumnWidths("60px", "");
    setOrder(ZAAK);

    Bean bean = new Bean();
    StringBuilder zaak = new StringBuilder(String.format("<b>%s</b> - status: <b>%s</b>",
        dossier.getType().getOms(), dossier.getStatus().getOms()));
    if (type != null) {
      DossierPersoon person = getPerson(dossier, type);
      if (person != null) {
        zaak.append(" - ")
            .append(person.getDossierPersoonType().getDescr().toLowerCase())
            .append(": <b>").append(person.getNaam()
                .getNaam_naamgebruik_eerste_voornaam());

        String adres = person.getAdres().getAdres_pc_wpl_gem();
        if (StringUtils.isNotBlank(adres)) {
          zaak.append("</b> - adres: <b>")
              .append(adres)
              .append("</b>");
        }
      }
    }
    bean.setZaak(zaak.toString());
    setBean(bean);
  }

  private DossierPersoon getPerson(Dossier dossier, DossierPersoonType type) {
    return dossier.getPersonen(type).stream().findFirst().orElse(null);
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Bean implements Serializable {

    @Field(type = FieldType.LABEL,
        caption = "Zaak")
    private String zaak = "";
  }
}
