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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.afstamming.naamskeuze;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.MOEDER;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.PARTNER;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.GbaRestDossierHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAktePersoon;
import nl.procura.gba.web.services.bs.lv.LatereVermelding;
import nl.procura.gba.web.services.bs.lv.naamskeuze.LvNaamskeuze;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.services.bs.naamskeuze.NaamskeuzeType;

public class GbaRestNaamskeuzeHandler extends GbaRestDossierHandler {

  public GbaRestNaamskeuzeHandler(Services services) {
    super(services);
  }

  protected void addAlgemeneElementen(GbaRestElement parent, Dossier dossier) {

    DossierNaamskeuze naamskeuze = (DossierNaamskeuze) dossier.getZaakDossier();
    NaamskeuzeType naamskeuzeType = naamskeuze.getDossierNaamskeuzeType();

    add(parent, GbaRestElementType.TYPE_NAAMSKEUZE, naamskeuzeType.getCode(), naamskeuzeType.getOms());
    add(parent, VOORVOEGSEL, naamskeuze.getKeuzeVoorvoegsel());
    add(parent, GESLACHTSNAAM, naamskeuze.getKeuzeGeslachtsnaam());
    add(parent, TITEL_PREDIKAAT, naamskeuze.getKeuzeTitel());

    addNationaliteiten(parent, dossier, dossier.getDatumIngang());
    addPersonen(parent, dossier);
    addLatereVermelding(parent, naamskeuze);
  }

  @Override
  protected void addDossierElementen(GbaRestElement dossierElement, Dossier zaak) {
    GbaRestElement naamskeuze = dossierElement.add(NAAMSKEUZE);
    addAlgemeneElementen(naamskeuze, zaak);
  }

  private void addLatereVermelding(GbaRestElement parent, DossierNaamskeuze naamskeuze) {

    if (naamskeuze.isSprakeLatereVermelding()) {

      LatereVermelding<LvNaamskeuze> lv = naamskeuze.getLatereVermelding();
      GbaRestElement lvElement = parent.add(LATERE_VERMELDING);
      GbaRestElement aktesElement = lvElement.add(AKTES);

      for (LvNaamskeuze akte : lv.getAktes()) {
        GbaRestElement akteElement = aktesElement.add(AKTE);
        GbaRestElement geboorteElement = akteElement.add(GEBOORTE);

        add(geboorteElement, AKTE_JAAR, akte.getGeboorteAkte().getJaar());
        add(geboorteElement, AKTE_PLAATS, akte.getGeboorteAkte().getPlaats());
        add(geboorteElement, AKTE_NUMMER, akte.getGeboorteAkte().getNummer());
        add(geboorteElement, AKTE_BRP_NUMMER, akte.getGeboorteAkte().getBrpNummer());

        GbaRestElement naamskeuzeElement = akteElement.add(NAAMSKEUZE);
        NaamskeuzeType naamskeuzeType = naamskeuze.getDossierNaamskeuzeType();
        add(naamskeuzeElement, TYPE_NAAMSKEUZE, naamskeuzeType.getCode(), naamskeuzeType.getOms());
        add(naamskeuzeElement, AKTE_JAAR, astr(akte.getAkte().getJaar()));
        add(naamskeuzeElement, AKTE_PLAATS, "");
        add(naamskeuzeElement, AKTE_NUMMER, akte.getAkte().getAkte());
        add(naamskeuzeElement, AKTE_BRP_NUMMER, akte.getAkte().getBrpAkte());
        add(naamskeuzeElement, DATUM_AKTE, akte.getAkte().getDatumIngang());

        GbaRestElement personenElement = naamskeuzeElement.add(PERSONEN);
        GbaRestElement persoonElement = personenElement.add(PERSOON);

        DossierAktePersoon p = akte.getAkte().getAktePersoon();
        add(persoonElement, SOORT, GbaRestElementType.KIND);
        add(persoonElement, BSN, p.getBurgerServiceNummer());
        add(persoonElement, GESLACHTSNAAM, akte.getGeslachtsnaam());
        add(persoonElement, VOORVOEGSEL, akte.getVoorvoegsel());
        add(persoonElement, VOORNAMEN, p.getVoornaam());
        add(persoonElement, GESLACHTSAANDUIDING, p.getGeslacht());
        add(persoonElement.add(GEBOORTE), DATUM_GEBOORTE, p.getGeboortedatum());

        addPersoon(personenElement, getSoort(MOEDER), akte.getMoeder());
        addPersoon(personenElement, getSoort(PARTNER), akte.getPartner());
      }
    }
  }
}
