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

package nl.procura.gba.web.modules.bs.registration.page20;

import nl.procura.gba.web.modules.bs.registration.SingleDisplayFormWindow;
import nl.procura.gba.web.modules.bs.registration.person.PersonSummaryForm;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.functies.BsDossierNaamgebruikUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;

class Page20RelativesInBrpTable extends AbstractPeopleTable {

  Page20RelativesInBrpTable(DossierRegistration zaakDossier) {
    super(zaakDossier, DossierPersoonType.GERELATEERDE_BRP);
  }

  @Override
  public void onDoubleClick(Record record) {
    final DossierPersoon person = record.getObject(DossierPersoon.class);
    final PersonSummaryForm personForm = new PersonSummaryForm(person);
    String title = BsDossierNaamgebruikUtils.getNormalizedNameWithAge(person);
    getApplication().getParentWindow().addWindow(new SingleDisplayFormWindow(personForm,
        "800px", "Persoonsgegevens", title));
  }
}
