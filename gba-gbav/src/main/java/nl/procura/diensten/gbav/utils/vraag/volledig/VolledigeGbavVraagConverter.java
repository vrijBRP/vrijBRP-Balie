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

import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import nl.bprbzk.gba.gba_v.vraag_v0.ArrayOfZoekparameter;
import nl.bprbzk.gba.gba_v.vraag_v0.VraagPLRequest;
import nl.bprbzk.gba.gba_v.vraag_v0.Zoekparameter;
import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;

public class VolledigeGbavVraagConverter {

  public static VraagPLRequest converteer(PLEArgs args) {

    VraagPLRequest vraag = new VraagPLRequest();
    vraag.setParameters(new ArrayOfZoekparameter());

    addParameter(vraag, GBACat.PERSOON, GBAElem.ANR, args.getAnummers());
    addParameter(vraag, GBACat.PERSOON, GBAElem.BSN, args.getBsn());
    addParameter(vraag, GBACat.PERSOON, GBAElem.GESLACHTSNAAM, args.getGeslachtsnaam());
    addParameter(vraag, GBACat.PERSOON, GBAElem.VOORNAMEN, args.getVoornaam());
    addParameter(vraag, GBACat.PERSOON, GBAElem.TITEL_PREDIKAAT, args.getTitel());
    addParameter(vraag, GBACat.PERSOON, GBAElem.VOORV_GESLACHTSNAAM, args.getVoorvoegsel());
    addParameter(vraag, GBACat.PERSOON, GBAElem.GEBOORTEDATUM, args.getGeboortedatum());
    addParameter(vraag, GBACat.PERSOON, GBAElem.GESLACHTSAAND, args.getGeslacht());

    addParameter(vraag, GBACat.VB, GBAElem.GEM_DEEL, args.getGemeentedeel());
    addParameter(vraag, GBACat.VB, GBAElem.GEM_INSCHR, args.getGemeente());
    addParameter(vraag, GBACat.VB, GBAElem.HNR, args.getHuisnummer());
    addParameter(vraag, GBACat.VB, GBAElem.HNR_L, args.getHuisletter());
    addParameter(vraag, GBACat.VB, GBAElem.HNR_T,
        args.getHuisnummertoevoeging());
    addParameter(vraag, GBACat.VB, GBAElem.POSTCODE, args.getPostcode());
    addParameter(vraag, GBACat.VB, GBAElem.STRAATNAAM, args.getStraat());

    return vraag;
  }

  private static void addParameter(VraagPLRequest vraag, GBACat categorie, GBAElem element, String content) {

    if (fil(content)) {
      vraag.getParameters().getItem().add(
          new Zoekparameter(aval(formatElementKey(categorie.getCode(), element.getCode())), content));
    }
  }

  private static void addParameter(VraagPLRequest vraag, GBACat categorie, GBAElem element,
      List<String> content) {

    for (String s : content) {
      addParameter(vraag, categorie, element, s);
    }
  }

  private static String formatElementKey(int cat_nr, int element_nr) {
    return String.format("%02d%04d", cat_nr, element_nr);
  }
}
