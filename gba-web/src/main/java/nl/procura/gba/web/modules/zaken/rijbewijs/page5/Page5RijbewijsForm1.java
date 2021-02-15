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

package nl.procura.gba.web.modules.zaken.rijbewijs.page5;

import static nl.procura.gba.web.modules.zaken.rijbewijs.page5.Page5RijbewijsBean1.*;
import static nl.procura.standard.Globalfunctions.*;

import java.util.List;

import com.vaadin.ui.Field;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.containers.GebruikerLocatieContainer;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.zaken.reisdocument.page11.TitelvermeldContainer;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.beheer.locatie.LocatieService;
import nl.procura.gba.web.services.beheer.locatie.LocatieType;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagSoort;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.rdw.processen.p1651.f08.AANRYBKOVERZ;
import nl.procura.rdw.processen.p1651.f08.AANVRRYBKGEG;
import nl.procura.rdw.processen.p1651.f08.CATAANRYBGEG;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page5RijbewijsForm1 extends GbaForm<Page5RijbewijsBean1> {

  public static final String NIET_VAN_TOEPASSING = "Niet van toepassing";

  public Page5RijbewijsForm1(Page5RijbewijsBean1 bean, final AANRYBKOVERZ proces) {

    initForm();
    setReadThrough(true);
    setReadonlyAsText(false);
    setBean(bean);

    getField(SOORT).addListener((ValueChangeListener) event -> {
      getField(INFO).setReadOnly(false);
      getField(INFO).setValue(getInfo((RijbewijsAanvraagSoort) event.getProperty().getValue(), proces));
      getField(INFO).setReadOnly(true);
    });
  }

  public boolean isValideProcesVerbaal() {
    Field pv = getField(PROCESVERBAAL);
    String pvValue = astr(pv.getValue());
    return !pv.isReadOnly() && fil(pvValue) && !pvValue.equals(NIET_VAN_TOEPASSING);
  }

  @Override
  public Field newField(Field field, Property property) {

    super.newField(field, property);

    if (property.is(VERVANGTRBW)) {
      getLayout().addBreak();
    }

    return field;
  }

  public void setAfhaalLocaties(GbaApplication application) {
    LocatieService db = application.getServices().getLocatieService();
    Gebruiker gebruiker = application.getServices().getGebruiker();
    List<Locatie> locaties = db.getGekoppeldeLocaties(gebruiker, LocatieType.AFHAAL_LOCATIE);

    GbaNativeSelect locatie = getField(Page5RijbewijsBean1.AFHAAL_LOCATIE, GbaNativeSelect.class);
    locatie.setVisible(locaties.size() > 0);
    locatie.setContainerDataSource(new GebruikerLocatieContainer(locaties));
    repaint();
  }

  public void setVermeldingTitel(BasePLExt pl) {
    TitelvermeldContainer tpVermeldContainer = new TitelvermeldContainer(pl);
    GbaNativeSelect vermeldTpField = getField(VERMELDING_TITEL, GbaNativeSelect.class);
    vermeldTpField.setDataSource(tpVermeldContainer);
    vermeldTpField.setValue(tpVermeldContainer.getDefaultValue());
    repaint();
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(SOORT, REDEN, INFO, DAGEN185, AFHAAL_LOCATIE)) {
      column.setColspan(5);
    }

    if (property.is(INFO)) {
      field.addStyleName(GbaWebTheme.TEXT.RED);
    }

    if (property.is(DAGEN185)) {
      field.addStyleName(
          field.getValue().toString().contains("Ja") ? GbaWebTheme.TEXT.GREEN : GbaWebTheme.TEXT.RED);
    }

    if (property.is(GBABESTENDIG)) {
      field.addStyleName(
          field.getValue().toString().contains("Ja") ? GbaWebTheme.TEXT.GREEN : GbaWebTheme.TEXT.RED);
    }

    super.setColumn(column, field, property);
  }

  protected void initForm() {
  }

  private String getInfo(RijbewijsAanvraagSoort soort, AANRYBKOVERZ proces) {

    String info = "-";

    if (proces != null && soort != null) {

      for (AANVRRYBKGEG a : proces.getAanvrrybktab().getAanvrrybkgeg()) {

        if (along(a.getSrtaanvrrybk()) == soort.getCode()) {

          int aantal = getMeldingen(a);

          if (aantal == 1) {
            info = "1 categorie zal niet meer op het rijbewijs komen.";
          } else if (aantal > 1) {
            info = aantal + " categorieën zullen niet meer op het rijbewijs komen.";
          }
        }
      }
    }

    return info;
  }

  private int getMeldingen(AANVRRYBKGEG a) {

    int count = 0;

    for (CATAANRYBGEG c : a.getCataanrybtab().getCataanrybgeg()) {
      if (fil(c.getCatmelding())) {
        count++;
      }
    }

    return count;
  }
}
