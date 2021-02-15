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

package nl.procura.gba.web.modules.bs.onderzoek.page30.gesprekwindow;

import java.math.BigDecimal;
import java.util.List;

import com.vaadin.data.Item;

import nl.procura.gba.web.components.containers.GbaContainer;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieType;

public class RelationContainer extends GbaContainer {

  public static final String OMSCHRIJVING = "Omschrijving";
  private List<Relatie>      relaties;

  public RelationContainer(List<Relatie> relaties) {
    this.relaties = relaties;
    addContainerProperty(OMSCHRIJVING, String.class, null);
    relaties.forEach(relatie -> {
      if (RelatieType.AANGEVER != relatie.getRelatieType()) {
        Item item = addItem(relatie);
        String naam = relatie.getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
        item.getItemProperty(OMSCHRIJVING).setValue(String.format("%s (%s)", naam,
            relatie.getRelatieType().getOms().toLowerCase()));
      }
    });
  }

  public Relatie getByBsn(BigDecimal bsn) {
    return relaties.stream()
        .filter(p -> p.getPl().getPersoon().getBsn().toLong() == bsn.longValue())
        .findFirst()
        .orElse(null);
  }
}
