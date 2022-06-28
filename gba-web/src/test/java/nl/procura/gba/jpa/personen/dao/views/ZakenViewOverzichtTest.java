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

package nl.procura.gba.jpa.personen.dao.views;

import static nl.procura.gba.web.services.TemporaryDatabase.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Table;

import org.junit.Test;

import nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType;

import liquibase.change.core.CreateViewChange;
import liquibase.changelog.ChangeSet;
import liquibase.database.core.HsqlDatabase;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

public class ZakenViewOverzichtTest {

  @Test
  @SneakyThrows
  public void mustMatchEnumeration() {
    Connection connection = getConnection();
    HsqlDatabase hsqlDatabase = getHsqlDatabase(connection);
    ChangeSet changeSet = getChangeSets(hsqlDatabase, "liquibase/changelogs/db.changelog-v1_27.xml")
        .stream().filter(set -> set.getId().equals("v1_27_6"))
        .findFirst().orElseThrow(() -> new IllegalArgumentException("No changeset found"));

    CreateViewChange change = (CreateViewChange) changeSet.getChanges().get(0);
    String query = change.getSelectQuery();

    String regex = "SELECT (\\w+).(\\w+), '(\\w+)' AS zaak_type, '(\\w+)' AS tabel";
    Matcher matcher = Pattern.compile(regex).matcher(cleanSQL(query));
    List<ViewGroup> viewGroups = new ArrayList<>();
    while (matcher.find()) {
      viewGroups.add(new ViewGroup(matcher.group(1), matcher.group(3), matcher.group(4)));
    }

    // Test if all subqueries in the view exist in the enumation
    viewGroups.forEach(vg -> Arrays.stream(VerwijderZaakType.values())
        .filter(vzt -> vg.id.equals(vzt.getId()))
        .filter(vzt -> vg.table.equals(toTableName(vzt)))
        .filter(vzt -> vg.dbTable.equals(toTableName(vzt)))
        .findFirst().orElseThrow(() -> new IllegalArgumentException("Not found: "
            + vg.id + " or " + vg.table)));

    Arrays.stream(VerwijderZaakType.values()).forEach(vzt -> viewGroups.stream()
        .filter(vg -> vg.id.equals(vzt.getId()))
        .filter(vg -> vg.table.equals(toTableName(vzt)))
        .filter(vg -> vg.dbTable.equals(toTableName(vzt)))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Not found: "
            + vzt.getId() + " or " + toTableName(vzt))));
  }

  private String toTableName(VerwijderZaakType vzt) {
    return vzt.getEntity().getAnnotation(Table.class).name();
  }

  @AllArgsConstructor
  private static class ViewGroup {

    private final String dbTable;
    private final String id;
    private final String table;
  }

  private static String cleanSQL(String sql) {
    return sql.replaceAll("\n", "").replaceAll("\\s+", " ");
  }
}
