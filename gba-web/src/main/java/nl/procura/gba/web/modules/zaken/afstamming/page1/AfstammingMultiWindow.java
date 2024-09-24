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

package nl.procura.gba.web.modules.zaken.afstamming.page1;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.ZaakFragment;
import nl.procura.gba.web.modules.zaken.common.ZaakMultiWindow;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.lv.afstamming.DossierLv;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.services.gba.functies.Geslacht;

public class AfstammingMultiWindow extends ZaakMultiWindow {

  public AfstammingMultiWindow() {
    super("Selecteer een zaak");
  }

  @Override
  protected void setKeuzes() {

    addKeuze(new KeuzeGeboorte());
    addKeuze(new KeuzeErkenning());
    addKeuze(new KeuzeNaamskeuze());
    addKeuze(new KeuzeLv());
  }

  public class KeuzeErkenning extends FragmentKeuze {

    public KeuzeErkenning() {
      super("Erkenning", ZaakFragment.FR_ERKENNING);
    }

    @Override
    public void buttonClick(ClickEvent event) {

      Dossier dossier = (Dossier) getGbaApplication().getServices().getErkenningService().getNewZaak();
      DossierErkenning zaakDossier = (DossierErkenning) dossier.getZaakDossier();

      BasePLExt pl = getGbaApplication().getServices().getPersonenWsService().getHuidige();
      if (Geslacht.VROUW.getAfkorting().equalsIgnoreCase(pl.getPersoon().getGeslacht().getCode())) {
        BsPersoonUtils.kopieDossierPersoon(pl, zaakDossier.getMoeder());
      }

      getGbaApplication().getServices().getMemoryService().setObject(Dossier.class, zaakDossier.getDossier());
      super.buttonClick(event);
    }
  }

  public class KeuzeNaamskeuze extends FragmentKeuze {

    public KeuzeNaamskeuze() {
      super("Naamskeuze", ZaakFragment.FR_NAAMSKEUZE);
    }

    @Override
    public void buttonClick(ClickEvent event) {

      Dossier dossier = (Dossier) getGbaApplication().getServices().getNaamskeuzeService().getNewZaak();
      DossierNaamskeuze zaakDossier = (DossierNaamskeuze) dossier.getZaakDossier();

      BasePLExt pl = getGbaApplication().getServices().getPersonenWsService().getHuidige();
      if (Geslacht.VROUW.getAfkorting().equalsIgnoreCase(pl.getPersoon().getGeslacht().getCode())) {
        BsPersoonUtils.kopieDossierPersoon(pl, zaakDossier.getMoeder());
      }

      getGbaApplication().getServices().getMemoryService().setObject(Dossier.class, zaakDossier.getDossier());
      super.buttonClick(event);
    }
  }

  public class KeuzeGeboorte extends FragmentKeuze {

    public KeuzeGeboorte() {
      super("Geboorte", ZaakFragment.FR_GEBOORTE);
    }

    @Override
    public void buttonClick(ClickEvent event) {

      Dossier dossier = (Dossier) getGbaApplication().getServices().getGeboorteService().getNewZaak();
      DossierGeboorte zaakDossier = (DossierGeboorte) dossier.getZaakDossier();

      BsPersoonUtils.kopieDossierPersoon(getGbaApplication().getServices().getPersonenWsService().getHuidige(),
          zaakDossier.getAangever());
      getGbaApplication().getServices().getMemoryService().setObject(Dossier.class, zaakDossier.getDossier());
      super.buttonClick(event);
    }
  }

  public class KeuzeLv extends FragmentKeuze {

    public KeuzeLv() {
      super("Latere vermelding", ZaakFragment.FR_LV);
    }

    @Override
    public void buttonClick(ClickEvent event) {

      Dossier dossier = (Dossier) getGbaApplication().getServices().getLvService().getNewZaak();
      DossierLv zaakDossier = (DossierLv) dossier.getZaakDossier();

      BasePLExt pl = getGbaApplication().getServices().getPersonenWsService().getHuidige();
      BsPersoonUtils.kopieDossierPersoon(pl, zaakDossier.getKind());

      getGbaApplication().getServices().getMemoryService().setObject(Dossier.class, zaakDossier.getDossier());
      super.buttonClick(event);
    }
  }
}
