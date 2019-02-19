package com.mkreidl.ephemeris.stars

object ConstellationsCatalog {

    fun findByName(name: String) = ALL.find { it.name == name }

    private val AQUARIUS = Constellation("Aquarius",
            intArrayOf(395, 1164, 157, 686),
            intArrayOf(157, 164, 1084, 997, 535),
            intArrayOf(997, 431, 164, 615, 2494, 377, 642, 797, 348),
            intArrayOf(507, 797, 230, 526, 377))
    private val AQUILA = Constellation("Aquila",
            intArrayOf(271, 250, 169, 11, 365, 217, 468, 250, 11, 119))
    private val ARA = Constellation("Ara",
            intArrayOf(163, 142, 200, 387),
            intArrayOf(200, 555),
            intArrayOf(243, 334))
    private val ARIES = Constellation("Aries",
            intArrayOf(1313, 105, 48, 337))
    private val CANCER = Constellation("Cancer",
            intArrayOf(665, 483, 289),
            intArrayOf(483, 1075, 529))
    private val CAPRICORNUS = Constellation("Capricornus",
            intArrayOf(315, 190, 603, 586, 900, 376, 1102, 147, 354, 696, 564, 2105, 315))
    private val CRATER = Constellation("Crater",
            intArrayOf(1124, 1324, 308, 567, 1159, 2015),
            intArrayOf(567, 879, 566, 308))
    private val DELPHINUS = Constellation("Delphinus",
            intArrayOf(339, 394, 685, 832, 339, 540))
    private val GEMINI = Constellation("Gemini",
            intArrayOf(46, 808, 165, 148, 231),
            intArrayOf(15, 554, 294, 399, 42),
            intArrayOf(294, 316, 248),
            intArrayOf(165, 605),
            intArrayOf(324, 808, 400, 554, 312))
    private val LEO = Constellation("Leo",
            intArrayOf(21, 291, 166, 719, 857, 457, 270, 101, 95, 62, 242, 484, 547),
            intArrayOf(95, 242, 291, 101),
            intArrayOf(166, 457))
    private val LIBRA = Constellation("Libra",
            intArrayOf(232, 123, 102, 475, 318, 346),
            intArrayOf(123, 475))
    private val PISCES = Constellation("Pisces",
            intArrayOf(1054, 1195, 904, 1054, 2499, 330, 668, 732, 836, 1335, 2134, 687, 826, 527, 598, 698, 358, 1512, 902, 598))
    private val TAURUS = Constellation("Taurus",
            intArrayOf(171, 12, 257, 341, 385, 293, 26),
            intArrayOf(341, 276, 375, 470),
            intArrayOf(375, 321, 689))
    private val SCORPIO = Constellation("Scorpio",
            intArrayOf(79, 153, 13, 136, 75, 333, 240, 39, 181, 83, 115, 24, 214),
            intArrayOf(1464, 79, 152, 460))
    private val SAGITTARIUS = Constellation("Sagittarius",
            intArrayOf(193, 36, 118, 168),
            intArrayOf(118, 135, 444),
            intArrayOf(135, 208, 51, 154, 288),
            intArrayOf(51, 239, 100))
    private val VIRGO = Constellation("Virgo",
            intArrayOf(463, 329, 537, 592, 463, 342, 251, 568, 459),
            intArrayOf(14, 787, 342, 255, 138),
            intArrayOf(251, 673, 368))

    private val ANDROMEDA = Constellation("Andromeda",
            intArrayOf(817, 553, 777, 225, 758, 712, 336),
            intArrayOf(55, 445, 928, 658, 311),
            intArrayOf(72, 55, 225, 54),
            intArrayOf(712, 604, 417),
            intArrayOf(55, 758))
    private val AURIGA = Constellation("Auriga",
            intArrayOf(5, 367, 40, 103, 26/* =Beta tauri; alternatively: Chi aurigae=1197 */, 112, 204, 5),
            intArrayOf(40, 5, 167, 378))
    private val BOOTES = Constellation("Bootes",
            intArrayOf(110, 2, 317, 180, 286, 279, 117, 2, 829),
            intArrayOf(180, 621, 945, 549, 621))
    private val CANIS_MAIOR = Constellation("Canis Maior",
            intArrayOf(0, 45, 488, 450, 22, 277, 35, 435, 88),
            intArrayOf(35, 179, 1095, 0, 780, 590, 559, 780))
    private val CANIS_MINOR = Constellation("Canis Minor",
            intArrayOf(7, 155))
    private val CASSIOPEIA = Constellation("Cassiopeia",
            intArrayOf(73, 66, 90, 109, 253))
    private val CENTAURUS = Constellation("Centaurus",
            intArrayOf(3, 10, 76, 63, 474, 99, 462),
            intArrayOf(474, 497, 1893),
            intArrayOf(63, 94, 262, 57, 548, 767, 420, 453, 94, 76),
            intArrayOf(199, 78, 420, 262, 122, 682))
    private val CEPHEUS = Constellation("Cepheus",
            intArrayOf(267, 87, 218, 215, 292, 383, 246, 87))
    private val CETUS = Constellation("Cetus",
            intArrayOf(275, 93, 1121, 676, 688, 1365, 275, 557, 182, 371, 320, 272, 305, 52, 285, 371))
    private val CORONA_AUSTRALIS = Constellation("Corona Australis",
            intArrayOf(1392, 1482, 584, 585, 994, 1192))
    private val CORONA_BOREALIS = Constellation("Corona Borealis",
            intArrayOf(602, 353, 68, 429, 1038, 609))
    private val CORVUS = Constellation("Corvus",
            intArrayOf(530, 172, 98, 162, 107, 172),
            intArrayOf(162, 720))
    private val CYGNUS = Constellation("Cygnus",
            intArrayOf(18, 64, 466, 189),
            intArrayOf(393, 401, 146, 64, 89, 213))
    private val DRACO = Constellation("Draco",
            intArrayOf(382, 69, 130, 1388, 382, 187, 423),
            intArrayOf(187, 636, 314),
            intArrayOf(636, 1265, 207, 121, 521, 233, 343, 452, 428))
    private val ERIDANUS = Constellation("Eridanus",
            intArrayOf(129, 528, 479, 543, 161, 818, 298, 372, 1254, 461, 660, 863, 573, 355, 678, 646, 1055, 1071, 907, 412, 493, 307, 616, 977, 748, 576, 1184, 659, 306, 359, 8))
    private val EQUULEUS = Constellation("Equuleus",
            intArrayOf(478, 889, 1114))
    private val HERCULES = Constellation("Hercules",
            intArrayOf(464, 629, 296, 134, 126, 282),
            intArrayOf(126, 380),
            intArrayOf(296, 203),
            intArrayOf(134, 477),
            intArrayOf(443, 923, 203, 477, 202, 265, 361, 422))
    private val HYDRA = Constellation("Hydra",
            intArrayOf(765, 714, 840, 613, 254, 191, 456, 472, 47, 591, 1001, 328, 411, 192, 566, 879, 301, 694))
    private val LEPUS = Constellation("Lepus",
            intArrayOf(97, 139, 323, 410, 1088, 362, 303, 97, 235, 210, 139),
            intArrayOf(703, 235, 763))
    private val LUPUS = Constellation("Lupus",
            intArrayOf(264, 128, 252, 454, 263),
            intArrayOf(128, 216),
            intArrayOf(309, 216, 111, 77))
    private val LYRA = Constellation("Lyra",
            intArrayOf(4, 771, 274, 222, 716, 771))
    private val OPHIUCHUS = Constellation("Ophiuchus",
            intArrayOf(705, 229, 85, 127, 59, 212, 96, 695, 823, 899),
            intArrayOf(85, 96, 1040, 221, 120, 415, 212),
            intArrayOf(127, 381, 244))
    private val ORION = Constellation("Orion",
            intArrayOf(6, 67, 25, 299, 9, 53, 56),
            intArrayOf(25, 867, 366, 356, 209, 762, 558),
            intArrayOf(807, 819, 589, 873, 1034),
            intArrayOf(9, 589))
    private val PEGASUS = Constellation("Pegasus",
            intArrayOf(82, 297, 259, 91, 84, 160),
            intArrayOf(84, 54, 137, 91)  // 54=Alpha Andromedae
    )
    private val PERSEUS = Constellation("Perseus",
            intArrayOf(61, 33, 158),
            intArrayOf(33, 174, 150, 140))
    private val PISCIS_AUSTRINUS = Constellation("Piscis Austrinus",
            intArrayOf(16, 633, 862, 711, 901, 746, 1647, 1468, 618, 16))
    private val SAGITTA = Constellation("Sagitta",
            intArrayOf(782, 416, 783),
            intArrayOf(416, 280))
    private val SERPENS = Constellation("Serpens",
            intArrayOf(350, 438, 575, 922, 350, 406, 108, 364, 295, 120),
            intArrayOf(85, 302, 244, 224, 1028))
    private val URSA_MAIOR = Constellation("Ursa Maior",
            intArrayOf(38, 74, 31, 236, 34, 349, 249, 404, 349),
            intArrayOf(201, 206, 404, 80, 86, 236),
            intArrayOf(325, 206),
            intArrayOf(184, 1138, 176, 363, 2383, 281),
            intArrayOf(810, 2383),
            intArrayOf(273, 1138),
            intArrayOf(80, 34),
            intArrayOf(86, 363))
    private val URSA_MINOR = Constellation("Ursa Minor",
            intArrayOf(50, 769, 650, 728, 58, 186, 1528, 728))
    private val TRIANGULUM = Constellation("Triangulum",
            intArrayOf(170, 261, 518, 170))

    private val PUPPIS = Constellation("Puppis",
            intArrayOf(133, 628, 241, 496, 116, 205, 159, 223, 70, 116))
    private val SCULPTOR = Constellation("Sculptor",
            intArrayOf(718, 976, 815, 785))
    private val CHAMAELEON = Constellation("Chamaeleon",
            intArrayOf(560, 580, 850, 670, 1445, 580))
    private val FORNAX = Constellation("Fornax",
            intArrayOf(446, 855, 1104))
    private val LACERTA = Constellation("Lacerta",
            intArrayOf(916, 861, 775, 396, 834, 975, 775, 974, 916, 597))
    private val MONOCEROS = Constellation("Monoceros",
            intArrayOf(480, 741, 606, 998, 509),
            intArrayOf(606, 868, 838, 892, 1072),
            intArrayOf(868, 892))
    private val MUSCA = Constellation("Musca",
            intArrayOf(114, 185, 332, 451, 114, 582, 340))
    private val VOLANS = Constellation("Volans",
            intArrayOf(515, 391, 510, 397, 489, 753, 391))
    private val VULPECULA = Constellation("Vulpecula",
            intArrayOf(925, 1053, 982, 842, 1221))
    private val CAMELOPARDALIS = Constellation("Camelopardalis",
            intArrayOf(866, 536, 702, 1033, 865, 630),
            intArrayOf(702, 1258, 954))
    private val CAELUM = Constellation("Caelum",
            intArrayOf(1752, 843, 1712, 953))
    private val COMA_BERENICES = Constellation("Coma Berenices",
            intArrayOf(2096, 672, 766))
    private val INDUS = Constellation("Indus",
            intArrayOf(194, 344, 803, 796, 194))
    private val CANES_VENATICI = Constellation("Canes Venatici",
            intArrayOf(156, 671))
    private val CARINA = Constellation("Carina",
            intArrayOf(1, 37, 71, 175, 27, 237, 124))
    private val HYDRUS = Constellation("Hydrus",
            intArrayOf(143, 220, 131, 143))
    private val LEO_MINOR = Constellation("Leo Minor",
            intArrayOf(955, 878, 632, 419, 1137, 878))
    private val GRUS = Constellation("Grus",
            intArrayOf(593, 284, 60, 30, 506, 1249, 860, 177),
            intArrayOf(60, 506))
    private val CRUX = Constellation("Crux",
            intArrayOf(19, 23),
            intArrayOf(17, 132))
    private val LYNX = Constellation("Lynx",
            intArrayOf(196, 413, 962, 502, 664, 1044, 752, 874))
    private val ANTLIA = Constellation("Antlia",
            intArrayOf(1002, 666, 909))
    private val PICTOR = Constellation("Pictor",
            intArrayOf(227, 908, 433))
    private val MICROSCOPIUM = Constellation("Microscopium",
            intArrayOf(1434, 1092, 1142, 1308, 1852, 1434))
    private val RETICULUM = Constellation("Reticulum",
            intArrayOf(245, 432, 961, 837, 245))
    private val OCTANS = Constellation("Octans",
            intArrayOf(610, 726, 388, 610))
    private val APUS = Constellation("Apus",
            intArrayOf(421, 1098, 655, 465))
    private val HOROLOGIUM = Constellation("Horologium",
            intArrayOf(441, 2583, 2322, 2069, 1834, 1581))
    private val PAVO = Constellation("Pavo",
            intArrayOf(335, 757, 770, 638, 310, 43, 641, 266, 310, 499),
            intArrayOf(523, 310, 841, 757))
    private val PHOENIX = Constellation("Phoenix",
            intArrayOf(81, 234, 260, 486, 476, 234, 455))
    private val PYXIS = Constellation("Pyxis",
            intArrayOf(501, 351, 519))
    private val SCUTUM = Constellation("Scutum",
            intArrayOf(440, 637, 1154, 1127, 440))
    private val DORADO = Constellation("Dorado",
            intArrayOf(226, 386, 750, 1058, 386, 1147, 226, 661))
    private val VELA = Constellation("Vela",
            intArrayOf(32, 44, 92, 300, 113, 326, 65, 32))
    private val SEXTANS = Constellation("Sextans",
            intArrayOf(1721, 887, 1801, 2075))
    private val TRIANGULUM_AUSTRALE = Constellation("Triangulum Australe",
            intArrayOf(41, 141, 151, 41))
    private val MENSA = Constellation("Mensa",
            intArrayOf(1797, 2031, 2758, 2326))
    private val COLUMBA = Constellation("Columba",
            intArrayOf(449, 106, 195, 764, 195),
            intArrayOf(195, 494))
    private val TELESCOPIUM = Constellation("Telescopium",
            intArrayOf(287, 594))
    private val TUCANA = Constellation("Tucana",
            intArrayOf(144, 512, 776, 643, 903, 881, 144))
    private val NORMA = Constellation("Norma",
            intArrayOf(1597, 871, 1153, 1064, 1597))
    private val CIRCINUS = Constellation("Circinus",
            intArrayOf(563, 211, 910))

    private val PTOLEMAIC = arrayOf(
            AQUARIUS, ARIES, CANCER, CAPRICORNUS, GEMINI, LEO, LIBRA, OPHIUCHUS, PISCES,
            SAGITTARIUS, SCORPIO, TAURUS, VIRGO, ANDROMEDA, AQUILA, ARA, AURIGA, BOOTES, CANIS_MAIOR, CANIS_MINOR,
            CASSIOPEIA, CENTAURUS, CEPHEUS, CETUS, CORONA_AUSTRALIS, CORONA_BOREALIS, CORVUS, CRATER, CYGNUS,
            DELPHINUS, DRACO, EQUULEUS, ERIDANUS, HERCULES, HYDRA, LEPUS, LUPUS, LYRA, ORION, PEGASUS, PERSEUS,
            PISCIS_AUSTRINUS, SAGITTA, SERPENS, TRIANGULUM, URSA_MAIOR, URSA_MINOR
    )

    private val MODERN = arrayOf(
            PUPPIS, SCULPTOR, CHAMAELEON, FORNAX, LACERTA, MONOCEROS, MUSCA, VOLANS, VULPECULA,
            CAMELOPARDALIS, CAELUM, COMA_BERENICES, INDUS, CANES_VENATICI, CARINA, HYDRUS, LEO_MINOR, GRUS, CRUX,
            LYNX, ANTLIA, PICTOR, MICROSCOPIUM, RETICULUM, OCTANS, APUS, HOROLOGIUM, PAVO, PHOENIX, PYXIS, SCUTUM,
            DORADO, VELA, SEXTANS, TRIANGULUM_AUSTRALE, MENSA, COLUMBA, TELESCOPIUM, TUCANA, NORMA, CIRCINUS
    )

    private val ALL = listOf(PTOLEMAIC, MODERN).flatMap { it.asList() }.toList()
}
