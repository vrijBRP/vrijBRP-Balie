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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.tree;

import java.util.List;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.views.ZakenAantalView;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;

public class ZakenAantalViewHandler {

  private final List<ZakenAantalView> aantallen;

  public ZakenAantalViewHandler(List<ZakenAantalView> aantallen) {
    this.aantallen = aantallen;
  }

  public long getAantal(ZaakArgumenten zaakArgumenten) {

    long totaal = 0;

    // Als een status genegeerd moet worden dan -1 teruggegeven
    for (long st : zaakArgumenten.getStatusCodes()) {
      if (zaakArgumenten.getNegeerStatusCodes().contains(st)) {
        return -1;
      }
    }

    for (ZakenAantalView aantal : aantallen) {
      for (long zaakType : zaakArgumenten.getTypeCodes()) {

        if (aantal.getZaakType() == zaakType) {
          ZaakArgumenten za = new ZaakArgumenten(zaakArgumenten);

          // Als sprake is van inhouding
          if (ZaakType.get(aantal.getZaakType()) == ZaakType.INHOUD_VERMIS) {
            za = getZaakArgumentenInhoudingen(zaakArgumenten);
          }

          // Overeenkomstige status
          for (long status : za.getStatusCodes()) {
            if (aantal.getIndVerwerkt() == status) {
              totaal += aantal.getAantal();
            }
          }
        }
      }
    }

    return totaal;
  }

  /**
   * Wijzigen ten behoeve van documentinhoudingen
   */
  private ZaakArgumenten getZaakArgumentenInhoudingen(ZaakArgumenten zaakArgumenten) {

    ZaakArgumenten nZaakArgumenten = new ZaakArgumenten(zaakArgumenten);

    if (nZaakArgumenten.getStatussen().size() > 0) {

      boolean isBehandelingWel = nZaakArgumenten.getStatussen().contains(ZaakStatusType.INBEHANDELING);
      boolean isVerwerktWel = nZaakArgumenten.getStatussen().contains(ZaakStatusType.VERWERKT);
      boolean isBehandelingNiet = nZaakArgumenten.getNegeerStatussen().contains(ZaakStatusType.INBEHANDELING);
      boolean isVerwerktNiet = nZaakArgumenten.getNegeerStatussen().contains(ZaakStatusType.VERWERKT);

      if (isBehandelingWel) { // Bij behandeling niets teruggeven
        nZaakArgumenten.getStatussen().remove(ZaakStatusType.INBEHANDELING);
        nZaakArgumenten.getStatussen().add(ZaakStatusType.ONBEKEND);
      }

      if (isBehandelingNiet) { // Bij behandeling niets teruggeven
        nZaakArgumenten.getNegeerStatussen().remove(ZaakStatusType.INBEHANDELING);
        nZaakArgumenten.getNegeerStatussen().add(ZaakStatusType.ONBEKEND);
      }

      if (isVerwerktWel) {
        nZaakArgumenten.getStatussen().add(ZaakStatusType.INBEHANDELING);
      }

      if (isVerwerktNiet) {
        nZaakArgumenten.getNegeerStatussen().add(ZaakStatusType.INBEHANDELING);
      }
    }

    return nZaakArgumenten;
  }
}
