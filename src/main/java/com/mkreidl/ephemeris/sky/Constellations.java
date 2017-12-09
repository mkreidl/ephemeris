package com.mkreidl.ephemeris.sky;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Constellations
{

    public static class Constellation implements Iterable<Integer>
    {

        protected List<int[]> mEdges;
        private String name = "";

        public List<int[]> getEdges()
        {
            return mEdges;
        }

        public String getName()
        {
            return name;
        }

        private Constellation( String name, int[]... edges )
        {
            this.name = name;
            mEdges = Arrays.asList( edges );
        }

        @Override
        public Iterator<Integer> iterator()
        {
            return new Iterator<Integer>()
            {

                int edge = 0;
                int vertex = 0;

                @Override
                public boolean hasNext()
                {
                    return vertex < mEdges.get( edge ).length - 1 || edge < mEdges.size() - 1;
                }

                @Override
                public Integer next()
                {
                    if ( vertex < mEdges.get( edge ).length - 1 )
                        vertex++;
                    else
                    {
                        vertex = 0;
                        edge++;
                    }
                    return mEdges.get( edge )[vertex];
                }
            };
        }
    }

    public static final Constellation AQUARIUS = new Constellation( "Aquarius",
            new int[]{395, 1164, 157, 686},
            new int[]{157, 164, 1084, 997, 535},
            new int[]{997, 431, 164, 615, 2494, 377, 642, 797, 348},
            new int[]{507, 797, 230, 526, 377} );
    public static final Constellation AQUILA = new Constellation( "Aquila",
            new int[]{271, 250, 169, 11, 365, 217, 468, 250, 11, 119} );
    public static final Constellation ARA = new Constellation( "Ara",
            new int[]{163, 142, 200, 387},
            new int[]{200, 555},
            new int[]{243, 334} );
    public static final Constellation ARIES = new Constellation( "Aries",
            new int[]{1313, 105, 48, 337} );
    public static final Constellation CANCER = new Constellation( "Cancer",
            new int[]{665, 483, 289},
            new int[]{483, 1075, 8662} );
    public static final Constellation CAPRICORNUS = new Constellation( "Capricornus",
            new int[]{315, 190, 4758, 603, 586, 900, 376, 1102,
                    147, 354, 696, 564, 2105, 315} );
    public static final Constellation CRATER = new Constellation( "Crater",
            new int[]{1124, 1324, 308, 567, 1159, 2015},
            new int[]{567, 879, 566, 308} );
    public static final Constellation DELPHINUS = new Constellation( "Delphinus",
            new int[]{339, 394, 685, 832, 339, 540} );
    public static final Constellation GEMINI = new Constellation( "Gemini",
            new int[]{46, 808, 165, 148, 231},
            new int[]{15, 554, 294, 399, 42},
            new int[]{294, 316, 248},
            new int[]{165, 605},
            new int[]{324, 808, 400, 554, 312} );
    public static final Constellation LEO = new Constellation( "Leo",
            new int[]{21, 291, 166, 719, 857, 457, 270, 101, 95, 62, 242, 484, 547},
            new int[]{95, 242, 291, 101},
            new int[]{166, 457} );
    public static final Constellation LIBRA = new Constellation( "Libra",
            new int[]{232, 123, 102, 475, 318, 346},
            new int[]{123, 475} );
    public static final Constellation PISCES = new Constellation( "Pisces",
            new int[]{1054, 1195, 904, 1054, 2499, 330, 668,
                    732, 836, 1335, 2134, 687, 826, 527, 598,
                    698, 358, 1512, 902, 598} );
    public static final Constellation TAURUS = new Constellation( "Taurus",
            new int[]{171, 12, 257, 341, 385, 293, 26},
            new int[]{341, 276, 375, 470},
            new int[]{375, 321, 689} );
    public static final Constellation SCORPIO = new Constellation( "Scorpio",
            new int[]{79, 153, 13, 136, 75, 333, 240, 39, 181, 83, 115, 24, 214},
            new int[]{1464, 79, 152, 460} );
    public static final Constellation SAGITTARIUS = new Constellation( "Sagittarius",
            new int[]{193, 36, 118, 168},
            new int[]{118, 135, 444},
            new int[]{135, 208, 51, 154, 288},
            new int[]{51, 239, 100} );
    public static final Constellation VIRGO = new Constellation( "Virgo",
            new int[]{463, 329, 537, 592, 463, 342, 251, 568, 459},
            new int[]{14, 787, 342, 255, 138},
            new int[]{251, 673, 368} );

    public static final Constellation ANDROMEDA = new Constellation( "Andromeda",
            new int[]{817, 553, 777, 225, 758, 712, 336},
            new int[]{55, 445, 928, 658, 311},
            new int[]{72, 55, 225, 54},
            new int[]{712, 604, 417},
            new int[]{55, 758} );
    public static final Constellation AURIGA = new Constellation( "Auriga",
            new int[]{5, 367, 40, 103, 26/* =Beta tauri; alternatively: Chi aurigae=1197 */, 112, 204, 5},
            new int[]{40, 5, 167, 378} );
    public static final Constellation BOOTES = new Constellation( "Boötes",
            new int[]{110, 2, 317, 180, 286, 279, 117, 2, 829},
            new int[]{180, 621, 945, 549, 621} );
    public static final Constellation CANIS_MAIOR = new Constellation( "Canis Major",
            new int[]{0, 45, 488, 450, 22, 277, 35, 435, 88},
            new int[]{35, 179, 1095, 0, 780, 590, 559, 780} );
    public static final Constellation CANIS_MINOR = new Constellation( "Canis Minor",
            new int[]{7, 155} );
    public static final Constellation CASSIOPEIA = new Constellation( "Cassiopeia",
            new int[]{73, 66, 90, 109, 253} );
    public static final Constellation CENTAURUS = new Constellation( "Centaurus",
            new int[]{3, 10, 76, 63, 474, 99, 462},
            new int[]{474, 497, 1893},
            new int[]{63, 94, 262, 57, 548, 767, 420, 453, 94, 76},
            new int[]{199, 78, 420, 262, 122, 682} );
    public static final Constellation CEPHEUS = new Constellation( "Cepheus",
            new int[]{267, 87, 218, 215, 292, 383, 246, 87} );
    public static final Constellation CETUS = new Constellation( "Cetus",
            new int[]{275, 93, 1121, 676, 688, 1365, 275, 557,
                    182, 371, 320, 272, 305, 52, 285, 371} );
    public static final Constellation CORONA_AUSTRALIS = new Constellation( "Corona Australis",
            new int[]{1392, 1482, 584, 585, 994, 1192} );
    public static final Constellation CORONA_BOREALIS = new Constellation( "Corona Borealis",
            new int[]{602, 353, 68, 429, 1038, 609} );
    public static final Constellation CORVUS = new Constellation( "Corvus",
            new int[]{530, 172, 98, 162, 107, 172},
            new int[]{162, 720} );
    public static final Constellation CYGNUS = new Constellation( "Cygnus",
            new int[]{18, 64, 466, 189},
            new int[]{393, 401, 146, 64, 89, 213} );
    private static final Constellation DRACO = new Constellation( "Draco",
            new int[]{382, 69, 130, 1388, 382, 187, 423},
            new int[]{187, 636, 314},
            new int[]{636, 1265, 207, 121, 521, 233, 343, 452, 428} );
    private static final Constellation ERIDANUS = new Constellation( "Eridanus",
            new int[]{129, 528, 479, 543, 161, 818, 298, 372, 1254,
                    461, 660, 863, 573, 355, 678, 646, 1055, 1071,
                    907, 412, 493, 307, 616, 977, 748, 576, 1184,
                    659, 306, 359, 8} );
    private static final Constellation EQUULEUS = new Constellation( "Equuleus",
            new int[]{478, 889, 1114} );
    private static final Constellation HERCULES = new Constellation( "Hercules",
            new int[]{464, 629, 296, 134, 126, 282},
            new int[]{126, 380},
            new int[]{296, 203},
            new int[]{134, 477},
            new int[]{443, 923, 203, 477, 202, 265, 361, 422} );
    public static final Constellation HYDRA = new Constellation( "Hydra",
            new int[]{765, 714, 840, 613, 254, 191, 456, 472, 47,
                    591, 1001, 328, 411, 192, 566, 879, 301, 694} );
    public static final Constellation LEPUS = new Constellation( "Lepus",
            new int[]{97, 139, 323, 410, 1088, 362, 303, 97, 235, 210, 139},
            new int[]{703, 235, 763} );
    public static final Constellation LUPUS = new Constellation( "Lupus",
            new int[]{264, 128, 252, 454, 263},
            new int[]{128, 216},
            new int[]{309, 216, 111, 77} );
    public static final Constellation LYRA = new Constellation( "Lyra",
            new int[]{4, 771, 274, 222, 716, 771} );
    public static final Constellation OPHIUCHUS = new Constellation( "Ophiuchus",
            new int[]{705, 229, 85, 127, 59, 212, 96, 695, 823, 899},
            new int[]{85, 96, 1040, 221, 120, 415, 212},
            new int[]{127, 381, 244} );
    public static final Constellation ORION = new Constellation( "Orion",
            new int[]{6, 67, 25, 299, 9, 53, 56},
            new int[]{25, 867, 366, 356, 209, 762, 558},
            new int[]{807, 819, 589, 873, 1034},
            new int[]{9, 589} );
    public static final Constellation PEGASUS = new Constellation( "Pegasus",
            new int[]{82, 297, 259, 91, 84, 160},
            new int[]{84, 54, 137, 91}  // 54=Alpha Andromedae
    );
    public static final Constellation PERSEUS = new Constellation( "Perseus",
            new int[]{61, 33, 158},
            new int[]{33, 174, 150, 140} );
    public static final Constellation PISCIS_AUSTRINUS = new Constellation( "Piscis Austrinus",
            new int[]{16, 633, 862, 711, 901, 746, 1647, 1468, 618, 16} );
    public static final Constellation SAGITTA = new Constellation( "Sagitta",
            new int[]{782, 416, 783},
            new int[]{416, 280} );
    public static final Constellation SERPENS = new Constellation( "Serpens",
            new int[]{350, 438, 575, 922, 350, 406, 108, 364, 295, 120},
            new int[]{85, 302, 244, 224, 1028} );
    public static final Constellation URSA_MAIOR = new Constellation( "Ursa Major",
            new int[]{38, 74, 31, 236, 34, 349, 249, 404, 349},
            new int[]{201, 206, 404, 80, 86, 236},
            new int[]{325, 206},
            new int[]{184, 1138, 176, 363, 2383, 281},
            new int[]{810, 2383},
            new int[]{273, 1138},
            new int[]{80, 34},
            new int[]{86, 363} );
    public static final Constellation URSA_MINOR = new Constellation( "Ursa Minor",
            new int[]{50, 769, 650, 728, 58, 186, 1528, 728} );
    public static final Constellation TRIANGULUM = new Constellation( "Triangulum",
            new int[]{170, 261, 518, 170} );

    public static final Constellation PUPPIS = new Constellation( "Puppis",
            new int[]{133, 628, 241, 496, 116, 205, 159, 223, 70, 116} );
    public static final Constellation SCULPTOR = new Constellation( "Sculptor",
            new int[]{718, 976, 815, 785} );
    public static final Constellation CHAMAELEON = new Constellation( "Chamaeleon",
            new int[]{560, 580, 850, 670, 1445, 580} );
    public static final Constellation FORNAX = new Constellation( "Fornax",
            new int[]{446, 855, 1104} );
    public static final Constellation LACERTA = new Constellation( "Lacerta",
            new int[]{916, 861, 775, 396, 834, 975, 775, 974, 916, 597} );
    public static final Constellation MONOCEROS = new Constellation( "Monoceros",
            new int[]{480, 741, 606, 998, 509},
            new int[]{606, 868, 838, 892, 1072},
            new int[]{868, 892} );
    public static final Constellation MUSCA = new Constellation( "Musca",
            new int[]{114, 185, 332, 451, 114, 582, 340} );
    public static final Constellation VOLANS = new Constellation( "Volans",
            new int[]{515, 391, 510, 397, 489, 753, 391} );
    public static final Constellation VULPECULA = new Constellation( "Vulpecula",
            new int[]{925, 1053, 982, 842, 1221} );
    public static final Constellation CAMELOPARDALIS = new Constellation( "Camelopardalis",
            new int[]{866, 536, 702, 1033, 865, 630},
            new int[]{702, 1258, 954} );
    public static final Constellation CAELUM = new Constellation( "Caelum",
            new int[]{1752, 843, 1712, 953} );
    public static final Constellation COMA_BERENICES = new Constellation( "Coma Berenices",
            new int[]{2096, 672, 766} );
    public static final Constellation INDUS = new Constellation( "Indus",
            new int[]{194, 344, 803, 796, 194} );
    public static final Constellation CANES_VENATICI = new Constellation( "Canes Venatici",
            new int[]{156, 671} );
    public static final Constellation CARINA = new Constellation( "Carina",
            new int[]{1, 37, 71, 175, 27, 237, 124,} );
    public static final Constellation HYDRUS = new Constellation( "Hydrus",
            new int[]{143, 220, 131, 143} );
    public static final Constellation LEO_MINOR = new Constellation( "Leo Minor",
            new int[]{955, 878, 632, 419, 1137, 878} );
    public static final Constellation GRUS = new Constellation( "Grus",
            new int[]{593, 284, 60, 30, 506, 1249, 860, 177},
            new int[]{60, 506} );
    public static final Constellation CRUX = new Constellation( "Crux",
            new int[]{19, 23},
            new int[]{17, 132} );
    public static final Constellation LYNX = new Constellation( "Lynx",
            new int[]{196, 413, 962, 502, 664, 1044, 752, 874} );
    public static final Constellation ANTLIA = new Constellation( "Antlia",
            new int[]{1002, 666, 909} );
    public static final Constellation PICTOR = new Constellation( "Pictor",
            new int[]{227, 908, 433} );
    public static final Constellation MICROSCOPIUM = new Constellation( "Microscopium",
            new int[]{1434, 1092, 1142, 1308, 1852, 1434} );
    public static final Constellation RETICULUM = new Constellation( "Reticulum",
            new int[]{245, 432, 961, 837, 245} );
    public static final Constellation OCTANS = new Constellation( "Octans",
            new int[]{610, 726, 388, 610} );
    public static final Constellation APUS = new Constellation( "Apus",
            new int[]{421, 1098, 655, 465,} );
    public static final Constellation HOROLOGIUM = new Constellation( "Horologium",
            new int[]{441, 2583, 2322, 2069, 1834, 1581} );
    public static final Constellation PAVO = new Constellation( "Pavo",
            new int[]{335, 757, 770, 638, 310, 43, 641, 266, 310, 499},
            new int[]{523, 310, 841, 757} );
    public static final Constellation PHOENIX = new Constellation( "Phoenix",
            new int[]{81, 234, 260, 486, 476, 234, 455} );
    public static final Constellation PYXIS = new Constellation( "Pyxis",
            new int[]{501, 351, 519} );
    public static final Constellation SCUTUM = new Constellation( "Scutum",
            new int[]{440, 637, 1154, 1127, 440} );
    public static final Constellation DORADO = new Constellation( "Dorado",
            new int[]{226, 386, 750, 1058, 386, 1147, 226, 661} );
    public static final Constellation VELA = new Constellation( "Vela",
            new int[]{32, 44, 92, 300, 113, 326, 65, 32} );
    public static final Constellation SEXTANS = new Constellation( "Sextans",
            new int[]{1721, 887, 1801, 2075} );
    public static final Constellation TRIANGULUM_AUSTRALE = new Constellation( "Triangulum Australe",
            new int[]{41, 141, 151, 41} );
    public static final Constellation MENSA = new Constellation( "Mensa",
            new int[]{1797, 2031, 2758, 2326} );
    public static final Constellation COLUMBA = new Constellation( "Columba",
            new int[]{449, 106, 195, 764, 195},
            new int[]{195, 494} );
    public static final Constellation TELESCOPIUM = new Constellation( "Telescopium",
            new int[]{287, 594} );
    public static final Constellation TUCANA = new Constellation( "Tucana",
            new int[]{144, 512, 776, 643, 903, 881, 144} );
    public static final Constellation NORMA = new Constellation( "Norma",
            new int[]{1597, 871, 1153, 1064, 1597} );
    public static final Constellation CIRCINUS = new Constellation( "Circinus",
            new int[]{563, 211, 910} );

    public static final Constellation[] ECLIPTIC = new Constellation[]{
            AQUARIUS,
            ARIES,
            CANCER,
            CAPRICORNUS,
            GEMINI,
            LEO,
            LIBRA,
            OPHIUCHUS,
            PISCES,
            SAGITTARIUS,
            SCORPIO,
            TAURUS,
            VIRGO
    };

    public static final Constellation[] PTOLEMAIC = new Constellation[]{
            AQUARIUS,
            ARIES,
            CANCER,
            CAPRICORNUS,
            GEMINI,
            LEO,
            LIBRA,
            OPHIUCHUS,
            PISCES,
            SAGITTARIUS,
            SCORPIO,
            TAURUS,
            VIRGO,

            ANDROMEDA,
            AQUILA,
            ARA,
            AURIGA,
            BOOTES,
            CANIS_MAIOR,
            CANIS_MINOR,
            CASSIOPEIA,
            CENTAURUS,
            CEPHEUS,
            CETUS,
            CORONA_AUSTRALIS,
            CORONA_BOREALIS,
            CORVUS,
            CRATER,
            CYGNUS,
            DELPHINUS,
            DRACO,
            EQUULEUS,
            ERIDANUS,
            HERCULES,
            HYDRA,
            LEPUS,
            LUPUS,
            LYRA,
            ORION,
            PEGASUS,
            PERSEUS,
            PISCIS_AUSTRINUS,
            SAGITTA,
            SERPENS,
            TRIANGULUM,
            URSA_MAIOR,
            URSA_MINOR
    };

    public static final Constellation[] MODERN = new Constellation[]{
            PUPPIS, SCULPTOR, CHAMAELEON, FORNAX, LACERTA, MONOCEROS, MUSCA, VOLANS, VULPECULA,
            CAMELOPARDALIS, CAELUM, COMA_BERENICES, INDUS, CANES_VENATICI, CARINA, HYDRUS,
            LEO_MINOR, GRUS, CRUX, LYNX, ANTLIA, PICTOR, MICROSCOPIUM, RETICULUM, OCTANS, APUS,
            HOROLOGIUM, PAVO, PHOENIX, PYXIS, SCUTUM, DORADO, VELA, SEXTANS, TRIANGULUM_AUSTRALE,
            MENSA, COLUMBA, TELESCOPIUM, TUCANA, NORMA, CIRCINUS
    };

    public static final Constellation[] ALL = new Constellation[PTOLEMAIC.length + MODERN.length];

    static
    {
        System.arraycopy( PTOLEMAIC, 0, ALL, 0, PTOLEMAIC.length );
        System.arraycopy( MODERN, 0, ALL, PTOLEMAIC.length, MODERN.length );
    }

    public static final Iterator<Constellation[]> CURSOR = new Iterator<Constellation[]>()
    {

        private int index = 0;

        @Override
        public boolean hasNext()
        {
            return true;
        }

        @Override
        public Constellation[] next()
        {
            if ( index >= MODERN.length )
                index = 0;
            return new Constellation[]{MODERN[index++]};
        }
    };

}
