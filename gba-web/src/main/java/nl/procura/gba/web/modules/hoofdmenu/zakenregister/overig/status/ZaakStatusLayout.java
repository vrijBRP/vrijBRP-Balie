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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.status;

import static java.util.Arrays.asList;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet.Tab;

import java.util.Collections;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabLayout;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;

public class ZaakStatusLayout extends GbaVerticalLayout implements ZaakTabLayout, ClickListener {

  private final Button buttonAdd = new Button("Status wijziging");

  private final HorizontalLayout hLayout;
  private final ZaakStatusTabel  tabel;
  private Zaak                   zaak;
  private Tab                    tab;

  public ZaakStatusLayout(Zaak zaak) {

    this.zaak = zaak;

    tabel = new ZaakStatusTabel(zaak);
    buttonAdd.addListener(this);

    hLayout = new HorizontalLayout();

    hLayout.addComponent(buttonAdd);
    hLayout.setComponentAlignment(buttonAdd, Alignment.BOTTOM_RIGHT);

    addComponent(hLayout);
    addComponent(tabel);
  }

  @Override
  public void buttonClick(ClickEvent event) {
    if (event.getButton() == buttonAdd) {
      onStatusToevoegen();
    }
  }

  public String getHeader() {
    return "Statussen (" + zaak.getZaakHistorie().getStatusHistorie().size() + ")";
  }

  @Override
  public void reloadLayout(GbaApplication application, Zaak zaak) {
    this.zaak = zaak;
    tabel.setZaak(zaak);
    tabel.init();
    tab.setCaption(getHeader());
  }

  public void setTab(Tab tab) {
    this.tab = tab;
  }

  protected void reloadTree() {
  }

  private void onReload() {

    tabel.init();
    tab.setCaption(getHeader());

    reloadTree();
  }

  private void onStatusToevoegen() {

    new ZaakStatusUpdater(getWindow(), Collections.singletonList(zaak)) {

      @Override
      protected void reload() {
        onReload();
      }
    };
  }
}
