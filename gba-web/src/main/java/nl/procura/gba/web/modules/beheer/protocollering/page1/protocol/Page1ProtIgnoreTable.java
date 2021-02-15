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

package nl.procura.gba.web.modules.beheer.protocollering.page1.protocol;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.PROT_IGNORE_LOGIN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.PROT_IGNORE_SEARCH;
import static nl.procura.gba.web.services.interfaces.GeldigheidStatus.ACTUEEL;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.join;

import java.util.*;
import java.util.stream.Collectors;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.gba.web.services.beheer.parameter.ParameterType;

public class Page1ProtIgnoreTable extends GbaTable {

  @Override
  public void setColumns() {

    setSizeFull();
    setSelectable(false);

    addColumn("Gebruiker");
    addColumn("Uitgezonderd", 120);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    Map<Gebruiker, Set<ParameterType>> map = new HashMap<>();
    ParameterService parameterService = getApplication().getServices().getParameterService();
    List<Gebruiker> searchUsers = parameterService.getParameterGebruikers(PROT_IGNORE_SEARCH, ACTUEEL);
    searchUsers.forEach(u -> map.put(u, add(map, u, PROT_IGNORE_SEARCH)));

    List<Gebruiker> loginUsers = parameterService.getParameterGebruikers(PROT_IGNORE_LOGIN, ACTUEEL);
    loginUsers.forEach(u -> map.put(u, add(map, u, PROT_IGNORE_LOGIN)));

    for (Map.Entry<Gebruiker, Set<ParameterType>> entry : map.entrySet()) {
      Record record = addRecord(entry.getKey());
      record.addValue(entry.getKey().getUsrfullname());
      record.addValue(capitalize(join(entry.getValue().stream()
          .map(p -> getLabel(p))
          .collect(Collectors.toList()).toArray(), " én ")
              .toLowerCase()));
    }
    super.setRecords();
  }

  private Set<ParameterType> add(Map<Gebruiker, Set<ParameterType>> map, Gebruiker u, ParameterType parameter) {
    if (!map.containsKey(u)) {
      map.put(u, new HashSet<>());
    }
    map.get(u).add(parameter);
    return map.get(u);
  }

  private String getLabel(ParameterType parm) {
    if (ParameterConstant.PROT_IGNORE_LOGIN == parm) {
      return "Inloggen";
    } else
      return "Zoeken";
  }
}
