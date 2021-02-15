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

package nl.procura.gba.web.services.beheer.parameter;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.gba.jpa.personen.db.Parm;

public class Parameter extends Parm {

  @Override
  public String getValue() {
    return astr(super.getValue());
  }

  public boolean isNiveauGebruiker() {
    return getUsr() != null && pos(getUsr().getCUsr());
  }

  public boolean isNiveauProfiel() {
    return getProfile() != null && pos(getProfile().getCProfile());
  }

  @Override
  public String toString() {
    return "ParameterImpl [getCParm()=" + getCParm() + ", getParm()=" + getParm() + ", getValue()=" + getValue() + "]";
  }
}
