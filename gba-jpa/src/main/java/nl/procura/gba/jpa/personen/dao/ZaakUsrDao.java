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

package nl.procura.gba.jpa.personen.dao;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.jpa.personen.db.ZaakUsr;

public class ZaakUsrDao extends GenericDao {

  public static final String ZAAK_USR = "zaakUsr";

  public static final List<ZaakUsr> find(ConditionalMap map) {

    if (map.isEmpty()) {
      return new ArrayList<>();
    }

    ZaakUsr usr = new ZaakUsr();

    if (map.containsKey(ZAAK_ID)) {
      usr.setZaakId(astr(map.get(ZAAK_ID)));
    }

    if (map.containsKey(ZAAK_USR)) {
      usr.setUsr(find(Usr.class, Long.parseLong(astr(map.get(ZAAK_USR)))));
    }

    List<ZaakUsr> list = findByExample(usr);
    Collections.sort(list, (o1, o2) -> o2.getId().compareTo(o1.getId()));
    return list;
  }
}
