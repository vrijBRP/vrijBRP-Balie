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

package nl.procura.gba.web.modules.bs.registration.person.modules.module1;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module1.PersonBean.*;
import static nl.procura.gba.web.services.bs.registration.SourceDocumentType.CUSTOM;
import static nl.procura.gba.web.services.zaken.identiteit.IdentificatieType.IDENTITEITSKAART;
import static nl.procura.gba.web.services.zaken.identiteit.IdentificatieType.PASPOORT;
import static nl.procura.standard.Globalfunctions.*;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

import com.vaadin.ui.Button;

import nl.procura.bsm.rest.v1_0.objecten.gba.probev.idnumbers.IdNumbersResponseRestElement;
import nl.procura.gba.jpa.personen.db.DossSourceDoc;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.bs.registration.person.modules.AbstractPersonPage;
import nl.procura.gba.web.modules.zaken.common.SourceDocumentForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.SourceDocumentType;
import nl.procura.gba.web.services.bs.registration.ValidityDateType;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.validation.Anummer;
import nl.procura.validation.Bsn;

public class PersonPage extends AbstractPersonPage {

  private static final List<ValidityDateType> DATE_TYPES = asList(ValidityDateType.DATE_OF_BIRTH,
      ValidityDateType.UNKNOWN, ValidityDateType.CUSTOM);

  private static final List<SourceDocumentType> DOCUMENT_TYPES = asList(SourceDocumentType.DUTCH,
      SourceDocumentType.CUSTOM);

  private final SourceDocumentForm sourceDocumentForm;
  private PersonalDetailsForm      personalDetailsForm;
  private BirthDetailsForm         birthDetailsForm;

  public PersonPage(DossierPersoon person, Consumer<DossierPersoon> addPersonListener) {
    super(person, addPersonListener);
    sourceDocumentForm = new SourceDocumentForm(fromPerson(person), DOCUMENT_TYPES, DATE_TYPES, null);
  }

  private DossSourceDoc fromPerson(DossierPersoon person) {
    return person.getDossSourceDoc().orElseGet(this::getNewDossSourceDoc);
  }

  private DossSourceDoc getNewDossSourceDoc() {
    final DossSourceDoc doc = DossSourceDoc.newNotSetSourceDocument();
    final DossierPersoon person = getPerson();
    String description = "";
    if (PASPOORT.getCode().equals(person.getSoort())) {
      description = PASPOORT + " " + person.getIssueingCountry();
      doc.setDocType(CUSTOM.getCode());
    } else if (IDENTITEITSKAART.getCode().equals(person.getSoort())) {
      description = IDENTITEITSKAART + " " + person.getIssueingCountry();
      doc.setDocType(CUSTOM.getCode());
    }
    doc.setDocDescr(description);
    return doc;
  }

  @Override
  public void initPage() {
    final Button bsnButton = new Button("Nieuw BSN", e -> onNewBsn());
    bsnButton.setWidth("130px");

    final Button anrButton = new Button("Nieuw A-nummer", e -> onNewANumber());
    anrButton.setWidth("130px");

    personalDetailsForm = new PersonalDetailsForm();
    birthDetailsForm = new BirthDetailsForm();

    personalDetailsForm.update(getPerson());
    birthDetailsForm.update(getPerson());

    bsnButton.setEnabled(emp(personalDetailsForm.getBean().getBsn()));
    anrButton.setEnabled(emp(personalDetailsForm.getBean()
        .getAnr() == null ? "" : personalDetailsForm.getBean().getAnr().getStringValue()));

    final OptieLayout layout = new OptieLayout();
    layout.getLeft().addComponent(new Fieldset("Persoon"));
    layout.getLeft().addComponent(personalDetailsForm);

    layout.getRight().setWidth("140px");
    layout.getRight().setCaption("Opties");
    layout.getRight().addButton(bsnButton, this);
    layout.getRight().addButton(anrButton, this);

    addComponent(layout);
    addComponent(new Fieldset("Geboorte"));
    addComponent(birthDetailsForm);
    addComponent(sourceDocumentForm);
    sourceDocumentForm.setIsBeforeValidation(birthDetailsForm.getField(F_DATE_OF_BIRTH, GbaDateField.class),
        "Geboortedatum");

    super.initPage();
  }

  @Override
  public void checkPage(Runnable next) {
    personalDetailsForm.commit();
    birthDetailsForm.commit();
    sourceDocumentForm.commit();
    checkFirstName(() -> updatePersonAndSave(next));
  }

  private void updatePersonAndSave(Runnable next) {
    updateDossierPerson(sourceDocumentForm.getValidatedValue());
    next.run();
  }

  private void checkFirstName(Runnable next) {
    DossierPersoon person = getPerson();
    if (!person.isVoornaamBevestigd() && isBlank(personalDetailsForm.getBean().getFirstNames())) {
      getParentWindow().addWindow(new ConfirmDialog("U heeft geen voornaam ingevuld. Is dat correct?") {

        @Override
        public void buttonYes() {
          super.buttonYes();
          person.setVoornaamBevestigd(true);
          next.run();
        }

      });
    } else {
      next.run();
    }
  }

  private void updateDossierPerson(DossSourceDoc sourceDocument) {
    final PersonBean pBean = personalDetailsForm.getBean();
    final PersonBean bBean = birthDetailsForm.getBean();
    final String currentBsn = astr(personalDetailsForm.getField(F_BSN).getValue());
    final String currentAnr = astr(personalDetailsForm.getField(F_ANR).getValue());

    getPerson().setBsn(new BigDecimal(new Bsn(currentBsn).getLongBsn()));
    getPerson().setAnr(new BigDecimal(new Anummer(currentAnr).getLongAnummer()));
    if (pBean.getPrefix() != null) {
      getPerson().setVoorvoegsel(pBean.getPrefix().getStringValue());
    }
    getPerson().setGeslachtsnaam(pBean.getFamilyName());
    getPerson().setVoornaam(pBean.getFirstNames());
    getPerson().setTitel(pBean.getTitle());
    getPerson().setGeslacht(pBean.getGender());

    getPerson().setDatumGeboorte(bBean.getDateOfBirth());
    if (bBean.getCountry() != null) {
      final boolean isGebNl = Landelijk.isNederland(bBean.getCountry());
      getPerson()
          .setGeboorteplaats(isGebNl ? bBean.getMunicipality() : new FieldValue(-1, bBean.getForeignMunicipality()));
      getPerson().setGeboorteland(bBean.getCountry());
    } else {
      getPerson().setGeboorteland(new FieldValue(-1, ""));
      getPerson().setGeboorteplaats(new FieldValue(-1, bBean.getForeignMunicipality()));
    }

    getPerson().setDossSourceDoc(sourceDocument);
    getAddPersonListener().accept(getPerson());
  }

  private void onNewBsn() {
    final String currentBsn = astr(personalDetailsForm.getField(F_BSN).getValue());
    final IdNumbersResponseRestElement response = getServices().getRegistrationService()
        .getIdentificationNumbers(!Bsn.isCorrect(currentBsn), false);

    if (fil(response.getBsn())) {
      final String bsn = response.getBsn();
      personalDetailsForm.getField(F_BSN).setValue(Bsn.format(bsn));
      personalDetailsForm.getBean().setBsn(bsn);
      personalDetailsForm.repaint();
    }
  }

  private void onNewANumber() {
    final String currentAnr = astr(personalDetailsForm.getField(F_ANR).getValue());
    final IdNumbersResponseRestElement response = getServices().getRegistrationService().getIdentificationNumbers(false,
        !Anummer.isCorrect(currentAnr));

    if (fil(response.getAnr())) {
      final String aNum = response.getAnr();
      personalDetailsForm.getField(F_ANR).setValue(Anummer.format(aNum));
      personalDetailsForm.getBean().setAnr(new AnrFieldValue(aNum));
      personalDetailsForm.repaint();
    }
  }
}
