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

package nl.procura.gba.web.modules.zaken.vog.page21;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.aval;

import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.services.zaken.vog.VogBelanghebbende;
import nl.procura.gba.web.services.zaken.vog.VogsService;

public class Page21Vog extends ZakenPage {

  private Page21VogForm1 form1 = null;

  private VogBelanghebbende belanghebbende = null;

  public Page21Vog(VogBelanghebbende belanghebbende) {

    super("Verklaring omtrent gedrag");

    addButton(buttonPrev, buttonSave);

    setBelanghebbende(belanghebbende);

    form1 = new Page21VogForm1(belanghebbende);

    addComponent(form1);
  }

  public VogBelanghebbende getBelanghebbende() {
    return belanghebbende;
  }

  public void setBelanghebbende(VogBelanghebbende belanghebbende) {
    this.belanghebbende = belanghebbende;
  }

  @Override
  public void onSave() {

    form1.commit();

    Page21VogBean1 b = form1.getBean();

    VogBelanghebbende bel = getBelanghebbende();

    bel.setNaam(b.getNaam());
    bel.setVertegenwoordiger(b.getVertegenwoordiger());
    bel.setStraat(b.getStraat());
    bel.setHnr(aval(b.getHnr()));
    bel.setHnrT(astr(b.getHnrT()));
    bel.setPc(b.getPostcode());
    bel.setPlaats(b.getPlaats());
    bel.setLand(b.getLand());
    bel.setTel(b.getTelefoon());

    VogsService d = getApplication().getServices().getVogService();
    d.saveBelanghebbende(bel);

    successMessage("De gegevens zijn opgeslagen.");

    getApplication().getProcess().endProcess();

    super.onSave();
  }
}
