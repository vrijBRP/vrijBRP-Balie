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

package nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page3;

import static com.vaadin.ui.Label.CONTENT_XHTML;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page3.Page3VrsBasisRegisterBean1.DATUM_INLEVERING;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page3.Page3VrsBasisRegisterBean1.DATUM_REDEN_MELDING;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page3.Page3VrsBasisRegisterBean1.DOC_NR;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page3.Page3VrsBasisRegisterBean1.DOC_SOORT;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page3.Page3VrsBasisRegisterBean1.INGELEVERD;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page3.Page3VrsBasisRegisterBean1.MELDING_TYPE;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page3.Page3VrsBasisRegisterBean1.REDEN_TYPE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_REIT_INSTRUCTIE_URL;

import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import java.util.function.Consumer;
import nl.procura.burgerzaken.vrsclient.api.VrsActieType;
import nl.procura.burgerzaken.vrsclient.api.VrsMeldingRedenType;
import nl.procura.burgerzaken.vrsclient.api.VrsMeldingType;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.zaken.inhouding.page2.VrsMeldingRedenTypeContainer;
import nl.procura.gba.web.modules.zaken.inhouding.page2.VrsMeldingTypeContainer;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page3VrsBasisRegisterForm1 extends GbaForm<Page3VrsBasisRegisterBean1> {

  private final VrsActieType            actieType;
  private final DocumentInhouding       inhouding;
  private final Consumer<InhoudingType> inhoudingSupplier;

  private Label dateHelp = new Label("", CONTENT_XHTML);

  public Page3VrsBasisRegisterForm1(VrsActieType actieType, DocumentInhouding inhouding, Consumer<InhoudingType> inhoudingSupplier) {
    this.actieType = actieType;
    this.inhouding = inhouding;
    this.inhoudingSupplier = inhoudingSupplier;
    setCaption("BR registratie melding");
    setColumnWidths("160px", "");
    setOrder(DOC_NR, DOC_SOORT, MELDING_TYPE, REDEN_TYPE, DATUM_REDEN_MELDING, INGELEVERD, DATUM_INLEVERING);
    update();
  }

  public void update() {
    Page3VrsBasisRegisterBean1 bean = new Page3VrsBasisRegisterBean1();
    bean.setDocumentnummer(inhouding.getNummerDocument());
    bean.setDocumentSoort(inhouding.getDocumentType().getOms());
    bean.setMeldingType(inhouding.getVrsMelding());
    bean.setRedenType(inhouding.getVrsReden());
    setBean(bean);
  }

  @Override
  public void setValue(String field, Object value) {
    super.setValue(field, value);
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {
    if (property.is(DATUM_REDEN_MELDING)) {
      column.addComponent(dateHelp);
    }
    super.afterSetColumn(column, field, property);
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
    ProNativeSelect meldingField = getField(MELDING_TYPE, ProNativeSelect.class);
    meldingField.setContainerDataSource(new VrsMeldingTypeContainer(actieType));

    // Update reden container on change of melding type
    ProNativeSelect redenField = getField(REDEN_TYPE, ProNativeSelect.class);
    meldingField.addListener((ValueChangeListener) event -> {
      VrsMeldingType type = (VrsMeldingType) event.getProperty().getValue();
      if (type != null) {
        redenField.setContainerDataSource(new VrsMeldingRedenTypeContainer(actieType));
        repaint();
      }
    });
    redenField.setContainerDataSource(new VrsMeldingRedenTypeContainer(actieType));
    redenField.addListener((ValueChangeListener) event -> updateDateHelp((VrsMeldingRedenType) event.getProperty().getValue()));

    GbaNativeSelect ingeleverd = getField(INGELEVERD, GbaNativeSelect.class);
    ingeleverd.setVisible(actieType == VrsActieType.RECHTSW_OVERIG);
    ingeleverd.addListener((ValueChangeListener) event -> updateIngeleverd((Boolean) event.getProperty().getValue()));
  }

  private void updateIngeleverd(Boolean ingeleverd) {
    DatumVeld datumInlevering = getField(DATUM_INLEVERING, DatumVeld.class);
    datumInlevering.setVisible(Boolean.TRUE.equals(ingeleverd));
    inhoudingSupplier.accept(ingeleverd ? InhoudingType.INHOUDING : inhouding.getInhoudingType());
    dateHelp = new Label("", CONTENT_XHTML);
    repaint();
    updateDateHelp((VrsMeldingRedenType) getField(REDEN_TYPE, ProNativeSelect.class).getValue());
  }

  private void updateDateHelp(VrsMeldingRedenType type) {
    String toelichting = VrsRedenDatumToelichting.get(VrsMeldingType.getByCode(inhouding.getVrsMeldingType()), type).getToelichting();
    String url = getApplication().getServices().getParameterService().getSysteemParm(VRS_REIT_INSTRUCTIE_URL, false);
    toelichting = toelichting.replaceAll("#", url);
    dateHelp.setValue(toelichting);
    VaadinUtils.resetHeight(getWindow());
  }

  @Override
  public void attach() {
    updateDateHelp(inhouding.getVrsReden());
    updateIngeleverd(false);
    super.attach();
  }
}
