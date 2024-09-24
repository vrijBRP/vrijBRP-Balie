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

import static nl.procura.geo.rest.domain.ngr.wfs.SearchType.HUISLETTER;
import static nl.procura.geo.rest.domain.ngr.wfs.SearchType.HUISNUMMER;
import static nl.procura.geo.rest.domain.ngr.wfs.SearchType.POSTCODE;
import static nl.procura.geo.rest.domain.ngr.wfs.SearchType.TOEVOEGING;
import static nl.procura.standard.Globalfunctions.aval;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.gba.web.services.beheer.bag.BagService;
import nl.procura.geo.rest.domain.baglv.adresV2.AdresParametersV2;
import nl.procura.geo.rest.domain.baglv.adresV2.AdresV2;
import nl.procura.geo.rest.domain.baglv.adresV2.LVBagRequestV2;
import nl.procura.geo.rest.domain.baglv.adresV2.LVBagResponseV2;
import nl.procura.geo.rest.domain.ngr.wfs.SearchParam;
import nl.procura.geo.rest.domain.ngr.wfs.WfsFeature;
import nl.procura.geo.rest.domain.ngr.wfs.WfsSearchRequest;
import nl.procura.geo.rest.domain.ngr.wfs.WfsSearchResponse;
import nl.procura.geo.rest.domain.ngr.wfs.types.FeatureType;
import nl.procura.validation.Postcode;

/**
 * Rule 13
 */
public abstract class AbstractBagRuleProcessor extends AbstractRuleProcessor {

  public AbstractBagRuleProcessor(RiskProfileRuleType ruleType) {
    super(ruleType);
  }

  protected LVBagResponseV2 searchBagLV(Adresformats newAddress) {
    BagService geoService = getServices().getGeoService();
    if (!geoService.isGeoServiceActive()) {
      log(ProExceptionSeverity.ERROR, "De parameter Geo / bag => " + "Geo / BAG service actief staat niet op Ja");
    }

    String pc = Postcode.getCompact(newAddress.getPostcode());
    String hnr = String.valueOf(newAddress.getHuisnummer());
    String hnrL = newAddress.getHuisletter();
    String hnrT = newAddress.getHuisnummertoev();

    LVBagRequestV2 request = new LVBagRequestV2()
        .setRequestorName("BRP / Risk analysis")
        .setAdres(new AdresParametersV2()
            .setExacteMatch(true)
            .setPostcode(pc)
            .setHuisnummer(aval(hnr))
            .setHuisletter(hnrL)
            .setHuisnummertoevoeging(hnrT));

    LVBagResponseV2 lvBagResponse = geoService.searchLvBagService(request);
    List<AdresV2> adressen = lvBagResponse.getAdresResponse().getAdressen();
    int total = adressen.size();

    if (total > NumberUtils.INTEGER_ONE) {
      warn("Meerdere adressen {0} gevonden", total);

    } else if (total == NumberUtils.INTEGER_ZERO) {
      warn("Geen adres gevonden", total);

    } else {
      return lvBagResponse;
    }
    return null;
  }

  protected WfsSearchResponse searchWfs(Adresformats newAddress) {
    BagService geoService = getServices().getGeoService();
    if (!geoService.isGeoServiceActive()) {
      log(ProExceptionSeverity.ERROR, "De parameter Geo / bag => " + "Geo / BAG service actief staat niet op Ja");
    }

    String pc = Postcode.getCompact(newAddress.getPostcode());
    String hnr = String.valueOf(newAddress.getHuisnummer());
    String hnrL = newAddress.getHuisletter();
    String hnrT = newAddress.getHuisnummertoev();

    WfsSearchRequest request = new WfsSearchRequest()
        .setRequestorName("BRP / Risk analysis")
        .setOffset(0)
        .setRows(5)
        .setFeatureType(FeatureType.VERBLIJFSOBJECT)
        //Add params
        .search(POSTCODE, pc)
        .search(HUISNUMMER, hnr)
        .search(new SearchParam(HUISLETTER, hnrL).setExclude(StringUtils.isBlank(hnrL)))
        .search(new SearchParam(TOEVOEGING, hnrT).setExclude(StringUtils.isBlank(hnrT)));

    info(formatSearchArgs(pc, hnr, hnrL, hnrT));

    WfsSearchResponse response = geoService.searchWfsService(request);
    Integer total = response.getTotalFeatures();

    if (total > NumberUtils.INTEGER_ONE) {
      warn("Meerdere adressen {0} gevonden", total);

    } else if (total == NumberUtils.INTEGER_ZERO) {
      warn("Geen adres gevonden", total);

    } else {
      WfsFeature feature = response.getFeatures().get(0);
      switch (feature.getType()) {
        case VERBLIJFSOBJECT:
          String id = feature.getVerblijfsobject().getIdentificatie();
          info("1 unieke verblijfplaats (verblijfsobject) gevonden: {0}", id);
          break;
        case LIGPLAATS:
          id = feature.getLigplaats().getIdentificatie();
          info("1 unieke verblijfplaats (ligplaats) gevonden: {0}", id);
          break;
        case STANDPLAATS:
          id = feature.getStandplaats().getIdentificatie();
          info("1 unieke verblijfplaats (standplaats) gevonden: {0}", id);
          break;
      }

      return response;
    }

    return null;
  }

  private String formatSearchArgs(String pc, String hnr, String hnrL, String hnrT) {
    return "Zoekargumenten: postcode: "
        + pc + ", huisnummer: "
        + (StringUtils.isBlank(hnr) ? "N.v.t." : hnr)
        + ", huisletter: "
        + (StringUtils.isBlank(hnrL) ? "N.v.t." : hnrL)
        + ", toevoeging: "
        + (StringUtils.isBlank(hnrT) ? "N.v.t." : hnrT);
  }
}
