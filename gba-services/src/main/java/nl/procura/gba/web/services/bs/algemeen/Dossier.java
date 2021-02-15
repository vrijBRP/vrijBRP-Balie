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

package nl.procura.gba.web.services.bs.algemeen;

import static ch.lambdaj.Lambda.*;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.EXPARTNER;
import static nl.procura.standard.Globalfunctions.*;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.Doss;
import nl.procura.gba.jpa.personen.db.DossAkte;
import nl.procura.gba.jpa.personen.db.Location;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.document.DossierDocument;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.functies.BsNatioHandler;
import nl.procura.gba.web.services.bs.algemeen.functies.BsNatioUtils;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersonenHandler;
import nl.procura.gba.web.services.bs.algemeen.functies.BsVereistenHandler;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteiten;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersonen;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoonFilter;
import nl.procura.gba.web.services.bs.algemeen.vereiste.DossierVereiste;
import nl.procura.gba.web.services.bs.algemeen.vereiste.DossierVereisten;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.gba.web.services.zaken.algemeen.ZaakHistorie;
import nl.procura.gba.web.services.zaken.algemeen.ZaakMetPagina;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.CommentaarZaak;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.ZaakCommentaren;
import nl.procura.gba.web.services.zaken.algemeen.contact.ContactZaak;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.identiteit.Identificatie;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class Dossier extends Doss
    implements DossierPersonen, DossierNationaliteiten, DossierVereisten, CommentaarZaak, ContactZaak, ZaakMetPagina {

  private static final long        serialVersionUID         = -6734389603805895982L;
  private final List<DossierAkte>  aktes                    = new ArrayList<>();
  private final BsZaak             zaak                     = new BsZaak();
  private final BsPersonenHandler  bsDossierPersonen        = new BsPersonenHandler();
  private final BsNatioHandler     bsDossierNationaliteiten = new BsNatioHandler();
  private final BsVereistenHandler bsDossierVereisten       = new BsVereistenHandler();
  private List<DossierDocument>    documenten               = new ArrayList<>();

  public Dossier() {
  }

  public Dossier(ZaakType zaakType) {
    this();
    setZaakType(zaakType);
    setStatus(ZaakStatusType.INCOMPLEET);
  }

  public Dossier(ZaakType zaakType, ZaakDossier zaakDossier) {
    this(zaakType);
    setZaakDossier(zaakDossier);
  }

  public DossierAkte addAkte(DossierAkte akte) {
    getAktes().add(akte);
    return akte;
  }

  // Personen management

  @Override
  public void addVereiste(String id, String dossierVereiste, String naam, String overruleReason, String voldoet,
      boolean overruled) {

    DossierVereiste vereiste = new DossierVereiste();
    vereiste.setIdKey(id);
    vereiste.setDossierVereiste(dossierVereiste);
    vereiste.setNaam(naam);
    vereiste.setToelichting(overruleReason);
    vereiste.setVoldaan(voldoet);
    vereiste.setOverruled(overruled);

    bsDossierVereisten.toevoegenVereiste(this, vereiste);
  }

  public List<DossierAkte> getAktes() {
    Collections.sort(aktes);
    return aktes;
  }

  @Override
  public List<DossierPersoon> getAllePersonen() {
    return bsDossierPersonen.getAllePersonen();
  }

  @Override
  public AnrFieldValue getAnummer() {
    return getZaakDossier().getAnummer();
  }

  @Override
  public void setAnummer(AnrFieldValue anummer) {
    getZaakDossier().setAnummer(anummer);
  }

  @Override
  public BasePLExt getBasisPersoon() {
    return zaak.getBasisPersoon();
  }

  @Override
  public void setBasisPersoon(BasePLExt basisPersoon) {
    zaak.setBasisPersoon(basisPersoon);
  }

  @Override
  public BsnFieldValue getBurgerServiceNummer() {
    return getZaakDossier().getBurgerServiceNummer();
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue burgerServiceNummer) {
    getZaakDossier().setBurgerServiceNummer(burgerServiceNummer);
  }

  public Long getCode() {
    return getCDoss();
  }

  @Override
  public String getCommentaar() {
    return getComment();
  }

  @Override
  public void setCommentaar(String commentaar) {
    setComment(commentaar);
  }

  @Override
  public ZaakCommentaren getCommentaren() {
    return zaak.getCommentaren(this);
  }

  @Override
  public ZaakContact getContact() {
    return zaak.getContact();
  }

  // Nationaliteiten management

  @Override
  public void setContact(ZaakContact email) {
    zaak.setContact(email);
  }

  @Override
  public DateTime getDatumIngang() {
    return new DateTime(getDIn());
  }

  @Override
  public void setDatumIngang(DateTime date) {
    setDIn(toBigDecimal(date.getLongDate()));
  }

  @Override
  public DateTime getDatumTijdInvoer() {
    return new DateTime(getDAanvr(), getTAanvr());
  }

  @Override
  public void setDatumTijdInvoer(DateTime dt) {

    setDAanvr(toBigDecimal(dt.getLongDate()));
    setTAanvr(toBigDecimal(dt.getLongTime()));
  }

  public List<DossierDocument> getDocumenten() {
    return documenten;
  }

  // Vereisten managemenent

  public void setDocumenten(List<DossierDocument> documenten) {
    this.documenten = documenten;
  }

  @Override
  public List<DossierPersoon> getExPartners() {
    return getPersonen(EXPARTNER);
  }

  @Override
  public Gemeente getGemeente() {
    return zaak.getGemeente();
  }

  @Override
  public void setGemeente(Gemeente gemeente) {
    zaak.setGemeente(gemeente);
  }

  @Override
  public Identificatie getIdentificatie() {
    return zaak.getIdentificatie();
  }

  @Override
  public void setIdentificatie(Identificatie identificatie) {
    zaak.setIdentificatie(identificatie);
  }

  @Override
  public UsrFieldValue getIngevoerdDoor() {
    return new UsrFieldValue(getUsr().getCUsr(), getUsr().getUsrfullname());
  }

  @Override
  public void setIngevoerdDoor(UsrFieldValue ingevoerdDoor) {
    setUsr(new Usr(along(ingevoerdDoor.getValue()), ingevoerdDoor.getDescription()));
  }

  @Override
  public List<DossierPersoon> getKinderen() {
    return getPersonen(DossierPersoonType.KIND);
  }

  @Override
  public String getKinderenSamenvatting() {
    return getPersonenSamenvatting(DossierPersoonType.KIND);
  }

  @Override
  public Locatie getLocatieInvoer() {
    return zaak.getLocatieInvoer(getLocation());
  }

  @Override
  public void setLocatieInvoer(Locatie locatieInvoer) {
    zaak.setLocatieInvoer(locatieInvoer);
    setLocation(ReflectionUtil.deepCopyBean(Location.class, locatieInvoer));
  }

  @Override
  public DossierNationaliteit getNationaliteit() {
    return BsNatioUtils.getNationaliteit(this);
  }

  @Override
  public List<DossierNationaliteit> getNationaliteiten() {
    return bsDossierNationaliteiten.getNationaliteiten();
  }

  @Override
  public String getNationaliteitenOmschrijving() {
    return BsNatioUtils.getNationaliteitOmschrijving(this);
  }

  @Override
  public List<DossierPersoon> getPersonen() {
    return bsDossierPersonen.getPersonen();
  }

  @Override
  public List<DossierPersoon> getPersonen(DossierPersoonType... types) {
    return bsDossierPersonen.getPersonen(types);
  }

  @Override
  public String getPersonenSamenvatting(DossierPersoonType... types) {
    return bsDossierPersonen.getSamenvatting(types);
  }

  @Override
  public DossierPersoon getPersoon(DossierPersoon persoon) {
    return bsDossierPersonen.getPersoon(persoon);
  }

  @Override
  public DossierPersoon getPersoon(DossierPersoonFilter filter) {
    return bsDossierPersonen.getPersoon(filter);
  }

  @Override
  public String getSoort() {
    return getDescr();
  }

  @Override
  public ZaakStatusType getStatus() {
    return ZaakStatusType.get(aval(getIndVerwerkt()));
  }

  @Override
  public void setStatus(ZaakStatusType status) {
    setIndVerwerkt(toBigDecimal(status.getCode()));
  }

  // Algemene methodes

  @Override
  public ZaakType getType() {
    return ZaakType.get(aval(getTypeDoss()));
  }

  @Override
  public List<DossierVereiste> getVereisten() {
    return bsDossierVereisten.getVereisten();
  }

  public ZaakDossier getZaakDossier() {
    return zaak.getZaakDossier();
  }

  public void setZaakDossier(ZaakDossier zaakDossier) {
    zaakDossier.setDossier(this);
    zaak.setZaakDossier(zaakDossier);
  }

  @Override
  public ZaakHistorie getZaakHistorie() {
    return zaak.getZaakHistorie();
  }

  @Override
  public String getZaakId() {
    return fil(super.getDossiernummer()) ? super.getDossiernummer() : super.getZaakId();
  }

  @Override
  public void setZaakId(String zaakId) {
    super.setDossiernummer(zaakId);
    super.setZaakId(zaakId);
  }

  @Override
  public boolean heeftPersoon(DossierPersoon persoon) {
    return bsDossierPersonen.heeftPersoon(persoon);
  }

  /**
   * Heeft aktes en alle aktes zijn correct
   */

  public boolean isAktesCorrect() {
    return !aktes.isEmpty() && aktes.stream().allMatch(DossierAkte::isCorrect);
  }

  @Override
  public boolean isNationaliteit(DossierNationaliteit nationaliteit) {
    return bsDossierNationaliteiten.isNationaliteit(nationaliteit);
  }

  @Override
  public boolean isPersoon(DossierPersoon persoon) {
    return bsDossierPersonen.isPersoon(persoon);
  }

  public boolean isStatus(ZaakStatusType... statussen) {
    return exists(statussen, having(on(ZaakStatusType.class), equalTo(getStatus())));
  }

  public void setZaakType(ZaakType type) {
    setTypeDoss(toBigDecimal(type.getCode()));
  }

  @Override
  public DossierNationaliteit toevoegenNationaliteit(DossierNationaliteit nationaliteit) {
    return bsDossierNationaliteiten.toevoegenNationaliteit(this, nationaliteit);
  }

  @Override
  public List<DossierPersoon> toevoegenPersonen(List<DossierPersoon> personen) {
    return bsDossierPersonen.addPersonen(this, personen);
  }

  @Override
  public DossierPersoon toevoegenPersoon(DossierPersoon persoon) {
    return bsDossierPersonen.addPersoon(this, persoon);
  }

  @Override
  public DossierPersoon toevoegenPersoon(DossierPersoonType type) {
    return toevoegenPersoon(new DossierPersoon(type));
  }

  public void verwijderAkte(DossierAkte akte) {
    for (DossierPersoon persoon : akte.getPersonen()) {
      persoon.ontkoppelAkte(akte);
    }

    getDossAktes().remove(ReflectionUtil.deepCopyBean(DossAkte.class, akte));
    getAktes().remove(akte);
  }

  @Override
  public void verwijderNationaliteit(DossierNationaliteit nationaliteit) {
    bsDossierNationaliteiten.verwijderNationaliteit(this, nationaliteit);
  }

  @Override
  public void verwijderPersoon(DossierPersoon persoon) {
    bsDossierPersonen.verwijderPersoon(this, persoon);

    // Verwijder ook de personen van de aktes
    for (DossierAkte akte : getAktes()) {
      akte.ontkoppelPersoon(persoon);
      persoon.ontkoppelAkte(akte);
    }
  }
}
