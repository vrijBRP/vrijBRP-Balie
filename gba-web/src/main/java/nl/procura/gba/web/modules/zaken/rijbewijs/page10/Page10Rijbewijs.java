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

package nl.procura.gba.web.modules.zaken.rijbewijs.page10;

import nl.procura.gba.web.modules.zaken.rijbewijs.RijbewijsPage;
import nl.procura.rdw.messages.P1651;
import nl.procura.rdw.messages.P1652;
import nl.procura.rdw.processen.p1652.f02.NATPOPNAAM;
import nl.procura.rdw.processen.p1652.f02.PERSADRESGEG;

/**
 * Meerdere personen
 */

public class Page10Rijbewijs extends RijbewijsPage {

  public Page10Rijbewijs(P1652 p1652) {

    super("Rijbewijsaanvraag: tonen geselecteerde personen");

    setMargin(true);

    addButton(buttonPrev);

    setInfo("Identificatie",
        "De aanvraag bij het CRB heeft meerdere personen opgeleverd. <br/>"
            + "De ambtenaar moet bepalen welke van de onderstaande personen overeenkomt met de aanvrager.");

    addComponent(new Page10RijbewijsTable1((NATPOPNAAM) p1652.getResponse().getObject()) {

      @Override
      public void onClick(Record record) {

        PERSADRESGEG gegevens = (PERSADRESGEG) record.getObject();

        P1651 p1651 = new P1651();

        p1651.newF2(gegevens.getNatpersoongeg().getNatperssl());

        if (sendMessage(p1651)) {

          // 1 iemand gevonden. Door naar de aanvraag.

          goTo(p1651);
        }

        super.onClick(record);
      }
    });
  }
}
