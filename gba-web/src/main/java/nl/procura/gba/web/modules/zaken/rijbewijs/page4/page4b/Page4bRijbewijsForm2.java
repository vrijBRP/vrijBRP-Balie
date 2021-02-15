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

package nl.procura.gba.web.modules.zaken.rijbewijs.page4.page4b;

import static nl.procura.gba.web.modules.zaken.rijbewijs.page4.page4b.Page4bRijbewijsBean2.*;
import static nl.procura.standard.Globalfunctions.date2str;

import com.vaadin.ui.Field;

import nl.procura.gba.web.modules.zaken.rijbewijs.RdwReadOnlyForm;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsGevordInleveringSoort;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsSoortMaatregel;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsUitgever;
import nl.procura.rdw.processen.p0252.f07.MAATREGELGEG;
import nl.procura.rdw.processen.p0252.f07.UITGMAATRGEG;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

/**
 * Maatregelgegevens
 */
public class Page4bRijbewijsForm2 extends RdwReadOnlyForm {

  Page4bRijbewijsForm2(UITGMAATRGEG c) {

    setCaption("Maatregelgegevens");

    setOrder(SOORT1, VOLGNR, AUTORITEIT1, AUTORITEIT2, AUTORITEIT3, REGISTRATIE, EINDDATUM, GEVORDINL, SOORT2);

    setColumnWidths(WIDTH_130, "300px", "130px", "");

    Page4bRijbewijsBean2 b = new Page4bRijbewijsBean2();

    MAATREGELGEG m = c.getMaatregelgeg();

    b.setSoort1(RijbewijsSoortMaatregel.getCodeOms(m.getSrtmaatr()));
    b.setVolgnr(trimInteger(m.getMaatrvolgnr()));
    b.setAutoriteit1(RijbewijsUitgever.getCodeOms(m.getSrtautmaatr()));
    b.setAutoriteit2(trimInteger(m.getAutorcmaatr()) + " " + m.getNaamautmaatr());
    b.setAutoriteit3(m.getNaammaatrdia());
    b.setRegistratie(date2str(m.getRegdatmaatr().toString()));
    b.setEinddatum(date2str(m.getEinddatmaatr().toString()));
    b.setGevordinl(date2str(m.getGevordinldat().toString()));
    b.setSoort2(RijbewijsGevordInleveringSoort.getCodeOms(m.getGevordinlsrt().toString()));

    setBean(b);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(AUTORITEIT1, AUTORITEIT2, AUTORITEIT3)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }
}
