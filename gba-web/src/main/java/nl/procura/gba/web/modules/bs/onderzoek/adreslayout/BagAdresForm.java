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

package nl.procura.gba.web.modules.bs.onderzoek.adreslayout;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.GEO_SEARCH_DEFAULT;
import static nl.procura.gba.web.services.interfaces.address.AddressSourceType.BAG;
import static nl.procura.gba.web.services.interfaces.address.AddressSourceType.BRP;
import static nl.procura.gba.web.services.interfaces.address.AddressSourceType.UNKNOWN;
import static nl.procura.standard.Globalfunctions.isTru;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.bag.ProcuraInhabitantsAddress;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.gba.web.services.interfaces.address.AddressRequest;
import nl.procura.gba.web.services.interfaces.address.AddressSourceType;

public class BagAdresForm<T> extends AdresForm<T> {

  public BagAdresForm() {
    super();
  }

  protected ProcuraInhabitantsAddress getBrpAddress(Address address) {
    ProcuraInhabitantsAddress out = null;
    if (address != null) {
      if (address.getSourceType().is(BRP)) {
        out = (ProcuraInhabitantsAddress) address;

      } else {
        String pc = address.getPostalCode();
        String hnr = address.getHnr();

        if (StringUtils.isNoneBlank(pc, hnr)) {
          ZoekArgumenten z = new ZoekArgumenten();
          z.setPostcode(pc);
          z.setHuisnummer(hnr);
          z.setHuisletter(address.getHnrL());
          z.setHuisnummertoevoeging(address.getHnrT());
          z.setHuisnummeraanduiding(address.getHnrA());
          z.setDatum_einde("-1");

          List<ProcuraInhabitantsAddress> adr = getServices().getPersonenWsService()
              .getAdres(z, true)
              .getBasisWkWrappers()
              .stream()
              .map(ProcuraInhabitantsAddress::new)
              .collect(Collectors.toList());

          if (adr.size() == 1) {
            out = adr.get(0);
          }
        }
      }
    }
    return out;
  }

  protected Address getBagAddress(Address address) {
    if (getServices().getGeoService().isGeoServiceActive()) {
      String pc = address.getPostalCode();
      String hnr = address.getHnr();
      if (StringUtils.isNoneBlank(pc, hnr)) {
        List<Address> results = getServices().getGeoService()
            .search(new AddressRequest()
                .setPc(pc)
                .setHnr(hnr)
                .setHnrL(address.getHnrL())
                .setHnrT(address.getHnrT()));
        if (results.size() == 1) {
          return results.get(0);
        }
      }
    }
    return null;
  }

  protected Services getServices() {
    return getApplication().getServices();
  }

  protected AddressSourceType getDefaultAddressSource(AddressSourceType sourceType) {
    if (UNKNOWN.is(sourceType)) {
      boolean isDefaultBag = isTru(getServices().getParameterService().getParm(GEO_SEARCH_DEFAULT));
      return isGeoServiceActive() && isDefaultBag ? BAG : BRP;
    }
    return sourceType;
  }

  protected boolean isGeoServiceActive() {
    return getServices().getGeoService().isGeoServiceActive();
  }
}
