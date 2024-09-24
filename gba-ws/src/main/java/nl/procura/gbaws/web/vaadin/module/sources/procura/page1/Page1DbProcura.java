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

package nl.procura.gbaws.web.vaadin.module.sources.procura.page1;

import static nl.procura.gbaws.web.vaadin.module.sources.procura.page1.Page1DbProcuraBean.CUSTOM_DRIVER;
import static nl.procura.gbaws.web.vaadin.module.sources.procura.page1.Page1DbProcuraBean.CUSTOM_URL;
import static nl.procura.gbaws.web.vaadin.module.sources.procura.page1.Page1DbProcuraBean.DATABASE;
import static nl.procura.gbaws.web.vaadin.module.sources.procura.page1.Page1DbProcuraBean.MAX_CONN;
import static nl.procura.gbaws.web.vaadin.module.sources.procura.page1.Page1DbProcuraBean.MIN_CONN;
import static nl.procura.gbaws.web.vaadin.module.sources.procura.page1.Page1DbProcuraBean.PERSON_ID;
import static nl.procura.gbaws.web.vaadin.module.sources.procura.page1.Page1DbProcuraBean.PORT;
import static nl.procura.gbaws.web.vaadin.module.sources.procura.page1.Page1DbProcuraBean.PW;
import static nl.procura.gbaws.web.vaadin.module.sources.procura.page1.Page1DbProcuraBean.SCHEMA;
import static nl.procura.gbaws.web.vaadin.module.sources.procura.page1.Page1DbProcuraBean.SERVER;
import static nl.procura.gbaws.web.vaadin.module.sources.procura.page1.Page1DbProcuraBean.SID;
import static nl.procura.gbaws.web.vaadin.module.sources.procura.page1.Page1DbProcuraBean.TNS_ADMIN_DIR;
import static nl.procura.gbaws.web.vaadin.module.sources.procura.page1.Page1DbProcuraBean.USERNAME;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.jpa.personenws.utils.GbaWsEclipseLinkUtil;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.db.handlers.ParmDao;
import nl.procura.gbaws.db.wrappers.ProcuraDbWrapper;
import nl.procura.gbaws.web.vaadin.module.auth.profile.page1.Page1AuthProfile;
import nl.procura.gbaws.web.vaadin.module.sources.ModuleDbPage;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1DbProcura extends ModuleDbPage {

  private Page1DbProcuraForm form1;
  private Page1DbProcuraForm form2;
  private Page1DbProcuraForm form3;
  private Page1DbProcuraForm form4;

  public Page1DbProcura() {
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonSave);
      addButton(buttonNext);

      buttonNext.setCaption("Controleer database");
      form1 = new Page1DbProcuraForm("Standaard", DATABASE, SID, SERVER, SCHEMA, PORT);
      form2 = new Page1DbProcuraForm("Handmatig invullen", TNS_ADMIN_DIR, CUSTOM_URL, CUSTOM_DRIVER);
      form3 = new Page1DbProcuraForm("Overige eigenschappen", USERNAME, PW, MIN_CONN, MAX_CONN);
      form4 = new Page1DbProcuraForm("Exporteren", PERSON_ID);

      Page1DbProcuraBean bean = new Page1DbProcuraBean();
      ProcuraDbWrapper procuraDb = ParmDao.getProcuraDb();

      bean.setDatabase(procuraDb.getDatabase());
      bean.setSid(procuraDb.getSid());
      bean.setServer(procuraDb.getServer());
      bean.setSchema(procuraDb.getSchema());
      bean.setPort(procuraDb.getPort());
      bean.setTnsAdminDir(procuraDb.getTnsAdminDir());
      bean.setCustomUrl(procuraDb.getUrl());
      bean.setCustomDriver(procuraDb.getDriver());
      bean.setUsername(procuraDb.getUsername());
      bean.setPw(procuraDb.getPassword());
      bean.setMinConn(procuraDb.getMinConnections());
      bean.setMaxConn(procuraDb.getMaxConnections());

      form1.setBean(bean);
      form2.setBean(bean);
      form3.setBean(bean);
      form4.setBean(bean);
      addComponent(form1);
      addComponent(new InfoLayout("", "Hieronder kan zelf een URL of driver worden ingevuld als " +
          "bovenstaande variabelen niet voldoende zijn. <br/>" +
          "Oracle TNS admin map verwijst naar de map waarin tnsnames.ora zich moet bevinden."));
      addComponent(form2);
      addComponent(form3);
      addComponent(form4);
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().removeOtherPages();
    getNavigation().goToPage(Page1AuthProfile.class);
  }

  @Override
  public void onSave() {

    form1.commit();
    form2.commit();
    form3.commit();

    Page1DbProcuraBean bean1 = form1.getBean();
    Page1DbProcuraBean bean2 = form2.getBean();
    Page1DbProcuraBean bean3 = form3.getBean();

    ProcuraDbWrapper procuraDb = new ProcuraDbWrapper();

    procuraDb.setDatabase(bean1.getDatabase());
    procuraDb.setSid(bean1.getSid());
    procuraDb.setServer(bean1.getServer());
    procuraDb.setSchema(bean1.getSchema());
    procuraDb.setPort(bean1.getPort());

    procuraDb.setTnsAdminDir(bean2.getTnsAdminDir());
    procuraDb.setUrl(bean2.getCustomUrl());
    procuraDb.setDriver(bean2.getCustomDriver());

    procuraDb.setUsername(bean3.getUsername());
    procuraDb.setPassword(bean3.getPw());
    procuraDb.setMinConnections(bean3.getMinConn());
    procuraDb.setMaxConnections(bean3.getMaxConn());

    ParmDao.mergeAndCommitProcuraDb(procuraDb);

    successMessage("Gegevens zijn opgeslagen");

    super.onSave();
  }

  @Override
  public void onNextPage() {

    form1.commit();
    form2.commit();
    form3.commit();

    Page1DbProcuraBean bean1 = form1.getBean();
    Page1DbProcuraBean bean2 = form2.getBean();
    Page1DbProcuraBean bean3 = form3.getBean();

    Properties properties = GbaConfig.getProperties();

    if (StringUtils.isBlank(bean1.getDatabase())) {
      throw new ProException(WARNING, "Geen database geselecteerd");
    }

    properties.put(GbaWsEclipseLinkUtil.DATABASE_TYPE, bean1.getDatabase());
    properties.put(GbaWsEclipseLinkUtil.DATABASE, bean1.getSid());
    properties.put(GbaWsEclipseLinkUtil.SERVER, bean1.getServer());
    properties.put(GbaWsEclipseLinkUtil.SCHEMA, bean1.getSchema());
    properties.put(GbaWsEclipseLinkUtil.PORT, bean1.getPort());

    properties.put(GbaWsEclipseLinkUtil.TNS_ADMIN_DIR, bean2.getTnsAdminDir());
    properties.put(GbaWsEclipseLinkUtil.CUSTOM_URL, bean2.getCustomUrl());
    properties.put(GbaWsEclipseLinkUtil.CUSTOM_DRIVER, bean2.getCustomDriver());

    properties.put(GbaWsEclipseLinkUtil.USERNAME, bean3.getUsername());
    properties.put(GbaWsEclipseLinkUtil.PW, bean3.getPw());
    properties.put(GbaWsEclipseLinkUtil.MIN_CONNECTIONS, bean3.getMinConn());
    properties.put(GbaWsEclipseLinkUtil.MAX_CONNECTIONS, bean3.getMaxConn());

    EntityManagerFactory factory = GbaWsJpa.getFactory(properties);
    factory.createEntityManager().close();
    factory.close();

    successMessage("De verbinding is correct");

    super.onNextPage();
  }
}
