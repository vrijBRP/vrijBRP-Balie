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

package nl.procura.diensten.gba.ple.procura.utils;

import static nl.procura.standard.Globalfunctions.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.procura.templates.PLETemplateProcura;
import nl.procura.diensten.gba.ple.procura.utils.diacrits.Diacrieten;
import nl.procura.diensten.gba.ple.procura.utils.diacrits.Diacs;
import nl.procura.gba.jpa.probev.db.BAdres;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PLEElementFormatter {

  private final Map<String, BasePLValue> cache    = new HashMap<>();
  private PLETemplateProcura             template = null;
  private Diacrieten                     diac;

  public PLEElementFormatter(PLETemplateProcura template) {
    setTemplate(template);
    setDiac(new Diacrieten(template.getEntityManager()));
  }

  public BasePLValue format(int catCode, int elementCode, Object input) {

    if (input == null) {
      return null;
    }

    BasePLValue waarden = getFormat(catCode, elementCode, input);
    GBAElem gbaElement = GBAElem.getByCode(elementCode);

    switch (gbaElement) {
      case STRAATNAAM:
        getTemplate().addElem(GBAElem.STRAATNAAM_OFFIC, waarden);
        getTemplate().addElem(GBAElem.STRAATNAAM_NEN, waarden);
        break;

      default:
        break;
    }

    return waarden;
  }

  public Diacrieten getDiac() {
    return diac;
  }

  public void setDiac(Diacrieten diac) {
    this.diac = diac;
  }

  public PLETemplateProcura getTemplate() {
    return template;
  }

  public void setTemplate(PLETemplateProcura template) {
    this.template = template;
  }

  private void diacriet(BasePLValue waarden, Object input, String diacType, boolean isLandelijk) {

    try {
      Object code = get(input, "C" + input.getClass().getSimpleName());

      if (aval(code) < 0) {
        return;
      }

      waarden.setCode(astr(code));
      String omschrijving = getDiac().merge(input, diacType);

      if (diacType.equals(Diacs.PLAATS) && (aval(code) > 2000)) {
        waarden.setVal(omschrijving);
      } else {
        waarden.setVal(astr(isLandelijk ? code : omschrijving));
      }

      waarden.setDescr(omschrijving);
    } catch (Exception e) {
      log.debug(e.toString());
    }
  }

  private void expand(BasePLValue waarden, Object input, String codeString, String expandString,
      boolean isLandelijk) {
    Object code = null;
    String value = "";
    String naam = fil(expandString) ? expandString : input.getClass().getSimpleName();

    try {
      code = get(input, (fil(codeString) ? codeString : "C" + naam));

      if ((code instanceof Number) && (aval(code) < 0)) {
        return;
      }

      value = astr(get(input, naam));
    } catch (Exception e) {
      log.error(e.toString());
    }

    waarden.setCode(astr(code));
    waarden.setVal(isLandelijk ? astr(code) : value);
    waarden.setDescr(value);
  }

  private BasePLValue getFormat(int catCode, int elementCode, Object input) {

    if (input == null) {
      return null;
    }

    BasePLValue waarden = new BasePLValue();
    String key = catCode + "." + elementCode + "." + input;

    if (cache.containsKey(key)) {
      return cache.get(key);
    }

    GBAElem gbaElement = GBAElem.getByCode(elementCode);
    boolean isLandelijkeTabel = gbaElement.getTable().isNational();

    if (input instanceof String) {
      waarden.setVal(((String) input).trim());
      waarden.setCode(((String) input).trim());

    } else if (input instanceof Number) {
      if (along(input) >= 0) {
        waarden.setVal(astr(input));
        waarden.setCode(astr(input));
      } else {
        cache.put(key, waarden);
        return waarden;
      }
    } else {
      expand(waarden, input, "", "", isLandelijkeTabel);
    }

    if (gbaElement.getTable() == GBATable.OPENBARE_RUIMTE) {
      diacriet(waarden, input, Diacs.PLAATS, isLandelijkeTabel);
    }

    if (gbaElement.getTable() == GBATable.PLAATS) {
      diacriet(waarden, input, Diacs.PLAATS, isLandelijkeTabel);
    }

    if (gbaElement.getTable() == GBATable.LAND) {
      diacriet(waarden, input, Diacs.LAND, isLandelijkeTabel);
    }

    if (gbaElement.getTable() == GBATable.NATIO) {
      diacriet(waarden, input, Diacs.NATIO, isLandelijkeTabel);
    }

    if (gbaElement.getTable() == GBATable.LOCATIE) {
      diacriet(waarden, input, Diacs.LOCATIE, isLandelijkeTabel);
    }

    switch (gbaElement) {
      case BSN:
        waarden.setVal(padl(waarden.getVal(), 9));
        break;

      case AAND_VBT:
        expand(waarden, input, "CVbt", "Oms", isLandelijkeTabel);
        break;

      case WPL_NAAM:
        diacriet(waarden, input, Diacs.WPL, isLandelijkeTabel);
        Object cWplBag = get(input, "CWplBag");
        waarden.setVal(pos(cWplBag) ? astr(cWplBag) : "");
        break;

      case LENGTE_HOUDER:
        waarden.setVal(padl(waarden.getVal(), 3));
        break;

      case VERSIENR:
        waarden.setVal(padl(waarden.getVal(), 4));
        break;

      case AFNEMERSINDICATIE:
      case AAND_GEG_IN_ONDERZ:
        waarden.setVal(padl(waarden.getVal(), 6));
        break;

      case INGANGSDAT_GELDIG:
        waarden.setVal(padl(waarden.getVal(), 8));
        break;

      case TITEL_PREDIKAAT:
        expand(waarden, input, "Zoekarg", "", isLandelijkeTabel);
        break;

      case VOORV_GESLACHTSNAAM:
        expand(waarden, input, "Voorv", "", isLandelijkeTabel);
        break;

      case VOORNAMEN:
        diacriet(waarden, input, Diacs.VOORN, isLandelijkeTabel);
        break;

      case GESLACHTSNAAM:
        diacriet(waarden, input, Diacs.NAAM, isLandelijkeTabel);
        onbekendeWaardeOmschrijving(waarden);
        break;

      case REDEN_ONTBINDING:
        expand(waarden, input, "Rdn", "Oms", isLandelijkeTabel);
        break;

      case STRAATNAAM:
        diacriet(waarden, input, Diacs.STRAAT, isLandelijkeTabel);
        onbekendeWaardeOmschrijving(waarden);
        break;

      case BESCHRIJVING_DOC:
        diacriet(waarden, input, Diacs.DOC, isLandelijkeTabel);
        onbekendeWaardeOmschrijving(waarden);
        break;

      case SOORT_NL_REISDOC:
        diacriet(waarden, input, Diacs.REISDOC, isLandelijkeTabel);
        waarden.setVal(astr(get(input, "Zoekarg")));
        break;

      case GEM_INSCHR_CODE:
        waarden.setVal(padl(waarden.getVal(), 4));
        break;

      case IND_GEZAG_MINDERJ:
        diacriet(waarden, input, Diacs.VOOGDIJ, isLandelijkeTabel);
        waarden.setVal(astr(get(input, "IndVoogdij")));
        break;

      default:
        break;
    }

    // Buitenlands adres
    if (input instanceof BAdres) {
      waarden.setVal(getDiac().merge(input, Diacs.B_ADRES, astr(get(input, "Adr"))));
    }

    if (aval(waarden.getVal()) >= 0) {
      switch (gbaElement.getTable()) {
        case PLAATS:
        case NATIO:
        case LAND:
          waarden.setVal(padl(waarden.getVal(), 4));
          break;

        case REDEN_NATIO:
          waarden.setVal(padl(waarden.getVal(), 3));
          break;

        default:
          break;
      }
    }

    // Omschrijving uit tabel aanvullen
    if (fil(waarden.getVal()) && emp(waarden.getDescr())) {
      // Waarde uit tabel halen
      String waarde = gbaElement.getTable().getValues().get(waarden.getVal()).getValue();

      // Formatteren
      waarden.setDescr(gbaElement.getVal().getFormat(waarde));
    }

    cache.put(key, waarden);
    return waarden;
  }

  private void onbekendeWaardeOmschrijving(BasePLValue waarden) {
    if (waarden.getVal().equals(".")) {
      waarden.setDescr("Onbekend (standaardwaarde)");
    }
  }

  private String padl(String waarde, int i) {
    return getTemplate().padl(waarde, i);
  }

  private String toGet(String name) {
    return "get" + name;
  }

  private Object get(Object o, String name) {
    try {
      Method subMethod = o.getClass().getMethod(toGet(name));
      return subMethod.invoke(o);
    } catch (Exception e) {
      log.debug("Method not found: " + o.getClass() + " = " + e);
    }

    return null;
  }
}
