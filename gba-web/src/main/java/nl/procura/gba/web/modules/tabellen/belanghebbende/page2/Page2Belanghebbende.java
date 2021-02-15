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

package nl.procura.gba.web.modules.tabellen.belanghebbende.page2;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.gba.basistabellen.belanghebbende.Belanghebbende;
import nl.procura.gba.web.services.gba.basistabellen.belanghebbende.BelanghebbendeType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2Belanghebbende extends NormalPageTemplate {

  private Page2BelanghebbendeForm form           = null;
  private Belanghebbende          belanghebbende = null;

  public Page2Belanghebbende() {
    super("Toevoegen / muteren belanghebbende");
    setNieuweBelanghebbende();
    setButtons();
  }

  public Page2Belanghebbende(Belanghebbende belanghebbende) {
    super("Toevoegen / muteren belanghebbende");
    this.belanghebbende = belanghebbende;
    setButtons();
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      form = new Page2BelanghebbendeForm(belanghebbende);
      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onNew() {
    form.reset();
    setNieuweBelanghebbende();
    form.updateBean(belanghebbende);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    form.commit();

    Page2BelanghebbendeBean b = form.getBean();

    belanghebbende.setBelanghebbendeType(b.getType());
    belanghebbende.setNaam(b.getNaam());
    belanghebbende.setTerAttentieVanAanhef(b.getTavAanhef());
    belanghebbende.setTavVoorl(b.getTavVoorl());
    belanghebbende.setTavNaam(b.getTavNaam());
    belanghebbende.setAdres(b.getAdres());
    belanghebbende.setPostcode(b.getPostcode());
    belanghebbende.setPlaats(b.getPlaats());
    belanghebbende.setLand(b.getLand());
    belanghebbende.setTelefoon(b.getTelefoon());
    belanghebbende.setEmail(b.getEmail());
    belanghebbende.setDatumIngang(new DateTime(b.getIngangGeld().getLongValue()));
    belanghebbende.setDatumEinde(new DateTime(b.getEindeGeld().getLongValue()));

    getServices().getBelanghebbendeService().save(belanghebbende);

    successMessage("De gegevens zijn opgeslagen.");

    super.onSave();
  }

  private void setButtons() {
    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonSave);
  }

  private void setNieuweBelanghebbende() {
    belanghebbende = new Belanghebbende();
    belanghebbende.setBelanghebbendeType(BelanghebbendeType.ADVOCATEN_KANTOOR);
    belanghebbende.setLand(Landelijk.getNederland());
  }
}
