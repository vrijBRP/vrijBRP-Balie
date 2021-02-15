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

package nl.procura.gba.web.modules.bs.omzetting.page20;

import static nl.procura.gba.web.modules.bs.omzetting.page20.Page20OmzettingBean1.*;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.pos;

import com.vaadin.data.Property.ValueChangeListener;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.DateTime.TimeType;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;
import nl.procura.gba.web.services.bs.omzetting.StatusVerbintenis;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatie;
import nl.procura.vaadin.component.field.fieldvalues.TimeFieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page20Omzetting extends BsPage<DossierOmzetting> {

  private Form1 form1 = null;

  public Page20Omzetting() {

    super("Omzetting GPS in huwelijk - planning");

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public boolean checkPage() {

    form1.commit();

    if (form1.getHuwelijksLocatie() == null) {
      form1.setHuwelijksLocatie(HuwelijksLocatie.getDefault());
    }

    getZaakDossier().setDatumVerbintenis(new DateTime(form1.getBean().getDatumVerbintenis()));
    getZaakDossier().setTijdVerbintenis(
        new DateTime(0, along(form1.getBean().getTijdVerbintenis().getValue()), TimeType.TIME_4_DIGITS));
    getZaakDossier().setStatusVerbintenis(form1.getBean().getStatusVerbintenis());
    getZaakDossier().setEinddatumStatus(new DateTime(form1.getBean().getDatumStatusTot()));
    getZaakDossier().setHuwelijksLocatie(form1.getHuwelijksLocatie());
    getZaakDossier().setToelichtingVerbintenis(form1.getBean().getToelichtingCeremonie());

    // Een ID opvragen in het Zaak-DMS
    getApplication().getServices().getZaakIdentificatieService().getDmsZaakId(getDossier());

    getProcessen().updateStatus();

    getApplication().getServices().getOmzettingService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    try {
      if (event.isEvent(InitPage.class)) {

        addComponent(new BsStatusForm(getDossier()));

        setInfo(
            "Plan de gebeurtenissen of bewerk deze. Maak indien van toepassing een optie definitief en bevestig deze. "
                + "Druk op Volgende (F2) om verder te gaan.");

        //              OptieLayout          ol   = new OptieLayout ();

        Page20OmzettingBean1 bean = new Page20OmzettingBean1();

        bean.setDatumVerbintenis(getZaakDossier().getDatumVerbintenis().getDate());
        bean.setStatusVerbintenis(getZaakDossier().getStatusVerbintenis());

        DateTime tijd = getZaakDossier().getTijdVerbintenis();

        if (pos(tijd.getLongTime())) {
          bean.setTijdVerbintenis(new TimeFieldValue(tijd.getFormatTime()));
        }

        bean.setDatumStatusTot(getZaakDossier().getEinddatumStatus().getDate());
        bean.setToelichtingCeremonie(getZaakDossier().getToelichtingVerbintenis());

        form1 = new Form1();
        form1.setBean(bean);

        form1.setHuwelijksLocatie(getZaakDossier().getHuwelijksLocatie());

        form1.getField(STATUSVERBINTENIS).addListener((ValueChangeListener) event1 -> {

          Object value = event1.getProperty().getValue();

          checkStatus((StatusVerbintenis) value);
        });

        checkStatus(form1.getBean().getStatusVerbintenis());
        addComponent(form1);
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

  private void checkStatus(StatusVerbintenis status) {

    form1.getField(DATUMSTATUSTOT).setVisible(status == StatusVerbintenis.OPTIE);
    form1.repaint();
  }

  public class Form1 extends Page20OmzettingForm1 {

    @Override
    public void init() {

      setCaption("Sluiting verbintenis");
      setOrder(LOCATIEVERBINTENIS, DATUMVERBINTENIS, TIJDVERBINTENIS, STATUSVERBINTENIS, DATUMSTATUSTOT,
          TOELICHTINGCEREMONIE);
      setColumnWidths("150px", "");
    }
  }
}
