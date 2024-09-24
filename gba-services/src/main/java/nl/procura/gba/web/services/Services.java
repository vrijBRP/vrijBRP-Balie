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

package nl.procura.gba.web.services;

import static nl.procura.standard.Globalfunctions.isTru;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;

import nl.procura.gba.web.services.aop.GbaGuiceInjector;
import nl.procura.gba.web.services.applicatie.MemoryStorageService;
import nl.procura.gba.web.services.applicatie.meldingen.MeldingService;
import nl.procura.gba.web.services.applicatie.onderhoud.OnderhoudService;
import nl.procura.gba.web.services.beheer.bag.BagService;
import nl.procura.gba.web.services.beheer.bsm.BsmService;
import nl.procura.gba.web.services.beheer.email.EmailService;
import nl.procura.gba.web.services.beheer.fileimport.FileImportService;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;
import nl.procura.gba.web.services.beheer.gebruiker.info.GebruikerInfoService;
import nl.procura.gba.web.services.beheer.inwonerapp.InwonerAppService;
import nl.procura.gba.web.services.beheer.kassa.KassaService;
import nl.procura.gba.web.services.beheer.link.LinkService;
import nl.procura.gba.web.services.beheer.locatie.LocatieService;
import nl.procura.gba.web.services.beheer.log.LogService;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.gba.web.services.beheer.personmutations.PersonListMutationsService;
import nl.procura.gba.web.services.beheer.persoonhistorie.PersoonHistorieService;
import nl.procura.gba.web.services.beheer.profiel.ProfielExtrasService;
import nl.procura.gba.web.services.beheer.profiel.ProfielService;
import nl.procura.gba.web.services.beheer.raas.RaasService;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxService;
import nl.procura.gba.web.services.beheer.sms.SmsService;
import nl.procura.gba.web.services.beheer.verkiezing.KiezersregisterService;
import nl.procura.gba.web.services.bs.algemeen.DossierService;
import nl.procura.gba.web.services.bs.algemeen.akte.AkteService;
import nl.procura.gba.web.services.bs.erkenning.ErkenningService;
import nl.procura.gba.web.services.bs.geboorte.GeboorteService;
import nl.procura.gba.web.services.bs.huwelijk.HuwelijkService;
import nl.procura.gba.web.services.bs.levenloos.LevenloosService;
import nl.procura.gba.web.services.bs.lv.afstamming.LvService;
import nl.procura.gba.web.services.bs.naamskeuze.NaamskeuzeService;
import nl.procura.gba.web.services.bs.naturalisatie.NaturalisatieService;
import nl.procura.gba.web.services.bs.omzetting.OmzettingService;
import nl.procura.gba.web.services.bs.onderzoek.OnderzoekService;
import nl.procura.gba.web.services.bs.ontbinding.OntbindingService;
import nl.procura.gba.web.services.bs.overlijden.buitenland.OverlijdenBuitenlandService;
import nl.procura.gba.web.services.bs.overlijden.gemeente.OverlijdenGemeenteService;
import nl.procura.gba.web.services.bs.overlijden.lijkvinding.LijkvindingService;
import nl.procura.gba.web.services.bs.registration.RegistrationService;
import nl.procura.gba.web.services.bs.registration.RelationService;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService;
import nl.procura.gba.web.services.gba.basistabellen.belanghebbende.BelanghebbendeService;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.GemeenteService;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagService;
import nl.procura.gba.web.services.gba.sql.ProbevSqlService;
import nl.procura.gba.web.services.gba.tabellen.TabellenService;
import nl.procura.gba.web.services.gba.verificatievraag.VerificatievraagService;
import nl.procura.gba.web.services.zaken.algemeen.ZakenService;
import nl.procura.gba.web.services.zaken.algemeen.ZakenVerwijderService;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.AantekeningService;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutService;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSService;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatieService;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;
import nl.procura.gba.web.services.zaken.algemeen.tasks.TaskService;
import nl.procura.gba.web.services.zaken.algemeen.zaakconfiguraties.ZaakConfiguratieService;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatieService;
import nl.procura.gba.web.services.zaken.algemeen.zkndms.ZaakDmsService;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.correspondentie.CorrespondentieService;
import nl.procura.gba.web.services.zaken.curatele.CurateleService;
import nl.procura.gba.web.services.zaken.documenten.DocumentService;
import nl.procura.gba.web.services.zaken.documenten.KoppelEnumeratieService;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZakenService;
import nl.procura.gba.web.services.zaken.documenten.kenmerk.KenmerkService;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptieService;
import nl.procura.gba.web.services.zaken.documenten.stempel.StempelService;
import nl.procura.gba.web.services.zaken.geheim.VerstrekkingsBeperkingService;
import nl.procura.gba.web.services.zaken.gemeenteinbox.GemeenteInboxService;
import nl.procura.gba.web.services.zaken.gpk.GpkService;
import nl.procura.gba.web.services.zaken.gv.GegevensVerstrekkingService;
import nl.procura.gba.web.services.zaken.identiteit.IdentificatieService;
import nl.procura.gba.web.services.zaken.indicaties.IndicatiesService;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingenService;
import nl.procura.gba.web.services.zaken.kennisbank.KennisbankService;
import nl.procura.gba.web.services.zaken.legezaak.LegeZaakService;
import nl.procura.gba.web.services.zaken.naamgebruik.NaamgebruikWijzigingService;
import nl.procura.gba.web.services.zaken.protocol.ProtocolleringService;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentService;
import nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.ReisdocumentBezorgingService;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsService;
import nl.procura.gba.web.services.zaken.selectie.SelectieService;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingBerichtService;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingService;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuizingService;
import nl.procura.gba.web.services.zaken.vog.VogBerichtService;
import nl.procura.gba.web.services.zaken.vog.VogsService;
import nl.procura.gba.web.services.zaken.zakenregister.ZakenregisterService;

public class Services {

  private static final ThreadLocal<Services> threadLocal = new ThreadLocal<>();

  private final static Logger         LOGGER    = LoggerFactory.getLogger(Services.class.getName());
  private final List<AbstractService> services  = new ArrayList<>();
  private Gebruiker                   gebruiker = null;
  private final Injector              injector;
  private final TYPE                  type;

  public Services(TYPE type) {

    this.type = type;

    injector = GbaGuiceInjector.getInjector();

    add(PersoonHistorieService.class);
    add(MemoryStorageService.class);
    add(MeldingService.class);
    add(PersonenWsService.class);
    add(TabellenService.class);
    add(ProbevSqlService.class);
    add(LocatieService.class);
    add(GebruikerService.class);
    add(ParameterService.class);
    add(ProfielService.class);
    add(ProfielExtrasService.class);
    add(GebruikerInfoService.class);
    add(OnderhoudService.class);
    add(VerificatievraagService.class);
    add(PresentievraagService.class);
    add(ContactgegevensService.class);
    add(ProtocolleringService.class);
    add(DocumentService.class);
    add(DMSService.class);
    add(ZaakDmsService.class);
    add(DocumentZakenService.class);
    add(TerugmeldingService.class);
    add(TerugmeldingBerichtService.class);
    add(LogService.class);
    add(VerstrekkingsBeperkingService.class);
    add(KassaService.class);
    add(BsmService.class);
    add(AantekeningService.class);
    add(ZakenService.class);
    add(ZaakIdentificatieService.class);
    add(ZaakRelatieService.class);
    add(ZaakAttribuutService.class);
    add(ZaakStatusService.class);
    add(GpkService.class);
    add(CorrespondentieService.class);
    add(ReisdocumentService.class);
    add(DocumentInhoudingenService.class);
    add(NaamgebruikWijzigingService.class);
    add(GegevensVerstrekkingService.class);
    add(IndicatiesService.class);
    add(LegeZaakService.class);
    add(VogsService.class);
    add(VogBerichtService.class);
    add(VerhuizingService.class);
    add(IdentificatieService.class);
    add(RijbewijsService.class);
    add(GeboorteService.class);
    add(LvService.class);
    add(HuwelijkService.class);
    add(OmzettingService.class);
    add(OntbindingService.class);
    add(ErkenningService.class);
    add(PrintOptieService.class);
    add(StempelService.class);
    add(KenmerkService.class);
    add(KoppelEnumeratieService.class);
    add(DossierService.class);
    add(AkteService.class);
    add(CurateleService.class);
    add(KennisbankService.class);
    add(OverlijdenGemeenteService.class);
    add(OverlijdenBuitenlandService.class);
    add(LijkvindingService.class);
    add(NaamskeuzeService.class);
    add(LevenloosService.class);
    add(ZakenService.class);
    add(ZakenVerwijderService.class);
    add(ZakenregisterService.class);
    add(EmailService.class);
    add(LinkService.class);
    add(GemeenteService.class);
    add(BelanghebbendeService.class);
    add(GemeenteInboxService.class);
    add(SmsService.class);
    add(KiezersregisterService.class);
    add(BagService.class);
    add(RaasService.class);
    add(OnderzoekService.class);
    add(NaturalisatieService.class);
    add(RiskAnalysisService.class);
    add(SelectieService.class);
    add(RegistrationService.class);
    add(RelationService.class);
    add(PersonListMutationsService.class);
    add(ZaakConfiguratieService.class);
    add(FileImportService.class);
    add(TaskService.class);
    add(RequestInboxService.class);
    add(InwonerAppService.class);
    add(ReisdocumentBezorgingService.class);
  }

  public static Services getInstance() {
    return threadLocal.get();
  }

  public static void setInstance(Services services) {
    threadLocal.set(services);
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractService> T get(Class<T> cl) {
    for (AbstractService db : getServices()) {
      if (cl.isAssignableFrom(db.getClass())) {
        if (db.getClass().toString().contains(cl.toString())) {
          return (T) db;
        }
      }
    }

    return null;
  }

  public AantekeningService getAantekeningService() {
    return get(AantekeningService.class);
  }

  public AkteService getAkteService() {
    return get(AkteService.class);
  }

  public BelanghebbendeService getBelanghebbendeService() {
    return get(BelanghebbendeService.class);
  }

  public BsmService getBsmService() {
    return get(BsmService.class);
  }

  public ContactgegevensService getContactgegevensService() {
    return get(ContactgegevensService.class);
  }

  public CorrespondentieService getCorrespondentieService() {
    return get(CorrespondentieService.class);
  }

  public CurateleService getCurateleService() {
    return get(CurateleService.class);
  }

  public DMSService getDmsService() {
    return get(DMSService.class);
  }

  public DocumentService getDocumentService() {
    return get(DocumentService.class);
  }

  public DocumentZakenService getDocumentZakenService() {
    return get(DocumentZakenService.class);
  }

  public DossierService getDossierService() {
    return get(DossierService.class);
  }

  public EmailService getEmailService() {
    return get(EmailService.class);
  }

  public ErkenningService getErkenningService() {
    return get(ErkenningService.class);
  }

  public GeboorteService getGeboorteService() {
    return get(GeboorteService.class);
  }

  public LvService getLvService() {
    return get(LvService.class);
  }

  public GebruikerInfoService getGebruikerInfoService() {
    return get(GebruikerInfoService.class);
  }

  public GebruikerService getGebruikerService() {
    return get(GebruikerService.class);
  }

  public GegevensVerstrekkingService getGegevensverstrekkingService() {
    return get(GegevensVerstrekkingService.class);
  }

  public GemeenteService getGemeenteService() {
    return get(GemeenteService.class);
  }

  public GpkService getGpkService() {
    return get(GpkService.class);
  }

  public HuwelijkService getHuwelijkService() {
    return get(HuwelijkService.class);
  }

  public IdentificatieService getIdentificatieService() {
    return get(IdentificatieService.class);
  }

  public GemeenteInboxService getInboxService() {
    return get(GemeenteInboxService.class);
  }

  public SmsService getSmsService() {
    return get(SmsService.class);
  }

  public KiezersregisterService getKiezersregisterService() {
    return get(KiezersregisterService.class);
  }

  public BagService getGeoService() {
    return get(BagService.class);
  }

  public RaasService getRaasService() {
    return get(RaasService.class);
  }

  public OnderzoekService getOnderzoekService() {
    return get(OnderzoekService.class);
  }

  public NaturalisatieService getNaturalisatieService() {
    return get(NaturalisatieService.class);
  }

  public RiskAnalysisService getRiskAnalysisService() {
    return get(RiskAnalysisService.class);
  }

  public RegistrationService getRegistrationService() {
    return get(RegistrationService.class);
  }

  public RelationService getRelationService() {
    return get(RelationService.class);
  }

  public PersonListMutationsService getPersonListMutationService() {
    return get(PersonListMutationsService.class);
  }

  public SelectieService getSelectieService() {
    return get(SelectieService.class);
  }

  public IndicatiesService getIndicatiesService() {
    return get(IndicatiesService.class);
  }

  public KassaService getKassaService() {
    return get(KassaService.class);
  }

  public KenmerkService getKenmerkService() {
    return get(KenmerkService.class);
  }

  public KennisbankService getKennisbankService() {
    return get(KennisbankService.class);
  }

  public KoppelEnumeratieService getKoppelEnumeratieService() {
    return get(KoppelEnumeratieService.class);
  }

  public LegeZaakService getLegeZaakService() {
    return get(LegeZaakService.class);
  }

  public LevenloosService getLevenloosService() {
    return get(LevenloosService.class);
  }

  public LijkvindingService getLijkvindingService() {
    return get(LijkvindingService.class);
  }

  public LinkService getLinkService() {
    return get(LinkService.class);
  }

  public LocatieService getLocatieService() {
    return get(LocatieService.class);
  }

  public LogService getLogService() {
    return get(LogService.class);
  }

  public MeldingService getMeldingService() {
    return get(MeldingService.class);
  }

  public MemoryStorageService getMemoryService() {
    return get(MemoryStorageService.class);
  }

  public NaamgebruikWijzigingService getNaamgebruikWijzigingService() {
    return get(NaamgebruikWijzigingService.class);
  }

  public NaamskeuzeService getNaamskeuzeService() {
    return get(NaamskeuzeService.class);
  }

  public OmzettingService getOmzettingService() {
    return get(OmzettingService.class);
  }

  public OnderhoudService getOnderhoudService() {
    return get(OnderhoudService.class);
  }

  public OntbindingService getOntbindingService() {
    return get(OntbindingService.class);
  }

  public OverlijdenBuitenlandService getOverlijdenBuitenlandService() {
    return get(OverlijdenBuitenlandService.class);
  }

  public OverlijdenGemeenteService getOverlijdenGemeenteService() {
    return get(OverlijdenGemeenteService.class);
  }

  public ParameterService getParameterService() {
    return get(ParameterService.class);
  }

  public PersonenWsService getPersonenWsService() {
    return get(PersonenWsService.class);
  }

  public PersoonHistorieService getPersoonHistorieService() {
    return get(PersoonHistorieService.class);
  }

  public PresentievraagService getPresentievraagService() {
    return get(PresentievraagService.class);
  }

  public PrintOptieService getPrintOptieService() {
    return get(PrintOptieService.class);
  }

  public ProbevSqlService getProbevSqlService() {
    return get(ProbevSqlService.class);
  }

  public ProfielExtrasService getProfielExtrasService() {
    return get(ProfielExtrasService.class);
  }

  public ProfielService getProfielService() {
    return get(ProfielService.class);
  }

  public ProtocolleringService getProtocolleringService() {
    return get(ProtocolleringService.class);
  }

  public DocumentInhoudingenService getDocumentInhoudingenService() {
    return get(DocumentInhoudingenService.class);
  }

  public ReisdocumentService getReisdocumentService() {
    return get(ReisdocumentService.class);
  }

  public ReisdocumentBezorgingService getReisdocumentBezorgingService() {
    return get(ReisdocumentBezorgingService.class);
  }

  public RijbewijsService getRijbewijsService() {
    return get(RijbewijsService.class);
  }

  public FileImportService getFileImportService() {
    return get(FileImportService.class);
  }

  public TaskService getTaskService() {
    return get(TaskService.class);
  }

  public StempelService getStempelService() {
    return get(StempelService.class);
  }

  public TabellenService getTabellenService() {
    return get(TabellenService.class);
  }

  public TerugmeldingBerichtService getTerugmeldingBerichtService() {
    return get(TerugmeldingBerichtService.class);
  }

  public TerugmeldingService getTerugmeldingService() {
    return get(TerugmeldingService.class);
  }

  public TYPE getType() {
    return type;
  }

  public VerhuizingService getVerhuizingService() {
    return get(VerhuizingService.class);
  }

  public VerificatievraagService getVerificatievraagService() {
    return get(VerificatievraagService.class);
  }

  public VerstrekkingsBeperkingService getVerstrekkingsBeperkingService() {
    return get(VerstrekkingsBeperkingService.class);
  }

  public VogBerichtService getVogBerichtService() {
    return get(VogBerichtService.class);
  }

  public VogsService getVogService() {
    return get(VogsService.class);
  }

  public ZaakAttribuutService getZaakAttribuutService() {
    return get(ZaakAttribuutService.class);
  }

  public ZaakDmsService getZaakDmsService() {
    return get(ZaakDmsService.class);
  }

  public ZaakIdentificatieService getZaakIdentificatieService() {
    return get(ZaakIdentificatieService.class);
  }

  public ZaakRelatieService getZaakRelatieService() {
    return get(ZaakRelatieService.class);
  }

  public ZaakStatusService getZaakStatusService() {
    return get(ZaakStatusService.class);
  }

  public ZaakConfiguratieService getZaakConfiguratieService() {
    return get(ZaakConfiguratieService.class);
  }

  public ZakenregisterService getZakenregisterService() {
    return get(ZakenregisterService.class);
  }

  public ZakenService getZakenService() {
    return get(ZakenService.class);
  }

  public ZakenVerwijderService getZakenVerwijderService() {
    return get(ZakenVerwijderService.class);
  }

  public RequestInboxService getRequestInboxService() {
    return get(RequestInboxService.class);
  }

  public InwonerAppService getInwonerAppService() {
    return get(InwonerAppService.class);
  }

  public List<AbstractService> getServices() {
    return services;
  }

  public Gebruiker getGebruiker() {
    return gebruiker;
  }

  public void setGebruiker(Gebruiker gebruiker) {
    this.gebruiker = gebruiker;
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> getServices(Class<T> cl) {
    List<T> l = new ArrayList<>();
    for (AbstractService db : getServices()) {
      if (cl.isAssignableFrom(db.getClass())) {
        l.add((T) db);
      }
    }

    return l;
  }

  public void init() {
    long startTimeContainer = System.currentTimeMillis();
    for (AbstractService db : services) {
      if (!db.isInitiated()) {
        long startTime = System.currentTimeMillis();
        db.init();
        showTime(false, startTime, "Service: " + db.getName() + " geladen");
      }
    }

    showTime(true, startTimeContainer, "Services geladen");
  }

  public boolean isTestomgeving() {
    return isTru(getGebruiker().getParameters().get(ParameterConstant.TEST_OMGEVING).getValue());
  }

  public boolean isType(TYPE type) {
    return getType() == type;
  }

  public void reloadGebruiker() {
    if (gebruiker != null) {
      LOGGER.debug("Herladen gebruiker");
      getGebruikerService().reload(gebruiker);
    }
  }

  /**
   * Voeg Service toe aan lijst. Aangemaakt door Google Guice.
   */
  private void add(Class<? extends AbstractService> cl) {

    AbstractService db = injector.getInstance(cl);
    db.setServices(this);
    services.add(db);
  }

  private void showTime(boolean showNul, long startTime, String caption) {
    long endTime = System.currentTimeMillis();
    long time = (endTime - startTime);
    if (showNul || time > 0) {
      LOGGER.debug(caption + " (" + time + "ms.)");
    }
  }

  public enum TYPE {
    PROWEB,
    REST
  }
}
