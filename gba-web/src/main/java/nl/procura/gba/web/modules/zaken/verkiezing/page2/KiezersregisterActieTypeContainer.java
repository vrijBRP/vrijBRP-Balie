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

package nl.procura.gba.web.modules.zaken.verkiezing.page2;

import static nl.procura.gba.web.services.beheer.verkiezing.KiezersregisterActieType.*;
import static nl.procura.gba.web.services.beheer.verkiezing.StempasAanduidingType.AAND_GEEN;

import com.vaadin.data.util.IndexedContainer;

import nl.procura.gba.web.services.beheer.verkiezing.Stempas;

public class KiezersregisterActieTypeContainer extends IndexedContainer {

  public static final String OMSCHRIJVING = "omschrijving";

  public KiezersregisterActieTypeContainer(Stempas stempas) {
    addContainerProperty(OMSCHRIJVING, String.class, "");
    if (stempas != null) {
      if (stempas.isOpgeslagen()) {
        if (AAND_GEEN == stempas.getAanduidingType()) {
          if (stempas.getStem().getKiesrVerk().isIndKiezerspas()) {
            addItem(ACT_KIEZERSPAS);
          }
          if (stempas.getStem().getKiesrVerk().isIndBriefstembewijs()) {
            addItem(ACT_BRIEFSTEMMEN);
          }
          addItem(ACT_VERVANGEN);
          addItem(ACT_MACHTIGEN);
          addItem(ACT_ONGELDIG_OVERL);
          addItem(ACT_ONGELDIG_KIESG);
          addItem(ACT_INTREKKEN_VERLIES);
          addItem(ACT_INTREKKEN_OVERIG);
        } else {
          addItem(ACT_VERWIJDEREN);
        }
      } else {
        addItem(ACT_TOEVOEGEN);
      }
    }
  }
}
