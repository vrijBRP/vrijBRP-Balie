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

package nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.*;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class PageBsRelaties<T extends ZaakDossier> extends BsPage<T> {

  public PageBsRelaties(String caption) {
    super(caption);
  }

  protected List<DossierPersoon> getDossierPersonen(DossierPersoon persoon) {

    List<DossierPersoon> relaties = new ArrayList<>();

    setTypePersonen(relaties, persoon, GBACat.HUW_GPS);
    setTypePersonen(relaties, persoon, GBACat.KINDEREN);
    setTypePersonen(relaties, persoon, GBACat.OUDER_1);
    setTypePersonen(relaties, persoon, GBACat.OUDER_2);

    return relaties;
  }

  private BasePLExt getPersoonPl(BasePLRec cat) {
    BasePLExt pl;
    if (cat.hasElems()) {
      BasePLValue bsn = cat.getElemVal(GBAElem.BSN);
      if (fil(bsn.getVal())) {
        pl = getPersoonslijst(new BsnFieldValue(bsn.getVal()));
        if (pl.getCats().size() > 0) {
          return pl;
        }
      }
      return new BasePLExt();
    }
    return null;
  }

  private DossierPersoon getTypePersoon(BasePLSet set, GBACat gbaCat) {

    DossierPersoon persoon = new DossierPersoon();
    BasePLRec cat = set.getLatestRec();
    BasePLExt pl = getPersoonPl(cat);

    if (pl == null || pl.getOverlijding().isOverleden()) {
      return persoon;
    }

    switch (gbaCat) {
      case OUDER_1:
        persoon = new DossierPersoon(MOEDER);
        break;

      case OUDER_2:
        persoon = new DossierPersoon(VADER_DUO_MOEDER);
        break;

      case HUW_GPS:
        persoon = new DossierPersoon(isHuidigePartner(cat) ? PARTNER : EXPARTNER);
        break;

      case KINDEREN:
        persoon = new DossierPersoon(KIND);

        break;

      default:
        break;
    }

    if (pl.getPersoon().getBsn().getVal().isEmpty()) {

      persoon.setGeslachtsnaam(cat.getElemVal(GBAElem.GESLACHTSNAAM).getVal());
      persoon.setVoorvoegsel(cat.getElemVal(GBAElem.VOORV_GESLACHTSNAAM).getVal());
      persoon.setVoornaam(cat.getElemVal(GBAElem.VOORNAMEN).getVal());
      persoon.setDatumGeboorte(new GbaDateFieldValue(cat.getElemVal(GBAElem.GEBOORTEDATUM)));

      if (GBACat.KINDEREN != cat.getCatType()) {
        persoon.setGeslacht(Geslacht.get(cat.getElemVal(GBAElem.GESLACHTSAAND).getVal()));
      }

      if (gbaCat == GBACat.HUW_GPS) {
        persoon.setDatumBurgerlijkeStaat(new DateTime(along(cat.getElemVal(
            GBAElem.DATUM_VERBINTENIS).getVal())));
      }

      if (gbaCat == GBACat.PERSOON) {
        persoon.setNaamgebruik(cat.getElemVal(GBAElem.AANDUIDING_NAAMGEBRUIK).getVal());
      }
    } else {
      BsPersoonUtils.kopieDossierPersoon(pl, persoon);
    }

    return persoon;
  }

  private boolean isHuidigePartner(BasePLRec cat) {
    return emp(cat.getElemVal(GBAElem.DATUM_ONTBINDING).getVal());
  }

  private void setTypePersonen(List<DossierPersoon> personen, DossierPersoon overledene, GBACat gbaCat) {

    if (overledene.isIngeschreven()) {

      BasePLExt overledenePl = getPersoonslijst(overledene.getBurgerServiceNummer());
      List<BasePLSet> sets = overledenePl.getCat(gbaCat).getSets();

      for (BasePLSet set : sets) {
        DossierPersoon persoon = getTypePersoon(set, gbaCat);
        if (persoon.isVolledig()) {
          personen.add(persoon);
        }
      }
    }
  }
}
