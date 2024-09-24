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

package nl.procura.gba.web.modules.zaken.personmutations.page7;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.AFNEMERS;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.CONTACT;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.DIV;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.INSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.KIESR;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.KINDEREN;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.KLADBLOK;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.LOK_AF_IND;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_1;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_2;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.TIJD_VBA;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.VB;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.VBTITEL;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.VERW;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.WK;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_BEZIT_BUITENL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VAN_OPNEMING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IND_ONJUIST;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.INGANGSDAT_GELDIG;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LENGTE_HOUDER;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.STRAATNAAM_NEN;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.STRAATNAAM_OFFIC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VBT_OMS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VOLGCODE_GELDIG;
import static nl.procura.burgerzaken.gba.core.enums.GBARecStatus.HIST;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBAGroup;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.modules.zaken.personmutations.page2.BCMCheckResultElem;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListPrintData;
import nl.procura.gba.web.services.zaken.documenten.printen.DocumentTemplateData;

public class PersonListCategoryTemplateData extends DocumentTemplateData {

  public PersonListCategoryTemplateData(PersonListPrintData printData) {
    BasePLSet set = printData.getSet();
    BasePLExt pl = printData.getPl();
    put("bsn", printData.getPl().getPersoon().getBsn().getDescr());
    put("naam", printData.getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());
    put("gebDatum", printData.getPl().getPersoon().getGeboorte().getDatum());
    put("category", getDescrAndCode(set.getCatType()));
    long recordSize = set.getRecs().stream()
        .filter(this::isRelevantRecord)
        .count();

    final AtomicLong recordIndex = new AtomicLong(recordSize);
    put("records", set.getRecs().stream()
        .filter(this::isRelevantRecord)
        .map(rec -> {
          long currentRecord = recordIndex.getAndDecrement();
          return new DocRec(currentRecord, recordSize, rec);
        })
        .collect(Collectors.toList()));

    put("categories", pl.getCats().stream()
        .filter(this::isRelevantCat)
        .map(DocCat::new)
        .collect(Collectors.toList()));

    put("bcmCheck", new BCMCheck(printData));
  }

  private boolean isRelevantCat(BasePLCat cat) {
    return !cat.isCategoryType(TIJD_VBA, CONTACT, AFNEMERS, VERW, DIV, WK, KLADBLOK, LOK_AF_IND);
  }

  public class DocCat extends DocumentTemplateData {

    public DocCat(BasePLCat cat) {
      put("type", cat.getCatType());
      put("sets", cat.getSets().stream()
          .map((BasePLSet set) -> new DocSet(cat, set))
          .collect(Collectors.toList()));
    }
  }

  public class DocSet extends DocumentTemplateData {

    public DocSet(BasePLCat cat, BasePLSet set) {
      final AtomicLong recordIndex = new AtomicLong(set.getRecs().size());
      long recordSize = set.getRecs().size();
      put("status", getStatus(cat, set));
      put("index", set.getExtIndex());
      put("records", set.getRecs().stream()
          .map(rec -> {
            long currentRecord = recordIndex.getAndDecrement();
            return new DocRec(currentRecord, recordSize, rec);
          })
          .collect(Collectors.toList()));
    }

    private String getStatus(BasePLCat cat, BasePLSet set) {
      return "Set " + set.getExtIndex() + " van " + cat.getSets().size();
    }
  }

  private class DocRec extends DocumentTemplateData {

    public DocRec(long currentHist, long totalHist, BasePLRec rec) {
      Set<GBAGroup> groups = rec.getElems().stream()
          .sorted(PersonListCategoryTemplateData.this::compareGroup)
          .map(PersonListCategoryTemplateData.this::getGroup)
          .collect(Collectors.toCollection(LinkedHashSet::new));

      put("status", getStatus(rec, currentHist, totalHist));
      put("groups", groups.stream()
          .map(group -> new DocGroup(group, rec))
          .collect(Collectors.toList()));
    }

    private String getStatus(BasePLRec record, long currentHist, long totalHist) {
      StringBuilder value = new StringBuilder();

      if (record.isCat(OUDER_1, OUDER_2)) {
        String geslachtsnaam = record.getElemVal(GBAElem.GESLACHTSNAAM).getVal();

        if (StringUtils.isEmpty(geslachtsnaam)) {
          value.append("Juridisch geen ouder");
          value.append(" - ");

        } else if (".".equals(geslachtsnaam)) {
          value.append("Ouder onbekend");
          value.append(" - ");
        }
      }

      if (record.isBagChange()) {
        value.append("Dubbel ivm bag wijziging");
        value.append(" - ");
      }

      if (record.isInOnderzoek()) {
        value.append("In Onderzoek");
        value.append(" - ");
      }

      if (record.isAdmHistory()) {
        value.append("Admin. historie");
        value.append(" - ");

      } else {

        if (record.isConflicting()) {
          value.append("Strijdig");
          value.append(" - ");

        } else if (record.isIncorrect()) {
          value.append("Onjuist");
          value.append(" - ");

        } else if (record.isCat(KINDEREN)
            && "L".equals(record.getElemVal(GBAElem.REG_BETREKK).getVal())) {
          value.append("Levenloos geboren");
          value.append(" - ");
        }

        value.append(record.getStatus());
        value.append(" - ");
      }

      return String.format("Record %d van %d (%s)", currentHist, totalHist,
          value.toString().trim().replaceAll(" -$", ""));
    }
  }

  public class DocGroup extends DocumentTemplateData {

    public DocGroup(GBAGroup group, BasePLRec rec) {
      put("status", rec.getStatus().getDescr());
      put("elements", rec.getElems().stream()
          .filter(e -> getGroup(e).is(group))
          .filter(elem -> isRelevantElement(rec, elem))
          .sorted(PersonListCategoryTemplateData.this::compareGroup)
          .map(DocElement::new)
          .collect(Collectors.toList()));
    }

    private boolean isRelevantElement(BasePLRec rec, BasePLElem elem) {
      GBAElem gbaElem = GBAElem.getByCode(elem.getElemCode());
      GBACat gbaCat = GBACat.getByCode(rec.getCatType().getCode());

      if (!gbaElem.isNational()) {
        return false;
      }
      if (gbaElem.is(VOLGCODE_GELDIG)) {
        return false;
      }
      if (gbaCat.is(INSCHR) && gbaElem.is(INGANGSDAT_GELDIG, DATUM_VAN_OPNEMING)) {
        return false;
      }
      if (gbaCat.is(VB) && gbaElem.is(STRAATNAAM_OFFIC, STRAATNAAM_NEN)) {
        return false;
      }
      if (gbaCat.is(VBTITEL) && gbaElem.is(VBT_OMS)) {
        return false;
      }
      if (gbaCat.is(REISDOC) && gbaElem.is(LENGTE_HOUDER, AAND_BEZIT_BUITENL_REISDOC, IND_ONJUIST)) {
        return false;
      }
      return !gbaCat.is(KIESR) || !gbaElem.is(INGANGSDAT_GELDIG, DATUM_VAN_OPNEMING);
    }
  }

  public class DocElement extends DocumentTemplateData {

    public DocElement(BasePLElem elem) {
      GBAGroup group = getGroup(elem);
      GBAElem gbaElemType = GBAElem.getByCode(elem.getElemCode());
      put("type", gbaElemType);
      put("group", getDescrAndCode(group));
      put("code", getCode(gbaElemType));
      put("descr", elem.getValue().getDescr());
      put("codeDescr", getDescrAndCode(gbaElemType));
      put("value", getValue(elem));
    }
  }

  public static class BCMCheck extends DocumentTemplateData {

    public BCMCheck(PersonListPrintData printData) {
      put("header", printData.getBcmHeader());
      put("results", printData.getBcmResults()
          .stream()
          .map(BCMCheckResult::new)
          .collect(Collectors.toList()));
    }
  }

  public static class BCMCheckResult extends DocumentTemplateData {

    public BCMCheckResult(BCMCheckResultElem elem) {
      put("code", elem.getCode());
      put("descr", elem.getOmschrijving());
    }
  }

  public String getDescrAndCode(GBACat cat) {
    return String.format("%02d: %s", cat.getCode(), cat.getDescr());
  }

  public String getDescrAndCode(GBAGroup group) {
    return String.format("%02d: %s", group.getCode(), group.getDescr());
  }

  public String getCode(GBAElem elem) {
    String elemCode = String.format("%04d", elem.getCode());
    return String.format("%s", elemCode.substring(0, 2) + "." + elemCode.substring(2, 4));
  }

  public String getDescrAndCode(GBAElem elem) {
    String elemCode = String.format("%04d", elem.getCode());
    return String.format("%s: %s", elemCode.substring(0, 2) + "." + elemCode.substring(2, 4), elem.getDescr());
  }

  private String getValue(BasePLElem elem) {
    GBAElem gbaElem = GBAElem.getByCode(elem.getElemCode());
    boolean notBlank = StringUtils.isNotBlank(elem.getValue().getVal());
    boolean different = !elem.getValue().getVal().equals(elem.getValue().getDescr());
    boolean nationTable = gbaElem.getTable().isNational();
    boolean hasValues = !gbaElem.getTable().getValues().getValues().isEmpty();
    if (notBlank && different && (nationTable || hasValues)) {
      return elem.getValue().getVal() + " (" + elem.getValue().getDescr() + ")";
    }
    return elem.getValue().getDescr();
  }

  public GBAGroup getGroup(BasePLElem elem) {
    return GBAGroupElements.getByCat(elem.getCatCode(), elem.getElemCode()).getGroup();
  }

  public int compareGroup(BasePLElem o1, BasePLElem o2) {
    GBAGroup group1 = getGroup(o1);
    GBAGroup group2 = getGroup(o2);
    int compare = Integer.compare(group1.getCode(), group2.getCode());
    if (compare == 0) {
      return Integer.compare(o1.getElemCode(), o2.getElemCode());
    }
    return compare;
  }

  private boolean isRelevantRecord(BasePLRec r) {
    return !r.isAdmHistory() && !(r.isStatus(HIST) && r.getCatType().is(INSCHR, REISDOC, KIESR));
  }
}
