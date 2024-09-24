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

package nl.procura.gba.web.modules.bs.common.pages.persoonpage;

import static java.util.Arrays.asList;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import com.vaadin.ui.Button;

import nl.procura.gba.web.modules.zaken.common.IdentificatieContactUtils;
import nl.procura.gba.web.modules.zaken.contact.ContactWindow;
import nl.procura.gba.web.modules.zaken.identificatie.IdentificatieWindow;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.zaken.contact.ContactStatusListener;
import nl.procura.gba.web.services.zaken.identiteit.IdentificatieStatusListener;
import nl.procura.commons.core.exceptions.ProException;

/**
 * Aangever
 */
public class BsContactpersoonPage<T extends ZaakDossier> extends BsPersoonPage<T> {

  protected final Button buttonIden    = new Button("Identificatie");
  protected final Button buttonContact = new Button("Contactgegevens");

  public BsContactpersoonPage(String title) {
    super(title);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonIden) {
      check(false, buttonIden);
    }

    if (button == buttonContact) {
      check(false, buttonContact);
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void update() {

    boolean isBsn = getDossierPersoon().getBurgerServiceNummer().isCorrect();
    boolean isIdentificatieNodig = isBsn && getDossierPersoon().isIdentificatieNodig();

    buttonIden.setEnabled(isBsn);
    buttonContact.setEnabled(isBsn);

    if (isIdentificatieNodig) {
      check(true, buttonIden, buttonContact);

    } else {
      check(true);
    }

    super.update();
  }

  @Override
  protected String getInfo() {
    return "Zoek de persoon, controleer de gegevens of vul deze in. "
        + "Stel de identiteit vast en vul contactgegevens in. Druk op Volgende (F2) om verder te gaan.";
  }

  private void check(final boolean init, Button... buttons) {

    final boolean isIdent = asList(buttons).contains(buttonIden);
    final boolean isContact = asList(buttons).contains(buttonContact);

    long bsn = along(getDossierPersoon().getBurgerServiceNummer().getValue());

    if (pos(bsn)) {

      final ContactStatusListener contactSuccesListener = (saved, newAdded) -> {

        if (saved) {
          if (newAdded) {
            successMessage("Contactgegevens zijn geaccordeerd");
          } else {
            successMessage("Contactgegevens waren reeds geaccordeerd");
          }
        }
      };

      final IdentificatieStatusListener idSuccesListener = (saved, newAdded) -> {

        if (saved) {
          if (newAdded) {
            successMessage("De identiteit is vastgesteld");
          } else {
            successMessage("De identiteit was reeds vastgesteld");
          }
        }

        if (init) {
          IdentificatieContactUtils.checkContactAkkoord(getParentWindow(),
              new ContactWindow(contactSuccesListener), contactSuccesListener);
        }
      };

      // Toevoegen aan historie zodat identificatie kan plaatsvinden
      getServices().getPersonenWsService().getPersoonslijst(true, astr(bsn));

      if (isIdent) {
        if (init) {
          IdentificatieContactUtils.checkIdentificatieAkkoord(getParentWindow(),
              new IdentificatieWindow(idSuccesListener), idSuccesListener);
        } else {
          getParentWindow().addWindow(new IdentificatieWindow(idSuccesListener));
        }
      } else if (isContact) {
        if (!init) {
          getParentWindow().addWindow(new ContactWindow(contactSuccesListener));
        }
      }
    } else {
      if (!init) {
        throw new ProException(WARNING, "Geen BSN om op te zoeken.");
      }
    }
  }
}
