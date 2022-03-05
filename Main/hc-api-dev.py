#!flask/bin/python
from flask import Flask, request, jsonify, make_response, abort
import cPickle as pickle

from main import *

import sys
reload(sys)
sys.setdefaultencoding('utf-8')

app = Flask(__name__)

#app.run(threaded=True)

questions = [
	{
		'qId': 1,
		'qText': 'Dummy Question',
		'qParts': [],
		'qFocus': [],
		'qFocRoots': [],
		'qMod': [],
		'qClass': [],
		'qPnoun': [],
		'qSubj': [],
		'qTransPhrase': [],
		'qTranslation': [],
		'qQuery': [],
		'qRelated_doc_ids': [],
		'qRelated_doc_titles': [],
		'qRelated_doc_texts': [],
		'done': False
		}
	]

question = []

@app.route('/hc-api/v0.1/dev', methods=['GET'])
def get_questions():
	return jsonify({'questions':questions})


@app.route('/hc-api/v0.1/devMass', methods=['POST'])
def mass_evaluate():
	print("HC_API: Running Mass Evaluator")
	if not request.json or not 'evalDataFile' in request.json:
		abort(400)

	dataPath = request.json['evalDataFile']
	topDocsNum = int(request.json['topDocs']) # we don't use it in mass, right?

	# READ DATA FILE (to fill these lists:)

	questionList, focusListAnnotatedT, modListAnnotatedT, classListAnnotated, parsedBefore, answerList = mainReadDataFile(dataPath)

	# RUN the SYSTEM on EVERY QUESTION (to fill these lists:)

	#total = len(questionList) # just for debugging
	# ['dummy']*total

	if topDocsNum == 1:
		bypassDocs=False
	else:
		bypassDocs=True

	focusListFoundT, modListFoundT, classListFound, transPhraseList, transList, relatedDocs, answerFoundInDocs, queries = mainEval(questionList, answerList, parsedBefore, dataPath, topDocs=topDocsNum, bypassDocs=bypassDocs)
	# format of relatedDocs -> [[titles, texts], [titles, texts], ...]

	print("HC_API: Compiling response ...")
	responseData = {
		'questionTexts': questionList,
		'answerList': answerList,
		'focusListAnnoT': focusListAnnotatedT,
		'focusListFoundT': focusListFoundT,
		'modListAnnoT': modListAnnotatedT,
		'modListFoundT': modListFoundT,
		'classListAnno': classListAnnotated,
		'classListFound': classListFound,
		'transPhraseList': transPhraseList,
		'transList': transList,
		'relatedDocs': relatedDocs,
		'answerFoundDocs': answerFoundInDocs,
		'queries': queries
			}

	print("HC_API: Posting response...")
	return jsonify({'responseData':responseData}), 201
	

@app.route('/hc-api/v0.1/dev', methods=['POST'])
def post_question():
	print("HC_API: Running Single")
	if not request.json or not 'questionText' in request.json:
		abort(400)

	print("\n\n\n=================\n\n")
	#print(request.json['topDocs'])
	print("\n\n\n=================\n\n")

	qText = request.json['questionText']

	topDocs = int(request.json['topDocs'])

	qObj = mainParse(qText)

	qFoc, qFocRoots, qMod, qClass, qPnoun, qSubj = mainAnalyze(qObj, False, False)

	qTerms = mainBuildQuery(qObj)

	docs = mainQuerySingle(count=topDocs)

	titles, texts = mainRelated(docs)

	transPhrase = " ".join([ qPnoun, qMod + qFoc ])
	translation = mainTranslate(transPhrase)


	print("HC_API: Compiling response...")
	question = {
		'qId': questions[-1]['qId'] + 1,
		'qText': qObj.questionText,
		'qParts': qObj.questionParts,
		'qFocus': qFoc,
		'qFocRoots': qFocRoots,
		'qMod': qMod,
		'qClass': qClass,
		'qPnoun': qPnoun,
		'qSubj': qSubj,
		'qTransPhrase': transPhrase,
		'qTranslation': translation,
		'qQuery': str(qTerms),
		'qRelated_doc_ids': docs,
		'qRelated_doc_titles': titles,
		'qRelated_doc_texts': texts,
		'done': True
		}

	questions.append(question)

	print("HC_API: Posting response...")
	return jsonify({'question':question}), 201

"""
@app.route('/')
def index():
        return "Hello, World!"
"""

@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)

if __name__ == '__main__':
        app.run(port=8080,debug=True)
