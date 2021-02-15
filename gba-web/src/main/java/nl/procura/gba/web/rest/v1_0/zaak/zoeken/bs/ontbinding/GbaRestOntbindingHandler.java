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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.ontbinding;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.PARTNER;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.GbaRestDossierHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.gba.web.services.bs.lv.LatereVermelding;
import nl.procura.gba.web.services.bs.lv.ontbinding.LvOntbinding;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;
import nl.procura.standard.ProcuraDate;

public class GbaRestOntbindingHandler extends GbaRestDossierHandler {

  public GbaRestOntbindingHandler(Services services) {
    super(services);
  }

  @Override
  protected void addDossierElementen(GbaRestElement dossierElement, Dossier zaak) {

    GbaRestElement ontbinding = dossierElement.add(ONTBINDING);
    addVerbintenisElementen(ontbinding, zaak);
    addAlgemeneElementen(ontbinding, zaak);
    addNaamgebruik(ontbinding, zaak);
  }

  private void addAlgemeneElementen(GbaRestElement parent, Dossier dossier) {

    DossierOntbinding dossierOntbinding = (DossierOntbinding) dossier.getZaakDossier();

    add(parent, GEMEENTE, dossierOntbinding.getOntbindingsGemeente());

    GbaRestElement personen = parent.add(GbaRestElementType.PERSONEN);
    GbaRestElement partner1 = addPersoon(personen, getSoort(PARTNER), dossierOntbinding.getPartner1());
    GbaRestElement partner2 = addPersoon(personen, getSoort(PARTNER), dossierOntbinding.getPartner2());

    addPersonen(partner1, dossierOntbinding.getPartner1());
    addPersonen(partner2, dossierOntbinding.getPartner2());
    addLatereVermelding(parent, dossierOntbinding);
  }

  private void addLatereVermelding(GbaRestElement parent, DossierOntbinding ontbinding) {

    if (ontbinding.isSprakeLatereVermelding()) {

      LatereVermelding<LvOntbinding> lv = ontbinding.getLatereVermelding();

      GbaRestElement lvElement = parent.add(LATERE_VERMELDING);
      GbaRestElement aktesElement = lvElement.add(AKTES);

      for (LvOntbinding akte : lv.getAktes()) {

        GbaRestElement akteElement = aktesElement.add(AKTE);
        GbaRestElement verbintenisElement = akteElement.add(VERBINTENIS);
        add(verbintenisElement, AKTE_JAAR, astr(akte.getRechtsfeitAkte().getJaar()));
        add(verbintenisElement, AKTE_PLAATS, astr(akte.getRechtsfeitAkte().getPlaats()));
        add(verbintenisElement, AKTE_NUMMER, akte.getRechtsfeitAkte().getNummer());
        add(verbintenisElement, AKTE_BRP_NUMMER, akte.getRechtsfeitAkte().getBrpNummer());

        GbaRestElement ontbindingElement = akteElement.add(ONTBINDING);
        add(ontbindingElement, AKTE_JAAR,
            new ProcuraDate(ontbinding.getDatumIngang().getStringDate()).getYear());
        add(ontbindingElement, AKTE_PLAATS, "");
        add(ontbindingElement, AKTE_NUMMER, ""); // Geen nieuwe aktenummer
        add(ontbindingElement, AKTE_BRP_NUMMER, ""); // Geen nieuwe aktenummer
        add(ontbindingElement, DATUM_AKTE, ontbinding.getDatumIngang());

        GbaRestElement personenElement = akteElement.add(PERSONEN);
        addPersoon(personenElement, getSoort(PARTNER), akte.getPartner1());
        addPersoon(personenElement, getSoort(PARTNER), akte.getPartner2());
      }
    }
  }

  private void addNaamgebruik(GbaRestElement parent, Dossier dossier) {

    DossierOntbinding huwelijk = (DossierOntbinding) dossier.getZaakDossier();
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

  private void addVerbintenisElementen(GbaRestElement parent, Dossier zaak) {

    GbaRestElement verbintenisElement = parent.add(GbaRestElementType.VERBINTENIS);
    DossierOntbinding dossierOntbinding = (DossierOntbinding) zaak.getZaakDossier();

    SoortVerbintenis soortVerbintenis = dossierOntbinding.getSoortVerbintenis();
    add(verbintenisElement, SOORT, soortVerbintenis.getCode(), soortVerbintenis.getOms());
    add(verbintenisElement, DATUM_VERBINTENIS, dossierOntbinding.getDatumVerbintenis());
    add(verbintenisElement, LAND, dossierOntbinding.getLandVerbintenis());
    add(verbintenisElement, PLAATS, dossierOntbinding.getPlaatsVerbintenis());

    GbaRestElement akteElement = verbintenisElement.add(GbaRestElementType.AKTE);
    add(akteElement, DATUM_FEIT, dossierOntbinding.getDatumIngang());
    add(akteElement, AKTE_NUMMER, dossierOntbinding.getBsAkteNummerVerbintenis());
    add(akteElement, AKTE_BRP_NUMMER, dossierOntbinding.getBrpAkteNummerVerbintenis());
    add(akteElement, AKTE_PLAATS, dossierOntbinding.getAktePlaatsVerbintenis());
    add(akteElement, AKTE_JAAR, dossierOntbinding.getAkteJaarVerbintenis());
    add(akteElement, AKTE_PLAATS, dossierOntbinding.getAktePlaatsVerbintenis());
  }
}
