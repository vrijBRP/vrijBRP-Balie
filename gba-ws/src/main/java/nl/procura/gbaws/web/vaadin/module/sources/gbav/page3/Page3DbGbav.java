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

package nl.procura.gbaws.web.vaadin.module.sources.gbav.page3;

import static nl.procura.gbaws.web.vaadin.module.sources.gbav.page3.Page3DbGbavBean.*;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.diensten.gbav.utils.GbavAntwoord;
import nl.procura.diensten.gbav.utils.GbavResultaat;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gbaws.db.handlers.PasswordHandler;
import nl.procura.gbaws.db.wrappers.GbavProfileWrapper;
import nl.procura.gbaws.web.vaadin.module.auth.ModuleAuthPage;
import nl.procura.gbaws.web.vaadin.module.sources.gbav.page2.Page2DbGbav;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page3DbGbav extends ModuleAuthPage {

  private final GbavProfileWrapper profile;
  private Page3DbGbavForm          form1;
  private Page3DbGbavForm          form2;

  public Page3DbGbav(GbavProfileWrapper profile) {
    this.profile = profile;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonSave);

      buttonSave.setCaption("Versturen (F9)");

      form1 = new Page3DbGbavForm() {

        @Override
        protected void init() {
          setOrder(PW);
        }
      };

      form2 = new Page3DbGbavForm() {

        @Override
        protected void init() {

          setCaption("Antwoord van GBA-V");
          setOrder(CODE, LETTER, OMSCHRIJVING, REFERENTIE);
        }

      };

      addComponent(form1);
      addComponent(form2);
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {

    getNavigation().removeOtherPages();
    getNavigation().goToPage(new Page2DbGbav(profile));
  }

  @Override
  public void onSave() {

    form1.commit();

    Page3DbGbavBean bean1 = form1.getBean();
    Page3DbGbavBean bean2 = form1.getBean();

    final GbavAntwoord antwoord = PasswordHandler.send(profile, bean1.getPassword());
    final GbavResultaat result = antwoord.getResultaat();

    bean2.setCode(MiscUtils.setClass(!result.isFout(), astr(result.getCode())));
    bean2.setLetter(MiscUtils.setClass(!result.isFout(), result.getLetter()));
    bean2.setOmschrijving(MiscUtils.setClass(!result.isFout(), result.getOmschrijving()));
    bean2.setReferentie(MiscUtils.setClass(!result.isFout(), result.getReferentie()));

    form2.setBean(bean2);

    if (result.isFout()) {
      errorMessage("Het wachtwoord is niet gewijzigd");
    } else {
      successMessage("Het wachtwoord is gewijzigd en opgeslagen");
    }

    super.onSave();
  }
}
