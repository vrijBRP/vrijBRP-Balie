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

package nl.procura.gba.web.modules.bs.overlijden.lijkbezorging;

import static nl.procura.gba.web.common.misc.GbaDatumUtils.*;
import static nl.procura.gba.web.modules.bs.overlijden.lijkbezorging.LijkbezorgingBean.*;
import static nl.procura.standard.Globalfunctions.along;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.vaadin.ui.Field;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.DateTime.TimeType;
import nl.procura.gba.web.common.misc.GbaDatumUtils;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.enums.Doodsoorzaak;
import nl.procura.gba.web.services.bs.algemeen.enums.OntvangenDocument;
import nl.procura.gba.web.services.bs.algemeen.enums.TermijnLijkbezorging;
import nl.procura.gba.web.services.bs.algemeen.enums.WijzeLijkbezorging;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijdenLijkbezorging;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.field.fieldvalues.TimeFieldValue;

public abstract class LijkbezorgingForm extends GbaForm<LijkbezorgingBean> {

  private final DossierOverlijdenLijkbezorging zaakDossier;

  public LijkbezorgingForm(DossierOverlijdenLijkbezorging zaakDossier) {
    this.zaakDossier = zaakDossier;

    setColumnWidths("170px", "");
    setReadThrough(true);
    setCaptionAndOrder();

    update();
  }

  @Override
  public void setBean(Object bean) {

    super.setBean(bean);

    addListener(getField(WIJZE_LIJKBEZORGING));
    addListener(getField(TERMIJN_LIJKBEZORGING));
    addListener(getField(BUITEN_BENELUX));
    addListener(getField(DATUM_LIJKBEZORGING));
    addListener(getField(TIJD_LIJKBEZORGING));

    toonVeldenWijzeLijkbezorging();
    toonVeldenTermijnLijkbezorging();
    toonVeldenBuitenBenelux();
  }

  /**
   * Eindtijdstip voor berekening periode
   */
  public Calendar getEindeTermijnTijdstip() {
    Date date = (Date) getField(DATUM_LIJKBEZORGING).getValue();
    TimeFieldValue time = (TimeFieldValue) getField(TIJD_LIJKBEZORGING).getValue();
    return GbaDatumUtils.dateTimeFieldtoCalendar(date, time, "000000");
  }

  /**
   * De 2 termijn van overlijden en lijkbezorging
   */
  public abstract List<Calendar> getFormCalendars();

  @Override
  public LijkbezorgingBean getNewBean() {
    return new LijkbezorgingBean();
  }

  public void setCaptionAndOrder() {

    setCaption("Lijkbezorging");
    setOrder(WIJZE_LIJKBEZORGING, DATUM_LIJKBEZORGING, TIJD_LIJKBEZORGING, PLAATS_ONTLEDING, BUITEN_BENELUX,
        DOODSOORZAAK, LAND_BESTEMMING, PLAATS_BESTEMMING, VIA, VERVOERMIDDEL, TERMIJN_LIJKBEZORGING,
        ONTVANGEN_DOCUMENT_LIJKBEZORGING);
  }

  public void toonVeldenBuitenBenelux() {

    Field veld = getField(BUITEN_BENELUX);

    if (veld != null) {
      boolean buitenBenelux = (Boolean) veld.getValue();
      getField(LAND_BESTEMMING).setVisible(buitenBenelux);
      getField(PLAATS_BESTEMMING).setVisible(buitenBenelux);
      getField(VIA).setVisible(buitenBenelux);
      getField(DOODSOORZAAK).setVisible(buitenBenelux);
      getField(VERVOERMIDDEL).setVisible(buitenBenelux);

      ProNativeSelect termijn = getField(TERMIJN_LIJKBEZORGING, ProNativeSelect.class);
      ProNativeSelect document = getField(ONTVANGEN_DOCUMENT_LIJKBEZORGING, ProNativeSelect.class);

      termijn.setVisible(!buitenBenelux);
      document.setVisible(!buitenBenelux && !document.getContainerDataSource().getItemIds().isEmpty());
    }

    repaint();
  }

  public void toonVeldenTermijnLijkbezorging() {

    Field veld = getField(TERMIJN_LIJKBEZORGING);

    if (veld != null) {
      TermijnLijkbezorging fv = (TermijnLijkbezorging) veld.getValue();

      if (fv != null) {
        GbaNativeSelect select = (GbaNativeSelect) getField(ONTVANGEN_DOCUMENT_LIJKBEZORGING);
        select.setDataSource(new OntvangenDocumentContainer(fv));
        select.setVisible(select.getContainerDataSource().size() > 0);
        select.setValue(zaakDossier.getOntvangenDocumentLijkbezorging());
      }
    }

    repaint();
  }

  public void toonVeldenWijzeLijkbezorging() {

    Field veld = getField(WIJZE_LIJKBEZORGING);

    if (veld != null) {
      WijzeLijkbezorging fv = (WijzeLijkbezorging) veld.getValue();

      if (fv != null) {
        if (fv == WijzeLijkbezorging.ONTLEDING) {
          // Datum
          getField(DATUM_LIJKBEZORGING).setValue(null);
          getField(DATUM_LIJKBEZORGING).setVisible(false);

          // Tijd
          getField(TIJD_LIJKBEZORGING).setValue(null);
          getField(TIJD_LIJKBEZORGING).setVisible(false);

          // Termijn
          getField(TERMIJN_LIJKBEZORGING).setValue(TermijnLijkbezorging.ONBEKEND);
          getField(TERMIJN_LIJKBEZORGING).setVisible(false);

          getField(PLAATS_ONTLEDING).setVisible(true);
        } else {
          getField(DATUM_LIJKBEZORGING).setVisible(true);
          getField(TIJD_LIJKBEZORGING).setVisible(true);
          getField(TERMIJN_LIJKBEZORGING).setVisible(true);
          getField(PLAATS_ONTLEDING).setVisible(false);
          getField(PLAATS_ONTLEDING).setValue(null);
        }
      }
    }

    repaint();
  }

  public void update() {

    LijkbezorgingBean bean = new LijkbezorgingBean();

    // Lijkbezorging
    bean.setWijzeLijkBezorging(zaakDossier.getWijzeLijkBezorging());
    bean.setDatumLijkbezorging(zaakDossier.getDatumLijkbezorging().getDate());

    DateTime tijdLijkbezorging = zaakDossier.getTijdLijkbezorging();
    if (tijdLijkbezorging.getLongTime() >= 0) {
      bean.setTijdLijkbezorging(new TimeFieldValue(tijdLijkbezorging.getFormatTime()));
    }

    // Overige
    bean.setPlaatsOntleding(zaakDossier.getPlaatsOntleding());
    bean.setBuitenBenelux(zaakDossier.isBuitenBenelux());
    bean.setLandBestemming(zaakDossier.getLandBestemming());
    bean.setPlaatsBestemming(zaakDossier.getPlaatsBestemming());
    bean.setVia(zaakDossier.getViaBestemming());
    bean.setDoodsoorzaak(zaakDossier.getDoodsoorzaak());
    bean.setVervoermiddel(zaakDossier.getVervoermiddel());
    bean.setTermijnLijkbezorging(zaakDossier.getTermijnLijkbezorging());
    bean.setOntvangenDocumentLijkbezorging(zaakDossier.getOntvangenDocumentLijkbezorging());

    setBean(bean);
  }

  /**
   * Bepaal of de termijn binnen of na 36 uur is
   */
  public void updateTermijnLijkbezorging() {

    List<Calendar> calendars = getFormCalendars();
    Calendar beginTijdstip = calendars.get(0);
    Calendar eindTijdstip = calendars.get(1);

    if (beginTijdstip != null && eindTijdstip != null) {
      if (isMinimaalAantalWerkdagen(toEindeDag(beginTijdstip), toBeginDag(eindTijdstip), 6)) {
        setTermijnLijkbezorging(TermijnLijkbezorging.MEER_DAN_6_WERKDAGEN);

      } else if (isMinimaalAantalUur(beginTijdstip, eindTijdstip, 36)) {
        setTermijnLijkbezorging(TermijnLijkbezorging.MEER_DAN_36_UUR);

      } else {
        setTermijnLijkbezorging(TermijnLijkbezorging.MINDER_DAN_36_UUR);
      }
    }
  }

  public void updateZaakDossier(DossierOverlijdenLijkbezorging zaakDossier) {

    zaakDossier.setWijzeLijkBezorging(getBean().getWijzeLijkBezorging());
    zaakDossier.setDatumLijkbezorging(new DateTime(getBean().getDatumLijkbezorging()));
    zaakDossier.setTijdLijkbezorging(
        new DateTime(0, along(getBean().getTijdLijkbezorging().getValue()), TimeType.TIME_4_DIGITS));
    zaakDossier.setBuitenBenelux(getBean().isBuitenBenelux());
    zaakDossier.setPlaatsOntleding(getBean().getPlaatsOntleding());
    zaakDossier.setTermijnLijkbezorging(getBean().getTermijnLijkbezorging());

    if (TermijnLijkbezorging.MEER_DAN_36_UUR.equals(getBean().getTermijnLijkbezorging())) {
      zaakDossier.setOntvangenDocumentLijkbezorging(OntvangenDocument.ONBEKEND);
    } else {
      zaakDossier.setOntvangenDocumentLijkbezorging(getBean().getOntvangenDocumentLijkbezorging());
    }

    if (getBean().isBuitenBenelux()) {
      zaakDossier.setLandBestemming(getBean().getLandBestemming());
      zaakDossier.setPlaatsBestemming(getBean().getPlaatsBestemming());
      zaakDossier.setViaBestemming(getBean().getVia());
      zaakDossier.setVervoermiddel(getBean().getVervoermiddel());
      zaakDossier.setDoodsoorzaak(getBean().getDoodsoorzaak());
      zaakDossier.setTermijnLijkbezorging(TermijnLijkbezorging.ONBEKEND);
      zaakDossier.setOntvangenDocumentLijkbezorging(OntvangenDocument.ONBEKEND);
    } else {
      zaakDossier.setLandBestemming(new FieldValue());
      zaakDossier.setPlaatsBestemming("");
      zaakDossier.setViaBestemming("");
      zaakDossier.setVervoermiddel("");
      zaakDossier.setDoodsoorzaak(Doodsoorzaak.ONBEKEND);
    }
  }

  // Override please
  protected void onDatumWijziging() {
  }

  private void addListener(Field field) {

    if (field != null) {
      field.addListener((ValueChangeListener) event -> {
        if (event.getProperty() == getField(WIJZE_LIJKBEZORGING)) {
          toonVeldenWijzeLijkbezorging();
        } else if (event.getProperty() == getField(TERMIJN_LIJKBEZORGING)) {
          toonVeldenTermijnLijkbezorging();
        } else if (event.getProperty() == getField(BUITEN_BENELUX)) {
          toonVeldenBuitenBenelux();
          updateTermijnLijkbezorging();
        } else {
          onDatumWijziging();
        }
      });
    }
  }

  private void setTermijnLijkbezorging(TermijnLijkbezorging termijnLijkbezorging) {

    if (!termijnLijkbezorging.equals(getField(TERMIJN_LIJKBEZORGING).getValue())) {
      getField(TERMIJN_LIJKBEZORGING).setValue(termijnLijkbezorging);
      getBean().setTermijnLijkbezorging(termijnLijkbezorging);
      toonVeldenTermijnLijkbezorging();
    }
  }
}
