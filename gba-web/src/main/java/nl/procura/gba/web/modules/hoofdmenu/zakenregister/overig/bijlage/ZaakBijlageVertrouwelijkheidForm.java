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

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.form.document.DocumentVertrouwelijkheidContainer;
import nl.procura.gba.web.services.zaken.documenten.DocumentVertrouwelijkheid;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;

import lombok.Data;

public class ZaakBijlageVertrouwelijkheidForm extends GbaForm<ZaakBijlageVertrouwelijkheidForm.AttachmentBean> {

  private static final String VERTROUWELIJKHEID = "vertrouwelijkheid";

  public ZaakBijlageVertrouwelijkheidForm() {
    setOrder(VERTROUWELIJKHEID);
    setCaption("Nieuw bestand");
    setColumnWidths(WIDTH_130, "");
  }

  @Override
  public void attach() {
    AttachmentBean bean = new AttachmentBean();
    bean.setVertrouwelijkheid(getApplication().getServices()
        .getDocumentService()
        .getStandaardVertrouwelijkheid(null, null));
    setBean(bean);
    super.attach();
  }

  public DocumentVertrouwelijkheid getVertrouwelijkheid() {
    commit();
    return getBean().getVertrouwelijkheid();
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class AttachmentBean implements Serializable {

    @Field(caption = "Vertrouwelijkheid",
        customTypeClass = GbaNativeSelect.class,
        required = true)
    @Select(containerDataSource = DocumentVertrouwelijkheidContainer.class,
        nullSelectionAllowed = false)
    private DocumentVertrouwelijkheid vertrouwelijkheid;
  }
}
