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

package nl.procura.gba.web.modules.verwijzing.page1;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gba.web.windows.home.HomeWindow;
import nl.procura.gba.web.windows.persoonslijst.PersoonslijstWindow;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.validation.Anummer;

public class Page1Verwijzing extends NormalPageTemplate {

  private final Button gbavButton    = new Button("Landelijke persoonslijst (F2)");
  private final Button archiefButton = new Button("Archief persoonlijst (F3)");

  public Page1Verwijzing() {

    super("Verwijsgegevens");

    InfoLayout info = new InfoLayout();
    info.setHeader("Ter informatie");
    info.setMessage(
        "Dit zijn gegevens die bij uitschrijving van deze persoon uit de gemeente zijn opgenomen. "
            + "In de archief persoonslijst zijn de gegevens te raadplegen zoals deze geldig waren vóór de uitschrijving. <hr/>"
            + "Klik op de link of toets F3 om de archief persoonslijst in te zien. ");

    addButton(buttonPrev);
    addButton(gbavButton);
    addButton(archiefButton);

    addComponent(info);

    addComponent(new Page1VerwijzingForm() {

      @Override
      public void setBean(Object bean) {

        BasePLExt pl = getPl();

        Page1VerwijzingBean b = new Page1VerwijzingBean();

        BasePLRec pc = pl.getLatestRec(GBACat.PERSOON);
        BasePLRec vc = pl.getLatestRec(GBACat.VERW);

        if (vc.hasElems()) {

          b.setBsn(get(vc, GBAElem.BSN));
          b.setAnr(get(vc, GBAElem.ANR));
          b.setGeslachtsnaam(get(vc, GBAElem.GESLACHTSNAAM));
          b.setVoorvoegsel(get(vc, GBAElem.VOORV_GESLACHTSNAAM));
          b.setVoornaam(get(vc, GBAElem.VOORNAMEN));

          if (pc.hasElems()) {
            b.setGeslacht(get(pc, GBAElem.GESLACHTSAAND));
          }

          b.setTitel(get(vc, GBAElem.TITEL_PREDIKAAT));
          b.setGeboren(pl.getVerwijzing().getGeboorte().getDatumTePlaatsLand());
          b.setVertrokken(get(vc, GBAElem.GEM_INSCHR) + " op " + get(vc,
              GBAElem.DATUM_INSCHR));
          b.setAdres(pl.getVerwijzing().getAdres().getAdres());
          b.setPostcode(pl.getVerwijzing().getAdres().getPostcode().getValue().getDescr());
          b.setLocatie(pl.getVerwijzing().getAdres().getLocatie().getValue().getDescr());
          b.setStatus(pl.getPersoon().getStatus().getOpsomming());

          if (!getServices().getPersonenWsService().isGbavZoeken()) {
            getButtonLayout().removeComponent(gbavButton);
          }
        }

        super.setBean(b);
      }
    });
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if ((button == gbavButton) || (keyCode == KeyCode.F2)) {
      getApplication().goToPl(getWindow(), "", PLEDatasource.GBAV, getPl());
    }

    if ((button == archiefButton) || (keyCode == KeyCode.F3)) {

      // Zoek de volledige PL met archief gegevens

      String anr = getPl().getPersoon().getAnr().getVal();

      if (Anummer.isCorrect(anr)) {

        PLEArgs a = new PLEArgs();
        a.setShowRemoved(true);
        a.setShowArchives(true);
        a.setShowHistory(true);
        a.setDatasource(PLEDatasource.PROCURA);
        a.addNummer(anr);

        PersonenWsService personenWs = getServices().getPersonenWsService();
        personenWs.getOpslag().clear();
        personenWs.getPersoonslijsten(a, true).getBasisPLWrappers();

        getApplication().openWindow(getWindow(), new PersoonslijstWindow(), "");
      } else {
        throw new ProException("A-nummer is niet correct");
      }
    }

    super.handleEvent(button, keyCode);
  }

  /**
   * Wat te doen als er op F1 wordt gedrukt.
   */
  @Override
  public void onPreviousPage() {

    // Terug naar HomeWindow
    getApplication().goBackToWindow(getWindow(), new HomeWindow());

    super.onPreviousPage();
  }

  private String get(BasePLRec r, GBAElem e) {
    return r.getElemVal(e).getDescr();
  }
}
