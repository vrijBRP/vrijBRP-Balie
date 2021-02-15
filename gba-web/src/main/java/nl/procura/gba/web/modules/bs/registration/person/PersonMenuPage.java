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

package nl.procura.gba.web.modules.bs.registration.person;

import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.standard.exceptions.ProExceptionType.ENTRY;

import java.util.function.Consumer;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;

import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.proces.ProcessLayout;
import nl.procura.gba.web.modules.bs.registration.identification.IdentificationWindow;
import nl.procura.gba.web.modules.bs.registration.person.modules.module1.PersonPage;
import nl.procura.gba.web.modules.bs.registration.person.modules.module2.NationalityPage;
import nl.procura.gba.web.modules.bs.registration.person.modules.module3.ParticularPage;
import nl.procura.gba.web.modules.bs.registration.person.modules.module4.DutchTravelDocumentPage;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.PresentievragenWindow;
import nl.procura.gba.web.modules.zaken.contact.ContactWindow;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.zaken.contact.Contact;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.label.Ruler;
import nl.procura.vaadin.component.layout.VLayout;

public class PersonMenuPage extends NormalPageTemplate {

  private final ProcessLayout            processLayout = new ProcessLayout();
  private final Consumer<DossierPersoon> addPersonListener;
  private final DossierPersoon           person;
  private DossierRegistration            zaakDossier;
  private ButtonNavigation               navigation;

  public PersonMenuPage(DossierRegistration zaakDossier, DossierPersoon person,
      Consumer<DossierPersoon> addPersonListener) {
    super();
    this.zaakDossier = zaakDossier;
    this.addPersonListener = addPersonListener;
    this.person = person;

    addButton(buttonPrev);
    addButton(buttonNext, 1F);
    addButton(buttonClose);
  }

  @Override
  public void onPreviousPage() {
    navigation.previous();
  }

  @Override
  public void onNextPage() {
    navigation.next(this::onClose);
  }

  @Override
  protected void initPage() {
    super.initPage();

    processLayout.setWidths("200px", "100%");

    processLayout.addButton("1. Persoon",
        button -> navigation.goTo(new PersonPage(person, addPersonListener), button));

    processLayout.addButton("2. Nationaliteiten",
        button -> navigation.goTo(new NationalityPage(person, addPersonListener), button));

    processLayout.addButton("3. Nederlands reisdocument",
        button -> navigation.goTo(new DutchTravelDocumentPage(person, addPersonListener), button));

    processLayout.addButton("4. Overige",
        button -> navigation.goTo(new ParticularPage(person, addPersonListener), button));

    navigation = new ButtonNavigation(processLayout.getNavigation(),
        processLayout.getButtons(), this::setNavigationButtonVisibility);
    navigation.goTo(0);

    final VLayout leftLayout = this.processLayout.getOuterLayout();
    final Button contactInfo = new Button("Contactgegevens", e -> onContactInfo());
    final Button identificationInfo = new Button("Identificatie", e -> onAddIdentification());
    final Button pvInfo = new Button("Presentievragen", e -> onPvInfo());

    contactInfo.setWidth("100%");
    identificationInfo.setWidth("100%");
    pvInfo.setWidth("100%");

    leftLayout.setHeight("450px");
    leftLayout.setStyleName("registration-proceslayout");
    VLayout extraOptions = new VLayout(new Ruler(),
        contactInfo,
        identificationInfo,
        pvInfo)
            .addExpandComponent(new Label())
            .width("180px")
            .height("145px");
    leftLayout.addComponent(extraOptions);
    leftLayout.setComponentAlignment(extraOptions, Alignment.BOTTOM_CENTER);

    addExpandComponent(this.processLayout.getLayout());
  }

  private void setNavigationButtonVisibility(ButtonNavigation navigation) {
    buttonPrev.setEnabled(navigation.hasPrevious());
    // buttonNext is always enabled as last page should close window
  }

  private void onPvInfo() {
    getParentWindow().addWindow(new PresentievragenWindow(zaakDossier, person));
  }

  private void onContactInfo() {
    if (!person.getAnummer().isCorrect() || !person.getBurgerServiceNummer().isCorrect()) {
      throw new ProException(ENTRY, INFO, "Haal eerst een nieuw BSN en A-nummer op");
    }
    final Contact contact = new Contact(person.getAktenaam(), person.getAnr().longValue(), person.getBsn().longValue());
    getParentWindow().addWindow(new ContactWindow(contact, null));
  }

  private void onAddIdentification() {
    getParentWindow().addWindow(new IdentificationWindow(person, ButtonPageTemplate::onClose));
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
    super.onClose();
  }

}
