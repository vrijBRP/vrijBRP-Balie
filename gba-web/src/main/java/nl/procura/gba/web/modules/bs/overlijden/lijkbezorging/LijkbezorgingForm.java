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

    updateWijzeLijkbezorging();
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

  private boolean isBegraven() {
    return WijzeLijkbezorging.BEGRAVING_CREMATIE.equals(getField(WIJZE_LIJKBEZORGING).getValue());
  }

  private boolean isBuitenBenelux() {
    return (boolean) getField(BUITEN_BENELUX).getValue();
  }

  private TermijnLijkbezorging getTermijn() {
    return (TermijnLijkbezorging) getField(TERMIJN_LIJKBEZORGING).getValue();
  }

  private void updateWijzeLijkbezorging() {
    if (isBegraven()) {
      getField(PLAATS_ONTLEDING).setVisible(false);
      getField(PLAATS_ONTLEDING).setValue(null);
      getField(DATUM_LIJKBEZORGING).setVisible(true);
      getField(TIJD_LIJKBEZORGING).setVisible(true);
      getField(TERMIJN_LIJKBEZORGING).setVisible(true);

    } else {
      getField(PLAATS_ONTLEDING).setVisible(true);
      getField(DATUM_LIJKBEZORGING).setValue(null);
      getField(DATUM_LIJKBEZORGING).setVisible(false);
      getField(TIJD_LIJKBEZORGING).setValue(null);
      getField(TIJD_LIJKBEZORGING).setVisible(false);
      getField(TERMIJN_LIJKBEZORGING).setValue(TermijnLijkbezorging.ONBEKEND);
      getField(TERMIJN_LIJKBEZORGING).setVisible(false);
      getField(ONTVANGEN_DOCUMENT_LIJKBEZORGING).setValue(OntvangenDocument.ONBEKEND);
      getField(ONTVANGEN_DOCUMENT_LIJKBEZORGING).setVisible(false);
    }

    setBuitenBenelux();
    repaint();
  }

  private void setBuitenBenelux() {
    boolean buitenBenelux = isBuitenBenelux();
    getField(LAND_BESTEMMING).setVisible(buitenBenelux);
    getField(PLAATS_BESTEMMING).setVisible(buitenBenelux);
    getField(VIA).setVisible(buitenBenelux);
    getField(DOODSOORZAAK).setVisible(buitenBenelux);
    getField(VERVOERMIDDEL).setVisible(buitenBenelux);
    onDatumsWijziging();
    repaint();
  }

  /**
   * Bepaal of de termijn binnen of na 36 uur is
   */
  public void onDatumsWijziging() {
    getField(TERMIJN_LIJKBEZORGING).setVisible(false);
    getField(ONTVANGEN_DOCUMENT_LIJKBEZORGING).setVisible(false);
    List<Calendar> calendars = getFormCalendars();
    if (calendars.size() > 1) {
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
    repaint();
  }

  private void setTermijnLijkbezorging(TermijnLijkbezorging termijnLijkbezorging) {
    getField(TERMIJN_LIJKBEZORGING).setVisible(isBegraven() && !isBuitenBenelux());
    getField(TERMIJN_LIJKBEZORGING).setValue(termijnLijkbezorging);
    updateVeldDocumentOntvangen();
  }

  private void updateVeldDocumentOntvangen() {
    if (isBegraven() && !isBuitenBenelux() && getTermijn() != null) {
      GbaNativeSelect select = (GbaNativeSelect) getField(ONTVANGEN_DOCUMENT_LIJKBEZORGING);
      select.setDataSource(new OntvangenDocumentContainer(getTermijn()));
      select.setVisible(select.getContainerDataSource().size() > 0);
      select.setValue(zaakDossier.getOntvangenDocumentLijkbezorging());
    }
    repaint();
  }

  private void addListener(Field field) {
    if (field != null) {
      field.addListener((ValueChangeListener) event -> {
        if (event.getProperty() == getField(WIJZE_LIJKBEZORGING)) {
          updateWijzeLijkbezorging();
        } else if (event.getProperty() == getField(BUITEN_BENELUX)) {
          setBuitenBenelux();
        } else if (event.getProperty() == getField(TERMIJN_LIJKBEZORGING)) {
          updateVeldDocumentOntvangen();
        } else {
          onDatumWijziging();
        }
      });
    }
  }

  // Override please
  protected void onDatumWijziging() {
  }
}
