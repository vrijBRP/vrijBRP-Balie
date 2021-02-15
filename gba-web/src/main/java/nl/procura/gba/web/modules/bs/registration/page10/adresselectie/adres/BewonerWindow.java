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

package nl.procura.gba.web.modules.bs.registration.page10.adresselectie.adres;

import java.util.ArrayList;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.gba.web.common.misc.SelectListener;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.bs.common.pages.residentpage.ResidentPage;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisAdres;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.zaken.algemeen.consent.ConsentProvider;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class BewonerWindow extends GbaModalWindow {

  public BewonerWindow(VerhuisAdres destinationAddress, ConsentProvider relationship,
      final SelectListener<ConsentProvider> listener) {
    super("Selecteer personen op basis van adres", "1300px");
    addComponent(new MainModuleContainer(false,
        new ResidentPage(destinationAddress, relationship, listener, this::getRelations)));
  }

  private List<Relatie> getRelations(PLEArgs plArg) {
    final List<Relatie> relations = new ArrayList<>();
    final PersonenWsService gbaWs = getGbaApplication().getServices().getPersonenWsService();
    final List<BasePLExt> bpls = gbaWs.getPersoonslijsten(plArg, false).getBasisPLWrappers();

    for (final BasePLExt bpl : bpls) {
      final Relatie relatie = new Relatie();
      relatie.setPl(bpl);

      final boolean isOpgeschort = bpl.getPersoon().getStatus().isOpgeschort();
      if (!isOpgeschort) {
        relations.add(relatie);
      }

    }
    return relations;
  }
}
