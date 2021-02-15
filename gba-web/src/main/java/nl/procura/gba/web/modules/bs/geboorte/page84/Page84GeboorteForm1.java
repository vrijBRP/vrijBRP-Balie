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

package nl.procura.gba.web.modules.bs.geboorte.page84;

import static nl.procura.gba.web.modules.bs.geboorte.page84.Page84GeboorteBean1.GEMEENTE_MOEDER;
import static nl.procura.gba.web.modules.bs.geboorte.page84.Page84GeboorteBean1.GEMEENTE_VADER;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.Field;

import nl.procura.diensten.gba.ple.openoffice.formats.Geboorteformats;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page84GeboorteForm1 extends ReadOnlyForm {

  private DossierGeboorte dossierGeboorte;

  public Page84GeboorteForm1(DossierGeboorte dossierGeboorte) {

    this.dossierGeboorte = dossierGeboorte;

    setColumnWidths("170px", "350px", "130px", "");
    setCaptionAndOrder();

    DossierGeboorte d = getGeboorteDossier();
    Page84GeboorteBean1 b = new Page84GeboorteBean1();

    b.setNaamAangever(d.getAangever().getNaam().getTitel_voorv_gesl());
    b.setGeborenAangever(getGeboorte(d.getAangever()));
    b.setRedenVerplicht(d.getRedenVerplichtBevoegd().getOms());

    String reden = "";

    if (fil(d.getTardieveReden())) {
      reden = ", reden: " + d.getTardieveReden();
    }

    b.setTardieveAangifte((d.isTardieveAangifte() ? "Ja" : "Nee") + reden);

    b.setNaamMoeder(d.getMoeder().getNaam().getTitel_voorv_gesl());
    b.setGeborenMoeder(getGeboorte(d.getMoeder()));
    b.setGemeenteMoeder(d.getMoeder().getWoongemeente().getDescription());

    b.setNaamVader(d.getVader().getNaam().getTitel_voorv_gesl());
    b.setGemeenteVader(d.getVader().getWoongemeente().getDescription());
    b.setGeborenVader(getGeboorte(d.getVader()));

    b.setGezin(d.getGezinssituatie().getOms());
    b.setAfstammingsRecht(d.getLandAfstammingsRecht().getDescription());
    b.setNaamsRecht(d.getLandNaamRecht().getDescription());

    setBean(b);
  }

  @Override
  public Page84GeboorteBean1 getBean() {
    return (Page84GeboorteBean1) super.getBean();
  }

  public DossierGeboorte getGeboorteDossier() {
    return dossierGeboorte;
  }

  public void setGeboorteDossier(DossierGeboorte geboorteDossier) {
    this.dossierGeboorte = geboorteDossier;
  }

  @Override
  public Object getNewBean() {
    return new Page84GeboorteBean1();
  }

  public void setCaptionAndOrder() {
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(GEMEENTE_VADER, GEMEENTE_MOEDER)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }

  private String getGeboorte(DossierPersoon p) {

    Geboorteformats gf = new Geboorteformats();

    gf.setValues(p.getDatumGeboorte().getFormatDate(), p.getGeboorteplaats().getDescription(),
        p.getGeboorteland().getDescription());

    return gf.getDatum_plaats_land();
  }
}
