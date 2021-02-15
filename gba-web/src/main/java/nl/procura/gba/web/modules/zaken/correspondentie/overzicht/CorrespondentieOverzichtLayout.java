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

package nl.procura.gba.web.modules.zaken.correspondentie.overzicht;

import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.modules.zaken.correspondentie.page3.Page3CorrespondentieForm;
import nl.procura.gba.web.modules.zaken.document.overzicht.DocumentOverzichtLayout;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZakenService;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatie;
import nl.procura.gba.web.services.zaken.correspondentie.CorrespondentieZaak;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;

public class CorrespondentieOverzichtLayout extends GbaVerticalLayout {

  private final CorrespondentieZaak zaak;
  private boolean                   relatiesToegevoegd = false;

  public CorrespondentieOverzichtLayout(final CorrespondentieZaak zaak) {
    this.zaak = zaak;
    setSpacing(true);
    addComponent(new Page3CorrespondentieForm(zaak));
  }

  @Override
  public void attach() {

    // Voeg gerelateerde zaken toe met hun eigen layouts
    if (!relatiesToegevoegd) {

      ZaakArgumenten args = new ZaakArgumenten();
      for (ZaakRelatie zr : zaak.getZaakHistorie().getRelaties().getRelaties()) {
        args.addZaakKey(new ZaakKey(zr.getGerelateerdZaakId()));
      }

      if (!args.getZaakKeys().isEmpty()) {
        ZakenService db = getApplication().getServices().getZakenService();
        for (Zaak relatieZaak : db.getVolledigeZaken(db.getMinimaleZaken(args))) {
          if (relatieZaak instanceof DocumentZaak) {
            addComponent(new DocumentOverzichtLayout((DocumentZaak) relatieZaak));
          }
        }
      }

      relatiesToegevoegd = true;
    }

    super.attach();
  }
}
