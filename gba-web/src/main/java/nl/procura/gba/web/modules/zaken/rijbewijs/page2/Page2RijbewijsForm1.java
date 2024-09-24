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

package nl.procura.gba.web.modules.zaken.rijbewijs.page2;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.gba.web.modules.zaken.rijbewijs.page2.Page2RijbewijsBean1.AANVRAAGNR;
import static nl.procura.gba.web.modules.zaken.rijbewijs.page2.Page2RijbewijsBean1.DAGEN185NL;
import static nl.procura.gba.web.modules.zaken.rijbewijs.page2.Page2RijbewijsBean1.GBABESTENDIG;
import static nl.procura.gba.web.modules.zaken.rijbewijs.page2.Page2RijbewijsBean1.NAAMGEBRUIK;
import static nl.procura.gba.web.modules.zaken.rijbewijs.page2.Page2RijbewijsBean1.PV;
import static nl.procura.gba.web.modules.zaken.rijbewijs.page2.Page2RijbewijsBean1.RASCODE;
import static nl.procura.gba.web.modules.zaken.rijbewijs.page2.Page2RijbewijsBean1.REDENAANVRAAG;
import static nl.procura.gba.web.modules.zaken.rijbewijs.page2.Page2RijbewijsBean1.RIJBEWIJSNR;
import static nl.procura.gba.web.modules.zaken.rijbewijs.page2.Page2RijbewijsBean1.SOORTAANVRAAG;
import static nl.procura.gba.web.modules.zaken.rijbewijs.page2.Page2RijbewijsBean1.SPOED;
import static nl.procura.gba.web.modules.zaken.rijbewijs.page2.Page2RijbewijsBean1.STATUSGBA;
import static nl.procura.gba.web.modules.zaken.rijbewijs.page2.Page2RijbewijsBean1.THUISBEZORGEN;
import static nl.procura.gba.web.modules.zaken.rijbewijs.page2.Page2RijbewijsBean1.VERMELDING_TITEL;
import static nl.procura.gba.web.modules.zaken.rijbewijs.page2.Page2RijbewijsBean1.VERVANGTRIJBEWIJS;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.trim;

import com.vaadin.ui.Button;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.gba.web.modules.zaken.rijbewijs.RdwReadOnlyForm;
import nl.procura.gba.web.modules.zaken.rijbewijs.errorpage.RijbewijsErrorWindow;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.VermeldTitelType;
import nl.procura.gba.web.services.zaken.rijbewijs.NrdServices;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsService;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsStatusType;
import nl.procura.rdw.messages.P1659;
import nl.procura.rdw.processen.p1659.f02.AANVRRYBKRT;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page2RijbewijsForm1 extends RdwReadOnlyForm {

  private final RijbewijsAanvraag aanvraag;

  protected Page2RijbewijsForm1(RijbewijsAanvraag aanvraag) {

    this.aanvraag = aanvraag;
    setReadThrough(true);
    setCaption("Aanvraag rijbewijs");
    setOrder(AANVRAAGNR, RASCODE, SOORTAANVRAAG, REDENAANVRAAG, VERVANGTRIJBEWIJS, SPOED, PV,
        VERMELDING_TITEL, NAAMGEBRUIK, GBABESTENDIG, DAGEN185NL, RIJBEWIJSNR, STATUSGBA, THUISBEZORGEN);
    setColumnWidths(WIDTH_130, "250px", "180px", "");

    Page2RijbewijsBean1 b = new Page2RijbewijsBean1();

    b.setAanvraagnr(aanvraag.getAanvraagNummer());
    b.setDagen185nl(aanvraag.isIndicatie185() ? "Ja" : "Nee");
    b.setGbaBestendig(aanvraag.isGbaBestendig() ? "Ja" : "Nee");
    b.setNaamgebruik(astr(aanvraag.getNaamgebruik()));
    b.setPv(aanvraag.getProcesVerbaalVerlies());
    b.setRasCode(astr(aanvraag.getCodeRas()));
    b.setRedenAanvraag(aanvraag.getRedenAanvraag().toString());
    b.setRijbewijsNr(fil(aanvraag.getRijbewijsnummer()) ? aanvraag.getRijbewijsnummer() : "Nog niet bekend");
    b.setSoortAanvraag(aanvraag.getSoortAanvraag().toString());
    b.setSpoed(aanvraag.isSpoed() ? "Ja" : "Nee");
    b.setVermeldingTitel(VermeldTitelType.get(aanvraag.getVermeldTp().intValue()).getOms());
    b.setVervangtRijbewijs(aanvraag.getVervangingsRbwNr());
    b.setBezorgen(trim((aanvraag.getIndBezorgen() ? "Ja, " : "Nee, ") + aanvraag.getOpmBezorgen()));
    setBean(b);
  }

  @Override
  public Page2RijbewijsBean1 getBean() {
    return (Page2RijbewijsBean1) super.getBean();
  }

  @Override
  public void setColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (property.is(DAGEN185NL, RIJBEWIJSNR, THUISBEZORGEN)) {
      column.setColspan(3);
    }

    if (property.is(RIJBEWIJSNR)) {
      column.addComponent(new ZoekButton());
    }

    super.setColumn(column, field, property);
  }

  public boolean update() {

    String oudNr = aanvraag.getRijbewijsnummer();

    P1659 p1659 = new P1659();
    p1659.newF1(along(aanvraag.getAanvraagNummer()));
    RijbewijsService db = getApplication().getServices().getRijbewijsService();

    if (NrdServices.sendMessage(db, p1659)) {
      AANVRRYBKRT antwoord = ((AANVRRYBKRT) p1659.getResponse().getObject());
      String statusCode = astr(antwoord.getStatrybkgeg().getStatcoderybk());
      String nieuwNr = astr(antwoord.getStatrybkgeg().getRybstatrybk());

      if (pos(nieuwNr)) {
        setRijbewijsNummer(nieuwNr);
        aanvraag.setRijbewijsnummer(nieuwNr);
        db.save(aanvraag);

        if (!nieuwNr.equals(oudNr)) {
          return true;
        }

        throw new ProException(INFO, "Het rijbewijsnummer is nog steeds {0}", nieuwNr);
      }

      String msg = "Geen rijbewijsnummer gevonden. De huidige status is nog <b>{0}</b>";
      throw new ProException(INFO, msg, RijbewijsStatusType.get(along(statusCode)));
    }

    getWindow().addWindow(new RijbewijsErrorWindow(p1659.getResponse().getMelding()));
    return false;
  }

  public void zoekRijbewijsNummer() {
    // Overriden please
  }

  private void setRijbewijsNummer(String nr) {

    getBean().setRijbewijsNr(nr);
    setBean(getBean());
  }

  public class ZoekButton extends Button {

    ZoekButton() {
      super("Zoek bij RDW");
      addListener((ClickListener) event -> zoekRijbewijsNummer());
    }
  }
}
