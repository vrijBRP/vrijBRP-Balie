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

package nl.procura.gba.web.services.beheer.sms;

import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.isTru;

import java.util.List;
import java.util.stream.Collectors;

import nl.procura.gba.common.UniqueList;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.jpa.personen.dao.SmsDao;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.contact.ContactZaak;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.contact.MobilePhoneNumber;
import nl.procura.gba.web.services.zaken.contact.PlContactgegeven;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.sms.rest.client.SmsRestClient;
import nl.procura.sms.rest.domain.*;

public class SmsService extends AbstractService {

  public SmsService() {
    super("SMS");
  }

  private SmsRestClient getSmsClient() {
    ParameterService parameterService = getServices().getParameterService();
    String baseUrl = parameterService.getSysteemParameter(ParameterConstant.SMS_ENDPOINT).getValue();
    String username = parameterService.getSysteemParameter(ParameterConstant.SMS_USERNAME).getValue();
    String password = parameterService.getSysteemParameter(ParameterConstant.SMS_PW).getValue();
    return new SmsRestClient(baseUrl, username, password);
  }

  public boolean isSmsServiceActive() {
    return isTru(getServices().getParameterService().getSysteemParameter(ParameterConstant.SMS_ENABLED).getValue());
  }

  @ThrowException("Fout bij ophalen SMS berichten")
  public Page<Message> getMessages(FindMessagesRequest request) {
    return getSmsClient().getMessage().findMessages(request).getPage();
  }

  @ThrowException("Fout bij ophalen gegevens")
  public FindCustomerResponse getCustomers() {
    return getSmsClient().getSms().getCustomers();
  }

  @ThrowException("Fout bij verwijderen SMS berichten")
  public List<ResponseMessage> delete(DeleteMessageRequest request) {
    return getSmsClient().getMessage().delete(request).getResponseMessages();
  }

  @ThrowException("Fout bij verzenden SMS berichten")
  public List<ResponseMessage> sendMessages(List<Message> messages) {
    List<String> uids = messages.stream().map(Message::getSmsId).collect(Collectors.toList());
    SendMessageRequest request = new SendMessageRequest(uids);
    return getSmsClient().getSms().sendMessages(request).getResponseMessages();
  }

  @ThrowException("Fout bij verzenden SMS berichten")
  public List<ResponseMessage> updateStatus(List<Message> messages) {
    List<String> uids = messages.stream().map(Message::getSmsId).collect(Collectors.toList());
    UpdateMessageStatusRequest request = new UpdateMessageStatusRequest(uids);
    return getSmsClient().getSms().getMessageStatus(request).getResponseMessages();
  }

  @ThrowException("Fout bij opslaan SMS bericht")
  public String addMessage(AddMessageRequest request) {
    return getSmsClient().getMessage().add(request).getMessageId();
  }

  @ThrowException("Fout bij ophalen gegevens")
  public List<Sender> getSenders() {
    return getSmsClient().getSms().getSenders().getSenders();
  }

  @ThrowException("Fout bij ophalen gegevens")
  public AccountInformationResponse getAccount() {
    return getSmsClient().getAccount().getAccountInformation();
  }

  @ThrowException("Fout bij wijzigen gegevens")
  public void updateSender(Sender sender) {

    if (fil(sender.getId())) {
      EditSenderRequest request = new EditSenderRequest();
      request.setSenderId(sender.getId());
      request.setNewSenderName(sender.getName());
      request.setNewSenderDescription(sender.getDescription());
      getSmsClient().getSms().editSender(request);
    } else {
      AddSenderRequest request = new AddSenderRequest();
      request.setSenderName(sender.getName());
      request.setSenderDescription(sender.getDescription());
      getSmsClient().getSms().addSender(request);
    }
  }

  @ThrowException("Fout bij verwijderen gegevens")
  public void deleteSender(DeleteSenderRequest request) {
    getSmsClient().getSms().deleteSender(request);
  }

  @ThrowException("Fout bij het toevoegen van een SMS")
  public void addSms(Zaak zaak, ZaakStatusType newStatus) {

    if (isSmsServiceActive() && newStatus.is(
        ZaakStatusType.DOCUMENT_ONTVANGEN) && (zaak instanceof ContactZaak)) {

      List<Sender> senders = getSenders();
      SmsTemplate template = null;

      if (zaak instanceof ReisdocumentAanvraag) {
        template = getActiveTemplate(senders, SmsType.REISDOCUMENT_ONTVANGEN);
      }

      if (zaak instanceof RijbewijsAanvraag) {
        template = getActiveTemplate(senders, SmsType.RIJBEWIJS_ONTVANGEN);
      }

      if (template != null) {
        ContactgegevensService contactgegevensService = getServices().getContactgegevensService();
        List<PlContactgegeven> contactgegevens = contactgegevensService.getContactgegevens(zaak);
        MobilePhoneNumber mobileNumber = contactgegevensService.getValidMobileNumber(contactgegevens);

        Customer customer = getCustomers().getCustomer();
        AddMessageRequest request = new AddMessageRequest();
        request.setApplicationName(customer.getApplications().get(0).getApplicationName());
        request.setBsn(zaak.getBurgerServiceNummer().getLongValue());
        request.setZaakId(zaak.getZaakId());
        request.setDestination(mobileNumber.getMobileNr());
        request.setSender(template.getSender());
        request.setAutoSend(template.isAutoSend());
        request.setSmsMessage(template.getContent());
        addMessage(request);
      }
    }
  }

  public List<SmsTemplate> getTemplates(List<Sender> senders) {
    List<SmsTemplate> templates = copyList(SmsDao.findSms(), SmsTemplate.class);
    for (SmsTemplate template : templates) {
      for (Sender sender : senders) {
        if (sender.getId().equals(template.getSenderId())) {
          template.setSender(sender);
        }
      }
    }
    return templates;
  }

  @ThrowException("Fout bij het opslaan")
  @Transactional
  public void save(SmsTemplate template) {
    saveEntity(template);
  }

  public List<SmsType> getSmsTypes(List<SmsTemplate> templates, SmsTemplate smsTemplate) {
    TypeList types = new TypeList();
    types.add(smsTemplate.getSmsType());
    for (SmsType type : SmsType.values()) {
      if (fil(type.getCode())) {
        if (!isType(templates, type)) {
          types.add(type);
        }
      }
    }

    return types;
  }

  private SmsTemplate getActiveTemplate(List<Sender> senders, SmsType type) {
    for (SmsTemplate template : getTemplates(senders)) {
      if (template.getSmsType().equals(type)) {
        if (template.isActivated()) {
          return template;
        }
      }
    }
    return null;
  }

  private boolean isType(List<SmsTemplate> templates, SmsType type) {
    for (SmsTemplate template : templates) {
      if (template.getSmsType().equals(type)) {
        return true;
      }
    }

    return false;
  }

  @ThrowException("Fout bij het verwijderen")
  @Transactional
  public void delete(SmsTemplate template) {
    removeEntity(template);
  }

  private class TypeList extends UniqueList<SmsType> {

    @Override
    public boolean add(SmsType type) {
      return (type != null && !SmsType.ONBEKEND.equals(type)) && super.add(type);
    }
  }
}
