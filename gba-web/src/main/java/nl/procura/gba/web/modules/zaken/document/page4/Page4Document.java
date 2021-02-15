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

package nl.procura.gba.web.modules.zaken.document.page4;

import com.vaadin.ui.Upload.SucceededEvent;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bijlage.ZaakBijlageVertrouwelijkheidForm;
import nl.procura.gba.web.modules.zaken.document.DocumentenPage;
import nl.procura.gba.web.services.zaken.algemeen.dms.DmsService;

public class Page4Document extends DocumentenPage {

  private final ZaakBijlageVertrouwelijkheidForm form        = new ZaakBijlageVertrouwelijkheidForm();
  private final Uploader                         docUploader = new Uploader();

  public Page4Document() {

    super("Documenten: uploaden");
    setMargin(true);

    addComponent(form);
    addComponent(docUploader);
  }

  public class Uploader extends DocUploader {

    @Override
    public void uploadSucceeded(SucceededEvent event) {
      form.commit();
      DmsService dms = getApplication().getServices().getDmsService();

      try {

        String gebruiker = getApplication().getServices().getGebruiker().getNaam();
        dms.save(getFile(), getFileName(), getFileName(), gebruiker, getPl(),
            form.getVertrouwelijkheid().getNaam());
        super.uploadSucceeded(event);

        reloadCaptions();
      } catch (Exception e) {
        getApplication().handleException(getWindow(), e);
      }
    }
  }
}
