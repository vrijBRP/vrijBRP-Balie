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

package nl.procura.diensten.gbav.utils.vraag.beperkt;

import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import nl.bprbzk.gba.lrdplus.version1.ArrayOfXsdInt;
import nl.bprbzk.gba.lrdplus.version1.ArrayOfZoekparameter;
import nl.bprbzk.gba.lrdplus.version1.Vraag2;
import nl.bprbzk.gba.lrdplus.version1.Zoekparameter;
import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gbav.utils.GbavAutorisatie;

public class BeperkteGbavVraagConverter {

  public static Vraag2 converteer(GbavAutorisatie autorisatie, PLEArgs args) {

    Vraag2 vraag = new Vraag2();
    vraag.setIndicatieAdresvraag(new Byte(args.isSearchOnAddress() ? "1" : "0"));
    vraag.setIndicatieZoekenInHistorie(new Byte("1"));
    vraag.setMasker(new ArrayOfXsdInt());
    vraag.setParameters(new ArrayOfZoekparameter());

    if (args.hasCat(GBACat.PERSOON)) {
      addCat(vraag, autorisatie, GBACat.PERSOON, args.isShowHistory());
    }

    if (args.hasCat(GBACat.OUDER_1)) {
      addCat(vraag, autorisatie, GBACat.OUDER_1, args.isShowHistory());
    }

    if (args.hasCat(GBACat.OUDER_2)) {
      addCat(vraag, autorisatie, GBACat.OUDER_2, args.isShowHistory());
    }

    if (args.hasCat(GBACat.NATIO)) {
      addCat(vraag, autorisatie, GBACat.NATIO, args.isShowHistory());
    }

    if (args.hasCat(GBACat.HUW_GPS)) {
      addCat(vraag, autorisatie, GBACat.HUW_GPS, args.isShowHistory());
    }

    if (args.hasCat(GBACat.OVERL)) {
      addCat(vraag, autorisatie, GBACat.OVERL, args.isShowHistory());
    }

    if (args.hasCat(GBACat.INSCHR)) {
      addCat(vraag, autorisatie, GBACat.INSCHR, args.isShowHistory());
    }

    if (args.hasCat(GBACat.VB)) {
      addCat(vraag, autorisatie, GBACat.VB, args.isShowHistory());
    }

    if (args.hasCat(GBACat.KINDEREN)) {
      addCat(vraag, autorisatie, GBACat.KINDEREN, args.isShowHistory());
    }

    if (args.hasCat(GBACat.VBTITEL)) {
      addCat(vraag, autorisatie, GBACat.VBTITEL, args.isShowHistory());
    }

    if (args.hasCat(GBACat.GEZAG)) {
      addCat(vraag, autorisatie, GBACat.GEZAG, args.isShowHistory());
    }

    if (args.hasCat(GBACat.REISDOC)) {
      addCat(vraag, autorisatie, GBACat.REISDOC, args.isShowHistory());
    }

    if (args.hasCat(GBACat.KIESR)) {
      addCat(vraag, autorisatie, GBACat.KIESR, args.isShowHistory());
    }

    if (args.hasCat(GBACat.AFNEMERS)) {
      addCat(vraag, autorisatie, GBACat.AFNEMERS, args.isShowHistory());
    }

    addParameter(vraag, autorisatie, GBACat.PERSOON, GBAElem.ANR, args.getAnummers());
    addParameter(vraag, autorisatie, GBACat.PERSOON, GBAElem.BSN, args.getBsn());
    addParameter(vraag, autorisatie, GBACat.PERSOON, GBAElem.GESLACHTSNAAM, args.getGeslachtsnaam());
    addParameter(vraag, autorisatie, GBACat.PERSOON, GBAElem.VOORNAMEN, args.getVoornaam());
    addParameter(vraag, autorisatie, GBACat.PERSOON, GBAElem.TITEL_PREDIKAAT, args.getTitel());
    addParameter(vraag, autorisatie, GBACat.PERSOON, GBAElem.VOORV_GESLACHTSNAAM, args.getVoorvoegsel());
    addParameter(vraag, autorisatie, GBACat.PERSOON, GBAElem.GEBOORTEDATUM, args.getGeboortedatum());
    addParameter(vraag, autorisatie, GBACat.PERSOON, GBAElem.GESLACHTSAAND, args.getGeslacht());

    addParameter(vraag, autorisatie, GBACat.VB, GBAElem.GEM_DEEL, args.getGemeentedeel());
    addParameter(vraag, autorisatie, GBACat.VB, GBAElem.GEM_INSCHR, args.getGemeente());
    addParameter(vraag, autorisatie, GBACat.VB, GBAElem.HNR, args.getHuisnummer());
    addParameter(vraag, autorisatie, GBACat.VB, GBAElem.HNR_L, args.getHuisletter());
    addParameter(vraag, autorisatie, GBACat.VB, GBAElem.HNR_T, args.getHuisnummertoevoeging());
    addParameter(vraag, autorisatie, GBACat.VB, GBAElem.POSTCODE, args.getPostcode());
    addParameter(vraag, autorisatie, GBACat.VB, GBAElem.STRAATNAAM, args.getStraat());

    return vraag;
  }

  private static void addCat(Vraag2 vraag, GbavAutorisatie autorisatie, GBACat categorie, boolean historie) {
    for (Integer element : autorisatie.getElements(categorie.getCode())) {
      vraag.getMasker().getItem().add(Integer.valueOf(formatElementKey(categorie.getCode(), element)));
      if (historie && autorisatie.getElements(categorie.getCode() + 50).contains(element)) {
        vraag.getMasker().getItem().add(Integer.valueOf(formatElementKey(categorie.getCode() + 50, element)));
      }
    }
  }

  private static void addParameter(Vraag2 vraag, GbavAutorisatie autorisatie, GBACat categorie,
      GBAElem element, String content) {

    if (fil(content)) {
      autorisatie.checkElement(categorie.getCode(), element.getCode());
      vraag.getParameters().getItem().add(
          new Zoekparameter(aval(formatElementKey(categorie.getCode(), element.getCode())),
              replaceWildcard(content)));
    }
  }

  private static String replaceWildcard(String content) {
    return content.replaceAll("%", "*");
  }

  private static void addParameter(Vraag2 vraag, GbavAutorisatie autorisatie, GBACat categorie,
      GBAElem element, List<String> content) {

    for (String s : content) {
      addParameter(vraag, autorisatie, categorie, element, s);
    }
  }

  private static String formatElementKey(int cat_nr, int element_nr) {
    return String.format("%02d%04d", cat_nr, element_nr);
  }
}
