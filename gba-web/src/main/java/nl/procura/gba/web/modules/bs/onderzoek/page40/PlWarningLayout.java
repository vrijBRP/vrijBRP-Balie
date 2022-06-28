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

package nl.procura.gba.web.modules.bs.onderzoek.page40;

import static java.lang.String.format;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

public abstract class PlWarningLayout extends VerticalLayout {

  private InfoLayout             warningLayout = new InfoLayout("", "");
  private final DossierOnderzoek zaakDossier;
  private final boolean          resultaat;

  private final HLayout hLayout = new HLayout();
  private long          aantal  = 0;
  private final String  MSG     = "<br>Druk op de knop <b>Onderzoek / deelresultaat</b> voor meer informatie.";

  public PlWarningLayout(DossierOnderzoek zaakDossier, boolean resultaat) {

    this.zaakDossier = zaakDossier;
    this.resultaat = resultaat;
    setSpacing(true);
    addComponent(new Fieldset("Persoonslijsten in onderzoek of met een deelresultaat"));
    addComponent(hLayout.align(Alignment.MIDDLE_LEFT)
        .add(new Button("Herladen", e -> reload(true)))
        .addExpandComponent(warningLayout)
        .widthFull());
  }

  protected abstract void onGeenWijzigingen();

  public void reload(boolean message) {

    long aantal = zaakDossier.getAantalOnderzoek();
    InfoLayout newLayout = new InfoLayout("", "");

    if (aantal == 1) {
      warn(newLayout, "Eén persoonslijst heeft een onderzoek of deelresultaat");
    } else if (aantal > 0) {
      warn(newLayout, format("%d persoonslijsten hebben een onderzoek of deelresultaat. ", aantal));
    } else {
      info(newLayout, "Geen persoonslijsten hebben momenteel een onderzoek of deelresultaat. ");
    }

    hLayout.replaceComponent(warningLayout, newLayout);
    hLayout.expand(newLayout, 1f);
    warningLayout = newLayout;

    if (message) {
      if (aantal == this.aantal) {
        onGeenWijzigingen();
      }
    }
    this.aantal = aantal;
  }

  private void warn(InfoLayout newLayout, String msg) {
    if (resultaat) {
      newLayout.setIcon(ProcuraTheme.ICOON_24.WARNING);
      newLayout.setMessage(MiscUtils.setClass(false, msg) + MSG);
    } else {
      newLayout.setIcon(ProcuraTheme.ICOON_24.INFORMATION);
      newLayout.setMessage(msg + MSG);
    }
  }

  private void info(InfoLayout newLayout, String msg) {
    if (resultaat) {
      newLayout.setIcon(ProcuraTheme.ICOON_24.INFORMATION);
      newLayout.setMessage(msg + MSG);
    } else {
      newLayout.setIcon(ProcuraTheme.ICOON_24.WARNING);
      newLayout.setMessage(MiscUtils.setClass(false, msg) + MSG);
    }
  }
}
