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

package nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.bs.algemeen.DossierService;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.theme.GbaWebTheme;

public abstract class PageBsGerelateerdeTable extends GbaTable {

  @Override
  public void setColumns() {

    setMultiSelect(true);
    setSelectable(true);

    addColumn("Nr", 30);
    addColumn("Naam", 400).setUseHTML(true);
    addColumn("Type");
    addColumn("&nbsp;", 70).setClassType(Component.class);
    addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

    super.setColumns();
  }

  public abstract List<DossierPersoon> getPersonen();

  @Override
  public void setRecords() {
    List<DossierPersoon> list = new ArrayList<>(getPersonen());
    int nr = list.size();
    int index = 0;
    for (DossierPersoon person : list) {
      String naam = BsPersoonUtils.getNaam(person);
      String type = person.getDossierPersoonType().getDescr();

      if (emp(naam)) {
        naam = setClass("grey", "Dubbelklik om de gegevens in te voeren");
      }

      Record r = addRecord(person);
      r.addValue(nr--);
      r.addValue(naam);

      boolean hasLeeftijd = getColumns().stream().anyMatch(c -> c.getCaption().equalsIgnoreCase("leeftijd"));
      boolean hasNav = getColumns().stream().anyMatch(c -> c.getCaption().equalsIgnoreCase("&nbsp;"));
      long count = list.stream().filter(p -> p.getDossierPersoonType() == person.getDossierPersoonType()).count();

      if (hasLeeftijd) {
        if (pos(person.getDatumGeboorte().getValue())) {
          r.addValue(astr(person.getLeeftijd()));
        } else {
          r.addValue("-");
        }
      }

      r.addValue(type);

      if (hasNav) {
        if (count > 1 && getApplication() != null) {
          DossierService dossierService = getApplication().getServices().getDossierService();
          r.addValue(new PageBsGerelateerdeNavLayout(dossierService, this, person, list, index++));

        } else {
          Label navLabel = new Label("");
          navLabel.setHeight("25px");
          r.addValue(navLabel);
        }
      }
    }

    super.setRecords();
  }
}
