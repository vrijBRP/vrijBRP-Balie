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

package nl.procura.gba.web.modules.hoofdmenu.gv.page1;

import static nl.procura.gba.web.modules.hoofdmenu.gv.page1.Page1GvBean3.ACTIESOORT;
import static nl.procura.gba.web.modules.hoofdmenu.gv.page1.Page1GvBean3.DATUM_EINDE_TERMIJN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.GV_DATUM_KENBAAR_MAKEN;
import static nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType.PA_KENBAAR_MAKEN;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.hoofdmenu.gv.containers.ProcesActieContainer;
import nl.procura.gba.web.services.beheer.parameter.ParameterType;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.ProNativeSelect;

public abstract class Page1GvForm3 extends GbaForm<Page1GvBean3> {

  public Page1GvForm3(String caption) {

    setCaption(caption);
    setColumnWidths(WIDTH_130, "");
    setReadThrough(true);
    setOrder(ACTIESOORT, DATUM_EINDE_TERMIJN);

    setBean(new Page1GvBean3());
  }

  @Override
  public void setBean(Object bean) {
    super.setBean(bean);

    getProcesactie().setContainerDataSource(new ProcesActieContainer(true));
    getProcesactie().addListener((ValueChangeListener) this);
  }

  @Override
  public Page1GvBean3 getNewBean() {
    return new Page1GvBean3();
  }

  public ProNativeSelect getProcesactie() {
    return (ProNativeSelect) getField(ACTIESOORT);
  }

  public void setProcesactie(KoppelEnumeratieType procesActie) {
    getProcesactie().setValue(procesActie);
  }

  public void setDatum(ParameterType parm) {

    if (getApplication() != null) {

      String datumParm = getApplication().getParmValue(parm);
      Field field = getField(DATUM_EINDE_TERMIJN);

      if (fil(datumParm)) {

        ProcuraDate datum = new ProcuraDate().addDays(aval(datumParm));
        field.setValue(datum.getDateFormat());
      } else {
        field.setValue(null);
      }
    }
  }

  public void setKenbaarMaken(boolean kenbaarMaken) {

    if (kenbaarMaken) {
      getBean().setActiesoort(KoppelEnumeratieType.PA_KENBAAR_MAKEN);
      getProcesactie().setValue(KoppelEnumeratieType.PA_KENBAAR_MAKEN);
    } else {
      getBean().setActiesoort(null);
      getProcesactie().setValue(null);
    }

    getProcesactie().setContainerDataSource(new ProcesActieContainer(true));
  }

  @Override
  public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
    KoppelEnumeratieType procesActie = (KoppelEnumeratieType) getProcesactie().getValue();
    if (event.getProperty() == getProcesactie()) {
      procesActie = (KoppelEnumeratieType) event.getProperty().getValue();
      wijzigProcesActie(procesActie);
      wijzigDatumEindeTermijn(procesActie);
    }

    repaint();
  }

  public abstract void wijzigProcesActie(KoppelEnumeratieType procesActie);

  private void wijzigDatumEindeTermijn(KoppelEnumeratieType procesActie) {
    setDatum(PA_KENBAAR_MAKEN.is(procesActie) ? GV_DATUM_KENBAAR_MAKEN : null);
  }
}
