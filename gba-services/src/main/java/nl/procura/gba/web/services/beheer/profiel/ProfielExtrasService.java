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

package nl.procura.gba.web.services.beheer.profiel;

import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.gba.jpa.personen.dao.ActionDao;
import nl.procura.gba.jpa.personen.dao.FieldDao;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.beheer.parameter.Parameter;
import nl.procura.gba.web.services.beheer.parameter.Parameters;
import nl.procura.gba.web.services.beheer.profiel.actie.Actie;
import nl.procura.gba.web.services.beheer.profiel.actie.Acties;
import nl.procura.gba.web.services.beheer.profiel.gba_categorie.PleCategorie;
import nl.procura.gba.web.services.beheer.profiel.gba_categorie.PleCategorieen;
import nl.procura.gba.web.services.beheer.profiel.gba_element.PleElement;
import nl.procura.gba.web.services.beheer.profiel.gba_element.PleElementen;
import nl.procura.gba.web.services.beheer.profiel.indicatie.PlAantekeningIndicaties;
import nl.procura.gba.web.services.beheer.profiel.veld.Veld;
import nl.procura.gba.web.services.beheer.profiel.veld.Velden;
import nl.procura.gba.web.services.beheer.profiel.zaak_conf.ZaakConfiguraties;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningIndicatie;
import nl.procura.gba.web.services.zaken.algemeen.zaakconfiguraties.ZaakConfiguratie;

public class ProfielExtrasService extends AbstractService {

  public ProfielExtrasService() {
    super("Profiel-extras");
  }

  @ThrowException("Fout bij ophalen profielacties")
  public Acties getActies() {
    return new Acties(copyList(ActionDao.find(), Actie.class));
  }

  @ThrowException("Fout bij ophalen profiel-acties")
  public Acties getActies(Profiel profiel) {
    return new Acties(copyList(to(profiel, Profiel.class).getActions(), Actie.class));
  }

  @ThrowException("Fout bij ophalen profiel-gba-categorie")
  public PleCategorie getCategorie(GBACat gbaCategorie) {
    return new PleCategorie(gbaCategorie);
  }

  @ThrowException("Fout bij ophalen profiel-gba-categorieen")
  public PleCategorieen getCategorieen(Profiel profiel) {
    return new PleCategorieen(copyList(to(profiel, Profiel.class).getGbaCategories(), PleCategorie.class));
  }

  @ThrowException("Fout bij ophalen profiel-gba-elementen")
  public PleElement getElement(GBAGroupElements.GBAGroupElem pleType) {

    PleElement pleElement = new PleElement();
    pleElement.setCategory(toBigDecimal(pleType.getCat().getCode()));
    pleElement.setElement(toBigDecimal(pleType.getElem().getCode()));

    return pleElement;
  }

  @ThrowException("Fout bij ophalen profiel-gba-elementen")
  public PleElementen getElementen(Profiel profiel) {
    return new PleElementen(copyList(to(profiel, Profiel.class)
        .getGbaElements(), PleElement.class));
  }

  @ThrowException("Fout bij ophalen indicaties")
  public PlAantekeningIndicaties getIndicaties(Profiel profiel) {
    return new PlAantekeningIndicaties(copyList(to(profiel, Profiel.class)
        .getAantekeningInds(), PlAantekeningIndicatie.class));
  }

  @ThrowException("Fout bij ophalen zaak configuraties")
  public ZaakConfiguraties getZaakConfiguraties(Profiel profiel) {
    return new ZaakConfiguraties(copyList(to(profiel, Profiel.class)
        .getZaakConfs(), ZaakConfiguratie.class));
  }

  @ThrowException("Fout bij ophalen profiel-parameters")
  public Parameters getParameters(Profiel profiel) {
    return new Parameters(
        copyList(to(profiel, Profiel.class).getParms(), Parameter.class));
  }

  @ThrowException("Fout bij ophalen profiel-velden")
  public Velden getVelden() {
    return new Velden(copyList(FieldDao.find(), Veld.class));
  }

  @ThrowException("Fout bij ophalen velden-acties")
  public Velden getVelden(Profiel profiel) {
    return new Velden(copyList(to(profiel, Profiel.class).getFields(), Veld.class));
  }
}
