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

package nl.procura.gba.web.modules.bs.common.pages.residentpage;

import java.util.List;
import java.util.function.Function;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainers;

import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.gba.web.common.misc.SelectListener;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisAdres;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.zaken.algemeen.consent.ConsentProvider;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class ResidentPageMock extends ResidentPage {

  private Services                   services;
  private FindResidentForm           residentForm;
  private ResidentPage.ResidentTable residentTable;

  public ResidentPageMock(VerhuisAdres destinationAddress, ConsentProvider relationship,
      SelectListener<ConsentProvider> listener,
      Function<PLEArgs, List<Relatie>> searchResidents) {
    super(destinationAddress, relationship, listener, searchResidents);
  }

  public void mockServices(Services services) {
    this.services = services;
  }

  @Override
  public Services getServices() {
    return services == null ? super.getServices() : services;
  }

  @Override
  public void event(PageEvent event) {
    super.event(event);
    if (event instanceof InitPage) {
      List<Component> allComponents = ComponentContainers.allComponents(this);
      residentForm = allComponents.stream()
          .filter(c -> c instanceof FindResidentForm)
          .map(c -> (FindResidentForm) c)
          .findFirst().orElseThrow(IllegalStateException::new);
      residentTable = allComponents.stream()
          .filter(c -> c instanceof ResidentPage.ResidentTable)
          .map(c -> (ResidentPage.ResidentTable) c)
          .findFirst().orElseThrow(IllegalStateException::new);
    }
  }

  public FindResidentForm residentForm() {
    return residentForm;
  }

  public ResidentTable residentTable() {
    return residentTable;
  }
}
