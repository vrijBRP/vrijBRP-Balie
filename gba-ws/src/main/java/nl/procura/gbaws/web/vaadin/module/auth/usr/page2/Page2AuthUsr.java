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

package nl.procura.gbaws.web.vaadin.module.auth.usr.page2;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.TextField;

import nl.procura.gba.common.password.DelegatingPasswordEncoder;
import nl.procura.gbaws.db.wrappers.UsrWrapper;
import nl.procura.gbaws.web.vaadin.module.auth.ModuleAuthPage;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2AuthUsr extends ModuleAuthPage {

  private final UsrWrapper usr;
  private Page2AuthUsrForm form;

  public Page2AuthUsr(UsrWrapper usr) {
    this.usr = usr;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonSave);

      form = new Page2AuthUsrForm();

      Page2AuthUsrBean bean = new Page2AuthUsrBean();
      bean.setName(usr.getGebruikersNaam());
      bean.setDisplay(usr.getVolledigeNaam());
      bean.setAdmin(usr.isAdmin());
      bean.setProfile(usr.getProfiel());

      form.setBean(bean);

      TextField passwordField = form.getField(Page2AuthUsrBean.PW, TextField.class);
      if (isBlank(usr.getWachtwoord())) {
        passwordField.setRequired(true);
      } else {
        passwordField.setInputPrompt("Wachtwoord");
      }

      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onSave() {

    form.commit();

    Page2AuthUsrBean bean = form.getBean();

    usr.setGebruikersNaam(bean.getName());
    usr.setVolledigeNaam(bean.getDisplay());
    usr.setAdmin(bean.getAdmin());
    usr.setProfiel(bean.getProfile());

    if (StringUtils.isNotBlank(bean.getPassword())) {
      usr.setWachtwoord(new DelegatingPasswordEncoder(1).encode(bean.getPassword()));
    }

    usr.mergeAndCommit();

    successMessage("Gegevens zijn opgeslagen");

    super.onSave();
  }
}
