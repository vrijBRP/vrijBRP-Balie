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

package nl.procura.gba.web.modules.hoofdmenu.raas.tab2.page2;

import static java.lang.String.format;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import nl.procura.dto.raas.bestand.RaasBestandDto;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.hoofdmenu.raas.tab2.page1.Tab2RaasPage1Layout;
import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;

public abstract class Tab2RaasPage2Form extends GbaForm<Tab2RaasPage2Bean> {

  Tab2RaasPage2Form(RaasBestandDto raasBestand) {
    setColumnWidths("100px", "300px", "100px", "", "100px", "");
    setReadonlyAsText(true);
    createFields();
    initFields(raasBestand);
  }

  protected abstract void createFields();

  protected void initFields(RaasBestandDto rb) {
    Tab2RaasPage2Bean bean = new Tab2RaasPage2Bean();
    bean.setCode(set(rb.getId()));
    bean.setAanvraagNummer(set(new Aanvraagnummer(rb.getAanvraagNr().getValue().toString()).getFormatNummer()));
    bean.setDatumTijd(getDatumTijd(rb.getTimestamp().getValue()));
    bean.setNaam(rb.getNaam());
    bean.setStatus(Tab2RaasPage1Layout.getStatus(rb.getStatus().getValue()));
    bean.setRichting(rb.getType().getValue().isToRaas() ? "Uitgaand bericht" : "Inkomend bericht");
    bean.setType(format("%s: %s", rb.getType().getValue().getCode(), rb.getType()));

    setBean(bean);
    repaint();
  }

  public Object set(Object obj) {
    if (obj instanceof Integer) {
      Integer i = (Integer) obj;
      if (i < 0) {
        return "";
      }
    }
    return obj;
  }

  private String getDatumTijd(Date date) {
    return DateFormatUtils.format(date, "dd-MM-yyyy hh:mm");
  }
}
