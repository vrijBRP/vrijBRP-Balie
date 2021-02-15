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

package nl.procura.diensten.gbav.utils.vraag.volledig;

import static nl.procura.burgerzaken.gba.core.enums.GBARecStatus.CURRENT;
import static nl.procura.burgerzaken.gba.core.enums.GBARecStatus.HIST;
import static nl.procura.standard.Globalfunctions.aval;

import java.util.List;

import nl.bprbzk.gba.gba_v.lo3_v1.*;
import nl.bprbzk.gba.gba_v.vraag_v0.Resultaat;
import nl.bprbzk.gba.gba_v.vraag_v0.VraagResponse;
import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.base.BasePLBuilder;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gbav.utils.GbavResultaat;
import nl.procura.diensten.gbav.utils.vraag.GbavAntwoordConverter;
import nl.procura.diensten.gbav.utils.vraag.GbavVraagAntwoord;

public class VolledigeGbavAntwoordConverter extends GbavAntwoordConverter {

  public static void convert(Resultaat gbavResultaat, GbavResultaat resultaat) {

    if (gbavResultaat != null) {
      resultaat.setCode(gbavResultaat.getCode());
      resultaat.setLetter(gbavResultaat.getLetter());
      resultaat.setOmschrijving(parseResultaatOmschrijving(gbavResultaat.getOmschrijving()));
      resultaat.setReferentie(gbavResultaat.getReferentie());
    }
  }

  public static void convert(VraagResponse antwoord,
      GbavVraagAntwoord vraagAntwoord,
      BasePLBuilder plBuilder) {

    vraagAntwoord.setBasisPLHandler(plBuilder);
    convert(antwoord.getResultaat(), vraagAntwoord.getResultaat());
    convert(antwoord, plBuilder);
  }

  private static void convert(VraagResponse gbavAntwoord, BasePLBuilder plBuilder) {
    ArrayOfPL gbavPersoonslijsten = gbavAntwoord.getPersoonslijsten();
    if (gbavPersoonslijsten != null) {
      List<PL> persoonslijsten = gbavPersoonslijsten.getItem();
      if (persoonslijsten != null) {
        for (PL pl : persoonslijsten) {
          plBuilder.addNewPL(PLEDatasource.GBAV);
          for (Categoriestapel gbavCat : pl.getCategoriestapels().getItem()) {
            for (Categorievoorkomen gbavSet : gbavCat.getCategorievoorkomens().getItem()) {
              int categorieNummer = gbavSet.getCategorienummer();
              GBARecStatus status = (categorieNummer < 50) ? CURRENT : HIST;
              GBACat cat = GBACat.getByCode(categorieNummer);
              plBuilder.addCat(cat, status).getCatType().getCode();
              for (Element element : gbavSet.getElementen().getItem()) {
                plBuilder.setElem(GBAElem.getByCode(aval(element.getNummer())), new BasePLValue(element.getWaarde()));
                checkNummer(plBuilder, element.getNummer(), element.getWaarde());
              }
            }
          }
          plBuilder.finishPL();
        }
      }
    }
  }
}
