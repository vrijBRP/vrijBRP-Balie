/*
 * Copyright 2023 - 2024 Procura B.V.
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

import static nl.procura.burgerzaken.vrsclient.api.VrsAanleidingType.IDENTITEITSONDERZOEK;
import static nl.procura.gba.web.components.containers.DutchTravelDocumentAuthorityContainer.getAuthority;
import static nl.procura.gba.web.components.containers.DutchTravelDocumentAuthorityContainer.getCountry;
import static nl.procura.gba.web.components.containers.DutchTravelDocumentAuthorityContainer.getMayorMunicipality;
import static nl.procura.gba.web.components.containers.DutchTravelDocumentAuthorityContainer.isCountryCode;
import static nl.procura.gba.web.components.containers.DutchTravelDocumentAuthorityContainer.isForeignAndInternalAffairs;
import static nl.procura.gba.web.components.containers.DutchTravelDocumentAuthorityContainer.isMayorCode;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module4.DutchTravelDocumentBean.F_AUTHORITY;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module4.DutchTravelDocumentBean.F_AUTHORITY_COUNTRY;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module4.DutchTravelDocumentBean.F_AUTHORITY_MUNICIPALITY;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module4.DutchTravelDocumentBean.F_DOSSIER_DESCRIPTION;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module4.DutchTravelDocumentBean.F_DOSSIER_MUNICIPALITY;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module4.DutchTravelDocumentBean.F_DOSSIER_START_DATE;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module4.DutchTravelDocumentBean.F_END_DATE;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module4.DutchTravelDocumentBean.F_ISSUE_DATE;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module4.DutchTravelDocumentBean.F_NUMBER;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module4.DutchTravelDocumentBean.F_TYPE;
import static nl.procura.standard.Globalfunctions.toBigDecimal;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

import nl.procura.burgerzaken.vrsclient.api.VrsRequest;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponseReisdocument;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personen.db.DossTravelDoc;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.components.containers.DutchTravelDocumentAuthorityContainer;
import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.containers.PlaatsContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.validators.DatumVolgordeValidator;
import nl.procura.gba.web.modules.bs.registration.AbstractRegistrationForm;
import nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsDocumentenWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentUtils;
import nl.procura.standard.Globalfunctions;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.field.fieldvalues.TabelFieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.vaadin.component.window.Message;

class DutchTravelDocumentForm extends AbstractRegistrationForm<DutchTravelDocumentBean> {

  private static final String LAISSER_PASSER       = "LP";
  private static final String DIPLOMATIEK_PASPOORT = "PD";
  private static final String DIENSTPASPOORT       = "PZ";
  private static final String NOODDOCUMENT1        = "NB";
  private static final String NOODPASPOORT1        = "NN";
  private static final String NOODPASPOORT2        = "NP";
  private static final String NOODDOCUMENT2        = "NV";
  private static final String GEMEENTE_SGRAVENHAGE = "0518";

  private static final Consumer<DossTravelDoc> NO_OPERATION = td -> {};
  private static final TabelFieldValue         NO_VALUE     = null;

  private final DossTravelDoc           travelDoc;
  private final Consumer<DossTravelDoc> saveConsumer;
  private final PlaatsContainer         municipalities = new PlaatsContainer();
  private final LandContainer           countries      = new LandContainer();
  private final DossierPersoon          person;
  private Label                         issueDateLabel = new Label("");

  DutchTravelDocumentForm(DossierPersoon person, DossTravelDoc travelDoc) {
    this(person, travelDoc, NO_OPERATION);
  }

  DutchTravelDocumentForm(DossierPersoon person, DossTravelDoc travelDoc, Consumer<DossTravelDoc> saveConsumer) {
    this.person = person;
    setCaption("Reisdocumentgegevens");
    setReadonlyAsText(false);
    setOrder(F_TYPE, F_NUMBER, F_ISSUE_DATE, F_END_DATE, F_AUTHORITY, F_AUTHORITY_MUNICIPALITY,
        F_AUTHORITY_COUNTRY, F_DOSSIER_MUNICIPALITY, F_DOSSIER_START_DATE, F_DOSSIER_DESCRIPTION);

    this.travelDoc = travelDoc;
    this.saveConsumer = saveConsumer;

    updateBean(new DutchTravelDocumentBean());
  }

  private void updateBean(DutchTravelDocumentBean bean) {
    bean.setType(new FieldValue(travelDoc.getNedReisdoc()));
    bean.setNumber(travelDoc.getDocNr());
    bean.setIssueDate(new DateTime(travelDoc.getDVerkrijging()).getDate());
    bean.setEndDate(new DateTime(travelDoc.getDEnd()).getDate());
    bean.setDossierDescription(travelDoc.getDossOms());
    setBean(bean);
    updateIssueDateLabel();
  }

  @Override
  public void afterSetBean() {
    final GbaNativeSelect type = getField(F_TYPE, GbaNativeSelect.class);
    final ProDateField issueDate = getField(F_ISSUE_DATE, ProDateField.class);
    final ProDateField endDate = getField(F_END_DATE, ProDateField.class);
    final GbaNativeSelect authority = getField(F_AUTHORITY, GbaNativeSelect.class);
    final GbaComboBox authorityMunicipality = getField(F_AUTHORITY_MUNICIPALITY, GbaComboBox.class);
    final GbaComboBox authorityCountry = getField(F_AUTHORITY_COUNTRY, GbaComboBox.class);

    // Order is important! First set data sources, then UI state, and then attach listeners
    final DutchTravelDocumentAuthorityContainer authorities = new DutchTravelDocumentAuthorityContainer();
    authority.setContainerDataSource(authorities);
    authorityMunicipality.setContainerDataSource(municipalities);
    authorityCountry.setContainerDataSource(countries);

    final TabelFieldValue code = authorities.get(travelDoc.getAutVerstrek());
    final TabelFieldValue municipality = municipalities.get(travelDoc.getAutVerstrekGem());
    final TabelFieldValue country = countries.get(travelDoc.getAutVerstrekLand());
    setAuthorityState(code, municipality, country, travelDoc.getDossOms());

    authority.addListener((ValueChangeListener) e -> onAuthorityChange((FieldValue) e.getProperty().getValue()));
    authorityMunicipality.addListener((ValueChangeListener) e -> {
      onAuthorityMunicipalityChange((TabelFieldValue) e.getProperty().getValue());
    });

    type.addListener((ValueChangeListener) event -> {
      updateEndDate();
    });
    issueDate.addListener((ValueChangeListener) event -> {
      updateEndDate();
    });

    type.addValidator(new TravelDocumentValidator());
    endDate.addValidator(new DatumVolgordeValidator(
        "Datum uitgifte",
        () -> (Date) issueDate.getValue(),
        "Datum einde",
        () -> (Date) endDate.getValue()));

    super.afterSetBean();
  }

  @Override
  public void commit(Runnable next) throws SourceException, Validator.InvalidValueException {
    super.commit();
    setTravelDocFromForm();
    saveConsumer.accept(travelDoc);
    next.run();
  }

  @Override
  public void setColumn(TableLayout.Column column, com.vaadin.ui.Field field, Property property) {
    if (property.is(F_DOSSIER_MUNICIPALITY)) {
      Fieldset fieldset = new Fieldset("Dossiergegevens");
      fieldset.setHeight("25px");
      getLayout().addBreak(fieldset);
    }
    super.afterSetColumn(column, field, property);
  }

  @Override
  public void afterSetColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_ISSUE_DATE)) {
      issueDateLabel = new Label("");
      issueDateLabel.setContentMode(Label.CONTENT_XHTML);
      column.addComponent(issueDateLabel);
    }

    if (property.is(F_NUMBER)) {
      if (Services.getInstance().getReisdocumentService().getVrsService().isBasisregisterEnabled()) {
        column.addComponent(new Button("Basisregister", event -> zoekBasisregister()));
      }
    }
    super.afterSetColumn(column, field, property);
  }

  private void zoekBasisregister() {
    String idNumber = getValue(F_NUMBER, String.class);
    if (isBlank(idNumber)) {
      new Message(getWindow(), "Nummer is verplicht", Message.TYPE_WARNING_MESSAGE);

    } else {
      Services services = getApplication().getServices();
      Optional<ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse> document = services
          .getReisdocumentService()
          .getVrsService()
          .getReisdocument(new VrsRequest()
              .aanleiding(IDENTITEITSONDERZOEK)
              .documentnummer(idNumber));

      document.ifPresent(response -> {
        getApplication()
            .getParentWindow()
            .addWindow(new VrsDocumentenWindow(response,
                (documentResponse, window) -> {
                  ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponseReisdocument reisdoc = documentResponse
                      .getReisdocument();
                  setValue(F_TYPE, new FieldValue(reisdoc.getDocumentsoort().getCode()));
                  setValue(F_ISSUE_DATE, DateTime.of(reisdoc.getDocumentAfgifte()).getDate());
                  setValue(F_END_DATE, DateTime.of(reisdoc.getDocumentGeldigTot()).getDate());

                  String aut = documentResponse.getAutoriteitVerstrekking().getAutoriteitVerstrekkingCode();
                  FieldValue authority = getAuthority(aut);
                  FieldValue municipality = getMayorMunicipality(aut);
                  FieldValue country = getCountry(aut);

                  setAuthorityState(authority, municipality, country, "");
                  repaint();
                  updateIssueDateLabel();
                  window.closeWindow();
                }));
      });
    }
  }

  private void setTravelDocFromForm() {
    final DutchTravelDocumentBean bean = getBean();
    travelDoc.setNedReisdoc(bean.getType().getStringValue());
    travelDoc.setDocNr(bean.getNumber());
    travelDoc.setDVerkrijging(toBigDecimal(new DateTime(bean.getIssueDate()).getLongDate()));
    travelDoc.setAutVerstrek(bean.getAuthority().getStringValue());
    FieldValue authMunValue = (FieldValue) getField(F_AUTHORITY_MUNICIPALITY).getValue();
    FieldValue countryValue = (FieldValue) getField(F_AUTHORITY_COUNTRY).getValue();
    travelDoc.setAutVerstrekGem(valueOrDefaultCode(authMunValue));
    travelDoc.setAutVerstrekLand(valueOrDefaultCode(countryValue));
    travelDoc.setDEnd(toBigDecimal(new DateTime(bean.getEndDate()).getLongDate()));
    FieldValue dossMunValue = (FieldValue) getField(F_DOSSIER_MUNICIPALITY).getValue();
    travelDoc.setDossGem(valueOrDefaultCode(dossMunValue));
    travelDoc.setDossDIn(BigDecimal.valueOf(0L));
    travelDoc.setDossOms(bean.getDossierDescription());
  }

  private String valueOrDefaultCode(FieldValue fieldValue) {
    return Optional.ofNullable(fieldValue)
        .map(FieldValue::getStringValue)
        .orElse("-1");
  }

  private void onAuthorityChange(FieldValue value) {
    setAuthorityState(value, NO_VALUE, NO_VALUE, "");
    repaint();
    updateIssueDateLabel();
  }

  private void setAuthorityState(FieldValue code, FieldValue municipality, FieldValue country, String dossierOms) {
    Field autority = getField(F_AUTHORITY);
    Field autorityMun = getField(F_AUTHORITY_MUNICIPALITY);
    Field autorityCountry = getField(F_AUTHORITY_COUNTRY);
    Field dossierDescr = getField(F_DOSSIER_DESCRIPTION);
    Field dossierMun = getField(F_DOSSIER_MUNICIPALITY);
    autority.setValue(code);

    FieldValue forceMunicipality = getMayorMunicipality(code.getStringValue());
    if (forceMunicipality != null) {
      municipality = forceMunicipality;
    }

    if (isForeignAndInternalAffairs(code.getStringValue())) {
      forceMunicipality = GbaTables.PLAATS.get(GEMEENTE_SGRAVENHAGE);
      municipality = forceMunicipality;
      setField(autorityMun, true, forceMunicipality != null, municipality);
      setField(autorityCountry, false, false, null);
      setField(dossierMun, true, true, null);
      setField(dossierDescr, true, false, dossierOms);
      autorityMun.focus();

    } else if (isMayorCode(code.getStringValue())) {
      setField(autorityMun, true, forceMunicipality != null, municipality);
      setField(autorityCountry, false, false, null);
      setField(dossierMun, true, true, municipality);
      setField(dossierDescr, true, true, null);
      getBean().setDossierDescription("");
      autorityMun.focus();

    } else if (isCountryCode(code.getStringValue())) {
      setField(autorityMun, false, false, null);
      setField(autorityCountry, true, false, country);
      setField(dossierMun, true, true, null);
      setField(dossierDescr, true, false, dossierOms);
      autorityCountry.focus();

    } else {
      setField(autorityMun, false, false, null);
      setField(autorityCountry, false, false, null);
      setField(dossierMun, true, true, null);
      setField(dossierDescr, true, false, dossierOms);
      dossierDescr.focus();
    }
  }

  private void setField(Field field, boolean visible, boolean readOnly, Object value) {
    field.setVisible(visible);
    field.setReadOnly(readOnly);
    field.setValue(value);
  }

  private void onAuthorityMunicipalityChange(TabelFieldValue municipality) {
    getField(F_DOSSIER_MUNICIPALITY).setValue(municipality);
  }

  private String getIssueDateValue() {
    ProDateField issueDateField = getField(F_ISSUE_DATE, ProDateField.class);
    Date dateFieldValue = (Date) issueDateField.getValue();
    if (dateFieldValue != null) {
      return new ProcuraDate(dateFieldValue).getSystemDate();
    }
    return "";
  }

  private int getLeeftijd() {
    return MiscUtils.getLeeftijd(person.getDatumGeboorte().getSystemDate(), getIssueDateValue());
  }

  private void updateIssueDateLabel() {
    issueDateLabel.setValue("");
    if (StringUtils.isNotBlank(getIssueDateValue())) {
      int leeftijd = getLeeftijd();
      if (leeftijd >= 0) {
        issueDateLabel.setValue(" (op deze datum was de persoon " + leeftijd + " jaar oud)");
      }
      if (leeftijd < 0) {
        issueDateLabel.setValue(MiscUtils.setClass(false, " (op deze datum was de persoon nog niet geboren)"));
      }
    }
  }

  private void updateEndDate() {
    ProDateField issueDateField = getField(F_ISSUE_DATE, ProDateField.class);
    ProDateField endDateField = getField(F_END_DATE, ProDateField.class);
    FieldValue typeValue = (FieldValue) getField(F_TYPE).getValue();

    if (typeValue != null) {
      ReisdocumentType type = ReisdocumentType.get(typeValue.getStringValue());
      Date current = (Date) issueDateField.getValue();
      int leeftijd = getLeeftijd();

      if (current != null) {
        int years = ReisdocumentUtils.getGeldigJaar(type, Globalfunctions.aval(leeftijd));
        endDateField.setValue(new ProcuraDate(current).addYears(years).getDateFormat());
      } else {
        endDateField.setValue(null);
      }

      updateIssueDateLabel();
    }
  }

  public class TravelDocumentValidator extends AbstractValidator {

    public TravelDocumentValidator() {
      super("Dit document wordt niet op de persoonslijst opgenomen.");
    }

    @Override
    public boolean isValid(Object value) {
      String travelDocValue = ((FieldValue) value).getStringValue();
      return !Arrays.asList(
          LAISSER_PASSER,
          DIPLOMATIEK_PASPOORT,
          NOODDOCUMENT1,
          NOODDOCUMENT2,
          NOODPASPOORT1,
          NOODPASPOORT2,
          DIENSTPASPOORT).contains(travelDocValue);
    }
  }
}
