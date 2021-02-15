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

package nl.procura.gba.web.modules.bs.huwelijk.page40;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Component;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsGerelateerdeNavLayout;
import nl.procura.gba.web.services.bs.algemeen.DossierService;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.theme.GbaWebTheme;

public class Page40HuwelijkTable extends GbaTable {

  final private List<DossierPersoon> getuigenLijst = new ArrayList<>();

  public Page40HuwelijkTable(DossierHuwelijk dossierHuwelijk) {

    List<DossierPersoon> set = dossierHuwelijk.getGetuigen();
    getuigenLijst.addAll(set);
    int aantal = dossierHuwelijk.getHuwelijksLocatie().getLocatieSoort().getAantalGetuigenMax();

    for (int i = set.size(); i < aantal; i++) {
      getuigenLijst.add(new DossierPersoon(DossierPersoonType.GETUIGE));
    }
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Nr", 30);
    addColumn("Naam", 300).setUseHTML(true);
    addColumn("Adres");
    addColumn("Toelichting");
    addColumn("&nbsp;", 70).setClassType(Component.class);
    addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    int nr = 0;
    int index = 0;

    for (DossierPersoon person : BsPersoonUtils.sort(getuigenLijst)) {
      nr++;

      Record r = addRecord(person);
      r.addValue(nr);

      if (person.isVolledig()) {
        String adres = person.getAdres().getAdres_pc_wpl();
        r.addValue(BsPersoonUtils.getNaam(person));
        r.addValue(fil(adres) ? adres : "Geen adres ingevoerd");

      } else {
        r.addValue(setClass("grey", "Dubbelklik om de gegevens in te voeren"));
        r.addValue("");
      }

      r.addValue(person.getToelichting());
      DossierService service = getApplication().getServices().getDossierService();
      r.addValue(new PageBsGerelateerdeNavLayout(service, this, person, getuigenLijst, index++));
    }

    super.setRecords();
  }
}
