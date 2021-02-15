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

package nl.procura.diensten.gba.wk.extensions;

import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.fil;

import java.text.Collator;
import java.util.Comparator;

public class WKWrapperSorter implements Comparator<BaseWKExt> {

  private final Collator collator = Collator.getInstance();

  @Override
  public int compare(BaseWKExt adr1, BaseWKExt adr2) {

    int c1 = compared(getStraat(adr1), getStraat(adr2));
    if (c1 != 0) {
      return c1;
    }

    int c2 = compared(getHnr(adr1), getHnr(adr2));
    if (c2 != 0) {
      return c2;
    }

    int c3 = compared(getHnrL(adr1), getHnrL(adr2));
    if (c3 != 0) {
      return c3;
    }

    int c4 = compared(getHnrT(adr1), getHnrT(adr2));
    if (c4 != 0) {
      return c4;
    }

    int c5 = compared(getHnrA(adr1), getHnrA(adr2));
    return c5;
  }

  private int compared(Object obj1, Object obj2) {

    if (obj1 instanceof Number) {
      return Integer.compare(aval(obj1), aval(obj2));
    }
    return collator.compare(obj1, obj2);
  }

  private String getStraat(BaseWKExt adr) {

    String straat = adr.getBasisWk().getStraat().getDescr();
    String locatie = adr.getBasisWk().getLocatie().getDescr();
    return fil(straat) ? straat : locatie;
  }

  private int getHnr(BaseWKExt adr) {
    return aval(adr.getBasisWk().getHuisnummer().getCode());
  }

  private String getHnrL(BaseWKExt adr) {
    return adr.getBasisWk().getHuisletter().getDescr();
  }

  private String getHnrT(BaseWKExt adr) {
    return adr.getBasisWk().getToevoeging().getDescr();
  }

  private String getHnrA(BaseWKExt adr) {
    return adr.getBasisWk().getAanduiding().getDescr();
  }
}
