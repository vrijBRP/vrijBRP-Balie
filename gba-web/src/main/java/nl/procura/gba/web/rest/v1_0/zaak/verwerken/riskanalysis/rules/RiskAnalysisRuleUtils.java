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

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;

import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.gba.jpa.personen.db.DossRiskAnalysisSubject;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;
import nl.procura.validation.Postcode;

public class RiskAnalysisRuleUtils {

  private final Services services;

  public RiskAnalysisRuleUtils(Services services) {
    this.services = services;
  }

  public BasePLExt getPL(DossRiskAnalysisSubject subject) {
    String bsn = subject.getPerson().getBsn().toString();
    String anr = subject.getPerson().getAnr().toString();
    return services.getPersonenWsService().getPersoonslijst(bsn, anr);
  }

  public RuleAddresses getAddresses(RiskAnalysisRelatedCase relatedCase, BasePLExt pl) {

    // RuleAddress of the relocation case
    RuleAddresses addresses = new RuleAddresses();
    RuleAddress caseAddress = addresses.add(new RuleAddress()
        .setCaseAddress(true)
        .setAddress(relatedCase.getAddress())
        .setDIn(relatedCase.getDatumIngang().getLongDate())
        .setAddressUsage(relatedCase.getFunction()));

    // Addresses from the personslist
    if (pl != null) {
      for (BasePLSet set : pl.getCat(GBACat.VB).getSets()) {
        for (BasePLRec record : set.getRecs()) {
          if (record.isBagChange() || record.isAdmHistory() || record.isIncorrect()) {
            continue;
          }

          RuleAddress plAddress = new RuleAddress().setCaseAddress(false)
              .setAddress(getAdres(record))
              .setDIn(record.getElemVal(DATUM_AANVANG_ADRESH).toLong())
              .setAddressUsage(FunctieAdres.get(record.getElemVal(FUNCTIE_ADRES).getVal()))
              .setCountryCode(record.getElemVal(LAND_VERTREK).toLong());

          // If address of the case is already on the personslist then don't store it.
          if (caseAddress.getDIn() != plAddress.getDIn()) {
            addresses.add(plAddress);
          }
        }
      }
    }

    return addresses.sortDescending();
  }

  public List<BaseWKExt> getRelocationAddress(RiskAnalysisRelatedCase relatedCase) {
    ZoekArgumenten za = new ZoekArgumenten();
    za.setStraatnaam(relatedCase.getAddress().getStraat());
    za.setPostcode(Postcode.getCompact(relatedCase.getAddress().getPostcode()));
    za.setHuisnummer(String.valueOf(relatedCase.getAddress().getHuisnummer()));
    za.setHuisletter(relatedCase.getAddress().getHuisletter());
    za.setHuisnummertoevoeging(relatedCase.getAddress().getHuisnummertoev());
    za.setHuisnummeraanduiding(relatedCase.getAddress().getHuisnummeraand());
    return services.getPersonenWsService().getAdres(za, true).getBasisWkWrappers();
  }

  /**
   * Parse the BasisPlCategorieRecord to an Adresformat
   */
  private Adresformats getAdres(BasePLRec record) {
    String street = getValue(record, STRAATNAAM);
    String hnr = getValue(record, HNR);
    String hnrL = getValue(record, HNR_L);
    String hnrT = getValue(record, HNR_T);
    String hnrA = getValue(record, HNR_A);
    String pc = getValue(record, POSTCODE);
    String munPart = getValue(record, GEM_DEEL);
    String wpl = getValue(record, WPL_NAAM);
    String mun = getValue(record, GEM_INSCHR);
    String startDate = getValue(record, DATUM_AANVANG_ADRESH);
    String emigrationCountry = getValue(record, LAND_VERTREK);
    String emigrationDate = getValue(record, DATUM_VERTREK_UIT_NL);
    String location = getValue(record, LOCATIEBESCHR);
    String foreignString1 = getValue(record, ADRES_BUITENL_1);
    String foreignString2 = getValue(record, ADRES_BUITENL_2);
    String foreignString3 = getValue(record, ADRES_BUITENL_3);

    Adresformats adresformats = new Adresformats();
    adresformats.setValues(street, hnr, hnrL, hnrT, hnrA, location, pc,
        munPart, wpl, mun, startDate, emigrationCountry, emigrationDate, foreignString1, foreignString2,
        foreignString3);
    return adresformats;
  }

  /**
   * Get Value from BasisPlCategorieRecord
   */
  private String getValue(BasePLRec record, GBAElem straatnaam) {
    return record.getElemVal(straatnaam).getVal();
  }
}
