#!/bin/bash


sed -E "s/PLANET/$2/g" Planet.java.template > $2.java


sed -E 's/^\s[0-9].{77}(.*)$/{\1},/g' $1 \
	| sed -E 's/([0-9])\s+/\1, /g' \
	| sed -E 's/\s+/ /g' \
	| sed -E 's/^\sVSOP87.*VARIABLE ([0-9]).*\*T\*\*([0-9]).*$/\t\t};\t}\tprivate static void initialize_\1_\2() {\n\t\tclassCoeff[\1][\2] = new double[][] {/g' \
	| sed -E 's/.*private static void initialize_1_0/\tprivate static void initialize_1_0/g' \
	| sed -E 's/\}(;?)(\s+)/}\1\n\2/g' \
	| sed -E 's/classCoeff\[1\]/classCoeff[0]/g' \
	| sed -E 's/classCoeff\[2\]/classCoeff[1]/g' \
	| sed -E 's/classCoeff\[3\]/classCoeff[2]/g' \
	| sed -E 's/^\{/\t\t\t{/g' \
	| sed -E 's/,(\s*)\}/\1}/g' >> $2.java


echo "		};
	}

};
" >> $2.java
