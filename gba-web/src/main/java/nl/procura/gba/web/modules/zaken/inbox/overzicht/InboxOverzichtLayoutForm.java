/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.inbox.overzicht;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.gemeenteinbox.GemeenteInboxRecord;

public class InboxOverzichtLayoutForm extends ReadOnlyForm<InboxOverzichtLayoutBean> {

  public InboxOverzichtLayoutForm(GemeenteInboxRecord record) {
    setCaption("Verzoek");
    setColumnWidths("140px", "");

    InboxOverzichtLayoutBean b = new InboxOverzichtLayoutBean();
    b.setOmschrijving(record.getOmschrijving());
    b.setZaakIdIntern(record.getZaakId());
    b.setZaakIdExtern(record.getZaakIdExtern());
    b.setBestandsnaam(record.getBestandsnaam());
    b.setSoort(record.getVerwerkingsTypeOmschrijving());

    if (record.isNieuwVerzoekType()) {
      setOrder(InboxOverzichtLayoutBean.SOORT,
          InboxOverzichtLayoutBean.OMSCHRIJVING);

    } else {
      setOrder(InboxOverzichtLayoutBean.SOORT,
          InboxOverzichtLayoutBean.OMSCHRIJVING,
          InboxOverzichtLayoutBean.ZAAK_ID_EXTERN,
          InboxOverzichtLayoutBean.BESTANDSNAAM);
    }
    setBean(b);
  }
}
