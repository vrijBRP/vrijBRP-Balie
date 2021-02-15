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

package nl.procura.gba.web.services.zaken.documenten.kenmerk;

import java.util.List;
import java.util.Optional;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personen.db.Document;
import nl.procura.gba.jpa.personen.db.Kenmerk;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.interfaces.DatabaseTable;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.KoppelbaarAanDocument;
import nl.procura.gba.web.services.zaken.documenten.KoppelbaarAanDocumentKenmerk;
import nl.procura.java.reflection.ReflectionUtil;

public class DocumentKenmerk extends Kenmerk implements KoppelbaarAanDocument, DatabaseTable {

  private static final String BEGIN_COUPLIN_ERROR_MESSAGE = "Object van type ";
  private static final String END_COUPLIN_ERROR_MESSAGE   = " kan niet gekoppeld worden aan een document.";

  public DocumentKenmerk() {
  }

  public Long getCode() {
    return getCKenmerk();
  }

  public String getDocumentKenmerk() {
    return getKenmerk();
  }

  public void setDocumentKenmerk(String kenmerk) {
    setKenmerk(kenmerk);
  }

  public DocumentKenmerkType getKenmerkType() {
    return DocumentKenmerkType.get(getType());
  }

  public void setKenmerkType(DocumentKenmerkType type) {
    setType(type.getCode());
  }

  @Override
  public boolean isGekoppeld(DocumentRecord document) {
    return MiscUtils.contains(document, getDocuments());
  }

  public <K extends KoppelbaarAanDocumentKenmerk> boolean isGekoppeld(List<K> objectList) {

    Optional<K> entity = objectList.stream().filter(object -> (object instanceof DocumentRecord)).findFirst();
    if (entity.isPresent()) {
      return isGekoppeld((DocumentRecord) entity.get());
    }
    throw new IllegalArgumentException(BEGIN_COUPLIN_ERROR_MESSAGE + entity.getClass() + END_COUPLIN_ERROR_MESSAGE);
  }

  @Override
  public void koppelActie(DocumentRecord document, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getDocuments().add(ReflectionUtil.deepCopyBean(Document.class, document));
    } else {
      getDocuments().remove(document);
    }
  }

  public <K extends KoppelbaarAanDocumentKenmerk> void koppelActie(K koppelObject, KoppelActie koppelActie) {

    if (koppelObject instanceof DocumentRecord) {
      koppelActie((DocumentRecord) koppelObject, koppelActie);
    } else {
      throw new IllegalArgumentException(
          BEGIN_COUPLIN_ERROR_MESSAGE + koppelObject.getClass() + END_COUPLIN_ERROR_MESSAGE);
    }
  }
}
