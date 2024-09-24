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

package nl.procura.gba.web.services.zaken.rijbewijs;

import static java.lang.String.format;
import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.common.ZaakStatusType.INBEHANDELING;
import static nl.procura.gba.common.ZaakStatusType.OPGENOMEN;
import static nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsStatusType.RIJBEWIJS_ONTVANGEN_DOOR_GEMEENTE_OK;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.trim;

import java.util.HashSet;
import java.util.List;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.jpa.personen.db.NrdStatus;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesListener;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesTemplate;
import nl.procura.gba.web.services.zaken.algemeen.controle.ZaakControle;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;
import nl.procura.rdw.processen.p1914.f02.AANVRRYBKGEG;
import nl.procura.rdw.processen.p1914.f02.NATPAANVRGEG;
import nl.procura.rdw.processen.p1914.f02.RYBAANVROVERZ;
import nl.procura.rdw.processen.p1914.f02.STATRYBKGEG;

import lombok.extern.slf4j.Slf4j;

/**
 * Alle controles van rijbewijszaken
 */
@Slf4j
public class RijbewijsZaakControles extends ControlesTemplate<RijbewijsService> {

  RijbewijsZaakControles(RijbewijsService service) {
    super(service);
  }

  /**
   * Controle per zaak
   */
  public RijbewijsAanvraag controleer(RijbewijsAanvraag zaak) {
    if (zaak.getStatus().isMaximaal(ZaakStatusType.INBEHANDELING)) {
      controleerStatusDocumentOntvangen(zaak);
    }
    return zaak;
  }

  @Override
  public Controles getControles(ControlesListener listener) {
    return getControlesByStatus(listener, RIJBEWIJS_ONTVANGEN_DOOR_GEMEENTE_OK);
  }

  /**
   * Zoek alle zaken met een bepaalde rijbewijsStatus
   */
  Controles getControlesByStatus(ControlesListener listener, RijbewijsStatusType rijbewijsStatus) {
    Controles controles = new Controles();

    if (getService().isRijbewijzenServiceActive()) {
      List<RYBAANVROVERZ> overzichten = NrdServices.getNrdAanvragenByStatus(getService(), rijbewijsStatus, 1000,
          listener, listener);

      for (RYBAANVROVERZ overzicht : overzichten) {
        List<NATPAANVRGEG> natpaanvrgeg = overzicht.getNatpaanvrtab().getNatpaanvrgeg();

        RijbewijsStatusType rst = RijbewijsStatusType.get(
            overzicht.getAanvrrybzgeg().getStatcoderybk().longValue());

        HashSet<String> rijbewijsNrs = new HashSet<>();
        for (NATPAANVRGEG aanvraag : natpaanvrgeg) {
          AANVRRYBKGEG r = aanvraag.getAanvrrybkgeg();
          rijbewijsNrs.add(r.getAanvrnrrybk().toString());
        }

        if (rijbewijsNrs.size() > 0) {

          ZaakArgumenten zaakArgumenten = new ZaakArgumenten();
          zaakArgumenten.setZaakIds(rijbewijsNrs);

          // Alleen zaken zoeken bijwerken met status opgenomen / inbehandeling
          if (RIJBEWIJS_ONTVANGEN_DOOR_GEMEENTE_OK.is(rijbewijsStatus)) {
            zaakArgumenten.setStatussen(OPGENOMEN, INBEHANDELING);
          }

          List<RijbewijsAanvraag> zaken = getService().getMinimalZaken(zaakArgumenten);

          for (NATPAANVRGEG aanvraag : natpaanvrgeg) {

            AANVRRYBKGEG r = aanvraag.getAanvrrybkgeg();
            STATRYBKGEG s = aanvraag.getStatrybkgeg();
            String aanvrNr = r.getAanvrnrrybk().toString();

            for (RijbewijsAanvraag zaak : zaken) {
              if (fil(aanvrNr) && aanvrNr.equals(zaak.getAanvraagNummer())) {
                if (zaak.getStatus().isEindStatus()) {
                  continue;
                }

                zaak = getService().getStandardZaak(zaak);
                ZaakControle<Zaak> controle = controles.addControle(
                    new ZaakControle<>("Rijbewijzen", zaak));

                boolean isAddRijbewijsNr = isRijbewijsNummerBijwerken(zaak, s);
                boolean isAddStatus = isRijbewijsStatusBijwerken(zaak, s, rst);

                StringBuilder info = new StringBuilder();

                if (isAddRijbewijsNr) {
                  info.append("rijbewijsnr. bijgewerkt");
                }

                if (isAddStatus) {
                  info.append(", Status ").append(rst.getCode()).append(" nu toegevoegd");
                } else {
                  controle.addOpmerking(format("Status %d reeds bekend", rst.getCode()));
                }

                String infoMsg = trim(info.toString());

                if (fil(infoMsg)) {
                  zaak.getZaakHistorie().addWijziging(info.toString());
                  if (listener != null) {
                    listener.info(infoMsg);
                  }
                }

                if (isAddRijbewijsNr || isAddStatus) {
                  getService().save(zaak);

                  // Zet de status op ontvangen
                  if (isAddStatus && RIJBEWIJS_ONTVANGEN_DOOR_GEMEENTE_OK.is(rijbewijsStatus)) {
                    ZaakStatusService zaakStatusService = getService().getZaakStatussen();
                    zaakStatusService.updateStatus(zaak, ZaakStatusType.DOCUMENT_ONTVANGEN, "");
                  }
                }
              }
            }
          }
        }
      }
    }

    return controles;
  }

  /**
   * Vult de zaak met de statussen
   */
  void vulRijbewijsStatussen(RijbewijsAanvraag zaak) {

    RijbewijsAanvraag aanvraagImpl = to(zaak, RijbewijsAanvraag.class);

    List<RijbewijsAanvraagStatus> rijbewijsStatussen = aanvraagImpl.getStatussen().getStatussen();
    rijbewijsStatussen.clear();

    List<NrdStatus> nrdStatusses = aanvraagImpl.getNrdStatuses();
    if (nrdStatusses != null) {
      rijbewijsStatussen.addAll(copyList(nrdStatusses, RijbewijsAanvraagStatus.class));
    }
  }

  /**
   * Als het document is ontvangen dan de zaak is nog niet verwerkt dan status toevoegen
   */
  private void controleerStatusDocumentOntvangen(RijbewijsAanvraag zaak) {
    if (fil(zaak.getAanvraagNummer()) && !zaak.getStatus().isEindStatus()) {
      RijbewijsAanvraagStatus rijbewijsStatus = zaak.getStatussen().getStatus();
      if (rijbewijsStatus.getStatus().is(RIJBEWIJS_ONTVANGEN_DOOR_GEMEENTE_OK)) {
        getService().getZaakStatussen().updateStatus(zaak, ZaakStatusType.DOCUMENT_ONTVANGEN, "");
      }
    }

  }

  private RijbewijsAanvraagStatus getNieuweStatus(RijbewijsStatusType status, STATRYBKGEG gegevens) {

    long dIn = gegevens.getStatdatrybk().longValue();
    long tIn = gegevens.getStattydrybk().longValue();

    return new RijbewijsAanvraagStatus(dIn, tIn, status, getService().getServices().getGebruiker());
  }

  /**
   * Moet dit rijbewijsnummer worden bijgewerkt?
   */
  private boolean isRijbewijsNummerBijwerken(RijbewijsAanvraag zaak, STATRYBKGEG status) {

    String rbwNr = astr(status.getRybstatrybk());
    boolean isNieuwRijbewijsNummer = pos(rbwNr) && !rbwNr.equals(zaak.getRijbewijsnummer());

    if (isNieuwRijbewijsNummer) { // Rijbewijsnummer updaten
      zaak.setRijbewijsnummer(rbwNr);
      return true;
    }

    return false;
  }

  /**
   * Moet deze status worden bijgewerkt?
   */
  private boolean isRijbewijsStatusBijwerken(RijbewijsAanvraag zaak, STATRYBKGEG status,
      RijbewijsStatusType rijbewijsStatusType) {

    RijbewijsAanvraagStatus rijbewijsStatus = zaak.getStatussen().getStatus(rijbewijsStatusType);

    if (rijbewijsStatus == null) {
      zaak.getStatussen().addStatus(getNieuweStatus(rijbewijsStatusType, status));
      return true;
    }

    return false;
  }
}
