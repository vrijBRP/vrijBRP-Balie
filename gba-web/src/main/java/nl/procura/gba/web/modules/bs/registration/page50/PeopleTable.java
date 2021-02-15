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

package nl.procura.gba.web.modules.bs.registration.page50;

import static java.util.Collections.unmodifiableList;

import java.util.List;
import java.util.function.Consumer;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.DeclarationType;
import nl.procura.vaadin.component.table.indexed.IndexedTable;

class PeopleTable extends GbaTable {

  private final List<DossierPersoon>     people;
  private final Consumer<DossierPersoon> onClick;

  PeopleTable(List<DossierPersoon> people, Consumer<DossierPersoon> onDoubleClick) {
    this.people = unmodifiableList(people);
    this.onClick = onDoubleClick;
    setSelectable(true);
    setSelectFirst(true);
    setNullSelectionAllowed(false);
  }

  @Override
  public void setColumns() {
    addColumn("Nr", 40);
    addColumn("Soort", 140);
    addColumn("Persoon");
    addColumn("Aangifte", 140);
    addColumn("Geslacht", 105);
    addColumn("Geboren", 120);

    super.setColumns();
  }

  @Override
  public void setRecords() {
    int number = 1;
    for (final DossierPersoon person : people) {
      final IndexedTable.Record r = addRecord(person);
      r.addValue(number);
      r.addValue(person.getDossierPersoonType().getDescr());
      r.addValue(person.getNaam().getNaam_naamgebruik_eerste_voornaam());
      r.addValue(DeclarationType.valueOfCode(person.getBron()));
      r.addValue(person.getGeslacht());
      r.addValue(person.getDatumGeboorte().getFormatDate());
      number++;
    }

    super.setRecords();
  }

  @Override
  public void onDoubleClick(Record record) {
    super.onDoubleClick(record);
    onClick.accept(record.getObject(DossierPersoon.class));
  }
}
