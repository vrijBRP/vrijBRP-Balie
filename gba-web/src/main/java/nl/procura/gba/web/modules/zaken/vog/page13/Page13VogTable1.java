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

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;
import nl.procura.gba.web.services.zaken.vog.VogProfiel;

public class Page13VogTable1 extends GbaTable {

  private VogAanvraag aanvraag   = null;
  private VogProfiel  vogProfiel = new VogProfiel();

  public Page13VogTable1(VogAanvraag aanvraag) {

    setAanvraag(aanvraag);

    setVogProfiel(aanvraag.getScreening().getProfiel());
  }

  @Override
  public void attach() {

    super.attach();

    for (Record record : getRecords()) {
      if (record.getObject().equals(getVogProfiel())) {
        select(record.getItemId());
      }
    }
  }

  public VogAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(VogAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  public VogProfiel getSelectedVogProfiel() {
    return (VogProfiel) ((getSelectedRecords().size() > 0) ? getSelectedRecord().getObject() : null);
  }

  public VogProfiel getVogProfiel() {
    return vogProfiel;
  }

  public void setVogProfiel(VogProfiel vogProfiel) {
    this.vogProfiel = vogProfiel;
  }

  @Override
  public void setColumns() {

    setSelectable(true);

    addColumn("Nr", 30);
    addColumn("Screeningsprofiel");

    super.setColumns();
  }

  @Override
  public void setPageLength(int pageLength) {
    super.setPageLength(getRecords().size());
  }

  @Override
  public void setRecords() {

    for (VogProfiel f : getApplication().getServices().getVogService().getProfielen()) {

      Record r = addRecord(f);

      r.addValue(f.getVogProfTab());
      r.addValue(f.getOms());
    }
  }
}
