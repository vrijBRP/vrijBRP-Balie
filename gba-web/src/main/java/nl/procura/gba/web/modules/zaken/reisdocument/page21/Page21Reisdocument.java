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

package nl.procura.gba.web.modules.zaken.reisdocument.page21;

import static nl.procura.gba.web.modules.zaken.reisdocument.page21.Page21ReisdocumentBean.CONCLUSIE1;
import static nl.procura.gba.web.modules.zaken.reisdocument.page21.Page21ReisdocumentBean.CONCLUSIE2;
import static nl.procura.gba.web.modules.zaken.reisdocument.page21.Page21ReisdocumentBean.CONCLUSIE3;
import static nl.procura.gba.web.modules.zaken.reisdocument.page21.Page21ReisdocumentBean.CONCLUSIE4;
import static nl.procura.gba.web.modules.zaken.reisdocument.page21.Page21ReisdocumentBean.CONSTATERING1;
import static nl.procura.gba.web.modules.zaken.reisdocument.page21.Page21ReisdocumentBean.CONSTATERING2;
import static nl.procura.gba.web.modules.zaken.reisdocument.page21.Page21ReisdocumentBean.CONSTATERING3;
import static nl.procura.gba.web.modules.zaken.reisdocument.page21.Page21ReisdocumentBean.CONSTATERING4;
import static nl.procura.gba.web.modules.zaken.reisdocument.page21.Page21ReisdocumentBean.CURATOR_LABEL;
import static nl.procura.gba.web.modules.zaken.reisdocument.page21.Page21ReisdocumentBean.CURATOR_TEXT;
import static nl.procura.gba.web.modules.zaken.reisdocument.page21.Page21ReisdocumentBean.DERDE_LABEL;
import static nl.procura.gba.web.modules.zaken.reisdocument.page21.Page21ReisdocumentBean.DERDE_TEXT;
import static nl.procura.gba.web.modules.zaken.reisdocument.page21.Page21ReisdocumentBean.OUDER1_LABEL;
import static nl.procura.gba.web.modules.zaken.reisdocument.page21.Page21ReisdocumentBean.OUDER2_LABEL;
import static nl.procura.gba.web.modules.zaken.reisdocument.page21.Page21ReisdocumentBean.TOESTEMMING1;
import static nl.procura.gba.web.modules.zaken.reisdocument.page21.Page21ReisdocumentBean.TOESTEMMING2;
import static nl.procura.gba.web.modules.zaken.reisdocument.page21.Page21ReisdocumentBean.TOESTEMMING3;
import static nl.procura.gba.web.modules.zaken.reisdocument.page21.Page21ReisdocumentBean.TOESTEMMING4;
import static nl.procura.gba.web.services.zaken.reisdocumenten.ToestemmingGegevenType.JA;
import static nl.procura.gba.web.services.zaken.reisdocumenten.ToestemmingGegevenType.VERVANGENDE;
import static nl.procura.standard.Globalfunctions.toBigDecimal;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.gba.web.modules.zaken.curatele.CurateleWindow;
import nl.procura.gba.web.modules.zaken.reisdocument.ReisdocumentAanvraagPage;
import nl.procura.gba.web.modules.zaken.reisdocument.page11.Page11Reisdocument;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentService;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentUtils;
import nl.procura.gba.web.services.zaken.reisdocumenten.Toestemming;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Toestemming van ouders + derde voor reisdocument van kind
 */
public class Page21Reisdocument extends ReisdocumentAanvraagPage {

  private final Button buttonCuratele = new Button("Curateleregister (F3)");

  private final boolean          leeftijdToestemming;
  private Page21ReisdocumentForm form1 = null;
  private Page21ReisdocumentForm form2 = null;
  private Page21ReisdocumentForm form3 = null;
  private Page21ReisdocumentForm form4 = null;

  public Page21Reisdocument(boolean leeftijdToestemming, ReisdocumentAanvraag aanvraag) {
    super("Reisdocument: toestemming bij nieuw document", aanvraag);
    this.leeftijdToestemming = leeftijdToestemming;

    addButton(buttonPrev);
    addButton(buttonNext);
    addButton(buttonCuratele);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      getToestemming(getAanvraag().getToestemmingen().getToestemmingOuder1());
      getToestemming(getAanvraag().getToestemmingen().getToestemmingOuder2());
      getToestemming(getAanvraag().getToestemmingen().getToestemmingDerde());
      getToestemming(getAanvraag().getToestemmingen().getToestemmingCurator());

      setInfo("Toestemming voor aanvraag van " + getAanvraag().getReisdocumentType() + " voor kind");
      form1 = new Form1(getAanvraag());
      addComponent(form1);

      form2 = new Form2(getAanvraag());
      addComponent(form2);

      form3 = new Form3(getAanvraag());
      addComponent(form3);

      form4 = new Form4(getAanvraag());
      addComponent(form4);
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonCuratele || keyCode == KeyCode.F3) {
      getWindow().addWindow(new CurateleWindow(getPl()));
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNextPage() {

    form1.commit();
    form2.commit();
    form3.commit();
    form4.commit();

    Toestemming t1 = getAanvraag().getToestemmingen().getToestemmingOuder1();
    Toestemming t2 = getAanvraag().getToestemmingen().getToestemmingOuder2();
    Toestemming t3 = getAanvraag().getToestemmingen().getToestemmingDerde();
    Toestemming t4 = getAanvraag().getToestemmingen().getToestemmingCurator();

    t1.setGegeven(form1.getBean().getToestemming1());
    t2.setGegeven(form2.getBean().getToestemming2());
    t3.setGegeven(form3.getBean().getToestemming3());
    t4.setGegeven(form4.getBean().getToestemming4());

    if (JA.equals(t3.getGegeven()) || VERVANGENDE.equals(t3.getGegeven())) {
      String naamDerde = form3.getBean().getDerdeText();
      getAanvraag().getToestemmingen().getToestemmingDerde().setNaam(naamDerde);
    } else {
      getAanvraag().getToestemmingen().getToestemmingDerde().setNaam("");
    }

    if (JA.equals(t4.getGegeven()) || VERVANGENDE.equals(t3.getGegeven())) {
      String naamCurator = form4.getBean().getCuratorText();
      getAanvraag().getToestemmingen().getToestemmingCurator().setNaam(naamCurator);
    } else {
      getAanvraag().getToestemmingen().getToestemmingCurator().setNaam("");
    }

    t1.opslaan();
    t2.opslaan();
    t3.opslaan();
    t4.opslaan();

    int aantalToestemmingen = 0;
    for (Toestemming t : getAanvraag().getToestemmingen().getToestemmingen()) {
      if (t.isGegeven()) {
        aantalToestemmingen++;
      }
    }

    checkAantalToestemmingen(aantalToestemmingen);

    super.onNextPage();
  }

  private void toNextPage(int aantalToestemmingen) {
    getAanvraag().setAantalToestemmingen(toBigDecimal(aantalToestemmingen));

    ReisdocumentService service = getApplication().getServices().getReisdocumentService();
    service.save(getAanvraag());

    getNavigation().goToPage(new Page11Reisdocument(getAanvraag()));
  }

  private void checkAantalToestemmingen(final int aantalToestemmingen) {

    if (aantalToestemmingen == 0) {
      ConfirmDialog confirmDialog = new ConfirmDialog("Er is geen toestemming gegeven, doorgaan?") {

        @Override
        public void buttonYes() {
          toNextPage(aantalToestemmingen);
          super.buttonYes();
        }
      };
      getWindow().addWindow(confirmDialog);

    } else if (aantalToestemmingen > 2) {
      throw new ProException(WARNING, "Er mag maximaal door 2 personen toestemming worden gegeven");

    } else {
      toNextPage(aantalToestemmingen);
    }
  }

  private void getToestemming(Toestemming toestemming) {
    ReisdocumentUtils.setDocumentAanvraagToestemming(getPl(), leeftijdToestemming, toestemming, getServices());
  }

  public class Form1 extends Page21ReisdocumentForm {

    public Form1(ReisdocumentAanvraag aanvraag) {
      super(aanvraag);
    }

    @Override
    public void initOrder() {
      setCaption("Ouder 1");
      setOrder(TOESTEMMING1, OUDER1_LABEL, CONSTATERING1, CONCLUSIE1);
    }
  }

  public class Form2 extends Page21ReisdocumentForm {

    public Form2(ReisdocumentAanvraag aanvraag) {
      super(aanvraag);
    }

    @Override
    public void initOrder() {
      setCaption("Ouder 2");
      setOrder(TOESTEMMING2, OUDER2_LABEL, CONSTATERING2, CONCLUSIE2);
    }
  }

  public class Form3 extends Page21ReisdocumentForm {

    public Form3(ReisdocumentAanvraag aanvraag) {
      super(aanvraag);
    }

    @Override
    public void initOrder() {
      setCaption("(Mede) gezaghouder");
      setOrder(TOESTEMMING3, DERDE_LABEL, DERDE_TEXT, CONSTATERING3, CONCLUSIE3);
    }
  }

  public class Form4 extends Page21ReisdocumentForm {

    public Form4(ReisdocumentAanvraag aanvraag) {
      super(aanvraag);
    }

    @Override
    public void initOrder() {
      setCaption("Curator");
      setOrder(TOESTEMMING4, CURATOR_LABEL, CURATOR_TEXT, CONSTATERING4, CONCLUSIE4);
    }
  }
}
