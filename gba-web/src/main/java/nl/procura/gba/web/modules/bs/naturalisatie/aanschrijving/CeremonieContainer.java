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

package nl.procura.gba.web.modules.bs.naturalisatie.aanschrijving;

import static java.util.Optional.of;

import java.math.BigDecimal;
import java.util.Date;

import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatieVerzoeker;
import nl.procura.gba.web.services.bs.naturalisatie.document.Ceremonie;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class CeremonieContainer extends ArrayListContainer {

  public CeremonieContainer(DossierNaturalisatieVerzoeker gegevens) {
    add(1, gegevens.getCeremonie1DIn(),
        gegevens.getCeremonie1TIn(),
        gegevens.getCeremonie1Bijgewoond());

    add(2, gegevens.getCeremonie2DIn(),
        gegevens.getCeremonie2TIn(),
        gegevens.getCeremonie2Bijgewoond());

    add(3, gegevens.getCeremonie3DIn(),
        gegevens.getCeremonie3TIn(),
        gegevens.getCeremonie3Bijgewoond());

    // Fills field with empty value
    if (getItemIds().isEmpty()) {
      addItem(new Ceremonie());
    }
  }

  private void add(Integer ceremonie, Date date, BigDecimal time, Boolean bijgewoond) {
    of(new Ceremonie(ceremonie, date, time, bijgewoond))
        .filter(Ceremonie::isCorrect)
        .ifPresent(this::addItem);
  }
}
