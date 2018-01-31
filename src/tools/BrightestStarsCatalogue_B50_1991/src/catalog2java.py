'''
Created on 24.01.2017

@author: mkreidl
'''


CATALOG = r"/home/martin/Projekte/Ephemeris/src/tools/BrightestStarsCatalogue_B50_1991/catalog"
NAMES = r"/home/martin/Projekte/Ephemeris/src/tools/BrightestStarsCatalogue_B50_1991/names.dat"
IAU_NAMES = r"/home/martin/Projekte/Ephemeris/src/tools/IAU_starnames_2017.txt"

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

def lines_sorted_by_magnitude( catalog_iterable ):
    lines = []
    for line in catalog_iterable:
        try:
            mag = float( line[102:107] )
            lines.append( ( mag, line ) )
        except:
            # Novas and galaxies -- treat them elsewhere?
            mag = float( "nan" )

    lines.sort( key=lambda pair: pair[0] )
    return map( lambda pair: pair[1], lines )

def parse_names( names_file ):
    result = {}
    with open( names_file, "r" ) as names:
        for line in names:
            result[ int( line[0:4] ) ] = line[6:].strip()
    return result

def parse_IAU_names( iau_file ):
    result = {}
    with open( iau_file, "r" ) as iau:
        for line in iau:
            components = line.split()
            if components[1] == "HR":
                result[ int( components[2] )] = components[0].strip()
    return result

def parse( catalog_iterable, names_dict, java_output ):

    with open( java_output, "w" ) as java_file:

        java_file.write( "private static void initializeStars()\n{\n" )
        for count in range( 0, len( catalog_iterable ), 100 ):
            java_file.write( "initializeStars_{0:04d}_{1:04d}();\n".format( count, count + 99 ) )
#         java_file.write( "initializeNames();\n".format( count, count + 99 ) )
        java_file.write( "}\n" )

#         java_file.write( "\nprivate static void initializeNames()\n{\n" )
#         for index, name in names_dict.items():
#             java_file.write( "{0}[{1}] = \"{2}\";\n".format(
#                 NAME_VARIABLE, "{0}[ {1} ]".format( INDEX_LOOKUP_VARIABLE, index - 1 ), name ) )
#         java_file.write( "}\n" )

        count = -1

        for line in catalog_iterable:
            count += 1

            if count % 100 == 0:
                if count > 0:
                    java_file.write( "}\n" )
                java_file.write( "\nprivate static void initializeStars_{0:04d}_{1:04d}()\n{{\n".format( count, count + 99 ) )

            try:
                ra_j2000_h = float( line[75:77] )
                ra_j2000_m = float( line[77:79] )
                ra_j2000_s = float( line[79:83] )
                de_j2000_sign = -1 if line[83] == "-" else +1
                de_j2000_d = float( line[84:86] )
                de_j2000_m = float( line[86:88] )
                de_j2000_s = float( line[88:90] )
            except:
                continue
            try:
                r_v_kmsec = "{0:.2e}f".format( float( line[166:170] ) )
            except:
                r_v_kmsec = "Float.NaN"
            try:
                ra_v_arcsec = float( line[148:154] )
                ra_v_rad = "{0:.2e}f".format( ra_v_arcsec / 3600 * math.pi / 180 )
            except:
                ra_v_rad = "Float.NaN"
            try:
                de_v_arcsec = float( line[154:160] )
                de_v_rad = "{0:.2e}f".format( de_v_arcsec / 3600 * math.pi / 180 )
            except:
                de_v_rad = "Float.NaN"

            index = int( line[0:4] )
            name = re.sub( r" +", " ", line[4:14].strip() )
            mag = float( line[102:107] )
            spectral_type = line[129]

            try:
                par = float( line[161:166] )
                dist = "{0:.3e}f".format( 3.26 / par )
            except:
                par = "Float.NaN"
                dist = "Float.NaN"

            ra_j2000_rad = "{0:.2e}f".format( ( ra_j2000_h + ra_j2000_m / 60 + ra_j2000_s / 3600 ) * math.pi / 12 )
            de_j2000_rad = "{0:.2e}f".format( de_j2000_sign * ( de_j2000_d + de_j2000_m / 60 + de_j2000_s / 3600 ) * math.pi / 180 )

            #===============================================================================
            # Note on pmRA:
            #      As usually assumed, the proper motion in RA is the projected
            #      motion (cos(DE).d(RA)/dt), i.e. the total proper motion is
            #      sqrt(pmRA^2^+pmDE^2^)
            #===============================================================================
            #ra_v_arcsec /= math.cos( de_j2000_rad )


            java_file.write( "{0}[{1}] = {2};\n".format( INDEX_VARIABLE, count, index ) )
            # java_file.write( "{0}[{1}] = {2};\n".format( INDEX_LOOKUP_VARIABLE, index-1, count ) )
            if name:
                java_file.write( "{0}[{1}] = \"{2}\";\n".format( SCI_NAME_VARIABLE, count, name ) )
            if index in names_dict:
                java_file.write( "{0}[{1}] = \"{2}\";\n".format( NAME_VARIABLE, count, names_dict[ index ] ) )
            java_file.write( "{0}[{1}] = {2}{3};\n".format( MAG_VARIABLE, count, mag, "f" if type( mag ) is float else "" ) )
            java_file.write( "{0}[{1}] = {2}{3};\n".format( PAR_VARIABLE, count, par, "f" if type( par ) is float else "" ) )
            
            java_file.write( "{0}[{1}] = new float[]{{{dst}, {ra}, {de}, {vdst}, {vra}, {vde}}};\n".format(
                PHASE_VARIABLE, count, dst=dist, ra=ra_j2000_rad, de=de_j2000_rad, vdst=r_v_kmsec, vra=ra_v_rad, vde=de_v_rad
            ) )
            java_file.write( "{0}[{1}] = '{2}';\n".format( "SPECTRAL_TYPE", count, spectral_type ) )

            #java_file.write( "{0}[{1}].lon = {2};\n".format( POS_VARIABLE, count, ra_j2000_rad ) )
            #java_file.write( "{0}[{1}].lat = {2};\n".format( POS_VARIABLE, count, de_j2000_rad ) )
            #java_file.write( "{0}[{1}].dst = {2};\n".format( POS_VARIABLE, count, dist ) )

            #java_file.write( "{0}[{1}].lon = {2};\n".format( VEL_VARIABLE, count, ra_v_rad ) )
            #java_file.write( "{0}[{1}].lat = {2};\n".format( VEL_VARIABLE, count, de_v_rad ) )
            #java_file.write( "{0}[{1}].dst = {2};\n".format( VEL_VARIABLE, count, r_v_kmsec ) )
			
            #java_file.write( "{0}[{1}].dst = {2};\n".format( VEL_VARIABLE, count, r_v_kmsec ) )

#             if index in [ 2061, 5340]:
#                 print name
#                 print dist
#                 print ra_j2000_rad * 12 / math.pi
#                 print de_j2000_rad * 180 / math.pi
#                 print ra_j2000_rad * 12 / math.pi
#                 print de_j2000_rad * 180 / math.pi
#                 print ra_v_arcsec
#                 print de_v_arcsec

        java_file.write( "}" )

if __name__ == '__main__':
    with open( CATALOG, "r" ) as catalog_file:
        lines = lines_sorted_by_magnitude( catalog_file )
    names_dict = parse_names( NAMES )
    iau_names = parse_IAU_names( IAU_NAMES )
    parse( lines, iau_names, "catalog.java" )
