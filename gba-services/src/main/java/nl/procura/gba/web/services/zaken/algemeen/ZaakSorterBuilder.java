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

package nl.procura.gba.web.services.zaken.algemeen;

import static nl.procura.standard.Globalfunctions.along;

import java.util.Comparator;

import com.google.common.collect.ComparisonChain;

import nl.procura.gba.jpa.personen.dao.ZaakKey;

public class ZaakSorterBuilder {

  public static ZaakKeySorter getZaakKeySorter(ZaakSortering sortering) {
    return new ZaakKeySorter(sortering);
  }

  public static ZaakSorter getZaakSorter(ZaakSortering sortering) {
    return new ZaakSorter(sortering);
  }

  private static int sort(ZaakSortering sortering, long dIn1, long dIn2, long tInv1, long tInv2, long dInv1,
      long dInv2, long code1, long code2) {

    switch (sortering) {
      case DATUM_INGANG_OUD_NIEUW:
      default:
        return ComparisonChain.start()
            .compare(dIn1, dIn2)
            .compare(dInv1, dInv2)
            .compare(tInv1, tInv2)
            .compare(code1, code2)
            .result();
      case DATUM_INGANG_NIEUW_OUD:
        return ComparisonChain.start()
            .compare(dIn2, dIn1)
            .compare(dInv2, dInv1)
            .compare(tInv2, tInv1)
            .compare(code2, code1)
            .result();
      case DATUM_INVOER_OUD_NIEUW:
        return ComparisonChain.start()
            .compare(dInv1, dInv2)
            .compare(tInv1, tInv2)
            .compare(code1, code2)
            .result();
      case DATUM_INVOER_NIEUW_OUD:
        return ComparisonChain.start()
            .compare(dInv2, dInv1)
            .compare(tInv2, tInv1)
            .compare(code2, code1)
            .result();
    }
  }

  public static class ZaakKeySorter implements Comparator<ZaakKey> {

    private ZaakSortering sortering = ZaakSortering.DATUM_INGANG_NIEUW_OUD;

    ZaakKeySorter(ZaakSortering sortering) {
      if (sortering != null) {
        this.sortering = sortering;
      }
    }

    @Override
    public int compare(ZaakKey o1, ZaakKey o2) {

      long dIn1 = o1.getdIng().longValue();
      long dIn2 = o2.getdIng().longValue();

      long tInv1 = o1.gettInv().longValue();
      long tInv2 = o2.gettInv().longValue();

      long dInv1 = o1.getdInv().longValue();
      long dInv2 = o2.getdInv().longValue();

      long code1 = o1.getCode();
      long code2 = o2.getCode();

      return sort(sortering, dIn1, dIn2, tInv1, tInv2, dInv1, dInv2, code1, code2);
    }
  }

  public static class ZaakSorter implements Comparator<Zaak> {

    private ZaakSortering sortering = ZaakSortering.DATUM_INGANG_NIEUW_OUD;

    ZaakSorter(ZaakSortering sortering) {
      if (sortering != null) {
        this.sortering = sortering;
      }
    }

    @Override
    public int compare(Zaak o1, Zaak o2) {

      long dIn1 = o1.getDatumIngang().getLongDate();
      long dIn2 = o2.getDatumIngang().getLongDate();

      long tInv1 = o1.getDatumTijdInvoer().getLongTime();
      long tInv2 = o2.getDatumTijdInvoer().getLongTime();

      long dInv1 = o1.getDatumTijdInvoer().getLongDate();
      long dInv2 = o2.getDatumTijdInvoer().getLongDate();

      long code1 = along(o1.getId());
      long code2 = along(o2.getId());

      return sort(sortering, dIn1, dIn2, tInv1, tInv2, dInv1, dInv2, code1, code2);
    }
  }
}
