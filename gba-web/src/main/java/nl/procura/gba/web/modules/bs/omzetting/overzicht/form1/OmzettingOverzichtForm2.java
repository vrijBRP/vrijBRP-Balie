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

package nl.procura.gba.web.modules.bs.omzetting.overzicht.form1;

import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.trim;

import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;

public abstract class OmzettingOverzichtForm2 extends ReadOnlyForm {

  public OmzettingOverzichtForm2(String tp, String voorv, String naam, String naamgebruik) {

    setReadonlyAsText(true);
    setColumnWidths("150px", "");

    init();

    OmzettingOverzichtBean2 bean = new OmzettingOverzichtBean2();

    Naamformats nf = new Naamformats("", naam, voorv, tp, naamgebruik, null);

    bean.setNaam(trim(nf.getPred_eersteinit_adel_voorv_gesl()));
    bean.setNaamgebruik(emp(naamgebruik) ? "Niet gewijzigd" : "gewijzigd in " + naamgebruik);

    setBean(bean);
  }

  @Override
  public OmzettingOverzichtBean2 getBean() {
    return (OmzettingOverzichtBean2) super.getBean();
  }

  @Override
  public Object getNewBean() {
    return new OmzettingOverzichtBean1();
  }

  protected abstract void init();
}
