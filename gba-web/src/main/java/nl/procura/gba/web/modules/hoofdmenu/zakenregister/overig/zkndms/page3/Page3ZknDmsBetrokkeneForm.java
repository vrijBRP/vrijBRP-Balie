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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.zkndms.page3;

import static nl.procura.bsm.rest.v1_0.objecten.stuf.zkn0310.zsdms.BsmZknDmsRestElementTypes.*;
import static nl.procura.standard.Globalfunctions.date2str;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nl.procura.bsm.rest.v1_0.objecten.algemeen.BsmRestElement;
import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.validation.Bsn;

public class Page3ZknDmsBetrokkeneForm extends GbaForm<Page3ZknDmsBetrokkeneBean> {

  public Page3ZknDmsBetrokkeneForm(BsmRestElement betrokkene) {

    List<String> order = new ArrayList<>();

    Page3ZknDmsBetrokkeneBean bean = new Page3ZknDmsBetrokkeneBean();

    addPersoon(betrokkene, order, bean);
    addAdres(betrokkene, order, bean);

    setOrder(order.toArray(new String[0]));
    setColumnWidths(WIDTH_130, "");
    setBean(bean);
  }

  private void addPersoon(BsmRestElement betrokkene, List<String> order, Page3ZknDmsBetrokkeneBean bean) {

    if (betrokkene.isAdded(PERSOON)) {

      String omschrijving = betrokkene.getElementWaarde(OMSCHRIJVING_BETROKKENHEID);

      BsmRestElement persoon = betrokkene.get(PERSOON);
      Bsn bsn = new Bsn(persoon.getElementWaarde(BSN));
      String anp = persoon.getElementWaarde(ANP);
      String voornamen = persoon.getElementWaarde(VOORNAMEN);
      String voorletters = persoon.getElementWaarde(VOORLETTERS);
      String geslachtsnaam = persoon.getElementWaarde(GESLACHTSNAAM);
      String voorv = persoon.getElementWaarde(VOORVOEGSEL);
      String geboortedatum = persoon.getElementWaarde(GEBOORTEDATUM);

      String id = "";
      if (bsn.isCorrect()) {
        id = bsn.getFormatBsn() + " (burgerservicenummer)";
      } else if (fil(anp)) {
        id = anp + " (ANP identificatie)";
      }

      bean.setId(id);
      bean.setNaam(new Naamformats(voorletters, geslachtsnaam, voorv, "", "", null)
          .getNaam_naamgebruik_eerste_voornaam());
      bean.setGeboortedatum(date2str(geboortedatum));

      if (persoon.isAdded(VERBLIJFSADRES)) {

        BsmRestElement verblijfsadres = persoon.get(VERBLIJFSADRES);
        String straat1 = verblijfsadres.getElementWaarde(STRAATNAAM);
        String straat2 = verblijfsadres.getElementWaarde(OPENBARE_RUIMTE_NAAM);
        String hnr = verblijfsadres.getElementWaarde(HUISNUMMER);
        String hnrL = verblijfsadres.getElementWaarde(HUISLETTER);
        String hnrT = verblijfsadres.getElementWaarde(HUISNUMMER_TOEVOEGING);
        String pc = verblijfsadres.getElementWaarde(POSTCODE);
        String wpl = verblijfsadres.getElementWaarde(WOONPLAATSNAAM);

        Adresformats format = new Adresformats();
        format.setValues(fil(straat1) ? straat1 : straat2, hnr, hnrL, hnrT, "", "", pc, "", wpl, "", "", "", "",
            "", "", "");
        bean.setVerblijfsadres(format.getAdres_pc_wpl());
      }

      setCaption(StringUtils.capitalize(omschrijving));

      order.add(Page3ZknDmsBetrokkeneBean.ID);
      order.add(Page3ZknDmsBetrokkeneBean.NAAM);
      order.add(Page3ZknDmsBetrokkeneBean.GEBOORTEDATUM);
      order.add(Page3ZknDmsBetrokkeneBean.VERBLIJFSADRES);
    }
  }

  private void addAdres(BsmRestElement betrokkene, List<String> order, Page3ZknDmsBetrokkeneBean bean) {

    if (betrokkene.isAdded(ADRES)) {

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
      format.setValues(fil(straat1) ? straat1 : straat2, hnr, hnrL, hnrT, "", locatie, pc, "", wpl, "", "", "",
          "", "", "", "");
      bean.setAdres(format.getAdres_pc_wpl());

      setCaption("Adres");
      order.add(Page3ZknDmsBetrokkeneBean.ADRES);
    }
  }
}
