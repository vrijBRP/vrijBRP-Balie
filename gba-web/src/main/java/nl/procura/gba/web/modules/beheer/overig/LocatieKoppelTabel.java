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
import static nl.procura.gba.web.services.beheer.locatie.LocatieType.AFHAAL_LOCATIE;
import static nl.procura.gba.web.services.beheer.locatie.LocatieType.NORMALE_LOCATIE;

import java.util.List;

import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.locatie.KoppelbaarAanLocatie;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.beheer.profiel.KoppelbaarAanProfiel;

/**
 * Een generieke locatietabel die gebruikt wordt om in beheer locaties aan gebruikers en
 * printopties te koppelen.
 * Het type van K kan dus Gebruiker of PrintOptie zijn.
 *

 * <p>
 * 2012
 */

public class LocatieKoppelTabel<K extends KoppelbaarAanLocatie> extends KoppelTabel<KoppelbaarAanProfiel> {

  private static final int INDEX_STATUS = 0;
  private final List<K>    koppelList;

  public LocatieKoppelTabel(List<K> koppelList) {
    this.koppelList = koppelList;
  }

  @Override
  public void onDoubleClick(Record record) {

    if (isSelectable()) {

      if (record.getObject() instanceof Locatie) {

        Locatie locatie = (Locatie) record.getObject();
        boolean isGekoppeld = locatie.isGekoppeld(koppelList);

        getApplication().getServices().getLocatieService().koppelActie(koppelList, asList(locatie),
            KoppelActie.get(!isGekoppeld));

        setRecordValue(record, INDEX_STATUS, KoppelActie.get(!isGekoppeld).getStatus());
      }
    }
  }

  @Override
  public void setColumns() {

    addColumn("Status", 100).setUseHTML(true);
    addColumn("ID", 50);
    addColumn("Type", 100);
    addColumn("Locatie");
  }

  @Override
  public void setRecords() {

    List<Locatie> list = getApplication().getServices().getLocatieService().getAlleLocaties(NORMALE_LOCATIE,
        AFHAAL_LOCATIE);

    for (Locatie loc : list) {

      boolean isCoupleObjectsCoupled = loc.isGekoppeld(koppelList);
      Record r = addRecord(loc);

      r.addValue(KoppelActie.get(isCoupleObjectsCoupled).getStatus());
      r.addValue(loc.getCLocation());
      r.addValue(loc.getLocatieType());
      r.addValue(loc.getLocatie());
    }
  }

  public void setTableStatus(KoppelActie koppelActie, List<Record> selectedRecords) {

    for (Record r : selectedRecords) {
      setRecordValue(r, INDEX_STATUS, koppelActie.getStatus());
    }
  }
}
