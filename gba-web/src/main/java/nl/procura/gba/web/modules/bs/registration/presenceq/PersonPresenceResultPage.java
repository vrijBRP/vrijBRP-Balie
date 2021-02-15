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

package nl.procura.gba.web.modules.bs.registration.presenceq;

import static java.lang.Long.parseLong;
import static nl.procura.java.reflection.ReflectionUtil.deepCopyBean;

import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import nl.bprbzk.bcgba.v14.BeheerIdenGegVraagDE;
import nl.procura.gba.jpa.personen.db.PresVraag;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.results.PresentatievraagResultsLayout;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.results.PresentievraagResultsWindow;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.results.PresentievraagToelichtingWindow;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagAntwoord;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagMatch;
import nl.procura.proweb.rest.utils.JsonUtils;

public class PersonPresenceResultPage extends NormalPageTemplate {

  private final Presentievraag     presentievraag;
  private DossierRegistration      dossier;
  private DossierPersoon           person;
  private Consumer<DossierPersoon> nextListener;

  public PersonPresenceResultPage(DossierRegistration dossier,
      DossierPersoon person,
      Consumer<DossierPersoon> addPersonListener,
      Presentievraag presentievraag) {

    super("Presentievraag - resultaat");
    this.dossier = dossier;

    this.person = person;
    this.nextListener = addPersonListener;
    this.presentievraag = presentievraag;

    PresentatievraagResultsLayout presentatievraagResultsLayout = new PresentatievraagResultsLayout(
        presentievraag) {

      @Override
      protected void navigateToResult(Presentievraag presentievraag, PresentievraagMatch match) {
        getParentWindow().addWindow(new PresentievraagResultsWindow(presentievraag, match));
      }
    };

    addButton(buttonNext);
    buttonClose.addListener(this);
    getMainbuttons().add(buttonClose);
    addExpandComponent(presentatievraagResultsLayout);
  }

  @Override
  public void onNextPage() {
    presentievraag.setZaakId(dossier.getDossier().getZaakId());
    presentievraag.setAntwoord(JsonUtils.toJson(presentievraag.getPresentievraagAntwoord()).trim());

    final String defaultText = "Deze presentievraag is uitgevoerd ten behoeve van een eerste inschrijving.";
    final PresentievraagToelichtingWindow commentaarWindow = new PresentievraagToelichtingWindow(defaultText) {

      @Override
      public void onSave(String comment) {
        presentievraag.setToelichting(comment);
        getServices().getPresentievraagService().save(presentievraag);
        fromPresenceQuestion();
        PersonPresenceResultPage.this.getWindow().closeWindow();
        nextListener.accept(person);
      }
    };
    getParentWindow().addWindow(commentaarWindow);
  }

  private void fromPresenceQuestion() {
    person.addPresentievraag(deepCopyBean(PresVraag.class, presentievraag));
    if (StringUtils.isBlank(person.getGeslachtsnaam())) {
      PresentievraagAntwoord antwoord = presentievraag.getPresentievraagAntwoord();
      if (antwoord != null) {
        BeheerIdenGegVraagDE vraag = antwoord.getGegevensVraag();
        if (vraag != null) {
          person.setVoornaam(vraag.getVoornamen());
          person.setGeslachtsnaam(vraag.getGeslachtsnaam());
          person.setVoorvoegsel(vraag.getVoorvoegselGeslachtsnaam());
          person.setGeslacht(Geslacht.get(vraag.getGeslachtsaanduiding()));
          person.setDatumGeboorte(new GbaDateFieldValue(parseLong(vraag.getGeboortedatum())));
        }
      }
    }
  }
}
