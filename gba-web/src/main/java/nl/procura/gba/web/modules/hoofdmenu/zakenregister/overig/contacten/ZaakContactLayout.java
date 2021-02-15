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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.contacten;

import java.util.List;

import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.modules.zaken.contact.ContactTabel;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.contact.ContactZaak;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoon;
import nl.procura.vaadin.component.layout.Fieldset;

public class ZaakContactLayout extends GbaVerticalLayout {

  public ZaakContactLayout(Zaak zaak) {

    GbaTabsheet tabsheet = new GbaTabsheet();

    if (zaak instanceof ContactZaak) {

      // Als een zaak de interface ContactZaak implementeerd

      ContactZaak contactZaak = (ContactZaak) zaak;

      List<ZaakContactpersoon> personen = contactZaak.getContact().getPersonen();

      if (!personen.isEmpty()) {

        for (ZaakContactpersoon persoon : personen) {

          tabsheet.addTab(new ContactTabel(persoon), persoon.getType().getDescr());
        }

        addComponent(new Fieldset("Contactgegevens", tabsheet));
      }
    } else {

      // Als een zaak NIET de interface ContactZaak implementeerd

      tabsheet.addTab(new ContactTabel(zaak), "Aangever");

      addComponent(new Fieldset("Contactgegevens", tabsheet));
    }
  }
}
