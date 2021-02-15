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

package nl.procura.gba.web.modules.bs.ontbinding.overzicht.form1;

import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.trim;

import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.bs.algemeen.functies.BsDossierNaamgebruikUtils;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public abstract class OntbindingOverzichtForm2 extends ReadOnlyForm {

  public OntbindingOverzichtForm2(String tp, String voorv, String naam, String naamgebruik) {

    setReadonlyAsText(true);
    setColumnWidths("220px", "");

    init();

    OntbindingOverzichtBean2 bean = new OntbindingOverzichtBean2();

    bean.setTitel(getTitelOmschrijving(tp));
    Naamformats nf = new Naamformats("", naam, voorv, tp, naamgebruik, null);
    bean.setNaam(trim(nf.getTitel_voorv_gesl()));
    String gewijzigdTekst = "gewijzigd in " + naamgebruik + " (" + GBATable.AAND_NAAMGEBRUIK.get(
        naamgebruik).getValue() + ")";
    bean.setNaamgebruik(emp(naamgebruik) ? "Niet gewijzigd" : gewijzigdTekst);

    setBean(bean);
  }

  @Override
  public OntbindingOverzichtBean2 getBean() {
    return (OntbindingOverzichtBean2) super.getBean();
  }

  @Override
  public Object getNewBean() {
    return new OntbindingOverzichtBean2();
  }

  protected abstract void init();

  private String getTitelOmschrijving(String tp) {
    StringBuilder out = new StringBuilder(tp);
    if (BsDossierNaamgebruikUtils.isPredikaat(new FieldValue(tp))) {
      out.append(" (predikaat)");
    }
    if (BsDossierNaamgebruikUtils.isAdel(new FieldValue(tp))) {
      out.append(" (adelijke titel)");
    }
    return out.toString();
  }
}
