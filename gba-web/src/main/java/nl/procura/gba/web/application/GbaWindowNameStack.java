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

package nl.procura.gba.web.application;

import java.util.ArrayList;

import com.vaadin.ui.Window;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;

public class GbaWindowNameStack extends ArrayList<String> {

  /**
   * Geeft vorige pagina terug
   */
  public String getPrevious() {

    if (size() > 1) {
      return get(size() - 2);
    }

    throw new ProException(ProExceptionSeverity.INFO, "Er is geen vorige scherm om naar terug te gaan.");
  }

  public void put(Window window) {

    if (window != null) {
      remove(window.getName());
      add(window.getName());
    }
  }

  public void remove(Window window) {

    if (window != null) {
      remove(window.getName());
    }
  }

  /**
   * Verwijderd laatste pagina
   */
  public void removePrevious() {

    if (size() > 1) {
      remove(size() - 1);
    }
  }
}
