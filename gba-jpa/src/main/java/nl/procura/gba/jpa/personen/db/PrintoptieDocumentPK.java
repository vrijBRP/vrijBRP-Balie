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

package nl.procura.gba.jpa.personen.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class PrintoptieDocumentPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "c_document",
      unique = true,
      nullable = false,
      precision = 1000)
  private long cDocument;

  @Column(name = "c_printoptie",
      unique = true,
      nullable = false,
      precision = 1000)
  private long cPrintoptie;

  public PrintoptieDocumentPK() {
  }

  public long getCDocument() {
    return this.cDocument;
  }

  public void setCDocument(long cDocument) {
    this.cDocument = cDocument;
  }

  public long getCPrintoptie() {
    return this.cPrintoptie;
  }

  public void setCPrintoptie(long cPrintoptie) {
    this.cPrintoptie = cPrintoptie;
  }
}
