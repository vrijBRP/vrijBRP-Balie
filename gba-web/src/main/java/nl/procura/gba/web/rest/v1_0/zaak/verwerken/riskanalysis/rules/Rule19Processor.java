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

package nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules;

import static nl.procura.gba.jpa.personen.types.RiskProfileRuleVar.X;
import static nl.procura.standard.Globalfunctions.aval;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.jpa.personen.db.DossRiskAnalysisSubject;
import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.standard.ProcuraDate;

import lombok.Data;

/**
 * Rule 17
 */
public class Rule19Processor extends AbstractBagRuleProcessor {

  public Rule19Processor() {
    super(RiskProfileRuleType.RULE_19);
  }

  @Override
  public boolean process(
      DossierRiskAnalysis riskAnalysis,
      RiskAnalysisRelatedCase relatedCase,
      DossRiskAnalysisSubject subject,
      RiskProfileRule rule) {

    String zaakId = relatedCase.getZaak().getZaakId();
    final int days = rule.getIntAttribute(X.name());
    BasePLExt pl = getUtils().getPL(subject);

    List<RelocationInfo> relocations = new ArrayList<>();
    relocations.addAll(getCases(pl, zaakId));
    relocations.addAll(getPersonListRelocation(pl));

    return relocations.stream()
        .filter(info -> info.getDate().isCorrect())
        .anyMatch(info -> isTooRecent(info, days));
  }

  private boolean isTooRecent(RelocationInfo info, int days) {
    int daysSinceLastAddress = info.getDaysSinceLastAddress();
    boolean tooRecent = daysSinceLastAddress < days;
    if (tooRecent) {
      info(String.format("Laatste verhuizing (%s) was korter dan %d dag(en) geleden, namelijk %d dag(en)",
          info.getAddress(),
          days,
          daysSinceLastAddress));
      return true;
    }
    return false;
  }

  private List<RelocationInfo> getCases(BasePLExt pl, String zaakId) {
    List<RelocationInfo> relocations = new ArrayList<>();
    ZaakArgumenten args = new ZaakArgumenten();
    args.setBsn(pl.getPersoon().getBsn().toLong());
    List<VerhuisAanvraag> zaken = getServices().getVerhuizingService().getMinimalZaken(args);
    for (VerhuisAanvraag zaak : zaken) {
      if (!zaak.getZaakId().equals(zaakId)) {
        RelocationInfo rec = new RelocationInfo();
        rec.setDate(new ProcuraDate(zaak.getDatumIngang().getIntDate()));
        rec.setAddress(zaak.getNieuwAdres().getAdres().getAdres_pc_wpl());
        relocations.add(rec);
      }
    }
    return relocations;
  }

  private List<RelocationInfo> getPersonListRelocation(BasePLExt pl) {
    List<RelocationInfo> relocations = new ArrayList<>();
    BasePLRec record = pl.getCat(GBACat.VB).getLatestRec();
    int datumAanvang = aval(record.getElemVal(GBAElem.DATUM_AANVANG_ADRESH).getVal());
    int datumVertrek = aval(record.getElemVal(GBAElem.DATUM_VERTREK_UIT_NL).getVal());
    RelocationInfo rec = new RelocationInfo();
    int max = NumberUtils.max(datumAanvang, datumVertrek);
    if (max > 0) {
      rec.setDate(new ProcuraDate(max));
      rec.setAddress(pl.getVerblijfplaats().getAdres().getAdresPcWpl());
      relocations.add(rec);
    }
    return relocations;
  }

  @Data
  private static class RelocationInfo {

    private ProcuraDate date;
    private String      address;

    public int getDaysSinceLastAddress() {
      return date.diffInDays(new ProcuraDate());
    }
  }
}
