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

import static nl.procura.gba.web.modules.bs.registration.person.modules.module1.PersonBean.F_ANR;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module1.PersonBean.F_BSN;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module1.PersonBean.F_FAMILY_NAME;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module1.PersonBean.F_FIRSTNAMES;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module1.PersonBean.F_GENDER;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module1.PersonBean.F_PREFIX;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module1.PersonBean.F_TITLE;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.ENTRY;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.Buffered;
import com.vaadin.data.Validator.InvalidValueException;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.standard.Globalfunctions;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.validation.Anummer;
import nl.procura.validation.Bsn;

public class PersonalDetailsForm extends GbaForm<PersonBean> {

  public PersonalDetailsForm() {

    setColumnWidths("130px", "260px", "130px", "");
    setOrder(F_BSN, F_ANR, F_FAMILY_NAME, F_PREFIX, F_FIRSTNAMES, F_TITLE, F_GENDER);

    final PersonBean bean = new PersonBean();
    setBean(bean);
  }

  public void update(DossierPersoon p) {
    if (Globalfunctions.def(p.getBsn())) {
      getField(F_BSN).setValue(Bsn.format(p.getBsn().toString()));
    }
    if (Globalfunctions.def(p.getAnr())) {
      getField(F_ANR).setValue(Anummer.format(p.getAnr().toString()));
    }
    if (p.getVoorvoegsel() != null) {
      getField(F_PREFIX).setValue(new FieldValue(p.getVoorvoegsel()));
    }
    if (p.getGeslachtsnaam() != null) {
      getField(F_FAMILY_NAME).setValue(p.getGeslachtsnaam());
    }
    if (p.getVoornaam() != null) {
      getField(F_FIRSTNAMES).setValue(p.getVoornaam());
    }
    if (p.getTitel() != null) {
      getField(F_TITLE).setValue(p.getTitel());
    }
    if (p.getGeslacht() != null) {
      getField(F_GENDER).setValue(p.getGeslacht());
    }
  }

  @Override
  public void setColumn(TableLayout.Column column, com.vaadin.ui.Field field, Property property) {
    if (property.is(F_GENDER)) {
      column.setColspan(3);
    }
    super.setColumn(column, field, property);
  }

  @Override
  public void commit() throws Buffered.SourceException, InvalidValueException {

    final String bsn = astr(getField(F_BSN).getValue());
    if (StringUtils.isBlank(bsn)) {
      throw new ProException(ENTRY, WARNING, "Vraag eerst een nieuw BSN op");
    }

    final String anr = astr(getField(F_ANR).getValue());
    if (StringUtils.isBlank(anr)) {
      throw new ProException(ENTRY, WARNING, "Vraag eerst een nieuw A-nummer op");
    }

    super.commit();
  }
}
