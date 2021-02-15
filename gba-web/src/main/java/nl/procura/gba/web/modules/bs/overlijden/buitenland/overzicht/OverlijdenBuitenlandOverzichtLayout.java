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

package nl.procura.gba.web.modules.bs.overlijden.buitenland.overzicht;

import static nl.procura.gba.web.modules.bs.overlijden.buitenland.overzicht.OverlijdenBuitenlandOverzichtBean1.*;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.services.bs.overlijden.buitenland.DossierOverlijdenBuitenland;

public class OverlijdenBuitenlandOverzichtLayout extends VerticalLayout {

  private final DossierOverlijdenBuitenland overlijding;

  public OverlijdenBuitenlandOverzichtLayout(final DossierOverlijdenBuitenland overlijding) {

    this.overlijding = overlijding;

    setSpacing(true);

    addComponent(new Form1());
    addComponent(new Form2());
  }

  public class Form1 extends OverlijdenBuitenlandOverzichtForm1 {

    public Form1() {
      super(overlijding);
    }

    @Override
    public void init() {

      setCaption("Overlijden");

      setOrder(DATUM);
    }
  }

  public class Form2 extends OverlijdenBuitenlandOverzichtForm1 {

    public Form2() {
      super(overlijding);
    }

    @Override
    public void init() {

      setCaption("Brondocument");

      setOrder(ONTVANGEN_OP, VAN, TYPE, LAND_AFGIFTE, VOLDOET_AANEISEN);
    }
  }
}
