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

package nl.procura.gba.web.modules.bs.onderzoek.page20;

import static nl.procura.gba.web.modules.bs.onderzoek.page20.Page20OnderzoekBean.*;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.*;
import static nl.procura.standard.Globalfunctions.isTru;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;

public class Page20OnderzoekForm3 extends GbaForm<Page20OnderzoekBean> {

  public Page20OnderzoekForm3(DossierOnderzoek zaakDossier) {
    setCaption("Kunnen onderdelen van het onderzoek worden overgeslagen?");
    setColumnWidths("450px", "");
    setOrder(ONDERZOEK_DOOR_ANDER_GEDAAN, VOLDOENDE_REDEN, TOELICHTING);
    setBean(zaakDossier);
  }

  public void setBean(DossierOnderzoek zaakDossier) {

    Page20OnderzoekBean bean = new Page20OnderzoekBean();
    bean.setVoldoendeReden(zaakDossier.getRedenOverslaan());
    bean.setOnderzoekDoorAnderGedaan(zaakDossier.getGedegenOnderzoek());
    bean.setToelichting(zaakDossier.getToelichtingOverslaan());

    ParameterService parameterService = Services.getInstance().getParameterService();
    String defaultAfhandelingstermijn = parameterService.getSysteemParameter(ONDERZ_5_DAGEN_TERM).getValue();

    if (StringUtils.isNotBlank(defaultAfhandelingstermijn) && !isTru(defaultAfhandelingstermijn)) {
      if (bean.getOnderzoekDoorAnderGedaan() == null) {
        String defaultAnderOrgaan = parameterService.getSysteemParameter(ONDERZ_ANDER_ORGAAN).getValue();
        bean.setOnderzoekDoorAnderGedaan(isTru(defaultAnderOrgaan));
      }

      if (bean.getVoldoendeReden() == null) {
        String defaultRedenOverslaan = parameterService.getSysteemParameter(ONDERZ_REDEN_OVERSLAAN).getValue();
        bean.setVoldoendeReden(isTru(defaultRedenOverslaan));
      }
    }

    setBean(bean);
  }
}
