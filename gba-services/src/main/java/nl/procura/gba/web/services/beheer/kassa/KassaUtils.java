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

import static ch.lambdaj.Lambda.exists;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static nl.procura.gba.web.services.beheer.kassa.KassaType.BEZORGING_REISDOC;
import static nl.procura.gba.web.services.beheer.kassa.KassaType.COVOG;
import static nl.procura.gba.web.services.beheer.kassa.KassaType.GPK;
import static nl.procura.gba.web.services.beheer.kassa.KassaType.JEUGDTARIEF_REISDOC;
import static nl.procura.gba.web.services.beheer.kassa.KassaType.REISDOCUMENT;
import static nl.procura.gba.web.services.beheer.kassa.KassaType.RIJBEWIJS;
import static nl.procura.gba.web.services.beheer.kassa.KassaType.SPOED_REISDOC;
import static nl.procura.gba.web.services.beheer.kassa.KassaType.SPOED_RIJBEWIJS;
import static nl.procura.gba.web.services.beheer.kassa.KassaType.UITTREKSEL;
import static nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagSoort.ONBEKEND;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.reisdocumenten.SoortReisdocument;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagSoort;

public class KassaUtils {

  public static List<KassaProduct> getOntbrekendeProducten(KassaService db) {

    List<KassaProduct> kassaproducten = db.getKassaProducten();
    List<KassaProduct> ontbrekendeProducten = new ArrayList<>();

    checkRijbewijzen(db, ontbrekendeProducten, kassaproducten);
    checkReisdocumenten(db, ontbrekendeProducten, kassaproducten);
    checkUittreksels(db, ontbrekendeProducten, kassaproducten);

    checkKassaDB(db, ontbrekendeProducten, COVOG);
    checkKassaDB(db, ontbrekendeProducten, GPK);
    checkKassaDB(db, ontbrekendeProducten, JEUGDTARIEF_REISDOC);
    checkKassaDB(db, ontbrekendeProducten, SPOED_RIJBEWIJS);
    checkKassaDB(db, ontbrekendeProducten, SPOED_REISDOC);
    checkKassaDB(db, ontbrekendeProducten, BEZORGING_REISDOC);

    return ontbrekendeProducten;
  }

  private static void checkKassaDB(KassaService db, List<KassaProduct> ontbrekendeProducten, KassaType kassaType) {

    KassaProduct saveNewKassaProduct = createNewKassaProduct(db, null, null, ONBEKEND, kassaType);
    if (saveNewKassaProduct != null) {
      ontbrekendeProducten.add(saveNewKassaProduct);
    }
  }

  private static void checkReisdocumenten(KassaService db, List<KassaProduct> ontbrekendeProducten,
      List<KassaProduct> kassaproducten) {

    for (SoortReisdocument soort : db.getServices().getReisdocumentService().getSoortReisdocumenten()) {

      Long codeReisdocument = on(KassaProduct.class).getKassaReisdocument().getCReisdoc();
      if (!exists(kassaproducten, having(codeReisdocument, equalTo(soort.getCReisdoc())))) {
        KassaProduct saveNewKassaProduct = createNewKassaProduct(db, soort, null, ONBEKEND, REISDOCUMENT);
        if (saveNewKassaProduct != null) {
          ontbrekendeProducten.add(saveNewKassaProduct);
        }
      }
    }
  }

  private static void checkRijbewijzen(KassaService db, List<KassaProduct> ontbrekendeProducten,
      List<KassaProduct> kassaproducten) {

    for (RijbewijsAanvraagSoort soort : RijbewijsAanvraagSoort.values()) {

      if (soort.getCode() > 0) {
        RijbewijsAanvraagSoort rijbewijsAanvraagSoort = on(KassaProduct.class).getKassaRijbewijs();
        if (!exists(kassaproducten, having(rijbewijsAanvraagSoort, equalTo(soort)))) {
          KassaProduct saveNewKassaProduct = createNewKassaProduct(db, null, null, soort, RIJBEWIJS);
          if (saveNewKassaProduct != null) {
            ontbrekendeProducten.add(saveNewKassaProduct);
          }
        }
      }
    }
  }

  private static void checkUittreksels(KassaService db, List<KassaProduct> ontbrekendeProducten,
      List<KassaProduct> kassaproducten) {

    for (DocumentRecord soort : db.getServices().getDocumentService().getDocumenten(DocumentType.PL_UITTREKSEL)) {
      long codeDocument = on(KassaProduct.class).getKassaDocument().getCDocument();
      if (!exists(kassaproducten, having(codeDocument, equalTo(soort.getCDocument())))) {
        KassaProduct saveNewKassaProduct = createNewKassaProduct(db, null, soort, ONBEKEND, UITTREKSEL);
        if (saveNewKassaProduct != null) {
          ontbrekendeProducten.add(saveNewKassaProduct);
        }
      }
    }
  }

  private static KassaProduct createNewKassaProduct(KassaService db, SoortReisdocument reisdoc, DocumentRecord doc,
      RijbewijsAanvraagSoort rijb, KassaType kt) {
    return db.createKassaProduct(reisdoc, doc, rijb, kt);
  }
}
