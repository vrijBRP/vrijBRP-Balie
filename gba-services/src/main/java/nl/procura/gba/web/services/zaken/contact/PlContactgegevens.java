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

package nl.procura.gba.web.services.zaken.contact;

import static nl.procura.gba.web.services.zaken.contact.ContactgegevensService.*;

import java.util.ArrayList;
import java.util.List;

public class PlContactgegevens {

  private List<PlContactgegevenPersoon> personen = new ArrayList<>();

  public List<PlContactgegevenPersoon> getPersonen() {
    return personen;
  }

  public void setPersonen(List<PlContactgegevenPersoon> personen) {
    this.personen = personen;
  }

  public List<PlContactgegevenType> getTypes() {

    List<PlContactgegevenType> types = new ArrayList<>();

    types.addAll(getTypes(get(EMAIL)));
    types.addAll(getTypes(get(TEL_MOBIEL)));
    types.addAll(getTypes(get(TEL_THUIS)));
    types.addAll(getTypes(get(TEL_WERK)));

    return types;
  }

  private List<PlContactgegeven> get(String gegeven) {

    List<PlContactgegeven> list = new ArrayList<>();
    for (PlContactgegevenPersoon p : getPersonen()) {
      for (PlContactgegeven g : p.getGegevens()) {
        if (g.getContactgegeven().getGegeven().equals(gegeven)) {
          list.add(g);
        }
      }
    }

    return list;
  }

  private List<PlContactgegevenType> getTypes(List<PlContactgegeven> gegevens) {

    List<PlContactgegevenType> types = new ArrayList<>();
    if (gegevens.size() > 0) {
      types.add(new PlContactgegevenType(gegevens.get(0).getContactgegeven(), gegevens));
    }

    return types;
  }
}
