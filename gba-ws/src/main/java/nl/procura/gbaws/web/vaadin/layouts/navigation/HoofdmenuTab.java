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

package nl.procura.gbaws.web.vaadin.layouts.navigation;

import nl.procura.gbaws.web.vaadin.login.GbaWsCredentials;
import nl.procura.gbaws.web.vaadin.module.auth.ModuleAuth;
import nl.procura.gbaws.web.vaadin.module.email.ModuleEmail;
import nl.procura.gbaws.web.vaadin.module.requests.ModuleRequestLog;
import nl.procura.gbaws.web.vaadin.module.sources.ModuleDb;
import nl.procura.gbaws.web.vaadin.module.tables.ModuleTables;
import nl.procura.gbaws.web.vaadin.module.ws.ModuleWs;
import nl.procura.vaadin.component.layout.accordion.AccordionLink;

@SuppressWarnings("serial")
public class HoofdmenuTab extends TemplateAccordionTab {

  public HoofdmenuTab() {
    setCaption("Navigatie");
  }

  @Override
  public void attach() {

    if (getComponentCount() == 0) {

      addLink(new AccordionLink(ModuleRequestLog.class, "#berichten", "Berichten", "icons/messages.png"));
      addLink(new AccordionLink(ModuleWs.class, "#ws", "Webservices", "icons/ws.png"));

      GbaWsCredentials creds = (GbaWsCredentials) getApplication().getUser();

      if (creds.isAdmin()) {
        addLink(new AccordionLink(ModuleAuth.class, "#autorisaties", "Autorisaties", "icons/groep.png"));
        addLink(new AccordionLink(ModuleDb.class, "#bronnen", "BRP Bronnen", "icons/database.png"));
        addLink(new AccordionLink(ModuleTables.class, "#gegevens", "Basisgegevens", "icons/table.png"));
        addLink(new AccordionLink(ModuleEmail.class, "#email", "E-mail", "icons/email.png"));
      }
    }

    super.attach();
  }
}
