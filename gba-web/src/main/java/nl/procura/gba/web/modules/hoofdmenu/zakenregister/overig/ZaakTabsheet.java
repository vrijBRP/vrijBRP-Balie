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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.modules.beheer.parameters.container.TabVolgordeContainer;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.aantekening.ZaakAantekeningLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bijlage.ZaakBijlageLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.overige.tree.ZaakOverigeLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.status.ZaakStatusLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.task.TaskLayout;
import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZakenService;

public class ZaakTabsheet<T extends Zaak> extends GbaTabsheet {

  private final ButtonPageTemplate page;
  private T                        zaak;
  private ZaakAlgemeenLayout       algemeenLayout = null;

  public ZaakTabsheet(ButtonPageTemplate page, T zaak) {
    addStyleName("zaaktab");
    this.page = page;
    this.zaak = zaak;
  }

  @Override
  public void attach() {

    if (getComponentCount() == 0) {

      algemeenLayout = new ZaakAlgemeenLayout(getZaak());

      addOptieButtons();

      setExtraTopMargin();

      boolean zaakTabEerst = TabVolgordeContainer.SPECIFIEK.equalsIgnoreCase(
          getApplication().getParmValue(ParameterConstant.ZAKEN_TAB_VOLGORDE));

      if (zaakTabEerst) {

        // Custom tabs
        addTabs();

        // Algemene tab
        addTab(algemeenLayout, "Algemeen", null);
      } else {
        // Algemene tab
        addTab(algemeenLayout, "Algemeen", null);

        // Custom tabs
        addTabs();
      }

      // Extra tabs
      addStandaardTabs();
    }

    super.attach();
  }

  public OptieLayout getAlgemeneLayout() {
    return algemeenLayout;
  }

  public T getZaak() {
    return zaak;
  }

  public void reloadTabs() {

    reloadZaak();

    // Update Zaak in ZakenPage
    if (page instanceof ZakenPage) {
      ((ZakenPage) page).setZaak(zaak);
    }
    // Update ZaakTabLayouts
    for (LazyTab tab : getLazyTabs()) {
      if (tab.getComponent() instanceof ZaakTabLayout) {
        ZaakTabLayout zaakTabLayout = (ZaakTabLayout) tab.getComponent();
        zaakTabLayout.reloadLayout(getApplication(), zaak);
      }
    }
  }

  protected void addOptieButton(Button button) {
    algemeenLayout.getRight().addButton(button, page);
  }

  protected void addOptieButtons() {
  }

  protected void addTabs() {
  }

  protected void reloadTree() {
  }

  private void addStandaardTabs() {

    // StatussenTab
    ZaakStatusLayout statussenLayout = new ZaakStatusLayout(getZaak()) {

      @Override
      protected void reloadTree() {
        ZaakTabsheet.this.reloadTree();
      }
    };
    statussenLayout.setTab(addTab(statussenLayout, statussenLayout.getHeader(), null));

    // BijlagenTab
    ZaakBijlageLayout bijlagenLayout = new ZaakBijlageLayout(getZaak());
    bijlagenLayout.setTab(addTab(bijlagenLayout, bijlagenLayout.getHeader(getApplication()), null));

    // AantekeningenTab
    ZaakAantekeningLayout aantekeningLayout = new ZaakAantekeningLayout(getZaak());
    aantekeningLayout.setTab(addTab(aantekeningLayout, aantekeningLayout.getHeader(getApplication()), null));

    // TakenTab
    TaskLayout taakLayout = new TaskLayout(getZaak());
    taakLayout.setTab(addTab(taakLayout, taakLayout.getHeader(getApplication()), null));

    // OverigeTab
    ZaakOverigeLayout attributenLayout = new ZaakOverigeLayout(getZaak());
    addTab(attributenLayout, attributenLayout.getHeader(), null);
  }

  private void reloadZaak() {
    ZakenService zaken = getApplication().getServices().getZakenService();
    for (Zaak newZaak : zaken.getMinimaleZaken(new ZaakArgumenten(zaak))) {
      zaak = (T) zaken.getVolledigeZaak(newZaak);
    }
  }
}
