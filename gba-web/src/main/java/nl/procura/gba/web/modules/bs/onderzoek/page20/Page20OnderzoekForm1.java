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
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ONDERZ_5_DAGEN_TERM;
import static nl.procura.standard.Globalfunctions.isTru;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;

public abstract class Page20OnderzoekForm1 extends GbaForm<Page20OnderzoekBean> {

  private final DossierOnderzoek zaakDossier;

  public Page20OnderzoekForm1(DossierOnderzoek zaakDossier) {
    this.zaakDossier = zaakDossier;
    setCaption("Af te handelen binnen 5 werkdagen?");
    setColumnWidths("450px", "");
    setOrder(DATUM_ONTVANGST, DATUM_EINDE, AFHANDELINGSTERMIJN, REDEN);
    setBean(zaakDossier);
  }

  public void setBean(DossierOnderzoek zaakDossier) {
    Page20OnderzoekBean bean = new Page20OnderzoekBean();
    bean.setDatumOntvangst(zaakDossier.getDatumOntvangstMelding().getFormatDate());
    bean.setDatumEinde(new DateTime(zaakDossier.getDatumEindeTermijn().getDate()).getFormatDate());

    if (zaakDossier.getDatumEindeTermijn().getLongDate() > 0) {
      bean.setAfhandelingstermijn(zaakDossier.getBinnenTermijn());
    }

    if (bean.getAfhandelingstermijn() == null) {
      ParameterService parameterService = Services.getInstance().getParameterService();
      String defaultAfhandelingstermijn = parameterService.getSysteemParameter(ONDERZ_5_DAGEN_TERM).getValue();
      if (StringUtils.isNotBlank(defaultAfhandelingstermijn)) {
        bean.setAfhandelingstermijn(isTru(defaultAfhandelingstermijn));
      }
    }

    bean.setReden(zaakDossier.getRedenTermijn());
    setBean(bean);
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
    getField(AFHANDELINGSTERMIJN).addListener((ValueChangeListener) event -> {
      onChangeTermijn((Boolean) event.getProperty().getValue());
      repaint();
    });
  }

  protected abstract void onChangeTermijn(Boolean binnen);
}
