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

package nl.procura.gba.web.modules.zaken.verhuizing;

import static nl.procura.standard.Globalfunctions.aval;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.diensten.gba.wk.baseWK.BaseWKPerson;
import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.gba.web.services.beheer.bag.ProcuraInhabitantsAddress;
import nl.procura.gba.web.services.beheer.bag.ProcuraPersonListAddress;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.interfaces.address.Address;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerhuisAdres {

  private Address              address     = null;
  private AdresType            addressType = AdresType.BINNENGEMEENTELIJK;
  private List<VerhuisRelatie> persons     = new ArrayList<>();

  public VerhuisAdres() {
  }

  public VerhuisAdres(BasePLExt pl, Gebruiker user) {
    setAddress(new ProcuraPersonListAddress(pl));
    setAddressType(getAddressType(user));
  }

  public VerhuisAdres(BaseWKExt wk) {
    setAddress(new ProcuraInhabitantsAddress(wk));
    setAddressType(AdresType.BINNENGEMEENTELIJK);
  }

  public VerhuisAdres(Address address, Gebruiker user) {
    setAddress(address);
    setAddressType(getAddressType(user));
  }

  public void addVerhuisRelatie(VerhuisRelatie vr) {
    if (!getPersons().contains(vr)) {
      getPersons().add(vr);
    }
  }

  public List<BaseWKPerson> getCurrentPersons() {
    List<BaseWKPerson> l = new ArrayList<>();
    if (address instanceof ProcuraInhabitantsAddress) {
      for (BaseWKPerson p : ((ProcuraInhabitantsAddress) address).getPersons()) {
        if (p.isCurrentResident()) {
          l.add(p);
        }
      }
    }

    return l;
  }

  public String getAddressLabel() {
    Adresformats af = new Adresformats();
    af.setValues(address.getStreet(),
        address.getHnr(),
        address.getHnrL(),
        address.getHnrT(),
        address.getHnrA(),
        "",
        address.getPostalCode(),
        "",
        address.getResidenceName(),
        address.getMunicipalityName(),
        "",
        address.getCountryName(),
        "",
        address.getForeignAddress1(),
        address.getForeignAddress2(),
        address.getForeignAddress3());

    return af.getAdres_pc_wpl();
  }

  public boolean isSuitableForLiving() {
    return address.isSuitableForLiving();
  }

  @Override
  public String toString() {
    return getAddressLabel();
  }

  @Override
  public boolean equals(Object o) {

    if (o == this) {
      return true;
    }

    if (!(o instanceof VerhuisAdres)) {
      return false;
    }

    VerhuisAdres that = (VerhuisAdres) o;
    return new EqualsBuilder()
        .append(this.getAddressLabel(), that.getAddressLabel())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(getAddressLabel())
        .toHashCode();
  }

  private AdresType getAddressType(Gebruiker gebruiker) {
    return (aval(gebruiker.getGemeenteCode()) == aval(address.getMunicipalityCode())) ? AdresType.BINNENGEMEENTELIJK
        : AdresType.BUITENGEMEENTELIJK;
  }
}
