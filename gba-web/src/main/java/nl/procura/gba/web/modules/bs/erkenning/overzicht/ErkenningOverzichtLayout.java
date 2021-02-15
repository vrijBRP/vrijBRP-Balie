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

package nl.procura.gba.web.modules.bs.erkenning.overzicht;

import static nl.procura.gba.web.modules.bs.erkenning.overzicht.binnen.ErkenningOverzichtBean2.*;
import static nl.procura.standard.Globalfunctions.pos;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.bs.common.pages.aktepage.page1.BsAkteTable;
import nl.procura.gba.web.modules.bs.common.pages.nationaliteitpage.BsNatioTable;
import nl.procura.gba.web.modules.bs.erkenning.overzicht.binnen.ErkenningOverzichtForm2;
import nl.procura.gba.web.modules.bs.erkenning.overzicht.buiten.ErkenningOverzichtForm1;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ErkenningBuitenProweb;
import nl.procura.vaadin.component.layout.Fieldset;

public class ErkenningOverzichtLayout extends VerticalLayout {

  private DossierErkenning erkenning;

  public ErkenningOverzichtLayout(final DossierErkenning erkenning) {

    this.erkenning = erkenning;

    setSpacing(true);

    Table1 table1 = new Table1();
    Table2 table2 = new Table2();
    Table3 table3 = new Table3();

    ErkenningOverzichtForm2 form2 = new ErkenningOverzichtForm2(erkenning) {

      @Override
      public void setCaptionAndOrder() {

        setCaption("Moeder");
        setOrder(NAAM_MOEDER, GEBOREN_MOEDER);
      }
    };

    ErkenningOverzichtForm2 form3 = new ErkenningOverzichtForm2(erkenning) {

      @Override
      public void setCaptionAndOrder() {

        setCaption("Erkenner");
        setOrder(NAAM_ERKENNER, GEBOREN_ERKENNER);
      }
    };

    ErkenningOverzichtForm2 form4 = new ErkenningOverzichtForm2(erkenning) {

      @Override
      public void setCaptionAndOrder() {

        setCaption("Afstamming");
        setOrder(AFSTAMMINGS_RECHT);
        setColumnWidths("180px", "");
      }
    };

    ErkenningOverzichtForm2 form5 = new ErkenningOverzichtForm2(erkenning) {

      @Override
      public void setCaptionAndOrder() {

        setCaption("Toestemming");
        setColumnWidths("180px", "");
        setOrder(TOESTEMMING_GEVER, TOESTEMMING_RECHT_MOEDER, TOESTEMMING_RECHT_KIND);
      }
    };

    ErkenningOverzichtForm2 form6 = new ErkenningOverzichtForm2(erkenning) {

      @Override
      public void setCaptionAndOrder() {

        setCaption("Namenrecht");
        setColumnWidths("180px", "250px", "120px", "");
        setOrder(KEUZE_GESLACHTSNAAM, NAMENRECHT, KEUZE_VOORV, NAAMSKEUZE, KEUZE_TITEL, NAAMSKEUZE_PERSOON);
      }
    };

    addComponent(new Fieldset("Erkenning - details", table2));

    if (erkenning.isBestaandKind()) {
      addComponent(table1);
    }

    addComponent(new Fieldset("Nationaliteit(en) als gevolg van de erkenning", table3));

    addComponent(form2);
    addComponent(form3);
    addComponent(form4);
    addComponent(form5);
    addComponent(form6);

    table1.focus();
  }

  public ErkenningOverzichtLayout(final ErkenningBuitenProweb erkenningBuitenProweb) {

    setSpacing(true);

    addComponent(new Fieldset("Erkenning - details", new ErkenningOverzichtForm1(erkenningBuitenProweb)));
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

      for (DossierPersoon kind : erkenning.getKinderen()) {

        Record r = addRecord(kind);

        nr++;

        r.addValue(nr);
        r.addValue(kind.getNaam().getPred_adel_voorv_gesl_voorn());

        if (pos(kind.getDatumGeboorte().getLongDate())) {

          r.addValue(kind.getDatumGeboorte().getFormatDate());
          r.addValue(kind.getGeslacht());
        } else {
          r.addValue("-");
          r.addValue("-");
        }

        r.addValue(kind.getNaamskeuzeType().getType());
      }

      super.setRecords();
    }
  }

  class Table2 extends BsAkteTable {

    public Table2() {
      super(erkenning.getDossier());
    }

    @Override
    public void setColumns() {
      super.setColumns();
      setSelectable(false);
    }
  }

  class Table3 extends BsNatioTable {

    public Table3() {
      super(erkenning.getDossier());
    }
  }
}
