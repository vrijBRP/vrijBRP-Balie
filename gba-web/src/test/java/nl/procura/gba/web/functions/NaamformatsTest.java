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

package nl.procura.gba.web.functions;

import static nl.procura.standard.Globalfunctions.pad_right;

import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;

public class NaamformatsTest {

  public static void main(String[] args) {

    Naamformats pnf = new Naamformats("Petra Truus ", "Geld", "Van der", "Jonkvrouw", "E", null);
    Naamformats nf1 = new Naamformats("Theodorus Marcus Jan-Peter Albertus Gaius", "Terlouw", "de", "Baron", "V",
        pnf);
    Naamformats nf2 = new Naamformats("Theodorus Marcus Jan-Peter Albertus Gaius", "Terlouw", "de", "Jonkheer", "V",
        pnf);
    Naamformats nf3 = new Naamformats("Cle-xaïs Ruth -Maria Sigmélie Migdailine Amelia", "Terlouw", "de", "Jonkvrouw",
        "E",
        pnf);
    Naamformats nf4 = new Naamformats("Ïsmaël Æjoud Álbert", "Terlouw", "de", "Jonkheer",
        "E",
        pnf);

    System.out.println("Met Baron\n");
    log(nf1);

    System.out.println("\nMet Jonkheer\n");
    log(nf2);

    log(nf3);

    log(nf4);
  }

  private static void log(Naamformats nf) {//
    log("adeltitel", nf.getAdeltitel());
    log("eersteinit", nf.getEersteinit());
    log("eersteinit_nen", nf.getEersteinit_nen());
    log("eerstevoorn_overiginit", nf.getEerstevoorn_overiginit());
    log("eerstevoorn_overiginit_nen", nf.getEerstevoorn_overiginit_nen());
    log("eerstevoornaam", nf.getEerstevoornaam());
    log("gesl_init_nen_voorv", nf.getGesl_init_nen_voorv());
    log("gesl_init_voorv", nf.getGesl_init_voorv());
    log("gesl_pred_init_adel_voorv", nf.getGesl_pred_init_adel_voorv());
    log("gesl_pred_init_nen_adel_voorv", nf.getGesl_pred_init_nen_adel_voorv());
    log("gesl_titel_voorv", nf.getGesl_titel_voorv());
    log("gesl_voorv", nf.getGesl_voorv());
    log("geslachtsnaam", nf.getGeslachtsnaam());
    log("init", nf.getInit());
    log("init_nen", nf.getInit_nen());
    log("init_nen_voorv_gesl", nf.getInit_nen_voorv_gesl());
    log("init_voorv_gesl", nf.getInit_voorv_gesl());
    log("initialen", nf.getInitialen());
    log("naam_naamgebruik", nf.getNaam_naamgebruik());
    log("overigeinitialen", nf.getOverigeinitialen());
    log("overigeinitialen_nen", nf.getOverigeinitialen_nen());
    log("pred_adel_voorv_gesl_voorn", nf.getPred_adel_voorv_gesl_voorn());
    log("pred_eersteinit", nf.getPred_eersteinit());
    log("pred_eersteinit_adel_voorv_gesl", nf.getPred_eersteinit_adel_voorv_gesl());
    log("pred_eersteinit_nen", nf.getPred_eersteinit_nen());
    log("pred_eersteinit_nen_adel_voorv_gesl", nf.getPred_eersteinit_nen_adel_voorv_gesl());
    log("pred_eerstevoorn_adel_voorv_gesl", nf.getPred_eerstevoorn_adel_voorv_gesl());
    log("pred_eerstevoorn_overiginit", nf.getPred_eerstevoorn_overiginit());
    log("pred_eerstevoorn_overiginit_nen", nf.getPred_eerstevoorn_overiginit_nen());
    log("pred_init", nf.getPred_init());
    log("pred_init_adel_voorv_gesl", nf.getPred_init_adel_voorv_gesl());
    log("pred_init_nen", nf.getPred_init_nen());
    log("pred_init_nen_adel_voorv_gesl", nf.getPred_init_nen_adel_voorv_gesl());
    log("pred_voornamen", nf.getPred_voornamen());
    log("gesl_pred_init_adel_voorv", nf.getGesl_pred_init_adel_voorv());
    log("gesl_pred_init_nen_adel_voorv", nf.getGesl_pred_init_nen_adel_voorv());
  }

  private static void log(String key, String value) {
    System.out.println(pad_right(key, " ", 40) + " = " + value);
  }
}
