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

package nl.procura.gba.web.modules.bs.naamskeuze.overzicht;

import static nl.procura.gba.web.modules.bs.naamskeuze.overzicht.binnen.NaamskeuzeOverzichtBean2.*;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.modules.bs.common.pages.aktepage.page1.BsAkteTable;
import nl.procura.gba.web.modules.bs.naamskeuze.overzicht.binnen.NaamskeuzeOverzichtForm2;
import nl.procura.gba.web.modules.bs.naamskeuze.overzicht.buiten.NaamskeuzeOverzichtForm1;
import nl.procura.gba.web.services.bs.geboorte.naamskeuzebuitenproweb.NaamskeuzeBuitenProweb;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.vaadin.component.layout.Fieldset;

public class NaamskeuzeOverzichtLayout extends VerticalLayout {

  private DossierNaamskeuze naamskeuze;

  public NaamskeuzeOverzichtLayout(final DossierNaamskeuze naamskeuze) {

    this.naamskeuze = naamskeuze;
    setSpacing(true);
    Table1 table1 = new Table1();

    NaamskeuzeOverzichtForm2 form2 = new NaamskeuzeOverzichtForm2(naamskeuze) {

      @Override
      public void setCaptionAndOrder() {
        setCaption("Moeder");
        setOrder(NAAM_MOEDER, GEBOREN_MOEDER);
      }
    };

    NaamskeuzeOverzichtForm2 form3 = new NaamskeuzeOverzichtForm2(naamskeuze) {

      @Override
      public void setCaptionAndOrder() {
        setCaption("Partner");
        setOrder(NAAM_PARTNER, GEBOREN_PARTNER);
      }
    };

    NaamskeuzeOverzichtForm2 form6 = new NaamskeuzeOverzichtForm2(naamskeuze) {

      @Override
      public void setCaptionAndOrder() {
        setCaption("Namenrecht");
        setColumnWidths("160px", "250px", "120px", "");
        setOrder(KEUZE_GESLACHTSNAAM, NAMENRECHT, KEUZE_VOORV, NAAMSKEUZE, KEUZE_TITEL, NAAMSKEUZE_PERSOON);
      }
    };

    addComponent(new Fieldset("Naamskeuze - details", table1));
    addComponent(form2);
    addComponent(form3);
    addComponent(form6);
  }

  public NaamskeuzeOverzichtLayout(final NaamskeuzeBuitenProweb naamskeuzeBuitenProweb) {
    setSpacing(true);
    NaamskeuzeOverzichtForm1 form1 = new NaamskeuzeOverzichtForm1(naamskeuzeBuitenProweb);
    addComponent(new Fieldset("Naamskeuze - details", form1));
  }

  class Table1 extends BsAkteTable {

    public Table1() {
      super(naamskeuze.getDossier());
    }

    @Override
    public void setColumns() {
      super.setColumns();
      setSelectable(false);
    }
  }
}
