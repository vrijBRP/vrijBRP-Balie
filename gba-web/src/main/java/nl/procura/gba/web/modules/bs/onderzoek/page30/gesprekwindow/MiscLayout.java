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

import nl.procura.gba.web.components.listeners.ChangeListener;
import nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresLayout;
import nl.procura.gba.web.modules.bs.onderzoek.adreslayout.types.BronAdres;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoekBron;
import nl.procura.gba.web.services.bs.onderzoek.enums.VermoedAdresType;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class MiscLayout extends AbstractBronLayout {

  private MiscForm             form2;
  private AdresLayout          adresLayout;
  private DossierOnderzoekBron bron;

  public MiscLayout(DossierOnderzoekBron bron, ChangeListener successListener) {
    this.bron = bron;
    form2 = new MiscForm(bron, type -> setAdresType(type));

    adresLayout = new AdresLayout(new BronAdres(bron), persoon -> {
      form2.setPersoon(persoon);
      successListener.onChange();
    });

    addComponent(form2);
    addComponent(adresLayout);
    setAdresType(bron.getAdresType());
  }

  @Override
  public void reset() {
    form2.setBron(resetBron(bron));
    adresLayout.setForm(null);
  }

  @Override
  public void save() {
    form2.commit();
    bron.setAdresType(form2.getBean().getAdresType());
    bron.setInst(form2.getBean().getInstantie());
    bron.setInstAfdeling(form2.getBean().getAfdeling());
    bron.setInstAanhef(FieldValue.from(form2.getBean().getTavAanhef()).getStringValue());
    bron.setInstVoorl(form2.getBean().getTavVoorl());
    bron.setInstNaam(form2.getBean().getTavNaam());
    bron.setInstEmail(form2.getBean().getEmail());
    adresLayout.save();
  }

  private void setAdresType(VermoedAdresType type) {
    if (VermoedAdresType.IN_GEMEENTE.equals(type)) {
      adresLayout.setForm(AdresLayout.FormType.BINNEN_GEM);
    } else if (VermoedAdresType.ANDERE_GEMEENTE.equals(type)) {
      adresLayout.setForm(AdresLayout.FormType.BUITEN_GEM);
    } else if (VermoedAdresType.BUITENLAND.equals(type)) {
      adresLayout.setForm(AdresLayout.FormType.LAND);
    } else {
      adresLayout.setForm(null);
    }
  }
}
