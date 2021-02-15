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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bijlage;

import static nl.procura.gba.common.MiscUtils.to;

import com.vaadin.ui.Upload.SucceededEvent;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.zaken.document.page4.DocUploader;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.dms.DmsService;
import nl.procura.standard.Globalfunctions;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class ZaakBijlageUploadPage extends NormalPageTemplate {

  private ZaakBijlageVertrouwelijkheidForm vertrouwelijkheidForm;
  private final Uploader                   docUploader = new Uploader();
  private final Zaak                       zaak;

  public ZaakBijlageUploadPage(Zaak zaak) {
    super("");
    this.zaak = zaak;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonClose);
      setInfo("Het bestand zal worden gekoppeld aan de aangever van de zaak");
      addComponent(new ZaakBijlageUploadForm(zaak));
      vertrouwelijkheidForm = new ZaakBijlageVertrouwelijkheidForm();
      addComponent(vertrouwelijkheidForm);
      addComponent(docUploader);
    }

    super.event(event);

    buttonClose.focus();
  }

  @Override
  public void onClose() {
    to(getWindow(), GbaModalWindow.class).closeWindow();
    super.onClose();
  }

  // Override
  protected void uploadSuccess() {
  }

  public class Uploader extends DocUploader {

    @Override
    public void uploadSucceeded(SucceededEvent event) {
      DmsService dms = getApplication().getServices().getDmsService();

      try {
        String vertrouwelijkheid = Globalfunctions.astr(vertrouwelijkheidForm.getVertrouwelijkheid().getNaam());
        dms.save(getFile(), getFileName(), getFileName(),
            getApplication().getServices().getGebruiker().getNaam(), zaak, vertrouwelijkheid);

        super.uploadSucceeded(event);

        uploadSuccess();
      } catch (Exception e) {
        getApplication().handleException(getWindow(), e);
      }
    }
  }
}
