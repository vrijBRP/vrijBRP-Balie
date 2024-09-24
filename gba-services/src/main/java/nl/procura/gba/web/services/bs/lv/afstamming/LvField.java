/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.services.bs.lv.afstamming;

import java.util.Arrays;
import java.util.List;

import nl.procura.gba.web.services.bs.lv.LvType;

import lombok.Data;
import lombok.Getter;

@Getter
public enum LvField {

  UITSPRAAK("uitspraak"),
  UITSPRAAK_ANDERS("uitspraakAnders"),
  DATUM_UITSPRAAK("datumUitspraak"),
  DATUM_GEWIJSDE("datumGewijsde"),
  SOORT_VERBINTENIS("soortVerbintenis"),
  DOCUMENT("document"),
  DOCUMENT_ANDERS("documentAnders"),
  NUMMER("nummer"),
  DATUM("datum"),
  PLAATS("plaats"),
  DOOR("door"),
  TWEEDE_DOC("tweedeDocument"),
  TWEEDE_DOC_OMS("tweedeDocumentOms"),
  TWEEDE_DOC_OMS_ANDERS("tweedeDocumentOmsAnders"),
  TWEEDE_DOC_DATUM("tweedeDocumentDatum"),
  TWEEDE_DOC_PLAATS("tweedeDocumentPlaats"),
  BETREFT_OUDER("betreftOuder"),
  BETREFT_OUDER_PERSOON("betreftOuderPersoon"),
  OUDERSCHAP_ONTKEND("ouderschapOntkend"),
  OUDERSCHAP_VASTGESTELD("ouderschapVastgesteld"),
  ERKENNING_DOOR("erkenningDoor"),
  FAMRECHT("famRecht"),
  TOESTEMMING_DOOR("toestemmingDoor"),
  TOESTEMMING_ANDERS("toestemmingAnders"),
  GESLN_OUDER_VG("geslOuderVastgesteld"),
  GESLN_OUDER_GW("geslOuderGewijzigd"),
  VOORN_OUDER("voornamenOuderVastgesteldAls"),
  KEUZE_GESLACHTSNAAM("keuzeGeslachtsnaam"),
  GESLN_IS("geslachtsnaamIs"),
  GESLN_VA("geslachtsnaamVastgesteldAls"),
  GESLNM_GW("geslachtsnaamGewijzigdIn"),
  GESLNM_KIND_GW("geslachtsnaamKindGewijzigdIn"),
  VOORNAMEN_GW("voornamenGewijzigd"),
  VOORNAMEN_VA("voornamenVastgesteldAls"),
  VOORNAMEN_GW_IN("voornamenGewijzigdIn"),
  GESL_AAND("geslachtsaand"),
  TOEGEPAST_RECHT("toegepastRecht"),
  TOEGEPAST_RECHT_ANDERS("toegepastRechtAnders"),
  GEZAG("gezag"),
  GEKOZEN_RECHT("gekozenRecht"),
  DAG_VAN_WIJZIGING("dagVanWijziging"),
  VERBETERD("verbeterd"),
  OUDERS("ouders"),
  ADOPTIEFOUDERS("adoptiefouders");

  private final String name;

  LvField(String name) {
    this.name = name;
  }

  public static LvField get(String name) {
    return Arrays.stream(values())
        .filter(field -> field.name.equals(name))
        .findFirst()
        .orElse(null);
  }

  public static String[] getForm1(LvType type) {
    return toMapping(type).toArray(Arrays.asList(UITSPRAAK, UITSPRAAK_ANDERS, DATUM_UITSPRAAK,
        DATUM_GEWIJSDE, SOORT_VERBINTENIS, DOCUMENT, DOCUMENT_ANDERS, NUMMER, DATUM, PLAATS, DOOR,
        TWEEDE_DOC, TWEEDE_DOC_OMS, TWEEDE_DOC_OMS_ANDERS, TWEEDE_DOC_DATUM, TWEEDE_DOC_PLAATS));
  }

  public static String[] getForm2(LvType type) {
    return toMapping(type).toArray(
        Arrays.asList(BETREFT_OUDER, OUDERSCHAP_ONTKEND, OUDERSCHAP_VASTGESTELD, FAMRECHT, ERKENNING_DOOR,
            BETREFT_OUDER_PERSOON, TOESTEMMING_DOOR, TOESTEMMING_ANDERS, GESLN_OUDER_VG, GESLN_OUDER_GW, VOORN_OUDER,
            KEUZE_GESLACHTSNAAM,
            GESLN_IS, GESLN_VA, GESLNM_GW, GESLNM_KIND_GW,
            VOORNAMEN_VA, VOORNAMEN_GW, VOORNAMEN_GW_IN, GESL_AAND, TOEGEPAST_RECHT, TOEGEPAST_RECHT_ANDERS, GEZAG,
            GEKOZEN_RECHT, DAG_VAN_WIJZIGING, VERBETERD,
            OUDERS, ADOPTIEFOUDERS));
  }

  public static Mapping toMapping(LvType type) {
    switch (type) {
      case ADOPTIE: // 1
        return new Mapping(UITSPRAAK, UITSPRAAK_ANDERS, DATUM_UITSPRAAK, DATUM_GEWIJSDE,
            FAMRECHT, BETREFT_OUDER_PERSOON, ADOPTIEFOUDERS,
            KEUZE_GESLACHTSNAAM, GESLN_IS, VOORNAMEN_GW, VOORNAMEN_GW_IN);

      case AMBTSHALVE_VERBETERING_AKTE: // 2
        return new Mapping(VERBETERD);

      case DOORHALING_AKTE: // 3
        return new Mapping(UITSPRAAK, UITSPRAAK_ANDERS, DATUM_UITSPRAAK);

      case GERECHTELIJKE_VASTSTELLING_OUDERSCHAP: // 4
        return new Mapping(UITSPRAAK, UITSPRAAK_ANDERS, DATUM_UITSPRAAK,
            OUDERSCHAP_VASTGESTELD, BETREFT_OUDER_PERSOON, OUDERS,
            KEUZE_GESLACHTSNAAM, GESLN_IS);

      case HERROEPING_ADOPTIE: // 5
        return new Mapping(UITSPRAAK, UITSPRAAK_ANDERS, DATUM_UITSPRAAK,
            DATUM_GEWIJSDE, GESLN_IS, ADOPTIEFOUDERS);

      case ONTKENNING_OUDERSCHAP: // 6
        return new Mapping(UITSPRAAK, UITSPRAAK_ANDERS, DATUM_UITSPRAAK,
            OUDERSCHAP_ONTKEND, BETREFT_OUDER_PERSOON, GESLN_IS, OUDERS);

      case VASTSTELLING_NAMEN_BIJ_KB: // 7
        return new Mapping(DOCUMENT, DOCUMENT_ANDERS, NUMMER, DATUM,
            BETREFT_OUDER, BETREFT_OUDER_PERSOON, GESLN_OUDER_VG, VOORN_OUDER,
            GESLN_VA, VOORNAMEN_VA);

      case VASTSTELLING_NAMEN_BIJ_OPTIE: // 8
        return new Mapping(DOCUMENT, DOCUMENT_ANDERS, DATUM, PLAATS, DOOR,
            BETREFT_OUDER, BETREFT_OUDER_PERSOON, GESLN_OUDER_VG, VOORN_OUDER,
            GESLN_VA, VOORNAMEN_VA);

      case VERBETERING_AKTE: // 9
        return new Mapping(UITSPRAAK, UITSPRAAK_ANDERS, DATUM_UITSPRAAK, VERBETERD);

      case VERNIETIGING_ERKENNING: // 10
        return new Mapping(UITSPRAAK, UITSPRAAK_ANDERS, DATUM_UITSPRAAK,
            BETREFT_OUDER, BETREFT_OUDER_PERSOON, OUDERS,
            GESLN_IS);

      case WETTIGING: // 11
        return new Mapping(DOCUMENT, DOCUMENT_ANDERS, NUMMER, DATUM, PLAATS, DOOR,
            BETREFT_OUDER, BETREFT_OUDER_PERSOON, OUDERS,
            GESLN_IS);

      case WIJZIGING_GESLACHT: // 12
        return new Mapping(DOCUMENT, DOCUMENT_ANDERS, DATUM, PLAATS,
            VOORNAMEN_GW, VOORNAMEN_GW_IN, GESL_AAND);

      case WIJZIGING_GESLACHTSNAAM_BIJ_KB: // 13
        return new Mapping(DOCUMENT, DOCUMENT_ANDERS, NUMMER, DATUM,
            BETREFT_OUDER, BETREFT_OUDER_PERSOON, GESLN_OUDER_GW,
            GESLNM_GW);

      case WIJZIGING_GESLACHTSNAAM_DOOR_RECHTERLIJKE_UITSPRAAK: // 14
        return new Mapping(UITSPRAAK, UITSPRAAK_ANDERS, DATUM_UITSPRAAK,
            GESLNM_GW);

      case WIJZIGING_GESLACHTSNAAM_TGV_HUWELIJK_GPS: // 15
        return new Mapping(SOORT_VERBINTENIS, DOCUMENT, DOCUMENT_ANDERS, NUMMER, DATUM, PLAATS, DOOR,
            TWEEDE_DOC, TWEEDE_DOC_OMS, TWEEDE_DOC_OMS_ANDERS, TWEEDE_DOC_DATUM, TWEEDE_DOC_PLAATS,
            KEUZE_GESLACHTSNAAM, GESLN_IS, BETREFT_OUDER, BETREFT_OUDER_PERSOON, GESLN_OUDER_GW,
            GEKOZEN_RECHT, DAG_VAN_WIJZIGING);

      case WIJZIGING_GESLACHTSNAAM_TGV_ECHTSCHEIDING: // 16
        return new Mapping(UITSPRAAK, UITSPRAAK_ANDERS, DATUM_UITSPRAAK,
            GESLNM_GW, DAG_VAN_WIJZIGING);

      case WIJZIGING_GESLACHTSNAAM_ERK_OUDER: // 17
        return new Mapping(DOCUMENT, DOCUMENT_ANDERS, NUMMER, DATUM, PLAATS, DOOR,
            BETREFT_OUDER, BETREFT_OUDER_PERSOON, GESLN_OUDER_GW,
            GESLNM_KIND_GW);

      case WIJZIGING_VOORNAMEN: // 18
        return new Mapping(UITSPRAAK, UITSPRAAK_ANDERS, DATUM_UITSPRAAK,
            VOORNAMEN_GW_IN);

      case NAAMSKEUZE: // 19
        return new Mapping(DOCUMENT, DOCUMENT_ANDERS, NUMMER, DATUM, PLAATS, DOOR, GESLN_IS);

      case ERKENNING: // 20
        return new Mapping(DOCUMENT, DOCUMENT_ANDERS, NUMMER, DATUM, PLAATS, DOOR,
            ERKENNING_DOOR, BETREFT_OUDER_PERSOON,
            TOESTEMMING_DOOR, TOESTEMMING_ANDERS, TOEGEPAST_RECHT, TOEGEPAST_RECHT_ANDERS, GEZAG,
            KEUZE_GESLACHTSNAAM, GESLN_IS);
      default:
        return new Mapping();
    }
  }

  @Data
  public static class Mapping {

    private List<LvField> fields;

    public Mapping(LvField... fields) {
      this.fields = Arrays.asList(fields);
    }

    public String[] toArray(List<LvField> allFields) {
      if (fields.isEmpty()) {
        return allFields.stream()
            .map(LvField::name)
            .toArray(String[]::new);
      } else {
        return fields.stream()
            .filter(allFields::contains)
            .map(field -> field.name)
            .toArray(String[]::new);
      }
    }
  }
}
