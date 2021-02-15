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

package nl.procura.diensten.gba.wk.procura.argumenten;

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.diensten.gba.ple.extensions.formats.Adres;

public class ZoekArgumenten {

  private String  code_gemeentedeel    = "";
  private String  code_lokatie         = "";
  private String  code_object          = "";
  private String  code_straat          = "";
  private String  datum_einde          = "";
  private String  gemeentedeel         = "";
  private String  huisletter           = "";
  private String  huisnummer           = "";
  private String  huisnummeraanduiding = "";
  private String  huisnummertoevoeging = "";
  private String  lokatie              = "";
  private String  postcode             = "";
  private String  straatnaam           = "";
  private String  volgcode_einde       = "";
  private boolean geen_bewoners        = false;
  private boolean extra_pl_gegevens    = true;

  public ZoekArgumenten() {
  }

  public boolean isGevuld() {

    return pos(getCode_lokatie()) || pos(getCode_object()) || pos(getCode_straat()) || fil(getLokatie()) || fil(
        getPostcode()) || fil(getStraatnaam());
  }

  public boolean isGeen_bewoners() {
    return geen_bewoners;
  }

  public void setGeen_bewoners(boolean geen_bewoners) {
    this.geen_bewoners = geen_bewoners;
  }

  public String getHuisletter() {
    return huisletter;
  }

  public void setHuisletter(String huisletter) {
    this.huisletter = huisletter;
  }

  public String getHuisnummeraanduiding() {
    return huisnummeraanduiding;
  }

  public void setHuisnummeraanduiding(String huisnummeraanduiding) {
    this.huisnummeraanduiding = huisnummeraanduiding;
  }

  public String getLokatie() {
    return lokatie;
  }

  public void setLokatie(String lokatie) {
    this.lokatie = lokatie;
  }

  public String getStraatnaam() {
    return straatnaam;
  }

  public void setStraatnaam(String straatnaam) {
    this.straatnaam = straatnaam;
  }

  public String getCode_gemeentedeel() {
    return code_gemeentedeel;
  }

  public void setCode_gemeentedeel(String code_gemeentedeel) {
    this.code_gemeentedeel = code_gemeentedeel;
  }

  public String getCode_lokatie() {
    return code_lokatie;
  }

  public void setCode_lokatie(String code_lokatie) {
    this.code_lokatie = code_lokatie;
  }

  public String getCode_straat() {
    return code_straat;
  }

  public void setCode_straat(String code_straat) {
    this.code_straat = code_straat;
  }

  public String getGemeentedeel() {
    return gemeentedeel;
  }

  public void setGemeentedeel(String gemeentedeel) {
    this.gemeentedeel = gemeentedeel;
  }

  public String getHuisnummer() {
    return huisnummer;
  }

  public void setHuisnummer(String huisnummer) {
    this.huisnummer = huisnummer;
  }

  public String getHuisnummertoevoeging() {
    return huisnummertoevoeging;
  }

  public void setHuisnummertoevoeging(String huisnummertoevoeging) {
    this.huisnummertoevoeging = huisnummertoevoeging;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public boolean isExtra_pl_gegevens() {
    return extra_pl_gegevens;
  }

  public void setExtra_pl_gegevens(boolean extra_pl_gegevens) {
    this.extra_pl_gegevens = extra_pl_gegevens;
  }

  public String getCode_object() {
    return code_object;
  }

  public void setCode_object(String code_object) {
    this.code_object = code_object;
  }

  public String getDatum_einde() {
    return datum_einde;
  }

  public void setDatum_einde(String datum_einde) {
    this.datum_einde = datum_einde;
  }

  public String getVolgcode_einde() {
    return volgcode_einde;
  }

  public void setVolgcode_einde(String volgcode_einde) {
    this.volgcode_einde = volgcode_einde;
  }

  @Override
  public String toString() {
    return "ZoekArgumenten [code_gemeentedeel=" + code_gemeentedeel + ", code_lokatie=" + code_lokatie
        + ", code_object=" + code_object + ", code_straat=" + code_straat + ", datum_einde=" + datum_einde
        + ", gemeentedeel=" + gemeentedeel + ", huisletter=" + huisletter + ", huisnummer=" + huisnummer
        + ", huisnummeraanduiding=" + huisnummeraanduiding + ", huisnummertoevoeging=" + huisnummertoevoeging
        + ", lokatie=" + lokatie + ", postcode=" + postcode + ", straatnaam=" + straatnaam + ", volgcode_einde="
        + volgcode_einde + ", geen_bewoners=" + geen_bewoners + ", extra_pl_gegevens=" + extra_pl_gegevens + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((code_gemeentedeel == null) ? 0 : code_gemeentedeel.hashCode());
    result = prime * result + ((code_lokatie == null) ? 0 : code_lokatie.hashCode());
    result = prime * result + ((code_object == null) ? 0 : code_object.hashCode());
    result = prime * result + ((code_straat == null) ? 0 : code_straat.hashCode());
    result = prime * result + ((datum_einde == null) ? 0 : datum_einde.hashCode());
    result = prime * result + (extra_pl_gegevens ? 1231 : 1237);
    result = prime * result + (geen_bewoners ? 1231 : 1237);
    result = prime * result + ((gemeentedeel == null) ? 0 : gemeentedeel.hashCode());
    result = prime * result + ((huisletter == null) ? 0 : huisletter.hashCode());
    result = prime * result + ((huisnummer == null) ? 0 : huisnummer.hashCode());
    result = prime * result + ((huisnummeraanduiding == null) ? 0 : huisnummeraanduiding.hashCode());
    result = prime * result + ((huisnummertoevoeging == null) ? 0 : huisnummertoevoeging.hashCode());
    result = prime * result + ((lokatie == null) ? 0 : lokatie.hashCode());
    result = prime * result + ((postcode == null) ? 0 : postcode.hashCode());
    result = prime * result + ((straatnaam == null) ? 0 : straatnaam.hashCode());
    result = prime * result + ((volgcode_einde == null) ? 0 : volgcode_einde.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ZoekArgumenten other = (ZoekArgumenten) obj;
    if (code_gemeentedeel == null) {
      if (other.code_gemeentedeel != null) {
        return false;
      }
    } else if (!code_gemeentedeel.equals(other.code_gemeentedeel)) {
      return false;
    }
    if (code_lokatie == null) {
      if (other.code_lokatie != null) {
        return false;
      }
    } else if (!code_lokatie.equals(other.code_lokatie)) {
      return false;
    }
    if (code_object == null) {
      if (other.code_object != null) {
        return false;
      }
    } else if (!code_object.equals(other.code_object)) {
      return false;
    }
    if (code_straat == null) {
      if (other.code_straat != null) {
        return false;
      }
    } else if (!code_straat.equals(other.code_straat)) {
      return false;
    }
    if (datum_einde == null) {
      if (other.datum_einde != null) {
        return false;
      }
    } else if (!datum_einde.equals(other.datum_einde)) {
      return false;
    }
    if (extra_pl_gegevens != other.extra_pl_gegevens) {
      return false;
    }
    if (geen_bewoners != other.geen_bewoners) {
      return false;
    }
    if (gemeentedeel == null) {
      if (other.gemeentedeel != null) {
        return false;
      }
    } else if (!gemeentedeel.equals(other.gemeentedeel)) {
      return false;
    }
    if (huisletter == null) {
      if (other.huisletter != null) {
        return false;
      }
    } else if (!huisletter.equals(other.huisletter)) {
      return false;
    }
    if (huisnummer == null) {
      if (other.huisnummer != null) {
        return false;
      }
    } else if (!huisnummer.equals(other.huisnummer)) {
      return false;
    }
    if (huisnummeraanduiding == null) {
      if (other.huisnummeraanduiding != null) {
        return false;
      }
    } else if (!huisnummeraanduiding.equals(other.huisnummeraanduiding)) {
      return false;
    }
    if (huisnummertoevoeging == null) {
      if (other.huisnummertoevoeging != null) {
        return false;
      }
    } else if (!huisnummertoevoeging.equals(other.huisnummertoevoeging)) {
      return false;
    }
    if (lokatie == null) {
      if (other.lokatie != null) {
        return false;
      }
    } else if (!lokatie.equals(other.lokatie)) {
      return false;
    }
    if (postcode == null) {
      if (other.postcode != null) {
        return false;
      }
    } else if (!postcode.equals(other.postcode)) {
      return false;
    }
    if (straatnaam == null) {
      if (other.straatnaam != null) {
        return false;
      }
    } else if (!straatnaam.equals(other.straatnaam)) {
      return false;
    }
    if (volgcode_einde == null) {
      return other.volgcode_einde == null;
    } else
      return volgcode_einde.equals(other.volgcode_einde);

  }

  public static ZoekArgumenten of(Adres adres) {
    ZoekArgumenten z = new ZoekArgumenten();
    if (adres.getStraat() != null) {
      z.setStraatnaam(adres.getStraat().getValue().getVal());
    }

    z.setHuisnummer(adres.getHuisnummer().getValue().getVal());
    z.setHuisletter(adres.getHuisletter().getValue().getVal());
    z.setHuisnummertoevoeging(adres.getHuisnummertoev().getValue().getVal());
    z.setHuisnummeraanduiding(adres.getHuisnummeraand().getValue().getVal());
    z.setPostcode(adres.getPostcode().getValue().getVal());

    if (fil(adres.getGemeentedeel().getValue().getVal())) {
      z.setGemeentedeel(adres.getGemeentedeel().getValue().getVal());
    }

    if (fil(adres.getLocatie().getValue().getVal())) {
      z.setLokatie(adres.getLocatie().getValue().getVal());
    }
    return z;
  }
}
