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

package nl.procura.gba.web.modules.hoofdmenu.gv.page2;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.hoofdmenu.gv.overzicht.GvOverzichtLayoutBean.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.modules.hoofdmenu.gv.overzicht.GvOverzichtLayoutBean;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page2GvForm1 extends ReadOnlyForm {

  public Page2GvForm1(DossierPersoon persoon) {

    setCaption("Betrokkene");

    setColumnWidths(WIDTH_130, "", "130px", "150px");

    setOrder(NAAM, GESLACHT, ADRES_PERSOON, GEBOORTE, STATUS);

    GvOverzichtLayoutBean b = new GvOverzichtLayoutBean();

    b.setNaam(persoon.getNaam().getNaam_naamgebruik_eerste_voornaam());
    b.setGeslacht(persoon.getGeslacht().getNormaal());
    b.setGeboorte(persoon.getGeboorte().getDatum_leeftijd());
    b.setAdresPersoon(persoon.getAdres().getAdres_pc_wpl());
    b.setStatus(persoon.isVerstrekkingsbeperking() ? setClass(false, "Verstrekkingsbeperking") : "");

    setBean(b);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(STATUS)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }
}
