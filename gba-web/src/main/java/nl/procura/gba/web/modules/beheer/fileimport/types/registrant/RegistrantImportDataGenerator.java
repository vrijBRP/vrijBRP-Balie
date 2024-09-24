package nl.procura.gba.web.modules.beheer.fileimport.types.registrant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;

import nl.procura.standard.ProcuraDate;

public class RegistrantImportDataGenerator {

  public static void main(String[] args) throws IOException {
    new RegistrantImportDataGenerator();
  }

  public RegistrantImportDataGenerator() throws IOException {
    StringBuilder out = new StringBuilder();
    out.append("Achternaam;Voorvoegsel;Voornamen;Geslacht;Geboortedatum;Geboorteplaats;Geboorteland;Nationaliteit;"
        + "Straatnaam;Huisnummer;Huisletter;Toevoeging;Postcode;Woonplaats;E-mail;Telefoon;Referentienummer;Land van vorig adres;Datum ingang bewoning;Opmerkingen\n");
    for (int i = 0; i < 10000; i++) {
      out.append(String.format("%s;%s;%s;v;%s;Ringerike;Noorwegen;Noorse;"
          + "%s;116;;5;6525 BJ;Nijmegen;burgerzaken@procura.nl;"
          + "06-123456790;4384783274382;Noorwegen;%s;Dit is een opmerking\n",
          randomName(1),
          randomPrefix(),
          randomName(0),
          randomBirthDate(),
          randomString(),
          randomDate()));
    }

    FileUtils.write(new File("registrants.csv"), out.toString(), "UTF-8");
  }

  private String randomBirthDate() {
    return new ProcuraDate().addYears(-19).addDays(-new Random().nextInt(400)).getSystemDate();
  }

  private String randomDate() {
    return new ProcuraDate().addDays(new Random().nextInt(100)).getSystemDate();
  }

  private String randomPrefix() {
    return new Random().nextInt(10) <= 3 ? "de" : "";
  }

  // Return a random String of maximum length 10
  private static String randomString() {
    return RandomStringUtils.randomAlphabetic(10);
  }

  public String randomName(int index) {
    List<String[]> names = getRandomNames();
    return names.get(new Random().nextInt(names.size() - 1))[index];
  }

  public List<String[]> getRandomNames() {
    List<String[]> names = new ArrayList<>();
    names.add(new String[]{ "Miley", "Spencer" });
    names.add(new String[]{ "Isabella", "Dixon" });
    names.add(new String[]{ "April", "Jones" });
    names.add(new String[]{ "Marcus", "Scott" });
    names.add(new String[]{ "Frederick", "Cole" });
    names.add(new String[]{ "Abigail", "West" });
    names.add(new String[]{ "Eleanor", "Kelly" });
    names.add(new String[]{ "Audrey", "Craig" });
    names.add(new String[]{ "David", "Foster" });
    names.add(new String[]{ "Reid", "Bennett" });
    names.add(new String[]{ "Roman", "Payne" });
    names.add(new String[]{ "Amber", "Cole" });
    names.add(new String[]{ "Tara", "Carroll" });
    names.add(new String[]{ "Caroline", "Douglas" });
    names.add(new String[]{ "Edward", "Smith" });
    names.add(new String[]{ "Mike", "Carter" });
    names.add(new String[]{ "Alexia", "Holmes" });
    names.add(new String[]{ "Melanie", "Tucker" });
    names.add(new String[]{ "Kimberly", "Carroll" });
    names.add(new String[]{ "Nicholas", "West" });
    names.add(new String[]{ "Max", "Carroll" });
    names.add(new String[]{ "Madaline", "Hall" });
    names.add(new String[]{ "Miranda", "Cunningham" });
    names.add(new String[]{ "Abigail", "Myers" });
    names.add(new String[]{ "Lyndon", "Campbell" });
    names.add(new String[]{ "Maddie", "Bailey" });
    names.add(new String[]{ "Evelyn", "Douglas" });
    names.add(new String[]{ "Evelyn", "Hill" });
    names.add(new String[]{ "Madaline", "Fowler" });
    names.add(new String[]{ "Frederick", "Robinson" });
    names.add(new String[]{ "Daniel", "Watson" });
    names.add(new String[]{ "Amy", "Morris" });
    names.add(new String[]{ "Adrianna", "Morgan" });
    names.add(new String[]{ "Vincent", "Moore" });
    names.add(new String[]{ "Maddie", "Carroll" });
    names.add(new String[]{ "Emily", "Harrison" });
    names.add(new String[]{ "Alberta", "Cooper" });
    names.add(new String[]{ "Daryl", "Fowler" });
    names.add(new String[]{ "Sam", "Lloyd" });
    names.add(new String[]{ "Ellia", "Murray" });
    names.add(new String[]{ "Luke", "Johnston" });
    names.add(new String[]{ "Chester", "Lloyd" });
    names.add(new String[]{ "Alissa", "Montgomery" });
    names.add(new String[]{ "Owen", "Lloyd" });
    names.add(new String[]{ "Melanie", "Craig" });
    names.add(new String[]{ "Freddie", "Scott" });
    names.add(new String[]{ "Lyndon", "Craig" });
    names.add(new String[]{ "Charlie", "Clark" });
    names.add(new String[]{ "Fenton", "Higgins" });
    names.add(new String[]{ "Savana", "Spencer" });
    names.add(new String[]{ "Lily", "Scott" });
    names.add(new String[]{ "Madaline", "Higgins" });
    names.add(new String[]{ "Melissa", "Fowler" });
    names.add(new String[]{ "Michael", "Warren" });
    names.add(new String[]{ "Clark", "Richardson" });
    names.add(new String[]{ "Naomi", "Craig" });
    names.add(new String[]{ "Oscar", "Carroll" });
    names.add(new String[]{ "Frederick", "Scott" });
    names.add(new String[]{ "Deanna", "Robinson" });
    names.add(new String[]{ "Oscar", "Alexander" });
    names.add(new String[]{ "Spike", "Warren" });
    names.add(new String[]{ "Tyler", "Carter" });
    names.add(new String[]{ "Derek", "Wright" });
    names.add(new String[]{ "Byron", "Dixon" });
    names.add(new String[]{ "Daryl", "Barnes" });
    names.add(new String[]{ "Amber", "Clark" });
    names.add(new String[]{ "Jenna", "Hill" });
    names.add(new String[]{ "Alexia", "Cunningham" });
    names.add(new String[]{ "Adele", "Holmes" });
    names.add(new String[]{ "James", "Baker" });
    names.add(new String[]{ "Kate", "Morris" });
    names.add(new String[]{ "Daniel", "Sullivan" });
    names.add(new String[]{ "Lucy", "Thomas" });
    names.add(new String[]{ "Lily", "Mitchell" });
    names.add(new String[]{ "Caroline", "Barrett" });
    names.add(new String[]{ "Owen", "Chapman" });
    names.add(new String[]{ "Stella", "Dixon" });
    names.add(new String[]{ "Penelope", "Adams" });
    names.add(new String[]{ "Charlotte", "Owens" });
    names.add(new String[]{ "Kristian", "Wright" });
    names.add(new String[]{ "Rubie", "Allen" });
    names.add(new String[]{ "Savana", "Riley" });
    names.add(new String[]{ "Caroline", "Casey" });
    names.add(new String[]{ "Penelope", "Ryan" });
    names.add(new String[]{ "Stella", "Armstrong" });
    names.add(new String[]{ "Lydia", "Harris" });
    names.add(new String[]{ "Jessica", "Warren" });
    names.add(new String[]{ "Miley", "Myers" });
    names.add(new String[]{ "Chelsea", "Stevens" });
    names.add(new String[]{ "Paul", "Cole" });
    names.add(new String[]{ "Gianna", "Crawford" });
    names.add(new String[]{ "Alissa", "Ellis" });
    names.add(new String[]{ "Kirsten", "Smith" });
    names.add(new String[]{ "Kellan", "Campbell" });
    names.add(new String[]{ "Oliver", "Holmes" });
    names.add(new String[]{ "Belinda", "Armstrong" });
    names.add(new String[]{ "Cherry", "Harper" });
    names.add(new String[]{ "Rubie", "Hill" });
    names.add(new String[]{ "Brad", "Craig" });
    names.add(new String[]{ "Edwin", "Morgan" });
    names.add(new String[]{ "James", "Moore" });
    names.add(new String[]{ "Adrian", "Williams" });
    names.add(new String[]{ "Daryl", "Hall" });
    names.add(new String[]{ "Elian", "Chapman" });
    names.add(new String[]{ "Spike", "Cunningham" });
    names.add(new String[]{ "Vanessa", "Montgomery" });
    names.add(new String[]{ "Brad", "Stewart" });
    names.add(new String[]{ "Chester", "Crawford" });
    names.add(new String[]{ "Miley", "Williams" });
    names.add(new String[]{ "Preston", "Harris" });
    names.add(new String[]{ "Paul", "Bailey" });
    names.add(new String[]{ "Amanda", "Wright" });
    names.add(new String[]{ "Antony", "Ryan" });
    names.add(new String[]{ "Carl", "Lloyd" });
    names.add(new String[]{ "Cherry", "Myers" });
    names.add(new String[]{ "William", "Douglas" });
    names.add(new String[]{ "Mike", "Holmes" });
    names.add(new String[]{ "Freddie", "Turner" });
    names.add(new String[]{ "Maria", "Perry" });
    names.add(new String[]{ "Cadie", "Ross" });
    names.add(new String[]{ "Charlie", "Harper" });
    names.add(new String[]{ "Spike", "Ryan" });
    names.add(new String[]{ "Cherry", "Kelley" });
    names.add(new String[]{ "Charlotte", "Allen" });
    names.add(new String[]{ "Agata", "Chapman" });
    names.add(new String[]{ "Daisy", "Wells" });
    names.add(new String[]{ "Rafael", "Myers" });
    names.add(new String[]{ "Natalie", "Cole" });
    names.add(new String[]{ "Walter", "Gray" });
    names.add(new String[]{ "Alan", "Thomas" });
    names.add(new String[]{ "Kevin", "Lloyd" });
    names.add(new String[]{ "Sofia", "Walker" });
    names.add(new String[]{ "Miller", "Craig" });
    names.add(new String[]{ "Martin", "Thomas" });
    names.add(new String[]{ "Annabella", "Owens" });
    names.add(new String[]{ "Fenton", "Casey" });
    names.add(new String[]{ "Vivian", "Chapman" });
    names.add(new String[]{ "James", "Hill" });
    names.add(new String[]{ "Sienna", "Morris" });
    names.add(new String[]{ "George", "Edwards" });
    names.add(new String[]{ "Arianna", "Howard" });
    names.add(new String[]{ "Stuart", "Brown" });
    names.add(new String[]{ "Victor", "Hamilton" });
    names.add(new String[]{ "Aldus", "Hunt" });
    names.add(new String[]{ "Wilson", "Gray" });
    names.add(new String[]{ "Mary", "Murray" });
    names.add(new String[]{ "Michelle", "Kelly" });
    names.add(new String[]{ "Sofia", "Sullivan" });
    names.add(new String[]{ "Chloe", "Chapman" });
    names.add(new String[]{ "Ryan", "Phillips" });
    names.add(new String[]{ "Kelvin", "Miller" });
    names.add(new String[]{ "Dainton", "Carroll" });
    names.add(new String[]{ "Tiana", "Martin" });
    names.add(new String[]{ "Kelsey", "Montgomery" });
    names.add(new String[]{ "Vincent", "Tucker" });
    names.add(new String[]{ "Madaline", "Ellis" });
    names.add(new String[]{ "Gianna", "Foster" });
    names.add(new String[]{ "Nicole", "Cole" });
    names.add(new String[]{ "Adison", "Howard" });
    names.add(new String[]{ "Byron", "Payne" });
    names.add(new String[]{ "Edward", "Howard" });
    names.add(new String[]{ "John", "Tucker" });
    names.add(new String[]{ "Roman", "Dixon" });
    names.add(new String[]{ "Aston", "Anderson" });
    names.add(new String[]{ "Amanda", "Martin" });
    names.add(new String[]{ "Kelvin", "Fowler" });
    names.add(new String[]{ "David", "Phillips" });
    names.add(new String[]{ "Maddie", "Johnson" });
    names.add(new String[]{ "Luke", "Bailey" });
    names.add(new String[]{ "Vivian", "Farrell" });
    names.add(new String[]{ "Alford", "Gibson" });
    names.add(new String[]{ "Vivian", "Payne" });
    names.add(new String[]{ "Michael", "Carroll" });
    names.add(new String[]{ "Jared", "Wright" });
    names.add(new String[]{ "Hailey", "Grant" });
    names.add(new String[]{ "Andrew", "Ross" });
    names.add(new String[]{ "Madaline", "Bailey" });
    names.add(new String[]{ "Belinda", "Smith" });
    names.add(new String[]{ "Reid", "Hamilton" });
    names.add(new String[]{ "Melanie", "Thomas" });
    names.add(new String[]{ "Eleanor", "Henderson" });
    names.add(new String[]{ "Lydia", "Craig" });
    names.add(new String[]{ "Deanna", "Chapman" });
    names.add(new String[]{ "Adrianna", "Farrell" });
    names.add(new String[]{ "Miranda", "Clark" });
    names.add(new String[]{ "Ada", "Hawkins" });
    names.add(new String[]{ "Roman", "Kelly" });
    names.add(new String[]{ "Sydney", "Dixon" });
    names.add(new String[]{ "Aida", "Carter" });
    names.add(new String[]{ "Tara", "Brown" });
    names.add(new String[]{ "Catherine", "Turner" });
    names.add(new String[]{ "Alexander", "Robinson" });
    names.add(new String[]{ "Richard", "Hamilton" });
    names.add(new String[]{ "Abigail", "Crawford" });
    names.add(new String[]{ "Ashton", "Johnson" });
    names.add(new String[]{ "Alexia", "Rogers" });
    names.add(new String[]{ "Mary", "Thomas" });
    names.add(new String[]{ "Dexter", "Jones" });
    names.add(new String[]{ "Luke", "Wilson" });
    names.add(new String[]{ "Freddie", "Richards" });
    return names;
  }
}
