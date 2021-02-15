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

package nl.procura.gba.web.modules.bs.common.pages.persooncontrole;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Component;

import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;

public class DossierPersoonSynchronizer {

  private static final String IDGEGEVENS             = "Idgegevens";
  private static final String NAAMGEGEVENS           = "Naam";
  private static final String ADRESGEGEVENS          = "Adres";
  private static final String GEBOORTEGEGEVENS       = "Geboorte";
  private static final String OVERLIJDENSGEGEVENS    = "Overlijden";
  private static final String CURATELE               = "Curatele";
  private static final String BURGERLIJKE_STAAT      = "Burgerlijke staat";
  private static final String NATIONALITEITEN        = "Nationaliteiten";
  private static final String VERBLIJFSTITEL         = "Verblijfstitel";
  private static final String VERSTREKKINGSBEPERKING = "Verstrekkingsbeperking";

  private final List<Element>  elementen = new ArrayList<>();
  private final DossierPersoon huidigePersoon;
  private final DossierPersoon nieuwePersoon;

  public DossierPersoonSynchronizer(DossierPersoon huidigePersoon, DossierPersoon nieuwePersoon) {

    this.huidigePersoon = huidigePersoon;
    this.nieuwePersoon = nieuwePersoon;

    // Naam
    elementen.add(new Element(false, "Burgerservicenummer", IDGEGEVENS, huidigePersoon.getBurgerServiceNummer(),
        nieuwePersoon.getBurgerServiceNummer()));
    elementen.add(new Element(false, "A-nummer", IDGEGEVENS, huidigePersoon.getAnummer(), nieuwePersoon.getAnummer()));
    elementen
        .add(new Element(false, "Aktenaam", NAAMGEGEVENS, huidigePersoon.getAktenaam(), nieuwePersoon.getAktenaam()));
    elementen.add(new Element(false, "Geslachtsnaam", NAAMGEGEVENS, huidigePersoon.getGeslachtsnaam(),
        nieuwePersoon.getGeslachtsnaam()));
    elementen.add(new Element(false, "Voorvoegsel", NAAMGEGEVENS, huidigePersoon.getVoorvoegsel(),
        nieuwePersoon.getVoorvoegsel()));
    elementen
        .add(new Element(false, "Titel-predikaat", NAAMGEGEVENS, huidigePersoon.getTitel(), nieuwePersoon.getTitel()));
    elementen
        .add(new Element(false, "Voornaam", NAAMGEGEVENS, huidigePersoon.getVoornaam(), nieuwePersoon.getVoornaam()));
    elementen
        .add(new Element(false, "Geslacht", NAAMGEGEVENS, huidigePersoon.getGeslacht(), nieuwePersoon.getGeslacht()));

    // Geboorteland
    Element geboorteland = new Element(false, "Geboorteland", GEBOORTEGEGEVENS, huidigePersoon.getGeboorteland(),
        nieuwePersoon.getGeboorteland());
    Element geboortelandAkte = new Element(true, "Geboorteland op akte", GEBOORTEGEGEVENS,
        huidigePersoon.getGeboortelandAkte(),
        nieuwePersoon.getGeboortelandAkte());
    if (geboorteland.isMatch()) {
      geboortelandAkte = new Element(true, "Geboorteland op akte", GEBOORTEGEGEVENS,
          huidigePersoon.getGeboortelandAkte(),
          huidigePersoon.getGeboortelandAkte());
    }

    // Geboorteplaats
    Element geboorteplaats = new Element(false, "Geboorteplaats", GEBOORTEGEGEVENS, huidigePersoon.getGeboorteplaats(),
        nieuwePersoon.getGeboorteplaats());
    Element geboorteplaatsAkte = new Element(true, "Geboorteplaats op akte", GEBOORTEGEGEVENS,
        huidigePersoon.getGeboorteplaatsAkte(), nieuwePersoon.getGeboorteplaatsAkte());

    if (geboorteplaats.isMatch()) {
      geboorteplaatsAkte = new Element(true, "Geboorteplaats op akte", GEBOORTEGEGEVENS,
          huidigePersoon.getGeboorteplaatsAkte(), huidigePersoon.getGeboorteplaatsAkte());
    }

    // Geboorte
    elementen.add(new Element(false, "Geboortedatum", NAAMGEGEVENS, huidigePersoon.getDatumGeboorte(),
        nieuwePersoon.getDatumGeboorte()));
    elementen.add(geboorteland);
    elementen.add(geboortelandAkte);
    elementen.add(geboorteplaats);
    elementen.add(geboorteplaatsAkte);

    // Overlijden
    elementen.add(new Element(false, "Overlijdensdatum", OVERLIJDENSGEGEVENS, huidigePersoon.getDatumOverlijden(),
        nieuwePersoon.getDatumOverlijden()));

    // adres
    elementen.add(new Element(false, "Straat", ADRESGEGEVENS, huidigePersoon.getStraat(), nieuwePersoon.getStraat()));
    elementen.add(
        new Element(false, "Huisnummer", ADRESGEGEVENS, huidigePersoon.getHuisnummer(), nieuwePersoon.getHuisnummer()));
    elementen.add(
        new Element(false, "Huisletter", ADRESGEGEVENS, huidigePersoon.getHuisnummerLetter(),
            nieuwePersoon.getHuisnummerLetter()));
    elementen.add(new Element(false, "Huisnummer toevoeging", ADRESGEGEVENS, huidigePersoon.getHuisnummerToev(),
        nieuwePersoon.getHuisnummerToev()));
    elementen.add(new Element(false, "Huisnummer aanduiding", ADRESGEGEVENS, huidigePersoon.getHuisnummerAand(),
        nieuwePersoon.getHuisnummerAand()));
    elementen
        .add(new Element(false, "Postcode", ADRESGEGEVENS, huidigePersoon.getPostcode(), nieuwePersoon.getPostcode()));

    // Woonland
    Element woonland = new Element(true, "Woonland", ADRESGEGEVENS, huidigePersoon.getLand(), nieuwePersoon.getLand());
    Element woonlandAkte = new Element(true, "Woonland op akte", ADRESGEGEVENS, huidigePersoon.getWoonlandAkte(),
        nieuwePersoon.getWoonlandAkte());

    if (woonland.isMatch()) {
      woonlandAkte = new Element(true, "Woonland op akte", ADRESGEGEVENS, huidigePersoon.getWoonlandAkte(),
          huidigePersoon.getWoonlandAkte());
    }

    // Woonplaats
    Element woonplaats = new Element(true, "Woonplaats", ADRESGEGEVENS, huidigePersoon.getWoonplaats(),
        nieuwePersoon.getWoonplaats());
    Element woonplaatsAkte = new Element(true, "Woonplaats op akte", ADRESGEGEVENS, huidigePersoon.getWoonplaatsAkte(),
        nieuwePersoon.getWoonplaatsAkte());

    if (woonland.isMatch()) {
      woonplaatsAkte = new Element(true, "Woonplaats op akte", ADRESGEGEVENS, huidigePersoon.getWoonplaatsAkte(),
          huidigePersoon.getWoonplaatsAkte());
    }

    elementen.add(woonland);
    elementen.add(woonlandAkte);
    elementen.add(woonplaats);
    elementen.add(woonplaatsAkte);
    elementen.add(new Element(false, "Gemeente", ADRESGEGEVENS, huidigePersoon.getWoongemeente().getDescription(),
        nieuwePersoon.getWoongemeente().getDescription()));

    elementen.add(new Element(false, BURGERLIJKE_STAAT, BURGERLIJKE_STAAT, huidigePersoon.getBurgerlijkeStaat(),
        nieuwePersoon.getBurgerlijkeStaat()));
    elementen.add(new Element(false, "Sinds", BURGERLIJKE_STAAT, huidigePersoon.getDatumBurgerlijkeStaat(),
        nieuwePersoon.getDatumBurgerlijkeStaat()));
    elementen.add(
        new Element(false, VERSTREKKINGSBEPERKING, VERSTREKKINGSBEPERKING, huidigePersoon.isVerstrekkingsbeperking(),
            nieuwePersoon.isVerstrekkingsbeperking()));
    elementen
        .add(new Element(false, CURATELE, CURATELE, huidigePersoon.isOnderCuratele(), nieuwePersoon.isOnderCuratele()));
    elementen.add(new Element(false, NATIONALITEITEN, NATIONALITEITEN, huidigePersoon.getNationaliteitenOmschrijving(),
        huidigePersoon.getNationaliteitenOmschrijving()));
    elementen.add(new Element(false, VERBLIJFSTITEL, VERBLIJFSTITEL, huidigePersoon.getVerblijfstitelOmschrijving(),
        nieuwePersoon.getVerblijfstitelOmschrijving()));
  }

  public List<Element> getElementen() {
    return elementen;
  }

  public DossierPersoon getHuidigePersoon() {
    return huidigePersoon;
  }

  public DossierPersoon getNieuwePersoon() {
    return nieuwePersoon;
  }

  public boolean isMatch() {
    for (Element element : elementen) {
      if (!element.isMatch()) {
        return false;
      }
    }
    return true;
  }

  public boolean isPersoonGevonden() {
    return nieuwePersoon != null && nieuwePersoon.getBurgerServiceNummer().isCorrect();
  }

  public class Element {

    private final String    naam;
    private final String    categorie;
    private final Object    huidigeWaarde;
    private final Object    nieuweWaarde;
    private final Component component;
    private boolean         updateable;

    public Element(boolean updateable, String naam, String categorie, Object huidigeWaarde, Object nieuweWaarde) {
      this(updateable, naam, categorie, huidigeWaarde, nieuweWaarde, null);
    }

    public Element(boolean updateable, String naam, String categorie, Object huidigeWaarde, Object nieuweWaarde,
        Component component) {
      super();
      this.updateable = updateable;
      this.naam = naam;
      this.categorie = categorie;
      this.huidigeWaarde = huidigeWaarde;
      this.nieuweWaarde = nieuweWaarde;
      this.component = component;
    }

    public String getCategorie() {
      return categorie;
    }

    public Component getComponent() {
      return component;
    }

    public Object getHuidigeWaarde() {
      return huidigeWaarde;
    }

    public String getNaam() {
      return naam;
    }

    public Object getNieuweWaarde() {
      return nieuweWaarde;
    }

    public boolean isMatch() {
      return astr(huidigeWaarde).equals(astr(nieuweWaarde));
    }

    public boolean isUpdateable() {
      return updateable;
    }

    public void setUpdateable(boolean updateable) {
      this.updateable = updateable;
    }
  }
}
