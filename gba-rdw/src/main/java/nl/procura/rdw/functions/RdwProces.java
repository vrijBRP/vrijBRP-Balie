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

package nl.procura.rdw.functions;

public enum RdwProces {

  P13_F1(13, 1, RdwProcesType.IN),
  P13_F8(13, 8, RdwProcesType.OUT),
  P13_F13(13, 13, RdwProcesType.ERROR),
  //
  P1303_F1(1303, 1, RdwProcesType.IN),
  P1303_F2(1303, 2, RdwProcesType.OUT),
  //
  P1651_F1(1651, 1, RdwProcesType.IN),
  P1651_F2(1651, 2, RdwProcesType.IN),
  P1651_F8(1651, 8, RdwProcesType.OUT),
  P1651_F9(1651, 9, RdwProcesType.ERROR),
  //
  P1652_F1(1652, 1, RdwProcesType.IN),
  P1652_F2(1652, 2, RdwProcesType.OUT),
  P1652_F9(1652, 9, RdwProcesType.ERROR),
  //
  P1653_F1(1653, 1, RdwProcesType.IN),
  P1653_F2(1653, 2, RdwProcesType.OUT),
  P1653_F9(1653, 9, RdwProcesType.ERROR),
  //
  P1654_F1(1654, 1, RdwProcesType.IN),
  P1654_F2(1654, 2, RdwProcesType.OUT),
  P1654_F9(1654, 9, RdwProcesType.ERROR),
  //
  P1656_F1(1656, 1, RdwProcesType.IN),
  P1656_F2(1656, 2, RdwProcesType.OUT),
  P1656_F9(1656, 9, RdwProcesType.ERROR),
  //
  P0252_F1(252, 1, RdwProcesType.IN),
  P0252_F2(252, 2, RdwProcesType.IN),
  P0252_F7(252, 7, RdwProcesType.OUT, 2),
  // Voor aanvraag bericht 2 is antwoord bericht 7
  P0252_F8(252, 8, RdwProcesType.OUT, 1),
  // Voor aanvraag bericht 1 is antwoord bericht 8
  P0252_F9(252, 9, RdwProcesType.ERROR),
  //
  P1658_F1(1658, 1, RdwProcesType.IN),
  P1658_F2(1658, 2, RdwProcesType.OUT),
  P1658_F9(1658, 9, RdwProcesType.ERROR),
  //
  P1659_F1(1659, 1, RdwProcesType.IN),
  P1659_F2(1659, 2, RdwProcesType.OUT),
  P1659_F9(1659, 9, RdwProcesType.ERROR),
  //
  P1660_F1(1660, 1, RdwProcesType.IN),
  P1660_F2(1660, 2, RdwProcesType.OUT),
  P1660_F9(1660, 9, RdwProcesType.ERROR),
  //
  P1722_F1(1722, 1, RdwProcesType.IN),
  P1722_F2(1722, 2, RdwProcesType.OUT),
  P1722_F9(1722, 9, RdwProcesType.ERROR),
  //
  P1908_F1(1908, 1, RdwProcesType.IN),
  P1908_F2(1908, 2, RdwProcesType.OUT),
  P1908_F9(1908, 9, RdwProcesType.ERROR),
  //
  P1914_F1(1914, 1, RdwProcesType.IN),
  P1914_F2(1914, 2, RdwProcesType.OUT),
  P1914_F9(1914, 9, RdwProcesType.ERROR),
  //
  P0177_F1(177, 1, RdwProcesType.IN),
  P0177_F8(177, 8, RdwProcesType.OUT),
  P0177_F9(177, 9, RdwProcesType.ERROR),
  //
  P0178_F1(178, 1, RdwProcesType.IN),
  P0178_F8(178, 8, RdwProcesType.OUT),
  P0178_F9(178, 9, RdwProcesType.ERROR),
  //
  P0179_F1(179, 1, RdwProcesType.IN),
  P0179_F8(179, 8, RdwProcesType.OUT),
  P0179_F9(179, 9, RdwProcesType.ERROR),
  //
  P0181_F1(181, 1, RdwProcesType.IN),
  P0181_F8(181, 8, RdwProcesType.OUT),
  P0181_F9(181, 9, RdwProcesType.ERROR),
  //
  P0182_F1(182, 1, RdwProcesType.IN),
  P0182_F8(182, 8, RdwProcesType.OUT),
  P0182_F9(182, 9, RdwProcesType.ERROR),
  //
  P0183_F1(183, 1, RdwProcesType.IN),
  P0183_F8(183, 8, RdwProcesType.OUT),
  P0183_F9(183, 9, RdwProcesType.ERROR),
  //
  P0184_F1(184, 1, RdwProcesType.IN),
  P0184_F2(184, 2, RdwProcesType.IN),
  P0184_F7(184, 7, RdwProcesType.OUT),
  P0184_F8(184, 8, RdwProcesType.OUT),
  P0184_F9(184, 9, RdwProcesType.ERROR);

  public long          p  = 0;
  public long          f  = 0;
  public RdwProcesType t  = RdwProcesType.IN;
  public long          df = 0;

  RdwProces(long p, long f, RdwProcesType t) {
    this(p, f, t, 0);
  }

  RdwProces(long p, long f, RdwProcesType t, long df) {

    this.p = p;
    this.f = f;
    this.t = t;
    this.df = df;
  }

  public static RdwProces get(long procId, long funcId, RdwProcesType type) {
    for (RdwProces proc : RdwProces.values()) {
      if (proc.p == procId && proc.t == type && (proc.df <= 0 || (proc.df == funcId))) {
        return proc;
      }
    }

    return null;
  }

  public static RdwProces get(long procId, long procFunc) {
    for (RdwProces proc : RdwProces.values()) {
      if (proc.p == procId && proc.f == procFunc) {
        return proc;
      }
    }

    return null;
  }

  public static RdwProces get(Proces proces) {
    for (RdwProces proc : RdwProces.values()) {
      if (proc.p == proces.getProces() && proc.f == proces.getFunctie()) {
        return proc;
      }
    }
    return null;
  }
}
