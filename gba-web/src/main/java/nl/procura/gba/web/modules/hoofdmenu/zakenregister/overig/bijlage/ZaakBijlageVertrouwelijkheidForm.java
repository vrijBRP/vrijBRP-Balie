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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bijlage;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Optional;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.form.document.DocumentVertrouwelijkheidContainer;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.documenten.DocumentVertrouwelijkheid;
import nl.procura.gba.web.services.zaken.documenten.dmstypes.DmsDocumentType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.container.ArrayListContainer;

import lombok.Data;

public class ZaakBijlageVertrouwelijkheidForm extends GbaForm<ZaakBijlageVertrouwelijkheidForm.AttachmentBean> {

  private static final String DOCUMENT_TYPE_OMSCHRIJVING = "documentTypeOmschrijving";
  private static final String VERTROUWELIJKHEID          = "vertrouwelijkheid";
  private static final String MAX_GROOTTE                = "maxGrootte";
  private final ZaakType      zaakType;

  public ZaakBijlageVertrouwelijkheidForm() {
    this(ZaakType.ONBEKEND);
  }

  public ZaakBijlageVertrouwelijkheidForm(ZaakType zaakType) {
    this.zaakType = zaakType;
    setOrder(DOCUMENT_TYPE_OMSCHRIJVING, VERTROUWELIJKHEID, MAX_GROOTTE);
    setCaption("Nieuw bestand");
    setColumnWidths(WIDTH_130, "");
  }

  @Override
  public void attach() {
    AttachmentBean bean = new AttachmentBean();
    Services services = getApplication().getServices();
    long maxFileSizeUploadsInMB = services.getDmsService().getMaxFileSizeUploadsInMB();
    bean.setVertrouwelijkheid(services
        .getDocumentService()
        .getStandaardVertrouwelijkheid(null, null));
    bean.setMaxGrootte(maxFileSizeUploadsInMB > 0 ? (maxFileSizeUploadsInMB + " MB") : "Onbeperkt");
    setBean(bean);
    super.attach();
  }

  @Override
  public void afterSetBean() {
    Services services = getApplication().getServices();
    DocumentTypeOmschrijvingContainer container = new DocumentTypeOmschrijvingContainer(services, zaakType);
    getField(DOCUMENT_TYPE_OMSCHRIJVING, GbaNativeSelect.class).setContainerDataSource(container);
  }

  public Optional<DmsDocumentType> getDmsDocumentType() {
    commit();
    return Optional.ofNullable(getBean().getDocumentTypeOmschrijving());
  }

  public DocumentVertrouwelijkheid getVertrouwelijkheid() {
    commit();
    return getBean().getVertrouwelijkheid();
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class AttachmentBean implements Serializable {

    @Field(customTypeClass = GbaNativeSelect.class,
        caption = "Documenttype",
        width = "250px")
    @TextField(maxLength = 250)
    private DmsDocumentType documentTypeOmschrijving;

    @Field(caption = "Vertrouwelijkheid",
        customTypeClass = GbaNativeSelect.class,
        width = "250px",
        required = true)
    @Select(containerDataSource = DocumentVertrouwelijkheidContainer.class,
        nullSelectionAllowed = false)
    private DocumentVertrouwelijkheid vertrouwelijkheid;

    @Field(type = FieldType.LABEL,
        caption = "Max. grootte",
        width = "250px")
    private String maxGrootte;
  }

  public static class DocumentTypeOmschrijvingContainer extends ArrayListContainer {

    public DocumentTypeOmschrijvingContainer(Services services, ZaakType zaakType) {
      services.getDocumentService().getDmsDocumentTypes().stream()
          .filter(t -> t.isZaakType(zaakType))
          .forEach(this::addItem);
    }
  }
}
