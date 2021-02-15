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

package nl.procura.gba.web.modules.bs.registration.identification;

import static nl.procura.gba.web.modules.bs.registration.identification.IdentificationBean.*;

import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.zaken.identiteit.IdentificatieType;

public class IdentificationForm extends GbaForm<IdentificationBean> {

  public IdentificationForm(DossierPersoon person) {
    setCaption("Identificate");
    setColumnWidths("130px", "");
    setOrder(F_SOORT, F_ISSUING_COUNTRY, F_NUMMER);
    setPersonIdentity(person);
  }

  public void setPersonIdentity(DossierPersoon person) {
    setBean(new IdentificationBean());
    getField(F_SOORT).setValue(IdentificatieType.get(person.getSoort()));
    getField(F_ISSUING_COUNTRY).setValue(GbaTables.LAND.get(person.getCIssueCountry()));
    if (person.getIdDocNr() != null) {
      getField(F_NUMMER).setValue(person.getIdDocNr());
    }
  }
}
