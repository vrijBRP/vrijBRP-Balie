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

package nl.procura.gba.web.modules.bs.registration.person.modules.module2;

import static java.util.stream.Collectors.toList;
import static nl.procura.gba.web.services.zaken.identiteit.IdentificatieType.IDENTITEITSKAART;
import static nl.procura.gba.web.services.zaken.identiteit.IdentificatieType.PASPOORT;

import java.util.List;
import java.util.function.Consumer;

import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.bs.registration.SingleEditFormWindow;
import nl.procura.gba.web.modules.bs.registration.person.modules.AbstractPersonPage;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.standard.exceptions.ProExceptionType;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.table.indexed.IndexedTable;

public class NationalityPage extends AbstractPersonPage {

  private static final String CONFIRM_ON_EMPTY_NATIONALITY = "Er is geen nationaliteit toegevoegd. <br/>Is dit correct?";

  private final NationalityTable table;

  public NationalityPage(DossierPersoon person, Consumer<DossierPersoon> addPersonListener) {
    super(person, addPersonListener);
    table = new NationalityTable(this, this::onDoubleClickNationality);
  }

  @Override
  protected void initPage() {
    buttonNew.setWidth("130px");
    buttonDel.setWidth("130px");
    buttonNew.setCaption("Toevoegen (F7)");

    final OptieLayout layout = new OptieLayout();
    layout.getLeft().addComponent(new Fieldset("Nationaliteiten"));
    layout.getLeft().addExpandComponent(table);

    layout.getRight().setWidth("140px");
    layout.getRight().setCaption("Opties");
    layout.getRight().addButton(buttonNew, this);
    layout.getRight().addButton(buttonDel, this);

    table.init();

    addExpandComponent(layout);
    super.initPage();
  }

  @Override
  public void checkPage(Runnable next) {
    if (getPerson().getNationaliteiten().isEmpty()) {
      getParentWindow().addWindow(new ConfirmDialog(CONFIRM_ON_EMPTY_NATIONALITY, 300) {

        @Override
        public void buttonYes() {
          super.buttonYes();
          next.run();
        }

        @Override
        public void buttonNo() {
          super.buttonNo();
        }
      });
    } else {
      next.run();
    }
  }

  @Override
  public void onNew() {
    DossierNationaliteit nationality = new DossierNationaliteit();
    nationality.setSourceDescription(getSourceDocument());
    NationalityDetailsForm nationalityForm = new NationalityDetailsForm(nationality, this::onNewNationality);
    showNationalityForm(nationalityForm);
  }

  private void onNewNationality(DossierNationaliteit nationality) {
    DossierPersoon person = getPerson();
    person.toevoegenNationaliteit(nationality);
    getAddPersonListener().accept(person);
    table.init();
  }

  private void onDoubleClickNationality(DossierNationaliteit nationality) {
    NationalityDetailsForm nationalityForm = new NationalityDetailsForm(nationality, n -> onEditNationality());
    showNationalityForm(nationalityForm);
  }

  private void onEditNationality() {
    getAddPersonListener().accept(getPerson());
    table.init();
  }

  private void showNationalityForm(NationalityDetailsForm nationalityForm) {
    getParentWindow().addWindow(new SingleEditFormWindow(nationalityForm, "700px", "Nationaliteit", "Nationaliteit"));
  }

  @Override
  public void onDelete() {
    final List<IndexedTable.Record> records = table.getSelectedRecords();
    if (records.isEmpty()) {
      throw new ProException(ProExceptionType.SELECT, ProExceptionSeverity.WARNING, "Geen records geselecteerd.");
    }
    // getPerson().verwijderNationaliteit doesn't delete nationality from DB somehow
    List<DossierNationaliteit> nationalities = records.stream()
        .map(r -> r.getObject(DossierNationaliteit.class))
        .collect(toList());
    getServices().getDossierService().deleteNationaliteiten(getPerson(), nationalities);
    table.init();
  }

  private String getSourceDocument() {
    String description = "";
    if (PASPOORT.getCode().equals(getPerson().getSoort())) {
      description = PASPOORT + " " + getPerson().getIssueingCountry();
    } else if (IDENTITEITSKAART.getCode().equals(getPerson().getSoort())) {
      description = IDENTITEITSKAART + " " + getPerson().getIssueingCountry();
    }
    return description;
  }
}
