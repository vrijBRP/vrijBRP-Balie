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

package nl.procura.gba.web.modules.account.wachtwoord.pages;

import static nl.procura.gba.web.modules.account.wachtwoord.pages.ChangePasswordBean.*;

import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.account.wachtwoord.pages.passwordExpired.PasswordExpired;
import nl.procura.gba.web.modules.account.wachtwoord.passwordStrength.NewPasswordListener;
import nl.procura.gba.web.modules.account.wachtwoord.passwordStrength.PasswordStrength;
import nl.procura.gba.web.modules.account.wachtwoord.validators.*;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;
import nl.procura.vaadin.component.validator.MinLengteValidator;

public class ChangePasswordForm extends GbaForm<ChangePasswordBean> {

  private static final int          AANT_OUDE_WW  = 6;                        // aantal oude ww waarmee vergeleken moet worden
  private final NewPasswordListener newPwListener = new NewPasswordListener();
  private final PasswordExpired     passwordExpired;

  public ChangePasswordForm(PasswordExpired passwordExpired) {
    this.passwordExpired = passwordExpired;
    setReadThrough(true);

    if (passwordExpired.getPersonenLink() == null) {
      setOrder(GEBRUIKER, DATUM, HUIDIG_WW, NIEUW_WW, HERHALING_NIEUW_WW);
    } else {
      setOrder(GEBRUIKER, DATUM, NIEUW_WW, HERHALING_NIEUW_WW);
    }

    setColumnWidths("200px", "");
    setBean(new ChangePasswordBean());
  }

  /**
   * Deze functie wordt na setBean() aangeroepen. Het checkt of
   * het huidige ww correct is ingevuld en of het nieuwe ww inderdaad verschillend is en
   * uit minimaal 6 karakters bestaat. Ook zorgt het voor het ingevuld blijven
   * van het verloopdatum veld bij het drukken op 'Reset (F7)'.
   */

  @Override
  public void afterSetBean() {
    setValidators();
    setListeners();
    setVerloopDatum();
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {

    if (property.is(NIEUW_WW)) {
      // nodig omdat bij het label een TableLayout als parent is toegevoegd die vernieuwd wordt bij een reset!
      newPwListener.setStrengthLabel(new StrengthLabel());
      column.addComponent(newPwListener.getStrengthLabel());
    }

    super.afterSetColumn(column, field, property);
  }

  private void checkEndDatePw(ProcuraDate dEndPw) {
    if (dEndPw.isExpired()) {
      getField(DATUM).addStyleName(GbaWebTheme.TEXT.RED);
    }
  }

  private String getHistPwErrorMsg() {
    return "Het nieuwe wachtwoord moet verschillen met de voorgaande "
        + ChangePasswordForm.AANT_OUDE_WW
        + " wachtwoorden.";
  }

  private void setListeners() {
    TextField nieuwVeld = (TextField) getField(NIEUW_WW);
    nieuwVeld.setTextChangeEventMode(TextChangeEventMode.LAZY);
    nieuwVeld.setTextChangeTimeout(50);
    nieuwVeld.addListener(newPwListener);
  }

  private void setValidators() {
    TextField huidigVeld = (TextField) getField(HUIDIG_WW);
    TextField nieuwVeld = (TextField) getField(NIEUW_WW);
    TextField herhalingVeld = (TextField) getField(HERHALING_NIEUW_WW);

    if (huidigVeld != null) {
      huidigVeld.addValidator(new HuidigWachtwoordValidator(passwordExpired));
    }

    nieuwVeld.addValidator(new MinLengteValidator(8));
    nieuwVeld.addValidator(new WachtwoordVariatieValidator());
    nieuwVeld.addValidator(new WachtwoordSpatiesValidator());
    nieuwVeld.addValidator(new WachtwoordHistorieValidator(getHistPwErrorMsg(), passwordExpired, AANT_OUDE_WW));
    herhalingVeld.addValidator(new BevestigWachtwoordValidator(this));
  }

  private void setVerloopDatum() {
    Gebruiker gebruiker = passwordExpired.getGebruiker();
    GebruikerService gebruikers = passwordExpired.getGebruikers();
    getBean().setGebruiker(gebruiker.getNaam() + " (" + gebruiker.getGebruikersnaam() + ")");

    if (gebruikers.isResetWachtwoord(gebruiker)) {
      ProcuraDate dInPw = gebruikers.getWachtwoordMutatiedatum(gebruiker);
      getBean().setDatum(dInPw.getFormatDate());
      checkEndDatePw(dInPw);

    } else {
      if (gebruikers.isWachtwoordKanVerlopen(gebruiker)) { // ww verloopt
        ProcuraDate verloopDatum = gebruikers.getWachtwoordVerloopdatum(gebruiker);
        getBean().setDatum(verloopDatum.getFormatDate());
        checkEndDatePw(verloopDatum);

      } else {
        getBean().setDatum("Niet van toepassing");
      }
    }
  }

  public static class StrengthLabel extends Label {

    public void set(PasswordStrength strength) {
      setValue(strength.getOms());
      setStyleName(strength.getColor());
    }
  }
}
