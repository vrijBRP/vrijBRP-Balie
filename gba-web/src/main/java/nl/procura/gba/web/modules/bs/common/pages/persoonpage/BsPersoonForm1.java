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

package nl.procura.gba.web.modules.bs.common.pages.persoonpage;

import static nl.procura.gba.common.ZaakType.LIJKVINDING;
import static nl.procura.gba.common.ZaakType.OVERLIJDEN_IN_GEMEENTE;
import static nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsPersoonBean1.AKTENAAM;
import static nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsPersoonBean1.BSN;
import static nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsPersoonBean1.GESLACHT;
import static nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsPersoonBean1.NAAM;
import static nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsPersoonBean1.STATUS;
import static nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsPersoonBean1.TITEL;
import static nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsPersoonBean1.VOORN;
import static nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsPersoonBean1.VOORV;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.AANGEVER;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.AMBTENAAR;
import static nl.procura.gba.web.services.gba.functies.PersoonslijstStatus.getStoredStatus;

import com.vaadin.ui.Field;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.lazyloading.LazyLayout;
import nl.procura.gba.web.components.layouts.page.StatusButton;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.gba.functies.PersoonslijstStatus;
import nl.procura.gba.web.services.gba.ple.opslag.PersoonStatusOpslagEntry;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;
import nl.procura.validation.Bsn;

public class BsPersoonForm1 extends BsPersoonForm {

  private final GbaApplication gbaApplication;

  public BsPersoonForm1(GbaApplication gbaApplication, Dossier dossier, DossierPersoon dossierPersoon,
      BsPersoonRequirement requirement) {

    super(dossierPersoon, requirement);
    this.gbaApplication = gbaApplication;
    DossierPersoonType persoonType = dossierPersoon.getDossierPersoonType();
    setCaption("Persoonsgegevens van " + persoonType.getDescrExtra());

    if (persoonType.is(AMBTENAAR)) {
      setOrder(NAAM, BSN, VOORV, TITEL, VOORN, GESLACHT, AKTENAAM, STATUS);

    } else if (dossier.getType().is(OVERLIJDEN_IN_GEMEENTE, LIJKVINDING) && persoonType.is(AANGEVER)) {
      setOrder(NAAM, BSN, VOORV, TITEL, VOORN, GESLACHT);

    } else {
      setOrder(NAAM, BSN, VOORV, TITEL, VOORN, GESLACHT, STATUS);
    }

    update();
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {

    if (property.is(STATUS)) {

      Bsn bsn = new Bsn(getBean().getBsn());

      if (bsn.isCorrect()) {

        final BasePLExt pl = gbaApplication.getServices().getPersonenWsService().getPersoonslijst(
            bsn.getDefaultBsn());

        if (pl != null) {
          PersoonStatusOpslagEntry status = getStoredStatus(gbaApplication.getServices(), pl);
          final StatusButton button = new StatusButton(status != null ? status.getStatus() : "", pl);

          if (status != null) {
            column.addComponent(button);

          } else {
            LazyLayout lazyLayout = new LazyLayout(button, 200, "500px", "25px") {

              @Override
              public void onVisible() {
                button.setCaption(PersoonslijstStatus.getStatus(gbaApplication.getServices(), pl));
              }

            }.setIndicatorLeft();

            column.addComponent(lazyLayout);
          }
        }
      }
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(AKTENAAM, STATUS)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }
}
