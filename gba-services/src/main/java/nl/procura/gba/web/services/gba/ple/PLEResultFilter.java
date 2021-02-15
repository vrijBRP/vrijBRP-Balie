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

package nl.procura.gba.web.services.gba.ple;

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IND_ONJUIST;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.REG_BETREKK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.PROT_IGNORE_SEARCH;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZOEK_PLE_ADMIN_HIST;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.PLResultComposite;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.profiel.gba_categorie.PleCategorie;
import nl.procura.gba.web.services.beheer.profiel.gba_element.PleElement;
import nl.procura.gba.web.services.beheer.profiel.veld.ProfielVeld;
import nl.procura.standard.Globalfunctions;

public class PLEResultFilter {

  private static final String SECRET = "geheim";

  public PLResultComposite filter(Services services, PLResultComposite resultaat) {

    int secretCount = 0;
    for (BasePLExt pl : resultaat.getBasisPLWrappers()) {
      boolean isSecret = services.getGebruiker()
          .getProfielen()
          .isProfielVeld(ProfielVeld.PL_VERSTREKKINGSBEPERKING);

      if (pl.getPersoon().isGeheim() && !isSecret) {
        secretCount++;
        pl.setToonbeperking(true);
      }

      authorizePL(services.getGebruiker(), pl);
    }

    if (resultaat.getBasisPLWrappers().size() == 1) {
      String protIgnoreParameter = services.getGebruiker().getParameters().get(PROT_IGNORE_SEARCH).getValue();
      boolean ignoreLogin = Globalfunctions.isTru(protIgnoreParameter);
      if (!ignoreLogin) {
        services.getProtocolleringService().save(resultaat.getBasisPLWrappers().get(0));
      }
    }

    resultaat.getNumbers().put(SECRET, secretCount);
    return resultaat;
  }

  private void authorizePL(Gebruiker gebruiker, BasePLExt pl) {

    for (BasePLCat catSoort : pl.getCats()) {

      PleCategorie pleCategorie = new PleCategorie();
      pleCategorie.setCategory(toBigDecimal(catSoort.getCatType().getCode()));

      boolean isCategorieToegestaan = gebruiker.getProfielen().isGbaCategorie(pleCategorie);
      boolean isAdminHist = isTru(gebruiker.getParameters().get(ZOEK_PLE_ADMIN_HIST).getValue());

      for (BasePLSet catSet : new ArrayList<>(catSoort.getSets())) {

        // Skip entire set if no autorisation for stillborns
        if (hasStillborn(catSet) && !isStillbornsAllowed(gebruiker)) {
          catSoort.getSets().remove(catSet);
          continue;
        }

        boolean recordRemoved = false;
        for (BasePLRec catRecord : new ArrayList<>(catSet.getRecs())) {
          if (isSkipAdminHistorie(isAdminHist, catRecord)) {
            catSet.getRecs().remove(catRecord);
            recordRemoved = true;
            continue;
          }

          // Overslaan als categorie historisch is en deze niet is toegestaan
          if (isSkipHistorie(isCategorieToegestaan, catRecord)) {
            catSet.getRecs().remove(catRecord);
            recordRemoved = true;
            continue;
          }

          for (BasePLElem basisPlElement : catRecord.getElems()) {
            boolean isElementAllowed = isElementAllowed(gebruiker, basisPlElement);
            basisPlElement.getValue().setNotBlank(fil(basisPlElement.getValue().getDescr()));

            if (!isElementAllowed) {
              basisPlElement.getValue().setDescr(MiscUtils.LOCK_ICON); // Unicode lock icon
              basisPlElement.setAllowed(false);
            }
          }
        }

        if (recordRemoved) {
          hernummer(catSet.getRecs());
        }
      }
    }
  }

  private boolean isElementAllowed(Gebruiker gebruiker, BasePLElem basisPlElement) {
    PleElement pleElement = new PleElement();
    pleElement.setCategory(toBigDecimal(basisPlElement.getCatCode()));
    pleElement.setElement(toBigDecimal(basisPlElement.getElemCode()));
    return gebruiker.getProfielen().isGbaElement(pleElement);
  }

  private boolean isStillbornsAllowed(Gebruiker gebruiker) {
    PleElement pleElement = new PleElement();
    pleElement.setCategory(toBigDecimal(GBACat.KINDEREN.getCode()));
    pleElement.setElement(toBigDecimal(REG_BETREKK.getCode()));
    return gebruiker.getProfielen().isGbaElement(pleElement);
  }

  private boolean hasStillborn(BasePLSet catSet) {
    BasePLRec catRecord = catSet.getLatestRec();
    if (catRecord.isCat(GBACat.KINDEREN)) {
      return "L".equals(catRecord.getElemVal(REG_BETREKK).getVal());
    }
    return false;
  }

  private boolean isSkipHistorie(boolean isCategorieToegestaan, BasePLRec catRecord) {
    return catRecord.isStatus(GBARecStatus.HIST) && !isCategorieToegestaan;
  }

  private boolean isSkipAdminHistorie(boolean isAdminHist, BasePLRec catRecord) {
    return !isAdminHist && catRecord.isElem(IND_ONJUIST) &&
        catRecord.getElemVal(IND_ONJUIST).getVal().matches("[XY]");
  }

  /**
   * Er zijn historische records weggehaald. Hernummer de andere records
   */
  private void hernummer(List<BasePLRec> records) {
    int newVolgCode = records.size();
    for (BasePLRec record : records) {
      record.setIndex(newVolgCode--);
    }
  }
}
