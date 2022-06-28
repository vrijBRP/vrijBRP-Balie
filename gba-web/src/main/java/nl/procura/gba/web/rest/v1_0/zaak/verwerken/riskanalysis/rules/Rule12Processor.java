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

package nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules;

import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;
import static nl.procura.gba.jpa.personen.types.RiskProfileRuleVar.X;
import static nl.procura.standard.Globalfunctions.astr;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.jpa.personen.db.DossRiskAnalysisSubject;
import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.gba.web.services.zaken.rijbewijs.NrdServices;
import nl.procura.rdw.messages.P0252;
import nl.procura.rdw.processen.p0252.f08.*;
import nl.procura.standard.ProcuraDate;

/**
 * Rule 12
 */
public class Rule12Processor extends AbstractRuleProcessor {

  public Rule12Processor() {
    super(RiskProfileRuleType.RULE_12);
  }

  @Override
  public boolean process(DossierRiskAnalysis riskAnalysis,
      RiskAnalysisRelatedCase relatedCase,
      DossRiskAnalysisSubject subject,
      RiskProfileRule rule) {

    int days = rule.getIntAttribute(X.name()); // Number of days
    BasePLExt pl = getUtils().getPL(subject);
    return checkTravelDocuments(days, pl) | checkDriversLicense(days, pl);
  }

  /**
   * Check the expiration of the travel documents
   */
  private boolean checkTravelDocuments(int days, BasePLExt pl) {

    boolean isFail = false;
    for (BasePLSet set : pl.getCat(GBACat.REISDOC).getSets()) {
      BasePLRec record = set.getLatestRec();
      String nrDoc = record.getElemVal(NR_NL_REISDOC).getDescr();
      BasePLValue aand = record.getElemVal(AAND_INH_VERMIS_NL_REISDOC);

      if (StringUtils.isBlank(aand.getVal())) {
        BasePLValue dateElement = record.getElemVal(DATUM_EINDE_GELDIG_NL_REISDOC);
        if (StringUtils.isNotBlank(dateElement.getVal())) {
          Date date = new Date(dateElement.getVal(), days);
          if (!date.expired && date.expiredInPeriod) {
            warn("Reisdocument {0}: verloopt over {1} dagen op {2}", nrDoc,
                astr(date.daysUntilExp), date.descrValue);
            isFail = true;
          }
        }
      }
    }
    return isFail;
  }

  /**
   * Check the expiration of the drivers license
   */
  private boolean checkDriversLicense(int days, BasePLExt pl) {

    boolean isFail = false;

    if (getServices().getRijbewijsService().getAccount().isIngevuld()) {

      BasePLValue bsn = pl.getPersoon().getBsn();
      if (bsn.toLong() > 0) {
        P0252 p0252 = new P0252();
        p0252.newF1(bsn.getCode());

        boolean rdwSuccess = NrdServices.sendMessage(getServices().getRijbewijsService(), p0252);

        if (rdwSuccess) {
          NATPRYBMAATR maatregelen = (NATPRYBMAATR) p0252.getResponse().getObject();
          Optional<UITGRYBGEG> response = maatregelen.getUitgrybtab().getUitgrybgeg().stream().findFirst();

          if (response.isPresent()) {
            RYBGEG driversLicense = response.get().getRybgeg();
            CATRYBTAB categories = response.get().getCatrybtab();
            int dEnd = driversLicense.getVerldiefstdat().intValue();

            if (dEnd > 0) {
              warn("Rijbewijs: verloren op {0}", new ProcuraDate(dEnd).getFormatDate());
            } else {
              // Only filter on category B (car drivers license)
              Optional<CATRYBGEG> catB = categories.getCatrybgeg()
                  .stream()
                  .filter(c -> c.getRybcatr().startsWith("B"))
                  .findFirst();

              if (catB.isPresent()) {
                Date date = new Date(catB.get().getEindgelddatc().toString(), days);
                if (!date.expired) {
                  if (date.expiredInPeriod) {
                    isFail = true;
                    warn("Rijbewijs: categorie B verloopt over {0} dagen op {1}", astr(date.daysUntilExp),
                        date.descrValue);
                  } else {
                    info("Rijbewijs: categorie B is {0} dagen geldig tot {1}", astr(date.daysUntilExp),
                        date.descrValue);
                  }
                } else {
                  warn("Rijbewijs: categorie B is verlopen op {0}", date.descrValue);
                }
              } else {
                info("Rijbewijs: geen categorie B");
              }
            }
          }
        } else {
          if (p0252.getResponse().isRipMelding()) {
            warn("Fout bij raadplegen RDW: {0}", p0252.getResponse().getMelding());
          }
        }
      } else {
        warn("Geen BSN om het RDW mee te raadplegen");
      }
    }
    return isFail;
  }

  public static class Date {

    public String    stringValue;
    public long      longValue;
    public LocalDate dateValue;
    public String    descrValue;
    public long      parmEndDate;
    public long      daysUntilExp;
    public boolean   expiredInPeriod;
    public boolean   expired;
    private final String DATE_FORMAT1 = "yyyyMMdd";
    private final String DATE_FORMAT2 = "dd-MM-yyyy";

    public Date(String sDate, int days) {
      this.stringValue = sDate;
      this.longValue = Long.valueOf(sDate);
      this.dateValue = LocalDate.parse(sDate, ofPattern(DATE_FORMAT1));
      this.descrValue = dateValue.format(ofPattern(DATE_FORMAT2));
      this.parmEndDate = Long.valueOf(now().plus(Period.ofDays(days)).format(ofPattern(DATE_FORMAT1)));
      this.daysUntilExp = ChronoUnit.DAYS.between(now(), dateValue);
      this.expired = daysUntilExp <= 0;
      this.expiredInPeriod = longValue > 0 && longValue < parmEndDate;
    }
  }
}
