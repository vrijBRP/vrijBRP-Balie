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

package nl.procura.gba.web.modules.bs.onderzoek.page30.gesprekwindow;

import static nl.procura.gba.web.modules.bs.onderzoek.page30.gesprekwindow.BronBean.*;
import static nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils.kopieDossierPersoon;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Field;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoekBron;
import nl.procura.gba.web.services.bs.onderzoek.enums.AanschrijvingBronType;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieLijst;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieType;
import nl.procura.vaadin.component.field.ProNativeSelect;

public class SummonsForm extends GbaForm<BronBean> {

  private DossierOnderzoekBron                                                                     bron;
  private List<DossierPersoon>                                                                     betrokkenen;
  private Services                                                                                 services;
  private final nl.procura.gba.web.components.listeners.ValueChangeListener<AanschrijvingBronType> changeListener;
  private RelatieListener                                                                          relatieListener;

  public SummonsForm(
      DossierOnderzoekBron bron,
      List<DossierPersoon> betrokkenen,
      Services services,
      nl.procura.gba.web.components.listeners.ValueChangeListener<AanschrijvingBronType> typeListener,
      RelatieListener relatieListener) {

    this.bron = bron;
    this.betrokkenen = betrokkenen;
    this.services = services;
    this.changeListener = typeListener;
    this.relatieListener = relatieListener;

    setCaption("Aanschrijving");
    setColumnWidths("200px", "");
    setOrder(F_TYPE, F_BETROKKENE, F_GERELATEERDE);

    BronBean bean = new BronBean();
    bean.setType(bron.getSummonsType());
    setBean(bean);
  }

  @Override
  public void afterSetBean() {
    Field typeField = getField(F_TYPE);
    ProNativeSelect betrokkeneField = getField(F_BETROKKENE, ProNativeSelect.class);
    ProNativeSelect gerelateerdeField = getField(F_GERELATEERDE, ProNativeSelect.class);
    gerelateerdeField.setItemCaptionPropertyId(RelationContainer.OMSCHRIJVING);
    betrokkeneField.setItemCaptionPropertyId(BetrokkeneContainer.OMSCHRIJVING);
    BetrokkeneContainer betrokkeneContainer = new BetrokkeneContainer(betrokkenen);

    gerelateerdeField.addListener((ValueChangeListener) event -> {
      Relatie relatie = (Relatie) event.getProperty().getValue();
      if (relatie != null) {
        DossierPersoon persoon = toDossierPersoon(relatie);
        relatieListener.onChange((DossierPersoon) betrokkeneField.getValue(), persoon, relatie.getRelatieType());
      }
      repaint();
    });

    betrokkeneField.addListener((ValueChangeListener) event -> {
      DossierPersoon betrokkene = (DossierPersoon) event.getProperty().getValue();
      RelationContainer container = getContainerByPersoon(betrokkene);
      gerelateerdeField.setVisible(betrokkene != null);
      gerelateerdeField.setContainerDataSource(container);
      repaint();
    });

    typeField.addListener((ValueChangeListener) event -> {
      AanschrijvingBronType value = (AanschrijvingBronType) event.getProperty().getValue();
      boolean isGerelateerde = AanschrijvingBronType.GERELATEERDE == value;
      betrokkeneField.setVisible(isGerelateerde);
      betrokkeneField.setDataSource(betrokkeneContainer);
      betrokkeneField.setValue(isGerelateerde ? betrokkeneContainer.getFirst() : null);
      gerelateerdeField.setVisible(betrokkeneField.getValue() != null);
      gerelateerdeField.setValue(null);
      changeListener.onChange(value);
      repaint();
    });

    betrokkeneField.setVisible(pos(bron.getInstBsnBetrok()));
    gerelateerdeField.setVisible(pos(bron.getInstBsnRel()));

    DossierPersoon betrokkene = betrokkeneContainer.getByBsn(bron.getInstBsnBetrok());
    RelationContainer relationContainer = getContainerByPersoon(betrokkene);

    betrokkeneField.setContainerDataSource(betrokkeneContainer);
    betrokkeneField.setValue(betrokkene);

    gerelateerdeField.setContainerDataSource(relationContainer);
    gerelateerdeField.setValue(relationContainer.getByBsn(bron.getInstBsnRel()));
  }

  private RelationContainer getContainerByPersoon(DossierPersoon betrokkene) {
    if (betrokkene != null) {
      String bsn = betrokkene.getBurgerServiceNummer().getStringValue();
      PersonenWsService service = services.getPersonenWsService();
      BasePLExt pl = service.getPersoonslijst(bsn);
      RelatieLijst relatielijst = service.getRelatieLijst(pl, false);
      return new RelationContainer(relatielijst.getSortedRelaties());
    }
    return new RelationContainer(new ArrayList<>());
  }

  private DossierPersoon toDossierPersoon(Relatie relatie) {
    return kopieDossierPersoon(relatie.getPl(), new DossierPersoon());
  }

  public interface RelatieListener {

    void onChange(DossierPersoon betrokkene, DossierPersoon gerelateerde, RelatieType relatieType);
  }
}
