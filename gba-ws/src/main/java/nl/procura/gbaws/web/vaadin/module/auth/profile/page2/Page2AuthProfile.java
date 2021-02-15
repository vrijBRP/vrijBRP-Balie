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

package nl.procura.gbaws.web.vaadin.module.auth.profile.page2;

import static nl.procura.gbaws.web.vaadin.module.auth.profile.page2.Page2AuthProfileBean.*;

import nl.procura.gbaws.db.enums.SearchOrderType;
import nl.procura.gbaws.db.wrappers.ProfileWrapper;
import nl.procura.gbaws.web.vaadin.module.auth.ModuleAuthPage;
import nl.procura.gbaws.web.vaadin.module.auth.profile.page1.Page1AuthProfile;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2AuthProfile extends ModuleAuthPage {

  private final ProfileWrapper profile;
  private Page2AuthProfileForm form1;
  private Page2AuthProfileForm form2;
  private Page2AuthProfileForm form3;

  public Page2AuthProfile(ProfileWrapper profile) {
    this.profile = profile;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonSave);

      form1 = new Page2AuthProfileForm() {

        @Override
        protected void init() {

          setCaption("Profiel");
          setOrder(PROFILE, DESCR);
          setColumnWidths("130px", "");
        }

      };
      form2 = new Page2AuthProfileForm() {

        @Override
        protected void init() {

          setCaption("Configuraties");
          setOrder(CONFIG_GBAV);
          setColumnWidths("130px", "");
        }

      };
      form3 = new Page2AuthProfileForm() {

        @Override
        protected void init() {

          setCaption("Zoeken");
          setOrder(SEARCH_ORDER, SEARCH_PROCURA, SEARCH_GBAV);
          setColumnWidths("130px", "");
        }

      };

      Page2AuthProfileBean bean = new Page2AuthProfileBean();

      bean.setProfile(profile.getNaam());
      bean.setDescr(profile.getOmschrijving());
      bean.setConfigGbav(profile.getGBAvProfiel());
      bean.setSearchOrder(SearchOrderType.get(profile.getAttributen().getZoekVolgorde()));
      bean.setSearchProcura(profile.getAttributen().isDatabron1());
      bean.setSearchGbav(profile.getAttributen().isDatabron2());

      form1.setBean(bean);
      form2.setBean(bean);
      form3.setBean(bean);

      addComponent(form1);
      addComponent(form2);
      addComponent(form3);
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().removeOtherPages();
    getNavigation().goToPage(Page1AuthProfile.class);
  }

  @Override
  public void onSave() {

    form1.commit();
    form2.commit();
    form3.commit();

    Page2AuthProfileBean bean = form1.getBean();

    profile.setNaam(bean.getProfile());
    profile.setOmschrijving(bean.getDescr());

    profile.getAttributen().setGbavBron(bean.getConfigGbav() != null ? bean.getConfigGbav().getPk() : -1);
    profile.getAttributen().setZoekVolgorde(bean.getSearchOrder().getCode());
    profile.getAttributen().setDatabron1(bean.isSearchProcura());
    profile.getAttributen().setDatabron2(bean.isSearchGbav());

    profile.mergeAndCommit();
    profile.getAttributen().mergeAndCommit();

    successMessage("Gegevens zijn opgeslagen");

    super.onSave();
  }
}
