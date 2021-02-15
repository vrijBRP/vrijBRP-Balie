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

package nl.procura.gba.web.modules.zaken.uittreksel.overzicht;

import static nl.procura.gba.web.modules.zaken.document.overzicht.DocumentOverzichtLayoutBean.*;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;

public class UittrekselOverzichtLayoutForm extends ReadOnlyForm {

  public UittrekselOverzichtLayoutForm(DocumentZaak aanvraag) {
    setCaption(aanvraag.getDoc().getDocumentType().getOms());
    setOrder(DOCUMENT, AFNEMER, DOEL);
    setColumnWidths(WIDTH_130, "");

    UittrekselOverzichtLayoutBean bean = new UittrekselOverzichtLayoutBean();
    bean.setDocument(aanvraag.getDoc().getDocument());
    bean.setAfnemer(fil(aanvraag.getDocumentAfn()) ? aanvraag.getDocumentAfn() : "Niet van toepassing");
    bean.setDoel(fil(aanvraag.getDocumentDoel()) ? aanvraag.getDocumentDoel() : "Niet van toepassing");

    setBean(bean);
  }

  @Override
  public UittrekselOverzichtLayoutBean getBean() {
    return (UittrekselOverzichtLayoutBean) super.getBean();
  }
}
