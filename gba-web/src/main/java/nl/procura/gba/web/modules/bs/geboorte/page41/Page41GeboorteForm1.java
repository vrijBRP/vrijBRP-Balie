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

package nl.procura.gba.web.modules.bs.geboorte.page41;

import static nl.procura.gba.web.modules.bs.geboorte.page41.Page41GeboorteBean1.*;

import com.vaadin.ui.Field;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.Cat1PersoonExt;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page41GeboorteForm1 extends ReadOnlyForm {

  public Page41GeboorteForm1(BasePLExt pl) {

    setColumnWidths("160px", "");
    setCaptionAndOrder();

    Page41GeboorteBean1 bean = new Page41GeboorteBean1();

    Cat1PersoonExt p = pl.getPersoon();

    bean.setBsn(p.getBsn().getDescr());
    bean.setGeslacht(p.getGeslacht().getDescr());
    bean.setNaam(p.getNaam().getTitelVoorvGesl());
    bean.setVbt(pl.getVerblijfstitel().getVerblijfstitel().getDescr());
    bean.setGeboren(p.getGeboorte().getDatumPlaatsLand());
    bean.setVerblijfplaats(pl.getVerblijfplaats().getAdres().getAdres());
    bean.setNationaliteit(pl.getNatio().getNationaliteiten());

    setBean(bean);
  }

  @Override
  public Page41GeboorteBean1 getBean() {
    return (Page41GeboorteBean1) super.getBean();
  }

  @Override
  public Object getNewBean() {
    return new Page41GeboorteBean1();
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(NAAM, GEBOREN, VBT)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }

  private void setCaptionAndOrder() {

    setColumnWidths("160px", "200px", "80px", "");

    setCaption("Kind");

    setOrder(BSN, GESLACHT, NAAM, GEBOREN, VBT, VERBLIJFPLAATS, NATIONALITEIT);
  }
}
