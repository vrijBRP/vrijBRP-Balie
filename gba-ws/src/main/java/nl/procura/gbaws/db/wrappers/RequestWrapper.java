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

package nl.procura.gbaws.db.wrappers;

import java.math.BigInteger;

import nl.procura.gba.jpa.personenws.db.Request;
import nl.procura.gba.jpa.personenws.db.Usr;

public class RequestWrapper extends AbstractTableWrapper<Request, Integer> {

  public RequestWrapper() {
    this(new Request());
  }

  public RequestWrapper(Request table) {
    super(table);
  }

  @Override
  public Integer getPk() {
    return getTable().getCRequest();
  }

  public String getInhoud() {
    return getTable().getContent();
  }

  public void setInhoud(String inhoud) {
    getTable().setContent(inhoud);
  }

  public int getDatumIngang() {
    return getTable().getDIn().intValue();
  }

  public void setDatumIngang(int d_in) {
    getTable().setDIn(BigInteger.valueOf(d_in));
  }

  public UsrWrapper getGebruiker() {
    return new UsrWrapper(getTable().getCUsr() != null ? getTable().getCUsr() : new Usr());
  }

  public void setGebruiker(UsrWrapper gebruiker) {
    getTable().setCUsr(gebruiker.getTable());
  }

  public int getTijdIngang() {
    return getTable().getTIn().intValue();
  }

  public void setTijdIngang(int t_in) {
    getTable().setTIn(BigInteger.valueOf(t_in));
  }
}
