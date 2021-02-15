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

package nl.procura.gbaws.web.vaadin.module.sources.gbav.page2;

import static nl.procura.gbaws.web.vaadin.module.sources.gbav.page2.Page2DbGbavBean.*;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.Button;

import nl.procura.gba.jpa.personenws.db.GbavProfileType;
import nl.procura.gbaws.db.wrappers.GbavProfileWrapper;
import nl.procura.gbaws.web.vaadin.module.auth.ModuleAuthPage;
import nl.procura.gbaws.web.vaadin.module.sources.gbav.page1.Page1DbGbav;
import nl.procura.gbaws.web.vaadin.module.sources.gbav.page3.Page3DbGbav;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

public class Page2DbGbav extends ModuleAuthPage {

  protected Button buttonDeblock  = new Button("Deblokkeren");
  protected Button buttonPassword = new Button("Nieuw GBA-V wachtwoord");

  private final GbavProfileWrapper profile;
  private Page2DbGbavForm          form1;
  private Page2DbGbavForm          form2;
  private Page2DbGbavForm          form3;
  private InfoLayout               blokkeringInfo = new InfoLayout("Account geblokkeerd",
      ProcuraTheme.ICOON_24.WARNING, "");

  public Page2DbGbav(GbavProfileWrapper profile) {
    this.profile = profile;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonSave);
      addButton(buttonDeblock);
      addButton(buttonPassword);

      if (profile.isGeblokkeerd()) {

        StringBuilder msg = new StringBuilder();
        msg.append(
            "Dit account is geblokkeerd door de applicatie omdat er een incorrect wachtwoord gebruikt is. </br>");
        msg.append("Wijzig het wachtwoord en deblokkeer het account. <br/>");
        msg.append("Na 3 tot 5 pogingen wordt dit account geblokkeerd bij GBA-V.");

        blokkeringInfo = new InfoLayout("Account geblokkeerd", ProcuraTheme.ICOON_24.WARNING, msg.toString());
        addComponent(blokkeringInfo);
      }

      form1 = new Page2DbGbavForm() {

        @Override
        protected void init() {

          setCaption("Profiel");
          setOrder(PROFILE, DESCR);
        }
      };

      form2 = new Page2DbGbavForm() {

        @Override
        protected void init() {

          setCaption("GBA-V account");
          setOrder(TYPE, URL_REQUEST, URL_PW, URL_INDICATIES, USERNAME);
        }
      };

      form3 = new Page2DbGbavForm() {

        @Override
        protected void init() {

          setCaption("GBA-V wachtwoord");
          setOrder(PW, DATE_CHANGED, DATE_EXPIRATION);
        }
      };

      Page2DbGbavBean bean = new Page2DbGbavBean();

      bean.setProfile(profile.getNaam());
      bean.setDescr(profile.getOmschrijving());

      if (profile.getType() >= 0) {
        bean.setType(GbavProfileType.get(profile.getType()));
      }

      bean.setUrlPassword(profile.getAttributen().getWachtwoordEndpoint());
      bean.setUrlRequest(profile.getAttributen().getZoekEndpoint());
      bean.setUrlIndicaties(profile.getAttributen().getAfnemerIndicatiesEndpoint());
      bean.setUsername(profile.getAttributen().getGebruikersnaam());
      bean.setPassword(profile.getAttributen().getWachtwoord());
      bean.setDateChanged(new DateFieldValue(profile.getAttributen().getAanpassingDatum()));

      form1.setBean(bean);
      form2.setBean(bean);
      form3.setBean(bean);

      addComponent(form1);
      addComponent(form2);
      addComponent(form3);

      checkBlokkering();
      checkButtonWachtwoord();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonDeblock) {

      profile.deblokkeer();

      checkBlokkering();

      successMessage("Het account is gedeblokkeerd");
    } else if (button == buttonPassword) {

      getNavigation().goToPage(new Page3DbGbav(profile));
    }

    super.handleEvent(button, keyCode);
  }

  private void checkBlokkering() {
    buttonDeblock.setEnabled(profile.isSaved() && profile.isGeblokkeerd());
    if (!profile.isGeblokkeerd()) {
      removeComponent(blokkeringInfo);
    }
  }

  private void checkButtonWachtwoord() {
    boolean ww = fil(profile.getAttributen().getWachtwoord());
    boolean gebr = fil(profile.getAttributen().getGebruikersnaam());

    buttonPassword.setEnabled(profile.isSaved() && ww && gebr);
  }

  @Override
  public void onPreviousPage() {

    getNavigation().removeOtherPages();
    getNavigation().goToPage(Page1DbGbav.class);
  }

  @Override
  public void onSave() {

    form1.commit();
    form2.commit();
    form3.commit();

    Page2DbGbavBean bean1 = form1.getBean();
    Page2DbGbavBean bean2 = form2.getBean();
    Page2DbGbavBean bean3 = form3.getBean();

    profile.setNaam(bean1.getProfile());
    profile.setOmschrijving(bean1.getDescr());

    profile.setType(bean2.getType().getCode());
    profile.getAttributen().setZoekEndpoint(bean2.getUrlRequest());
    profile.getAttributen().setWachtwoordEndpoint(bean2.getUrlPassword());
    profile.getAttributen().setAfnemerIndicatiesEndpoint(bean2.getUrlIndicaties());

    profile.getAttributen().setGebruikersnaam(bean2.getUsername());

    profile.getAttributen().setWachtwoord(bean3.getPassword());
    profile.getAttributen().setAanpassingDatum(bean3.getDateChanged().getStringValue());

    profile.mergeAndCommit();
    profile.getAttributen().mergeAndCommit();

    checkBlokkering();
    checkButtonWachtwoord();

    successMessage("Gegevens zijn opgeslagen");

    super.onSave();
  }
}
