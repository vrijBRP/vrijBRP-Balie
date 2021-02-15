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

package nl.procura.gba.web.common.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import nl.procura.gba.web.services.zaken.algemeen.Zaak;

/**
 * Zaken toevoegen op basis van hun ZaakId's.
 */
public class ZakenList<T extends Zaak> extends ArrayList<T> {

  private final HashSet<String> zaakIds = new HashSet<>();

  public ZakenList() {
  }

  public ZakenList(List<T> zaken) {
    addAll(zaken);
  }

  @Override
  public void add(int index, T element) {
    super.add(index, element);
  }

  @Override
  public boolean add(T zaak) {

    if (!zaakIds.contains(zaak.getZaakId())) {
      zaakIds.add(zaak.getZaakId());
      return super.add(zaak);
    }

    return false;
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {

    for (T zaak : c) {
      add(zaak);
    }

    return true;
  }
}
