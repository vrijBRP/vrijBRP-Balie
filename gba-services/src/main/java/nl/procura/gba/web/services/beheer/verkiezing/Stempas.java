/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.services.beheer.verkiezing;

import static java.lang.Long.parseLong;
import static nl.procura.gba.web.services.beheer.verkiezing.StempasAanduidingType.AAND_VERVANGEN;
import static nl.procura.standard.Globalfunctions.date2str;
import static nl.procura.standard.Globalfunctions.time2str;
import static nl.procura.standard.Globalfunctions.trim;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.jpa.personen.db.KiesrStem;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.standard.ProcuraDate;
import nl.procura.validation.Anr;

public class Stempas {

  private BasePLExt  stemgerechtigde;
  private BasePLExt  gemachtigde;
  private Verkiezing verkiezing;
  private KiesrStem  stem = new KiesrStem();

  public Stempas(Verkiezing verkiezing) {
    this.verkiezing = verkiezing;
    stem.setcKiesrVerk(verkiezing.getVerk().getcKiesrVerk());
    stem.setKiesrVerk(verkiezing.getVerk());
  }

  public Stempas(KiesrStem stem) {
    this.stem = stem;
    this.verkiezing = new Verkiezing(stem.getKiesrVerk());
  }

  @Override
  public String toString() {
    if (stem.isStored()) {
      StringBuilder sb = new StringBuilder(getPasnummer());
      if (getAanduidingType().getCode() > 0) {
        sb.append(" (");
        sb.append(getAanduidingType().getType().toLowerCase());
        sb.append(")");
      } else {
        sb.append(" (actueel)");
      }
      return sb.toString();
    } else {
      return "Geen stempas geregistreerd";
    }
  }

  public Long getVnrVervanging() {
    return stem.isStored() ? stem.getVnrVervanging() : -1L;
  }

  public boolean isOpgeslagen() {
    return stem.isStored();
  }

  public boolean isToegevoegd() {
    return stem.isStored() && stem.isIndToegevoegd();
  }

  public Long getVolgnr() {
    return stem.isStored() ? stem.getVnr() : 0L;
  }

  public Anr getAnr() {
    return stem.isStored() ? new Anr(stem.getAnr()) : new Anr();
  }

  public Anr getAnrGemachtigde() {
    return stem.isStored() ? new Anr(stem.getAnrVolmacht()) : new Anr();
  }

  public void setAnrGemachtigde(Anr anummer) {
    stem.setAnrVolmacht(anummer.isCorrect() ? anummer.getLongAnummer() : -1L);
  }

  public String getPasnummer() {
    return stem.isStored() ? stem.getPasNr() : "Geen stempas opgeslagen";
  }

  public StempasAanduidingType getAanduidingType() {
    return StempasAanduidingType.get(stem.isStored() ? stem.getAand() : -1L);
  }

  public String getAanduidingOmschrijving() {
    StringBuilder out = new StringBuilder(getAanduidingType().getOms());
    switch (getAanduidingType()) {
      case AAND_VERVANGEN:
        out.append(" (met volgnummer ").append(stem.getVnrVervanging()).append(")");
        break;
      case AAND_VOLMACHTBEWIJS:
        out.append(" (")
            .append(getGemachtigde().getPersoon().getNaam().getNaamNaamgebruikGeslachtsnaamVoornaamAanschrijf())
            .append(")");
        break;
      default:
        break;
    }
    if (stem.getdAand() > 0) {
      out.append(". Gewijzigd op ");
      out.append(getAanduidingTijdstip());
    }
    return out.toString();
  }

  public String getAanduidingTijdstip() {
    if (stem.getdAand() > 0) {
      return date2str(stem.getdAand()) + " / " + time2str(stem.gettAand().toString());
    } else {
      return "";
    }
  }

  public void setAanduiding(StempasAanduidingType aanduiding) {
    stem.setAand(aanduiding.getCode());
    if (getAanduidingType() == StempasAanduidingType.AAND_GEEN) {
      stem.setdAand(-1L);
      stem.settAand(-1L);
    } else {
      stem.setdAand(parseLong(new ProcuraDate().getSystemDate()));
      stem.settAand(parseLong(new ProcuraDate().getSystemTime()));
    }
  }

  public String getAdres() {
    return String
        .format("%s %d %s %s", stem.getStraat(), stem.getHnr(), stem.getHnrL(), stem.getHnrT())
        .replaceAll("\\s+", " ");
  }

  public boolean isVervangen() {
    return AAND_VERVANGEN == getAanduidingType();
  }

  public String getPostcodeWoonplaats() {
    return stem.getPc() + " " + stem.getWpl();
  }

  public String getPostcode() {
    return stem.getPc();
  }

  public String getNaam() {
    return trim(stem.getNaam() + ", " + stem.getVoorn());
  }

  public Geslacht getGeslacht() {
    return Geslacht.get(stem.getGeslacht());
  }

  public ProcuraDate getGeboortedatum() {
    return new ProcuraDate(stem.getdGeb().toString());
  }

  public KiesrStem getStem() {
    return stem;
  }

  public BasePLExt getStemgerechtigde() {
    return stemgerechtigde;
  }

  public void setStemgerechtigde(BasePLExt stemgerechtigde) {
    this.stemgerechtigde = stemgerechtigde;
  }

  public BasePLExt getGemachtigde() {
    return gemachtigde;
  }

  public void setGemachtigde(BasePLExt gemachtigde) {
    this.gemachtigde = gemachtigde;
  }

  public void setVerkiezing(Verkiezing verkiezing) {
    this.verkiezing = verkiezing;
  }

  public Verkiezing getVerkiezing() {
    return verkiezing;
  }
}
