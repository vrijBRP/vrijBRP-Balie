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

package nl.procura.gba.web.common.tables;

import static nl.procura.commons.misc.teletex.Teletex.fromUtf8;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.burgerzaken.gba.core.tables.GBATableValues;
import nl.procura.commons.misc.teletex.Teletex;
import nl.procura.gba.web.services.gba.tabellen.TabelRecord;
import nl.procura.gba.web.services.gba.tabellen.TabelRecordAttributen;
import nl.procura.gba.web.services.gba.tabellen.TabellenService;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValueAttributes;
import nl.procura.vaadin.component.field.fieldvalues.TabelFieldValue;

public final class GBATableList {

  private final List<TabelFieldValue> items = new ArrayList<>();
  private GBATable                    tabel;
  private TabelRecordAttributen       matchingAttributen;

  public GBATableList(GBATable tabel) {
    this(tabel, null);
  }

  public GBATableList(GBATable tabel, TabelRecordAttributen recordAttributen) {
    setMatchingAttributen(recordAttributen);
    setTabel(tabel);
  }

  public List<TabelFieldValue> get() {
    loadIfEmpty();
    return items;
  }

  public TabelFieldValue get(Number waarde) {
    return get(astr(waarde));
  }

  public FieldValue get(Number waarde, String omschrijving) {
    return pos(waarde) ? get(astr(waarde)) : new FieldValue(omschrijving);
  }

  public TabelFieldValue get(String waarde) {
    return get(waarde, false);
  }

  public TabelFieldValue getByKey(Number waarde) {
    return getByKey(astr(waarde));
  }

  public TabelFieldValue getByDescr(String omschrijving) {
    return get().stream()
        .filter(tfv -> {
          byte[] data1 = fromUtf8(tfv.getDescription().getBytes()).getBaseCharactersData();
          byte[] data2 = fromUtf8(omschrijving.getBytes()).getBaseCharactersData();
          return Arrays.equals(data1, data2);
        })
        .findFirst()
        .orElse(new TabelFieldValue(omschrijving, omschrijving));
  }

  public TabelRecordAttributen getMatchingAttributen() {
    return matchingAttributen;
  }

  public void setMatchingAttributen(TabelRecordAttributen matchingAttributen) {
    this.matchingAttributen = matchingAttributen;
  }

  public GBATable getTabel() {
    return tabel;
  }

  public void setTabel(GBATable tabel) {
    this.tabel = tabel;
    reload();
  }

  /**
   * loads the table items if table is empty
   */
  public void loadIfEmpty() {
    if (items.isEmpty()) {
      reload();
    }
  }

  /**
   * Clears the items of the table and reloads
   */
  public void reload() {
    items.clear();
    if (getTabel().getValues().getValues().size() > 0) {
      // Standaard waarden gebruiken
      for (GBATableValues.Value waarde : getTabel().getValues().getValues()) {
        items.add(new TabelFieldValue(waarde.getKey(), waarde.getValue()));
      }
    } else {

      // Tabel waarden gebruiken (gemeentelijk / landelijk)
      for (TabelRecord record : TabellenService.getTabel(getTabel()).getRecords()) {
        String key = record.getCode();
        String oms = record.getOmschrijving();
        long dIn = record.getDatumIngang();
        long dEnd = record.getDatumEinde();

        TabelFieldValue itemId;
        if (getTabel().isNational()) {
          // Bij landelijke tabel is de code van belang
          itemId = new TabelFieldValue(key, key, oms, dIn, dEnd);
        } else {
          // Bij gemeentelijke tabel is de code van NIET van belang.
          itemId = new TabelFieldValue(key, oms, oms, dIn, dIn);
        }

        // Voeg attributen toe aan de TabelFieldValue
        Map<String, String> attributes = record.getAttributen().getMap();
        if (attributes != null && !attributes.isEmpty()) {
          itemId.setAttributes(new FieldValueAttributes(attributes));
        }

        // Match attributen
        if (matchingAttributen != null) {
          if (matches(itemId.getAttributes())) {
            items.add(itemId);
          }
        } else {
          items.add(itemId);
        }
      }
    }
    sortByValue();
  }

  private TabelFieldValue get(String waarde, boolean byKey) {
    for (TabelFieldValue tfv : get()) {
      Object val = (byKey ? tfv.getKey() : tfv.getValue());
      long codeVal = along(val);
      long codeWaarde = along(waarde);
      boolean isCodeMatch = (codeVal >= 0 && codeWaarde >= 0 && codeVal == codeWaarde);
      if (Objects.equals(astr(val), waarde) || isCodeMatch) {
        return tfv;
      }
    }

    return new TabelFieldValue();
  }

  private TabelFieldValue getByKey(String waarde) {
    return get(waarde, true);
  }

  private boolean matches(FieldValueAttributes fieldValueAttributes) {
    if (matchingAttributen != null) {
      Map<String, String> matchMap = matchingAttributen.getMap();
      if (matchMap != null && fieldValueAttributes != null) {
        for (Entry<String, String> matchEntry : matchMap.entrySet()) {
          if (!fieldValueAttributes.is(matchEntry.getKey(), matchEntry.getValue())) {
            return false;
          }
        }
      }
    }

    return true;
  }

  /**
   * Non-expired values more important than expired values.
   * Otherwise sort on description
   */
  private void sortByValue() {
    items.sort((val1, val2) -> {
      if (val1.getDateEnd() < 0 && val2.getDateEnd() >= 0) {
        return -1;
      } else if (val1.getDateEnd() >= 0 && val2.getDateEnd() < 0) {
        return 1;
      }
      return val1.getDescription().compareTo(val2.getDescription());
    });
  }
}
