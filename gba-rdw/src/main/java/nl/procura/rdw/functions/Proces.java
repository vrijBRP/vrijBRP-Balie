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

import static nl.procura.rdw.functions.RdwMeldingSoort.FOUT;
import static nl.procura.rdw.functions.RdwMeldingSoort.SYSTEEMFOUT;
import static nl.procura.standard.Globalfunctions.pos;

public class Proces {

  private long          proces  = 0;
  private long          functie = 0;
  private ProcesObject  object  = null;
  private ProcesMelding melding = new ProcesMelding();

  public Proces() {
  }

  public Proces(RdwProces pf, ProcesObject o) {
    setObject(o);
    setProces(pf.p);
    setFunctie(pf.f);
  }

  public long getProces() {
    return proces;
  }

  public void setProces(long proces) {
    this.proces = proces;
  }

  public long getFunctie() {
    return functie;
  }

  public void setFunctie(long functie) {
    this.functie = functie;
  }

  public Object getObject() {
    return object;
  }

  public void setObject(ProcesObject object) {
    this.object = object;
  }

  public ProcesMelding getMelding() {
    return melding;
  }

  public boolean isMelding() {
    return pos(getMelding().getNr());
  }

  public void setMelding(ProcesMelding melding) {
    this.melding = melding;
  }

  public boolean isRipMelding() {
    return pos(getMelding().getRipNr());
  }

  public boolean isFoutMelding() {
    return isMelding() && getMelding().getMeldingSoort().is(FOUT, SYSTEEMFOUT);
  }

  @Override
  public String toString() {
    return String.format("Proces [proces=%d, functie=%d, object=%s, melding=%s]", proces, functie, object, melding);
  }
}
