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

package nl.procura.gba.jpa.personen.dao;

import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import nl.procura.gba.common.ZaakType;

/**
 * Sleutel van de zaak met zaak-d + datum ingang + datum / tijd invoer
 */
public class ZaakKey {

  private String   zaakId   = "";
  private ZaakType zaakType = ZaakType.ONBEKEND;

  private BigDecimal dIng = null; // Datum ingang zaak
  private BigDecimal dInv = null; // Datum invoer
  private BigDecimal tInv = null; // Tijd invoer
  private long       code = 0;    // Code van de zaak

  public ZaakKey() {
  }

  public ZaakKey(String zaakId) {
    this.zaakId = zaakId != null ? zaakId.trim() : "";
  }

  public ZaakKey(String zaakId, ZaakType zaakType) {
    this(zaakId);
    this.zaakType = zaakType;
  }

  public ZaakKey(String zaakId, BigDecimal dIng, BigDecimal dInv, BigDecimal tInv) {
    this(zaakId, dIng, dInv, tInv, toBigDecimal(-1));
  }

  public ZaakKey(Number code, String zaakId, BigDecimal dIng, BigDecimal dInv, BigDecimal tInv) {
    this(code, zaakId, dIng, dInv, tInv, toBigDecimal(-1));
  }

  public ZaakKey(String zaakId, BigDecimal dIng, BigDecimal dInv, BigDecimal tInv, BigDecimal zaakType) {
    this(toBigDecimal(0), zaakId, dIng, dInv, tInv, zaakType);
  }

  public ZaakKey(Number code, String zaakId, BigDecimal dIng, BigDecimal dInv, BigDecimal tInv, BigDecimal zaakType) {
    this(zaakId, ZaakType.get(zaakType.longValue()));
    this.code = code.longValue();
    this.dIng = dIng;
    this.dInv = dInv;
    this.tInv = tInv;
  }

  public String getZaakId() {
    return zaakId;
  }

  public void setZaakId(String zaakId) {
    this.zaakId = zaakId;
  }

  public BigDecimal getdIng() {
    return dIng;
  }

  public void setdIng(BigDecimal dIng) {
    this.dIng = dIng;
  }

  public BigDecimal getdInv() {
    return dInv;
  }

  public void setdInv(BigDecimal dInv) {
    this.dInv = dInv;
  }

  public BigDecimal gettInv() {
    return tInv;
  }

  public void settInv(BigDecimal tInv) {
    this.tInv = tInv;
  }

  @Override
  public int hashCode() {

    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append((zaakId == null) ? 0 : zaakId.hashCode());

    return builder.build();
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
    ZaakKey other = (ZaakKey) obj;

    EqualsBuilder builder = new EqualsBuilder();
    builder.append(zaakId, other.zaakId).build();
    return builder.build();
  }

  public ZaakType getZaakType() {
    return zaakType;
  }

  public void setZaakType(ZaakType zaakType) {
    this.zaakType = zaakType;
  }

  public long getCode() {
    return code;
  }

  public void setCode(long code) {
    this.code = code;
  }

  @Override
  public String toString() {
    return "ZaakKey [zaakId=" + zaakId + ", zaakType=" + zaakType + ", dIng=" + dIng + ", dInv=" + dInv + ", tInv="
        + tInv + "]";
  }
}
