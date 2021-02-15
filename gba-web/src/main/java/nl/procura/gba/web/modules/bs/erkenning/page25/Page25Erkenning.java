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

package nl.procura.gba.web.modules.bs.erkenning.page25;

import static nl.procura.gba.web.modules.bs.erkenning.page25.Page25ErkenningBean2.*;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.KIND;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.MOEDER;
import static nl.procura.gba.web.services.bs.algemeen.functies.BsNatioUtils.heeftAsiel;
import static nl.procura.gba.web.services.bs.algemeen.functies.BsNatioUtils.heeftNederlandseNationaliteit;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import com.vaadin.data.Container;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.erkenning.BsPageErkenning;
import nl.procura.gba.web.services.bs.algemeen.enums.RechtbankLocatie;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ToestemminggeverType;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Erkenning
 */

public class Page25Erkenning extends BsPageErkenning {

  private Page25ErkenningForm1 form1 = null;
  private Page25ErkenningForm2 form2 = null;

  public Page25Erkenning() {

    super("Erkenning - toestemming");

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public boolean checkPage() {

    form1.commit();
    form2.commit();

    DossierErkenning dossier = getZaakDossier();

    dossier.setLandToestemmingsRechtMoeder(form2.getBean().getRechtMoeder());
    dossier.setLandToestemmingsRechtKind(form2.getBean().getRechtKind());
    dossier.setToestemminggeverType(form2.getBean().getToestemminggeverType());
    dossier.setToestemmingStap(toBigDecimal(0));

    RechtbankLocatie rechtbank = form2.getBean().getRechtbank();
    dossier.setRechtbank(rechtbank != null ? rechtbank.getCode() : null);

    // Als veld uitstaat dan niet opslaan
    if (!form2.getField(RECHT_KIND).isVisible()) {
      dossier.setLandToestemmingsRechtKind(new FieldValue());
    }

    if (!form2.getField(RECHT_MOEDER).isVisible()) {
      dossier.setLandToestemmingsRechtMoeder(new FieldValue());
    }

    getServices().getErkenningService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addComponent(new BsStatusForm(getZaakDossier().getDossier()));

      setInfo(
          "Bepaal welk recht van toepassing is op de toestemming voor de erkenning en leg vast of en door wie toestemming is verleend. "
              + "Druk op Volgende (F2) om verder te gaan");

      initFormulieren();

      addComponent(form1);
      addComponent(new Fieldset("Conclusie toestemmingsrecht"));
      addComponent(form2);

      initWaarden();
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  private void initFormulieren() {

    form1 = new Page25ErkenningForm1(getZaakDossier());

    form2 = new Page25ErkenningForm2(getZaakDossier()) {

      @Override
      protected Container getContainerKind() {
        return new Page25RechtContainer(getServices(), getZaakDossier(), KIND);
      }

      @Override
      protected Container getContainerMoeder() {
        return new Page25RechtContainer(getServices(), getZaakDossier(), MOEDER);
      }

      @Override
      protected void onChangeToestemminggever(ToestemminggeverType toestemminggeverType) {

        getField(RECHTBANK).setVisible(
            toestemminggeverType.is(ToestemminggeverType.RECHTBANK, ToestemminggeverType.KIND_EN_RECHTBANK,
                ToestemminggeverType.MOEDER_EN_RECHTBANK));

        getField(RECHT_MOEDER).setVisible(toestemminggeverType.is(ToestemminggeverType.MOEDER_EN_RECHTBANK,
            ToestemminggeverType.MOEDER_EN_KIND,
            ToestemminggeverType.MOEDER));

        getField(RECHT_KIND).setVisible(toestemminggeverType.is(ToestemminggeverType.KIND_EN_RECHTBANK,
            ToestemminggeverType.MOEDER_EN_KIND,
            ToestemminggeverType.KIND));

        repaint();
      }
    };
  }

  private void initWaarden() {

    DossierErkenning impl = getZaakDossier();

    String redenMoeder = "";
    String redenKind = "";

    if (heeftNederlandseNationaliteit(impl.getMoeder())) {
      redenMoeder = "De moeder heeft de Nederlandse nationaliteit";
    } else if (heeftAsiel(impl.getMoeder())) {
      redenMoeder = "De moeder heeft verblijfsvergunning asiel";
    }

    if (impl.isBestaandKind()) {
      if (heeftNederlandseNationaliteit(impl.getKinderen())) {
        redenKind = "Het kind heeft de Nederlandse nationaliteit";
      }
    }

    setNederlandsRecht(redenMoeder, redenKind);
  }

  private void setNederlandsRecht(String redenMoeder, String redenKind) {

    InfoLayout info = new InfoLayout("", "");

    DossierErkenning impl = getZaakDossier();

    if (fil(redenMoeder) && !impl.isToestemmingsRechtBepaald()) {
      form2.getField(Page25ErkenningBean2.RECHT_MOEDER).setValue(Landelijk.getNederland());
    }

    if (fil(redenKind) && !impl.isToestemmingsRechtBepaald()) {
      form2.getField(Page25ErkenningBean2.RECHT_KIND).setValue(Landelijk.getNederland());
    }

    if (fil(redenMoeder)) {
      info.append(
          fil(redenMoeder) ? redenMoeder + (". Hierdoor is het recht van Nederland van toepassing.") : "");
    } else {
      info.append("Leidt zelf af van welk land het recht op de toestemming van de moeder van toepassing is.");
    }

    if (fil(redenKind)) {
      info.appendLine(
          fil(redenKind) ? (redenKind + ". Hierdoor is het recht van Nederland van toepassing.") : "");
    } else if (form2.getBean().getToestemminggeverType() != ToestemminggeverType.MOEDER) {
      info.appendLine("Leidt zelf af van welk land het recht op de toestemming van het kind van toepassing is.");
    }

    addComponent(info, getComponentIndex(form2));
  }
}
