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

package nl.procura.gba.web.modules.bs.naturalisatie.page20;

import static nl.procura.gba.web.modules.bs.naturalisatie.page20.Page20NaturalisatieBean.F_BASIS_VERZOEK;
import static nl.procura.gba.web.modules.bs.naturalisatie.page20.Page20NaturalisatieBean.F_DOSSIERNR;
import static nl.procura.gba.web.modules.bs.naturalisatie.page20.Page20NaturalisatieBean.F_VERTEGENWOORDIGER;
import static nl.procura.gba.web.services.bs.naturalisatie.enums.BevoegdTotVerzoekType.JA;
import static nl.procura.standard.Globalfunctions.along;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.QuickSearchPersonConfig;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.QuickSearchPersonWindow;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.SelectListener;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatie;
import nl.procura.gba.web.services.bs.naturalisatie.enums.BasisVerzoekType;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.validation.Anummer;

public class Page20NaturalisatieForm extends GbaForm<Page20NaturalisatieBean> {

  private QuickSearchPersonWindow    snelZoekWindow;
  private final DossierNaturalisatie dossier;
  private final GbaApplication       application;

  private final Button selectButton = new Button("Zoek");
  private final Button resetButton  = new Button("Reset");

  public Page20NaturalisatieForm(DossierNaturalisatie dossier, GbaApplication application) {
    this.dossier = dossier;
    this.application = application;

    setCaption("Situatie");
    setReadThrough(true);
    setColumnWidths("200px", "");
    setOrder(F_BASIS_VERZOEK, F_DOSSIERNR, F_VERTEGENWOORDIGER);

    Page20NaturalisatieBean bean = new Page20NaturalisatieBean();
    bean.setBasisVerzoek(dossier.getBasisVerzoekType());
    bean.setDossiernr(getDossierNummer(dossier.getDossiernr()));
    setBean(bean);

    selectButton.addListener((Button.ClickListener) event -> onSelectVertegenwoordiger());
    resetButton.addListener((Button.ClickListener) event -> onResetVertegenwoordiger());
  }

  private String getDossierNummer(String dossiernr) {
    String nextDossierNummer = application.getServices().getNaturalisatieService().getNextDossierNummer().getValue();
    return StringUtils.defaultIfBlank(dossiernr, nextDossierNummer);
  }

  @Override
  public void afterSetBean() {
    getField(F_DOSSIERNR).setVisible(!dossier.isOptie());
    getField(F_VERTEGENWOORDIGER).setVisible(!dossier.isMeerderjarig());
    setNaamVertegenwoordiger();

    long gemeenteCode = along(application.getServices().getGebruiker().getGemeenteCode());
    getField(F_DOSSIERNR).addValidator(new NaturalisatieDossierNummerValidator(gemeenteCode));
    IndexedContainer container = new ArrayListContainer(
        BasisVerzoekType.get(dossier.getBevoegdTotVerzoekType() == JA, dossier.isOptie()));

    GbaNativeSelect basisField = getField(F_BASIS_VERZOEK, GbaNativeSelect.class);
    basisField.setContainerDataSource(container);
    basisField.setValue(getBean().getBasisVerzoek());
    basisField.setCaption(dossier.isOptie() ? "Basis voor optieverzoek" : "Basis voor naturalisatieverzoek");

    super.afterSetBean();
  }

  @Override
  public void afterSetColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_VERTEGENWOORDIGER)) {
      selectButton.setWidth("50px");
      resetButton.setWidth("55px");
      selectButton.setParent(null);
      resetButton.setParent(null);
      column.addComponent(selectButton);
      column.addComponent(resetButton);
    }
    super.afterSetColumn(column, field, property);
  }

  @Override
  public Page20NaturalisatieBean getNewBean() {
    return new Page20NaturalisatieBean();
  }

  private void onSelectVertegenwoordiger() {
    if (snelZoekWindow == null) {
      Anummer anummer = new Anummer(dossier.getAangever().getAnummer().getStringValue());
      SelectListener selectListener = pl -> {
        BsPersoonUtils.kopieDossierPersoon(pl, dossier.getVertegenwoordiger());
        setNaamVertegenwoordiger();
        repaint();
      };
      snelZoekWindow = new QuickSearchPersonWindow(QuickSearchPersonConfig.builder()
          .selectListener(selectListener)
          .anummer(anummer)
          .build());
    }
    snelZoekWindow.setParent(null);
    getApplication().getParentWindow().addWindow(snelZoekWindow);
  }

  private void setNaamVertegenwoordiger() {
    String name = dossier.getVertegenwoordiger().getNaam().getNaam_naamgebruik_eerste_voornaam();
    getField(F_VERTEGENWOORDIGER).setValue(StringUtils.defaultIfBlank(name, "[ Selecteer de persoon ]"));
  }

  private void onResetVertegenwoordiger() {
    BsPersoonUtils.reset(dossier.getVertegenwoordiger());
    setNaamVertegenwoordiger();
    repaint();
  }
}
