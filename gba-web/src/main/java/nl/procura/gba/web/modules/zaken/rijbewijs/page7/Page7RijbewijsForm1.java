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

package nl.procura.gba.web.modules.zaken.rijbewijs.page7;

import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.Field;

import nl.procura.gba.web.modules.zaken.rijbewijs.RdwReadOnlyForm;
import nl.procura.gba.web.services.zaken.rijbewijs.*;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class Page7RijbewijsForm1 extends RdwReadOnlyForm {

  Page7RijbewijsForm1(RijbewijsAanvraagAntwoord a) {

    initForm();

    setColumnWidths(WIDTH_130, "350px", "130px", "");

    Page7RijbewijsBean1 b = new Page7RijbewijsBean1();

    RijbewijsAanvraagAntwoord.Stat_gegevens st = a.getStat_gegevens();

    b.setStatus(st.getStatus());
    b.setDatumTijdStatus(st.getDatum() + " / " + st.getTijd());
    b.setGemRef("Niet van toepassing");
    b.setRdwNr(st.getRBW_nr());

    RijbewijsAanvraagAntwoord.Rijb_gegevens r = a.getRijb_gegevens();

    b.setRijbewijsnummer(r.getRijb_nr());
    b.setDend(r.getDatum_einde_geldig());
    b.setAfgifte(r.getDatum_afgifte_geldig());
    b.setVerliesDiefstal(
        fil(r.getDatum_verlies_diefstal()) ? r.getDatum_verlies_diefstal() : "Niet van toepassing");

    RijbewijsAanvraagAntwoord.Pers_gegevens p = a.getPers_gegevens();

    b.setCrbSleutel(p.getCrbsleutel());
    b.setBsn(p.getSnr());
    b.setAnr(p.getAnr());
    b.setNaam(p.getFormats().getNaam().getGeslachtsnaam());
    b.setVoornamen(p.getFormats().getNaam().getPred_voornamen());
    b.setVoorvoegsel(p.getVoorvoegsel());
    b.setGeboren(p.getGeboren());
    b.setNaamgebruik(NaamgebruikType.getByRdwCode(p.getNaamgebruik()).toString());
    b.setGeslacht(p.getGeslacht());
    b.setBurgStaat(RijbewijsBurgStaat.get(p.getBurgstaat()).getOms());
    b.setPartner(p.getPartner());

    RijbewijsAanvraagAntwoord.Adr_gegevens ad = a.getAdr_gegevens();

    b.setAdres(ad.getAdres());
    b.setPcWoonplaats(ad.getPostcode() + " " + ad.getWoonplaats() + " " + ad.getLand());

    RijbewijsAanvraagAntwoord.Aanvr_gegevens aa = a.getAanvr_gegevens();

    b.setAanvraagnr(aa.getAanvraag_nr());
    b.setDatumTijdAanvraag(aa.getDatum() + " / " + aa.getTijd());
    b.setSoort(RijbewijsAanvraagSoort.get(aa.getSoort()).toString());
    b.setReden(RijbewijsAanvraagReden.get(aa.getReden()).toString());
    b.setSpoed(aa.getSpoed());
    b.setBestendig(aa.getBestendig());
    b.setVervangt(aa.getVervangt_rijb());
    b.setAutoriteit(aa.getAutoriteit());
    b.setLoc(aa.getLoc());
    b.setCollo("Niet van toepassing");

    setBean(b);
  }

  @Override
  public void afterSetColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(Page7RijbewijsBean1.PARTNER)) {
      column.setColspan(3);
    }
    super.afterSetColumn(column, field, property);
  }

  protected void initForm() {
  }
}
