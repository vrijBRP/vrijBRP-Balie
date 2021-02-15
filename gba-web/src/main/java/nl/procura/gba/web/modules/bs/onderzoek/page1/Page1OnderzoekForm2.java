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

package nl.procura.gba.web.modules.bs.onderzoek.page1;

import static nl.procura.gba.web.modules.bs.onderzoek.page1.Page1OnderzoekBean.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.onderzoek.enums.OnderzoekBronType;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page1OnderzoekForm2 extends GbaForm<Page1OnderzoekBean> {

  public Page1OnderzoekForm2(DossierOnderzoek zaakDossier, OnderzoekBronType bron) {
    setCaption(bron.getOms());
    setColumnWidths("200px", "");
    switch (bron) {
      case BURGER:
        // Nvt
        break;
      case TMV:
        setOrder(NR, KENMERK);
        break;
      case INSTANTIE:
        setOrder(INSTANTIE, TAV_AANHEF, TAV_VOORL, TAV_NAAM, ADRES, PC, PLAATS, KENMERK);
        setColumnWidths("200px", "280px", "100px", "");
        break;
      case AMBTSHALVE:
        setOrder(AFDELING, KENMERK);
        break;
      case ONBEKEND:
        break;
    }
    setBean(zaakDossier);
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {

    if (property.is(NR) && getColumnWidths().length > 2) {
      column.setColspan(3);
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public Page1OnderzoekBean getNewBean() {
    return new Page1OnderzoekBean();
  }

  public void setBean(DossierOnderzoek zaakDossier) {
    Page1OnderzoekBean b = new Page1OnderzoekBean();
    b.setNr(zaakDossier.getAanlTmvNr());
    b.setInstantie(zaakDossier.getAanlInst());
    b.setTavAanhef(new FieldValue(zaakDossier.getAanlInstAanhef()));
    b.setTavVoorl(zaakDossier.getAanlInstVoorl());
    b.setTavNaam(zaakDossier.getAanlInstNaam());
    b.setAdres(zaakDossier.getAanlInstAdres());
    b.setPc(new FieldValue(zaakDossier.getAanlInstPc()));
    b.setPlaats(zaakDossier.getAanlInstPlaats());
    b.setKenmerk(zaakDossier.getAanlKenmerk());
    b.setAfdeling(zaakDossier.getAanlAfdeling());
    setBean(b);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(TAV_VOORL, TAV_NAAM)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }
}
