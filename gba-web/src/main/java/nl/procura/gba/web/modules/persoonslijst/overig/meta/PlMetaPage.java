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

package nl.procura.gba.web.modules.persoonslijst.overig.meta;

import static java.lang.String.format;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.*;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.DELETE_MUT;
import static nl.procura.standard.Globalfunctions.fil;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.*;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.verwerken.BsmVerwerkingWindow;
import nl.procura.gba.web.modules.persoonslijst.overig.PlPage;
import nl.procura.gba.web.modules.persoonslijst.overig.grid.PlGridPage;
import nl.procura.gba.web.modules.persoonslijst.overig.tmv.PlTmvPage;
import nl.procura.gba.web.modules.persoonslijst.overzicht.ModuleOverzichtPersoonslijst;
import nl.procura.gba.web.services.beheer.personmutations.PersonListMutation;

public class PlMetaPage extends PlPage {

  private final Label    setLabel   = new Label("", Label.CONTENT_XHTML);
  private final Button   prevButton = new Button("Vorig");
  private final Button   nextButton = new Button("Volgend");
  private BasePLSet      gbaSet     = new BasePLSet(UNKNOWN, 1);
  private BasePLRec      gbaRecord  = null;
  private Button         typeButton = new Button("Andere opmaak (F2)");
  private Button         zoekButton = new Button("Zoek (F3)");
  private Button         tmvButton  = new Button("Terugmelding (F4)");
  private VerticalLayout layout     = new VerticalLayout();

  public PlMetaPage(String title, BasePLSet gbaSet, BasePLRec gbaRecord) {

    super(title);

    buttonPrev.addListener(this);
    zoekButton.addListener(this);
    prevButton.addListener(this);
    nextButton.addListener(this);
    typeButton.addListener(this);
    tmvButton.addListener(this);

    setGbaSet(gbaSet);
    setGbaRecord(gbaRecord);

    addComponent(getLayout());
    setLayouts();
  }

  public BasePLElem getElement(GBAElem type) {
    return getGbaRecord().getElem(type);
  }

  public BasePLValue getElementW(GBAElem type) {
    return getGbaRecord().getElemVal(type);
  }

  public BasePLRec getGbaRecord() {

    if (gbaRecord == null) {
      gbaRecord = gbaSet.getRecs().get(0);
    }

    return gbaRecord;
  }

  public void setGbaRecord(BasePLRec gbaRecord) {
    this.gbaRecord = gbaRecord;
  }

  public BasePLSet getGbaSet() {
    return gbaSet;
  }

  public void setGbaSet(BasePLSet gbaSet) {
    this.gbaSet = gbaSet;
  }

  public VerticalLayout getLayout() {
    return layout;
  }

  public void setLayout(VerticalLayout layout) {
    this.layout = layout;
  }

  public Button getTmvButton() {
    return tmvButton;
  }

  public void setTmvButton(Button tmvButton) {
    this.tmvButton = tmvButton;
  }

  public Button getTypeButton() {
    return typeButton;
  }

  public void setTypeButton(Button typeButton) {
    this.typeButton = typeButton;
  }

  public Button getZoekButton() {
    return zoekButton;
  }

  public void setZoekButton(Button zoekButton) {
    this.zoekButton = zoekButton;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if ((button == getZoekButton() || keyCode == KeyCode.F3) && getZoekButton().isVisible()) {

      // Zoekbutton
      String bsn = getGbaRecord().getElemVal(GBAElem.BSN).getVal();
      String anr = getGbaRecord().getElemVal(GBAElem.ANR).getVal();

      getApplication().goToPl(getWindow(), ModuleOverzichtPersoonslijst.NAME, PLEDatasource.STANDAARD,
          fil(bsn) ? bsn : anr);
    } else if (button == prevButton || button == nextButton) {

      // Vorige / volgende
      setGbaRecord(button == nextButton ? getNextRecord() : getPrevRecord());
      setLayouts();
    } else if ((button == getTypeButton() || keyCode == KeyCode.F2) && getTypeButton().isVisible()) {

      // Wissel van pagina weergave
      if (!(getNavigation().getCurrentPage() instanceof PlGridPage)) {
        getNavigation().goToPage(new PlGridPage(getTitle(), getGbaSet(), getGbaRecord()));
      }
    } else if ((button == getTmvButton() || keyCode == KeyCode.F4) && getTmvButton().isVisible()) {

      handleTmvButton();
    }

    super.handleEvent(button, keyCode);
  }

  protected void setLayouts() {

    getLayout().removeAllComponents();
    getLayout().setSizeUndefined();
    getLayout().setWidth("100%");
    getLayout().setSpacing(true);

    setStatus();
    setToolbar();
  }

  private Button getNextButton() {
    return nextButton;
  }

  private BasePLRec getNextRecord() {

    int size = getGbaSet().getRecs().size();

    return size == getGbaRecord().getIndex() ? getGbaRecord()
        : getGbaSet().getRecs()
            .get(size - getGbaRecord().getIndex() - 1);
  }

  private Button getPrevButton() {
    return prevButton;
  }

  private BasePLRec getPrevRecord() {

    int size = getGbaSet().getRecs().size();

    return getGbaRecord().getIndex() == 1 ? getGbaRecord()
        : getGbaSet().getRecs()
            .get(size - getGbaRecord().getIndex() + 1);
  }

  private void handleTmvButton() {

    getNavigation().goToPage(new PlTmvPage(getTitle(), getGbaSet().getLatestRec()));
  }

  private void setStatus() {

    StringBuilder value = new StringBuilder("<b>");

    if (getGbaRecord().isCat(OUDER_1, OUDER_2)) {
      String geslachtsnaam = getGbaRecord().getElemVal(GBAElem.GESLACHTSNAAM).getVal();
      if (StringUtils.isEmpty(geslachtsnaam)) {
        value.append(setClass(false, "JURIDISCH GEEN OUDER"));
        value.append(" - ");

      } else if (".".equals(geslachtsnaam)) {
        value.append(setClass(false, "OUDER ONBEKEND"));
        value.append(" - ");
      }
    }

    if (getGbaRecord().isBagChange()) {
      value.append(setClass(false, "DUBBEL IVM BAG WIJZIGING"));
      value.append(" - ");
    }

    if (getGbaRecord().isAdmHistory()) {
      value.append(setClass(false, "ADMIN. HISTORIE"));
      value.append(" - ");

    } else {

      if (getGbaRecord().isConflicting()) {
        value.append(setClass(false, "STRIJDIG"));
        value.append(" - ");

      } else if (getGbaRecord().isIncorrect()) {
        value.append(setClass(false, "ONJUIST"));
        value.append(" - ");

      } else if (getGbaRecord().isCat(KINDEREN)
          && "L".equals(getGbaRecord().getElemVal(GBAElem.REG_BETREKK).getVal())) {
        value.append(setClass(false, "LEVENLOOS GEBOREN"));
        value.append(" - ");
      }

      value.append(getGbaRecord().getStatus().getDescr().toUpperCase());
      value.append(" - ");
    }

    value.append("</b> ");

    int recordCount = getGbaSet().getRecs().size();
    value.append(format("record: %d van %d", getGbaRecord().getIndex(), recordCount));
    setLabel.setValue(value.toString());

    getPrevButton().setEnabled(getGbaRecord().getIndex() > 1);
    getNextButton().setEnabled(getGbaRecord().getIndex() < recordCount);
  }

  private void setToolbar() {

    HorizontalLayout tb = new HorizontalLayout();
    tb.setHeight("30px");
    tb.setWidth("100%");
    tb.setSpacing(true);

    setLabel.setSizeUndefined();
    setLabel.addStyleName("rechts-uitgelijnd");

    tb.addComponent(buttonPrev);
    tb.addComponent(getTypeButton());
    tb.addComponent(getTmvButton());
    tb.addComponent(getZoekButton());

    Label spacer = new Label("", Label.CONTENT_XHTML);
    tb.addComponent(spacer);
    tb.setExpandRatio(spacer, 1f);

    if (getGbaRecord().isStatus(GBARecStatus.MUTATION)) {
      DeleteMutationButton deleteMutationButton = new DeleteMutationButton(this::onDeleteMutation);
      tb.addComponent(deleteMutationButton);
      tb.setComponentAlignment(deleteMutationButton, Alignment.MIDDLE_RIGHT);
    }

    tb.addComponent(setLabel);
    tb.addComponent(getPrevButton());
    tb.addComponent(getNextButton());

    tb.setComponentAlignment(buttonPrev, Alignment.MIDDLE_RIGHT);
    tb.setComponentAlignment(getTypeButton(), Alignment.MIDDLE_RIGHT);
    tb.setComponentAlignment(getTmvButton(), Alignment.MIDDLE_RIGHT);
    tb.setComponentAlignment(getZoekButton(), Alignment.MIDDLE_RIGHT);
    tb.setComponentAlignment(getPrevButton(), Alignment.MIDDLE_RIGHT);
    tb.setComponentAlignment(getNextButton(), Alignment.MIDDLE_RIGHT);
    tb.setComponentAlignment(setLabel, Alignment.MIDDLE_RIGHT);

    // Alleen zoekbutton als het een gerelateerde is
    getZoekButton().setVisible(getGbaRecord().isCat(OUDER_1, OUDER_2, HUW_GPS, HUW_GPS, KINDEREN));
    getLayout().addComponent(tb);
  }

  private void onDeleteMutation() {
    PersonListMutation mutation = getServices().getPersonListMutationService().getNewZaak();
    mutation.setAction(DELETE_MUT);
    mutation.setCat(getGbaRecord().getCatType());
    mutation.setSet(getGbaRecord().getSet());
    mutation.setDescrRec("Mutatie record");

    //Save mutation
    getServices().getPersonListMutationService().save(mutation);

    // Process mutation
    getParentWindow().addWindow(new BsmVerwerkingWindow<PersonListMutation>(mutation) {

      @Override
      public void reload() {
        getGbaApplication().reloadCurrentPersonList();
      }
    });
  }
}
