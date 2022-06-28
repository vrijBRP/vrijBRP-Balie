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

package nl.procura.gba.web.modules.zaken.verkiezing.page2;

import static nl.procura.gba.web.services.beheer.verkiezing.KiezersregisterActieType.ACT_VERWIJDEREN;

import nl.procura.gba.jpa.personen.db.KiesrWijz;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.beheer.verkiezing.KiezersregisterService;
import nl.procura.gba.web.services.beheer.verkiezing.Stempas;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.ProcuraTheme;
import nl.procura.validation.Anummer;

public class Page2Verkiezing extends NormalPageTemplate {

  private Page2VerkiezingForm form = null;
  private final Stempas       stempas;

  public Page2Verkiezing(Stempas stempas) {
    this.stempas = stempas;
    addButton(buttonSave, 1f);
    addButton(buttonClose);
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {
    if (event instanceof InitPage) {
      form = new Page2VerkiezingForm(stempas);
      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onSave() {
    form.commit();
    Page2VerkiezingBean bean = form.getBean();
    KiezersregisterService service = getServices().getKiezersregisterService();
    KiesrWijz wijziging = service.getNieuweWijziging(stempas.getStem(),
        bean.getActie(),
        bean.getOpmerking());

    // Vervangende stempas verwijderen
    boolean vervTerugdraaien = ACT_VERWIJDEREN == bean.getActie() && stempas.isVervangen();
    if (vervTerugdraaien && service.getVervangendeStempas(stempas).isPresent()) {
      getParentWindow().addWindow(getVervangingTerugdraaienDialog(() -> {
        service.verwijderAanduiding(stempas, wijziging);
        Page2Verkiezing.this.getParentWindow().closeWindow();
        getWindow().closeWindow();
      }));
    } else {
      stempas.setAanduiding(bean.getActie().getAanduidingType());
      switch (bean.getActie()) {
        case ACT_VERWIJDEREN:
          stempas.setAnrGemachtigde(new Anummer());
          service.save(stempas.getStem(), wijziging);
          break;
        case ACT_TOEVOEGEN:
          AnrFieldValue anr = new AnrFieldValue(getPl().getPersoon().getAnr().getVal());
          stempas.getStem().setAnr(anr.getLongValue());
          service.toevoegen(stempas.getStem(), wijziging);
          break;
        case ACT_MACHTIGEN:
          stempas.setAnrGemachtigde(form.getAnrGemachtigde());
          service.save(stempas.getStem(), wijziging);
          break;
        case ACT_VERVANGEN:
          service.vervangen(stempas.getStem(), wijziging);
          break;
        default:
          service.save(stempas.getStem(), wijziging);
          break;
        case ACT_ONBEKEND:
          break;
      }

      getWindow().closeWindow();
    }
  }

  private ConfirmDialog getVervangingTerugdraaienDialog(Runnable onYes) {
    return new ConfirmDialog("Weet u het zeker?",
        "Hierdoor wordt de vervangende stempas verwijderd. Doorgaan?",
        "450px", ProcuraTheme.ICOON_24.WARNING) {

      @Override
      public void buttonYes() {
        onYes.run();
        super.buttonYes();
      }
    };
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }
}
