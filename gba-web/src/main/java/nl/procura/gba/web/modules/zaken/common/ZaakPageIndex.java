/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.common;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.gba.web.components.layouts.page.GbaPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page10.Page10Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page100.Page100Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page110.Page110Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page120.Page120Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page130.Page130Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page140.Page140Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page150.Page150Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page170.Page170Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page180.Page180Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page190.Page190Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page20.Page20Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page200.Page200Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page210.Page210Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page220.Page220Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page230.Page230Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page240.Page240Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page250.Page250Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page260.Page260Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page270.Page270Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page280.Page280Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page290.Page290Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page30.Page30Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page300.Page300Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page40.Page40Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page50.Page50Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page60.Page60Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page70.Page70Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page75.Page75Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page80.Page80Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page90.Page90Zaken;
import nl.procura.gba.web.services.beheer.personmutations.PersonListMutation;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.bs.levenloos.DossierLevenloos;
import nl.procura.gba.web.services.bs.lv.afstamming.DossierLv;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatie;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;
import nl.procura.gba.web.services.bs.overlijden.buitenland.DossierOverlijdenBuitenland;
import nl.procura.gba.web.services.bs.overlijden.gemeente.DossierOverlijdenGemeente;
import nl.procura.gba.web.services.bs.overlijden.lijkvinding.DossierLijkvinding;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.correspondentie.CorrespondentieZaak;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;
import nl.procura.gba.web.services.zaken.geheim.GeheimAanvraag;
import nl.procura.gba.web.services.zaken.gemeenteinbox.GemeenteInboxRecord;
import nl.procura.gba.web.services.zaken.gpk.GpkAanvraag;
import nl.procura.gba.web.services.zaken.gv.GvAanvraag;
import nl.procura.gba.web.services.zaken.indicaties.IndicatieAanvraag;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.naamgebruik.NaamgebruikAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;

public class ZaakPageIndex {

  public static GbaPageTemplate selectPage(Zaak zaak) {

    if (zaak instanceof NaamgebruikAanvraag) {
      return new Page10Zaken((NaamgebruikAanvraag) zaak);

    } else if (zaak instanceof IndicatieAanvraag) {
      return new Page170Zaken((IndicatieAanvraag) zaak);

    } else if (zaak instanceof CorrespondentieZaak) {
      return new Page180Zaken((CorrespondentieZaak) zaak);

    } else if (zaak instanceof GeheimAanvraag) {
      return new Page20Zaken((GeheimAanvraag) zaak);

    } else if (zaak instanceof GpkAanvraag) {
      return new Page30Zaken((GpkAanvraag) zaak);

    } else if (zaak instanceof GemeenteInboxRecord) {
      return new Page230Zaken((GemeenteInboxRecord) zaak);

    } else if (zaak instanceof VogAanvraag) {
      return new Page40Zaken((VogAanvraag) zaak);

    } else if (zaak instanceof DocumentZaak) {
      return new Page50Zaken((DocumentZaak) zaak);

    } else if (zaak instanceof VerhuisAanvraag) {
      return new Page60Zaken((VerhuisAanvraag) zaak);

    } else if (zaak instanceof ReisdocumentAanvraag) {
      return new Page70Zaken((ReisdocumentAanvraag) zaak);

    } else if (zaak instanceof DocumentInhouding) {
      return new Page75Zaken((DocumentInhouding) zaak);

    } else if (zaak instanceof RijbewijsAanvraag) {
      return new Page90Zaken((RijbewijsAanvraag) zaak);

    } else if (zaak instanceof TerugmeldingAanvraag) {
      return new Page80Zaken((TerugmeldingAanvraag) zaak);

    } else if (zaak instanceof GvAanvraag) {
      return new Page190Zaken((GvAanvraag) zaak);

    } else if (zaak instanceof PersonListMutation) {
      return new Page270Zaken((PersonListMutation) zaak);

    } else if (zaak instanceof Dossier) {
      Dossier dossier = (Dossier) zaak;

      if (dossier.getZaakDossier() instanceof DossierLevenloos) {
        return new Page200Zaken((Dossier) zaak);

      } else if (dossier.getZaakDossier() instanceof DossierGeboorte) {
        return new Page100Zaken((Dossier) zaak);

      } else if (dossier.getZaakDossier() instanceof DossierHuwelijk) {
        return new Page110Zaken((Dossier) zaak);

      } else if (dossier.getZaakDossier() instanceof DossierOmzetting) {
        return new Page210Zaken((Dossier) zaak);

      } else if (dossier.getZaakDossier() instanceof DossierOntbinding) {
        return new Page220Zaken((Dossier) zaak);

      } else if (dossier.getZaakDossier() instanceof DossierOverlijdenGemeente) {
        return new Page120Zaken((Dossier) zaak);

      } else if (dossier.getZaakDossier() instanceof DossierLijkvinding) {
        return new Page130Zaken((Dossier) zaak);

      } else if (dossier.getZaakDossier() instanceof DossierOverlijdenBuitenland) {
        return new Page140Zaken((Dossier) zaak);

      } else if (dossier.getZaakDossier() instanceof DossierErkenning) {
        return new Page150Zaken((Dossier) zaak);

      } else if (dossier.getZaakDossier() instanceof DossierNaamskeuze) {
        return new Page280Zaken((Dossier) zaak);

      } else if (dossier.getZaakDossier() instanceof DossierNaturalisatie) {
        return new Page290Zaken((Dossier) zaak);

      } else if (dossier.getZaakDossier() instanceof DossierOnderzoek) {
        return new Page240Zaken((Dossier) zaak);

      } else if (dossier.getZaakDossier() instanceof DossierRiskAnalysis) {
        return new Page250Zaken((Dossier) zaak);

      } else if (dossier.getZaakDossier() instanceof DossierRegistration) {
        return new Page260Zaken((Dossier) zaak);

      } else if (dossier.getZaakDossier() instanceof DossierLv) {
        return new Page300Zaken((Dossier) zaak);
      }
    }

    throw new ProException(WARNING, "Onbekend zaak type: " + zaak.getType() + " zaak-id: " + zaak.getZaakId());
  }
}
