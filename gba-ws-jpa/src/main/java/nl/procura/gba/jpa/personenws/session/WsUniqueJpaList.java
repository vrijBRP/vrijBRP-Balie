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

package nl.procura.gba.jpa.personenws.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.persistence.indirection.IndirectList;

public class WsUniqueJpaList extends IndirectList {

  private static final long serialVersionUID = -8734414320479937088L;

  @Override
  public boolean addAll(Collection c) {
    List list = new ArrayList();
    for (Object t : c) {
      if (!contains(t)) {
        list.add(t);
      }
    }

    return super.addAll(list);
  }

  @Override
  public boolean add(Object element) {
    if (!contains(element)) {
      return super.add(element);
    }

    return false;
  }

  @Override
  public void add(int index, Object element) {
    if (!contains(element)) {
      super.add(index, element);
    }
  }
}
