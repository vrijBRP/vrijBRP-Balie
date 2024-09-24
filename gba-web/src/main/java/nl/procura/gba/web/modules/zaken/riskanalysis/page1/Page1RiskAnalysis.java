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

package nl.procura.gba.web.modules.zaken.riskanalysis.page1;

import nl.procura.gba.common.ZaakFragment;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.zaken.bs.page1.Page1BsTemplate;
import nl.procura.gba.web.modules.zaken.riskanalysis.page2.Page2RiskAnalysis;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;

public class Page1RiskAnalysis extends Page1BsTemplate {

  public Page1RiskAnalysis() {
    super("Risicoanalyses");
  }

  @Override
  protected DocumentType getDocumentType(ZaakType zaakType) {
    return DocumentType.RISK_ANALYSE;
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Page2RiskAnalysis());
  }

  @Override
  protected String getFragment(ZaakType zaakType) {
    return ZaakFragment.FR_RISICO_ANALYSE;
  }

  @Override
  protected ProfielActie getProfielActie() {
    return ProfielActie.UPDATE_CASE_RISK_ANALYSIS;
  }

  @Override
  protected ZaakType[] getZaakTypes() {
    return new ZaakType[]{ ZaakType.RISK_ANALYSIS };
  }
}
