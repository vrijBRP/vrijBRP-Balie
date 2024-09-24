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

package nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.selectie;

import static nl.procura.standard.Globalfunctions.date2str;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.common.misc.SelectListener;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.bag.ProcuraInhabitantsAddress;
import nl.procura.vaadin.component.dialog.ModalWindow;

/**
 * Selecteer een adres
 */
public class AdresSelectiePage extends NormalPageTemplate {

  private final Table                                     table    = new Table();
  private List<ProcuraInhabitantsAddress>                 adressen = new ArrayList<>();
  private final SelectListener<ProcuraInhabitantsAddress> listener;

  public AdresSelectiePage(List<ProcuraInhabitantsAddress> adressen,
      SelectListener<ProcuraInhabitantsAddress> listener) {
    this.listener = listener;
    setInfo("Selecteer een adres.");
    setAdressen(adressen);
    addExpandComponent(table);
  }

  public List<ProcuraInhabitantsAddress> getAdressen() {
    return adressen;
  }

  public void setAdressen(List<ProcuraInhabitantsAddress> adressen) {
    this.adressen = adressen;
  }

  @Override
  public void onEnter() {
    listener.select(table.getSelectedRecord().getObject(ProcuraInhabitantsAddress.class));
    super.onEnter();
  }

  private class Table extends GbaTable {

    @Override
    public void setColumns() {
      setSelectable(true);
      addColumn("Adres");
      super.setColumns();
    }

    @Override
    public void onClick(Record record) {
      listener.select(record.getObject(ProcuraInhabitantsAddress.class));
      super.onClick(record);
      ((ModalWindow) getWindow()).closeWindow();
    }

    @Override
    public void setRecords() {
      for (ProcuraInhabitantsAddress a : getAdressen()) {
        Record r = addRecord(a);
        String adres = a.getAddressLabel();

        if (pos(a.getEndDate())) {
          adres += " - beëindigd op " + date2str(a.getEndDate());
        }

        r.addValue(adres);
      }
    }
  }
}
