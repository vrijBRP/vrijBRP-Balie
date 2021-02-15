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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.omzetting;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.PARTNER;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.GbaRestDossierHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.gba.web.services.bs.lv.LatereVermelding;
import nl.procura.gba.web.services.bs.lv.omzetting.LvOmzetting;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatie;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatieSoort;

public class GbaRestOmzettingHandler extends GbaRestDossierHandler {

  public GbaRestOmzettingHandler(Services services) {
    super(services);
  }

  protected void addAlgemeneElementen(GbaRestElement parent, Dossier dossier) {

    if (dossier.getZaakDossier() instanceof DossierOmzetting) {

      DossierOmzetting dossierOmzetting = (DossierOmzetting) dossier.getZaakDossier();

      GbaRestElement personen = parent.add(GbaRestElementType.PERSONEN);
      GbaRestElement partner1 = addPersoon(personen, getSoort(PARTNER), dossierOmzetting.getPartner1());
      GbaRestElement partner2 = addPersoon(personen, getSoort(PARTNER), dossierOmzetting.getPartner2());

      addPersonen(partner1, dossierOmzetting.getPartner1());
      addPersonen(partner2, dossierOmzetting.getPartner2());
      addLatereVermelding(parent, dossierOmzetting);
    }
  }

  @Override
  protected void addDossierElementen(GbaRestElement dossierElement, Dossier zaak) {

    GbaRestElement huwelijk = dossierElement.add(HUWELIJK);

    addAlgemeneElementen(huwelijk, zaak);
    addPlanning(huwelijk, zaak);
    addNaamgebruik(huwelijk, zaak);

    GbaRestElement gps = dossierElement.add(GbaRestElementType.GPS);
    addGpsElementen(gps, zaak);
  }

  private void addGpsElementen(GbaRestElement parent, Dossier dossier) {

    if (dossier.getZaakDossier() instanceof DossierOmzetting) {

      DossierOmzetting dossierOmzetting = (DossierOmzetting) dossier.getZaakDossier();

      GbaRestElement sluiting = parent.add(GbaRestElementType.VERBINTENIS);
      add(sluiting, GbaRestElementType.DATUM_VERBINTENIS, dossierOmzetting.getDatumPartnerschap());
      add(sluiting, GbaRestElementType.PLAATS, dossierOmzetting.getPlaatsPartnerschap());
      add(sluiting, GbaRestElementType.LAND, dossierOmzetting.getLandPartnerschap());
    }
  }

  private void addLatereVermelding(GbaRestElement parent, DossierOmzetting omzetting) {

    if (omzetting.isSprakeLatereVermelding()) {

      LatereVermelding<LvOmzetting> lv = omzetting.getLatereVermelding();

      GbaRestElement lvElement = parent.add(LATERE_VERMELDING);
      GbaRestElement aktesElement = lvElement.add(AKTES);

      for (LvOmzetting akte : lv.getAktes()) {

        GbaRestElement akteElement = aktesElement.add(AKTE);
        GbaRestElement gpsElement = akteElement.add(GPS);

        add(gpsElement, AKTE_JAAR, akte.getGpsAkte().getJaar());
        add(gpsElement, AKTE_PLAATS, akte.getGpsAkte().getPlaats());
        add(gpsElement, AKTE_NUMMER, akte.getGpsAkte().getNummer());
        add(gpsElement, AKTE_BRP_NUMMER, akte.getGpsAkte().getBrpNummer());

        GbaRestElement erkenningElement = akteElement.add(HUWELIJK);
        add(erkenningElement, AKTE_JAAR, astr(akte.getAkte().getJaar()));
        add(erkenningElement, AKTE_PLAATS, "");
        add(erkenningElement, AKTE_NUMMER, akte.getAkte().getAkte());
        add(erkenningElement, AKTE_BRP_NUMMER, akte.getAkte().getBrpAkte());
        add(erkenningElement, DATUM_AKTE, akte.getAkte().getDatumIngang());

        GbaRestElement personenElement = erkenningElement.add(PERSONEN);
        addPersoon(personenElement, getSoort(PARTNER), akte.getPartner1());
        addPersoon(personenElement, getSoort(PARTNER), akte.getPartner2());
      }
    }
  }

  private void addNaamgebruik(GbaRestElement parent, Dossier dossier) {

    DossierOmzetting huwelijk = (DossierOmzetting) dossier.getZaakDossier();
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

    DossierOmzetting omzetting = (DossierOmzetting) dossier.getZaakDossier();
    GbaRestElement planningElement = parent.add(PLANNING);

    DateTime tijdVerbintenis = omzetting.getTijdVerbintenis();

    add(planningElement, SOORT, SoortVerbintenis.HUWELIJK.getCode(), SoortVerbintenis.HUWELIJK.getOms());
    add(planningElement, DATUM_VERBINTENIS, omzetting.getDatumVerbintenis());
    add(planningElement, TIJD_VERBINTENIS, tijdVerbintenis.getLongTime(), tijdVerbintenis.getFormatTime("HH:mm"));
    add(planningElement, GEMEENTE, omzetting.getHuwelijksGemeente());

    GbaRestElement locatieElement = planningElement.add(LOCATIE);
    HuwelijksLocatie huwelijksLocatie = omzetting.getHuwelijksLocatie();
    add(locatieElement, OMSCHRIJVING, huwelijksLocatie.getHuwelijksLocatie());
    add(locatieElement, TOELICHTING, huwelijksLocatie.getToelichting());

    GbaRestElement soortElement = locatieElement.add(SOORT);
    HuwelijksLocatieSoort locatieSoort = huwelijksLocatie.getLocatieSoort();
    add(soortElement, CODE, locatieSoort.getCode());
    add(soortElement, OMSCHRIJVING, locatieSoort.getOms());
  }
}
