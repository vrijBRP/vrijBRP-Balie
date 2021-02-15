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

package nl.procura.gba.web.modules.bs.riskanalysis.page1;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.INSCHRIJVER;

import nl.procura.gba.jpa.personen.db.DossRiskAnalysisSubject;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisPersoon;

public class Page1RiskAnalysisTable2 extends GbaTable {

  private RiskAnalysisRelatedCase relocation;

  public Page1RiskAnalysisTable2() {
  }

  public void setRelocation(RiskAnalysisRelatedCase relocation) {
    this.relocation = relocation;
    init();
  }

  @Override
  public void setColumns() {

    addColumn("Persoon", 300);
    addColumn("Aangifte");
    addColumn("Geslacht", 80);
    addColumn("Geboren", 130);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    if (relocation != null) {
      if (relocation.isRelocation()) {
        for (VerhuisPersoon p : relocation.getRelocation().getPersonen()) {
          DossierPersoon persoon = new DossierPersoon();
          BsPersoonUtils.kopieDossierPersoon(p.getPersoon().getBasisPl(), persoon);

          DossRiskAnalysisSubject item = new DossRiskAnalysisSubject();
          item.setPerson(persoon);

          Record r = addRecord(item);
          String naam = p.getPersoon().getPersoon().getFormats().getNaam().getPred_eerstevoorn_adel_voorv_gesl();
          String leeftijd = p.getPersoon().getPersoon().getFormats().getGeboorte().getDatum_leeftijd();

          r.addValue(naam);
          r.addValue(p.getAangifte());
          r.addValue(p.getPersoon().getPersoon().getGeslacht());
          r.addValue(leeftijd);
        }
      } else if (relocation.isRegistration()) {
        for (DossierPersoon p : relocation.getRegistration().getDossier().getPersonen(INSCHRIJVER)) {
          DossierPersoon persoon = new DossierPersoon();
          BsPersoonUtils.kopieDossierPersoon(p.getPersoon().getBasisPl(), persoon);

          DossRiskAnalysisSubject item = new DossRiskAnalysisSubject();
          item.setPerson(persoon);

          Record r = addRecord(item);
          String naam = persoon.getNaam().getPred_eerstevoorn_adel_voorv_gesl();
          String leeftijd = persoon.getGeboorte().getDatum_leeftijd();

          r.addValue(naam);
          r.addValue("N.v.t.");
          r.addValue(persoon.getGeslacht());
          r.addValue(leeftijd);
        }
      }
    }
  }
}
