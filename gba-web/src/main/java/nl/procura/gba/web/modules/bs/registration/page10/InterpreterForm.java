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

package nl.procura.gba.web.modules.bs.registration.page10;

import static nl.procura.gba.web.modules.bs.registration.page10.InterpreterBean.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.bs.registration.InterpreterType;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class InterpreterForm extends GbaForm<InterpreterBean> {

  private final DossierRegistration dossRegistration;

  InterpreterForm(DossierRegistration dossier) {
    dossRegistration = dossier;
    setCaption("Aanwezigheid tolk");
    setColumnWidths("200px", "400px", "100px", "");

    final InterpreterBean bean = new InterpreterBean();
    bean.setInterpreter(InterpreterType.valueOfCode(dossRegistration.getInterpreter()));
    bean.setName(dossRegistration.getInterpreterName());
    bean.setLanguage(dossRegistration.getInterpreterLanguage());
    setBean(bean, F_INTERPRETER, F_NAME, F_LANGUAGE);
  }

  public void save() {
    commit();
    dossRegistration.setInterpreter(getBean().getInterpreter().getCode());
    dossRegistration.setInterpreterName(getBean().getName());
    dossRegistration.setInterpreterLanguage(getBean().getLanguage());
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(F_INTERPRETER)) {
      column.setColspan(3);
    }
    super.setColumn(column, field, property);
  }

  @Override
  public void afterSetBean() {
    final Field interpreter = getField(F_INTERPRETER);
    onInterpreterChange(InterpreterType.valueOfCode(dossRegistration.getInterpreter()));
    interpreter
        .addListener((ValueChangeListener) e -> onInterpreterChange((InterpreterType) e.getProperty().getValue()));
    super.afterSetBean();
  }

  private void onInterpreterChange(InterpreterType type) {
    if (InterpreterType.NONE.equals(type)) {
      getField(F_NAME).setVisible(false);
      getField(F_LANGUAGE).setVisible(false);
    } else {
      getField(F_NAME).setVisible(true);
      getField(F_LANGUAGE).setVisible(true);
    }
    repaint();
  }
}
