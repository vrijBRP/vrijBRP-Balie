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

package nl.procura.gba.jpa.personen.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personen.db.FileImport;
import nl.procura.gba.jpa.personen.db.FileRecord;

public class FileImportDao extends GenericDao {

  public static Optional<FileRecord> getFileRecordById(Long cFileRecord) {
    TypedQuery<FileRecord> query = createQuery("select r from FileRecord r "
        + "where r.cFileRecord = :cFileRecord", FileRecord.class);
    query.setParameter("cFileRecord", cFileRecord);
    return query.getResultList().stream().findFirst();
  }

  public static List<String> getZaakIds(Long cFileRecord) {
    TypedQuery<String> query = createQuery("select r.doss.zaakId from DossRegistration r "
        + "where r.cFileRecord = :cFileRecord", String.class);
    query.setParameter("cFileRecord", cFileRecord);
    return query.getResultList();
  }

  public static List<FileImport> getFileImports() {
    return createQuery("select f from FileImport f "
        + "order by f.cFileImport desc", FileImport.class).getResultList();
  }

  public static long countNewFileRecords(FileImport fileImport) {
    TypedQuery<Long> query = createQuery("select count(f) from FileRecord f "
        + "where f.cFileImport = :cFileImport and f.cFileRecord "
        + "not in (select r.cFileRecord from DossRegistration r where r.cFileRecord is not null)", Long.class);
    query.setParameter("cFileImport", fileImport.getCFileImport());
    return query.getSingleResult();
  }

  public static long countAllFileRecords(FileImport fileImport) {
    TypedQuery<Long> query = createQuery("select count(f) from FileRecord f "
        + "where f.cFileImport = :cFileImport", Long.class);
    query.setParameter("cFileImport", fileImport.getCFileImport());
    return query.getSingleResult();
  }

  public static List<FileRecord> getFileRecords(FileImport fileImport) {
    TypedQuery<FileRecord> query = createQuery("select f from FileRecord f "
        + "where f.cFileImport = :cFileImport order by f.cFileRecord desc", FileRecord.class);
    query.setParameter("cFileImport", fileImport.getCFileImport());
    return query.getResultList();
  }

  public static List<Long> getIdsWithReferenceToRegistration(FileImport fileImport) {
    TypedQuery<Long> query = createQuery("select f.cFileRecord from FileRecord f "
        + "where f.cFileImport = :cFileImport and f.cFileRecord "
        + "in (select r.cFileRecord from DossRegistration r where r.cFileRecord is not null)", Long.class);
    query.setParameter("cFileImport", fileImport.getCFileImport());
    return query.getResultList();
  }
}
