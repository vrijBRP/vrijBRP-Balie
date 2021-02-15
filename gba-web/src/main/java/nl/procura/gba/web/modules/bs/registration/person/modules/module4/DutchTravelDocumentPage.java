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

package nl.procura.gba.web.modules.bs.registration.person.modules.module4;

import static nl.procura.gba.web.services.bs.algemeen.functies.BsNatioUtils.heeftNederlandseNationaliteit;

import java.util.List;
import java.util.function.Consumer;

import nl.procura.gba.jpa.personen.db.DossTravelDoc;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.bs.registration.SingleEditFormWindow;
import nl.procura.gba.web.modules.bs.registration.person.modules.AbstractPersonPage;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.zaken.identiteit.IdentificatieType;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.standard.exceptions.ProExceptionType;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.table.indexed.IndexedTable;

public class DutchTravelDocumentPage extends AbstractPersonPage {

  private static final String            EMPTY_PASSPORT_CONFIRMATION = "Er zijn geen NL reisdocumentgegevens ingevuld, klopt dit?";
  private final DutchTravelDocumentTable table;

  public DutchTravelDocumentPage(DossierPersoon person, Consumer<DossierPersoon> addPersonListener) {
    super(person, addPersonListener);
    table = new DutchTravelDocumentTable(person, travelDoc -> onDoubleClickTravelDocument(person, travelDoc));
  }

  @Override
  protected void initPage() {
    buttonNew.setWidth("130px");
    buttonDel.setWidth("130px");
    buttonNew.setCaption("Toevoegen (F7)");

    final OptieLayout layout = new OptieLayout();
    layout.getLeft().addComponent(new Fieldset("Nederlandse reisdocumenten"));
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
    if (heeftNederlandseNationaliteit(getPerson()) && table.getRecords().isEmpty()) {
      getParentWindow().addWindow(new ConfirmDialog(EMPTY_PASSPORT_CONFIRMATION) {

        @Override
        public void buttonYes() {
          super.buttonYes();
          next.run();
        }

      });
    } else {
      next.run();
    }
  }

  @Override
  public void onDelete() {
    final List<IndexedTable.Record> records = table.getSelectedRecords();
    if (records.isEmpty()) {
      throw new ProException(ProExceptionType.SELECT, ProExceptionSeverity.WARNING, "Geen records geselecteerd.");
    }
    records.forEach(r -> getPerson().removeDossTravelDoc(r.getObject(DossTravelDoc.class)));
    table.init();
  }

  @Override
  public void onNew() {

    final DossierPersoon person = getPerson();
    final DossTravelDoc travelDoc = new DossTravelDoc(person);

    if (IdentificatieType.PASPOORT.getCode().equals(person.getSoort())) {
      travelDoc.setNedReisdoc(ReisdocumentType.EERSTE_NATIONAAL_PASPOORT.getCode());
      travelDoc.setDocNr(person.getIdDocNr());

    } else if (IdentificatieType.IDENTITEITSKAART.getCode().equals(person.getSoort())) {
      travelDoc.setNedReisdoc(ReisdocumentType.NEDERLANDSE_IDENTITEITSKAART.getCode());
      travelDoc.setDocNr(person.getIdDocNr());
    }
    DutchTravelDocumentForm form = new DutchTravelDocumentForm(person, travelDoc, this::onNewTravelDoc);
    showTravelDocForm(form);
  }

  private void onNewTravelDoc(DossTravelDoc travelDoc) {
    getPerson().addDossTravelDoc(travelDoc);
    getAddPersonListener().accept(getPerson());
    table.init();
  }

  private void onDoubleClickTravelDocument(DossierPersoon person, DossTravelDoc travelDoc) {
    DutchTravelDocumentForm form = new DutchTravelDocumentForm(person, travelDoc, t -> onEditTravelDoc());
    showTravelDocForm(form);
  }

  private void onEditTravelDoc() {
    getAddPersonListener().accept(getPerson());
    table.init();
  }

  private void showTravelDocForm(DutchTravelDocumentForm form) {
    SingleEditFormWindow window = new SingleEditFormWindow(form,
        "700px", "Nederlands reisdocument", "Nederlands reisdocument");
    getParentWindow().addWindow(window);
  }
}
