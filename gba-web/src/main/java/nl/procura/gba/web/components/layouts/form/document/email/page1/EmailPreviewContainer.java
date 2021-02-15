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

package nl.procura.gba.web.components.layouts.form.document.email.page1;

import nl.procura.gba.web.common.misc.email.EmailAddressList;
import nl.procura.gba.web.common.misc.email.EmailAddressType;

import lombok.Data;

@Data
public class EmailPreviewContainer {

  private final EmailAddressList adressen     = new EmailAddressList();
  private Page1EmailAdresTable   table        = null;
  private EmailAddressList       zaakAdressen = new EmailAddressList();

  public EmailPreviewContainer() {
    adressen.add(EmailAddressType.FROM);
    adressen.add(EmailAddressType.TO);
    adressen.add(EmailAddressType.REPLY_TO);
    adressen.add(EmailAddressType.CC);
    adressen.add(EmailAddressType.BCC);
  }
}
