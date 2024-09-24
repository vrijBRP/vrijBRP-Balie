/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.behandelaar.page2;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.behandelaar.page2.Page2ZaakBehandelaarBean.BEHANDELAAR;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.behandelaar.page2.Page2ZaakBehandelaarBean.OPMERKING;

import java.util.List;

import nl.procura.gba.web.components.containers.GebruikerContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakBehandelaar;

public class Page2ZaakBehandelaarForm1 extends GbaForm<Page2ZaakBehandelaarBean> {

  public Page2ZaakBehandelaarForm1(ZaakBehandelaar zaakBehandelaar) {
    setOrder(BEHANDELAAR, OPMERKING);
    setColumnWidths(WIDTH_130, "");
    setReadonlyAsText(true);

    Page2ZaakBehandelaarBean bean = new Page2ZaakBehandelaarBean();
    bean.setBehandelaar(zaakBehandelaar.getBehandelaar());
    bean.setOpmerking(zaakBehandelaar.getOpm());
    setBean(bean);
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
    GbaComboBox field = getField(BEHANDELAAR, GbaComboBox.class);
    Services services = Services.getInstance();
    GebruikerService gebruikerService = services.getGebruikerService();
    List<Gebruiker> gebruikers = gebruikerService.getGebruikers(false);
    GebruikerContainer gebruikerContainer = new GebruikerContainer(gebruikers, false);
    field.setContainerDataSource(gebruikerContainer);
    field.setValue(new UsrFieldValue(services.getGebruiker()));
  }
}
