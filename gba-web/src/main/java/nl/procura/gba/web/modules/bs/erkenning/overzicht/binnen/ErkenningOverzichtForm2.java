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

package nl.procura.gba.web.modules.bs.erkenning.overzicht.binnen;

import static nl.procura.gba.web.modules.bs.erkenning.overzicht.binnen.ErkenningOverzichtBean2.TOESTEMMING_RECHT_KIND;
import static nl.procura.gba.web.modules.bs.erkenning.overzicht.binnen.ErkenningOverzichtBean2.TOESTEMMING_RECHT_MOEDER;
import static nl.procura.standard.Globalfunctions.pos;

import com.vaadin.ui.Field;

import nl.procura.diensten.gba.ple.openoffice.formats.Geboorteformats;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class ErkenningOverzichtForm2 extends ReadOnlyForm<ErkenningOverzichtBean2> {

  public ErkenningOverzichtForm2(DossierErkenning erkenning) {

    setColumnWidths("180px", "300px", "60px", "");

    setCaptionAndOrder();

    ErkenningOverzichtBean2 bean = new ErkenningOverzichtBean2();

    if (erkenning != null) {

      bean.setNaamMoeder(erkenning.getMoeder().getNaam().getTitel_voorv_gesl());
      bean.setGeborenMoeder(getGeboorte(erkenning.getMoeder()));

      bean.setNaamErkenner(erkenning.getErkenner().getNaam().getTitel_voorv_gesl());
      bean.setGeborenErkenner(getGeboorte(erkenning.getErkenner()));

      bean.setAfstammingsRecht(erkenning.getLandAfstammingsRecht());
      bean.setToestemmingrechtMoeder(erkenning.getLandToestemmingsRechtMoeder());
      bean.setToestemmingrechtKind(erkenning.getLandToestemmingsRechtKind());
      bean.setToestemminggever(erkenning.getToestemming());
      bean.setNamenRecht(erkenning.getLandNaamRecht());
      bean.setNaamskeuze(erkenning.getNaamskeuzeType().toString());
      bean.setNaamskeuzePersoon(erkenning.getNaamskeuzePersoon().getType());
      bean.setKeuzegeslachtsnaam(erkenning.getKeuzeGeslachtsnaam());
      bean.setKeuzevoorv(erkenning.getKeuzeVoorvoegsel());
      bean.setKeuzetitel(erkenning.getKeuzeTitel());
      bean.setVerklaringGezag(erkenning.isVerklaringGezag() ? "Ja" : "Nee");
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
  public void setBean(Object bean) {

    super.setBean(bean);

    if (getField(TOESTEMMING_RECHT_MOEDER) != null) {
      getField(TOESTEMMING_RECHT_MOEDER).setVisible(pos(getBean().getToestemmingrechtMoeder().getValue()));
      repaint();
    }

    if (getField(TOESTEMMING_RECHT_KIND) != null) {
      getField(TOESTEMMING_RECHT_KIND).setVisible(pos(getBean().getToestemmingrechtKind().getValue()));
      repaint();
    }
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(TOESTEMMING_RECHT_MOEDER, TOESTEMMING_RECHT_KIND)) {
      column.setColspan(3);
    }
    super.setColumn(column, field, property);
  }

  @Override
  public ErkenningOverzichtBean2 getNewBean() {
    return new ErkenningOverzichtBean2();
  }
}
