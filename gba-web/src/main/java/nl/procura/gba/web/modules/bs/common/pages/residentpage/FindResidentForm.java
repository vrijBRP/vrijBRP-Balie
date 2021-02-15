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

package nl.procura.gba.web.modules.bs.common.pages.residentpage;

import static nl.procura.gba.web.modules.bs.common.pages.residentpage.ResidentPageBean.*;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisAdres;

class FindResidentForm extends GbaForm<ResidentPageBean> {

  FindResidentForm(VerhuisAdres verhuisAdres) {

    setCaption("Zoek bewoners");
    setOrder(F_NIEUW_ADRES, F_GEBOORTEDATUM, F_BSN, F_GESLACHTSNAAM);

    setColumnWidths("130px", "", "100px", "250px");
    final ResidentPageBean b = new ResidentPageBean();
    b.setNieuwAdres(verhuisAdres.getAddressLabel());
    setBean(b);
  }
}
