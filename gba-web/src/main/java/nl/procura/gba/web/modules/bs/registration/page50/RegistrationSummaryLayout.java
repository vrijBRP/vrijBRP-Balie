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

package nl.procura.gba.web.modules.bs.registration.page50;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.*;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.bs.registration.page10.RegistrationAddress;
import nl.procura.gba.web.modules.bs.registration.person.PersonSummaryWindow;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.PresentievragenTable;
import nl.procura.gba.web.modules.zaken.woningkaart.window.WoningObjectWindow;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagService;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.vaadin.component.layout.Fieldset;

public class RegistrationSummaryLayout extends GbaVerticalLayout implements Button.ClickListener {

  private final Button        buttonObjectInfo = new Button("Verblijfsobject");
  private final OptieLayout   optionLayout     = new OptieLayout();
  private DossierRegistration dossier;

  public RegistrationSummaryLayout(DossierRegistration dossier, PresentievraagService presenceQService) {
    this.dossier = dossier;

    optionLayout.getLeft().addComponent(new RegistrationAddressForm(dossier));
    optionLayout.getRight().setWidth("180px");
    optionLayout.getRight().setCaption("Opties");
    optionLayout.getRight().addButton(buttonObjectInfo, this);

    addComponent(optionLayout);

    final List<DossierPersoon> people = dossier.getDossier().getPersonen(INSCHRIJVER, GERELATEERDE_BRP, AANGEVER);
    if (!people.isEmpty()) {
      final PeopleTable peopleTable = new PeopleTable(people, this::onDoubleClickPerson);
      addComponent(new Fieldset("Relevante personen", peopleTable));
      List<Presentievraag> presenceQuestions = new ArrayList<>();
      people.stream().map(presenceQService::getPresenceQuestionsByPerson).forEach(presenceQuestions::addAll);
      addComponent(new Fieldset("Presentievragen", new Table(presenceQuestions)));
    }
  }

  private void onDoubleClickPerson(final DossierPersoon person) {
    getParentWindow().addWindow(new PersonSummaryWindow(person, true));
  }

  @Override
  public void buttonClick(Button.ClickEvent event) {
    if (event.getButton() == buttonObjectInfo) {
      onObjectInfo();
    }
  }

  private void onObjectInfo() {
    Address address = new RelocationCaseAddress(new RegistrationAddress(dossier));
    getParentWindow().addWindow(new WoningObjectWindow(address));
  }

  public class Table extends PresentievragenTable {

    public Table(List<Presentievraag> presenceQuestions) {
      super(presenceQuestions);
    }

    @Override
    public void setColumns() {
      addColumn("Nr", 30);
      addColumn("Tijdstip", 150);
      addColumn("Bericht");
      addColumn("Resultaat", 300).setUseHTML(true);
    }
  }
}
