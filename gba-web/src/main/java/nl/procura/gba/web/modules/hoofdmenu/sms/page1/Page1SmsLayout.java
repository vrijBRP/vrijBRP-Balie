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

package nl.procura.gba.web.modules.hoofdmenu.sms.page1;

import static nl.procura.gba.common.MiscUtils.setClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.common.PagingLayout;
import nl.procura.gba.web.modules.zaken.common.PagingLayout.Paging;
import nl.procura.gba.web.services.Services;
import nl.procura.sms.rest.domain.*;
import nl.procura.standard.ProcuraDate;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.validation.Bsn;

public abstract class Page1SmsLayout extends CssLayout {

  private final Table        table;
  private final PagingLayout pagingLayout;
  public final Button        buttonVerwerk = new Button("Verwerk berichten");
  private Page<Message>      page;
  private Bsn                bsn;

  public Page1SmsLayout() {
    pagingLayout = new PagingLayout();
    getPagingLayout().addReloadListener(this::search);
    table = new Table();
  }

  public Page1SmsLayout(Bsn bsn) {
    this();
    this.bsn = bsn;
  }

  public void search() {

    if (getServices().getSmsService().isSmsServiceActive()) {

      Paging paging = pagingLayout.getPaging();
      FindMessagesRequest request = new FindMessagesRequest();

      if (bsn != null && bsn.isCorrect()) {
        request.setBsn(bsn.getLongBsn());
      }

      PageRequest pageRequest = new PageRequest();
      pageRequest.setAscending(false);
      pageRequest.setPage(paging.getPage());
      pageRequest.setPageSize(paging.getPageSize());
      pageRequest.setAscending(paging.isAscending());
      request.setPageRequest(pageRequest);
      request.setFrom(paging.getFrom());
      request.setUntil(paging.getUntil());

      page = getServices().getSmsService().getMessages(request);
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

  public void onSend() {

    List<Message> selectedMessages = getTable().getSelectedValues(Message.class);

    if (selectedMessages.isEmpty()) {
      throw new ProException(ProExceptionSeverity.INFO, "Geen records geselecteerd");
    }

    List<ResponseMessage> responses = new ArrayList<>();

    List<Message> sendableMessages = selectedMessages.stream().filter(
        m -> !m.isSendToSmsService() && !m.isStatusFinal()).collect(Collectors.toList());

    if (!sendableMessages.isEmpty()) {
      responses.addAll(getServices().getSmsService().sendMessages(selectedMessages));
    }

    List<Message> updateableMessages = selectedMessages.stream().filter(
        m -> m.isSendToSmsService() && !m.isStatusFinal()).collect(Collectors.toList());

    if (!updateableMessages.isEmpty()) {
      responses.addAll(getServices().getSmsService().updateStatus(updateableMessages));
    }

    if (responses.isEmpty()) {
      infoMessage("Geen berichten bijgewerkt");
    } else {
      long updated = responses.stream().filter(ResponseMessage::isUpdated).count();
      if (updated > 0) {
        infoMessage(updated + " bericht(en) is bijgewerkt");
      } else {
        infoMessage("Er zijn geen berichten bijgewerkt");
      }
    }

    search();
  }

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
      addColumn("Datum / tijd", 150);
      addColumn("Bsn", 110);
      addColumn("Telefoon nr.", 110);
      addColumn("SMS afzender", 200);
      addColumn("Naar SMS service", 110).setUseHTML(true);
      addColumn("Ontvangen door telefoon", 200).setUseHTML(true);
      addColumn("Melding").setUseHTML(true);

      super.setColumns();
    }

    @Override
    public void setRecords() {
      getPagingLayout().update();

      if (page != null) {
        for (Message message : page.getContent()) {
          ProcuraDate timestamp = new ProcuraDate(message.getTimestamp());
          Record record = addRecord(message);
          record.addValue(message.getNr());
          record.addValue(timestamp.getFormatDate("dd-MM-yyyy HH:mm"));
          record.addValue(message.getBsn());
          record.addValue(message.getDestination());
          record.addValue(message.getSender().getDescription());
          record.addValue(getStatusService(message));
          record.addValue(getStatusDestination(message));
          record.addValue(getError(message));
        }
      }
    }

    private String getError(Message message) {
      MessageStatus status = message.getStatus();
      return status != null ? setClass(false, status.getMessage()) : "";
    }

    private String getStatusService(Message message) {
      if (message.isStatusFinal() && !message.isSendToSmsService()) {
        return setClass(false, "Nee");
      }
      if (message.isSendToSmsService()) {
        return setClass(true, "Ja");
      }
      return "Nog niet";
    }

    private String getStatusDestination(Message message) {
      if (message.isStatusFinal() && !message.isSendToDestinationSuccess()) {
        return setClass(false, "Nee");
      }
      if (message.isSendToDestinationSuccess()) {
        return setClass(true, "Ja");
      } else if (message.isSendToDestinationFailed()) {
        return setClass(false, "Nee");
      }
      if (message.getStatus().getSmsStatus() != null) {
        return "Nog niet: " + message.getStatus().getSmsStatus().getName();
      }
      return "Onbekend";
    }
  }
}
