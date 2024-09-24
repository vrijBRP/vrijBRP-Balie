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

package nl.procura.gba.web.services.zaken.documenten;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import nl.procura.gba.jpa.personen.db.Translation;
import nl.procura.gba.jpa.personen.db.TranslationRec;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = { "entity" })
public class DocumentTranslation {

  private final Translation entity;

  public DocumentTranslation(Translation entity) {
    this.entity = entity;
  }

  public List<TranslationRec> getSortedRecords() {
    ArrayList<TranslationRec> records = new ArrayList<>(getEntity().getTranslations());
    records.sort(Comparator.comparing(TranslationRec::getNl));
    return records;
  }

  @Override
  public String toString() {
    return getEntity().getName();
  }
}
