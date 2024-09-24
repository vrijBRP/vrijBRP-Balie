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

package nl.procura.gba.web.modules.zaken.rijbewijs.page15;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.math.BigInteger;
import java.util.ArrayList;

import nl.procura.gba.web.modules.zaken.rijbewijs.RijbewijsPage;
import nl.procura.gba.web.modules.zaken.rijbewijs.page15.OngeldigVerklaring.Categorie;
import nl.procura.gba.web.modules.zaken.rijbewijs.page4.Page4Rijbewijs;
import nl.procura.rdw.messages.P0177;
import nl.procura.rdw.messages.P0252;
import nl.procura.rdw.processen.p0177.f01.ONGELDCATGEG;
import nl.procura.rdw.processen.p0177.f01.RYBONDERHOUD;
import nl.procura.rdw.processen.p0252.f08.NATPRYBMAATR;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Ongeldigverklaren rijbewijs
 */
public class Page15Rijbewijs extends RijbewijsPage {

  private final P0252 p0252f1;
  private final P0252 p0252f2;

  private Page15RijbewijsForm1 form1;
  private Page15RijbewijsForm2 form2;

  public Page15Rijbewijs(P0252 p0252f1, P0252 p0252f2) {

    super("Ongeldigverklaren rijbewijs");
    this.p0252f1 = p0252f1;
    this.p0252f2 = p0252f2;

    buttonSave.setCaption("Registeren (F9)");
    setMargin(true);
    addButton(buttonPrev);
    addButton(buttonSave);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form1 = new Page15RijbewijsForm1(getRijbewijsNummer());
      form2 = new Page15RijbewijsForm2(p0252f1, p0252f2);

      addComponent(form1);
      addComponent(form2);
    }

    super.event(event);
  }

  private void onPreviousPage(boolean naSuccesRegistratie) {

    P0252 newP0252 = new P0252();
    newP0252.newF1(getPl().getPersoon().getBsn().getVal());

    if (sendMessage(newP0252)) {
      getNavigation().removePage(Page15Rijbewijs.class);
      getNavigation().removePage(Page4Rijbewijs.class);
      getNavigation().goToPage(new Page4Rijbewijs(newP0252, naSuccesRegistratie));
    }
  }

  @Override
  public void onPreviousPage() {
    onPreviousPage(false);
  }

  private BigInteger getRijbewijsNummer() {
    NATPRYBMAATR a = (NATPRYBMAATR) p0252f1.getResponse().getObject();
    return a.getUitgrybtab()
        .getUitgrybgeg()
        .stream()
        .findFirst()
        .map(c -> c.getRybgeg().getRybnr())
        .orElse(BigInteger.valueOf(-1));
  }

  @Override
  public void onSave() {

    form1.commit();
    form2.commit();

    OngeldigVerklaring ongeldigVerklaring = new OngeldigVerklaring();
    ongeldigVerklaring.setRijbewijsNummer(getRijbewijsNummer());

    form1.fill(ongeldigVerklaring);
    form2.fill(ongeldigVerklaring);

    if (ongeldigVerklaring.getCategorieen().stream().noneMatch(Categorie::isDatumOngeldig)) {
      throw new ProException(WARNING, "Er is geen enkele categorie gevuld");
    }

    OngeldigVerklaringDialog ongeldigVerklaringDialog = new OngeldigVerklaringDialog(ongeldigVerklaring) {

      @Override
      public void onSend(OngeldigVerklaring ov) {
        if (stuur0177f1(ov)) {
          onPreviousPage(true);
        }
      }
    };

    getWindow().addWindow(ongeldigVerklaringDialog);

    super.onSave();
  }

  /**
   * Registeren ongeldig verklaring
   */
  private boolean stuur0177f1(OngeldigVerklaring ov) {
    P0177 p0177 = new P0177();
    RYBONDERHOUD p0177f1 = p0177.newF1(ov.getRijbewijsNummer());

    // Ongeldigverklaring
    if (ov.getDatumInlevering().getLongValue() > 0) { // Datum inlevering is optioneel
      p0177f1.getOngverklgeg().setInlevdatov(BigInteger.valueOf(ov.getDatumInlevering().getLongValue()));
    }

    p0177f1.getOngverklgeg().setOngeldcodeov(ov.getRedenOngeldig().getCode());

    // Ongeldige categorieën
    ArrayList<ONGELDCATGEG> categorieen = new ArrayList<>();
    for (Categorie c : ov.getCategorieen()) {
      if (c.getDatum().getLongValue() > 0) {
        ONGELDCATGEG cat = new ONGELDCATGEG();
        cat.setRybcatov(c.getCode());
        cat.setOngelddatcat(BigInteger.valueOf(c.getDatum().getLongValue()));
        categorieen.add(cat);
      }
    }

    p0177f1.getOngeldcattab().setOngeldcatgeg(categorieen);

    return sendMessage(p0177);
  }
}
