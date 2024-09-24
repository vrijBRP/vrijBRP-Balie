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

package nl.procura.gba.web.modules.bs.overlijden.levenloos.page20;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.DateTime.TimeType;
import nl.procura.gba.web.modules.bs.geboorte.page20.Page20Geboorte;
import nl.procura.gba.web.modules.bs.overlijden.lijkbezorging.LijkbezorgingForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.levenloos.DossierLevenloos;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Levenloos
 */
public class Page20Levenloos extends Page20Geboorte<DossierLevenloos> {

  private LijkbezorgingForm form = null;

  public Page20Levenloos() {
    super("Levenloos geboren kind - aangifte");
  }

  @Override
  public boolean checkPage() {

    super.checkPage();

    checkDatumVelden();

    form.commit();

    // FormLijkbezorging
    form.updateZaakDossier(getZaakDossier());

    getServices().getLevenloosService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    super.event(event);

    if (event.isEvent(InitPage.class)) {

      form = new LijkbezorgingForm(getZaakDossier()) {

        @Override
        public List<Calendar> getFormCalendars() {
          if (form != null) {
            return asList(getAanvangTermijnTijdstip(), form.getEindeTermijnTijdstip());
          }
          return new ArrayList<>();
        }

        @Override
        protected void onDatumWijziging() {
          onDatumsWijziging();
        }
      };

      addComponent(form);
    }
  }

  public Calendar getAanvangTermijnTijdstip() {

    Calendar calendar = Calendar.getInstance();

    for (DossierPersoon kind : getZaakDossier().getKinderen()) {

      long datum = kind.getDatumGeboorte().getLongDate();
      long tijd = kind.getTijdGeboorte().getLongDateTime();
      calendar.setTime(new DateTime(datum, tijd, TimeType.TIME_6_DIGITS).getDate());
    }

    return calendar;
  }

  @Override
  protected boolean isToonTermijnLayout() {
    return false;
  }

  private void checkDatumVelden() {

    List<Calendar> calendars = form.getFormCalendars();

    // Datum overlijden niet in toekomst!
    if (calendars.get(0) != null) {
      // Datum overlijden niet later dan datum lijkbezorging!
      if (calendars.get(1) != null) {
        if (calendars.get(0).after(calendars.get(1))) {
          throw new ProException(ProExceptionSeverity.WARNING,
              "Het tijdstip van lijkbezorging kan niet eerder plaatsvinden dan het tijdstip van geboorte");
        }
      }
    }
  }
}
