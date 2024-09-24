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

package nl.procura.gba.web.modules.hoofdmenu.klapper.page1;

import static nl.procura.gba.web.modules.hoofdmenu.klapper.page1.Page1KlapperBean.BSN;
import static nl.procura.gba.web.modules.hoofdmenu.klapper.page1.Page1KlapperBean.DATUM_AKTE;
import static nl.procura.gba.web.modules.hoofdmenu.klapper.page1.Page1KlapperBean.DATUM_FEIT;
import static nl.procura.gba.web.modules.hoofdmenu.klapper.page1.Page1KlapperBean.DEEL;
import static nl.procura.gba.web.modules.hoofdmenu.klapper.page1.Page1KlapperBean.GEBOORTEDATUM;
import static nl.procura.gba.web.modules.hoofdmenu.klapper.page1.Page1KlapperBean.GESLACHTSNAAM;
import static nl.procura.gba.web.modules.hoofdmenu.klapper.page1.Page1KlapperBean.INVOERTYPE;
import static nl.procura.gba.web.modules.hoofdmenu.klapper.page1.Page1KlapperBean.JAAR;
import static nl.procura.gba.web.modules.hoofdmenu.klapper.page1.Page1KlapperBean.NUMMER;
import static nl.procura.gba.web.modules.hoofdmenu.klapper.page1.Page1KlapperBean.OPMERKING;
import static nl.procura.gba.web.modules.hoofdmenu.klapper.page1.Page1KlapperBean.SOORT;
import static nl.procura.gba.web.modules.hoofdmenu.klapper.page1.Page1KlapperBean.VOLGORDE;
import static nl.procura.gba.web.modules.hoofdmenu.klapper.page1.Page1KlapperBean.VOORNAMEN;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.util.List;

import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.beheer.aktes.page3.AkteRegistersoortContainer;
import nl.procura.gba.web.services.bs.algemeen.akte.AkteService;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteDeel;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteInvoerType;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteRegistersoort;
import nl.procura.gba.web.services.bs.algemeen.akte.KlapperVolgordeType;
import nl.procura.gba.web.services.bs.algemeen.akte.KlapperZoekargumenten;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.BsnField;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;
import nl.procura.validation.Bsn;

public abstract class Page1KlapperForm extends GbaForm<Page1KlapperBean> {

  private final GbaApplication application;

  public Page1KlapperForm(GbaApplication application) {
    this.application = application;

    setColumnWidths("80px", "200px", "130px", "200px", "140px", "");
    setCaption("Zoeken");

    setOrder(JAAR, SOORT, GESLACHTSNAAM, VOLGORDE, DEEL, VOORNAMEN,
        DATUM_AKTE, DATUM_FEIT, NUMMER, GEBOORTEDATUM, INVOERTYPE, OPMERKING, BSN);
    setBean(new Page1KlapperBean());
  }

  @Override
  public void afterSetBean() {

    getInvoerVeld().setDataSource(new AkteInvoerTypeContainer());
    getInvoerVeld().addListener((ValueChangeListener) this);

    getSoortVeld().setDataSource(new AkteRegistersoortContainer());
    getSoortVeld().addListener((ValueChangeListener) this);

    getDeelVeld().setDataSource(new DeelContainer(getSoort()));
    getDeelVeld().addListener((ValueChangeListener) this);

    getDatumAkteVeld().addListener((ValueChangeListener) this);
    getDatumFeitVeld().addListener((ValueChangeListener) this);

    getNummerVeld().setTextChangeEventMode(TextChangeEventMode.LAZY);
    getNummerVeld().setTextChangeTimeout(200);
    getNummerVeld().addListener((TextChangeListener) event -> {

      KlapperZoekargumenten za = getZoekargumenten();
      za.setNummer(along(event.getText()));

      update(za);
    });

    getJaarVeld().setDataSource(new JaarContainer(application));
    getJaarVeld().addListener((ValueChangeListener) this);
    getJaarVeld().setValue(toBigDecimal(new ProcuraDate().getYear()));

    getBsnVeld().addListener((ValueChangeListener) this);
    getGeslachtsnaamVeld().addListener((ValueChangeListener) this);
    getVoornamenVeld().addListener((ValueChangeListener) this);
    getGeboorteDatumVeld().addListener((ValueChangeListener) this);
    getOpmerkingVeld().addListener((ValueChangeListener) this);

    getVolgordeVeld().setDataSource(new VolgordeTypeContainer());
    getVolgordeVeld().addListener((ValueChangeListener) this);
    getVolgordeVeld().setValue(KlapperVolgordeType.DATUM_OPLOPEND);

    update(getZoekargumenten());

    super.afterSetBean();
  }

  public long getBsn() {
    return new Bsn(astr(getBsnVeld().getValue())).getLongBsn();
  }

  public BsnField getBsnVeld() {
    return getField(BSN, BsnField.class);
  }

  public long getDatumFeit() {
    return along(new ProcuraDate(astr(getDatumFeitVeld().getValue())).getSystemDate());
  }

  public DatumVeld getDatumFeitVeld() {
    return getField(DATUM_FEIT, DatumVeld.class);
  }

  public long getDatumAkte() {
    return along(new ProcuraDate(astr(getDatumAkteVeld().getValue())).getSystemDate());
  }

  public DatumVeld getDatumAkteVeld() {
    return getField(DATUM_AKTE, DatumVeld.class);
  }

  public DossierAkteDeel getDeel() {
    return (DossierAkteDeel) getDeelVeld().getValue();
  }

  public GbaNativeSelect getDeelVeld() {
    return getField(DEEL, GbaNativeSelect.class);
  }

  public String getGeslachtsnaam() {
    return astr(getGeslachtsnaamVeld().getValue());
  }

  public TextField getGeslachtsnaamVeld() {
    return getField(GESLACHTSNAAM, TextField.class);
  }

  public String getVoornamen() {
    return astr(getVoornamenVeld().getValue());
  }

  public TextField getVoornamenVeld() {
    return getField(VOORNAMEN, TextField.class);
  }

  public long getGeboorteDatum() {
    return along(new ProcuraDate(astr(getGeboorteDatumVeld().getValue())).getSystemDate());
  }

  public GbaDateField getGeboorteDatumVeld() {
    return getField(GEBOORTEDATUM, GbaDateField.class);
  }

  public String getOpmerking() {
    return astr(getField(OPMERKING).getValue());
  }

  public TextField getOpmerkingVeld() {
    return getField(OPMERKING, TextField.class);
  }

  public DossierAkteInvoerType getInvoerType() {
    return (DossierAkteInvoerType) getInvoerVeld().getValue();
  }

  public GbaNativeSelect getInvoerVeld() {
    return getField(INVOERTYPE, GbaNativeSelect.class);
  }

  public long getJaar() {
    return along(getJaarVeld().getValue());
  }

  public GbaNativeSelect getJaarVeld() {
    return getField(JAAR, GbaNativeSelect.class);
  }

  public long getNummer() {
    return along(getField(NUMMER).getValue());
  }

  public TextField getNummerVeld() {
    return getField(NUMMER, TextField.class);
  }

  public DossierAkteRegistersoort getSoort() {
    return (DossierAkteRegistersoort) getSoortVeld().getValue();
  }

  public GbaNativeSelect getSoortVeld() {
    return getField(SOORT, GbaNativeSelect.class);
  }

  public KlapperVolgordeType getVolgorde() {
    return (KlapperVolgordeType) getVolgordeVeld().getValue();
  }

  public GbaNativeSelect getVolgordeVeld() {
    return getField(VOLGORDE, GbaNativeSelect.class);
  }

  public boolean isOplopend() {
    return getVolgorde() == KlapperVolgordeType.DATUM_OPLOPEND || getVolgorde() == KlapperVolgordeType.NAAM_OPLOPEND;
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(DATUM_FEIT)) {
      column.setAppend(true);
    }
    super.setColumn(column, field, property);
  }

  public abstract void update(KlapperZoekargumenten zoekargumenten);

  @Override
  public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {

    if (event.getProperty() == getSoortVeld()) {
      getDeelVeld().setDataSource(new DeelContainer(getSoort()));
    }

    update(getZoekargumenten());
    super.valueChange(event);
  }

  private KlapperZoekargumenten getZoekargumenten() {
    KlapperZoekargumenten za = new KlapperZoekargumenten();
    za.setJaar(getJaar());
    za.setDatumAkte(getDatumAkte());
    za.setDatumFeit(getDatumFeit());
    za.setInvoerType(getInvoerType());
    za.getSoorten().add(getSoort());
    za.setDeel(getDeel());
    za.setNummer(getNummer());
    za.setGeslachtsnaam(getGeslachtsnaam());
    za.setVoornamen(getVoornamen());
    za.setGeboortedatum(getGeboorteDatum());
    za.setOpmerking(getOpmerking());
    za.setBsn(getBsn());
    za.setVolgorde(getVolgorde());
    za.setLimit(pos(getJaar()) ? -1 : 5000);
    return za;
  }

  public class AkteInvoerTypeContainer extends ArrayListContainer {

    public AkteInvoerTypeContainer() {
      addItem(DossierAkteInvoerType.PROWEB_PERSONEN);
      addItem(DossierAkteInvoerType.BLANCO);
      addItem(DossierAkteInvoerType.HANDMATIG);
    }
  }

  public class DeelContainer extends ArrayListContainer {

    public DeelContainer(DossierAkteRegistersoort soort) {
      if (soort != null) {
        AkteService aktes = application.getServices().getAkteService();
        List<DossierAkteDeel> registerDelen = aktes.getAkteRegisterDelen(soort.getCode());

        if (registerDelen != null) {
          addItems(registerDelen);
        }
      }
    }
  }

  public class JaarContainer extends ArrayListContainer {

    public JaarContainer(GbaApplication application) {
      application.getServices().getAkteService().getJaren().forEach(this::addItem);
    }
  }

  public class VolgordeTypeContainer extends ArrayListContainer {

    public VolgordeTypeContainer() {
      addItem(KlapperVolgordeType.DATUM_OPLOPEND);
      addItem(KlapperVolgordeType.DATUM_AFLOPEND);
      addItem(KlapperVolgordeType.NAAM_OPLOPEND);
      addItem(KlapperVolgordeType.NAAM_AFLOPEND);
    }
  }
}
