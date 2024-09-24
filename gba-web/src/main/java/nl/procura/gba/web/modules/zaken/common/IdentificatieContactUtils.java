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

package nl.procura.gba.web.modules.zaken.common;

import com.vaadin.ui.Window;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.page.GbaPageTemplate;
import nl.procura.gba.web.modules.zaken.contact.ContactWindow;
import nl.procura.gba.web.modules.zaken.identificatie.IdentificatieWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.contact.ContactStatusListener;
import nl.procura.gba.web.services.zaken.contact.ContactVerplichtMate;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.identiteit.IdVerplichtMate;
import nl.procura.gba.web.services.zaken.identiteit.IdentificatieService;
import nl.procura.gba.web.services.zaken.identiteit.IdentificatieStatusListener;
import nl.procura.vaadin.component.layout.page.PageLayout;

public class IdentificatieContactUtils {

  /**
   * Combineert het identificeren van de aanvrager en invullen van de contactgegevens.
   */
  public static void startProcess(final GbaPageTemplate page, final GbaPageTemplate nextPage, boolean startProcess) {
    start(page, nextPage, startProcess);
  }

  /**
   * Combineert het identificeren van de aanvrager en invullen van de contactgegevens.
   */
  public static void startProcess(final GbaPageTemplate page, final Window nextWindow, boolean startProcess) {
    start(page, nextWindow, startProcess);
  }

  /**
   * Combineert het identificeren van de aanvrager en invullen van de contactgegevens.
   */
  public static void startProcess(final GbaPageTemplate page, final Runnable runnable, boolean startProcess) {
    start(page, runnable, startProcess);
  }

  public static void checkContactAkkoord(Window window, Window contactWindow,
      ContactStatusListener succesListener) {

    Services services = Services.getInstance();
    ContactgegevensService contactService = services.getContactgegevensService();
    BasePLExt pl = services.getPersonenWsService().getHuidige();

    boolean isRequired = ContactVerplichtMate.NIET_VERPLICHT_NIET_TONEN == contactService.getMateVerplicht();
    boolean isAlreadyAsked = contactService.isVastGesteld(pl, true) && succesListener != null;
    if (isRequired || isAlreadyAsked) {
      succesListener.onStatus(true, false);
    } else {
      window.addWindow(contactWindow);
    }
  }

  public static void checkIdentificatieAkkoord(Window window, Window identificatieWindow,
      IdentificatieStatusListener succesListener) {

    Services services = Services.getInstance();
    IdentificatieService idService = services.getIdentificatieService();
    BasePLExt pl = services.getPersonenWsService().getHuidige();

    boolean isRequired = IdVerplichtMate.NIET_VERPLICHT_NIET_TONEN == idService.getMateVerplicht();
    boolean isAlreadyAsked = idService.isVastGesteld(pl) && succesListener != null;

    if (isRequired || isAlreadyAsked) {
      succesListener.onStatus(true, false);
    } else {
      window.addWindow(identificatieWindow);
    }
  }

  private static void start(final GbaPageTemplate page, final Object next, final boolean startProcess) {

    final Services services = page.getApplication().getServices();
    final ContactgegevensService contactService = services.getContactgegevensService();
    final IdentificatieService idService = services.getIdentificatieService();
    final Window window = page.getParentWindow();

    IdentificatieStatusListener idSuccesListener = (saved, newAdded) -> {

      ContactStatusListener contactSuccesListener = (saved1, newAdded1) -> {
        if (startProcess) {
          page.getApplication().getProcess().startProcess();
        }

        if (next instanceof GbaPageTemplate) {
          page.getNavigation().goToPage((PageLayout) next);

        } else if (next instanceof Window) {
          window.addWindow((Window) next);

        } else if (next instanceof Runnable) {
          ((Runnable) next).run();
        }
      };

      checkContactAkkoord(window, new ContactWindow(contactSuccesListener),
          contactSuccesListener);
    };

    checkIdentificatieAkkoord(window, new IdentificatieWindow(idSuccesListener), idSuccesListener);
  }
}
