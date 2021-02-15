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

package nl.procura.gba.web.modules.bs.common.utils;

import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.Cat2OuderExt;
import nl.procura.diensten.gba.ple.extensions.Cat9KindExt;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.functies.BsUtils;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierNamenrecht;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.validation.Bsn;

public class BsOuderUtils extends BsUtils {

  /**
   * Geeft aantal eerdere kinderen.
   */
  public static int getEerdereKinderen(Services services, DossierNamenrecht dossier, boolean persoon2,
      boolean matchNamen) {

    String bsnPersoon1 = dossier.getMoeder().getBurgerServiceNummer().getStringValue();
    String bsnPersoon2 = dossier.getVaderErkenner().getBurgerServiceNummer().getStringValue();

    int count = 0;

    if (fil(bsnPersoon1)) {

      BasePLExt plMoeder = services.getPersonenWsService().getPersoonslijst(bsnPersoon1);

      if (fil(bsnPersoon2)) {

        for (Cat9KindExt kind : plMoeder.getKinderen().getKinderen()) {

          if (fil(kind.getBsn().getVal())) {

            BasePLExt plKind = services.getPersonenWsService().getPersoonslijst(
                kind.getBsn().getVal());

            for (Cat2OuderExt kindOuder : plKind.getOuders().getOuders()) {

              // kind heeft beide ouders en naam komt overeen met vader of moeder

              boolean isBsn = new Bsn(kindOuder.getBsn().getVal()).eq(
                  persoon2 ? bsnPersoon2 : bsnPersoon1);

              if (heeftBeideOuders(plKind, bsnPersoon1, bsnPersoon2) && isBsn) {

                Object kindNaam = kindOuder.getNaam().getGeslachtsnaam().getValue().getVal();
                Object plKindNaam = plKind.getPersoon().getNaam().getGeslachtsnaam().getValue().getVal();

                if (!matchNamen || kindNaam.equals(plKindNaam)) {
                  count++;
                }
              }
            }
          }
        }
      }
    }

    return count;
  }

  public static DossierPersoon getOuder(Services services, DossierPersoon ouder, DossierPersoon partner,
      GBACat categorie) {
    BsnFieldValue bsn = partner.getBurgerServiceNummer();
    if (!ouder.isVolledig() && bsn.isCorrect()) {

      BasePLExt pl = getPl(services, bsn);
      BasePLRec ouderCat = pl.getLatestRec(categorie);

      if (ouderCat.hasElems()) {
        BasePLValue bsnOuder = ouderCat.getElemVal(GBAElem.BSN);

        if (fil(bsnOuder.getVal())) {
          BasePLExt plOuder = getPl(services, new BsnFieldValue(bsnOuder.getVal()));

          if (plOuder.getCats().size() > 0) {
            BsPersoonUtils.kopieDossierPersoon(plOuder, ouder);
            return ouder;
          }
        }

        ouder.setGeslachtsnaam(ouderCat.getElemVal(GBAElem.GESLACHTSNAAM).getVal());
        ouder.setVoorvoegsel(ouderCat.getElemVal(GBAElem.VOORV_GESLACHTSNAAM).getVal());
        ouder.setVoornaam(ouderCat.getElemVal(GBAElem.VOORNAMEN).getVal());
        ouder.setNaamgebruik("");
        ouder.setGeslacht(Geslacht.get(ouderCat.getElemVal(GBAElem.GESLACHTSAAND).getVal()));
      }
    }

    return ouder;
  }

  public static DossierPersoon getOuder(Services services, DossierPersoon partner, GBACat categorie) {
    DossierPersoon ouder = new DossierPersoon();
    getOuder(services, ouder, partner, categorie);
    return ouder;
  }

  /**
   * Dit kind heeft ook beide ouders
   */
  private static boolean heeftBeideOuders(BasePLExt plKind, String bsnPersoon1, String bsnPersoon2) {

    int count = 0;

    for (Cat2OuderExt kindOuder : plKind.getOuders().getOuders()) {

      if (new Bsn(kindOuder.getBsn().getVal()).eq(bsnPersoon2)) {
        count++;
      }

      if (new Bsn(kindOuder.getBsn().getVal()).eq(bsnPersoon1)) {
        count++;
      }
    }

    return count >= 2;
  }
}
