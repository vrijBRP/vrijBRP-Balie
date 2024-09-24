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

package nl.procura.gba.web.services.zaken.algemeen.samenvatting;

import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.bs.levenloos.DossierLevenloos;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijden;
import nl.procura.gba.web.services.bs.overlijden.gemeente.DossierOverlijdenGemeente;
import nl.procura.gba.web.services.bs.overlijden.lijkvinding.DossierLijkvinding;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakAfhaalbaar;
import nl.procura.gba.web.services.zaken.algemeen.ZaakStatus;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekening;
import nl.procura.gba.web.services.zaken.algemeen.goedkeuring.GoedkeuringZaak;
import nl.procura.gba.web.services.zaken.correspondentie.CorrespondentieZaak;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;
import nl.procura.gba.web.services.zaken.geheim.GeheimAanvraag;
import nl.procura.gba.web.services.zaken.gpk.GpkAanvraag;
import nl.procura.gba.web.services.zaken.gv.GvAanvraag;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.naamgebruik.NaamgebruikAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

import lombok.Data;

/**
 * Een samenvatting van de zaak t.b.v. een samenvattingsdocument
 */
@Data
public class ZaakSamenvatting {

  private final List<Status>  statussen         = new ArrayList<>();
  private final Aantekeningen aantekeningen     = new Aantekeningen();
  private String              zaaktype          = "";
  private String              status            = "";
  private String              soort             = "";
  private String              zaakId            = "";
  private String              gebruikerInvoer   = "";
  private String              tijdstipInvoer    = "";
  private String              datumIngang       = "";
  private String              bron              = "";
  private String              leverancier       = "";
  private String              afhaalLocatie     = "";
  private String              datumEindeTermijn = "";
  private String              goedkeuring       = "";
  private String              identificatie     = "";
  private String              aangever          = "";
  private String              aangeverAdres     = "";
  private ZaakItemRubrieken   zaakItemRubrieken = new ZaakItemRubrieken();
  private Deelzaken           deelzaken         = new Deelzaken();
  private boolean             zaakSpecifiek     = false;

  public ZaakSamenvatting(Zaak zaak) {

    String soort = (fil(zaak.getSoort()) ? " (" + zaak.getSoort() + ")" : "");
    String gebruikerInvoer = (fil(
        zaak.getIngevoerdDoor().getDescription())
            ? (" door " + zaak.getIngevoerdDoor().getDescription() + ", " + zaak.getLocatieInvoer().getLocatie())
            : "");

    String tijdstipInvoer = zaak.getDatumTijdInvoer().toString();
    String datumIngang = pos(
        zaak.getDatumIngang().getLongDate()) ? zaak.getDatumIngang().toString() : "Onbekende datum";

    setZaaktype(zaak.getType().getOms() + soort);
    setStatus(zaak.getStatus().getOms());
    setGebruikerInvoer(gebruikerInvoer);
    setTijdstipInvoer(tijdstipInvoer);
    setDatumIngang(datumIngang);

    setBron(zaak.getBron());
    setLeverancier(zaak.getLeverancier());
    setZaakId(ZaakUtils.getRelevantZaakId(zaak));

    setAangever(getAangever(zaak));
    setAangeverAdres(getAangeverAdres(zaak));
    setIdentificatie(zaak.getIdentificatie() != null
        ? zaak.getIdentificatie().getOmschrijving()
        : "Niet vastgesteld");

    setGoedkeuring("N.v.t");
    if (zaak instanceof GoedkeuringZaak) {
      setGoedkeuring(((GoedkeuringZaak) zaak).getGoedkeuringsType().getOms());
    }

    // Zet de afhaallocatie
    setAfhaalLocatie("N.v.t.");
    if (zaak instanceof ZaakAfhaalbaar) {
      Locatie locatie = ((ZaakAfhaalbaar) zaak).getLocatieAfhaal();
      if (locatie != null && pos(locatie.getCLocation())) {
        setAfhaalLocatie(locatie.getLocatie());
      }
    }

    setDatumEindeTermijn("N.v.t.");
    setAantekeningen(zaak);
    setStatussen(zaak);

    if (zaak instanceof VerhuisAanvraag) {
      new VerhuizingSamenvatting(this, (VerhuisAanvraag) zaak);
    } else if (zaak instanceof DocumentZaak) {
      new DocumentSamenvatting(this, (DocumentZaak) zaak);
    } else if (zaak instanceof GeheimAanvraag) {
      new GeheimSamenvatting(this, (GeheimAanvraag) zaak);
    } else if (zaak instanceof NaamgebruikAanvraag) {
      new NaamgebruikSamenvatting(this, (NaamgebruikAanvraag) zaak);
    } else if (zaak instanceof VogAanvraag) {
      new VogSamenvatting(this, (VogAanvraag) zaak);
    } else if (zaak instanceof GpkAanvraag) {
      new GpkSamenvatting(this, (GpkAanvraag) zaak);
    } else if (zaak instanceof TerugmeldingAanvraag) {
      new TmvSamenvatting(this, (TerugmeldingAanvraag) zaak);
    } else if (zaak instanceof RijbewijsAanvraag) {
      new RdwSamenvatting(this, (RijbewijsAanvraag) zaak);
    } else if (zaak instanceof ReisdocumentAanvraag) {
      new ReisdocumentSamenvatting(this, (ReisdocumentAanvraag) zaak);
    } else if (zaak instanceof GvAanvraag) {
      new GvSamenvatting(this, (GvAanvraag) zaak);
    } else if (zaak instanceof CorrespondentieZaak) {
      new CorrespondentieSamenvatting(this, (CorrespondentieZaak) zaak);
    } else if (zaak instanceof DocumentInhouding) {
      new InhoudingSamenvatting(this, (DocumentInhouding) zaak);
    } else if (zaak instanceof Dossier) {
      Dossier dossier = (Dossier) zaak;
      if (dossier.getZaakDossier() instanceof DossierOmzetting) {
        new OmzettingSamenvatting(this, (DossierOmzetting) dossier.getZaakDossier());
      } else if (dossier.getZaakDossier() instanceof DossierOntbinding) {
        new OntbindingSamenvatting(this, (DossierOntbinding) dossier.getZaakDossier());
      } else if (dossier.getZaakDossier() instanceof DossierHuwelijk) {
        new HuwelijkSamenvatting(this, (DossierHuwelijk) dossier.getZaakDossier());
      } else if (dossier.getZaakDossier() instanceof DossierLevenloos) {
        new LevenloosSamenvatting(this, (DossierLevenloos) dossier.getZaakDossier());
      } else if (dossier.getZaakDossier() instanceof DossierGeboorte) {
        new GeboorteSamenvatting(this, (DossierGeboorte) dossier.getZaakDossier());
      } else if (dossier.getZaakDossier() instanceof DossierErkenning) {
        DossierErkenning erkenning = (DossierErkenning) dossier.getZaakDossier();
        new ErkenningSamenvatting(this, erkenning, erkenning.getErkenningsTypeOmschrijving());
      } else if (dossier.getZaakDossier() instanceof DossierOverlijdenGemeente) {
        new OverlijdenGemeenteSamenvatting(this, (DossierOverlijdenGemeente) dossier.getZaakDossier());
      } else if (dossier.getZaakDossier() instanceof DossierLijkvinding) {
        new LijkvindingSamenvatting(this, (DossierLijkvinding) dossier.getZaakDossier());
      } else if (dossier.getZaakDossier() instanceof DossierRegistration) {
        new RegistrationSamenvatting(this, (DossierRegistration) dossier.getZaakDossier());
      } else if (dossier.getZaakDossier() instanceof DossierOnderzoek) {
        new OnderzoekSamenvatting(this, (DossierOnderzoek) dossier.getZaakDossier());
      }
    }
  }

  private String getAangever(Zaak zaak) {

    BsnFieldValue bsn = zaak.getBurgerServiceNummer();
    AnrFieldValue anr = zaak.getAnummer();

    String out = "";
    if (bsn != null && fil(bsn.getDescription())) {
      out = bsn.getDescription();
    } else if (anr != null && fil(anr.getDescription())) {
      out = anr.getDescription();
    }

    if (zaak.getBasisPersoon() != null && zaak.getBasisPersoon().getCat(
        GBACat.PERSOON).hasSets()) {
      String naam = zaak.getBasisPersoon().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
      out = naam + " (" + out + ")";
    }

    // Aangever overlijden kan leeg zijn
    if (emp(out)) {
      if (zaak instanceof Dossier) {
        Dossier dossier = (Dossier) zaak;
        if (dossier.getZaakDossier() instanceof DossierOverlijden) {
          DossierOverlijden zaakDossier = (DossierOverlijden) dossier.getZaakDossier();
          out = (zaakDossier.getAangever().getNaam().getNaam_naamgebruik_eerste_voornaam());
        }
      }
    }

    return fil(out) ? out : "Onbekend";
  }

  private String getAangeverAdres(Zaak zaak) {
    String out = "";
    if (zaak.getBasisPersoon() != null && zaak.getBasisPersoon().getCat(GBACat.VB).hasSets()) {
      out = zaak.getBasisPersoon().getVerblijfplaats().getAdres().getAdresPcWpl();
    }
    return fil(out) ? out : "Onbekend";
  }

  private void setAantekeningen(Zaak zaak) {

    List<PlAantekening> plAantekeningen = zaak.getZaakHistorie().getAantekeningHistorie().getAantekeningen();
    for (PlAantekening aantekening : plAantekeningen) {
      Aantekening a = new Aantekening();
      a.setOnderwerp(aantekening.getLaatsteHistorie().getOnderwerp());
      a.setAantekening(aantekening.getLaatsteHistorie().getInhoud());
      a.setGebruikerInvoer(aantekening.getLaatsteHistorie().getGebruiker().getDescription());
      a.setTijdstipInvoer(aantekening.getLaatsteHistorie().getTijdstip().toString());
      this.aantekeningen.getItems().add(a);
    }
  }

  private void setStatussen(Zaak zaak) {

    List<ZaakStatus> historieStatussen = zaak.getZaakHistorie().getStatusHistorie().getStatussen();
    for (ZaakStatus zaakStatus : historieStatussen) {
      Status s = new Status();
      s.setStatus(zaakStatus.getStatus().getOms());
      s.setGebruikerInvoer(zaakStatus.getIngevoerdDoor().getDescription());
      s.setTijdstipInvoer(zaakStatus.getDatumTijdInvoer().toString());
      s.setOpmerking(zaakStatus.getOpmerking());
      this.statussen.add(s);
    }

  }

  @Data
  public static class Aantekening {

    private String onderwerp       = "";
    private String aantekening     = "";
    private String gebruikerInvoer = "";
    private String tijdstipInvoer  = "";
  }

  @Data
  public static class Aantekeningen {

    private List<Aantekening> items = new ArrayList<>();

    public boolean isEmpty() {
      return items.isEmpty();
    }
  }

  @Data
  public static class Deelzaak {

    private int            nr       = 0;
    private String         volgorde = "";
    private List<ZaakItem> items    = new ArrayList<>();

    public void add(String naam, Object waarde) {
      items.add(new ZaakItem(items.size(), naam, waarde));
    }

    public boolean isEerste() {
      return nr == 0;
    }
  }

  @Data
  public static class Deelzaken {

    private String         naam      = "";
    private List<Deelzaak> deelzaken = new ArrayList<>();

    public Deelzaken() {
    }

    public Deelzaken(String naam) {
      this.naam = naam;
    }

    public Deelzaak add(Deelzaak deelzaak) {
      deelzaak.setNr(deelzaken.size());
      deelzaken.add(deelzaak);
      return deelzaak;
    }

    public void add(Deelzaken nieuweDeelzaken) {
      deelzaken.addAll(nieuweDeelzaken.getDeelzaken());
    }

    public List<Deelzaak> getDeelzaken() {

      int nr = 0;
      for (Deelzaak dz : deelzaken) {
        nr++;
        dz.setVolgorde(nr + " / " + deelzaken.size());
      }

      return deelzaken;
    }
  }

  @Data
  public static class Status {

    private String status          = "";
    private String tijdstipInvoer  = "";
    private String gebruikerInvoer = "";
    private String opmerking       = "";
  }

  @Data
  public static class ZaakItem {

    private int    nr;
    private String naam;
    private Object waarde;

    public ZaakItem(int nr, String naam, Object waarde) {
      super();
      this.nr = nr;
      this.naam = naam;
      this.waarde = waarde;
    }

    public boolean isEerste() {
      return nr == 0;
    }
  }

  @Data
  public static class ZaakItemRubriek {

    private String         naam;
    private List<ZaakItem> items = new ArrayList<>();

    public ZaakItemRubriek(String naam) {
      this.naam = naam;
    }

    public void add(String naam, Object waarde) {
      items.add(new ZaakItem(items.size(), naam, waarde));
    }

    public void add(String naam, String waarde, Object... parameters) {
      items.add(new ZaakItem(items.size(), naam, MessageFormat.format(waarde, parameters)));
    }
  }

  @Data
  public static class ZaakItemRubrieken {

    private List<ZaakItemRubriek> rubrieken = new ArrayList<>();

    public ZaakItemRubriek add(String naam) {
      ZaakItemRubriek rubriek = new ZaakItemRubriek(naam);
      rubrieken.add(rubriek);
      return rubriek;
    }

    public void add(ZaakItemRubrieken nieuweRubrieken) {
      rubrieken.addAll(nieuweRubrieken.getRubrieken());
    }
  }
}
