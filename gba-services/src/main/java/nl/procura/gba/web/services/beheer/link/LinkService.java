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

package nl.procura.gba.web.services.beheer.link;

import static java.util.Arrays.asList;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.config.GbaProperties;
import nl.procura.gba.jpa.personen.dao.LinkDao;
import nl.procura.gba.jpa.personen.db.Link;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;

public class LinkService extends AbstractService {

  public LinkService() {
    super("Links");
  }

  public void addPersonenLink(PersonenLink personenLink) {
    PersonenLink impl = personenLink;
    impl.setProps(GbaProperties.toByteArray(impl.getProperties()));
    impl.setDIn(toBigDecimal(new DateTime().getLongDate()));
    saveEntity(personenLink);
  }

  public PersonenLink getById(String linkId) {

    Link link = LinkDao.findById(linkId);
    return (link != null) ? getLinks(asList(link)).get(0) : null;
  }

  public List<PersonenLink> getLinks() {
    return getLinks(LinkDao.findLinks());
  }

  public String getRandomId() {
    String id = getNewRandomId();
    while (LinkDao.findById(id) != null) {
      id = getNewRandomId();
    }

    return id;
  }

  @ThrowException("Fout bij het verwijderen")
  @Transactional
  public void delete(PersonenLink link) {
    removeEntity(link);
  }

  private List<PersonenLink> getLinks(List<Link> links) {

    List<PersonenLink> personenLinks = new ArrayList<>();
    for (Link link : links) {
      PersonenLink personenLink = MiscUtils.copy(link, PersonenLink.class);
      personenLink.setProperties(GbaProperties.getProperties(link.getProps()));
      personenLinks.add(personenLink);
    }

    return personenLinks;
  }

  private String getNewRandomId() {
    return UUID.randomUUID().toString();
  }
}
