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

package nl.procura.gba.web.services.zaken.documenten.dmstypes;

import static nl.procura.gba.common.ZaakType.ONBEKEND;
import static nl.procura.standard.Globalfunctions.along;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.DocumentDmsType;
import nl.procura.gba.web.services.interfaces.DatabaseTable;

public class DmsDocumentType extends DocumentDmsType
    implements Comparable<DmsDocumentType>, DatabaseTable<Long> {

  public DmsDocumentType() {
    setDocumentDmsType("");
  }

  public Long getCode() {
    return getcDocumentDmsType();
  }

  @Override
  public int compareTo(DmsDocumentType type) {
    return getDocumentDmsType().compareToIgnoreCase(type.getDocumentDmsType());
  }

  public String toString() {
    return getDocumentDmsType();
  }

  public List<ZaakType> getZaakTypesAsList() {
    return Arrays.stream(Objects.toString(getZaakTypes(), "").split(","))
        .map(val -> ZaakType.get(along(val)))
        .filter(zt -> !zt.is(ONBEKEND))
        .collect(Collectors.toList());
  }

  public void setZaakTypes(List<ZaakType> zaakTypes) {
    setZaakTypes(zaakTypes.stream()
        .map(fv -> String.valueOf(fv.getCode()))
        .collect(Collectors.joining(",")));
  }

  public boolean isZaakType(ZaakType zaakType) {
    return getZaakTypesAsList().isEmpty() || getZaakTypesAsList().contains(zaakType);
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

    DmsDocumentType other = (DmsDocumentType) obj;
    return getcDocumentDmsType().equals(other.getcDocumentDmsType());
  }
}
