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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm;

import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;

public class BsmUitvoerenForm extends ReadOnlyForm {

  public BsmUitvoerenForm(String caption, boolean isVisible, String... fields) {
    setCaption(caption);
    setReadThrough(true);
    setOrder(fields);
    setVisible(isVisible);

    setColumnWidths("70px", "");
    setBean(new BsmUitvoerenBean());
  }

  @Override
  public void afterSetBean() {
    if (getField(BsmUitvoerenBean.RESULTAAT) != null) {
      setVisible(fil(getBean().getResultaat()));
    }
    super.afterSetBean();
  }

  @Override
  public BsmUitvoerenBean getBean() {
    return (BsmUitvoerenBean) super.getBean();
  }
}
