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

import static nl.procura.geo.rest.domain.ngr.wfs.types.GebruiksdoelType.WOONFUNCTIE;
import static nl.procura.geo.rest.domain.ngr.wfs.types.PandStatusType.PAND_GEBRUIK_NIET_INGEMETEN;
import static nl.procura.geo.rest.domain.ngr.wfs.types.PandStatusType.PAND_IN_GEBRUIK;
import static nl.procura.geo.rest.domain.ngr.wfs.types.PlaatsStatusType.PLAATS_AANGEWEZEN;
import static nl.procura.geo.rest.domain.ngr.wfs.types.VboStatusType.VBO_IN_GEBRUIK;
import static nl.procura.geo.rest.domain.ngr.wfs.types.VboStatusType.VBO_IN_GEBRUIK_NIET_INGEMETEN;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.procura.gba.jpa.personen.db.DossRiskAnalysisSubject;
import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.geo.rest.domain.ngr.wfs.WfsLigplaatsProperties;
import nl.procura.geo.rest.domain.ngr.wfs.WfsSearchResponse;
import nl.procura.geo.rest.domain.ngr.wfs.WfsStandplaatsProperties;
import nl.procura.geo.rest.domain.ngr.wfs.WfsVboProperties;
import nl.procura.geo.rest.domain.ngr.wfs.types.EnumType;
import nl.procura.geo.rest.domain.ngr.wfs.types.GebruiksdoelType;
import nl.procura.geo.rest.domain.ngr.wfs.types.PandStatusType;
import nl.procura.geo.rest.domain.ngr.wfs.types.PlaatsStatusType;
import nl.procura.geo.rest.domain.ngr.wfs.types.VboStatusType;

/**
 * Rule 13
 */
public class Rule13Processor extends AbstractBagRuleProcessor {

  public Rule13Processor() {
    super(RiskProfileRuleType.RULE_13);
  }

  @Override
  public boolean process(DossierRiskAnalysis riskAnalysis,
      RiskAnalysisRelatedCase relatedCase,
      DossRiskAnalysisSubject subject,
      RiskProfileRule rule) {

    boolean isMatch = false;
    WfsSearchResponse response = searchWfs(relatedCase.getAddress());

    if (response != null) {
      WfsVboProperties vbo = response.getFeatures().get(0).getVerblijfsobject();
      WfsLigplaatsProperties ligp = response.getFeatures().get(0).getLigplaats();
      WfsStandplaatsProperties standp = response.getFeatures().get(0).getStandplaats();

      if (vbo != null) {
        List<GebruiksdoelType> gebruiksdoelen = vbo.getGebruiksdoelen();
        VboStatusType status = vbo.getStatus();
        PandStatusType pandStatus = vbo.getPandstatus();

        info("Gebruiksdoel: " + joinEnums(gebruiksdoelen.stream()));
        info("Status verblijfsobject: " + status.getValue());
        info("Status pand: " + pandStatus.getValue());

        boolean isPandStatus = EnumSet.of(PAND_IN_GEBRUIK, PAND_GEBRUIK_NIET_INGEMETEN).contains(pandStatus);
        boolean isVboStatus = EnumSet.of(VBO_IN_GEBRUIK, VBO_IN_GEBRUIK_NIET_INGEMETEN).contains(status);
        boolean isGebruiksdoel = gebruiksdoelen.contains(WOONFUNCTIE);

        if (!isPandStatus) {
          warn("Het pand heeft niet de status ''{0}'', maar ''{1}''",
              joinEnums(Stream.of(PAND_IN_GEBRUIK, PAND_GEBRUIK_NIET_INGEMETEN)),
              pandStatus.getValue());
        }

        if (!isVboStatus) {
          warn("Het verblijfsobject heeft niet de status ''{0}'', maar ''{1}''",
              joinEnums(Stream.of(VBO_IN_GEBRUIK, VBO_IN_GEBRUIK_NIET_INGEMETEN)),
              status.getValue());
        }

        if (!isGebruiksdoel) {
          warn("Het verblijfsobject heeft niet als gebruiksdoel ''{0}'', maar ''{1}''",
              WOONFUNCTIE.getValue(),
              joinEnums(gebruiksdoelen.stream()));
        }

        isMatch = !(isPandStatus && isVboStatus && isGebruiksdoel);
        if (!isMatch) {
          info("Alle statussen zijn correct");
        }

      } else if (ligp != null) {
        isMatch = !isCorrectStatus(ligp.getStatus());

      } else if (standp != null) {
        isMatch = !isCorrectStatus(standp.getStatus());

      } else {
        warn("Zowel verblijfsobject, ligplaats en standplaatsgegevens zijn niet gevuld");
      }
    }

    return isMatch;
  }

  private <T extends EnumType<?>> String joinEnums(Stream<T> stream) {
    return stream.map(EnumType::getValue).collect(Collectors.joining(", "));
  }

  /**
   * Is match for ligplaats and standplaats
   */
  private boolean isCorrectStatus(PlaatsStatusType status) {

    info("Status verblijfplaats: " + status.getValue());

    boolean isCorrect = PLAATS_AANGEWEZEN.equals(status);
    if (isCorrect) {
      info("Alle statussen zijn correct");
    }

    return isCorrect;
  }
}
