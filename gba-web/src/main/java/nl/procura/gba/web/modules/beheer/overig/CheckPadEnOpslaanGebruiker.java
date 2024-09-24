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

package nl.procura.gba.web.modules.beheer.overig;

import static java.text.MessageFormat.format;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.eq;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.ENTRY;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.beheer.gebruikers.page2.Page2GebruikersBean;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;

/**
 * Deze klasse bevat de logica voor het opslaan van een nieuwe mapnaam bij een gebruiker.
 * Er wordt gecontroleerd of de mapnaam nieuw is en lege mapnamen worden niet toegestaan.
 * Merk op dat er bij het invoeren van de mapnaam een check wordt gedaan door MapValidator.
 *

 * <p>
 * 2012
 */

public abstract class CheckPadEnOpslaanGebruiker {

  private final GbaForm form;

  public CheckPadEnOpslaanGebruiker(String cleanedPath, Page2GebruikersBean bean, GbaForm form) {
    this.form = form;
    String legalPath = normaliseerPad(cleanedPath).toString();
    checkPadEnOpslaanGebruiker(legalPath, bean);
  }

  protected abstract void nietOpslaanGebruikerActies();

  protected abstract void opslaanGebruiker(String pad, Page2GebruikersBean bean);

  protected abstract void welOpslaanGebruikerActies(String pad, Page2GebruikersBean bean);

  private boolean bestaatPad(String pad) {
    for (Gebruiker gebr : getAllUsers()) {
      if (eq(gebr.getPad(), pad)) {
        return true;
      }
    }
    return false;
  }

  private void checkPadEnOpslaanGebruiker(final String gebruikerPad, Page2GebruikersBean bean) {

    final Page2GebruikersBean b = bean;

    if (!emp(
        gebruikerPad)) { // gebruikersPath bevat in ieder geval een letter, cijfer, underscore of koppelteken vanwege mapValidator

      boolean isExistingPath = bestaatPad(gebruikerPad);

      if (isExistingPath) {
        opslaanGebruiker(gebruikerPad, b);
      } else {

        String msg = format("Ingevoerde mapnaam ''{0}'' <br> is nieuw. Aan lijst toevoegen?", gebruikerPad);
        form.getWindow().addWindow(new ConfirmDialog(msg) {

          @Override
          public void buttonNo() {
            close();
            nietOpslaanGebruikerActies();
          }

          @Override
          public void buttonYes() {
            close();
            welOpslaanGebruikerActies(gebruikerPad, b);
          }
        });
      }
    } else {
      // gebruikersPath is gelijk aan de lege string omdat
      // er al een trim() overheen is gegaan in onSave().
      opslaanGebruiker(gebruikerPad, bean);
    }
  }

  private List<Gebruiker> getAllUsers() {
    Services dbC = form.getApplication().getServices();
    return dbC.getGebruikerService().getGebruikers(false);
  }

  /**
   * Deze functie controleert of er geen lege mapnamen voorkomen.
   *
   * @return mapstructuur zonder lege mappen.
   */

  private StringBuilder normaliseerPad(String cleanedPath) {

    List<String> mappenString = new ArrayList<>();
    StringBuilder legaalPad = new StringBuilder();
    String[] mappen = cleanedPath.split("/");
    int index = 0;

    if (!emp(cleanedPath)) {
      for (String map : mappen) {
        if (emp(map)) {
          throw new ProException(ENTRY, WARNING, "Gebruik geen lege mapnamen a.u.b.");
        }
        // niet-lege mapnaam
        mappenString.add(map);
      }

      for (String dir : mappenString) {
        if (index == mappenString.size() - 1) {
          legaalPad = legaalPad.append(dir);
        } else {
          legaalPad = legaalPad.append(dir).append("/");
        }
        index++;
      }
    }
    return legaalPad;
  }
}
