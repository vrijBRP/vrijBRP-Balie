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

import nl.procura.gba.web.modules.bs.registration.person.modules.module1.RelativeNotInBrpWindow;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;

class Page20RelativesNotInBrpTable extends AbstractPeopleTable {

  private final DossierRegistration zaakDossier;

  Page20RelativesNotInBrpTable(DossierRegistration zaakDossier) {
    super(zaakDossier, DossierPersoonType.GERELATEERDE_NIET_BRP);
    this.zaakDossier = zaakDossier;
  }

  @Override
  public void onDoubleClick(Record record) {
    final DossierPersoon p = record.getObject(DossierPersoon.class);
    getApplication().getParentWindow().addWindow(new RelativeNotInBrpWindow(p, dossierPersoon -> {
      zaakDossier.getDossier().toevoegenPersoon(p);
      setRecords();
    }));
  }
}
