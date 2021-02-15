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

package nl.procura.gba.web.modules.persoonslijst.verblijfplaats.page2;

import static nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBean.*;
import static nl.procura.standard.Globalfunctions.trim;

import com.vaadin.ui.Field;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.formats.Adres;
import nl.procura.gba.web.modules.persoonslijst.overig.PlForm;
import nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBeanForm;
import nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataPage;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page2Verblijfplaats extends PlDataPage {

  public Page2Verblijfplaats(BasePLSet set, BasePLRec record) {
    super("Verblijfplaats", set, record);
  }

  @Override
  public PlForm getMainForm() {

    return new Page2VerblijfplaatsForm() {

      @Override
      public void setBean(Object bean) {

        Page2VerblijfplaatsBean b = new Page2VerblijfplaatsBean();

        String adres;
        BasePLElem straat = getElement(GBAElem.STRAATNAAM);
        BasePLElem huisnummer = getElement(GBAElem.HNR);
        BasePLElem huisletter = getElement(GBAElem.HNR_L);
        BasePLElem huisnummertoev = getElement(GBAElem.HNR_T);
        BasePLElem huisnummeraand = getElement(GBAElem.HNR_A);
        BasePLElem postcode = getElement(GBAElem.POSTCODE);
        BasePLElem gemeentedeel = getElement(GBAElem.GEM_DEEL);
        BasePLElem gemeente = getElement(GBAElem.GEM_INSCHR);
        BasePLElem datum_aanvang = getElement(GBAElem.DATUM_AANVANG_ADRESH);
        BasePLElem emigratieland = getElement(GBAElem.LAND_VERTREK);
        BasePLElem emigratiedatum = getElement(GBAElem.DATUM_VERTREK_UIT_NL);
        BasePLElem locatie = getElement(GBAElem.LOCATIEBESCHR);
        BasePLElem buitenland1 = getElement(GBAElem.ADRES_BUITENL_1);
        BasePLElem buitenland2 = getElement(GBAElem.ADRES_BUITENL_2);
        BasePLElem buitenland3 = getElement(GBAElem.ADRES_BUITENL_3);
        BasePLElem woonplaats = getElement(GBAElem.WPL_NAAM);

        adres = new Adres(straat, huisnummer, huisletter, huisnummertoev, huisnummeraand, locatie, postcode,
            gemeentedeel, woonplaats, gemeente, datum_aanvang, emigratieland, emigratiedatum,
            buitenland1, buitenland2, buitenland3).getAdresPc();

        b.setAangifte(getElementW(GBAElem.OMSCHR_VAN_DE_AANGIFTE_ADRESH).getDescr());
        b.setAanvang(getElementW(GBAElem.DATUM_AANVANG_ADRESH).getDescr());
        b.setAdres(adres);
        b.setDatumInschrijving(getElementW(GBAElem.DATUM_INSCHR).getDescr());
        b.setFunctieadres(getElementW(GBAElem.FUNCTIE_ADRES).getDescr());
        b.setGemeente(getElementW(GBAElem.GEM_INSCHR).getDescr());
        b.setGemeentedeel(getElementW(GBAElem.GEM_DEEL).getDescr());

        b.setWoonplaats(getElementW(GBAElem.WPL_NAAM).getDescr());
        b.setOpenbareRuimte(getElementW(GBAElem.OPENB_RUIMTE).getDescr());
        b.setAon(getElementW(GBAElem.ID_VERBLIJFPLAATS).getDescr());
        b.setIna(getElementW(GBAElem.IDCODE_NUMMERAAND).getDescr());

        String sEmigratiedatum = getElementW(GBAElem.DATUM_VERTREK_UIT_NL).getDescr();
        String sEmigratieland = getElementW(GBAElem.LAND_VERTREK).getDescr();

        b.setVertrek(trim(sEmigratiedatum + ", " + sEmigratieland));

        String sAdresVertrokken1 = getElementW(
            GBAElem.ADRES_BUITENL_1).getDescr();
        String sAdresVertrokken2 = getElementW(
            GBAElem.ADRES_BUITENL_2).getDescr();
        String sAdresVertrokken3 = getElementW(
            GBAElem.ADRES_BUITENL_3).getDescr();

        b.setVertrekAdres(trim(sAdresVertrokken1 + "; " + sAdresVertrokken2 + "; " + sAdresVertrokken3));

        String sImmigratiedatum = getElementW(GBAElem.DATUM_VESTIGING_IN_NL).getDescr();
        String sImmigratieland = getElementW(GBAElem.LAND_VESTIGING).getDescr();

        b.setVestiging(trim(sImmigratiedatum + ", " + sImmigratieland));
        super.setBean(b);
      }
    };
  }

  @Override
  public PlDataBeanForm getMetaForm(BasePLRec record) {

    return new PlDataBeanForm(record) {

      @Override
      public String[] getOrder() {

        return new String[]{ GELDIGHEID, OPNAME, INDDOCUMENT, ONJUIST, INONDERZOEK, DATUMINGANG, DATUMEINDE };
      }

      @Override
      public void setColumn(Column column, Field field, Property property) {

        if (property.is(INDDOCUMENT, ONJUIST)) {
          getLayout().addBreak();
        }
      }
    };
  }
}
