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

package nl.procura.gba.web.modules.zaken.rijbewijs.page4;

import static nl.procura.gba.web.modules.zaken.rijbewijs.page4.Page4RijbewijsBean1.*;
import static nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie.ZAAK_RIJBEWIJS_ONGELDIG;
import static nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie.ZAAK_RIJBEWIJS_ONTVANGST;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.services.Services;
import nl.procura.rdw.messages.P0252;
import nl.procura.rdw.processen.p0252.f08.NATPRYBMAATR;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public abstract class Page4RijbewijsSubpage1 extends GbaVerticalLayout {

  protected final Button buttonOntvangst = new Button("Ontvangst / doorsturen");
  protected final Button buttonOngeldig  = new Button("Ongeldigverklaren");

  public Page4RijbewijsSubpage1(P0252 p0252f1, P0252 p0252f2) {

    setSpacing(true);
    NATPRYBMAATR a = (NATPRYBMAATR) p0252f1.getResponse().getObject();
    Page4RijbewijsForm1 rijbewijsForm1 = new Page4RijbewijsForm1(a) {

      @Override
      protected void initForm() {

        setCaption("Persoonsgegevens");
        setOrder(NAAM, GEBOREN, CRBSLEUTEL, MAATREGEL, BSN, NATIONALITEIT, ANUMMER, TITEL, NAAMGEBRUIK,
            BURGSTAAT, PARTNER);
      }

    };

    buttonOntvangst.setWidth("100%");
    buttonOngeldig.setWidth("100%");

    if (p0252f2 != null) {
      setMargin(true, false, false, false);
      addComponent(new InfoLayout("Ter informatie", "Er zijn maatregelen aanwezig."));
    }

    OptieLayout ol = new OptieLayout(rijbewijsForm1, "200px");
    ol.getRight().setCaption("Opties");

    boolean isActie1 = Services.getInstance().getGebruiker().getProfielen().isProfielActie(
        ZAAK_RIJBEWIJS_ONTVANGST);
    boolean isActie2 = Services.getInstance().getGebruiker().getProfielen().isProfielActie(ZAAK_RIJBEWIJS_ONGELDIG);

    if (isActie1) {
      ol.getRight().addButton(buttonOntvangst, e -> onButtonOntvangst());
    }

    if (isActie2) {
      ol.getRight().addButton(buttonOngeldig, e -> onButtonOngeldig());
    }

    boolean isRemove = !(isActie1 || isActie2);
    ol.removeRightVisible(isRemove);

    Page4RijbewijsForm1 adresForm = new Page4RijbewijsForm1(a) {

      @Override
      protected void initForm() {
        setCaption("Adresgegevens");
        setOrder(ADRES);
      }
    };

    addComponent(ol);
    addComponent(adresForm);
  }

  public abstract void onButtonOngeldig();

  public abstract void onButtonOntvangst();
}
