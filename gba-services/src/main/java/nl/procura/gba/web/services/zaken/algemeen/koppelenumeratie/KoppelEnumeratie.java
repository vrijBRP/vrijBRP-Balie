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

package nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personen.db.Document;
import nl.procura.gba.jpa.personen.db.Koppelenum;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.KoppelbaarAanDocument;
import nl.procura.java.reflection.ReflectionUtil;

public class KoppelEnumeratie extends Koppelenum implements KoppelbaarAanDocument {

  public KoppelEnumeratie() {
  }

  public KoppelEnumeratie(KoppelEnumeratieType type) {
    setKoppelenum(type.getCode());
  }

  public KoppelEnumeratieType getType() {
    return KoppelEnumeratieType.get(getKoppelenum());
  }

  @Override
  public boolean isGekoppeld(DocumentRecord document) {
    return MiscUtils.contains(document, getDocuments());
  }

  @Override
  public void koppelActie(DocumentRecord document, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getDocuments().add(ReflectionUtil.deepCopyBean(Document.class, document));
    } else {
      getDocuments().remove(document);
    }
  }

  public String toString() {
    return getType().toString();
  }
}
