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

package nl.procura.gba.web.modules.bs.registration.page40.relations;

import static com.vaadin.event.ShortcutAction.KeyCode.F2;
import static nl.procura.gba.common.MiscUtils.setClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.zaken.common.SourceDocumentForm;
import nl.procura.gba.web.services.bs.registration.RelationMatchType;
import nl.procura.gba.web.services.bs.registration.relations.Relation;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.theme.ProcuraWindow;

public abstract class AbstractRelationPage extends NormalPageTemplate {

  private Relation           relation;
  private SourceDocumentForm srcDocForm;
  private InfoLayout         infoLayout;

  private Consumer<Optional<Relation>> savePageListener;

  public AbstractRelationPage(String title, Relation relation) {
    super(title);
    this.relation = relation;
    savePageListener = r -> onClose();
    buttonSave.addListener(this);
    buttonClose.addListener(this);
    buttonClose.setWidth("99px");
    buttonSave.setWidth("99px");
    getMainbuttons().add(buttonSave);
    getMainbuttons().add(buttonClose);
    setSpacing(true);
    addComponent(new RelationNavLayout(this, relation));
    infoLayout = setInfo("");
  }

  public AbstractRelationPage withSaveListener(Consumer<Optional<Relation>> nextPageListener) {
    this.savePageListener = nextPageListener;
    return this;
  }

  @Override
  public void attach() {
    getWindow().setWidth("900px");
    super.attach();
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (isKeyCode(button, keyCode, F2, buttonSave)) {
      onSave();
    }
    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNextPage() {
    onSave();
  }

  @Override
  public void onSave() {
    savePageListener.accept(validate());
  }

  @Override
  public void onClose() {
    ProcuraWindow relationWindow = getWindow();
    // Only ask this with a new relation
    if (relationWindow instanceof ExistingRelationWindow) {
      relationWindow.closeWindow();
    } else {
      getParentWindow().addWindow(new ConfirmDialog("Wilt u deze gegevens niet opslaan?", 300) {

        @Override
        public void buttonYes() {
          super.buttonYes();
          relationWindow.closeWindow();
        }
      });
    }
    super.onClose();
  }

  public Relation getRelation() {
    return relation;
  }

  public void setRelation(Relation relation) {
    this.relation = relation;
  }

  public SourceDocumentForm getSrcDocForm() {
    return srcDocForm;
  }

  public void setSrcDocForm(SourceDocumentForm srcDocForm) {
    this.srcDocForm = srcDocForm;
  }

  public InfoLayout getInfoLayout() {
    return infoLayout;
  }

  public void setInfoLayout(InfoLayout infoLayout) {
    this.infoLayout = infoLayout;
  }

  protected void updateWarnings() {
    List<String> warnings = new ArrayList<>();
    if (!getSrcDocForm().isReadOnly()) {
      if (getSrcDocForm().isNoDocument()) {
        warnings.add("Er is geen brondocument ingevoerd.");
      }
      if (getRelation().getRelationMatchType().is(RelationMatchType.NO)) {
        warnings.add("Er is aangegeven dat dit de gegevens zouden zijn van "
            + relation.getMatchName().getPred_eerstevoorn_adel_voorv_gesl()
            + ", maar die gegevens komen niet overeen.");
      }
      if (!warnings.isEmpty()) {
        getInfoLayout().append("<hr/>");
        getInfoLayout().append("Deze relatie zal niet verwerkt worden in de BRP vanwege de volgende " +
            (warnings.size() == 1 ? "reden" : "redenen") + ":<br/>");
        getInfoLayout().append("<ul>");
        warnings.forEach(line -> getInfoLayout().append("<li>" + setClass(false, line) + "</li>"));
        getInfoLayout().append("</ul>");
      }
    }
  }

  protected abstract Relation validateForms();

  protected abstract Relation getReverseRelation();

  protected abstract void disable(List<String> reasons);

  protected abstract boolean isDisabled();

  protected abstract void setRelativeDetails();

  private Optional<Relation> validate() {
    return Optional.of(validateForms());
  }
}
