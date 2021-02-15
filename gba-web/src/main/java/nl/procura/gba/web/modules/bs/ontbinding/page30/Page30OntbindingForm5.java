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

import static nl.procura.gba.web.modules.bs.ontbinding.page30.Page30OntbindingBean5.*;
import static nl.procura.gba.web.services.gba.basistabellen.belanghebbende.BelanghebbendeType.ADVOCATEN_KANTOOR;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import com.vaadin.ui.Field;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.gebruiker.info.GebruikerInfoType;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;
import nl.procura.gba.web.services.gba.basistabellen.belanghebbende.Belanghebbende;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page30OntbindingForm5 extends GbaForm<Page30OntbindingBean5> {

  private final GbaApplication application;

  public Page30OntbindingForm5(GbaApplication application) {

    this.application = application;
    setColumnWidths("200px", "280px", "100px", "");
    setOrder(KANTOREN, NAAM, TAV_AANHEF, TAV_VOORL, TAV_NAAM, ADRES, PC, PLAATS, KENMERK, LAND, KENMERK2);
  }

  @Override
  public void afterSetBean() {

    super.afterSetBean();

    getField(KANTOREN, ProNativeSelect.class).addListener(new FieldChangeListener<FieldValue>() {

      @Override
      public void onChange(FieldValue value) {

        if (AdvocatenkantorenContainer.ANDERS == value) {
          setFields(new Belanghebbende());
          setFields(true);
        } else if (value != null && (value.getValue() instanceof Belanghebbende)) {
          setFields((Belanghebbende) value.getValue());
          setFields(true);
        } else {
          setFields(false);
        }
      }
    });

    updateKantoren();
    setFields(fil(getBean().getNaam()));
  }

  @Override
  public void afterSetColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (property.is(KANTOREN)) {
      column.setColspan(3);
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public Page30OntbindingBean5 getNewBean() {
    return new Page30OntbindingBean5();
  }

  public void setBean(DossierOntbinding zaakDossier) {

    Page30OntbindingBean5 b = new Page30OntbindingBean5();

    b.setNaam(zaakDossier.getAdvocatenkantoor().getNaam());
    b.setTavAanhef(zaakDossier.getAdvocatenkantoor().getTavAanhef());
    b.setTavVoorl(zaakDossier.getAdvocatenkantoor().getTavVoorl());
    b.setTavNaam(zaakDossier.getAdvocatenkantoor().getTavNaam());
    b.setAdres(zaakDossier.getAdvocatenkantoor().getAdres());
    b.setPc(zaakDossier.getAdvocatenkantoor().getPostcode());
    b.setPlaats(zaakDossier.getAdvocatenkantoor().getPlaats());
    b.setLand(zaakDossier.getAdvocatenkantoor().getLand());
    b.setKenmerk(zaakDossier.getAdvocatenkantoor().getKenmerk());
    b.setKenmerk2(zaakDossier.getAdvocatenkantoor().getKenmerk2());

    setBean(b);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(TAV_VOORL, TAV_NAAM)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }

  public void updateKantoren() {
    List<Belanghebbende> belanghebbenden = application.getServices().getBelanghebbendeService().getBelanghebbenden(
        ADVOCATEN_KANTOOR, GeldigheidStatus.ACTUEEL);
    ProNativeSelect kantorenField = getField(KANTOREN, ProNativeSelect.class);
    kantorenField.setContainerDataSource(new AdvocatenkantorenContainer(belanghebbenden));
  }

  private void setFields(Belanghebbende belanghebbende) {

    getField(NAAM).setValue(belanghebbende.getNaam());
    getField(TAV_AANHEF).setValue(belanghebbende.getTerAttentieVanAanhef());
    getField(TAV_VOORL).setValue(belanghebbende.getTavVoorl());
    getField(TAV_NAAM).setValue(belanghebbende.getTavNaam());
    getField(ADRES).setValue(belanghebbende.getAdres());
    getField(PC).setValue(belanghebbende.getPostcode());
    getField(PLAATS).setValue(belanghebbende.getPlaats());
    getField(LAND).setValue(belanghebbende.getLand());
    getField(KENMERK).setValue(
        Services.getInstance().getGebruiker().getInformatie().getInfo(GebruikerInfoType.kenmerk).getWaarde());
    getField(KENMERK2).setValue("");
  }

  private void setFields(boolean visible) {

    getField(NAAM).setVisible(visible);
    getField(TAV_AANHEF).setVisible(visible);
    getField(TAV_VOORL).setVisible(visible);
    getField(TAV_NAAM).setVisible(visible);
    getField(ADRES).setVisible(visible);
    getField(PC).setVisible(visible);
    getField(PLAATS).setVisible(visible);
    getField(LAND).setVisible(visible);
    getField(KENMERK).setVisible(visible);
    getField(KENMERK2).setVisible(visible);

    if (!visible) { // Als alle velden leeg zijn dan veld kantoren op NVT zetten
      getField(KANTOREN).setValue(AdvocatenkantorenContainer.NVT);
    }

    repaint();
  }
}
