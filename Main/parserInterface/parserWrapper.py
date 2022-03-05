# -*- coding: utf-8 -*- 

import subprocess, shutil, os, sys
import codecs

#sys.path.append('../')
#sys.path.append('utils')

from sysUtil import *
from tokenizer import *

from question import Question

reload(sys)
sys.setdefaultencoding('utf-8')

parserDir = "../Parser/"

"""
PROVIDED FUNCTIONS

preParse
parserSubprocess
postParse

parse
"""


"""
PREPARE FOR PARSING

Grabs a question text and turns it into parser input format and flushes into a temp file.

Normal Text : A B C
Formatted   : ****** A \n\n B \n\n C \n\n. \n\n*******
File Name   : tmp.input

"""
def preParse(qText, debug=False, qID=6666):
    if debug:
        printMsg('Preparing for Parsing')

    delimiter = '******'
    parseReady = delimiter

    for token in tokenize(qText):
        parseReady += ' \n\n'
        parseReady += token

    parseReady += ' \n\n. \n\n' + delimiter

#print(parseReady)

    fname = 'tmp' + str(qID) + '.input'

    with open(parserDir+fname, 'w') as inputFile:
        inputFile.write(parseReady)

    if debug:
        printMsg('Done')
        printResult('Parse input is written to', 'tmp.input')

"""
PARSING

Actual parsing. 
Runs the parser (jar) with an external input file argument. 
Removes the input and temporary files (TODO: make this parametric)
"""
def parserSubprocess(debug=False, qID=6666):
    if debug:
        printMsg('Parsing Begins')

    fname = 'tmp' + str(qID) + '.input'

    subprocess.call('java -jar ' + parserDir + 'hazirCevapParser.jar ' + parserDir + fname, shell=True)

# now the output should be in the file : ./tmp.input.morphed2011.postagged.conll.parsed
    tmpFiles = [parserDir+fname+'.morphed2011', 
                parserDir+fname+'.morphed2011.postagged',
                parserDir+fname+'.morphed2011.postagged.conll']

    if debug:
        printResult('Deleting input and tmp files', str(tmpFiles))

# removing the temporary input file
    #subprocess.call('rm ' + parserDir + 'tmp.input', shell=True)

# removing temporary files
    #for tmp in tmpFiles:
        #subprocess.call('rm ' + tmp, shell=True)

    if debug:
        printMsg('Done Parsing')

"""
PREPARE THE question OBJECT

Reads the parser output and prepares the question object for hazircevap.
Removes the parser output file. (TODO: optional?)
"""
def postParse(qText, debug=False, qID=6666):
    if debug:
        printMsg('Preparing question object')

    fname = 'tmp' + str(qID) + '.input'

    parsedFile = codecs.open(parserDir + fname + '.morphed2011.postagged.conll.parsed', 'r', 'utf-8');
    parsedText = parsedFile.readlines();
    parsedText = [text.strip().split('\t') for text in parsedText];
    qParts = []
    for pText in parsedText:
        if len(pText)>1:
            qParts.append([t.replace('\ufeff', '') for t in pText]);

    subprocess.call('mv ' + parserDir + fname + '.morphed2011.postagged.conll.parsed ' + parserDir + 'question' + str(qID) + '.parsed', shell=True)

#print(parsedText)
#print('\n\n')
#print(qParts)

    qstn = Question(qText, qParts)

    if debug:
        printMsg('PARSE DONE PARSE DONE PARSE DONE PARSE DONE')
        printMsg('question Object (qstn) is READY!')

    return qstn

def parse(qText, debug=False, qID=6666):
    preParse(qText, debug, qID)
    parserSubprocess(debug, qID)
    return postParse(qText, debug, qID)
