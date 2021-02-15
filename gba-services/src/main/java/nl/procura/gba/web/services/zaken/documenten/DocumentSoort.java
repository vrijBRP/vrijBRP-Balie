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

package nl.procura.gba.web.services.zaken.documenten;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ComparisonChain;

import nl.procura.gba.common.MiscUtils;

public class DocumentSoort implements Comparable<DocumentSoort> {

  private DocumentType         type       = null;
  private List<DocumentRecord> documenten = new ArrayList<>();

  public DocumentSoort(DocumentType type) {
    setType(type);
  }

  @Override
  public int compareTo(DocumentSoort o) {
    return ComparisonChain.start().compare(getType().getOrder(), o.getType().getOrder()).compare(getType().getOms(),
        o.getType().getOms()).result();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof DocumentSoort) {
      return getType().equals(((DocumentSoort) obj).getType());
    }

    return false;
  }

  public List<DocumentRecord> getDocumenten() {
    MiscUtils.sort(documenten);
    return documenten;
  }

  public void setDocumenten(List<DocumentRecord> documenten) {
    this.documenten = documenten;
  }

  public DocumentType getType() {
    return type;
  }

  public void setType(DocumentType type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return getType().getOms();
  }
}
