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

package nl.procura.gba.web.modules.zaken.huwelijk.page1;

import nl.procura.gba.web.modules.zaken.common.ZaakMultiWindow;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;

public class HuwelijkMultiWindow extends ZaakMultiWindow {

  public HuwelijkMultiWindow() {
    super("Selecteer een zaak");
  }

  @Override
  protected void setKeuzes() {

    if (getGbaApplication().isProfielActie(ProfielActie.SELECT_ZAAK_HUWELIJKGPS)) {
      addKeuze(new KeuzeSluiting());
    }

    if (getGbaApplication().isProfielActie(ProfielActie.SELECT_ZAAK_OMZETTING)) {
      addKeuze(new KeuzeOmzetting());
    }

    if (getGbaApplication().isProfielActie(ProfielActie.SELECT_ZAAK_ONTBINDING)) {
      addKeuze(new KeuzeOntbinding());
    }
  }

  public class KeuzeOmzetting extends FragmentKeuze {

    public KeuzeOmzetting() {
      super("Omzetting in gemeente", "bs.omzetting");
    }

    @Override
    public void buttonClick(ClickEvent event) {

      DossierOmzetting zaakDossier = new DossierOmzetting();

      BsPersoonUtils.kopieDossierPersoon(getGbaApplication().getServices().getPersonenWsService().getHuidige(),
          zaakDossier.getPartner1());

      getGbaApplication().getServices().getMemoryService().setObject(Dossier.class, zaakDossier.getDossier());

      super.buttonClick(event);
    }
  }

  public class KeuzeOntbinding extends FragmentKeuze {

    public KeuzeOntbinding() {
      super("Ontbinding/einde in gemeente", "bs.ontbinding");
    }

    @Override
    public void buttonClick(ClickEvent event) {

      DossierOntbinding zaakDossier = new DossierOntbinding();

      BsPersoonUtils.kopieDossierPersoon(getGbaApplication().getServices().getPersonenWsService().getHuidige(),
          zaakDossier.getPartner1());

      getGbaApplication().getServices().getMemoryService().setObject(Dossier.class, zaakDossier.getDossier());

      super.buttonClick(event);
    }
  }

  public class KeuzeSluiting extends FragmentKeuze {

    public KeuzeSluiting() {
      super("Sluiting in gemeente", "bs.huwelijk");
    }

    @Override
    public void buttonClick(ClickEvent event) {

      DossierHuwelijk zaakDossier = new DossierHuwelijk();

      BsPersoonUtils.kopieDossierPersoon(getGbaApplication().getServices().getPersonenWsService().getHuidige(),
          zaakDossier.getPartner1());

      getGbaApplication().getServices().getMemoryService().setObject(Dossier.class, zaakDossier.getDossier());

      super.buttonClick(event);
    }
  }
}
