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

package examples.nl.procura.ple.local;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Query;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEJpaManager;
import nl.procura.validation.Anummer;

public class FindTableRecords extends AbstractLocalExample {

  private final String TEST_ANR = "317.2164.810";

  private final PLEJpaManager manager;

  public static void main(String[] args) throws IOException {
    new FindTableRecords();
  }

  public FindTableRecords() throws IOException {
    manager = createManager();

    String[] tables = { "INW", "VDR", "MDR", "NAT", "HUW", "OVERL", "INSCHR",
        "VB", "AFST", "VBT", "GEZAG", "REISD", "KIESR", "VERW", "MUT_INDX", "AANT3" };

    Map<String, List<Map<String, Object>>> values = new LinkedHashMap<>();
    for (String table : tables) {
      values.put(table, getRows(table, new Anummer(TEST_ANR)));
    }
    URL resource = FindTableRecords.class.getClassLoader().getResource("ple.properties");
    if (resource != null) {
      File file = new File(resource.getFile());
      if (file.exists()) {
        File targetDir = file.getParentFile().getParentFile();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_HH_mm_ss"));
        FileUtils.writeStringToFile(new File(targetDir, "tables_" + timestamp + ".json"), gson.toJson(values));
      }
    }
  }

  private List<Map<String, Object>> getRows(String table, Anummer anr) {
    List<Map<String, Object>> valuesList = new ArrayList<>();
    List<String> columns = getColumns(table);
    String columnsString = String.join(", ", columns);
    String sql = "SELECT {0} from {1} where A1 = ''{2}'' AND A2 = ''{3}'' AND A3 = ''{4}'' {5}";
    String formattedSql = MessageFormat.format(sql, columnsString, table,
        anr.getA1(), anr.getA2(), anr.getA3(), getOrderBy(columns));
    Query query = manager.getManager().createNativeQuery(formattedSql);

    for (Object result : query.getResultList()) {
      Object[] values = (Object[]) result;
      int idx = 0;
      Map<String, Object> valueMap = new LinkedHashMap<>();
      for (String col : columns) {
        valueMap.put(col, values[idx++]);
      }
      valuesList.add(valueMap);
    }
    return valuesList;
  }

  private String getOrderBy(List<String> columns) {
    List<String> order = Stream.of("D_GELD", "D_GBA", "D_MUT", "T_MUT")
        .filter(columns::contains)
        .map(orderCol -> orderCol + " desc")
        .collect(Collectors.toList());
    return order.isEmpty() ? "" : " order by " + String.join(", ", order);
  }

  private List<String> getColumns(String table) {
    String sql = "select column_name from all_tab_columns where table_name = ''{0}'' order by COLUMN_ID";
    Query query = manager.getManager().createNativeQuery(MessageFormat.format(sql, table));
    List<String> columns = new ArrayList<>();
    for (Object result : query.getResultList()) {
      columns.add(Objects.toString(result));
    }
    return columns;
  }
}
