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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page250;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.modules.bs.riskanalysis.summary.RiskAnalysisSummaryBuilder;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page101.Page101Zaken;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;

/**
 * Tonen onderzoek
 */
public class Page250Zaken extends ZakenregisterOptiePage<Dossier> {

  public Page250Zaken(Dossier zaak) {
    super(zaak, "Zakenregister - risicoanalyse");
    addButton(buttonPrev);
  }

  @Override
  protected void addOptieButtons() {
    addOptieButton(buttonDoc);
    addOptieButton(buttonVerwerken);
  }

  @Override
  protected void addTabs(ZaakTabsheet<Dossier> tabsheet) {
    RiskAnalysisSummaryBuilder.addTab(tabsheet, getZaak().getZaakDossier());
  }

  @Override
  protected void goToDocument() {
    List<DocumentType> types = new ArrayList<>();
    types.add(DocumentType.RISK_ANALYSE);
    getNavigation().goToPage(new Page101Zaken(getZaak(), getTitle(), types.toArray(new DocumentType[0])));
  }
}
