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

package nl.procura.diensten.gbav.utils.vraag.afnemerindicaties;

import static nl.procura.standard.Globalfunctions.*;

import java.util.List;

import org.apache.commons.lang3.Validate;

import nl.bprbzk.gba.gba_v.vraag_v0.ArrayOfZoekparameter;
import nl.bprbzk.gba.gba_v.vraag_v0.VraagAIRequest;
import nl.bprbzk.gba.gba_v.vraag_v0.Zoekparameter;
import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;

public class AfnemerIndicatiesGbavVraagConverter {

  public static VraagAIRequest converteer(PLEArgs args) {

    Validate.isTrue(pos(args.getNumbers().size()), "Geen a-nummer of burgerservicenummer");
    Validate.notNull(args.getReasonForIndications(), "Geen opvraagreden");

    VraagAIRequest vraag = new VraagAIRequest();
    vraag.setParameters(new ArrayOfZoekparameter());

    addParameter(vraag, GBACat.PERSOON, GBAElem.ANR, args.getAnummers());
    addParameter(vraag, GBACat.PERSOON, GBAElem.BSN, args.getBsn());
    vraag.setOpvraagreden(args.getReasonForIndications());

    return vraag;
  }

  private static void addParameter(VraagAIRequest vraag, GBACat categorie, GBAElem element, String content) {

    if (fil(content)) {
      vraag.getParameters().getItem().add(
          new Zoekparameter(aval(formatElementKey(categorie.getCode(), element.getCode())), content));
    }
  }

  private static void addParameter(VraagAIRequest vraag, GBACat categorie, GBAElem element,
      List<String> content) {

    for (String s : content) {
      addParameter(vraag, categorie, element, s);
    }
  }

  private static String formatElementKey(int cat_nr, int element_nr) {
    return String.format("%02d%04d", cat_nr, element_nr);
  }
}
