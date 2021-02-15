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

package nl.procura.gba.web.modules.bs.ontbinding.page30;

import java.util.Date;

import com.vaadin.ui.Component;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.services.bs.algemeen.enums.RechtbankLocatie;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;
import nl.procura.gba.web.services.bs.ontbinding.WijzeBeeindigingVerbintenis;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page30Ontbinding extends BsPage<DossierOntbinding> {

  private FormOntvangenDocumentenUitspraak  formOntvangenDocumentenUitspraak  = null;
  private FormWijzeBeeindiging              formWijzeBeeindiging              = null;
  private FormOntvangenDocumentenWederzijds formOntvangenDocumentenWederzijds = null;
  private FormIngang                        formIngang                        = null;
  private AdvocatenkantorenLayout           layoutAdvocaten                   = null;

  public Page30Ontbinding() {
    super("Ontbinding/einde huwelijk/GPS in gemeente - brondocument");

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public boolean checkPage() {

    super.checkPage();

    if (formOntvangenDocumentenUitspraak.isAdded()) {
      formOntvangenDocumentenUitspraak.commit();
    }
    if (formOntvangenDocumentenWederzijds.isAdded()) {
      formOntvangenDocumentenWederzijds.commit();
    }
    if (formWijzeBeeindiging.isAdded()) {
      formWijzeBeeindiging.commit();
    }
    if (formIngang.isAdded()) {
      formIngang.commit();
    }
    if (layoutAdvocaten.isAdded()) {
      layoutAdvocaten.commit();
    }

    Date datumIngang = formIngang.getBean().getDatumIngang();

    if (formOntvangenDocumentenWederzijds.isAdded()) {

      Date datumVerklaring = formOntvangenDocumentenWederzijds.getBean().getDatumVerklaring();
      Date datumOndertekening = formOntvangenDocumentenWederzijds.getBean().getDatumOndertekening();

      if (new org.joda.time.DateTime(datumIngang.getTime()).isBefore(datumVerklaring.getTime())) {
        throw new ProException(ProExceptionSeverity.WARNING,
            "Datum ingang moet op of ná datum verklaring liggen.");
      }

      if (new org.joda.time.DateTime(datumVerklaring.getTime()).isBefore(datumOndertekening.getTime())) {
        throw new ProException(ProExceptionSeverity.WARNING,
            "Datum verklaring moet op of ná datum ondertekening liggen.");
      }
    }

    // Reset waarden in de zaak
    getZaakDossier().setUitspraakDoor(RechtbankLocatie.ONBEKEND);
    getZaakDossier().setDatumUitspraak(new DateTime(-1));
    getZaakDossier().setDatumIngang(new DateTime(-1));
    getZaakDossier().setDatumGewijsde(new DateTime(-1));
    getZaakDossier().setVerzoekTotInschrijvingDoor(new FieldValue());
    getZaakDossier().setDatumVerzoek(new DateTime(-1));
    getZaakDossier().setBinnenTermijn(false);

    getZaakDossier().setDatumVerklaring(new DateTime(-1));
    getZaakDossier().setOndertekendDoor("");
    getZaakDossier().setDatumOndertekening(new DateTime(-1));

    // Advocatenkantoor
    getZaakDossier().setWijzeBeeindigingVerbintenis(WijzeBeeindigingVerbintenis.ONBEKEND);
    getZaakDossier().getAdvocatenkantoor().setNaam("");
    getZaakDossier().getAdvocatenkantoor().setTavAanhef(new FieldValue());
    getZaakDossier().getAdvocatenkantoor().setTavVoorl("");
    getZaakDossier().getAdvocatenkantoor().setTavNaam("");
    getZaakDossier().getAdvocatenkantoor().setAdres("");
    getZaakDossier().getAdvocatenkantoor().setPostcode(new FieldValue());
    getZaakDossier().getAdvocatenkantoor().setLand(new FieldValue());
    getZaakDossier().getAdvocatenkantoor().setPlaats("");
    getZaakDossier().getAdvocatenkantoor().setKenmerk("");
    getZaakDossier().getAdvocatenkantoor().setKenmerk2("");

    // Waarden toevoegen
    if (formOntvangenDocumentenUitspraak.isAdded()) {
      getZaakDossier().setUitspraakDoor(formOntvangenDocumentenUitspraak.getBean().getUitspraak());
      getZaakDossier().setDatumUitspraak(
          new DateTime(formOntvangenDocumentenUitspraak.getBean().getDatumUitspraak()));
      getZaakDossier().setDatumGewijsde(
          new DateTime(formOntvangenDocumentenUitspraak.getBean().getDatumGewijsde()));
      getZaakDossier().setVerzoekTotInschrijvingDoor(
          formOntvangenDocumentenUitspraak.getBean().getVerzoekInschrijvingDoor());
      getZaakDossier().setDatumVerzoek(
          new DateTime(formOntvangenDocumentenUitspraak.getBean().getDatumVerzoek()));
      getZaakDossier().setBinnenTermijn(formOntvangenDocumentenUitspraak.getBean().getBinnenTermijn());
    }

    if (formOntvangenDocumentenWederzijds.isAdded()) {
      getZaakDossier().setDatumVerklaring(
          new DateTime(formOntvangenDocumentenWederzijds.getBean().getDatumVerklaring()));
      getZaakDossier().setOndertekendDoor(formOntvangenDocumentenWederzijds.getBean().getOndertekendDoor());
      getZaakDossier().setDatumOndertekening(
          new DateTime(formOntvangenDocumentenWederzijds.getBean().getDatumOndertekening()));
    }

    if (formWijzeBeeindiging.isAdded()) {
      getZaakDossier().setWijzeBeeindigingVerbintenis(formWijzeBeeindiging.getBean().getDoor());
    }

    if (layoutAdvocaten.isAdded()) {
      if (layoutAdvocaten.isGezet()) {
        getZaakDossier().getAdvocatenkantoor().setNaam(layoutAdvocaten.getBean().getNaam());
        getZaakDossier().getAdvocatenkantoor().setTavAanhef(layoutAdvocaten.getBean().getTavAanhef());
        getZaakDossier().getAdvocatenkantoor().setTavVoorl(layoutAdvocaten.getBean().getTavVoorl());
        getZaakDossier().getAdvocatenkantoor().setTavNaam(layoutAdvocaten.getBean().getTavNaam());
        getZaakDossier().getAdvocatenkantoor().setAdres(layoutAdvocaten.getBean().getAdres());
        getZaakDossier().getAdvocatenkantoor().setPostcode(layoutAdvocaten.getBean().getPc());
        getZaakDossier().getAdvocatenkantoor().setPlaats(layoutAdvocaten.getBean().getPlaats());
        getZaakDossier().getAdvocatenkantoor().setLand(layoutAdvocaten.getBean().getLand());
        getZaakDossier().getAdvocatenkantoor().setKenmerk(layoutAdvocaten.getBean().getKenmerk());
        getZaakDossier().getAdvocatenkantoor().setKenmerk2(layoutAdvocaten.getBean().getKenmerk2());
      }
    }

    getZaakDossier().setDatumIngang(new DateTime(datumIngang));

    getApplication().getServices().getOntbindingService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    try {
      if (event.isEvent(InitPage.class)) {

        addComponent(new BsStatusForm(getDossier()));

        SoortVerbintenis soortVerbintenis = getZaakDossier().getSoortVerbintenis();

        formOntvangenDocumentenUitspraak = new FormOntvangenDocumentenUitspraak(getZaakDossier());
        formWijzeBeeindiging = new FormWijzeBeeindiging(getZaakDossier());
        formOntvangenDocumentenWederzijds = new FormOntvangenDocumentenWederzijds(getZaakDossier());
        formIngang = new FormIngang(getZaakDossier());
        layoutAdvocaten = new AdvocatenkantorenLayout(getApplication(), getZaakDossier());

        formOntvangenDocumentenUitspraak.setFormIngang(formIngang);

        if (soortVerbintenis == SoortVerbintenis.GPS) {
          setForm(formWijzeBeeindiging, true);
          setWijze(getZaakDossier().getWijzeBeeindigingVerbintenis());
        } else {
          setForm(formOntvangenDocumentenUitspraak, true);
          setForm(formIngang, true);
          setForm(layoutAdvocaten, true);
        }
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

  public void setForm(Component component, boolean enable) {
    if (enable) {
      addComponent(component);
    } else {
      removeComponent(component);
    }
  }

  protected void setWijze(WijzeBeeindigingVerbintenis wijze) {

    setForm(formIngang, false);
    setForm(layoutAdvocaten, false);

    if (wijze == null) {
      setForm(formOntvangenDocumentenWederzijds, false);
      setForm(formOntvangenDocumentenUitspraak, false);
    } else {
      switch (wijze) {
        case WEDERZIJDS_GOEDVINDEN:
          setForm(formOntvangenDocumentenUitspraak, false);
          setForm(formOntvangenDocumentenWederzijds, true);
          break;

        case RECHTERLIJKE_UITSPRAAK:
          setForm(formOntvangenDocumentenWederzijds, false);
          setForm(formOntvangenDocumentenUitspraak, true);
          break;

        default:
          break;
      }

      setForm(formIngang, true);
      setForm(layoutAdvocaten, true);
    }
  }

  public class FormAdvocaat extends Page30OntbindingForm5 {

    private FormAdvocaat(GbaApplication application, DossierOntbinding zaakDossier) {
      super(application);
      setBean(zaakDossier);
    }
  }

  public class FormIngang extends Page30OntbindingForm4 {

    private FormIngang(DossierOntbinding zaakDossier) {
      super(formOntvangenDocumentenUitspraak);
      setBean(zaakDossier);
    }
  }

  public class FormOntvangenDocumentenUitspraak extends Page30OntbindingForm1 {

    private FormOntvangenDocumentenUitspraak(DossierOntbinding zaakDossier) {
      super(Page30Ontbinding.this.getApplication());
      setBean(zaakDossier);
    }
  }

  public class FormOntvangenDocumentenWederzijds extends Page30OntbindingForm3 {

    private FormOntvangenDocumentenWederzijds(DossierOntbinding zaakDossier) {
      setBean(zaakDossier);
    }
  }

  public class FormWijzeBeeindiging extends Page30OntbindingForm2 {

    private FormWijzeBeeindiging(DossierOntbinding zaakDossier) {
      setBean(zaakDossier);
    }

    @Override
    protected void onChangeWijze(WijzeBeeindigingVerbintenis wijze) {
      setWijze(wijze);
    }
  }
}
