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

package nl.procura.gba.web.common.misc.charts;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Embedded;

import nl.procura.gba.config.GbaConfig;
import nl.procura.vaadin.theme.ProcuraApplication;

public class Chart extends Embedded {

  private final String        id;
  private final StringBuilder sb = new StringBuilder();

  public Chart(String id) {

    this.id = id;

    String url = "/" + String.format(GbaConfig.getPath().getApplicationName() + "/chart?key=%s", id);
    url = url.replaceAll("'", "\\\\'"); // Fix voor quotes (') in de omschrijving.
    setSource(new ExternalResource(url));
    setType(Embedded.TYPE_BROWSER);
  }

  public void add(String line) {
    sb.append(line + "\n");
  }

  @Override
  public void attach() {
    ((ProcuraApplication) getApplication()).getSession().setAttribute(id, sb.toString());
    super.attach();
  }
}
