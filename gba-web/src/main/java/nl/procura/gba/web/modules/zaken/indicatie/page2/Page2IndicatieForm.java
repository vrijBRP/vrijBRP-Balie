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

package nl.procura.gba.web.modules.zaken.indicatie.page2;

import static nl.procura.gba.web.modules.zaken.indicatie.page2.Page2IndicatieBean.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningIndicatie;
import nl.procura.gba.web.services.zaken.indicaties.IndicatieActie;
import nl.procura.standard.exceptions.ProException;

public class Page2IndicatieForm extends GbaForm<Page2IndicatieBean> {

  public Page2IndicatieForm(Page2IndicatieBean bean) {

    setCaption("Nieuwe aanvraag wijziging indicatie");
    setOrder(ACTIE, INDICATIE, TOELICHTING);
    setColumnWidths(WIDTH_130, "");
    setBean(bean);

    if (getField(ACTIE) != null) {
      getActieVeld().addListener(new FieldChangeListener<IndicatieActie>() {

        @Override
        public void onChange(IndicatieActie actie) {
          onWijzigActie(actie);
        }
      });
    }

    if (getField(INDICATIE) != null) {

      getIndicatieVeld().addListener(new FieldChangeListener<PlAantekeningIndicatie>() {

        @Override
        public void onChange(PlAantekeningIndicatie indicatie) {
          onWijzigIndicatie(indicatie);
        }
      });
    }

    onWijzigActie((IndicatieActie) getActieVeld().getValue());
  }

  protected void onWijzigActie(IndicatieActie actie) {
    getIndicatieVeld().setContainerDataSource(new IndicatieContainer(getApplication(), actie));
    repaint();
    if (actie == IndicatieActie.VERWIJDEREN) {
      if (getIndicatieVeld().getContainerDataSource().getItemIds().isEmpty()) {
        throw new ProException(INFO,
            "Deze persoon heeft geen indicaties op de persoonslijst of u bent niet geautoriseerd om ze te verwijderen");
      }
    }
  }

  @SuppressWarnings("unused")
  protected void onWijzigIndicatie(PlAantekeningIndicatie actie) {
  }

  private GbaNativeSelect getActieVeld() {
    return ((GbaNativeSelect) getField(ACTIE));
  }

  private GbaNativeSelect getIndicatieVeld() {
    return ((GbaNativeSelect) getField(INDICATIE));
  }
}
