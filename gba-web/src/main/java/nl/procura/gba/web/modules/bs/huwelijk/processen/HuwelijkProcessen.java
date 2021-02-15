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

package nl.procura.gba.web.modules.bs.huwelijk.processen;

import static java.util.Arrays.asList;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.bs.common.BsProcessen;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatussen;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatussen.BspProcesStatusEntry;
import nl.procura.gba.web.modules.bs.huwelijk.page1.Page1Huwelijk;
import nl.procura.gba.web.modules.bs.huwelijk.page10.Page10Huwelijk;
import nl.procura.gba.web.modules.bs.huwelijk.page16.Page16Huwelijk;
import nl.procura.gba.web.modules.bs.huwelijk.page20.Page20Huwelijk;
import nl.procura.gba.web.modules.bs.huwelijk.page30.Page30Huwelijk;
import nl.procura.gba.web.modules.bs.huwelijk.page40.Page40Huwelijk;
import nl.procura.gba.web.modules.bs.huwelijk.page50.Page50Huwelijk;
import nl.procura.gba.web.modules.bs.huwelijk.page60.Page60Huwelijk;
import nl.procura.gba.web.modules.bs.huwelijk.page70.Page70Huwelijk;
import nl.procura.gba.web.modules.bs.huwelijk.page75.Page65Huwelijk;
import nl.procura.gba.web.modules.bs.huwelijk.page80.Page80Huwelijk;
import nl.procura.gba.web.modules.bs.huwelijk.page90.Page90Huwelijk;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.vereiste.DossierVereiste;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.bs.huwelijk.StatusVerbintenis;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.ZaakCommentaren;

public class HuwelijkProcessen extends BsProcessen {

  public HuwelijkProcessen(Dossier dossier, GbaApplication gbaApplication) {
    this(gbaApplication);
    setDossier(dossier);
  }

  private HuwelijkProcessen(GbaApplication gbaApplication) {

    super(gbaApplication);

    addProces("Partner 1", Page1Huwelijk.class);
    addProces("Partner 2", Page10Huwelijk.class);
    addProces("Vereisten", Page50Huwelijk.class);
    addProces("Gerelateerden", Page16Huwelijk.class);
    addProces("Planning", Page20Huwelijk.class);
    addProces("Bijzonderheden", Page30Huwelijk.class);
    addProces("Getuigen", Page40Huwelijk.class);
    addProces("Naam(gebruik)", Page60Huwelijk.class);
    addProces("Afdrukken voorbereiding", Page65Huwelijk.class);
    addProces("Overzicht", Page70Huwelijk.class);
    addProces("Aktenummer", Page80Huwelijk.class);
    addProces("Afdrukken", Page90Huwelijk.class);
  }

  public static BsProcesStatussen getProcesStatussen(Dossier dossier) {

    BsProcesStatussen statussen = new BsProcesStatussen();

    if (fil(dossier.getZaakId())) {

      DossierHuwelijk huwelijk = (DossierHuwelijk) dossier.getZaakDossier();

      boolean isPartner1 = huwelijk.getPartner1().isVolledig();
      boolean isPartner2 = huwelijk.getPartner2().isVolledig();
      boolean isPartners = isPartner1 && isPartner2;
      boolean isPlanning = isPartners && !SoortVerbintenis.ONBEKEND.equals(huwelijk.getSoortVerbintenis());

      String planningMessage = "";
      String getuigenMessage = "";
      String vereistenMessage = "";
      String bijzonderMessage = "";
      String partner1Message = "";
      String partner2Message = "";
      String gerelateerdeMessage = "";

      // Partner1
      if (isPartner1 && huwelijk.getPartner1().isOverleden()) {
        partner1Message = "Partner 1 is overleden";
      }

      if (isPartner2 && huwelijk.getPartner2().isOverleden()) {
        partner2Message = "Partner 2 is overleden";
      }

      // Vereisten
      if (isPartners) {
        for (DossierVereiste v : huwelijk.getDossier().getVereisten()) {
          if (!isTru(v.getVoldaan())) {
            vereistenMessage = "Nog niet voldaan aan alle vereisten";
          }
        }
      }

      // Gerelateerde(n)
      List<DossierPersoon> relaties = new ArrayList<>();
      relaties.addAll(huwelijk.getPartner1().getPersonen());
      relaties.addAll(huwelijk.getPartner2().getPersonen());

      if (BsPersoonUtils.isOverleden(relaties)) {
        gerelateerdeMessage = "Er zijn relaties van de partners overleden";
      }

      // Planning
      if (isPlanning) {
        if (huwelijk.getDatumVerbintenis().getLongDate() <= 0) {
          planningMessage = "De datum van de verbintenis is niet gevuld";
        } else if (huwelijk.getTijdVerbintenis().getLongTime() <= 0) {
          planningMessage = "Het tijdstip van de verbintenis is niet gevuld";
        } else if (huwelijk.getHuwelijksLocatie() == null || !pos(huwelijk.getHuwelijksLocatie().getId())) {
          planningMessage = "De huwelijkslocatie is nog niet gevuld";
        } else if (huwelijk.getStatusVerbintenis() != StatusVerbintenis.DEFINITIEF) {
          planningMessage = "De status staat nog niet op definitief";
        }
      }

      // Bijzonderheden
      boolean isAmbtenaar1 = huwelijk.getAmbtenaar1().isVolledig();
      boolean isAmbtenaar2 = huwelijk.getAmbtenaar2().isVolledig();
      boolean isAmbtenaar3 = huwelijk.getAmbtenaar3().isVolledig();

      if (isPlanning && !isAmbtenaar1 && !isAmbtenaar2 && !isAmbtenaar3) {
        bijzonderMessage = "Er zijn nog geen ambtenaren geselecteerd";
      }
      List<DossierPersoon> ambtenaren = asList(huwelijk.getAmbtenaar1(), huwelijk.getAmbtenaar2(),
          huwelijk.getAmbtenaar3());
      if (BsPersoonUtils.isOverleden(ambtenaren)) {
        bijzonderMessage = "Er zijn ambtenaren overleden";
      }

      // Getuigen
      boolean isGetuigen = isPlanning && huwelijk.getGetuigen().size() >= 0;
      if (isGetuigen && huwelijk.getHuwelijksLocatie() != null) {
        int minAantalGetuigen = huwelijk.getHuwelijksLocatie().getLocatieSoort().getAantalGetuigenMin();
        int ingevoerd = huwelijk.getVolledigIngevuldeGetuigen().size();

        if (ingevoerd < minAantalGetuigen) {
          getuigenMessage = "Er dienen minimaal " + minAantalGetuigen + " getuigen worden ingevoerd";
        }
      }

      if (BsPersoonUtils.isOverleden(huwelijk.getGetuigen())) {
        getuigenMessage = "Er zijn getuigen overleden";
      }

      boolean isNaam = fil(huwelijk.getNaamPartner1()) && fil(huwelijk.getNaamPartner2());
      boolean isDatumVoornemen = isNaam && pos(huwelijk.getDatumVoornemen().getLongDate());

      statussen.add(Page1Huwelijk.class, isPartner1, partner1Message);
      statussen.add(Page10Huwelijk.class, isPartner2, partner2Message);
      statussen.add(Page50Huwelijk.class, isPartners, vereistenMessage);
      statussen.add(Page16Huwelijk.class, isPartners, gerelateerdeMessage);
      statussen.add(Page20Huwelijk.class, isPlanning, planningMessage);
      statussen.add(Page30Huwelijk.class, isPlanning, bijzonderMessage);
      statussen.add(Page40Huwelijk.class, isGetuigen, getuigenMessage);
      statussen.add(Page60Huwelijk.class, isNaam, "");
      statussen.add(Page65Huwelijk.class, isNaam, "");
      statussen.add(Page70Huwelijk.class, isDatumVoornemen, "");
      statussen.add(Page80Huwelijk.class, huwelijk.getDossier().isAktesCorrect(), "");
      statussen.add(Page90Huwelijk.class, huwelijk.getDossier().isAktesCorrect(), "");
    }

    return statussen;
  }

  @Override
  public void initStatusses(GbaApplication app) {
    updateStatus();
  }

  @Override
  public void updateStatus() {

    BsProcesStatussen procesStatussen = getProcesStatussen(getDossier());
    for (BspProcesStatusEntry entry : procesStatussen.getList()) {
      getProces(entry.getCl()).setStatus(entry.getStatus(), entry.getMessage());
    }

    if (isPaginaReset(getDossier())) {
      goToFirst();
    }

    ZaakCommentaren commentaar = getDossier().getCommentaren();
    commentaar.verwijderen().toevoegenWarn(procesStatussen.getMessages());
  }
}
