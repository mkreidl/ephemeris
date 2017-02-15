# ephemeris
Library for calculation of ephemerides of the Sun, the Moon and the planets

To calculate the positions of the eight planets, ephemeris implements the VSOP87 theory,
version C, in ecliptical cartesian coordinates of the date. The other theories are
planned to be added.
[Bretagnon P., Francou G. <cite>Variations Séculaires des Orbites Planétaires</cite>;
Astron. Astrophys. 202, 309 (1988);
<a href="http://cdsarc.u-strasbg.fr/viz-bin/ftp-index?VI/81">http://cdsarc.u-strasbg.fr/</a>]

For the calculation of the Moon's position, ephemeris implements a numerical solution
of the Kepler equation as well as a series of perturbation terms for the so obtained
coordinates, following
[Paul Schlyter, <cite>How to compute planetary positions</cite>; Stockholm, Sweden;
<a href="http://www.stjarnhimlen.se/comp/ppcomp.html">http://www.stjarnhimlen.se/comp/ppcomp.html</a>]
