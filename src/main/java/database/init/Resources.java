/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.init;

/**
 *
 * @author mountant
 */
public class Resources {

    static String admin = "{\"username\":\"admin\",\"email\":\"admin@e199.gr\","
            + "\"password\":\"admiN12@*\","
            + "\"firstname\":\"Admin\",\"lastname\":\"Admin\","
            + "\"birthdate\":\"1980-06-03\",\"gender\":\"Male\",\"afm\":\"1234554321\","
            + "\"country\":\"Greece\",\"address\":\"Pl. Kiprou 5, Iraklio 713 06\",\"municipality\":\"Heraklion\",\"prefecture\":\"Heraklion\",\"lat\":\"35.3332276\","
            + "\"lon\":\"25.1162213\",\"telephone\":\"2813407000\","
            + "\"job\":\"Firemen\"}";

    static String user1JSON = "{\"username\":\"userone\",\"email\":\"userone@example.com\",\"password\":\"ab$A12cde\","
            + "\"firstname\":\"Andreas\",\"lastname\":\"Papadopoulos\",\"birthdate\":\"1992-06-03\",\"gender\":\"Male\",\"afm\":\"1238585123\","
            + "\"country\":\"Greece\",\"address\":\"CSD Voutes\",\"municipality\":\"Heraklion\",\"prefecture\":\"Heraklion\",\"lat\":\"35.3053121\","
            + "\"lon\":\"25.0722869\",\"telephone\":\"1234567890\","
            + "\"job\":\"Researcher\"}";

    static String user2JSON = "{\"username\":\"usertwo\",\"email\":\"usertwo@example.com\",\"password\":\"ab$A12cde\","
            + "\"firstname\":\"Eleni\",\"lastname\":\"Nikolaou\",\"birthdate\":\"1998-08-12\",\"gender\":\"Female\",\"afm\":\"2525252525\","
            + "\"country\":\"Greece\",\"address\":\"Dimokratias 99\",\"municipality\":\"Heraklion\",\"prefecture\":\"Heraklion\",\"lat\":\"35.3401097\","
            + "\"lon\":\"25.1562301\",\"telephone\":\"6911111122\","
            + "\"job\":\"Engineer\"}";

    static String user3JSON = "{\"username\":\"userthree\",\"email\":\"userthree@example.com\",\"password\":\"ab$A12cde\","
            + "\"firstname\":\"Dimitris\",\"lastname\":\"Alexandrou\",\"birthdate\":\"1981-11-12\",\"gender\":\"Male\",\"afm\":\"1579991110\","
            + "\"country\":\"Greece\",\"address\":\"Limenas Chersonisou\",\"municipality\":\"Hersonissos\",\"prefecture\":\"Heraklion\",\"lat\":\"35.318761\","
            + "\"lon\":\"25.3715371\",\"telephone\":\"6977889900\","
            + "\"job\":\"Project Manager\"}";
    
    static String user4JSON = "{\"username\":\"userfour\",\"email\":\"userfour@example.com\",\"password\":\"ab$A12cde\","
            + "\"firstname\":\"Katerina\",\"lastname\":\"Vasileiou\",\"birthdate\":\"2003-07-12\",\"gender\":\"Female\",\"afm\":\"1179991110\","
            + "\"country\":\"Greece\",\"address\":\"I. Koriotaki\",\"municipality\":\"Faistos\",\"prefecture\":\"Heraklion\",\"lat\":\"35.0722851\","
            + "\"lon\":\"24.7588403\",\"telephone\":\"6977880000\","
            + "\"job\":\"Farmer\"}";
    
    
    static String volunteer1JSON = "{\"username\":\"volone\",\"email\":\"volone@example.com\",\"password\":\"ab$A12cde\","
            + "\"firstname\":\"Georgios\",\"lastname\":\"Ioannou\",\"birthdate\":\"1992-08-12\",\"gender\":\"Male\",\"afm\":\"1234567891\","
            + "\"country\":\"Greece\",\"address\":\"El. Venizelou 160, Malia\",\"municipality\":\"Hersonissos\",\"prefecture\":\"Heraklion\",\"lat\":\"35.2836391\","
            + "\"lon\":\"25.4600817\",\"telephone\":\"6988877755\","
            + "\"job\":\"Taxi Driver\",\"volunteer_type\":\"driver\",\"height\":\"1.80\",\"weight\":\"90.0\"}";

    static String volunteer2JSON = "{\"username\":\"voltwo\",\"email\":\"voltwo@example.com\",\"password\":\"ab$A12cde\","
            + "\"firstname\":\"Panagiotis\",\"lastname\":\"Christodoulou\",\"birthdate\":\"1988-08-12\",\"gender\":\"Male\",\"afm\":\"1234567891\","
            + "\"country\":\"Greece\",\"address\":\"Evans 124\",\"municipality\":\"Heraklion\",\"prefecture\":\"Heraklion\",\"lat\":\"35.2976896\","
            + "\"lon\":\"25.0806272\",\"telephone\":\"6978912345\","
            + "\"job\":\"Barista\",\"volunteer_type\":\"simple\",\"height\":\"1.99\",\"weight\":\"112.5\"}";

    static String volunteer3JSON = "{\"username\":\"volthree\",\"email\":\"volthree@example.com\",\"password\":\"ab$A12cde\","
            + "\"firstname\":\"Sophia\",\"lastname\":\"Dimitriou\",\"birthdate\":\"1992-11-12\",\"gender\":\"Female\",\"afm\":\"8882223335\","
            + "\"country\":\"Greece\",\"address\":\"Stalida\",\"municipality\":\"Hersonissos\",\"prefecture\":\"Heraklion\",\"lat\":\"35.2908868\","
            + "\"lon\":\"25.4600817\",\"telephone\":\"6977777777\","
            + "\"job\":\"Receptionist\",\"volunteer_type\":\"simple\",\"height\":\"1.70\",\"weight\":\"60.0\"}";

    static String volunteer4JSON = "{\"username\":\"volfour\",\"email\":\"volfour@example.com\",\"password\":\"ab$A12cde\","
            + "\"firstname\":\"Nikolaos\",\"lastname\":\"Konstantinou\",\"birthdate\":\"1978-08-12\",\"gender\":\"Male\",\"afm\":\"7899991112\","
            + "\"country\":\"Greece\",\"address\":\"Kondylaki 88\",\"municipality\":\"Heraklion\",\"prefecture\":\"Heraklion\",\"lat\":\"35.3295412\","
            + "\"lon\":\"25.1185202\",\"telephone\":\"6991234567\","
            + "\"job\":\"Dikigoros\",\"volunteer_type\":\"driver\",\"height\":\"1.69\",\"weight\":\"78.5\"}";

    
    static String incident1 = "{\"incident_type\":\"fire\","
            + "\"description\":\"Fotia konta stis voutes\","
            + "\"user_phone\":\"2813407000\","
            + "\"user_type\":\"admin\","
            + "\"address\":\"Leof. Panepistimiou 121\","
            + "\"lat\":\"35.2975689\","
            + "\"lon\":\"25.0787173\","
            + "\"municipality\":\"Heraklion\","
            + "\"prefecture\":\"Heraklion\","
            + "\"status\":\"running\","
            + "\"danger\":\"high\"}";

    static String incident2 = "{\"incident_type\":\"accident\","
            + "\"description\":\"Atuxima me 2 gourounes\","
            + "\"user_phone\":\"6977141414\","
            + "\"user_type\":\"guest\","
            + "\"address\":\"El. Venizelou 170\","
            + "\"start_datetime\":\"2024-10-10 15:40:05\","
            + "\"status\":\"submitted\","
            + "\"danger\":\"unknown\"}";

    static String incident3 = "{\"incident_type\":\"fire\","
            + "\"description\":\"Fotia se mi katoikimeni perioxi\","
            + "\"user_phone\":\"6977142314\","
            + "\"user_type\":\"guest\","
            + "\"address\":\"Archanes-Asterousia 70100\","
            + "\"start_datetime\":\"2024-10-10 15:50:05\","
            + "\"status\":\"submitted\","
            + "\"danger\":\"unknown\"}";

    
    static String incident4 = "{\"incident_type\":\"accident\","
            + "\"description\":\"Atuxima me fortigo\","
            + "\"user_phone\":\"6978912345\","
            + "\"user_type\":\"user\","
            + "\"address\":\"Archanes-Asterousia 70100\","
            + "\"start_datetime\":\"2024-08-08 10:50:05\","
            + "\"status\":\"submitted\","
            + "\"danger\":\"unknown\"}";

     static String incident5 = "{\"incident_type\":\"accident\","
            + "\"description\":\"Atuxima me autokinita\","
            + "\"user_phone\":\"2813407000\","
            + "\"user_type\":\"admin\","
            + "\"address\":\"Leof. Knossou 17\","
            + "\"lat\":\"35.331356\","
            + "\"lon\":\"25.133087\","
            + "\"municipality\":\"Heraklion\","
            + "\"prefecture\":\"Heraklion\","
            + "\"status\":\"running\","
            + "\"danger\":\"low\"}";
    
    
    static String participant1 = "{\"incident_id\":\"1\","
            + "\"volunteer_type\":\"simple\","
            + "\"status\":\"requested\"}";

    static String participant2 = "{\"incident_id\":\"1\","
            + "\"volunteer_type\":\"driver\","
            + "\"status\":\"requested\"}";

    static String participant3 = "{\"incident_id\":\"3\","
            + "\"volunteer_type\":\"driver\","
            + "\"status\":\"requested\"}";

    
    static String message1 = "{\"incident_id\":\"1\","
            + "\"message\":\"Min plisiazetai tin perioxi, dromos kleistos\","
            + "\"sender\":\"admin\"," + "\"recipient\":\"public\"}";

    static String message2 = "{\"incident_id\":\"1\","
            + "\"message\":\"Uparxei kindunos me ladia\","
            + "\"sender\":\"volone\"," + "\"recipient\":\"public\"}";
}
