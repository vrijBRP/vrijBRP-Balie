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

package nl.procura.gba.web.modules.persoonslijst.overig.data;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.gba.web.modules.persoonslijst.overig.PlForm;
import nl.procura.gba.web.modules.persoonslijst.overig.meta.PlMetaPage;
import nl.procura.vaadin.component.layout.Fieldset;

public class PlDataPage extends PlMetaPage {

  public PlDataPage(String title, BasePLSet set) {
    this(title, set, null);
  }

  public PlDataPage(String title, BasePLSet set, BasePLRec record) {
    super(title, set, record);
  }

  public PlForm getMainForm() {
    return null;
  }

  public List<PlForm> getMainForms() {
    return new ArrayList<>();
  }

  public PlDataBeanForm getMetaForm(BasePLRec record) {
    return new PlDataBeanForm(record);
  }

  @Override
  protected void setLayouts() {

    super.setLayouts();

    PlDataBeanForm metaForm = getMetaForm(getGbaRecord());

    HorizontalLayout h = new HorizontalLayout();
    h.setSpacing(true);
    h.setWidth("100%");

    VerticalLayout catV = new VerticalLayout();
    catV.setSpacing(true);
    catV.setWidth("100%");

    VerticalLayout metaV = new VerticalLayout();
    metaV.setSpacing(true);
    metaV.setWidth("300px");

    PlForm<?> mainForm = getMainForm();
    if (mainForm != null) {
      catV.addComponent(mainForm);
    }
    getMainForms().forEach(form -> catV.addComponent(new Fieldset(form)));
    metaV.addComponent(metaForm);

    h.addComponent(catV);

    if (metaForm.getOrder().length > 0) {
      h.addComponent(metaV);
    }

    h.setExpandRatio(catV, 1.0f);
    getLayout().addComponent(h);
  }
}
