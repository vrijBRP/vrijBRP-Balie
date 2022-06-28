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

package nl.procura.gba.web.modules.zaken.verkiezing.page2;

import static nl.procura.gba.web.modules.zaken.verkiezing.page2.Page2VerkiezingBean.*;

import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.QuickSearchPersonWindow;
import nl.procura.gba.web.services.beheer.verkiezing.KiezersregisterActieType;
import nl.procura.gba.web.services.beheer.verkiezing.KiezersregisterService;
import nl.procura.gba.web.services.beheer.verkiezing.Stempas;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.validation.Anummer;

public class Page2VerkiezingForm extends GbaForm<Page2VerkiezingBean> {

  private QuickSearchPersonWindow snelZoekWindow;
  private Anummer                 anrGemachtigde;

  private final Button  selectButton = new Button("Zoek");
  private final Stempas stempas;

  public Page2VerkiezingForm(Stempas stempas) {
    this.stempas = stempas;
    setCaption("Nieuwe aanpassing kiezersregister");
    setOrder(F_VERKIEZING, F_STEMPAS, F_AAND, F_ACTIE, F_OPM, F_MACHT_NAAM);
    setColumnWidths("160px", "");

    Page2VerkiezingBean bean = new Page2VerkiezingBean();
    bean.setVerkiezing(stempas.getVerkiezing());
    bean.setStempas(stempas);
    setBean(bean);
    selectButton.addListener((Button.ClickListener) event -> onSelectPersoon());
  }

  @Override
  public void afterSetBean() {
    KiezersregisterActieTypeContainer container = new KiezersregisterActieTypeContainer(stempas);
    getField(F_ACTIE, GbaNativeSelect.class).setContainerDataSource(container);
    getField(F_MACHT_NAAM).setVisible(false);

    getField(F_ACTIE).addListener((ValueChangeListener) event -> {
      KiezersregisterActieType actieType = (KiezersregisterActieType) event.getProperty().getValue();
      getField(F_MACHT_NAAM).setVisible(false);

      if (KiezersregisterActieType.ACT_MACHTIGEN == actieType) {
        getField(F_MACHT_NAAM).setValue("[ Selecteer de persoon ]");
        getField(F_MACHT_NAAM).setVisible(true);
        getField(F_MACHT_NAAM).addValidator(new GeenGemachtigdeValidator());
        getField(F_MACHT_NAAM).addValidator(new FoutieveGemachtigdeValidator());
        getField(F_MACHT_NAAM).addValidator(new MaxVolmachtValidator());
        repaint();
        onSelectPersoon();
      } else {
        getField(F_MACHT_NAAM).setValue("");
        anrGemachtigde = null;
      }
      repaint();
    });

    super.afterSetBean();
  }

  public Stempas getStempas() {
    return stempas;
  }

  @Override
  public void setColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_MACHT_NAAM)) {
      getLayout().addFieldset("Gemachtigde");
    }
    super.setColumn(column, field, property);
  }

  @Override
  public void afterSetColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_MACHT_NAAM)) {
      selectButton.setParent(null);
      column.addComponent(selectButton);
    }
    super.afterSetColumn(column, field, property);
  }

  public Anummer getAnrGemachtigde() {
    return anrGemachtigde;
  }

  private void onSelectPersoon() {
    if (snelZoekWindow == null) {
      snelZoekWindow = new QuickSearchPersonWindow(stempas.getAnr(), pl -> {
        anrGemachtigde = new Anummer(pl.getPersoon().getAnr().getVal());
        getField(F_MACHT_NAAM).setValue(pl.getPersoon().getNaam().getNaamNaamgebruikGeslachtsnaamVoornaamAanschrijf());
        repaint();
      });
    }
    snelZoekWindow.setParent(null);
    getApplication().getParentWindow().addWindow(snelZoekWindow);
  }

  private class GeenGemachtigdeValidator extends AbstractValidator {

    public GeenGemachtigdeValidator() {
      super("Geen gemachtigde geselecteerd");
    }

    @Override
    public boolean isValid(Object o) {
      return anrGemachtigde != null && anrGemachtigde.isCorrect();
    }
  }

  private class FoutieveGemachtigdeValidator extends AbstractValidator {

    public FoutieveGemachtigdeValidator() {
      super("De gemachtigde is zelf niet stemgerechtigd voor deze verkiezing");
    }

    @Override
    public boolean isValid(Object o) {
      if (anrGemachtigde != null && anrGemachtigde.isCorrect()) {
        KiezersregisterService service = getApplication().getServices().getKiezersregisterService();
        return !service.getStempassenByAnr(stempas.getStem().getKiesrVerk(), anrGemachtigde).isEmpty();
      }
      return true;
    }
  }

  private class MaxVolmachtValidator extends AbstractValidator {

    public MaxVolmachtValidator() {
      super("");
    }

    @Override
    public boolean isValid(Object o) {
      if (anrGemachtigde != null && anrGemachtigde.isCorrect()) {
        long aantalVolmachten = getAantalVolmachten();
        long maxVolmachten = getMaxVolmachten();
        setErrorMessage("Deze persoon heeft al het maximaal aantal volmachten (" + maxVolmachten + ")");
        return aantalVolmachten < maxVolmachten;
      }
      return true;
    }

    private Long getAantalVolmachten() {
      return (long) getApplication().getServices().getKiezersregisterService()
          .getStempassenByVolmachtAnr(stempas.getVerkiezing().getVerk(), anrGemachtigde).size();
    }

    private Long getMaxVolmachten() {
      return stempas.getVerkiezing().getVerk().getAantalVolm();
    }
  }
}
