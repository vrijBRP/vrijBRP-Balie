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

package nl.procura.gba.web.modules.beheer.profielen.page6;

import static java.util.Arrays.asList;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.beheer.profiel.ProfielExtrasService;
import nl.procura.gba.web.services.beheer.profiel.gba_element.PleElement;
import nl.procura.commons.core.exceptions.ProException;

public class GBAelementenTable extends GbaTable {

  private static final int INDEX_STATUS = 0;
  private final GBACat     gbaCategorie;

  private final Profiel profiel;

  public GBAelementenTable(Profiel profiel, GBACat gbaCategorie) {

    this.profiel = profiel;
    this.gbaCategorie = gbaCategorie;
  }

  public ProfielExtrasService getProfielExtras() {
    return getApplication().getServices().getProfielExtrasService();
  }

  @Override
  public void onDoubleClick(Record record) {

    PleElementContainer pleContainer = (PleElementContainer) record.getObject();
    PleElement pleElement = pleContainer.getPleElement();
    boolean isGekoppeld = profiel.isGekoppeld(pleElement);

    getApplication().getServices().getProfielService().koppelActie(asList(pleElement), asList(profiel),
        KoppelActie.get(!isGekoppeld));
    setRecordValue(record, INDEX_STATUS, geefStatus(!isGekoppeld));
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Status", 120).setUseHTML(true);
    addColumn("", 20);
    addColumn("BRP-categorie", 150);
    addColumn("", 30);
    addColumn("BRP-element");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    try {

      List<GBAGroupElements.GBAGroupElem> list = GBAGroupElements.getAll();
      list.sort(new GBAGroupElements.Sorter());

      for (GBAGroupElements.GBAGroupElem pleType : list) {

        if (gbaCategorie == null || gbaCategorie.equals(pleType.getCat())) {

          PleElement pleElement = getProfielExtras().getElement(pleType);
          PleElementContainer pleContainer = new PleElementContainer(pleType, pleElement);

          fillColumns(pleContainer, pleType);
        }
      }
    } catch (Exception e) {
      throw new ProException(WARNING, "Fout bij tonen elementen", e);
    }
  }

  private void fillColumns(PleElementContainer pleContainer, GBAGroupElements.GBAGroupElem pleType) {

    PleElement pleElement = pleContainer.getPleElement();

    boolean b = profiel.isGekoppeld(pleElement);
    String status = geefStatus(b);
    Record r = addRecord(pleContainer);

    String catCode = String.format("%02d", pleType.getCat().getCode());
    String elementCode = String.format("%04d", pleType.getElem().getCode());

    r.addValue(status);
    r.addValue(catCode);
    r.addValue(pleType.getCat().getDescr());
    r.addValue(elementCode);
    r.addValue(pleType.getElem().getDescr());
  }

  private String geefStatus(boolean b) {
    return b ? setClass("green", "Gekoppeld") : setClass("red", "Niet-gekoppeld");
  }
}
