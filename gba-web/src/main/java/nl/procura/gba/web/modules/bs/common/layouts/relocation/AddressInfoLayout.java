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

package nl.procura.gba.web.modules.bs.common.layouts.relocation;

import static java.text.MessageFormat.format;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisAdres;
import nl.procura.gba.web.services.interfaces.address.AddressSourceType;
import nl.procura.vaadin.component.layout.info.InfoLayout;

/**
 * Shows information on the address
 */
public class AddressInfoLayout extends InfoLayout {

  public AddressInfoLayout removeAddress() {
    setMessage("");
    return this;
  }

  public AddressInfoLayout set(VerhuisAdres adres, String extraMessage) {
    StringBuilder message = new StringBuilder();
    if (adres.getAddress().getSourceType().is(AddressSourceType.BAG)) {
      message.append(format("<b>Let op.</b> {0} komt niet voor in de gemeentelijke BRP.",
          adres.getAddressLabel()));
    } else {
      int count = adres.getCurrentPersons().size();
      switch (count) {
        case 0:
          message
              .append(format("<b>Let op.</b> Op {0} wonen momenteel <b>geen</b> bewoners.", adres.getAddressLabel()));
          break;

        case 1:
          message.append(format("<b>Let op.</b> Op {0} woont momenteel <b>1</b> bewoner.", adres.getAddressLabel()));
          break;

        default:
          message.append(
              format("<b>Let op.</b> Op {0} wonen momenteel <b>{1}</b> bewoners.", adres.getAddressLabel(), count));
          break;
      }

      int indicationCode = adres.getAddress().getAddressIndicationCode();
      String indicationName = adres.getAddress().getAddressIndicationName();

      if (count > 0) {
        message.append(extraMessage);
      }

      if (pos(indicationCode)) {
        if (fil(message.toString())) {
          message.append("<hr/>");
        }

        message.append(MessageFormat.format("{0}</b> ({1})", setClass(false, "Dit adres heeft adresindicatie <b>i" +
            indicationCode), indicationName));
      }
    }

    if (!adres.getAddress().isSuitableForLiving()) {
      message.append("<hr/>");
      message.append(setClass(false, "Dit adres is niet geschikt voor bewoning."));
      String purpose = adres.getAddress().getPurpose();
      if (StringUtils.isNotBlank(purpose)) {
        message.append(setClass(false, " Het heeft <b>" + purpose + "</b> als gebruiksdoel."));
      }
    }
    setMessage(message.toString());
    return this;
  }
}
