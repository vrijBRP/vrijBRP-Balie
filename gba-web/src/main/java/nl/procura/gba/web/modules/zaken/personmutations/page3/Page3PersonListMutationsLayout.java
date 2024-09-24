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

package nl.procura.gba.web.modules.zaken.personmutations.page3;

import static nl.procura.gba.web.common.misc.Landelijk.isNederland;
import static nl.procura.gba.web.modules.zaken.personmutations.PersonListMutationsCheckBoxType.ADMIN;
import static nl.procura.gba.web.modules.zaken.personmutations.PersonListMutationsCheckBoxType.REQUIRED_GROUPS;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.ADD_HISTORIC;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.ADD_SET;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.CORRECT_CATEGORY;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.CORRECT_CURRENT_ADMIN;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.CORRECT_HISTORIC_ADMIN;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.OVERWRITE_CURRENT;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.OVERWRITE_HISTORIC;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.SUPER_OVERWRITE_CURRENT;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.SUPER_OVERWRITE_HISTORIC;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.UPDATE_SET;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;

import nl.procura.burgerzaken.gba.StringUtils;
import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.burgerzaken.gba.core.validators.AlfVal;
import nl.procura.burgerzaken.gba.core.validators.DatVal;
import nl.procura.burgerzaken.gba.core.validators.NumVal;
import nl.procura.burgerzaken.gba.core.validators.Val;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.common.validators.GbaDatumValidator;
import nl.procura.gba.web.components.containers.AutoriteitReisdocumentContainer;
import nl.procura.gba.web.components.containers.EULandContainer;
import nl.procura.gba.web.components.containers.TabelContainer;
import nl.procura.gba.web.components.fields.DateReference;
import nl.procura.gba.web.components.fields.GbaBsnField;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.IndicatieOnjuistField;
import nl.procura.gba.web.components.fields.UpperCaseField;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.validators.GbaStringValidator;
import nl.procura.gba.web.components.validators.TeletexValidator;
import nl.procura.gba.web.modules.zaken.personmutations.AbstractPersonMutationsTable;
import nl.procura.gba.web.modules.zaken.personmutations.PersonListMutationsCheckBox;
import nl.procura.gba.web.modules.zaken.personmutations.PersonListMutationsCheckBoxType;
import nl.procura.gba.web.modules.zaken.personmutations.PersonListMutationsChecks;
import nl.procura.gba.web.modules.zaken.personmutations.UnknownValueField;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElem;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElems;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.personmutations.PersonListActionType;
import nl.procura.gba.web.services.beheer.personmutations.PersonListMutation;
import nl.procura.standard.ProcuraDate;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.field.AnrField;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.field.PostalcodeField;
import nl.procura.vaadin.component.field.ProComboBox;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.Fieldset;

public class Page3PersonListMutationsLayout extends GbaVerticalLayout {

  private final PersonListMutElems                    elems;
  private final AbstractPersonMutationsTable          table;
  private final Page3PersonListValidationErrorsLayout validationLayout;

  public Page3PersonListMutationsLayout(PersonListMutElems elems, PersonListMutation mutation) {
    this.elems = elems;
    validationLayout = new Page3PersonListValidationErrorsLayout(elems, mutation.getActionType());
    elems.forEach(mutElem -> elems.setField(mutElem, getDefaultField(mutElem)));
    addCountryMunicipalityListeners(elems);
    addDateRestrictions(elems);
    elems.forEach(this::setDefaultValues);
    elems.forEach(this::setClearFieldValidator);

    addStyleName("v-form-error");
    this.table = new Page3PersonMutationsTable(elems);

    addComponent(new Fieldset("Overzicht elementen"));
    getFilter(mutation.getActionType()).ifPresent(this::addComponent);

    addExpandComponent(getTable());
    addComponent(validationLayout);
    setTabIndex();
  }

  /**
   * Resets tabIndex to use tab key
   */
  private void setTabIndex() {
    int tabIndex = 0;
    for (PersonListMutElem elem : elems) {
      if (!elem.isReadonly()) {
        elem.setTabIndex(++tabIndex);
      }
    }
  }

  private Optional<CheckBox> getFilter(PersonListActionType actionType) {
    Consumer<PersonListMutationsCheckBoxType> changeListener = table::setFilter;

    if (actionType.is(CORRECT_CATEGORY)) {
      return Optional.of(new PersonListMutationsCheckBox(REQUIRED_GROUPS, changeListener));

    } else if (actionType.is(CORRECT_CURRENT_ADMIN, CORRECT_HISTORIC_ADMIN)) {
      return Optional.of(new PersonListMutationsCheckBox(ADMIN, changeListener));
    }
    return Optional.empty();
  }

  public AbstractPersonMutationsTable getTable() {
    return table;
  }

  public PersonListMutElems getNewRecords() {
    elems.validateAll();
    validationLayout.getErrors().stream()
        .findFirst().ifPresent(error -> {
          throw new ProException(WARNING, error.getMsg());
        });
    return elems;
  }

  /**
   * Als de gemeente van de inschrijving de huidige gemeente is dan de straten tonen
   */
  private void checkMunicipality() {
    elems.getElem(GBAElem.GEM_INSCHR)
        .ifPresent(gemeente -> {
          checkStreet(gemeente);
          checkOpenbareRuimte(gemeente);
        });
  }

  private void checkStreet(PersonListMutElem gemeente) {
    elems.getElem(GBAElem.STRAATNAAM).ifPresent(straat -> {
      FieldValue gem = (FieldValue) gemeente.getField().getValue();
      int plaatsCode = aval((gem != null) ? gem.getValue() : gemeente.getElem().getValue().getVal());
      boolean isGemeente = Services.getInstance().getGebruiker().isGemeente(plaatsCode);
      AbstractField field = isGemeente ? getDefaultField(straat) : new UnknownValueField();
      elems.setField(straat, setFieldStyle(field));
      setDefaultValues(straat);
      setEditableFieldProperties(field, straat);
      if (getTable() != null) {
        getTable().init();
      }
    });
  }

  private void checkOpenbareRuimte(PersonListMutElem gemeente) {
    elems.getElem(GBAElem.OPENB_RUIMTE).ifPresent(obr -> {
      FieldValue gem = (FieldValue) gemeente.getField().getValue();
      int plaatsCode = aval((gem != null) ? gem.getValue() : gemeente.getElem().getValue().getVal());
      boolean isGemeente = Services.getInstance().getGebruiker().isGemeente(plaatsCode);
      AbstractField field = isGemeente ? getDefaultField(obr) : new GbaTextField();
      elems.setField(obr, setFieldStyle(field));
      setDefaultValues(obr);
      setEditableFieldProperties(field, obr);
      if (getTable() != null) {
        getTable().init();
      }
    });
  }

  private AbstractField getDefaultField(PersonListMutElem mutElem) {
    int catCode = mutElem.getElem().getCatCode();
    int elementCode = mutElem.getElem().getElemCode();
    GBAGroupElements.GBAGroupElem pleE = GBAGroupElements.getByCat(catCode, elementCode);

    Val val = pleE.getElem().getVal();
    GBATable tabel = pleE.getElem().getTable();
    AbstractField field = new GbaTextField();

    if (pleE.getElem().is(GBAElem.BSN)) {
      field = new GbaBsnField();

    } else if (pleE.getElem().is(GBAElem.ANR)) {
      field = new AnrField();

    } else if (pleE.getElem().is(GBAElem.POSTCODE)) {
      field = new PostalcodeField();

    } else if (pleE.getElem().is(GBAElem.GESLACHTSNAAM)) {
      field = new UnknownValueField();

    } else if (pleE.getElem().is(GBAElem.AAND_GEG_IN_ONDERZ)) {
      field = new IndicatieOnjuistField(mutElem.getCat());

    } else if (pleE.getElem().is(GBAElem.AKTENR, GBAElem.NR_NL_REISDOC)) {
      field = new UpperCaseField();

    } else if (pleE.getElem().is(GBAElem.AUTORIT_VAN_AFGIFTE_NL_REISDOC)) {
      ProComboBox select = new ProComboBox();
      select.setContainerDataSource(new AutoriteitReisdocumentContainer());
      select.setItemCaptionPropertyId(TabelContainer.OMSCHRIJVING);
      select.setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_CONTAINS);
      mutElem.setDefaultValue(() -> new FieldValue(mutElem.getCurrentValue().getVal()));
      field = select;

    } else if (tabel != GBATable.ONBEKEND) {
      TabelContainer container = new FilteredTabelContainer(tabel, false, true);
      ProComboBox select = new ProComboBox();
      select.setContainerDataSource(container);
      select.setItemCaptionPropertyId(TabelContainer.OMSCHRIJVING);
      select.setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_CONTAINS);
      field = select;

      // Alleen europese landen
      if (mutElem.getElemType() == GBAElem.LAND_EU_LIDSTAAT_VAN_HERKOMST) {
        select.setContainerDataSource(new EULandContainer());
      }

      // Bij geboorteland of gemeente van inschrijving
      // listeners toevoegen.
      if (mutElem.getElemType() == GBAElem.GEM_INSCHR) {
        field.setImmediate(true);
        field.addListener(new ComboChangeListener());
      }
    } else if (val instanceof NumVal) {
      field = new NumberField();

    } else if (val instanceof DatVal) {
      GbaDatumValidator datumValidator = new GbaDatumValidator();
      GbaStringValidator validator = new GbaStringValidator("Incorrecte datum", datumValidator::isValid);
      DatumVeld datumVeld = new DatumVeld(null, validator);
      datumVeld.setUitzonderingenToestaan(true);
      field = datumVeld;
    }

    if (val instanceof AlfVal && field instanceof AbstractTextField) {
      TeletexValidator.add(field);
      int min = ((AlfVal) val).getMin();
      int max = ((AlfVal) val).getMax();
      if (max > 0 && min == max) {
        field.addValidator(new StringLengthValidator("Verplichte lengte van " + min + " tekens", min, max, true));
        ((AbstractTextField) field).setMaxLength(max);
      } else if (max > 0) {
        ((AbstractTextField) field).setMaxLength(max);
      }
    }

    // Null waarde niet tonen
    if (field instanceof AbstractTextField) {
      ((AbstractTextField) field).setNullRepresentation("");
    }

    field.setDescription(pleE.getElem().getDescr());

    // Indicatie onjuist altijd leegmaken
    if (mutElem.getAction().is(ADD_HISTORIC)) {
      if (GBAElem.IND_ONJUIST.is(pleE.getElem())) {
        mutElem.setDefaultValue(FieldValue::new);
      }
    }

    // Ingangsdatum geldigheid altijd leegmaken
    if (mutElem.getAction().is(UPDATE_SET, ADD_SET, ADD_HISTORIC)) {
      if (GBAElem.INGANGSDAT_GELDIG.is(pleE.getElem())) {
        mutElem.setDefaultValue(() -> new GbaDateFieldValue(""));
      }
    }

    if (mutElem.getAction().is(CORRECT_CATEGORY)) {
      if (GBAElem.REDEN_EINDE_NATIO.is(pleE.getElem())) {
        TabelContainer tabelContainer = (TabelContainer) ((Select) field).getContainerDataSource();
        mutElem.setDefaultValue(() -> tabelContainer.get(Landelijk.REDEN_ONTERECHT_OPGENOMEN_CAT));

      } else if (GBAElem.GEMEENTE_DOC.is(pleE.getElem())) {
        TabelContainer tabelContainer = (TabelContainer) ((Select) field).getContainerDataSource();
        String gemeenteCode = Services.getInstance().getGebruiker().getGemeenteCode();
        mutElem.setDefaultValue(() -> tabelContainer.get(gemeenteCode));

      } else if (GBAElem.DATUM_DOC.is(pleE.getElem())) {
        mutElem.setDefaultValue(() -> new DateFieldValue(new ProcuraDate().getSystemDate()));

      } else if (GBAElem.BESCHRIJVING_DOC.is(pleE.getElem())) {
        mutElem.setDefaultValue(() -> new FieldValue("ten onrechte opgenomen categorie"));

      } else if (GBAElem.INGANGSDAT_GELDIG.is(pleE.getElem())) {
        mutElem.setDefaultValue(() -> new GbaDateFieldValue(getOldestValidityDate(mutElem)));

      } else {
        mutElem.setDefaultValue(FieldValue::new);
      }
    }

    if (!mutElem.getAction().is(
        OVERWRITE_CURRENT,
        OVERWRITE_HISTORIC,
        SUPER_OVERWRITE_CURRENT,
        SUPER_OVERWRITE_HISTORIC)) {
      if (GBAElem.DATUM_VAN_OPNEMING.is(pleE.getElem())) {
        mutElem.setDefaultValue(() -> new DateFieldValue(new ProcuraDate().getSystemDate()));
      }
    }

    if (mutElem.isReadonly()) {
      field.setReadOnly(true);

    } else {
      setEditableFieldProperties(field, mutElem);
    }

    return setFieldStyle(field);
  }

  private <T extends AbstractField> T setEditableFieldProperties(T field, PersonListMutElem mutElem) {
    field.setImmediate(true);
    field.addValidator(PersonListMutationsChecks.getValidator(mutElem, elems));
    field.addListener((ValueChangeListener) valueChangeEvent -> {
      validationLayout.clearMessages();
      elems.forEach(elem -> {
        try {
          checkRequired(elem, elem.getField());
          elem.validate();
        } catch (Validator.InvalidValueException e) {
          if (StringUtils.isNotBlank(e.getMessage())) {
            validationLayout.addError(elem, e.getMessage());
          }
        }
      });
      validationLayout.setErrors();
    });
    return field;
  }

  private void checkRequired(PersonListMutElem mutElem, AbstractField field) {
    Optional<String> requiredError = PersonListMutationsChecks.getRequiredError(mutElem, elems);
    field.setRequired(requiredError.isPresent());
    requiredError.ifPresent(field::setRequiredError);
  }

  /**
   * Stijl van velden aanpassen
   */
  private AbstractField setFieldStyle(AbstractField field) {
    if (field instanceof TextField) {
      field.setHeight("23px");
    }
    field.setWidth("100%");
    return field;
  }

  private String getOldestValidityDate(PersonListMutElem elem) {
    return elem.getSet().getRecs().stream()
        .filter(BasePLRec::isCorrect)
        .reduce((first, second) -> second)
        .map(rec -> rec.getElemVal(GBAElem.INGANGSDAT_GELDIG).getVal())
        .orElse("");
  }

  private void setClearFieldValidator(PersonListMutElem mutElem) {
    mutElem.getField().addListener((ValueChangeListener) event -> {
      boolean isBlank = StringUtils.isBlank(astr(event.getProperty().getValue()));
      PersonListMutationsChecks.checkCleared(mutElem, isBlank, elems);
    });
  }

  private void setDefaultValues(PersonListMutElem mutationRecord) {
    BasePLValue val = mutationRecord.getElem().getValue();
    mutationRecord.getField()
        .setValue(mutationRecord.getDefaultValue()
            .map(Supplier::get)
            .orElse(new FieldValue(val.getVal(), val.getDescr())));
  }

  public void updatePl(BasePLExt pl) {
    updateElement(pl, GBAElem.ANR);
    updateElement(pl, GBAElem.BSN);
    updateElement(pl, GBAElem.VOORNAMEN);
    updateElement(pl, GBAElem.TITEL_PREDIKAAT);
    updateElement(pl, GBAElem.VOORV_GESLACHTSNAAM);
    updateElement(pl, GBAElem.GESLACHTSNAAM);
    updateElement(pl, GBAElem.GEBOORTEDATUM);
    updateElement(pl, GBAElem.GEBOORTELAND);
    updateElement(pl, GBAElem.GEBOORTEPLAATS);
    updateElement(pl, GBAElem.GESLACHTSAAND);
  }

  private void updateElement(BasePLExt pl, GBAElem gbaElem) {
    elems.getElem(gbaElem)
        .ifPresent(element -> {
          BasePLValue val = pl.getCat(GBACat.PERSOON).getCurrentRec().getElem(gbaElem).getValue();
          element.getField().setValue(new FieldValue(val.getVal(), val.getDescr()));
        });
  }

  public class ComboChangeListener implements ValueChangeListener {

    private ComboChangeListener() {
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
      checkMunicipality();
    }

  }

  private static void addDateRestrictions(PersonListMutElems elements) {
    addDateRestriction(elements, GBAElem.DATUM_DOC, GBAElem.GEMEENTE_DOC);
    addDateRestriction(elements, GBAElem.GEBOORTEDATUM, GBAElem.GEBOORTEPLAATS);
    addDateRestriction(elements, GBAElem.GEBOORTEDATUM, GBAElem.GEBOORTELAND);
    addDateRestriction(elements, GBAElem.DATUM_VERBINTENIS, GBAElem.PLAATS_VERBINTENIS);
    addDateRestriction(elements, GBAElem.DATUM_VERBINTENIS, GBAElem.LAND_VERBINTENIS);
    addDateRestriction(elements, GBAElem.DATUM_VERBINTENIS, GBAElem.REGIST_GEM_AKTE);
    addDateRestriction(elements, GBAElem.DATUM_ONTBINDING, GBAElem.PLAATS_ONTBINDING);
    addDateRestriction(elements, GBAElem.DATUM_ONTBINDING, GBAElem.LAND_ONTBINDING);
    addDateRestriction(elements, GBAElem.DATUM_OVERL, GBAElem.PLAATS_OVERL);
    addDateRestriction(elements, GBAElem.DATUM_OVERL, GBAElem.LAND_OVERL);
  }

  private static void addDateRestriction(PersonListMutElems elements, GBAElem dateElement, GBAElem tableElement) {
    Optional<AbstractField> dateField = elements.getElem(dateElement)
        .map(PersonListMutElem::getField);
    Optional<AbstractSelect> select = elements.getElem(tableElement)
        .filter(elem -> elem.getField() instanceof AbstractSelect)
        .map(elem -> (AbstractSelect) elem.getField());
    if (dateField.isPresent() && select.isPresent()) {
      DateReference.setFieldWithValidator(select.get(), dateField.get());
    }
  }

  private void addCountryMunicipalityListeners(PersonListMutElems elements) {
    addCountryMunicipalityListener(elements, GBAElem.GEBOORTELAND, GBAElem.GEBOORTEPLAATS);
    addCountryMunicipalityListener(elements, GBAElem.LAND_VERBINTENIS, GBAElem.PLAATS_VERBINTENIS);
    addCountryMunicipalityListener(elements, GBAElem.LAND_ONTBINDING, GBAElem.PLAATS_ONTBINDING);
    addCountryMunicipalityListener(elements, GBAElem.LAND_OVERL, GBAElem.PLAATS_OVERL);
    addCountryMunicipalityListener(elements, GBAElem.LAND_EU_LIDSTAAT_VAN_HERKOMST,
        GBAElem.PLAATS_EU_LIDSTAAT_VAN_HERKOMST);
  }

  private void addCountryMunicipalityListener(PersonListMutElems elements, GBAElem country, GBAElem municipality) {
    Optional<AbstractField> countryField = elements.getElem(country)
        .map(PersonListMutElem::getField);
    Optional<PersonListMutElem> municipalityElem = elements.getElem(municipality);
    if (countryField.isPresent() && municipalityElem.isPresent()) {
      countryField.get().setImmediate(true);
      countryField.get().addListener((ValueChangeListener) e -> this.changeMunicipality(e, municipalityElem.get()));
    }
  }

  private void changeMunicipality(ValueChangeEvent e, PersonListMutElem municipality) {
    boolean isNederland = isNederland((FieldValue) e.getProperty().getValue());
    Class<? extends AbstractField> fieldClass = isNederland ? AbstractSelect.class : GbaTextField.class;
    Supplier<AbstractField> fieldSupplier = isNederland
        ? () -> getMunicipalityComboBox(municipality)
        : () -> getMunicipalityTextField(municipality);
    setMunicipalityField(fieldClass, fieldSupplier, municipality);
  }

  private AbstractField getMunicipalityComboBox(PersonListMutElem mutElem) {
    AbstractSelect field = (AbstractSelect) getDefaultField(mutElem);
    elems.getElem(mutElem.getElemType())
        .map(PersonListMutElem::getField)
        .ifPresent(dateField -> DateReference.setFieldWithValidator(field, dateField));
    return field;
  }

  private AbstractField getMunicipalityTextField(PersonListMutElem mutElem) {
    GbaTextField field = setEditableFieldProperties(new GbaTextField(), mutElem);
    field.setNullRepresentation("");
    return setFieldStyle(field);
  }

  private void setMunicipalityField(Class<? extends AbstractField> fieldClass, Supplier<AbstractField> fieldSupplier,
      PersonListMutElem municipality) {
    if (fieldClass.isAssignableFrom(municipality.getField().getClass())) {
      return;
    }
    elems.setField(municipality, fieldSupplier.get());
    if (municipality.isReadonly()) {
      municipality.getField().setReadOnly(true);
    }
    setDefaultValues(municipality);
    if (getTable() != null) {
      getTable().init();
    }
  }
}
