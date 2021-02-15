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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab4.search;

import com.vaadin.ui.Button;
import com.vaadin.ui.Window;

import nl.procura.bvbsn.BvBsnActionTemplate;
import nl.procura.bvbsn.actions.IDGegevens;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.ZoekBean;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.ZoekTabPage;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab4.search.window.BvBsnWindow;
import nl.procura.gba.web.services.gba.verificatievraag.VerificatievraagService.VerificatieActie;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Tab4SearchPage extends ZoekTabPage {

  private Tab4Form form = new Tab4Form();

  private Button         button1 = new Button("Toets BSN");
  private Button         button2 = new Button("Zoek ID");
  private Button         button3 = new Button("Toets BSN/ID");
  private Button         button4 = new Button("Toets BSN");
  private Button         button5 = new Button("Toets document");
  private final String[] order;

  public Tab4SearchPage(String title, String... order) {

    super(title);

    setMargin(true);
    addStyleName("");

    this.order = order;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setInlogOpmerking();

      addButton(buttonSearch);
      addButton(buttonReset);
      addButton(button1);
      addButton(button2);
      addButton(button3);
      addButton(button4);
      addButton(button5);

      getButtonLayout().setExpandRatio(buttonSearch, 1f);
      getButtonLayout().setWidth("100%");

      buttonSearch.setCaption("Zoeken (Enter)");

      form.setOrder(order);

      addComponent(form);
      setExpandRatio(form, 1);
    }

    super.event(event);
  }

  public Button getButton1() {
    return button1;
  }

  public void setButton1(Button button1) {
    this.button1 = button1;
  }

  public Button getButton2() {
    return button2;
  }

  public void setButton2(Button button2) {
    this.button2 = button2;
  }

  public Button getButton3() {
    return button3;
  }

  public void setButton3(Button button3) {
    this.button3 = button3;
  }

  public Button getButton4() {
    return button4;
  }

  public void setButton4(Button button4) {
    this.button4 = button4;
  }

  public Button getButton5() {
    return button5;
  }

  public void setButton5(Button button5) {
    this.button5 = button5;
  }

  public Tab4Form getForm() {
    return form;
  }

  public void setForm(Tab4Form form) {
    this.form = form;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == button1) {
      getNavigation().goToPage(Tab4SearchPage1.class);
    } else if (button == button2) {
      getNavigation().goToPage(Tab4SearchPage2.class);
    } else if (button == button3) {
      getNavigation().goToPage(Tab4SearchPage3.class);
    } else if (button == button4) {
      getNavigation().goToPage(Tab4SearchPage4.class);
    } else if (button == button5) {
      getNavigation().goToPage(Tab4SearchPage5.class);
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  @Override
  public void onNew() {
    form.reset();
  }

  @Override
  public void onSearch() {
    form.commit();
    zoek();
  }

  @Override
  public String toString() {

    return "Tab4SearchPage [button1=" + button1 + ", button2=" + button2 + ", button3=" + button3 + ", button4="
        + button4 + ", button5=" + button5 + "]";
  }

  protected IDGegevens getIdGegevens() {

    ZoekBean b = form.getBean();

    IDGegevens idGegevens = new IDGegevens();

    idGegevens.setAanduidingBijHuisnummer("");
    idGegevens.setGeboortedatum(b.getGeboortedatum().getStringValue());
    idGegevens.setGeboorteland("");
    idGegevens.setGeboorteplaats("");
    idGegevens.setGemeenteVanInschrijving("");

    if (b.getGeslacht() != null) {
      idGegevens.setGeslachtsaanduiding(b.getGeslacht().getAfkorting());
    }

    idGegevens.setGeslachtsnaam(b.getGeslachtsnaam());
    idGegevens.setHuisletter("");
    idGegevens.setHuisnummer(b.getHnr());
    idGegevens.setHuisnummertoevoeging("");
    idGegevens.setLandVanwaarIngeschreven("");
    idGegevens.setLocatiebeschrijving("");
    idGegevens.setPostcode(b.getPostcode());

    if (b.getStraat() != null) {
      idGegevens.setStraatnaam(b.getStraat().getDescription());
    }

    idGegevens.setVoornamen(b.getVoornaam());

    if (b.getVoorvoegsel() != null) {
      idGegevens.setVoorvoegselGeslachtsnaam(b.getVoorvoegsel().getDescription());
    }

    return idGegevens;
  }

  protected void zoek() {
  }

  protected void zoek(Window window, BvBsnActionTemplate actie) {

    VerificatieActie vactie = getServices().getVerificatievraagService().find(actie);

    if (vactie != null) {
      window.addWindow(new BvBsnWindow(vactie.getTitle(), actie));
    }
  }
}
