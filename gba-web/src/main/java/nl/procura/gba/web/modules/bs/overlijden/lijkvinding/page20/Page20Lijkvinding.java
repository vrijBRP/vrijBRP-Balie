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

package nl.procura.gba.web.modules.bs.overlijden.lijkvinding.page20;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.components.containers.Container.PLAATS;
import static nl.procura.gba.web.modules.bs.overlijden.lijkvinding.page20.Page20LijkvindingBean.*;
import static nl.procura.standard.Globalfunctions.along;

import java.util.*;

import com.vaadin.ui.Field;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.DateTime.TimeType;
import nl.procura.gba.web.common.misc.GbaDatumUtils;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.overlijden.lijkbezorging.LijkbezorgingForm;
import nl.procura.gba.web.modules.bs.overlijden.lijkvinding.BsPageLijkvinding;
import nl.procura.gba.web.services.bs.algemeen.enums.OntvangenDocument;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.field.fieldvalues.TimeFieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

/**

 * <p>
 * 6 Feb. 2013 8:00:00
 */
public class Page20Lijkvinding extends BsPageLijkvinding {

  private Form1 form1     = null;
  private Form2 form2     = null;
  private Form3 formLijkb = null;

  public Page20Lijkvinding() {

    super("Lijkvinding - aangifte");
  }

  @Override
  public boolean checkPage() {

    checkDatumVelden();

    form1.commit();
    form2.commit();
    formLijkb.commit();

    // Aangever
    getZaakDossier().setSchriftelijkeAangever(form1.getBean().getSchriftelijkeAangever());

    // Aangifte
    getZaakDossier().getAangever().setGeslachtsnaam(getZaakDossier().getSchriftelijkeAangever().getOms());
    getZaakDossier().setPlaatsLijkvinding(form2.getBean().getPlaatsLijkvinding());
    getZaakDossier().setPlaatsToevoeging(form2.getBean().getPlaatsToevoeging());
    getZaakDossier().setDatumLijkvinding(new DateTime(form2.getBean().getDatumLijkvinding()));
    getZaakDossier().setTijdLijkvinding(
        new DateTime(0, along(form2.getBean().getTijdLijkvinding().getValue()), TimeType.TIME_4_DIGITS));
    getZaakDossier().setOntvangenDocument(form2.getBean().getOntvangenDocument());

    // Lijkbezorging
    formLijkb.updateZaakDossier(getZaakDossier());

    getServices().getOverlijdenGemeenteService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      getZaakDossier().setPlaatsLijkvinding(getPlaats());

      buttonPrev.setEnabled(false);

      form1 = new Form1();
      form2 = new Form2();
      formLijkb = new Form3();

      form1.update();
      form2.update();
      formLijkb.update();

      addButton(buttonPrev);
      addButton(buttonNext);

      addComponent(new BsStatusForm(getDossier()));

      setInfo("Vul de aangiftegegevens in en druk op Volgende (F2) om verder te gaan.");

      addComponent(form1);
      addComponent(form2);
      addComponent(formLijkb);
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  private void checkDatumVelden() {

    Date date1 = (Date) form2.getField(DATUM_LIJKVINDING).getValue();
    TimeFieldValue time1 = (TimeFieldValue) form2.getField(TIJD_LIJKVINDING).getValue();
    Calendar calendar = GbaDatumUtils.dateTimeFieldtoCalendar(date1, time1, "235900");

    if (calendar != null) {
      // Datum lijkvinding niet in toekomst!
      if (calendar.after(new GregorianCalendar())) {
        throw new ProException(ProExceptionSeverity.WARNING,
            "De datum en tijd van lijkvinding kan niet in de toekomst liggen");
      }
    }
  }

  private FieldValue getPlaats() {
    return PLAATS.get(getApplication().getServices().getGebruiker().getGemeenteCode());
  }

  public class Form1 extends Page20LijkvindingForm {

    public Form1() {
      super(getZaakDossier());
      update();
    }

    @Override
    public void setCaptionAndOrder() {
      setCaption("Aangever");
      setOrder(SCHRIFTELIJKE_AANGEVER);
    }

    @Override
    public void update() {
      Page20LijkvindingBean bean = getBean();
      bean.setSchriftelijkeAangever(getZaakDossier().getSchriftelijkeAangever());
      setBean(bean);
    }
  }

  public class Form2 extends Page20LijkvindingForm {

    public Form2() {
      super(getZaakDossier());
      update();
    }

    @Override
    public void afterSetColumn(Column column, Field field, Property property) {

      if (property.is(ONTVANGEN_DOCUMENT)) {
        column.addText(" en proces-verbaal (ook verplicht)");
      }
    }

    @Override
    public void setCaptionAndOrder() {
      setCaption("Aangifte");
      setOrder(PLAATS_LIJKVINDING, PLAATS_TOEVOEGING, DATUM_LIJKVINDING, TIJD_LIJKVINDING, ONTVANGEN_DOCUMENT);
    }

    @Override
    public void update() {

      Page20LijkvindingBean bean = getBean();
      bean.setPlaatsLijkvinding(getPlaats());
      bean.setPlaatsToevoeging(getZaakDossier().getPlaatsToevoeging());
      bean.setDatumLijkvinding(getZaakDossier().getDatumLijkvinding().getDate());
      DateTime tijdLijkvinding = getZaakDossier().getTijdLijkvinding();

      if (tijdLijkvinding.getLongTime() >= 0) {
        bean.setTijdLijkvinding(new TimeFieldValue(tijdLijkvinding.getFormatTime()));
      }

      if (getZaakDossier().getOntvangenDocument() != OntvangenDocument.ONBEKEND) {
        bean.setOntvangenDocument(getZaakDossier().getOntvangenDocument());
      }

      setBean(bean);
    }
  }

  public class Form3 extends LijkbezorgingForm {

    public Form3() {
      super(getZaakDossier());
    }

    @Override
    public List<Calendar> getFormCalendars() {
      if (form2 != null && formLijkb != null) {
        return asList(form2.getAanvangTermijnTijdstip(), formLijkb.getEindeTermijnTijdstip());
      }
      return new ArrayList<>();
    }

    @Override
    protected void onDatumWijziging() {
      onDatumsWijziging();
    }
  }
}
