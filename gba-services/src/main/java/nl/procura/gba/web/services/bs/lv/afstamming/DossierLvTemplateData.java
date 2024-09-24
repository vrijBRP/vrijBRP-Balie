/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.services.bs.lv.afstamming;

import static nl.procura.gba.web.common.tables.GbaTables.PLAATS;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.EnumWithCode;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.gba.web.services.zaken.documenten.printen.DocumentTemplateData;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DossierLvTemplateData extends DocumentTemplateData {

  public DossierLvTemplateData(DossierLv dossier) {
    put("zaak", new ZaakData(dossier));
    put("kind", dossier.getKind());
    put("ouders", dossier.getOuders());
    put("adoptiefouders", dossier.getAdoptiefouders());
    put("soort", toFieldValue(dossier.getSoort()));
    put("datumLv", new DateTime(dossier.getDatumLv()));
    put("geboorteakte", new GeboorteAktegegevens(dossier));
    put("brondocument", new Brondocument(dossier));
  }

  private static class ZaakData extends DocumentTemplateData {

    public ZaakData(DossierLv d) {
      put("zaakId", d.getDossier().getZaakId());
      put("datumTijdInvoer", d.getDossier().getDatumTijdInvoer());
      put("ingevoerdDoor", d.getDossier().getIngevoerdDoor());
      put("locatieInvoer", d.getDossier().getLocatieInvoer());
    }
  }

  private static class GeboorteAktegegevens extends DocumentTemplateData {

    public GeboorteAktegegevens(DossierLv dossier) {
      put("akte", dossier.getAkte());
      put("huidigeBRPakte", dossier.getHuidigBrpAkte());
      put("nieuweBRPakte", dossier.getNieuweBrpAkte());
      put("gemeente", PLAATS.get(dossier.getAkteGem()));
      put("jaar", astr(dossier.getAkteJaar()));
    }
  }

  private static class Brondocument extends DocumentTemplateData {

    public Brondocument(DossierLv d) {
      put("ontvangen", new OntvangenDocumenten(d));
      put("inhoud", new InhoudDocumenten(d));
    }
  }

  private static class OntvangenDocumenten extends DocumentTemplateData {

    public OntvangenDocumenten(DossierLv d) {
      put("uitspraak", d.getUitspraak());
      put("datumUitspraak", new DateTime(d.getDatumUitspraak()));
      put("datumGewijsde", new DateTime(d.getDatumGewijsde()));
      put("soortVerbintenis", toFieldValue(LvSoortVerbintenisType.get(d.getSoortVerbintenis())));
      put("document", d.getDoc());
      put("nummer", d.getDocNr());
      put("datum", new DateTime(d.getDocDatum()));
      put("plaats", d.getDocPlaats());
      put("door", toFieldValue(LvDocumentDoorType.get(d.getDocDoor().intValue())));
      put("tweedeDocument", toFieldValue(d.getTweedeDoc()));
      put("tweedeDocumentOms", d.getTweedeDocOms());
      put("tweedeDocumentDatum", new DateTime(d.getTweedeDocDatum()));
      put("tweedeDocumentPlaats", d.getTweedeDocPlaats());
    }
  }

  private static class InhoudDocumenten extends DocumentTemplateData {

    public InhoudDocumenten(DossierLv d) {

      String[] fields = LvField.getForm2(d.getSoort());
      String geslachtsnaamOuderLabel = "";
      String geslachtsnaamLabel = "";
      String voornamenLabel = "";
      String betreftOuderLabel = "";
      for (String field : fields) {
        switch (LvField.get(field)) {
          case GESLN_OUDER_GW:
            geslachtsnaamOuderLabel = "Geslachtsnaam ouder gewijzigd in";
            break;
          case GESLN_OUDER_VG:
            geslachtsnaamOuderLabel = "Geslachtsnaam ouder vastgesteld als";
            break;
          case GESLNM_GW:
            geslachtsnaamLabel = "Geslachtsnaam gewijzigd in";
            break;
          case GESLN_IS:
            geslachtsnaamLabel = "Geslachtsnaam is";
            break;
          case GESLN_VA:
            geslachtsnaamLabel = "Geslachtsnaam vastgesteld als";
            break;
          case GESLNM_KIND_GW:
            geslachtsnaamLabel = "Geslachtsnaam kind gewijzigd in";
            break;
          case VOORNAMEN_GW_IN:
            voornamenLabel = "Voornamen gewijzigd in";
            break;
          case VOORNAMEN_VA:
            voornamenLabel = "Voornamen vastgesteld als";
            break;
          case OUDERSCHAP_ONTKEND:
            betreftOuderLabel = "Ouderschap ontkend van";
            break;
          case OUDERSCHAP_VASTGESTELD:
            betreftOuderLabel = "Ouderschap vastgesteld van";
            break;
          case ERKENNING_DOOR:
            betreftOuderLabel = "Erkenning gedaan door";
            break;
          case FAMRECHT:
            betreftOuderLabel = "Familierechtelijke betrekking met deze ouder blijft in stand";
            break;
        }
      }

      put("betreftOuder", toFieldValue(LvOuderType.get(d.getBetreftOuder())));
      put("betreftOuderPersoon", d.getBetreftOuderPersoon());
      put("betreftOuderLabel", betreftOuderLabel);

      put("geslachtsnaamOuder", d.getGeslOuder());
      put("geslachtsnaamOuderLabel", geslachtsnaamOuderLabel);
      put("voornamenOuder", d.getVoornOuder());

      put("keuzeGeslachtsnaam", toFieldValue(KeuzeVaststellingGeslachtsnaam.get(d.getKeuzeGesl())));
      put("geslachtsnaam", d.getGesl());
      put("geslachtsnaamLabel", geslachtsnaamLabel);

      put("voornamenGewijzigd", fil(d.getVoorn()) ? "Ja" : "Nee");
      put("voornamen", d.getVoorn());
      put("voornamenLabel", voornamenLabel);

      put("toestemming", d.getToestemming());
      put("toegepastRecht", getToegepastRecht(d));
      put("gezag", LvGezagType.get(d.getGezag()));
      put("geslacht", Geslacht.get(d.getGeslAand()));
      put("gekozenRecht", d.getGekozenRecht());
      put("dagVanWijziging", new DateTime(d.getDatumWijziging()));
      put("verbeteringen", d.getVerbeteringen());
    }
  }

  private static String getToegepastRecht(DossierLv zaakDossier) {
    LvToegepastRechtType type = LvToegepastRechtType.get(zaakDossier.getToegepastRecht().longValue());
    if (type == LvToegepastRechtType.ONBEKEND) {
      return GbaTables.LAND.get(zaakDossier.getToegepastRecht()).getDescription();
    }
    return type.getOms();
  }

  private static FieldValue toFieldValue(EnumWithCode<?> value) {
    if (value != null) {
      return new FieldValue(value.getCode(), value.toString());
    }
    return new FieldValue();
  }

  private static FieldValue toFieldValue(Boolean value) {
    if (value != null) {
      return new FieldValue(value, value ? "Ja" : "Nee");
    }
    return new FieldValue();
  }
}
