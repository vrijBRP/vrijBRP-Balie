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

package nl.procura.gba.web.services.beheer.kassa;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.services.beheer.requestinbox.zaken.reisdocument.InboxReisdocumentProcessor.ReisdocumentInboxData;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.SoortReisdocument;
import nl.procura.gba.web.services.zaken.reisdocumenten.SpoedType;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;

public class KassaProductConverter {

  /**
   * Vertaald zaak naar kassaproduct
   */
  public static List<KassaProduct> getKassaProductAanvragen(KassaService service, Zaak zaak) {
    List<KassaProduct> list = new ArrayList<>();
    if (zaak.getStatus().isMinimaal(ZaakStatusType.OPGENOMEN)) {
      switch (zaak.getType()) {
        case GPK:
          list.add(getKassaProduct(KassaType.GPK));
          break;

        case UITTREKSEL:
          addUittreksels(list, zaak);
          break;

        case RIJBEWIJS:
          addRijbewijzen(list, zaak);
          break;

        default:
          break;
      }
    }

    // Deze zaken hoeven nog niet op opgenomen te staan
    switch (zaak.getType()) {
      case COVOG:
        list.add(getKassaProduct(KassaType.COVOG));
        break;

      case REISDOCUMENT:
        addReisdocumenten(service, list, zaak);
        break;

      default:
        break;
    }

    return list;
  }

  private static void addReisdocumenten(KassaService service, List<KassaProduct> list, Zaak zaak) {
    ReisdocumentAanvraag aanvraag = (ReisdocumentAanvraag) zaak;

    // Verzoek
    Optional<ReisdocumentInboxData> verzoekData = service.getServices()
        .getReisdocumentService()
        .getInboxRequestData(aanvraag);

    if (!verzoekData.isPresent()) {
      KassaProduct kassaProduct = new KassaProduct();
      kassaProduct.setKassaType(KassaType.REISDOCUMENT);

      // Type document
      for (SoortReisdocument soort : service.getServices().getReisdocumentService().getSoortReisdocumenten()) {
        if (equalsIgnoreCase(soort.getZkarg(), aanvraag.getReisdocumentType().getCode())) {
          kassaProduct.setKassaReisdocument(soort);
        }
      }

      list.add(kassaProduct);

      // Jeugd
      if (aanvraag.isGratis()) {
        list.add(getKassaProduct(KassaType.JEUGDTARIEF_REISDOC));
      }
    }

    boolean isSpoedBijVerzoek = verzoekData.map(ReisdocumentInboxData::getExpeditedProcessing).orElse(false);
    boolean isBezorgingBijVerzoek = verzoekData.map(ReisdocumentInboxData::getHomeDelivery).orElse(false);

    // Spoed
    if (!aanvraag.getSpoed().is(SpoedType.NEE)) {
      if (!isSpoedBijVerzoek) {
        list.add(getKassaProduct(KassaType.SPOED_REISDOC));
      }
    }

    // Thuisbezorging
    if (aanvraag.isThuisbezorgingGewenst()) {
      if (!isBezorgingBijVerzoek) {
        list.add(getKassaProduct(KassaType.BEZORGING_REISDOC));
      }
    }
  }

  private static void addRijbewijzen(List<KassaProduct> list, Zaak zaak) {

    RijbewijsAanvraag aanvraag = (RijbewijsAanvraag) zaak;

    KassaProduct kassaProduct = new KassaProduct();

    // Type document
    kassaProduct.setKassaType(KassaType.RIJBEWIJS);
    kassaProduct.setKassaRijbewijs(aanvraag.getSoortAanvraag());

    list.add(kassaProduct);

    // Spoed
    if (aanvraag.isSpoed()) {
      list.add(getKassaProduct(KassaType.SPOED_RIJBEWIJS));
    }
  }

  private static void addUittreksels(List<KassaProduct> list, Zaak zaak) {
    DocumentZaak aanvraag = (DocumentZaak) zaak;
    if (DocumentType.PL_UITTREKSEL.equals(aanvraag.getDoc().getDocumentType())) {
      KassaProduct kp = new KassaProduct();
      kp.setKassaType(KassaType.UITTREKSEL);
      kp.setKassaDocument(aanvraag.getDoc());
      list.add(kp);
    }
  }

  private static KassaProduct getKassaProduct(KassaType type) {

    KassaProduct kassaProduct = new KassaProduct();
    kassaProduct.setKassaType(type);
    return kassaProduct;
  }
}
