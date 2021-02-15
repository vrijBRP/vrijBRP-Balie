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

package nl.procura.gba.web.modules.persoonslijst.verblijfplaats.page1;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.persoonslijst.verblijfplaats.page1.RecordHistorieContainer.JUISTE_RECORDS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.PL_TOON_RECORDS;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.pos;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.Property;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.NativeSelect;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.formats.Adres;
import nl.procura.gba.web.modules.persoonslijst.GBACategorieSoortTable;
import nl.procura.gba.web.modules.persoonslijst.overig.listpage.PlListPage;
import nl.procura.gba.web.modules.persoonslijst.verblijfplaats.page2.Page2Verblijfplaats;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page1Verblijfplaats extends PlListPage {

  private GBACategorieSoortTable table             = null;
  private final NativeSelect     recordFilterField = new NativeSelect();
  private final BasePLCat        soort;

  public Page1Verblijfplaats(BasePLCat soort) {
    super("Verblijfplaats");
    this.soort = soort;

    if (heeftOnjuisteRecords()) {
      getButtonLayout().add(recordFilterField);
      getButtonLayout().setWidth("100%");
      getButtonLayout().setComponentAlignment(recordFilterField, Alignment.MIDDLE_RIGHT);
    }
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      setRecordFilterField();
    }
    super.event(event);
  }

  private void setRecordFilterField() {

    recordFilterField.setNullSelectionAllowed(false);
    recordFilterField.setContainerDataSource(new RecordHistorieContainer());
    Gebruiker gebruiker = getServices().getGebruiker();
    String parmValue = gebruiker.getParameters().get(PL_TOON_RECORDS).getValue();
    recordFilterField.setValue(isNotBlank(parmValue) ? parmValue : JUISTE_RECORDS);
    recordFilterField.setImmediate(true);
    recordFilterField.addListener((Property.ValueChangeListener) e -> {
      String value = astr(e.getProperty().getValue());
      getServices().getParameterService().saveParameter(PL_TOON_RECORDS, value, gebruiker, null, true);
      table.init();
    });

    table = new GBACategorieSoortTable(soort) {

      @Override
      public void onClick(Record record) {
        selectRecord(record);
      }

      @Override
      public void setColumns() {

        addColumn("Vnr.", 50);
        addColumn("&nbsp;", 20).setUseHTML(true);
        addColumn("&nbsp;", 20).setUseHTML(true);
        addColumn("Adres").setUseHTML(true);
        addColumn("Woonplaats", 120);
        addColumn("Aanvang", 70);
        addColumn("Gemeente", 200);

        super.setColumns();
      }

      @Override
      public void setRecords() {

        List<Object[]> records = getVbRecords();

        int volgnr = records.size();

        for (Object[] obj : records) {

          BasePLSet set = (BasePLSet) obj[0];
          BasePLRec record = (BasePLRec) obj[1];

          String adres;
          BasePLElem straat = record.getElem(GBAElem.STRAATNAAM);
          BasePLElem huisnummer = record.getElem(GBAElem.HNR);
          BasePLElem huisletter = record.getElem(GBAElem.HNR_L);
          BasePLElem huisnummertoev = record.getElem(GBAElem.HNR_T);
          BasePLElem huisnummeraand = record.getElem(GBAElem.HNR_A);
          BasePLElem postcode = record.getElem(GBAElem.POSTCODE);
          BasePLElem gemeentedeel = record.getElem(GBAElem.GEM_DEEL);
          BasePLElem woonplaats = record.getElem(GBAElem.WPL_NAAM);
          BasePLElem gemeente = record.getElem(GBAElem.GEM_INSCHR);
          BasePLElem datum_aanvang = record.getElem(GBAElem.DATUM_AANVANG_ADRESH);
          BasePLElem emigratieland = record.getElem(GBAElem.LAND_VERTREK);
          BasePLElem emigratiedatum = record.getElem(GBAElem.DATUM_VERTREK_UIT_NL);
          BasePLElem fa = record.getElem(GBAElem.FUNCTIE_ADRES);
          BasePLElem locatie = record.getElem(GBAElem.LOCATIEBESCHR);
          BasePLElem buitenland1 = record.getElem(
              GBAElem.ADRES_BUITENL_1);
          BasePLElem buitenland2 = record.getElem(
              GBAElem.ADRES_BUITENL_2);
          BasePLElem buitenland3 = record.getElem(
              GBAElem.ADRES_BUITENL_3);

          String s_aanv = datum_aanvang.getValue().getDescr();
          if (pos(emigratiedatum.getValue().getCode())) {
            s_aanv = emigratiedatum.getValue().getDescr();
          }

          adres = new Adres(straat, huisnummer, huisletter, huisnummertoev, huisnummeraand, locatie, postcode,
              gemeentedeel, woonplaats, gemeente, datum_aanvang, emigratieland, emigratiedatum,
              buitenland1, buitenland2, buitenland3).getAdresPc();

          Record r = addRecord(new Object[]{ set, record });
          r.addValue(astr(volgnr));
          r.addValue(getMutatieIcon(record));
          r.addValue(fa.isAllowed() ? fa.getValue().getCode() : fa.getValue().getDescr());
          r.addValue(adres + getStatus(record));
          r.addValue(woonplaats.getValue().getDescr());
          r.addValue(s_aanv);
          r.addValue(gemeente.getValue().getDescr());

          volgnr--;
        }
      }

      private List<Object[]> getVbRecords() {
        List<Object[]> l = new ArrayList<>();
        for (BasePLSet set : getSoort().getSets()) {
          for (BasePLRec record : set.getRecs()) {
            if (JUISTE_RECORDS.equals(recordFilterField.getValue()) &&
                (record.isIncorrect() || record.isBagChange())) {
              continue;
            }

            l.add(new Object[]{ set, record });
          }
        }

        return l;
      }

      private String getStatus(BasePLRec record) {
        List<String> statusses = new ArrayList<>();
        if (record.isAdmHistory()) {
          statusses.add("ADMIN. HISTORIE");
        } else if (record.isIncorrect()) {
          statusses.add("ONJUIST");
        }
        if (record.isBagChange()) {
          statusses.add("DUBBEL IVM BAG WIJZIGING");
        }

        return statusses.isEmpty() ? "" : (" - " + setClass(false, StringUtils.join(statusses, " - ")));
      }
    };

    addExpandComponent(table);
  }

  /**
   * Deze categorie heeft records die onjuist / administratief zijn
   */
  private boolean heeftOnjuisteRecords() {
    for (BasePLSet set : soort.getSets()) {
      for (BasePLRec record : set.getRecs()) {
        if (record.isIncorrect() || record.isBagChange()) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void onEnter() {
    if (table.getRecord() != null) {
      selectRecord(table.getRecord());
    }
  }

  private void selectRecord(Record record) {
    Object[] obj = (Object[]) record.getObject();
    getNavigation().goToPage(
        new Page2Verblijfplaats((BasePLSet) obj[0], (BasePLRec) obj[1]));
  }
}
