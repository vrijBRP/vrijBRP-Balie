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

package nl.procura.gba.web.modules.zaken.woningkaart.page1;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.*;
import static nl.procura.gba.web.common.misc.java.PartitionUtil.partition;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.Adres;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gba.ple.procura.templates.custom.CustomTemplate;
import nl.procura.diensten.gba.wk.baseWK.BaseWKPerson;
import nl.procura.diensten.gba.wk.baseWK.BaseWKValue;
import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.diensten.gba.wk.extensions.WKResultWrapper;
import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.tablefilter.export.ExportButton;
import nl.procura.gba.web.modules.persoonslijst.overig.header.LopendeZakenWindow;
import nl.procura.gba.web.modules.zaken.woningkaart.window.WoningObjectWindow;
import nl.procura.gba.web.services.beheer.bag.ProcuraInhabitantsAddress;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.profiel.Profielen;
import nl.procura.gba.web.services.beheer.profiel.veld.ProfielVeld;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.VaadinUtils;

public class WoningkaartLayout extends NormalPageTemplate implements ValueChangeListener {

  private static final int SET_SIZE         = 20;
  private final Button     objectInfoButton = new Button("Objectinformatie (F3)");

  private final Label               adresIndicatie = new Label();
  private final AdresSelect         select         = new AdresSelect();
  private Page1WoningkaartTable     table          = null;
  private BaseWKExt                 wk             = null;
  private WoningkaartPersonenLayout personenLayout = null;

  public WoningkaartLayout() {
    addButton(objectInfoButton);
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      select.setHeight("25px");
      adresIndicatie.setWidth("80px");
      adresIndicatie.setHeight("25px");
      adresIndicatie.setVisible(false);
      objectInfoButton.setEnabled(false);

      getButtonLayout().addComponent(select);
      getButtonLayout().addComponent(adresIndicatie);
      getButtonLayout().setExpandRatio(select, 1);
      getButtonLayout().setWidth("100%");
      getButtonLayout().setComponentAlignment(objectInfoButton, Alignment.MIDDLE_LEFT);
      getButtonLayout().setComponentAlignment(select, Alignment.MIDDLE_LEFT);
      getButtonLayout().setComponentAlignment(adresIndicatie, Alignment.MIDDLE_LEFT);

      table = new Page1WoningkaartTable() {

        @Override
        public void onClick(Record record) {
          selectRow((BaseWKPerson) record.getObject());
        }

        @Override
        public void setRecords() {

          try {
            if (select.getValue() != null) {
              selectAdres((Adres) select.getValue());
            }
          } catch (Exception e) {
            getApplication().handleException(getWindow(), e);
          }
        }
      };

      addButton(new ExportButton(table));
      addExpandComponent(table);

      select.addListener(this);
      select.setContainerDataSource(new PersoonslijstAdresContainer(getPl()));
      select.setItemCaptionPropertyId(PersoonslijstAdresContainer.WAARDE);
    }

    super.event(event);
  }

  public BaseWKExt getWk() {
    return wk;
  }

  public void setWk(BaseWKExt wk) {
    this.wk = wk;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if ((button == objectInfoButton) || (objectInfoButton.isEnabled() && (keyCode == KeyCode.F3))) {
      getParentWindow().addWindow(new WoningObjectWindow(new ProcuraInhabitantsAddress(getWk())));
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onEnter() {

    if (table.getRecord() != null) {
      selectRow((BaseWKPerson) table.getRecord().getObject());
    }
  }

  @Override
  public void valueChange(ValueChangeEvent event) {
    table.init();
  }

  private void parseResult(WKResultWrapper result) {
    objectInfoButton.setEnabled(true);
    Profielen profielen = getApplication().getServices().getGebruiker().getProfielen();
    boolean isGeheimToegestaan = profielen.isProfielVeld(ProfielVeld.PL_VERSTREKKINGSBEPERKING);
    boolean zoekNaamgebruik = isTru(getApplication().getServices().getGebruiker().getParameters().get(
        ParameterConstant.ZOEK_PLE_NAAMGEBRUIK).getValue());

    for (BaseWKExt wk : result.getBasisWkWrappers()) {
      setWk(wk);

      personenLayout = new WoningkaartPersonenLayout().set(wk);
      addComponent(personenLayout, getComponentIndex(table));
      setAdresIndicatie(wk);

      List<BasePLExt> persoonslijsten = new ArrayList<>();
      partition(wk.getBasisWk().getPersonen(), SET_SIZE).stream()
          .map(partition -> getPersoonslijsten(partition,
              persoonslijsten.size(),
              zoekNaamgebruik))
          .forEach(persoonslijsten::addAll);

      table.addToTable(wk.getBasisWk().getPersonen(), persoonslijsten, isGeheimToegestaan);
      setGeheimMelding();
      return;
    }
  }

  private List<BasePLExt> getPersoonslijsten(List<BaseWKPerson> personen, int total, boolean zoekNaamgebruik) {
    if (pos(personen.size())) {
      PLEArgs args = new PLEArgs();

      for (BaseWKPerson p : personen) {
        if (!pos(p.getDatum_vertrek().getValue()) || total < 100) {
          String anr = p.getAnummer().getCode();
          String bsn = p.getBsn().getCode();
          args.addNummer(pos(anr) ? anr : bsn);
        }
      }

      args.setCustomTemplate(CustomTemplate.WK);
      args.setDatasource(PLEDatasource.PROCURA);
      args.setShowHistory(false);
      args.setShowArchives(false);
      args.addCat(PERSOON, VB, INSCHR, VERW);
      args.setCat(HUW_GPS, zoekNaamgebruik);

      if (args.getNumbers().size() > 0) {
        return getApplication().getServices().getPersonenWsService()
            .getPersoonslijsten(args, false).getBasisPLWrappers();
      }
    }

    return new ArrayList<>();
  }

  private void selectAdres(Adres adres) {

    try {
      adresIndicatie.setVisible(false);
      objectInfoButton.setEnabled(false);

      ZoekArgumenten z = ZoekArgumenten.of(adres);
      WKResultWrapper result = getApplication().getServices().getPersonenWsService().getAdres(z, true);

      if (personenLayout != null) {
        removeComponent(personenLayout);
      }

      if (result.getBasisWkWrappers().size() > 0) {
        parseResult(result);
      } else {
        personenLayout = new WoningkaartPersonenLayout().setNoResults();
        addComponent(personenLayout, getComponentIndex(table));
      }
    } catch (Exception e) {
      getApplication().handleException(getWindow(), e);
    }
  }

  private void selectRow(BaseWKPerson wk) {
    BasePLExt pl = getApplication().getServices().getPersonenWsService().getPersoonslijst(
        wk.getAnummer().getValue());
    getParentWindow().addWindow(new LopendeZakenWindow(pl));
  }

  private void setAdresIndicatie(BaseWKExt wk) {
    BaseWKValue ind = wk.getBasisWk().getAdres_indicatie();

    if (pos(ind.getCode())) {
      adresIndicatie.setValue(StringUtils.abbreviate(ind.getDescr(), 10));
      adresIndicatie.setVisible(true);
      adresIndicatie.setStyleName(GbaWebTheme.TEXTBOX.RED);
      adresIndicatie.setDescription(ind.getDescr());
    }
  }

  private void setGeheimMelding() {
    String melding = table.getGeheimMelding();

    if (fil(melding)) {
      VaadinUtils.getChild(this, WoningkaartPersonenLayout.class).append(melding);
    }
  }

  public static class AdresSelect extends GbaNativeSelect {

    public AdresSelect() {
      setImmediate(true);
      setSizeFull();
      setNullSelectionAllowed(false);
    }
  }
}
