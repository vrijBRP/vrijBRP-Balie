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

package nl.procura.gba.web.modules.bs.overlijden.lijkvinding.overzicht;

import static nl.procura.gba.web.modules.bs.overlijden.lijkvinding.overzicht.LijkvindingOverzichtBean1.*;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.bs.common.pages.aktepage.page1.BsAkteTable;
import nl.procura.gba.web.modules.bs.overlijden.lijkbezorging.LijkbezorgingOverzichtForm;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.overlijden.lijkvinding.DossierLijkvinding;
import nl.procura.vaadin.component.layout.Fieldset;

public class LijkvindingOverzichtLayout extends VerticalLayout {

  private final DossierLijkvinding lijkvinding;

  public LijkvindingOverzichtLayout(final DossierLijkvinding lijkvinding) {

    this.lijkvinding = lijkvinding;

    setSpacing(true);

    addComponent(new Fieldset("Overzicht", new Table1()));
    addComponent(new Table2(lijkvinding.getDossier()));
    addComponent(new Form1());
    addComponent(new Form2());
    addComponent(new Form3());
  }

  public class Form1 extends LijkvindingOverzichtForm1 {

    public Form1() {
      super(lijkvinding);
    }

    @Override
    public void init() {

      setCaption("Aangifte / aangever");

      setOrder(SCHRIFTELIJKEAANGEVER, PLAATSLIJKVINDING, DOCUMENT);
    }
  }

  public class Form2 extends LijkvindingOverzichtForm1 {

    public Form2() {
      super(lijkvinding);
    }

    @Override
    public void init() {

      setCaption("Gerelateerden");
      setOrder(PARTNER, KINDEREN);
      setColumnWidths("170px", "350px", "130px", "");
    }
  }

  public class Form3 extends LijkbezorgingOverzichtForm {

    public Form3() {
      super(lijkvinding);
    }
  }

  class Table1 extends GbaTable {

    @Override
    public void setColumns() {

      addColumn("Naam");
      addColumn("Woonachtig");
      addColumn("Lijkvinding op", 170);
      addColumn("Geslacht", 60);

      super.setColumns();
    }

    @Override
    public void setRecords() {

      DossierPersoon overledene = lijkvinding.getOverledene();

      Record r = addRecord(overledene);

      r.addValue(overledene.getNaam().getPred_adel_voorv_gesl_voorn());
      r.addValue(overledene.getAdres().getAdres_pc_wpl_gem());
      r.addValue(lijkvinding.getDatumLijkvinding() + " om " + lijkvinding.getTijdLijkvindingStandaard());
      r.addValue(overledene.getGeslacht());

      super.setRecords();
    }
  }

  class Table2 extends BsAkteTable {

    public Table2(Dossier dossier) {
      super(dossier);
    }

    @Override
    public void setColumns() {
      super.setColumns();
      setSelectable(false);
    }
  }

}
