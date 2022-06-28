/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.beheer.verkiezing.page4;

import static nl.procura.gba.web.modules.beheer.verkiezing.page4.Page4VerkiezingBean.F_TOEGEVOEGD;
import static nl.procura.standard.Globalfunctions.astr;

import com.vaadin.ui.Field;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.KiesrStem;
import nl.procura.gba.jpa.personen.db.KiesrVerk;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.beheer.verkiezing.Stempas;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class Page4VerkiezingForm extends GbaForm<Page4VerkiezingBean> {

  public Page4VerkiezingForm(String caption, String columnWidth, KiesrVerk verk, KiesrStem stem, String... fields) {
    setCaption(caption);
    setOrder(fields);
    setColumnWidths(columnWidth, "");

    Stempas stempas = new Stempas(stem);

    Page4VerkiezingBean bean = new Page4VerkiezingBean();
    // Verkiezing
    bean.setVerkiezing(String.format("%s / %s / %s",
        verk.getGemeente(),
        verk.getVerkiezing(),
        new DateTime(verk.getdVerk()).getFormatDate()));

    // Stempas
    bean.setCode(astr(stem.getVnr()));
    bean.setPasNr(stem.getPasNr());
    bean.setToegevoegd(stem.isIndToegevoegd() ? "Ja" : "Nee");
    bean.setAanduiding(stempas.getAanduidingType().getOms());

    // Kiesgerechtigde
    bean.setAnr(new AnrFieldValue(astr(stem.getAnr())).toString());
    bean.setNaam(stempas.getNaam());
    bean.setGeboortedatum(stempas.getGeboortedatum().getFormatDate());
    bean.setGeslacht(stempas.getGeslacht().getNormaal());
    bean.setAdres(stempas.getAdres() + " " + stempas.getPostcodeWoonplaats());

    setBean(bean);
  }

  @Override
  public void setColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_TOEGEVOEGD)) {
      getLayout().addFieldset("Aanduidingen");
    }
    super.setColumn(column, field, property);
  }
}
