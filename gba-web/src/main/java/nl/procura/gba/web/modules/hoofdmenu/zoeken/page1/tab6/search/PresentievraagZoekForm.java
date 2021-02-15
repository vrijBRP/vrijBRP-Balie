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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.search;

import static nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.search.PresentievraagZoekBean.*;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.Field;

import nl.procura.bcgba.v14.actions.IDGegevens;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class PresentievraagZoekForm extends GbaForm<PresentievraagZoekBean> {

  public PresentievraagZoekForm(PresentievraagZoekBean vraagBean) {
    setReadonlyAsText(true);
    setCaption("Zoekargumenten");
    setOrder(VOORNAMEN, GEBOORTEDATUM, VOORVOEGSEL, GEBOORTEPLAATS, GESLACHTSNAAM, GEBOORTELAND, GESLACHT,
        DATUM_AANVANG_ADRES_BUITENLAND, GEMEENTE, BUITENLAND_NR, NATIONALITEIT);
    setColumnWidths("200px", "250px", "180px", "");
    setBean(vraagBean);
  }

  public void disable(String... fields) {

    for (String field : fields) {
      getField(field).setValue(null);
      getField(field).setEnabled(false);
    }
  }

  public IDGegevens getIDGegevens() {

    IDGegevens idGegevens = new IDGegevens();

    String voornamen = getBean().getVoornamen();
    String geslachtsnaam = getBean().getGeslachtsnaam();
    String voorvoegsel = astr(getBean().getVoorvoegsel());
    String geboortedatum = astr(getBean().getGeboortedatum().getValue());
    String geboorteplaats = getBean().getGeboorteplaats();
    FieldValue geboorteland = getBean().getGeboorteland();
    String buitenlandnr = getBean().getBuitenlandnr();
    FieldValue natio = getBean().getNationaliteit();
    FieldValue gemeente = getBean().getGemeente();
    String datumAanvangAdresBuitenland = astr(getBean().getDatumAanvangAdresBuitenland().getValue());

    idGegevens.setVoornamen(voornamen);
    idGegevens.setVoorvoegselGeslachtsnaam(voorvoegsel);
    idGegevens.setGeslachtsnaam(geslachtsnaam);
    idGegevens.setGeboortedatum(geboortedatum);
    idGegevens.setGeboorteplaats(geboorteplaats);

    if (gemeente != null) {
      idGegevens.setGemeenteVanInschrijving(gemeente.getDescription());
    }

    idGegevens.setDatumVertrekUitNederland(datumAanvangAdresBuitenland);

    if (getBean().getGeslacht() != null) {
      String geslachtsaanduiding = getBean().getGeslacht().getAfkorting();
      idGegevens.setGeslachtsaanduiding(geslachtsaanduiding);
    }

    if (geboorteland != null) {
      idGegevens.setGeboorteland(geboorteland.getDescription());
    }

    if (fil(buitenlandnr)) {
      idGegevens.setBuitenlandsPersoonsnummer(buitenlandnr);
    }

    if (natio != null) {
      String natioValue = natio.getDescription();
      if (fil(natioValue)) {
        idGegevens.setNationaliteit(natioValue);
      }
    }

    return idGegevens;
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(GESLACHT)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }

  public void setRequired(String... fields) {
    for (String field : fields) {
      getField(field).setRequired(true);
    }
  }
}
