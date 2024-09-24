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

package nl.procura.gba.web.modules.bs.geboorte.page81;

import static nl.procura.gba.web.modules.bs.geboorte.page81.Page81GeboorteBean1.*;
import static nl.procura.standard.Globalfunctions.*;

import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.field.fieldvalues.TimeFieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;
import nl.procura.validation.Anr;
import nl.procura.validation.Bsn;

public class Page81GeboorteForm1 extends GbaForm<Page81GeboorteBean1> {

  private DossierGeboorte dossierGeboorte;
  private DossierPersoon  dossierPersoon;

  public Page81GeboorteForm1(DossierGeboorte dossierGeboorte, DossierPersoon dossierPersoon) {

    setDossierGeboorte(dossierGeboorte);
    setDossierPersoon(dossierPersoon);

    setCaption("Persoonsgegevens");
    setReadonlyAsText(false);
    setColumnWidths(WIDTH_130, "250px", "110px", "");

    setOrder(BSN, ANR, NAAM, NAAMSKEUZE, VOORV, TITEL, VOORN, GESLACHT, VERSTREK, GEBOORTEDATUM, GEBOORTETIJD,
        GEBOORTEPLAATS, GEBOORTELAND, WOONPLAATS, WOONLAND, VBT, NAT, TOELICHTING);

    update();
  }

  @Override
  public void afterSetBean() {

    // Als de erkenning is afgerond dan
    // kunnen de kindgegevens niet meer worden gewijzigd
    if (getDossierGeboorte().getVragen().heeftErkenningBijGeboorte()) {
      DossierErkenning erkenning = getDossierGeboorte().getErkenningBijGeboorte();
      if (erkenning != null && erkenning.getDossier().isAktesCorrect()) {
        getField(VOORN).setReadOnly(true);
        getField(VERSTREK).setReadOnly(true);
        getField(TITEL).setReadOnly(true);
        getField(GESLACHT).setReadOnly(true);
      }
    }

    super.afterSetBean();
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {

    if (property.is(VERSTREK)) {

      String typeOms;

      switch (getVerstrekkingsBeperkingOuders()) {
        case MOEDER:
          typeOms = " (De moeder heeft een verstrekkingsbeperking)";
          break;

        case VADER_DUO_MOEDER:
          typeOms = " (De vader / duo-moeder heeft een verstrekkingsbeperking)";
          break;

        case BEIDE:
          typeOms = " (Beide ouders hebben een verstrekkingsbeperking)";
          break;

        default:
          typeOms = " (Geen van de ouders heeft een verstrekkingsbeperking)";
      }

      column.addComponent(new Label(typeOms));
    }

    if (property.is(NAAM)) {

      // TODO Kennisbank - namenrecht

      FieldValue namenrecht = getDossierGeboorte().getLandNaamRecht();

      if (namenrecht != null) {

        // TODO Geboorte - kb button uitgezet. Marco vragen!
        // column.addComponent (new KennisbankButton (KennisBankBron.LAND, KennisBankDoel.NAMENRECHT, along (namenrecht.getValue ())));
      }
    }
  }

  public DossierGeboorte getDossierGeboorte() {
    return dossierGeboorte;
  }

  public void setDossierGeboorte(DossierGeboorte dossierGeboorte) {
    this.dossierGeboorte = dossierGeboorte;
  }

  public DossierPersoon getDossierPersoon() {
    return dossierPersoon;
  }

  public void setDossierPersoon(DossierPersoon dossierPersoon) {
    this.dossierPersoon = dossierPersoon;
  }

  @Override
  public Page81GeboorteBean1 getNewBean() {
    return new Page81GeboorteBean1();
  }

  @Override
  public Field newField(Field field, Property property) {
    super.newField(field, property);

    if (property.is(GEBOORTEDATUM, WOONPLAATS, NAT)) {
      getLayout().addBreak();
    }

    return field;
  }

  @Override
  public void reset() {

    try {
      ReflectionUtil.deepCopyBean(getDossierPersoon(), new DossierPersoon());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(VBT, NAT, VERSTREK, TOELICHTING)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }

  private String getNationaliteiten() {

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

  /**
   * Geeft waarde terug als ouders geheimhouding hebben.
   */
  private GEHEIM_TYPE getVerstrekkingsBeperkingOuders() {

    boolean isMoederGeheim = getDossierGeboorte().getMoeder().isVerstrekkingsbeperking();
    boolean isVaderGeheim = getDossierGeboorte().getVader().isVerstrekkingsbeperking();

    GEHEIM_TYPE type = GEHEIM_TYPE.ONBEKEND;

    if (isMoederGeheim && isVaderGeheim) {
      type = GEHEIM_TYPE.BEIDE;
    } else if (isMoederGeheim) {
      type = GEHEIM_TYPE.MOEDER;
    } else if (isVaderGeheim) {
      type = GEHEIM_TYPE.VADER_DUO_MOEDER;
    }

    return type;
  }

  private void update() {

    Page81GeboorteBean1 bean = new Page81GeboorteBean1();

    DossierPersoon p = getDossierPersoon();

    Anr anr = new Anr(p.getAnummer().getStringValue());

    if (anr.isCorrect()) {
      bean.setAnr(anr.getFormatAnummer());
    } else {
      bean.setAnr("Wordt later toegekend");
    }

    Bsn bsn = new Bsn(p.getBurgerServiceNummer().getStringValue());

    if (bsn.isCorrect()) {
      bean.setBsn(bsn.getFormatBsn());
    } else {
      bean.setBsn("Wordt later toegekend");
    }

    bean.setNaam(p.getGeslachtsnaam());
    bean.setVoorv(new FieldValue(p.getVoorvoegsel()));
    bean.setTitel(p.getTitel());
    bean.setVoorn(p.getVoornaam());
    bean.setNaamskeuze(p.getNaamskeuzeType().getOms());
    bean.setGeslacht(p.getGeslacht() != Geslacht.NIET_VAST_GESTELD ? p.getGeslacht() : null);
    bean.setGeboortedatum(p.getDatumGeboorte());
    bean.setGeboortetijd(new TimeFieldValue(astr(p.getTijdGeboorte().getFormatTime())));
    bean.setGeboorteland(p.getGeboorteland());
    bean.setGeboorteplaats(p.getGeboorteplaats().getDescription());
    bean.setWoonplaats(p.getWoonplaats().getDescription());
    bean.setVbt(p.getVerblijfstitelOmschrijving());
    bean.setNat(getNationaliteiten());
    bean.setVerstrek(p.isVerstrekkingsbeperking());
    bean.setToelichting(p.getToelichting());

    if (getVerstrekkingsBeperkingOuders() == GEHEIM_TYPE.ONBEKEND) {
      bean.setVerstrek(false);
    }

    setBean(bean);

    if (getVerstrekkingsBeperkingOuders() == GEHEIM_TYPE.ONBEKEND) {
      getField(VERSTREK).setReadOnly(true);
    }
  }

  public enum GEHEIM_TYPE {
    MOEDER,
    VADER_DUO_MOEDER,
    BEIDE,
    ONBEKEND
  }
}
