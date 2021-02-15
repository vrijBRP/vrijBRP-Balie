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

package nl.procura.gba.web.modules.zaken.verhuizing.overzicht.bewoners.page1;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraagAdres;

public class Page1BewonersAdresContainer extends IndexedContainer {

  public static final String WAARDE = "Waarde";

  public Page1BewonersAdresContainer() {

    addContainerProperty(WAARDE, String.class, "");
    removeAllItems();
  }

  @Override
  public Item addItem(Object itemId) {

    Item item = null;

    if (itemId instanceof BewonersAdres) {

      BewonersAdres adres = (BewonersAdres) itemId;
      item = super.addItem(itemId);

      if (item != null) {
        String tekst = adres.getOmschrijving() + ": " + adres.getAdres().getAdres().getAdres_pc_wpl_gem();
        item.getItemProperty(WAARDE).setValue(tekst);
      }
    }

    return item;
  }

  protected void addAdres(VerhuisAanvraagAdres adres, String omschrijving) {
    addItem(new BewonersAdres(adres, omschrijving));
  }

  public class BewonersAdres {

    private VerhuisAanvraagAdres adres;
    private String               omschrijving = "";

    public BewonersAdres(VerhuisAanvraagAdres adres, String omschrijving) {
      this.adres = adres;
      this.omschrijving = omschrijving;
    }

    public VerhuisAanvraagAdres getAdres() {
      return adres;
    }

    public void setAdres(VerhuisAanvraagAdres adres) {
      this.adres = adres;
    }

    public String getOmschrijving() {
      return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
      this.omschrijving = omschrijving;
    }

  }
}
