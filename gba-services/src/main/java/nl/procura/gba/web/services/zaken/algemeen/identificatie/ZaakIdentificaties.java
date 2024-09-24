/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.services.zaken.algemeen.identificatie;

import static nl.procura.standard.Globalfunctions.pos;

import java.util.ArrayList;
import java.util.List;

public class ZaakIdentificaties {

  private List<ZaakIdentificatie> nummers = new ArrayList<>();

  public boolean exists() {
    return pos(size());
  }

  public String getNummer(String idType) {
    for (ZaakIdentificatie nummer : nummers) {
      if (nummer.getType().equalsIgnoreCase(idType)) {
        return nummer.getExternId();
      }
    }
    return "";
  }

  public List<ZaakIdentificatie> getNummers() {
    return nummers;
  }

  public void setNummers(List<ZaakIdentificatie> nummers) {
    this.nummers = nummers;
  }

  public int size() {
    return nummers.size();
  }
}
