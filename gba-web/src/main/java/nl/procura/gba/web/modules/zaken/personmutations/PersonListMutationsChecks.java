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

package nl.procura.gba.web.modules.zaken.personmutations;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.HUW_GPS;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.INSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.KINDEREN;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_1;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_2;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OVERL;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.PERSOON;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AANDUIDING_EURO_KIESR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_GEG_IN_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_UITGESLOTEN_KIESR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_VBT;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ADRES_BUITENL_1;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ADRES_BUITENL_2;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ADRES_BUITENL_3;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AKTENR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ANR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AUTORIT_VAN_AFGIFTE_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.BSN;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_AANVANG_ADRESH;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_EINDE_GELDIG_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_INGANG_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_ONTBINDING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_OPSCH_BIJHOUD;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_UITGIFTE_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VERZ_OF_MED_EURO_KIESR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.FUNCTIE_ADRES;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GESLACHTSNAAM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IDCODE_NUMMERAAND;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ID_VERBLIJFPLAATS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.INGANGSDATUM_VBT;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.INGANGSDAT_GELDIG;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_VERTREK;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.NR_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.OMSCHR_REDEN_OPSCH_BIJHOUD;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.OPENB_RUIMTE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.PLAATS_VERBINTENIS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.REDEN_ONTBINDING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.REGIST_GEM_AKTE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.SOORT_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.STRAATNAAM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.WPL_NAAM;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.ADRES;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.ADRESAANGIFTE;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.ADRESHUISHOUDING;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.AKTE;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.ANUMMERVERWIJZINGEN;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.BIJZ_NED_SCHAP;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.BUITENL_PERSOONSNR;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.CURATELE;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.DOCUMENT;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.DOCUMENTINDICATIE;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.EMIGRATIE;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.EUROPEES_KIESRECHT;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.FAM_RECHT_BETREK;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.GEBOORTE;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.GEHEIM;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.GELDIGHEID;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.GEMEENTE;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.GESLACHT;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.GEZAG_MINDERJARIGE;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.IDNUMMERS;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.IMMIGRATIE;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.LOCATIE;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.NAAM;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.NAAMGEBRUIK;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.NATIONALITEIT;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.NED_REISDOCUMENT;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.ONJUIST;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.ONTBIND_VERBINTENIS;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.OPNAME;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.OPNEMING;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.OPSCHORTING;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.PROCEDURE;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.RDN_EINDE_NATIO;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.REG_AFSTAMMING;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.RND_OPN_NATIO;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.SIGNALERING;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.SLUITING_VERBINTENIS;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.SOORT_VERBINTENIS;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.SYNCHRONICITEIT;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.UITSL_KIESR;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.VERBLIJFSTITEL;
import static nl.procura.burgerzaken.gba.core.enums.GBARecStatus.HIST;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.ADD_HISTORIC;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.CORRECT_CATEGORY;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.CORRECT_CURRENT_ADMIN;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.CORRECT_HISTORIC_ADMIN;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.CORRECT_HISTORIC_GENERAL;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.OVERWRITE_CURRENT;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.OVERWRITE_HISTORIC;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.SUPER_OVERWRITE_CURRENT;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.SUPER_OVERWRITE_HISTORIC;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.UPDATE_SET;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.Validator;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBAGroup;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElem;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElems;
import nl.procura.gba.web.services.beheer.personmutations.PersonListActionType;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.field.fieldvalues.TabelFieldValue;

public class PersonListMutationsChecks {

  private final static String IS_REQUIRED      = "%s is verplicht.";
  private final static String ANY_IS_REQUIRED  = "%s óf %s is verplicht.";
  private final static String REQUIRED_WITH    = "%s is verplicht in combinatie met %s.";
  private final static String BOTH_REQUIRED    = "%s en %s zijn beide verplicht";
  private final static String THIRD_REQUIRED   = "Als %s of %s voorkomt, komt %s verplicht voor.";
  private final static String MUTUAL_EXCLUSIVE = "%s en %s mogen niet tegelijkertijd voorkomen.";
  private final static String NOT_ALLOWED      = "Als %s niet voorkomt mag %s ook niet voorkomen.";

  /*
  - Clear whole group if one element in a group is cleared or
  - Clear other group if one element of a group is filled
   */
  public static void checkCleared(PersonListMutElem mutElem, boolean isBlank, PersonListMutElems elems) {
    if (isBlank) { // If the field is cleared, made blank
      if (mutElem.getGroup().is(
          GEBOORTE, AKTE, DOCUMENT,
          SLUITING_VERBINTENIS, ONTBIND_VERBINTENIS,
          IMMIGRATIE, EMIGRATIE)) {
        for (PersonListMutElem elem : elems) {
          if (elem.getGroup().is(mutElem.getGroup())) {
            elem.getField().setValue(null);
          }
        }
      }
    } else {
      clearOtherGroup(mutElem, elems, NATIONALITEIT, BIJZ_NED_SCHAP); // 5 / 65
      clearOtherGroup(mutElem, elems, SLUITING_VERBINTENIS, ONTBIND_VERBINTENIS); // 6 / 7
      clearOtherGroup(mutElem, elems, EMIGRATIE, ADRESHUISHOUDING, ADRES, LOCATIE); // 13 / 10,11,12
      clearOtherGroup(mutElem, elems, EMIGRATIE, IMMIGRATIE); // 13 / 14
      clearOtherGroup(mutElem, elems, GEZAG_MINDERJARIGE, CURATELE); // 32 / 33
      clearOtherGroup(mutElem, elems, RND_OPN_NATIO, RDN_EINDE_NATIO); // 63 / 64
      clearOtherGroup(mutElem, elems, AKTE, DOCUMENT); // 81 / 82
    }
  }

  private static void clearOtherGroup(PersonListMutElem mutElem, PersonListMutElems elems,
      GBAGroup group1, GBAGroup... group2) {
    clearGroup(mutElem, elems, new GBAGroup[]{ group1 }, group2);
    clearGroup(mutElem, elems, group2, group1);
  }

  private static void clearGroup(PersonListMutElem mutElem, PersonListMutElems elems,
      GBAGroup[] group1, GBAGroup... group2) {
    if (Arrays.asList(group1).contains(mutElem.getGroup())) {
      elems.stream()
          .filter(elem -> Arrays.asList(group2).contains(elem.getGroup()))
          .forEach(elem -> elem.getField().setValue(null));
    }
  }

  public static Optional<String> getRequiredError(PersonListMutElem elem, PersonListMutElems elems) {
    Optional<String> req = Optional.empty();
    if (!elem.getElemType().isNational() || elem.getAction().isSuperuser()) {
      return Optional.empty();
    }

    // Groepen
    if (elem.getCat().is(PERSOON, HUW_GPS, OVERL, KINDEREN, REISDOC)) {
      if (elem.getGroup().is(AKTE, DOCUMENT)
          && elems.isAllBlank(AKTE, DOCUMENT)) {
        req = msg(ANY_IS_REQUIRED, AKTE, DOCUMENT);
      }
    }

    if (elem.getGroup().is(GELDIGHEID, OPNEMING)) {
      req = msg(BOTH_REQUIRED, GELDIGHEID, OPNEMING);
    }

    // checks for category 1
    if (elem.getCat().is(PERSOON)) {
      if (elem.getGroup().is(NAAM, IDNUMMERS, GEBOORTE, GESLACHT, NAAMGEBRUIK)) {
        if (elems.isAllBlank(elem.getGroup())) {
          req = msg(IS_REQUIRED, elem.getGroup());
        }
      }
    }

    // checks for category 2 / 3
    if (elem.getCat().is(OUDER_1, OUDER_2)) {
      if (elem.getGroup().is(FAM_RECHT_BETREK) && !elems.isAllBlank(NAAM)) {
        req = msg(REQUIRED_WITH, FAM_RECHT_BETREK, NAAM);
      }
    }

    // checks for category 4
    if (elem.getCat().is(GBACat.NATIO)) {
      if (elem.getGroup().is(RND_OPN_NATIO)) {
        if (!elems.isAllBlank(NATIONALITEIT, BIJZ_NED_SCHAP)) {
          req = msg(THIRD_REQUIRED, NATIONALITEIT, BIJZ_NED_SCHAP, RND_OPN_NATIO);
        }
      }

      if (elem.getGroup().is(RDN_EINDE_NATIO)) {
        if (elems.isAllBlank(NATIONALITEIT, BIJZ_NED_SCHAP)) {
          req = msg("Als %s of %s niet voorkomt, komt %s verplicht voor.",
              NATIONALITEIT, BIJZ_NED_SCHAP, RDN_EINDE_NATIO);
        }
      }
    }

    // checks for category 5
    if (elem.getCat().is(HUW_GPS)) {
      //Als of groep 06 of groep 07 voorkomt, dan komen ook de groepen 02, 03 en 15 voor
      if (!elems.isAllBlank(SLUITING_VERBINTENIS, ONTBIND_VERBINTENIS)) {
        if (elem.getGroup().is(NAAM) && elems.isAllBlank(NAAM)) {
          req = msg(THIRD_REQUIRED, SLUITING_VERBINTENIS, ONTBIND_VERBINTENIS, elem.getGroup());
        }
        if (elem.getGroup().is(GEBOORTE, SOORT_VERBINTENIS)) {
          req = msg(THIRD_REQUIRED, SLUITING_VERBINTENIS, ONTBIND_VERBINTENIS, elem.getGroup());
        }
      }
    }

    // checks for category 7
    if (elem.getCat().is(GBACat.INSCHR)) {
      if (elem.getGroup().is(OPNAME, GEHEIM, SYNCHRONICITEIT)) {
        req = msg(IS_REQUIRED, elem.getGroup());
      }
    }

    // checks for category 8
    if (elem.getCat().is(GBACat.VB)) {
      if (elem.getGroup().is(GEMEENTE, ADRESAANGIFTE)) {
        req = msg(IS_REQUIRED, elem.getGroup());
      }

      if (elem.getGroup().is(ADRESHUISHOUDING, ADRES, LOCATIE)) {
        if (elems.isAllBlank(ADRESHUISHOUDING) && elems.isAllBlank(EMIGRATIE)) {
          req = msg("%s kan niet voorkomen in combatie met %s.", EMIGRATIE, ADRESHUISHOUDING);

        }
        if (elems.isAllBlank(ADRES, LOCATIE) && elems.isAllBlank(EMIGRATIE)) {
          req = msg("%s kan niet voorkomen in combatie met %s en %s.", EMIGRATIE, ADRES, LOCATIE);
        }
      }

      if (elem.getGroup().is(EMIGRATIE)) {
        if (!elems.isAllBlank(EMIGRATIE) && !elems.isAllBlank(ADRESHUISHOUDING, ADRES, LOCATIE)) {
          req = msg("%s kan niet voorkomen in combinatie met %s, en of %s of %s.",
              EMIGRATIE, ADRESHUISHOUDING, ADRES, LOCATIE);
        }
      }
    }

    // checks for category 9
    if (elem.getCat().is(KINDEREN)) {
      if (elem.getGroup().is(NAAM)) {
        if (!elems.isAllBlank(REG_AFSTAMMING)) {
          req = msg(REQUIRED_WITH, NAAM, REG_AFSTAMMING);
        }
      }
    }

    // checks for category 12
    if (elem.getCat().is(GBACat.REISDOC)) {
      if (elem.getGroup().is(NED_REISDOCUMENT, SIGNALERING)) {
        if (elems.isAllBlank(NED_REISDOCUMENT, SIGNALERING)) {
          req = msg(ANY_IS_REQUIRED, NED_REISDOCUMENT, SIGNALERING);
        }
      }
    }

    // checks for category 13
    if (elem.getCat().is(GBACat.KIESR)) {
      if (elem.getGroup().is(EUROPEES_KIESRECHT, UITSL_KIESR)) {
        if (elems.isAllBlank(EUROPEES_KIESRECHT, UITSL_KIESR)) {
          req = msg(ANY_IS_REQUIRED, EUROPEES_KIESRECHT, UITSL_KIESR);
        }
      }

      if (elem.getGroup().is(EUROPEES_KIESRECHT, DOCUMENT)) {
        if (!elems.isAllBlank(EUROPEES_KIESRECHT, DOCUMENT)) {
          if (elems.isAllBlank(elem.getGroup())) {
            req = msg("Als %s of %s voorkomt, dan komen ze beide voor",
                EUROPEES_KIESRECHT, UITSL_KIESR);
          }
        }
      }
    }

    // checks for category 21
    if (elem.getCat().is(GBACat.VERW)) {
      if (elem.getGroup().is(IDNUMMERS, NAAM, GEBOORTE, GEMEENTE, GEHEIM)) {
        req = msg(IS_REQUIRED, elem.getGroup());
      }
    }

    // Specifieke elementen
    if (elem.getElem().isElement(BSN)
        && (elem.getRec().getStatus().is(HIST)
            || elem.getAction().is(ADD_HISTORIC, CORRECT_HISTORIC_GENERAL, CORRECT_HISTORIC_ADMIN))) {
      return Optional.empty();
    }

    if (elem.getGroup().is(IDNUMMERS)) {
      if (elem.getCat().is(PERSOON)) {
        return Optional.of("Verplicht in categorie 1 (persoon)");
      }
    }

    if (elem.getGroup().is(ADRES)) {
      if (elem.getElemType().is(OPENB_RUIMTE, WPL_NAAM, ID_VERBLIJFPLAATS, IDCODE_NUMMERAAND)) {
        if (!elems.isAllBlank(OPENB_RUIMTE, WPL_NAAM, ID_VERBLIJFPLAATS, IDCODE_NUMMERAAND)) {
          return Optional.of("Als één 'BAG velden' is gevuld dan zijn ze alle 4 verplicht");
        }
      }
    }

    if (elem.getGroup().is(EMIGRATIE)) {
      if (!elems.isAllBlank(ADRES_BUITENL_1, ADRES_BUITENL_2, ADRES_BUITENL_3)) {
        if (elem.getElemType().is(ADRES_BUITENL_2)) {
          return Optional.of("Als er een buitenlands adres is opgenomen, komt minimaal dit element voor");
        }
      }
      if (!elems.isAllBlank(ADRES_BUITENL_3)) {
        if (elem.getElemType().is(ADRES_BUITENL_1)) {
          return Optional.of("Is verplicht om regel 3 buitenland ook voorkomt");
        }
      }
    }

    if (isRequiredInGroup(elem)) {
      if (elems.isAllBlank(elem.getGroup())) {
        return req;
      } else {
        if (req.isPresent()) {
          return req;
        }
        return msg("Als %s voorkomt is dit element verplicht", elem.getGroup());
      }
    } else {
      return Optional.empty();
    }
  }

  private static boolean isRequiredInGroup(PersonListMutElem elem) {
    List<GBAElem> requiredElements = getRequiredElements().get(elem.getGroup());
    return requiredElements == null || requiredElements.stream()
        .anyMatch(e -> e.is(elem.getElemType()));
  }

  public static boolean isReadonly(PersonListMutElem mutElem) {
    PersonListActionType action = mutElem.getAction();
    GBAElem elem = mutElem.getElemType();
    GBACat cat = mutElem.getCat();
    GBAGroup group = mutElem.getTypes().getGroup();
    GBARecStatus status = mutElem.getRec().getStatus();

    if (action.is(SUPER_OVERWRITE_CURRENT, SUPER_OVERWRITE_HISTORIC)) {
      return false;

    } else if (action.is(CORRECT_CATEGORY)) {
      return true;

    } else if (action.is(CORRECT_CURRENT_ADMIN) && !elem.isAdmin()) {
      return true;

    } else if (action.is(OVERWRITE_CURRENT, OVERWRITE_HISTORIC)) {

      if (group.is(EMIGRATIE, IMMIGRATIE, DOCUMENTINDICATIE, PROCEDURE)) {
        return false;
      }

      if (group.is(ONJUIST) && status.is(HIST)) {
        return false;
      }

      // Voor overschrijven voor gegevens voor adoptie/geslachtswijz.
      boolean histNaam = status.is(HIST) && group.is(NAAM, GESLACHT, NAAMGEBRUIK);
      return !cat.is(PERSOON, OUDER_1, OUDER_2, HUW_GPS, KINDEREN) || (!histNaam && !elem.is(AKTENR));

    } else if (!action.is(ADD_HISTORIC) && elem.is(GBAElem.IND_ONJUIST)) {
      return true;
    }
    return GBAElem.DATUM_VAN_OPNEMING == elem;
  }

  public static Optional<String> getValueError(PersonListMutElem elem, PersonListMutElems elems) {
    if (!elem.getElemType().isNational() || elem.getAction().isSuperuser()) {
      return Optional.empty();
    }

    if (GBAElem.INGANGSDAT_GELDIG.is(elem.getElemType())) {
      ProcuraDate newDate = new ProcuraDate(elem.getNewValue());
      int dOpsch = aval(elem.getPl().getCat(INSCHR).getLatestRec().getElemVal(DATUM_OPSCH_BIJHOUD).getVal());
      if (dOpsch > 0) {
        ProcuraDate currentDate = new ProcuraDate(dOpsch);
        if (isFirstDateOlderThanSecond(currentDate, newDate)) {
          return Optional.of("De datum moet vóór de opschortingsdatum ("
              + currentDate.getFormatDate() + ") liggen");
        }
      }

      BasePLValue currentVal = elem.getSet().getCurrentRec().getElemVal(INGANGSDAT_GELDIG);
      ProcuraDate currentDate = new ProcuraDate(currentVal.getVal());

      if (StringUtils.isNotBlank(elem.getCurrentValue().getVal())) {
        if (elem.getAction().is(UPDATE_SET)) {
          if (isFirstDateOlderThanSecond(newDate, currentDate)) {
            return Optional.of("Nieuwe datum moet recenter zijn dan de oude datum");
          }
        } else if (elem.getAction().is(ADD_HISTORIC, CORRECT_HISTORIC_GENERAL)) {
          if (isFirstDateOlderThanSecond(currentDate, newDate)) {
            return Optional.of("Een historische datum moet vóór de datum van het actuele record ("
                + currentDate.getFormatDate() + ") liggen");
          }
        }
      }

    }

    if (elem.getGroup().is(NAAM)
        && elems.isApplicable(PersonListMutationsChecks::isDefaultValue, GESLACHTSNAAM)
        && !elem.getElem().isElement(GESLACHTSNAAM)) {
      return msg("Als %s onbekend is dan mogen de andere elementen uit de groep ook niet voorkomen",
          GESLACHTSNAAM.getDescr());
    }

    if (isNotMutual(elem, elems, AKTE, DOCUMENT)) {
      return msg(MUTUAL_EXCLUSIVE, AKTE, DOCUMENT);
    }

    if (!elem.getRec().getStatus().is(HIST) && GBAElem.IND_ONJUIST == elem.getElemType()) {
      return msg("%s kan uitsluitend in historie voorkomen.", ONJUIST);
    }

    // checks for category 2/3
    if (elem.getCat().is(OUDER_1, OUDER_2)) {
      if (elem.getGroup().is(IDNUMMERS, GEBOORTE, GESLACHT, FAM_RECHT_BETREK)) {
        if (elems.isAllBlank(NAAM) && !elems.isAllBlank(elem.getGroup())) {
          return msg(NOT_ALLOWED, NAAM, elem.getGroup());
        }
      }
    }

    // checks for category 4
    if (elem.getCat().is(GBACat.NATIO)) {
      if (elem.getGroup().is(BUITENL_PERSOONSNR)) {
        Optional<PersonListMutElem> natio = elems.getElem(GBAElem.NATIONALITEIT);
        if (natio.isPresent() && !natio.get().isBlank()) {
          FieldValue fieldValue = (FieldValue) natio.get().getField().getValue();
          TabelFieldValue natioValue = GbaTables.NATIO.get(fieldValue.getBigDecimalValue());
          if (!pos(natioValue.getAttributes().get("eu"))) {
            return msg("%s kan uitsluitend voorkomen als %s voorkomt en uitsluitend als " +
                "de betreffende stapel betrekking heeft op een EU nationaliteit",
                BUITENL_PERSOONSNR, NATIONALITEIT);
          }
        } else {
          return msg("%s kan uitsluitend voorkomen als %s voorkomt en uitsluitend als " +
              "de betreffende stapel betrekking heeft op een EU nationaliteit",
              BUITENL_PERSOONSNR, NATIONALITEIT);
        }
      }

      if (isNotMutual(elem, elems, NATIONALITEIT, BIJZ_NED_SCHAP)) {
        return msg(MUTUAL_EXCLUSIVE, NATIONALITEIT, BIJZ_NED_SCHAP);
      }
      if (isNotMutual(elem, elems, RND_OPN_NATIO, RDN_EINDE_NATIO)) {
        return msg(MUTUAL_EXCLUSIVE, RND_OPN_NATIO, RDN_EINDE_NATIO);
      }
    }

    // checks for category 5
    if (elem.getCat().is(HUW_GPS)) {
      if (isNotMutual(elem, elems, SLUITING_VERBINTENIS, ONTBIND_VERBINTENIS)) {
        return msg(MUTUAL_EXCLUSIVE, SLUITING_VERBINTENIS, ONTBIND_VERBINTENIS);
      }
      if (REGIST_GEM_AKTE.is(elem.getElemType())) {
        Optional<BasePLValue> gemeente = getRegisterGemeenteVerbintenis(elems, elem);
        if (gemeente.isPresent() && !elem.getNewValue().equals(gemeente.get().getVal())) {
          String descr = gemeente.map(v -> v.getDescr() + " (" + v.getVal() + ")").orElse("");
          return msg("Element %s moet gelijk zijn aan de registergemeente akte in het sluitingsrecord (%s)",
              REGIST_GEM_AKTE.getDescrAndCode(), descr);
        }
      }
    }
    // checks for category 8
    if (elem.getCat().is(GBACat.VB)) {
      if (isNotMutual(elem, elems, EMIGRATIE, IMMIGRATIE)) {
        return msg(MUTUAL_EXCLUSIVE, EMIGRATIE, IMMIGRATIE);
      }
      if (elem.getGroup().is(DOCUMENTINDICATIE) && !elems.isAllBlank(DOCUMENTINDICATIE)) {
        String rdn = elem.getPl().getCat(INSCHR).getLatestRec().getElemVal(OMSCHR_REDEN_OPSCH_BIJHOUD).getVal();
        if (!rdn.matches("[EM]")) {
          return msg("Groep %s kan voorkomen wanneer in categorie inschrijving (07), groep %s is " +
              "aangegeven dat de bijhouding is opgeschort met reden \"E\" of \"M\"",
              DOCUMENTINDICATIE, OPSCHORTING);
        }
      }

      if (elem.getGroup().is(ADRES)
          && elems.isApplicable(PersonListMutationsChecks::isDefaultValue, STRAATNAAM)
          && !elem.getElem().isElement(STRAATNAAM)) {
        return msg("Als %s onbekend is dan mogen de andere elementen uit de groep ook niet voorkomen",
            STRAATNAAM.getDescr());
      }
    }

    // checks for category 9
    if (elem.getCat().is(KINDEREN)) {
      if (isNotMutual(elem, elems, IDNUMMERS, REG_AFSTAMMING)) {
        return msg(MUTUAL_EXCLUSIVE, IDNUMMERS, REG_AFSTAMMING);
      }
    }

    // checks for category 11
    if (elem.getCat().is(GBACat.GEZAG)) {
      if (isNotMutual(elem, elems, GEZAG_MINDERJARIGE, CURATELE)) {
        return msg(MUTUAL_EXCLUSIVE, GEZAG_MINDERJARIGE, CURATELE);
      }
    }

    // checks for category 12
    if (elem.getCat().is(GBACat.REISDOC)) {
      if (isNotMutual(elem, elems, NED_REISDOCUMENT, SIGNALERING)) {
        return msg(MUTUAL_EXCLUSIVE, NED_REISDOCUMENT, SIGNALERING);
      }
    }

    // checks for category 13
    if (elem.getCat().is(GBACat.KIESR)) {
      if (isNotMutual(elem, elems, EUROPEES_KIESRECHT, UITSL_KIESR)) {
        return msg(MUTUAL_EXCLUSIVE, EUROPEES_KIESRECHT, UITSL_KIESR);
      }
    }

    return Optional.empty();
  }

  private static Optional<BasePLValue> getRegisterGemeenteVerbintenis(
      PersonListMutElems elems,
      PersonListMutElem mutElem) {
    Optional<PersonListMutElem> reden = elems.getElem(REDEN_ONTBINDING);
    if (reden.isPresent()) {
      if ("S".equals(reden.get().getNewValue())) { // S = Echtscheiding
        for (BasePLRec rec : mutElem.getSet().getRecs()) {
          if (!rec.isIncorrect()) {
            BasePLValue datumOntbinding = rec.getElemVal(DATUM_ONTBINDING);
            BasePLValue plaatsVerbintenis = rec.getElemVal(PLAATS_VERBINTENIS);
            BasePLValue registerGemeente = rec.getElemVal(REGIST_GEM_AKTE);
            if (StringUtils.isBlank(datumOntbinding.getDescr())
                && plaatsVerbintenis.isNotBlank()
                && registerGemeente.isNotBlank()) {
              return Optional.of(registerGemeente);
            }
          }
        }
      }
    }
    return Optional.empty();
  }

  /**
   * Is het first date is older than the second date
   */
  private static boolean isFirstDateOlderThanSecond(ProcuraDate firstDate, ProcuraDate secondDate) {
    return secondDate.getSystemDate().equals(firstDate.getSystemDate())
        || (secondDate.isCorrect()
            && firstDate.isCorrect()
            && secondDate.isExpired(firstDate));
  }

  private static Optional<String> msg(String msg, GBAGroup... groups) {
    return Optional.of(String.format(msg, Arrays.stream(groups)
        .map(g -> "groep " + g.getDescrAndCode().toLowerCase())
        .toArray()));
  }

  private static Optional<String> msg(String msg, String... values) {
    return Optional.of(String.format(msg, values));
  }

  public static Validator getValidator(PersonListMutElem mutElem, PersonListMutElems mutElems) {

    return new Validator() {

      @Override
      public void validate(Object value) throws InvalidValueException {
        Optional<String> error = getValueError(mutElem, mutElems);
        if (error.isPresent()) {
          throw new InvalidValueException(error.get());
        }
      }

      @Override
      public boolean isValid(Object value) {
        return !getValueError(mutElem, mutElems).isPresent();
      }
    };
  }

  private static boolean isNotMutual(PersonListMutElem mutElem,
      PersonListMutElems mutElems,
      GBAGroup group1,
      GBAGroup group2) {
    return ((mutElem.getGroup().is(group1) && !mutElems.isAllBlank(group2))
        || (mutElem.getGroup().is(group2) && !mutElems.isAllBlank(group1)));
  }

  private static boolean isDefaultValue(PersonListMutElem elem) {
    return StringUtils.containsAny(elem.getNewValue().toLowerCase(), "onbekend", "standaardwaarde");
  }

  private static Map<GBAGroup, List<GBAElem>> getRequiredElements() {
    Map<GBAGroup, List<GBAElem>> elems = new LinkedHashMap<>();
    elems.put(IDNUMMERS, singletonList(ANR));
    elems.put(NAAM, singletonList(GESLACHTSNAAM));
    elems.put(ADRESHUISHOUDING, asList(FUNCTIE_ADRES, DATUM_AANVANG_ADRESH));
    elems.put(ADRES, singletonList(STRAATNAAM));
    elems.put(EMIGRATIE, asList(LAND_VERTREK, DATUM_AANVANG_ADRESH));
    elems.put(ANUMMERVERWIJZINGEN, new ArrayList<>());
    elems.put(EUROPEES_KIESRECHT, asList(AANDUIDING_EURO_KIESR, DATUM_VERZ_OF_MED_EURO_KIESR));
    elems.put(NED_REISDOCUMENT, asList(SOORT_NL_REISDOC, NR_NL_REISDOC, DATUM_UITGIFTE_NL_REISDOC,
        AUTORIT_VAN_AFGIFTE_NL_REISDOC, DATUM_EINDE_GELDIG_NL_REISDOC));
    elems.put(UITSL_KIESR, singletonList(AAND_UITGESLOTEN_KIESR));
    elems.put(VERBLIJFSTITEL, asList(AAND_VBT, INGANGSDATUM_VBT));
    elems.put(PROCEDURE, asList(AAND_GEG_IN_ONDERZ, DATUM_INGANG_ONDERZ));
    elems.put(GBAGroup.RNI_DEELNEMER, singletonList(GBAElem.RNI_DEELNEMER));
    return elems;
  }
}
