/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.services.zaken.algemeen.attribuut;

import static nl.procura.standard.Globalfunctions.along;

import java.util.Optional;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.jpa.personen.db.ZaakUsr;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.standard.ProcuraDate;

public class ZaakBehandelaar extends ZaakUsr {

  public ZaakBehandelaar() {
    ProcuraDate pd = new ProcuraDate();
    setDIn(along(pd.getSystemDate()));
    setTIn(along(pd.getSystemTime()));
  }

  public DateTime getDatumTijdInvoer() {
    return new DateTime(getDIn(), getTIn());
  }

  public UsrFieldValue getGebruiker() {
    return Optional.ofNullable(getUsr())
        .map(u -> new UsrFieldValue(u.getCUsr(), u.getUsrfullname()))
        .orElse(new UsrFieldValue());
  }

  public void setBehandelaar(UsrFieldValue usr) {
    setUsrToek(new Usr(along(usr.getValue()), usr.getDescription()));
  }

  public UsrFieldValue getBehandelaar() {
    return Optional.ofNullable(getUsrToek())
        .map(u -> new UsrFieldValue(u.getCUsr(), u.getUsrfullname()))
        .orElse(new UsrFieldValue());
  }
}
