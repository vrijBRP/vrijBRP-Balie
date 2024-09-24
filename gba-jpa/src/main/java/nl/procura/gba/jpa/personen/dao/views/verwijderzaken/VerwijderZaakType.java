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

package nl.procura.gba.jpa.personen.dao.views.verwijderzaken;

import static nl.procura.gba.common.ZaakType.OVERLIJDEN_IN_BUITENLAND;
import static nl.procura.gba.common.ZaakType.PL_MUTATION;
import static nl.procura.gba.common.ZaakType.REGISTRATION;
import static nl.procura.gba.common.ZaakType.RISK_ANALYSIS;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.BaseEntity;
import nl.procura.gba.jpa.personen.db.BvhPark;
import nl.procura.gba.jpa.personen.db.Correspondentie;
import nl.procura.gba.jpa.personen.db.DocInh;
import nl.procura.gba.jpa.personen.db.Doss;
import nl.procura.gba.jpa.personen.db.Geheimhouding;
import nl.procura.gba.jpa.personen.db.Gpk;
import nl.procura.gba.jpa.personen.db.Gv;
import nl.procura.gba.jpa.personen.db.Inbox;
import nl.procura.gba.jpa.personen.db.Indicatie;
import nl.procura.gba.jpa.personen.db.Naamgebruik;
import nl.procura.gba.jpa.personen.db.Nrd;
import nl.procura.gba.jpa.personen.db.PlMut;
import nl.procura.gba.jpa.personen.db.Rdm01;
import nl.procura.gba.jpa.personen.db.Terugmelding;
import nl.procura.gba.jpa.personen.db.UittAanvr;
import nl.procura.gba.jpa.personen.db.VogAanvr;

import lombok.Getter;

@Getter
public enum VerwijderZaakType {

  UITTREKSEL("uittreksel", "Uittreksels", UittAanvr.class, ZaakType.UITTREKSEL),
  FORMULIER("formulier", "Formulieren", UittAanvr.class, ZaakType.UITTREKSEL),
  VERSTREKKINGSBEPERKING("verstrekkingsbeperking", "Verstrekkingsbeperkingen", Geheimhouding.class,
      ZaakType.VERSTREKKINGSBEPERKING),
  NAAMGEBRUIK("naamgebruik", "Naamgebruik wijzigingen", Naamgebruik.class, ZaakType.NAAMGEBRUIK),
  BINNENVERHUIZING("binnenverhuizing", "Binnenverhuizingen", BvhPark.class, ZaakType.VERHUIZING),
  BUITENVERHUIZING("buitenverhuizing", "Intergemeentelijke verhuizingen", BvhPark.class, ZaakType.VERHUIZING),
  EMIGRATIE("emigratie", "Emigraties", BvhPark.class, ZaakType.VERHUIZING),
  HERVESTIGING("hervestiging", "Hervestigingen", BvhPark.class, ZaakType.VERHUIZING),
  VOG("vog", "VOG aanvragen", VogAanvr.class, ZaakType.COVOG),
  GPK("gpk", "Gehandicapte parkeerkaarten", Gpk.class, ZaakType.GPK),
  REISDOCUMENT("reisdocument", "Reisdocumentaanvragen", Rdm01.class, ZaakType.REISDOCUMENT),
  INHOUDING_VERMISSING("inhouding_vermissing", "Inhoudingen/vermissingen", DocInh.class, ZaakType.INHOUD_VERMIS),
  RIJBEWIJS("rijbewijs", "Rijbewijzen", Nrd.class, ZaakType.RIJBEWIJS),
  TERUGMELDING("terugmelding", "Terugmeldingen", Terugmelding.class, ZaakType.TERUGMELDING),
  GEBOORTE("geboorte", "Geboortes", Doss.class, ZaakType.GEBOORTE),
  ERKENNING("erkenning", "Erkenningen", Doss.class, ZaakType.ERKENNING),
  HUW_GPS("huw_gps", "Huwelijken/GPS", Doss.class, ZaakType.HUWELIJK_GPS_GEMEENTE),
  NAAMSKEUZE("naamskeuze", "Naamskeuze", Doss.class, ZaakType.NAAMSKEUZE),
  OVERLIJDEN_GEMEENTE("overlijden_in_gemeente", "Overlijden in gemeente", Doss.class, ZaakType.OVERLIJDEN_IN_GEMEENTE),
  LIJKVINDING("lijkvinding", "Lijkvindingen", Doss.class, ZaakType.LIJKVINDING),
  OVERLIJDEN_BUITENLAND("overlijden_in_buitenland", "Overlijdens buitenland", Doss.class, OVERLIJDEN_IN_BUITENLAND),
  LEVENLOOS("levenloos_geboren_kind", "Levenloos geboren kind", Doss.class, ZaakType.LEVENLOOS),
  INDICATIE("indicatie", "Indicaties", Indicatie.class, ZaakType.INDICATIE),
  CORRESPONDENTIE("correspondentie", "Correspondenties", Correspondentie.class, ZaakType.CORRESPONDENTIE),
  GEGEVENSVERSTREKKING("gegevensverstrekking", "Gegevensverstrekkingen", Gv.class, ZaakType.GEGEVENSVERSTREKKING),
  OMZETTING_GPS("omzetting_gps", "Omzettingen GPS", Doss.class, ZaakType.OMZETTING_GPS),
  ONTBINDING_HUW_GPS("ontbinding_huwelijk_gps", "Ontbindingen huwelijk/GPS", Doss.class, ZaakType.ONTBINDING_GEMEENTE),
  INBOX("inbox", "Inbox zaken", Inbox.class, ZaakType.INBOX),
  ONDERZOEK("onderzoek", "Onderzoeken", Doss.class, ZaakType.ONDERZOEK),
  NATURALISATIE("naturalisatie", "Naturalisaties", Doss.class, ZaakType.NATURALISATIE),
  EERSTE_INSCHRIJVING("eerste_inschrijving", "Eerste inschrijvingen", Doss.class, REGISTRATION),
  RISICOANALYSE("risicoanalyse", "Risicoanalyses", Doss.class, RISK_ANALYSIS),
  PL_MUTATIE("pl_mutatie", "PL Mutaties", PlMut.class, PL_MUTATION);

  private final String                      id;
  private final String                      omschrijving;
  private final Class<? extends BaseEntity> entity;
  private final ZaakType                    zaakType;

  VerwijderZaakType(String id, String omschrijving, Class<? extends BaseEntity> entity, ZaakType zaakType) {
    this.id = id;
    this.omschrijving = omschrijving;
    this.entity = entity;
    this.zaakType = zaakType;
  }
}
