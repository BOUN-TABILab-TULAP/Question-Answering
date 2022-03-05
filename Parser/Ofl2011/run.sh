#!/bin/bash

dir=.

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

$DIR/lookup $DIR/tfeaturesulx.fst < $1 > $2