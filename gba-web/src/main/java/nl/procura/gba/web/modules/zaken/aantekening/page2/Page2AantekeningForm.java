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

package nl.procura.gba.web.modules.zaken.aantekening.page2;

import static nl.procura.gba.web.modules.zaken.aantekening.page2.Page2AantekeningBean.*;
import static nl.procura.standard.Globalfunctions.*;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningHistorie;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningIndicatie;

public class Page2AantekeningForm extends GbaForm<Page2AantekeningBean> {

  public Page2AantekeningForm(GbaApplication application, final PlAantekeningHistorie historie,
      final boolean meestActuele, boolean isZaakAantekening) {

    if (isZaakAantekening) {
      setOrder(DATUM, GEBRUIKER, ONDERWERP, ONDERWERP_LABEL, STATUS, INHOUD, INHOUD_LABEL);
    } else {
      setOrder(DATUM, GEBRUIKER, INDICATIE, INDICATIE_LABEL, ONDERWERP, ONDERWERP_LABEL, STATUS, INHOUD,
          INHOUD_LABEL);
    }

    setColumnWidths(WIDTH_130, "");
    setReadonlyAsText(false);

    Page2AantekeningBean bean = new Page2AantekeningBean();

    if (isZaakAantekening) {
      historie.setIndicatie(new PlAantekeningIndicatie(PlAantekeningIndicatie.VRIJE_AANTEKENING));
    }

    if (historie != null) {
      bean.setDatum(tr(astr(historie.getTijdstip())));
      bean.setGebruiker(tr(historie.getGebruiker().getDescription()));

      bean.setOnderwerp(historie.getOnderwerp());
      bean.setOnderwerpLabel(astr(historie.getOnderwerp()));

      bean.setInhoud(astr(historie.getInhoud()));
      bean.setInhoudLabel(astr(historie.getInhoud()));

      bean.setIndicatie(historie.getIndicatie());
      bean.setIndicatieLabel(astr(historie.getIndicatie()));
      bean.setStatus(historie.getHistorieStatus());

      setBean(bean);

      if (getField(INDICATIE) != null) {
        GbaNativeSelect indicatie = ((GbaNativeSelect) getField(INDICATIE));
        indicatie.setContainerDataSource(new IndicatieContainer(application));

        if (historie.getIndicatie() != null) {
          indicatie.setValue(historie.getIndicatie());
        }

        bean.setIndicatie((PlAantekeningIndicatie) indicatie.getValue());
        indicatie.addListener(new FieldChangeListener<PlAantekeningIndicatie>() {

          @Override
          public void onChange(PlAantekeningIndicatie value) {
            checkVisibility(historie, historie.getIndicatie(), meestActuele);
          }
        });
      }

      checkVisibility(historie, historie.getIndicatie(), meestActuele);
    }

    if (meestActuele && getField(INHOUD) != null) {
      getField(INHOUD).setRequired(true);
    }
  }

  private void checkVisibility(PlAantekeningHistorie historie, PlAantekeningIndicatie indicatie,
      boolean meestActuele) {

    setVisible(ONDERWERP, !pos(historie.getCode()) && (indicatie != null && indicatie.isVrijeAantekening()));
    setVisible(ONDERWERP_LABEL, pos(historie.getCode()) && (indicatie != null && indicatie.isVrijeAantekening()));
    setVisible(INDICATIE, indicatie == null);
    setVisible(INDICATIE_LABEL, indicatie != null);
    setVisible(INHOUD, meestActuele);
    setVisible(INHOUD_LABEL, !meestActuele);
    setVisible(STATUS, pos(historie.getCode()) && (indicatie != null && indicatie.isVrijeAantekening()));

    repaint();
  }

  private void setVisible(String field, boolean visible) {
    if (getField(field) != null) {
      getField(field).setVisible(visible);
    }
  }

  private String tr(String x) {
    return (emp(x) ? "-" : x);
  }
}
