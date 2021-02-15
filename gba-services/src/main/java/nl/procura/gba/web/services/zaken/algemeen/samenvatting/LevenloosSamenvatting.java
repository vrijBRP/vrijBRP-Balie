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

package nl.procura.gba.web.services.zaken.algemeen.samenvatting;

import nl.procura.gba.web.services.bs.levenloos.DossierLevenloos;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijdenLijkbezorging;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.ZaakItemRubriek;

/**
 * Maakt samenvatting van gegevens over een specifieke aanvraag voor een levenloos geboren kind
 */
public class LevenloosSamenvatting extends GeboorteSamenvatting<DossierLevenloos> {

  public LevenloosSamenvatting(ZaakSamenvatting zaakSamenvatting, DossierLevenloos zaak) {
    super(zaakSamenvatting, zaak);
  }

  @Override
  public void addZaakItems(DossierLevenloos zaak) {
    super.addZaakItems(zaak);
    setLijkbezorging(zaak);
  }

  protected void setLijkbezorging(DossierOverlijdenLijkbezorging zaak) {
    ZaakItemRubriek rubriek = addRubriek("Lijkbezorging");
    rubriek.add("Wijze", zaak.getWijzeLijkBezorging().getOms());
    rubriek.add("Op", zaak.getDatumLijkbezorging() + " " + zaak.getTijdLijkbezorgingStandaard());
    rubriek.add("Termijn", zaak.getTermijnLijkbezorging().getOms());
    rubriek.add("Buiten Benelux", zaak.getBuitenBeneluxTekst());
    rubriek.add("Ontvangen document", zaak.getOntvangenDocumentLijkbezorging().getOms());
  }
}
