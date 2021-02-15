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

package nl.procura.gba.web.modules.hoofdmenu.gv.page2;

import static nl.procura.gba.web.modules.hoofdmenu.gv.page2.Page2GvBean4.*;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.hoofdmenu.gv.containers.ProcesActieContainer;
import nl.procura.gba.web.modules.hoofdmenu.gv.containers.ReactieTypeContainer;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.parameter.ParameterType;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.ProNativeSelect;

public abstract class Page2GvForm4 extends GbaForm<Page2GvBean4> {

  public Page2GvForm4(String caption) {
    setCaption(caption);
    setColumnWidths(WIDTH_130, "");
    setOrder(ACTIESOORT, REACTIE, MOTIVERING, DATUM_EINDE_TERMIJN);
    setBean(new Page2GvBean4());
  }

  @Override
  public void setBean(Object bean) {

    super.setBean(bean);

    getProcesactie().setContainerDataSource(new ProcesActieContainer(false));
    getReactie().setContainerDataSource(new ReactieTypeContainer());

    getProcesactie().addListener((ValueChangeListener) this);
    getReactie().addListener((ValueChangeListener) this);
  }

  public ProNativeSelect getProcesactie() {
    return (ProNativeSelect) getField(ACTIESOORT);
  }

  public void setProcesactie(KoppelEnumeratieType procesActie) {
    getProcesactie().setValue(procesActie);
  }

  public ProNativeSelect getReactie() {
    return (ProNativeSelect) getField(REACTIE);
  }

  public void setBestaandeAfweging() {
    getProcesactie().setContainerDataSource(new VerstrekkenContainer());
  }

  public void setNieuweAfweging() {
    getProcesactie().setContainerDataSource(new KenbaarMakenContainer());
  }

  @Override
  public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {

    KoppelEnumeratieType pa = (KoppelEnumeratieType) getProcesactie().getValue();

    if (event.getProperty() == getProcesactie()) {

      pa = (KoppelEnumeratieType) event.getProperty().getValue();

      wijzigProcesActie(pa);
    }

    pa = (KoppelEnumeratieType) getProcesactie().getValue();

    KoppelEnumeratieType ra = (KoppelEnumeratieType) getReactie().getValue();

    wijzigDatum(pa, ra);

    wijzig(pa, ra);

    repaint();
  }

  protected abstract void wijzig(KoppelEnumeratieType da, KoppelEnumeratieType ra);

  private void disableField(String field) {
    getField(field).setVisible(false);
    getField(field).setValue(null);
  }

  private void enableField(String field) {
    getField(field).setVisible(true);
  }

  private void setDatum(ParameterType parm) {

    String datumParm = getApplication().getParmValue(parm);
    Field field = getField(DATUM_EINDE_TERMIJN);

    if (fil(datumParm)) {
      ProcuraDate datum = new ProcuraDate().addDays(aval(datumParm));
      field.setValue(datum.getDateFormat());
    } else {
      field.setValue(null);
    }
  }

  private void wijzigDatum(KoppelEnumeratieType procesActie, KoppelEnumeratieType reactie) {

    setDatum(null);

    if (KoppelEnumeratieType.PA_NA_TERMIJN_VERSTREKKEN.is(procesActie)) {
      if (reactie != null) {
        if (KoppelEnumeratieType.REACTIE_GEEN_BEZWAAR.is(reactie)) {
          setDatum(ParameterConstant.GV_DATUM_VERSTREKKEN_GEEN_BEZW);
        } else {
          setDatum(ParameterConstant.GV_DATUM_VERSTREKKEN);
        }
      }
    }
  }

  private void wijzigProcesActie(KoppelEnumeratieType procesActie) {

    if (procesActie == null) {

      disableField(REACTIE);
      disableField(MOTIVERING);
      disableField(DATUM_EINDE_TERMIJN);
    } else {
      switch (procesActie) {
        case PA_NA_TERMIJN_VERSTREKKEN:
          enableField(REACTIE);
          disableField(MOTIVERING);
          enableField(DATUM_EINDE_TERMIJN);
          break;

        case PA_NIET_VERSTREKKEN:
          disableField(DATUM_EINDE_TERMIJN);
          enableField(REACTIE);
          enableField(MOTIVERING);

          // Als gekozen worden voor niet verstrekken dan kiezen voor wel bezwaar
          getField(REACTIE).setValue(KoppelEnumeratieType.REACTIE_WEL_BEZWAAR);
          break;

        default:
          disableField(REACTIE);
          disableField(MOTIVERING);
          disableField(DATUM_EINDE_TERMIJN);
          break;
      }
    }
  }

  public class KenbaarMakenContainer extends ArrayListContainer {

    public KenbaarMakenContainer() {
      addItem(KoppelEnumeratieType.PA_KENBAAR_MAKEN);
    }
  }

  public class VerstrekkenContainer extends ArrayListContainer {

    public VerstrekkenContainer() {
      addItem(KoppelEnumeratieType.PA_NIET_VERSTREKKEN);
      addItem(KoppelEnumeratieType.PA_NA_TERMIJN_VERSTREKKEN);
    }
  }

}
