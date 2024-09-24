package nl.procura.gba.web.modules.zaken.personmutations.page7;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.INSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.KIESR;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.KINDEREN;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_1;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_2;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.REISDOC;
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
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.gba.web.modules.zaken.personmutations.page2.BCMCheckResultElem;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListPrintData;
import nl.procura.gba.web.services.zaken.documenten.printen.DocumentTemplateData;

public class PersonListCategoryTemplateData extends DocumentTemplateData {

  private final BasePLSet set;

  public PersonListCategoryTemplateData(PersonListPrintData printData) {
    set = printData.getSet();
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
          return new Record(currentRecord, recordSize, rec);
        })
        .collect(Collectors.toList()));
    put("bcmCheck", new BCMCheck(printData));
  }

  private class Record extends DocumentTemplateData {

    public Record(long currentHist, long totalHist, BasePLRec rec) {
      Set<GBAGroup> groups = rec.getElems().stream()
          .sorted(PersonListCategoryTemplateData.this::compareGroup)
          .map(PersonListCategoryTemplateData.this::getGroup)
          .collect(Collectors.toCollection(LinkedHashSet::new));

      put("status", getStatus(rec, currentHist, totalHist));
      put("groups", groups.stream()
          .map(group -> new Group(group, rec))
          .collect(Collectors.toList()));
    }
  }

  public class Group extends DocumentTemplateData {

    public Group(GBAGroup group, BasePLRec rec) {
      put("elements", rec.getElems().stream()
          .filter(e -> getGroup(e).is(group))
          .filter(e -> GBAElem.getByCode(e.getElemCode()).isNational())
          .sorted(PersonListCategoryTemplateData.this::compareGroup)
          .map(Element::new)
          .collect(Collectors.toList()));
    }
  }

  public class Element extends DocumentTemplateData {

    public Element(BasePLElem elem) {
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

  public class BCMCheck extends DocumentTemplateData {

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
    return GBAGroupElements.getByCat(set.getCatType().getCode(), elem.getElemCode()).getGroup();
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
