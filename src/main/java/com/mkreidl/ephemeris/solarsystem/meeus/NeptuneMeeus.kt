package com.mkreidl.ephemeris.solarsystem.meeus

import com.mkreidl.ephemeris.solarsystem.vsop87.ModelVsop87

object NeptuneMeeus : ModelMeeus {

    override fun createModel() = ModelVsop87.LBR(coefficients)

    val coefficients = arrayOf(
            arrayOf(coefficients10(), coefficients11(), coefficients12(), coefficients13(), coefficients14()),
            arrayOf(coefficients20(), coefficients21(), coefficients22(), coefficients23(), coefficients24()),
            arrayOf(coefficients30(), coefficients31(), coefficients32(), coefficients33())
    )

    private fun coefficients10() =
            arrayOf(
                    doubleArrayOf(5.31188633047, 0.00000000000, 0.00000000000),
                    doubleArrayOf(0.01798475509, 2.90101273050, 38.13303563780),
                    doubleArrayOf(0.01019727662, 0.48580923660, 1.48447270830),
                    doubleArrayOf(0.00124531845, 4.83008090682, 36.64856292950),
                    doubleArrayOf(0.00042064450, 5.41054991607, 2.96894541660),
                    doubleArrayOf(0.00037714589, 6.09221834946, 35.16409022120),
                    doubleArrayOf(0.00033784734, 1.24488865578, 76.26607127560),
                    doubleArrayOf(0.00016482741, 0.00007729261, 491.55792945680),
                    doubleArrayOf(0.00009198582, 4.93747059924, 39.61750834610),
                    doubleArrayOf(0.00008994249, 0.27462142569, 175.16605980020),
                    doubleArrayOf(0.00004216235, 1.98711914364, 73.29712585900),
                    doubleArrayOf(0.00003364818, 1.03590121818, 33.67961751290),
                    doubleArrayOf(0.00002284800, 4.20606932559, 4.45341812490),
                    doubleArrayOf(0.00001433512, 2.78340432711, 74.78159856730),
                    doubleArrayOf(0.00000900240, 2.07606702418, 109.94568878850),
                    doubleArrayOf(0.00000744996, 3.19032530145, 71.81265315070),
                    doubleArrayOf(0.00000506206, 5.74785370252, 114.39910691340),
                    doubleArrayOf(0.00000399552, 0.34972342569, 1021.24889455140),
                    doubleArrayOf(0.00000345195, 3.46186210169, 41.10198105440),
                    doubleArrayOf(0.00000306338, 0.49684039897, 0.52126486180),
                    doubleArrayOf(0.00000287322, 4.50523446022, 0.04818410980),
                    doubleArrayOf(0.00000323004, 2.24815188609, 32.19514480460),
                    doubleArrayOf(0.00000340323, 3.30369900416, 77.75054398390),
                    doubleArrayOf(0.00000266605, 4.88932609483, 0.96320784650),
                    doubleArrayOf(0.00000227079, 1.79713054538, 453.42489381900),
                    doubleArrayOf(0.00000244722, 1.24693337933, 9.56122755560),
                    doubleArrayOf(0.00000232887, 2.50459795017, 137.03302416240),
                    doubleArrayOf(0.00000282170, 2.24565579693, 146.59425171800),
                    doubleArrayOf(0.00000251941, 5.78166597292, 388.46515523820),
                    doubleArrayOf(0.00000150180, 2.99706110414, 5.93789083320),
                    doubleArrayOf(0.00000170404, 3.32390630650, 108.46121608020),
                    doubleArrayOf(0.00000151401, 2.19153094280, 33.94024994380),
                    doubleArrayOf(0.00000148295, 0.85948986145, 111.43016149680),
                    doubleArrayOf(0.00000118672, 3.67706204305, 2.44768055480),
                    doubleArrayOf(0.00000101821, 5.70539236951, 0.11187458460),
                    doubleArrayOf(0.00000097873, 2.80518260528, 8.07675484730),
                    doubleArrayOf(0.00000103054, 4.40441222000, 70.32818044240),
                    doubleArrayOf(0.00000103305, 0.04078966679, 0.26063243090),
                    doubleArrayOf(0.00000109300, 2.41599378049, 183.24281464750)
            )

    private fun coefficients11() =
            arrayOf(
                    doubleArrayOf(38.37687716731, 0.00000000000, 0.00000000000),
                    doubleArrayOf(0.00016604187, 4.86319129565, 1.48447270830),
                    doubleArrayOf(0.00015807148, 2.27923488532, 38.13303563780),
                    doubleArrayOf(0.00003334701, 3.68199676020, 76.26607127560),
                    doubleArrayOf(0.00001305840, 3.67320813491, 2.96894541660),
                    doubleArrayOf(0.00000604832, 1.50477747549, 35.16409022120),
                    doubleArrayOf(0.00000178623, 3.45318524147, 39.61750834610),
                    doubleArrayOf(0.00000106537, 2.45126138334, 4.45341812490),
                    doubleArrayOf(0.00000105747, 2.75479326550, 33.67961751290),
                    doubleArrayOf(0.00000072684, 5.48724732699, 36.64856292950),
                    doubleArrayOf(0.00000057069, 5.21649804970, 0.52126486180),
                    doubleArrayOf(0.00000057355, 1.85767603384, 114.39910691340),
                    doubleArrayOf(0.00000035368, 4.51676827545, 74.78159856730),
                    doubleArrayOf(0.00000032216, 5.90411489680, 77.75054398390),
                    doubleArrayOf(0.00000029871, 3.67043294114, 388.46515523820),
                    doubleArrayOf(0.00000028866, 5.16877529164, 9.56122755560),
                    doubleArrayOf(0.00000028742, 5.16732589024, 2.44768055480),
                    doubleArrayOf(0.00000025507, 5.24526281928, 168.05251279940),
                    doubleArrayOf(0.00000024869, 4.73193067810, 182.27960680100),
                    doubleArrayOf(0.00000020205, 5.78945415677, 1021.24889455140)
            )

    private fun coefficients12() =
            arrayOf(
                    doubleArrayOf(0.00053892649, 0.00000000000, 0.00000000000),
                    doubleArrayOf(0.00000281251, 1.19084538887, 38.13303563780),
                    doubleArrayOf(0.00000295693, 1.85520292248, 1.48447270830),
                    doubleArrayOf(0.00000270190, 5.72143228148, 76.26607127560),
                    doubleArrayOf(0.00000023023, 1.21035596452, 2.96894541660),
                    doubleArrayOf(0.00000007333, 0.54033306830, 2.44768055480),
                    doubleArrayOf(0.00000009057, 4.42544992035, 35.16409022120)
            )

    private fun coefficients13() =
            arrayOf(
                    doubleArrayOf(0.00000031254, 0.00000000000, 0.00000000000),
                    doubleArrayOf(0.00000012461, 6.04431418812, 1.48447270830),
                    doubleArrayOf(0.00000014541, 1.35337075856, 76.26607127560),
                    doubleArrayOf(0.00000011547, 6.11257808366, 38.13303563780)
            )

    private fun coefficients14() =
            arrayOf(doubleArrayOf(0.00000113998, 3.14159265359, 0.00000000000))

    private fun coefficients20() =
            arrayOf(
                    doubleArrayOf(0.03088622933, 1.44104372626, 38.13303563780),
                    doubleArrayOf(0.00027780087, 5.91271882843, 76.26607127560),
                    doubleArrayOf(0.00027623609, 0.00000000000, 0.00000000000),
                    doubleArrayOf(0.00015355490, 2.52123799481, 36.64856292950),
                    doubleArrayOf(0.00015448133, 3.50877080888, 39.61750834610),
                    doubleArrayOf(0.00001999919, 1.50998669505, 74.78159856730),
                    doubleArrayOf(0.00001967540, 4.37778195768, 1.48447270830),
                    doubleArrayOf(0.00001015137, 3.21561035875, 35.16409022120),
                    doubleArrayOf(0.00000605767, 2.80246601405, 73.29712585900),
                    doubleArrayOf(0.00000594878, 2.12892708114, 41.10198105440),
                    doubleArrayOf(0.00000588805, 3.18655882497, 2.96894541660),
                    doubleArrayOf(0.00000401830, 4.16883287237, 114.39910691340),
                    doubleArrayOf(0.00000254333, 3.27120499438, 453.42489381900),
                    doubleArrayOf(0.00000261647, 3.76722704749, 213.29909543800),
                    doubleArrayOf(0.00000279964, 1.68165309699, 77.75054398390),
                    doubleArrayOf(0.00000205590, 4.25652348864, 529.69096509460),
                    doubleArrayOf(0.00000140455, 3.52969556376, 137.03302416240)
            )

    private fun coefficients21() =
            arrayOf(
                    doubleArrayOf(0.00227279214, 3.80793089870, 38.13303563780),
                    doubleArrayOf(0.00001803120, 1.97576485377, 76.26607127560),
                    doubleArrayOf(0.00001385733, 4.82555548018, 36.64856292950),
                    doubleArrayOf(0.00001433300, 3.14159265359, 0.00000000000),
                    doubleArrayOf(0.00001073298, 6.08054240712, 39.61750834610),
                    doubleArrayOf(0.00000147903, 3.85766231348, 74.78159856730),
                    doubleArrayOf(0.00000136448, 0.47764957338, 1.48447270830),
                    doubleArrayOf(0.00000070285, 6.18782052139, 35.16409022120),
                    doubleArrayOf(0.00000051899, 5.05221791891, 73.29712585900),
                    doubleArrayOf(0.00000037273, 4.89476629246, 41.10198105440),
                    doubleArrayOf(0.00000042568, 0.30721737205, 114.39910691340),
                    doubleArrayOf(0.00000037104, 5.75999349109, 2.96894541660),
                    doubleArrayOf(0.00000026399, 5.21566335936, 213.29909543800)
            )

    private fun coefficients22() =
            arrayOf(
                    doubleArrayOf(0.00009690766, 5.57123750291, 38.13303563780),
                    doubleArrayOf(0.00000078815, 3.62705474219, 76.26607127560),
                    doubleArrayOf(0.00000071523, 0.45476688580, 36.64856292950),
                    doubleArrayOf(0.00000058646, 3.14159265359, 0.00000000000),
                    doubleArrayOf(0.00000029915, 1.60671721861, 39.61750834610),
                    doubleArrayOf(0.00000006472, 5.60736756575, 74.78159856730)
            )

    private fun coefficients23() =
            arrayOf(
                    doubleArrayOf(0.00000273423, 1.01688979072, 38.13303563780),
                    doubleArrayOf(0.00000002274, 2.36805657126, 36.64856292950),
                    doubleArrayOf(0.00000002029, 5.33364321342, 76.26607127560),
                    doubleArrayOf(0.00000002393, 0.00000000000, 0.00000000000)
            )

    private fun coefficients24() =
            arrayOf(doubleArrayOf(0.00000005728, 2.66872693322, 38.13303563780))

    private fun coefficients30() =
            arrayOf(
                    doubleArrayOf(30.07013206102, 0.00000000000, 0.00000000000),
                    doubleArrayOf(0.27062259490, 1.32999458930, 38.13303563780),
                    doubleArrayOf(0.01691764281, 3.25186138896, 36.64856292950),
                    doubleArrayOf(0.00807830737, 5.18592836167, 1.48447270830),
                    doubleArrayOf(0.00537760613, 4.52113902845, 35.16409022120),
                    doubleArrayOf(0.00495725642, 1.57105654815, 491.55792945680),
                    doubleArrayOf(0.00274571970, 1.84552256801, 175.16605980020),
                    doubleArrayOf(0.00135134095, 3.37220607384, 39.61750834610),
                    doubleArrayOf(0.00121801825, 5.79754444303, 76.26607127560),
                    doubleArrayOf(0.00100895397, 0.37702748681, 73.29712585900),
                    doubleArrayOf(0.00069791722, 3.79617226928, 2.96894541660),
                    doubleArrayOf(0.00046687838, 5.74937810094, 33.67961751290),
                    doubleArrayOf(0.00024593778, 0.50801728204, 109.94568878850),
                    doubleArrayOf(0.00016939242, 1.59422166991, 71.81265315070),
                    doubleArrayOf(0.00014229686, 1.07786112902, 74.78159856730),
                    doubleArrayOf(0.00012011825, 1.92062131635, 1021.24889455140),
                    doubleArrayOf(0.00008394731, 0.67816895547, 146.59425171800),
                    doubleArrayOf(0.00007571800, 1.07149263431, 388.46515523820),
                    doubleArrayOf(0.00005720852, 2.59059512267, 4.45341812490),
                    doubleArrayOf(0.00004839672, 1.90685991070, 41.10198105440),
                    doubleArrayOf(0.00004483492, 2.90573457534, 529.69096509460),
                    doubleArrayOf(0.00004270202, 3.41343865825, 453.42489381900),
                    doubleArrayOf(0.00004353790, 0.67985662370, 32.19514480460),
                    doubleArrayOf(0.00004420804, 1.74993796503, 108.46121608020),
                    doubleArrayOf(0.00002881063, 1.98600105123, 137.03302416240),
                    doubleArrayOf(0.00002635535, 3.09755943422, 213.29909543800),
                    doubleArrayOf(0.00003380930, 0.84810683275, 183.24281464750),
                    doubleArrayOf(0.00002878942, 3.67415901855, 350.33211960040),
                    doubleArrayOf(0.00002306293, 2.80962935724, 70.32818044240),
                    doubleArrayOf(0.00002530149, 5.79839567009, 490.07345674850),
                    doubleArrayOf(0.00002523132, 0.48630800015, 493.04240216510),
                    doubleArrayOf(0.00002087303, 0.61858378281, 33.94024994380)
            )

    private fun coefficients31() =
            arrayOf(
                    doubleArrayOf(0.00236338502, 0.70498011235, 38.13303563780),
                    doubleArrayOf(0.00013220279, 3.32015499895, 1.48447270830),
                    doubleArrayOf(0.00008621863, 6.21628951630, 35.16409022120),
                    doubleArrayOf(0.00002701740, 1.88140666779, 39.61750834610),
                    doubleArrayOf(0.00002153150, 5.16873840979, 76.26607127560),
                    doubleArrayOf(0.00002154735, 2.09431198086, 2.96894541660),
                    doubleArrayOf(0.00001463924, 1.18417031047, 33.67961751290),
                    doubleArrayOf(0.00001603165, 0.00000000000, 0.00000000000),
                    doubleArrayOf(0.00001135773, 3.91891199655, 36.64856292950),
                    doubleArrayOf(0.00000897650, 5.24122933533, 388.46515523820),
                    doubleArrayOf(0.00000789908, 0.53315484580, 168.05251279940),
                    doubleArrayOf(0.00000760030, 0.02051033644, 182.27960680100),
                    doubleArrayOf(0.00000607183, 1.07706500350, 1021.24889455140),
                    doubleArrayOf(0.00000571622, 3.40060785432, 484.44438245600),
                    doubleArrayOf(0.00000560790, 2.88685815667, 498.67147645760)
            )

    private fun coefficients32() =
            arrayOf(
                    doubleArrayOf(0.00004247412, 5.89910679117, 38.13303563780),
                    doubleArrayOf(0.00000217570, 0.34581829080, 1.48447270830),
                    doubleArrayOf(0.00000163025, 2.23872947130, 168.05251279940),
                    doubleArrayOf(0.00000156285, 4.59414467342, 182.27960680100),
                    doubleArrayOf(0.00000127141, 2.84786298079, 35.16409022120)
            )

    private fun coefficients33() =
            arrayOf(doubleArrayOf(0.00000166297, 4.55243893489, 38.13303563780))
}
