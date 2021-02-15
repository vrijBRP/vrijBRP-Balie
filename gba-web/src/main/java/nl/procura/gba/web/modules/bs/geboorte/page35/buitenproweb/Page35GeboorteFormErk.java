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

import static nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanErk.*;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.enums.RechtbankLocatie;
import nl.procura.gba.web.services.bs.erkenning.NaamskeuzeVanToepassingType;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ErkenningBuitenProweb;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ToestemminggeverType;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class Page35GeboorteFormErk extends GbaForm<Page35GeboorteBeanErk> {

  public Page35GeboorteFormErk(DossierGeboorte geboorte) {

    setColumnWidths("140px", "");
    setOrder(LAND, GEMEENTE, PLAATS, DATUM, AKTENR, TOESTEMMINGGEVER_TYPE, RECHTBANK, NAAMSKEUZE_TYPE,
        NAAMS_PERSOON_TYPE, NAAMS_PERSOON_TYPE_ERK, AFSTAMMINGSRECHT);

    setGeboorte(geboorte);
    setCaptionAndOrder();
  }

  @Override
  public void afterSetBean() {

    getField(NAAMSKEUZE_TYPE)
        .addListener((ValueChangeListener) event -> {
          onChangeNaamskeuze((NaamskeuzeVanToepassingType) event.getProperty().getValue());
        });

    getField(TOESTEMMINGGEVER_TYPE).addListener((ValueChangeListener) event -> {
      onChangeToestemminggever((ToestemminggeverType) event.getProperty().getValue());
    });

    getField(LAND).addListener((ValueChangeListener) event -> {
      onChangeLand((FieldValue) event.getProperty().getValue());
    });

    onChangeLand(getBean().getLand());
    onChangeNaamskeuze(getBean().getNaamskeuzeType());
    onChangeToestemminggever(getBean().getToestemminggeverType());
  }

  @Override
  public Page35GeboorteBeanErk getNewBean() {
    return new Page35GeboorteBeanErk();
  }

  public void setCaptionAndOrder() {
  }

  public void setGeboorte(DossierGeboorte geboorte) {
    Page35GeboorteBeanErk bean = new Page35GeboorteBeanErk();

    if (geboorte != null && geboorte.getVragen().heeftErkenningBuitenProweb()) {
      ErkenningBuitenProweb erkenningBuitenProweb = geboorte.getErkenningBuitenProweb();
      bean.setAktenr(erkenningBuitenProweb.getAktenummer());
      bean.setDatum(new DateFieldValue(erkenningBuitenProweb.getDatumErkenning().getLongDate()));
      bean.setGemeente(erkenningBuitenProweb.getGemeente());
      bean.setLand(erkenningBuitenProweb.getLandErkenning());
      bean.setPlaats(erkenningBuitenProweb.getBuitenlandsePlaats());
      bean.setToestemminggeverType(erkenningBuitenProweb.getToestemminggeverType());
      bean.setRechtbank(RechtbankLocatie.get(erkenningBuitenProweb.getRechtbank()));
      bean.setNaamskeuzeType(erkenningBuitenProweb.getNaamskeuzeType());
      bean.setNaamsPersoonType(erkenningBuitenProweb.getNaamskeuzePersoon());
      bean.setAfstammingsrecht(erkenningBuitenProweb.getLandAfstamming());
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

  private void onChangeNaamskeuze(NaamskeuzeVanToepassingType type) {
    boolean isJa = NaamskeuzeVanToepassingType.JA.equals(type);
    getField(NAAMS_PERSOON_TYPE_ERK).setVisible(isJa);
    getField(NAAMS_PERSOON_TYPE).setVisible(!isJa);
    repaint();
  }

  private void onChangeToestemminggever(ToestemminggeverType toestemminggeverType) {
    getField(RECHTBANK).setVisible(toestemminggeverType == ToestemminggeverType.RECHTBANK);
    repaint();
  }
}
