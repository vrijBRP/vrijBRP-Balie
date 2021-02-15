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

package nl.procura.gba.web.modules.beheer.documenten;

import static nl.procura.gba.web.modules.beheer.documenten.KoppelAlleGebruikersBean.ALLALLOWED;

import java.util.Collections;
import java.util.List;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.gebruikers.KoppelGebruikerTabel;
import nl.procura.gba.web.modules.beheer.gebruikers.KoppelGebruikersPage;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;

public class KoppelGebruikersAanDocumentenPage extends NormalPageTemplate {

  private final VerticalLayout                 buttonsAndTable = new VerticalLayout();
  private final HorizontalLayout               koppelButtons   = new HorizontalLayout();
  private KoppelGebruikerTabel                 table           = null;
  private KoppelAlleGebruikersForm             form            = null;
  private KoppelGebruikersPage<DocumentRecord> coupleUsers     = null;

  public KoppelGebruikersAanDocumentenPage(DocumentRecord document) {

    super("Overzicht van gekoppelde gebruikers van document " + document.getBestand());
    addButton(buttonPrev);
    List<DocumentRecord> docList = Collections.singletonList(document);
    makePage(docList, false);
  }

  public KoppelGebruikersAanDocumentenPage(List<DocumentRecord> docList) {
    super("");
    makePage(docList, true);
  }

  /**
   * Nodig als de pagina ingevoegd wordt in een pagina waar al een 'vorige' knop
   * staat.
   */

  public void disablePreviousButton() {
    buttonPrev.setVisible(false);
  }

  public VerticalLayout getButtonsAndTable() {
    return buttonsAndTable;
  }

  public KoppelAlleGebruikersForm getForm() {
    return form;
  }

  public void setForm(KoppelAlleGebruikersForm form) {
    this.form = form;
  }

  public HorizontalLayout getKoppelButtons() {
    return koppelButtons;
  }

  public KoppelGebruikerTabel getTable() {
    return table;
  }

  public void setTable(KoppelGebruikerTabel table) {
    this.table = table;
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  private void addCoupleUsersPage(List<DocumentRecord> docList) {

    coupleUsers = new KoppelGebruikersPage<>(docList, "documenten");
    coupleUsers.disablePreviousButton();
    addExpandComponent(coupleUsers);
  }

  private void addListenerToForm(List<DocumentRecord> docList) {

    getForm().getField(ALLALLOWED).addListener(new AllAllowedListener(docList));
  }

  private void doPageActions(boolean coupleMultipleDocs) {

    if (coupleMultipleDocs) {
      disablePreviousButton();
    } else {
      setMargin(true);
    }
  }

  private void giveSuccesMessage(List<DocumentRecord> docList, boolean allAllowed) {
    String message;

    if (allAllowed) {
      if (docList.size() == 1) {
        message = "Alle gebruikers zijn gekoppeld aan het document.";
      } else {
        // meer dan 1 doc
        message = "Alle gebruikers zijn gekoppeld aan de documenten.";
      }
    } else {
      message = "De optie 'iedereen toegang' is uitgeschakeld; zie de tabel voor een overzicht van de gekoppelde gebruikers.";
    }

    successMessage(message);
  }

  private void makePage(List<DocumentRecord> docList,
      boolean coupleMultipleDocs) { // boolean is true wanneer we meteen vanuit docTabel gebr koppelen

    doPageActions(coupleMultipleDocs);
    setForm(new KoppelAlleGebruikersForm(docList));
    addComponent(form);

    addCoupleUsersPage(docList);
    setVisibilityUsersPage();
    addListenerToForm(docList);
  }

  private void save(List<DocumentRecord> docList, boolean allAllowed) {

    setAllAllowed(docList, allAllowed);
    getServices().getDocumentService().save(docList);
    giveSuccesMessage(docList, allAllowed);
  }

  private void setAllAllowed(List<DocumentRecord> docList, boolean allAllowed) {

    for (DocumentRecord doc : docList) {
      doc.setIedereenToegang(allAllowed);
    }
  }

  private void setVisibilityUsersPage() {

    boolean allAllowed = form.getBean().isAllAllowed();
    coupleUsers.setVisible(!allAllowed);
  }

  private void updatePage(boolean allAllowed) {
    coupleUsers.setVisible(!allAllowed);
  }

  private final class AllAllowedListener implements ValueChangeListener {

    private final List<DocumentRecord> documents;

    private AllAllowedListener(List<DocumentRecord> docList) {
      this.documents = docList;
    }

    @Override
    public void valueChange(ValueChangeEvent event) {

      getForm().commit();
      boolean allAllowed = getForm().getBean().isAllAllowed();

      save(documents, allAllowed);
      updatePage(allAllowed);
    }
  }
}
