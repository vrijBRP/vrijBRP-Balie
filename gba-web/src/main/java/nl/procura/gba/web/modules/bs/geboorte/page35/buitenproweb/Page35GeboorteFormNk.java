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

import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenNk.AKTENR;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenNk.BIJZONDERHEDEN;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenNk.DATUM;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenNk.DUBBELE_NAAM;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenNk.GEMEENTE;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenNk.GESLACHTSNAAM;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenNk.LAND;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenNk.NAAMS_PERSOON_TYPE;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenNk.PLAATS;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenNk.TITEL;
import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanBuitenNk.VOORV;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.trim;

import com.vaadin.ui.Button;
import com.vaadin.ui.Field;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.common.pages.naamgebruikpage.BsNamenPage.NaamsKeuze;
import nl.procura.gba.web.modules.bs.common.pages.naamgebruikpage.BsNamenWindow;
import nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanBinnenErk;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.naamskeuzebuitenproweb.NaamskeuzeBuitenProweb;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page35GeboorteFormNk extends GbaForm<Page35GeboorteBeanBuitenNk> {

  private final DossierGeboorte geboorte;

  public Page35GeboorteFormNk(DossierGeboorte geboorte) {
    this.geboorte = geboorte;
    setColumnWidths("200px", "");
    setOrder(LAND, GEMEENTE, PLAATS, DATUM, AKTENR, NAAMS_PERSOON_TYPE,
        TITEL, VOORV, GESLACHTSNAAM, DUBBELE_NAAM, BIJZONDERHEDEN);
    setGeboorte(geboorte);
    setCaptionAndOrder();
  }

  @Override
  public void afterSetBean() {
    getField(LAND).addListener((ValueChangeListener) event -> {
      onChangeLand((FieldValue) event.getProperty().getValue());
    });

    onChangeLand(getBean().getLand());
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
  public Page35GeboorteBeanBuitenNk getNewBean() {
    return new Page35GeboorteBeanBuitenNk();
  }

  public void setCaptionAndOrder() {
  }

  public void setGeboorte(DossierGeboorte geboorte) {
    Page35GeboorteBeanBuitenNk bean = new Page35GeboorteBeanBuitenNk();

    if (geboorte.getVragen().heeftNaamskeuzeBuitenProweb()) {
      NaamskeuzeBuitenProweb naamskeuzeBuitenProweb = geboorte.getNaamskeuzeBuitenProweb();
      bean.setAktenr(naamskeuzeBuitenProweb.getAktenummer());
      bean.setDatum(new DateFieldValue(naamskeuzeBuitenProweb.getDatum().getLongDate()));
      bean.setGemeente(naamskeuzeBuitenProweb.getGemeente());
      bean.setLand(naamskeuzeBuitenProweb.getLand());
      bean.setPlaats(naamskeuzeBuitenProweb.getBuitenlandsePlaats());
      bean.setNaamsPersoonType(naamskeuzeBuitenProweb.getNaamskeuzePersoon());
      bean.setBijzonderheden(naamskeuzeBuitenProweb.getBijzonderheden());
      bean.setTitel(geboorte.getKeuzeTitel());
      bean.setVoorv(new FieldValue(geboorte.getKeuzeVoorvoegsel()));
      bean.setGeslachtsnaam(geboorte.getKeuzeGeslachtsnaam());
      bean.setDubbeleNaam(geboorte.getOrgKeuzeNaam());
    }

    setBean(bean);
    Field dubbeleNaam = getField(DUBBELE_NAAM);
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
}
