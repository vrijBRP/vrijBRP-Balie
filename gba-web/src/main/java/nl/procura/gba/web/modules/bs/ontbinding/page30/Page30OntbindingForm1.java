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

package nl.procura.gba.web.modules.bs.ontbinding.page30;

import static nl.procura.gba.web.modules.bs.ontbinding.page30.Page30OntbindingBean1.*;
import static nl.procura.gba.web.modules.bs.ontbinding.page30.Page30OntbindingBean4.DATUM_INGANG;

import java.util.Date;

import com.vaadin.ui.Component;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.buttons.KennisbankButton;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.components.validators.DatumVolgordeValidator;
import nl.procura.gba.web.modules.bs.ontbinding.page30.Page30Ontbinding.FormIngang;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.bs.algemeen.enums.RechtbankLocatie;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page30OntbindingForm1 extends GbaForm<Page30OntbindingBean1> {

  private final GbaApplication     application;
  private final BinnenTermijnLabel binnenTermijnLabel = new BinnenTermijnLabel();
  private FormIngang               formIngang;

  public Page30OntbindingForm1(GbaApplication application) {

    this.application = application;

    setCaption("Gegevens ontvangen document(en)");
    setColumnWidths("200px", "");
    setOrder(UITSPRAAK, DATUM_UITSPRAAK, DATUM_GEWIJSDE, VERZOEK_INSCHRIJVING_DOOR, DATUM_VERZOEK, BINNEN_TERMIJN);
  }

  @Override
  public void afterSetColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (application != null) {
      if (property.is(DATUM_GEWIJSDE)) {
        String url = application.getParmValue(ParameterConstant.ONTBINDING_KB_URL);
        Component kb = new KennisbankButton(url);
        column.addComponent(kb);
      }
      if (property.is(BINNEN_TERMIJN)) {
        column.addComponent(binnenTermijnLabel);
      }
    }

    super.afterSetColumn(column, field, property);
  }

  public void setBean(DossierOntbinding zaakDossier) {

    DossierPersoon p1 = zaakDossier.getPartner1();
    DossierPersoon p2 = zaakDossier.getPartner2();

    Page30OntbindingBean1 bean = new Page30OntbindingBean1();

    if (RechtbankLocatie.ONBEKEND != zaakDossier.getUitspraakDoor()) {
      bean.setUitspraak(zaakDossier.getUitspraakDoor());
    }

    bean.setDatumUitspraak(zaakDossier.getDatumUitspraak().getDate());
    bean.setDatumGewijsde(zaakDossier.getDatumGewijsde().getDate());
    bean.setVerzoekInschrijvingDoor(zaakDossier.getVerzoekTotInschrijvingDoor());
    bean.setDatumVerzoek(zaakDossier.getDatumVerzoek().getDate());
    bean.setBinnenTermijn(zaakDossier.isBinnenTermijn());

    setBean(bean);

    GbaNativeSelect verzoekInschrijvingDoor = getField(VERZOEK_INSCHRIJVING_DOOR, GbaNativeSelect.class);
    ProDateField datumUitspraak = getField(DATUM_UITSPRAAK, ProDateField.class);
    ProDateField datumGewijsde = getField(DATUM_GEWIJSDE, ProDateField.class);
    ProDateField datumVerzoek = getField(DATUM_VERZOEK, ProDateField.class);

    VerzoekInschrijvingContainer verzoekContainer = new VerzoekInschrijvingContainer(p1, p2);
    verzoekInschrijvingDoor.setContainerDataSource(verzoekContainer);
    verzoekInschrijvingDoor.setValue(zaakDossier.getVerzoekTotInschrijvingDoor());

    datumGewijsde.addValidator(
        new DatumVolgordeValidator("Datum uitspraak", datumUitspraak, "Datum kracht van gewijsde",
            datumGewijsde));
    datumVerzoek.addValidator(
        new DatumVolgordeValidator("Datum kracht van gewijsde", datumGewijsde, "Verzoek ontvangen op",
            datumVerzoek));

    datumGewijsde.addListener(new BinnenTermijnListenerField(datumVerzoek, false));
    datumVerzoek.addListener(new BinnenTermijnListenerField(datumGewijsde, true));
    datumVerzoek.addListener(new VerzoekListenerField());

    String dGewijsde = new DateTime(bean.getDatumGewijsde()).getStringDate();
    String dVerzoek = new DateTime(bean.getDatumVerzoek()).getStringDate();

    binnenTermijnLabel.setDatum(dGewijsde, dVerzoek);
  }

  public FormIngang getFormIngang() {
    return formIngang;
  }

  public void setFormIngang(FormIngang formIngang) {
    this.formIngang = formIngang;
  }

  @Override
  public Page30OntbindingBean1 getNewBean() {
    return new Page30OntbindingBean1();
  }

  public class BinnenTermijnListenerField extends FieldChangeListener<Date> {

    private final boolean datumVerzoekGewijzigd;
    private ProDateField  datumVerzoek  = null;
    private ProDateField  datumGewijsde = null;

    public BinnenTermijnListenerField(ProDateField gewijzigdeDatum, boolean datumVerzoekGewijzigd) {

      this.datumVerzoekGewijzigd = datumVerzoekGewijzigd;

      if (datumVerzoekGewijzigd) {
        this.datumGewijsde = gewijzigdeDatum;
      } else {
        this.datumVerzoek = gewijzigdeDatum;
      }
    }

    @Override
    public void onChange(Date gewijzigdeWaarde) {

      String dGewijsde = getDatumGewijsde(gewijzigdeWaarde);
      String dVerzoek = getDatumVerzoek(gewijzigdeWaarde);
      binnenTermijnLabel.setDatum(dGewijsde, dVerzoek);

      // Update het veld
      getField(BINNEN_TERMIJN).setValue(binnenTermijnLabel.isBinnenTermijn(dGewijsde, dVerzoek));
    }

    private String getDatumGewijsde(Date gewijzigdeWaarde) {

      String dGewijsde;

      if (datumVerzoekGewijzigd) {
        dGewijsde = new DateTime((Date) datumGewijsde.getValue()).getStringDate();
      } else {
        dGewijsde = new DateTime(gewijzigdeWaarde).getStringDate();
      }

      return dGewijsde;
    }

    private String getDatumVerzoek(Date gewijzigdeWaarde) {

      String dVerzoek;

      if (datumVerzoekGewijzigd) {
        dVerzoek = new DateTime(gewijzigdeWaarde).getStringDate();
      } else {
        dVerzoek = new DateTime((Date) datumVerzoek.getValue()).getStringDate();
      }

      return dVerzoek;
    }
  }

  public class VerzoekListenerField extends FieldChangeListener<Date> {

    @Override
    public void onChange(Date value) {
      getFormIngang().getField(DATUM_INGANG, ProDateField.class).setValue(value);
    }
  }
}
