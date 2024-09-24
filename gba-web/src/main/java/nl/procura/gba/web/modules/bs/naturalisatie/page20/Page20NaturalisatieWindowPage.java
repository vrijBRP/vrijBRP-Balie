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

package nl.procura.gba.web.modules.bs.naturalisatie.page20;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.HUW_GPS;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.KINDEREN;
import static nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsGerelateerdenUtils.getTypePersonen;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.KIND;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.MEDE_VERZOEKER_KIND;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.MEDE_VERZOEKER_PARTNER;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.PARTNER;

import java.util.List;
import java.util.stream.Collectors;

import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatie;
import nl.procura.vaadin.component.label.H3;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page20NaturalisatieWindowPage extends ButtonPageTemplate {

  private final Page20Naturalisatie page;
  private final DossierPersoon      persoon;

  public Page20NaturalisatieWindowPage(Page20Naturalisatie page, DossierPersoon persoon) {
    this.page = page;
    this.persoon = persoon;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      H3 h3 = new H3("Gerelateerden die in aanmerking komen als mede-verzoeker");

      addButton(buttonClose);
      setSpacing(true);
      setInfo("Dubbelklik om een persoon te selecteren.");

      getButtonLayout().addComponent(h3, getButtonLayout().getComponentIndex(buttonClose));
      getButtonLayout().setExpandRatio(h3, 1f);
      getButtonLayout().setWidth("100%");

      addComponent(new GerelateerdenTable(page.getZaakDossier()));
    }

    super.event(event);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  private void addToList(DossierPersoon dossierPersoon) {
    page.toevoegenMedeVerzoeker(dossierPersoon);
  }

  private List<DossierPersoon> getMedeVerzoekers(DossierPersoon persoon) {
    return getTypePersonen(getServices(), persoon, HUW_GPS, KINDEREN)
        .stream()
        .filter(this::komtInAanmerkingNaturalisatie)
        .map(this::setPersoonType)
        .collect(Collectors.toList());
  }

  private DossierPersoon setPersoonType(DossierPersoon persoon) {
    if (persoon.getDossierPersoonType().is(KIND)) {
      persoon.setDossierPersoonType(MEDE_VERZOEKER_KIND);
    }
    if (persoon.getDossierPersoonType().is(PARTNER)) {
      persoon.setDossierPersoonType(MEDE_VERZOEKER_PARTNER);
    }
    return persoon;
  }

  private boolean komtInAanmerkingNaturalisatie(DossierPersoon persoon) {
    return persoon != null
        && !persoon.isOverleden()
        && persoon.getDossierPersoonType().is(KIND, PARTNER, MEDE_VERZOEKER_KIND, MEDE_VERZOEKER_PARTNER);
  }

  class GerelateerdenTable extends Page20NaturalisatieTable {

    public GerelateerdenTable(DossierNaturalisatie naturalisatie) {
      super(naturalisatie);
    }

    @Override
    public void onDoubleClick(Record record) {
      addToList(record.getObject(DossierPersoon.class));
      addDossierPersonen(getMedeVerzoekers(persoon));
      super.onDoubleClick(record);
    }

    @Override
    public void setRecords() {
      addDossierPersonen(getMedeVerzoekers(persoon));
      super.setRecords();
    }

    @Override
    protected boolean isMetCheckboxes() {
      return true;
    }
  }
}
