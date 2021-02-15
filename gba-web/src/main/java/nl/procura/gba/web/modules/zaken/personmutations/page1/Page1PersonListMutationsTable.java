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

package nl.procura.gba.web.modules.zaken.personmutations.page1;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.personmutations.PersonListMutation;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;

public class Page1PersonListMutationsTable extends GbaTable {

  public Page1PersonListMutationsTable() {
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Nr", 20);
    addColumn("Ingevoerd op", 130);
    addColumn("Categorie");
    addColumn("Set");
    addColumn("Actie");
    addColumn("Status", 100);
    addColumn("Ingevoerd door", 180);

    super.setColumns();
  }

  @Override
  public void setRecords() {
    BasePLExt pl = getApplication().getServices().getPersonenWsService().getHuidige();

    int nr = 0;
    ZaakArgumenten za = new ZaakArgumenten(pl);
    for (PersonListMutation mut : getApplication().getServices()
        .getPersonListMutationService().getMinimalZaken(za)) {

      DateTime dt = new DateTime(mut.getDIn(), mut.getTIn());
      Record record = addRecord(mut);
      record.addValue(++nr);
      record.addValue(dt.toString());
      record.addValue(mut.getDescrCat());
      record.addValue(mut.getDescrSet());
      record.addValue(mut.getDescrAction());
      record.addValue(ZaakStatusType.get(mut.getIndVerwerkt().longValue()));
      record.addValue(mut.getUsr().getUsrfullname());
    }

    super.setRecords();
  }
}
