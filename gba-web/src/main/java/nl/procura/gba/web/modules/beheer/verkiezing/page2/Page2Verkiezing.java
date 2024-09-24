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

package nl.procura.gba.web.modules.beheer.verkiezing.page2;

import com.vaadin.ui.Button;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.KiesrVerk;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.verkiezing.page2.ongeldigverklaren.OngeldigVerklarenWindow;
import nl.procura.gba.web.modules.beheer.verkiezing.page3.Page3Verkiezing;
import nl.procura.gba.web.modules.beheer.verkiezing.page5.Page5Verkiezing;
import nl.procura.gba.web.services.beheer.verkiezing.KiezersregisterService;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2Verkiezing extends NormalPageTemplate {

  private Page2VerkiezingForm form                    = null;
  private KiesrVerk           verkiezing;
  private final Button        buttonImport            = new Button("Importeren");
  private final Button        buttonTekstblokken      = new Button("Tekstblokken");
  private final Button        buttonKiezersregister   = new Button("Kiezersregister");
  private final Button        buttonOngeldigverklaren = new Button("Ongeldig verklaren");

  public Page2Verkiezing(KiesrVerk verkiezing) {
    super("Toevoegen / muteren verkiezing");
    this.verkiezing = verkiezing;
    addButton(buttonPrev, buttonNew, buttonSave);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      KiezersregisterService service = getApplication().getServices().getKiezersregisterService();
      form = new Page2VerkiezingForm(verkiezing);
      form.setAantalStemgerechtigden(service.getAantalKiezers(verkiezing));

      OptieLayout ol = new OptieLayout();
      ol.getLeft().addComponent(form);
      ol.getRight().setCaption("Opties");
      ol.getRight().addButton(buttonImport, this);
      ol.getRight().addButton(buttonTekstblokken, this);
      ol.getRight().addButton(buttonKiezersregister, this);
      ol.getRight().addButton(buttonOngeldigverklaren, this);
      ol.getRight().setWidth("200px");
      addComponent(ol);
      checkVerkiezingButtons();
    }

    super.event(event);
  }

  @Override
  public void onNew() {
    this.verkiezing = new KiesrVerk();
    form.reset();
    checkVerkiezingButtons();
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonImport) {
      importStemDistricten();
    } else if (button == buttonTekstblokken) {
      toonTekstblokken();
    } else if (button == buttonOngeldigverklaren) {
      toonOngeldigverklarenWindow();
    } else if (button == buttonKiezersregister) {
      toonKiesgerechtigden();
    }
    super.handleEvent(button, keyCode);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {
    doSave();
    successMessage("De gegevens zijn opgeslagen.");
    checkVerkiezingButtons();
  }

  private void refreshStemgerechtigden() {
    KiezersregisterService service = getServices().getKiezersregisterService();
    verkiezing = service.getKiesrVerk(verkiezing);
    form.setAantalStemgerechtigden(service.getAantalKiezers(verkiezing));
  }

  private void importStemDistricten() {
    getApplication().getParentWindow().addWindow(new KiezersregisterUploadWindow(verkiezing) {

      @Override
      public void closeWindow() {
        super.closeWindow();
        refreshStemgerechtigden();
      }
    });
  }

  private void toonOngeldigverklarenWindow() {
    getApplication().getParentWindow().addWindow(new OngeldigVerklarenWindow(verkiezing));
  }

  private void toonTekstblokken() {
    getNavigation().addPage(new Page5Verkiezing(verkiezing));
  }

  private void toonKiesgerechtigden() {
    getNavigation().addPage(new Page3Verkiezing(verkiezing));
  }

  private void doSave() {
    form.commit();
    Page2VerkiezingBean bean = form.getBean();
    verkiezing.setCodeGemeente(bean.getGemeente().getLongValue());
    verkiezing.setGemeente(bean.getGemeente().getDescription());
    verkiezing.setAfkVerkiezing(bean.getAfkVerkiezing());
    verkiezing.setVerkiezing(bean.getVerkiezing());
    verkiezing.setdKand(new DateTime(bean.getDatumKandidaatstelling()).getDate());
    verkiezing.setdVerk(new DateTime(bean.getDatumVerkiezing()).getDate());
    verkiezing.setIndKiezerspas(bean.isKiezerspas());
    verkiezing.setIndBriefstembewijs(bean.isBriefstembewijs());
    verkiezing.setIndGemachtKiesr(bean.isGemachtigdeKiesregister());
    verkiezing.setAantalVolm(bean.getAantalVolmachten());
    getServices().getKiezersregisterService().save(verkiezing);
    checkVerkiezingButtons();
  }

  private void checkVerkiezingButtons() {
    buttonImport.setEnabled(this.verkiezing.isStored());
    buttonTekstblokken.setEnabled(this.verkiezing.isStored());
    buttonKiezersregister.setEnabled(this.verkiezing.isStored());
    buttonOngeldigverklaren.setEnabled(this.verkiezing.isStored());
  }
}
