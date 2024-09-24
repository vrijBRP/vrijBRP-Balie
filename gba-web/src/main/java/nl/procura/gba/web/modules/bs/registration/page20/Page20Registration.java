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

package nl.procura.gba.web.modules.bs.registration.page20;

import static java.util.Arrays.asList;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.vaadin.ui.Button;
import com.vaadin.ui.Window.CloseListener;

import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.bs.common.pages.zoekpage.BsZoekWindow;
import nl.procura.gba.web.modules.bs.registration.AbstractRegistrationPage;
import nl.procura.gba.web.modules.bs.registration.identification.IdentificationWindow;
import nl.procura.gba.web.modules.bs.registration.person.PersonWindow;
import nl.procura.gba.web.modules.bs.registration.person.modules.module1.RelativeNotInBrpWindow;
import nl.procura.gba.web.modules.bs.registration.presenceq.PersonPresenceWindow;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.search.PresentievraagZoekBean;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.DeclarationType;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.table.indexed.IndexedTable;

/**
 * First registration - persons
 */
public class Page20Registration extends AbstractRegistrationPage {

  private static final String PERSON_ADD               = "Persoon toevoegen";
  private static final String PERSON_REMOVE            = "Persoon verwijderen";
  private static final String CONFIRM_ON_REMOVE_PERSON = "Weet u zeker dat u de persoon wilt verwijderen? " +
      "<br/>Het gebruikte BSN en A-nummer zal verloren gaan.";

  private final Button buttonAddRegistrant = new Button(PERSON_ADD);
  private final Button buttonDelRegistrant = new Button(PERSON_REMOVE);

  private final Button buttonAddRelativeInBrp = new Button(PERSON_ADD);
  private final Button buttonDelRelativeInBrp = new Button(PERSON_REMOVE);

  private final Button buttonAddRelativeNotInBrp = new Button(PERSON_ADD);
  private final Button buttonDelRelativeNotInBrp = new Button(PERSON_REMOVE);

  private Page20RegistrantsTable       tableRegistrants;
  private Page20RelativesInBrpTable    tableRelativesInBrp;
  private Page20RelativesNotInBrpTable tableRelatives;
  private DossierPersoon               person;

  public Page20Registration() {
    super("Eerste inschrijving - in te schrijven personen");
  }

  @Override
  public void initPage() {
    super.initPage();
    addButton(buttonPrev);
    addButton(buttonNext);

    addComponent(registrantLayout);
    addComponent(createNewPeopleTable());
    addComponent(createRelativeFromBrpTable());
    addComponent(createRelativeTable());
  }

  private OptieLayout createNewPeopleTable() {
    tableRegistrants = new Page20RegistrantsTable(getZaakDossier(), this::savePerson);
    final OptieLayout optieLayout = new OptieLayout();
    optieLayout.getLeft().addComponent(new Fieldset("In te schrijven personen"));
    optieLayout.getLeft().addComponent(tableRegistrants);
    optieLayout.getRight().addButton(buttonAddRegistrant, this);
    optieLayout.getRight().addButton(buttonDelRegistrant, this);
    optieLayout.getRight().setCaption("Opties");
    optieLayout.getRight().setWidth("170px");

    return optieLayout;
  }

  private OptieLayout createRelativeFromBrpTable() {
    tableRelativesInBrp = new Page20RelativesInBrpTable(getZaakDossier());
    final OptieLayout layout = new OptieLayout();
    layout.getLeft().addComponent(new Fieldset("Gerelateerden in de BRP"));
    layout.getLeft().addComponent(tableRelativesInBrp);
    layout.getRight().addButton(buttonAddRelativeInBrp, this);
    layout.getRight().addButton(buttonDelRelativeInBrp, this);
    layout.getRight().setCaption("Opties");
    layout.getRight().setWidth("170px");
    return layout;
  }

  private OptieLayout createRelativeTable() {
    tableRelatives = new Page20RelativesNotInBrpTable(getZaakDossier());

    final OptieLayout layout = new OptieLayout();
    layout.getLeft().addComponent(new Fieldset("Gerelateerden niet in de BRP"));
    layout.getLeft().addComponent(tableRelatives);
    layout.getRight().addButton(buttonAddRelativeNotInBrp, this);
    layout.getRight().addButton(buttonDelRelativeNotInBrp, this);
    layout.getRight().setCaption("Opties");
    layout.getRight().setWidth("170px");
    return layout;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (buttonAddRegistrant.equals(button)) {
      onAddRegistrant();
    }
    if (buttonDelRegistrant.equals(button)) {
      onDeletePersoon();
    }
    if (buttonDelRelativeInBrp.equals(button)) {
      onDeleteRelativeInBrp();
    }
    if (buttonDelRelativeNotInBrp.equals(button)) {
      onDeleteRelativeNotInBrp();
    }
    if (buttonAddRelativeInBrp.equals(button)) {
      onAddRelativeInBrp();
    }
    if (buttonAddRelativeNotInBrp.equals(button)) {
      onAddRelativeNotInBrp();
    }
    super.handleEvent(button, keyCode);
  }

  @Override
  public boolean checkPage() {
    if (super.checkPage()) {
      getServices().getRegistrationService().saveRegistration(getZaakDossier());
      if (tableRegistrants.getRecords().isEmpty()) {
        throw new ProException(WARNING, "Er is nog geen in te schrijven persoon toegevoegd. "
            + "Voeg ten minste één in te schrijven persoon toe om verder te gaan.");
      }
      return true;
    }

    return false;
  }

  private void openPersonWindow() {
    getParentWindow().addWindow(new PersonWindow(getZaakDossier(), person, this::savePerson));
  }

  private void savePerson(DossierPersoon person) {
    getZaakDossier().getDossier().toevoegenPersoon(person);
    tableRegistrants.setRecords();
    checkPage();
  }

  private void onDeleteRelativeInBrp() {
    new DeleteProcedure<Object>(tableRelativesInBrp) {

      @Override
      protected void deleteValue(final Object value) {
        deleteTableRecord(value);
        tableRelativesInBrp.init();
      }
    };
  }

  private void onDeleteRelativeNotInBrp() {
    new DeleteProcedure<Object>(tableRelatives) {

      @Override
      protected void deleteValue(final Object value) {
        deleteTableRecord(value);
        tableRelatives.init();
      }
    };
  }

  private void onDeletePersoon() {
    final List<IndexedTable.Record> records = tableRegistrants.getSelectedRecords();
    if (records.isEmpty()) {
      throw new ProException(ProExceptionSeverity.WARNING, "Er zijn geen records om te verwijderen");
    }

    getParentWindow().addWindow(new ConfirmDialog(CONFIRM_ON_REMOVE_PERSON, 400) {

      @Override
      public void buttonYes() {
        deleteTableRecord(records.get(0).getObject());
        tableRegistrants.init();
        super.buttonYes();
      }
    });
  }

  private void deleteTableRecord(Object value) {
    final DossierPersoon toDelete = (DossierPersoon) value;
    getServices().getDossierService().deletePersonen(getZaakDossier().getDossier(), asList(toDelete));
  }

  private void onAddRegistrant() {
    person = new DossierPersoon(DossierPersoonType.INSCHRIJVER);
    getParentWindow().addWindow(new IdentificationWindow(person,
        page -> onStartPresenceQuestion()));
  }

  private void onAddRelativeInBrp() {
    final DossierPersoon relative = new DossierPersoon(DossierPersoonType.GERELATEERDE_BRP);
    final BsZoekWindow zoekWindow = new BsZoekWindow(relative, new ArrayList<>());
    zoekWindow.addListener((CloseListener) e -> {
      if (relative.getBsn() != null) {
        getZaakDossier().getDossier().toevoegenPersoon(relative);
        successMessage("Persoon " + relative.getAktenaam() + " is toegevoegd");

      }
      tableRelativesInBrp.setRecords();
    });

    getParentWindow().addWindow(zoekWindow);
  }

  private void onAddRelativeNotInBrp() {
    final DossierPersoon relative = new DossierPersoon(DossierPersoonType.GERELATEERDE_NIET_BRP);
    final RelativeNotInBrpWindow window = new RelativeNotInBrpWindow(relative, dossierPersoon -> {
      getZaakDossier().getDossier().toevoegenPersoon(dossierPersoon);
      successMessage("Persoon " + relative.getAktenaam() + " is toegevoegd");
      tableRelatives.setRecords();
    });

    getParentWindow().addWindow(window);
  }

  private void onStartPresenceQuestion() {
    getParentWindow().addWindow(new PersonPresenceWindow(getZaakDossier(), person,
        getSearchBean(), getDossierPersoon()));
  }

  private Consumer<DossierPersoon> getDossierPersoon() {
    return dossierPersoon -> {
      getImportRegistrant().ifPresent(registrant -> {
        dossierPersoon.setGeslachtsnaam(registrant.getLastname());
        dossierPersoon.setVoornaam(registrant.getFirstname());
        dossierPersoon.setVoorvoegsel(registrant.getPrefix());
        dossierPersoon.setGeslacht(Geslacht.get(registrant.getGender()));
        dossierPersoon.setDatumGeboorte(new GbaDateFieldValue(registrant.getBirthDate().getSystemDate()));
        dossierPersoon.setGeboorteland(GbaTables.LAND.getByDescr(registrant.getBirthCountry()));
        dossierPersoon.setGeboorteplaats(GbaTables.PLAATS.getByDescr(registrant.getBirthPlace()));
        dossierPersoon.setBron(DeclarationType.REGISTERED.getCode());
      });
      openPersonWindow();
    };
  }

  private Supplier<PresentievraagZoekBean> getSearchBean() {
    return () -> getImportRegistrant().map(registrant -> {
      PresentievraagZoekBean zb = new PresentievraagZoekBean();
      zb.setGeslachtsnaam(registrant.getLastname());
      zb.setVoorvoegsel(registrant.getPrefix());
      zb.setGeboortedatum(new GbaDateFieldValue(registrant.getBirthDate().getSystemDate()));
      zb.setGeslacht(Geslacht.get(registrant.getGender()));
      return zb;
    }).orElse(null);
  }
}
