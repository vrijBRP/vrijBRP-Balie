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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.zkndms.page1;

import static nl.procura.bsm.rest.v1_0.objecten.stuf.zkn0310.zsdms.BsmZknDmsRestElementTypes.*;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;

import nl.procura.bsm.rest.v1_0.objecten.algemeen.BsmRestElement;
import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.validation.Bsn;

public class Page1ZknDmsBetrokkeneTable extends GbaTable {

  private List<BsmRestElement> betrokkenen = new ArrayList<>();

  public Page1ZknDmsBetrokkeneTable() {
  }

  public void setBetrokkenen(List<BsmRestElement> statussen) {
    this.betrokkenen = statussen;
  }

  @Override
  public void setColumns() {

    setClickable(true);

    addColumn("Omschrijving", 300);
    addColumn("Identificatie", 300);
    addColumn("Betreft");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    for (BsmRestElement betrokkene : betrokkenen) {

      if (betrokkene.isAdded(PERSOON)) {

        BsmRestElement persoon = betrokkene.get(PERSOON);
        Bsn bsn = new Bsn(persoon.getElementWaarde(BSN));
        String anp = persoon.getElementWaarde(ANP);
        String voorletters = persoon.getElementWaarde(VOORLETTERS);
        String voornamen = persoon.getElementWaarde(VOORNAMEN);
        String geslachtsnaam = persoon.getElementWaarde(GESLACHTSNAAM);
        String voorv = persoon.getElementWaarde(VOORVOEGSEL);

        String omschrijving = betrokkene.getElementWaarde(OMSCHRIJVING_BETROKKENHEID);
        Naamformats naam = new Naamformats(fil(voornamen) ? voornamen : voorletters, geslachtsnaam, voorv, "",
            "", null);

        String id = "";
        if (bsn.isCorrect()) {
          id = bsn.getFormatBsn() + " (burgerservicenummer)";
        } else if (fil(anp)) {
          id = anp + " (ANP identificatie)";
        }

        Record record = addRecord(betrokkene);
        record.addValue(omschrijving);
        record.addValue(id);
        record.addValue(naam.getNaam_naamgebruik_eerste_voornaam());
      } else if (betrokkene.isAdded(ADRES)) {

        BsmRestElement adres = betrokkene.get(ADRES);

        Adresformats format = new Adresformats();
        String straat1 = adres.getElementWaarde(STRAATNAAM);
        String straat2 = adres.getElementWaarde(OPENBARE_RUIMTE_NAAM);
        String hnr = adres.getElementWaarde(HUISNUMMER);
        String hnrL = adres.getElementWaarde(HUISLETTER);
        String hnrT = adres.getElementWaarde(HUISNUMMER_TOEVOEGING);
        String locatie = adres.getElementWaarde(LOCATIE);
        String pc = adres.getElementWaarde(POSTCODE);
        String wpl = adres.getElementWaarde(WOONPLAATSNAAM);
        format.setValues(fil(straat1) ? straat1 : straat2, hnr, hnrL, hnrT, "", locatie, pc, "", wpl, "", "",
            "", "", "", "", "");

        Record record = addRecord(betrokkene);
        record.addValue("Adres");
        record.addValue(adres.getElementWaarde(IDENTIFICATIE));
        record.addValue(format.getAdres_pc_wpl());
      }
    }

    super.setRecords();
  }
}
