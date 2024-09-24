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

package nl.procura.gba.web.services.beheer.inwonerapp;

import static nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType.INWONER_AFSLUITEN_ZAAK;
import static nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType.INWONER_APP_SAMENV;
import static nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdType.INWONER_APP;
import static nl.procura.gba.web.services.zaken.documenten.DocumentVertrouwelijkheid.ONBEKEND;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.jpa.personen.db.ZaakId;
import nl.procura.gba.jpa.personen.utils.GbaJpa;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZakenService;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuut;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesListener;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesTemplate;
import nl.procura.gba.web.services.zaken.algemeen.controle.StandaardControle;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSBytesContent;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;

public class InwonerAppControles extends ControlesTemplate<InwonerAppService> {

  public InwonerAppControles(InwonerAppService service) {
    super(service);
  }

  @Override
  public Controles getControles(ControlesListener listener) {
    Controles controles = new Controles();
    Services services = getService().getServices();
    ZakenService zakenService = services.getZakenService();
    for (String zaakId : getCasesThatNeedSummary()) {
      for (Zaak zaak : zakenService.getStandaardZaken(new ZaakArgumenten(zaakId))) {
        services.getInwonerAppService().getSummary(zaak).ifPresent(pdf -> {
          // Save the document
          DMSDocument dmsDocument = DMSDocument.builder()
              .content(DMSBytesContent.fromFilename("inwoner_app_samenvatting.pdf", pdf))
              .title("Inwoner.app samenvatting")
              .zaakId(zaak.getZaakId())
              .documentTypeDescription("")
              .confidentiality(getConfidentiality(services))
              .build();

          services.getDmsService().save(zaak, dmsDocument);
          // Save the zaakattribuut
          ZaakAttribuut attribuut = new ZaakAttribuut(zaakId, INWONER_APP_SAMENV, "Bestand gedownload");
          getService().getServices().getZaakAttribuutService().save(attribuut);
          controles.addControle(new InwonerAppControle(zaakId, "Inwoner.app", "Berichten samenvatting gedownload"));
        });
      }
    }

    for (String zaakId : getCasesThatNeedClosed()) {
      for (Zaak zaak : zakenService.getStandaardZaken(new ZaakArgumenten(zaakId))) {
        if (getService().getServices().getInwonerAppService().sluitZaak(zaak)) {
          ZaakAttribuut attribuut = new ZaakAttribuut(zaakId, INWONER_AFSLUITEN_ZAAK, "Inwoner.app zaak afgesloten");
          getService().getServices().getZaakAttribuutService().save(attribuut);
          controles.addControle(new InwonerAppControle(zaakId, "Inwoner.app", "Inwoner.app zaak afgesloten"));
        }
      }
    }

    return controles;
  }

  private static String getConfidentiality(Services services) {
    return services.getDocumentService()
        .getStandaardVertrouwelijkheid(ONBEKEND, ONBEKEND)
        .getOmschrijving();
  }

  private static List<String> getCasesThatNeedClosed() {
    String sb = "select z from ZaakId z "
        + "where z.id.type = :type "
        + "and z.id.internId in (select i.zaakId from IndVerwerkt i "
        + "where i.indVerwerkt in :statusses) "
        + "and z.id.internId in (select a.id.zaakId from ZaakAttr a where a.id.zaakAttr = :attr1) "
        + "and z.id.internId not in (select a.id.zaakId from ZaakAttr a where a.id.zaakAttr = :attr2)";

    EntityManager em = GbaJpa.getManager();
    TypedQuery<ZaakId> query = em.createQuery(sb, ZaakId.class);
    query.setParameter("type", INWONER_APP.getCode());
    query.setParameter("attr1", INWONER_APP_SAMENV.getCode());
    query.setParameter("attr2", INWONER_AFSLUITEN_ZAAK.getCode());
    query.setParameter("statusses", Arrays.stream(ZaakStatusType.values())
        .filter(ZaakStatusType::isEindStatus)
        .map(ZaakStatusType::getCode)
        .collect(Collectors.toList()));

    return query.getResultList()
        .stream()
        .map(z -> z.getId().getInternId())
        .collect(Collectors.toList());
  }

  private static List<String> getCasesThatNeedSummary() {
    String sb = "select z from ZaakId z "
        + "where z.id.type = :type "
        + "and z.id.internId in (select i.zaakId from IndVerwerkt i "
        + "where i.indVerwerkt in :statusses) "
        + "and z.id.internId not in (select a.id.zaakId from ZaakAttr a "
        + "where a.id.zaakAttr = :attr)";

    EntityManager em = GbaJpa.getManager();
    TypedQuery<ZaakId> query = em.createQuery(sb, ZaakId.class);
    query.setParameter("type", INWONER_APP.getCode());
    query.setParameter("attr", INWONER_APP_SAMENV.getCode());
    query.setParameter("statusses", Arrays.stream(ZaakStatusType.values())
        .filter(ZaakStatusType::isEindStatus)
        .map(ZaakStatusType::getCode)
        .collect(Collectors.toList()));

    return query.getResultList()
        .stream()
        .map(z -> z.getId().getInternId())
        .collect(Collectors.toList());
  }

  public static class InwonerAppControle extends StandaardControle {

    public InwonerAppControle(String zaakId, String onderwerp, String omschrijving) {
      super(onderwerp, omschrijving);
      setId(zaakId);
      setGewijzigd(true);
    }
  }
}
