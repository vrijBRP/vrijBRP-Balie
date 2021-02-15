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

package nl.procura.gba.web.services.zaken.legezaak;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.zaken.algemeen.AbstractZaakService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakService;

public class LegeZaakService extends AbstractZaakService<LegeZaak> implements ZaakService<LegeZaak> {

  public LegeZaakService() {
    super("Lege zaak", ZaakType.LEGE_PERSOONLIJST);
  }

  @Override
  @ThrowException("Fout bij het zoeken van de reisdocumenten")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return 0;
  }

  @Override
  @ThrowException("Fout bij het zoeken van de reisdocumenten")
  public List<LegeZaak> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return new ArrayList<>();
  }

  @Override
  public Zaak getNewZaak() {
    return null;
  }

  @Override
  public LegeZaak getStandardZaak(LegeZaak zaak) {
    return null;
  }

  @Override
  public LegeZaak getCompleteZaak(LegeZaak zaak) {
    return this.getStandardZaak(zaak);
  }

  @Override
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return new ArrayList<>();
  }

  @Override
  public void save(LegeZaak zaak) {
  }

  @Override
  public void delete(LegeZaak zaak) {
  }
}
