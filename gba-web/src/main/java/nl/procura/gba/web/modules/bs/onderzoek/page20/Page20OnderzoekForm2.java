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

import static nl.procura.gba.web.modules.bs.onderzoek.page20.Page20OnderzoekBean.AANDUIDING_GEG_ONDERZOEK;
import static nl.procura.gba.web.modules.bs.onderzoek.page20.Page20OnderzoekBean.AANDUIDING_GEG_ONDERZOEK_NVT;
import static nl.procura.gba.web.modules.bs.onderzoek.page20.Page20OnderzoekBean.DATUM_AANVANG_ONDERZOEK;
import static nl.procura.gba.web.modules.bs.onderzoek.page20.Page20OnderzoekBean.DATUM_AANVANG_ONDERZOEK_NVT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ONDERZ_DEFAULT_AAND;

import java.util.Date;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.validators.DatumVolgordeValidator;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.onderzoek.enums.AanduidingOnderzoekType;
import nl.procura.vaadin.component.field.ProDateField;

public class Page20OnderzoekForm2 extends GbaForm<Page20OnderzoekBean> {

  private final DossierOnderzoek zaakDossier;

  public Page20OnderzoekForm2(DossierOnderzoek zaakDossier) {
    this.zaakDossier = zaakDossier;
    setCaption("Start onderzoek");
    setColumnWidths("450px", "");
    setOrder(DATUM_AANVANG_ONDERZOEK, AANDUIDING_GEG_ONDERZOEK);
    setBean(zaakDossier);
  }

  public void setBean(DossierOnderzoek zaakDossier) {
    Page20OnderzoekBean bean = new Page20OnderzoekBean();
    bean.setDatumAanvangOnderzoek(zaakDossier.getDatumAanvangOnderzoek().getDate());
    bean.setAanduidingGegevensOnderzoek(zaakDossier.getAanduidingGegevensOnderzoek().getCode());

    if (AanduidingOnderzoekType.ONBEKEND.equals(zaakDossier.getAanduidingGegevensOnderzoek())) {
      ParameterService parameterService = Services.getInstance().getParameterService();
      String defaultAand = parameterService.getSysteemParameter(ONDERZ_DEFAULT_AAND).getValue();
      bean.setAanduidingGegevensOnderzoek(AanduidingOnderzoekType.get(defaultAand).getCode());
    }

    if (!isInLokaleBRP(zaakDossier)) {
      setOrder(DATUM_AANVANG_ONDERZOEK_NVT, AANDUIDING_GEG_ONDERZOEK_NVT);
      setCaption("Start onderzoek (persoon is niet ingeschreven in de gemeente)");
      bean.setDatumAanvangOnderzoek(null);
      bean.setAanduidingGegevensOnderzoek(null);
    }

    setBean(bean);
  }

  private boolean isInLokaleBRP(DossierOnderzoek zaakDossier) {
    return zaakDossier.getBetrokkenen().stream()
        .filter(DossierPersoon::isInGemeente)
        .anyMatch(DossierPersoon::isIngeschreven);
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
    Date datumOntvangst = zaakDossier.getDatumOntvangstMelding().getDate();
    ProDateField datumAanvangOnderzoek = getField(DATUM_AANVANG_ONDERZOEK, ProDateField.class);
    if (datumAanvangOnderzoek != null) {
      datumAanvangOnderzoek.addValidator(
          new DatumVolgordeValidator("Datum ontvangst melding", () -> datumOntvangst, "Datum aanvang onderzoek",
              () -> (Date) datumAanvangOnderzoek.getValue()));
    }
  }
}
