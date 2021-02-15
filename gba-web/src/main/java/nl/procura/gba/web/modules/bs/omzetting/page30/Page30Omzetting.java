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

package nl.procura.gba.web.modules.bs.omzetting.page30;

import static nl.procura.gba.web.modules.bs.omzetting.page30.Page30OmzettingBean1.*;

import java.util.List;

import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.omzetting.page30.ambtenaren.OmzettingAmbtenarenWindow;
import nl.procura.gba.web.modules.bs.omzetting.page35.Page35Omzetting;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzettingOptie;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page30Omzetting extends BsPage<DossierOmzetting> {

  private Form1                     form1 = null;
  private Page30OmzettingOptiesForm form2 = null;

  public Page30Omzetting() {

    super("Omzetting GPS in huwelijk - bijzonderheden");

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public boolean checkPage() {

    form2.commit();

    getZaakDossier().setOpties(form2.getOpties());

    getProcessen().updateStatus();

    getApplication().getServices().getOmzettingService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    try {

      if (event.isEvent(InitPage.class)) {

        addComponent(new BsStatusForm(getDossier()));

        setInfo("Geef aan van welke bijzonderheden van toepassing zijn m.b.t. de gekozen locatie en de ceremonie. "
            + "Druk op Volgende (F2) om verder te gaan.");

        form1 = new Form1();

        addComponent(form1);

        List<DossierOmzettingOptie> opties = getServices().getOmzettingService().getDossierHuwelijksOpties(
            getZaakDossier());

        form2 = new Page30OmzettingOptiesForm(opties);

        if (getZaakDossier().getHuwelijksLocatie().getCodeHuwelijksLocatie() > 0) {
          addComponent(new Fieldset(
              "Opties voor locatie " + getZaakDossier().getHuwelijksLocatie().getHuwelijksLocatie(),
              form2));
        } else {
          addComponent(new Fieldset("Opties voor de huwelijkslocatie"));
          addComponent(new InfoLayout("",
              "Er is nog geen huwelijkslocatie geselecteerd.<br/>Dat kan in het vorige scherm."));
        }
      }

      if (event.isEvent(InitPage.class, AfterReturn.class)) {

        form1.update();
      }
    } finally {
      super.event(event);
    }
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  public class Form1 extends Page30OmzettingForm1 {

    public Form1() {

      super(getZaakDossier());
    }

    @Override
    protected void onClickAnders(Property property) {

      if (property.is(AMBTENAAR1)) {

        getNavigation().goToPage(new Page35Omzetting(getZaakDossier().getAmbtenaar1()));
      } else if (property.is(AMBTENAAR2)) {

        getNavigation().goToPage(new Page35Omzetting(getZaakDossier().getAmbtenaar2()));
      } else if (property.is(AMBTENAAR3)) {

        getNavigation().goToPage(new Page35Omzetting(getZaakDossier().getAmbtenaar3()));
      }
    }

    @Override
    protected void onClickDelete(Property property) {

      if (property.is(AMBTENAAR1)) {

        BsPersoonUtils.reset(getZaakDossier().getAmbtenaar1());
      } else if (property.is(AMBTENAAR2)) {

        BsPersoonUtils.reset(getZaakDossier().getAmbtenaar2());
      } else if (property.is(AMBTENAAR3)) {

        BsPersoonUtils.reset(getZaakDossier().getAmbtenaar3());
      }

      form1.update();
    }

    @Override
    protected void onClickSelect(Property property) {

      if (property.is(AMBTENAAR1)) {

        getWindow().addWindow(new OmzettingAmbtenarenWindow(form1, getZaakDossier().getAmbtenaar1()));
      } else if (property.is(AMBTENAAR2)) {

        getWindow().addWindow(new OmzettingAmbtenarenWindow(form1, getZaakDossier().getAmbtenaar2()));
      } else if (property.is(AMBTENAAR3)) {

        getWindow().addWindow(new OmzettingAmbtenarenWindow(form1, getZaakDossier().getAmbtenaar3()));
      }
    }
  }
}
