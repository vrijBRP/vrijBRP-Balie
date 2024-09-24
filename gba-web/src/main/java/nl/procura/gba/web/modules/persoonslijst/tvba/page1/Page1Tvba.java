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

package nl.procura.gba.web.modules.persoonslijst.tvba.page1;

import static nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBean.DATUMEINDE;
import static nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBean.DATUMINGANG;
import static nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBean.GELDIGHEID;
import static nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBean.INDDOCUMENT;
import static nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBean.INONDERZOEK;
import static nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBean.ONJUIST;
import static nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBean.OPNAME;

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

public class Page1Tvba extends PlDataPage {

  public Page1Tvba(BasePLSet set) {
    super("Tijdelijk verblijfsadres", set);
  }

  @Override
  public PlForm getMainForm() {

    return new Page1TvbaForm() {

      @Override
      public void setBean(Object bean) {

        Page1TvbaBean b = new Page1TvbaBean();

        String adres;
        BasePLElem straat = getElement(GBAElem.STRAATNAAM);
        BasePLElem huisnummer = getElement(GBAElem.HNR);
        BasePLElem huisletter = getElement(GBAElem.HNR_L);
        BasePLElem huisnummertoev = getElement(GBAElem.HNR_T);
        BasePLElem huisnummeraand = getElement(GBAElem.HNR_A);
        BasePLElem postcode = getElement(GBAElem.POSTCODE);
        BasePLElem gemeente = getElement(GBAElem.GEM_INSCHR);
        BasePLElem woonplaats = getElement(GBAElem.WPL_NAAM);

        adres = new Adres(straat, huisnummer, huisletter, huisnummertoev, huisnummeraand, new BasePLElem(), postcode,
            new BasePLElem(), woonplaats, gemeente, new BasePLElem(), new BasePLElem(), new BasePLElem(),
            new BasePLElem(), new BasePLElem(), new BasePLElem()).getAdresPc();

        b.setAangifte(getElementW(GBAElem.OMSCHR_VAN_DE_AANGIFTE_ADRESH).getDescr());
        b.setAdres(adres);
        b.setDatumInschrijving(getElementW(GBAElem.DATUM_INSCHR).getDescr());
        b.setGemeente(getElementW(GBAElem.GEM_INSCHR).getDescr());

        b.setWoonplaats(getElementW(GBAElem.WPL_NAAM).getDescr());
        b.setOpenbareRuimte(getElementW(GBAElem.OPENB_RUIMTE).getDescr());
        b.setAon(getElementW(GBAElem.ID_VERBLIJFPLAATS).getDescr());
        b.setIna(getElementW(GBAElem.IDCODE_NUMMERAAND).getDescr());

        b.setEindeGeldigheid(getElementW(GBAElem.EINDDATUM_GELDIG).getDescr());
        b.setTypeAdres(getElementW(GBAElem.TYPE_ADRES).getDescr());

        super.setBean(b);
      }
    };
  }

  @Override
  public PlDataBeanForm getMetaForm(BasePLRec record) {

    return new PlDataBeanForm(record) {

      @Override
      public String[] getOrder() {
        return new String[]{ GELDIGHEID, OPNAME, ONJUIST, INONDERZOEK, DATUMINGANG, DATUMEINDE };
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
