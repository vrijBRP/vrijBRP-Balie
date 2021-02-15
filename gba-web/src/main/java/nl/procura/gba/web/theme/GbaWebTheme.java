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

package nl.procura.gba.web.theme;

import nl.procura.vaadin.theme.twee.ProcuraTheme;

public class GbaWebTheme extends ProcuraTheme {

  public interface TABSHEET {

    String MARGIN_TOP = "tabsheet-margin-top";
  }

  public interface TEXTBOX {

    String RED   = "redbox";
    String GREEN = "greenbox";
  }

  public interface TEXT_FIELD {

    String UPPERCASE = "uppercase";
  }

  public interface ICOON_24 {

    String OK = "../gba-web/icons/24/ok.png";
  }
}
