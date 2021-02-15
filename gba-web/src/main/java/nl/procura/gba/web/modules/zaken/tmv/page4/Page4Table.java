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

package nl.procura.gba.web.modules.zaken.tmv.page4;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingDetail;

public class Page4Table extends GbaTable {

  private TerugmeldingAanvraag tmv = null;

  public Page4Table(TerugmeldingAanvraag tmv) {
    setTmv(tmv);
  }

  public TerugmeldingAanvraag getTmv() {
    return tmv;
  }

  public void setTmv(TerugmeldingAanvraag tmv) {
    this.tmv = tmv;
  }

  @Override
  public void setColumns() {

    addColumn("Categorie", 120);
    addColumn("Element", 160);
    addColumn("Set", 60);
    addColumn("Huidig");
    addColumn("Nieuw");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    try {

      for (TerugmeldingDetail e : tmv.getDetails()) {

        Record r = addRecord(e);

        r.addValue(e.getCat().getDescr());
        r.addValue(e.getPleType().getElem().getDescr());
        r.addValue(e.getVolgnr() + 1);
        r.addValue(e.getFormatOrigineel());
        r.addValue(e.getFormatNieuw());
      }
    } catch (Exception e) {
      getApplication().handleException(getWindow(), e);
    }
  }
}
