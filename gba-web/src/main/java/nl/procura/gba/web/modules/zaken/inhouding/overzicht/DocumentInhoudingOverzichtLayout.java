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

package nl.procura.gba.web.modules.zaken.inhouding.overzicht;

import static nl.procura.gba.web.modules.zaken.inhouding.page2.Page2InhoudingBean2.NUMMERPV;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.REISD_PV_NR;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.emp;

import com.vaadin.ui.Field;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.modules.zaken.inhouding.page2.Page2InhoudingForm1;
import nl.procura.gba.web.modules.zaken.inhouding.page2.Page2InhoudingForm2;
import nl.procura.gba.web.modules.zaken.inhouding.page2.Page2InhoudingForm3;
import nl.procura.gba.web.modules.zaken.inhouding.page2.Page2InhoudingForm4;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType;
import nl.procura.gba.web.services.zaken.reisdocumenten.Reisdocument;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class DocumentInhoudingOverzichtLayout extends GbaVerticalLayout {

  private final DocumentInhouding   zaak;
  private final Reisdocument        reisdocument;
  private final boolean             readOnlyDatumGeldigheid;
  private final boolean             readOnlyVermissing;
  private       Page2InhoudingForm2 form2;
  private       Page2InhoudingForm3 form3;

  public DocumentInhoudingOverzichtLayout(DocumentInhouding zaak, Reisdocument reisdocument,
      boolean readOnlyDatumGeldigheid,
      boolean readOnlyVermissing) {

    this.zaak = zaak;
    this.reisdocument = reisdocument;
    this.readOnlyDatumGeldigheid = readOnlyDatumGeldigheid;
    this.readOnlyVermissing = readOnlyVermissing;
    setSpacing(true);
  }

  @Override
  public void attach() {

    if (getComponentCount() == 0) {
      if (zaak != null) {
        Page2InhoudingForm1 form1 = new Page2InhoudingForm1(zaak, reisdocument);
        if (zaak.isStored()) {
          form1.setWidth("600px");
          Page2InhoudingForm4 form4 = new Page2InhoudingForm4(zaak);
          add(new HLayout().add(form1).addExpandComponent(form4).spacing(true).widthFull());
        } else {
          addComponent(form1);
        }

        addComponent(new Fieldset("Inhouding op de persoonslijst"));
        if (zaak.isVrsOnlyBasisregister()) {
          addComponent(new InfoLayout("Dit document komt niet (meer) voor op de persoonslijst."));
        }
        form3 = new Page2InhoudingForm3(zaak, readOnlyDatumGeldigheid);
        addComponent(this.form3);
      }

      if (isVermissing()) {
        addComponent(new Fieldset("Proces-verbaal"));
        form2 = new Page2InhoudingForm2(zaak, reisdocument, readOnlyVermissing);
        addComponent(form2);

        if (isStandaardPv()) {
          String info = "Het veld <b>nummer proces-verbaal</b> is gevuld met de standaardwaarde.";
          addComponent(new InfoLayout("", info), getComponentIndex(form2));
        }
      }
    }

    super.attach();
  }

  public void commit() {

    if (form3 != null) {
      form3.commit();
      zaak.setDatumIngang(new DateTime(form3.getBean().getDatum()));
    }

    if (isVermissing()) {
      form2.commit();
      zaak.setProcesVerbaalNummer(form2.getBean().getNummerPv());
      zaak.setProcesVerbaalOms(form2.getBean().getVerzoekOrgAanvraag());
    }
  }

  public void save() {
    getApplication().getServices().getDocumentInhoudingenService().save(zaak);
  }

  private boolean isStandaardPv() {
    Field nummerPv = form2.getField(NUMMERPV);
    String standaardPvNr = getApplication().getParmValue(REISD_PV_NR);
    String nummerPvNr = astr(nummerPv.getValue());

    if (emp(nummerPvNr) || standaardPvNr.equals(nummerPvNr)) {
      nummerPv.setValue(standaardPvNr);
      return true;
    }

    return false;
  }

  private boolean isVermissing() {
    return zaak != null && InhoudingType.VERMISSING.equals(zaak.getInhoudingType());
  }
}