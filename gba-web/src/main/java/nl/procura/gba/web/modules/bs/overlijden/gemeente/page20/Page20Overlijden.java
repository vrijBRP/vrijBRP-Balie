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

package nl.procura.gba.web.modules.bs.overlijden.gemeente.page20;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.components.containers.Container.PLAATS;
import static nl.procura.gba.web.modules.bs.overlijden.gemeente.page20.Page20OverlijdenBean.*;
import static nl.procura.standard.Globalfunctions.along;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.DateTime.TimeType;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.overlijden.gemeente.BsPageOverlijden;
import nl.procura.gba.web.modules.bs.overlijden.gemeente.page1.Page1Overlijden;
import nl.procura.gba.web.modules.bs.overlijden.lijkbezorging.LijkbezorgingForm;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page20Overlijden extends BsPageOverlijden {

  private Page20OverlijdenForm form1     = null;
  private LijkbezorgingForm    formLijkb = null;

  public Page20Overlijden() {

    super("Overlijden - aangifte");
  }

  @Override
  public boolean checkPage() {

    checkDatumVelden();

    form1.commit();
    formLijkb.commit();

    // Form1
    getZaakDossier().setPlaatsOverlijden(form1.getBean().getPlaatsOverlijden());
    getZaakDossier().setDatumOverlijden(new DateTime(form1.getBean().getDatumOverlijden()));
    getZaakDossier().setTijdOverlijden(
        new DateTime(0, along(form1.getBean().getTijdOverlijden().getValue()), TimeType.TIME_4_DIGITS));
    getZaakDossier().setOntvangenDocument(form1.getBean().getOntvangenDocument());

    // FormLijkbezorging
    formLijkb.updateZaakDossier(getZaakDossier());

    getServices().getOverlijdenGemeenteService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      getZaakDossier().setPlaatsOverlijden(getPlaats());

      form1 = new Page20OverlijdenForm(getZaakDossier()) {

        @Override
        public void setCaptionAndOrder() {

          setCaption("Aangifte");
          setOrder(PLAATS_OVERLIJDEN, DATUM_OVERLIJDEN, TIJD_OVERLIJDEN, ONTVANGEN_DOCUMENT);
        }

        @Override
        protected void onDatumWijziging() {
          formLijkb.onDatumsWijziging();
        }
      };

      formLijkb = new LijkbezorgingForm(getZaakDossier()) {

        @Override
        public List<Calendar> getFormCalendars() {
          if (form1 != null && formLijkb != null) {
            return asList(form1.getOverlijdenTijdstip(), formLijkb.getEindeTermijnTijdstip());
          }
          return new ArrayList<>();
        }

        @Override
        protected void onDatumWijziging() {
          onDatumsWijziging();
        }
      };

      addButton(buttonPrev);
      addButton(buttonNext);

      addComponent(new BsStatusForm(getDossier()));

      setInfo("Vul de aangiftegegevens in en druk op Volgende (F2) om verder te gaan.");

      addComponent(form1);
      addComponent(formLijkb);

      formLijkb.onDatumsWijziging();
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPage(Page1Overlijden.class);
  }

  private void checkDatumVelden() {

    List<Calendar> calendars = formLijkb.getFormCalendars();
    if (calendars.size() > 0) {
      // Datum overlijden niet in toekomst!
      if (calendars.get(0) != null) {
        if (calendars.get(0).after(new GregorianCalendar())) {
          throw new ProException(ProExceptionSeverity.WARNING,
              "Het tijdstip van overlijden kan niet in de toekomst liggen");
        }

        // Datum overlijden niet later dan datum lijkbezorging!
        Calendar timeLijkbezorging = calendars.get(1);
        if (timeLijkbezorging != null) {
          if (calendars.get(0).after(timeLijkbezorging)) {
            throw new ProException(ProExceptionSeverity.WARNING,
                "Het tijdstip van lijkbezorging kan niet eerder plaatsvinden dan het tijdstip van overlijden");
          }

          GregorianCalendar now = now(timeLijkbezorging);
          if (now.after(timeLijkbezorging)) {
            throw new ProException(ProExceptionSeverity.WARNING,
                "Het tijdstip van lijkbezorging kan niet in het verleden liggen");
          }
        }
      }
    }
  }

  /**
   * Houdt geen rekening met tijd als tijd in vergelijkende calendar ook ontbreekt
   */
  private GregorianCalendar now(Calendar dateTime) {
    GregorianCalendar now = new GregorianCalendar();
    if (dateTime.get(Calendar.HOUR_OF_DAY) == 0) {
      now.set(Calendar.HOUR_OF_DAY, 0);
      now.set(Calendar.MINUTE, 0);
      now.set(Calendar.SECOND, 0);
      now.set(Calendar.MILLISECOND, 0);
    }
    return now;
  }

  private FieldValue getPlaats() {
    return PLAATS.get(getApplication().getServices().getGebruiker().getGemeenteCode());
  }
}
