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

package nl.procura.gba.web.modules.bs.naamskeuze.overzicht.binnen;

import nl.procura.diensten.gba.ple.openoffice.formats.Geboorteformats;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;

public class NaamskeuzeOverzichtForm2 extends ReadOnlyForm<NaamskeuzeOverzichtBean2> {

  public NaamskeuzeOverzichtForm2(DossierNaamskeuze naamskeuze) {
    setColumnWidths("160px", "250px", "60px", "");
    setCaptionAndOrder();
    NaamskeuzeOverzichtBean2 bean = new NaamskeuzeOverzichtBean2();

    if (naamskeuze != null) {
      bean.setNaamMoeder(naamskeuze.getMoeder().getNaam().getTitel_voorv_gesl());
      bean.setGeborenMoeder(getGeboorte(naamskeuze.getMoeder()));
      bean.setNaamPartner(naamskeuze.getPartner().getNaam().getTitel_voorv_gesl());
      bean.setGeborenPartner(getGeboorte(naamskeuze.getPartner()));
      bean.setNamenRecht(naamskeuze.getLandNaamRecht());
      bean.setNaamskeuze(naamskeuze.getNaamskeuzeType().toString());
      bean.setNaamskeuzePersoon(naamskeuze.getNaamskeuzePersoon().getType());
      bean.setKeuzegeslachtsnaam(naamskeuze.getKeuzeGeslachtsnaam());
      bean.setKeuzevoorv(naamskeuze.getKeuzeVoorvoegsel());
      bean.setKeuzetitel(naamskeuze.getKeuzeTitel());
    }

    setBean(bean);
  }

  public void setCaptionAndOrder() {
  }

  private String getGeboorte(DossierPersoon p) {
    Geboorteformats gf = new Geboorteformats();
    gf.setValues(p.getDatumGeboorte().getFormatDate(), p.getGeboorteplaats().getDescription(),
        p.getGeboorteland().getDescription());
    return gf.getDatum_plaats_land();
  }

  @Override
  public NaamskeuzeOverzichtBean2 getNewBean() {
    return new NaamskeuzeOverzichtBean2();
  }
}
