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

package nl.procura.gba.web.modules.bs.onderzoek.page30.gesprekwindow;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab3.Tab3DocumentWindow;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoekBron;
import nl.procura.gba.web.services.bs.onderzoek.enums.VermoedAdresType;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class StakeholderLayout extends AbstractBronLayout {

  private StakeholderForm      form4;
  private DossierOnderzoekBron bron;

  public StakeholderLayout(NormalPageTemplate page, DossierOnderzoekBron bron) {
    this.bron = bron;
    form4 = new StakeholderForm(bron, page.getApplication()) {

      @Override
      protected void onButtonStakeholder() {
        page.getParentWindow().addWindow(new Tab3DocumentWindow() {

          @Override
          public void closeWindow() {
            form4.updateAfnemers();
            super.closeWindow();
          }
        });
      }
    };

    addComponent(form4);
  }

  @Override
  public void reset() {
    form4.setBron(resetBron(bron));
  }

  @Override
  public void save() {
    form4.commit();
    bron.setInst(form4.getBean().getInstantie());
    bron.setInstAfdeling(form4.getBean().getAfdeling());
    bron.setInstAanhef(FieldValue.from(form4.getBean().getTavAanhef()).getStringValue());
    bron.setInstVoorl(form4.getBean().getTavVoorl());
    bron.setInstNaam(form4.getBean().getTavNaam());
    bron.setInstEmail(form4.getBean().getEmail());
    bron.setAdresType(VermoedAdresType.ONBEKEND);
    bron.setAdr(form4.getBean().getAdres());
    bron.setPc(form4.getBean().getPc().getStringValue());
    bron.setPlaats(form4.getBean().getPlaats());
  }
}
