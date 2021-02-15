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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig;

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.List;

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
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page30.Page30Zaken;
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
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;
import nl.procura.gba.web.services.bs.overlijden.buitenland.DossierOverlijdenBuitenland;
import nl.procura.gba.web.services.bs.overlijden.gemeente.DossierOverlijdenGemeente;
import nl.procura.gba.web.services.bs.overlijden.lijkvinding.DossierLijkvinding;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.inbox.InboxRecord;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZakenService;
import nl.procura.gba.web.services.zaken.correspondentie.CorrespondentieZaak;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;
import nl.procura.gba.web.services.zaken.geheim.GeheimAanvraag;
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
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.PageNavigation;

public class ZaakregisterNavigator {

  public static void navigatoTo(Zaak z, GbaPageTemplate page, boolean resetNavigatie) {

    if (fil(z.getZaakId())) {

      ZakenService zaken = page.getApplication().getServices().getZakenService();

      // Herlaad de gehele zaak
      List<Zaak> minimaleZaken = zaken.getMinimaleZaken(new ZaakArgumenten(z));

      for (Zaak zaak : zaken.getVolledigeZaken(minimaleZaken)) {

        PageNavigation nav = page.getParentLayout().getNavigation();

        if (zaak instanceof NaamgebruikAanvraag) {
          nav.goToPage(new Page10Zaken((NaamgebruikAanvraag) zaak));

        } else if (zaak instanceof IndicatieAanvraag) {
          nav.goToPage(new Page170Zaken((IndicatieAanvraag) zaak));

        } else if (zaak instanceof CorrespondentieZaak) {
          nav.goToPage(new Page180Zaken((CorrespondentieZaak) zaak));

        } else if (zaak instanceof GeheimAanvraag) {
          nav.goToPage(new Page20Zaken((GeheimAanvraag) zaak));

        } else if (zaak instanceof GpkAanvraag) {
          nav.goToPage(new Page30Zaken((GpkAanvraag) zaak));

        } else if (zaak instanceof InboxRecord) {
          nav.goToPage(new Page230Zaken((InboxRecord) zaak));

        } else if (zaak instanceof VogAanvraag) {
          nav.goToPage(new Page40Zaken((VogAanvraag) zaak));

        } else if (zaak instanceof DocumentZaak) {
          nav.goToPage(new Page50Zaken((DocumentZaak) zaak));

        } else if (zaak instanceof VerhuisAanvraag) {
          nav.goToPage(new Page60Zaken((VerhuisAanvraag) zaak));

        } else if (zaak instanceof ReisdocumentAanvraag) {
          nav.goToPage(new Page70Zaken((ReisdocumentAanvraag) zaak));

        } else if (zaak instanceof DocumentInhouding) {
          nav.goToPage(new Page75Zaken((DocumentInhouding) zaak));

        } else if (zaak instanceof RijbewijsAanvraag) {
          nav.goToPage(new Page90Zaken((RijbewijsAanvraag) zaak));

        } else if (zaak instanceof TerugmeldingAanvraag) {
          nav.goToPage(new Page80Zaken((TerugmeldingAanvraag) zaak));

        } else if (zaak instanceof GvAanvraag) {
          nav.goToPage(new Page190Zaken((GvAanvraag) zaak));

        } else if (zaak instanceof PersonListMutation) {
          nav.goToPage(new Page270Zaken((PersonListMutation) zaak));

        } else if (zaak instanceof Dossier) {
          Dossier dossier = (Dossier) zaak;

          if (dossier.getZaakDossier() instanceof DossierLevenloos) {
            nav.goToPage(new Page200Zaken((Dossier) zaak));
          } else if (dossier.getZaakDossier() instanceof DossierGeboorte) {
            nav.goToPage(new Page100Zaken((Dossier) zaak));
          } else if (dossier.getZaakDossier() instanceof DossierHuwelijk) {
            nav.goToPage(new Page110Zaken((Dossier) zaak));
          } else if (dossier.getZaakDossier() instanceof DossierOmzetting) {
            nav.goToPage(new Page210Zaken((Dossier) zaak));
          } else if (dossier.getZaakDossier() instanceof DossierOntbinding) {
            nav.goToPage(new Page220Zaken((Dossier) zaak));
          } else if (dossier.getZaakDossier() instanceof DossierOverlijdenGemeente) {
            nav.goToPage(new Page120Zaken((Dossier) zaak));
          } else if (dossier.getZaakDossier() instanceof DossierLijkvinding) {
            nav.goToPage(new Page130Zaken((Dossier) zaak));
          } else if (dossier.getZaakDossier() instanceof DossierOverlijdenBuitenland) {
            nav.goToPage(new Page140Zaken((Dossier) zaak));
          } else if (dossier.getZaakDossier() instanceof DossierErkenning) {
            nav.goToPage(new Page150Zaken((Dossier) zaak));
          } else if (dossier.getZaakDossier() instanceof DossierNaamskeuze) {
            nav.goToPage(new Page280Zaken((Dossier) zaak));
          } else if (dossier.getZaakDossier() instanceof DossierOnderzoek) {
            nav.goToPage(new Page240Zaken((Dossier) zaak));
          } else if (dossier.getZaakDossier() instanceof DossierRiskAnalysis) {
            nav.goToPage(new Page250Zaken((Dossier) zaak));
          } else if (dossier.getZaakDossier() instanceof DossierRegistration) {
            nav.goToPage(new Page260Zaken((Dossier) zaak));
          } else {
            throw new ProException(WARNING, "Deze zaak is onvolledig en kan niet worden geladen.");
          }
        }

        if (resetNavigatie) {
          nav.removeOtherPages();
        }

        break;
      }
    }
  }
}
