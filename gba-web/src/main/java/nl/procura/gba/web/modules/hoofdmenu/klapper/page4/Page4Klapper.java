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

package nl.procura.gba.web.modules.hoofdmenu.klapper.page4;

import static ch.lambdaj.Lambda.join;
import static nl.procura.standard.Globalfunctions.astr;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.form.document.PrintMultiLayout;
import nl.procura.gba.web.modules.hoofdmenu.klapper.PageKlapperTemplate;
import nl.procura.gba.web.modules.hoofdmenu.klapper.page4.klapper.KlapperTelling;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.akte.KlapperZoekargumenten;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page4Klapper extends PageKlapperTemplate {

  private final List<DossierAkte>     aktes;
  private final KlapperZoekargumenten zoekargumenten;
  private PrintMultiLayout            printLayout;

  public Page4Klapper(List<DossierAkte> aktes, KlapperZoekargumenten zoekargumenten) {
    super("Klapper - afdrukken");
    this.aktes = aktes;
    this.zoekargumenten = zoekargumenten;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      KlapperTelling telling = new KlapperTelling(aktes);
      telling.setTitel(zoekargumenten.getVolgorde().getType() + " tafel op de " + getAktes(aktes));
      telling.setGemeente(getApplication().getServices().getGebruiker().getGemeente());
      telling.setPeriode(zoekargumenten.getPeriodes());
      telling.setVolgorde(astr(zoekargumenten.getVolgorde()));

      printLayout = new PrintMultiLayout(telling, null, null, DocumentType.KLAPPER);
      printLayout.setInfo("");

      addButton(buttonPrev);
      addButton(printLayout.getButtons());

      addComponent(printLayout);
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    printLayout.handleActions(button, keyCode);
    super.handleEvent(button, keyCode);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  private String getAktes(List<DossierAkte> aktes) {

    StringBuilder text = new StringBuilder();
    Set<String> types = new HashSet<>();

    for (DossierAkte akte : aktes) {
      types.add(akte.getAkteRegistersoort().getOms());
    }

    text.append(aktes.size() == 1 ? " akte van " : " akten van ");
    text.append(join(types));

    return text.toString();
  }
}
