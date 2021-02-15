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

package nl.procura.gba.web.modules.zaken.personmutations.page3;

import static nl.procura.gba.web.common.misc.Landelijk.isNederland;
import static nl.procura.gba.web.modules.zaken.personmutations.PersonListMutationsCheckBoxType.ADMIN;
import static nl.procura.gba.web.modules.zaken.personmutations.PersonListMutationsCheckBoxType.REQUIRED_GROUPS;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.*;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.ui.*;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.burgerzaken.gba.core.validators.AlfVal;
import nl.procura.burgerzaken.gba.core.validators.DatVal;
import nl.procura.burgerzaken.gba.core.validators.NumVal;
import nl.procura.burgerzaken.gba.core.validators.Val;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.common.validators.GbaDatumValidator;
import nl.procura.gba.web.components.containers.TabelContainer;
import nl.procura.gba.web.components.fields.DateReference;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.IndicatieOnjuistField;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.modules.zaken.personmutations.AbstractPersonMutationsTable;
import nl.procura.gba.web.modules.zaken.personmutations.PersonListMutationsCheckBox;
import nl.procura.gba.web.modules.zaken.personmutations.PersonListMutationsCheckBoxType;
import nl.procura.gba.web.modules.zaken.personmutations.PersonListMutationsChecks;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElem;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElems;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.personmutations.PersonListActionType;
import nl.procura.gba.web.services.beheer.personmutations.PersonListMutation;
import nl.procura.standard.ProcuraDate;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.*;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.Fieldset;

public class Page3PersonListMutationsLayout extends GbaVerticalLayout {

  private final PersonListMutElems                    list;
  private final AbstractPersonMutationsTable          table;
  private final Page3PersonListValidationErrorsLayout validationLayout;

  public Page3PersonListMutationsLayout(PersonListMutElems list, PersonListMutation mutation) {
    this.list = list;
    validationLayout = new Page3PersonListValidationErrorsLayout(list, mutation.getActionType());
    list.forEach(mutElem -> mutElem.setField(getInitComponent(mutElem)));
    addCountryMunicipalityListeners(list);
    addDateRestrictions(list);
    list.forEach(this::setDefaultValues);

    addStyleName("v-form-error");
    this.table = new Page3PersonMutationsTable(list);

    addComponent(new Fieldset("Overzicht elementen"));
    getFilter(mutation.getActionType()).ifPresent(this::addComponent);
    addExpandComponent(getTable());

    addComponent(validationLayout);
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
    list.validateAll();
    validationLayout.getErrors().stream()
        .findFirst().ifPresent(error -> {
          throw new ProException(WARNING, error.getMsg());
        });
    return list;
  }

  /**
   * Als de gemeente van de inschrijving de huidige gemeente is dan de straten tonen
   */
  private void checkMunicipality() {
    list.getElem(GBAElem.GEM_INSCHR)
        .ifPresent(gemeente -> {
          checkStreet(gemeente);
          checkOpenbareRuimte(gemeente);
        });
  }

  private void checkStreet(PersonListMutElem gemeente) {
    list.getElem(GBAElem.STRAATNAAM).ifPresent(straat -> {
      FieldValue gem = (FieldValue) gemeente.getField().getValue();
      int plaatsCode = aval((gem != null) ? gem.getValue() : gemeente.getElem().getValue().getVal());
      boolean isGemeente = Services.getInstance().getGebruiker().isGemeente(plaatsCode);
      AbstractField field = isGemeente ? getDefaultComponent(straat) : new GbaTextField();
      straat.setField(setFieldStyle(field));
      setDefaultValues(straat);
      if (getTable() != null) {
        getTable().init();
      }
    });
  }

  private void checkOpenbareRuimte(PersonListMutElem gemeente) {
    list.getElem(GBAElem.OPENB_RUIMTE).ifPresent(obr -> {
      FieldValue gem = (FieldValue) gemeente.getField().getValue();
      int plaatsCode = aval((gem != null) ? gem.getValue() : gemeente.getElem().getValue().getVal());
      boolean isGemeente = Services.getInstance().getGebruiker().isGemeente(plaatsCode);
      AbstractField field = isGemeente ? getDefaultComponent(obr) : new GbaTextField();
      obr.setField(setFieldStyle(field));
      setDefaultValues(obr);
      if (getTable() != null) {
        getTable().init();
      }
    });
  }

  private AbstractField getDefaultComponent(PersonListMutElem mutationElement) {
    return setFieldStyle(getField(mutationElement));
  }

  private AbstractField getField(PersonListMutElem mutElem) {
    int catCode = mutElem.getElem().getCatCode();
    int elementCode = mutElem.getElem().getElemCode();
    GBAGroupElements.GBAGroupElem pleE = GBAGroupElements.getByCat(catCode, elementCode);

    Val val = pleE.getElem().getVal();
    GBATable tabel = pleE.getElem().getTable();
    AbstractField field = new GbaTextField();

    if (pleE.getElem() == GBAElem.BSN) {
      field = new BsnField();

    } else if (pleE.getElem() == GBAElem.ANR) {
      field = new AnrField();

    } else if (pleE.getElem() == GBAElem.POSTCODE) {
      field = new PostalcodeField();

    } else if (pleE.getElem() == GBAElem.AAND_GEG_IN_ONDERZ) {
      field = new IndicatieOnjuistField(mutElem.getCat());

    } else if (pleE.getElem() == GBAElem.AUTORIT_VAN_AFGIFTE_NL_REISDOC) {
      mutElem.setDefaultValue(() -> new FieldValue(mutElem.getCurrentValue().getVal()));

    } else if (tabel != GBATable.ONBEKEND) {
      TabelContainer container = new TabelContainer(tabel, false, true);
      if (container.getItemIds().size() < 10) {
        ProNativeSelect select = new ProNativeSelect();
        select.setContainerDataSource(container);
        select.setItemCaptionPropertyId(TabelContainer.OMSCHRIJVING);
        field = select;

      } else {
        ProComboBox select = new ProComboBox();
        select.setContainerDataSource(container);
        select.setItemCaptionPropertyId(TabelContainer.OMSCHRIJVING);
        select.setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_CONTAINS);
        field = select;
      }
    } else if (val instanceof NumVal) {
      field = new NumberField();

    } else if (val instanceof AlfVal) {
      ((GbaTextField) field).setMaxLength(((AlfVal) val).getMax());

    } else if (val instanceof DatVal) {
      DatumVeld datumVeld = new DatumVeld(null,
          new AbstractStringValidator("Incorrecte datum") {

            @Override
            public boolean isValidString(String value) {
              return new GbaDatumValidator().isValid(value);
            }
          });

      datumVeld.setUitzonderingenToestaan(true);
      field = datumVeld;

    } else {
      field.addValidator(getValidator(val));
    }

    if (!mutElem.isChangeble()) {
      field.setReadOnly(true);
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

    if (GBAElem.DATUM_VAN_OPNEMING.is(pleE.getElem())) {
      mutElem.setDefaultValue(() -> new DateFieldValue(new ProcuraDate().getSystemDate()));
    }

    if (!field.isReadOnly()) {
      setEditableFieldProperties(field, mutElem);
    }

    return field;
  }

  private AbstractField setEditableFieldProperties(AbstractField field, PersonListMutElem mutElem) {
    field.setImmediate(true);
    field.addValidator(PersonListMutationsChecks.getValidator(mutElem, list));
    field.addListener((ValueChangeListener) valueChangeEvent -> {
      validationLayout.clearMessages();
      list.forEach(elem -> {
        try {
          checkRequired(elem, elem.getField());
          elem.validate();
        } catch (Validator.InvalidValueException e) {
          validationLayout.addError(elem, e.getMessage());
        }
      });
      validationLayout.setErrors();
    });
    return field;
  }

  private void checkRequired(PersonListMutElem mutElem, AbstractField field) {
    Optional<String> requiredError = PersonListMutationsChecks.getRequiredError(mutElem, list);
    field.setRequired(requiredError.isPresent());
    requiredError.ifPresent(field::setRequiredError);
  }

  private AbstractStringValidator getValidator(Val val) {
    return new AbstractStringValidator("Incorrecte waarde") {

      @Override
      protected boolean isValidString(String value) {
        return val.isCorrect(value);
      }
    };
  }

  private AbstractField getInitComponent(PersonListMutElem mutationElement) {
    AbstractField f = getDefaultComponent(mutationElement);
    if (f.isReadOnly()) {
      return f;
    }
    // Bij geboorteland of gemeente van inschrijving
    // listeners toevoegen.
    if (mutationElement.getElemType() == GBAElem.GEM_INSCHR) {
      f.setImmediate(true);
      f.addListener(new ComboChangeListener());
    }
    return f;
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
    String val = "";
    for (BasePLRec rec : elem.getSet().getRecs()) {
      if (!rec.isIncorrect()) {
        val = rec.getElemVal(GBAElem.INGANGSDAT_GELDIG).getVal();
      }
    }
    return val;
  }

  private void setDefaultValues(PersonListMutElem mutationRecord) {
    BasePLValue val = mutationRecord.getElem().getValue();
    mutationRecord.getField()
        .setValue(mutationRecord.getDefaultValue()
            .map(Supplier::get)
            .orElse(new FieldValue(val.getVal(), val.getDescr())));
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

  private AbstractField getMunicipalityComboBox(PersonListMutElem place) {
    AbstractSelect field = (AbstractSelect) getDefaultComponent(place);
    list.getElem(place.getElemType())
        .map(PersonListMutElem::getField)
        .ifPresent(dateField -> DateReference.setFieldWithValidator(field, dateField));
    return field;
  }

  private AbstractField getMunicipalityTextField(PersonListMutElem municipality) {
    return setFieldStyle(setEditableFieldProperties(new GbaTextField(), municipality));
  }

  private void setMunicipalityField(Class<? extends AbstractField> fieldClass, Supplier<AbstractField> fieldSupplier,
      PersonListMutElem municipality) {
    if (fieldClass.isAssignableFrom(municipality.getField().getClass())) {
      return;
    }
    municipality.setField(fieldSupplier.get());
    setDefaultValues(municipality);
    if (getTable() != null) {
      getTable().init();
    }
  }

}
