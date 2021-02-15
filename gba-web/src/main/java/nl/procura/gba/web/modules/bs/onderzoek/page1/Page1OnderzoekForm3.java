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

package nl.procura.gba.web.modules.bs.onderzoek.page1;

import static nl.procura.gba.web.modules.bs.onderzoek.page1.Page1OnderzoekBean.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.onderzoek.enums.OnderzoekAardType;
import nl.procura.gba.web.services.bs.onderzoek.enums.VermoedAdresType;
import nl.procura.vaadin.component.layout.table.TableLayout;

public abstract class Page1OnderzoekForm3 extends GbaForm<Page1OnderzoekBean> {

  public Page1OnderzoekForm3(DossierOnderzoek zaakDossier) {
    setCaption("Gegevens melding");
    setColumnWidths("200px", "");
    setOrder(DATUM_ONTVANGST, AARD, AARD_ANDERS, VERMOED_ADRES);
    setBean(zaakDossier);
  }

  @Override
  public Page1OnderzoekBean getNewBean() {
    return new Page1OnderzoekBean();
  }

  public void setBean(DossierOnderzoek zaakDossier) {
    Page1OnderzoekBean bean = new Page1OnderzoekBean();
    bean.setDatumOntvangst(zaakDossier.getDatumOntvangstMelding().getDate());
    bean.setAard(zaakDossier.getOnderzoekAard());
    bean.setAardAnders(zaakDossier.getOnderzoekAardAnders());
    bean.setVermoedAdres(zaakDossier.getVermoedelijkAdres());
    setBean(bean);
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
    getField(AARD).addListener((ValueChangeListener) event -> {
      OnderzoekAardType aard = (OnderzoekAardType) event.getProperty().getValue();
      getField(AARD_ANDERS).setVisible(OnderzoekAardType.ANDERS.equals(aard));
      repaint();
    });
    getField(AARD_ANDERS).setVisible(OnderzoekAardType.ANDERS.equals(getBean().getAard()));
    getField(VERMOED_ADRES).addListener((ValueChangeListener) event -> {
      onChangeVermoedAdres((VermoedAdresType) event.getProperty().getValue());
      repaint();
    });
  }

  protected abstract void onChangeVermoedAdres(VermoedAdresType value);

  @Override
  public void setColumn(TableLayout.Column column, Field field, Property property) {

    if (property.is(AARD_ANDERS)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }
}
