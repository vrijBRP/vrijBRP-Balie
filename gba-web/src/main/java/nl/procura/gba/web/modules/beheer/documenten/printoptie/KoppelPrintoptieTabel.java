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

package nl.procura.gba.web.modules.beheer.documenten.printoptie;

import static java.util.Arrays.asList;

import java.util.List;

import nl.procura.gba.web.modules.beheer.overig.KoppelTabel;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.profiel.KoppelbaarAanProfiel;
import nl.procura.gba.web.services.zaken.documenten.KoppelbaarAanPrintOptie;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;

public class KoppelPrintoptieTabel<K extends KoppelbaarAanPrintOptie> extends KoppelTabel<KoppelbaarAanProfiel> {

  private final List<K> koppelList;

  public KoppelPrintoptieTabel(List<K> koppelList) {
    this.koppelList = koppelList;
  }

  @Override
  public void onDoubleClick(Record record) {

    if (isSelectable()) {

      PrintOptie printoptie = (PrintOptie) record.getObject();

      boolean isGekoppeld = printoptie.isGekoppeld(koppelList);

      getApplication().getServices().getPrintOptieService().koppelActie(koppelList, asList(printoptie),
          KoppelActie.get(!isGekoppeld));

      setRecordValue(record, 0, KoppelActie.get(!isGekoppeld).getStatus());
    }
  }

  @Override
  public void setColumns() {

    addColumn("Status", 100).setUseHTML(true);
    addColumn("ID", 50);
    addColumn("Naam");
    addColumn("Opties");
  }

  @Override
  public void setRecords() {

    List<PrintOptie> list = getApplication().getServices().getPrintOptieService().getPrintOpties();

    for (PrintOptie printOptie : list) {

      boolean isKoppelObjectenGekoppeld = printOptie.isGekoppeld(koppelList);

      Record r = addRecord(printOptie);
      r.addValue(KoppelActie.get(isKoppelObjectenGekoppeld).getStatus());
      r.addValue(printOptie.getCPrintoptie());
      r.addValue(printOptie.getOms());
      r.addValue(printOptie.getCmd());
    }
  }
}
