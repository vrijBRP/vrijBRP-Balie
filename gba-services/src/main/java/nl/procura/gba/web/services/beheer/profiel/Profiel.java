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

import static nl.procura.java.reflection.ReflectionUtil.deepCopyBean;

import java.util.List;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personen.db.*;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.KoppelbaarAanGebruiker;
import nl.procura.gba.web.services.beheer.parameter.Parameters;
import nl.procura.gba.web.services.beheer.profiel.actie.Actie;
import nl.procura.gba.web.services.beheer.profiel.actie.Acties;
import nl.procura.gba.web.services.beheer.profiel.actie.KoppelbaarAanActie;
import nl.procura.gba.web.services.beheer.profiel.gba_categorie.KoppelbaarAanPleCategorie;
import nl.procura.gba.web.services.beheer.profiel.gba_categorie.PleCategorie;
import nl.procura.gba.web.services.beheer.profiel.gba_categorie.PleCategorieen;
import nl.procura.gba.web.services.beheer.profiel.gba_element.KoppelbaarAanPleElement;
import nl.procura.gba.web.services.beheer.profiel.gba_element.PleElement;
import nl.procura.gba.web.services.beheer.profiel.gba_element.PleElementen;
import nl.procura.gba.web.services.beheer.profiel.indicatie.KoppelbaarAanIndicatie;
import nl.procura.gba.web.services.beheer.profiel.indicatie.PlAantekeningIndicaties;
import nl.procura.gba.web.services.beheer.profiel.veld.KoppelbaarAanVeld;
import nl.procura.gba.web.services.beheer.profiel.veld.Veld;
import nl.procura.gba.web.services.beheer.profiel.veld.Velden;
import nl.procura.gba.web.services.beheer.profiel.zaak_conf.KoppelbaarAanZaakConfiguratie;
import nl.procura.gba.web.services.beheer.profiel.zaak_conf.ZaakConfiguraties;
import nl.procura.gba.web.services.interfaces.DatabaseTable;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningIndicatie;
import nl.procura.gba.web.services.zaken.algemeen.zaakconfiguraties.ZaakConfiguratie;

public class Profiel extends Profile implements KoppelbaarAanGebruiker, KoppelbaarAanActie, KoppelbaarAanVeld,
    KoppelbaarAanPleElement, KoppelbaarAanPleCategorie, KoppelbaarAanIndicatie, KoppelbaarAanZaakConfiguratie,
    DatabaseTable {

  private static final long serialVersionUID = 5552031404786082266L;

  private static final String BEGIN_COUPLE_ERROR_MESSAGE = "Object van type ";
  private static final String END_COUPLE_ERROR_MESSAGE   = " kan niet gekoppeld worden aan een profiel.";
  private Parameters          parameters                 = new Parameters();
  private Acties              acties                     = new Acties();
  private Velden              velden                     = new Velden();
  private PleElementen        elementen                  = new PleElementen();

  private PleCategorieen          categorieen       = new PleCategorieen();
  private PlAantekeningIndicaties indicaties        = new PlAantekeningIndicaties();
  private ZaakConfiguraties       zaakConfiguraties = new ZaakConfiguraties();

  public Profiel() {
  }

  public Acties getActies() {
    return acties;
  }

  public void setActies(Acties acties) {
    this.acties = acties;
  }

  public PleCategorieen getCategorieen() {
    return categorieen;
  }

  public void setCategorieen(PleCategorieen categorieen) {
    this.categorieen = categorieen;
  }

  public PleElementen getElementen() {
    return elementen;
  }

  public void setElementen(PleElementen elementen) {
    this.elementen = elementen;
  }

  public PlAantekeningIndicaties getIndicaties() {
    return indicaties;
  }

  public void setIndicaties(PlAantekeningIndicaties indicaties) {
    this.indicaties = indicaties;
  }

  public ZaakConfiguraties getZaakConfiguraties() {
    return zaakConfiguraties;
  }

  public void setZaakConfiguraties(ZaakConfiguraties zaakConfiguraties) {
    this.zaakConfiguraties = zaakConfiguraties;
  }

  public String getOmschrijving() {
    return getDescr();
  }

  public void setOmschrijving(String omschrijving) {
    setDescr(omschrijving);
  }

  public Parameters getParameters() {
    return parameters;
  }

  public void setParameters(Parameters parameters) {
    this.parameters = parameters;
  }

  public String getProfiel() {
    return getProfile();
  }

  public void setProfiel(String profiel) {
    setProfile(profiel);
  }

  public Velden getVelden() {
    return velden;
  }

  public void setVelden(Velden velden) {
    this.velden = velden;
  }

  @Override
  public boolean isGekoppeld(Actie actie) {
    return MiscUtils.contains(actie, getActions());
  }

  @Override
  public boolean isGekoppeld(Gebruiker gebruiker) {
    return MiscUtils.contains(gebruiker, getUsrs());
  }

  public <K extends KoppelbaarAanProfiel> boolean isGekoppeld(List<K> koppelList) {
    for (K koppelobject : koppelList) {
      if (koppelobject instanceof Gebruiker) {
        if (!isGekoppeld((Gebruiker) koppelobject)) {
          return false;
        }
      } else if (koppelobject instanceof Actie) {
        if (!isGekoppeld((Actie) koppelobject)) {
          return false;
        }
      } else if (koppelobject instanceof Veld) {
        if (!isGekoppeld((Veld) koppelobject)) {
          return false;
        }
      } else if (koppelobject instanceof PleElement) {
        if (!isGekoppeld((PleElement) koppelobject)) {
          return false;
        }
      } else if (koppelobject instanceof PleCategorie) {
        if (!isGekoppeld((PleCategorie) koppelobject)) {
          return false;
        }
      } else if (koppelobject instanceof PlAantekeningIndicatie) {
        if (!isGekoppeld((PlAantekeningIndicatie) koppelobject)) {
          return false;
        }
      } else if (koppelobject instanceof ZaakConfiguratie) {
        if (!isGekoppeld((ZaakConfiguratie) koppelobject)) {
          return false;
        }
      } else {
        throw new IllegalArgumentException(
            BEGIN_COUPLE_ERROR_MESSAGE + koppelobject.getClass() + END_COUPLE_ERROR_MESSAGE);
      }
    }

    return true;
  }

  @Override
  public boolean isGekoppeld(PlAantekeningIndicatie indicatie) {
    return MiscUtils.contains(indicatie, getAantekeningInds());
  }

  @Override
  public boolean isGekoppeld(ZaakConfiguratie configuratie) {
    return MiscUtils.contains(configuratie, getZaakConfs());
  }

  @Override
  public boolean isGekoppeld(PleCategorie pleCategorie) {
    return MiscUtils.contains(pleCategorie, getGbaCategories());
  }

  @Override
  public boolean isGekoppeld(PleElement pleElement) {
    return MiscUtils.contains(pleElement, getGbaElements());
  }

  @Override
  public boolean isGekoppeld(Veld veld) {
    return MiscUtils.contains(veld, getFields());
  }

  public <K extends KoppelbaarAanProfiel> void koppelActie(K koppelobject, KoppelActie koppelActie) {
    if (koppelobject instanceof Gebruiker) {
      koppelActie((Gebruiker) koppelobject, koppelActie);
    } else if (koppelobject instanceof Actie) {
      koppelActie((Actie) koppelobject, koppelActie);
    } else if (koppelobject instanceof Veld) {
      koppelActie((Veld) koppelobject, koppelActie);
    } else if (koppelobject instanceof PleElement) {
      koppelActie((PleElement) koppelobject, koppelActie);
    } else if (koppelobject instanceof PleCategorie) {
      koppelActie((PleCategorie) koppelobject, koppelActie);
    } else if (koppelobject instanceof PlAantekeningIndicatie) {
      koppelActie((PlAantekeningIndicatie) koppelobject, koppelActie);
    } else if (koppelobject instanceof ZaakConfiguratie) {
      koppelActie((ZaakConfiguratie) koppelobject, koppelActie);
    } else {
      throw new IllegalArgumentException(
          BEGIN_COUPLE_ERROR_MESSAGE + koppelobject.getClass() + END_COUPLE_ERROR_MESSAGE);
    }
  }

  @Override
  @ThrowException("Fout bij het koppelen van actie")
  public void koppelActie(Actie actie, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getActions().add(deepCopyBean(nl.procura.gba.jpa.personen.db.Action.class, actie));
    } else {
      getActions().remove(actie);
    }
  }

  @Override
  @ThrowException("Fout bij het koppelen van gebruiker")
  public void koppelActie(Gebruiker gebruiker, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getUsrs().add(deepCopyBean(Usr.class, gebruiker));
    } else {
      getUsrs().remove(gebruiker);
    }
  }

  @Override
  @ThrowException("Fout bij het koppelen van een indicatie")
  public void koppelActie(PlAantekeningIndicatie indicatie, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getAantekeningInds().add(deepCopyBean(AantekeningInd.class, indicatie));
    } else {
      getAantekeningInds().remove(indicatie);
    }
  }

  @Override
  @ThrowException("Fout bij het koppelen van een zaak configuratie")
  public void koppelActie(ZaakConfiguratie configuratie, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getZaakConfs().add(deepCopyBean(ZaakConf.class, configuratie));
    } else {
      getZaakConfs().remove(configuratie);
    }
  }

  @Override
  @ThrowException("Fout bij het koppelen van categorie")
  public void koppelActie(PleCategorie pleCategorie, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getGbaCategories().add(deepCopyBean(GbaCategory.class, pleCategorie));
    } else {
      getGbaCategories().remove(pleCategorie);
    }
  }

  @Override
  @ThrowException("Fout bij het koppelen van een element")
  public void koppelActie(PleElement pleElement, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getGbaElements().add(deepCopyBean(GbaElement.class, pleElement));
    } else {
      getGbaElements().remove(pleElement);
    }
  }

  @Override
  @ThrowException("Fout bij het koppelen van veld")
  public void koppelActie(Veld veld, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getFields().add(deepCopyBean(Field.class, veld));
    } else {
      getFields().remove(veld);
    }
  }

  @Override
  public String toString() {
    return getProfiel();
  }
}
