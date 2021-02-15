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

package nl.procura.gba.web.modules.bs.registration.page40;

import static java.util.stream.Collectors.toList;
import static nl.procura.gba.web.services.bs.algemeen.functies.BsDossierNaamgebruikUtils.getNameWithAge;

import java.util.List;
import java.util.function.Consumer;

import com.vaadin.ui.Embedded;

import nl.procura.gba.jpa.personen.db.DossPersRelation;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.services.bs.registration.relations.Relation;
import nl.procura.vaadin.theme.twee.Icons;

public class Page40RegistrationTable1 extends Page40RegistrationTable {

  private final Consumer<Relation> onDoubleClickListener;

  Page40RegistrationTable1(Consumer<Relation> onDoubleClickListener) {
    this.onDoubleClickListener = onDoubleClickListener;
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(false);

    addColumn("Nr.", 30);
    addColumn("&nbsp;", 20).setClassType(Embedded.class);
    addColumn("Naam").setUseHTML(true);
    addColumn("Relatie").setUseHTML(true);
    addColumn("Naam").setUseHTML(true);

    super.setColumns();
  }

  void update(List<Relation> relations) {
    clear();
    int nr = 1;
    for (Relation relation : relations) {
      final Record record = addRecord(relation);
      record.addValue(nr);
      record.addValue(getStatus(relation));
      record.addValue(toRow(getNameWithAge(relation.getPerson())));
      record.addValue(toRow(relation.getRelationType().getOms()));

      if (relation.getRelationType().isRelated()) {
        record.addValue(toRow(getNameWithAge(relation.getRelatedPerson())));
      } else {
        record.addValue("");
      }
      nr++;
    }
    reloadRecords();
  }

  private TableImage getStatus(Relation relation) {
    if (relation.isProcessable()) {
      return null;
    }
    return new TableImage(Icons.getIcon(Icons.ICON_WARN));
  }

  public List<DossPersRelation> getSelectedRelations() {
    return getSelectedRecords().stream()
        .map(r -> (Relation) r.getObject())
        .map(Relation::getRelation)
        .collect(toList());
  }

  @Override
  public void onDoubleClick(Record record) {
    Relation relation = record.getObject(Relation.class);
    onDoubleClickListener.accept(relation);
  }
}
