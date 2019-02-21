package com.mkreidl.ephemeris.solarsystem.meeus

import com.mkreidl.ephemeris.solarsystem.vsop87.ModelVsop87

object UranusMeeus : ModelMeeus {

    override fun createModel() = ModelVsop87.LBR(coefficients)

    val coefficients = arrayOf(
            arrayOf(coefficients10(), coefficients11(), coefficients12(), coefficients13(), coefficients14()),
            arrayOf(coefficients20(), coefficients21(), coefficients22(), coefficients23(), coefficients24()),
            arrayOf(coefficients30(), coefficients31(), coefficients32(), coefficients33(), coefficients34())
    )

    private fun coefficients10() =
            arrayOf(
                    doubleArrayOf(5.48129294299, 0.00000000000, 0.00000000000),
                    doubleArrayOf(0.09260408252, 0.89106421530, 74.78159856730),
                    doubleArrayOf(0.01504247826, 3.62719262195, 1.48447270830),
                    doubleArrayOf(0.00365981718, 1.89962189068, 73.29712585900),
                    doubleArrayOf(0.00272328132, 3.35823710524, 149.56319713460),
                    doubleArrayOf(0.00070328499, 5.39254431993, 63.73589830340),
                    doubleArrayOf(0.00068892609, 6.09292489045, 76.26607127560),
                    doubleArrayOf(0.00061998592, 2.26952040469, 2.96894541660),
                    doubleArrayOf(0.00061950714, 2.85098907565, 11.04570026390),
                    doubleArrayOf(0.00026468869, 3.14152087888, 71.81265315070),
                    doubleArrayOf(0.00025710505, 6.11379842935, 454.90936652730),
                    doubleArrayOf(0.00021078897, 4.36059465144, 148.07872442630),
                    doubleArrayOf(0.00017818665, 1.74436982544, 36.64856292950),
                    doubleArrayOf(0.00014613471, 4.73732047977, 3.93215326310),
                    doubleArrayOf(0.00011162535, 5.82681993692, 224.34479570190),
                    doubleArrayOf(0.00010997934, 0.48865493179, 138.51749687070),
                    doubleArrayOf(0.00009527487, 2.95516893093, 35.16409022120),
                    doubleArrayOf(0.00007545543, 5.23626440666, 109.94568878850),
                    doubleArrayOf(0.00004220170, 3.23328535514, 70.84944530420),
                    doubleArrayOf(0.00004051850, 2.27754158724, 151.04766984290),
                    doubleArrayOf(0.00003354607, 1.06549008887, 4.45341812490),
                    doubleArrayOf(0.00002926671, 4.62903695486, 9.56122755560),
                    doubleArrayOf(0.00003490352, 5.48305567292, 146.59425171800),
                    doubleArrayOf(0.00003144093, 4.75199307603, 77.75054398390),
                    doubleArrayOf(0.00002922410, 5.35236743380, 85.82729883120),
                    doubleArrayOf(0.00002272790, 4.36600802756, 70.32818044240),
                    doubleArrayOf(0.00002051209, 1.51773563459, 0.11187458460),
                    doubleArrayOf(0.00002148599, 0.60745800902, 38.13303563780),
                    doubleArrayOf(0.00001991726, 4.92437290826, 277.03499374140),
                    doubleArrayOf(0.00001376208, 2.04281409054, 65.22037101170),
                    doubleArrayOf(0.00001666910, 3.62744580852, 380.12776796000),
                    doubleArrayOf(0.00001284183, 3.11346336879, 202.25339517410),
                    doubleArrayOf(0.00001150416, 0.93344454002, 3.18139373770),
                    doubleArrayOf(0.00001533223, 2.58593414266, 52.69019803950),
                    doubleArrayOf(0.00001281641, 0.54269869505, 222.86032299360),
                    doubleArrayOf(0.00001372100, 4.19641615561, 111.43016149680),
                    doubleArrayOf(0.00001220998, 0.19901396193, 108.46121608020),
                    doubleArrayOf(0.00000946195, 1.19249463066, 127.47179660680),
                    doubleArrayOf(0.00001150993, 4.17898207045, 33.67961751290),
                    doubleArrayOf(0.00001244342, 0.91612680579, 2.44768055480),
                    doubleArrayOf(0.00001072008, 0.23564502877, 62.25142559510),
                    doubleArrayOf(0.00001090461, 1.77501638912, 12.53017297220),
                    doubleArrayOf(0.00000707875, 5.18285226584, 213.29909543800),
                    doubleArrayOf(0.00000653401, 0.96586909116, 78.71375183040),
                    doubleArrayOf(0.00000627562, 0.18210181975, 984.60033162190),
                    doubleArrayOf(0.00000524495, 2.01276706996, 299.12639426920),
                    doubleArrayOf(0.00000559370, 3.35776737704, 0.52126486180),
                    doubleArrayOf(0.00000606827, 5.43209728952, 529.69096509460),
                    doubleArrayOf(0.00000404891, 5.98689011389, 8.07675484730),
                    doubleArrayOf(0.00000467211, 0.41484068933, 145.10977900970),
                    doubleArrayOf(0.00000471288, 1.40664336447, 184.72728735580),
                    doubleArrayOf(0.00000483219, 2.10553990154, 0.96320784650),
                    doubleArrayOf(0.00000395614, 5.87039580949, 351.81659230870),
                    doubleArrayOf(0.00000433532, 5.52142978255, 183.24281464750),
                    doubleArrayOf(0.00000309885, 5.83301304674, 145.63104387150),
                    doubleArrayOf(0.00000378609, 2.34975805006, 56.62235130260),
                    doubleArrayOf(0.00000398996, 0.33810765436, 415.55249061210),
                    doubleArrayOf(0.00000300379, 5.64353974146, 22.09140052780),
                    doubleArrayOf(0.00000249229, 4.74617120584, 225.82926841020),
                    doubleArrayOf(0.00000239334, 2.35045874708, 137.03302416240),
                    doubleArrayOf(0.00000294172, 5.83916826225, 39.61750834610),
                    doubleArrayOf(0.00000216480, 4.77847481363, 340.77089204480),
                    doubleArrayOf(0.00000251792, 1.63696775578, 221.37585028530),
                    doubleArrayOf(0.00000219621, 1.92212987979, 67.66805156650),
                    doubleArrayOf(0.00000201963, 1.29693040865, 0.04818410980),
                    doubleArrayOf(0.00000224097, 0.51574863468, 84.34282612290),
                    doubleArrayOf(0.00000216549, 6.14211862702, 5.93789083320),
                    doubleArrayOf(0.00000222588, 2.84309380331, 0.26063243090),
                    doubleArrayOf(0.00000207828, 5.58020570040, 68.84370773410),
                    doubleArrayOf(0.00000187474, 1.31924326253, 0.16005869440),
                    doubleArrayOf(0.00000158028, 0.73811997211, 54.17467074780),
                    doubleArrayOf(0.00000199146, 0.95634155010, 152.53214255120),
                    doubleArrayOf(0.00000168648, 5.87874000882, 18.15924726470),
                    doubleArrayOf(0.00000170300, 3.67717520688, 5.41662597140),
                    doubleArrayOf(0.00000193652, 1.88800122606, 456.39383923560),
                    doubleArrayOf(0.00000192998, 0.91616058506, 453.42489381900),
                    doubleArrayOf(0.00000181934, 3.53624029238, 79.23501669220),
                    doubleArrayOf(0.00000173145, 1.53860728054, 160.60889739850),
                    doubleArrayOf(0.00000164588, 1.42379714838, 106.97674337190),
                    doubleArrayOf(0.00000171968, 5.67952685533, 219.89137757700),
                    doubleArrayOf(0.00000162792, 3.05029377666, 112.91463420510),
                    doubleArrayOf(0.00000146653, 1.26300172265, 59.80374504030),
                    doubleArrayOf(0.00000139453, 5.38597723400, 32.19514480460),
                    doubleArrayOf(0.00000138585, 4.25994786673, 909.81873305460),
                    doubleArrayOf(0.00000143058, 1.29995487555, 35.42472265210),
                    doubleArrayOf(0.00000123840, 1.37359990336, 7.11354700080),
                    doubleArrayOf(0.00000104414, 5.02820888813, 0.75075952540),
                    doubleArrayOf(0.00000103277, 0.68095301267, 14.97785352700),
                    doubleArrayOf(0.00000110163, 2.02685778976, 554.06998748280),
                    doubleArrayOf(0.00000109376, 5.70581833286, 77.96299230500),
                    doubleArrayOf(0.00000103562, 1.45770270246, 24.37902238820)
            )

    private fun coefficients11() =
            arrayOf(
                    doubleArrayOf(75.02543121646, 0.00000000000, 0.00000000000),
                    doubleArrayOf(0.00154458244, 5.24201658072, 74.78159856730),
                    doubleArrayOf(0.00024456413, 1.71255705309, 1.48447270830),
                    doubleArrayOf(0.00009257828, 0.42844639064, 11.04570026390),
                    doubleArrayOf(0.00008265977, 1.50220035110, 63.73589830340),
                    doubleArrayOf(0.00007841715, 1.31983607251, 149.56319713460),
                    doubleArrayOf(0.00003899105, 0.46483574024, 3.93215326310),
                    doubleArrayOf(0.00002283777, 4.17367533997, 76.26607127560),
                    doubleArrayOf(0.00001926600, 0.53013080152, 2.96894541660),
                    doubleArrayOf(0.00001232727, 1.58634458237, 70.84944530420),
                    doubleArrayOf(0.00000791206, 5.43641224143, 3.18139373770),
                    doubleArrayOf(0.00000766954, 1.99555409575, 73.29712585900),
                    doubleArrayOf(0.00000481671, 2.98401996914, 85.82729883120),
                    doubleArrayOf(0.00000449798, 4.13826237508, 138.51749687070),
                    doubleArrayOf(0.00000445600, 3.72300400331, 224.34479570190),
                    doubleArrayOf(0.00000426554, 4.73126059388, 71.81265315070),
                    doubleArrayOf(0.00000347735, 2.45372261286, 9.56122755560),
                    doubleArrayOf(0.00000353752, 2.58324496886, 148.07872442630),
                    doubleArrayOf(0.00000317084, 5.57855232072, 52.69019803950),
                    doubleArrayOf(0.00000179920, 5.68367730922, 12.53017297220),
                    doubleArrayOf(0.00000171084, 3.00060075287, 78.71375183040),
                    doubleArrayOf(0.00000205585, 2.36263144251, 2.44768055480),
                    doubleArrayOf(0.00000158029, 2.90931969498, 0.96320784650),
                    doubleArrayOf(0.00000189068, 4.20242881378, 56.62235130260),
                    doubleArrayOf(0.00000154670, 5.59083925605, 4.45341812490),
                    doubleArrayOf(0.00000183762, 0.28371004654, 151.04766984290),
                    doubleArrayOf(0.00000143464, 2.59049246726, 62.25142559510),
                    doubleArrayOf(0.00000151984, 2.94217326890, 77.75054398390),
                    doubleArrayOf(0.00000153515, 4.65186885939, 35.16409022120),
                    doubleArrayOf(0.00000121452, 4.14839204920, 127.47179660680),
                    doubleArrayOf(0.00000115546, 3.73224603791, 65.22037101170),
                    doubleArrayOf(0.00000102022, 4.18754517993, 145.63104387150),
                    doubleArrayOf(0.00000101718, 6.03385875009, 0.11187458460),
                    doubleArrayOf(0.00000088202, 3.99035787994, 18.15924726470),
                    doubleArrayOf(0.00000087549, 6.15520787584, 202.25339517410),
                    doubleArrayOf(0.00000080530, 2.64124743934, 22.09140052780),
                    doubleArrayOf(0.00000072047, 6.04545933578, 70.32818044240),
                    doubleArrayOf(0.00000068570, 4.05071895264, 77.96299230500),
                    doubleArrayOf(0.00000059173, 3.70413919082, 67.66805156650),
                    doubleArrayOf(0.00000047267, 3.54312460519, 351.81659230870),
                    doubleArrayOf(0.00000042534, 5.72357370899, 5.41662597140),
                    doubleArrayOf(0.00000044339, 5.90865821911, 7.11354700080),
                    doubleArrayOf(0.00000035605, 3.29197259183, 8.07675484730),
                    doubleArrayOf(0.00000035524, 3.32784616138, 71.60020482960),
                    doubleArrayOf(0.00000036116, 5.89964278801, 33.67961751290),
                    doubleArrayOf(0.00000030608, 5.46414592601, 160.60889739850),
                    doubleArrayOf(0.00000031454, 5.62015632303, 984.60033162190),
                    doubleArrayOf(0.00000038544, 4.91519003848, 222.86032299360),
                    doubleArrayOf(0.00000034996, 5.08034112149, 38.13303563780),
                    doubleArrayOf(0.00000030811, 5.49591403863, 59.80374504030),
                    doubleArrayOf(0.00000028947, 4.51867390414, 84.34282612290),
                    doubleArrayOf(0.00000026627, 5.54127301037, 131.40394986990),
                    doubleArrayOf(0.00000029866, 1.65980844667, 447.79581952650),
                    doubleArrayOf(0.00000029206, 1.14722640419, 462.02291352810),
                    doubleArrayOf(0.00000025753, 4.99362028417, 137.03302416240),
                    doubleArrayOf(0.00000025373, 5.73584678604, 380.12776796000),
                    doubleArrayOf(0.00000021672, 2.80556379586, 69.36497259590),
                    doubleArrayOf(0.00000026605, 6.14640604128, 299.12639426920)
            )

    private fun coefficients12() =
            arrayOf(
                    doubleArrayOf(0.00053033277, 0.00000000000, 0.00000000000),
                    doubleArrayOf(0.00002357636, 2.26014661705, 74.78159856730),
                    doubleArrayOf(0.00000769129, 4.52561041823, 11.04570026390),
                    doubleArrayOf(0.00000551533, 3.25814281023, 63.73589830340),
                    doubleArrayOf(0.00000541532, 2.27573907424, 3.93215326310),
                    doubleArrayOf(0.00000529473, 4.92348433826, 1.48447270830),
                    doubleArrayOf(0.00000257521, 3.69059216858, 3.18139373770),
                    doubleArrayOf(0.00000238835, 5.85806638405, 149.56319713460),
                    doubleArrayOf(0.00000181904, 6.21763603405, 70.84944530420),
                    doubleArrayOf(0.00000049401, 6.03101301723, 56.62235130260),
                    doubleArrayOf(0.00000053504, 1.44225240953, 76.26607127560),
                    doubleArrayOf(0.00000038222, 1.78467827781, 52.69019803950),
                    doubleArrayOf(0.00000044753, 3.90904910523, 2.44768055480),
                    doubleArrayOf(0.00000044530, 0.81152639478, 85.82729883120),
                    doubleArrayOf(0.00000037403, 4.46228598032, 2.96894541660),
                    doubleArrayOf(0.00000033029, 0.86388149962, 9.56122755560),
                    doubleArrayOf(0.00000024292, 2.10702559049, 18.15924726470),
                    doubleArrayOf(0.00000029423, 5.09818697708, 73.29712585900),
                    doubleArrayOf(0.00000022135, 4.81730808582, 78.71375183040),
                    doubleArrayOf(0.00000022491, 5.99320728691, 138.51749687070),
                    doubleArrayOf(0.00000017226, 2.53537183199, 145.63104387150),
                    doubleArrayOf(0.00000021392, 2.39880709309, 77.96299230500),
                    doubleArrayOf(0.00000020578, 2.16918786539, 224.34479570190),
                    doubleArrayOf(0.00000016777, 3.46631344086, 12.53017297220),
                    doubleArrayOf(0.00000012012, 0.01941361902, 22.09140052780),
                    doubleArrayOf(0.00000010466, 4.45556032593, 62.25142559510),
                    doubleArrayOf(0.00000011010, 0.08496274370, 127.47179660680),
                    doubleArrayOf(0.00000008668, 4.25550086984, 7.11354700080),
                    doubleArrayOf(0.00000010476, 5.16453084068, 71.60020482960),
                    doubleArrayOf(0.00000007160, 1.24903906391, 5.41662597140),
                    doubleArrayOf(0.00000008387, 5.50115930045, 67.66805156650),
                    doubleArrayOf(0.00000006087, 5.44611674384, 65.22037101170),
                    doubleArrayOf(0.00000006013, 4.51836836347, 151.04766984290),
                    doubleArrayOf(0.00000005718, 1.82933915340, 202.25339517410),
                    doubleArrayOf(0.00000006109, 3.36320161279, 447.79581952650),
                    doubleArrayOf(0.00000006003, 5.72500086735, 462.02291352810),
                    doubleArrayOf(0.00000005111, 3.52374555791, 59.80374504030),
                    doubleArrayOf(0.00000005155, 1.05810305746, 131.40394986990),
                    doubleArrayOf(0.00000005969, 5.61147374852, 148.07872442630)
            )

    private fun coefficients13() =
            arrayOf(
                    doubleArrayOf(0.00000120936, 0.02418789918, 74.78159856730),
                    doubleArrayOf(0.00000068064, 4.12084267733, 3.93215326310),
                    doubleArrayOf(0.00000052828, 2.38964061260, 11.04570026390),
                    doubleArrayOf(0.00000043754, 2.95965039734, 1.48447270830),
                    doubleArrayOf(0.00000045300, 2.04423798410, 3.18139373770),
                    doubleArrayOf(0.00000045806, 0.00000000000, 0.00000000000),
                    doubleArrayOf(0.00000024969, 4.88741307918, 63.73589830340),
                    doubleArrayOf(0.00000021061, 4.54511486862, 70.84944530420),
                    doubleArrayOf(0.00000019897, 2.31320314136, 149.56319713460),
                    doubleArrayOf(0.00000008901, 1.57548871761, 56.62235130260),
                    doubleArrayOf(0.00000004271, 0.22777319552, 18.15924726470),
                    doubleArrayOf(0.00000003613, 5.39244611308, 76.26607127560),
                    doubleArrayOf(0.00000003488, 4.97622811775, 85.82729883120),
                    doubleArrayOf(0.00000003479, 4.12969359977, 52.69019803950),
                    doubleArrayOf(0.00000003572, 0.95052448578, 77.96299230500),
                    doubleArrayOf(0.00000002328, 0.85770961794, 145.63104387150),
                    doubleArrayOf(0.00000002696, 0.37287796344, 78.71375183040),
                    doubleArrayOf(0.00000001946, 2.67997393431, 7.11354700080),
                    doubleArrayOf(0.00000002156, 5.65647821519, 9.56122755560)
            )

    private fun coefficients14() =
            arrayOf(
                    doubleArrayOf(0.00000113855, 3.14159265359, 0.00000000000),
                    doubleArrayOf(0.00000005599, 4.57882424417, 74.78159856730),
                    doubleArrayOf(0.00000003203, 0.34623003207, 11.04570026390),
                    doubleArrayOf(0.00000001217, 3.42199121826, 56.62235130260)
            )

    private fun coefficients20() =
            arrayOf(
                    doubleArrayOf(0.01346277639, 2.61877810545, 74.78159856730),
                    doubleArrayOf(0.00062341405, 5.08111175856, 149.56319713460),
                    doubleArrayOf(0.00061601203, 3.14159265359, 0.00000000000),
                    doubleArrayOf(0.00009963744, 1.61603876357, 76.26607127560),
                    doubleArrayOf(0.00009926151, 0.57630387917, 73.29712585900),
                    doubleArrayOf(0.00003259455, 1.26119385960, 224.34479570190),
                    doubleArrayOf(0.00002972318, 2.24367035538, 1.48447270830),
                    doubleArrayOf(0.00002010257, 6.05550401088, 148.07872442630),
                    doubleArrayOf(0.00001522172, 0.27960386377, 63.73589830340),
                    doubleArrayOf(0.00000924055, 4.03822927853, 151.04766984290),
                    doubleArrayOf(0.00000760624, 6.14000431923, 71.81265315070),
                    doubleArrayOf(0.00000420265, 5.21279984788, 11.04570026390),
                    doubleArrayOf(0.00000430668, 3.55445034854, 213.29909543800),
                    doubleArrayOf(0.00000436843, 3.38082524317, 529.69096509460),
                    doubleArrayOf(0.00000522309, 3.32085194770, 138.51749687070),
                    doubleArrayOf(0.00000434625, 0.34065281858, 77.75054398390),
                    doubleArrayOf(0.00000462630, 0.74256727574, 85.82729883120),
                    doubleArrayOf(0.00000232649, 2.25716421383, 222.86032299360),
                    doubleArrayOf(0.00000215838, 1.59121704940, 38.13303563780),
                    doubleArrayOf(0.00000244698, 0.78795150326, 2.96894541660),
                    doubleArrayOf(0.00000179935, 3.72487952673, 299.12639426920),
                    doubleArrayOf(0.00000174895, 1.23550262213, 146.59425171800),
                    doubleArrayOf(0.00000173667, 1.93654269131, 380.12776796000),
                    doubleArrayOf(0.00000160368, 5.33635436463, 111.43016149680),
                    doubleArrayOf(0.00000144064, 5.96239326415, 35.16409022120),
                    doubleArrayOf(0.00000102049, 2.61876256513, 78.71375183040),
                    doubleArrayOf(0.00000116363, 5.73877190007, 70.84944530420),
                    doubleArrayOf(0.00000106441, 0.94103112994, 70.32818044240)
            )

    private fun coefficients21() =
            arrayOf(
                    doubleArrayOf(0.00206366162, 4.12394311407, 74.78159856730),
                    doubleArrayOf(0.00008563230, 0.33819986165, 149.56319713460),
                    doubleArrayOf(0.00001725703, 2.12193159895, 73.29712585900),
                    doubleArrayOf(0.00001368860, 3.06861722047, 76.26607127560),
                    doubleArrayOf(0.00001374449, 0.00000000000, 0.00000000000),
                    doubleArrayOf(0.00000399847, 2.84767037795, 224.34479570190),
                    doubleArrayOf(0.00000450639, 3.77656180977, 1.48447270830),
                    doubleArrayOf(0.00000307214, 1.25456766737, 148.07872442630),
                    doubleArrayOf(0.00000154336, 3.78575467747, 63.73589830340),
                    doubleArrayOf(0.00000110888, 5.32888676461, 138.51749687070),
                    doubleArrayOf(0.00000112432, 5.57299891505, 151.04766984290),
                    doubleArrayOf(0.00000083493, 3.59152795558, 71.81265315070),
                    doubleArrayOf(0.00000055573, 3.40135416354, 85.82729883120),
                    doubleArrayOf(0.00000041377, 4.45476669141, 78.71375183040),
                    doubleArrayOf(0.00000053690, 1.70455769943, 77.75054398390),
                    doubleArrayOf(0.00000041912, 1.21476607434, 11.04570026390),
                    doubleArrayOf(0.00000031959, 3.77446207748, 222.86032299360),
                    doubleArrayOf(0.00000030297, 2.56371683644, 2.96894541660),
                    doubleArrayOf(0.00000026977, 5.33695500294, 213.29909543800),
                    doubleArrayOf(0.00000026222, 0.41620628369, 380.12776796000)
            )

    private fun coefficients22() =
            arrayOf(
                    doubleArrayOf(0.00009211656, 5.80044305785, 74.78159856730),
                    doubleArrayOf(0.00000556926, 0.00000000000, 0.00000000000),
                    doubleArrayOf(0.00000286265, 2.17729776353, 149.56319713460),
                    doubleArrayOf(0.00000094969, 3.84237569809, 73.29712585900),
                    doubleArrayOf(0.00000045419, 4.87822046064, 76.26607127560),
                    doubleArrayOf(0.00000020107, 5.46264485369, 1.48447270830),
                    doubleArrayOf(0.00000014793, 0.87983715652, 138.51749687070),
                    doubleArrayOf(0.00000013963, 5.07234043994, 63.73589830340),
                    doubleArrayOf(0.00000014261, 2.84517742687, 148.07872442630),
                    doubleArrayOf(0.00000010122, 5.00290894862, 224.34479570190),
                    doubleArrayOf(0.00000008299, 6.26655615197, 78.71375183040)
            )

    private fun coefficients23() =
            arrayOf(
                    doubleArrayOf(0.00000267832, 1.25097888291, 74.78159856730),
                    doubleArrayOf(0.00000011048, 3.14159265359, 0.00000000000),
                    doubleArrayOf(0.00000006154, 4.00663614486, 149.56319713460),
                    doubleArrayOf(0.00000003361, 5.77804694935, 73.29712585900)
            )

    private fun coefficients24() =
            arrayOf(doubleArrayOf(0.00000005719, 2.85499529315, 74.78159856730))

    private fun coefficients30() =
            arrayOf(
                    doubleArrayOf(19.21264847881, 0.00000000000, 0.00000000000),
                    doubleArrayOf(0.88784984055, 5.60377526994, 74.78159856730),
                    doubleArrayOf(0.03440835545, 0.32836098991, 73.29712585900),
                    doubleArrayOf(0.02055653495, 1.78295170028, 149.56319713460),
                    doubleArrayOf(0.00649321851, 4.52247298119, 76.26607127560),
                    doubleArrayOf(0.00602248144, 3.86003820462, 63.73589830340),
                    doubleArrayOf(0.00496404171, 1.40139934716, 454.90936652730),
                    doubleArrayOf(0.00338525522, 1.58002682946, 138.51749687070),
                    doubleArrayOf(0.00243508222, 1.57086595074, 71.81265315070),
                    doubleArrayOf(0.00190521915, 1.99809364502, 1.48447270830),
                    doubleArrayOf(0.00161858251, 2.79137863469, 148.07872442630),
                    doubleArrayOf(0.00143705902, 1.38368574483, 11.04570026390),
                    doubleArrayOf(0.00093192359, 0.17437193645, 36.64856292950),
                    doubleArrayOf(0.00071424265, 4.24509327405, 224.34479570190),
                    doubleArrayOf(0.00089805842, 3.66105366329, 109.94568878850),
                    doubleArrayOf(0.00039009624, 1.66971128869, 70.84944530420),
                    doubleArrayOf(0.00046677322, 1.39976563936, 35.16409022120),
                    doubleArrayOf(0.00039025681, 3.36234710692, 277.03499374140),
                    doubleArrayOf(0.00036755160, 3.88648934736, 146.59425171800),
                    doubleArrayOf(0.00030348875, 0.70100446346, 151.04766984290),
                    doubleArrayOf(0.00029156264, 3.18056174556, 77.75054398390),
                    doubleArrayOf(0.00020471584, 1.55588961500, 202.25339517410),
                    doubleArrayOf(0.00025620360, 5.25656292802, 380.12776796000),
                    doubleArrayOf(0.00025785805, 3.78537741503, 85.82729883120),
                    doubleArrayOf(0.00022637152, 0.72519137745, 529.69096509460),
                    doubleArrayOf(0.00020473163, 2.79639811626, 70.32818044240),
                    doubleArrayOf(0.00017900561, 0.55455488605, 2.96894541660),
                    doubleArrayOf(0.00012328151, 5.96039150918, 127.47179660680),
                    doubleArrayOf(0.00014701566, 4.90434406648, 108.46121608020),
                    doubleArrayOf(0.00011494701, 0.43774027872, 65.22037101170),
                    doubleArrayOf(0.00015502809, 5.35405037603, 38.13303563780),
                    doubleArrayOf(0.00010792699, 1.42104858472, 213.29909543800),
                    doubleArrayOf(0.00011696085, 3.29825599114, 3.93215326310),
                    doubleArrayOf(0.00011959355, 1.75044072173, 984.60033162190),
                    doubleArrayOf(0.00012896507, 2.62154018241, 111.43016149680),
                    doubleArrayOf(0.00011852996, 0.99342814582, 52.69019803950),
                    doubleArrayOf(0.00009111446, 4.99638600045, 62.25142559510),
                    doubleArrayOf(0.00008420550, 5.25350716616, 222.86032299360),
                    doubleArrayOf(0.00007449125, 0.79491905956, 351.81659230870),
                    doubleArrayOf(0.00008402147, 5.03877516489, 415.55249061210),
                    doubleArrayOf(0.00006046370, 5.67960948357, 78.71375183040),
                    doubleArrayOf(0.00005524133, 3.11499484161, 9.56122755560),
                    doubleArrayOf(0.00007329454, 3.97277527840, 183.24281464750),
                    doubleArrayOf(0.00005444878, 5.10575635361, 145.10977900970),
                    doubleArrayOf(0.00005238103, 2.62960141797, 33.67961751290),
                    doubleArrayOf(0.00004079167, 3.22064788674, 340.77089204480),
                    doubleArrayOf(0.00003801606, 6.10985558505, 184.72728735580),
                    doubleArrayOf(0.00003919476, 4.25015288873, 39.61750834610),
                    doubleArrayOf(0.00002940492, 2.14637460319, 137.03302416240),
                    doubleArrayOf(0.00003781219, 3.45840272873, 456.39383923560),
                    doubleArrayOf(0.00002942239, 0.42393808854, 299.12639426920),
                    doubleArrayOf(0.00003686787, 2.48718116535, 453.42489381900),
                    doubleArrayOf(0.00003101743, 4.14031063896, 219.89137757700),
                    doubleArrayOf(0.00002962641, 0.82977991995, 56.62235130260),
                    doubleArrayOf(0.00002937799, 3.67657450930, 140.00196957900),
                    doubleArrayOf(0.00002865128, 0.30996903761, 12.53017297220),
                    doubleArrayOf(0.00002538032, 4.85457831993, 131.40394986990),
                    doubleArrayOf(0.00001962510, 5.24342224065, 84.34282612290),
                    doubleArrayOf(0.00002363550, 0.44253328372, 554.06998748280),
                    doubleArrayOf(0.00001979394, 6.12836181686, 106.97674337190),
                    doubleArrayOf(0.00002182572, 2.94040431638, 305.34616939270)
            )

    private fun coefficients31() =
            arrayOf(
                    doubleArrayOf(0.01479896370, 3.67205705317, 74.78159856730),
                    doubleArrayOf(0.00071212085, 6.22601006675, 63.73589830340),
                    doubleArrayOf(0.00068626972, 6.13411265052, 149.56319713460),
                    doubleArrayOf(0.00020857262, 5.24625494219, 11.04570026390),
                    doubleArrayOf(0.00021468152, 2.60176704270, 76.26607127560),
                    doubleArrayOf(0.00024059649, 3.14159265359, 0.00000000000),
                    doubleArrayOf(0.00011405346, 0.01848461561, 70.84944530420),
                    doubleArrayOf(0.00007496775, 0.42360033283, 73.29712585900),
                    doubleArrayOf(0.00004243800, 1.41692350371, 85.82729883120),
                    doubleArrayOf(0.00003505936, 2.58354048851, 138.51749687070),
                    doubleArrayOf(0.00003228835, 5.25499602896, 3.93215326310),
                    doubleArrayOf(0.00003926694, 3.15513991323, 71.81265315070),
                    doubleArrayOf(0.00003060010, 0.15321893225, 1.48447270830),
                    doubleArrayOf(0.00003578446, 2.31160668309, 224.34479570190),
                    doubleArrayOf(0.00002564251, 0.98076846352, 148.07872442630),
                    doubleArrayOf(0.00002429445, 3.99440122468, 52.69019803950),
                    doubleArrayOf(0.00001644719, 2.65349313124, 127.47179660680),
                    doubleArrayOf(0.00001583766, 1.43045619196, 78.71375183040),
                    doubleArrayOf(0.00001413112, 4.57461892062, 202.25339517410),
                    doubleArrayOf(0.00001489525, 2.67559167316, 56.62235130260),
                    doubleArrayOf(0.00001403237, 1.36985349744, 77.75054398390),
                    doubleArrayOf(0.00001228220, 1.04703640149, 62.25142559510),
                    doubleArrayOf(0.00001508028, 5.05996325425, 151.04766984290),
                    doubleArrayOf(0.00000992085, 2.17168865909, 65.22037101170),
                    doubleArrayOf(0.00001032731, 0.26459059027, 131.40394986990),
                    doubleArrayOf(0.00000861867, 5.05530802218, 351.81659230870),
                    doubleArrayOf(0.00000744445, 3.07640148939, 35.16409022120),
                    doubleArrayOf(0.00000604362, 0.90717667985, 984.60033162190),
                    doubleArrayOf(0.00000646851, 4.47290422910, 70.32818044240),
                    doubleArrayOf(0.00000574710, 3.23070708457, 447.79581952650),
                    doubleArrayOf(0.00000687470, 2.49912565674, 77.96299230500),
                    doubleArrayOf(0.00000623602, 0.86253073820, 9.56122755560),
                    doubleArrayOf(0.00000527794, 5.15136007084, 2.96894541660),
                    doubleArrayOf(0.00000561839, 2.71778158980, 462.02291352810),
                    doubleArrayOf(0.00000530364, 5.91655309045, 213.29909543800)
            )

    private fun coefficients32() =
            arrayOf(
                    doubleArrayOf(0.00022439904, 0.69953118760, 74.78159856730),
                    doubleArrayOf(0.00004727037, 1.69901641488, 63.73589830340),
                    doubleArrayOf(0.00001681903, 4.64833551727, 70.84944530420),
                    doubleArrayOf(0.00001433755, 3.52119917947, 149.56319713460),
                    doubleArrayOf(0.00001649559, 3.09660078980, 11.04570026390),
                    doubleArrayOf(0.00000770188, 0.00000000000, 0.00000000000),
                    doubleArrayOf(0.00000461009, 0.76676632849, 3.93215326310),
                    doubleArrayOf(0.00000500429, 6.17229032223, 76.26607127560),
                    doubleArrayOf(0.00000390371, 4.49605283502, 56.62235130260),
                    doubleArrayOf(0.00000389945, 5.52673426377, 85.82729883120),
                    doubleArrayOf(0.00000292097, 0.20389012095, 52.69019803950),
                    doubleArrayOf(0.00000272898, 3.84707823651, 138.51749687070),
                    doubleArrayOf(0.00000286579, 3.53357683270, 73.29712585900),
                    doubleArrayOf(0.00000205449, 3.24758017121, 78.71375183040),
                    doubleArrayOf(0.00000219674, 1.96418942891, 131.40394986990),
                    doubleArrayOf(0.00000215788, 0.84812474187, 77.96299230500),
                    doubleArrayOf(0.00000128834, 2.08146849515, 3.18139373770),
                    doubleArrayOf(0.00000148554, 4.89840863841, 127.47179660680)
            )

    private fun coefficients33() =
            arrayOf(
                    doubleArrayOf(0.00001164382, 4.73453291602, 74.78159856730),
                    doubleArrayOf(0.00000212367, 3.34255734999, 63.73589830340),
                    doubleArrayOf(0.00000196408, 2.98004616318, 70.84944530420),
                    doubleArrayOf(0.00000104527, 0.95807937648, 11.04570026390),
                    doubleArrayOf(0.00000071681, 0.02528455665, 56.62235130260),
                    doubleArrayOf(0.00000072540, 0.99701907912, 149.56319713460),
                    doubleArrayOf(0.00000054875, 2.59436811267, 3.93215326310),
                    doubleArrayOf(0.00000034029, 3.81553325635, 76.26607127560),
                    doubleArrayOf(0.00000032081, 3.59825177840, 131.40394986990),
                    doubleArrayOf(0.00000029641, 3.44111535957, 85.82729883120),
                    doubleArrayOf(0.00000036377, 5.65035573017, 77.96299230500)
            )

    private fun coefficients34() =
            arrayOf(
                    doubleArrayOf(0.00000052996, 3.00838033088, 74.78159856730),
                    doubleArrayOf(0.00000009887, 1.91399083603, 56.62235130260),
                    doubleArrayOf(0.00000007008, 5.08677527404, 11.04570026390),
                    doubleArrayOf(0.00000006728, 5.42924958121, 149.56319713460)
            )
}
