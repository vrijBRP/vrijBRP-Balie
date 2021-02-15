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

package nl.procura.gba.web.modules.bs.omzetting.page50.info;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class Page50InfoWindow extends GbaModalWindow {

  public Page50InfoWindow() {

    super("Uitleg (Druk op escape om te sluiten)", "900px");

    VerticalLayout v = new VerticalLayout();
    v.setSpacing(true);
    v.setMargin(true);

    v.addComponent(new Fieldset("Leeftijd"));

    v.addComponent(new InfoLayout("",
        "Wie wil trouwen, moet minimaal 18 jaar zijn. "
            + "Minderjarigen van 16 of 17 jaar kunnen trouwen wanneer de vrouw zwanger is of al bevallen is. "
            + "De vrouw moet bij een zwangerschap een verklaring van een arts overleggen dat zij zwanger is. "
            + "Verder zijn om gewichtige redenen uitzonderingen mogelijk, dit heet huwelijksdispensatie en hierover "
            + "beslist de minister van Veiligheid en Justitie. "
            + "Een minderjarige die wil trouwen, heeft bovendien altijd toestemming nodig van diens ouders of voogd. "
            + "Als die geen toestemming geven, kan de minderjarige de rechtbank om vervangende toestemming vragen"));

    v.addComponent(new Fieldset("Wilsbepaling en curatele"));

    v.addComponent(new InfoLayout("",
        "Iemand die onder curatele staat wegens verkwisting of drankmisbruik heeft, om te mogen trouwen, "
            + "toestemming van de curator nodig. "
            + "Als de curator geen toestemming geeft, kan de kantonrechter toestemming geven. "
            + "Iemand die onder curatele staat wegens een geestelijke stoornis heeft toestemming van de kantonrechter nodig. "
            + "Curatele wegens een geestelijke stoornis houdt niet automatisch in dat de persoon niet in staat is zijn wil te bepalen. "
            + "Het gaat er daarbij om dat duidelijk (algemeen kenbaar) is dat de persoon bijvoorbeeld niet in staat is de "
            + "betekenis van het ja-woord te begrijpen."));

    v.addComponent(new Fieldset("Geen actueel huwelijk/GPS"));

    v.addComponent(new InfoLayout("",
        "Als iemand in Nederland wil trouwen, kan dat met 1 persoon tegelijkertijd. "
            + "Ook mag iemand die wil trouwen, niet op hetzelfde moment getrouwd zijn of een registreerd partnerschap "
            + "hebben met een ander."));

    v.addComponent(new Fieldset("Geen te nauwe verwantschap"));

    v.addComponent(new InfoLayout("",
        "Ouders en kinderen, grootouders en kleinkinderen, (half) broers en (half) zusters mogen niet met elkaar trouwen. "
            + "Als broers en zus door adoptie familie van elkaar zijn, kan de minister van Veiligheid en Justitie het verbod opheffen"
            + " door huwelijksdispensatie te verlenen. "
            + "Een huwelijk tussen neef en nicht en een huwelijk tussen zwager en schoonzus zijn niet verboden."));

    addComponent(v);
  }
}
