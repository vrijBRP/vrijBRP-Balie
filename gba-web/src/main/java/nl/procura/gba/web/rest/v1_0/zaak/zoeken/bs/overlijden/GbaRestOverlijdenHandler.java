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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.overlijden;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.OVERLIJDEN;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.*;

import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.GbaRestDossierHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijden;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijdenLijkbezorging;

public class GbaRestOverlijdenHandler extends GbaRestDossierHandler {

  public GbaRestOverlijdenHandler(Services services) {
    super(services);
  }

  protected void addAlgemeneElementen(GbaRestElement parent, Dossier dossier) {

    if (dossier.getZaakDossier() instanceof DossierOverlijden) {

      DossierOverlijden dossierOverlijden = (DossierOverlijden) dossier.getZaakDossier();

      GbaRestElement personen = parent.add(GbaRestElementType.PERSONEN);
      // Aangever
      addPersoon(personen, getSoort(AANGEVER), dossierOverlijden.getAangever());

      // Overledene
      GbaRestElement overledene = addPersoon(personen, getSoort(OVERLEDENE), dossierOverlijden.getOverledene());
      addPersonen(overledene, dossierOverlijden.getOverledene());
      addPersoon(personen, getSoort(PARTNER), dossierOverlijden.getGehuwdePartner());
      addPersoon(personen, getSoort(PARTNER), dossierOverlijden.getGeregistreerdePartner());

      // dossierOverlijden.getHuidigePartners ();
      // dossierOverlijden.getDatumOverlijden ();
      // dossierOverlijden.getOntvangenDocument ();
      // dossierOverlijden.getWijzeLijkBezorging ();
    }
  }

  @Override
  protected void addDossierElementen(GbaRestElement dossierElement, Dossier zaak) {

    GbaRestElement overlijden = dossierElement.add(OVERLIJDEN);
    addAlgemeneElementen(overlijden, zaak);
    // addBuitenBeneluxElementen (overlijden, zaak);
  }

  @SuppressWarnings("unused")
  private void addBuitenBeneluxElementen(GbaRestElement parent, Dossier dossier) {

    if (dossier instanceof DossierOverlijdenLijkbezorging) {

      DossierOverlijdenLijkbezorging dossierOverlijden = (DossierOverlijdenLijkbezorging) dossier.getZaakDossier();

      // dossierOverlijden.isBuitenBenelux ();
      // dossierOverlijden.getLandBestemming ();
      // dossierOverlijden.getPlaatsBestemming ();
      // dossierOverlijden.getBestemming ();
      // dossierOverlijden.getViaBestemming ();
      // dossierOverlijden.getVervoermiddel ();
      // dossierOverlijden.getBuitenBeneluxTekst ();
    }
  }
}
