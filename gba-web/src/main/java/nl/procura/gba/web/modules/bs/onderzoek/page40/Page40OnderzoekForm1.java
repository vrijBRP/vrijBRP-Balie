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

package nl.procura.gba.web.modules.bs.onderzoek.page40;

import static nl.procura.gba.web.modules.bs.onderzoek.page40.Page40OnderzoekBean1.*;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.onderzoek.enums.BetrokkeneType;
import nl.procura.gba.web.services.bs.onderzoek.enums.VermoedAdresType;

public abstract class Page40OnderzoekForm1 extends GbaForm<Page40OnderzoekBean1> {

  private final DossierOnderzoek zaakDossier;

  public Page40OnderzoekForm1(DossierOnderzoek zaakDossier) {
    this.zaakDossier = zaakDossier;

    setCaption("Resultaat onderzoek");
    setColumnWidths("200px", "");
    setOrder(HETZELFDE, BETROKKENEN, DATUM_EINDE_ONDERZOEK, NOGMAALS_AANSCHRIJVEN, TOELICHTING);

    Page40OnderzoekBean1 bean = new Page40OnderzoekBean1();
    bean.setHetzelfde(zaakDossier.getResultaatAdresGelijk());
    bean.setBetrokkene(zaakDossier.getResultaatOnderzoekBetrokkene());
    bean.setDatumEindeOnderzoek(zaakDossier.getDatumEindeOnderzoek().getDate());
    bean.setNogmaalsAanschrijven(zaakDossier.getNogmaalsAanschrijven());
    bean.setToelichting(zaakDossier.getResultaatToelichting());

    setBean(bean);
  }

  @Override
  public Page40OnderzoekBean1 getNewBean() {
    return new Page40OnderzoekBean1();
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
    getField(HETZELFDE).addListener((ValueChangeListener) event -> {
      updateHetzelfde((Boolean) event.getProperty().getValue());
    });
    getField(BETROKKENEN).addListener((ValueChangeListener) event -> {
      updateBetrokkene((BetrokkeneType) event.getProperty().getValue());
    });
    updateBetrokkene(getBean().getBetrokkene());
  }

  protected void updateHetzelfde(Boolean value) {
    if (value) {
      VermoedAdresType type = zaakDossier.getVermoedelijkAdres();
      getField(BETROKKENEN).setValue(type.getBetrokkeneType());
      onChangeAdresOvernemen();
    }
  }

  protected abstract void onChangeAdresOvernemen();

  protected abstract void onChangeBetrokkene(BetrokkeneType betrokkeneType);

  private void updateBetrokkene(BetrokkeneType betrokkeneType) {
    getField(DATUM_EINDE_ONDERZOEK).setVisible(false);
    getField(NOGMAALS_AANSCHRIJVEN).setVisible(false);
    getField(TOELICHTING).setVisible(false);

    onShowWarning(false);
    getField(TOELICHTING).setVisible(betrokkeneType != null);
    if (betrokkeneType != null) {
      switch (betrokkeneType) {
        case ZELFDE:
          getField(DATUM_EINDE_ONDERZOEK)
              .setVisible(zaakDossier.getBinnenTermijn() != null && !zaakDossier.getBinnenTermijn());
          onShowWarning(true);
          break;
        case IMMIGRATIE:
        case BINNEN:
        case NAAR_ANDERE:
        case EMIGRATIE:
          getField(NOGMAALS_AANSCHRIJVEN).setVisible(true);
          break;
        case NAAR_ONBEKEND:
          break;
      }
    }

    if (!getField(DATUM_EINDE_ONDERZOEK).isVisible()) {
      getField(DATUM_EINDE_ONDERZOEK).setValue(null);
    }
    if (!getField(NOGMAALS_AANSCHRIJVEN).isVisible()) {
      getField(NOGMAALS_AANSCHRIJVEN).setValue("");
    }
    if (!getField(TOELICHTING).isVisible()) {
      getField(TOELICHTING).setValue("");
    }
    onChangeBetrokkene(betrokkeneType);
    repaint();
  }

  protected abstract void onShowWarning(boolean showWarning);
}
