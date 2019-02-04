package com.mkreidl.ephemeris.solarsystem.meeus

import com.mkreidl.ephemeris.solarsystem.ModelVsop87

class VenusMeeus : ModelVsop87.LBR(
        arrayOf(
                arrayOf(coefficients10(), coefficients11(), coefficients12(), coefficients13(), coefficients14(), coefficients15()),
                arrayOf(coefficients20(), coefficients21(), coefficients22(), coefficients23(), coefficients24(), coefficients25()),
                arrayOf(coefficients30(), coefficients31(), coefficients32(), coefficients33(), coefficients34())
        )
) {
    companion object {
        private fun coefficients10() =
                arrayOf(
                        doubleArrayOf(3.17614666774, 0.00000000000, 0.00000000000),
                        doubleArrayOf(0.01353968419, 5.59313319619, 10213.28554621100),
                        doubleArrayOf(0.00089891645, 5.30650048468, 20426.57109242200),
                        doubleArrayOf(0.00005477201, 4.41630652531, 7860.41939243920),
                        doubleArrayOf(0.00003455732, 2.69964470778, 11790.62908865880),
                        doubleArrayOf(0.00002372061, 2.99377539568, 3930.20969621960),
                        doubleArrayOf(0.00001317108, 5.18668219093, 26.29831979980),
                        doubleArrayOf(0.00001664069, 4.25018935030, 1577.34354244780),
                        doubleArrayOf(0.00001438322, 4.15745043958, 9683.59458111640),
                        doubleArrayOf(0.00001200521, 6.15357115319, 30639.85663863300),
                        doubleArrayOf(0.00000761380, 1.95014702120, 529.69096509460),
                        doubleArrayOf(0.00000707676, 1.06466707214, 775.52261132400),
                        doubleArrayOf(0.00000584836, 3.99839884762, 191.44826611160),
                        doubleArrayOf(0.00000769314, 0.81629615911, 9437.76293488700),
                        doubleArrayOf(0.00000499915, 4.12340210074, 15720.83878487840),
                        doubleArrayOf(0.00000326221, 4.59056473097, 10404.73381232260),
                        doubleArrayOf(0.00000429498, 3.58642859752, 19367.18916223280),
                        doubleArrayOf(0.00000326967, 5.67736583705, 5507.55323866740),
                        doubleArrayOf(0.00000231937, 3.16251057072, 9153.90361602180),
                        doubleArrayOf(0.00000179695, 4.65337915578, 1109.37855209340),
                        doubleArrayOf(0.00000128263, 4.22604493736, 20.77539549240),
                        doubleArrayOf(0.00000155464, 5.57043888948, 19651.04848109800),
                        doubleArrayOf(0.00000127907, 0.96209822685, 5661.33204915220),
                        doubleArrayOf(0.00000105547, 1.53721191253, 801.82093112380)
                )

        private fun coefficients11() =
                arrayOf(
                        doubleArrayOf(10213.52943052898, 0.00000000000, 0.00000000000),
                        doubleArrayOf(0.00095707712, 2.46424448979, 10213.28554621100),
                        doubleArrayOf(0.00014444977, 0.51624564679, 20426.57109242200),
                        doubleArrayOf(0.00000213374, 1.79547929368, 30639.85663863300),
                        doubleArrayOf(0.00000151669, 6.10635282369, 1577.34354244780),
                        doubleArrayOf(0.00000173904, 2.65535879443, 26.29831979980),
                        doubleArrayOf(0.00000082233, 5.70234133730, 191.44826611160),
                        doubleArrayOf(0.00000069734, 2.68136034979, 9437.76293488700),
                        doubleArrayOf(0.00000052408, 3.60013087656, 775.52261132400),
                        doubleArrayOf(0.00000038318, 1.03379038025, 529.69096509460),
                        doubleArrayOf(0.00000029633, 1.25056322354, 5507.55323866740),
                        doubleArrayOf(0.00000025056, 6.10664792855, 10404.73381232260)
                )

        private fun coefficients12() =
                arrayOf(
                        doubleArrayOf(0.00054127076, 0.00000000000, 0.00000000000),
                        doubleArrayOf(0.00003891460, 0.34514360047, 10213.28554621100),
                        doubleArrayOf(0.00001337880, 2.02011286082, 20426.57109242200),
                        doubleArrayOf(0.00000023836, 2.04592119012, 26.29831979980),
                        doubleArrayOf(0.00000019331, 3.53527371458, 30639.85663863300),
                        doubleArrayOf(0.00000009984, 3.97130221102, 775.52261132400),
                        doubleArrayOf(0.00000007046, 1.51962593409, 1577.34354244780),
                        doubleArrayOf(0.00000006014, 0.99926757893, 191.44826611160)
                )

        private fun coefficients13() =
                arrayOf(
                        doubleArrayOf(0.00000135742, 4.80389020993, 10213.28554621100),
                        doubleArrayOf(0.00000077846, 3.66876371591, 20426.57109242200),
                        doubleArrayOf(0.00000026023, 0.00000000000, 0.00000000000)
                )

        private fun coefficients14() =
                arrayOf(
                        doubleArrayOf(0.00000114016, 3.14159265359, 0.00000000000),
                        doubleArrayOf(0.00000003209, 5.20514170164, 20426.57109242200),
                        doubleArrayOf(0.00000001714, 2.51099591706, 10213.28554621100)
                )

        private fun coefficients15() =
                arrayOf(doubleArrayOf(0.00000000874, 3.14159265359, 0.00000000000))


        private fun coefficients20() =
                arrayOf(
                        doubleArrayOf(0.05923638472, 0.26702775813, 10213.28554621100),
                        doubleArrayOf(0.00040107978, 1.14737178106, 20426.57109242200),
                        doubleArrayOf(0.00032814918, 3.14159265359, 0.00000000000),
                        doubleArrayOf(0.00001011392, 1.08946123021, 30639.85663863300),
                        doubleArrayOf(0.00000149458, 6.25390296069, 18073.70493865020),
                        doubleArrayOf(0.00000137788, 0.86020146523, 1577.34354244780),
                        doubleArrayOf(0.00000129973, 3.67152483651, 9437.76293488700),
                        doubleArrayOf(0.00000119507, 3.70468812804, 2352.86615377180),
                        doubleArrayOf(0.00000107971, 4.53903677647, 22003.91463486980)
                )

        private fun coefficients21() =
                arrayOf(
                        doubleArrayOf(0.00513347602, 1.80364310797, 10213.28554621100),
                        doubleArrayOf(0.00004380100, 3.38615711591, 20426.57109242200),
                        doubleArrayOf(0.00000196586, 2.53001197486, 30639.85663863300),
                        doubleArrayOf(0.00000199162, 0.00000000000, 0.00000000000)
                )

        private fun coefficients22() =
                arrayOf(
                        doubleArrayOf(0.00022377665, 3.38509143877, 10213.28554621100),
                        doubleArrayOf(0.00000281739, 0.00000000000, 0.00000000000),
                        doubleArrayOf(0.00000173164, 5.25563766915, 20426.57109242200),
                        doubleArrayOf(0.00000026945, 3.87040891568, 30639.85663863300)
                )

        private fun coefficients23() =
                arrayOf(
                        doubleArrayOf(0.00000646671, 4.99166565277, 10213.28554621100),
                        doubleArrayOf(0.00000019952, 3.14159265359, 0.00000000000),
                        doubleArrayOf(0.00000005540, 0.77376923951, 20426.57109242200),
                        doubleArrayOf(0.00000002526, 5.44493763020, 30639.85663863300)
                )

        private fun coefficients24() =
                arrayOf(doubleArrayOf(0.00000014102, 0.31537190181, 10213.28554621100))


        private fun coefficients25() =
                arrayOf(
                        doubleArrayOf(0.00000000239, 2.05201727566, 10213.28554621100),
                        doubleArrayOf(0.00000000039, 0.00000000000, 0.00000000000),
                        doubleArrayOf(0.00000000011, 3.82500275251, 20426.57109242200),
                        doubleArrayOf(0.00000000009, 2.32953116868, 30639.85663863300)
                )

        private fun coefficients30() =
                arrayOf(
                        doubleArrayOf(0.72334820905, 0.00000000000, 0.00000000000),
                        doubleArrayOf(0.00489824185, 4.02151832268, 10213.28554621100),
                        doubleArrayOf(0.00001658058, 4.90206728012, 20426.57109242200),
                        doubleArrayOf(0.00001632093, 2.84548851892, 7860.41939243920),
                        doubleArrayOf(0.00001378048, 1.12846590600, 11790.62908865880),
                        doubleArrayOf(0.00000498399, 2.58682187717, 9683.59458111640),
                        doubleArrayOf(0.00000373958, 1.42314837063, 3930.20969621960),
                        doubleArrayOf(0.00000263616, 5.52938185920, 9437.76293488700),
                        doubleArrayOf(0.00000237455, 2.55135903978, 15720.83878487840),
                        doubleArrayOf(0.00000221983, 2.01346776772, 19367.18916223280),
                        doubleArrayOf(0.00000119467, 3.01975365264, 10404.73381232260),
                        doubleArrayOf(0.00000125896, 2.72769833559, 1577.34354244780)
                )

        private fun coefficients31() =
                arrayOf(
                        doubleArrayOf(0.00034551039, 0.89198710598, 10213.28554621100),
                        doubleArrayOf(0.00000234203, 1.77224942714, 20426.57109242200),
                        doubleArrayOf(0.00000233998, 3.14159265359, 0.00000000000)
                )


        private fun coefficients32() =
                arrayOf(
                        doubleArrayOf(0.00001406587, 5.06366395190, 10213.28554621100),
                        doubleArrayOf(0.00000015529, 5.47321687981, 20426.57109242200),
                        doubleArrayOf(0.00000013059, 0.00000000000, 0.00000000000)
                )

        private fun coefficients33() =
                arrayOf(doubleArrayOf(0.00000049582, 3.22263554520, 10213.28554621100))

        private fun coefficients34() =
                arrayOf(doubleArrayOf(0.00000000573, 0.92229697820, 10213.28554621100))
    }
}
