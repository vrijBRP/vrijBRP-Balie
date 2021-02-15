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

package nl.procura.gba.web.services.bs.riskanalysis;

import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;

public class RiskAnalysisRelatedCase {

  private long         numberOfPersons;
  private Zaak         zaak;
  private Adresformats address;
  private String       type;
  private FunctieAdres function;

  public RiskAnalysisRelatedCase(Zaak zaak) {
    this.zaak = zaak;
    if (isRelocation()) {
      type = getRelocation().getTypeVerhuizing().getOms();
      address = getRelocation().getNieuwAdres().getAdres();
      function = getRelocation().getNieuwAdres().getFunctieAdres();
      numberOfPersons = getRelocation().getNieuwAdres().getAantalPersonen();
    } else if (isRegistration()) {
      type = "Eerste inschrijving";
      address = getRegistration().getAddress();
      function = FunctieAdres.get(getRegistration().getAddressFunction());
      numberOfPersons = getRegistration().getNoOfPeople();
    }
  }

  public Zaak getZaak() {
    return zaak;
  }

  public String getZaakId() {
    return zaak.getZaakId();
  }

  public boolean isRelocation() {
    return zaak instanceof VerhuisAanvraag;
  }

  public boolean isRegistration() {
    if (zaak instanceof Dossier) {
      Dossier dossier = (Dossier) zaak;
      return dossier.getZaakDossier() instanceof DossierRegistration;
    }
    return false;
  }

  public VerhuisAanvraag getRelocation() {
    return (VerhuisAanvraag) zaak;
  }

  public DossierRegistration getRegistration() {
    return (DossierRegistration) ((Dossier) zaak).getZaakDossier();
  }

  public long getNumberOfPersons() {
    return numberOfPersons;
  }

  public String getDescr() {
    return type;
  }

  public Adresformats getAddress() {
    return address;
  }

  public FunctieAdres getFunction() {
    return function;
  }

  public DateTime getDatumIngang() {
    return getZaak().getDatumIngang();
  }

  public DateTime getDatumTijdInvoer() {
    return getZaak().getDatumTijdInvoer();
  }
}
