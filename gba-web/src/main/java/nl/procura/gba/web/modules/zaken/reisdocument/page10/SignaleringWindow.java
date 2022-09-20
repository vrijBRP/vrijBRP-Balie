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

package nl.procura.gba.web.modules.zaken.reisdocument.page10;

import static nl.procura.gba.web.modules.zaken.reisdocument.page10.PersoonBean.BSN;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.PersoonBean.GEBOREN;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.PersoonBean.GESLACHT;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.PersoonBean.NAAM;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.ResultaatBean.RESULTAAT_CODE;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.ResultaatBean.RESULTAAT_OMS;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.SignaleringBean.ARTIKELEN;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.SignaleringBean.CONTACTPERSOON;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.SignaleringBean.EMAIL;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.SignaleringBean.INSTANTIE;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.SignaleringBean.REGISTRATIEDATUM;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.SignaleringBean.TELEFOON;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.SignaleringBean.ZAAKNUMMER;

import java.util.concurrent.atomic.AtomicInteger;

import nl.procura.burgerzaken.vrsclient.model.Signalering;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.zaken.reisdocumenten.SignaleringResult;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.theme.twee.layout.ScrollLayout;

public class SignaleringWindow extends GbaModalWindow {

  public SignaleringWindow(SignaleringResult result) {
    setCaption(result.getResultaatOmschrijving() + " (Esc om te sluiten)");
    setWidth(1000, UNITS_PIXELS);
    VLayout layout = new VLayout()
        .margin(true)
        .spacing(true);
    result.getMededelingRvIG()
        .ifPresent(note -> layout.addComponent(new InfoLayout("Mededeling van RvIG", note)));

    addResultaatForm(result, layout);
    if (result.getPersoon() != null) {
      addPersoonForm(result, layout);
    }

    AtomicInteger nr = new AtomicInteger(1);
    addSignaleringenForms(result, layout, nr);

    ScrollLayout scrollLayout = new ScrollLayout(layout);
    scrollLayout.setSizeUndefined();
    setContent(scrollLayout);
  }

  private void addResultaatForm(SignaleringResult result, VLayout layout) {
    ReadOnlyForm<ResultaatBean> resultaatBeanReadOnlyForm = new ReadOnlyForm<ResultaatBean>() {

      @Override
      public ResultaatBean getNewBean() {
        return new ResultaatBean(result);
      }
    };

    resultaatBeanReadOnlyForm.setColumnWidths("130px", "");
    resultaatBeanReadOnlyForm.setOrder(RESULTAAT_CODE, RESULTAAT_OMS);
    resultaatBeanReadOnlyForm.setCaption("Resultaat");
    layout.addComponent(resultaatBeanReadOnlyForm);
  }

  private void addPersoonForm(SignaleringResult result, VLayout layout) {
    ReadOnlyForm<PersoonBean> persoonForm = new ReadOnlyForm<PersoonBean>() {

      @Override
      public PersoonBean getNewBean() {
        return new PersoonBean(result.getPersoon());
      }
    };

    persoonForm.setColumnWidths("130px", "");
    persoonForm.setOrder(BSN, NAAM, GESLACHT, GEBOREN);
    persoonForm.setCaption("Persoonsgegevens");
    layout.addComponent(persoonForm);
  }

  private void addSignaleringenForms(SignaleringResult result, VLayout layout, AtomicInteger nr) {
    for (Signalering signalering : result.getSignaleringen()) {
      ReadOnlyForm<SignaleringBean> signaleringForm = new ReadOnlyForm<SignaleringBean>() {

        @Override
        public SignaleringBean getNewBean() {
          return new SignaleringBean(signalering);
        }
      };
      signaleringForm.setOrder(REGISTRATIEDATUM, ARTIKELEN, INSTANTIE, ZAAKNUMMER, CONTACTPERSOON, TELEFOON, EMAIL);
      signaleringForm.setCaption(String.format("Signalering %d van %d",
          nr.getAndIncrement(), result.getSignaleringen().size()));
      layout.addComponent(signaleringForm);
    }
  }

  @Override
  public void attach() {
    super.attach();
    bringToFront();
  }
}
