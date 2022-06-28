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

package nl.procura.gba.web.modules.zaken.personmutationsindex.page1;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Embedded;

import nl.procura.bsm.rest.v1_0.objecten.algemeen.BsmRestElement;
import nl.procura.bsm.rest.v1_0.objecten.gba.probev.mutations.MutationRestElement;
import nl.procura.bsm.rest.v1_0.objecten.gba.probev.mutations.MutationType;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.vaadin.theme.twee.Icons;

public class Page1MutationsIndexTable extends GbaTable {

  private List<MutationRestElement> mutations = new ArrayList<>();

  @Override
  public void setColumns() {
    setSelectable(true);
    setMultiSelect(true);

    addColumn("Nr", 50);
    addColumn("&nbsp;", 20).setClassType(Embedded.class);
    addColumn("Status", 230);
    addColumn("Naam");
    addColumn("A-nummer", 100);
    addColumn("Datum/tijd mutatie", 160);
    addColumn("Categorie", 160);
    addColumn("Datum/tijd goedkeuring", 160);

    super.setColumns();
  }

  public void setMutations(List<MutationRestElement> mutations) {
    this.mutations = mutations;
    init();
  }

  @Override
  public int getPageLength() {
    return 15;
  }

  @Override
  public void setRecords() {
    long nr = 0;
    for (MutationRestElement mut : mutations) {
      Record record = addRecord(mut);
      record.addValue(++nr);
      record.addValue(getIcon(mut.getStatusMutation().getWaarde()));
      record.addValue(getWaardeOms(mut.getStatusMutation()));
      record.addValue(mut.getName().getWaarde());
      record.addValue(mut.getAnr().getOmschrijving());
      record.addValue(getWaarde(mut.getDateMutation(), mut.getTimeMutation()));
      record.addValue(getWaardeOms(mut.getCat()));
      record.addValue(mut.getDateApproval().map(BsmRestElement::getOmschrijving).orElse("") + " "
          + mut.getTimeApproval().map(BsmRestElement::getOmschrijving).orElse(""));
    }

    super.setRecords();
  }

  private String getWaarde(BsmRestElement e1, BsmRestElement e2) {
    return e1.getOmschrijving() + " " + e2.getOmschrijving();
  }

  private String getWaardeOms(BsmRestElement e) {
    return e.getWaarde() + " - " + e.getOmschrijving();
  }

  @Override
  public void init() {
    super.init();
    getRecords()
        .stream()
        .filter(rec -> MutationType.NIET_GOEDGEKEURD.is(rec.getObject(MutationRestElement.class)
            .getStatusMutation()
            .getWaarde()))
        .forEach(rec -> select(rec.getItemId()));
  }

  protected TableImage getIcon(String status) {
    int icon = Icons.ICON_EMPTY;
    switch (MutationType.get(status)) {
      case GOEDGEKEURD:
      case GOEDGEKEURD_EN_SPONTAAN_MUTATIE:
      case GOEDGEKEURD_EN_SPONTAAN_VERWERKT:
      case GOEDGEKEURD_EN_SPONTAAN_VOORBEREID:
        icon = Icons.ICON_OK;
        break;
      case NIET_GOEDGEKEURD:
        icon = Icons.ICON_WARN;
        break;
      default:
    }
    return new TableImage(Icons.getIcon(icon));
  }
}
