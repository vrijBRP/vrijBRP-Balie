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

package nl.procura.gba.web.modules.zaken.rijbewijs.page14;

import static nl.procura.gba.web.modules.zaken.rijbewijs.page14.RijbewijsOntvangstDialog.*;

import java.math.BigInteger;
import java.util.Iterator;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.modules.zaken.rijbewijs.RijbewijsPage;
import nl.procura.gba.web.modules.zaken.rijbewijs.page4.Page4Rijbewijs;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType;
import nl.procura.rdw.messages.*;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.label.Break;
import nl.procura.vaadin.component.label.Ruler;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.Icons;

/**
 * Ontvangst / doorsturen rijbewijs
 */
public class Page14Rijbewijs extends RijbewijsPage {

  private final P0252 p0252;
  private HLayout     option183_2;
  private HLayout     option183_1;
  private HLayout     option182;
  private HLayout     option181;
  private HLayout     option178;
  private HLayout     option179;
  private Label       option179Message;

  public Page14Rijbewijs(P0252 p0252) {

    super("Ontvangst / doorsturen rijbewijs");
    this.p0252 = p0252;

    setMargin(true);
    addButton(buttonPrev);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      String caption179 = "(0179) Registreren verlies/diefstal afgegeven rijbewijs";
      String caption178 = "(0178) Registreren ontvangst rijbewijs na ongeldigverklaring";
      String caption181 = "(0181) Registreren ontvangst rijbewijs na ongeldigverklaring a.g.v. vorderingsprocedure";
      String caption182 = "(0182) Registreren ontvangst rijbewijs algemeen";
      String caption183_1 = "(0183) Registreren doorzending rijbewijs";
      String caption183_2 = "(0183) Registreren teruggave rijbewijs";

      option179 = addOption(caption179, e -> {
        RijbewijsOntvangstDialog dialog = new RijbewijsOntvangstDialog(p0252, caption179, "400px",
            RIJB_NR_FIELD, PROCESVERBAAL_FIELD) {

          @Override
          public void onSend(Bean bean) {
            if (stuur0179f1(getRijbewijsNummer(), bean.getProcesVerbaal())) {

              setOkIcon(option179);
              successMessage("De registratie is verstuurd");

              String dIn = new ProcuraDate().getSystemDate();
              DocumentInhouding inh = Services.getInstance().getDocumentInhoudingenService().setRijbewijsInhouding(
                  getPl(), dIn, getRijbewijsNummer().toString(), bean.getProcesVerbaal(),
                  InhoudingType.VERMISSING);

              String message = "(De diefstal/vermissing was al geregisteerd. Zie inhoudingen / vermissingen)";

              if (inh != null) {
                message = "(De diefstal/vermissing is opgeslagen. Zie inhoudingen / vermissingen)";
              }

              option179Message = setMessage(option179, option179Message, message);
            }
          }
        };

        getWindow().addWindow(dialog);

      });

      option178 = addOption(caption178, e -> {
        RijbewijsOntvangstDialog dialog = new RijbewijsOntvangstDialog(p0252, caption178, "400px",
            RIJB_NR_FIELD) {

          @Override
          public void onSend(Bean bean) {
            if (stuur0178f1(getRijbewijsNummer())) {
              stuur0182f1(getRijbewijsNummer(), getNatPersSleutel());
              setOkIcon(option178);
              setOkIcon(option182);
              successMessage("De registratie is verstuurd");
            }
          }
        };
        getWindow().addWindow(dialog);
      });

      option181 = addOption(caption181, e -> {
        RijbewijsOntvangstDialog dialog = new RijbewijsOntvangstDialog(p0252, caption181, "400px",
            RIJB_NR_FIELD) {

          @Override
          public void onSend(final Bean bean) {
            if (stuur0181f1(getRijbewijsNummer())) {
              stuur0182f1(getRijbewijsNummer(), getNatPersSleutel());
              setOkIcon(option181);
              setOkIcon(option182);
              successMessage("De registratie is verstuurd");
            }
          }
        };
        getWindow().addWindow(dialog);
      });

      option182 = addOption(caption182, e -> {
        RijbewijsOntvangstDialog dialog = new RijbewijsOntvangstDialog(p0252, caption182, "400px",
            RIJB_NR_FIELD, NAT_PERS_SL_FIELD) {

          @Override
          public void onSend(Bean bean) {
            if (stuur0182f1(getRijbewijsNummer(), getNatPersSleutel())) {
              setOkIcon(option182);
              successMessage("De registratie is verstuurd");
            }
          }
        };
        getWindow().addWindow(dialog);
      });

      option183_1 = addOption(caption183_1, e -> {
        RijbewijsOntvangstDialog dialog = new RijbewijsOntvangstDialog(p0252, caption183_1, "400px",
            RIJB_NR_FIELD, NAT_PERS_SL_FIELD,
            AUTHCODE_FIELD) {

          @Override
          public void onSend(Bean bean) {
            if (stuur0183f1(getRijbewijsNummer(), getNatPersSleutel(),
                BigInteger.valueOf(Long.valueOf(bean.getAutoriteitscode())))) {
              setOkIcon(option183_1);
              successMessage("De registratie is verstuurd");
            }
          }
        };
        getWindow().addWindow(dialog);
      });

      option183_2 = addOption(caption183_2, e -> {
        RijbewijsOntvangstDialog dialog = new RijbewijsOntvangstDialog(p0252, caption183_2, "400px",
            RIJB_NR_FIELD, NAT_PERS_SL_FIELD) {

          @Override
          public void onSend(Bean bean) {
            if (stuur0183f1(getRijbewijsNummer(), getNatPersSleutel(), null)) {
              setOkIcon(option183_2);
              successMessage("De registratie is verstuurd");
            }
          }
        };
        getWindow().addWindow(dialog);
      });

      addComponent(new Break());
      addComponent(option179);
      addComponent(new Ruler());
      addComponent(option178);
      addComponent(new Ruler());
      addComponent(option181);
      addComponent(new Ruler());
      addComponent(option182);
      addComponent(new Ruler());
      addComponent(option183_1);
      addComponent(new Ruler());
      addComponent(option183_2);
    }

    super.event(event);
  }

  private void setOkIcon(HLayout layout) {
    Iterator<Component> it = layout.getComponentIterator();
    while (it.hasNext()) {
      Component component = it.next();
      if (component instanceof TableImage) {
        layout.removeComponent(component);
        break;
      }
    }
    layout.addComponent(new TableImage(Icons.getIcon(Icons.ICON_OK)));
  }

  private Label setMessage(HLayout layout, Label label, String message) {
    if (label != null) {
      layout.removeComponent(label);
    }
    Label newLabel = new Label(message, Label.CONTENT_XHTML);
    layout.addComponent(newLabel);
    return newLabel;
  }

  /**
   * Registeren ontvangst rijbewijs na ongeldig verklaring
   */
  public boolean stuur0178f1(BigInteger rbwNr) {
    P0178 p0178F1 = new P0178();
    p0178F1.newF1(rbwNr);
    return sendMessage(p0178F1);
  }

  /**
   * Registeren verlies / diefstal afgegeven rijbewijs
   */
  public boolean stuur0179f1(BigInteger rbwNr, String procesVerbaal) {
    P0179 p0179F1 = new P0179();
    p0179F1.newF1(rbwNr, procesVerbaal);
    return sendMessage(p0179F1);
  }

  /**
   * Registreren ontvangst rijbewijs na ongeldigverklaring a.g.v. vorderingsprocedure
   */
  public boolean stuur0181f1(BigInteger rbwNr) {
    P0181 p0181F1 = new P0181();
    p0181F1.newF1(rbwNr);
    return sendMessage(p0181F1);
  }

  /**
   * Registreren ontvangst rijbewijs algemeen
   */
  public boolean stuur0182f1(BigInteger rbwNr, String natPersSleutel) {
    P0182 p0182F1 = new P0182();
    p0182F1.newF1(rbwNr, natPersSleutel);
    return sendMessage(p0182F1);
  }

  /**
   * Registreren doorzending rijbewijs
   */
  public boolean stuur0183f1(BigInteger rbwNr, String natPersSleutel, BigInteger autorCode) {
    P0183 p0182F1 = new P0183();
    p0182F1.newF1(rbwNr, natPersSleutel, autorCode);
    return sendMessage(p0182F1);
  }

  private HLayout addOption(String caption, ClickListener clickListener) {

    Button button = new Button("");
    button.setWidth("40px");
    button.setDescription(caption);
    button.setIcon(new ThemeResource("../gba-web/buttons/img/forward.png"));
    button.addListener(clickListener);

    return new HLayout().align(Alignment.MIDDLE_LEFT).add(button, new Label(caption));
  }

  @Override
  public void onPreviousPage() {

    P0252 newP0252 = new P0252();
    newP0252.newF1(getPl().getPersoon().getBsn().getVal());

    if (sendMessage(newP0252)) {
      getNavigation().removePage(Page14Rijbewijs.class);
      getNavigation().removePage(Page4Rijbewijs.class);
      getNavigation().goToPage(new Page4Rijbewijs(newP0252, false));
    }
  }
}
