from subprocess import Popen, PIPE, STDOUT
import sys
indriDir = "../IR/indri-5.0/"
dumpindex = indriDir + 'dumpindex/dumpindex'

turkceIndexDir = indriDir + 'vikiEBAindex/' # '../IR/indri-5.0/vikiEBAindex/'
ingilizceIndexDir = '../IR/indri-5.0/wikipediaIndex/'
def getDoc(docID,lang="tr"):
    indexDir = turkceIndexDir
    if lang == "en":
        indexDir = ingilizceIndexDir
    
    dump = Popen([dumpindex + ' ' + indexDir + ' dt ' + str(docID)], stdout=PIPE, stderr=PIPE, shell=True)

    #print(dumpindex + ' ' + indexDir + ' dt ' + str(docID))

    stdout, stderr = dump.communicate()
    if stderr:
        sys.stderr.write("[Doc id:%s] %s\n" %(docID,stderr))
        return None,None
    """
    <DOC>
    <DOCNO>26378</DOCNO>
    <TEXT>
    Messina ili

    .... Document Text ....

    </TEXT></DOC>
    """

    docSplit = stdout.strip('\n').split('\n')

    if len(docSplit) > 2:
        docTitle = docSplit[3]

        docText = "\n".join(docSplit[5:len(docSplit)-1])
    else:
        docTitle = docSplit
        docText = docSplit

    return docTitle, docText
