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

package nl.procura.gba.web.modules.beheer.overig;

import static java.util.Arrays.asList;

import java.util.List;

import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.profiel.KoppelbaarAanProfiel;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.theme.GbaWebTheme;

public class ProfielKoppelTabel<K extends KoppelbaarAanProfiel> extends KoppelTabel<KoppelbaarAanProfiel> {

  private static final int INDEX_STATUS = 0;
  private final List<K>    koppelList;

  public ProfielKoppelTabel(List<K> koppelList) {
    this.koppelList = koppelList;
  }

  @Override
  public void onDoubleClick(Record record) {

    if (isSelectable()) {

      if (record.getObject() instanceof Profiel) {

        Profiel profiel = (Profiel) record.getObject();

        boolean isGekoppeld = profiel.isGekoppeld(koppelList);

        getApplication().getServices().getProfielService().koppelActie(koppelList, asList(profiel),
            KoppelActie.get(!isGekoppeld));

        setRecordValue(record, INDEX_STATUS, KoppelActie.get(!isGekoppeld).getStatus());
      }
    }
  }

  @Override
  public void setColumns() {

    addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

    addColumn("Status", 100).setUseHTML(true);
    addColumn("Code", 50);
    addColumn("Profiel");
    addColumn("Omschrijving");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    try {

      List<Profiel> list = getApplication().getServices().getProfielService().getProfielen();

      for (Profiel profiel : list) {

        boolean isGekoppeld = profiel.isGekoppeld(koppelList);

        Record r = addRecord(profiel);
        r.addValue(KoppelActie.get(isGekoppeld).getStatus());
        r.addValue(profiel.getCProfile());
        r.addValue(profiel.getProfiel());
        r.addValue(profiel.getOmschrijving());
      }
    } catch (Exception e) {
      getApplication().handleException(getWindow(), e);
    }
  }

  public void setTableStatus(KoppelActie koppelActie, List<Record> selectedRecords) {
    setTableStatus(koppelActie, INDEX_STATUS, selectedRecords);
  }
}
