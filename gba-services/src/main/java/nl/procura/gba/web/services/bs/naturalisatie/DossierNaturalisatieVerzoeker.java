/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.services.bs.naturalisatie;

import static java.util.Optional.ofNullable;

import java.math.BigDecimal;

import nl.procura.gba.common.EnumWithCode;
import nl.procura.gba.jpa.personen.db.DossNatur;
import nl.procura.gba.jpa.personen.db.DossNaturVerz;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.naturalisatie.enums.AdviesBurgermeesterType;
import nl.procura.gba.web.services.bs.naturalisatie.enums.AndereOuderAkkoordType;
import nl.procura.gba.web.services.bs.naturalisatie.enums.BeslissingType;
import nl.procura.gba.web.services.bs.naturalisatie.enums.InburgeringType;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DossierNaturalisatieVerzoeker extends DossNaturVerz {

  private DossierPersoon toestemminggever;

  public DossierNaturalisatieVerzoeker() {
    super();
    setVnr("");
    setBsn(new BigDecimal(-1L));
    setBehAndereVertBsn(new BigDecimal(-1L));
  }

  public BsnFieldValue getBurgerServiceNummer() {
    return new BsnFieldValue(getBsn().longValue());
  }

  public BsnFieldValue getBsnToestemminggever() {
    return new BsnFieldValue(getBehAndereVertBsn().longValue());
  }

  public void setBsnToestemminggever(BsnFieldValue bsn) {
    setBehAndereVertBsn(bsn.getBigDecimalValue());
    setToestemminggever(null);
  }

  public DossierPersoon getToestemminggever() {
    return toestemminggever;
  }

  public void setToestemminggever(DossierPersoon toestemminggever) {
    this.toestemminggever = toestemminggever;
  }

  public Long getCode() {
    return getCDossNaturVerz();
  }

  public void setDossier(DossierNaturalisatie dossier) {
    setDossNatur(ReflectionUtil.deepCopyBean(DossNatur.class, dossier));
  }

  public AndereOuderAkkoordType getAndereOuderAkkoordType() {
    return AndereOuderAkkoordType.get(getBehAndereVertAkk());
  }

  public void setAndereOuderAkkoordType(AndereOuderAkkoordType type) {
    setBehAndereVertAkk(toBigDecimal(type));
  }

  public InburgeringType getInburgeringType() {
    return InburgeringType.get(getToetsInburgering());
  }

  public void setInburgeringType(InburgeringType type) {
    setToetsInburgering(toBigDecimal(type));
  }

  public BeslissingType getBeslissingType() {
    return BeslissingType.get(getBehBeslissOptie());
  }

  public void setBeslissingType(BeslissingType type) {
    setBehBeslissOptie(toBigDecimal(type));
  }

  public AdviesBurgermeesterType getAdviesBurgermeesterType() {
    return AdviesBurgermeesterType.get(getBehAdviesNatur());
  }

  public void setAdviesBurgermeesterType(AdviesBurgermeesterType type) {
    setBehAdviesNatur(toBigDecimal(type));
  }

  private BigDecimal toBigDecimal(EnumWithCode<Integer> value) {
    return BigDecimal.valueOf(ofNullable(value)
        .map(EnumWithCode::getCode)
        .orElse(-1));
  }
}
