'''
Created on 24.01.2017

@author: mkreidl
'''


CATALOG = r"/Users/martinkreidl/workspace/ephemeris/src/tools/BrightestStarsCatalogue_B50_1991/catalog"
NAMES = r"/Users/martinkreidl/workspace/ephemeris/src/tools/BrightestStarsCatalogue_B50_1991/names.dat"
IAU_NAMES = r"/Users/martinkreidl/workspace/ephemeris/src/tools/IAU_starnames_2017.txt"

INDEX_VARIABLE = "BRIGHT_STAR_NUMBER"
INDEX_LOOKUP_VARIABLE = "INDEX_LOOKUP"
SCI_NAME_VARIABLE = "FLAMSTEED_BAYER"
NAME_VARIABLE = "IAU_NAME"
POS_VARIABLE = "POS"
VEL_VARIABLE = "VEL"
PHASE_VARIABLE = "QP"
MAG_VARIABLE = "MAG"
PAR_VARIABLE = "PAR"

import math
import re


if __name__ == '__main__':
    with open("pos", "r") as positions:
        lines = list(positions)
    
    matcher = re.compile("\s*QP\[(\d*)\]")
    with open( "StarsCatalog.java", "r" ) as catalog_file, open("new.java", "w") as output:
        for line in catalog_file:
            m = re.match("\s*QP\[(\d*)\]", line)
            if m:
                index = int(m.group(1))
                output.write(lines[index])
            else:
                output.write(line)
                
    print(lines[0])
    print(lines[1])
