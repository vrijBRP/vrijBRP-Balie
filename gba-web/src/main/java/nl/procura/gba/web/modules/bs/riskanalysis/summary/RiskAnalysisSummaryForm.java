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

package nl.procura.gba.web.modules.bs.riskanalysis.summary;

import java.text.MessageFormat;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personen.db.RiskProfile;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;

public abstract class RiskAnalysisSummaryForm extends GbaForm<RiskAnalysisSummaryBean> {

  public RiskAnalysisSummaryForm(DossierRiskAnalysis zaakDossier, Zaak relatedCase) {
    setColumnWidths(WIDTH_130, "");
    setCaptionAndOrder();
    setBean(zaakDossier, relatedCase);
  }

  protected abstract void setCaptionAndOrder();

  public void setBean(DossierRiskAnalysis zaakDossier, Zaak relatedCase) {
    RiskAnalysisSummaryBean bean = new RiskAnalysisSummaryBean();
    RiskProfile riskProfile = zaakDossier.getRiskProfile();

    if (riskProfile != null) {
      bean.setProfile(riskProfile.getName());
      bean.setThreshold(riskProfile.getThreshold().toString());

      StringBuilder relatedCaseString = new StringBuilder();
      relatedCaseString.append(zaakDossier.getRefCaseDescr());
      if (relatedCase == null) {
        relatedCaseString.append(MiscUtils.setClass(false, MessageFormat
            .format(" (Zaak met zaak-id {0} niet gevonden in het zakenregister)", zaakDossier.getRefCaseId())));
      }
      bean.setRelatedCase(relatedCaseString.toString());
    } else {
      bean.setProfile(MiscUtils.setClass(false, "Deze zaak is niet (meer) gekoppeld aan een risicoprofiel"));
    }
    setBean(bean);
  }

  @Override
  public RiskAnalysisSummaryBean getNewBean() {
    return new RiskAnalysisSummaryBean();
  }
}
