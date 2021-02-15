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

import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanNk.*;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.naamskeuzebuitenproweb.NaamskeuzeBuitenProweb;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class Page35GeboorteFormNk extends GbaForm<Page35GeboorteBeanNk> {

  public Page35GeboorteFormNk(DossierGeboorte geboorte) {
    setColumnWidths("140px", "");
    setOrder(LAND, GEMEENTE, PLAATS, DATUM, AKTENR, NAAMS_PERSOON_TYPE, GESLACHTSNAAM, VOORV, TITEL, BIJZONDERHEDEN);
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
  public Page35GeboorteBeanNk getNewBean() {
    return new Page35GeboorteBeanNk();
  }

  public void setCaptionAndOrder() {
  }

  public void setGeboorte(DossierGeboorte geboorte) {
    Page35GeboorteBeanNk bean = new Page35GeboorteBeanNk();

    if (geboorte != null && geboorte.getVragen().heeftNaamskeuzeBuitenProweb()) {
      NaamskeuzeBuitenProweb naamskeuzeBuitenProweb = geboorte.getNaamskeuzeBuitenProweb();
      bean.setAktenr(naamskeuzeBuitenProweb.getAktenummer());
      bean.setDatum(new DateFieldValue(naamskeuzeBuitenProweb.getDatum().getLongDate()));
      bean.setGemeente(naamskeuzeBuitenProweb.getGemeente());
      bean.setLand(naamskeuzeBuitenProweb.getLand());
      bean.setPlaats(naamskeuzeBuitenProweb.getBuitenlandsePlaats());
      bean.setNaamsPersoonType(naamskeuzeBuitenProweb.getNaamskeuzePersoon());
      bean.setBijzonderheden(naamskeuzeBuitenProweb.getBijzonderheden());
      bean.setGeslachtsnaam(geboorte.getKeuzeGeslachtsnaam());
      bean.setVoorv(new FieldValue(geboorte.getKeuzeVoorvoegsel()));
      bean.setTitel(geboorte.getKeuzeTitel());
    }

    setBean(bean);
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
