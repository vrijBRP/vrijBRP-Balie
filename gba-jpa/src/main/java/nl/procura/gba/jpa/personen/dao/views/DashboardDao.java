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

import static org.apache.commons.lang3.StringUtils.split;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public abstract class DashboardDao {

  protected static Collection<? extends BigDecimal> typeDoss(ZaakType... zaakTypes) {
    return Arrays.stream(zaakTypes).map(zt -> BigDecimal.valueOf(zt.getCode())).collect(Collectors.toList());
  }

  protected static Predicate andAnyOf(boolean digitaal, StringPath field, String value) {
    if (digitaal && StringUtils.isNotBlank(value)) {
      return new BooleanBuilder()
          .andAnyOf(Arrays.stream(split(value, ','))
              .map(String::trim)
              .map(field::like)
              .toArray(Predicate[]::new));
    }
    return null;
  }

  protected static BooleanExpression period(NumberPath<?> field, DashboardPeriode periode) {
    return field.goe(periode.getdFrom()).and(field.loe(periode.getdTo()));
  }

  protected static JPAQueryFactory getJpaQueryFactory() {
    EntityManager em = GbaJpa.getManager();
    return new JPAQueryFactory(em);
  }

  protected static List<ZaakKey> toZaakKeys(List<String> zaakIds, ZaakType zaakType) {
    List<ZaakKey> zaakKeys = new ArrayList<>();
    for (String zaakId : zaakIds) {
      zaakKeys.add(new ZaakKey(zaakId, zaakType));
    }

    return zaakKeys;
  }

  protected static BooleanBuilder getBronEnLeverancier(StringPath bronField, StringPath levField,
                                                       boolean digitaal, DashboardPeriode periode) {
    return new BooleanBuilder(andAnyOf(digitaal, bronField, periode.getBronnen()))
        .or(andAnyOf(digitaal, levField, periode.getLeveranciers()));
  }

  public static class DashboardPeriode {

    private final String bronnen;
    private final String leveranciers;
    private long         dFrom;
    private long         dTo;

    public DashboardPeriode(long dFrom, long dTo, String bronnen, String leveranciers) {
      super();
      this.dFrom = dFrom;
      this.dTo = dTo;
      this.bronnen = bronnen;
      this.leveranciers = leveranciers;
    }

    public long getdFrom() {
      return dFrom;
    }

    public long getdTo() {
      return dTo;
    }

    public String getBronnen() {
      return bronnen;
    }

    public String getLeveranciers() {
      return leveranciers;
    }
  }
}
