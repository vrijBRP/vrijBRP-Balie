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

package nl.procura.gba.web.modules.bs.naamskeuze.page5;

import static ch.lambdaj.Lambda.*;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.*;
import static org.hamcrest.Matchers.equalTo;

import java.util.Collection;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Embedded;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class Page5NaamskeuzeTable1 extends GbaTable {

  private final DossierNaamskeuze naamskeuze;

  public Page5NaamskeuzeTable1(DossierNaamskeuze naamskeuze) {
    this.naamskeuze = naamskeuze;
  }

  public void addDossierPersonen(Collection<DossierPersoon> personen) {
    clear();
    for (DossierPersoon persoon : personen) {
      if (pos(persoon.getDatumGeboorte().getLongDate())) {
        Record record = addRecord(persoon);

        if (isMetCheckboxes()) {
          record.addValue(getCheckbox(persoon));
        }

        record.addValue(persoon.getNaam().getPred_adel_voorv_gesl_voorn());
        record.addValue(persoon.getLand());
        record.addValue(persoon.getDatumGeboorte().getFormatDate() + " (" + persoon.getLeeftijd() + ")");
        record.addValue(persoon.getGeslacht().getNormaal());

        if (!isMetCheckboxes()) {
          record.addValue(getAkte(persoon));
        }
      }
    }

    reloadRecords();
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

    if (isMetCheckboxes()) {
      addColumn("&nbsp;", 20).setClassType(Embedded.class);
    }

    addColumn("Naam");
    addColumn("Woonland");
    addColumn("Geboren");
    addColumn("Geslacht");

    if (!isMetCheckboxes()) {
      addColumn("Geboorteakte", 400).setUseHTML(true);
    }

    super.setColumns();
  }

  protected boolean isMetCheckboxes() {
    return false;
  }

  private String getAkte(DossierPersoon persoon) {
    StringBuilder sb = new StringBuilder()
        .append(astr(persoon.getGeboorteAkteNummer()))
        .append(" ")
        .append(trim("/ " + persoon.getGeboorteAkteBrpNummer()))
        .append(", ")
        .append(pos(persoon.getGeboorteAkteJaar()) ? persoon.getGeboorteAkteJaar() : "")
        .append(", ")
        .append(astr(persoon.getGeboorteAktePlaats()));

    String akte = trim(sb.toString());
    return fil(akte) ? akte : setClass(false, "Onbekend");
  }

  private Embedded getCheckbox(DossierPersoon persoon) {
    String icon = GbaWebTheme.ICOON_16.CHECKBOX_NO;
    BsnFieldValue persoonBsn = persoon.getBurgerServiceNummer();
    BsnFieldValue kindBsn = on(DossierPersoon.class).getBurgerServiceNummer();

    if (exists(naamskeuze.getKinderen(), having(kindBsn, equalTo(persoonBsn)))) {
      icon = GbaWebTheme.ICOON_16.CHECKBOX_YES;
    }

    return new Embedded(null, new ThemeResource(icon));
  }
}
