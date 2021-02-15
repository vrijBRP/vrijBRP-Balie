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

package nl.procura.gba.web.services.beheer.email;

import static java.util.Arrays.asList;
import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.UniqueList;
import nl.procura.gba.jpa.personen.dao.EmailDao;
import nl.procura.gba.web.common.misc.email.Email;
import nl.procura.gba.web.common.misc.email.Verzending;
import nl.procura.gba.web.common.misc.email.templates.NieuweGebruikerEmail;
import nl.procura.gba.web.common.misc.email.templates.WachtwoordVergetenEmail;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.gba.web.services.beheer.parameter.Parameters;

public class EmailService extends AbstractService {

  public EmailService() {
    super("E-mail");
  }

  public <T> T getEmailParameters(Email e) {
    ParameterService service = getServices().getParameterService();
    Parameters parameters = service.getGebruikerParameters(Gebruiker.getDefault());

    e.setHost(parameters.get(ParameterConstant.EMAIL_HOST).getValue());
    e.setPort(parameters.get(ParameterConstant.EMAIL_PORT).getValue());
    e.setUsername(parameters.get(ParameterConstant.EMAIL_USERNAME).getValue());
    e.setPassword(parameters.get(ParameterConstant.EMAIL_PW).getValue());
    e.addProperties(parameters.get(ParameterConstant.EMAIL_EIGENSCHAPPEN).getValue());

    return (T) e;
  }

  public List<EmailType> getEmailTypes(EmailTemplate emailTemplate) {
    TypeList types = new TypeList();
    types.add(emailTemplate.getType());
    List<EmailTemplate> templates = getTemplates();

    for (EmailType type : EmailType.values()) {
      if (fil(type.getCode())) {
        if (!isType(templates, type) || !type.isEenmalig()) {
          types.add(type);
        }
      }
    }

    return types;
  }

  public NieuweGebruikerEmail getNewGebruikerEmail(String url, Verzending verzending) {
    EmailTemplate emailTemplate = getTemplate(EmailType.NIEUWE_GEBRUIKER);
    NieuweGebruikerEmail email = new NieuweGebruikerEmail(emailTemplate, getServices(), url, verzending);
    return getEmailParameters(email);
  }

  public EmailTemplate getTemplate(EmailType type) {
    for (EmailTemplate template : getTemplates(type)) {
      if (template.isGeactiveerd()) {
        return template;
      }
    }

    return new EmailTemplate(type);
  }

  public List<EmailTemplate> getTemplates() {
    return copyList(EmailDao.findEmail(), EmailTemplate.class);
  }

  public List<EmailTemplate> getTemplates(EmailType... types) {
    List<EmailTemplate> templates = new ArrayList<>();
    for (EmailTemplate template : getTemplates()) {
      if (asList(types).contains(template.getType())) {
        templates.add(template);
      }
    }
    return templates;
  }

  public List<EmailTemplate> getTemplates(EmailType type) {
    List<EmailTemplate> templates = new ArrayList<>();
    for (EmailTemplate template : getTemplates()) {
      if (template.getType() == type) {
        templates.add(template);
      }
    }

    return templates;
  }

  public WachtwoordVergetenEmail getWachtwoordVergetenEmail(String url, Verzending verzending) {
    EmailTemplate emailTemplate = getTemplate(EmailType.WACHTWOORD_VERGETEN);
    WachtwoordVergetenEmail email = new WachtwoordVergetenEmail(emailTemplate, getServices(), url, verzending);
    return getEmailParameters(email);
  }

  public boolean isGeactiveerd(EmailType type) {
    return getTemplate(type).isGeactiveerd();
  }

  @ThrowException("Fout bij het opslaan")
  @Transactional
  public void save(EmailTemplate template) {
    saveEntity(template);
  }

  @ThrowException("Fout bij het verwijderen")
  @Transactional
  public void delete(EmailTemplate template) {
    removeEntity(template);
  }

  private boolean isType(List<EmailTemplate> templates, EmailType type) {
    for (EmailTemplate template : templates) {
      if (template.getType().equals(type)) {
        return true;
      }
    }

    return false;
  }

  private class TypeList extends UniqueList<EmailType> {

    @Override
    public boolean add(EmailType type) {
      return (type != null && !EmailType.ONBEKEND.equals(type)) && super.add(type);
    }
  }
}
