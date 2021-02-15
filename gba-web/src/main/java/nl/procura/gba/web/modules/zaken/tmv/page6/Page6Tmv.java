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

package nl.procura.gba.web.modules.zaken.tmv.page6;

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.exceptions.ProExceptionType.WEBSERVICE;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.gba.web.modules.zaken.tmv.layouts.TmvTabPage;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingRegistratie;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.window.Message;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page6Tmv extends TmvTabPage {

  private final Button buttonRegist = new Button("Registreren (F2)");
  private final Button buttonInzage = new Button("Status ophalen (F3)");
  private final Button buttonIntrek = new Button("Melding intrekken (F4)");

  public Page6Tmv(TerugmeldingAanvraag tmv) {

    super(tmv);
    setMargin(false);

    if (tmv.getDetails().size() > 0) {

      addButton(buttonRegist);
      addButton(buttonInzage);
      addButton(buttonIntrek);

      setInfoLayout();

      initLayout();
    } else {

      addComponent(new InfoLayout("Externe handeling niet mogelijk",
          "Bij deze terugmelding zijn geen elementen opgenomen en kan daarom niet bij de landelijk TMV worden geregistreerd."));
    }
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if ((button == buttonRegist) || (keyCode == KeyCode.F2)) {
      registreer();
    }

    if ((button == buttonInzage) || (keyCode == KeyCode.F3)) {
      inzage();
    }

    if ((button == buttonIntrek) || (keyCode == KeyCode.F4)) {
      intrekken();
    }

    super.handleEvent(button, keyCode);
  }

  private void initLayout() {

    Page6TmvForm2 registratieForm = new Page6TmvForm2();
    VaadinUtils.addComponent(this, new Page6TmvForm1(getTmv()));
    VaadinUtils.addComponent(this, new Page6TmvTable(getTmv(), registratieForm));
    VaadinUtils.addComponent(this, registratieForm);
  }

  private void intrekken() {

    try {

      TerugmeldingRegistratie r = getServices().getTerugmeldingService().intrekken(getTmv());

      new Message(getWindow(), "Verwerking succes", r.getVerwerkingomschrijving(), Message.TYPE_SUCCESS);

      initLayout();
    } catch (Exception e) {
      throw new ProException(WEBSERVICE, "Fout bij intrekken.", e);
    }
  }

  private void inzage() {

    try {

      TerugmeldingRegistratie r = getServices().getTerugmeldingService().inzage(getTmv());

      new Message(getWindow(), "Verwerking succes", r.getVerwerkingomschrijving(), Message.TYPE_SUCCESS);

      initLayout();
    } catch (Exception e) {
      throw new ProException(WEBSERVICE, "Fout bij inzage.", e);
    }
  }

  private void registreer() {

    try {

      TerugmeldingRegistratie r = getServices().getTerugmeldingService().registeer(getTmv());

      new Message(getWindow(), "Verwerking succes", r.getVerwerkingomschrijving(), Message.TYPE_SUCCESS);

      initLayout();
    } catch (Exception e) {
      throw new ProException(WEBSERVICE, "Fout bij registreren.", e);
    }
  }

  private void setInfoLayout() {

    if (fil(getTmv().getWaarschuwing().getMsg())) {
      InfoLayout il = new InfoLayout("Melding", getTmv().getWaarschuwing().getMsg());
      addComponent(il);
    }
  }
}
