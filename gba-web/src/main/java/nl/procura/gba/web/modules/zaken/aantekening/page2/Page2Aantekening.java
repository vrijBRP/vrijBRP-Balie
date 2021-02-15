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

import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.common.misc.SimplePaginator;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.listeners.ChangeListener;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekening;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningHistorie;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Aantekening
 */
public class Page2Aantekening extends NormalPageTemplate {

  private final Button         buttonNewer = new Button("<b><</b>");
  private final Button         buttonOlder = new Button("<b>></b>");
  private final PlAantekening  aantekening;
  private final Label          pageLabel;
  private final ChangeListener listener;
  private Page2AantekeningForm form1;
  private SimplePaginator      paginator;

  public Page2Aantekening(final PlAantekening aantekening, ChangeListener listener) {

    this.aantekening = aantekening;
    this.listener = listener;

    buttonNewer.setHtmlContentAllowed(true);
    buttonOlder.setHtmlContentAllowed(true);

    pageLabel = new Label("");

    addButton(buttonPrev);
    addButton(buttonSave);
    addButton(buttonSave);

    getButtonLayout().addComponent(pageLabel);
    getButtonLayout().setComponentAlignment(pageLabel, Alignment.MIDDLE_RIGHT);
    getButtonLayout().setStyleName("rechts-uitgelijnd");

    addButton(buttonNewer);
    addButton(buttonOlder);

    getButtonLayout().setWidth("100%");
    getButtonLayout().setExpandRatio(pageLabel, 1f);

    setSpacing(true);
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      newPaginator();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonNewer) {
      paginator.previous();
    }

    if (button == buttonOlder) {
      paginator.next();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    if (buttonSave.isVisible() && buttonSave.isEnabled()) {

      form1.commit();

      PlAantekeningHistorie historie = new PlAantekeningHistorie();
      historie.setTijdstip(new DateTime());
      historie.setGebruiker(new UsrFieldValue(getApplication().getServices().getGebruiker()));
      historie.setIndicatie(form1.getBean().getIndicatie());
      historie.setOnderwerp(form1.getBean().getOnderwerp());
      historie.setInhoud(form1.getBean().getInhoud());
      historie.setHistorieStatus(form1.getBean().getStatus());

      getServices().getAantekeningService().save(aantekening, historie);

      successMessage("De gegevens zijn opgeslagen.");

      paginator.doCheck();
      listener.onChange();

      onPreviousPage();
    }

    super.onSave();
  }

  private void newPaginator() {

    paginator = new SimplePaginator() {

      @Override
      public void doCheck() {

        super.doCheck();

        pageLabel.setValue((getMax() - getCurrent()) + " van " + getMax());

        if (aantekening.getHistorie().size() > 0) {
          replaceForm(aantekening.getHistorie().get(getCurrent()), getCurrent() == 0);

        } else {

          // New record

          PlAantekeningHistorie historie = new PlAantekeningHistorie();
          historie.setGebruiker(new UsrFieldValue(getApplication().getServices().getGebruiker()));
          replaceForm(historie, true);
        }
      }

      @Override
      public int getMax() {
        return aantekening.getHistorie().size();
      }

      @Override
      public void onFirst() {

        buttonNewer.setEnabled(false);
        buttonOlder.setEnabled(true);
        buttonSave.setEnabled(true);
      }

      @Override
      public void onLast() {

        buttonNewer.setEnabled(true);
        buttonOlder.setEnabled(false);
        buttonSave.setEnabled(false);
      }

      @Override
      public void onMiddle() {

        buttonNewer.setEnabled(true);
        buttonOlder.setEnabled(true);
        buttonSave.setEnabled(false);
      }

      @Override
      public void onNoPages() {

        buttonNewer.setEnabled(false);
        buttonOlder.setEnabled(false);
        buttonSave.setEnabled(true);
      }
    };
  }

  private void replaceForm(PlAantekeningHistorie historie, boolean meestActueel) {

    if (!aantekening.isVerwijderbaar()) {

      buttonSave.setVisible(false);

      meestActueel = false;

      setInfo(
          "Dit is de aantekening op de persoonlijst. Wijzigen van deze aantekening is niet mogelijk via deze applicatie.");
    }

    Page2AantekeningForm form2 = new Page2AantekeningForm(getApplication(), historie, meestActueel,
        fil(aantekening.getZaakId()));

    replaceComponent(form1, form2);

    form1 = form2;
  }
}
