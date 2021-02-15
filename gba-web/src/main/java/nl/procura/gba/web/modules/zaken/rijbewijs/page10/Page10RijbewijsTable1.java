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

package nl.procura.gba.web.modules.zaken.rijbewijs.page10;

import static nl.procura.standard.Globalfunctions.*;

import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.rijbewijs.NaamgebruikType;
import nl.procura.rdw.processen.p1652.f02.ADRESNATPGEG;
import nl.procura.rdw.processen.p1652.f02.NATPERSOONGEG;
import nl.procura.rdw.processen.p1652.f02.NATPOPNAAM;
import nl.procura.rdw.processen.p1652.f02.PERSADRESGEG;
import nl.procura.validation.Bsn;

public class Page10RijbewijsTable1 extends GbaTable {

  private final NATPOPNAAM a;

  public Page10RijbewijsTable1(NATPOPNAAM a) {
    this.a = a;
  }

  @Override
  public void setColumns() {

    setSelectable(true);

    addColumn("Nr", 30);
    addColumn("BSN", 150);
    addColumn("Geboortedatum", 150);
    addColumn("Naam");
    addColumn("Adres");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    int nr = 0;

    for (PERSADRESGEG gegevens : a.getPersadrestab().getPersadresgeg()) {

      nr++;

      NATPERSOONGEG natgeg = gegevens.getNatpersoongeg();
      ADRESNATPGEG adrgeg = gegevens.getAdresnatpgeg();

      String snr = new Bsn(astr(natgeg.getFiscnrnatp())).getFormatBsn();
      String sng = NaamgebruikType.getByRdwCode(along(natgeg.getNaamgebrnatp())).getAfk();

      String gesl = natgeg.getGeslnaamnatp();
      String titel = natgeg.getAdelprednatp();
      String voorv = natgeg.getVoorvoegnatp();
      String voorn = natgeg.getVoornaamnatp();

      Naamformats naam = new Naamformats(voorn, gesl, voorv, titel, sng, null);

      String adres = "";

      if (fil(adrgeg.getLocregelnatp())) {
        adres += adrgeg.getLocregelnatp();
      } else {
        adres += adrgeg.getStraatnatp() + " " + adrgeg.getHuisnrnatp() + " " + adrgeg.getHuistvnatp() + " ";
      }

      adres += ", " + adrgeg.getPostcnnatp() + " " + adrgeg.getWoonplnatp();

      Record r = addRecord(gegevens);

      r.addValue(nr);
      r.addValue(snr);
      r.addValue(date2str(astr(natgeg.getGebdatnatp())));
      r.addValue(naam.getPred_eerstevoorn_adel_voorv_gesl());
      r.addValue(adres);
    }
  }
}
