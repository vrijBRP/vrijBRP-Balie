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

package nl.procura.gba.web.services.zaken.documenten.doelen;

import nl.procura.gba.web.services.interfaces.DatabaseTable;

public class DocumentDoel extends nl.procura.gba.jpa.personen.db.DocumentDoel
    implements Comparable<DocumentDoel>, DatabaseTable {

  private static final long serialVersionUID = 6035126734782129174L;

  public DocumentDoel() {
  }

  @Override
  public int compareTo(DocumentDoel documentDoel) {
    return getDocumentDoel().compareToIgnoreCase(documentDoel.getDocumentDoel());
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }

    DocumentDoel other = (DocumentDoel) obj;
    return getCDocumentDoel().equals(other.getCDocumentDoel());
  }

  @Override
  public String toString() {
    return getDocumentDoel();
  }
}
