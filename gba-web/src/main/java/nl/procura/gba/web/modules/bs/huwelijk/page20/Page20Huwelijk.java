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

package nl.procura.gba.web.modules.bs.huwelijk.page20;

import static nl.procura.gba.web.modules.bs.huwelijk.page20.Page20HuwelijkBean1.*;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.DateTime.TimeType;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.bs.huwelijk.StatusVerbintenis;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatie;
import nl.procura.standard.ProcuraDate;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.field.fieldvalues.TimeFieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page20Huwelijk extends BsPage<DossierHuwelijk> {

  private final Button buttonAgenda      = new Button("Locatieagenda (F3)");
  private final Button buttonBevestiging = new Button("Bevestiging (F4)");

  private Form1 form1 = null;
  private Form2 form2 = null;
  private Form3 form3 = null;

  private boolean daysConfirm = false;

  public Page20Huwelijk() {

    super("Huwelijk/GPS - planning");

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public boolean checkPage() {

    form1.commit();
    form2.commit();
    form3.commit();

    // Form 1
    getZaakDossier().setSoortVerbintenis(form1.getBean().getSoort());

    // Form 2
    getZaakDossier().setDatumVoornemen(new DateTime(form2.getBean().getDatumVoornemen()));

    // Huwelijkslocatie is niet meer verplicht
    if (form3.getHuwelijksLocatie() == null) {
      form3.setHuwelijksLocatie(HuwelijksLocatie.getDefault());
    }

    // Form 3
    getZaakDossier().setDatumVerbintenis(new DateTime(form3.getBean().getDatumVerbintenis()));
    getZaakDossier().setTijdVerbintenis(
        new DateTime(0, along(form3.getBean().getTijdVerbintenis().getValue()), TimeType.TIME_4_DIGITS));
    getZaakDossier().setStatusVerbintenis(form3.getBean().getStatusVerbintenis());
    getZaakDossier().setEinddatumStatus(new DateTime(form3.getBean().getDatumStatusTot()));
    getZaakDossier().setHuwelijksLocatie(form3.getHuwelijksLocatie());
    getZaakDossier().setToelichtingVerbintenis(form3.getBean().getToelichtingCeremonie());

    long dVoornemen = getZaakDossier().getDatumVoornemen().getLongDate();
    long dVerbind = getZaakDossier().getDatumVerbintenis().getLongDate();

    if (!checkVoornemen(dVoornemen, dVerbind)) {
      return false;
    }

    // Een ID opvragen in het Zaak-DMS
    getApplication().getServices().getZaakIdentificatieService().getDmsZaakId(getDossier());

    getProcessen().updateStatus();

    getApplication().getServices().getHuwelijkService().save(getDossier());

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

        OptieLayout ol = new OptieLayout();

        Page20HuwelijkBean1 bean = new Page20HuwelijkBean1();

        SoortVerbintenis soort = getZaakDossier().getSoortVerbintenis();
        bean.setSoort(soort == SoortVerbintenis.ONBEKEND ? SoortVerbintenis.HUWELIJK : soort);

        bean.setDatumVoornemen(getZaakDossier().getDatumVoornemen().getDate());
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

        form2 = new Form2();
        form2.setBean(bean);

        form3 = new Form3();
        form3.setBean(bean);

        form3.setHuwelijksLocatie(getZaakDossier().getHuwelijksLocatie());

        form3.getField(STATUSVERBINTENIS).addListener((ValueChangeListener) event1 -> {

          Object value = event1.getProperty().getValue();

          checkStatus((StatusVerbintenis) value);
        });

        checkStatus(form3.getBean().getStatusVerbintenis());

        ol.getLeft().addComponent(form1);
        ol.getLeft().addComponent(form2);
        ol.getLeft().addComponent(form3);

        //        ol.getRight().addButton(buttonAgenda, this);
        //        ol.getRight().addButton(buttonBevestiging, this);
        //        ol.getRight().setWidth("150px");
        //        ol.getRight().setCaption("Opties");

        addComponent(ol);
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

    form3.getField(DATUMSTATUSTOT).setVisible(status == StatusVerbintenis.OPTIE);
    form3.repaint();
  }

  private boolean checkVoornemen(long dVoornemen, long dVerbind) {

    if (dVoornemen > 0) {

      int diff = new ProcuraDate(astr(dVoornemen)).diffInDays(astr(dVerbind));

      if (diff <= 0) {
        throw new ProException(WARNING, "De datum verbintenis kan niet op of vóór de datum voornemen liggen");
      }

      if (diff > 365) {
        throw new ProException(WARNING,
            "Er zit meer dan een jaar tussen de datum voornemen en de datum verbintenis");
      }

      if (!daysConfirm && (diff > 0 && diff < 14)) {

        getWindow().addWindow(new ConfirmDialog(
            "Er zit minder dan 14 dagen tussen de datum voornemen en datum verbintenis.<hr/>Is dit correct?") {

          @Override
          public void buttonYes() {

            closeWindow();

            daysConfirm = true;

            onNextPage();
          }
        });

        return false;
      }
    }

    return true;
  }

  public class Form1 extends Page20HuwelijkForm1 {

    @Override
    public void init() {

      setCaption("Soort verbintenis");
      setOrder(SOORT);
      setColumnWidths("150px", "");
    }
  }

  public class Form2 extends Page20HuwelijkForm1 {

    @Override
    public void init() {

      setCaption("Voornemen");
      setOrder(WIJZEVOORNEMEN, DATUMVOORNEMEN);
      setColumnWidths("150px", "");
    }
  }

  public class Form3 extends Page20HuwelijkForm1 {

    @Override
    public void init() {

      setCaption("Sluiting verbintenis");
      setOrder(LOCATIEVERBINTENIS, DATUMVERBINTENIS, TIJDVERBINTENIS, STATUSVERBINTENIS, DATUMSTATUSTOT,
          TOELICHTINGCEREMONIE);
      setColumnWidths("150px", "");
    }
  }
}
