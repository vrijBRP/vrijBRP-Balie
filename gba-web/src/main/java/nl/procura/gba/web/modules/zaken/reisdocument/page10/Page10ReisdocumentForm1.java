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

package nl.procura.gba.web.modules.zaken.reisdocument.page10;

import static nl.procura.gba.web.modules.zaken.reisdocument.page10.Page10ReisdocumentBean1.*;
import static nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType.*;
import static nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentUtils.isAanvraagbaar;

import java.util.List;

import com.vaadin.ui.Field;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.containers.GbaContainer;
import nl.procura.gba.web.components.containers.GebruikerLocatieContainer;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.zaken.common.ToelichtingButton;
import nl.procura.gba.web.modules.zaken.common.ToelichtingWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.beheer.locatie.LocatieService;
import nl.procura.gba.web.services.beheer.locatie.LocatieType;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentService;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentUtils;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page10ReisdocumentForm1 extends GbaForm<Page10ReisdocumentBean1> {

  private final BasePLExt pl;
  private final Services  db;
  private boolean         jeugdTarief = false;

  public Page10ReisdocumentForm1(BasePLExt pl, ReisdocumentAanvraag zaak, Services db) {
    this.pl = pl;
    this.db = db;

    setCaption("Bijzonderheden");
    setOrder(AANVRAAGNUMMER, REISDOCUMENT, LENGTE, LENGTENVT, SPOED, JEUGDTARIEF, REDENNIETAANWEZIG, AFHAAL_LOCATIE);
    setReadThrough(true);
    setReadonlyAsText(false);
    setColumnWidths(WIDTH_130, "");

    Page10ReisdocumentBean1 bean = new Page10ReisdocumentBean1();
    bean.setAanvraagnr(zaak.getAanvraagnummer().getFormatNummer());
    bean.setSpoed(zaak.getSpoed());

    if (zaak.getLengte().intValue() > 0) {
      bean.setLengte(zaak.getLengte().toString());
    }

    bean.setAfhaalLocatie(zaak.getLocatieAfhaal());
    bean.setRedenNietAanwezig(zaak.getRedenAfwezig());

    setBean(bean);
    getReisdocumentField().setContainerDataSource(new ReisdocumentContainer(zaak.getReisdocumentType()));
    getReisdocumentField().setValue(zaak.getReisdocumentType());
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {

    if (property.is(JEUGDTARIEF)) {

      column.addComponent(new ToelichtingButton() {

        @Override
        public void buttonClick(ClickEvent event) {

          String caption;
          StringBuilder msg = new StringBuilder();

          if (isNieuweReisdocumentenRegelsVanToepassing()) {
            caption = "Voorwaarden reisdocument met jeugdtarief";

            msg.append("<ul>");
            msg.append(
                "<li>Aanvrager vraagt een Nationaal paspoort, faciliteitenpaspoort, Nederlandse identiteitskaart"
                    + ", zakenpaspoort of een tweede paspoort aan;</li><br/>");
            msg.append(
                "<li>Aanvrager is op de dag van de aanvraag jonger dan 18 jaar, waarbij als de dag of de dag en de maand "
                    + "van geboorte onbekend zijn de laatste dag van de onbekende periode geldt.</li>");
            msg.append("</ul>");

          } else {
            caption = "Voorwaarden Nederlandse identiteitskaart met jeugdtarief";

            msg.append("<ul>");
            msg.append(
                "<li>Aanvrager moet als ingezetene in de gemeente van aanvraag zijn ingeschreven in ");
            msg.append("de basisregistratie personen (BRP);</li><br/>");
            msg.append("<li>Aanvrager moet jonger zijn dan 14 jaar.</li>");
            msg.append("</ul>");
          }

          getWindow().addWindow(new ToelichtingWindow(caption, msg.toString()));
        }
      });
    }
  }

  public void checkJeugdTarief(ReisdocumentType type) {

    setJeugdTarief(false);

    if (isNieuweReisdocumentenRegelsVanToepassing()) {

      boolean isNieuweRegels = ReisdocumentUtils.isNieuweReisdocumentenRegelsVanToepassing(getReisdocumenten());
      boolean isJeugdTariefOud = type.isDocument(EERSTE_NATIONAAL_PASPOORT, FACILITEITEN_PASPOORT,
          NEDERLANDSE_IDENTITEITSKAART, EERSTE_ZAKENPASPOORT);
      boolean isJeugdTariefNieuwe = type.isDocument(TWEEDE_NATIONAAL_PASPOORT, TWEEDE_ZAKENPASPOORT);

      // Jeugdtarief bij alle 'oude' reisdocumenten of als de nieuwe regels gelden ook bij de nieuwe.

      if (isJeugdTariefOud || (isNieuweRegels && isJeugdTariefNieuwe)) {

        if (ReisdocumentUtils.getReisdocumentenLeeftijd(pl) < 18) {

          getField(JEUGDTARIEF).setWidth("20px");
          getBean().setJeugdtarief("Ja");
          setJeugdTarief(true);
        } else {
          getField(JEUGDTARIEF).setWidth("30px");
          getBean().setJeugdtarief("Nee");
        }
      } else {
        getBean().setJeugdtarief("Niet van toepassing op dit reisdocument");
        getField(JEUGDTARIEF).setWidth("230px");
      }
    } else {

      if (type == NEDERLANDSE_IDENTITEITSKAART) {

        if (ReisdocumentUtils.isJeugdTarief(pl, type)) {

          getField(JEUGDTARIEF).setWidth("20px");
          getBean().setJeugdtarief("Ja");
          setJeugdTarief(true);

        } else {

          getField(JEUGDTARIEF).setWidth("30px");
          getBean().setJeugdtarief("Nee");
        }
      } else {
        getBean().setJeugdtarief("Niet van toepassing op dit reisdocument");
        getField(JEUGDTARIEF).setWidth("230px");
      }
    }
  }

  public GbaNativeSelect getReisdocumentField() {
    return (GbaNativeSelect) getField(REISDOCUMENT);
  }

  public ReisdocumentType getReisdocumentType() {

    Object t = getReisdocumentField().getValue();

    if (t != null) {
      return (ReisdocumentType) t;
    }

    return ONBEKEND;
  }

  public boolean isJeugdTarief() {
    return jeugdTarief;
  }

  public void setJeugdTarief(boolean jeugdTarief) {
    this.jeugdTarief = jeugdTarief;
  }

  /**
   * Vul de afhaallocaties in
   */
  public void setAfhaalLocaties(GbaApplication application) {

    LocatieService db = application.getServices().getLocatieService();
    Gebruiker gebruiker = application.getServices().getGebruiker();
    List<Locatie> locaties = db.getGekoppeldeLocaties(gebruiker, LocatieType.AFHAAL_LOCATIE);

    GbaNativeSelect locatie = getField(Page10ReisdocumentBean1.AFHAAL_LOCATIE, GbaNativeSelect.class);
    locatie.setVisible(locaties.size() > 0);
    locatie.setContainerDataSource(new GebruikerLocatieContainer(locaties));
    repaint();
  }

  private ReisdocumentService getReisdocumenten() {
    return getApplication().getServices().getReisdocumentService();
  }

  private boolean isNieuweReisdocumentenRegelsVanToepassing() {
    return ReisdocumentUtils.isNieuweReisdocumentenRegelsVanToepassing(getReisdocumenten());
  }

  class ReisdocumentContainer extends GbaContainer {

    public ReisdocumentContainer(ReisdocumentType reisdocumentType) {

      // Add previous selected document as a possible choice
      if (!ONBEKEND.isDocument(reisdocumentType)) {
        addItem(reisdocumentType);
      }

      for (ReisdocumentType type : ReisdocumentType.values()) {
        if (!ONBEKEND.isDocument(type) && isAanvraagbaar(pl, type, db)) {
          addItem(type);
        }
      }
    }
  }
}
