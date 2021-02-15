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

package nl.procura.gba.web.modules.zaken.reisdocument.overzicht;

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.dto.raas.aanvraag.AfsluitingDto;
import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.dto.raas.aanvraag.LeveringDto;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentStatus;

public class ReisdocumentOverzichtLayoutForm4 extends ReadOnlyForm {

  private final ReisdocumentAanvraag zaak;
  private final DocAanvraagDto       raasZaak;

  public ReisdocumentOverzichtLayoutForm4(ReisdocumentAanvraag zaak, DocAanvraagDto raasZaak, String... fields) {
    this.zaak = zaak;
    this.raasZaak = raasZaak;

    setColumnWidths("130px", "");
    setOrder(fields);
    updateBean();
  }

  public void updateBean() {
    ReisdocumentOverzichtBean3 b = new ReisdocumentOverzichtBean3();
    updateProwebStatus(b);
    updateRaasStatus(b);
    setBean(b);
  }

  private void updateProwebStatus(ReisdocumentOverzichtBean3 b) {

    ReisdocumentStatus status = zaak.getReisdocumentStatus();

    String aflevering = status.getStatusLevering().toString();
    String afsluiting = status.getStatusAfsluiting().toString();

    b.setDocument(fil(status.getNrNederlandsDocument()) ? status.getNrNederlandsDocument() : "Onbekend");

    if (pos(status.getDatumTijdLevering().getLongDate())) {
      aflevering += ", gemeld op " + status.getDatumTijdLevering();
    }

    if (pos(status.getDatumTijdAfsluiting().getLongDate())) {
      afsluiting += ", afgesloten op " + status.getDatumTijdAfsluiting();
    }

    b.setAflevering(aflevering);
    b.setAfsluiting(afsluiting);
  }

  private void updateRaasStatus(ReisdocumentOverzichtBean3 b) {

    LeveringDto lev = raasZaak.getLevering();
    AfsluitingDto afsl = raasZaak.getAfsluiting();

    String aflevering = lev.getStatus().getValue().toString();
    String afsluiting = afsl.getStatus().getValue().toString();

    if (pos(lev.getDatum().getValue())) {
      aflevering += ", gemeld op " + new DateTime(lev.getDatum().getValue(), lev.getTijd().getValue());
    }

    if (pos(afsl.getDatum().getValue())) {
      afsluiting += ", afgesloten op " + new DateTime(afsl.getDatum().getValue(), afsl.getTijd().getValue());
    }

    b.setAflevering2(aflevering);
    b.setAfsluiting2(afsluiting);
  }
}
