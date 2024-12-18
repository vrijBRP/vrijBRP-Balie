/*
 * Copyright 2024 - 2025 Procura B.V.
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
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module1.PersonBean.F_ANR;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module1.PersonBean.F_BSN;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module1.PersonBean.F_DATE_OF_BIRTH;
import static nl.procura.gba.web.services.bs.registration.SourceDocumentType.CUSTOM;
import static nl.procura.gba.web.services.zaken.identiteit.IdentificatieType.IDENTITEITSKAART;
import static nl.procura.gba.web.services.zaken.identiteit.IdentificatieType.PASPOORT;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.vaadin.ui.Button;

import nl.procura.bsm.rest.v1_0.objecten.gba.probev.idnumbers.IdNumbersResponseRestElement;
import nl.procura.burgerzaken.vrsclient.api.VrsRequest;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.gba.jpa.personen.db.DossSourceDoc;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.beheer.onderhoud.page1.tab1.IdVoorraadWindow;
import nl.procura.gba.web.modules.bs.registration.person.modules.AbstractPersonPage;
import nl.procura.gba.web.modules.zaken.common.SourceDocumentForm;
import nl.procura.gba.web.modules.zaken.reisdocument.page10.SignaleringWindow;
import nl.procura.gba.web.services.applicatie.onderhoud.OnderhoudService.IdVoorraad;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.SourceDocumentType;
import nl.procura.gba.web.services.bs.registration.ValidityDateType;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.theme.twee.ProcuraTheme;
import nl.procura.validation.Anr;
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

    final Button idVoorraad = new Button("Nummer voorraad", e -> onShowIDVoorraad());
    idVoorraad.setWidth("130px");

    final Button rpsInfoButton = new Button("Toon RPS info", e -> onShowRPSInfo());
    rpsInfoButton.setWidth("130px");

    personalDetailsForm = new PersonalDetailsForm();
    birthDetailsForm = new BirthDetailsForm();

    personalDetailsForm.update(getPerson());
    birthDetailsForm.update(getPerson());

    final OptieLayout layout = new OptieLayout();
    layout.getLeft().addComponent(new Fieldset("Persoon"));
    layout.getLeft().addComponent(personalDetailsForm);

    layout.getRight().setWidth("140px");
    layout.getRight().setCaption("Opties");
    layout.getRight().addButton(bsnButton, this);
    layout.getRight().addButton(anrButton, this);
    layout.getRight().addButton(idVoorraad, this);
    layout.getRight().addButton(rpsInfoButton, this);

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
    getPerson().setAnr(new BigDecimal(new Anr(currentAnr).getLongAnummer()));
    getPerson().setVoorvoegsel(Optional.ofNullable(pBean.getPrefix()).map(FieldValue::getStringValue).orElse(null));
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
    final String bsn = astr(personalDetailsForm.getField(F_BSN).getValue());
    if (Bsn.isCorrect(bsn)) {
      getApplication().getParentWindow()
          .addWindow(new ConfirmDialog("Weet u het zeker?",
              "Er is al een BSN uit de voorraad gehaald en toegekend aan deze persoon<hr>" +
                  "Weet u zeker dat u nogmaals een nieuw BSN uit de voorraad wilt halen?",
              "500px", ProcuraTheme.ICOON_24.WARNING) {

            @Override
            public void buttonYes() {
              getNewBsn();
              super.buttonYes();
            }
          });
    } else {
      getNewBsn();
    }
  }

  private void getNewBsn() {
    final IdNumbersResponseRestElement response = getServices()
        .getRegistrationService().getIdentificationNumbers(true, false);
    if (fil(response.getBsn())) {
      final String bsn = response.getBsn();
      personalDetailsForm.getField(F_BSN).setValue(Bsn.format(bsn));
      personalDetailsForm.getBean().setBsn(new BsnFieldValue(bsn));
      personalDetailsForm.repaint();
    }
  }

  private void onNewANumber() {
    final String anr = astr(personalDetailsForm.getField(F_ANR).getValue());
    if (Anr.isCorrect(anr)) {
      getApplication().getParentWindow()
          .addWindow(new ConfirmDialog("Weet u het zeker?",
              "Er is al een A-nummer uit de voorraad gehaald en toegekend aan deze persoon<hr>" +
                  "Weet u zeker dat u nogmaals een nieuw A-nummer uit de voorraad wilt halen?",
              "550px", ProcuraTheme.ICOON_24.WARNING) {

            @Override
            public void buttonYes() {
              getNewAnr();
              super.buttonYes();
            }
          });
    } else {
      getNewAnr();
    }
  }

  private void getNewAnr() {
    final IdNumbersResponseRestElement response = getServices()
        .getRegistrationService().getIdentificationNumbers(false, true);
    if (fil(response.getAnr())) {
      final String aNum = response.getAnr();
      personalDetailsForm.getField(F_ANR).setValue(Anr.format(aNum));
      personalDetailsForm.getBean().setAnr(new AnrFieldValue(aNum));
      personalDetailsForm.repaint();
    }
  }

  private void onShowIDVoorraad() {
    IdVoorraad idVoorraad = getServices().getOnderhoudService().getIdVoorraad();
    getParentWindow().addWindow(new IdVoorraadWindow(idVoorraad));
  }

  private void onShowRPSInfo() {
    String firstNames = astr(personalDetailsForm.getValue(PersonBean.F_FIRSTNAMES, String.class));
    String prefix = astr(personalDetailsForm.getValue(PersonBean.F_PREFIX, FieldValue.class));
    String familyName = astr(personalDetailsForm.getValue(PersonBean.F_FAMILY_NAME, String.class));
    GbaDateFieldValue birthday = birthDetailsForm.getValue(PersonBean.F_DATE_OF_BIRTH, GbaDateFieldValue.class);

    if (isBlank(familyName) || birthday == null || isBlank(birthday.getSystemDate())) {
      throw new ProException(WARNING, "Geslachtsnaam en geboortedatum zijn verplicht");
    }

    VrsRequest request = new VrsRequest()
        .voornamen(firstNames)
        .voorvoegsel(prefix)
        .geslachtsnaam(familyName)
        .geboortedatum(birthday.getSystemDate());

    getServices().getReisdocumentService().getVrsService()
        .checkSignalering(request)
        .ifPresent(sig -> getParentWindow().addWindow(new SignaleringWindow(sig)));
  }
}
