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

package nl.procura.gba.web.modules.zaken.vog.page13;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;
import nl.procura.gba.web.services.zaken.vog.VogFunctie;

public class Page13VogTable2 extends GbaTable {

  private VogAanvraag aanvraag = null;

  public Page13VogTable2(VogAanvraag aanvraag) {
    setAanvraag(aanvraag);
  }

  @Override
  public void attach() {

    super.attach();

    for (Record r : getRecords()) {
      if (getAanvraagFunctiegebieden().contains(r.getObject())) {
        select(r.getItemId());
      }
    }
  }

  public VogAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(VogAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  /**
   * De functiegebieden van de aanvraag
   */
  public List<VogFunctie> getAanvraagFunctiegebieden() {
    return aanvraag.getScreening().getFunctiegebieden();
  }

  public List<VogFunctie> getSelectedVogFuncties() {

    List<VogFunctie> l = new ArrayList<>();

    for (Record r : getSelectedRecords()) {
      l.add((VogFunctie) r.getObject());
    }

    return l;
  }

  @Override
  public void onClick(Record record) {

    VogFunctie f = (VogFunctie) record.getObject();

    if (getAanvraagFunctiegebieden().contains(f)) {
      getAanvraagFunctiegebieden().remove(f);
    } else {
      getAanvraagFunctiegebieden().add(f);
    }

    super.onClick(record);
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Nr", 30);
    addColumn("Functiegebied");

    super.setColumns();
  }

  @Override
  public void setPageLength(int pageLength) {
    super.setPageLength(getRecords().size());
  }

  @Override
  public void setRecords() {

    for (VogFunctie f : getApplication().getServices().getVogService().getFuncties()) {

      Record r = addRecord(f);

      r.addValue(f.getVogFuncTab());
      r.addValue(f.getOms());
    }
  }
}
