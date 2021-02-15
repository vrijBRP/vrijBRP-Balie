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

package nl.procura.gba.web.modules.bs.onderzoek.page30.gesprekwindow;

import static nl.procura.gba.web.modules.bs.onderzoek.page30.gesprekwindow.BronBean.*;

import com.vaadin.ui.Button;
import com.vaadin.ui.Field;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.modules.hoofdmenu.gv.containers.AfnemerContainer;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoekBron;
import nl.procura.gba.web.services.zaken.documenten.afnemers.DocumentAfnemer;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout;

public abstract class StakeholderForm extends AbstractBronForm {

  private GbaApplication application;

  public StakeholderForm(DossierOnderzoekBron bron, GbaApplication application) {
    setCaption("Aanschrijving belanghebbende");
    setColumnWidths("200px", "");
    setOrder(F_STAKEHOLDER, F_INSTANTIE, F_AFDELING, F_TAV_AANHEF, F_TAV_VOORL, F_TAV_NAAM, F_ADRES, F_PC, F_PLAATS,
        F_EMAIL);

    this.application = application;
    setBron(bron);
  }

  public void setAfnemer(DocumentAfnemer da) {

    if (da != null) {
      getBean().setStakeholder(da);
      getBean().setInstantie(da.getDocumentAfn());
      getBean().setTavAanhef(da.getTerAttentieVanAanhef());
      getBean().setTavVoorl(da.getTavVoorl());
      getBean().setTavNaam(da.getTavNaam());
      getBean().setEmail(da.getEmail());
      getBean().setAdres(da.getAdres());
      getBean().setPc(new FieldValue(da.getPostcode()));
      getBean().setPlaats(da.getPlaats());
    }

    setBean(getBean());
  }

  @Override
  public void afterSetBean() {
    getAfnemerVeld().setContainerDataSource(new AfnemerContainer(application));
    getAfnemerVeld().addListener((ValueChangeListener) event -> {
      setAfnemer((DocumentAfnemer) event.getProperty().getValue());
    });
  }

  @Override
  public void setColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_TAV_VOORL, F_TAV_NAAM, F_PLAATS)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }

  @Override
  public void afterSetColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_STAKEHOLDER) && application != null) {
      Button button = new Button("+", (Button.ClickListener) event -> onButtonStakeholder());
      button.addStyleName("plus-button");
      column.addComponent(button);
    }

    super.afterSetColumn(column, field, property);
  }

  public GbaComboBox getAfnemerVeld() {
    return (GbaComboBox) getField(F_STAKEHOLDER);
  }

  public void updateAfnemers() {
    DocumentAfnemer af = (DocumentAfnemer) getAfnemerVeld().getValue();
    getAfnemerVeld().setContainerDataSource(new AfnemerContainer(application));
    getAfnemerVeld().setValue(getStakeholder(af));
  }

  protected abstract void onButtonStakeholder();

  private DocumentAfnemer getStakeholder(DocumentAfnemer af) {
    if (af != null) {
      return application.getServices()
          .getDocumentService()
          .getAfnemers()
          .stream()
          .filter(afnemer -> afnemer.getCDocumentAfn().equals(af.getCDocumentAfn()))
          .findFirst()
          .orElse(af);
    }

    return af;
  }
}
