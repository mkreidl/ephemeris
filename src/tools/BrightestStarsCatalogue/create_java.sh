#!/bin/bash

IFS=$'\n'

cat catalog.dat | cut -b1-5,73-86,119-131,154-159,171-176 | sed -r -e "s/^\s*//g" -e 's/\s+/, /g' -e 's/^(\w*), (.*)/BSC_\1( \1, \2, "" );/g' > $1

rm $2 2> /dev/null

for line in $(cat names.dat)
do
	nr=${line%% *}
	name=${line##* }
	echo "s/^(BSC_${nr## }\(.*)\"\"(.*)\$/\1\"$name\"\2/g" >> $2
done

sed -r -i -f $2 $1

