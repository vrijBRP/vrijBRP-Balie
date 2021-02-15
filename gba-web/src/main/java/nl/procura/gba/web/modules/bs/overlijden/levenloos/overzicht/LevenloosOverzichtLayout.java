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

package nl.procura.gba.web.modules.bs.overlijden.levenloos.overzicht;

import static nl.procura.gba.web.modules.bs.geboorte.page84.Page84GeboorteBean1.*;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.bs.common.pages.aktepage.page1.BsAkteTable;
import nl.procura.gba.web.modules.bs.common.pages.nationaliteitpage.BsNatioTable;
import nl.procura.gba.web.modules.bs.geboorte.page84.Page84GeboorteForm1;
import nl.procura.gba.web.modules.bs.overlijden.lijkbezorging.LijkbezorgingOverzichtForm;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.levenloos.DossierLevenloos;
import nl.procura.vaadin.component.layout.Fieldset;

public class LevenloosOverzichtLayout extends VerticalLayout {

  private final DossierLevenloos levenloos;

  public LevenloosOverzichtLayout(final DossierLevenloos levenloos) {

    this.levenloos = levenloos;

    setSpacing(true);

    Table1 table1 = new Table1();
    Table2 table2 = new Table2(levenloos.getDossier());

    Page84GeboorteForm1 form1 = new Page84GeboorteForm1(levenloos) {

      @Override
      public void setCaptionAndOrder() {

        setCaption("Aangifte / Aangever");
        setOrder(NAAM_AANGEVER, GEBOREN_AANGEVER, REDENVERPLICHT, TARDIEVE_AANGIFTE);
      }
    };

    Page84GeboorteForm1 form2 = new Page84GeboorteForm1(levenloos) {

      @Override
      public void setCaptionAndOrder() {

        setCaption("Moeder");
        setOrder(NAAM_MOEDER, GEBOREN_MOEDER);
      }
    };

    Page84GeboorteForm1 form3 = new Page84GeboorteForm1(levenloos) {

      @Override
      public void setCaptionAndOrder() {

        setCaption("Vader");
        setOrder(NAAM_VADER, GEBOREN_VADER);
      }
    };

    Page84GeboorteForm1 form4 = new Page84GeboorteForm1(levenloos) {

      @Override
      public void setCaptionAndOrder() {

        setCaption("Afstamming");
        setOrder(GEZIN, AFSTAMMINGSRECHT);
      }
    };

    Page84GeboorteForm1 form5 = new Page84GeboorteForm1(levenloos) {

      @Override
      public void setCaptionAndOrder() {

        setCaption("Namenrecht");
        setOrder(NAAMSRECHT);
        setColumnWidths("170px", "");
      }
    };

    LijkbezorgingOverzichtForm form6 = new LijkbezorgingOverzichtForm(levenloos);

    addComponent(new Fieldset("Geboorte - details", table1));
    addComponent(table2);
    addComponent(new Table3());
    addComponent(form1);
    addComponent(form6);
    addComponent(form2);
    addComponent(form3);
    addComponent(form4);
    addComponent(form5);

    table1.focus();
  }

  class Table1 extends GbaTable {

    @Override
    public void setColumns() {

      addColumn("Nr", 40);
      addColumn("Naam");
      addColumn("Geboren op", 170);
      addColumn("Geslacht", 105);
      addColumn("Naamskeuze", 100);

      super.setColumns();
    }

    @Override
    public void setRecords() {

      int nr = 0;

      for (DossierPersoon kind : levenloos.getKinderen()) {

        Record r = addRecord(kind);

        nr++;

        r.addValue(nr);
        r.addValue(kind.getNaam().getPred_adel_voorv_gesl_voorn());
        r.addValue(kind.getDatumGeboorte().getFormatDate() + " om " + kind.getTijdGeboorte().getFormatTime(
            "HH:mm"));
        r.addValue(kind.getGeslacht());
        r.addValue(kind.getNaamskeuzeType().getType());
      }

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

  class Table3 extends BsNatioTable {

    public Table3() {

      super(levenloos.getDossier());
    }
  }
}
