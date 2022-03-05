# -*- coding: utf-8 -*- 

from subprocess import Popen, PIPE, STDOUT
import shutil, os, sys
import codecs

reload(sys)
sys.setdefaultencoding('utf-8')

summaryDir = "../Summarization/"

""" Summarization Module

Input: questionText, answerText, relatedDocumentTitles, relatedDocumentTexts

Output: [string, string, string]
"""



"""
PREPARE INPUT FILE FROM RETURNED RELATED DOCS

<-- FORMAT -->
Question: İlk para kullanımı hangi uygarlık döneminde olmuştur
Answer: lidyalılar
Doc1:0:<DOC>
<DOCNO></DOCNO>
<DOCTITLE>Lidyalılar</DOCTITLE>
<TEXT>
<------------>
"""
def preSummary(qText, aText, relatedTitles, relatedDocs, qID, en):

    # HARCODED VALUE : system must be on TOPDOCS=10 all the time
    # if len(relatedTitles) != 10 or len(relatedTitles) != len(relatedDocs):
    #     print("\nRelated Docs are less then expected, if not zero...\n")
    #     return False


    inputFileStr = "Question: " + qText + "\n" + "Answer: " + aText + "\n"

    for count in range(0,10):
        inputFileStr += "Doc" + str(count+1) + ":0:<DOC>\n<DOCNO></DOCNO>\n"

        if (count >= len(relatedTitles)):
            title = ""
        else:
            title = relatedTitles[count]


        if (count >= len(relatedDocs)):
            text = ""
        else:
            text = relatedDocs[count]

        inputFileStr += "<DOCTITLE>" + str(title) + "</DOCTITLE>\n"
        
        inputFileStr += "<TEXT>\n\n" + str(text) + "\n</TEXT>\n</DOC>\n\n"

    if en:
        fname = 'summaryInput' + str(qID) + '_en.txt'
    else:
        fname = 'summaryInput' + str(qID) + '.txt'

    with open(summaryDir + fname, 'w') as inputFile:
        inputFile.write(inputFileStr)

    return True


"""
RUN THE SUMMARIZER
"""
def runSummary(qText, howMany, qID, en):
    if en:
        fname = 'summaryInput' + str(qID) + '_en.txt'
    else:
        fname = 'summaryInput' + str(qID) + '.txt'

    cmd = Popen(['java Multisum ' + summaryDir + fname + ' ' + str(howMany) + " \"" + qText + "\""], stdout=PIPE, stderr=PIPE, shell=True)

    stdout, stderr = cmd.communicate()

    return parseSummaryOutput(stdout, stderr)


"""
PARSE THE SYSOUT PRINT
"""
# what to do when there is an error?? how to utilize stderr?
def parseSummaryOutput(stdout, stderr):

    outPrep = stdout.split('******')
    # print("OUTPREP 0 -> " + outPrep[0])
    # print("OUTPREP 1 -> " + outPrep[1])
    # print("OUTPREP 2 -> " + outPrep[2])

    if 'Exception' in stderr:
        print(stdout)
        print(stderr)
        print("Summary output is unusual, only printing the error for now..")
        #raise Exception('Summary output is unusual, check automatically generated summaryInput.txt')
        return ["<< ERROR >> \n\n--- STDOUT --- \n" + stdout + "--- STDERR --- \n" + stderr]
    else:
        print("Summary: everything seems to be normal")
        summaries = outPrep[2:len(outPrep)-1]
    
        return summaries


def summarize(qText, aText, relatedTitles, relatedDocs, howMany, qID, en):
    if preSummary(qText, aText, relatedTitles, relatedDocs, qID, en):
        return runSummary(qText, howMany, qID, en)
    else:
        return ["Check the number of related docs (it should be exactly 10)"]
