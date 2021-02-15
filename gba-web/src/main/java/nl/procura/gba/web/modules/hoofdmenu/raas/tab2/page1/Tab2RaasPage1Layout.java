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

package nl.procura.gba.web.modules.hoofdmenu.raas.tab2.page1;

import static java.lang.String.format;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;

import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.dto.raas.bestand.RaasBestandDto;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.common.PagingLayout;
import nl.procura.gba.web.modules.zaken.common.PagingLayout.Paging;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;
import nl.procura.raas.rest.domain.Page;
import nl.procura.raas.rest.domain.PageRequest;
import nl.procura.raas.rest.domain.raas.bestand.FindRaasBestandRequest;
import nl.procura.raas.rest.domain.raas.bestand.RaasBestandStatusSoortType;
import nl.procura.raas.rest.domain.raas.bestand.RaasBestandStatusType;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public abstract class Tab2RaasPage1Layout extends CssLayout {

  private final Table          table;
  private final PagingLayout   pagingLayout;
  private Page<RaasBestandDto> page;
  private final DocAanvraagDto aanvraag;

  Tab2RaasPage1Layout(DocAanvraagDto aanvraag) {
    setWidth("100%");
    this.aanvraag = aanvraag;
    pagingLayout = new PagingLayout();
    getPagingLayout().addReloadListener(this::search);
    table = new Table();
  }

  public void search() {

    if (getServices().getRaasService().isRaasServiceActive()) {

      Paging paging = pagingLayout.getPaging();
      PageRequest pageRequest = new PageRequest();
      pageRequest.setAscending(false);
      pageRequest.setPage(paging.getPage());
      pageRequest.setPageSize(paging.getPageSize());
      pageRequest.setAscending(paging.isAscending());

      FindRaasBestandRequest request = new FindRaasBestandRequest();
      if (aanvraag != null) {
        Long aanvraagNr = aanvraag.getAanvraagNr().getValue();
        request.setAanvraagNr(aanvraagNr);
      }
      request.setPageRequest(pageRequest);
      request.setDatumFrom(paging.getFrom());
      request.setDatumUntil(paging.getUntil());

      page = getServices().getRaasService().getAanvragen(request);
      paging.setTotalSize(page.getTotalElements());
      paging.setPage(page.getPageNumber());
      paging.setPages(page.getTotalPages());
      paging.setPageSize(page.getMaxNumberOfElementsInPage());
      paging.setFirst(page.isFirstPage());
      paging.setLast(page.isLastPage());
    }

    table.init();
  }

  public abstract void selectRecord(Record record);

  public abstract Services getServices();

  public abstract void infoMessage(String msg);

  public Table getTable() {
    return table;
  }

  public PagingLayout getPagingLayout() {
    return pagingLayout;
  }

  public class Table extends GbaTable {

    @Override
    public void onDoubleClick(Record record) {
      selectRecord(record);
      super.onDoubleClick(record);
    }

    @Override
    public void setColumns() {

      setSelectable(true);
      setMultiSelect(true);

      addColumn("Nr", 50);
      addColumn("Richting", 60).setClassType(Embedded.class);
      addColumn("Status", 200).setUseHTML(true);
      addColumn("Berichtsoort");
      addColumn("Datum / tijd", 130);
      addColumn("Aanvraagnr.", 100);
      addColumn("Bestandsnaam", 130);

      super.setColumns();
    }

    @Override
    public void setRecords() {
      getPagingLayout().update();
      if (page != null) {
        for (RaasBestandDto rb : page.getContent()) {
          ProcuraDate date = new ProcuraDate(rb.getTimestamp().getValue());
          Record record = addRecord(rb);
          record.addValue(rb.getNr());
          record.addValue(getDirection(rb.getType().getValue().isToRaas()));
          record.addValue(getStatus(rb.getStatus().getValue()));
          record.addValue(format("%s: %s", rb.getType().getValue().getCode(), rb.getType()));
          record.addValue(format("%s om %s", date.getFormatDate(), date.getFormatTime("HH:mm")));
          record.addValue(new Aanvraagnummer(rb.getAanvraagNr().getValue().toString()).getFormatNummer());
          record.addValue(rb.getNaam());
        }
      }
    }

    private Embedded getDirection(boolean toRaas) {
      return new Embedded(null, new ThemeResource("icons/" + (toRaas ? "outgoing.png" : "incoming.png")));
    }

  }

  public static String getStatus(RaasBestandStatusType status) {
    if (RaasBestandStatusSoortType.SUCCESS == status.getStatusSoortType()) {
      return MiscUtils.setClass(true, status.getName());
    }
    if (RaasBestandStatusSoortType.ERROR == status.getStatusSoortType()) {
      return MiscUtils.setClass(false, status.getName());
    }
    return status.getName();
  }
}
