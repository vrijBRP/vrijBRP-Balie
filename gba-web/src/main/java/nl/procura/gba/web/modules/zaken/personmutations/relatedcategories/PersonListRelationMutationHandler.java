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

package nl.procura.gba.web.modules.zaken.personmutations.relatedcategories;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import nl.procura.burgerzaken.gba.StringUtils;
import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePL;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.Naam;
import nl.procura.validation.Anummer;
import nl.procura.validation.Bsn;

import lombok.AllArgsConstructor;
import lombok.Data;

public class PersonListRelationMutationHandler {

  /**
   * Returns existing mutations
   */
  public static List<PersonListRelationMutation> getExisting(String anr, Function<String, BasePLExt> plFetcher) {
    final List<PersonListRelationMutation> mutations = new ArrayList<>();
    // Mutations of person it self
    fetchPL(anr, plFetcher).ifPresent(pl -> getMutationRecords(pl)
        .forEach(rec -> mutations.add(toExistingMutation("-", pl, rec))));

    // Mutations of relations
    for (Result result : getRelatedRecords(anr, plFetcher)) {
      for (BasePLRec mutRec : getMutationRecords(result.getRelatedPL())) {
        mutations.add(toExistingMutation(toRelation(result.getRec().getCatType()),
            result.getRelatedPL(), mutRec));
      }
    }
    return mutations;
  }

  /**
   * Returns mutations base on mutation of category 1
   */
  public static List<PersonListRelationMutation> getNew(String anr, Function<String, BasePLExt> plFetcher) {
    return getRelatedRecords(anr, plFetcher).stream()
        .map(PersonListRelationMutationHandler::toNewMutation)
        .collect(toList());
  }

  private static List<Result> getRelatedRecords(String anr,
      Function<String, BasePLExt> plFetcher) {
    List<Result> results = new ArrayList<>();
    fetchPL(anr, plFetcher).ifPresent(pl -> {
      String bsn = getBsn(pl.getCurrentRec(GBACat.PERSOON));
      for (BasePLRec rec : getRelatedRecords(pl)) {
        Optional<BasePLExt> fetch = fetchPL(getAnr(rec), plFetcher);
        if (fetch.isPresent()) {
          BasePLExt relatedPL = fetch.get();
          for (BasePLRec relatedRec : getRelatedRecords(relatedPL)) {
            String relatedAnr = getAnr(relatedRec);
            String relatedBsn = getBsn(relatedRec);
            boolean isAnr = new Anummer(anr).eq(relatedAnr);
            boolean isBsn = new Bsn(bsn).eq(relatedBsn);
            if (isAnr && isBsn) {
              results.add(new Result(pl, relatedPL, rec, relatedRec));
            }
          }
        }
      }
    });
    return results;
  }

  private static Optional<BasePLExt> fetchPL(String anr, Function<String, BasePLExt> plFetcher) {
    Optional<BasePLExt> value = Optional.empty();
    if (StringUtils.isNotBlank(anr)) {
      try {
        value = Optional.ofNullable(plFetcher.apply(anr));
      } catch (RuntimeException ignored) {}
    }
    return value;
  }

  private static PersonListRelationMutation toExistingMutation(String relation,
      BasePLExt pl,
      BasePLRec rec) {

    return PersonListRelationMutation.builder()
        .found(true)
        .relation(relation)
        .municipality(getMunicipality(pl))
        .name(getName(pl.getCurrentRec(GBACat.PERSOON)))
        .relatedPL(pl)
        .relatedRecord(rec)
        .build();
  }

  private static PersonListRelationMutation toNewMutation(Result result) {
    String mun = getMunicipality(result.getPl());
    String relatedMun = getMunicipality(result.getRelatedPL());

    return PersonListRelationMutation.builder()
        .found(true)
        .record(result.getRelatedRec())
        .relation(toRelation(result.getRelatedRec().getCatType()))
        .name(getName(result.getRec()))
        .municipality(relatedMun)
        .change(String.format("Categorie %s", result.getRelatedRec().getCatType().getDescr().toLowerCase()))
        .remark(mun.equals(relatedMun) ? ""
            : "Let op!. Gerelateerde gevonden in gemeente " + relatedMun)
        .relatedPL(result.getRelatedPL())
        .relatedRecord(result.getRelatedRec())
        .build();
  }

  private static String getName(BasePLRec rec) {
    return new Naam(rec).getNaamNaamgebruikEersteVoornaam();
  }

  private static String getMunicipality(BasePL pl) {
    return pl.getCat(GBACat.VB).getCurrentRec().getElemVal(GBAElem.GEM_INSCHR).getDescr();
  }

  private static String toRelation(GBACat cat) {
    switch (cat) {
      case OUDER_1:
      case OUDER_2:
        return "Ouder";
      case HUW_GPS:
        return "Partner";
      case KINDEREN:
        return "Kind";
      default:
        return "";
    }
  }

  private static List<BasePLRec> getRelatedRecords(BasePL pl) {
    return pl.getCats(GBACat.OUDER_1, GBACat.OUDER_2, GBACat.HUW_GPS, GBACat.KINDEREN).stream()
        .flatMap(cat -> cat.getSets().stream())
        .map(BasePLSet::getCurrentRec)
        .collect(toList());
  }

  private static List<BasePLRec> getMutationRecords(BasePL pl) {
    return pl.getCats().stream()
        .flatMap(cat -> cat.getSets().stream())
        .map(BasePLSet::getMutationRec)
        .filter(BasePLRec::hasElems)
        .collect(toList());
  }

  private static String getAnr(BasePLRec rec) {
    return rec.getElemVal(GBAElem.ANR).getVal();
  }

  private static String getBsn(BasePLRec rec) {
    return rec.getElemVal(GBAElem.BSN).getVal();
  }

  @Data
  @AllArgsConstructor
  public static class Result {

    private BasePLExt pl;
    private BasePLExt relatedPL;
    private BasePLRec rec;
    private BasePLRec relatedRec;
  }
}
