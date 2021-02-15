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

package nl.procura.gbaws.web.vaadin.module.auth.profile.page3.upload;

import java.io.File;

import com.vaadin.ui.Upload.SucceededEvent;

import nl.procura.gbaws.web.vaadin.module.tables.page1.upload.DocUploader;
import nl.procura.vaadin.component.dialog.ModalWindow;

public abstract class ElementsUploadWindow extends ModalWindow {

  private final Uploader uploader = new Uploader();

  public ElementsUploadWindow() {
    setWidth("420px");
    setCaption("Bestand met tabellen uploaden");
    addComponent(uploader);
  }

  public abstract void onUpload(File bestand, SucceededEvent event);

  private class Uploader extends DocUploader {

    @Override
    public void uploadSucceeded(final SucceededEvent event) {

      onUpload(getFile(), event);

      super.uploadSucceeded(event);
    }
  }
}
