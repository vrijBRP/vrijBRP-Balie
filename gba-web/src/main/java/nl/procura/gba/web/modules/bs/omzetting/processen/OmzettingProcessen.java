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

package nl.procura.gba.web.modules.bs.omzetting.processen;

import static nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus.*;
import static nl.procura.standard.Globalfunctions.*;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.bs.common.BsProcessen;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatussen;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatussen.BspProcesStatusEntry;
import nl.procura.gba.web.modules.bs.omzetting.page1.Page1Omzetting;
import nl.procura.gba.web.modules.bs.omzetting.page10.Page10Omzetting;
import nl.procura.gba.web.modules.bs.omzetting.page16.Page16Omzetting;
import nl.procura.gba.web.modules.bs.omzetting.page20.Page20Omzetting;
import nl.procura.gba.web.modules.bs.omzetting.page30.Page30Omzetting;
import nl.procura.gba.web.modules.bs.omzetting.page40.Page40Omzetting;
import nl.procura.gba.web.modules.bs.omzetting.page50.Page50Omzetting;
import nl.procura.gba.web.modules.bs.omzetting.page60.Page60Omzetting;
import nl.procura.gba.web.modules.bs.omzetting.page70.Page70Omzetting;
import nl.procura.gba.web.modules.bs.omzetting.page75.Page65Omzetting;
import nl.procura.gba.web.modules.bs.omzetting.page80.Page80Omzetting;
import nl.procura.gba.web.modules.bs.omzetting.page90.Page90Omzetting;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.vereiste.DossierVereiste;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;
import nl.procura.gba.web.services.bs.omzetting.StatusVerbintenis;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.ZaakCommentaren;

public class OmzettingProcessen extends BsProcessen {

  public OmzettingProcessen(Dossier dossier, GbaApplication gbaApplication) {
    this(gbaApplication);
    setDossier(dossier);
  }

  private OmzettingProcessen(GbaApplication gbaApplication) {

    super(gbaApplication);

    addProces("Partner 1", Page1Omzetting.class);
    addProces("Partner 2", Page10Omzetting.class);
    addProces("Vereisten", Page50Omzetting.class);
    addProces("Gerelateerden", Page16Omzetting.class);
    addProces("Planning", Page20Omzetting.class);
    addProces("Bijzonderheden", Page30Omzetting.class);
    addProces("Getuigen", Page40Omzetting.class);
    addProces("Naam(gebruik)", Page60Omzetting.class);
    addProces("Afdrukken voorbereiding", Page65Omzetting.class);
    addProces("Overzicht", Page70Omzetting.class);
    addProces("Aktenummer", Page80Omzetting.class);
    addProces("Afdrukken", Page90Omzetting.class);
  }

  public static BsProcesStatussen getProcesStatussen(Dossier dossier) {

    BsProcesStatussen statussen = new BsProcesStatussen();

    if (fil(dossier.getZaakId())) {

      DossierOmzetting omzetting = (DossierOmzetting) dossier.getZaakDossier();

      boolean isPartner1 = omzetting.getPartner1().isVolledig();
      boolean isPartner2 = omzetting.getPartner2().isVolledig();
      boolean isPartners = isPartner1 && isPartner2;
      boolean isPlanning = isPartners && omzetting.getDatumVerbintenis().getLongDate() > 0;

      boolean isPlanningCompleet = true;
      boolean isBijzonderCompleet = true;
      boolean isVereistenCompleet = true;

      String planningMessage = "";
      String vereistenMessage = "";
      String bijzonderMessage = "";

      // Vereisten
      if (isPartners) {
        for (DossierVereiste v : omzetting.getDossier().getVereisten()) {
          if (!isTru(v.getVoldaan())) {
            isVereistenCompleet = false;
            vereistenMessage = "Nog niet voldaan aan alle vereisten";
          }
        }
      }

      // Planning
      if (isPlanning) {
        if (omzetting.getDatumVerbintenis().getLongDate() <= 0) {
          isPlanningCompleet = false;
          planningMessage = "De datum van de verbintenis is niet gevuld";
        } else if (omzetting.getHuwelijksLocatie() == null || !pos(omzetting.getHuwelijksLocatie().getId())) {
          planningMessage = "De huwelijkslocatie is nog niet gevuld";
        } else if (omzetting.getStatusVerbintenis() != StatusVerbintenis.DEFINITIEF) {
          isPlanningCompleet = false;
          planningMessage = "De status staat nog niet op definitief";
        }
      }

      // Bijzonderheden
      boolean isAmbtenaar1 = omzetting.getAmbtenaar1().isVolledig();
      boolean isAmbtenaar2 = omzetting.getAmbtenaar2().isVolledig();
      boolean isAmbtenaar3 = omzetting.getAmbtenaar3().isVolledig();

      if (isPlanning && !isAmbtenaar1 && !isAmbtenaar2 && !isAmbtenaar3) {
        isBijzonderCompleet = false;
        bijzonderMessage = "Er zijn nog geen ambtenaren geselecteerd";
      }

      // Getuigen
      boolean isGetuigen = isPlanning && omzetting.getGetuigen().size() >= 0;
      boolean isNaam = fil(omzetting.getNaamPartner1()) && fil(omzetting.getNaamPartner2());

      statussen.add(Page1Omzetting.class, isPartner1 ? COMPLETE : EMPTY);
      statussen.add(Page10Omzetting.class, isPartner2 ? COMPLETE : EMPTY);
      statussen.add(Page50Omzetting.class, isPartners ? (isVereistenCompleet ? COMPLETE : INCOMPLETE) : EMPTY,
          vereistenMessage);
      statussen.add(Page16Omzetting.class, isPartners ? COMPLETE : EMPTY);
      statussen.add(Page20Omzetting.class, isPlanning ? (isPlanningCompleet ? COMPLETE : INCOMPLETE) : EMPTY,
          planningMessage);
      statussen.add(Page30Omzetting.class, isPlanning ? (isBijzonderCompleet ? COMPLETE : INCOMPLETE) : EMPTY,
          bijzonderMessage);
      statussen.add(Page40Omzetting.class, isGetuigen ? COMPLETE : EMPTY);
      statussen.add(Page60Omzetting.class, isNaam ? COMPLETE : EMPTY);
      statussen.add(Page65Omzetting.class, isNaam ? COMPLETE : EMPTY);
      statussen.add(Page70Omzetting.class, isNaam ? COMPLETE : EMPTY);
      statussen.add(Page80Omzetting.class, omzetting.getDossier().isAktesCorrect() ? COMPLETE : EMPTY);
      statussen.add(Page90Omzetting.class, omzetting.getDossier().isAktesCorrect() ? COMPLETE : EMPTY);
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
