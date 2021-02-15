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

package nl.procura.gba.web.services.bs.riskanalysis;

import static nl.procura.standard.Globalfunctions.toBigDecimal;

import javax.persistence.Transient;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.DossRiskAnalysis;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class DossierRiskAnalysis extends DossRiskAnalysis implements ZaakDossier {

  private Dossier dossier = new Dossier(ZaakType.RISK_ANALYSIS);

  public DossierRiskAnalysis() {
    super();
    setDossier(new Dossier(ZaakType.RISK_ANALYSIS, this));
  }

  @Override
  public void beforeSave() {
    setcDossRa(getDossier().getCode());
  }

  @Override
  public AnrFieldValue getAnummer() {
    return new AnrFieldValue();
  }

  @Override
  public void setAnummer(AnrFieldValue anummer) {
  }

  @Override
  public BsnFieldValue getBurgerServiceNummer() {
    return new BsnFieldValue();
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue burgerServiceNummer) {
  }

  public Long getCode() {
    return getcDossRa();
  }

  @Override
  public Dossier getDossier() {
    return dossier;
  }

  @Override
  public void setDossier(Dossier dossier) {
    this.dossier = dossier;
  }

  @Override
  public boolean isVolledig() {
    return true;
  }

  @Transient
  public void setRelocation(RiskAnalysisRelatedCase relatedCase) {
    setRefCaseId(relatedCase.getZaakId());
    setRefCaseType(toBigDecimal(relatedCase.getZaak().getType().getCode()));
    setRefCaseDescr(String.format("%s naar %s",
        relatedCase.getDescr(),
        relatedCase.getAddress().getAdres()));
  }
}
