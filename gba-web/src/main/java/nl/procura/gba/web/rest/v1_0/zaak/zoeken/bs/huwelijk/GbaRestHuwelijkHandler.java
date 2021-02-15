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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.huwelijk;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.PARTNER;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.GbaRestDossierHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatie;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatieSoort;

public class GbaRestHuwelijkHandler extends GbaRestDossierHandler {

  public GbaRestHuwelijkHandler(Services services) {
    super(services);
  }

  protected void addAlgemeneElementen(GbaRestElement parent, Dossier dossier) {

    if (dossier.getZaakDossier() instanceof DossierHuwelijk) {

      DossierHuwelijk dossierHuwelijk = (DossierHuwelijk) dossier.getZaakDossier();

      GbaRestElement personen = parent.add(GbaRestElementType.PERSONEN);
      GbaRestElement partner1 = addPersoon(personen, getSoort(PARTNER), dossierHuwelijk.getPartner1());
      GbaRestElement partner2 = addPersoon(personen, getSoort(PARTNER), dossierHuwelijk.getPartner2());

      addPersonen(partner1, dossierHuwelijk.getPartner1());
      addPersonen(partner2, dossierHuwelijk.getPartner2());
    }
  }

  @Override
  protected void addDossierElementen(GbaRestElement dossierElement, Dossier zaak) {

    GbaRestElement huwelijk = dossierElement.add(HUWELIJK);

    addAlgemeneElementen(huwelijk, zaak);
    addPlanning(huwelijk, zaak);
    addNaamgebruik(huwelijk, zaak);
  }

  private void addNaamgebruik(GbaRestElement parent, Dossier dossier) {

    DossierHuwelijk huwelijk = (DossierHuwelijk) dossier.getZaakDossier();
    GbaRestElement naamgElement = parent.add(NAAMGEBRUIK);
    GbaRestElement personenElement = naamgElement.add(PERSONEN);

    addNaamgebruik(personenElement, huwelijk.getTitelPartner1().getStringValue(), huwelijk.getVoorvPartner1(),
        huwelijk.getNaamPartner1(), huwelijk.getNaamGebruikPartner1());
    addNaamgebruik(personenElement, huwelijk.getTitelPartner2().getStringValue(), huwelijk.getVoorvPartner2(),
        huwelijk.getNaamPartner2(), huwelijk.getNaamGebruikPartner2());
  }

  private void addNaamgebruik(GbaRestElement personenElement, String titel, String voorv, String naam,
      String naamgebruik) {
    GbaRestElement persoonElement = personenElement.add(PERSOON);
    add(persoonElement, TITEL_PREDIKAAT, titel);
    add(persoonElement, VOORVOEGSEL, voorv);
    add(persoonElement, GESLACHTSNAAM, naam);
    add(persoonElement, NAAMGEBRUIK, naamgebruik);
  }

  private void addPlanning(GbaRestElement parent, Dossier dossier) {

    DossierHuwelijk huwelijk = (DossierHuwelijk) dossier.getZaakDossier();
    GbaRestElement planningElement = parent.add(PLANNING);

    SoortVerbintenis soortVerbintenis = huwelijk.getSoortVerbintenis();
    DateTime tijdVerbintenis = huwelijk.getTijdVerbintenis();

    add(planningElement, SOORT, soortVerbintenis.getCode(), soortVerbintenis.getOms());
    add(planningElement, DATUM_VOORNEMEN, huwelijk.getDatumVoornemen());
    add(planningElement, DATUM_VERBINTENIS, huwelijk.getDatumVerbintenis());
    add(planningElement, TIJD_VERBINTENIS, tijdVerbintenis.getLongTime(), tijdVerbintenis.getFormatTime("HH:mm"));
    add(planningElement, GEMEENTE, huwelijk.getHuwelijksGemeente());

    GbaRestElement locatieElement = planningElement.add(LOCATIE);
    HuwelijksLocatie huwelijksLocatie = huwelijk.getHuwelijksLocatie();
    add(locatieElement, OMSCHRIJVING, huwelijksLocatie.getHuwelijksLocatie());
    add(locatieElement, TOELICHTING, huwelijksLocatie.getToelichting());

    GbaRestElement soortElement = locatieElement.add(SOORT);
    HuwelijksLocatieSoort locatieSoort = huwelijksLocatie.getLocatieSoort();
    add(soortElement, CODE, locatieSoort.getCode());
    add(soortElement, OMSCHRIJVING, locatieSoort.getOms());
  }
}
