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

import static nl.procura.gba.web.modules.bs.registration.person.modules.module1.PersonBean.*;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.GERELATEERDE_NIET_BRP;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.Field;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.fields.CountryBox;
import nl.procura.gba.web.components.fields.DateReference;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.registration.MunicipalityEnabler;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class BirthDetailsForm extends GbaForm<PersonBean> {

  private static final int MANDATORY_BIRTH_FIELD_SIZE = 3;
  private static final int MINIMUM_BIRTH_FIELD_SIZE   = 0;
  private DossierPersoon   person;

  public BirthDetailsForm() {
    setColumnWidths("130px", "");
    setOrder(F_DATE_OF_BIRTH, F_COUNTRY, F_FOREIGN_MUN, F_NL_MUN);
    setBean(new PersonBean());
  }

  @Override
  public void afterSetBean() {
    DateReference.setField(getField(F_COUNTRY, CountryBox.class), getField(F_DATE_OF_BIRTH));

    MunicipalityEnabler countryListener = new MunicipalityEnabler(this, F_NL_MUN, F_FOREIGN_MUN);

    Field country = getField(F_COUNTRY);
    country.addListener(countryListener);
    countryListener.valueChange(new ValueChangeEvent(country));

    super.afterSetBean();
  }

  @Override
  public void commit() {
    if (person.getDossierPersoonType().is(GERELATEERDE_NIET_BRP)) {
      ArrayList<Field> birthDetails = getFields(F_DATE_OF_BIRTH, F_COUNTRY, F_FOREIGN_MUN, F_NL_MUN);
      long size = birthDetails.stream()
          .filter(f -> f.getValue() != null)
          .filter(f -> StringUtils.isNotEmpty(f.getValue().toString()))
          .count();
      // Either all fields are empty or all fields should be present
      if (size > MINIMUM_BIRTH_FIELD_SIZE && size < MANDATORY_BIRTH_FIELD_SIZE) {
        throw new ProException(ProExceptionSeverity.WARNING,
            "Vul alle velden van de groep geboorte (datum/plaats/land) in. " +
                "Als één van deze velden is ingevuld, dan moeten alledrie worden ingevuld.");
      }
    }
    super.commit();
  }

  public void update(DossierPersoon person) {
    this.person = person;

    if (person.getDossierPersoonType().is(GERELATEERDE_NIET_BRP)) {
      removeMandatoryFields();
    }

    if (person.getDatumGeboorte() != null) {
      getField(F_DATE_OF_BIRTH).setValue(person.getDatumGeboorte());
    }
    if (person.getGeboorteland() != null) {
      getField(F_COUNTRY).setValue(person.getGeboorteland());
    }
    showMun(person.getGeboorteplaats());
  }

  private void showMun(FieldValue mun) {

    final FieldValue country = (FieldValue) getField(F_COUNTRY).getValue();

    if (country == null || Landelijk.isNederland(country)) {
      getField(F_NL_MUN).setVisible(true);
      getField(F_NL_MUN).setValue(mun);
      getField(F_FOREIGN_MUN).setVisible(false);
    } else {
      getField(F_NL_MUN).setVisible(false);
      getField(F_FOREIGN_MUN).setVisible(true);
      getField(F_FOREIGN_MUN).setValue(mun);
    }

    repaint();
  }

  private void removeMandatoryFields() {
    getField(F_DATE_OF_BIRTH).setRequired(false);
    getField(F_NL_MUN).setRequired(false);
    getField(F_FOREIGN_MUN).setRequired(false);
    getField(F_COUNTRY).setRequired(false);
  }
}
