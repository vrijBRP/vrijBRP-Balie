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

package nl.procura.gba.web.services.zaken.algemeen;

import static nl.procura.gba.web.services.zaken.algemeen.ZaakTypeStatussen.getAlle;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.ENTRY;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.beheer.parameter.ZaakStatusParameterType;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisType;
import nl.procura.commons.core.exceptions.ProException;

public class ZaakStatusUtils {

  public static void check(Zaak zaak, ZaakStatusType nieuweStatus) {

    if (!getAlle(zaak.getType()).contains(nieuweStatus)) {

      StringBuilder msg = new StringBuilder();
      msg.append("Zaak met type <b>");
      msg.append(zaak.getType());
      msg.append("</b> kan niet worden gewijzigd in status <b>");
      msg.append(nieuweStatus.getOms());
      msg.append("</b");

      throw new ProException(WARNING, msg.toString());
    }
  }

  /**
   * Geeft de initiele zaak terug op basis van de parameters
   */
  public static ZaakStatusType getInitieleStatus(AbstractService service, Zaak zaak) {
    for (ZaakStatusParameterType parmType : ZaakStatusParameterType.getByZaakType(zaak.getType())) {
      if (parmType != null) {
        String parm = service.getParm(parmType.getParameterType());
        parm = getVerhuizingInitieleStatus(zaak, parm, parmType.getFunctieAdres(), parmType.getVerhuisType(), parm);
        parm = getRegistrationInitieleStatus(zaak, parm, parmType.getFunctieAdres(), parm);
        parm = getInhoudingRijbewijsInitieleStatus(zaak, parm, parmType.isInhoudingRijbewijs(), parm);
        if (fil(parm)) {
          ZaakStatusType status = ZaakStatusType.get(along(parm));
          if (status != ZaakStatusType.ONBEKEND) {
            return status;
          }
        }
      }
    }

    throw new ProException(ENTRY, WARNING, "Van dit zaaktype is geen initiele status " +
        "vastgelegd in de parameters");
  }

  /**
   * Geef de parameter terug van een verhuisaanvraag
   */
  private static String getVerhuizingInitieleStatus(Zaak zaak, String value, FunctieAdres functieAdres,
      VerhuisType verhuisType, String parm) {

    if (zaak instanceof VerhuisAanvraag) {
      if (FunctieAdres.ONBEKEND != functieAdres) {
        parm = "";
        VerhuisAanvraag verhuisAanvraag = (VerhuisAanvraag) zaak;
        FunctieAdres fa = verhuisAanvraag.getNieuwAdres().getFunctieAdres();
        VerhuisType tv = verhuisAanvraag.getTypeVerhuizing();
        boolean isFunctieAdres1 = (fa == functieAdres);
        boolean isFunctieAdres2 = (fa == FunctieAdres.ONBEKEND) && (functieAdres == FunctieAdres.WOONADRES);
        boolean isVerhuisType = (verhuisType == tv);

        if ((isFunctieAdres1 || isFunctieAdres2) && isVerhuisType) {
          parm = value;
        }
      }
    }
    return parm;
  }

  /**
   * Geef de parameter terug van een eerste inschrijving
   */
  private static String getRegistrationInitieleStatus(Zaak zaak, String value, FunctieAdres functieAdres, String parm) {
    if (zaak instanceof Dossier) {
      if (zaak.getType().is(ZaakType.REGISTRATION)) {
        Dossier dossier = (Dossier) zaak;
        DossierRegistration registration = (DossierRegistration) dossier.getZaakDossier();
        FunctieAdres fa = FunctieAdres.get(registration.getAddressFunction());
        if (FunctieAdres.ONBEKEND != functieAdres) {
          parm = "";
          boolean isFunctieAdres1 = (fa == functieAdres);
          boolean isFunctieAdres2 = (fa == FunctieAdres.ONBEKEND) && (functieAdres == FunctieAdres.WOONADRES);
          if ((isFunctieAdres1 || isFunctieAdres2)) {
            parm = value;
          }
        }
      }
    }
    return parm;
  }

  /**
   * Geef de parameter terug van een rijbewijs vermissing
   */
  private static String getInhoudingRijbewijsInitieleStatus(Zaak zaak, String value,
      boolean inhoudingRijbewijs, String parm) {
    if (zaak instanceof DocumentInhouding) {
      parm = "";
      DocumentInhouding inhouding = (DocumentInhouding) zaak;
      if (inhouding.isSprakeVanRijbewijs() && inhoudingRijbewijs) {
        parm = value;
      }
      if (!inhouding.isSprakeVanRijbewijs() && !inhoudingRijbewijs) {
        parm = value;
      }
    }
    return parm;
  }
}
