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

package nl.procura.gba.web.services.zaken.reisdocumenten;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.toBigDecimal;
import static nl.procura.standard.Globalfunctions.trim;
import static org.hamcrest.Matchers.equalTo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.BaseEntity;
import nl.procura.gba.jpa.personen.db.Location;
import nl.procura.gba.jpa.personen.db.Rdm01;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.gba.web.services.zaken.algemeen.GenericZaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakAfhaalbaar;
import nl.procura.gba.web.services.zaken.algemeen.ZaakHistorie;
import nl.procura.gba.web.services.zaken.algemeen.contact.ContactZaak;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.identiteit.Identificatie;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingBasisregister;
import nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType;
import nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.Bezorging;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.Clausules;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.VermeldTitelType;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class ReisdocumentAanvraag extends Rdm01
    implements ContactZaak, ZaakAfhaalbaar, ToestemmingbaarReisdocument, IdentificatieUitreikingZaak {

  private static final long serialVersionUID = -3468735287544148942L;

  private final GenericZaak                    zaak               = new GenericZaak();
  private       Clausules                      clausules          = new Clausules(this);
  private       Contactgegevens                contactgegevens    = new Contactgegevens(this);
  private       ReisdocumentStatus             reisdocumentStatus = new ReisdocumentStatus(this);
  private       Toestemmingen                  toestemmingen      = new Toestemmingen(this);
  private       List<DocumentInhouding>        inhoudingen        = new ArrayList<>();
  private       List<Reisdocument>             documentHistorie   = new ArrayList<>();
  private       Bezorging                      thuisbezorging     = new Bezorging();
  private       Identificatie                  identificatieBijUitreiking;
  private       DocumentInhoudingBasisregister basisregister;

  public ReisdocumentAanvraag() {
    setUsr1(new Usr(BaseEntity.DEFAULT));
    setUsr2(new Usr(BaseEntity.DEFAULT));
    setStatus(ZaakStatusType.OPGENOMEN);
  }

  @Override
  public BigDecimal getAantalToestemmingen() {
    return toBigDecimal(getToestAantal());
  }

  public void setAantalToestemmingen(BigDecimal aantal) {
    setToestAantal(aantal);
  }

  public Aanvraagnummer getAanvraagnummer() {
    return new Aanvraagnummer(getAanvrNr());
  }

  public Bezorging getThuisbezorging() {
    return thuisbezorging;
  }

  public void setThuisbezorging(Bezorging thuisbezorging) {
    this.thuisbezorging = thuisbezorging;
  }

  public void setAanvraagnummer(Aanvraagnummer nr) {
    setAanvrNr(nr.getNummer());
  }

  @Override
  public AnrFieldValue getAnummer() {
    return new AnrFieldValue(getAnr());
  }

  @Override
  public void setAnummer(AnrFieldValue anr) {
    setAnr(FieldValue.from(anr).getStringValue());
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
    return new BsnFieldValue(astr(getBsn()));
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue bsn) {
    setBsn(FieldValue.from(bsn).getBigDecimalValue());
  }

  public Clausules getClausules() {
    return clausules;
  }

  public void setClausules(Clausules clausules) {
    this.clausules = clausules;
  }

  public int getCodeRaas() {
    return aval(getCRaas());
  }

  public void setCodeRaas(int codeRaas) {
    setCRaas(toBigDecimal(codeRaas));
  }

  @Override
  public ZaakContact getContact() {
    return zaak.getContact();
  }

  @Override
  public void setContact(ZaakContact contact) {
    zaak.setContact(contact);
  }

  public Contactgegevens getContactgegevens() {
    return contactgegevens;
  }

  public void setContactgegevens(Contactgegevens contactgegevens) {
    this.contactgegevens = contactgegevens;
  }

  public DateTime getDatumEindeGeldigheid() {
    return new DateTime(getDGeldEnd());
  }

  public void setDatumEindeGeldigheid(DateTime dt) {
    setDGeldEnd(toBigDecimal(dt == null ? -1 : dt.getLongDate()));
  }

  public DateTime getDatumEindeGeldigheidVb() {
    return new DateTime(getDVbDoc());
  }

  public void setDatumEindeGeldigheidVb(DateTime dt) {
    setDVbDoc(toBigDecimal(dt.getLongDate()));
  }

  @Override
  public DateTime getDatumIngang() {
    return new DateTime(getDatumTijdInvoer().getLongDate());
  }

  @Override
  public void setDatumIngang(DateTime dt) {
    setDAanvr(toBigDecimal(dt.getLongDate()));
  }

  @Override
  public DateTime getDatumTijdInvoer() {
    return new DateTime(along(getDAanvr()), along(getTAanvr()));
  }

  @Override
  public void setDatumTijdInvoer(DateTime dt) {

    setDAanvr(toBigDecimal(dt.getLongDate()));
    setTAanvr(toBigDecimal(dt.getLongTime()));
  }

  public DateTime getDatumVerstrek() {
    return new DateTime(getDVerstrek());
  }

  public List<Reisdocument> getDocumentHistorie() {
    return documentHistorie;
  }

  public void setDocumentHistorie(List<Reisdocument> documentHistorie) {
    this.documentHistorie = documentHistorie;
  }

  public UsrFieldValue getGebruikerAanvraag() {
    return getIngevoerdDoor();
  }

  public void setGebruikerAanvraag(UsrFieldValue gebruikerAanvraag) {
    setIngevoerdDoor(gebruikerAanvraag);
  }

  public UsrFieldValue getGebruikerUitgifte() {
    return new UsrFieldValue(getUsr2().getCUsr(), getUsr2().getUsrfullname());
  }

  public void setGebruikerUitgifte(UsrFieldValue gebruikerUitgifte) {
    Usr usr = new Usr();
    usr.setCUsr(along(gebruikerUitgifte.getValue()));
    usr.setUsrfullname(gebruikerUitgifte.getDescription());
    setUsr2(usr);
  }

  public String getGeldigheid() {

    if (pos(getDGeldEnd())) {
      return "tot " + getDatumEindeGeldigheid().getFormatDate();
    } else if (pos(getGeldigheidsTermijn())) {
      return getGeldigheidsTermijn() + " jaar";
    } else {
      return "";
    }
  }

  public BigDecimal getGeldigheidsTermijn() {
    return getTermijnGeld();
  }

  public void setGeldigheidsTermijn(BigDecimal termijn) {
    setTermijnGeld(termijn);
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
    return new UsrFieldValue(getUsr1().getCUsr(), getUsr1().getUsrfullname());
  }

  @Override
  public void setIngevoerdDoor(UsrFieldValue ingevoerdDoor) {
    Usr usr = new Usr();
    usr.setCUsr(along(ingevoerdDoor.getValue()));
    usr.setUsrfullname(ingevoerdDoor.getDescription());
    setUsr1(usr);
  }

  public List<DocumentInhouding> getInhoudingen() {
    return inhoudingen;
  }

  public void setInhoudingen(List<DocumentInhouding> inhoudingen) {
    this.inhoudingen = inhoudingen;
  }

  @Override
  public Locatie getLocatieAfhaal() {
    return zaak.getLocatieAfhaal(getAfhaalLocation());
  }

  @Override
  public void setLocatieAfhaal(Locatie locatieAfhaal) {
    zaak.setLocatieAfhaal(locatieAfhaal);
    setAfhaalLocation(ReflectionUtil.deepCopyBean(Location.class, locatieAfhaal));
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

  public String getNrVbDocument() {
    return getNrVbDoc();
  }

  public void setNrVbDocument(String value) {
    setNrVbDoc(value);
  }

  // Algemene methodes

  public DocumentPL getPersoon() {
    return zaak.getPersoon();
  }

  public String getRedenAfwezig() {
    return getSNietAanw();
  }

  public void setRedenAfwezig(String value) {
    setSNietAanw(value);
  }

  public ReisdocumentStatus getReisdocumentStatus() {
    return reisdocumentStatus;
  }

  public void setReisdocumentStatus(ReisdocumentStatus reisdocumentStatus) {
    this.reisdocumentStatus = reisdocumentStatus;
  }

  public ReisdocumentType getReisdocumentType() {
    return ReisdocumentType.get(getZkargNlDoc());
  }

  public void setReisdocumentType(ReisdocumentType type) {
    setZkargNlDoc(type.getCode());
  }

  public String getSluitingStatus() {

    StringBuilder sb = new StringBuilder();

    switch (getReisdocumentStatus().getStatusLevering()) {

      case DOCUMENT_GOED:
        sb.append("document goed");
        break;

      case DOCUMENT_NIET_GELEVERD:
        sb.append("document is niet geleverd");
        break;

      case DOCUMENT_NIET_GOED:
        sb.append("document is niet goed");
        break;

      case DOCUMENT_VERDWENEN:
        sb.append("document is verdwenen");
        break;

      case LEVERING_NIET_BEKEND:
        return "levering is onbekend";

      case ONBEKEND:
        return "Nog niet bekend";

      default:
        break;
    }

    switch (getReisdocumentStatus().getStatusAfsluiting()) {
      case AANVRAAG_NIET_AFGESLOTEN:
        sb.append(", de aanvraag is niet afgesloten");
        break;

      case DOCUMENT_NIET_OPGEHAALD:
        return "Document is niet opgehaald";

      case DOCUMENT_NIET_UITGEREIKT_ONJUIST:
        return "Document is niet uitgereikt";

      case DOCUMENT_NIET_UITGEREIKT_OVERIGE_REDEN:
        return "Document is niet uitgereikt (overige)";

      case DOCUMENT_UITGEREIKT:
        return "Document is uitgereikt";

      case DOCUMENT_UITGEREIKT_DOOR_ANDERE_INSTANTIE:
        return "Document is uitgereikt (andere inst.)";

      default:
        break;
    }

    return trim(sb.toString());
  }

  @Override
  public String getSoort() {
    return getReisdocumentType().getOms();
  }

  public SpoedType getSpoed() {
    return SpoedType.get(aval(getIndSpoed()));
  }

  public void setSpoed(SpoedType spoed) {
    setIndSpoed(toBigDecimal(spoed.getCode()));
  }

  @Override
  public ZaakStatusType getStatus() {
    return ZaakStatusType.get(along(getIndVerwerkt()));
  }

  @Override
  public void setStatus(ZaakStatusType status) {
    setIndVerwerkt(toBigDecimal(status.getCode()));
  }

  public Toestemmingen getToestemmingen() {
    return toestemmingen;
  }

  public void setToestemmingen(Toestemmingen toestemmingen) {
    this.toestemmingen = toestemmingen;
  }

  @Override
  public ZaakType getType() {
    return ZaakType.REISDOCUMENT;
  }

  public List<DocumentInhouding> getVermissingen() {
    return select(getInhoudingen(),
        having(on(DocumentInhouding.class).getInhoudingType(), equalTo(InhoudingType.VERMISSING)));
  }

  @Override
  public ZaakHistorie getZaakHistorie() {
    return zaak.getZaakHistorie();
  }

  public boolean isGratis() {
    return pos(getIndGratis());
  }

  public SignaleringStatusType getSignalering() {
    return SignaleringStatusType.get(getIndSignal().intValue());
  }

  public void setSignalering(SignaleringStatusType type) {
    setIndSignal(BigDecimal.valueOf(type.getCode()));
  }

  public void setGratis(boolean gratis) {
    setIndGratis(toBigDecimal(gratis ? 1 : 0));
  }

  public boolean isVermeldingLand() {
    return isVermeldLand();
  }

  public void setVermeldingLand(boolean vermeldingLand) {
    setVermeldLand(vermeldingLand);
  }

  public VermeldTitelType getVermeldingTitel() {
    return VermeldTitelType.get(getVermeldTp().intValue());
  }

  public void setVermeldingTitel(VermeldTitelType type) {
    setVermeldTp(toBigDecimal(type.getCode()));
  }

  public boolean isThuisbezorgingGewenst() {
    if (thuisbezorging != null && thuisbezorging.getMelding() != null) {
      return thuisbezorging.getMelding().isBezorgingGewenst();
    }
    return false;
  }

  public DocumentInhoudingBasisregister getBasisregister() {
    return basisregister;
  }

  public void setBasisregister(DocumentInhoudingBasisregister basisregister) {
    this.basisregister = basisregister;
  }

  @Override
  public Identificatie getIdentificatieBijUitreiking() {
    return this.identificatieBijUitreiking;
  }

  @Override
  public void setIdentificatieBijUitreiking(Identificatie identificatie) {
    this.identificatieBijUitreiking = identificatie;
  }
}
