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

package nl.procura.gba.web.services;

import java.util.Optional;

import nl.procura.gba.jpa.personen.utils.GbaJpa;
import nl.procura.gba.web.services.beheer.bsm.BsmService;
import nl.procura.gba.web.services.beheer.bsm.BsmServiceMock;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gba.web.services.gba.ple.PersonenWsServiceMock;
import nl.procura.gba.web.services.gba.tabellen.TabellenServiceMock;

public class ServicesMock extends Services {

  private final PersonenWsServiceMock personenWsServiceMock;
  private final BsmServiceMock        bsmServiceMock;

  public ServicesMock() {
    this(TYPE.REST);
    Gebruiker user = retrieveMockUser().orElse(createMockUser());
    setGebruiker(user);
  }

  public ServicesMock(TYPE type) {
    super(type);
    requireGbaJpaStorage();
    TabellenServiceMock.init();
    personenWsServiceMock = new PersonenWsServiceMock(this);
    bsmServiceMock = new BsmServiceMock();
  }

  private static void requireGbaJpaStorage() {
    try {
      GbaJpa.getManager();
    } catch (Exception e) {
      throw new IllegalStateException("GbaJpaStorage has not been set yet with ThreadLocalStorage.init", e);
    }
  }

  @Override
  public PersonenWsService getPersonenWsService() {
    return personenWsServiceMock;
  }

  @Override
  public BsmService getBsmService() {
    return bsmServiceMock;
  }

  private Optional<Gebruiker> retrieveMockUser() {
    return Optional.ofNullable(
        getGebruikerService().getGebruikerByNaam("mock"));
  }

  private Gebruiker createMockUser() {
    Gebruiker newUser = new Gebruiker();
    newUser.setCUsr(1L);
    newUser.setUsr("mock");
    getGebruikerService().save(newUser);
    // retrieve user to read extra data like parameters too
    return retrieveMockUser().orElseThrow(IllegalStateException::new);
  }

}
