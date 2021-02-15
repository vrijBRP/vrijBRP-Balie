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

package nl.procura.gba.web.modules.zaken.verhuizing.page2;

import static nl.procura.gba.web.modules.zaken.verhuizing.page2.Page2VerhuizingBean1.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisToestemminggever;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page2VerhuizingForm3 extends ReadOnlyForm {

  public Page2VerhuizingForm3(VerhuisAanvraag aanvraag) {

    setCaption("Inwoning");
    setOrder(INWONING, HOOFDBEWONER, TOESTEMMING, BESTEMMING, AANGIFTE);
    setColumnWidths(WIDTH_130, "", "200px", "200px");

    Page2VerhuizingBean1 b = new Page2VerhuizingBean1();

    BsnFieldValue bsnH = aanvraag.getHoofdbewoner().getBurgerServiceNummer();

    if (bsnH.isCorrect()) {
      b.setHoofdbewoner(bsnH.getDescription());
    } else {
      if (aanvraag.isSprakeVanInwoning()) {
        b.setHoofdbewoner("Geen hoofdbewoner aangemerkt.");
      }
    }

    b.setBestemming(aanvraag.getBestemmingHuidigeBewoners());
    b.setInwoning(aanvraag.isSprakeVanInwoning() ? "Ja" : "Nee");

    VerhuisToestemminggever toest = aanvraag.getToestemminggever();

    switch (toest.getToestemmingStatus()) {
      case JA:
        String naam = toest.getAnders();
        if (toest.isNatuurlijkPersoon()) {
          naam = toest.getPersoon().getPersoon().getFormats().getNaam().getNaam_naamgebruik_eerste_voornaam();
        }

        b.setToestemming("Ja, " + naam);
        break;

      case NEE:
        b.setToestemming("Nee");
        break;

      case NIET_INGEVULD:
        b.setToestemming("Niet ingevuld");
        break;

      case NIET_VAN_TOEPASSING:
        b.setToestemming("Niet van toepassing");
        break;

      default:
        break;
    }

    switch (toest.getAangifteStatus()) {

      case GEACCEPTEERD:
        b.setAangifte("Ja");
        break;

      case GEACCEPTEERD_ZONDER_TOESTEMMING:
        b.setAangifte("Ja, zonder toestemming verwerken");
        break;

      case NIET_GEACCEPTEERD:
        b.setAangifte("Nee");
        break;

      case NIET_INGEVULD:
        b.setAangifte("Nog niet aangegeven");
        break;

      default:
        break;
    }

    setBean(b);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(AANGIFTE)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }
}
