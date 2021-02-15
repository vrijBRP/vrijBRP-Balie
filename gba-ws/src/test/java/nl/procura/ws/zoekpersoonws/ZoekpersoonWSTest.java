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

package nl.procura.ws.zoekpersoonws;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.google.gson.JsonParser;

import nl.procura.diensten.zoekpersoon.objecten.*;

public class ZoekpersoonWSTest {

  @Test
  public void requestMustMatchJsonFiles() throws IOException {
    compare(new ZoekArgumenten(), "ZoekArgumenten.json");
    compare(new CategorieArgumenten(), "CategorieArgumenten.json");
    compare(new LoginArgumenten(), "LoginArgumenten.json");
    compare(getZoekPersoonWSAntwoord(), "ZoekPersoonWSAntwoord.json");
  }

  private void compare(Object obj, String file) throws IOException {
    JsonParser parser = new JsonParser();
    InputStream stream = getClass().getClassLoader().getResourceAsStream(file);
    // GSON uses the (private) fields to generate json. Objectmapper does not.
    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(obj);
    // Uses GSON to compare two json strings.
    assert stream != null;
    assertEquals(parser.parse(IOUtils.toString(stream)), parser.parse(json));
  }

  private ZoekPersoonWSAntwoord getZoekPersoonWSAntwoord() {
    ZoekPersoonWSAntwoord zoekPersoonWSAntwoord = new ZoekPersoonWSAntwoord();
    zoekPersoonWSAntwoord.setMeldingen(new String[]{ "Melding1", "Melding2" });
    zoekPersoonWSAntwoord.setDatabron(1);
    Persoonslijst pl = new Persoonslijst();

    Woningkaart wk = new Woningkaart();
    wk.setWoningkaart_actueel(new Woningkaartgegevens());
    pl.setWoningkaart(wk);

    Rijbewijs rijbewijs = new Rijbewijs();
    rijbewijs.setRijbewijs_actueel(new Rijbewijsgegevens());
    pl.setRijbewijs(rijbewijs);

    Diversen diversen = new Diversen();
    diversen.setDiversen_actueel(new Diversengegevens());
    pl.setDiversen(diversen);

    Nationaliteit nat = new Nationaliteit();
    nat.setNationaliteit_actueel(new Nationaliteitgegevens());
    pl.setNationaliteiten(new Nationaliteit[]{ nat });

    Huwelijk huw = new Huwelijk();
    huw.setHuwelijk_actueel(new Huwelijkgegevens());
    pl.setHuwelijken(new Huwelijk[]{ huw });

    Verblijfplaats vb = new Verblijfplaats();
    vb.setVerblijfplaats_actueel(new Verblijfplaatsgegevens());
    pl.setVerblijfplaats(vb);

    Kind kind = new Kind();
    kind.setKind_actueel(new Kindgegevens());
    pl.setKinderen(new Kind[]{ kind });

    Reisdocument reisd = new Reisdocument();
    reisd.setReisdocument_actueel(new Reisdocumentgegevens());
    pl.setReisdocumenten(new Reisdocument[]{ reisd });

    Afnemer afn = new Afnemer();
    afn.setAfnemer_actueel(new Afnemergegevens());
    pl.setAfnemers(new Afnemer[]{ afn });

    Persoon persoon = new Persoon();
    persoon.setPersoon_actueel(new Persoonsgegevens());
    pl.setPersoon(persoon);

    Ouder ouder = new Ouder();
    ouder.setOuder_actueel(new Oudergegevens());
    pl.setOuder_1(ouder);
    pl.setOuder_2(ouder);

    Inschrijving inschr = new Inschrijving();
    inschr.setInschrijving_actueel(new Inschrijvinggegevens());
    pl.setInschrijving(inschr);

    Verwijzing verw = new Verwijzing();
    verw.setVerwijzing_actueel(new Verwijzinggegevens());
    pl.setVerwijzing(verw);

    Overlijden overl = new Overlijden();
    overl.setOverlijden_actueel(new Overlijdengegevens());
    pl.setOverlijden(overl);

    Gezag gezag = new Gezag();
    gezag.setGezag_actueel(new Gezaggegevens());
    pl.setGezag(gezag);

    Kiesrecht kiesrecht = new Kiesrecht();
    kiesrecht.setKiesrecht_actueel(new Kiesrechtgegevens());
    pl.setKiesrecht(kiesrecht);

    Verblijfstitel vbt = new Verblijfstitel();
    vbt.setVerblijfstitel_actueel(new Verblijfstitelgegevens());
    pl.setVerblijfstitel(vbt);

    Kladblokaantekening kladblokaantekening = new Kladblokaantekening();
    kladblokaantekening.setRegels(new String[]{ "" });
    pl.setKladblokaantekening(kladblokaantekening);

    Lokaleafnemersindicatie lok = new Lokaleafnemersindicatie();
    pl.setLokaleafnemersindicaties(new Lokaleafnemersindicatie[]{ lok });

    MetaInfoGegevens meta = new MetaInfoGegevens();
    pl.setMetaInfoGegevens(new MetaInfoGegevens[]{ meta });

    zoekPersoonWSAntwoord.setPersoonslijsten(new Persoonslijst[]{ pl });
    return zoekPersoonWSAntwoord;
  }
}
