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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bijlage;

import static nl.procura.standard.Globalfunctions.fil;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

public class ZaakBijlageUploadForm extends GbaForm<ZaakBijlageUploadForm.ZaakHeaderBean> {

  private static final String ZAAKTYPE = "zaaktype";
  private static final String ID       = "id";

  public ZaakBijlageUploadForm(Zaak zaak) {
    setOrder(ZAAKTYPE, ID);
    setColumnWidths(WIDTH_130, "");

    ZaakHeaderBean bean = new ZaakHeaderBean();
    String soort = (fil(zaak.getSoort()) ? " (" + zaak.getSoort() + ")" : "");
    bean.setZaaktype(zaak.getType().getOms() + soort);
    bean.setId(zaak.getZaakId());

    setBean(bean);
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class ZaakHeaderBean implements Serializable {

    @Field(caption = "Zaak-type",
        readOnly = true)
    private String zaaktype = "";

    @Field(caption = "Zaak-id",
        readOnly = true)
    private String id = "";
  }
}
