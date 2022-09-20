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

package nl.procura.gba.web.modules.bs.registration.fileimport;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.vaadin.theme.twee.layout.ScrollLayout;
import nl.procura.validation.Postcode;

public class FileImportRegistrantDataWindow extends GbaModalWindow {

  public FileImportRegistrantDataWindow(FileImportRegistrant importRegistrant) {
    super(true);
    setCaption(importRegistrant.getSummary());
    setWidth("700px");

    TableLayout layout1 = new TableLayout();
    layout1.setColumnWidths("90px", "");
    layout1.addLabel("Voornamen");
    layout1.addData(new Label(importRegistrant.getFirstname()));
    layout1.addLabel("Voorvoegsel");
    layout1.addData(new Label(importRegistrant.getPrefix()));
    layout1.addLabel("Geslachtsnaam");
    layout1.addData(new Label(importRegistrant.getLastname()));
    layout1.addLabel("Geslacht");
    layout1.addData(new Label(Geslacht.get(importRegistrant.getGender()).getNormaal()));
    layout1.addLabel("Geboortedatum");
    layout1.addData(new Label(importRegistrant.getBirthDate().getFormatDate()));
    layout1.addLabel("Geboorteland");
    layout1.addData(new Label(importRegistrant.getBirthCountry()));
    layout1.addLabel("Geboorteplaats");
    layout1.addData(new Label(importRegistrant.getBirthPlace()));
    layout1.addLabel("Nationaliteit");
    layout1.addData(new Label(importRegistrant.getNationality()));
    layout1.addLabel("E-mail");
    layout1.addData(new Label(importRegistrant.getEmail()));

    TableLayout layout2 = new TableLayout();
    layout2.setColumnWidths("90px", "");
    layout2.addLabel("Straatnaam");
    layout2.addData(new Label(importRegistrant.getStreet()));
    layout2.addLabel("Huisnummer");
    layout2.addData(new Label(importRegistrant.getHnr()));
    layout2.addLabel("Huisletter");
    layout2.addData(new Label(importRegistrant.getHnrL()));
    layout2.addLabel("Toevoeging");
    layout2.addData(new Label(importRegistrant.getHnrT()));
    layout2.addLabel("Postcode");
    layout2.addData(new Label(Postcode.getFormat(importRegistrant.getPostalcode())));
    layout2.addLabel("Woonplaats");
    layout2.addData(new Label(importRegistrant.getPlace()));

    Button button = new Button("Sluiten (Esc)", (ClickListener) clickEvent -> closeWindow());
    button.focus();

    setContent(new VLayout()
        .spacing(true)
        .margin(true)
        .add(button)
        .addExpandComponent(new ScrollLayout(new HLayout()
            .sizeFull()
            .add(layout1, layout2)
            .expand(layout1, 0.5F)
            .expand(layout2, 0.5F)
            .spacing(true))));
  }
}
