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

package nl.procura.gba.web.modules.beheer.parameters.form.popup;

import static nl.procura.gba.web.modules.beheer.parameters.form.popup.ParameterWarningBean.*;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;

public class ParameterWarningForm extends ReadOnlyForm<ParameterWarningBean> {

  public ParameterWarningForm(String parameter, String warning, String cause) {

    setCaption("Waarschuwing");

    if (fil(cause)) {
      setOrder(PARAMETER, WARNING, CAUSE);
    } else {
      setOrder(PARAMETER, WARNING);
    }

    setColumnWidths("100px", "");

    ParameterWarningBean bean = new ParameterWarningBean();
    bean.setParameter(parameter);
    bean.setWarning(warning);
    bean.setCause(cause);

    setBean(bean);
  }
}
