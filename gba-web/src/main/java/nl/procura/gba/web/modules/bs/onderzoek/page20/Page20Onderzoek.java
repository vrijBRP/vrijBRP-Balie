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

package nl.procura.gba.web.modules.bs.onderzoek.page20;

import static nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus.DISABLED;
import static nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus.EMPTY;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.modules.bs.common.BsProces;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.onderzoek.BsPageOnderzoek;
import nl.procura.gba.web.modules.bs.onderzoek.page30.Page30Onderzoek;
import nl.procura.gba.web.services.bs.onderzoek.enums.AanduidingOnderzoekType;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Onderzoek - beoordeling
 */
public class Page20Onderzoek extends BsPageOnderzoek {

  private Page20OnderzoekForm1 form1;
  private Page20OnderzoekForm2 form2;
  private Page20OnderzoekForm3 form3;
  private final VLayout        onderzoekLayout = new VLayout();

  public Page20Onderzoek() {
    super("Onderzoek - beoordeling");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonNext);

      form1 = new Page20OnderzoekForm1(getZaakDossier()) {

        @Override
        protected void onChangeTermijn(Boolean binnen) {
          setTermijn(binnen);
        }
      };

      addComponent(new BsStatusForm(getDossier()));
      setInfo("Beoordeel of de melding binnen 5 werkdagen kan worden afgedaan en geef aan waarom (niet).</br> " +
          "Als dit niet kan, start het onderzoek door op Volgende (F2) te drukken en verder te gaan.</br> " +
          "Vergeet niet als dit van toepassing is om de TMV bij te werken!");

      addComponent(form1);
      addComponent(onderzoekLayout);
      setTermijn(form1.getBean().getAfhandelingstermijn());
    }

    super.event(event);
  }

  private void setTermijn(Boolean binnen) {
    form2 = null;
    form3 = null;
    onderzoekLayout.removeAllComponents();

    if (binnen != null && !binnen) {
      form2 = new Page20OnderzoekForm2(getZaakDossier());
      form3 = new Page20OnderzoekForm3(getZaakDossier());
      onderzoekLayout.addComponent(form2);
      onderzoekLayout.addComponent(form3);
    }
  }

  @Override
  public boolean checkPage() {

    if (super.checkPage()) {

      form1.commit();

      getZaakDossier().setDatumEindeTermijn(new DateTime(form1.getBean().getDatumEinde()));
      getZaakDossier().setBinnenTermijn(form1.getBean().getAfhandelingstermijn());
      getZaakDossier().setRedenTermijn(form1.getBean().getReden());
      getZaakDossier().setDatumAanvangOnderzoek(null);
      getZaakDossier().setAanduidingGegevensOnderzoek(AanduidingOnderzoekType.ONBEKEND);
      getZaakDossier().setGedegenOnderzoek(null);
      getZaakDossier().setRedenOverslaan(null);
      getZaakDossier().setToelichtingOverslaan(null);

      if (form2 != null) {
        form2.commit();
        getZaakDossier().setDatumAanvangOnderzoek(new DateTime(form2.getBean().getDatumAanvangOnderzoek()));
        getZaakDossier()
            .setAanduidingGegevensOnderzoek(AanduidingOnderzoekType.get(form2.getBean().getAanduidingGegevens()));
      }

      if (form3 != null) {
        form3.commit();
        getZaakDossier().setGedegenOnderzoek(form3.getBean().getOnderzoekDoorAnderGedaan());
        getZaakDossier().setRedenOverslaan(form3.getBean().getVoldoendeReden());
        getZaakDossier().setToelichtingOverslaan(form3.getBean().getToelichting());
      }

      getServices().getOnderzoekService().save(getDossier());

      return true;
    }

    return false;
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  @Override
  public void onNextPage() {

    form1.commit();
    boolean binnenTermijn = form1.getBean().getAfhandelingstermijn();
    BsProces uitbreiding = getProcessen().getProces(Page30Onderzoek.class);
    uitbreiding.forceStatus(binnenTermijn ? DISABLED : EMPTY);

    goToNextProces();
  }
}
