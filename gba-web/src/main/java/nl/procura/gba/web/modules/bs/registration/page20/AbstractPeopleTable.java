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

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;

import lombok.Getter;

abstract class AbstractPeopleTable extends GbaTable {

  @Getter
  private final DossierRegistration zaakDossier;
  private final DossierPersoonType  type;

  AbstractPeopleTable(DossierRegistration zaakDossier, DossierPersoonType type) {
    this.zaakDossier = zaakDossier;
    this.type = type;
  }

  @Override
  public void setColumns() {
    setSelectable(true);
    setMultiSelect(true);

    addColumn("Nr.", 30);
    addColumn("Naam");
    addColumn("Geboren", 100);
    addColumn("Geslacht", 60);

    super.setColumns();
  }

  @Override
  public void setRecords() {
    int nr = 1;
    getRecords().clear();
    for (final DossierPersoon person : zaakDossier.getDossier().getPersonen(type)) {
      final Record record = addRecord(person);
      record.addValue(nr++);
      record.addValue(person.getNaam().getNaam_naamgebruik_eerste_voornaam());
      record.addValue(person.getGeboorte().getDatum_leeftijd());
      record.addValue(person.getGeslacht().getNormaal());
    }
    reloadRecords();
  }

}
