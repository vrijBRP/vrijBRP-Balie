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

package nl.procura.gba.web.modules.bs.overlijden.levenloos.page95;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.modules.bs.geboorte.page95.Page95Geboorte;
import nl.procura.gba.web.services.bs.levenloos.DossierLevenloos;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;

public class Page95Levenloos extends Page95Geboorte<DossierLevenloos> {

  private static final String INTERNATIONAAL = "internationaal";

  public Page95Levenloos() {
    super("Levenloos geboren kind - afdrukken", "Afdrukken documenten geboorteaangifte");
  }

  @Override
  public boolean onSelectDocument(DocumentRecord document, boolean isPreSelect) {

    String doc = document.getDocument().toLowerCase();

    // Als er er sprake is van 'buiten benelux' dan internationaal uittreksel selecteren
    if (isDoc(doc, INTERNATIONAAL)) {
      return getZaakDossier().isBuitenBenelux();
    }

    return super.onSelectDocument(document, isPreSelect);
  }

  @Override
  protected DocumentType[] getDocumentTypes() {

    List<DocumentType> types = new ArrayList<>();
    types.add(DocumentType.LEVENLOOS);

    if (getZaakDossier().isSprakeLatereVermelding()) {
      types.add(DocumentType.LATERE_VERMELDING_ERK);
    }

    return types.toArray(new DocumentType[0]);
  }
}
