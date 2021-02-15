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

package nl.procura.gba.web.common.misc.email.templates;

import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.web.common.misc.email.Email;
import nl.procura.gba.web.common.misc.email.EmailAddressType;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.email.EmailTemplate;
import nl.procura.gba.web.services.beheer.email.EmailTypeContent;

public class TemplateEmail extends Email {

  private int      duration;
  private Services services;
  private String   applicationUrl;

  public TemplateEmail(EmailTemplate template, Services services, String applicationUrl) {

    setServices(services);
    setApplicationUrl(astr(applicationUrl).replaceAll("/$", ""));
    setDuration(template.getGeldigheid());

    getAdresses().add(EmailAddressType.FROM, "", template.getVan());
    getAdresses().add(EmailAddressType.REPLY_TO, "", template.getAntwoordNaar());
    getAdresses().add(EmailAddressType.BCC, "", template.getBcc());

    setHtml(template.getTypeContent() == EmailTypeContent.HTML);
    setSubject(template.getOnderwerp());
    addLine(template.getInhoud());
  }

  public String getApplicationUrl() {
    return applicationUrl;
  }

  public void setApplicationUrl(String applicationUrl) {
    this.applicationUrl = applicationUrl;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public String getDurationInDays() {
    return duration == 1 ? "1 dag" : (duration + " dagen");
  }

  public Services getServices() {
    return services;
  }

  public void setServices(Services services) {
    this.services = services;
  }
}
