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

package nl.procura.gba.web.modules.hoofdmenu.gv.overzicht;

import static nl.procura.gba.web.modules.hoofdmenu.gv.overzicht.GvOverzichtLayoutBean.*;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.services.zaken.gv.GvAanvraag;
import nl.procura.vaadin.component.layout.Fieldset;

public class GvOverzichtLayout extends VerticalLayout {

  public GvOverzichtLayout(GvAanvraag aanvraag) {

    setSpacing(true);

    addComponent(new Form1(aanvraag));
    addComponent(new Form2(aanvraag));
    addComponent(new Fieldset("Huidige procesinformatie", new Table1(aanvraag)));
  }

  public class Form1 extends GvOverzichtLayoutForm {

    public Form1(GvAanvraag aanvraag) {
      super(aanvraag);
    }

    @Override
    public void init(GvAanvraag aanvraag) {

      setCaption("Verzoek");
      setOrder(DATUM_ONTVANGST, AFNEMER, GRONDSLAG, BA_AFWEGING, TOEKENNING);
      setColumnWidths(WIDTH_130, "");
    }
  }

  public class Form2 extends GvOverzichtLayoutForm {

    public Form2(GvAanvraag aanvraag) {
      super(aanvraag);
    }

    @Override
    public void init(GvAanvraag aanvraag) {
      setCaption("Adressering");
      setOrder(INFORMATIEVRAGER, TAV, ADRES_AANVRAGER, PC, EMAIL, KENMERK);
      setColumnWidths(WIDTH_130, "350px", "100px", "");
    }
  }

  public class Table1 extends GvOverzichtTable {

    public Table1(GvAanvraag aanvraag) {
      super(aanvraag);
    }
  }
}
