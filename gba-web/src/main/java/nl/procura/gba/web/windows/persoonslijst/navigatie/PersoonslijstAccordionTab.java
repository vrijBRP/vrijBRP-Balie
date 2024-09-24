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

package nl.procura.gba.web.windows.persoonslijst.navigatie;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.CONTACT;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.GEZAG;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.HUW_GPS;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.INSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.KIESR;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.KINDEREN;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.KLADBLOK;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.LOK_AF_IND;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.NATIO;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_1;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_2;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OVERL;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.PERSOON;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.TIJD_VBA;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.VB;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.VBTITEL;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.EmbeddedResource;
import nl.procura.gba.web.modules.persoonslijst.afnIndicaties.ModuleAfnIndicaties;
import nl.procura.gba.web.modules.persoonslijst.contact.ModuleContact;
import nl.procura.gba.web.modules.persoonslijst.gezag.ModuleGezag;
import nl.procura.gba.web.modules.persoonslijst.huwelijk.ModuleHuwelijk;
import nl.procura.gba.web.modules.persoonslijst.inschrijving.ModuleInschrijving;
import nl.procura.gba.web.modules.persoonslijst.kiesrecht.ModuleKiesrecht;
import nl.procura.gba.web.modules.persoonslijst.kind.ModuleKind;
import nl.procura.gba.web.modules.persoonslijst.kladblok.ModuleKladblok;
import nl.procura.gba.web.modules.persoonslijst.lokAfn.ModuleLokAfn;
import nl.procura.gba.web.modules.persoonslijst.nationaliteit.ModuleNationaliteit;
import nl.procura.gba.web.modules.persoonslijst.onderzoek.ModuleOnderzoek;
import nl.procura.gba.web.modules.persoonslijst.ouder.ModuleOuder1;
import nl.procura.gba.web.modules.persoonslijst.ouder.ModuleOuder2;
import nl.procura.gba.web.modules.persoonslijst.overlijden.ModuleOverlijden;
import nl.procura.gba.web.modules.persoonslijst.persoon.ModulePersoon;
import nl.procura.gba.web.modules.persoonslijst.reisdocument.ModuleReisdocument;
import nl.procura.gba.web.modules.persoonslijst.tvba.ModuleTvba;
import nl.procura.gba.web.modules.persoonslijst.verblijfplaats.ModuleVerblijfplaats;
import nl.procura.gba.web.modules.persoonslijst.verblijfstitel.ModuleVerblijfstitel;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.accordion.AccordionLink;
import nl.procura.vaadin.component.window.Message;

public class PersoonslijstAccordionTab extends PlAccordionTab {

  private static final long serialVersionUID = 3625454361060373084L;

  public PersoonslijstAccordionTab(GbaApplication application) {

    super("Persoonslijst", application);

    BasePLExt pl = application.getServices().getPersonenWsService().getHuidige();

    if (pl != null) {

      // Als er sprake is van GBA-V plus dan afnemers zoeken in de GBA-V
      boolean heeftAfnemerIndicaties = application.getServices().getPersonenWsService().heeftGbavPlus();

      addLink(ModulePersoon.class, getSoort(pl, PERSOON));
      addLink(ModuleOuder1.class, getSoort(pl, OUDER_1));
      addLink(ModuleOuder2.class, getSoort(pl, OUDER_2));
      addLink(ModuleNationaliteit.class, getSoort(pl, NATIO));
      addLink(ModuleHuwelijk.class, getSoort(pl, HUW_GPS));
      addLink(ModuleOverlijden.class, getSoort(pl, OVERL));
      addLink(ModuleInschrijving.class, getSoort(pl, INSCHR));
      addLink(ModuleVerblijfplaats.class, getSoort(pl, VB));
      addLink(ModuleKind.class, getSoort(pl, KINDEREN));
      addLink(ModuleVerblijfstitel.class, getSoort(pl, VBTITEL));
      addLink(ModuleGezag.class, getSoort(pl, GEZAG));
      addLink(ModuleReisdocument.class, getSoort(pl, REISDOC));
      addLink(ModuleKiesrecht.class, getSoort(pl, KIESR));
      addLink(ModuleTvba.class, getSoort(pl, TIJD_VBA));
      addLink(ModuleContact.class, getSoort(pl, CONTACT));
      addLink(ModuleAfnIndicaties.class, heeftAfnemerIndicaties, false);
      addLink(ModuleKladblok.class, getSoort(pl, KLADBLOK));
      addLink(ModuleLokAfn.class, getSoort(pl, LOK_AF_IND));
      addLink(ModuleOnderzoek.class, pl.getPersoon().getOnderzoek().getCategoriesInOnderzoek().size() > 0, false);
    }
  }

  @Override
  public void onLinkSelect(AccordionLink link) {

    if (link.getButton().getStyleName().contains("off")) {
      new Message(getWindow(), "Deze categorie komt niet voor op de persoonlijst.", Message.TYPE_WARNING_MESSAGE);
    } else {
      super.onLinkSelect(link);
    }
  }

  private AccordionLink addLink(Class<?> c, BasePLCat soort) {

    boolean filled = soort.hasSets();
    boolean mutatie = false;

    for (BasePLSet set : soort.getSets()) {
      for (BasePLRec record : set.getRecs()) {
        if (record.isStatus(GBARecStatus.MUTATION)) {
          mutatie = true;
          break;
        }
      }
    }

    return addLink(c, filled, mutatie);
  }

  private AccordionLink addLink(Class<?> c, boolean filled, boolean mutatie) {

    AccordionLink link = super.addLink(c);

    EmbeddedResource checkbox = new EmbeddedResource(GbaWebTheme.ICOON_16.CHECKBOX_NO);

    if (filled) {
      checkbox = new EmbeddedResource(GbaWebTheme.ICOON_16.CHECKBOX_YES);
      link.getButton().addStyleName("on");
    } else {
      link.getButton().addStyleName("off");
    }

    checkbox.addStyleName("zaken_controle");

    if (mutatie) {
      Component mutatieLabel = getMutatieLabel();
      link.addComponent(mutatieLabel);
      link.setComponentAlignment(mutatieLabel, Alignment.MIDDLE_LEFT);
    }

    link.addComponent(checkbox);
    link.setComponentAlignment(checkbox, Alignment.MIDDLE_LEFT);

    return link;
  }

  private Component getMutatieLabel() {
    Label label = new Label("M");
    label.setDescription("Deze categorie staat in mutatie");
    label.setWidth("16px");
    label.setHeight("16px");
    label.setStyleName("mutatie-list");
    return label;
  }

  private BasePLCat getSoort(BasePLExt pl, GBACat cat) {
    return pl.getCat(cat);
  }
}
