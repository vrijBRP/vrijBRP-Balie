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

package nl.procura.gba.web.modules.beheer.onderhoud.page1.tab2;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.date2str;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.ssl.web.rest.v1_0.certificates.SslRestCertificate;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class Tab2CertificatenWindow extends GbaModalWindow {

  public Tab2CertificatenWindow(SslRestCertificate certificate) {
    setCaption("Certificaat");
    setWidth("800px");

    TableLayout layout = new TableLayout();
    layout.setColumnWidths("90px", "");
    layout.addLabel("Omschrijving");
    layout.addData(new Label(certificate.getDescription()));

    layout.addLabel("Soort");
    layout.addData(new Label(certificate.isPrivateKey() ? "Sleutel" : "Vertrouwd certificaat"));

    layout.addLabel("Dagen geldig");
    layout.addData(new Label(String.valueOf(certificate.getDaysValid()), Label.CONTENT_XHTML));

    layout.addLabel("Verloopdatum");
    layout.addData(new Label(date2str(certificate.getEndDate()), Label.CONTENT_XHTML));

    layout.addLabel("Status");
    layout.addData(new Label(getStatus(certificate), Label.CONTENT_XHTML));

    layout.addLabel("Onderwerp");
    layout.addData(new Label(certificate.getSubject()));

    layout.addLabel("Uitgever");
    layout.addData(new Label(certificate.getIssuer()));

    Button button = new Button("Sluiten", (Button.ClickListener) clickEvent -> closeWindow());
    button.focus();
    setContent(new VLayout()
        .margin(true)
        .add(button)
        .add(layout));
  }

  public static String getStatus(SslRestCertificate cert) {
    if (cert.getDaysValid() < 0) {
      return setClass(false, "Certificaat is verlopen");
    }
    return setClass(true, "Certificaat is nog geldig");
  }
}
