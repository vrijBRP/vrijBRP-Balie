/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.services.beheer.kassa;

import static nl.procura.gba.common.MiscUtils.copy;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.jpa.personen.dao.GenericDao;
import nl.procura.gba.jpa.personen.dao.KassaDao;
import nl.procura.gba.jpa.personen.db.Document;
import nl.procura.gba.jpa.personen.db.Kassa;
import nl.procura.gba.jpa.personen.db.Reisdoc;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.kassa.gkas.GKasFile;
import nl.procura.gba.web.services.beheer.kassa.key2betalen.Key2BetalenFile;
import nl.procura.gba.web.services.beheer.kassa.piv4all.PIV4AllFile;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakNumbers;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.reisdocumenten.SoortReisdocument;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagSoort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KassaService extends AbstractService implements ZaakNumbers {

  private final List<KassaProductAanvraag> productenInWinkelwagen = new ArrayList<>();

  public KassaService() {
    super("Kassa");
  }

  public KassaProduct createKassaProduct(SoortReisdocument reisdocument, DocumentRecord document,
      RijbewijsAanvraagSoort rijbewijs, KassaType kassaType) {

    boolean isReisdoc = (reisdocument != null);
    boolean isDocument = (document != null);
    boolean isRijbewijs = (rijbewijs != null);

    SoortReisdocument reisdocumentImpl = copy(GenericDao.find(Reisdoc.class, 0L), SoortReisdocument.class);
    DocumentRecord documentImpl = copy(GenericDao.find(Document.class, 0L), DocumentRecord.class);

    KassaProduct nieuwKassaProduct = new KassaProduct();
    nieuwKassaProduct.setKassa("++");
    nieuwKassaProduct.setKassaType(kassaType);
    nieuwKassaProduct.setKassaDocument(isDocument ? document : documentImpl);
    nieuwKassaProduct.setKassaReisdocument(isReisdoc ? reisdocument : reisdocumentImpl);
    nieuwKassaProduct.setKassaRijbewijs(isRijbewijs ? rijbewijs : RijbewijsAanvraagSoort.ONBEKEND);

    if (!isKassaProductInDatabase(nieuwKassaProduct)) {
      return nieuwKassaProduct;
    }

    return null;
  }

  @Override
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return getProductenInWinkelwagen().size();
  }

  public KassaProduct getKassaProduct(KassaProduct kassaProduct) {

    KassaProduct out = kassaProduct;

    if (kassaProduct.getKassaType() != KassaType.ONBEKEND) {

      List<Kassa> kps = new ArrayList<>();
      BigDecimal type = toBigDecimal(kassaProduct.getKassaType().getNr());

      switch (kassaProduct.getKassaType()) {

        case UITTREKSEL:
          kps.addAll(
              KassaDao.findKassaByTypeAndDocument(type, kassaProduct.getKassaDocument().getCDocument()));
          break;

        case REISDOCUMENT:
          kps.addAll(KassaDao.findKassaByTypeAndReisdocument(type,
              kassaProduct.getKassaReisdocument().getCReisdoc()));
          break;

        case RIJBEWIJS:
          kps.addAll(KassaDao.findKassaByTypeAndRijbewijs(type, kassaProduct.getKassaRijbewijs().getCode()));
          break;

        case ANDERS:
          kps.addAll(KassaDao.findKassaByTypeAndAnders(type, kassaProduct.getAnders(),
              kassaProduct.getProductgroep()));
          break;

        default: // Alle overige producten
          kps.addAll(KassaDao.findKassaByType(type));
          break;
      }

      if (kps.size() > 0) {
        out = copy(kps.get(0), KassaProduct.class);
      }
    }

    return vulGegevensAan(out);
  }

  @ThrowException("Fout bij het zoeken van kassaproducten")
  public List<KassaProduct> getKassaProducten() {

    List<KassaProduct> kassaObjecten = new ArrayList<>();
    for (Kassa kassa : KassaDao.findKassaProducten()) {
      kassaObjecten.add(vulGegevensAan(copy(kassa, KassaProduct.class)));
    }

    return kassaObjecten;
  }

  @Override
  public List<Zaak> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return new ArrayList<>();
  }

  /**
   * Geef de inhoud van de winkelwagen terug
   */
  public List<KassaProductAanvraag> getProductenInWinkelwagen() {
    productenInWinkelwagen.sort(new KassaProductComparator(false));
    return productenInWinkelwagen;
  }

  /**
   * Ververs de winkelwagen
   */
  public List<KassaProductAanvraag> getVerversdeWinkelwagen() {
    List<KassaProductAanvraag> list = getProductenInWinkelwagen();
    for (KassaProductAanvraag p : list) {
      for (KassaProduct kassaProduct : getKassaProducten(Collections.singletonList(p.getKassaProduct()))) {
        p.setKassaProduct(kassaProduct);
      }
    }

    return list;
  }

  @Override
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<String> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return new ArrayList<>();
  }

  public boolean isKassaProductInDatabase(KassaProduct kassaProduct) {
    return getKassaProduct(kassaProduct).isStored();
  }

  /**
   * Koppel 2 kassaproducten aan elkaar
   */
  @ThrowException("Fout bij het koppelen van record")
  @Transactional
  public void koppelActie(KassaProduct kassaProduct, KassaProduct koppelProduct) {

    if ((kassaProduct == null) || (koppelProduct == null)) {
      return;
    }

    if (kassaProduct.isGekoppeld(koppelProduct)) {
      kassaProduct.ontKoppel(koppelProduct);
    } else {
      kassaProduct.koppel(koppelProduct);
    }

    saveEntity(kassaProduct);
    saveEntity(koppelProduct);
  }

  /**
   * Maak de winkelwagen leeg
   */
  public void leegWinkelwagen() {
    getProductenInWinkelwagen().clear();
    callListeners(ServiceEvent.CHANGE);
  }

  @ThrowException("Fout bij het opslaan van kassaproduct")
  @Transactional
  public void save(KassaProduct kassaProduct) {
    saveEntity(kassaProduct);
  }

  public void addToWinkelwagen(List<KassaProduct> producten) {
    List<KassaProduct> nieuweProducten = new ArrayList<>(producten);
    for (KassaProduct kassa : getKassaProducten()) {
      if (kassa.isKassaBundel()) {
        if (kassa.heeftAlleGekoppeldeProducten(producten)) {
          // Verwijder de producten die voorkomen in de bundel
          nieuweProducten.removeAll(kassa.getGekoppeldeProducten());
          // Voeg de nieuwe bundel toe
          nieuweProducten.add(kassa);
        }
      }
    }

    for (KassaProduct nieuwProduct : nieuweProducten) {
      if (nieuwProduct.getKassaType() != KassaType.ONBEKEND) {
        BasePLExt pl = getServices().getPersonenWsService().getHuidige();
        UsrFieldValue gebruiker = new UsrFieldValue(getServices().getGebruiker());
        KassaProductAanvraag aanvraag = new KassaProductAanvraag(nieuwProduct, pl, gebruiker);
        getProductenInWinkelwagen().add(aanvraag);
        callListeners(ServiceEvent.CHANGE);
      }
    }
  }

  public void addToWinkelwagen(Zaak zaak) {
    addToWinkelwagen(zaak, true);
  }

  public void addToWinkelwagen(Zaak zaak, boolean magDubbelVoorkomen) {
    if (getServices().isType(Services.TYPE.PROWEB)) {
      List<KassaProduct> producten = KassaProductConverter.getKassaProductAanvragen(this, zaak);
      if (magDubbelVoorkomen) {
        addToWinkelwagen(getKassaProducten(producten));
      } else {
        addToWinkelwagen(getOntbrekendeProducten(producten));
      }
    }
  }

  public boolean verstuur() {
    KassaApplicationType kassaLeverancier = KassaApplicationType.get(
        getParm(ParameterConstant.KASSA_TYPE, true));

    KassaParameters parameters = new KassaParameters(getServices());
    List<KassaProductAanvraag> aanvragen = getServices().getKassaService().getProductenInWinkelwagen();

    switch (kassaLeverancier) {
      case GKAS:
        new KassaSender(parameters).send(GKasFile.of(parameters, aanvragen));
        break;

      case KEY2BETALEN:
        new KassaSender(parameters).send(Key2BetalenFile.of(parameters, aanvragen));
        break;

      case PIV4ALL:
        new KassaSender(parameters).send(PIV4AllFile.of(parameters, aanvragen));
        break;

      default:
        return false;
    }

    return true;
  }

  /**
   * Vervang de kassaproducten met de bundel
   */
  public void vervangBundel(KassaProduct kassaProduct) {
    addToWinkelwagen(kassaProduct.getGekoppeldeProducten());
    for (KassaProduct gekoppeldProduct : kassaProduct.getGekoppeldeProducten()) {
      verwijderEersteProductUitWinkelwagen(gekoppeldProduct);
    }
    callListeners(ServiceEvent.CHANGE);
  }

  @ThrowException("Fout bij het verwijderen van kassaproduct")
  @Transactional
  public void delete(KassaProduct kassaProduct) {
    removeEntity(kassaProduct);
  }

  public void deleteProductUitWinkelwagen(KassaProductAanvraag product) {
    if (isToegevoegdAanWinkelwagen(product)) {
      getProductenInWinkelwagen().remove(product);
    }

    callListeners(ServiceEvent.CHANGE);
  }

  /**
   * Zoek de mogelijke bundels in de kassaproducten
   */
  public Set<KassaProduct> findBundels() {
    List<KassaProduct> producten = new ArrayList<>();
    for (KassaProductAanvraag aanvraag : productenInWinkelwagen) {
      producten.add(aanvraag.getKassaProduct());
    }

    Set<KassaProduct> bundels = new LinkedHashSet();
    for (Set<KassaProduct> set : KassaCombinations.getCombinationsFor(producten)) {
      for (KassaProduct kassa : getKassaProducten()) {
        if (kassa.isKassaBundel() && kassa.heeftAlleGekoppeldeProducten(new ArrayList(set))) {
          bundels.add(kassa);
        }
      }
    }

    return bundels;
  }

  /**
   * Haal kassaproduct uit database op
   */
  private List<KassaProduct> getKassaProducten(List<KassaProduct> kassaProducten) {

    List<KassaProduct> list = new ArrayList<>();

    for (KassaProduct kassaProduct : kassaProducten) {
      kassaProduct = getKassaProduct(kassaProduct);
      if (!kassaProduct.isStored()) {
        log.error("Kassa product komt niet voor: " + kassaProduct.getKassaType());
        kassaProduct.setKassa("++");
        getServices().getKassaService().save(kassaProduct);
      }

      list.add(kassaProduct);
    }

    return list;
  }

  /**
   * Is de aanvraag al in de kassa
   */
  private boolean isToegevoegdAanWinkelwagen(KassaProductAanvraag product) {
    return getProductenInWinkelwagen().contains(product);
  }

  /**
   * Is het product van deze persoon al in de kassa
   */
  private List<KassaProduct> getOntbrekendeProducten(List<KassaProduct> producten) {
    List<KassaProduct> nieuweProducten = new ArrayList<>();
    PROD_LIST: for (KassaProduct nieuwProdukt : producten) {
      for (KassaProductAanvraag aanvraag : getProductenInWinkelwagen()) {
        if (aanvraag.getKassaProduct().getKassaType() == nieuwProdukt.getKassaType()) {
          BasePLExt pl = getServices().getPersonenWsService().getHuidige();
          if (pl.is(aanvraag.getPl())) {
            continue PROD_LIST;
          }
        }
      }
      nieuweProducten.add(nieuwProdukt);
    }
    return nieuweProducten;
  }

  private void verwijderEersteProductUitWinkelwagen(KassaProduct kassaProduct) {
    for (KassaProductAanvraag aanvraag : new ArrayList<>(productenInWinkelwagen)) {
      if (kassaProduct.equals(aanvraag.getKassaProduct())) {
        productenInWinkelwagen.remove(aanvraag);
        return;
      }
    }
  }

  /**
   * Afgeleide gegevens aanvullen
   */
  private KassaProduct vulGegevensAan(KassaProduct kassa) {

    if (kassa.getDocument() != null) {
      kassa.setKassaDocument(copy(kassa.getDocument(), DocumentRecord.class));
    }

    if (kassa.getReisdoc() != null) {
      kassa.setKassaReisdocument(copy(kassa.getReisdoc(), SoortReisdocument.class));
    }

    if (kassa.isKassaBundel()) {
      for (KassaProduct gp : kassa.getGekoppeldeProducten()) {
        vulGegevensAan(gp);
      }
    }

    return kassa;
  }

  public static class KassaProductComparator implements Comparator<KassaProductAanvraag> {

    private final boolean newFirst;

    public KassaProductComparator(boolean newFirst) {
      this.newFirst = newFirst;
    }

    @Override
    public int compare(KassaProductAanvraag o1, KassaProductAanvraag o2) {
      if (newFirst) {
        return o2.getTijdstip().compareTo(o1.getTijdstip());
      }
      return o1.getTijdstip().compareTo(o2.getTijdstip());
    }
  }
}
