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

package nl.procura.gba.web.modules.zaken.onderzoek.page1;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.ZaakFragment;
import nl.procura.gba.web.modules.zaken.common.ZaakMultiWindow;
import nl.procura.gba.web.modules.zaken.common.ZaakPersonChoiceWindow;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.onderzoek.enums.OnderzoekBronType;

public class OnderzoekMultiWindow extends ZaakMultiWindow {

  public OnderzoekMultiWindow() {
    super("Selecteer een zaak");
  }

  @Override
  protected void setKeuzes() {
    addKeuze(new KeuzeAdresonderzoek());
  }

  public class KeuzeAdresonderzoek extends FragmentKeuze {

    public KeuzeAdresonderzoek() {
      super("Adresonderzoek", ZaakFragment.FR_ONDERZOEK);
    }

    @Override
    public void buttonClick(ClickEvent event) {
      Dossier dossier = (Dossier) getGbaApplication().getServices().getOnderzoekService().getNewZaak();
      BasePLExt pl = getGbaApplication().getServices().getPersonenWsService().getHuidige();
      getGbaApplication().getParentWindow().addWindow(new PersonChoiceWindow(dossier, pl));
    }
  }

  public static class PersonChoiceWindow extends ZaakPersonChoiceWindow {

    public PersonChoiceWindow(Dossier dossier, BasePLExt pl) {
      super("Selecteer de rol van de persoon (Escape om te sluiten)", "400px");

      addChoice("Aangever / bron", () -> {
        DossierOnderzoek zaakDossier = (DossierOnderzoek) dossier.getZaakDossier();
        zaakDossier.setOnderzoekBron(OnderzoekBronType.BURGER);
        BsPersoonUtils.kopieDossierPersoon(pl, zaakDossier.getAangever());
        goToZaak(dossier);
      });

      addChoice("Betrokkene (te onderzoeken persoon)", () -> {
        DossierPersoon betrokkene = new DossierPersoon(DossierPersoonType.BETROKKENE);
        BsPersoonUtils.kopieDossierPersoon(pl, betrokkene);
        betrokkene.setDefinitief(true);
        dossier.toevoegenPersoon(betrokkene);
        goToZaak(dossier);
      });
    }
  }
}
