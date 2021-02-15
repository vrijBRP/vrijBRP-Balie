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

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class RelativesNotInBrpDetailsForm extends GbaForm<PersonBean> {

  RelativesNotInBrpDetailsForm() {
    setCaption("Persoon");
    setColumnWidths("130px", "260px", "130px", "");
    setOrder(F_FAMILY_NAME, F_FIRSTNAMES, F_PREFIX, F_TITLE, F_GENDER);

    setBean(new PersonBean());
  }

  public void update(DossierPersoon p) {
    if (p.getGeslachtsnaam() != null) {
      getField(F_FAMILY_NAME).setValue(p.getGeslachtsnaam());
    }
    if (p.getVoornaam() != null) {
      getField(F_FIRSTNAMES).setValue(p.getVoornaam());
    }
    if (p.getVoorvoegsel() != null) {
      getField(F_TITLE).setValue(p.getTitel());
    }
    if (p.getVoorvoegsel() != null) {
      getField(F_PREFIX).setValue(new FieldValue(p.getVoorvoegsel()));
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
}
