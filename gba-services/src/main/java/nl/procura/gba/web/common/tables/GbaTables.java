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

import java.util.HashMap;
import java.util.Map;

import nl.procura.burgerzaken.gba.core.enums.GBATable;

public final class GbaTables {

  private static Map<GBATable, GBATableList> map = new HashMap<>();

  public static GBATableList STRAAT                 = get(GBATable.STRAAT);
  public static GBATableList WOONPLAATS             = get(GBATable.WOONPLAATS);
  public static GBATableList PLAATS                 = get(GBATable.PLAATS);
  public static GBATableList LAND                   = get(GBATable.LAND);
  public static GBATableList NATIO                  = get(GBATable.NATIO);
  public static GBATableList WIJK                   = get(GBATable.WIJK);
  public static GBATableList BUURT                  = get(GBATable.BUURT);
  public static GBATableList SUBBUURT               = get(GBATable.SUBBUURT);
  public static GBATableList LOCATIE                = get(GBATable.LOCATIE);
  public static GBATableList GEMDEEL                = get(GBATable.GEMEENTE_DEEL);
  public static GBATableList AANDUIDING             = get(GBATable.HNR_AANDUIDING);
  public static GBATableList TITEL                  = get(GBATable.TITEL_PREDIKAAT);
  public static GBATableList VBT                    = get(GBATable.VERBLIJFSTITEL);
  public static GBATableList REDEN_NATIO            = get(GBATable.REDEN_NATIO);
  public static GBATableList REDEN_HUW_ONTB         = get(GBATable.REDEN_HUW_ONTB);
  public static GBATableList NED_REISDOC            = get(GBATable.NED_REISDOC);
  public static GBATableList AKTE_AANDUIDING        = get(GBATable.AKTE_AANDUIDING);
  public static GBATableList AUT_VERSTREK_NED_REISD = get(GBATable.AUT_VERSTREK_NED_REISD);
  public static GBATableList NAAMGEBRUIK            = get(GBATable.AAND_NAAMGEBRUIK);
  public static GBATableList VOORVOEGSEL            = get(GBATable.VOORVOEGSEL);

  private GbaTables() {
  }

  /**
   * Stores the tables in a static map.
   */
  public synchronized static GBATableList get(GBATable table) {
    GBATableList tableList = map.get(table);
    if (tableList != null) {
      return tableList;
    }
    tableList = new GBATableList(table);
    map.put(table, tableList);
    return tableList;
  }

  public synchronized static void reload() {
    map.values().forEach(GBATableList::reload);
  }

  public synchronized static void loadIfNeeded() {
    map.values().forEach(GBATableList::loadIfEmpty);
  }
}
