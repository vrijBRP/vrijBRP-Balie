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

package nl.procura.gba.web.services.bs.algemeen.akte;

import static nl.procura.standard.Globalfunctions.*;

import nl.procura.standard.ProcuraDate;

public class DossierAkteNummer {

  private DossierAkteDeel deel;
  private long            volgNummer;
  private long            datum;

  public DossierAkteNummer(long datum, DossierAkteDeel deel, long volgNummer) {
    this.datum = datum;
    this.deel = deel;
    this.volgNummer = volgNummer;
  }

  public String getAkte() {
    return getSoort().getCode() + getDeel().getRegisterdeel() + pad_left(astr(volgNummer), "0", 4);
  }

  public long getDatum() {
    return datum;
  }

  public DossierAkteDeel getDeel() {
    return deel;
  }

  public long getJaar() {
    return along(new ProcuraDate(astr(getDatum())).getYear());
  }

  public DossierAkteRegistersoort getSoort() {
    return deel.getRegisterSoort();
  }

  public long getVolgNummer() {
    return volgNummer;
  }

  @Override
  public String toString() {
    return getAkte();
  }
}
