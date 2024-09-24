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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page6;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.modules.beheer.overig.TableSelectionCheck;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterPage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page80.Page80Zaken;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingRegistratie;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingService.REGISTRATIE_STATUS;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.theme.twee.Icons;

public class Page6Zaken extends ZakenregisterPage<Zaak> {

  private static final int EXT_STAT   = 2;
  private static final int EXT_RESULT = 3;
  private static final int EXT_ICON   = 4;
  private static final int EXT_ACTIE  = 5;

  private Table1 table1 = null;

  public Page6Zaken() {
    super(null, "Zakenregister: terugmeldingen actualiseren");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      buttonSearch.setCaption("Actualiseren (F3)");

      addButton(buttonSearch);

      setInfo("Alle terugmeldingen die in de applicatie op <b>opgenomen</b> of <b>in behandeling</b> staan.");

      table1 = new Table1();

      addExpandComponent(table1);
    }

    super.event(event);
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  @Override
  public void onSearch() {

    table1.getRecords();

    TableSelectionCheck.checkSelection(table1);

    for (Record record : table1.getSelectedRecords()) {

      TerugmeldingAanvraag aanvraag = (TerugmeldingAanvraag) record.getObject();

      try {

        REGISTRATIE_STATUS status = getServices().getTerugmeldingService().actualiseer(aanvraag);

        TerugmeldingRegistratie reg = getLaatsteRegistratie(aanvraag);

        table1.setRecordValue(record, EXT_STAT, reg.getStatus());
        table1.setRecordValue(record, EXT_RESULT, reg.getOnderzoekResultaat());

        if (fil(aanvraag.getWaarschuwing().getMsg())) {

          Embedded tI = (Embedded) table1.getItem(record.getItemId()).getItemProperty(EXT_ICON).getValue();

          tI.setSource(new ThemeResource(Icons.getIcon(Icons.ICON_WARN)));
        } else {
          table1.setRecordValue(record, EXT_ICON, null);
        }

        if (status == REGISTRATIE_STATUS.TOEGEVOEGD) {

          table1.setRecordValue(record, EXT_ACTIE, setClass(true, "Bijgewerkt"));
        } else {

          table1.setRecordValue(record, EXT_ACTIE, "Geen nieuwe status");
        }

      } catch (ProException e) {

        table1.setRecordValue(record, EXT_ACTIE, setClass(false, "Fout: " + e.getMessage()));
        throw e;
      }
    }
  }

  private TerugmeldingRegistratie getLaatsteRegistratie(TerugmeldingAanvraag aanvraag) {

    for (TerugmeldingRegistratie reg : aanvraag.getRegistraties()) {

      if (reg.isStored()) {

        return reg;
      }
    }
    return null;
  }

  class Table1 extends Page6ZakenTable1 {

    private List<TerugmeldingAanvraag> zaken = new ArrayList<>();

    @Override
    public void onDoubleClick(Record record) {

      if (record.getObject() instanceof TerugmeldingAanvraag) {

        getNavigation().goToPage(new Page80Zaken((TerugmeldingAanvraag) record.getObject()));
      }
    }

    @Override
    public void setRecords() {

      if (zaken.isEmpty()) {

        ZaakArgumenten z = new ZaakArgumenten();

        z.addStatussen(ZaakStatusType.OPGENOMEN, ZaakStatusType.INBEHANDELING);

        zaken = getServices().getZakenService().getVolledigeZaken(
            getServices().getTerugmeldingService().getMinimalZaken(z));
      }

      for (TerugmeldingAanvraag zaak : zaken) {

        TerugmeldingRegistratie reg = getLaatsteRegistratie(zaak);

        if (reg != null) {

          Record r = addRecord(zaak);
          r.addValue(reg.getDossiernummer());
          r.addValue(ZaakUtils.getStatus(zaak.getStatus()));
          r.addValue(reg.getStatus());
          r.addValue(reg.getOnderzoekResultaat());
          r.addValue(new Label(""));
          r.addValue("Nog niet geactualiseerd");
        }
      }

      super.setRecords();
    }
  }
}
