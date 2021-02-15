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

package nl.procura.gba.web.common.misc.email;

import static nl.procura.standard.Globalfunctions.emp;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.services.beheer.email.EmailTemplate;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;

public class Verzending {

  private String           emailAdres = "";
  private EmailTemplate    email      = null;
  private Gebruiker        gebruiker  = null;
  private VerzendingStatus status     = VerzendingStatus.GEEN;
  private String           fout       = "";

  public Verzending(EmailTemplate email, Verzending verzending) {
    setEmail(email);
    setEmailAdres(verzending.getEmailAdres());
    setGebruiker(verzending.getGebruiker());
    setStatus(verzending.getStatus());
    setFout(verzending.getFout());
  }

  public Verzending(Gebruiker gebruiker) {
    this(gebruiker, gebruiker.getEmail());
  }

  public Verzending(Gebruiker gebruiker, String emailAdres) {

    super();

    this.gebruiker = gebruiker;
    this.emailAdres = emailAdres;

    if (emp(emailAdres)) {
      setStatus(VerzendingStatus.FOUT);
      setFout("Geen e-mailadres");
    }
  }

  public static List<Verzending> getVerzendingen(List<Gebruiker> gebruikers) {

    List<Verzending> verzendingen = new ArrayList<>();

    for (Gebruiker gebruiker : gebruikers) {

      verzendingen.add(new Verzending(gebruiker));
    }

    return verzendingen;
  }

  public EmailTemplate getEmail() {
    return email;
  }

  public void setEmail(EmailTemplate email) {
    this.email = email;
  }

  public String getEmailAdres() {
    return emailAdres;
  }

  public void setEmailAdres(String emailAdres) {
    this.emailAdres = emailAdres;
  }

  public String getFout() {
    return fout;
  }

  public void setFout(String fout) {
    this.fout = fout;
  }

  public Gebruiker getGebruiker() {
    return gebruiker;
  }

  public void setGebruiker(Gebruiker gebruiker) {
    this.gebruiker = gebruiker;
  }

  public VerzendingStatus getStatus() {
    return status;
  }

  public void setStatus(VerzendingStatus status) {
    this.status = status;
  }

  public void setStatus(VerzendingStatus status, String message) {
    setStatus(status);
    setFout(message);
  }

  public enum VerzendingStatus {
    GEEN,
    FOUT,
    VERSTUURD
  }
}
