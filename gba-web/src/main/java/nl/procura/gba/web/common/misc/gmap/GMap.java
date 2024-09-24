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

package nl.procura.gba.web.common.misc.gmap;

import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.CONFIG;
import static nl.procura.commons.core.exceptions.ProExceptionType.ENTRY;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Embedded;

import nl.procura.gba.config.GbaConfig;
import nl.procura.commons.core.exceptions.ProException;

public class GMap extends Embedded {

  private String key     = "";
  private String html    = "";
  private String address = "";

  public GMap(String key, String html, String address) {

    if (emp(key)) {
      throw new ProException(CONFIG, WARNING,
          "<b>Deze functionaliteit werkt alleen met een geldige 'Google maps API sleutel' van Google.</b><hr/> Ga naar <br/><a href='http://www.google.com/apis/maps' target='_blank'>http://www.google.com/apis/maps</a><br/> en maak een sleutel aan.<hr/> Voer deze sleutel vervolgens in bij de parameters.");
    }

    if (emp(address)) {
      throw new ProException(ENTRY, INFO, "Er is geen adres aangegeven.");
    }

    setKey(key);
    setHtml(html);
    setAddress(address);
    String url = "/" + String.format(
        GbaConfig.getPath().getApplicationName() + "/gmap?key=%s&content=%s&address=%s", key, html, address);
    url = url.replaceAll("'", "\\\\'"); // Fix voor quotes (') in de omschrijving.
    setSource(new ExternalResource(url));
    setType(Embedded.TYPE_BROWSER);
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }
}
