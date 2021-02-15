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

import static nl.procura.gba.web.modules.bs.onderzoek.page1.Page1OnderzoekBean.BRON;
import static nl.procura.gba.web.modules.bs.onderzoek.page1.Page1OnderzoekBean.RELATIE;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.onderzoek.enums.OnderzoekBronType;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public abstract class Page1OnderzoekForm1 extends GbaForm<Page1OnderzoekBean> {

  public Page1OnderzoekForm1(DossierOnderzoek zaakDossier) {
    setCaption("Aangever");
    setColumnWidths("200px", "");
    setOrder(BRON, RELATIE);
    setBean(zaakDossier);
  }

  public void setBean(DossierOnderzoek zaakDossier) {

    Page1OnderzoekBean bean = new Page1OnderzoekBean();
    bean.setBron(zaakDossier.getOnderzoekBron());
    bean.setRelatie(zaakDossier.getAanlRelatie());
    bean.setNr(zaakDossier.getAanlTmvNr());
    bean.setInstantie(zaakDossier.getAanlInst());
    bean.setTavAanhef(new FieldValue(zaakDossier.getAanlInstAanhef()));
    bean.setTavVoorl(zaakDossier.getAanlInstVoorl());
    bean.setTavNaam(zaakDossier.getAanlInstNaam());
    bean.setAdres(zaakDossier.getAanlInstAdres());
    bean.setPc(new FieldValue(zaakDossier.getAanlInstPc()));
    bean.setPlaats(zaakDossier.getAanlInstPlaats());
    bean.setKenmerk(zaakDossier.getAanlKenmerk());
    bean.setDatumOntvangst(zaakDossier.getDatumOntvangstMelding().getDate());
    bean.setAard(zaakDossier.getOnderzoekAard());
    bean.setAardAnders(zaakDossier.getOnderzoekAardAnders());
    bean.setVermoedAdres(zaakDossier.getVermoedelijkAdres());

    setBean(bean);
  }

  @Override
  public Page1OnderzoekBean getNewBean() {
    return new Page1OnderzoekBean();
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
    getField(BRON).addListener((ValueChangeListener) event -> {
      onChangeBron((OnderzoekBronType) event.getProperty().getValue());
      repaint();
    });
  }

  protected abstract void onChangeBron(OnderzoekBronType bron);
}
