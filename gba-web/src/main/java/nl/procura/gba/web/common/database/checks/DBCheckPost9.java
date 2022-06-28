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

package nl.procura.gba.web.common.database.checks;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.EntityManager;

import nl.procura.gba.web.common.database.DBCheckTemplateLb;

import liquibase.database.Database;

public class DBCheckPost9 extends DBCheckTemplateLb {

  public DBCheckPost9(EntityManager entityManager, Database database, String type) {
    super(entityManager, database, type, "Zaak_id gerelateerde tabellen opschonen");
  }

  @Override
  public void init() throws SQLException {
    count(deleteDanglingZaakIdRecords());
    count(deleteDanglingAantekeningRecords());
    count(deleteDanglingZaakAttrRecords());
    count(deleteDanglingZaakRelRecords());
    count(deleteDanglingPresentievraagRecords());
  }

  public int deleteDanglingZaakIdRecords() {
    return update("delete from zaak_id where zaak_id.intern_id != '' " +
        "and not exists (select zaken_view.zaak_id from zaken_view " +
        "where zaken_view.zaak_id = zaak_id.intern_id)");
  }

  public int deleteDanglingAantekeningRecords() {
    return update("delete from aantekening where aantekening.zaak_id != '' " +
        "and not exists (select zaken_view.zaak_id from zaken_view " +
        "where zaken_view.zaak_id = aantekening.zaak_id)");
  }

  public int deleteDanglingZaakAttrRecords() {
    return update("delete from zaak_attr where zaak_attr.zaak_id != '' " +
        "and not exists (select zaak_id from zaken_view " +
        "where zaken_view.zaak_id = zaak_attr.zaak_id)");
  }

  public int deleteDanglingZaakRelRecords() {
    return update("delete from zaak_rel " +
        "where (zaak_rel.zaak_id != '' " +
        "and (not exists (select zaak_id from zaken_view where zaak_rel.zaak_id = zaken_view.zaak_id) and " +
        "     not exists (select intern_id from zaak_id where zaak_rel.zaak_id = zaak_id.intern_id) and " +
        "     not exists (select extern_id from zaak_id where zaak_rel.zaak_id = zaak_id.extern_id))) " +
        "or (zaak_rel.zaak_id_rel != '' " +
        "and (not exists (select zaak_id from zaken_view where zaak_rel.zaak_id_rel = zaken_view.zaak_id) and " +
        "     not exists (select intern_id from zaak_id where zaak_rel.zaak_id_rel = zaak_id.intern_id) and " +
        "     not exists (select extern_id from zaak_id where zaak_rel.zaak_id_rel = zaak_id.extern_id)))");
  }

  public int deleteDanglingPresentievraagRecords() {
    return update("delete from presentievraag " +
        "where (presentievraag.zaak_id != '' and not exists (select zaak_id from zaken_view " +
        "where presentievraag.zaak_id = zaken_view.zaak_id)) " +
        "or (presentievraag.zaak_id = '' and d_in > 0 and d_in <= " + lastYear() + ")");
  }

  public int lastYear() {
    return Integer.parseInt(LocalDate.now().minusYears(1)
        .format(DateTimeFormatter.ofPattern("yyyyMMdd")));
  }
}
