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

package nl.procura.gba.jpa.personen.dao.views.verwijderzaken;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

import lombok.Data;

@Data
public class VerwijderVerlopenZaakActie extends VerwijderZaakActie<ZaakKey> {

  private VerwijderZaakType type;
  private int               jaren;
  private final int         maanden;

  public VerwijderVerlopenZaakActie(VerwijderZaakType type, int jaren, int maanden) {
    this.type = type;
    this.jaren = jaren;
    this.maanden = maanden;
  }

  @Override
  public long getAantal() {
    VerwijderZakenQuery verwijderQuery = getAantalQuery();
    Query query = GbaJpa.getManager().createQuery(verwijderQuery.getSql().toString());
    verwijderQuery.getParameters().forEach(query::setParameter);
    return (long) query.getSingleResult();
  }

  @Override
  public List<VerwijderZakenQuery> getQueries() {
    return Arrays.asList(getAantalQuery(), getZaakIdsQuery(), getVerwijderQuery());
  }

  @Override
  public List<ZaakKey> getResultaten(int maxAantal) {
    VerwijderZakenQuery verwijderQuery = getZaakIdsQuery();
    Query query = GbaJpa.getManager().createQuery(verwijderQuery.getSql().toString());
    query.setMaxResults(maxAantal);
    verwijderQuery.getParameters().forEach(query::setParameter);
    return toZaakKeys(query.getResultList(), type.getZaakType());
  }

  @Override
  public int verwijder() {
    VerwijderZakenQuery verwijderQuery = getVerwijderQuery();
    Query query = GbaJpa.getManager().createQuery(verwijderQuery.getSql().toString());
    verwijderQuery.getParameters().forEach(query::setParameter);
    return query.executeUpdate();
  }

  public LocalDate getTermijnAsLocalDate() {
    return LocalDate.now().minusYears(jaren).minusMonths(maanden);
  }

  public String getTermijnAsString() {
    StringBuilder termijn = new StringBuilder();
    if (jaren > 0) {
      termijn.append(jaren).append(" jaar");
    }
    if (maanden > 0) {
      if (termijn.length() > 0) {
        termijn.append(" en ");
      }
      termijn.append(maanden).append(" maanden");
    }
    return termijn.toString();
  }

  private VerwijderZakenQuery getAantalQuery() {
    return addParameters(new VerwijderZakenQuery()
        .sql("select count(t) from ZakenView t where t.zaakType = :id")
        .sql(whereZaakDate()));
  }

  private VerwijderZakenQuery getZaakIdsQuery() {
    return addParameters(new VerwijderZakenQuery()
        .sql("select t.zaakId from ZakenView t where t.zaakType = :id")
        .sql(whereZaakDate())
        .sql(" order by t.dInvoer desc"));
  }

  private VerwijderZakenQuery getVerwijderQuery() {
    return addParameters(new VerwijderZakenQuery()
        .sql("delete from ")
        .sql(type.getEntity().getSimpleName())
        .sql(" t where t.zaakId in (select t.zaakId from ZakenView t ")
        .sql("where t.zaakType = :id")
        .sql(whereZaakDate())
        .sql(")"));
  }

  private VerwijderZakenQuery addParameters(VerwijderZakenQuery query) {
    query.param("id", type.getId())
        .param("enddate", toNumericDate(jaren, maanden))
        .param("canceldate", toNumericDate(1, 0))
        .param("cancelstatus", ZaakStatusType.GEANNULEERD.getCode());
    return query;
  }

  private String whereZaakDate() {
    return " and (((t.dIngang > 0 and t.dIngang <= :enddate) or (t.dInvoer > 0 and t.dInvoer <= :enddate)) " +
        "or (t.indVerwerkt = :cancelstatus and t.dInvoer <= :canceldate))";
  }
}
