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

package nl.procura.gba.web.modules.beheer.parameters.form;

import nl.procura.gba.web.services.beheer.parameter.ParameterType;

public class ParameterChangeListener {

  public void onChange(ParameterType key, Object value, DatabaseParameterForm form) {

    // -------------------------------------------------------------
    // Might be used again if we have a dedicated 'document service'
    // -------------------------------------------------------------
    //    switch (key) {
    //      case DOC_DMS_TYPE:
    //        form.getVaadinFieldByAnnotation(AppParameterType.DOC_OUTPUT_PAD).setVisible(DmsTypeContainer.PROWEB_PERSONEN.equals(value));
    //        form.getVaadinFieldByAnnotation (AppParameterType.DOC_DMS_ARCHIEF_ENDPOINT).setVisible (DmsTypeContainer.PROWEB_ARCHIEF.equals (value));
    //        form.getVaadinFieldByAnnotation (AppParameterType.DOC_DMS_ARCHIEF_WORKSPACE).setVisible (DmsTypeContainer.PROWEB_ARCHIEF.equals (value));
    //        form.getVaadinFieldByAnnotation (AppParameterType.DOC_DMS_ARCHIEF_GEBRUIKER).setVisible (DmsTypeContainer.PROWEB_ARCHIEF.equals (value));
    //        form.getVaadinFieldByAnnotation (AppParameterType.DOC_DMS_ARCHIEF_WW).setVisible (DmsTypeContainer.PROWEB_ARCHIEF.equals (value));
    //        break;
    //      default:
    //        break;
    //    }

    form.repaint();
  }
}
