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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.afstamming.geboorte;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.ERKENNER;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.MOEDER;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.GbaRestDossierHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAktePersoon;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.erkenning.ErkenningsType;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.lv.LatereVermelding;
import nl.procura.gba.web.services.bs.lv.erkenning.LvErkenning;
import nl.procura.gba.web.services.bs.lv.naamskeuze.LvNaamskeuze;
import nl.procura.gba.web.services.bs.naamskeuze.NaamskeuzeType;

public class GbaRestGeboorteHandler extends GbaRestDossierHandler {

  public GbaRestGeboorteHandler(Services services) {
    super(services);
  }

  protected void addAlgemeneElementen(GbaRestElement parent, Dossier dossier) {

    addNationaliteiten(parent, dossier, null); // Geen datum_verkrijging, want kinderen kunnen verschillende
    // geboortedatums krijgen.
    addPersonen(parent, dossier);
    addErkenning(parent, dossier);
    addNaamskeuze(parent, dossier);
  }

  @Override
  protected void addDossierElementen(GbaRestElement dossierElement, Dossier zaak) {
    GbaRestElement geboorte = dossierElement.add(GEBOORTE);
    addAlgemeneElementen(geboorte, zaak);
  }

  private void addErkenning(GbaRestElement parent, Dossier dossier) {

    DossierGeboorte geboorte = (DossierGeboorte) dossier.getZaakDossier();

    if (geboorte.getVragen().heeftErkenningBijGeboorte()) {

      GbaRestElement erkenningElement = parent.add(ERKENNING);

      add(erkenningElement, TYPE_ERKENNING, geboorte.getErkenningsType().getCode(),
          geboorte.getErkenningsType().getOms());

      // Alleen erkenning bij geboorte moet worden verwerkt.
      // Alle andere erkenningstype zijn al verwerkt.
      DossierErkenning erkenning = geboorte.getErkenningBijGeboorte();
      add(erkenningElement, ZAAKID, geboorte.getErkenningBijGeboorte().getDossier().getZaakId());
      add(erkenningElement, DATUM_INGANG, erkenning.getDossier().getDatumIngang());
      add(erkenningElement, VOORVOEGSEL, erkenning.getKeuzeVoorvoegsel());
      add(erkenningElement, GESLACHTSNAAM, erkenning.getKeuzeGeslachtsnaam());
      add(erkenningElement, TITEL_PREDIKAAT, erkenning.getKeuzeTitel());

      addPersonen(erkenningElement, erkenning.getDossier());
      addNationaliteiten(erkenningElement, erkenning.getDossier(), erkenning.getDossier().getDatumIngang());
      addAktes(erkenningElement, erkenning.getDossier());
    }

    addLatereVermeldingErkenning(parent, geboorte);
  }

  private void addNaamskeuze(GbaRestElement parent, Dossier dossier) {
    DossierGeboorte geboorte = (DossierGeboorte) dossier.getZaakDossier();
    addLatereVermeldingNaamskeuze(parent, geboorte);
  }

  private void addLatereVermeldingErkenning(GbaRestElement parent, DossierGeboorte geboorte) {

    if (geboorte.isSprakeLatereVermeldingErkenning()) {
      LatereVermelding<LvErkenning> lv = geboorte.getLatereVermelding();
      GbaRestElement lvElement = parent.add(LATERE_VERMELDING);
      GbaRestElement aktesElement = lvElement.add(AKTES);

      for (LvErkenning akte : lv.getAktes()) {
        GbaRestElement akteElement = aktesElement.add(AKTE);
        GbaRestElement geboorteElement = akteElement.add(GEBOORTE);

        add(geboorteElement, AKTE_JAAR, akte.getGeboorteAkte().getJaar());
        add(geboorteElement, AKTE_PLAATS, akte.getGeboorteAkte().getPlaats());
        add(geboorteElement, AKTE_NUMMER, akte.getGeboorteAkte().getNummer());
        add(geboorteElement, AKTE_BRP_NUMMER, akte.getGeboorteAkte().getBrpNummer());

        GbaRestElement erkElement = akteElement.add(ERKENNING);
        ErkenningsType type = geboorte.getErkenningsType();
        add(erkElement, GbaRestElementType.TYPE_ERKENNING, type.getCode(), type.getOms());
        add(erkElement, AKTE_JAAR, astr(akte.getAkte().getJaar()));
        add(erkElement, AKTE_PLAATS, "");
        add(erkElement, AKTE_NUMMER, akte.getAkte().getAkte());
        add(erkElement, AKTE_BRP_NUMMER, akte.getAkte().getBrpAkte());
        add(erkElement, DATUM_AKTE, akte.getAkte().getDatumIngang());

        GbaRestElement personenElement = erkElement.add(PERSONEN);
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
        addPersoon(personenElement, getSoort(ERKENNER), akte.getErkenner());
      }
    }
  }

  private void addLatereVermeldingNaamskeuze(GbaRestElement parent, DossierGeboorte geboorte) {

    if (geboorte.isSprakeLatereVermeldingNaamskeuze()) {
      LatereVermelding<LvNaamskeuze> lv = geboorte.getLatereVermelding();
      GbaRestElement lvElement = parent.add(LATERE_VERMELDING);
      GbaRestElement aktesElement = lvElement.add(AKTES);

      for (LvNaamskeuze akte : lv.getAktes()) {
        GbaRestElement akteElement = aktesElement.add(AKTE);
        GbaRestElement geboorteElement = akteElement.add(GEBOORTE);

        add(geboorteElement, AKTE_JAAR, akte.getGeboorteAkte().getJaar());
        add(geboorteElement, AKTE_PLAATS, akte.getGeboorteAkte().getPlaats());
        add(geboorteElement, AKTE_NUMMER, akte.getGeboorteAkte().getNummer());
        add(geboorteElement, AKTE_BRP_NUMMER, akte.getGeboorteAkte().getBrpNummer());

        GbaRestElement nkElement = akteElement.add(NAAMSKEUZE);
        NaamskeuzeType type = geboorte.getNaamskeuzeSoort();
        add(nkElement, TYPE_NAAMSKEUZE, type.getCode(), type.getOms());
        add(nkElement, AKTE_JAAR, astr(akte.getAkte().getJaar()));
        add(nkElement, AKTE_PLAATS, "");
        add(nkElement, AKTE_NUMMER, akte.getAkte().getAkte());
        add(nkElement, AKTE_BRP_NUMMER, akte.getAkte().getBrpAkte());
        add(nkElement, DATUM_AKTE, akte.getAkte().getDatumIngang());

        GbaRestElement personenElement = nkElement.add(PERSONEN);
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
        addPersoon(personenElement, getSoort(DossierPersoonType.PARTNER), akte.getPartner());
      }
    }
  }
}
