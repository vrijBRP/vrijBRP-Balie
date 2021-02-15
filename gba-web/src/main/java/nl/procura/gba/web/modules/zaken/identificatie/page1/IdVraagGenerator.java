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

package nl.procura.gba.web.modules.zaken.identificatie.page1;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.Cat2OuderExt;

public class IdVraagGenerator {

  public List<IDVraag> getVragen(BasePLExt pl) {

    try {

      List<IDVraag> rvragen = new ArrayList<>();

      String dGeb = pl.getPersoon().getGeboorte().getGeboortedatum().getDescr();
      String pGeb = pl.getPersoon().getGeboorte().getGeboorteplaats().getDescr();
      String lGeb = pl.getPersoon().getGeboorte().getGeboorteland().getDescr();
      String naam = pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();

      rvragen.add(new IDVraag("Wat is uw geboortedatum?", dGeb));
      rvragen.add(new IDVraag("Wat is uw naam?", naam));
      rvragen.add(new IDVraag("Wat is uw geboorteplaats?", pGeb));
      rvragen.add(new IDVraag("Wat is uw geboorteland?", lGeb));

      for (Cat2OuderExt w : pl.getOuders().getOuders()) {

        String odGeb = w.getGeboorte().getDatum();
        String opGeb = w.getGeboorte().getGeboorteplaats().getDescr();
        String olGeb = w.getGeboorte().getGeboorteland().getDescr();
        String onaam = w.getNaam().getNaamNaamgebruikEersteVoornaam();

        String gesl = "M".equalsIgnoreCase(w.getGeslacht().getVal()) ? "vader" : "moeder";

        rvragen.add(new IDVraag(MessageFormat.format("Wat is de geboortedatum van uw {0}?", gesl), odGeb));
        rvragen.add(new IDVraag(MessageFormat.format("Wat is de naam van uw {0}?", gesl), onaam));
        rvragen.add(new IDVraag(MessageFormat.format("Wat is de geboorteplaats van uw {0}?", gesl), opGeb));
        rvragen.add(new IDVraag(MessageFormat.format("Wat is de geboorteland van uw {0}?", gesl), olGeb));
      }

      String vb = pl.getVerblijfplaats().getAdres().getAdres();

      rvragen.add(new IDVraag("Wat is uw adres?", vb));

      Collections.shuffle(rvragen);

      return (rvragen.size() > 6) ? rvragen.subList(0, 6) : rvragen;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new ArrayList<>();
  }

  public class IDVraag {

    private String  antwoord = "";
    private String  vraag    = "";
    private boolean correct  = false;

    private IDVraag(String vraag, String antwoord) {

      setVraag(vraag);
      setAntwoord(antwoord);
    }

    public String getAntwoord() {
      return antwoord;
    }

    public void setAntwoord(String antwoord) {
      this.antwoord = antwoord;
    }

    public String getVraag() {
      return vraag;
    }

    public void setVraag(String vraag) {
      this.vraag = vraag;
    }

    public boolean isCorrect() {
      return correct;
    }

    public void setCorrect(boolean correct) {
      this.correct = correct;
    }
  }
}
