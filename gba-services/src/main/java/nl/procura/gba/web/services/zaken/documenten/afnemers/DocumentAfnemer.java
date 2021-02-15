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

package nl.procura.gba.web.services.zaken.documenten.afnemers;

import static nl.procura.standard.Globalfunctions.*;

import nl.procura.gba.jpa.personen.db.DocumentAfn;
import nl.procura.gba.web.services.interfaces.DatabaseTable;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.validation.Postcode;

public class DocumentAfnemer extends DocumentAfn implements Comparable<DocumentAfnemer>, DatabaseTable {

  private static final long serialVersionUID = 387048809338735972L;

  public DocumentAfnemer() {
  }

  @Override
  public int compareTo(DocumentAfnemer documentAfnemer) {
    return getDocumentAfn().compareToIgnoreCase(documentAfnemer.getDocumentAfn());
  }

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

    DocumentAfnemer other = (DocumentAfnemer) obj;
    return getCDocumentAfn().equals(other.getCDocumentAfn());
  }

  public KoppelEnumeratieType getGrondslagType() {
    return KoppelEnumeratieType.get(along(getcGrondslag()));
  }

  public void setGrondslagType(KoppelEnumeratieType grondslag) {
    setcGrondslag(toBigDecimal(grondslag == null ? null : grondslag.getCode()));
  }

  public FieldValue getPostcode() {
    return new FieldValue(getPc(), Postcode.getFormat(getPc()));
  }

  public void setPostcode(FieldValue postcode) {
    setPc(FieldValue.from(postcode).getStringValue());
  }

  public String getTav() {
    return getTavAanhef() + " " + getTavVoorl() + " " + getTavNaam();
  }

  public FieldValue getTerAttentieVanAanhef() {
    return new FieldValue(getTavAanhef());
  }

  public void setTerAttentieVanAanhef(FieldValue tav) {
    setTavAanhef(FieldValue.from(tav).getStringValue());
  }

  public KoppelEnumeratieType getToekenning() {
    return KoppelEnumeratieType.get(along(getcTk()));
  }

  public void setToekenning(KoppelEnumeratieType toekenning) {
    setcTk(toBigDecimal(toekenning.getCode()));
  }

  public boolean isVerstrekBep() {
    return pos(getVerstrek());
  }

  public void setVerstrekBep(boolean isVertrekBep) {
    setVerstrek(toBigDecimal(isVertrekBep ? 1 : 0));
  }

  public String toString() {
    return getDocumentAfn();
  }
}
