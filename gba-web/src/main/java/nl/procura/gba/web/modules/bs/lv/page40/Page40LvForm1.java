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

package nl.procura.gba.web.modules.bs.lv.page40;

import static nl.procura.burgerzaken.gba.StringUtils.isBlank;
import static nl.procura.gba.web.services.bs.algemeen.enums.LvDocumentType.ANDERS;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.DATUM_VERZOEKSCHRIFT;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.TWEEDE_DOC;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.UITSPRAAK;

import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

import nl.procura.gba.web.services.bs.algemeen.enums.LvDocumentType;
import nl.procura.gba.web.services.bs.algemeen.enums.RechtbankLocatie;
import nl.procura.gba.web.services.bs.lv.afstamming.DossierLv;
import nl.procura.gba.web.services.bs.lv.afstamming.LvDocumentDoorType;
import nl.procura.gba.web.services.bs.lv.afstamming.LvField;
import nl.procura.gba.web.services.bs.lv.afstamming.LvSoortVerbintenisType;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page40LvForm1 extends Page40LvForm {

  public Page40LvForm1() {
    setCaption("Gegevens ontvangen document(en)");
    setColumnWidths("220px", "");
  }

  public void setBean(DossierLv zaakDossier) {
    setOrder(LvField.getForm1(zaakDossier.getSoort()));
    Page40LvBean1 bean = new Page40LvBean1();

    RechtbankLocatie rechtbankLocatie = RechtbankLocatie.get(zaakDossier.getUitspraak());
    if (RechtbankLocatie.ONBEKEND != rechtbankLocatie) {
      bean.setUitspraak(rechtbankLocatie);
    } else {
      if (isBlank(zaakDossier.getUitspraak())) {
        bean.setUitspraak(null);
      } else {
        bean.setUitspraak(RechtbankLocatie.ANDERS);
        bean.setUitspraakAnders(zaakDossier.getUitspraak());
      }
    }

    bean.setDatumUitspraak(zaakDossier.getDatumUitspraak());
    bean.setDatumGewijsde(zaakDossier.getDatumGewijsde());
    bean.setDatumVerzoekschrift(zaakDossier.getDatumVerzoekschrift());

    bean.setSoortVerbintenis(LvSoortVerbintenisType.get(zaakDossier.getSoortVerbintenis()));
    bean.setNummer(zaakDossier.getDocNr());
    bean.setDatum(zaakDossier.getDocDatum());
    bean.setPlaats(zaakDossier.getDocPlaats());
    bean.setDoor(LvDocumentDoorType.get(zaakDossier.getDocDoor().intValue()));

    LvDocumentType document = LvDocumentType.get(zaakDossier.getDoc());
    if (LvDocumentType.ONBEKEND != document) {
      bean.setDocument(document);
    } else {
      if (isBlank(zaakDossier.getDoc())) {
        bean.setDocument(null);
      } else {
        bean.setDocument(ANDERS);
        bean.setDocumentAnders(zaakDossier.getDoc());
      }
    }

    bean.setTweedeDocument(zaakDossier.getTweedeDoc());
    bean.setTweedeDocumentDatum(zaakDossier.getTweedeDocDatum());
    bean.setTweedeDocumentPlaats(zaakDossier.getTweedeDocPlaats());

    LvDocumentType tweedeDocumentOms = LvDocumentType.get(zaakDossier.getTweedeDocOms());
    if (LvDocumentType.ONBEKEND != tweedeDocumentOms) {
      bean.setTweedeDocumentOms(tweedeDocumentOms);
    } else {
      if (isBlank(zaakDossier.getTweedeDocOms())) {
        bean.setTweedeDocumentOms(null);
      } else {
        bean.setTweedeDocumentOms(ANDERS);
        bean.setTweedeDocumentOmsAnders(zaakDossier.getTweedeDocOms());
      }
    }

    setBean(bean);
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {
    if (property.is(DATUM_VERZOEKSCHRIFT.getName())) {
      column.addComponent(new Label(" (bij adoptie vóór of binnen 6 mnd. na geboorte)"));
    }
    super.afterSetColumn(column, field, property);
  }

  @Override
  public void afterSetBean() {
    Field uitspraak = getLvField(UITSPRAAK);
    if (uitspraak != null) {
      uitspraak.addListener((ValueChangeListener) event -> {
        checkUitspraakAnders((RechtbankLocatie) event.getProperty().getValue());
      });
      checkUitspraakAnders((RechtbankLocatie) uitspraak.getValue());
    }

    Field doc = getLvField(LvField.DOCUMENT);
    if (doc != null) {
      doc.addListener((ValueChangeListener) event -> {
        checkDocumentAnders((LvDocumentType) event.getProperty().getValue());
      });
      checkDocumentAnders((LvDocumentType) doc.getValue());
    }

    Field tweedeDoc = getLvField(TWEEDE_DOC);
    if (tweedeDoc != null) {
      tweedeDoc.addListener((ValueChangeListener) event -> {
        checkTweedeDocument((Boolean) event.getProperty().getValue());
      });
      checkTweedeDocument((Boolean) tweedeDoc.getValue());
    }

    Field tweedeDocOms = getLvField(LvField.TWEEDE_DOC_OMS);
    if (tweedeDocOms != null) {
      tweedeDocOms.addListener((ValueChangeListener) event -> {
        checkTweedeDocumentAnders((LvDocumentType) event.getProperty().getValue());
      });
      checkTweedeDocumentAnders((LvDocumentType) tweedeDocOms.getValue());
    }
    super.afterSetBean();
  }

  private void checkUitspraakAnders(RechtbankLocatie rechtbankLocatie) {
    boolean isAnders = RechtbankLocatie.ANDERS == rechtbankLocatie;
    Field anders = getLvField(LvField.UITSPRAAK_ANDERS);
    if (isAnders) {
      anders.setVisible(true);
    } else {
      anders.setValue("");
      anders.setVisible(false);
    }
    repaint();
  }

  private void checkDocumentAnders(LvDocumentType document) {
    boolean isAnders = ANDERS == document;
    Field anders = getLvField(LvField.DOCUMENT_ANDERS);
    if (isAnders) {
      anders.setVisible(true);
    } else {
      anders.setValue("");
      anders.setVisible(false);
    }
    repaint();
  }

  private void checkTweedeDocument(Boolean value) {
    boolean visible = Boolean.TRUE.equals(value);
    boolean tweedeDocumentAnders = ANDERS == getLvField(LvField.TWEEDE_DOC_OMS).getValue();
    getLvField(LvField.TWEEDE_DOC_OMS).setVisible(visible);
    getLvField(LvField.TWEEDE_DOC_OMS_ANDERS).setVisible(visible && tweedeDocumentAnders);
    getLvField(LvField.TWEEDE_DOC_DATUM).setVisible(visible);
    getLvField(LvField.TWEEDE_DOC_PLAATS).setVisible(visible);
    repaint();
  }

  private void checkTweedeDocumentAnders(LvDocumentType tweedeDocument) {
    boolean isAnders = ANDERS == tweedeDocument;
    Field anders = getLvField(LvField.TWEEDE_DOC_OMS_ANDERS);
    if (isAnders) {
      anders.setVisible(true);
    } else {
      anders.setValue("");
      anders.setVisible(false);
    }
    repaint();
  }

  @Override
  public Page40LvBean1 getNewBean() {
    return new Page40LvBean1();
  }
}
