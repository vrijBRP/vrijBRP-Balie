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

import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijden;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijdenLijkbezorging;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.ZaakItemRubriek;

/**
 * Maakt samenvatting van gegevens over een specifieke aanvraag voor een overlijden
 */
public abstract class OverlijdenSamenvatting<T extends DossierOverlijden> extends ZaakSamenvattingTemplate<T> {

  public OverlijdenSamenvatting(ZaakSamenvatting zaakSamenvatting, T zaak) {
    super(zaakSamenvatting, zaak);
  }

  @Override
  public void addDeelzaken(DossierOverlijden zaak) {
  }

  protected abstract void setAangifte(T zaak);

  protected void setGerelateerden(DossierOverlijden zaak) {
    DossierPersoon overledene = zaak.getOverledene();
    ZaakItemRubriek rubriek = addRubriek("Gerelateerden");
    rubriek.add("Partner", overledene.getPersonen(DossierPersoonType.PARTNER).isEmpty() ? "Geen" : "Ja");
    rubriek.add("Minderjarige kinderen", overledene.getPersonen(DossierPersoonType.KIND).isEmpty() ? "Geen" : "Ja");
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
