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

package nl.procura.gba.web.modules.bs.registration;

import com.vaadin.data.Property;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.form.tableform.TableForm;

public class MunicipalityEnabler implements Property.ValueChangeListener {

  private final TableForm form;
  private final String    municipality;
  private final String    foreignMunicipality;

  public MunicipalityEnabler(TableForm form, String municipality, String foreignMunicipality) {
    this.form = form;
    this.municipality = municipality;
    this.foreignMunicipality = foreignMunicipality;
  }

  @Override
  public void valueChange(Property.ValueChangeEvent event) {
    if (Landelijk.isNederland((FieldValue) event.getProperty().getValue())) {
      form.getField(municipality).setVisible(true);
      form.getField(municipality).focus();
      form.getField(foreignMunicipality).setValue("");
      form.getField(foreignMunicipality).setVisible(false);
    } else {
      form.getField(municipality).setValue("");
      form.getField(municipality).setVisible(false);
      form.getField(foreignMunicipality).setVisible(true);
      form.getField(foreignMunicipality).focus();
    }
    form.repaint();
  }
}
