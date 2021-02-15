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
import java.util.Objects;

import com.vaadin.data.Item;

import nl.procura.gba.web.components.containers.GbaContainer;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;

public class BetrokkeneContainer extends GbaContainer {

  public static final String   OMSCHRIJVING = "Omschrijving";
  private List<DossierPersoon> personen;

  public BetrokkeneContainer(List<DossierPersoon> personen) {
    this.personen = personen;
    addContainerProperty(OMSCHRIJVING, String.class, null);
    personen.forEach(persoon -> {
      Item item = addItem(persoon);
      item.getItemProperty(OMSCHRIJVING).setValue(persoon.getNaam().getNaam_naamgebruik_eerste_voornaam());
    });
  }

  public DossierPersoon getByBsn(BigDecimal bsn) {
    return personen.stream()
        .filter(p -> bsn != null && Objects.equals(p.getBurgerServiceNummer().getLongValue(), bsn.longValue()))
        .findFirst()
        .orElse(null);
  }

  public DossierPersoon getFirst() {
    return personen.stream().findFirst().orElse(null);
  }
}
