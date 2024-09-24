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

package nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb;

import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenErk.AFSTAMMINGSRECHT;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenErk.AKTENR;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenErk.DATUM;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenErk.DUBBELE_NAAM;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenErk.GEMEENTE;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenErk.GESLACHTSNAAM;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenErk.LAND;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenErk.NAAMSKEUZE_TYPE;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenErk.NAAMS_PERSOON_TYPE;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenErk.PLAATS;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenErk.RECHTBANK;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenErk.TITEL;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenErk.TOESTEMMINGGEVER_TYPE;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenErk.VERKLARING_GEZAG;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenErk.VOORV;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.trim;

import com.vaadin.ui.Button;
import com.vaadin.ui.Field;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.common.pages.naamgebruikpage.BsNamenPage.NaamsKeuze;
import nl.procura.gba.web.modules.bs.common.pages.naamgebruikpage.BsNamenWindow;
import nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanBinnenErk;
import nl.procura.gba.web.services.bs.algemeen.enums.RechtbankLocatie;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ErkenningBuitenProweb;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ToestemminggeverType;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page35GeboorteFormErk extends GbaForm<Page35GeboorteBeanBuitenErk> {

  private final DossierGeboorte geboorte;

  public Page35GeboorteFormErk(DossierGeboorte geboorte) {
    this.geboorte = geboorte;

    setColumnWidths("200px", "");
    setOrder(LAND, GEMEENTE, PLAATS, DATUM, AKTENR, TOESTEMMINGGEVER_TYPE, VERKLARING_GEZAG,
        RECHTBANK, NAAMSKEUZE_TYPE, NAAMS_PERSOON_TYPE, TITEL, VOORV, GESLACHTSNAAM,
        DUBBELE_NAAM, AFSTAMMINGSRECHT);

    setGeboorte(geboorte);
    setCaptionAndOrder();
  }

  @Override
  public void afterSetBean() {

    getField(TOESTEMMINGGEVER_TYPE).addListener((ValueChangeListener) event -> {
      onChangeToestemminggever((ToestemminggeverType) event.getProperty().getValue());
    });

    getField(LAND).addListener((ValueChangeListener) event -> {
      onChangeLand((FieldValue) event.getProperty().getValue());
    });

    onChangeLand(getBean().getLand());
    onChangeToestemminggever(getBean().getToestemminggeverType());
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {
    if (property.is(Page35GeboorteBeanBuitenErk.DUBBELE_NAAM)) {
      column.addComponent(new Button("Naamselectie", event -> {
        getApplication().getParentWindow().addWindow(new BsNamenWindow(geboorte) {

          @Override
          public void setNaam(NaamsKeuze naamsKeuze) {
            getField(Page35GeboorteBeanBinnenErk.DUBBELE_NAAM).setValue(trim(String.format("%s %s",
                astr(naamsKeuze.getVoorvoegsel()), naamsKeuze.getGeslachtsnaam())));
          }
        });
      }));
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public Page35GeboorteBeanBuitenErk getNewBean() {
    return new Page35GeboorteBeanBuitenErk();
  }

  public void setCaptionAndOrder() {
  }

  public void setGeboorte(DossierGeboorte geboorte) {
    Page35GeboorteBeanBuitenErk bean = new Page35GeboorteBeanBuitenErk();

    if (geboorte.getVragen().heeftErkenningBuitenProweb()) {
      ErkenningBuitenProweb erkenningBuitenProweb = geboorte.getErkenningBuitenProweb();
      bean.setAktenr(erkenningBuitenProweb.getAktenummer());
      bean.setDatum(new DateFieldValue(erkenningBuitenProweb.getDatumErkenning().getLongDate()));
      bean.setGemeente(erkenningBuitenProweb.getGemeente());
      bean.setLand(erkenningBuitenProweb.getLandErkenning());
      bean.setPlaats(erkenningBuitenProweb.getBuitenlandsePlaats());
      bean.setToestemminggeverType(erkenningBuitenProweb.getToestemminggeverType());
      bean.setVerklaringGezag(erkenningBuitenProweb.isVerklaringGezag());
      bean.setRechtbank(RechtbankLocatie.get(erkenningBuitenProweb.getRechtbank()));
      bean.setNaamskeuzeType(erkenningBuitenProweb.getNaamskeuzeType());
      bean.setNaamsPersoonType(erkenningBuitenProweb.getNaamskeuzePersoon());
      bean.setAfstammingsrecht(erkenningBuitenProweb.getLandAfstamming());
      bean.setTitel(geboorte.getKeuzeTitel());
      bean.setVoorv(new FieldValue(geboorte.getKeuzeVoorvoegsel()));
      bean.setGeslachtsnaam(geboorte.getKeuzeGeslachtsnaam());
      bean.setDubbeleNaam(geboorte.getOrgKeuzeNaam());
    }

    setBean(bean);
    Field dubbeleNaam = getField(Page35GeboorteBeanBuitenNk.DUBBELE_NAAM);
    dubbeleNaam.setVisible(geboorte.isOvergangsperiodeNaamskeuze());
    dubbeleNaam.setReadOnly(false);
    repaint();
  }

  private void onChangeLand(FieldValue land) {

    boolean isNederland = Landelijk.getNederland().equals(land);

    getField(GEMEENTE).setVisible(isNederland);
    getField(PLAATS).setVisible(!isNederland);

    if (isNederland) {
      getField(PLAATS).setValue("");
    } else {
      getField(GEMEENTE).setValue(null);
    }

    repaint();
  }

  private void onChangeToestemminggever(ToestemminggeverType toestemminggeverType) {
    getField(RECHTBANK).setVisible(toestemminggeverType == ToestemminggeverType.RECHTBANK);
    repaint();
  }
}
