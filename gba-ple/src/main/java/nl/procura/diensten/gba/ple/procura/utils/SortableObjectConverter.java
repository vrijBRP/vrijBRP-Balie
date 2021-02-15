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

package nl.procura.diensten.gba.ple.procura.utils;

import static nl.procura.standard.Globalfunctions.*;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.jpa.probev.db.VboKrt;

public class SortableObjectConverter {

  public static final int FORCE_NEW_SET = -2; // Altijd een nieuw setje maken

  private final static Logger LOGGER = LoggerFactory.getLogger(SortableObjectConverter.class);

  private String anr(Object a1, Object a2, Object a3) {
    if (aval(astr(a1)) > 100) {
      return padl(a1, 3) + padl(a2, 4) + padl(a3, 3);
    }

    return null;
  } // anr (a1, a2, a3)

  public SortableObject convert(Object o) {
    SortableObject so = new SortableObject();
    String name = o.getClass().getSimpleName();
    Object hist = get(o, "Hist");

    so.setHist("");
    so.setObject(o);

    if (o instanceof VboKrt) {
      int dEnd = aval(get(get(o, "Id"), "DEnd"));
      if (pos(dEnd)) {
        hist = "Z";
      } else if (dEnd == -1) {
        hist = "A";
      }
    }

    if (hist != null) {
      so.setHist(hist.toString().toUpperCase());
    }

    Object pk = get(o, "Id");
    if (pk != null) {
      so.setAnr(Long.valueOf(anr(get(pk, "A1"), get(pk, "A2"), get(pk, "A3"))));

      // dGeld
      Object dGeld = get(pk, "DGeld");

      if (dGeld != null) {
        so.setDGeld(aval(astr(dGeld)));
      }

      // dGba
      Object dGba = get(o, "DGba");

      if (dGba != null) {
        so.setDGba(aval(dGba));
      }

      // vGeld
      Object vGeld = get(pk, "VGeld");

      if (vGeld != null) {
        so.setVGeld(aval(astr(vGeld)));
      }

      // vCat
      Object vCat = get(pk, "V" + name);

      if (name.matches("Aant3")) { // Aant3 altijd als aparte setjes
        vCat = FORCE_NEW_SET;
      }

      if (vCat != null) {
        so.setVCat(aval(astr(vCat)));
      }
    }

    return so;
  }

  private String padl(Object waarde, int i) {
    if (aval(astr(waarde)) >= 0) {
      return pad_left(astr(waarde), "0", i);
    }

    return null;
  } // padl

  private String toGet(String name) {
    return "get" + name;
  }

  private Object get(Object o, String name) {
    try {
      Method subMethod = o.getClass().getMethod(toGet(name));
      return subMethod.invoke(o);
    } catch (Exception e) {
      LOGGER.debug(e.toString());
    }

    return null;
  }
}
