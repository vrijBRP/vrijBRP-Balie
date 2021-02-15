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

package nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page1;

import static java.lang.String.format;
import static nl.procura.standard.Globalfunctions.astr;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;

import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.common.PagingLayout;
import nl.procura.gba.web.modules.zaken.common.PagingLayout.Paging;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;
import nl.procura.raas.rest.domain.Page;
import nl.procura.raas.rest.domain.PageRequest;
import nl.procura.raas.rest.domain.aanvraag.FindAanvraagRequest;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.validation.Bsn;

public abstract class Tab1Page1RaasLayout extends CssLayout {

  private final Table          table;
  private final PagingLayout   pagingLayout;
  private Page<DocAanvraagDto> page;
  private Bsn                  bsn;

  Tab1Page1RaasLayout() {
    pagingLayout = new PagingLayout();
    getPagingLayout().addReloadListener(this::search);
    table = new Table();
  }

  public Tab1Page1RaasLayout(Bsn bsn) {
    this();
    this.bsn = bsn;
  }

  public void search() {

    if (getServices().getRaasService().isRaasServiceActive()) {

      Paging paging = pagingLayout.getPaging();
      FindAanvraagRequest request = new FindAanvraagRequest();

      if (bsn != null && bsn.isCorrect()) {
        request.setBsn(bsn.getLongBsn());
      }

      PageRequest pageRequest = new PageRequest();
      pageRequest.setAscending(false);
      pageRequest.setPage(paging.getPage());
      pageRequest.setPageSize(paging.getPageSize());
      pageRequest.setAscending(paging.isAscending());
      request.setPageRequest(pageRequest);
      request.setDatumAanvraagFrom(paging.getFrom());
      request.setDatumAanvraagUntil(paging.getUntil());

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

  //    public void onProcess() {
  //
  //        List<ResponseMessage> responses = new ArrayList<>();
  //        responses.addAll(getServices().getRaasService().processAanvragen());
  //        responses.addAll(getServices().getRaasService().processBerichten());
  //
  //        StringBuilder out = new StringBuilder();
  //        if (responses.isEmpty()) {
  //            out.append("Geen berichten bijgewerkt.");
  //        } else {
  //            long updated = responses.stream().filter(m -> m.isUpdated()).count();
  //            if (updated > 0) {
  //                out.append("Geen berichten bijgewerkt.");
  //                infoMessage(updated + " bericht(en) is bijgewerkt");
  //            } else {
  //                infoMessage("Er zijn geen berichten bijgewerkt");
  //            }
  //        }
  //
  //        search();
  //    }

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

      addColumn("&nbsp;", 20).setClassType(Embedded.class);
      addColumn("Nr", 50);
      addColumn("Datum / tijd", 150);
      addColumn("Aanvraagnummer", 120);
      addColumn("Reisdocument");
      addColumn("Aanvraag");
      addColumn("Levering");
      addColumn("Afsluiting");

      super.setColumns();
    }

    @Override
    public void setRecords() {
      getPagingLayout().update();

      if (page != null) {
        for (DocAanvraagDto aanvraag : page.getContent()) {

          ProcuraDate dAanvraag = new ProcuraDate(aanvraag.getAanvraag().getDatum().getValue());
          ProcuraDate tAanvraag = new ProcuraDate().setStringTime(astr(aanvraag.getAanvraag().getTijd()));
          Aanvraagnummer aanvrNr = new Aanvraagnummer(aanvraag.getAanvraagNr().getValue().toString());

          Record record = addRecord(aanvraag);
          record.addValue(null);
          record.addValue(aanvraag.getNr());
          record.addValue(format("%s om %s", dAanvraag.getFormatDate(), tAanvraag.getFormatTime("HH:mm")));
          record.addValue(aanvrNr.getFormatNummer());
          record.addValue(aanvraag.getDocSoort());
          record.addValue(aanvraag.getAanvraag().getStatus());
          record.addValue(aanvraag.getLevering().getStatus());
          record.addValue(aanvraag.getAfsluiting().getStatus());
        }
      }
    }
  }
}
