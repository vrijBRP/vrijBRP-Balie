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

package nl.procura.gba.web.modules.hoofdmenu.inbox.overzicht;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.form.document.preview.FilePreviewWindow;
import nl.procura.gba.web.components.layouts.form.document.preview.FilePreviewWindow.PreviewFile;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page230.Page230Zaken;
import nl.procura.gba.web.services.inbox.InboxRecord;
import nl.procura.gba.web.services.inbox.InboxService;
import nl.procura.gba.web.services.zaken.documenten.BestandType;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.functies.VaadinUtils;

public class InboxOverzichtLayout extends GbaVerticalLayout implements ClickListener {

  private final Button      buttonShow = new Button("Toon bestand");
  private final InboxRecord inboxRecord;

  public InboxOverzichtLayout(InboxRecord inboxRecord) {
    this.inboxRecord = inboxRecord;

    setSizeFull();
    setSpacing(true);

    buttonShow.setWidth("100%");

    OptieLayout optieLayout = new OptieLayout();
    optieLayout.getLeft().addComponent(new InboxOverzichtLayoutForm(inboxRecord));
    optieLayout.getRight().setCaption("Opties");
    optieLayout.getRight().setWidth("200px");
    optieLayout.getRight().addButton(buttonShow, this);

    addComponent(optieLayout);
    addComponent(new Fieldset("Gerelateerde berichten"));
    addComponent(new InboxOverzichtLayoutTable(inboxRecord) {

      @Override
      public void onDoubleClick(Record record) {
        NormalPageTemplate pageTemplate = VaadinUtils.getParent(InboxOverzichtLayout.this,
            NormalPageTemplate.class);
        InboxRecord inboxRecord = record.getObject(InboxRecord.class);
        InboxService inbox = getApplication().getServices().getInboxService();
        pageTemplate.getNavigation().addPage(new Page230Zaken(inbox.getCompleteZaak(inboxRecord)));
      }
    });
  }

  @Override
  public void buttonClick(ClickEvent event) {
    if (event.getButton() == buttonShow) {
      onToonBestand();
    }
  }

  private void onToonBestand() {
    byte[] bestandsbytes = inboxRecord.getBestandsbytes();
    String bestandsnaam = inboxRecord.getBestandsnaam();
    BestandType type = BestandType.getType(bestandsnaam);
    PreviewFile file = new PreviewFile(bestandsbytes, bestandsnaam, bestandsnaam, type);
    FilePreviewWindow.preview(getParentWindow(), file);
  }
}
