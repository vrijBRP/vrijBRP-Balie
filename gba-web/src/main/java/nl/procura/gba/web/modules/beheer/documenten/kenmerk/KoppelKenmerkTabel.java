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

package nl.procura.gba.web.modules.beheer.documenten.kenmerk;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Set;

import nl.procura.gba.web.modules.beheer.overig.KoppelTabel;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.zaken.documenten.KoppelbaarAanDocumentKenmerk;
import nl.procura.gba.web.services.zaken.documenten.kenmerk.DocumentKenmerk;

public class KoppelKenmerkTabel<K extends KoppelbaarAanDocumentKenmerk> extends KoppelTabel {

  private final List<K> koppelList;

  public KoppelKenmerkTabel(List<K> koppelList) {

    this.koppelList = koppelList;
  }

  @Override
  public void onDoubleClick(Record record) {

    if (isSelectable()) {

      DocumentKenmerk kenmerk = (DocumentKenmerk) record.getObject();

      boolean isGekoppeld = kenmerk.isGekoppeld(koppelList);

      getApplication().getServices().getKenmerkService().koppelActie(koppelList, asList(kenmerk),
          KoppelActie.get(!isGekoppeld));

      setRecordValue(record, 0, KoppelActie.get(!isGekoppeld).getStatus());
    }
  }

  @Override
  public void setColumns() {

    addColumn("Status", 100).setUseHTML(true);
    addColumn("ID", 50);
    addColumn("Naam");
  }

  @Override
  public void setRecords() {

    Set<DocumentKenmerk> list = getApplication().getServices().getKenmerkService().getKenmerken();

    for (DocumentKenmerk kenmerk : list) {

      boolean isKoppelObjectenGekoppeld = kenmerk.isGekoppeld(koppelList);

      Record r = addRecord(kenmerk);
      r.addValue(KoppelActie.get(isKoppelObjectenGekoppeld).getStatus());
      r.addValue(kenmerk.getCode());
      r.addValue(kenmerk.getKenmerkType());
    }
  }
}
