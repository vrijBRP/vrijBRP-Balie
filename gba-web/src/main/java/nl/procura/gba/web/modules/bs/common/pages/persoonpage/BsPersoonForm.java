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

package nl.procura.gba.web.modules.bs.common.pages.persoonpage;

import static nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsPersoonBean1.*;
import static nl.procura.standard.Globalfunctions.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;
import nl.procura.validation.Bsn;

public class BsPersoonForm extends GbaForm<BsPersoonBean1> {

  private BsPersoonRequirement requirement;
  private DossierPersoon       dossierPersoon;

  public BsPersoonForm(DossierPersoon dossierPersoon, BsPersoonRequirement requirement) {

    this.requirement = requirement;
    this.dossierPersoon = dossierPersoon;

    setColumnWidths("150px", "249px", "120px", "");
  }

  @Override
  public void setBean(Object bean) {

    super.setBean(bean);
    for (String field : requirement.getBeanFields()) {
      if (getField(field) != null) {
        getField(field).setRequired(true);
      }
    }

    repaint();
  }

  public DossierPersoon getDossierPersoon() {
    return dossierPersoon;
  }

  public void setDossierPersoon(DossierPersoon dossierPersoon) {
    this.dossierPersoon = dossierPersoon;
  }

  @Override
  public BsPersoonBean1 getNewBean() {
    return new BsPersoonBean1();
  }

  public BsPersoonRequirement getRequirement() {
    return requirement;
  }

  public void setRequirement(BsPersoonRequirement requirement) {
    this.requirement = requirement;
  }

  @Override
  public Field newField(Field field, Property property) {

    super.newField(field, property);

    field.setReadOnly(isCorrecteBsn() || property.is(TYPE, BSN, NAT));

    if (property.is(GEBOORTEPLAATS_AKTE, GEBOORTELAND_AKTE, WOONLAND, WOONLAND_AKTE, WOONPLAATS_NL, WOONPLAATS_BL,
        WOONPLAATS_AKTE, AKTENAAM, TOELICHTING)) {
      field.setReadOnly(false);
    }

    if (property.is(WOONGEMEENTE, STATUS)) {
      field.setReadOnly(true);
      field.setVisible(isCorrecteBsn());
    }

    return field;
  }

  @Override
  public void reset() {
    BsPersoonUtils.reset(getDossierPersoon());
  }

  @Override
  public void setBean(Object bean, String... order) {

    super.setBean(bean, order);

    if (getField(GEBOORTELAND) != null) {
      getField(GEBOORTELAND).addListener((ValueChangeListener) event -> changeGeboorteland());

      changeGeboorteland();
    }

    if (getField(WOONLAND) != null) {
      getField(WOONLAND).addListener((ValueChangeListener) event -> changeWoonland());

      changeWoonland();
    }
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(WOONGEMEENTE, NAT, VBT, STRAAT, PC, TOELICHTING)) {
      column.setColspan(3);
    }

    if (property.is(HNR, HNRL, HNRT, HNRA)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }

  public void update() {

    BsPersoonBean1 bean = new BsPersoonBean1();

    DossierPersoon p = getDossierPersoon();

    bean.setType(p.getDossierPersoonType().getDescr());
    bean.setBsn(p.getBurgerServiceNummer().getDescription());
    bean.setNaam(p.getGeslachtsnaam());
    bean.setVoorv(new FieldValue(p.getVoorvoegsel()));
    bean.setTitel(p.getTitel());
    bean.setVoorn(p.getVoornaam());
    bean.setNg(p.getNaamgebruik());
    bean.setAktenaam(p.getAktenaam());

    if (p.getGeslacht() != Geslacht.NIET_VAST_GESTELD) {
      bean.setGeslacht(p.getGeslacht());
    }

    // Geboorte
    bean.setGeboortedatum(p.getDatumGeboorte());

    if (p.getGeboorteland() != null) {
      bean.setGeboorteland(p.getGeboorteland());
    }

    if (p.getWoongemeente() != null) {
      bean.setWoongemeente(p.getWoongemeente());
    }

    if (p.getGeboorteplaats() != null) {
      if (Landelijk.isNederlandOfOnbekend(p.getGeboorteland())) {
        bean.setGeboorteplaatsNL(p.getGeboorteplaats());
      } else {
        bean.setGeboorteplaatsBuitenland(p.getGeboorteplaats().getDescription());
      }
    }

    bean.setGeboortelandAkte(p.getGeboortelandAkte());

    // Als er nog geen geboortelandAkte is

    if (emp(bean.getGeboortelandAkte())) {

      FieldValue geboorteland = bean.getGeboorteland();
      if (geboorteland != null && fil(geboorteland.getDescription())) {
        bean.setGeboortelandAkte(geboorteland.getDescription());
      }
    }

    bean.setGeboorteplaatsAkte(p.getGeboorteplaatsAkte());

    // Als er nog geen geboorteplaatsAkte is

    if (emp(bean.getGeboorteplaatsAkte())) {

      FieldValue geboorteplaatsNl = bean.getGeboorteplaatsNL();
      String geboorteplaatsBu = bean.getGeboorteplaatsBuitenland();

      if (geboorteplaatsNl != null && fil(geboorteplaatsNl.getDescription())) {
        bean.setGeboorteplaatsAkte(geboorteplaatsNl.getDescription());
      } else if (fil(geboorteplaatsBu)) {
        bean.setGeboorteplaatsAkte(geboorteplaatsBu);
      }
    }

    // Overlijden
    bean.setOverlijdensdatum(p.getDatumOverlijden());

    // Immigratie
    bean.setImmigratiedatum(p.getDatumImmigratie());
    bean.setImmigratieland(p.getImmigratieland());

    // Adres
    bean.setStraat(p.getStraat());
    bean.setHnr(pos(p.getHuisnummer()) ? astr(p.getHuisnummer()) : "");
    bean.setHnrL(p.getHuisnummerLetter());
    bean.setHnrT(p.getHuisnummerToev());
    bean.setHnrA(new FieldValue(p.getHuisnummerAand()));
    bean.setPc(p.getPostcode());

    // Land + plaats
    // Als het land NL is dan veld woonplaatsNL vullen.
    if (p.getLand() != null) {
      bean.setWoonland(p.getLand());

      if (p.getWoonplaats() != null) {
        boolean isWoonNl = Landelijk.isNederlandOfOnbekend(bean.getWoonland());

        if (isWoonNl) {
          bean.setWoonplaatsNL(p.getWoonplaats());
        } else {
          bean.setWoonplaatsBuitenland(p.getWoonplaats().getDescription());
        }
      }
    }

    bean.setWoonplaatsAkte(p.getWoonplaatsAkte());

    if (emp(bean.getWoonplaatsAkte())) {

      FieldValue woonplaatsNl = bean.getWoonplaatsNL();
      String woonplaatsBu = bean.getWoonplaatsBuitenland();

      if (woonplaatsNl != null && fil(woonplaatsNl.getDescription())) {
        bean.setWoonplaatsAkte(woonplaatsNl.getDescription());
      } else if (fil(woonplaatsBu)) {
        bean.setWoonplaatsAkte(woonplaatsBu);
      }
    }

    // Woonland Akte
    bean.setWoonlandAkte(p.getWoonlandAkte());

    if (emp(bean.getWoonlandAkte())) {
      FieldValue woonland = bean.getWoonland();

      if (woonland != null) {
        bean.setWoonlandAkte(woonland.getDescription());
      }
    }

    bean.setBs(p.getBurgerlijkeStaat());
    bean.setBsVanaf(new GbaDateFieldValue(p.getDatumBurgerlijkeStaat().getLongDate()));
    bean.setVbt(p.getVerblijfstitel());
    bean.setNat(getNationaliteiten());
    bean.setVerstrek(p.isVerstrekkingsbeperking());
    bean.setCuratele(p.isOnderCuratele());
    bean.setToelichting(p.getToelichting());

    setBean(bean);
  }

  protected String getNationaliteiten() {

    StringBuilder nats = new StringBuilder();

    for (DossierNationaliteit nat : getDossierPersoon().getNationaliteiten()) {

      nats.append(nat.getNationaliteit().getDescription());

      if (aval(nat.getDatumVerkrijging().getLongDate()) == 0) {

        nats.append(" (sinds 00-00-0000)");
      } else if (pos(nat.getDatumVerkrijging().getLongDate())) {

        nats.append(" (sinds " + nat.getDatumVerkrijging().getFormatDate() + ")");
      } else {
        nats.append(" (sinds " + nat.getVerkrijgingType().getDescr().toLowerCase() + ")");
      }

      nats.append(", ");
    }

    if (emp(trim(nats.toString()))) {
      nats = new StringBuilder("Geen nationaliteiten ingegeven.");
    }

    return trim(nats.toString());
  }

  private void changeGeboorteland() {

    FieldValue fv = (FieldValue) getField(GEBOORTELAND).getValue();

    if (fv == null || Landelijk.isNederlandOfOnbekend(fv)) {

      getField(GEBOORTEPLAATS_NL).setVisible(true);
      getField(GEBOORTEPLAATS_BL).setVisible(false);
    } else {

      getField(GEBOORTEPLAATS_NL).setVisible(false);
      getField(GEBOORTEPLAATS_BL).setVisible(true);
    }

    repaint();
  }

  private void changeWoonland() {

    FieldValue fv = (FieldValue) getField(WOONLAND).getValue();

    if (fv == null || Landelijk.isNederland(fv)) {

      getField(WOONPLAATS_NL).setVisible(true);
      getField(WOONPLAATS_BL).setVisible(false);
    } else {

      getField(WOONPLAATS_NL).setVisible(false);
      getField(WOONPLAATS_BL).setVisible(true);
    }

    repaint();
  }

  private boolean isCorrecteBsn() {
    Bsn bsn = new Bsn(getBean().getBsn());
    return (!bsn.isEmpty() && bsn.isCorrect());
  }
}
