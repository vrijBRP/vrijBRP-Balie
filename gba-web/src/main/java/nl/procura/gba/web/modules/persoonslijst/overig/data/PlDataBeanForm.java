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

package nl.procura.gba.web.modules.persoonslijst.overig.data;

import static nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBean.AKTE;
import static nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBean.ONJUIST;
import static nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBean.VORIGANR;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.Field;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.UnknownGBAElementException;
import nl.procura.gba.web.modules.persoonslijst.overig.PlForm;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class PlDataBeanForm extends PlForm {

  private final PlDataBean bean = new PlDataBean();
  private BasePLRec        record;

  public PlDataBeanForm(BasePLRec record) {

    setRecord(record);
    setColumnWidths("90px", "");
    fillMetaBean(bean);
  }

  @Override
  public Object getNewBean() {
    return bean;
  }

  public BasePLRec getRecord() {
    return record;
  }

  public void setRecord(BasePLRec record) {
    this.record = record;
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(VORIGANR, ONJUIST, AKTE)) {
      getLayout().addBreak();
    }
  }

  private void fillMetaBean(PlDataBean metaBean) {

    // 20
    metaBean.setVoriganr(getMetaElement(GBAElem.VORIG_A_NUMMER));
    metaBean.setVolgendanr(getMetaElement(GBAElem.VOLGEND_A_NUMMER));

    // 75
    String indDoc = getMetaElement(GBAElem.IND_DOC);
    metaBean.setInddocument(fil(indDoc) ? "niet-verwerkt document" : "");

    // 81
    String akteNr = getMetaElement(GBAElem.AKTENR);
    String akteGem = getMetaElement(GBAElem.REGIST_GEM_AKTE);
    metaBean.setAkte(akteNr + " " + akteGem);

    // 82
    String datDoc = getMetaElement(GBAElem.DATUM_DOC);
    String gemDoc = getMetaElement(GBAElem.GEMEENTE_DOC);
    String beschrDoc = getMetaElement(GBAElem.BESCHRIJVING_DOC);
    metaBean.setOntlening(datDoc + " " + gemDoc);
    metaBean.setDocument(beschrDoc);

    // Geen admin tonen. Hoort eigenlijk niet in Onjuist veld
    //  83
    metaBean.setInonderzoek(getMetaElement(GBAElem.AAND_GEG_IN_ONDERZ));
    metaBean.setDatumingang(getMetaElement(GBAElem.DATUM_INGANG_ONDERZ));
    metaBean.setDatumeinde(getMetaElement(GBAElem.DATUM_EINDE_ONDERZ));

    // 84
    String indOnjuist = getMetaElement(GBAElem.IND_ONJUIST);
    metaBean.setOnjuist(indOnjuist.contains("Adm") ? "" : indOnjuist);

    // 85
    metaBean.setGeldigheid(getMetaElement(GBAElem.INGANGSDAT_GELDIG));

    // 86
    metaBean.setOpname(getMetaElement(GBAElem.DATUM_VAN_OPNEMING));
  }

  private String getMetaElement(GBAElem type) {

    try {
      return getRecord().getElemVal(type).getDescr();
    } catch (UnknownGBAElementException e) {
      // Do not catch
    }

    return "";
  }
}
