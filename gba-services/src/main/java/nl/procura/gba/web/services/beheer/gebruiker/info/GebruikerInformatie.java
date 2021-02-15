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

package nl.procura.gba.web.services.beheer.gebruiker.info;

import static nl.procura.standard.Globalfunctions.eq;

import java.util.ArrayList;
import java.util.List;

public class GebruikerInformatie {

  private final List<GebruikerInfo> all = new ArrayList<>();

  public List<GebruikerInfo> getAlles() {
    return all;
  }

  public GebruikerInfo getInfo(GebruikerInfoType type) {
    for (GebruikerInfo i : getAlles()) {
      if (eq(i.getInfo(), type.getKey())) {
        return i;
      }
    }

    return null;
  }

  @Override
  public String toString() {
    return "GebruikerInformatieImpl [all=" + all + "]";
  }
}
