/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.inbox.overzicht;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.form.document.preview.FilePreviewWindow;
import nl.procura.gba.web.components.layouts.form.document.preview.FilePreviewWindow.PreviewFile;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page230.Page230Zaken;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.documenten.BestandType;
import nl.procura.gba.web.services.zaken.gemeenteinbox.GemeenteInboxRecord;
import nl.procura.gba.web.services.zaken.gemeenteinbox.GemeenteInboxService;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.functies.VaadinUtils;

public class InboxOverzichtLayout extends GbaVerticalLayout {

  private final Button              buttonShow = new Button("Toon bestand");
  private final GemeenteInboxRecord inboxRecord;

  public InboxOverzichtLayout(GemeenteInboxRecord inboxRecord) {
    this.inboxRecord = inboxRecord;
    buttonShow.setWidth("100%");
    setSizeFull();
    setSpacing(true);
  }

  @Override
  public void attach() {
    if (components.isEmpty()) {
      OptieLayout optieLayout = new OptieLayout();
      optieLayout.getLeft().addComponent(new InboxOverzichtLayoutForm(inboxRecord));
      optieLayout.getRight().setCaption("Opties");
      optieLayout.getRight().setWidth("180px");
      optieLayout.getRight().addButton(buttonShow, (ClickListener) event -> onToonBestand());
      addComponent(optieLayout);

      ZaakArgumenten argumenten = new ZaakArgumenten(StringUtils.defaultIfBlank(
          inboxRecord.getZaakIdExtern(),
          inboxRecord.getZaakId()));
      inboxRecord.setRelatedRecords(getApplication().getServices().getInboxService().getZaken(argumenten));
      addComponent(new Fieldset("Gerelateerde verzoeken"));
      addComponent(new InboxOverzichtLayoutTable(inboxRecord) {

        @Override
        public void onDoubleClick(Record record) {
          GemeenteInboxRecord inboxRecord = record.getObject(GemeenteInboxRecord.class);
          GemeenteInboxService inbox = getApplication().getServices().getInboxService();
          VaadinUtils.getParent(InboxOverzichtLayout.this, NormalPageTemplate.class)
              .getNavigation().addPage(new Page230Zaken(inbox.getCompleteZaak(inboxRecord)));
        }
      });
    }
    super.attach();
  }

  private void onToonBestand() {
    byte[] bestandsbytes = inboxRecord.getBestandsbytes();
    String bestandsnaam = inboxRecord.getBestandsnaam();
    BestandType type = BestandType.getType(bestandsnaam);
    PreviewFile file = new PreviewFile(bestandsbytes, bestandsnaam, bestandsnaam, type);
    FilePreviewWindow.preview(getParentWindow(), file);
  }
}
