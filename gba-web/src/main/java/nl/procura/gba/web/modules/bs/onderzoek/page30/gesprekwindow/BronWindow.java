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

package nl.procura.gba.web.modules.bs.onderzoek.page30.gesprekwindow;

import static java.util.Optional.ofNullable;
import static nl.procura.gba.web.services.bs.onderzoek.enums.AanschrijvingBronType.GERELATEERDE;

import java.math.BigDecimal;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoekBron;
import nl.procura.gba.web.services.bs.onderzoek.enums.AanschrijvingBronType;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class BronWindow extends GbaModalWindow {

  private final DossierOnderzoek     zaakDossier;
  private final DossierOnderzoekBron bron;
  private VLayout                    contentLayout = new VLayout();

  private PersonLayout      personLayout;
  private RelativeLayout    relativeLayout;
  private StakeholderLayout stakeholderLayout;
  private MiscLayout        miscLayout;

  public BronWindow(DossierOnderzoek zaakDossier, DossierOnderzoekBron bron) {
    super("Externe bron (Escape om te sluiten)", "900px");
    setHeight("400px");

    this.zaakDossier = zaakDossier;
    this.bron = bron;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new Page()));
  }

  public class Page extends NormalPageTemplate {

    private BronForm    bronForm;
    private SummonsForm summonsForm;

    @Override
    public void event(PageEvent event) {

      if (event.isEvent(InitPage.class)) {

        buttonClose.setCaption("Annuleren (Esc)");
        addButton(buttonSave, 1F);
        addButton(buttonClose);

        personLayout = new PersonLayout(this, bron);
        relativeLayout = new RelativeLayout(bron);
        stakeholderLayout = new StakeholderLayout(this, bron);
        miscLayout = new MiscLayout(bron, () -> successMessage("De persoon is geselecteerd."));

        bronForm = new BronForm(bron);
        summonsForm = new SummonsForm(bron,
            zaakDossier.getBetrokkenen(),
            getApplication().getServices(),
            type -> {
              getLayout(type).reset();
              setType(type);
            },
            (betrokkene, relatie, relatieType) -> relativeLayout.setRelatie(betrokkene, relatie, relatieType));

        addComponent(bronForm);
        addComponent(summonsForm);
        addComponent(contentLayout);
        setType(summonsForm.getBean().getType());
      }

      super.event(event);
    }

    @Override
    public void onClose() {
      closeWindow();
    }

    @Override
    public void onSave() {
      bronForm.commit();
      summonsForm.commit();

      bron.setBron(bronForm.getBean().getBron());
      bron.setGesprek(bronForm.getBean().getInhoud());
      bron.setSummonsType(summonsForm.getBean().getType());

      if (GERELATEERDE == summonsForm.getBean().getType()) {
        bron.setInstBsnBetrok(toBsn(summonsForm.getBean().getBetrokkene()));
        bron.setInstBsnRel(toBsn(summonsForm.getBean().getGerelateerde()));
      } else {
        bron.setInstBsnBetrok(BigDecimal.valueOf(-1L));
        bron.setInstBsnRel(BigDecimal.valueOf(-1L));
      }

      bron.setDossier(zaakDossier);
      getLayout(summonsForm.getBean().getType()).save();

      zaakDossier.getBronnen().add(bron);
      getServices().getOnderzoekService().save(zaakDossier.getDossier());

      closeWindow();
      super.onSave();
    }

    private BigDecimal toBsn(Relatie gerelateerde) {
      return BigDecimal.valueOf(ofNullable(gerelateerde)
          .map(b -> b.getPl().getPersoon().getBsn().toLong())
          .orElse(-1L));
    }

    private BigDecimal toBsn(DossierPersoon gerelateerde) {
      return BigDecimal.valueOf(ofNullable(gerelateerde)
          .map(b -> b.getBurgerServiceNummer().getLongValue())
          .orElse(-1L));
    }

    private void setType(AanschrijvingBronType type) {
      contentLayout.removeAllComponents();
      contentLayout.addComponent(getLayout(type));
      getWindow().center();
    }

    private AbstractBronLayout getLayout(AanschrijvingBronType type) {
      if (type != null) {
        switch (type) {
          case PERSOON:
            return personLayout;
          case GERELATEERDE:
            return relativeLayout;
          case BELANGHEBBENDE:
            return stakeholderLayout;
          case OVERIGE:
          default:
            return miscLayout;
        }
      } else {
        return miscLayout;
      }
    }
  }
}
