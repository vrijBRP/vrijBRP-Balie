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

package nl.procura.gba.web.modules.beheer.profielen.page8;

import static java.util.Arrays.asList;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.beheer.profiel.ProfielExtrasService;
import nl.procura.gba.web.services.beheer.profiel.gba_categorie.PleCategorie;
import nl.procura.commons.core.exceptions.ProException;

public class GBACategorieenTable extends GbaTable {

  private static final int INDEX_STATUS = 0;
  private final Profiel    profiel;

  public GBACategorieenTable(Profiel profiel) {
    this.profiel = profiel;
  }

  public ProfielExtrasService getProfielExtras() {
    return getApplication().getServices().getProfielExtrasService();
  }

  @Override
  public void onDoubleClick(Record record) {

    PleCategorieContainer pleContainer = (PleCategorieContainer) record.getObject();
    PleCategorie pleElement = pleContainer.getPleCategorie();
    boolean isGekoppeld = profiel.isGekoppeld(pleElement);

    getApplication().getServices().getProfielService().koppelActie(asList(pleElement), asList(profiel),
        KoppelActie.get(!isGekoppeld));
    setRecordValue(record, INDEX_STATUS, geefStatus(!isGekoppeld));
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Toon historie", 120).setUseHTML(true);
    addColumn("BRP-categorie");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    try {

      for (GBACat gbaCategorie : GBACat.values()) {

        if (gbaCategorie.getCode() > 0) {

          PleCategorie pleCategorie = getProfielExtras().getCategorie(gbaCategorie);
          PleCategorieContainer pleContainer = new PleCategorieContainer(gbaCategorie, pleCategorie);

          fillColumns(pleContainer, gbaCategorie);
        }
      }
    } catch (Exception e) {

      e.printStackTrace();
      throw new ProException(WARNING, "Fout bij tonen categorieen", e);
    }
  }

  private void fillColumns(PleCategorieContainer pleContainer, GBACat gbaCategorie) {

    PleCategorie pleCategorie = pleContainer.getPleCategorie();

    boolean isGekoppeld = profiel.isGekoppeld(pleCategorie);
    String status = geefStatus(isGekoppeld);
    Record r = addRecord(pleContainer);

    r.addValue(status);
    r.addValue(gbaCategorie.getCode() + ": " + gbaCategorie.getDescr());
  }

  private String geefStatus(boolean b) {
    return b ? setClass("green", "Wel historie") : setClass("red", "Geen historie");
  }
}
