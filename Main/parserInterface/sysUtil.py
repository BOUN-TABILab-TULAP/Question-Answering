import sys
reload(sys)
sys.setdefaultencoding('utf-8')

msgDelim = "--------------------"
def printMsg(text):
    print('\n' + msgDelim + ' ' + text + ' ' + msgDelim + '\n')

def printResult(text, result):
    print('\n' + msgDelim + ' ' + text + ' : \n\n' + result + '\n' + msgDelim + '\n')
